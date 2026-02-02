package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LargeOakTreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(LargeOakTreeFeature.class)
public class LargeOakMixin extends Feature {

    public boolean generate(World arg, Random random, int i, int j, int k) {
        return false;
    }

    @Shadow
    World world;
    @Shadow
    int[] origin = new int[]{0, 0, 0};

    @Inject(method = "canPlace", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private void injected(CallbackInfoReturnable<Boolean> cir) {
        if (!UnsortedUtils.CanPlantGrowOnBlock(world, origin[0], origin[1] - 1, origin[2], Block.SAPLING))
            cir.setReturnValue(false);
    }

}
