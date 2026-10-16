package net.kozibrodka.wolves.utils;

import net.minecraft.world.BlockView;

public interface ITorchBox {

    void updateBoundingBoxForBeamRender(BlockView blockView, int x, int y, int z);
}
