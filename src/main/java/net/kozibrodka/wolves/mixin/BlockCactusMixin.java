package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CactusBlock.class)
public class BlockCactusMixin extends Block{

    protected BlockCactusMixin(int i, Material arg) {
        super(i, arg);
    }

    @Inject(method = "canGrow", at = @At(value = "RETURN", ordinal = 4), cancellable = true)
    private void injected(World arg, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        int var5 = arg.getBlockId(i, j - 1, k);
        cir.setReturnValue(var5 == Block.CACTUS.id || var5 == Block.SAND.id || UnsortedUtils.CanPlantGrowOnBlock(arg, i, j - 1, k, this));
    }
}
