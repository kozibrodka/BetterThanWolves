package net.kozibrodka.wolves.mixin;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityBaseAccessor {

    @Accessor("field_1636")
    void setFallDistance(float distance);
}
