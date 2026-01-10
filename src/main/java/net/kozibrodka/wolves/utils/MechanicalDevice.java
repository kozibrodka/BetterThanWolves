package net.kozibrodka.wolves.utils;

import net.minecraft.world.World;

public interface MechanicalDevice {

    boolean canOutputMechanicalPower();

    boolean canInputMechanicalPower();

    default boolean canInputMechanicalPower(World world, int x, int y, int z, int side) {
        return canInputMechanicalPower();
    }

    boolean isInputtingMechanicalPower(World world, int i, int j, int k);

    boolean isOutputtingMechanicalPower(World world, int i, int j, int k);

    default void powerMachine(World world, int x, int y, int z) {
        System.out.println("Powering machine");
    }

    default void unpowerMachine(World world, int x, int y, int z) {
        System.out.println("Unpowering machine");
    }
}
