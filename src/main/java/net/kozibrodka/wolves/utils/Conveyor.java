package net.kozibrodka.wolves.utils;

import net.minecraft.world.World;

public interface Conveyor {
    int getPowerSourceDistance(World world, int x, int y, int z, int distance);
}
