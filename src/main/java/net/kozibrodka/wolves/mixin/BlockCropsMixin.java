package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.utils.FCUtilsMisc;
import net.minecraft.block.BlockBase;
import net.minecraft.block.Crops;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Crops.class)
public class BlockCropsMixin {

    @Inject(method = "growCropStage", at = @At(value = "INVOKE", target = "Lnet/minecraft/level/Level;getTileMeta(III)I", shift = At.Shift.BY, by = 3), locals = LocalCapture.CAPTURE_FAILHARD)
    private void injected(Level i, int j, int k, int par4, CallbackInfoReturnable<Float> cir, float f1) {

    }
}
