package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.itemblocks.PlanterItemBlock;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.SoilTemplate;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.HasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.util.Random;
@HasCustomBlockItemFactory(PlanterItemBlock.class)
public class Planter extends TemplateBlockBase
    implements RotatableBlock, SoilTemplate, BlockWithWorldRenderer, BlockWithInventoryRenderer
{

    public Planter(Identifier iid)
    {
        super(iid, Material.GLASS);
        setHardness(0.6F);
        setSounds(GLASS_SOUNDS);
        setTicksRandomly(true);
    }

    public int getTextureForSide(int i)
    {
        return TextureListener.planter;
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    protected int droppedMeta(int iMetaData)
    {
        return iMetaData & 1;
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
        int iOldGrowthState = GetGrowthState(world, i, j, k);
        int iNewGrowthState = 0;
        if(world.isAir(i, j + 1, k) && world.placeTile(i, j + 1, k) >= 8)
        {
            iNewGrowthState = iOldGrowthState;
            if(random.nextInt(50) == 0 && ++iNewGrowthState > 1)
            {
                iNewGrowthState = 0;
                if(random.nextInt(2) == 0)
                {
                    world.setTile(i, j + 1, k, BlockBase.ROSE.id);
                } else
                {
                    world.setTile(i, j + 1, k, BlockBase.DANDELION.id);
                }
            }
        }
        if(iNewGrowthState != iOldGrowthState)
        {
            SetGrowthState(world, i, j, k, iNewGrowthState);
        }
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

    public boolean CanPlantGrowOnBlock(Level world, int i, int j, int k, BlockBase plantBlock)
    {
        return DoesPlanterContainSoil(world, i, j, k);
    }

    public boolean IsBlockHydrated(Level world, int i, int j, int k)
    {
        return DoesPlanterContainSoil(world, i, j, k);
    }

    public boolean IsBlockConsideredNeighbouringWater(Level world, int i, int j, int k)
    {
        return DoesPlanterContainSoil(world, i, j, k);
    }

    public boolean DoesPlanterContainSoil(BlockView iBlockAccess, int i, int j, int k)
    {
        int iMetaData = iBlockAccess.getTileMeta(i, j, k);
        return (iMetaData & 1) > 0;
    }

    public void SetDoesPlanterContainSoil(Level world, int i, int j, int k, boolean bContainsSoil)
    {
        int iMetaData = world.getTileMeta(i, j, k) & -2;
        if(bContainsSoil)
        {
            iMetaData |= 1;
        }
        world.setTileMeta(i, j, k, iMetaData);
        world.method_243(i, j, k);
    }

    public int GetGrowthState(BlockView iBlockAccess, int i, int j, int k)
    {
        int iMetaData = iBlockAccess.getTileMeta(i, j, k);
        return (iMetaData & 6) >> 1;
    }

    public void SetGrowthState(Level world, int i, int j, int k, int iGrowthState)
    {
        int iMetaData = world.getTileMeta(i, j, k) & -7;
        iMetaData |= (iGrowthState & 3) << 1;
        world.setTileMeta(i, j, k, iMetaData);
        world.method_243(i, j, k);
    }

    public static final float m_fPlanterWidth = 0.75F;
    public static final float m_fPlanterHalfWidth = 0.375F;
    public static final float m_fPlanterBandHeight = 0.3125F;
    public static final float m_fPlanterBandHalfHeight = 0.15625F;
    private final int iPlanterDirtTextureIndex = 78;

    @Override
    public boolean renderWorld(BlockRenderer tileRenderer, BlockView tileView, int x, int y, int z) {
        this.setBoundingBox(0.125F, 0.0F, 0.125F, 0.25F, 0.6875F, 0.75F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.125F, 0.0F, 0.75F, 0.75F, 0.6875F, 0.875F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.75F, 0.0F, 0.25F, 0.875F, 0.6875F, 0.875F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.25F, 0.0F, 0.125F, 0.875F, 0.6875F, 0.25F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.25F, 0.0F, 0.25F, 0.75F, 0.125F, 0.75F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.0F, 0.6875F, 0.0F, 0.125F, 1.0F, 0.875F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.0F, 0.6875F, 0.875F, 0.875F, 1.0F, 1.0F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.875F, 0.6875F, 0.125F, 1.0F, 1.0F, 1.0F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.125F, 0.6875F, 0.0F, 1.0F, 1.0F, 0.125F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        if(DoesPlanterContainSoil(tileView, x, y, z))
        {
            this.setBoundingBox(0.125F, 0.9F, 0.125F, 0.875F, 1.0F, 0.875F);
            CustomBlockRendering.RenderStandardBlockWithTexture(tileRenderer, this, x, y, z, TextureListener.planter_soil);
        }
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }

    @Override
    public void renderInventory(BlockRenderer tileRenderer, int meta) {
        this.setBoundingBox(0.125F, 0.0F, 0.125F, 0.25F, 0.6875F, 0.75F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.planter);
        this.setBoundingBox(0.125F, 0.0F, 0.75F, 0.75F, 0.6875F, 0.875F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.planter);
        this.setBoundingBox(0.75F, 0.0F, 0.25F, 0.875F, 0.6875F, 0.875F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.planter);
        this.setBoundingBox(0.25F, 0.0F, 0.125F, 0.875F, 0.6875F, 0.25F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.planter);
        this.setBoundingBox(0.25F, 0.0F, 0.25F, 0.75F, 0.125F, 0.75F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.planter);
        this.setBoundingBox(0.0F, 0.6875F, 0.0F, 0.125F, 1.0F, 0.875F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.planter);
        this.setBoundingBox(0.0F, 0.6875F, 0.875F, 0.875F, 1.0F, 1.0F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.planter);
        this.setBoundingBox(0.875F, 0.6875F, 0.125F, 1.0F, 1.0F, 1.0F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.planter);
        this.setBoundingBox(0.125F, 0.6875F, 0.0F, 1.0F, 1.0F, 0.125F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.planter);
        if(meta > 0)
        {
            this.setBoundingBox(0.125F, 0.9F, 0.125F, 0.875F, 1.0F, 0.875F);
            CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.planter_soil);
        }
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
