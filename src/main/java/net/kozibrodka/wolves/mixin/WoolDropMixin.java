package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.ConfigListener;
import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SheepEntity.class)
public abstract class WoolDropMixin extends AnimalEntity {
    @Shadow public abstract boolean isSheared();

    @Shadow public abstract int getColor();

    @Shadow public abstract void setSheared(boolean sheared);

    public WoolDropMixin(World world) {
        super(world);
    }

    @Inject(at = @At("HEAD"), method = "drop", cancellable = true)
    private void drop(CallbackInfo callbackInfo) {
        if (!ConfigListener.wolvesGlass.difficulty.woolKnitting) {
            return;
        }
        if (!isSheared()) {
            this.dropItem(new ItemStack(Block.LOG, 1), 0.0F);
        }
        callbackInfo.cancel();
    }

    @Inject(at = @At("HEAD"), method = "interact", cancellable = true)
    private void interact(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (!ConfigListener.wolvesGlass.difficulty.woolKnitting) {
            return;
        }
        ItemStack var2 = player.inventory.getSelectedItem();
        if (var2 != null && var2.itemId == Item.SHEARS.id && !this.isSheared()) {
            if (!this.world.isRemote) {
                setSheared(true);
                int var3 = 2 + this.random.nextInt(3);

                for(int var4 = 0; var4 < var3; ++var4) {
                    ItemEntity var5 = this.dropItem(new ItemStack(Block.LOG, 1), 1.0F);
                    var5.velocityY += this.random.nextFloat() * 0.05F;
                    var5.velocityX += (this.random.nextFloat() - this.random.nextFloat()) * 0.1F;
                    var5.velocityZ += (this.random.nextFloat() - this.random.nextFloat()) * 0.1F;
                }
            }

            var2.damage(1, player);
        }
        cir.setReturnValue(false);
    }
}
