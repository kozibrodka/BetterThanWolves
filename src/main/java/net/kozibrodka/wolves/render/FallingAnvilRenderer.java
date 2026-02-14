package net.kozibrodka.wolves.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.block.AnchorBlock;
import net.kozibrodka.wolves.entity.FallingAnvilEntity;
import net.kozibrodka.wolves.entity.WindMillEntity;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class FallingAnvilRenderer extends EntityRenderer {
    private final BlockRenderManager tileRenderer = new BlockRenderManager();
    Block block = BlockListener.anvil;

    public FallingAnvilRenderer() {
        this.shadowDarkness = 0.5F;
    }

    public void render(Entity entity, double d, double d1, double d2,
                       float f, float f1) {
        World world = entity.world;
        FallingAnvilEntity entityAnvil = (FallingAnvilEntity) entity;
        tileRenderer.blockView = world;
        GL11.glPushMatrix();
        float f2 = 0.3125F;
        GL11.glTranslatef((float) d, (float) d1, (float) d2);
        GL11.glDisable(2896 /*GL_LIGHTING*/);
//        int i = MathHelper.floor(entity.x);
//        int j = MathHelper.floor(entity.y);
//        int k = MathHelper.floor(entity.z);
//        int l = MathHelper.floor(entityAnvil.getFacingMeta());
        int i = MathHelper.floor(entity.x);
        int j = MathHelper.floor(entity.y);
        int k = MathHelper.floor(entity.z);
        int l = entityAnvil.getFacingMeta();
        bindTexture("/terrain.png");

        SetBlockBoundsRotatedAboutJToFacing(0.0F, 0.0F, 0.25F, 1.0F, 0.125F, 0.75F, l);
        CustomBlockRendering.RenderMovingBlockWithMetadata(tileRenderer, block, world, i, j, k, 0);
        SetBlockBoundsRotatedAboutJToFacing(0.375F, 0.125F, 0.375F, 0.625F, 0.5625F, 0.625F, l);
        CustomBlockRendering.RenderMovingBlockWithMetadata(tileRenderer, block, world, i, j, k, 0);
        SetBlockBoundsRotatedAboutJToFacing(0.3125F, 0.5625F, 0.3125F, 0.6875F, 1.0F, 0.6875F, l);
        CustomBlockRendering.RenderMovingBlockWithMetadata(tileRenderer, block, world, i, j, k, 0);
        SetBlockBoundsRotatedAboutJToFacing(0.6875F, 0.75F, 0.3125F, 0.75F, 1.0F, 0.6875F, l);
        CustomBlockRendering.RenderMovingBlockWithMetadata(tileRenderer, block, world, i, j, k, 0);
        SetBlockBoundsRotatedAboutJToFacing(0.75F, 0.9375F, 0.3125F, 1.0F, 1.0F, 0.6875F, l);
        CustomBlockRendering.RenderMovingBlockWithMetadata(tileRenderer, block, world, i, j, k, 0);
        SetBlockBoundsRotatedAboutJToFacing(0.25F, 0.75F, 0.3125F, 0.3125F, 1.0F, 0.6875F, l);
        CustomBlockRendering.RenderMovingBlockWithMetadata(tileRenderer, block, world, i, j, k, 0);
        SetBlockBoundsRotatedAboutJToFacing(0.125F, 0.8125F, 0.375F, 0.25F, 1.0F, 0.625F, l);
        CustomBlockRendering.RenderMovingBlockWithMetadata(tileRenderer, block, world, i, j, k, 0);
        SetBlockBoundsRotatedAboutJToFacing(0.0F, 0.875F, 0.4375F, 0.125F, 1.0F, 0.5625F, l);
        CustomBlockRendering.RenderMovingBlockWithMetadata(tileRenderer, block, world, i, j, k, 0);
//
//        block.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
//        CustomBlockRendering.RenderMovingBlock(tileRenderer, block, world, i, j, k);

        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glPopMatrix();
    }

//    public void render2(Entity entity, double d, double e, double f, float g, float h) {
//        FallingAnvilEntity fallingBlockEntity = (FallingAnvilEntity) entity;
//        GL11.glPushMatrix();
//        GL11.glTranslatef((float)d, (float)e, (float)f);
//        this.bindTexture("/terrain.png");
//        Block var10 = Block.BLOCKS[Block.BRICKS.id];
//        Block var10 = Block.BLOCKS[BlockListener.anvil.id];
//        World var11 = fallingBlockEntity.world;
//        GL11.glDisable(2896);
//        this.tileRenderer.renderFallingBlockEntity(var10, var11, MathHelper.floor(fallingBlockEntity.x), MathHelper.floor(fallingBlockEntity.y), MathHelper.floor(fallingBlockEntity.z));
//        GL11.glEnable(2896);
//        GL11.glPopMatrix();
//    }

    public void SetBlockBoundsRotatedAboutJToFacing(float x1, float y1, float z1, float x2, float y2, float z2, int iFacing) {
        float rotatedX1;
        float rotatedZ1;
        float rotatedX2;
        float rotatedZ2;
        if (iFacing == 4) {
            rotatedX1 = 1.0F - x2;
            rotatedZ1 = 1.0F - z2;
            rotatedX2 = 1.0F - x1;
            rotatedZ2 = 1.0F - z1;
        } else if (iFacing == 3) {
            rotatedX1 = z1;
            rotatedZ1 = x1;
            rotatedX2 = z2;
            rotatedZ2 = x2;
        } else if (iFacing == 2) {
            rotatedX1 = 1.0F - z2;
            rotatedZ1 = 1.0F - x2;
            rotatedX2 = 1.0F - z1;
            rotatedZ2 = 1.0F - x1;
        } else {
            rotatedX1 = x1;
            rotatedZ1 = z1;
            rotatedX2 = x2;
            rotatedZ2 = z2;
        }
        block.setBoundingBox(rotatedX1, y1, rotatedZ1, rotatedX2, y2, rotatedZ2);
    }
}
