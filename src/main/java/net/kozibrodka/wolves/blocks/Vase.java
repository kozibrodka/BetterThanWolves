package net.kozibrodka.wolves.blocks;

import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.itemblocks.VaseItemBlock;
import net.kozibrodka.wolves.tileentity.VaseTileEntity;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationapi.api.block.HasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;

import java.util.Random;

@HasCustomBlockItemFactory(VaseItemBlock.class)
public class Vase extends TemplateBlockWithEntity
    implements RotatableBlock, BlockWithWorldRenderer, BlockWithInventoryRenderer
{

    public Vase(Identifier iid)
    {
        super(iid, Material.GLASS);
        texture = 224;
        setHardness(0.0F);
        setSounds(GLASS_SOUNDS);
        setBoundingBox(0.1875F, 0.0F, 0.1875F, 0.8125F, 1.0F, 0.8125F);
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public int getDropCount(Random random)
    {
        return 0;
    }

    protected int droppedMeta(int i)
    {
        return i;
    }

    public int getTextureForSide(int iSide, int iMetaData)
    {
        return switch (iMetaData) {
            case 0 -> TextureListener.vase_white;
            case 1 -> TextureListener.vase_orange;
            case 2 -> TextureListener.vase_magenta;
            case 3 -> TextureListener.vase_light_blue;
            case 4 -> TextureListener.vase_yellow;
            case 5 -> TextureListener.vase_lime;
            case 6 -> TextureListener.vase_pink;
            case 7 -> TextureListener.vase_gray;
            case 8 -> TextureListener.vase_light_gray;
            case 9 -> TextureListener.vase_cyan;
            case 10 -> TextureListener.vase_purple;
            case 11 -> TextureListener.vase_blue;
            case 12 -> TextureListener.vase_brown;
            case 13 -> TextureListener.vase_green;
            case 14 -> TextureListener.vase_red;
            case 15 -> TextureListener.vase_black;
            default -> 0;
        };
    }

    protected TileEntityBase createTileEntity()
    {
        return new VaseTileEntity();
    }

    public boolean canUse(Level world, int i, int j, int k, PlayerBase entityPlayer)
    {
        if(world.isServerSide)
        {
            return true;
        }
        ItemInstance playerEquippedItemInstance = entityPlayer.getHeldItem();
        if(playerEquippedItemInstance != null && playerEquippedItemInstance.count > 0)
        {
            VaseTileEntity tileEntityVase = (VaseTileEntity)world.getTileEntity(i, j, k);
            int iTempStackSize = playerEquippedItemInstance.count;
            if(InventoryHandler.addItemInstanceToInventory(tileEntityVase, playerEquippedItemInstance))
            {
                entityPlayer.breakHeldItem();
                 world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.pop", 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                return true;
            }
            if(playerEquippedItemInstance.count < iTempStackSize)
            {
                 world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.pop", 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                return true;
            }
        }
        return false;
    }

    public void onBlockRemoved(Level world, int i, int j, int k)
    {
        VaseTileEntity tileEntity = (VaseTileEntity)world.getTileEntity(i, j, k);
        if(tileEntity != null)
        {
            InventoryHandler.ejectInventoryContents(world, i, j, k, tileEntity);
        }
        super.onBlockRemoved(world, i, j, k);
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

    public void BreakVase(Level world, int i, int j, int k)
    {
        Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).particleManager.addTileBreakParticles(i, j, k, id, 0);
        Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).soundHelper.playSound(sounds.getWalkSound(), (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, (sounds.getVolume() + 1.0F) / 2.0F, sounds.getPitch() * 0.8F);
        world.setTile(i, j, k, 0);
    }

    public static final float m_fVaseBaseWidth = 0.5F;
    public static final float m_fVaseBaseHalfWidth = 0.25F;
    public static final float m_fVaseBaseHeight = 0.0625F;
    public static final float m_fVaseBodyWidth = 0.625F;
    public static final float m_fVaseBodyHalfWidth = 0.3125F;
    public static final float m_fVaseBodyHeight = 0.375F;
    public static final float m_fVaseNeckBaseWidth = 0.5F;
    public static final float m_fVaseNeckBaseHalfWidth = 0.25F;
    public static final float m_fVaseNeckBaseHeight = 0.0625F;
    public static final float m_fVaseNeckWidth = 0.25F;
    public static final float m_fVaseNeckHalfWidth = 0.125F;
    public static final float m_fVaseNeckHeight = 0.4375F;
    public static final float m_fVaseTopWidth = 0.375F;
    public static final float m_fVaseTopHalfWidth = 0.1875F;
    public static final float m_fVaseTopHeight = 0.0625F;
    private final int iVaseFirstTextureIndex = 224;

    @Override
    public boolean renderWorld(BlockRenderer tileRenderer, BlockView tileView, int x, int y, int z) {
        this.setBoundingBox(0.25F, 0.0F, 0.25F, 0.75F, 0.0625F, 0.75F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.1875F, 0.0625F, 0.1875F, 0.8125F, 0.4375F, 0.8125F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.25F, 0.4375F, 0.25F, 0.75F, 0.5F, 0.75F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.375F, 0.5F, 0.375F, 0.625F, 0.9375F, 0.625F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.3125F, 0.9375F, 0.3125F, 0.6875F, 1.0F, 0.6875F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        setBoundingBox(0.1875F, 0.0F, 0.1875F, 0.8125F, 1.0F, 0.8125F);
        return true;
    }

    @Override
    public void renderInventory(BlockRenderer tileRenderer, int meta) {
        this.setBoundingBox(0.25F, 0.0F, 0.25F, 0.75F, 0.0625F, 0.75F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, meta);
        this.setBoundingBox(0.1875F, 0.0625F, 0.1875F, 0.8125F, 0.4375F, 0.8125F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, meta);
        this.setBoundingBox(0.25F, 0.4375F, 0.25F, 0.75F, 0.5F, 0.75F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, meta);
        this.setBoundingBox(0.375F, 0.5F, 0.375F, 0.625F, 0.9375F, 0.625F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, meta);
        this.setBoundingBox(0.3125F, 0.9375F, 0.3125F, 0.6875F, 1.0F, 0.6875F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, meta);
        setBoundingBox(0.1875F, 0.0F, 0.1875F, 0.8125F, 1.0F, 0.8125F);
    }
}
