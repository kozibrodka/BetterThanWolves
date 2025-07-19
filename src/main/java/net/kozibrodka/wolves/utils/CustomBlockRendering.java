// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.kozibrodka.wolves.utils;

import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;

/**
 * This util is an illogical amalgamation.
 * TODO: Create separate util for moving blocks. Replace block models with JSON models. Fix item rendering for the JSON models.
 */
public class CustomBlockRendering {

    public CustomBlockRendering() {
    }

    public static void SetOverrideBlockTexture(BlockRenderManager renderblocks, int i) {
        try {
            Field field = (BlockRenderManager.class).getDeclaredFields()[1];
            field.setAccessible(true);
            field.setInt(renderblocks, i);
        } catch (IllegalAccessException illegalaccessexception) {
            throw new RuntimeException(illegalaccessexception);
        } catch (SecurityException securityexception) {
            throw new RuntimeException(securityexception);
        }
    }

    public static int GetOverrideBlockTexture(BlockRenderManager renderblocks) {
        int i = -1;
        try {
            Field field = (BlockRenderManager.class).getDeclaredFields()[1];
            field.setAccessible(true);
            i = field.getInt(renderblocks);
        } catch (IllegalAccessException illegalaccessexception) {
            throw new RuntimeException(illegalaccessexception);
        } catch (SecurityException securityexception) {
            throw new RuntimeException(securityexception);
        }
        return i;
    }

    public static void renderStandardBlockWithTexture(BlockRenderManager renderblocks, Block block, int i, int j, int k, int l) {
        int i1 = GetOverrideBlockTexture(renderblocks);
        if (i1 < 0) {
            SetOverrideBlockTexture(renderblocks, l);
        }
        renderblocks.renderBlock(block, i, j, k);
        if (i1 < 0) {
            SetOverrideBlockTexture(renderblocks, -1);
        }
    }

    public static void RenderMovingBlockWithTexture(BlockRenderManager renderblocks, Block block, World world, int i, int j, int k, int l) {
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        Tessellator tessellator = Tessellator.INSTANCE;
        tessellator.startQuads();
        float f4 = block.getLuminance(world, i, j, k);
        float f5 = block.getLuminance(world, i, j - 1, k);
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.color(f * f5, f * f5, f * f5);
        renderblocks.renderBottomFace(block, -0.5D, -0.5D, -0.5D, l);
        f5 = block.getLuminance(world, i, j + 1, k);
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.color(f1 * f5, f1 * f5, f1 * f5);
        renderblocks.renderTopFace(block, -0.5D, -0.5D, -0.5D, l);
        f5 = block.getLuminance(world, i, j, k - 1);
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.color(f2 * f5, f2 * f5, f2 * f5);
        renderblocks.renderEastFace(block, -0.5D, -0.5D, -0.5D, l);
        f5 = block.getLuminance(world, i, j, k + 1);
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.color(f2 * f5, f2 * f5, f2 * f5);
        renderblocks.renderWestFace(block, -0.5D, -0.5D, -0.5D, l);
        f5 = block.getLuminance(world, i - 1, j, k);
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.color(f3 * f5, f3 * f5, f3 * f5);
        renderblocks.renderNorthFace(block, -0.5D, -0.5D, -0.5D, l);
        f5 = block.getLuminance(world, i + 1, j, k);
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.color(f3 * f5, f3 * f5, f3 * f5);
        renderblocks.renderSouthFace(block, -0.5D, -0.5D, -0.5D, l);
        tessellator.draw();
    }

    public static void RenderMovingBlock(BlockRenderManager renderblocks, Block block, World world, int i, int j, int k) {
        RenderMovingBlockWithMetadata(renderblocks, block, world, i, j, k, 0);
    }

