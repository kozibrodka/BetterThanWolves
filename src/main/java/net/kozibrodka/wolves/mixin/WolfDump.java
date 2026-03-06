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
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(WolfEntity.class)
public abstract class WolfDump extends AnimalEntity {

    @Unique
    private int foodCounter;
    @Unique
    private boolean ateBadFood = false;
    @Unique
    private int foodCheckCooldown;

    @Shadow
    public abstract boolean isTamed();

    @Shadow
    public abstract void setAngry(boolean angry);

    @Shadow
    public abstract boolean isInSittingPose();

    @Shadow
    public abstract void setSitting(boolean inSittingPose);

    @Shadow
    public abstract boolean isAngry();

    @Shadow
    public abstract void setTamed(boolean tamed);

    public WolfDump(World arg) {
        super(arg);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo callbackInfo) {
        if (ateBadFood) {
            if (isInSittingPose()) {
                setSitting(false);
            }
            setAngry(true);
            if (isTamed()) {
                setTamed(false);
            }
        }
        if (ConfigListener.wolvesGlass.small_tweaks.deactivateDung) {
            foodCounter = 0;
            return;
        }
        if (foodCheckCooldown <= 0) {
            locateFood();
            foodCheckCooldown = 20;
        } else {
            foodCheckCooldown--;
        }
        if (foodCounter < 1) return;
        int dungBooster = 1;
        if (world.getBrightness((int) x, (int) y, (int) z) < 5) dungBooster *= 2;
        if (isTamed()) dungBooster *= 4;
        if (world.random.nextInt(9600) > dungBooster) return;
        foodCounter--;
        int turboDump = 1;
        if (world.random.nextInt(1000) == 0) turboDump = world.random.nextInt(91) + 10;
        for (; turboDump > 0; turboDump--) {
            float xOffset = -(-MathHelper.sin(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F));
            float zOffset = -(MathHelper.cos(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F));
            float yOffset = 0.25F;
            ItemEntity itemEntity = new ItemEntity(this.world, this.x + (double) xOffset, this.y + (double) yOffset, this.z + (double) zOffset, new ItemStack(ItemListener.dung));
            float velocityFactor = 0.05F;
            itemEntity.velocityX = (double) ((MathHelper.sin(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F)) * 10.0F * velocityFactor) + random.nextFloat() * 0.2F - 0.1F;
            itemEntity.velocityY = (double) (-(MathHelper.cos(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F)) * 10.0F * velocityFactor) + random.nextFloat() * 0.2F - 0.1F;
            itemEntity.velocityZ = (double) ((-MathHelper.cos(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F)) * 10.0F * velocityFactor) + random.nextFloat() * 0.2F - 0.1F;
            itemEntity.pickupDelay = 10;
            this.world.spawnEntity(itemEntity);
        }
        this.world.playSound(this, "random.explode", 0.2F, 1.25F);
        this.world.playSound(this, "mob.wolf.growl", this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }

    @Inject(at = @At("HEAD"), method = "interact")
    private void detectFeeding(PlayerEntity playerEntity, CallbackInfoReturnable<Boolean> cir) {
        ItemStack selectedItemStack = playerEntity.inventory.getSelectedItem();
        if (this.isTamed()) {
            if (selectedItemStack != null) {
                Item selectedItem = selectedItemStack.getItem();
                if (selectedItem != null && selectedItem == ItemListener.wolfRaw) {
                    --selectedItemStack.count;
                    if (selectedItemStack.count <= 0) {
                        playerEntity.inventory.setStack(playerEntity.inventory.selectedSlot, null);
                    }
                    ateBadFood = true;
                } else if (selectedItem instanceof FoodItem foodItem) {
                    if (foodItem.isMeat() && this.dataTracker.getInt(18) == 20) {
                        --selectedItemStack.count;
                        if (selectedItemStack.count <= 0) {
                            playerEntity.inventory.setStack(playerEntity.inventory.selectedSlot, null);
                        }
                        foodCounter++;
                    }
                }
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "readNbt")
    private void readFoodCounter(NbtCompound nbtCompound, CallbackInfo callbackInfo) {
        foodCounter = nbtCompound.getInt("FoodCounter");
        ateBadFood = nbtCompound.getBoolean("AteBadFood");
    }

    @Inject(at = @At("TAIL"), method = "writeNbt")
    private void writeFoodCounter(NbtCompound nbtCompound, CallbackInfo callbackInfo) {
        nbtCompound.putInt("FoodCounter", foodCounter);
        nbtCompound.putBoolean("AteBadFood", ateBadFood);
    }

    @Unique
    private void locateFood() {
        List list = world.collectEntitiesByClass(ItemEntity.class, Box.createCached(x - 2.5D, y - 1.0D, z - 2.5D, x + 2.5D, y + 1.0D, z + 2.5D));
        if(!list.isEmpty()) {
            for (Object o : list) {
                ItemEntity itemEntity = (ItemEntity) o;
                if (itemEntity.pickupDelay != 0) {
                    continue;
                }
                int itemId = itemEntity.stack.itemId;
                Item item = Item.ITEMS[itemId];
                if ((item instanceof FoodItem) && ((FoodItem) item).isMeat()) {
                    world.playSound(this, "random.pop", 0.25F, ((world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    itemEntity.markDead();
                    heal(((FoodItem) Item.RAW_PORKCHOP).getHealthRestored());
                    foodCounter++;
                }
            }
        }
    }
}