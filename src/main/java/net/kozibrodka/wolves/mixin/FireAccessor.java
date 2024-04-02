package net.kozibrodka.wolves.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Random;
import net.minecraft.world.World;


@Mixin(net.minecraft.block.FireBlock.class)
public interface FireAccessor {

    @Invoker("method_1823")
    void invokeFireTick(World arg, int i, int j, int k, int l, Random random, int m);

    @Invoker
    int invokeMethod_1827(World arg, int i, int j, int k);

}

