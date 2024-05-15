package net.kozibrodka.wolves.utils;

import net.minecraft.level.BlockView;
import net.minecraft.level.Level;

public interface RotatableBlock
{

    int GetFacing(BlockView iblockaccess, int i, int j, int k);

    void SetFacing(Level world, int i, int j, int k, int l);

    boolean CanRotate(BlockView iblockaccess, int i, int j, int k);

    boolean CanTransmitRotation(BlockView iblockaccess, int i, int j, int k);

    void Rotate(Level world, int i, int j, int k, boolean flag);
}
