// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.kozibrodka.wolves.render;

import java.util.List;

import net.kozibrodka.wolves.entity.FCEntityMovingPlatform;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.mixin.BlockRendererAccessor;
import net.kozibrodka.wolves.utils.FCUtilsRender;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.EntityBase;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.MathHelper;
import org.lwjgl.opengl.GL11;

public class FCRenderMovingPlatform extends EntityRenderer
{

    public FCRenderMovingPlatform()
    {
        localRenderBlocks = new BlockRenderer();
        field_2678 = 0.0F;
    }

    public void render(EntityBase entity, double d, double d1, double d2,
                       float f, float f1)
    {
        Level world = entity.level;
        ((BlockRendererAccessor)localRenderBlocks).setBlockView(world);
//        localRenderBlocks.blockView = world;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        int i = MathHelper.floor(entity.x);
        int j = MathHelper.floor(entity.y);
        int k = MathHelper.floor(entity.z);
        bindTexture("/assets/wolves/stationapi/textures/entity/btwterrain01.png");
        BlockBase block = mod_FCBetterThanWolves.fcPlatform;
        List list = entity.level.getEntities(FCEntityMovingPlatform.class, Box.createButWasteMemory(entity.x - 1.0D, entity.y - 0.10000000149011612D, entity.z - 0.10000000149011612D, entity.x - 0.89999997615814209D, entity.y + 0.10000000149011612D, entity.z + 0.10000000149011612D));
        if(list == null || list.size() <= 0)
        {
            block.setBoundingBox(0.0001F, 0.0625F, 0.0001F, 0.0625F, 0.9375F, 0.9999F);
            FCUtilsRender.RenderMovingBlock(localRenderBlocks, block, world, i, j, k);
        }
        list = entity.level.getEntities(FCEntityMovingPlatform.class, Box.createButWasteMemory(entity.x - 0.10000000149011612D, entity.y - 0.10000000149011612D, entity.z + 0.89999997615814209D, entity.x + 0.10000000149011612D, entity.y + 0.10000000149011612D, entity.z + 1.0D));
        if(list == null || list.size() <= 0)
        {
            block.setBoundingBox(0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F, 1.0F);
            FCUtilsRender.RenderMovingBlock(localRenderBlocks, block, world, i, j, k);
        }
        list = entity.level.getEntities(FCEntityMovingPlatform.class, Box.createButWasteMemory(entity.x + 0.89999997615814209D, entity.y - 0.10000000149011612D, entity.z - 0.10000000149011612D, entity.x + 1.0D, entity.y + 0.10000000149011612D, entity.z + 0.10000000149011612D));
        if(list == null || list.size() <= 0)
        {
            block.setBoundingBox(0.9375F, 0.0625F, 0.0001F, 0.9999F, 0.9375F, 0.9999F);
            FCUtilsRender.RenderMovingBlock(localRenderBlocks, block, world, i, j, k);
        }
        list = entity.level.getEntities(FCEntityMovingPlatform.class, Box.createButWasteMemory(entity.x - 0.10000000149011612D, entity.y - 0.10000000149011612D, entity.z - 1.0D, entity.x + 0.10000000149011612D, entity.y + 0.10000000149011612D, entity.z - 0.89999997615814209D));
        if(list == null || list.size() <= 0)
        {
            block.setBoundingBox(0.0F, 0.0625F, 0.0F, 1.0F, 0.9375F, 0.0625F);
            FCUtilsRender.RenderMovingBlock(localRenderBlocks, block, world, i, j, k);
        }
        block.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        FCUtilsRender.RenderMovingBlock(localRenderBlocks, block, world, i, j, k);
        block.setBoundingBox(0.0F, 0.9375F, 0.0F, 1.0F, 1.0F, 1.0F);
        FCUtilsRender.RenderMovingBlock(localRenderBlocks, block, world, i, j, k);
        block.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glPopMatrix();
    }

    private BlockRenderer localRenderBlocks;
}
