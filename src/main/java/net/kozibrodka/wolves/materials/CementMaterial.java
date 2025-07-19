// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.kozibrodka.wolves.materials;

import net.minecraft.block.MapColor;
import net.minecraft.block.material.Material;

public class CementMaterial extends Material {
    public CementMaterial(MapColor mapcolor) {
        super(mapcolor);
        setReplaceable();
        setDestroyPistonBehavior();
    }
}
