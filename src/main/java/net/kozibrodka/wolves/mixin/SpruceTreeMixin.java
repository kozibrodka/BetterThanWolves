package net.kozibrodka.wolves.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SpruceTreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpruceTreeFeature.class)
public abstract class SpruceTreeMixin extends Feature {

    @WrapOperation(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I", ordinal = 1))
    public int spoofBlockId(World world, int x, int y, int z, Operation<Integer> original) {
        if (UnsortedUtils.CanPlantGrowOnBlock(world, x, y - 1, z, Block.SAPLING)) {
            return Block.DIRT.id;
        }
        return original.call(world, x, y, z);
    }

    @WrapWithCondition(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockWithoutNotifyingNeighbors(IIII)Z", ordinal = 0))
    public boolean wrapDirtCheck(World world, int x, int y, int z, int i) {
        return !world.getBlockState(x, y, z).isOf(BlockListener.planter);
    }
}
