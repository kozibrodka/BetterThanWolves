// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.kozibrodka.wolves.utils;

import java.lang.reflect.Field;

import net.kozibrodka.wolves.events.TextureListener;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.Level;
import org.lwjgl.opengl.GL11;

/**
 * This util is an illogical amalgamation.
 * TODO: Create separate util for moving blocks. Replace block models with JSON models. Fix item rendering for the JSON models.
 */
public class CustomBlockRendering
{

    public CustomBlockRendering()
    {
    }

    public static void SetOverrideBlockTexture(BlockRenderer renderblocks, int i)
    {
        try
        {
            Field field = (BlockRenderer.class).getDeclaredFields()[1];
            field.setAccessible(true);
            field.setInt(renderblocks, i);
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            throw new RuntimeException(illegalaccessexception);
        }
        catch(SecurityException securityexception)
        {
            throw new RuntimeException(securityexception);
        }
    }

    public static int GetOverrideBlockTexture(BlockRenderer renderblocks)
    {
        int i = -1;
        try
        {
            Field field = (BlockRenderer.class).getDeclaredFields()[1];
            field.setAccessible(true);
            i = field.getInt(renderblocks);
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            throw new RuntimeException(illegalaccessexception);
        }
        catch(SecurityException securityexception)
        {
            throw new RuntimeException(securityexception);
        }
        return i;
    }

    public static void RenderStandardBlockWithTexture(BlockRenderer renderblocks, BlockBase block, int i, int j, int k, int l)
    {
        int i1 = GetOverrideBlockTexture(renderblocks);
        if(i1 < 0)
        {
            SetOverrideBlockTexture(renderblocks, l);
        }
        renderblocks.renderStandardBlock(block, i, j, k);
        if(i1 < 0)
        {
            SetOverrideBlockTexture(renderblocks, -1);
        }
    }

    public static void RenderMovingBlockWithTexture(BlockRenderer renderblocks, BlockBase block, Level world, int i, int j, int k, int l)
    {
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        Tessellator tessellator = Tessellator.INSTANCE;
        tessellator.start();
        float f4 = block.getBrightness(world, i, j, k);
        float f5 = block.getBrightness(world, i, j - 1, k);
        if(f5 < f4)
        {
            f5 = f4;
        }
        tessellator.colour(f * f5, f * f5, f * f5);
        renderblocks.renderBottomFace(block, -0.5D, -0.5D, -0.5D, l);
        f5 = block.getBrightness(world, i, j + 1, k);
        if(f5 < f4)
        {
            f5 = f4;
        }
        tessellator.colour(f1 * f5, f1 * f5, f1 * f5);
        renderblocks.renderTopFace(block, -0.5D, -0.5D, -0.5D, l);
        f5 = block.getBrightness(world, i, j, k - 1);
        if(f5 < f4)
        {
            f5 = f4;
        }
        tessellator.colour(f2 * f5, f2 * f5, f2 * f5);
        renderblocks.renderEastFace(block, -0.5D, -0.5D, -0.5D, l);
        f5 = block.getBrightness(world, i, j, k + 1);
        if(f5 < f4)
        {
            f5 = f4;
        }
        tessellator.colour(f2 * f5, f2 * f5, f2 * f5);
        renderblocks.renderWestFace(block, -0.5D, -0.5D, -0.5D, l);
        f5 = block.getBrightness(world, i - 1, j, k);
        if(f5 < f4)
        {
            f5 = f4;
        }
        tessellator.colour(f3 * f5, f3 * f5, f3 * f5);
        renderblocks.renderNorthFace(block, -0.5D, -0.5D, -0.5D, l);
        f5 = block.getBrightness(world, i + 1, j, k);
        if(f5 < f4)
        {
            f5 = f4;
        }
        tessellator.colour(f3 * f5, f3 * f5, f3 * f5);
        renderblocks.renderSouthFace(block, -0.5D, -0.5D, -0.5D, l);
        tessellator.draw();
    }

    public static void RenderMovingBlock(BlockRenderer renderblocks, BlockBase block, Level world, int i, int j, int k)
    {
        RenderMovingBlockWithMetadata(renderblocks, block, world, i, j, k, 0);
    }

