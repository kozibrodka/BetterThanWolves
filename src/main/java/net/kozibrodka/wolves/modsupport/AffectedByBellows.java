package net.kozibrodka.wolves.modsupport;

import net.kozibrodka.wolves.utils.BlockPosition;
import net.minecraft.level.Level;

public interface AffectedByBellows {
    void affectBlock(Level world, int i, int j, int k, BlockPosition tempTargetPos, int facing);
}
