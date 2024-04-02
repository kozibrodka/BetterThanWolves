package net.kozibrodka.wolves.mixin;

import net.minecraft.entity.passive.WolfEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WolfEntity.class)
public interface WolfAccessor {
    @Invoker("method_912")
    String invokeGetHurtSound();

    @Invoker("method_915")
    float invokeGetSoundVolume();
}
