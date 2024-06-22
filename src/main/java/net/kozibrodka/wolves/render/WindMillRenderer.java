package net.kozibrodka.wolves.render;

import net.kozibrodka.wolves.entity.WindMillEntity;
import net.kozibrodka.wolves.model.WindMillModel;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class WindMillRenderer extends EntityRenderer
{

    public WindMillRenderer()
    {
        shadowOpacity = 0.0F;
        modelWindMill = new WindMillModel();
    }

    public void render(Entity entity, double d, double d1, double d2,
                         float f, float f1)
    {
        WindMillEntity fcentitywindmill = (WindMillEntity)entity;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        bindTexture("/assets/wolves/stationapi/textures/entity/fcwindmillent.png");
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        float f2 = (float)fcentitywindmill.iWindMillTimeSinceHit - f1;
        float f3 = (float)fcentitywindmill.iWindMillCurrentDamage - f1;
        float f4 = 0.0F;
        if(f3 < 0.0F)
        {
            f3 = 0.0F;
        }
        if(f3 > 0.0F)
        {
            f4 = ((MathHelper.sin(f2) * f2 * f3) / 40F) * (float)fcentitywindmill.iWindMillRockDirection;
        }
        if(fcentitywindmill.getAligned())
        {
            GL11.glRotatef(fcentitywindmill.getMillRotation(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f4, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
        } else
        {
            GL11.glRotatef(fcentitywindmill.getMillRotation(), 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(f4, 1.0F, 0.0F, 0.0F);
        }
        modelWindMill.render(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, fcentitywindmill);
        GL11.glPopMatrix();
    }

    protected WindMillModel modelWindMill;
}
