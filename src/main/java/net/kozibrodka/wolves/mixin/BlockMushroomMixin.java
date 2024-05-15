package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Mushroom;
import net.minecraft.block.Plant;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mushroom.class)
public class BlockMushroomMixin extends Plant {
    public BlockMushroomMixin(int i, int j) {
        super(i, j);
    }

    @Inject(method = "canGrow", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private void injected(Level arg, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(arg.getLightLevel(i, j, k) < 13 && (this.canPlantOnTopOf(arg.getTileId(i, j - 1, k)) || UnsortedUtils.CanPlantGrowOnBlock(arg, i, j - 1, k, this)));
    }
}
