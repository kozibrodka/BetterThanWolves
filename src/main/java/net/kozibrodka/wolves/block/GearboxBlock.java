package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ConfigListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.MechanicalDevice;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Random;

public class GearboxBlock extends TemplateBlock
        implements MechanicalDevice, RotatableBlock {

    public GearboxBlock(Identifier identifier) {
        super(identifier, Material.WOOD);
        setHardness(2.0F);
        setSoundGroup(WOOD_SOUND_GROUP);
    }

    public int getTextureId(BlockView blockView, int x, int y, int z, int side) {
        int iFacing = getFacing(blockView, x, y, z);
        if (side == iFacing) {
            return TextureListener.gearbox_front;
        }
        BlockPosition sideBlockPos = new BlockPosition(x, y, z);
        sideBlockPos.addFacingAsOffset(side);
        if (blockView.getBlockId(sideBlockPos.x, sideBlockPos.y, sideBlockPos.z) == BlockListener.axleBlock.id && ((AxleBlock) BlockListener.axleBlock).isAxleOrientedTowardsFacing(blockView, sideBlockPos.x, sideBlockPos.y, sideBlockPos.z, side)
                || blockView.getBlockId(sideBlockPos.x, sideBlockPos.y, sideBlockPos.z) == BlockListener.nonCollidingAxleBlock.id && ((AxleBlock) BlockListener.nonCollidingAxleBlock).isAxleOrientedTowardsFacing(blockView, sideBlockPos.x, sideBlockPos.y, sideBlockPos.z, side)) {
            return TextureListener.gearbox_output;
        } else {
            return TextureListener.gearbox_side;
        }
    }

    public int getTexture(int side) {
        if (side == 3) {
            return TextureListener.gearbox_front;
        } else {
            return TextureListener.gearbox_side;
        }
    }

    public int getTickRate() {
        return 10;
    }

    public void onPlaced(World world, int x, int y, int z, int facing) {
        setFacing(world, x, y, z, UnsortedUtils.getOppositeFacing(facing));
    }

    public void onPlaced(World world, int x, int y, int z, LivingEntity livingEntity) {
        int facing = UnsortedUtils.ConvertPlacingEntityOrientationToBlockFacing(livingEntity);
        if (ConfigListener.wolvesGlass.small_tweaks.faceGearBoxAwayFromPlayer) {
            facing = UnsortedUtils.getOppositeFacing(facing);
        }
        setFacing(world, x, y, z, facing);
    }

    public void onBreak(World world, int x, int y, int z) {
        if (isGearBoxOn(world, x, y, z)) {
            setGearBoxOnState(world, x, y, z, false);
            validateOutputs(world, x, y, z, false);
        }
        super.onBreak(world, x, y, z);
    }

    @Override
    public void neighborUpdate(World world, int x, int y, int z, int id) {
        super.neighborUpdate(world, x, y, z, id);
        boolean redstonePowered = isPoweredByRedstone(world, x, y, z);
        boolean gearBoxOn = isGearBoxOn(world, x, y, z);
        if (redstonePowered && gearBoxOn) {
            setGearBoxOnState(world, x, y, z, false);
            handleGearBoxDeactivation(world, x, y, z);
        }
    }

    private boolean isPoweredByRedstone(World world, int x, int y, int z) {
        return world.isEmittingRedstonePower(x, y, z) || world.isEmittingRedstonePower(x, y + 1, z);
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(World world, String name, int x, int y, int z, float g, float h) {
        List list2 = world.players;
        if (list2.size() != 0) {
            for (int k = 0; k < list2.size(); k++) {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g, h));
            }
        }
    }

    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (isGearBoxOn(world, x, y, z)) {
            emitGearBoxParticles(world, x, y, z, random);
        }
    }

    public int getFacing(BlockView blockView, int x, int y, int z) {
        return getFacingFromMeta(blockView.getBlockMeta(x, y, z));
    }

    private int getFacingFromMeta(int meta) {
        return meta & 7;
    }

    public void setFacing(World world, int x, int y, int z, int facing) {
        int metadata = world.getBlockMeta(x, y, z) & 8;
        metadata |= facing;
        world.setBlockMeta(x, y, z, metadata);
    }

    public boolean canRotate(BlockView blockView, int x, int y, int z) {
        return true;
    }

    public boolean canTransmitRotation(BlockView blockView, int x, int y, int z) {
        return true;
    }

    public void rotate(World world, int x, int y, int z, boolean reverse) {
        System.out.println("METODA ROTATE W GEARBOX");
        int facing = getFacing(world, x, y, z);
        int newFacing = UnsortedUtils.RotateFacingAroundJ(facing, reverse);
        if (newFacing != facing) {
            setFacing(world, x, y, z, newFacing);
            world.setBlocksDirty(x, y, z, x, y, z);
            world.scheduleBlockUpdate(x, y, z, BlockListener.gearBox.id, getTickRate());
            world.blockUpdate(x, y, z, BlockListener.gearBox.id);
        }
        UnsortedUtils.DestroyHorizontallyAttachedAxles(world, x, y, z);
    }

    public boolean isGearBoxOn(BlockView blockView, int x, int y, int z) {
        return (blockView.getBlockMeta(x, y, z) & 8) > 0;
    }

    public void setGearBoxOnState(World world, int x, int y, int z, boolean isOn) {
        int metadata = world.getBlockMeta(x, y, z) & 7;
        if (isOn) {
            metadata |= 8;
        }
        world.setBlockMeta(x, y, z, metadata);
        world.blockUpdateEvent(x, y, z);
    }

    void emitGearBoxParticles(World world, int x, int y, int z, Random random) {
        for (int counter = 0; counter < 5; counter++) {
            float smokeX = (float) x + random.nextFloat();
            float smokeY = (float) y + random.nextFloat() * 0.5F + 1.0F;
            float smokeZ = (float) z + random.nextFloat();
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

    }

    /**
     * Checks each output of the gear box for attached axles
     * @param world World the block is in.
     * @param x x coordinate of the block.
     * @param y y coordinate of the block.
     * @param z z coordinate of the block.
     * @param destroyIfAlreadyPowered Determines if already powered axles should be destroyed.
     */
    private void validateOutputs(World world, int x, int y, int z, boolean destroyIfAlreadyPowered) {
        int blockFacing = getFacing(world, x, y, z);
        boolean isGearBoxOn = isGearBoxOn(world, x, y, z);
        if(world.isRemote) {
            return;
        }
        for (int facing = 0; facing <= 5; facing++) { // Iterate through all sides
            if (facing == blockFacing) {
                continue;
            }
            // Check if block is an axle
            BlockPosition tempPos = new BlockPosition(x, y, z);
            tempPos.addFacingAsOffset(facing);
            if (world.getBlockId(tempPos.x, tempPos.y, tempPos.z) != BlockListener.axleBlock.id && world.getBlockId(tempPos.x, tempPos.y, tempPos.z) != BlockListener.nonCollidingAxleBlock.id) {
                continue;
            }
            // Convert non-colliding axles into normal axles
            AxleBlock axleBlock = (AxleBlock) BlockListener.axleBlock;
            if (world.getBlockId(tempPos.x, tempPos.y, tempPos.z) == BlockListener.nonCollidingAxleBlock.id) {
                axleBlock = (AxleBlock) BlockListener.nonCollidingAxleBlock;
            }
            // Skip misaligned axles
            if (!axleBlock.isAxleOrientedTowardsFacing(world, tempPos.x, tempPos.y, tempPos.z, facing)) {
                continue;
            }
            // Check if axle is already powered
            int tempPowerLevel = axleBlock.getPowerLevel(world, tempPos.x, tempPos.y, tempPos.z);
            if (tempPowerLevel > 0 && destroyIfAlreadyPowered) {
                axleBlock.breakAxle(world, tempPos.x, tempPos.y, tempPos.z);
                continue;
            }
            // Activate axle
            if (isGearBoxOn) {
                if (tempPowerLevel != 3) {
                    axleBlock.setPowerLevel(world, tempPos.x, tempPos.y, tempPos.z, 3);
                }
                continue;
            }
            // Deactivate axle
            if (tempPowerLevel != 0) {
                axleBlock.setPowerLevel(world, tempPos.x, tempPos.y, tempPos.z, 0);
            }
        }

    }

    public void overpower(World world, int x, int y, int z) {
        boolean isRedstonePowered = world.canTransferPower(x, y, z) || world.canTransferPower(x, y + 1, z);
        if (!isRedstonePowered) {
            breakGearBox(world, x, y, z);
        }
    }

    public void breakGearBox(World world, int x, int y, int z) {
        for (int drops = 0; drops < 4; drops++) {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, x, y, z, Block.PLANKS.asItem().id, 0);
        }

        for (int drops = 0; drops < 3; drops++) {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, x, y, z, ItemListener.gear.id, 0);
        }

        UnsortedUtils.EjectSingleItemWithRandomOffset(world, x, y, z, Item.REDSTONE.id, 0);
        world.playSound((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.explode", 0.2F, 1.25F);
        if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
            voicePacket(world, "random.explode", x, y, z, 0.05F, 1.0F);
        }
        world.setBlock(x, y, z, 0);
    }

    public boolean canOutputMechanicalPower() {
        return true;
    }

    public boolean canInputMechanicalPower() {
        return true;
    }

    public boolean isInputtingMechanicalPower(World world, int x, int y, int z) {
        int facing = getFacing(world, x, y, z);
        BlockPosition targetBlockPos = new BlockPosition(x, y, z);
        targetBlockPos.addFacingAsOffset(facing);
        int targetId = world.getBlockId(targetBlockPos.x, targetBlockPos.y, targetBlockPos.z);
        return targetId == BlockListener.axleBlock.id && ((AxleBlock) BlockListener.axleBlock).isAxleOrientedTowardsFacing(world, targetBlockPos.x, targetBlockPos.y, targetBlockPos.z, facing) && ((AxleBlock) BlockListener.axleBlock).getPowerLevel(world, targetBlockPos.x, targetBlockPos.y, targetBlockPos.z) > 0
                || targetId == BlockListener.nonCollidingAxleBlock.id && ((AxleBlock) BlockListener.nonCollidingAxleBlock).isAxleOrientedTowardsFacing(world, targetBlockPos.x, targetBlockPos.y, targetBlockPos.z, facing) && ((AxleBlock) BlockListener.nonCollidingAxleBlock).getPowerLevel(world, targetBlockPos.x, targetBlockPos.y, targetBlockPos.z) > 0;
    }

    public boolean isOutputtingMechanicalPower(World world, int x, int y, int z) {
        return isGearBoxOn(world, x, y, z);
    }

    @Override
    public boolean canInputMechanicalPower(World world, int x, int y, int z, int side) {
        if (isPoweredByRedstone(world, x, y, z)) {
            return false;
        }
        return getFacing(world, x, y, z) == side;
    }

    @Override
    public boolean isOutputtingMechanicalPower(World world, int x, int y, int z, int side) {
        return isGearBoxOn(world, x, y, z) && getFacing(world, x, y, z) != side;
    }

    @Override
    public void powerMachine(World world, int x, int y, int z, int side) {
        setGearBoxOnState(world, x, y, z, true);
        handleGearBoxActivation(world, x, y, z, new Random());
    }

    @Override
    public void unpowerMachine(World world, int x, int y, int z, int side) {
        setGearBoxOnState(world, x, y, z, false);
        handleGearBoxDeactivation(world, x, y, z);
    }

    private void handleGearBoxDeactivation(World world, int x, int y, int z) {
        setGearBoxOnState(world, x, y, z, false);
        validateOutputs(world, x, y, z, false);
    }

    private void handleGearBoxActivation(World world, int x, int y, int z, Random random) {
        world.playSound((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.explode", 0.05F, 1.0F);
        if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
            voicePacket(world, "random.explode", x, y, z, 0.05F, 1.0F);
        }
        emitGearBoxParticles(world, x, y, z, random);
        setGearBoxOnState(world, x, y, z, true);
        validateOutputs(world, x, y, z, true);
    }

    @Override
    public boolean isMachinePowered(World world, int x, int y, int z) {
        return isGearBoxOn(world, x, y, z);
    }
}
