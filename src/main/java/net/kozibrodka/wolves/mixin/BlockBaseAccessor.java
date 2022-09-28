package net.kozibrodka.wolves.mixin;

import net.minecraft.block.BlockBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockBase.class)
public interface BlockBaseAccessor {

    @Invoker
    int invokeDroppedMeta(int meta);
}
