package net.kozibrodka.wolves.mixin;

import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Random;


@Mixin(net.minecraft.block.Fire.class)
public interface FireAccessor {

    @Invoker
    void invokeFireTick(Level arg, int i, int j, int k, int l, Random random, int m);

    @Invoker
    int invokeMethod_1827(Level arg, int i, int j, int k);

}

