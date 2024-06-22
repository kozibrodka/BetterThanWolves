package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.itemblocks.VaseBlockItem;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.block.entity.VaseBlockEntity;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.HasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;

import java.util.List;
import java.util.Random;

@HasCustomBlockItemFactory(VaseBlockItem.class)
public class VaseBlock extends TemplateBlockWithEntity
    implements RotatableBlock, BlockWithWorldRenderer, BlockWithInventoryRenderer
{

    public VaseBlock(Identifier iid)
    {
        super(iid, Material.field_994);
        textureId = 224;
        setHardness(0.0F);
        setSoundGroup(GLASS_SOUND_GROUP);
        setBoundingBox(0.1875F, 0.0F, 0.1875F, 0.8125F, 1.0F, 0.8125F);
    }

    public boolean isOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public int getDroppedItemCount(Random random)
    {
        return 0;
    }

    protected int getDroppedItemMeta(int i)
    {
        return i;
    }

    public int getTexture(int iSide, int iMetaData)
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

    protected BlockEntity createBlockEntity()
    {
        return new VaseBlockEntity();
    }

    public boolean onUse(World world, int i, int j, int k, PlayerEntity entityPlayer)
    {
        if(world.isRemote)
        {
            return true;
        }
        ItemStack playerEquippedItemInstance = entityPlayer.getHand();
        if(playerEquippedItemInstance != null && playerEquippedItemInstance.count > 0)
        {
            VaseBlockEntity tileEntityVase = (VaseBlockEntity)world.getBlockEntity(i, j, k);
            int iTempStackSize = playerEquippedItemInstance.count;
            if(InventoryHandler.addItemInstanceToInventory(tileEntityVase, playerEquippedItemInstance))
            {
                entityPlayer.method_503();
                world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.pop", 0.25F, ((world.field_214.nextFloat() - world.field_214.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                    voicePacket(world, "random.explode", i, j, k, 0.25F, ((world.field_214.nextFloat() - world.field_214.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                }
                return true;
            }
            if(playerEquippedItemInstance.count < iTempStackSize)
            {
                world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.pop", 0.25F, ((world.field_214.nextFloat() - world.field_214.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                    voicePacket(world, "random.explode", i, j, k, 0.25F, ((world.field_214.nextFloat() - world.field_214.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                }
                return true;
            }
        }
        return false;
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(World world, String name, int x, int y, int z, float g, float h){
        List list2 = world.field_200;
        if(list2.size() != 0) {
            for(int k = 0; k < list2.size(); k++)
            {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g,h));
            }
        }
    }

    public void onBreak(World world, int i, int j, int k)
    {
        VaseBlockEntity tileEntity = (VaseBlockEntity)world.getBlockEntity(i, j, k);
        if(tileEntity != null)
        {
            InventoryHandler.ejectInventoryContents(world, i, j, k, tileEntity);
        }
        super.onBreak(world, i, j, k);
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int l)
    {
        return 0;
    }

    public void SetFacing(World world1, int l, int i1, int j1, int k1)
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

    public void Rotate(World world1, int l, int i1, int j1, boolean flag)
    {
    }

    public void BreakVase(World world, int i, int j, int k)
    {
        Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).field_2808.method_322(i, j, k, id, 0);
        Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).soundManager.playSound(soundGroup.getSound(), (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, (soundGroup.method_1976() + 1.0F) / 2.0F, soundGroup.method_1977() * 0.8F);
        world.setBlock(i, j, k, 0);
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
    public boolean renderWorld(BlockRenderManager tileRenderer, BlockView tileView, int x, int y, int z) {
        this.setBoundingBox(0.25F, 0.0F, 0.25F, 0.75F, 0.0625F, 0.75F);
        tileRenderer.renderBlock(this, x, y, z);
        this.setBoundingBox(0.1875F, 0.0625F, 0.1875F, 0.8125F, 0.4375F, 0.8125F);
        tileRenderer.renderBlock(this, x, y, z);
        this.setBoundingBox(0.25F, 0.4375F, 0.25F, 0.75F, 0.5F, 0.75F);
        tileRenderer.renderBlock(this, x, y, z);
        this.setBoundingBox(0.375F, 0.5F, 0.375F, 0.625F, 0.9375F, 0.625F);
        tileRenderer.renderBlock(this, x, y, z);
        this.setBoundingBox(0.3125F, 0.9375F, 0.3125F, 0.6875F, 1.0F, 0.6875F);
        tileRenderer.renderBlock(this, x, y, z);
        setBoundingBox(0.1875F, 0.0F, 0.1875F, 0.8125F, 1.0F, 0.8125F);
        return true;
    }

    @Override
    public void renderInventory(BlockRenderManager tileRenderer, int meta) {
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
