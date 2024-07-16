package net.kozibrodka.wolves.render;

import net.kozibrodka.wolves.entity.WaterWheelEntity;
import net.kozibrodka.wolves.mixin.BlockRendererAccessor;
import net.kozibrodka.wolves.model.WaterWheelModel;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class WaterWheelRenderer extends EntityRenderer
{

    public WaterWheelRenderer()
    {
        field_2679 = 0.0F;
        modelWaterWheel = new WaterWheelModel();
    }

    @Override
    public void render(Entity entity, double x, double y, double z, float f, float f1)
    {
        WaterWheelEntity fcentitywaterwheel = (WaterWheelEntity)entity;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
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
        if(fcentitywaterwheel.getAligned())
        {
            GL11.glRotatef(fcentitywaterwheel.getWheelRotation(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f4, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
        } else
        {
            GL11.glRotatef(fcentitywaterwheel.getWheelRotation(), 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(f4, 1.0F, 0.0F, 0.0F);
        }
        modelWaterWheel.render(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    protected EntityModel modelWaterWheel;
}
