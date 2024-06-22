package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.ItemListener;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfEntity.class)
public abstract class WolfDump extends AnimalEntity {

    @Shadow
    public abstract boolean method_425(); // isTamed

    public WolfDump(World arg) {
        super(arg);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo callbackInfo)
    {
        int dungBooster = 1;
        if (world.method_252((int)x, (int)y, (int)z) < 5) dungBooster *= 2;
        if (method_425()) dungBooster *= 4;
        if (world.field_214.nextInt(9600) > dungBooster) return;
        int turboDump = 1;
        if (world.field_214.nextInt(1000) == 0) turboDump = world.field_214.nextInt(91) + 10;
        for (; turboDump > 0; turboDump--) {
            float xOffset = -(-MathHelper.sin(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F));
            float zOffset = -(MathHelper.cos(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F));
            float yOffset = 0.25F;
            ItemEntity entityitem = new ItemEntity(this.world, this.x + (double)xOffset, this.y + (double)yOffset, this.z + (double)zOffset, new ItemStack(ItemListener.dung));
            float velocityFactor = 0.05F;
            entityitem.velocityX = (double)((MathHelper.sin(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F)) * 10.0F * velocityFactor) + random.nextFloat() * 0.2F - 0.1F;
            entityitem.velocityY = (double)(-(MathHelper.cos(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F)) * 10.0F * velocityFactor) + random.nextFloat() * 0.2F - 0.1F;
            entityitem.velocityZ = (double)((-MathHelper.cos(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F)) * 10.0F * velocityFactor) + random.nextFloat() * 0.2F - 0.1F;
            entityitem.pickupDelay = 10;
            this.world.method_210(entityitem);
        }
        this.world.playSound(this, "random.explode", 0.2F, 1.25F);
        this.world.playSound(this, "mob.wolf.growl", this.method_915(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }
}