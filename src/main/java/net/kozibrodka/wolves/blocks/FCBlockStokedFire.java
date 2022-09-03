package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.mixin.FireAccessor;
import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateFire;

import java.util.Random;


public class FCBlockStokedFire extends TemplateFire
{
//TODO get rid of metadata usage later
    public FCBlockStokedFire(Identifier iid)
    {
        super(iid, 31);
        setHardness(0.0F);
        setLightEmittance(1.0F);
        setSounds(WOOD_SOUNDS);
        disableStat();
        disableNotifyOnMetaDataChange();
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
        boolean flag = world.getTileId(i, j - 1, k) == BlockBase.NETHERRACK.id;
        if(!canPlaceAt(world, i, j, k))
        {
            world.setTile(i, j, k, 0);
        }
        if(world.getTileId(i, j - 1, k) == mod_FCBetterThanWolves.fcBBQ.id)
        {
            if(((FCBlockBBQ)mod_FCBetterThanWolves.fcBBQ).IsBBQLit(world, i, j - 1, k))
            {
                flag = true;
            } else
            {
                world.setTile(i, j, k, 0);
                return;
            }
        } else
        {
            world.placeBlockWithMetaData(i, j, k, BlockBase.FIRE.id, 15);
        }
        int iMetaData = world.getTileMeta(i, j, k);
        if(iMetaData < 15)
        {
            iMetaData++;
            world.setTileMeta(i, j, k, iMetaData);
        }
        ((FireAccessor) this).invokeFireTick(world, i + 1, j, k, 300, random, 15);
        ((FireAccessor) this).invokeFireTick(world, i - 1, j, k, 300, random, 15);
        ((FireAccessor) this).invokeFireTick(world, i, j - 1, k, 250, random, 15);
        ((FireAccessor) this).invokeFireTick(world, i, j + 1, k, 250, random, 15);
        ((FireAccessor) this).invokeFireTick(world, i, j, k - 1, 300, random, 15);
        ((FireAccessor) this).invokeFireTick(world, i, j, k + 1, 300, random, 15);
        for(int i1 = i - 1; i1 <= i + 1; i1++)
        {
            for(int j1 = k - 1; j1 <= k + 1; j1++)
            {
                for(int k1 = j - 1; k1 <= j + 4; k1++)
                {
                    if(i1 == i && k1 == j && j1 == k)
                    {
                        continue;
                    }
                    int l1 = 100;
                    if(k1 > j + 1)
                    {
                        l1 += (k1 - (j + 1)) * 100;
                    }
                    int i2 = ((FireAccessor) this).invokeMethod_1827(world, i1, k1, j1);
                    if(i2 <= 0)
                    {
                        continue;
                    }
                    int j2 = (i2 + 40) / 45;
                    if(
                            j2 <= 0 ||
                            random.nextInt(l1) > j2 ||
                            world.isRaining() && world.canRainAt(i1, k1, j1) ||
                            world.canRainAt(i1 - 1, k1, k) ||
                            world.canRainAt(i1 + 1, k1, j1) ||
                            world.canRainAt(i1, k1, j1 - 1) ||
                            world.canRainAt(i1, k1, j1 + 1))
                    {
                        continue;
                    }
                    int k2 = iMetaData + random.nextInt(5) / 4;
                    if(k2 > 15)
                    {
                        k2 = 15;
                    }
                    world.placeBlockWithMetaData(i1, k1, j1, BlockBase.FIRE.id, k2);
                }

            }

        }

        if(iMetaData >= 3)
        {
            world.placeBlockWithMetaData(i, j, k, BlockBase.FIRE.id, 15);
        }
        world.method_216(i, j, k, id, getTickrate());
    }

}
