package net.kozibrodka.wolves.utils;

import net.minecraft.world.World;

public interface MechanicalDevice {

    boolean canOutputMechanicalPower();

    boolean canInputMechanicalPower();

    default boolean canInputMechanicalPower(World world, int x, int y, int z, int side) {
        return canInputMechanicalPower();
    }

    default boolean isOutputtingMechanicalPower(World world, int x, int y, int z, int side) {
        return canOutputMechanicalPower();
    }

    void powerMachine(World world, int x, int y, int z);

    void unpowerMachine(World world, int x, int y, int z);

    boolean isMachinePowered(World world, int x, int y, int z);
}
