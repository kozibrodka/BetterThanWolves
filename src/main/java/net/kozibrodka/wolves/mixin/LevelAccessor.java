package net.kozibrodka.wolves.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;


@Mixin(net.minecraft.world.World.class)
public interface LevelAccessor {

    @Invoker
    void invokeBlockUpdate(int i, int j, int k, int l);
}

