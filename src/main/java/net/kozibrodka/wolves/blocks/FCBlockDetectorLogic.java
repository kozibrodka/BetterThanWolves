// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCBlockDetectorLogic.java

package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.utils.FCBlockPos;
import net.kozibrodka.wolves.utils.FCUtilsMisc;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.ParticleBase;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.util.List;
import java.util.Random;

public class FCBlockDetectorLogic extends TemplateBlockBase
{

    public FCBlockDetectorLogic(Identifier iid)
    {
        super(iid, Material.AIR);
        bLogicDebugDisplay = false;
        texture = 250;
        setTicksRandomly(true);
    }

    public int getTickrate()
    {
        return 8;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public int getPistonPushMode()
    {
        return 1;
    }

    public void onBlockRemoved(Level world, int i, int j, int k)
    {
        for(int iFacing = 0; iFacing <= 5; iFacing++)
        {
            FCBlockPos targetPos = new FCBlockPos(i, j, k);
            targetPos.AddFacingAsOffset(iFacing);
            int iTargetid = world.getTileId(targetPos.i, targetPos.j, targetPos.k);
            if(iTargetid == mod_FCBetterThanWolves.fcBlockDetector.id)
            {
                BlockBase.BY_ID[iTargetid].onAdjacentBlockUpdate(world, targetPos.i, targetPos.j, targetPos.k, id);
            }
        }

        super.onBlockRemoved(world, i, j, k);
    }

    public int getDropId(int i, Random random)
    {
        return 0;
    }

    public boolean isCollidable(int i, boolean flag)
    {
        return false;
    }

    public int getDropCount(Random random)
    {
        return 0;
    }

    public Box getCollisionShape(Level world, int i, int j, int l)
    {
        return null;
    }

    public void onAdjacentBlockUpdate(Level world, int i, int j, int k, int l)
    {
        if(!CheckForNeighbouringDetector(world, i, j, k))
        {
            world.setTile(i, j, k, 0);
        } else
        {
            world.method_216(i, j, k, id, getTickrate());
        }
    }

    private boolean CheckForNeighbouringDetector(Level world, int i, int j, int k)
    {
        for(int iTempFacing = 0; iTempFacing <= 5; iTempFacing++)
        {
            FCBlockPos tempPos = new FCBlockPos(i, j, k);
            tempPos.AddFacingAsOffset(iTempFacing);
            if(world.getTileId(tempPos.i, tempPos.j, tempPos.k) == mod_FCBetterThanWolves.fcBlockDetector.id && ((FCBlockDetectorBlock)mod_FCBetterThanWolves.fcBlockDetector).GetFacing(world, tempPos.i, tempPos.j, tempPos.k) == FCUtilsMisc.GetOppositeFacing(iTempFacing))
            {
                return true;
            }
        }

        return false;
    }

    public void onEntityCollision(Level world, int i, int j, int k, EntityBase entity)
    {
        if(!(entity instanceof ParticleBase))
        {
            boolean bIsOn = world.getTileMeta(i, j, k) > 0;
            if(!bIsOn)
            {
                world.setTileMeta(i, j, k, 1);
                world.method_216(i, j, k, id, getTickrate());
            }
        }
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
        boolean bIsOn = world.getTileMeta(i, j, k) > 0;
        boolean bShouldBeOn = IsEntityWithinBounds(world, i, j, k);
        if(bShouldBeOn)
        {
            if(!bIsOn)
            {
                world.setTileMeta(i, j, k, 1);
            }
            world.method_216(i, j, k, id, getTickrate());
        } else
        if(world.getTileId(i, j - 1, k) == BlockBase.CROPS.id && world.getTileMeta(i, j - 1, k) == 7)
        {
            if(!bIsOn)
            {
                world.setTileMeta(i, j, k, 1);
            }
        } else
        if(bIsOn)
        {
            world.setTileMeta(i, j, k, 0);
        }
    }

    private boolean IsEntityWithinBounds(Level world, int i, int j, int k)
    {
        List list = world.getEntities(EntityBase.class, Box.createButWasteMemory(i, j, k, i + 1, j + 1, k + 1));
        if(list != null && list.size() > 0)
        {
            for(int listIndex = 0; listIndex < list.size(); listIndex++)
            {
                EntityBase targetEntity = (EntityBase)list.get(listIndex);
                if(!(targetEntity instanceof ParticleBase))
                {
                    return true;
                }
            }

        }
        return false;
    }

    //TODO: Forge methods?
//    public boolean isBlockReplaceable(Level world, int i, int j, int l)
//    {
//        return true;
//    }
//
//    public boolean isAir(Level world, int i, int j, int l)
//    {
//        return true;
//    }

    private static final int iDetectorLogicTickRate = 8;
    private boolean bLogicDebugDisplay;
}
