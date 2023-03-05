package net.kozibrodka.wolves.utils;


import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;


public class FCUtilsWorld
{

    public FCUtilsWorld()
    {
    }

    public static boolean IsReplaceableBlock(Level world, int i, int j, int k)
    {
        int l = world.getTileId(i, j, k);
        BlockBase block = BlockBase.BY_ID[l];
        return l <= 0 || l == BlockBase.FLOWING_WATER.id || l == BlockBase.STILL_WATER.id || l == BlockBase.FLOWING_LAVA.id || l == BlockBase.STILL_LAVA.id || l == BlockBase.FIRE.id || l == BlockBase.SNOW.id;
//                || block.isBlockReplaceable(world, i, j, k);
        /**
         * isBlockReplaceable is forge method?
         */

    }
}
