// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCBlockDetectorBlock.java

package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.entity.BroadheadArrowEntity;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.mixin.LevelAccessor;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.projectile.Arrow;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.util.Random;


public class DetectorBlock extends TemplateBlockBase
    implements RotatableBlock
{

    public DetectorBlock(Identifier iid)
    {
        super(iid, Material.STONE);
        texture = 10;
        setLightOpacity(255);
        setTicksRandomly(true);
    }

    public int getTextureForSide(BlockView iblockaccess, int i, int j, int k, int l)
    {
        int iFacing = GetFacing(iblockaccess, i, j, k);
        if(l == iFacing)
        {
            if(IsBlockOn(iblockaccess, i, j, k))
            {
                return TextureListener.detector_on;
            } else
            {
                return TextureListener.detector_off;
            }
        }
        if(l == 1)
        {
            return TextureListener.detector_top;
        }
        if(l == 0)
        {
            return TextureListener.detector_bottom;
        } else
        {
            return TextureListener.detector_side;
        }
    }

    public int getTextureForSide(int i)
    {
        if(i == 3)
        {
            return TextureListener.detector_off;
        }
        if(i == 1)
        {
            return TextureListener.detector_top;
        }
        if(i == 0)
        {
            return TextureListener.detector_bottom;
        } else
        {
            return TextureListener.detector_side;
        }
    }

    public int getTickrate()
    {
        return 4;
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public boolean IsBlockOn(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getTileMeta(i, j, k) & 1) > 0;
    }

    public void SetBlockOn(Level world, int i, int j, int k, boolean bOn)
    {
        if(bOn != IsBlockOn(world, i, j, k))
        {
            int iMetaData = world.getTileMeta(i, j, k);
            if(bOn)
            {
                iMetaData |= 1;
                world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.click", 0.75F, 2.0F);
            } else
            {
                iMetaData &= -2;
            }
            world.setTileMeta(i, j, k, iMetaData);
            world.updateAdjacentBlocks(i, j, k, id);
            world.method_202(i, j, k, i, j, k);
        }
    }

    public void onBlockPlaced(Level world, int i, int j, int k, int iFacing)
    {
        SetFacing(world, i, j, k, UnsortedUtils.GetOppositeFacing(iFacing));
    }

    public void afterPlaced(Level world, int i, int j, int k, Living entityLiving)
    {
        int iFacing = UnsortedUtils.ConvertPlacingEntityOrientationToBlockFacing(entityLiving);
        SetFacing(world, i, j, k, iFacing);
    }

    public void onBlockPlaced(Level world, int i, int j, int k)
    {
        super.onBlockPlaced(world, i, j, k);
        world.method_216(i, j, k, id, getTickrate());
    }

    public void onAdjacentBlockUpdate(Level world, int i, int j, int k, int l)
    {
        world.method_216(i, j, k, id, getTickrate());
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
        boolean bPlacedLogic = PlaceDetectorLogicIfNecessary(world, i, j, k);
        boolean bDetected = CheckForDetection(world, i, j, k);
        if(!bDetected)
        {
            int iFacingDirection = GetFacing(world, i, j, k);
            if(iFacingDirection == 1)
            {
                if(UnsortedUtils.IsBlockBeingPrecipitatedOn(world, i, j + 1, k))
                {
                    bDetected = true;
                }
                world.method_216(i, j, k, id, getTickrate());
            }
        }
        if(bDetected)
        {
            if(!IsBlockOn(world, i, j, k))
            {
                SetBlockOn(world, i, j, k, true);
            }
        } else
        if(IsBlockOn(world, i, j, k))
        {
            if(!bPlacedLogic)
            {
                SetBlockOn(world, i, j, k, false);
            } else
            {
                world.method_216(i, j, k, id, getTickrate());
            }
        }
    }

    public boolean isPowered(BlockView iblockaccess, int i, int j, int k, int l)
    {
        return IsBlockOn(iblockaccess, i, j, k);
    }

    public boolean indirectlyPowered(Level world, int i, int j, int i1, int j1)
    {
        return false;
    }

    public boolean getEmitsRedstonePower()
    {
        return true;
    }

    public void onEntityCollision(Level world, int i, int j, int k, EntityBase entity)
    {
        if((entity instanceof Arrow) || (entity instanceof BroadheadArrowEntity))
        {
            int iFacingDirection = GetFacing(world, i, j, k);
            int targeti = i;
            int targetj = j;
            int targetk = k;
            if(iFacingDirection == 0)
            {
                targetj--;
            } else
            if(iFacingDirection == 1)
            {
                targetj++;
            } else
            if(iFacingDirection == 3)
            {
                targetk++;
            } else
            if(iFacingDirection == 2)
            {
                targetk--;
            } else
            if(iFacingDirection == 5)
            {
                targeti++;
            } else
            {
                targeti--;
            }
            if(world.getTileId(targeti, targetj, targetk) == BlockListener.fcBlockDetectorLogic.id)
            {
                BlockListener.fcBlockDetectorLogic.onEntityCollision(world, targeti, targetj, targetk, entity);
            }
        }
    }

    public void randomDisplayTick(Level world, int i, int j, int k, Random random)
    {
        if(IsBlockOn(world, i, j, k))
        {
            int iFacingDirection = GetFacing(world, i, j, k);
            float targeti = i;
            float targetj = j;
            float targetk = k;
            float targeti2 = targeti;
            float targetj2 = targetj;
            float targetk2 = targetk;
            if(iFacingDirection == 0)
            {
                targetj2 = targetj -= 0.2F;
                targetk2 = targetk += 0.25F;
                targeti += 0.33F;
                targeti2 += 0.66F;
            } else
            if(iFacingDirection == 1)
            {
                targetj2 = targetj += 1.1F;
                targetk2 = targetk += 0.25F;
                targeti += 0.33F;
                targeti2 += 0.66F;
            } else
            if(iFacingDirection == 3)
            {
                targetj2 = targetj += 0.75F;
                targetk2 = targetk += 1.1F;
                targeti += 0.33F;
                targeti2 += 0.66F;
            } else
            if(iFacingDirection == 2)
            {
                targetj2 = targetj += 0.75F;
                targetk2 = targetk -= 0.1F;
                targeti += 0.33F;
                targeti2 += 0.66F;
            } else
            if(iFacingDirection == 5)
            {
                targeti2 = targeti += 1.1F;
                targetj2 = targetj += 0.75F;
                targetk = (float)((double)targetk + 0.33000000000000002D);
                targetk2 += 0.66F;
            } else
            {
                targeti2 = targeti -= 0.1F;
                targetj2 = targetj += 0.75F;
                targetk += 0.33F;
                targetk2 += 0.66F;
            }
            targeti += (random.nextFloat() - 0.5F) * 0.1F;
            targetj += (random.nextFloat() - 0.5F) * 0.1F;
            targetk += (random.nextFloat() - 0.5F) * 0.1F;
            float f = 0.06666667F;
            float f1 = f * 0.6F + 0.4F;
            float f2 = f * f * 0.7F - 0.5F;
            float f3 = f * f * 0.6F - 0.7F;
            if(f2 < 0.0F)
            {
                f2 = 0.0F;
            }
            if(f3 < 0.0F)
            {
                f3 = 0.0F;
            }
            if(random.nextFloat() >= 0.5F)
            {
                world.addParticle("reddust", targeti, targetj, targetk, f1, f2, f3);
            } else
            {
                world.addParticle("reddust", targeti2, targetj2, targetk2, f1, f2, f3);
            }
        }
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getTileMeta(i, j, k) & -2) >> 1;
    }

    public void SetFacing(Level world, int i, int j, int k, int iFacing)
    {
        int iMetaData = world.getTileMeta(i, j, k);
        iMetaData = iMetaData & 1 | iFacing << 1;
        world.setTileMeta(i, j, k, iMetaData);
    }

    public boolean CanRotate(BlockView iBlockAccess, int i, int j, int l)
    {
        return true;
    }

    public boolean CanTransmitRotation(BlockView iBlockAccess, int i, int j, int l)
    {
        return true;
    }

    public void Rotate(Level world, int i, int j, int k, boolean bReverse)
    {
        int iFacing = GetFacing(world, i, j, k);
        int iNewFacing = UnsortedUtils.RotateFacingAroundJ(iFacing, bReverse);
        if(iNewFacing != iFacing)
        {
            SetFacing(world, i, j, k, iNewFacing);
            world.method_202(i, j, k, i, j, k);
            world.method_216(i, j, k, id, getTickrate());
            ((LevelAccessor) world).invokeMethod_235(i, j, k, id);
        }
    }

    public boolean PlaceDetectorLogicIfNecessary(Level world, int i, int j, int k)
    {
        int iFacingDirection = GetFacing(world, i, j, k);
        int targeti = i;
        int targetj = j;
        int targetk = k;
        if(iFacingDirection == 0)
        {
            targetj--;
        } else
        if(iFacingDirection == 1)
        {
            targetj++;
        } else
        if(iFacingDirection == 3)
        {
            targetk++;
        } else
        if(iFacingDirection == 2)
        {
            targetk--;
        } else
        if(iFacingDirection == 5)
        {
            targeti++;
        } else
        {
            targeti--;
        }
        if(world.getTileId(targeti, targetj, targetk) == 0)
        {
            world.setTile(targeti, targetj, targetk, BlockListener.fcBlockDetectorLogic.id);
            return true;
        } else
        {
            return false;
        }
    }

    public boolean CheckForDetection(Level world, int i, int j, int k)
    {
        int iFacingDirection = GetFacing(world, i, j, k);
        int targeti = i;
        int targetj = j;
        int targetk = k;
        if(iFacingDirection == 0)
        {
            targetj--;
        } else
        if(iFacingDirection == 1)
        {
            targetj++;
        } else
        if(iFacingDirection == 3)
        {
            targetk++;
        } else
        if(iFacingDirection == 2)
        {
            targetk--;
        } else
        if(iFacingDirection == 5)
        {
            targeti++;
        } else
        {
            targeti--;
        }
        int targetid = world.getTileId(targeti, targetj, targetk);
        if(targetid > 0)
        {
            if(targetid == BlockListener.fcBlockDetectorLogic.id)
            {
                if(world.getTileMeta(targeti, targetj, targetk) > 0)
                {
                    return true;
                }
            } else
            {
                return true;
            }
        }
        return false;
    }

    private final int detectorTextureIDTop = 10;
    private final int detectorTextureIDFront = 11;
    private final int detectorTextureIDFrontOn = 12;
    private final int detectorTextureIDSide = 13;
    private final int detectorTextureIDBottom = 14;
    private static final int iDetectorTickRate = 4;
    private static final int iDetectorTickRateForWeatherDetection = 100;
}
