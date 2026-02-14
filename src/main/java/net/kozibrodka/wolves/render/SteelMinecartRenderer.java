package net.kozibrodka.wolves.render;

import net.kozibrodka.wolves.entity.SteelMinecartEntity;
import net.kozibrodka.wolves.entity.WindMillEntity;
import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.MinecartEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class SteelMinecartRenderer extends EntityRenderer {
    protected EntityModel model;

    public SteelMinecartRenderer() {
        this.shadowRadius = 0.5F;
        this.model = new MinecartEntityModel();
    }

    public void render(Entity entity, double d, double e, double f, float g, float h) {
        SteelMinecartEntity minecartEntity = (SteelMinecartEntity) entity;
        GL11.glPushMatrix();
        double var10 = minecartEntity.lastTickX + (minecartEntity.x - minecartEntity.lastTickX) * (double)h;
        double var12 = minecartEntity.lastTickY + (minecartEntity.y - minecartEntity.lastTickY) * (double)h;
        double var14 = minecartEntity.lastTickZ + (minecartEntity.z - minecartEntity.lastTickZ) * (double)h;
        double var16 = (double)0.3F;
        Vec3d var18 = minecartEntity.snapPositionToRail(var10, var12, var14);
        float var19 = minecartEntity.prevPitch + (minecartEntity.pitch - minecartEntity.prevPitch) * h;
        if (var18 != null) {
            Vec3d var20 = minecartEntity.snapPositionToRailWithOffset(var10, var12, var14, var16);
            Vec3d var21 = minecartEntity.snapPositionToRailWithOffset(var10, var12, var14, -var16);
            if (var20 == null) {
                var20 = var18;
            }

            if (var21 == null) {
                var21 = var18;
            }

            d += var18.x - var10;
            e += (var20.y + var21.y) / (double)2.0F - var12;
            f += var18.z - var14;
            Vec3d var22 = var21.add(-var20.x, -var20.y, -var20.z);
            if (var22.length() != (double)0.0F) {
                var22 = var22.normalize();
                g = (float)(Math.atan2(var22.z, var22.x) * (double)180.0F / Math.PI);
                var19 = (float)(Math.atan(var22.y) * (double)73.0F);
            }
        }

        GL11.glTranslatef((float)d, (float)e, (float)f);
        GL11.glRotatef(180.0F - g, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var19, 0.0F, 0.0F, 1.0F);
        float var23 = (float)minecartEntity.getMillTimeSinceHit() - h;
//        float var23 = (float)minecartEntity.damageWobbleTicks - h;
        float var24 = (float)minecartEntity.getMillCurrentDamage() - h;
//        float var24 = (float)minecartEntity.damageWobbleStrength - h;
        if (var24 < 0.0F) {
            var24 = 0.0F;
        }

        if (var23 > 0.0F) {
            GL11.glRotatef(MathHelper.sin(var23) * var23 * var24 / 10.0F * (float)minecartEntity.getMillRockDirection(), 1.0F, 0.0F, 0.0F);
//            GL11.glRotatef(MathHelper.sin(var23) * var23 * var24 / 10.0F * (float)minecartEntity.damageWobbleSide, 1.0F, 0.0F, 0.0F);
        }

        if (minecartEntity.type != 0) {
            this.bindTexture("/terrain.png");
            float var26 = 0.75F;
            GL11.glScalef(var26, var26, var26);
            GL11.glTranslatef(0.0F, 0.3125F, 0.0F);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            if (minecartEntity.type == 1) {
                (new BlockRenderManager()).render(Block.CHEST, 0, minecartEntity.getBrightnessAtEyes(h));
            } else if (minecartEntity.type == 2) {
                (new BlockRenderManager()).render(Block.FURNACE, 0, minecartEntity.getBrightnessAtEyes(h));
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.3125F, 0.0F);
            GL11.glScalef(1.0F / var26, 1.0F / var26, 1.0F / var26);
        }

        this.bindTexture("/assets/wolves/stationapi/textures/entity/steelcart.png");
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        this.model.render(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }
}
