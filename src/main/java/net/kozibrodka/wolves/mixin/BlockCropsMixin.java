package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.block.CropBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public class BlockCropsMixin {
    @Inject(method = "canPlantOnTop", at = @At(value = "RETURN"), cancellable = true)
    private void injected(int id, CallbackInfoReturnable<Boolean> cir) {
        if (id == BlockListener.planter.id) {
            cir.setReturnValue(true);
        }
    }
}
