package net.kozibrodka.wolves.client.model.block;

import net.kozibrodka.wolves.block.AxleBlock;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.model.block.BlockModelPredicateProvider;
import org.jetbrains.annotations.Nullable;

public class AxleBlockModelPredicateProvider implements BlockModelPredicateProvider {
    @Override
    public float call(BlockState blockState, @Nullable BlockView blockView, @Nullable BlockPos blockPos, int i) {
        Block block = Block.BLOCKS[blockView.getBlockId(blockPos.x, blockPos.y, blockPos.z)];
        if(block instanceof AxleBlock axleBlock)
        {
            int state = axleBlock.getAxisAlignment(blockView, blockPos.x, blockPos.y, blockPos.z);
            if(axleBlock.getPowerLevel(blockView, blockPos.x, blockPos.y, blockPos.z) > 0){
                state += 3;
            }
            return state;
        }
        return 0;
    }
}
