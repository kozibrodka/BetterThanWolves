// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCTileEntityCement.java

package net.kozibrodka.wolves.block.entity;


import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;


public class CementBlockEntity extends BlockEntity {

    public CementBlockEntity() {
        spreadDist = 0;
        dryTime = 0;
    }

    public void writeNbt(NbtCompound nbttagcompound) {
        super.writeNbt(nbttagcompound);
        nbttagcompound.putInt("dryTime", dryTime);
        nbttagcompound.putInt("spreadDist", spreadDist);
    }

    public void readNbt(NbtCompound nbttagcompound) {
        super.readNbt(nbttagcompound);
        if (nbttagcompound.contains("dryTime")) {
            dryTime = nbttagcompound.getInt("dryTime");
        } else {
            dryTime = 12;
        }
        if (nbttagcompound.contains("spreadDist")) {
            spreadDist = nbttagcompound.getInt("spreadDist");
        } else {
            spreadDist = 16;
        }
    }

    public int dryTime;
    public int spreadDist;
}
