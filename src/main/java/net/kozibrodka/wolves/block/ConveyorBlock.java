package net.kozibrodka.wolves.block;

import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.utils.MechanicalDevice;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

public class ConveyorBlock extends LazyBlockTemplate implements MechanicalDevice {
    public ConveyorBlock(Identifier identifier, Material material, float hardness, BlockSoundGroup blockSounds) {
        super(identifier, material, hardness, blockSounds);
    }

    @Override
    public int getTexture(int side, int meta) {
        return switch (meta) {
            case 0, 2 -> switch (side) {
                case 0 -> TextureListener.conveyor_bottom;
                case 1 -> TextureListener.conveyor_up;
                case 2 -> TextureListener.conveyor_front;
                case 3 -> TextureListener.conveyor_back;
                default -> TextureListener.conveyor_side_inlet;
            };
            case 3 -> switch (side) {
                case 0 -> TextureListener.conveyor_bottom;
                case 1 -> TextureListener.conveyor_down;
                case 2 -> TextureListener.conveyor_back;
                case 3 -> TextureListener.conveyor_front;
                default -> TextureListener.conveyor_side_inlet;
            };
            case 4 -> switch (side) {
                case 0 -> TextureListener.conveyor_bottom;
                case 1 -> TextureListener.conveyor_left;
                case 4 -> TextureListener.conveyor_front;
                case 5 -> TextureListener.conveyor_back;
                default -> TextureListener.conveyor_side_inlet;
            };
            case 5 -> switch (side) {
                case 0 -> TextureListener.conveyor_bottom;
                case 1 -> TextureListener.conveyor_right;
                case 4 -> TextureListener.conveyor_back;
                case 5 -> TextureListener.conveyor_front;
                default -> TextureListener.conveyor_side_inlet;
            };
            case 8 -> switch (side) {
                case 0 -> TextureListener.conveyor_bottom;
                case 1 -> TextureListener.conveyor_up_on;
                case 2 -> TextureListener.conveyor_front_on;
                case 3 -> TextureListener.conveyor_back_on;
                default -> TextureListener.conveyor_side_inlet;
            };
            case 9 -> switch (side) {
                case 0 -> TextureListener.conveyor_bottom;
                case 1 -> TextureListener.conveyor_down_on;
                case 2 -> TextureListener.conveyor_back_on;
                case 3 -> TextureListener.conveyor_front_on;
                default -> TextureListener.conveyor_side_inlet;
            };
            case 10 -> switch (side) {
                case 0 -> TextureListener.conveyor_bottom;
                case 1 -> TextureListener.conveyor_left_on;
                case 4 -> TextureListener.conveyor_front_on;
                case 5 -> TextureListener.conveyor_back_on;
                default -> TextureListener.conveyor_side_inlet;
            };
            case 11 -> switch (side) {
                case 0 -> TextureListener.conveyor_bottom;
                case 1 -> TextureListener.conveyor_right_on;
                case 4 -> TextureListener.conveyor_back_on;
                case 5 -> TextureListener.conveyor_front_on;
                default -> TextureListener.conveyor_side_inlet;
            };
            default -> 0;
        };
    }

    public void onPlaced(World world, int x, int y, int z, LivingEntity placer) {
        int facing = MathHelper.floor((double)(placer.yaw * 4.0F / 360.0F) + (double)0.5F) & 3;
        if (facing == 0) {
            world.setBlockMeta(x, y, z, 2);
        }

        if (facing == 1) {
            world.setBlockMeta(x, y, z, 5);
        }

        if (facing == 2) {
            world.setBlockMeta(x, y, z, 3);
        }

        if (facing == 3) {
            world.setBlockMeta(x, y, z, 4);
        }
    }

    @Override
    public boolean canOutputMechanicalPower() {
        return false;
    }

    @Override
    public boolean canInputMechanicalPower() {
        return true;
    }

    @Override
    public void powerMachine(World world, int x, int y, int z, int side) {
        int blockMeta = world.getBlockMeta(x, y, z);
        if (blockMeta < 6) {
            world.setBlockMeta(x, y, z, blockMeta + 6);
        }
    }

    @Override
    public void unpowerMachine(World world, int x, int y, int z, int side) {
        int blockMeta = world.getBlockMeta(x, y, z);
        if (blockMeta >= 6) {
            world.setBlockMeta(x, y, z, blockMeta - 6);
        }
    }

    @Override
    public boolean isMachinePowered(World world, int x, int y, int z) {
        return world.getBlockMeta(x, y, z) >= 6;
    }
}
