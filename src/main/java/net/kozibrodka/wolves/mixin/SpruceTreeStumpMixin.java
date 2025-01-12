package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.ConfigListener;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SpruceTreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(SpruceTreeFeature.class)
public abstract class SpruceTreeStumpMixin extends Feature {

    @Inject(at = @At("TAIL"), method = "generate")
    private void generateStump(World world, Random random, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (!ConfigListener.wolvesGlass.difficulty.treeStumps) {
            return;
        }
        if (y < 1 || y + 10 > 128) {
            return;
        }
        world.setBlock(x, y, z, Block.WOOL.id, 1);
    }
}
