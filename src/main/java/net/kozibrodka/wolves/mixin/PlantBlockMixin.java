package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.block.PlantBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlantBlock.class)
public class PlantBlockMixin {

    @Inject(method = "canPlantOnTop", at = @At(value = "HEAD"), cancellable = true)
    private void addPlanterCondition(int id, CallbackInfoReturnable<Boolean> cir) {
        if (id == BlockListener.planter.id) {
            cir.setReturnValue(true);
        }
    }
}
