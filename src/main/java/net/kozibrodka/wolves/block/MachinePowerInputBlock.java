package net.kozibrodka.wolves.block;

import net.kozibrodka.wolves.utils.MechanicalDevice;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class MachinePowerInputBlock extends LazyBlockTemplate implements MechanicalDevice {
    public MachinePowerInputBlock(Identifier identifier, Material material, float hardness, BlockSoundGroup blockSounds) {
        super(identifier, material, hardness, blockSounds);
    }

    @Override
    public int getTexture(int side, int meta) {
        if (meta >= 6) {
            meta -= 6;
        }
        if (side == meta) {
            return topTexture;
        }
        return sideTexture;
    }

    @Override
    public void onPlaced(World world, int x, int y, int z, LivingEntity placer) {
        int var6 = MathHelper.floor((double)(placer.yaw * 4.0F / 360.0F) + (double)0.5F) & 3;
        if (var6 == 0) {
            world.setBlockMeta(x, y, z, 2);
        }

        if (var6 == 1) {
            world.setBlockMeta(x, y, z, 5);
        }

        if (var6 == 2) {
            world.setBlockMeta(x, y, z, 3);
        }

        if (var6 == 3) {
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
    public boolean canInputMechanicalPower(World world, int x, int y, int z, int side) {
        int meta = world.getBlockMeta(x, y, z);
        if (meta >= 6) {
            meta -= 6;
        }
        return side == meta;
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

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (world.getBlockMeta(x, y, z) >= 6) {
            for (int counter = 0; counter < 5; counter++) {
                float smokeX = (float) x + random.nextFloat();
                float smokeY = (float) y + random.nextFloat() * 0.5F + 1.0F;
                float smokeZ = (float) z + random.nextFloat();
                world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
