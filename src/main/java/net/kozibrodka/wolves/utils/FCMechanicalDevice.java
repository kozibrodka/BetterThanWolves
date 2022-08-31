
package net.kozibrodka.wolves.utils;

import net.minecraft.level.Level;

public interface FCMechanicalDevice
{

    public abstract boolean CanOutputMechanicalPower();

    public abstract boolean CanInputMechanicalPower();

    public abstract boolean IsInputtingMechanicalPower(Level world, int i, int j, int k);

    public abstract boolean IsOutputtingMechanicalPower(Level world, int i, int j, int k);
}
