package net.kozibrodka.wolves.utils;

import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public interface RotatableBlock {

    int getFacing(BlockView iblockaccess, int i, int j, int k);

    void setFacing(World world, int i, int j, int k, int l);

    boolean canRotate(BlockView iblockaccess, int i, int j, int k);

    boolean canTransmitRotation(BlockView iblockaccess, int i, int j, int k);

    void rotate(World world, int i, int j, int k, boolean flag);
}
