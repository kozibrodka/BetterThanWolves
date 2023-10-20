package net.kozibrodka.wolves.blocks;


import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.itemblocks.UnfiredPotteryItemBlock;
import net.kozibrodka.wolves.tileentity.UnfiredPotteryTileEntity;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.block.HasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;

@HasCustomBlockItemFactory(UnfiredPotteryItemBlock.class)
public class UnfiredPottery extends TemplateBlockWithEntity
   implements RotatableBlock, BlockWithWorldRenderer, BlockWithInventoryRenderer
{

    public UnfiredPottery(Identifier iid)
    {
        super(iid, Material.CLAY);
        setHardness(0.6F);
        setSounds(GRAVEL_SOUNDS);
    }

    public int getTextureForSide(int iSide)
    {
        return TextureListener.unfiredpottery;
    }

    protected int droppedMeta(int iMetaData)
    {
        return iMetaData;
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public Box getCollisionShape(Level world, int i, int j, int k)
    {
        int iMetaData = world.getTileMeta(i, j, k);
        switch(iMetaData)
        {
            case 0: // '\0'
            case 1: // '\001'
                return Box.createButWasteMemory((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);

            case 2: // '\002'
                return Box.createButWasteMemory((float)i + 0.1875F, (float)j, (float)k + 0.1875F, (float)i + 0.8125F, (float)j + 1.0F, (float)k + 0.8125F);
        }
        return Box.createButWasteMemory((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);
    }

    public void updateBoundingBox(BlockView iBlockAccess, int i, int j, int k)
    {
        int iMetaData = iBlockAccess.getTileMeta(i, j, k);
        switch(iMetaData)
        {
            case 0: // '\0'
            case 1: // '\001'
                setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                break;

            case 2: // '\002'
                setBoundingBox(0.1875F, 0.0F, 0.1875F, 0.8125F, 1.0F, 0.8125F);
                break;

            default:
                setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                break;
        }
    }

    protected TileEntityBase createTileEntity()
    {
        return new UnfiredPotteryTileEntity();
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
        return false;
    }

    public void Rotate(Level world1, int l, int i1, int j1, boolean flag)
    {
    }

    public void Cook(Level world, int i, int j, int k)
    {
        int iMetaData = world.getTileMeta(i, j, k);
        int iNewid = 0;
        switch(iMetaData)
        {
            case 0: // '\0'
                iNewid = mod_FCBetterThanWolves.fcCrucible.id;
                break;

            case 1: // '\001'
                iNewid = mod_FCBetterThanWolves.fcPlanter.id;
                break;

            case 2: // '\002'
                iNewid = mod_FCBetterThanWolves.fcVase.id;
                break;
        }
        world.setTile(i, j, k, 0);
        if(iNewid > 0)
        {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, iNewid, 0);
        }
    }

    public static final float m_fUnfiredPotteryCrucibleHeight = 1F;
    public static final float m_fUnfiredPotteryCrucibleWidth = 0.875F;
    public static final float m_fUnfiredPotteryCrucibleHalfWidth = 0.4375F;
    public static final float m_fUnfiredPotteryCrucibleBandHeight = 0.75F;
    public static final float m_fUnfiredPotteryCrucibleBandHalfHeight = 0.375F;
    public static final float m_fUnfiredPotteryPotWidth = 0.75F;
    public static final float m_fUnfiredPotteryPotHalfWidth = 0.375F;
    public static final float m_fUnfiredPotteryPotBandHeight = 0.3125F;
    public static final float m_fUnfiredPotteryPotBandHalfHeight = 0.15625F;
    public static final float m_fUnfiredPotteryVaseBaseWidth = 0.5F;
    public static final float m_fUnfiredPotteryVaseBaseHalfWidth = 0.25F;
    public static final float m_fUnfiredPotteryVaseBaseHeight = 0.0625F;
    public static final float m_fUnfiredPotteryVaseBodyWidth = 0.625F;
    public static final float m_fUnfiredPotteryVaseBodyHalfWidth = 0.3125F;
    public static final float m_fUnfiredPotteryVaseBodyHeight = 0.375F;
    public static final float m_fUnfiredPotteryVaseNeckBaseWidth = 0.5F;
    public static final float m_fUnfiredPotteryVaseNeckBaseHalfWidth = 0.25F;
    public static final float m_fUnfiredPotteryVaseNeckBaseHeight = 0.0625F;
    public static final float m_fUnfiredPotteryVaseNeckWidth = 0.25F;
    public static final float m_fUnfiredPotteryVaseNeckHalfWidth = 0.125F;
    public static final float m_fUnfiredPotteryVaseNeckHeight = 0.4375F;
    public static final float m_fUnfiredPotteryVaseTopWidth = 0.375F;
    public static final float m_fUnfiredPotteryVaseTopHalfWidth = 0.1875F;
    public static final float m_fUnfiredPotteryVaseTopHeight = 0.0625F;
    private final int iUnfiredPotteryCookingTexture = 76;

    @Override
    public boolean renderWorld(BlockRenderer tileRenderer, BlockView tileView, int x, int y, int z) {
        updateBoundingBox(tileView, x, y, z);
        UnfiredPotteryTileEntity fctileentityunfiredpottery = (UnfiredPotteryTileEntity)tileView.getTileEntity(x, y, z);
        int l = TextureListener.unfiredpottery;
        if(fctileentityunfiredpottery != null && fctileentityunfiredpottery.IsCooking())
        {
            l = TextureListener.unfiredpottery_cook;
        }
        int i1 = tileView.getTileMeta(x, y, z);
        switch(i1)
        {
            case 0: // '\0'
                RenderUnfiredCrucible(tileRenderer, tileView, x, y, z, l);
                break;

            case 1: // '\001'
                RenderUnfiredPot(tileRenderer, tileView, x, y, z, l);
                break;

            case 2: // '\002'
                RenderUnfiredVase(tileRenderer, tileView, x, y, z, l);
                break;

            default:
                tileRenderer.renderStandardBlock(this, x, y, z);
                break;
        }
        return true;
    }

    public boolean RenderUnfiredCrucible(BlockRenderer renderblocks, BlockView iblockaccess, int i, int j, int k, int l)
    {
        this.setBoundingBox(0.0625F, 0.0F, 0.0625F, 0.1875F, 1.0F, 0.8125F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.0625F, 0.0F, 0.8125F, 0.8125F, 1.0F, 0.9375F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.8125F, 0.0F, 0.1875F, 0.9375F, 1.0F, 0.9375F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.1875F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.1875F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.1875F, 0.0F, 0.1875F, 0.8125F, 0.125F, 0.8125F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.0F, 0.125F, 0.0F, 0.125F, 0.875F, 0.875F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.0F, 0.125F, 0.875F, 0.875F, 0.875F, 1.0F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.875F, 0.125F, 0.125F, 1.0F, 0.875F, 1.0F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.125F, 0.125F, 0.0F, 1.0F, 0.875F, 0.125F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        updateBoundingBox(iblockaccess, i, j, k);
        return true;
    }

    public boolean RenderUnfiredPot(BlockRenderer renderblocks, BlockView iblockaccess, int i, int j, int k, int l)
    {
        this.setBoundingBox(0.125F, 0.0F, 0.125F, 0.25F, 0.6875F, 0.75F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.125F, 0.0F, 0.75F, 0.75F, 0.6875F, 0.875F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.75F, 0.0F, 0.25F, 0.875F, 0.6875F, 0.875F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.25F, 0.0F, 0.125F, 0.875F, 0.6875F, 0.25F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.25F, 0.0F, 0.25F, 0.75F, 0.125F, 0.75F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.0F, 0.6875F, 0.0F, 0.125F, 1.0F, 0.875F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.0F, 0.6875F, 0.875F, 0.875F, 1.0F, 1.0F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.875F, 0.6875F, 0.125F, 1.0F, 1.0F, 1.0F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.125F, 0.6875F, 0.0F, 1.0F, 1.0F, 0.125F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        updateBoundingBox(iblockaccess, i, j, k);
        return true;
    }

    public boolean RenderUnfiredVase(BlockRenderer renderblocks, BlockView iblockaccess, int i, int j, int k, int l)
    {
        this.setBoundingBox(0.25F, 0.0F, 0.25F, 0.75F, 0.0625F, 0.75F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.1875F, 0.0625F, 0.1875F, 0.8125F, 0.4375F, 0.8125F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.25F, 0.4375F, 0.25F, 0.75F, 0.5F, 0.75F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.375F, 0.5F, 0.375F, 0.625F, 0.9375F, 0.625F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.3125F, 0.9375F, 0.3125F, 0.6875F, 1.0F, 0.6875F);
        CustomBlockRendering.RenderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        updateBoundingBox(iblockaccess, i, j, k);
        return true;
    }

    @Override
    public void renderInventory(BlockRenderer tileRenderer, int meta) {
        this.method_1605();
        switch(meta)
        {
            case 0: // '\0'
                RenderUnfiredCrucibleInvBlock(tileRenderer, this, meta);
                break;

            case 1: // '\001'
                RenderUnfiredPotInvBlock(tileRenderer, this, meta);
                break;

            case 2: // '\002'
                RenderUnfiredVaseInvBlock(tileRenderer, this, meta);
                break;

            default:
                CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
                break;
        }
    }

    public void RenderUnfiredCrucibleInvBlock(BlockRenderer renderblocks, BlockBase block, int i)
    {
        this.setBoundingBox(0.0625F, 0.0F, 0.0625F, 0.1875F, 1.0F, 0.8125F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.setBoundingBox(0.0625F, 0.0F, 0.8125F, 0.8125F, 1.0F, 0.9375F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.setBoundingBox(0.8125F, 0.0F, 0.1875F, 0.9375F, 1.0F, 0.9375F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.setBoundingBox(0.1875F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.1875F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.setBoundingBox(0.1875F, 0.0F, 0.1875F, 0.8125F, 0.125F, 0.8125F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.setBoundingBox(0.0F, 0.125F, 0.0F, 0.125F, 0.875F, 0.875F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.setBoundingBox(0.0F, 0.125F, 0.875F, 0.875F, 0.875F, 1.0F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.setBoundingBox(0.875F, 0.125F, 0.125F, 1.0F, 0.875F, 1.0F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.setBoundingBox(0.125F, 0.125F, 0.0F, 1.0F, 0.875F, 0.125F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.method_1605();
    }

    public void RenderUnfiredPotInvBlock(BlockRenderer renderblocks, BlockBase block, int i)
    {
        this.setBoundingBox(0.125F, 0.0F, 0.125F, 0.25F, 0.6875F, 0.75F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.setBoundingBox(0.125F, 0.0F, 0.75F, 0.75F, 0.6875F, 0.875F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.setBoundingBox(0.75F, 0.0F, 0.25F, 0.875F, 0.6875F, 0.875F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.setBoundingBox(0.25F, 0.0F, 0.125F, 0.875F, 0.6875F, 0.25F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.setBoundingBox(0.25F, 0.0F, 0.25F, 0.75F, 0.125F, 0.75F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.setBoundingBox(0.0F, 0.6875F, 0.0F, 0.125F, 1.0F, 0.875F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.setBoundingBox(0.0F, 0.6875F, 0.875F, 0.875F, 1.0F, 1.0F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.setBoundingBox(0.875F, 0.6875F, 0.125F, 1.0F, 1.0F, 1.0F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.setBoundingBox(0.125F, 0.6875F, 0.0F, 1.0F, 1.0F, 0.125F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.method_1605();
    }

    public void RenderUnfiredVaseInvBlock(BlockRenderer renderblocks, BlockBase block, int i)
    {
        this.setBoundingBox(0.25F, 0.0F, 0.25F, 0.75F, 0.0625F, 0.75F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.setBoundingBox(0.1875F, 0.0625F, 0.1875F, 0.8125F, 0.4375F, 0.8125F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.setBoundingBox(0.25F, 0.4375F, 0.25F, 0.75F, 0.5F, 0.75F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.setBoundingBox(0.375F, 0.5F, 0.375F, 0.625F, 0.9375F, 0.625F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.setBoundingBox(0.3125F, 0.9375F, 0.3125F, 0.6875F, 1.0F, 0.6875F);
        CustomBlockRendering.RenderInvBlockWithTexture(renderblocks, this, -0.5F, -0.5F, -0.5F, TextureListener.unfiredpottery);
        this.method_1605();
    }
}
