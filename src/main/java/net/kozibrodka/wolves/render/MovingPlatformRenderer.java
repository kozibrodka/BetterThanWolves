// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.kozibrodka.wolves.render;

import net.kozibrodka.wolves.entity.MovingPlatformEntity;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class MovingPlatformRenderer extends EntityRenderer {

    public MovingPlatformRenderer() {
        localRenderBlocks = new BlockRenderManager();
        field_2679 = 0.0F;
    }

    public void render(Entity entity, double d, double d1, double d2,
                       float f, float f1) {
        World world = entity.world;
        localRenderBlocks.blockView = world;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) d, (float) d1, (float) d2);
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        int i = MathHelper.floor(entity.x);
        int j = MathHelper.floor(entity.y);
        int k = MathHelper.floor(entity.z);
        bindTexture("/terrain.png");
        Block block = BlockListener.platform;
        List list = entity.world.collectEntitiesByClass(MovingPlatformEntity.class, Box.createCached(entity.x - 1.0D, entity.y - 0.10000000149011612D, entity.z - 0.10000000149011612D, entity.x - 0.89999997615814209D, entity.y + 0.10000000149011612D, entity.z + 0.10000000149011612D));
        if (list == null || list.size() <= 0) {
            block.setBoundingBox(0.0001F, 0.0625F, 0.0001F, 0.0625F, 0.9375F, 0.9999F);
//            this.localRenderBlocks.method_53(block, world, i, j, k);
            CustomBlockRendering.RenderMovingBlock(localRenderBlocks, block, world, i, j, k);
        }
        list = entity.world.collectEntitiesByClass(MovingPlatformEntity.class, Box.createCached(entity.x - 0.10000000149011612D, entity.y - 0.10000000149011612D, entity.z + 0.89999997615814209D, entity.x + 0.10000000149011612D, entity.y + 0.10000000149011612D, entity.z + 1.0D));
        if (list == null || list.size() <= 0) {
            block.setBoundingBox(0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F, 1.0F);
//            this.localRenderBlocks.method_53( block, world, i, j, k);
            CustomBlockRendering.RenderMovingBlock(localRenderBlocks, block, world, i, j, k);
        }
        list = entity.world.collectEntitiesByClass(MovingPlatformEntity.class, Box.createCached(entity.x + 0.89999997615814209D, entity.y - 0.10000000149011612D, entity.z - 0.10000000149011612D, entity.x + 1.0D, entity.y + 0.10000000149011612D, entity.z + 0.10000000149011612D));
        if (list == null || list.size() <= 0) {
            block.setBoundingBox(0.9375F, 0.0625F, 0.0001F, 0.9999F, 0.9375F, 0.9999F);
//            this.localRenderBlocks.method_53(block, world, i, j, k);
            CustomBlockRendering.RenderMovingBlock(localRenderBlocks, block, world, i, j, k);
        }
        list = entity.world.collectEntitiesByClass(MovingPlatformEntity.class, Box.createCached(entity.x - 0.10000000149011612D, entity.y - 0.10000000149011612D, entity.z - 1.0D, entity.x + 0.10000000149011612D, entity.y + 0.10000000149011612D, entity.z - 0.89999997615814209D));
        if (list == null || list.size() <= 0) {
            block.setBoundingBox(0.0F, 0.0625F, 0.0F, 1.0F, 0.9375F, 0.0625F);
//            this.localRenderBlocks.method_53( block, world, i, j, k);
            CustomBlockRendering.RenderMovingBlock(localRenderBlocks, block, world, i, j, k);
        }
        block.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
//        this.localRenderBlocks.method_53( block, world, i, j, k);
        CustomBlockRendering.RenderMovingBlock(localRenderBlocks, block, world, i, j, k);
        block.setBoundingBox(0.0F, 0.9375F, 0.0F, 1.0F, 1.0F, 1.0F);
//        this.localRenderBlocks.method_53( block, world, i, j, k);
        CustomBlockRendering.RenderMovingBlock(localRenderBlocks, block, world, i, j, k);
        block.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glPopMatrix();
    }


    private final BlockRenderManager localRenderBlocks;
}
