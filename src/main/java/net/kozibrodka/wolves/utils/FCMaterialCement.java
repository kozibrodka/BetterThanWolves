// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.kozibrodka.wolves.utils;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColour;

public class FCMaterialCement extends Material
{
    public FCMaterialCement(MaterialColour mapcolor)
    {
        super(mapcolor);
        replaceable();
        breaksWhenPushed();
    }
}
