package net.kozibrodka.wolves.model;

import net.minecraft.client.model.Cuboid;
import net.minecraft.client.render.entity.model.EntityModelBase;
import net.minecraft.util.maths.MathHelper;

public class WaterWheelModel extends EntityModelBase
{

    public WaterWheelModel()
    {
        waterWheelComponents = new Cuboid[16];
        for(int i = 0; i < 8; i++)
        {
            waterWheelComponents[i] = new Cuboid(0, 0);
            waterWheelComponents[i].method_1817(2.5F, -1F, -7F, 36, 2, 14);
            waterWheelComponents[i].setRotationPoint(0.0F, 0.0F, 0.0F);
            waterWheelComponents[i].roll = (3.141593F * (float)i) / 4F;
        }

        for(int j = 0; j < 8; j++)
        {
            waterWheelComponents[j + 8] = new Cuboid(0, 0);
            waterWheelComponents[j + 8].method_1817(0.0F, -1F, -6F, 22, 2, 12);
            float f = 0.7853982F * (float)j;
            waterWheelComponents[j + 8].setRotationPoint(30F * MathHelper.cos(f), 30F * MathHelper.sin(f), 0.0F);
            waterWheelComponents[j + 8].roll = 1.963496F + 0.7853982F * (float)j;
        }

    }

    public void render(float f, float f1, float f2, float f3, float f4, float f5)
    {
        for(int i = 0; i < 16; i++)
        {
            waterWheelComponents[i].method_1815(f5);
        }

    }

    public void setAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
    }

    public Cuboid waterWheelComponents[];
    private final int iNumWaterWheelComponents = 16;
    private final float fLocalPi = 3.141593F;
    private final float fStrutDistFromCent = 30F;
}
