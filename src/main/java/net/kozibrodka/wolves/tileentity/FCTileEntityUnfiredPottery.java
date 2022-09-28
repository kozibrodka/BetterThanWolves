// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCTileEntityUnfiredPottery.java

package net.kozibrodka.wolves.tileentity;

import net.kozibrodka.wolves.blocks.FCBlockUnfiredPottery;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.utils.FCBlockPos;
import net.minecraft.block.BlockBase;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.io.CompoundTag;

public class FCTileEntityUnfiredPottery extends TileEntityBase
{

    public FCTileEntityUnfiredPottery()
    {
        m_iCookStateCount = 0;
        m_iStateUpdateTickCount = 0;
    }

    public void readIdentifyingData(CompoundTag nbttagcompound)
    {
        super.readIdentifyingData(nbttagcompound);
        m_iCookStateCount = nbttagcompound.getInt("m_iCookStateUpdateCount");
        m_iStateUpdateTickCount = nbttagcompound.getInt("m_iStateUpdateTickCount");
    }

    public void writeIdentifyingData(CompoundTag nbttagcompound)
    {
        super.writeIdentifyingData(nbttagcompound);
        nbttagcompound.put("m_iCookStateUpdateCount", m_iCookStateCount);
        nbttagcompound.put("m_iStateUpdateTickCount", m_iStateUpdateTickCount);
    }

    public void tick()
    {
        m_iStateUpdateTickCount++;
        if(m_iStateUpdateTickCount >= 20)
        {
            if(IsInFiredKiln())
            {
                if(m_iCookStateCount == 0)
                {
                    level.method_202(x, y, z, x, y, z);
                }
                m_iCookStateCount += GetFireFactor();
                if(m_iCookStateCount >= 130)
                {
                    int iTargetid = level.getTileId(x, y, z);
                    BlockBase targetBlock = BlockBase.BY_ID[iTargetid];
                    ((FCBlockUnfiredPottery)targetBlock).Cook(level, x, y, z);
                    return;
                }
            } else
            if(m_iCookStateCount != 0)
            {
                m_iCookStateCount = 0;
                level.method_202(x, y, z, x, y, z);
            }
            m_iStateUpdateTickCount = 0;
        }
    }

    private boolean IsInFiredKiln()
    {
        if(level.getTileId(x, y - 1, z) != BlockBase.BRICKS.id)
        {
            return false;
        }
        BlockBase _tmp = mod_FCBetterThanWolves.fcStokedFire;
        if(level.getTileId(x, y - 1, z) != BlockBase.BRICKS.id)
        {
            return false;
        }
        int iNonBrickNeighbourCount = 0;
        for(int iTempFacing = 1; iTempFacing <= 5; iTempFacing++)
        {
            FCBlockPos tempPos = new FCBlockPos(x, y, z);
            tempPos.AddFacingAsOffset(iTempFacing);
            if(level.getTileId(tempPos.i, tempPos.j, tempPos.k) != BlockBase.BRICKS.id && ++iNonBrickNeighbourCount > 1)
            {
                return false;
            }
        }

        return true;
    }

    public int GetFireFactor()
    {
        int fireFactor = 0;
        if(level.getTileId(x, y - 2, z) == mod_FCBetterThanWolves.fcStokedFire.id)
        {
            fireFactor += 5;
            int tempY = y - 2;
            for(int tempX = x - 1; tempX <= x + 1; tempX++)
            {
                for(int tempZ = z - 1; tempZ <= z + 1; tempZ++)
                {
                    if((tempX != x || tempZ != z) && level.getTileId(tempX, tempY, tempZ) == mod_FCBetterThanWolves.fcStokedFire.id)
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
