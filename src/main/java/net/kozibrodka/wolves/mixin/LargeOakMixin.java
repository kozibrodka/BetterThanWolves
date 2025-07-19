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
    int[] field_648 = new int[]{0, 0, 0};

    @Inject(method = "method_611", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private void injected(CallbackInfoReturnable<Boolean> cir) {
        if (!UnsortedUtils.CanPlantGrowOnBlock(world, field_648[0], field_648[1] - 1, field_648[2], Block.SAPLING))
            cir.setReturnValue(false);
    }

}
