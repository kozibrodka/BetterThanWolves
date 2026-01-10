package net.kozibrodka.wolves.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MushroomPlantBlock.class)
public abstract class BlockMushroomMixin extends PlantBlock {
    @Shadow
    protected abstract boolean canPlantOnTop(int id);

    public BlockMushroomMixin(int i, int j) {
        super(i, j);
    }

//    @Inject(method = "canGrow", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
//    private void injected(World arg, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
//        cir.setReturnValue(arg.getBrightness(x, y, z) < 13 && (this.canPlantOnTop(arg.getBlockId(x, y - 1, z)) || UnsortedUtils.CanPlantGrowOnBlock(arg, x, y - 1, z, this)));
//    }

    @WrapOperation(method = "canGrow", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/MushroomPlantBlock;canPlantOnTop(I)Z"))
    public boolean allowGrowth(MushroomPlantBlock block, int id, Operation<Boolean> original, @Local(ordinal = 0, argsOnly = true) World world, @Local(ordinal = 0, argsOnly = true) int x, @Local(ordinal = 1, argsOnly = true) int y, @Local(ordinal = 2, argsOnly = true) int z) {
        if (UnsortedUtils.CanPlantGrowOnBlock(world, x, y - 1, z, this)) {
            return true;
        }
        return original.call(block, id);
    }


}
