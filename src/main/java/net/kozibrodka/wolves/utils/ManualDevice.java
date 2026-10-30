package net.kozibrodka.wolves.utils;

import net.minecraft.world.World;

public interface ManualDevice {

    void powerManually(World world, int x, int y, int z);

    void stopPoweringManually(World world, int x, int y, int z);

}
