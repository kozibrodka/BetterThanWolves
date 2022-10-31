// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.kozibrodka.wolves.render;

import net.kozibrodka.wolves.blocks.FCBlockAnchor;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.mixin.BlockRendererAccessor;
import net.kozibrodka.wolves.utils.FCUtilsRender;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.EntityBase;
import net.minecraft.level.Level;
import net.minecraft.util.maths.MathHelper;
import org.lwjgl.opengl.GL11;


public class FCRenderMovingAnchor extends EntityRenderer
{

    public FCRenderMovingAnchor()
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
        float f2 = 0.3125F;
        GL11.glTranslatef((float)d, (float)d1 + f2, (float)d2);
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        int i = MathHelper.floor(entity.x);
        int j = MathHelper.floor(entity.y);
        int k = MathHelper.floor(entity.z);
//        bindTexture("/assets/wolves/stationapi/textures/entity/btwterrain01.png");
//        bindTexture("/assets/wolves/stationapi/textures/entity/terrain_btw.png");
//        this.bindTexture("/terrain.png");
        bindTexture("/assets/wolves/stationapi/textures/entity/terrain_og.png");
        BlockBase block = mod_FCBetterThanWolves.fcAnchor;
        float f3 = 0.5F;
        float f5 = 0.5F;
        float f7 = FCBlockAnchor.fAnchorBaseHeight;
        block.setBoundingBox(0.5F - f5, 0.0F, 0.5F - f3, 0.5F + f5, f7, 0.5F + f3);
        FCUtilsRender.RenderMovingBlockWithMetadata(localRenderBlocks, block, world, i, j, k, 1);
        f3 = 0.125F;
        f5 = 0.125F;
        f7 = 0.25F;
        block.setBoundingBox(0.5F - f5, FCBlockAnchor.fAnchorBaseHeight, 0.5F - f3, 0.5F + f5, FCBlockAnchor.fAnchorBaseHeight + f7, 0.5F + f3);
        ((FCBlockAnchor)mod_FCBetterThanWolves.fcAnchor).getClass();
        FCUtilsRender.RenderMovingBlockWithTexture(localRenderBlocks, block, world, i, j, k, 0);
        if(world.getTileId(i, j, k) != mod_FCBetterThanWolves.fcRopeBlock.id)
        {
            float f4 = 0.062375F;
            float f6 = 0.062375F;
            float f8 = 1.0F;
            block.setBoundingBox(0.5F - f6, FCBlockAnchor.fAnchorBaseHeight, 0.5F - f4, 0.5F + f6, 1.99F, 0.5F + f4);
            ((FCBlockAnchor)mod_FCBetterThanWolves.fcAnchor).getClass();
            FCUtilsRender.RenderMovingBlockWithTexture(localRenderBlocks, block, world, i, j, k, 3);
        }
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glPopMatrix();
    }

    private BlockRenderer localRenderBlocks;
}
