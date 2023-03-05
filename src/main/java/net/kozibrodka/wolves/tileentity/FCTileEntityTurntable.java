// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCTileEntityTurntable.java

package net.kozibrodka.wolves.tileentity;

import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.blocks.FCBlockTurntable;
import net.kozibrodka.wolves.blocks.FCBlockUnfiredPottery;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.utils.FCBlockPos;
import net.kozibrodka.wolves.utils.FCIBlock;
import net.kozibrodka.wolves.utils.FCUtilsMisc;
import net.kozibrodka.wolves.utils.FCUtilsWorld;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBase;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.io.CompoundTag;


// Referenced classes of package net.minecraft.src:
//            TileEntity, NBTTagCompound, mod_FCBetterThanWolves, FCBlockTurntable, 
//            World, Block, FCIBlock, BlockRail, 
//            BlockRedstoneRepeater, BlockPistonBase, BlockFurnace, BlockStairs, 
//            BlockPumpkin, BlockPistonExtension, BlockPistonMoving, FCBlockPos, 
//            FCUtilsMisc, FCUtilsWorld, ModLoader, StepSound, 
//            SoundManager, Item

public class FCTileEntityTurntable extends TileEntityBase
{

    public FCTileEntityTurntable()
    {
        m_iRotationCount = 0;
        m_iSwitchSetting = 0;
        m_iPotteryRotationCount = 0;
        m_bPotteryRotated = false;
    }

    public void readIdentifyingData(CompoundTag nbttagcompound)
    {
        super.readIdentifyingData(nbttagcompound);
        m_iRotationCount = nbttagcompound.getInt("m_iRotationCount");
        m_iSwitchSetting = nbttagcompound.getInt("m_iSwitchSetting");
        if(m_iSwitchSetting > 3)
        {
            m_iSwitchSetting = 3;
        }
        if(nbttagcompound.containsKey("m_iPotteryRotationCount"))
        {
            m_iPotteryRotationCount = nbttagcompound.getInt("m_iPotteryRotationCount");
        }
    }

    public void writeIdentifyingData(CompoundTag nbttagcompound)
    {
        super.writeIdentifyingData(nbttagcompound);
        nbttagcompound.put("m_iRotationCount", m_iRotationCount);
        nbttagcompound.put("m_iSwitchSetting", m_iSwitchSetting);
        nbttagcompound.put("m_iPotteryRotationCount", m_iSwitchSetting);
    }

    public void tick()
    {
        if(((FCBlockTurntable) mod_FCBetterThanWolves.fcTurntable).IsBlockMechanicalOn(level, x, y, z))
        {
            m_iRotationCount++;
            if(m_iRotationCount >= GetTicksToRotate())
            {
                RotateTurntable();
                m_iRotationCount = 0;
            }
        } else
        {
            m_iRotationCount = 0;
        }
    }

    private int GetTicksToRotate()
    {
        return m_iTicksToRotate[m_iSwitchSetting];
    }

