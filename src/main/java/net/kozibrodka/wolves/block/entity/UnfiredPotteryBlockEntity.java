// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCTileEntityUnfiredPottery.java

package net.kozibrodka.wolves.block.entity;

import net.kozibrodka.wolves.block.UnfiredPotteryBlock;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;

public class UnfiredPotteryBlockEntity extends BlockEntity
{

    public UnfiredPotteryBlockEntity()
    {
        m_iCookStateCount = 0;
        m_iStateUpdateTickCount = 0;
    }

    public void readNbt(NbtCompound nbttagcompound)
    {
        super.readNbt(nbttagcompound);
        m_iCookStateCount = nbttagcompound.getInt("m_iCookStateUpdateCount");
        m_iStateUpdateTickCount = nbttagcompound.getInt("m_iStateUpdateTickCount");
    }

    public void writeNbt(NbtCompound nbttagcompound)
    {
        super.writeNbt(nbttagcompound);
        nbttagcompound.putInt("m_iCookStateUpdateCount", m_iCookStateCount);
        nbttagcompound.putInt("m_iStateUpdateTickCount", m_iStateUpdateTickCount);
    }

    public void method_1076()
    {
        if(world.isRemote){
            return;
        }
        m_iStateUpdateTickCount++;
        if(m_iStateUpdateTickCount >= 20)
        {
            if(IsInFiredKiln())
            {
                if(m_iCookStateCount == 0)
                {
                    world.method_202(x, y, z, x, y, z);
                }
                m_iCookStateCount += GetFireFactor();
                if(m_iCookStateCount >= 130)
                {
                    int iTargetid = world.getBlockId(x, y, z);
                    Block targetBlock = Block.BLOCKS[iTargetid];
                    ((UnfiredPotteryBlock)targetBlock).Cook(world, x, y, z);
                    return;
                }
            } else
            if(m_iCookStateCount != 0)
            {
                m_iCookStateCount = 0;
                world.method_202(x, y, z, x, y, z);
            }
            m_iStateUpdateTickCount = 0;
        }
    }

    private boolean IsInFiredKiln()
    {
        if(world.getBlockId(x, y - 1, z) != Block.BRICKS.id)
        {
            return false;
        }
        Block _tmp = BlockListener.stokedFire;
        if(world.getBlockId(x, y - 1, z) != Block.BRICKS.id)
        {
            return false;
        }
        int iNonBrickNeighbourCount = 0;
        for(int iTempFacing = 1; iTempFacing <= 5; iTempFacing++)
        {
            BlockPosition tempPos = new BlockPosition(x, y, z);
            tempPos.AddFacingAsOffset(iTempFacing);
            if(world.getBlockId(tempPos.i, tempPos.j, tempPos.k) != Block.BRICKS.id && ++iNonBrickNeighbourCount > 1)
            {
                return false;
            }
        }

        return true;
    }

    public int GetFireFactor()
    {
        int fireFactor = 0;
        if(world.getBlockId(x, y - 2, z) == BlockListener.stokedFire.id)
        {
            fireFactor += 5;
            int tempY = y - 2;
            for(int tempX = x - 1; tempX <= x + 1; tempX++)
            {
                for(int tempZ = z - 1; tempZ <= z + 1; tempZ++)
                {
                    if((tempX != x || tempZ != z) && world.getBlockId(tempX, tempY, tempZ) == BlockListener.stokedFire.id)
                    {
                        fireFactor++;
                    }
                }

            }

        }
        return fireFactor;
    }

    public boolean IsCooking()
    {
        return m_iCookStateCount != 0;
    }

    private final int m_iTicksPerStateUpdate = 20;
    private final int iPrimaryFireFactor = 5;
    private final int iSecondaryFireFactor = 1;
    private final int iCookStateCountToCook = 130;
    private int m_iCookStateCount;
    private int m_iStateUpdateTickCount;
}
