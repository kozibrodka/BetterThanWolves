
package net.kozibrodka.wolves.utils;

import net.minecraft.world.World;

public interface MechanicalDevice
{

    boolean CanOutputMechanicalPower();

    boolean CanInputMechanicalPower();

    boolean IsInputtingMechanicalPower(World world, int i, int j, int k);

    boolean IsOutputtingMechanicalPower(World world, int i, int j, int k);
}
