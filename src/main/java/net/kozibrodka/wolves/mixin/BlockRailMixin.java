package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.BlockListener;
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
        tor.setReturnValue(arg.shouldSuffocate(i, j - 1, k) || arg.getBlockId(i, j - 1, k) == BlockListener.hopper.id); //TODO: not enough
    }
}