    public static void RenderMovingBlockWithMetadata(BlockRenderer renderblocks, BlockBase block, Level world, int i, int j, int k, int l)
    {
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        Tessellator tessellator = Tessellator.INSTANCE;
        tessellator.start();
        float f4 = block.getBrightness(world, i, j, k);
        float f5 = block.getBrightness(world, i, j - 1, k);
        if(f5 < f4)
        {
            f5 = f4;
        }
        tessellator.colour(f * f5, f * f5, f * f5);
        renderblocks.renderBottomFace(block, -0.5D, -0.5D, -0.5D, block.getTextureForSide(0, l));
//        renderblocks.renderBottomFace(block, -0.5D, -0.5D, -0.5D, TextureListener.platform_side);
        f5 = block.getBrightness(world, i, j + 1, k);
        if(f5 < f4)
        {
            f5 = f4;
        }
        tessellator.colour(f1 * f5, f1 * f5, f1 * f5);
        renderblocks.renderTopFace(block, -0.5D, -0.5D, -0.5D, block.getTextureForSide(1, l));
//        renderblocks.renderTopFace(block, -0.5D, -0.5D, -0.5D, TextureListener.platform_side);
        f5 = block.getBrightness(world, i, j, k - 1);
        if(f5 < f4)
        {
            f5 = f4;
        }
        tessellator.colour(f2 * f5, f2 * f5, f2 * f5);
        renderblocks.renderEastFace(block, -0.5D, -0.5D, -0.5D, block.getTextureForSide(2, l));
//        FCUtilsRenderMoving.renderEastFace(block, -0.5D, -0.5D, -0.5D, block.getTextureForSide(2, l), renderblocks);
//        renderblocks.renderEastFace(block, -0.5D, -0.5D, -0.5D, TextureListener.platform_side);
        f5 = block.getBrightness(world, i, j, k + 1);
        if(f5 < f4)
        {
            f5 = f4;
        }
        tessellator.colour(f2 * f5, f2 * f5, f2 * f5);
        renderblocks.renderWestFace(block, -0.5D, -0.5D, -0.5D, block.getTextureForSide(3, l));
//        renderblocks.renderWestFace(block, -0.5D, -0.5D, -0.5D, TextureListener.platform_side);
        f5 = block.getBrightness(world, i - 1, j, k);
        if(f5 < f4)
        {
            f5 = f4;
        }
        tessellator.colour(f3 * f5, f3 * f5, f3 * f5);
        renderblocks.renderNorthFace(block, -0.5D, -0.5D, -0.5D, block.getTextureForSide(4, l));
//        renderblocks.renderNorthFace(block, -0.5D, -0.5D, -0.5D, TextureListener.platform_side);
        f5 = block.getBrightness(world, i + 1, j, k);
        if(f5 < f4)
        {
            f5 = f4;
        }
        tessellator.colour(f3 * f5, f3 * f5, f3 * f5);
        renderblocks.renderSouthFace(block, -0.5D, -0.5D, -0.5D, block.getTextureForSide(5, l));
//        renderblocks.renderSouthFace(block, -0.5D, -0.5D, -0.5D, TextureListener.platform_side);
        tessellator.draw();
    }

    public static void RenderInvBlockWithTexture(BlockRenderer renderblocks, BlockBase block, float f, float f1, float f2, int i)
    {
        Tessellator tessellator = Tessellator.INSTANCE;
        GL11.glTranslatef(f, f1, f2);
        tessellator.start();
        tessellator.setNormal(0.0F, -1F, 0.0F);
        renderblocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, i);
        tessellator.draw();
        tessellator.start();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderblocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, i);
        tessellator.draw();
        tessellator.start();
        tessellator.setNormal(0.0F, 0.0F, -1F);
        renderblocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, i);
        tessellator.draw();
        tessellator.start();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderblocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, i);
        tessellator.draw();
        tessellator.start();
        tessellator.setNormal(-1F, 0.0F, 0.0F);
        renderblocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, i);
        tessellator.draw();
        tessellator.start();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderblocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, i);
        tessellator.draw();
        GL11.glTranslatef(-f, -f1, -f2);
    }

    public static void RenderInvBlockWithMetaData(BlockRenderer renderblocks, BlockBase block, float f, float f1, float f2, int i)
    {
        Tessellator tessellator = Tessellator.INSTANCE;
        GL11.glTranslatef(f, f1, f2);
        tessellator.start();
        tessellator.setNormal(0.0F, -1F, 0.0F);
        renderblocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getTextureForSide(0, i));
        tessellator.draw();
        tessellator.start();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderblocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getTextureForSide(1, i));
        tessellator.draw();
        tessellator.start();
        tessellator.setNormal(0.0F, 0.0F, -1F);
        renderblocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getTextureForSide(2, i));
        tessellator.draw();
        tessellator.start();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderblocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getTextureForSide(3, i));
        tessellator.draw();
        tessellator.start();
        tessellator.setNormal(-1F, 0.0F, 0.0F);
        renderblocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getTextureForSide(4, i));
        tessellator.draw();
        tessellator.start();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderblocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getTextureForSide(5, i));
        tessellator.draw();
        GL11.glTranslatef(-f, -f1, -f2);
    }
}
