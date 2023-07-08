package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.utils.FCUtilsMisc;
import net.minecraft.block.BlockBase;
import net.minecraft.block.SugarCane;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SugarCane.class)
public class BlockSugarCaneMixin extends BlockBase {

    public BlockSugarCaneMixin(int i, Material arg) {
        super(i, arg);
    }

    @Inject(at = @At(value = "HEAD"), method = "canPlaceAt", cancellable = true)
    private void injected(Level arg, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        if(FCUtilsMisc.CanPlantGrowOnBlock(arg, i, j - 1, k, this))
        cir.setReturnValue(true);
    }
}
