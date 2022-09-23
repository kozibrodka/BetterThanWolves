package net.kozibrodka.wolves.mixin;

import net.minecraft.entity.EntityBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityBase.class)
public interface EntityBaseAccessor {

    @Accessor
    void setFallDistance(float distance);
}
