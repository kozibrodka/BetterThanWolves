package net.kozibrodka.wolves.model;

import net.kozibrodka.wolves.entity.WindMillEntity;
import net.minecraft.client.model.Cuboid;
import net.minecraft.client.render.entity.model.EntityModelBase;
import net.minecraft.entity.animal.Sheep;
import org.lwjgl.opengl.GL11;

public class WindMillModel extends EntityModelBase
{

    public WindMillModel()
    {
        windMillComponents = new Cuboid[8];
        for(int i = 0; i < 4; i++)
        {
            windMillComponents[i] = new Cuboid(0, 0);
            windMillComponents[i].method_1817(2.5F, -2F, -2F, 97, 4, 4);
            windMillComponents[i].setRotationPoint(0.0F, 0.0F, 0.0F);
            windMillComponents[i].roll = (3.141593F * (float)(i - 4)) / 2.0F;
        }

        for(int j = 4; j < 8; j++)
        {
            windMillComponents[j] = new Cuboid(0, 15);
            windMillComponents[j].method_1817(15F, 1.75F, 1.0F, 84, 16, 1);
            windMillComponents[j].setRotationPoint(0.0F, 0.0F, 0.0F);
            windMillComponents[j].pitch = -0.2617994F;
            windMillComponents[j].roll = (3.141593F * (float)j) / 2.0F;
        }

    }

    public void render(float f, float f1, float f2, float f3, float f4, float f5, WindMillEntity fcentitywindmill)
    {
        for(int i = 0; i < 4; i++)
        {
            windMillComponents[i].method_1815(f5);
        }

        float f6 = fcentitywindmill.getBrightnessAtEyes(f);
        for(int j = 4; j < 8; j++)
        {
            int k = fcentitywindmill.getBladeColor(j - 4);
            GL11.glColor3f(f6 * Sheep.field_2698[k][0], f6 * Sheep.field_2698[k][1], f6 * Sheep.field_2698[k][2]);
            windMillComponents[j].method_1815(f5);
        }

    }

    public void setAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
    }

    public Cuboid windMillComponents[];
    private final int iNumWindMillComponents = 8;
    private final float fLocalPi = 3.141593F;
    private final float fBladeOffsetFromCenter = 15F;
    private final int iBladeLength = 84;
    private final int iBladeWidth = 16;
    private final float fShaftOffsetFromCenter = 2.5F;
    private final int iShaftLength = 97;
    private final int iShaftWidth = 4;
}
