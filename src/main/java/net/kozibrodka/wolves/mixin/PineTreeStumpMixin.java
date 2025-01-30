package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ConfigListener;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PineTreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(PineTreeFeature.class)
public abstract class PineTreeStumpMixin extends Feature {

    @Inject(at = @At("TAIL"), method = "generate")
    private void generateStump(World world, Random random, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (!ConfigListener.wolvesGlass.difficulty.treeStumps) {
            return;
        }
        if (y < 1 || y + 12 > 128) {
            return;
        }
        world.setBlock(x, y, z, BlockListener.spruceStump.id);
    }
}
