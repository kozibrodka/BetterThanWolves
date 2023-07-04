package net.kozibrodka.wolves.mixin;

import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockRenderer.class)
public interface BlockRendererAccessor {

    @Accessor
    void setBlockView(BlockView blockView);

    @Accessor
    boolean getMirrorTexture();

    @Accessor
    int getEastFaceRotation();

    @Accessor
    boolean getShadeTopFace();

    @Accessor
    float getColourRed00();
    @Accessor
    float getColourRed01();
    @Accessor
    float getColourRed10();
    @Accessor
    float getColurRed11();
}
