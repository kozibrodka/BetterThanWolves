package net.kozibrodka.wolves.render;

import net.kozibrodka.wolves.entity.FCEntityTEST;
import net.kozibrodka.wolves.entity.WindMillEntity;
import net.kozibrodka.wolves.model.WindMillModel;
import net.kozibrodka.wolves.model.WindMillModelTEST;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelBase;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.animal.Sheep;
import net.minecraft.util.maths.MathHelper;
import org.lwjgl.opengl.GL11;

public class FCEntityTestRender extends EntityRenderer
{

    public FCEntityTestRender()
    {
        field_2678 = 0.0F;
        modelWindMill = new WindMillModelTEST();
    }

    public void render(EntityBase entity, double d, double d1, double d2,
                       float f, float f1)
    {
        FCEntityTEST fcentitywindmill = (FCEntityTEST)entity;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        bindTexture("/assets/wolves/stationapi/textures/entity/fcwindmillent.png");
        GL11.glScalef(1.0F, 1.0F, 1.0F);

        modelWindMill.render(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, fcentitywindmill);
        GL11.glPopMatrix();
    }

    protected WindMillModelTEST modelWindMill;
}
