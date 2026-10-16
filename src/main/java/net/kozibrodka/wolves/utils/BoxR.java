package net.kozibrodka.wolves.utils;

import net.minecraft.util.math.Box;

public class BoxR {

    public float minX;
    public float minY;
    public float minZ;
    public float maxX;
    public float maxY;
    public float maxZ;

//    public static BoxR create(double x1, double y1, double z1, double x2, double y2, double z2) {
//        return new BoxR(x1, y1, z1, x2, y2, z2);
//    }

    public BoxR(float x1, float y1, float z1, float x2, float y2, float z2) {
        this.minX = x1;
        this.minY = y1;
        this.minZ = z1;
        this.maxX = x2;
        this.maxY = y2;
        this.maxZ = z2;
    }
}
