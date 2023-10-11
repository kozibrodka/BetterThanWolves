package net.kozibrodka.wolves.render;

import net.kozibrodka.wolves.entity.WaterWheelEntity;
import net.kozibrodka.wolves.model.FCModelWaterWheel;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelBase;
import net.minecraft.entity.EntityBase;
import net.minecraft.util.maths.MathHelper;
import org.lwjgl.opengl.GL11;

public class FCRenderWaterWheel extends EntityRenderer
{

    public FCRenderWaterWheel()
    {
        field_2678 = 0.0F;
        modelWaterWheel = new FCModelWaterWheel();
    }

    public void render(EntityBase entity, double d, double d1, double d2,
                         float f, float f1)
    {
        WaterWheelEntity fcentitywaterwheel = (WaterWheelEntity)entity;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        bindTexture("/assets/wolves/stationapi/textures/entity/fcwaterwheelent.png");
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        float f2 = (float)fcentitywaterwheel.iWaterWheelTimeSinceHit - f1;
        float f3 = (float)fcentitywaterwheel.iWaterWheelCurrentDamage - f1;
        float f4 = 0.0F;
        if(f3 < 0.0F)
        {
            f3 = 0.0F;
        }
        if(f3 > 0.0F)
        {
            f4 = ((MathHelper.sin(f2) * f2 * f3) / 40F) * (float)fcentitywaterwheel.iWaterWheelRockDirection;
        }
        if(fcentitywaterwheel.bWaterWheelIAligned)
        {
            GL11.glRotatef(fcentitywaterwheel.fRotation, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f4, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
        } else
        {
            GL11.glRotatef(fcentitywaterwheel.fRotation, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(f4, 1.0F, 0.0F, 0.0F);
        }
        modelWaterWheel.render(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    protected EntityModelBase modelWaterWheel;
}
