package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.block.SandBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SandBlock.class)
public class BlockSandMixin {

    @Redirect(
            method = "Lnet/minecraft/block/SandBlock;canFallThrough(Lnet/minecraft/world/World;III)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getBlockId(III)I"
            )
    )
    private static int redirectGetBlockId(World world, int x, int y, int z) {
        int blockId = world.getBlockId(x, y, z);
        if (blockId == BlockListener.beamLight.id) {
            return 0;
        }else{
            return blockId;
        }
    }
}
