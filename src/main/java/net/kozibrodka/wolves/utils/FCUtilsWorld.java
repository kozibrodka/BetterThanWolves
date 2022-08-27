package net.kozibrodka.wolves.utils;


import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.vanillafix.block.Blocks;

public class FCUtilsWorld
{

    public FCUtilsWorld()
    {
    }

    public static boolean IsReplaceableBlock(Level world, int i, int j, int k)
    {
        int l = world.getTileId(i, j, k);
        BlockBase block = BlockBase.BY_ID[l];
        return l <= 0 || l == Blocks.FLOWING_WATER.id || l == Blocks.WATER.id || l == Blocks.FLOWING_LAVA.id || l == Blocks.LAVA.id || l == Blocks.FIRE.id || l == Blocks.SNOW.id;
//                || block.isBlockReplaceable(world, i, j, k);
        /**
         * isBlockReplaceable is forge method?
         */

    }
}
