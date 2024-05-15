package net.kozibrodka.wolves.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayNetworkHandler.class)
public interface ClientPlayerAccessor {
    @Accessor("minecraft")
    Minecraft getMinecraft();
}