package net.kozibrodka.wolves.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.blocks.Anvil;
import net.kozibrodka.wolves.entity.FCEntityTEST;
import net.kozibrodka.wolves.entity.FallingAnvil;
import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.FallingBlock;
import net.minecraft.level.Level;
import net.minecraft.util.maths.MathHelper;
import org.checkerframework.checker.units.qual.A;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class FallingAnvilRender extends EntityRenderer {
    private BlockRenderer tileRenderer = new BlockRenderer();

    public FallingAnvilRender() {
        this.field_2678 = 0.5F;
    }

    @Override
    public void render(EntityBase entity, double d, double e, double f, float g, float h) {
        FallingAnvil fallingAnvil = (FallingAnvil)entity;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)e, (float)f);
        this.bindTexture("/terrain.png");
        BlockBase var10 = BlockBase.BY_ID[fallingAnvil.tile];
        Level var11 = fallingAnvil.getFallingLevel();
        GL11.glDisable(2896);

        this.tileRenderer.method_53(var10, var11, MathHelper.floor(fallingAnvil.x), MathHelper.floor(fallingAnvil.y), MathHelper.floor(fallingAnvil.z));

//        int l = fallingAnvil.facing;
//        int x = MathHelper.floor(fallingAnvil.x);
//        int y = MathHelper.floor(fallingAnvil.y);
//        int z = MathHelper.floor(fallingAnvil.z);
//        SetBlockBoundsRotatedAboutJToFacing(0.0F, 0.0F, 0.25F, 1.0F, 0.125F, 0.75F, l);
//        tileRenderer.renderStandardBlock(var10, x, y, z);
//        SetBlockBoundsRotatedAboutJToFacing(0.375F, 0.125F, 0.375F, 0.625F, 0.5625F, 0.625F, l);
//        tileRenderer.renderStandardBlock(var10, x, y, z);
//        SetBlockBoundsRotatedAboutJToFacing(0.3125F, 0.5625F, 0.3125F, 0.6875F, 1.0F, 0.6875F, l);
//        tileRenderer.renderStandardBlock(var10, x, y, z);
//        SetBlockBoundsRotatedAboutJToFacing(0.6875F, 0.75F, 0.3125F, 0.75F, 1.0F, 0.6875F, l);
//        tileRenderer.renderStandardBlock(var10, x, y, z);
//        SetBlockBoundsRotatedAboutJToFacing(0.75F, 0.9375F, 0.3125F, 1.0F, 1.0F, 0.6875F, l);
//        tileRenderer.renderStandardBlock(var10, x, y, z);
//        SetBlockBoundsRotatedAboutJToFacing(0.25F, 0.75F, 0.3125F, 0.3125F, 1.0F, 0.6875F, l);
//        tileRenderer.renderStandardBlock(var10, x, y, z);
//        SetBlockBoundsRotatedAboutJToFacing(0.125F, 0.8125F, 0.375F, 0.25F, 1.0F, 0.625F, l);
//        tileRenderer.renderStandardBlock(var10, x, y, z);
//        SetBlockBoundsRotatedAboutJToFacing(0.0F, 0.875F, 0.4375F, 0.125F, 1.0F, 0.5625F, l);
//        tileRenderer.renderStandardBlock(var10, x, y, z);

        GL11.glEnable(2896);
        GL11.glPopMatrix();
    }

//    public void SetBlockBoundsRotatedAboutJToFacing(float x1, float y1, float z1, float x2, float y2, float z2, int iFacing)
//    {
//        float rotatedX1;
//        float rotatedZ1;
//        float rotatedX2;
//        float rotatedZ2;
//        if(iFacing == 4)
//        {
//            rotatedX1 = 1.0F - x2;
//            rotatedZ1 = 1.0F - z2;
//            rotatedX2 = 1.0F - x1;
//            rotatedZ2 = 1.0F - z1;
//        } else
//        if(iFacing == 3)
//        {
//            rotatedX1 = z1;
//            rotatedZ1 = x1;
//            rotatedX2 = z2;
//            rotatedZ2 = x2;
//        } else
//        if(iFacing == 2)
//        {
//            rotatedX1 = 1.0F - z2;
//            rotatedZ1 = 1.0F - x2;
//            rotatedX2 = 1.0F - z1;
//            rotatedZ2 = 1.0F - x1;
//        } else
//        {
//            rotatedX1 = x1;
//            rotatedZ1 = z1;
//            rotatedX2 = x2;
//            rotatedZ2 = z2;
//        }
//        setBoundingBox(rotatedX1, y1, rotatedZ1, rotatedX2, y2, rotatedZ2);
//    }

//    public void method_770(FallingBlock arg, double d, double e, double f, float g, float h) {
//        GL11.glPushMatrix();
//        GL11.glTranslatef((float)d, (float)e, (float)f);
//        this.bindTexture("/terrain.png");
//        BlockBase var10 = BlockBase.BY_ID[arg.tile];
//        Level var11 = arg.getFallingLevel();
//        GL11.glDisable(2896);
//        this.field_857.method_53(var10, var11, MathHelper.floor(arg.x), MathHelper.floor(arg.y), MathHelper.floor(arg.z));
//        GL11.glEnable(2896);
//        GL11.glPopMatrix();
//    }
}
