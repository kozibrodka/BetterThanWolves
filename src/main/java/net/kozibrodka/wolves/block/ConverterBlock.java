package net.kozibrodka.wolves.block;

import net.kozibrodka.wolves.utils.MechanicalDevice;
import net.minecraft.block.material.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;
import net.newfrontiercraft.nfc.api.TorqueGenerator;

public class ConverterBlock extends LazyBlockTemplate implements MechanicalDevice, TorqueGenerator {
    public ConverterBlock(Identifier identifier, Material material, float hardness, BlockSoundGroup blockSounds) {
        super(identifier, material, hardness, blockSounds);
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
        return side == 0;
    }

    @Override
    public void powerMachine(World world, int x, int y, int z, int side) {
        world.setBlockMeta(x, y, z, 1);
    }

    @Override
    public void unpowerMachine(World world, int x, int y, int z, int side) {
        world.setBlockMeta(x, y, z, 0);
    }

    @Override
    public boolean isMachinePowered(World world, int x, int y, int z) {
        return world.getBlockMeta(x, y, z) >= 1;
    }

    @Override
    public int getTorque(World world, int x, int y, int z) {
        return isMachinePowered(world, x, y, z) ? 10 : 0;
    }
}
