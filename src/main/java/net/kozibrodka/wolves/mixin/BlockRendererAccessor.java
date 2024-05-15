package net.kozibrodka.wolves.mixin;

import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockRenderManager.class)
public interface BlockRendererAccessor {

    @Accessor
    void setBlockView(BlockView blockView);

    @Accessor("flipTextureHorizontally")
    boolean getMirrorTexture();

    @Accessor("eastFaceRotation")
    int getEastFaceRotation();

    @Accessor("textureOverride")
    int getTextureOverride();

    @Accessor("useAo")
    boolean getShadeTopFace();

    @Accessor("firstVertexRed")
    float getColourRed00();
    @Accessor("secondVertexRed")
    float getColourRed01();
    @Accessor("fourthVertexRed")
    float getColourRed10();
    @Accessor("thirdVertexRed")
    float getColurRed11();
}
