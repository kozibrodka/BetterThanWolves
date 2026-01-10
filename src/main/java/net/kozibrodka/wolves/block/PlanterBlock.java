package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.EnvironmentInterface;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.itemblocks.PlanterBlockItem;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.SoilTemplate;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.HasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithWorldRenderer.class)
@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithInventoryRenderer.class)
@HasCustomBlockItemFactory(PlanterBlockItem.class)
public class PlanterBlock extends TemplateBlock
        implements RotatableBlock, SoilTemplate, BlockWithWorldRenderer, BlockWithInventoryRenderer {

    public PlanterBlock(Identifier iid) {
        super(iid, Material.GLASS);
        setHardness(0.6F);
        setSoundGroup(GLASS_SOUND_GROUP);
        setTickRandomly(true);
    }

    public int getTexture(int i) {
        return TextureListener.planter;
    }

    public boolean isOpaque() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public int getDroppedItemMeta(int iMetaData) {
        return iMetaData & 1;
    }

    public void onTick(World world, int i, int j, int k, Random random) {
        int iOldGrowthState = GetGrowthState(world, i, j, k);
        int iNewGrowthState = 0;
        if (world.isAir(i, j + 1, k) && world.getLightLevel(i, j + 1, k) >= 8) {
            iNewGrowthState = iOldGrowthState;
            if (random.nextInt(50) == 0 && ++iNewGrowthState > 1) {
                iNewGrowthState = 0;
                if (random.nextInt(2) == 0) {
                    world.setBlock(i, j + 1, k, Block.ROSE.id);
                } else {
                    world.setBlock(i, j + 1, k, Block.DANDELION.id);
                }
            }
        }
        if (iNewGrowthState != iOldGrowthState) {
            SetGrowthState(world, i, j, k, iNewGrowthState);
        }
    }

    public int getFacing(BlockView iBlockAccess, int i, int j, int l) {
        return 0;
    }

    public void setFacing(World world1, int l, int i1, int j1, int k1) {
    }

    public boolean canRotate(BlockView iBlockAccess, int i, int j, int l) {
        return false;
    }

    public boolean canTransmitRotation(BlockView iBlockAccess, int i, int j, int l) {
        return false;
    }

    public void rotate(World world1, int l, int i1, int j1, boolean flag) {
    }

    public boolean CanPlantGrowOnBlock(World world, int i, int j, int k, Block plantBlock) {
        return DoesPlanterContainSoil(world, i, j, k);
    }

    public boolean IsBlockHydrated(World world, int i, int j, int k) {
        return DoesPlanterContainSoil(world, i, j, k);
    }

    public boolean IsBlockConsideredNeighbouringWater(World world, int i, int j, int k) {
        return DoesPlanterContainSoil(world, i, j, k);
    }

    public boolean DoesPlanterContainSoil(BlockView iBlockAccess, int i, int j, int k) {
        int iMetaData = iBlockAccess.getBlockMeta(i, j, k);
        return (iMetaData & 1) > 0;
    }

    public void SetDoesPlanterContainSoil(World world, int i, int j, int k, boolean bContainsSoil) {
        int iMetaData = world.getBlockMeta(i, j, k) & -2;
        if (bContainsSoil) {
            iMetaData |= 1;
        }
        world.setBlockMeta(i, j, k, iMetaData);
        world.blockUpdateEvent(i, j, k);
    }

    public int GetGrowthState(BlockView iBlockAccess, int i, int j, int k) {
        int iMetaData = iBlockAccess.getBlockMeta(i, j, k);
        return (iMetaData & 6) >> 1;
    }

    public void SetGrowthState(World world, int i, int j, int k, int iGrowthState) {
        int iMetaData = world.getBlockMeta(i, j, k) & -7;
        iMetaData |= (iGrowthState & 3) << 1;
        world.setBlockMeta(i, j, k, iMetaData);
        world.blockUpdateEvent(i, j, k);
    }

    public static final float m_fPlanterWidth = 0.75F;
    public static final float m_fPlanterHalfWidth = 0.375F;
    public static final float m_fPlanterBandHeight = 0.3125F;
    public static final float m_fPlanterBandHalfHeight = 0.15625F;
    private final int iPlanterDirtTextureIndex = 78;

    @Override
    public boolean renderWorld(BlockRenderManager tileRenderer, BlockView tileView, int x, int y, int z) {
        this.setBoundingBox(0.125F, 0.0F, 0.125F, 0.25F, 0.6875F, 0.75F);
        tileRenderer.renderBlock(this, x, y, z);
        this.setBoundingBox(0.125F, 0.0F, 0.75F, 0.75F, 0.6875F, 0.875F);
        tileRenderer.renderBlock(this, x, y, z);
        this.setBoundingBox(0.75F, 0.0F, 0.25F, 0.875F, 0.6875F, 0.875F);
        tileRenderer.renderBlock(this, x, y, z);
        this.setBoundingBox(0.25F, 0.0F, 0.125F, 0.875F, 0.6875F, 0.25F);
        tileRenderer.renderBlock(this, x, y, z);
        this.setBoundingBox(0.25F, 0.0F, 0.25F, 0.75F, 0.125F, 0.75F);
        tileRenderer.renderBlock(this, x, y, z);
        this.setBoundingBox(0.0F, 0.6875F, 0.0F, 0.125F, 1.0F, 0.875F);
        tileRenderer.renderBlock(this, x, y, z);
        this.setBoundingBox(0.0F, 0.6875F, 0.875F, 0.875F, 1.0F, 1.0F);
        tileRenderer.renderBlock(this, x, y, z);
        this.setBoundingBox(0.875F, 0.6875F, 0.125F, 1.0F, 1.0F, 1.0F);
        tileRenderer.renderBlock(this, x, y, z);
        this.setBoundingBox(0.125F, 0.6875F, 0.0F, 1.0F, 1.0F, 0.125F);
        tileRenderer.renderBlock(this, x, y, z);
        if (DoesPlanterContainSoil(tileView, x, y, z)) {
            this.setBoundingBox(0.125F, 0.9F, 0.125F, 0.875F, 1.0F, 0.875F);
            CustomBlockRendering.renderStandardBlockWithTexture(tileRenderer, this, x, y, z, TextureListener.planter_soil);
        }
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }

    @Override
    public void renderInventory(BlockRenderManager tileRenderer, int meta) {
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
        if (meta > 0) {
            this.setBoundingBox(0.125F, 0.9F, 0.125F, 0.875F, 1.0F, 0.875F);
            CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.planter_soil);
        }
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
