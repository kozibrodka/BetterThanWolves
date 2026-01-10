package net.kozibrodka.wolves.utils;

import net.minecraft.world.World;

public interface MechanicalDevice {

    boolean canOutputMechanicalPower();

    boolean canInputMechanicalPower();

    boolean isInputtingMechanicalPower(World world, int i, int j, int k);

    boolean isOutputtingMechanicalPower(World world, int i, int j, int k);
}
