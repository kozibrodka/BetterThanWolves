package net.kozibrodka.wolves.api;

import net.minecraft.world.World;

/**
 * Preparation for a mechanical power rewrite.
 */
public interface MechanicalPowerConsumer {

    void receivePower(World world, int x, int y, int z, int side);

    void stopReceivingPower(World world, int x, int y, int z, int side);
}
