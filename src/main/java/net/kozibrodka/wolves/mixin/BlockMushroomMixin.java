package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MushroomPlantBlock.class)
public abstract class BlockMushroomMixin extends PlantBlock {
    @Shadow protected abstract boolean canPlantOnTop(int id);

    public BlockMushroomMixin(int i, int j) {
        super(i, j);
    }

    @Inject(method = "canGrow", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private void injected(World arg, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(arg.getBrightness(i, j, k) < 13 && (this.canPlantOnTop(arg.getBlockId(i, j - 1, k)) || UnsortedUtils.CanPlantGrowOnBlock(arg, i, j - 1, k, this)));
    }
}
