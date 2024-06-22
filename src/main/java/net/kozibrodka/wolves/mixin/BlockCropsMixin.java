package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.block.Block;
import net.minecraft.block.CropBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public class BlockCropsMixin {

//    @Inject(method = "growCropStage", at = @At(value = "INVOKE", target = "Lnet/minecraft/level/Level;getTileMeta(III)I", shift = At.Shift.BY, by = 3), locals = LocalCapture.CAPTURE_FAILHARD)
//    private void injected(Level i, int j, int k, int par4, CallbackInfoReturnable<Float> cir, float f1) {
//
//    }

    @Inject(method = "canPlantOnTop", at = @At(value = "RETURN"), cancellable = true)
    private void injected(int i, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(i == Block.FARMLAND.id || i == BlockListener.planter.id);
    }
}