    private void RotateTurntable()
    {
        level.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.05F, 1.0F);
        boolean bReverseDirection = ((FCBlockTurntable)mod_FCBetterThanWolves.fcTurntable).IsBlockRedstoneOn(level, x, y, z);
        m_bPotteryRotated = false;
        int iTempJ = y + 1;
        do
        {
            if(iTempJ > y + 2)
            {
                break;
            }
            RotateBlock(x, iTempJ, z, bReverseDirection);
            if(CanBlockTransmitRotation(x, iTempJ, z))
            {
                RotateBlocksAttachedToBlock(x, iTempJ, z, bReverseDirection);
            } else
            {
                int iTempid = level.getTileId(x, iTempJ, z);
                if(iTempid == BlockBase.CLAY.id)
                {
                    RotateBlocksAttachedToBlock(x, iTempJ, z, bReverseDirection);
                }
                break;
            }
            iTempJ++;
        } while(true);
        if(!m_bPotteryRotated)
        {
            m_iPotteryRotationCount = 0;
        }
    }

    private void RotateBlock(int i, int j, int k, boolean bReverseDirection)
    {
        int iTargetid = level.getTileId(i, j, k);
        BlockBase targetBlock = BlockBase.BY_ID[iTargetid];
        if(iTargetid == BlockBase.CLAY.id)
        {
            RotateClay(i, j, k, bReverseDirection);
            m_bPotteryRotated = true;
            return;
        }
        if(targetBlock instanceof FCBlockUnfiredPottery) //if(iTargetid == mod_FCBetterThanWolves.fcUnfiredPottery.id)
        {
            RotateUnfiredPottery(i, j, k, bReverseDirection);
            m_bPotteryRotated = true;
            return;
        }
        if(targetBlock instanceof FCIBlock)
        {
            FCIBlock targetFCBlock = (FCIBlock)targetBlock;
            if(targetFCBlock.CanRotate(level, i, j, k))
            {
                targetFCBlock.Rotate(level, i, j, k, bReverseDirection);
            }
        } else
        if(targetBlock instanceof Rail)
        {
            Rail targetRail = (Rail)targetBlock;
            RotateRail(targetRail, i, j, k, bReverseDirection);
        } else
        if(iTargetid == BlockBase.REDSTONE_REPEATER_LIT.id || iTargetid == BlockBase.REDSTONE_REPEATER.id)
        {
            RedstoneRepeater targetRepeater = (RedstoneRepeater)targetBlock;
            RotateRepeater(targetRepeater, i, j, k, bReverseDirection);
        } else
        if(targetBlock instanceof Piston)
        {
            Piston targetPiston = (Piston)targetBlock;
            RotatePiston(targetPiston, i, j, k, bReverseDirection);
        } else
        if(iTargetid == BlockBase.DISPENSER.id)
        {
            RotateDispenser(i, j, k, bReverseDirection);
        } else
        if(targetBlock instanceof Furnace)
        {
            Furnace targetFurnace = (Furnace)targetBlock;
            RotateFurnace(i, j, k, bReverseDirection);
        } else
        if(targetBlock instanceof Stairs)
        {
            RotateStairs(i, j, k, bReverseDirection);
        } else
        if(targetBlock instanceof Pumpkin)
        {
            RotatePumpkin(i, j, k, bReverseDirection);
        }
    }

    private boolean CanBlockTransmitRotation(int i, int j, int k)
    {
        int iTargetid = level.getTileId(i, j, k);
        BlockBase targetBlock = BlockBase.BY_ID[iTargetid];
        if(targetBlock instanceof FCIBlock)
        {
            FCIBlock targetFCBlock = (FCIBlock)targetBlock;
            return targetFCBlock.CanTransmitRotation(level, i, j, k);
        }
        if(iTargetid == BlockBase.GLASS.id)
        {
            return true;
        }
        if(iTargetid == BlockBase.CLAY.id)
        {
            return false;
        }
        if(targetBlock instanceof Piston)
        {
            Piston blockPiston = (Piston)targetBlock;
            int iMetaData = level.getTileMeta(i, j, k);
            Piston _tmp = blockPiston;
            return !Piston.isExtendedByMeta(iMetaData);
        }
        if(iTargetid == BlockBase.PISTON_HEAD.id || iTargetid == BlockBase.MOVING_PISTON.id)
        {
            return false;
        } else
        {
            return level.canSuffocate(i, j, k);
        }
    }

    private void RotateBlocksAttachedToBlock(int i, int j, int k, boolean bReverseDirection)
    {
        int newids[] = new int[4];
        for(int iTempIndex = 0; iTempIndex < 4; iTempIndex++)
        {
            newids[iTempIndex] = 0;
        }

        for(int iTempFacing = 2; iTempFacing <= 5; iTempFacing++)
        {
            FCBlockPos tempPos = new FCBlockPos(i, j, k);
            tempPos.AddFacingAsOffset(iTempFacing);
            int iTempid = level.getTileId(tempPos.i, tempPos.j, tempPos.k);
            int iTempMetaData = level.getTileMeta(tempPos.i, tempPos.j, tempPos.k);
            boolean bAttached = false;
            if(iTempid == BlockBase.TORCH.id || iTempid == BlockBase.REDSTONE_TORCH_LIT.id || iTempid == BlockBase.REDSTONE_TORCH.id)
            {
                if(iTempMetaData == 1 && iTempFacing == 5 || iTempMetaData == 2 && iTempFacing == 4 || iTempMetaData == 3 && iTempFacing == 3 || iTempMetaData == 4 && iTempFacing == 2)
                {
                    bAttached = true;
                    if(iTempid == BlockBase.REDSTONE_TORCH.id)
                    {
                        iTempid = BlockBase.REDSTONE_TORCH_LIT.id;
                    }
                }
            } else
            if(iTempid == BlockBase.LADDER.id)
            {
                if(iTempMetaData == iTempFacing)
                {
                    bAttached = true;
                }
            } else
            if(iTempid == BlockBase.WALL_SIGN.id)
            {
                BlockBase tempBlock = BlockBase.BY_ID[iTempid];
                if(iTempMetaData == iTempFacing)
                {
                    tempBlock.drop(level, tempPos.i, tempPos.j, tempPos.k, iTempid);
                    level.setTile(tempPos.i, tempPos.j, tempPos.k, 0);
                }
            } else
            if(iTempid == BlockBase.BUTTON.id || iTempid == BlockBase.LEVER.id)
            {
                BlockBase tempBlock = BlockBase.BY_ID[iTempid];
                if(iTempMetaData == 1 && iTempFacing == 5 || iTempMetaData == 2 && iTempFacing == 4 || iTempMetaData == 3 && iTempFacing == 3 || iTempMetaData == 4 && iTempFacing == 2)
                {
                    tempBlock.drop(level, tempPos.i, tempPos.j, tempPos.k, iTempid);
                    level.setTile(tempPos.i, tempPos.j, tempPos.k, 0);
                }
            }
            if(bAttached)
            {
                int iDestFacing = FCUtilsMisc.RotateFacingAroundJ(iTempFacing, bReverseDirection);
                newids[iDestFacing - 2] = iTempid;
                level.setTile(tempPos.i, tempPos.j, tempPos.k, 0);
            }
        }

        for(int iTempIndex = 0; iTempIndex < 4; iTempIndex++)
        {
            int iTempid = newids[iTempIndex];
            if(iTempid == 0)
            {
                continue;
            }
            int iTempFacing = iTempIndex + 2;
            int iTempMetaData = 0;
            FCBlockPos tempPos = new FCBlockPos(i, j, k);
            tempPos.AddFacingAsOffset(iTempFacing);
            if(iTempid == BlockBase.TORCH.id || iTempid == BlockBase.REDSTONE_TORCH_LIT.id)
            {
                int iTargetFacing = 0;
                if(iTempFacing == 2)
                {
                    iTargetFacing = 4;
                } else
                if(iTempFacing == 3)
                {
                    iTargetFacing = 3;
                } else
                if(iTempFacing == 4)
                {
                    iTargetFacing = 2;
                } else
                if(iTempFacing == 5)
                {
                    iTargetFacing = 1;
                }
                iTempMetaData = iTargetFacing;
            } else
            if(iTempid == BlockBase.LADDER.id)
            {
                iTempMetaData = iTempFacing;
            }
            if(FCUtilsWorld.IsReplaceableBlock(level, tempPos.i, tempPos.j, tempPos.k))
            {
                level.setTile(tempPos.i, tempPos.j, tempPos.k, iTempid);
                level.setTileMeta(tempPos.i, tempPos.j, tempPos.k, iTempMetaData);
                level.method_202(tempPos.i, tempPos.j, tempPos.k, tempPos.i, tempPos.j, tempPos.k);
            } else
            {
                BlockBase tempBlock = BlockBase.BY_ID[iTempid];
                int iOldFacing = FCUtilsMisc.RotateFacingAroundJ(iTempFacing, !bReverseDirection);
                FCBlockPos oldPos = new FCBlockPos(i, j, k);
                oldPos.AddFacingAsOffset(iOldFacing);
                tempBlock.drop(level, oldPos.i, oldPos.j, oldPos.k, iTempid);
            }
        }

    }

    private void RotatePiston(Piston blockPiston, int i, int j, int k, boolean bReverseDirection)
    {
        int iMetaData = level.getTileMeta(i, j, k);
        Piston _tmp = blockPiston;
        if(!Piston.isExtendedByMeta(iMetaData))
        {
            int iDirection = iMetaData & 7;
            int iNewDirection = FCUtilsMisc.RotateFacingAroundJ(iDirection, bReverseDirection);
            if(iDirection != iNewDirection)
            {
                iMetaData = iMetaData & -8 | iNewDirection;
                level.setTileMeta(i, j, k, iMetaData);
                level.method_202(i, j, k, i, j, k);
            }
        }
    }

    private void RotateRepeater(RedstoneRepeater blockRepeater, int i, int j, int k, boolean bReverseDirection)
    {
        int iMetaData = level.getTileMeta(i, j, k);
        int iDirection = iMetaData & 3;
        if(bReverseDirection)
        {
            if(++iDirection > 3)
            {
                iDirection = 0;
            }
        } else
        if(--iDirection < 0)
        {
            iDirection = 3;
        }
        iMetaData = iMetaData & -4 | iDirection;
        level.setTileMeta(i, j, k, iMetaData);
        level.method_202(i, j, k, i, j, k);
        blockRepeater.onAdjacentBlockUpdate(level, i, j, k, 0);
    }

    private void RotateRail(Rail blockRail, int i, int j, int k, boolean bReverseDirection)
    {
        int iMetaData = level.getTileMeta(i, j, k);
        int iDirection = iMetaData;
        if(blockRail.method_1108())
        {
            iDirection &= 7;
        }
        if(iDirection == 0)
        {
            iDirection = 1;
        } else
        if(iDirection == 1)
        {
            iDirection = 0;
        } else
        if(iDirection != 2 && iDirection != 3 && iDirection != 4 && iDirection != 5)
        {
            if(iDirection == 6)
            {
                if(bReverseDirection)
                {
                    iDirection = 7;
                } else
                {
                    iDirection = 9;
                }
            } else
            if(iDirection == 7)
            {
                if(bReverseDirection)
                {
                    iDirection = 8;
                } else
                {
                    iDirection = 6;
                }
            } else
            if(iDirection == 8)
            {
                if(bReverseDirection)
                {
                    iDirection = 9;
                } else
                {
                    iDirection = 7;
                }
            } else
            if(iDirection == 9)
            {
                if(bReverseDirection)
                {
                    iDirection = 6;
                } else
                {
                    iDirection = 8;
                }
            }
        }
        if(blockRail.method_1108())
        {
            iMetaData = iMetaData & 8 | iDirection;
        } else
        {
            iMetaData = iDirection;
        }
        level.setTileMeta(i, j, k, iMetaData);
        level.method_202(i, j, k, i, j, k);
    }

    private void RotateDispenser(int i, int j, int k, boolean bReverseDirection)
    {
        int iMetaData = level.getTileMeta(i, j, k);
        int iDirection = iMetaData;
        iDirection = FCUtilsMisc.RotateFacingAroundJ(iDirection, bReverseDirection);
        iMetaData = iDirection;
        level.setTileMeta(i, j, k, iMetaData);
        level.method_202(i, j, k, i, j, k);
    }

    private void RotateFurnace(int i, int j, int k, boolean bReverseDirection)
    {
        int iMetaData = level.getTileMeta(i, j, k);
        int iDirection = iMetaData;
        iDirection = FCUtilsMisc.RotateFacingAroundJ(iDirection, bReverseDirection);
        iMetaData = iDirection;
        level.setTileMeta(i, j, k, iMetaData);
        level.method_202(i, j, k, i, j, k);
    }

    private void RotateStairs(int i, int j, int k, boolean bReverseDirection)
    {
        int iMetaData = level.getTileMeta(i, j, k);
        int iDirection = iMetaData + 2;
        iDirection = FCUtilsMisc.RotateFacingAroundJ(iDirection, !bReverseDirection);
        iMetaData = iDirection - 2;
        level.setTileMeta(i, j, k, iMetaData);
        level.method_202(i, j, k, i, j, k);
    }

    private void RotatePumpkin(int i, int j, int k, boolean bReverseDirection)
    {
        int iMetaData = level.getTileMeta(i, j, k);
        int iDirection = iMetaData;
        if(bReverseDirection)
        {
            if(++iDirection > 3)
            {
                iDirection = 0;
            }
        } else
        if(--iDirection < 0)
        {
            iDirection = 3;
        }
        iMetaData = iDirection;
        level.setTileMeta(i, j, k, iMetaData);
        level.method_202(i, j, k, i, j, k);
    }

    private void RotateClay(int i, int j, int k, boolean bReverseDirection)
    {
        BlockBase targetBlock = BlockBase.CLAY;
        Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).soundHelper.playSound(targetBlock.sounds.getWalkSound(), (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, (targetBlock.sounds.getVolume() + 1.0F) / 2.0F, targetBlock.sounds.getPitch() * 0.8F);
        m_iPotteryRotationCount++;
        if(m_iPotteryRotationCount >= 8)
        {
            level.setTile(i, j, k, mod_FCBetterThanWolves.fcUnfiredPottery.id);
            level.method_202(i, j, k, i, j, k);
            FCUtilsMisc.EjectSingleItemWithRandomOffset(level, i, j + 1, k, ItemBase.clay.id, 0);
            m_iPotteryRotationCount = 0;
        }
    }

    private void RotateUnfiredPottery(int i, int j, int k, boolean bReverseDirection)
    {
//        BlockBase targetBlock = mod_FCBetterThanWolves.fcUnfiredPottery_crucible;
        BlockBase targetBlock = BlockBase.CLAY;
        Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).soundHelper.playSound(targetBlock.sounds.getWalkSound(), (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, (targetBlock.sounds.getVolume() + 1.0F) / 2.0F, targetBlock.sounds.getPitch() * 0.8F);
        m_iPotteryRotationCount++;
        if(m_iPotteryRotationCount >= 8)
        {
            int iMetaData = level.getTileMeta(i, j, k);
            if(iMetaData < 2)
            {
                if(iMetaData == 1)
                {
                    FCUtilsMisc.EjectSingleItemWithRandomOffset(level, i, j + 1, k, ItemBase.clay.id, 0);
                }
                iMetaData++;
                level.setTileMeta(i, j, k, iMetaData);
                level.method_202(i, j, k, i, j, k);
//                mod_FCBetterThanWolves.sendData(this, level, i, j, k);
            } else
            {
                level.setTile(i, j, k, 0);
                for(int iTemp = 0; iTemp < 2; iTemp++)
                {
                    FCUtilsMisc.EjectSingleItemWithRandomOffset(level, i, j, k, ItemBase.clay.id, 0);
                }

            }
            m_iPotteryRotationCount = 0;
        }
    }

    private final int m_iMaxHeightOfBlocksRotated = 2;
    private final int m_iRotationsToSpinPottery = 8;
    private int m_iRotationCount;
    public int m_iSwitchSetting;
    public int m_iPotteryRotationCount;
    private boolean m_bPotteryRotated;
    private static int m_iTicksToRotate[] = {
        10, 20, 40, 80, 200, 600, 1200, 2400, 6000, 12000, 
        24000
    };

}
