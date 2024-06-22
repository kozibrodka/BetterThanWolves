package net.kozibrodka.wolves.utils;

import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public interface RotatableBlock
{

    int GetFacing(BlockView iblockaccess, int i, int j, int k);

    void SetFacing(World world, int i, int j, int k, int l);

    boolean CanRotate(BlockView iblockaccess, int i, int j, int k);

    boolean CanTransmitRotation(BlockView iblockaccess, int i, int j, int k);

    void Rotate(World world, int i, int j, int k, boolean flag);
}
