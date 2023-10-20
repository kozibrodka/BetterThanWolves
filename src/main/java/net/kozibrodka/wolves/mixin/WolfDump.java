package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.minecraft.entity.Item;
import net.minecraft.entity.animal.AnimalBase;
import net.minecraft.entity.animal.Wolf;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.maths.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.event.ItemListener;

@Mixin(Wolf.class)
public abstract class WolfDump extends AnimalBase {

    @Shadow
    public abstract boolean isTamed();

    public WolfDump(Level arg) {
        super(arg);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo callbackInfo)
    {
        int dungBooster = 1;
        if (level.getLightLevel((int)x, (int)y, (int)z) < 5) dungBooster *= 2;
        if (isTamed()) dungBooster *= 4;
        if (level.rand.nextInt(9600) > dungBooster) return;
        int turboDump = 1;
        if (level.rand.nextInt(1000) == 0) turboDump = level.rand.nextInt(91) + 10;
        for (; turboDump > 0; turboDump--) {
            float xOffset = -(-MathHelper.sin(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F));
            float zOffset = -(MathHelper.cos(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F));
            float yOffset = 0.25F;
            Item entityitem = new Item(this.level, this.x + (double)xOffset, this.y + (double)yOffset, this.z + (double)zOffset, new ItemInstance(mod_FCBetterThanWolves.fcDung));
            float velocityFactor = 0.05F;
            entityitem.velocityX = (double)(-(-MathHelper.sin(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F)) * 10.0F * velocityFactor);
            entityitem.velocityY = (double)(-(MathHelper.cos(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F)) * 10.0F * velocityFactor);
            entityitem.velocityZ = (double)((float)this.level.rand.nextGaussian() * velocityFactor + 0.2F);
            entityitem.pickupDelay = 10;
            this.level.spawnEntity(entityitem);
        }
        this.level.playSound(this, "random.explode", 0.2F, 1.25F);
        this.level.playSound(this, "mob.wolf.growl", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
    }
}