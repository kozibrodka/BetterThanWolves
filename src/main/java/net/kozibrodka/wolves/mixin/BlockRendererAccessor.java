package net.kozibrodka.wolves.mixin;

import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockRenderer.class)
public interface BlockRendererAccessor {

    @Accessor
    void setBlockView(BlockView blockView);
}
