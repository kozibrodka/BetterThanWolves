package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SeedsItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SeedsItem.class)
public class SeedsItemMixin {

    @Shadow private int cropBlockId;

    @Inject(method = "useOnBlock", at = @At(value = "HEAD"), cancellable = true)
    private void addPlanterCondition(ItemStack itemStack, PlayerEntity playerEntity, World world, int x, int y, int z, int side, CallbackInfoReturnable<Boolean> cir) {
        if (side == 1 && world.getBlockId(x, y, z) == BlockListener.planter.id) {
            world.setBlock(x, y + 1, z, this.cropBlockId);
            --itemStack.count;
            cir.setReturnValue(true);
        }
    }
}
