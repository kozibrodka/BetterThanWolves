package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.ConfigListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WolfEntity.class)
public abstract class WolfDump extends AnimalEntity {

    @Unique
    private int foodCounter;

    @Shadow
    public abstract boolean isTamed();

    public WolfDump(World arg) {
        super(arg);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo callbackInfo) {
        if (ConfigListener.wolvesGlass.small_tweaks.deactivateDung) {
            foodCounter = 0;
            return;
        }
        if (foodCounter < 1) return;
        int dungBooster = 1;
        if (world.getBrightness((int)x, (int)y, (int)z) < 5) dungBooster *= 2;
        if (isTamed()) dungBooster *= 4;
        if (world.random.nextInt(9600) > dungBooster) return;
        foodCounter--;
        int turboDump = 1;
        if (world.random.nextInt(1000) == 0) turboDump = world.random.nextInt(91) + 10;
        for (; turboDump > 0; turboDump--) {
            float xOffset = -(-MathHelper.sin(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F));
            float zOffset = -(MathHelper.cos(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F));
            float yOffset = 0.25F;
            ItemEntity itemEntity = new ItemEntity(this.world, this.x + (double)xOffset, this.y + (double)yOffset, this.z + (double)zOffset, new ItemStack(ItemListener.dung));
            float velocityFactor = 0.05F;
            itemEntity.velocityX = (double)((MathHelper.sin(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F)) * 10.0F * velocityFactor) + random.nextFloat() * 0.2F - 0.1F;
            itemEntity.velocityY = (double)(-(MathHelper.cos(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F)) * 10.0F * velocityFactor) + random.nextFloat() * 0.2F - 0.1F;
            itemEntity.velocityZ = (double)((-MathHelper.cos(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F)) * 10.0F * velocityFactor) + random.nextFloat() * 0.2F - 0.1F;
            itemEntity.pickupDelay = 10;
            this.world.spawnEntity(itemEntity);
        }
        this.world.playSound(this, "random.explode", 0.2F, 1.25F);
        this.world.playSound(this, "mob.wolf.growl", this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }

    @Inject(at = @At("HEAD"), method = "interact")
    private void detectFeeding(PlayerEntity playerEntity, CallbackInfoReturnable<Boolean> cir) {
        ItemStack var2 = playerEntity.inventory.getSelectedItem();
        if (this.isTamed()) {
            if (var2 != null && Item.ITEMS[var2.itemId] instanceof FoodItem var3) {
                if (var3.isMeat() && this.dataTracker.getInt(18) == 20) {
                    --var2.count;
                    if (var2.count <= 0) {
                        playerEntity.inventory.setStack(playerEntity.inventory.selectedSlot, null);
                    }
                    foodCounter++;
                }
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "readNbt")
    private void readFoodCounter(NbtCompound nbtCompound, CallbackInfo callbackInfo) {
        foodCounter = nbtCompound.getInt("FoodCounter");
    }

    @Inject(at = @At("TAIL"), method = "writeNbt")
    private void writeFoodCounter(NbtCompound nbtCompound, CallbackInfo callbackInfo) {
        nbtCompound.putInt("FoodCounter", foodCounter);
    }
}