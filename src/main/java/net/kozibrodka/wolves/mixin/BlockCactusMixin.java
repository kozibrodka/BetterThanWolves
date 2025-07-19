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
public class BlockCactusMixin extends Block {

    protected BlockCactusMixin(int id, Material material) {
        super(id, material);
    }

    @Inject(method = "canGrow", at = @At(value = "RETURN", ordinal = 4), cancellable = true)
    private void injected(World world, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (UnsortedUtils.CanPlantGrowOnBlock(world, x, y - 1, z, this)) {
            cir.setReturnValue(true);
        }
    }
}
