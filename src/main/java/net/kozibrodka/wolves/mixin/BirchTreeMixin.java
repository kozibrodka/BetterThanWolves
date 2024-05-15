package net.kozibrodka.wolves.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.BirchTreeFeature;
import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(BirchTreeFeature.class)
public abstract class BirchTreeMixin extends Feature {
    @Redirect(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I", ordinal = 1))
    public int spoofBlockId(World world, int x, int y, int z) {
        if (UnsortedUtils.CanPlantGrowOnBlock(world, x, y - 1, z, Block.SAPLING)) {
            return Block.DIRT.id;
        }
        return world.getBlockId(x, y - 1, z);
    }

    @WrapWithCondition(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_200(IIII)Z", ordinal = 0))
    public boolean wrapDirtCheck(World world, int x, int y, int z, int i) {
        return !world.getBlockState(x,y,z).isOf(BlockListener.planter);
    }
}
