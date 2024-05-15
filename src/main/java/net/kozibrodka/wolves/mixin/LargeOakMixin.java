package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.structure.LargeOak;
import net.minecraft.level.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(LargeOak.class)
public class LargeOakMixin extends Structure{

    public boolean generate(Level arg, Random random, int i, int j, int k) {
        return false;
    }

    @Shadow
    Level field_647;
    @Shadow
    int[] field_648 = new int[]{0, 0, 0};

    @Inject(method = "method_611", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private void injected(CallbackInfoReturnable<Boolean> cir) {
        if(!UnsortedUtils.CanPlantGrowOnBlock(field_647, field_648[0], field_648[1] - 1, field_648[2], BlockBase.SAPLING))
        cir.setReturnValue(false);
    }

}
