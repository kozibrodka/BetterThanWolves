package net.kozibrodka.wolves.utils;


import net.minecraft.block.Block;
import net.minecraft.world.World;

/**
 * This util seems unnecessary and is very limited in its capabilities. Perhaps replace this with a Station API solution?
 */
public class ReplaceableBlockChecker
{

    public ReplaceableBlockChecker()
    {
    }

    public static boolean IsReplaceableBlock(World world, int i, int j, int k)
    {
        int l = world.getBlockId(i, j, k);
        Block block = Block.BLOCKS[l];
        return l <= 0 || l == Block.FLOWING_WATER.id || l == Block.WATER.id || l == Block.FLOWING_LAVA.id || l == Block.LAVA.id || l == Block.FIRE.id || l == Block.SNOW.id;
    }
}
