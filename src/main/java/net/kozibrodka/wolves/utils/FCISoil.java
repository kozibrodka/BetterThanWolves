package net.kozibrodka.wolves.utils;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;


public interface FCISoil
{

    boolean CanPlantGrowOnBlock(Level world, int i, int j, int k, BlockBase block);

    boolean IsBlockHydrated(Level world, int i, int j, int k);

    boolean IsBlockConsideredNeighbouringWater(Level world, int i, int j, int k);
}
