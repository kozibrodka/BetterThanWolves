package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.BlockBase;
import net.minecraft.block.Cactus;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Cactus.class)
public class BlockCactusMixin extends BlockBase{

    protected BlockCactusMixin(int i, Material arg) {
        super(i, arg);
    }

    @Inject(method = "canGrow", at = @At(value = "RETURN", ordinal = 4), cancellable = true)
    private void injected(Level arg, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        int var5 = arg.getTileId(i, j - 1, k);
        cir.setReturnValue(var5 == BlockBase.CACTUS.id || var5 == BlockBase.SAND.id || UnsortedUtils.CanPlantGrowOnBlock(arg, i, j - 1, k, this));
    }
}
