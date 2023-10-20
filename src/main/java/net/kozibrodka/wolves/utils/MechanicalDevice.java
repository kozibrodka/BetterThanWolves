
package net.kozibrodka.wolves.utils;

import net.minecraft.level.Level;

public interface MechanicalDevice
{

    boolean CanOutputMechanicalPower();

    boolean CanInputMechanicalPower();

    boolean IsInputtingMechanicalPower(Level world, int i, int j, int k);

    boolean IsOutputtingMechanicalPower(Level world, int i, int j, int k);
}
