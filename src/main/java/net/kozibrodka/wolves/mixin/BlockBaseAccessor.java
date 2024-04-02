package net.kozibrodka.wolves.mixin;

import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Block.class)
public interface BlockBaseAccessor {

    @Invoker("getDroppedItemMeta")
    int invokeDroppedMeta(int meta);

    @Accessor
    float getResistance();
}