    public static void RenderMovingBlockWithMetadata(BlockRenderManager renderblocks, Block block, World world, int i, int j, int k, int l) {
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        Tessellator tessellator = Tessellator.INSTANCE;
        tessellator.startQuads();
        float f4 = block.getLuminance(world, i, j, k);
        float f5 = block.getLuminance(world, i, j - 1, k);
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.color(f * f5, f * f5, f * f5);
        renderblocks.renderBottomFace(block, -0.5D, -0.5D, -0.5D, block.getTexture(0, l));
//        renderblocks.renderBottomFace(block, -0.5D, -0.5D, -0.5D, TextureListener.platform_side);
        f5 = block.getLuminance(world, i, j + 1, k);
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.color(f1 * f5, f1 * f5, f1 * f5);
        renderblocks.renderTopFace(block, -0.5D, -0.5D, -0.5D, block.getTexture(1, l));
//        renderblocks.renderTopFace(block, -0.5D, -0.5D, -0.5D, TextureListener.platform_side);
        f5 = block.getLuminance(world, i, j, k - 1);
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.color(f2 * f5, f2 * f5, f2 * f5);
        renderblocks.renderEastFace(block, -0.5D, -0.5D, -0.5D, block.getTexture(2, l));
//        FCUtilsRenderMoving.renderEastFace(block, -0.5D, -0.5D, -0.5D, block.getTextureForSide(2, l), renderblocks);
//        renderblocks.renderEastFace(block, -0.5D, -0.5D, -0.5D, TextureListener.platform_side);
        f5 = block.getLuminance(world, i, j, k + 1);
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.color(f2 * f5, f2 * f5, f2 * f5);
        renderblocks.renderWestFace(block, -0.5D, -0.5D, -0.5D, block.getTexture(3, l));
//        renderblocks.renderWestFace(block, -0.5D, -0.5D, -0.5D, TextureListener.platform_side);
        f5 = block.getLuminance(world, i - 1, j, k);
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.color(f3 * f5, f3 * f5, f3 * f5);
        renderblocks.renderNorthFace(block, -0.5D, -0.5D, -0.5D, block.getTexture(4, l));
//        renderblocks.renderNorthFace(block, -0.5D, -0.5D, -0.5D, TextureListener.platform_side);
        f5 = block.getLuminance(world, i + 1, j, k);
        if (f5 < f4) {
            f5 = f4;
        }
        tessellator.color(f3 * f5, f3 * f5, f3 * f5);
        renderblocks.renderSouthFace(block, -0.5D, -0.5D, -0.5D, block.getTexture(5, l));
//        renderblocks.renderSouthFace(block, -0.5D, -0.5D, -0.5D, TextureListener.platform_side);
        tessellator.draw();
    }

    public static void RenderInvBlockWithTexture(BlockRenderManager renderblocks, Block block, float f, float f1, float f2, int i) {
        Tessellator tessellator = Tessellator.INSTANCE;
        GL11.glTranslatef(f, f1, f2);
        tessellator.startQuads();
        tessellator.normal(0.0F, -1F, 0.0F);
        renderblocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, i);
        tessellator.draw();
        tessellator.startQuads();
        tessellator.normal(0.0F, 1.0F, 0.0F);
        renderblocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, i);
        tessellator.draw();
        tessellator.startQuads();
        tessellator.normal(0.0F, 0.0F, -1F);
        renderblocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, i);
        tessellator.draw();
        tessellator.startQuads();
        tessellator.normal(0.0F, 0.0F, 1.0F);
        renderblocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, i);
        tessellator.draw();
        tessellator.startQuads();
        tessellator.normal(-1F, 0.0F, 0.0F);
        renderblocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, i);
        tessellator.draw();
        tessellator.startQuads();
        tessellator.normal(1.0F, 0.0F, 0.0F);
        renderblocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, i);
        tessellator.draw();
        GL11.glTranslatef(-f, -f1, -f2);
    }

    public static void RenderInvBlockWithMetaData(BlockRenderManager renderblocks, Block block, float f, float f1, float f2, int i) {
        Tessellator tessellator = Tessellator.INSTANCE;
        GL11.glTranslatef(f, f1, f2);
        tessellator.startQuads();
        tessellator.normal(0.0F, -1F, 0.0F);
        renderblocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getTexture(0, i));
        tessellator.draw();
        tessellator.startQuads();
        tessellator.normal(0.0F, 1.0F, 0.0F);
        renderblocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getTexture(1, i));
        tessellator.draw();
        tessellator.startQuads();
        tessellator.normal(0.0F, 0.0F, -1F);
        renderblocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getTexture(2, i));
        tessellator.draw();
        tessellator.startQuads();
        tessellator.normal(0.0F, 0.0F, 1.0F);
        renderblocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getTexture(3, i));
        tessellator.draw();
        tessellator.startQuads();
        tessellator.normal(-1F, 0.0F, 0.0F);
        renderblocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getTexture(4, i));
        tessellator.draw();
        tessellator.startQuads();
        tessellator.normal(1.0F, 0.0F, 0.0F);
        renderblocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getTexture(5, i));
        tessellator.draw();
        GL11.glTranslatef(-f, -f1, -f2);
    }
}
