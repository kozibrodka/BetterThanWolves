package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.kozibrodka.wolves.utils.MechanicalDevice;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Random;

@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithInventoryRenderer.class)
public class HandCrankBlock extends TemplateBlock
        implements MechanicalDevice, BlockWithInventoryRenderer {

    public HandCrankBlock(Identifier iid) {
        super(iid, Material.PISTON_BREAKABLE);
        setHardness(0.5F);
        setSoundGroup(WOOD_SOUND_GROUP);
        setTickRandomly(true);
    }

    @Override
    public int getTickRate() {
        return handCrankTickRate;
    }

    @Override
    public int getTexture(int iSide) {
        if (iSide == 1) {
            return TextureListener.handcrack_top;
        }
        if (iSide == 0) {
            return TextureListener.handcrack_bottom;
        } else {
            return TextureListener.handcrack_side;
        }
    }

    @Override
    public Box getCollisionShape(World world, int i, int j, int k) {
        return Box.createCached((float) i, (float) j, (float) k, (float) i + 1.0F, (float) j + handCrankBaseHeight, (float) k + 1.0F);
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public void updateBoundingBox(BlockView iBlockAccess, int i, int j, int k) {
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean canPlaceAt(World world, int i, int j, int k) {
        return world.method_1783(i, j - 1, k);
    }

    @Override
    public void onBlockBreakStart(World world, int i, int j, int k, PlayerEntity entityplayer) {
        onUse(world, i, j, k, entityplayer);
    }

    @Override
    public boolean onUse(World world, int i, int j, int k, PlayerEntity entityPlayer) {
        if (world == null) {
            return true;
        }
        int iMetaData = world.getBlockMeta(i, j, k);
        if (iMetaData == 0) {
            if (!CheckForOverpower(world, i, j, k)) {
                world.setBlockMeta(i, j, k, 1);
                world.setBlocksDirty(i, j, k, i, j, k);
                world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 1.0F, 2.0F);
                world.notifyNeighbors(i, j, k, BlockListener.handCrank.id);
                world.scheduleBlockUpdate(i, j, k, BlockListener.handCrank.id, getTickRate());
            } else {
                BreakCrankWithDrop(world, i, j, k);
            }
        }
        return true;
    }

    @Override
    public void onTick(World world, int i, int j, int k, Random random) {
        int iMetaData = world.getBlockMeta(i, j, k);
        if (iMetaData > 0) {
            if (iMetaData < 7) {
                if (iMetaData <= 6) {
                    world.playNoteBlockActionAt(i,j,k, 0, 1); ///
                }
                if (iMetaData <= 5) {
                    world.scheduleBlockUpdate(i, j, k, BlockListener.handCrank.id, getTickRate() + iMetaData);
                } else {
                    world.scheduleBlockUpdate(i, j, k, BlockListener.handCrank.id, handCrankDelayBeforeReset);
                }
                world.setBlockMetaWithoutNotifyingNeighbors(i, j, k, iMetaData + 1);
                world.blockUpdateEvent(i, j, k);
                world.setBlocksDirty(i, j, k, i, j, k);
            } else {
                world.setBlockMeta(i, j, k, 0);
                world.blockUpdateEvent(i, j, k);
                world.setBlocksDirty(i, j, k, i, j, k);
                world.playNoteBlockActionAt(i,j,k, 0, 0); ///
            }
        }
    }

    @Override
    public void onBlockAction(World world, int x, int y, int z, int code, int pitch) {
        if(pitch == 0){ /// Ciche
            world.playSound((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.click", 0.3F, 0.7F);
        }else if(pitch == 1){ /// Glosne
            world.playSound((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.click", 1.0F, 2.0F);
        }
    }

    @Override
    public void neighborUpdate(World world, int i, int j, int k, int iid) {
        if (!world.method_1783(i, j - 1, k)) {
            dropStacks(world, i, j, k, world.getBlockMeta(i, j, k));
            world.setBlock(i, j, k, 0);
        }
    }

    @Override
    public boolean canOutputMechanicalPower() {
        return false;
    }

    @Override
    public boolean canInputMechanicalPower() {
        return false;
    }

    @Override
    public void powerMachine(World world, int x, int y, int z, int side) {
    }

    @Override
    public void unpowerMachine(World world, int x, int y, int z, int side) {
    }

    @Override
    public boolean isMachinePowered(World world, int x, int y, int z) {
        return false;
    }

    public boolean CheckForOverpower(World world, int i, int j, int k) {
        int iNumPotentialDevicesToPower = 0;
        for (int iTempFacing = 2; iTempFacing <= 5; iTempFacing++) {
            BlockPosition tempPos = new BlockPosition(i, j, k);
            tempPos.addFacingAsOffset(iTempFacing);
            int iTempid = world.getBlockId(tempPos.x, tempPos.y, tempPos.z);
            Block tempBlock = Block.BLOCKS[iTempid];
            if (tempBlock == null || !(tempBlock instanceof MechanicalDevice tempDevice)) {
                continue;
            }
            if (tempDevice.canInputMechanicalPower()) {
                iNumPotentialDevicesToPower++;
            }
        }

        return iNumPotentialDevicesToPower > 1;
    }

    public void BreakCrankWithDrop(World world, int i, int j, int k) {
        UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, Item.STICK.id, 0);
        UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, Block.COBBLESTONE.asItem().id, 0);
        UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, Block.COBBLESTONE.asItem().id, 0);
        UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, ItemListener.gear.id, 0);
        world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.explode", 0.2F, 1.25F);
        world.setBlock(i, j, k, 0);
    }

    private static final int handCrankTickRate = 3;
    private static final int handCrankDelayBeforeReset = 15;
    public static float handCrankBaseHeight = 0.25F;

    @Override
    public void renderInventory(BlockRenderManager tileRenderer, int meta) {
        float f = 0.5F;
        float f1 = 0.5F;
        float f2 = handCrankBaseHeight;
        this.setBoundingBox(0.5F - f1, 0.0F, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        f = 0.0625F;
        f1 = 0.0625F;
        f2 = 1.0F;
        this.setBoundingBox(0.5F - f1, 0.0F, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.handcrack_lever);
    }
}
