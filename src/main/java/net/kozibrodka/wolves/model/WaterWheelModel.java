package net.kozibrodka.wolves.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.util.math.MathHelper;

public class WaterWheelModel extends EntityModel
{

    public WaterWheelModel()
    {
        waterWheelComponents = new ModelPart[16];
        for(int i = 0; i < 8; i++)
        {
            waterWheelComponents[i] = new ModelPart(0, 0);
            waterWheelComponents[i].addCuboid(2.5F, -1F, -7F, 36, 2, 14);
            waterWheelComponents[i].setPivot(0.0F, 0.0F, 0.0F);
            waterWheelComponents[i].roll = (3.141593F * (float)i) / 4F;
        }

        for(int j = 0; j < 8; j++)
        {
            waterWheelComponents[j + 8] = new ModelPart(0, 0);
            waterWheelComponents[j + 8].addCuboid(0.0F, -1F, -6F, 22, 2, 12);
            float f = 0.7853982F * (float)j;
            waterWheelComponents[j + 8].setPivot(30F * MathHelper.cos(f), 30F * MathHelper.sin(f), 0.0F);
            waterWheelComponents[j + 8].roll = 1.963496F + 0.7853982F * (float)j;
        }

    }

    public void render(float f, float f1, float f2, float f3, float f4, float f5)
    {
        for(int i = 0; i < 16; i++)
        {
            waterWheelComponents[i].render(f5);
        }

    }

    public void setAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
    }

    public ModelPart waterWheelComponents[];
    private final int iNumWaterWheelComponents = 16;
    private final float fLocalPi = 3.141593F;
    private final float fStrutDistFromCent = 30F;
}
