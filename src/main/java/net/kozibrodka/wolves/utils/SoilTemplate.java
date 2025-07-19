package net.kozibrodka.wolves.utils;

import net.minecraft.block.Block;
import net.minecraft.world.World;


public interface SoilTemplate {

    boolean CanPlantGrowOnBlock(World world, int i, int j, int k, Block block);

    boolean IsBlockHydrated(World world, int i, int j, int k);

    boolean IsBlockConsideredNeighbouringWater(World world, int i, int j, int k);
}
