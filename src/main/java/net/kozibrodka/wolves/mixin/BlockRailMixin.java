package net.kozibrodka.wolves.mixin;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Rail;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Rail.class)
public class BlockRailMixin {

    @Inject(method = "method_1109", at = @At("RETURN"), cancellable = true)
    private static void injected(Level arg, int i, int j, int k, CallbackInfoReturnable<Boolean> tor) {
        int var4 = arg.getTileId(i, j, k);
        tor.setReturnValue(BlockBase.BY_ID[var4] instanceof Rail);
    }

    @Inject(method = "isRail", at = @At("RETURN"), cancellable = true)
    private static void injected(int blockID, CallbackInfoReturnable<Boolean> tor) {
        tor.setReturnValue(BlockBase.BY_ID[blockID] instanceof Rail);
    }
}
