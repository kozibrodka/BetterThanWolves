package net.kozibrodka.wolves.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.RailBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RailBlock.class)
public class BlockRailMixin {

    @Inject(method = "isRail(Lnet/minecraft/world/World;III)Z", at = @At("RETURN"), cancellable = true)
    private static void injected(World arg, int i, int j, int k, CallbackInfoReturnable<Boolean> tor) {
        int var4 = arg.getBlockId(i, j, k);
        tor.setReturnValue(Block.BLOCKS[var4] instanceof RailBlock);
    }

    @Inject(method = "isRail(I)Z", at = @At("RETURN"), cancellable = true)
    private static void injected2(int blockID, CallbackInfoReturnable<Boolean> tor) {
        tor.setReturnValue(Block.BLOCKS[blockID] instanceof RailBlock);
    }

    @Inject(method = "canPlaceAt", at = @At("RETURN"), cancellable = true)
    private void injected3(World arg, int i, int j, int k, CallbackInfoReturnable<Boolean> tor) {
        tor.setReturnValue(arg.shouldSuffocate(i, j - 1, k) || arg.getBlockId(i, j - 1, k) == BlockListener.hopper.id);
    }

    @WrapOperation(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;shouldSuffocate(III)Z", ordinal = 0))
    public boolean injected5(World instance, int x, int y, int z, Operation<Boolean> original) {
        if (instance.getBlockId(x, y, z) == BlockListener.hopper.id){
            return true;
        }
        return original.call(instance, x, y, z);
    }
    }
