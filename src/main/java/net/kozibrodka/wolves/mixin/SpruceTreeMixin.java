package net.kozibrodka.wolves.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.kozibrodka.wolves.blocks.Planter;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.structure.SpruceTree;
import net.minecraft.level.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(SpruceTree.class)
public abstract class SpruceTreeMixin extends Structure {

    @Redirect(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/level/Level;getTileId(III)I", ordinal = 1))
    public int spoofBlockId(Level world, int x, int y, int z) {
        if (UnsortedUtils.CanPlantGrowOnBlock(world, x, y - 1, z, BlockBase.SAPLING)) {
            return BlockBase.DIRT.id;
        }
        return world.getTileId(x, y - 1, z);
    }

    @WrapWithCondition(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/level/Level;setTileInChunk(IIII)Z", ordinal = 0))
    public boolean wrapDirtCheck(Level world, int x, int y, int z, int i) {
        return !world.getBlockState(x,y,z).isOf(BlockListener.planter);
    }
}
