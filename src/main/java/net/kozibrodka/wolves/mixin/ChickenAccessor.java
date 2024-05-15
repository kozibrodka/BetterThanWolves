package net.kozibrodka.wolves.mixin;

import net.minecraft.entity.passive.ChickenEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChickenEntity.class)
public interface ChickenAccessor {
    @Invoker("method_912")
    String invokeGetHurtSound();

}
