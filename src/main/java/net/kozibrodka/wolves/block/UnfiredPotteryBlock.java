package net.kozibrodka.wolves.block;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.EnvironmentInterface;
import net.kozibrodka.wolves.block.entity.UnfiredPotteryBlockEntity;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.itemblocks.UnfiredPotteryBlockItem;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.HasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithWorldRenderer.class)
@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithInventoryRenderer.class)
@HasCustomBlockItemFactory(UnfiredPotteryBlockItem.class)
public class UnfiredPotteryBlock extends TemplateBlockWithEntity
        implements RotatableBlock, BlockWithWorldRenderer, BlockWithInventoryRenderer {

    public UnfiredPotteryBlock(Identifier iid) {
        super(iid, Material.CLAY);
        setHardness(0.6F);
        setSoundGroup(GRAVEL_SOUND_GROUP);
    }

    public int getTexture(int iSide) {
        return TextureListener.unfiredpottery;
    }

    public int getDroppedItemMeta(int itemMetaData) {
        return itemMetaData;
    }

    public boolean isOpaque() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public Box getCollisionShape(World world, int i, int j, int k) {
        int blockMetaData = world.getBlockMeta(i, j, k);
        switch (blockMetaData) {
            case 0: // '\0'
            case 1: // '\001'
                return Box.createCached((float) i, (float) j, (float) k, (float) i + 1.0F, (float) j + 1.0F, (float) k + 1.0F);

            case 2: // '\002'
                return Box.createCached((float) i + 0.1875F, (float) j, (float) k + 0.1875F, (float) i + 0.8125F, (float) j + 1.0F, (float) k + 0.8125F);
        }
        return Box.createCached((float) i, (float) j, (float) k, (float) i + 1.0F, (float) j + 1.0F, (float) k + 1.0F);
    }

    public void updateBoundingBox(BlockView iBlockAccess, int i, int j, int k) {
        int blockMetaData = iBlockAccess.getBlockMeta(i, j, k);
        switch (blockMetaData) {
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

    protected BlockEntity createBlockEntity() {
        return new UnfiredPotteryBlockEntity();
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

    public void Cook(World world, int i, int j, int k) {
        int blockMetaData = world.getBlockMeta(i, j, k);
        int newItemId = 0;
        switch (blockMetaData) {
            case 0: // '\0'
                newItemId = BlockListener.crucible.asItem().id;
                break;

            case 1: // '\001'
                newItemId = BlockListener.planter.asItem().id;
                break;

            case 2: // '\002'
                newItemId = BlockListener.vase.asItem().id;
                break;
        }
        world.setBlock(i, j, k, 0);
        if (newItemId > 0) {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, newItemId, 0);
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
    public boolean renderWorld(BlockRenderManager tileRenderer, BlockView tileView, int x, int y, int z) {
        updateBoundingBox(tileView, x, y, z);
        UnfiredPotteryBlockEntity fctileentityunfiredpottery = (UnfiredPotteryBlockEntity) tileView.getBlockEntity(x, y, z);
        int l = TextureListener.unfiredpottery;
        if (fctileentityunfiredpottery != null && fctileentityunfiredpottery.IsCooking()) {
            l = TextureListener.unfiredpottery_cook;
        }
        int i1 = tileView.getBlockMeta(x, y, z);
        switch (i1) {
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
                tileRenderer.renderBlock(this, x, y, z);
                break;
        }
        return true;
    }

    public boolean RenderUnfiredCrucible(BlockRenderManager renderblocks, BlockView iblockaccess, int i, int j, int k, int l) {
        this.setBoundingBox(0.0625F, 0.0F, 0.0625F, 0.1875F, 1.0F, 0.8125F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.0625F, 0.0F, 0.8125F, 0.8125F, 1.0F, 0.9375F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.8125F, 0.0F, 0.1875F, 0.9375F, 1.0F, 0.9375F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.1875F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.1875F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.1875F, 0.0F, 0.1875F, 0.8125F, 0.125F, 0.8125F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.0F, 0.125F, 0.0F, 0.125F, 0.875F, 0.875F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.0F, 0.125F, 0.875F, 0.875F, 0.875F, 1.0F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.875F, 0.125F, 0.125F, 1.0F, 0.875F, 1.0F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.125F, 0.125F, 0.0F, 1.0F, 0.875F, 0.125F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        updateBoundingBox(iblockaccess, i, j, k);
        return true;
    }

    public boolean RenderUnfiredPot(BlockRenderManager renderblocks, BlockView iblockaccess, int i, int j, int k, int l) {
        this.setBoundingBox(0.125F, 0.0F, 0.125F, 0.25F, 0.6875F, 0.75F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.125F, 0.0F, 0.75F, 0.75F, 0.6875F, 0.875F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.75F, 0.0F, 0.25F, 0.875F, 0.6875F, 0.875F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.25F, 0.0F, 0.125F, 0.875F, 0.6875F, 0.25F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.25F, 0.0F, 0.25F, 0.75F, 0.125F, 0.75F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.0F, 0.6875F, 0.0F, 0.125F, 1.0F, 0.875F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.0F, 0.6875F, 0.875F, 0.875F, 1.0F, 1.0F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.875F, 0.6875F, 0.125F, 1.0F, 1.0F, 1.0F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.125F, 0.6875F, 0.0F, 1.0F, 1.0F, 0.125F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        updateBoundingBox(iblockaccess, i, j, k);
        return true;
    }

    public boolean RenderUnfiredVase(BlockRenderManager renderblocks, BlockView iblockaccess, int i, int j, int k, int l) {
        this.setBoundingBox(0.25F, 0.0F, 0.25F, 0.75F, 0.0625F, 0.75F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.1875F, 0.0625F, 0.1875F, 0.8125F, 0.4375F, 0.8125F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.25F, 0.4375F, 0.25F, 0.75F, 0.5F, 0.75F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.375F, 0.5F, 0.375F, 0.625F, 0.9375F, 0.625F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        this.setBoundingBox(0.3125F, 0.9375F, 0.3125F, 0.6875F, 1.0F, 0.6875F);
        CustomBlockRendering.renderStandardBlockWithTexture(renderblocks, this, i, j, k, l);
        updateBoundingBox(iblockaccess, i, j, k);
        return true;
    }

    @Override
    public void renderInventory(BlockRenderManager tileRenderer, int meta) {
        this.setupRenderBoundingBox();
        switch (meta) {
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

    public void RenderUnfiredCrucibleInvBlock(BlockRenderManager renderblocks, Block block, int i) {
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
        this.setupRenderBoundingBox();
    }

    public void RenderUnfiredPotInvBlock(BlockRenderManager renderblocks, Block block, int i) {
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
        this.setupRenderBoundingBox();
    }

    public void RenderUnfiredVaseInvBlock(BlockRenderManager renderblocks, Block block, int i) {
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
        this.setupRenderBoundingBox();
    }
}
