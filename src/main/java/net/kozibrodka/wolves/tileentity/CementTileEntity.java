// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCTileEntityCement.java

package net.kozibrodka.wolves.tileentity;


// Referenced classes of package net.minecraft.src:
//            TileEntity, NBTTagCompound

import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.io.CompoundTag;

// TODO: Replace this whole thing with BlockStates
public class CementTileEntity extends TileEntityBase
{

    public CementTileEntity()
    {
        spreadDist = 0;
        dryTime = 0;
    }

    public void writeIdentifyingData(CompoundTag nbttagcompound)
    {
        super.writeIdentifyingData(nbttagcompound);
        nbttagcompound.put("dryTime", dryTime);
        nbttagcompound.put("spreadDist", spreadDist);
    }

    public void readIdentifyingData(CompoundTag nbttagcompound)
    {
        super.readIdentifyingData(nbttagcompound);
        if(nbttagcompound.containsKey("dryTime"))
        {
            dryTime = nbttagcompound.getInt("dryTime");
        } else
        {
            dryTime = 12;
        }
        if(nbttagcompound.containsKey("spreadDist"))
        {
            spreadDist = nbttagcompound.getInt("spreadDist");
        } else
        {
            spreadDist = 16;
        }
    }

    public int dryTime;
    public int spreadDist;
}
