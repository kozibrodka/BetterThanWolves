// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCTileEntityTurntable.java

package net.kozibrodka.wolves.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.block.TurntableBlock;
import net.kozibrodka.wolves.block.UnfiredPotteryBlock;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.kozibrodka.wolves.utils.ReplaceableBlockChecker;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;

import java.util.List;


// Referenced classes of package net.minecraft.src:
//            TileEntity, NBTTagCompound, mod_FCBetterThanWolves, FCBlockTurntable, 
//            World, Block, FCIBlock, BlockRail, 
//            BlockRedstoneRepeater, BlockPistonBase, BlockFurnace, BlockStairs, 
//            BlockPumpkin, BlockPistonExtension, BlockPistonMoving, FCBlockPos, 
//            FCUtilsMisc, FCUtilsWorld, ModLoader, StepSound, 
//            SoundManager, Item

public class TurntableBlockEntity extends BlockEntity
{

    public TurntableBlockEntity()
    {
        m_iRotationCount = 0;
        m_iSwitchSetting = 0;
        m_iPotteryRotationCount = 0;
        m_bPotteryRotated = false;
    }

    public void readNbt(NbtCompound nbttagcompound)
    {
        super.readNbt(nbttagcompound);
        m_iRotationCount = nbttagcompound.getInt("m_iRotationCount");
        m_iSwitchSetting = nbttagcompound.getInt("m_iSwitchSetting");
        if(m_iSwitchSetting > 3)
        {
            m_iSwitchSetting = 3;
        }
        if(nbttagcompound.contains("m_iPotteryRotationCount"))
        {
            m_iPotteryRotationCount = nbttagcompound.getInt("m_iPotteryRotationCount");
        }
    }

    public void writeNbt(NbtCompound nbttagcompound)
    {
        super.writeNbt(nbttagcompound);
        nbttagcompound.putInt("m_iRotationCount", m_iRotationCount);
        nbttagcompound.putInt("m_iSwitchSetting", m_iSwitchSetting);
        nbttagcompound.putInt("m_iPotteryRotationCount", m_iSwitchSetting);
    }

