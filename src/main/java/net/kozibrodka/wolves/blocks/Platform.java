package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.entity.LiftedBlockEntity;
import net.kozibrodka.wolves.entity.MovingAnchorEntity;
import net.kozibrodka.wolves.entity.MovingPlatformEntity;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class Platform extends TemplateBlock
    implements RotatableBlock, BlockWithWorldRenderer, BlockWithInventoryRenderer
{

    public Platform(Identifier iid)
    {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSounds(WOOD_SOUNDS);
        bPlatformAlreadyConsideredForEntityConversion = new boolean[5][5][5];
        bPlatformAlreadyConsideredForConnectedTest = new boolean[5][5][5];
        ResetPlatformConsideredForEntityConversionArray();
        ResetPlatformConsideredForConnectedTestArray();
    }

    public int getTextureForSide(int iSide)
    {
        if(iSide == 1)
        {
            return TextureListener.platform_top;
        }
        return iSide != 0 ? TextureListener.platform_side : TextureListener.platform_top;
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return true;
    } //ZMIANA KODU

    public boolean canSuffocate(Level world, int i, int j, int l)
    {
        return true;
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int l)
    {
        return 0;
    }

    public void SetFacing(Level world1, int l, int i1, int j1, int k1)
    {
    }

    public boolean CanRotate(BlockView iBlockAccess, int i, int j, int l)
    {
        return false;
    }

    public boolean CanTransmitRotation(BlockView iBlockAccess, int i, int j, int l)
    {
        return true;
    }

    public void Rotate(Level world1, int l, int i1, int j1, boolean flag)
    {
    }

    private void ConvertToEntity(Level world, int i, int j, int k, MovingAnchorEntity associatedAnchorEntity)
    {
        MovingPlatformEntity entityPlatform = new MovingPlatformEntity(world, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, associatedAnchorEntity);
        world.spawnEntity(entityPlatform);
        AttemptToLiftBlockWithPlatform(world, i, j + 1, k);
        world.setTile(i, j, k, 0);
    }

    private void AttemptToLiftBlockWithPlatform(Level world, int i, int j, int k)
    {
        if(LiftedBlockEntity.CanBlockBeConvertedToEntity(world, i, j, k))
        {
            new LiftedBlockEntity(world, i, j, k);
        }
    }

    private int GetDistToClosestConnectedAnchorPoint(Level world, int i, int j, int k)
    {
        int iClosestDist = -1;
        for(int tempI = i - 2; tempI <= i + 2; tempI++)
        {
            for(int tempJ = j - 2; tempJ <= j + 2; tempJ++)
            {
                for(int tempK = k - 2; tempK <= k + 2; tempK++)
                {
                    int iTempid = world.getTileId(tempI, tempJ, tempK);
                    if(iTempid != id)
                    {
                        continue;
                    }
                    int iUpwardsid = world.getTileId(tempI, tempJ + 1, tempK);
                    if(iUpwardsid != BlockListener.anchor.id || ((Anchor)BlockListener.anchor).GetAnchorFacing(world, tempI, tempJ + 1, tempK) != 1 || !IsPlatformConnectedToAnchorPoint(world, i, j, k, tempI, tempJ, tempK))
                    {
                        continue;
                    }
                    int iTempDist = Math.abs(tempI - i) + Math.abs(tempJ - j) + Math.abs(tempK - k);
                    if(iClosestDist == -1 || iTempDist < iClosestDist)
                    {
                        iClosestDist = iTempDist;
                    }
                }
            }
        }
        return iClosestDist;
    }

    private boolean IsPlatformConnectedToAnchorPoint(Level world, int platformI, int platformJ, int platformK, int anchorPointI, int anchorPointJ, int anchorPointK)
    {
        ResetPlatformConsideredForConnectedTestArray();
        if(platformI == anchorPointI && platformJ == anchorPointJ && platformK == anchorPointK)
        {
            return true;
        } else
        {
            return PropogateTestForConnected(world, anchorPointI, anchorPointJ, anchorPointK, anchorPointI, anchorPointJ, anchorPointK, platformI, platformJ, platformK);
        }
    }

    private boolean PropogateTestForConnected(Level world, int i, int j, int k, int sourceI, int sourceJ, int sourceK, 
            int targetI, int targetJ, int targetK)
    {
        int iDeltaI = i - sourceI;
        int iDeltaJ = j - sourceJ;
        int iDeltaK = k - sourceK;
        if(bPlatformAlreadyConsideredForConnectedTest[iDeltaI + 2][iDeltaJ + 2][iDeltaK + 2])
        {
            return false;
        }
        bPlatformAlreadyConsideredForConnectedTest[iDeltaI + 2][iDeltaJ + 2][iDeltaK + 2] = true;
        for(int iFacing = 0; iFacing < 6; iFacing++)
        {
            BlockPosition tempPos = new BlockPosition(i, j, k);
            tempPos.AddFacingAsOffset(iFacing);
            if(tempPos.i == targetI && tempPos.j == targetJ && tempPos.k == targetK)
            {
                return true;
            }
            int iTempid = world.getTileId(tempPos.i, tempPos.j, tempPos.k);
            if(iTempid != id)
            {
                continue;
            }
            int tempDistI = Math.abs(sourceI - tempPos.i);
            int tempDistJ = Math.abs(sourceJ - tempPos.j);
            int tempDistK = Math.abs(sourceK - tempPos.k);
            if(tempDistI <= 2 && tempDistJ <= 2 && tempDistK <= 2 && PropogateTestForConnected(world, tempPos.i, tempPos.j, tempPos.k, sourceI, sourceJ, sourceK, targetI, targetJ, targetK))
            {
                return true;
            }
        }
        return false;
    }

    void ResetPlatformConsideredForEntityConversionArray()
    {
        for(int tempI = 0; tempI < 5; tempI++)
        {
            for(int tempJ = 0; tempJ < 5; tempJ++)
            {
                for(int tempK = 0; tempK < 5; tempK++)
                {
                    bPlatformAlreadyConsideredForEntityConversion[tempI][tempJ][tempK] = false;
                }

            }

        }

    }

    void ResetPlatformConsideredForConnectedTestArray()
    {
        for(int tempI = 0; tempI < 5; tempI++)
        {
            for(int tempJ = 0; tempJ < 5; tempJ++)
            {
                for(int tempK = 0; tempK < 5; tempK++)
                {
                    bPlatformAlreadyConsideredForConnectedTest[tempI][tempJ][tempK] = false;
                }
            }
        }
    }

    public void CovertToEntitiesFromThisPlatform(Level world, int i, int j, int k, MovingAnchorEntity associatedAnchorEntity)
    {
        ResetPlatformConsideredForEntityConversionArray();
        PropogateCovertToEntity(world, i, j, k, associatedAnchorEntity, i, j, k);
    }

    private void PropogateCovertToEntity(Level world, int i, int j, int k, MovingAnchorEntity associatedAnchorEntity, int sourceI, int sourceJ,
                                         int sourceK)
    {
        int iDeltaI = i - sourceI;
        int iDeltaJ = j - sourceJ;
        int iDeltaK = k - sourceK;
        if(bPlatformAlreadyConsideredForEntityConversion[iDeltaI + 2][iDeltaJ + 2][iDeltaK + 2])
        {
            return;
        }
        bPlatformAlreadyConsideredForEntityConversion[iDeltaI + 2][iDeltaJ + 2][iDeltaK + 2] = true;
        int distToSource = Math.abs(iDeltaI) + Math.abs(iDeltaJ) + Math.abs(iDeltaK);
        int closestAnchorDist = GetDistToClosestConnectedAnchorPoint(world, i, j, k);
        if(closestAnchorDist == -1 || distToSource <= closestAnchorDist)
        {
            ConvertToEntity(world, i, j, k, associatedAnchorEntity);
        } else
        {
            return;
        }
        for(int iFacing = 0; iFacing < 6; iFacing++)
        {
            BlockPosition tempPos = new BlockPosition(i, j, k);
            tempPos.AddFacingAsOffset(iFacing);
            int iTempid = world.getTileId(tempPos.i, tempPos.j, tempPos.k);
            if(iTempid != id)
            {
                continue;
            }
            int tempDistI = Math.abs(sourceI - tempPos.i);
            int tempDistJ = Math.abs(sourceJ - tempPos.j);
            int tempDistK = Math.abs(sourceK - tempPos.k);
            if(tempDistI <= 2 && tempDistJ <= 2 && tempDistK <= 2)
            {
                PropogateCovertToEntity(world, tempPos.i, tempPos.j, tempPos.k, associatedAnchorEntity, sourceI, sourceJ, sourceK);
            }
        }

    }

    private final int iPlatformTopTextureIndex = 72;
    private final int iPlatformSideTextureIndex = 73;
    private final int iPlatformBottomTextureIndex = 74;
    private boolean bPlatformAlreadyConsideredForEntityConversion[][][];
    private boolean bPlatformAlreadyConsideredForConnectedTest[][][];

    @Override
    public boolean renderWorld(BlockRenderer tileRenderer, BlockView tileView, int x, int y, int z) {
        if(tileView.getTileId(x - 1, y, z) != id)
        {
            this.setBoundingBox(0.0001F, 0.0625F, 0.0001F, 0.0625F, 0.9375F, 0.9999F);
            tileRenderer.renderStandardBlock(this, x, y, z);
        }
        if(tileView.getTileId(x, y, z + 1) != id)
        {
            this.setBoundingBox(0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F, 1.0F);
            tileRenderer.renderStandardBlock(this, x, y, z);
        }
        if(tileView.getTileId(x + 1, y, z) != id)
        {
            this.setBoundingBox(0.9375F, 0.0625F, 0.0001F, 0.9999F, 0.9375F, 0.9999F);
            tileRenderer.renderStandardBlock(this, x, y, z);
        }
        if(tileView.getTileId(x, y, z - 1) != id)
        {
            this.setBoundingBox(0.0F, 0.0625F, 0.0F, 1.0F, 0.9375F, 0.0625F);
            tileRenderer.renderStandardBlock(this, x, y, z);
        }
        this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.0F, 0.9375F, 0.0F, 1.0F, 1.0F, 1.0F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }

    @Override
    public void renderInventory(BlockRenderer tileRenderer, int meta) {
        this.setBoundingBox(1E-005F, 1E-005F, 1E-005F, 0.0625F, 0.99999F, 0.99999F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.platform_side);
        this.setBoundingBox(0.0F, 0.0F, 0.9375F, 1.0F, 1.0F, 1.0F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.platform_side);
        this.setBoundingBox(0.9375F, 1E-005F, 1E-005F, 0.99999F, 0.99999F, 0.99999F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.platform_side);
        this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0625F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.platform_side);
        this.setBoundingBox(0.0001F, 0.001F, 0.0001F, 0.9999F, 0.0625F, 0.9999F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.platform_top);
        this.setBoundingBox(0.0001F, 0.9375F, 0.0001F, 0.9999F, 0.999F, 0.9999F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.platform_top);
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
