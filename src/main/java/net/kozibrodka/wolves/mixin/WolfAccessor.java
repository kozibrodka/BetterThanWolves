package net.kozibrodka.wolves.mixin;

import net.minecraft.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Wolf.class)
public interface WolfAccessor {
    @Invoker
    String invokeGetHurtSound();

    @Invoker
    float invokeGetSoundVolume();
}
