package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.BlockBase;
import net.minecraft.block.Plant;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Plant.class)
public class BlockPlantMixin extends BlockBase {

    @Shadow
    protected boolean canPlantOnTopOf(int i) {
        return false;
    }

    public BlockPlantMixin(int i, Material arg) {super(i, arg);}

    @Inject(method = "canPlaceAt", at = @At(value = "RETURN"), cancellable = true)
    private void injected(Level arg, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(super.canPlaceAt(arg, i, j, k) && (this.canPlantOnTopOf2(arg.getTileId(i, j - 1, k)) || UnsortedUtils.CanPlantGrowOnBlock(arg, i, j - 1, k, this)));
    }

    @Inject(method = "canPlantOnTopOf", at = @At(value = "RETURN"), cancellable = true)
    private void injected2(int i, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(i == BlockBase.GRASS.id || i == BlockBase.DIRT.id || i == BlockBase.FARMLAND.id || i == BlockListener.fcPlanter.id);
    }

    private boolean canPlantOnTopOf2(int i) {
        return i == BlockBase.GRASS.id || i == BlockBase.DIRT.id || i == BlockBase.FARMLAND.id;
    }


}
