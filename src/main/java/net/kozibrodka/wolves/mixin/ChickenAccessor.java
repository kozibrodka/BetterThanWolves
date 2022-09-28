package net.kozibrodka.wolves.mixin;

import net.minecraft.entity.animal.Chicken;
import net.minecraft.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Chicken.class)
public interface ChickenAccessor {
    @Invoker
    String invokeGetHurtSound();

}