    public void method_1076()
    {
        if(world.isRemote){
            return;
        }
        if(((TurntableBlock) BlockListener.turntable).IsBlockMechanicalOn(world, x, y, z))
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
        world.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.05F, 1.0F);
        if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
            voicePacket(world, "random.click", x, y, z, 0.05F, 1.0F);
        }
        boolean bReverseDirection = ((TurntableBlock)BlockListener.turntable).IsBlockRedstoneOn(world, x, y, z);
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
                int iTempid = world.getBlockId(x, iTempJ, z);
                if(iTempid == Block.CLAY.id)
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
        int iTargetid = world.getBlockId(i, j, k);
        Block targetBlock = Block.BLOCKS[iTargetid];
        if(iTargetid == Block.CLAY.id)
        {
            RotateClay(i, j, k, bReverseDirection);
            m_bPotteryRotated = true;
            return;
        }
        if(targetBlock instanceof UnfiredPotteryBlock) //if(iTargetid == mod_FCBetterThanWolves.unfiredPottery.id)
        {
            RotateUnfiredPottery(i, j, k, bReverseDirection);
            m_bPotteryRotated = true;
            return;
        }
        if(targetBlock instanceof RotatableBlock)
        {
            RotatableBlock targetFCBlock = (RotatableBlock)targetBlock;
            if(targetFCBlock.CanRotate(world, i, j, k))
            {
                targetFCBlock.Rotate(world, i, j, k, bReverseDirection);
            }
        } else
        if(targetBlock instanceof RailBlock)
        {
            RailBlock targetRail = (RailBlock)targetBlock;
            RotateRail(targetRail, i, j, k, bReverseDirection);
        } else
        if(iTargetid == Block.POWERED_REPEATER.id || iTargetid == Block.REPEATER.id)
        {
            RepeaterBlock targetRepeater = (RepeaterBlock)targetBlock;
            RotateRepeater(targetRepeater, i, j, k, bReverseDirection);
        } else
        if(targetBlock instanceof PistonBlock)
        {
            PistonBlock targetPiston = (PistonBlock)targetBlock;
            RotatePiston(targetPiston, i, j, k, bReverseDirection);
        } else
        if(iTargetid == Block.DISPENSER.id)
        {
            RotateDispenser(i, j, k, bReverseDirection);
        } else
        if(targetBlock instanceof FurnaceBlock)
        {
            FurnaceBlock targetFurnace = (FurnaceBlock)targetBlock;
            RotateFurnace(i, j, k, bReverseDirection);
        } else
        if(targetBlock instanceof StairsBlock)
        {
            RotateStairs(i, j, k, bReverseDirection);
        } else
        if(targetBlock instanceof PumpkinBlock)
        {
            RotatePumpkin(i, j, k, bReverseDirection);
        }
    }

    private boolean CanBlockTransmitRotation(int i, int j, int k)
    {
        int iTargetid = world.getBlockId(i, j, k);
        Block targetBlock = Block.BLOCKS[iTargetid];
        if(targetBlock instanceof RotatableBlock)
        {
            RotatableBlock targetFCBlock = (RotatableBlock)targetBlock;
            return targetFCBlock.CanTransmitRotation(world, i, j, k);
        }
        if(iTargetid == Block.GLASS.id)
        {
            return true;
        }
        if(iTargetid == Block.CLAY.id)
        {
            return false;
        }
        if(targetBlock instanceof PistonBlock)
        {
            PistonBlock blockPiston = (PistonBlock)targetBlock;
            int iMetaData = world.getBlockMeta(i, j, k);
            PistonBlock _tmp = blockPiston;
            return !PistonBlock.method_762(iMetaData);
        }
        if(iTargetid == Block.PISTON_HEAD.id || iTargetid == Block.MOVING_PISTON.id)
        {
            return false;
        } else
        {
            return world.method_1780(i, j, k);
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
            BlockPosition tempPos = new BlockPosition(i, j, k);
            tempPos.AddFacingAsOffset(iTempFacing);
            int iTempid = world.getBlockId(tempPos.i, tempPos.j, tempPos.k);
            int iTempMetaData = world.getBlockMeta(tempPos.i, tempPos.j, tempPos.k);
            boolean bAttached = false;
            if(iTempid == Block.TORCH.id || iTempid == Block.LIT_REDSTONE_TORCH.id || iTempid == Block.REDSTONE_TORCH.id)
            {
                if(iTempMetaData == 1 && iTempFacing == 5 || iTempMetaData == 2 && iTempFacing == 4 || iTempMetaData == 3 && iTempFacing == 3 || iTempMetaData == 4 && iTempFacing == 2)
                {
                    bAttached = true;
                    if(iTempid == Block.REDSTONE_TORCH.id)
                    {
                        iTempid = Block.LIT_REDSTONE_TORCH.id;
                    }
                }
            } else
            if(iTempid == Block.LADDER.id)
            {
                if(iTempMetaData == iTempFacing)
                {
                    bAttached = true;
                }
            } else
            if(iTempid == Block.WALL_SIGN.id)
            {
                Block tempBlock = Block.BLOCKS[iTempid];
                if(iTempMetaData == iTempFacing)
                {
                    tempBlock.dropStacks(world, tempPos.i, tempPos.j, tempPos.k, iTempid);
                    world.setBlock(tempPos.i, tempPos.j, tempPos.k, 0);
                }
            } else
            if(iTempid == Block.BUTTON.id || iTempid == Block.LEVER.id)
            {
                Block tempBlock = Block.BLOCKS[iTempid];
                if(iTempMetaData == 1 && iTempFacing == 5 || iTempMetaData == 2 && iTempFacing == 4 || iTempMetaData == 3 && iTempFacing == 3 || iTempMetaData == 4 && iTempFacing == 2)
                {
                    tempBlock.dropStacks(world, tempPos.i, tempPos.j, tempPos.k, iTempid);
                    world.setBlock(tempPos.i, tempPos.j, tempPos.k, 0);
                }
            }
            if(bAttached)
            {
                int iDestFacing = UnsortedUtils.RotateFacingAroundJ(iTempFacing, bReverseDirection);
                newids[iDestFacing - 2] = iTempid;
                world.setBlock(tempPos.i, tempPos.j, tempPos.k, 0);
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
            BlockPosition tempPos = new BlockPosition(i, j, k);
            tempPos.AddFacingAsOffset(iTempFacing);
            if(iTempid == Block.TORCH.id || iTempid == Block.LIT_REDSTONE_TORCH.id)
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
            if(iTempid == Block.LADDER.id)
            {
                iTempMetaData = iTempFacing;
            }
            if(ReplaceableBlockChecker.IsReplaceableBlock(world, tempPos.i, tempPos.j, tempPos.k))
            {
                world.setBlock(tempPos.i, tempPos.j, tempPos.k, iTempid);
                world.method_215(tempPos.i, tempPos.j, tempPos.k, iTempMetaData);
                world.method_202(tempPos.i, tempPos.j, tempPos.k, tempPos.i, tempPos.j, tempPos.k);
            } else
            {
                Block tempBlock = Block.BLOCKS[iTempid];
                int iOldFacing = UnsortedUtils.RotateFacingAroundJ(iTempFacing, !bReverseDirection);
                BlockPosition oldPos = new BlockPosition(i, j, k);
                oldPos.AddFacingAsOffset(iOldFacing);
                tempBlock.dropStacks(world, oldPos.i, oldPos.j, oldPos.k, iTempid);
            }
        }

    }

    private void RotatePiston(PistonBlock blockPiston, int i, int j, int k, boolean bReverseDirection)
    {
        int iMetaData = world.getBlockMeta(i, j, k);
        PistonBlock _tmp = blockPiston;
        if(!PistonBlock.method_762(iMetaData))
        {
            int iDirection = iMetaData & 7;
            int iNewDirection = UnsortedUtils.RotateFacingAroundJ(iDirection, bReverseDirection);
            if(iDirection != iNewDirection)
            {
                iMetaData = iMetaData & -8 | iNewDirection;
                world.method_215(i, j, k, iMetaData);
                world.method_202(i, j, k, i, j, k);
            }
        }
    }

    private void RotateRepeater(RepeaterBlock blockRepeater, int i, int j, int k, boolean bReverseDirection)
    {
        int iMetaData = world.getBlockMeta(i, j, k);
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
        world.method_215(i, j, k, iMetaData);
        world.method_202(i, j, k, i, j, k);
        blockRepeater.neighborUpdate(world, i, j, k, 0);
    }

    private void RotateRail(RailBlock blockRail, int i, int j, int k, boolean bReverseDirection)
    {
        int iMetaData = world.getBlockMeta(i, j, k);
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
        world.method_215(i, j, k, iMetaData);
        world.method_202(i, j, k, i, j, k);
    }

    private void RotateDispenser(int i, int j, int k, boolean bReverseDirection)
    {
        int iMetaData = world.getBlockMeta(i, j, k);
        int iDirection = iMetaData;
        iDirection = UnsortedUtils.RotateFacingAroundJ(iDirection, bReverseDirection);
        iMetaData = iDirection;
        world.method_215(i, j, k, iMetaData);
        world.method_202(i, j, k, i, j, k);
    }

    private void RotateFurnace(int i, int j, int k, boolean bReverseDirection)
    {
        int iMetaData = world.getBlockMeta(i, j, k);
        int iDirection = iMetaData;
        iDirection = UnsortedUtils.RotateFacingAroundJ(iDirection, bReverseDirection);
        iMetaData = iDirection;
        world.method_215(i, j, k, iMetaData);
        world.method_202(i, j, k, i, j, k);
    }

    private void RotateStairs(int i, int j, int k, boolean bReverseDirection)
    {
        int iMetaData = world.getBlockMeta(i, j, k);
        int iDirection = iMetaData + 2;
        iDirection = UnsortedUtils.RotateFacingAroundJ(iDirection, !bReverseDirection);
        iMetaData = iDirection - 2;
        world.method_215(i, j, k, iMetaData);
        world.method_202(i, j, k, i, j, k);
    }

    private void RotatePumpkin(int i, int j, int k, boolean bReverseDirection)
    {
        int iMetaData = world.getBlockMeta(i, j, k);
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
        world.method_215(i, j, k, iMetaData);
        world.method_202(i, j, k, i, j, k);
    }

    private void RotateClay(int i, int j, int k, boolean bReverseDirection)
    {
        Block targetBlock = Block.CLAY;
        world.playSound((float) i + 0.5F, (float) j + 0.5F, (float) k + 0.5F, targetBlock.soundGroup.getSound(), (targetBlock.soundGroup.method_1976() + 1.0F) / 5.0F, targetBlock.soundGroup.method_1977() * 0.8F);
        if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
            voicePacket(world, targetBlock.soundGroup.getSound(), i, j, k, (targetBlock.soundGroup.method_1976() + 1.0F) / 5.0F, targetBlock.soundGroup.method_1977() * 0.8F);
        }
        m_iPotteryRotationCount++;
        if(m_iPotteryRotationCount >= 8)
        {
            world.setBlock(i, j, k, BlockListener.unfiredPottery.id);
            world.method_202(i, j, k, i, j, k);
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j + 1, k, Item.CLAY.id, 0);
            m_iPotteryRotationCount = 0;
        }
    }

    private void RotateUnfiredPottery(int i, int j, int k, boolean bReverseDirection)
    {
        Block targetBlock = Block.CLAY;
        world.playSound((float) i + 0.5F, (float) j + 0.5F, (float) k + 0.5F, targetBlock.soundGroup.getSound(), (targetBlock.soundGroup.method_1976() + 1.0F) / 5.0F, targetBlock.soundGroup.method_1977() * 0.8F);
        if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
            voicePacket(world, targetBlock.soundGroup.getSound(), i, j, k, (targetBlock.soundGroup.method_1976() + 1.0F) / 5.0F, targetBlock.soundGroup.method_1977() * 0.8F);
        }
        m_iPotteryRotationCount++;
        if(m_iPotteryRotationCount >= 8)
        {
            int iMetaData = world.getBlockMeta(i, j, k);
            if(iMetaData < 2)
            {
                if(iMetaData == 1)
                {
                    UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j + 1, k, Item.CLAY.id, 0);
                }
                iMetaData++;
                world.method_215(i, j, k, iMetaData);
                world.method_243(i, j, k);
                world.method_202(i, j, k, i, j, k);
//                mod_FCBetterThanWolves.sendData(this, level, i, j, k);
            } else
            {
                world.setBlock(i, j, k, 0);
                world.method_202(i, j, k, i, j, k);
                for(int iTemp = 0; iTemp < 2; iTemp++)
                {
                    UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, Item.CLAY.id, 0);
                }

            }
            m_iPotteryRotationCount = 0;
        }
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(World world, String name, int x, int y, int z, float g, float h){
        List list2 = world.field_200;
        if(list2.size() != 0) {
            for(int k = 0; k < list2.size(); k++)
            {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g,h));
            }
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
