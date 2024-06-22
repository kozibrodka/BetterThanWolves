package net.kozibrodka.wolves.api;

import net.kozibrodka.wolves.utils.BlockPosition;
import net.minecraft.world.World;

public interface AffectedByBellows {
    void affectBlock(World world, int i, int j, int k, BlockPosition tempTargetPos, int facing);
}