package net.kozibrodka.wolves.mixin;


import net.kozibrodka.wolves.entity.BroadheadArrowEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.EntityTracker;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin extends PlayerEntity {


    public ServerPlayerEntityMixin(World world) {
        super(world);
    }

    @Inject(method = "sendPickup", at = @At("HEAD"))
    private void injected1(Entity item, int count, CallbackInfo ci) {
        if (!item.dead) {
            EntityTracker var3 = this.server.getEntityTracker(this.dimensionId);
            if (item instanceof BroadheadArrowEntity) {
                var3.sendToListeners(item, new ItemPickupAnimationS2CPacket(item.id, this.id));
            }
        }
    }

    @Shadow  public MinecraftServer server;

    @Override
    public void spawn() {

    }
}
