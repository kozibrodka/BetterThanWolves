// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.kozibrodka.wolves.materials;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColour;

public class Cement extends Material
{
    public Cement(MaterialColour mapcolor)
    {
        super(mapcolor);
        replaceable();
        breaksWhenPushed();
    }
}
