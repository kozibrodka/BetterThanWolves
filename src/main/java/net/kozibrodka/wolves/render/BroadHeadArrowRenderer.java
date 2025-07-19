package net.kozibrodka.wolves.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.entity.BroadheadArrowEntity;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class BroadHeadArrowRenderer extends EntityRenderer {
    public BroadHeadArrowRenderer() {
    }

    public void render(Entity entityBase, double d, double e, double f, float g, float h) {
        BroadheadArrowEntity fcentitybroadheadarrow = (BroadheadArrowEntity) entityBase;
        if (fcentitybroadheadarrow.prevYaw != 0.0F || fcentitybroadheadarrow.prevPitch != 0.0F) {
            bindTexture("/assets/wolves/stationapi/textures/entity/fcbroadhead.png");
            GL11.glPushMatrix();
            GL11.glTranslatef((float) d, (float) e, (float) f);
            GL11.glRotatef(fcentitybroadheadarrow.prevYaw + (fcentitybroadheadarrow.yaw - fcentitybroadheadarrow.prevYaw) * h - 90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(fcentitybroadheadarrow.prevPitch + (fcentitybroadheadarrow.pitch - fcentitybroadheadarrow.prevPitch) * h, 0.0F, 0.0F, 1.0F);
            Tessellator var10 = Tessellator.INSTANCE;
            byte var11 = 0;
            float var12 = 0.0F;
            float var13 = 0.5F;
            float var14 = (float) (var11 * 10) / 32.0F;
            float var15 = (float) (5 + var11 * 10) / 32.0F;
            float var16 = 0.0F;
            float var17 = 0.15625F;
            float var18 = (float) (5 + var11 * 10) / 32.0F;
            float var19 = (float) (10 + var11 * 10) / 32.0F;
            float var20 = 0.05625F;
            GL11.glEnable(32826);
            float var21 = (float) fcentitybroadheadarrow.shake - h;
            if (var21 > 0.0F) {
                float var22 = -MathHelper.sin(var21 * 3.0F) * var21;
                GL11.glRotatef(var22, 0.0F, 0.0F, 1.0F);
            }

            GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(var20, var20, var20);
            GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
            GL11.glNormal3f(var20, 0.0F, 0.0F);
            var10.startQuads();
            var10.vertex(-7.0D, -2.0D, -2.0D, var16, var18);
            var10.vertex(-7.0D, -2.0D, 2.0D, var17, var18);
            var10.vertex(-7.0D, 2.0D, 2.0D, var17, var19);
            var10.vertex(-7.0D, 2.0D, -2.0D, var16, var19);
            var10.draw();
            GL11.glNormal3f(-var20, 0.0F, 0.0F);
            var10.startQuads();
            var10.vertex(-7.0D, 2.0D, -2.0D, var16, var18);
            var10.vertex(-7.0D, 2.0D, 2.0D, var17, var18);
            var10.vertex(-7.0D, -2.0D, 2.0D, var17, var19);
            var10.vertex(-7.0D, -2.0D, -2.0D, var16, var19);
            var10.draw();

            for (int var23 = 0; var23 < 4; ++var23) {
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glNormal3f(0.0F, 0.0F, var20);
                var10.startQuads();
                var10.vertex(-8.0D, -2.0D, 0.0D, var12, var14);
                var10.vertex(8.0D, -2.0D, 0.0D, var13, var14);
                var10.vertex(8.0D, 2.0D, 0.0D, var13, var15);
                var10.vertex(-8.0D, 2.0D, 0.0D, var12, var15);
                var10.draw();
            }

            GL11.glDisable(32826);
            GL11.glPopMatrix();
        }
    }
}
