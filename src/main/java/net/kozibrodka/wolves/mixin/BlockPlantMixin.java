package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlantBlock.class)
public abstract class BlockPlantMixin extends Block {
    public BlockPlantMixin(int i, Material arg) {
        super(i, arg);
    }

    @Inject(method = "canPlaceAt", at = @At(value = "RETURN"), cancellable = true)
    private void injected(World world, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (UnsortedUtils.CanPlantGrowOnBlock(world, x, y - 1, z, this)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "canPlantOnTop", at = @At(value = "RETURN"), cancellable = true)
    private void injected2(int id, CallbackInfoReturnable<Boolean> cir) {
        if (id == BlockListener.planter.id) {
            cir.setReturnValue(true);
        }
    }
}
