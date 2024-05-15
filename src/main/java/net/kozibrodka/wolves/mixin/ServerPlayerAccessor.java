package net.kozibrodka.wolves.mixin;

import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.server.network.ServerPlayerPacketHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerPlayerPacketHandler.class)
public interface ServerPlayerAccessor {
    @Accessor("serverPlayer")
    ServerPlayer getServerPlayer();
}
