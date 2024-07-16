package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlantBlock.class)
public class BlockPlantMixin extends Block {

    @Shadow
    protected boolean canPlantOnTop(int i) {
        return false;
    }

    public BlockPlantMixin(int i, Material arg) {super(i, arg);}

    @Inject(method = "canPlaceAt", at = @At(value = "RETURN"), cancellable = true)
    private void injected(World arg, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(super.canPlaceAt(arg, i, j, k) && (this.canPlantOnTopOf2(arg.getBlockId(i, j - 1, k)) || UnsortedUtils.CanPlantGrowOnBlock(arg, i, j - 1, k, this)));
    }

    @Inject(method = "canPlantOnTop", at = @At(value = "RETURN"), cancellable = true)
    private void injected2(int i, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(i == Block.GRASS_BLOCK.id || i == Block.DIRT.id || i == Block.FARMLAND.id || i == BlockListener.planter.id);
    }

    private boolean canPlantOnTopOf2(int i) {
        return i == Block.GRASS_BLOCK.id || i == Block.DIRT.id || i == Block.FARMLAND.id;
    }


}
