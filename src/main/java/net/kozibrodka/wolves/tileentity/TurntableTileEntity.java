// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCTileEntityTurntable.java

package net.kozibrodka.wolves.tileentity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.blocks.Turntable;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.recipe.TurntableRecipeRegistry;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.kozibrodka.wolves.utils.ReplaceableBlockChecker;
import net.minecraft.block.*;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;

import java.util.List;


// Referenced classes of package net.minecraft.src:
//            TileEntity, NBTTagCompound, mod_FCBetterThanWolves, FCBlockTurntable, 
//            World, Block, FCIBlock, BlockRail, 
//            BlockRedstoneRepeater, BlockPistonBase, BlockFurnace, BlockStairs, 
//            BlockPumpkin, BlockPistonExtension, BlockPistonMoving, FCBlockPos, 
//            FCUtilsMisc, FCUtilsWorld, ModLoader, StepSound, 
//            SoundManager, Item

public class TurntableTileEntity extends TileEntityBase
{

    public TurntableTileEntity()
    {
        rotationCount = 0;
        switchSetting = 0;
        craftingRotationCount = 0;
    }

    public void readIdentifyingData(CompoundTag nbttagcompound)
    {
        super.readIdentifyingData(nbttagcompound);
        rotationCount = nbttagcompound.getInt("rotationCount");
        switchSetting = nbttagcompound.getInt("switchSetting");
        if(switchSetting > 3)
        {
            switchSetting = 3;
        }
        if(nbttagcompound.containsKey("craftingRotationCount"))
        {
            craftingRotationCount = nbttagcompound.getInt("craftingRotationCount");
        }
    }

    public void writeIdentifyingData(CompoundTag nbttagcompound)
    {
        super.writeIdentifyingData(nbttagcompound);
        nbttagcompound.put("rotationCount", rotationCount);
        nbttagcompound.put("switchSetting", switchSetting);
        nbttagcompound.put("craftingRotationCount", switchSetting);
    }

    public void tick()
    {
        if(level.isServerSide){
            return;
        }
        if(((Turntable) BlockListener.turntable).IsBlockMechanicalOn(level, x, y, z))
        {
            rotationCount++;
            if(rotationCount >= GetTicksToRotate())
            {
                rotateTurntable();
                rotationCount = 0;
            }
        } else
        {
            rotationCount = 0;
        }
    }

    private int GetTicksToRotate()
    {
        return TICKS_TO_ROTATE[switchSetting];
    }

    private void rotateTurntable() {
        level.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.05F, 1.0F);
        if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
            voicePacket(level, "random.click", x, y, z, 0.05F, 1.0F);
        }
        boolean reverseDirection = ((Turntable)BlockListener.turntable).IsBlockRedstoneOn(level, x, y, z);
        int iTempJ = y + 1;
        do
        {
            if(iTempJ > y + 2)
            {
                break;
            }
            rotateBlock(x, iTempJ, z, reverseDirection);
            if(CanBlockTransmitRotation(x, iTempJ, z))
            {
                RotateBlocksAttachedToBlock(x, iTempJ, z, reverseDirection);
            } else
            {
                int iTempid = level.getTileId(x, iTempJ, z);
                if(iTempid == BlockBase.CLAY.id)
                {
                    RotateBlocksAttachedToBlock(x, iTempJ, z, reverseDirection);
                }
                break;
            }
            iTempJ++;
        } while(true);
    }

    private void rotateBlock(int i, int j, int k, boolean reverseDirection) {
        int targetId = level.getTileId(i, j, k);
        int targetMeta = level.getTileMeta(i, j, k);
        BlockBase targetBlock = BlockBase.BY_ID[targetId];
        ItemInstance[] outputs = TurntableRecipeRegistry.getInstance().getResult(new ItemInstance(targetBlock, 1, targetMeta));
        if (outputs != null) {
            level.playSound((float) i + 0.5F, (float) j + 0.5F, (float) k + 0.5F, targetBlock.sounds.getWalkSound(), (targetBlock.sounds.getVolume() + 1.0F) / 5.0F, targetBlock.sounds.getPitch() * 0.8F);
            if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                voicePacket(level, targetBlock.sounds.getWalkSound(), i, j, k, (targetBlock.sounds.getVolume() + 1.0F) / 5.0F, targetBlock.sounds.getPitch() * 0.8F);
            }
            rotateRegisteredBlock(i, j, k, outputs.clone());
            return;
        }
        Integer[][] rotationMeta = TurntableRecipeRegistry.getInstance().getRotationValue(targetId);
        if (rotationMeta != null) {
            int matrixIndex = 0;
            int currentIndex = 0;
            boolean foundMeta = false;
            for (; matrixIndex < rotationMeta.length; matrixIndex++) {
                for (currentIndex = 0; currentIndex < rotationMeta[matrixIndex].length; currentIndex++) {
                    if (rotationMeta[matrixIndex][currentIndex] == targetMeta) {
                        foundMeta = true;
                        break;
                    }
                }
                if (foundMeta) break;
            }
            if (foundMeta) {
                if (reverseDirection) {
                    currentIndex++;
                } else {
                    currentIndex--;
                }
                if (currentIndex < 0) {
                    currentIndex = rotationMeta[matrixIndex].length - 1;
                } else if (currentIndex >= rotationMeta[matrixIndex].length) {
                    currentIndex = 0;
                }
                level.setTileMeta(i, j, k, rotationMeta[matrixIndex][currentIndex]);
                level.method_202(i, j, k, i, j, k);
                return;
            }
        }
        if(targetBlock instanceof RotatableBlock)
        {
            RotatableBlock targetFCBlock = (RotatableBlock)targetBlock;
            if(targetFCBlock.CanRotate(level, i, j, k))
            {
                targetFCBlock.Rotate(level, i, j, k, reverseDirection);
            }
        } else
        if(targetBlock instanceof Rail)
        {
            Rail targetRail = (Rail)targetBlock;
            RotateRail(targetRail, i, j, k, reverseDirection);
        } else
        if(targetBlock instanceof Stairs)
        {
            RotateStairs(i, j, k, reverseDirection);
        }
    }

    private void rotateRegisteredBlock(int x, int y, int z, ItemInstance[] outputs) {
        if (craftingRotationCount < outputs[0].count) {
            craftingRotationCount++;
            return;
        }
        craftingRotationCount = 0;
        level.setTileWithMetadata(x, y, z, outputs[0].itemId, outputs[0].getDamage());
        level.method_202(x, y, z, x, y, z);
        if (outputs[1] != null) {
            UnsortedUtils.ejectStackWithRandomOffset(level, x, y + 1, z, outputs[1]);
        }
    }

    private boolean CanBlockTransmitRotation(int i, int j, int k)
    {
        int iTargetid = level.getTileId(i, j, k);
        BlockBase targetBlock = BlockBase.BY_ID[iTargetid];
        if(targetBlock instanceof RotatableBlock)
        {
            RotatableBlock targetFCBlock = (RotatableBlock)targetBlock;
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
            BlockPosition tempPos = new BlockPosition(i, j, k);
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
                int iDestFacing = UnsortedUtils.RotateFacingAroundJ(iTempFacing, bReverseDirection);
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
            BlockPosition tempPos = new BlockPosition(i, j, k);
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
            if(ReplaceableBlockChecker.IsReplaceableBlock(level, tempPos.i, tempPos.j, tempPos.k))
            {
                level.setTile(tempPos.i, tempPos.j, tempPos.k, iTempid);
                level.setTileMeta(tempPos.i, tempPos.j, tempPos.k, iTempMetaData);
                level.method_202(tempPos.i, tempPos.j, tempPos.k, tempPos.i, tempPos.j, tempPos.k);
            } else
            {
                BlockBase tempBlock = BlockBase.BY_ID[iTempid];
                int iOldFacing = UnsortedUtils.RotateFacingAroundJ(iTempFacing, !bReverseDirection);
                BlockPosition oldPos = new BlockPosition(i, j, k);
                oldPos.AddFacingAsOffset(iOldFacing);
                tempBlock.drop(level, oldPos.i, oldPos.j, oldPos.k, iTempid);
            }
        }

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

    private void RotateStairs(int i, int j, int k, boolean bReverseDirection)
    {
        int iMetaData = level.getTileMeta(i, j, k);
        int iDirection = iMetaData + 2;
        iDirection = UnsortedUtils.RotateFacingAroundJ(iDirection, !bReverseDirection);
        iMetaData = iDirection - 2;
        level.setTileMeta(i, j, k, iMetaData);
        level.method_202(i, j, k, i, j, k);
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(Level world, String name, int x, int y, int z, float g, float h){
        List list2 = world.players;
        if(list2.size() != 0) {
            for(int k = 0; k < list2.size(); k++)
            {
                ServerPlayer player1 = (ServerPlayer) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g,h));
            }
        }
    }

    // Maximum rotation height: 2
    private int rotationCount;
    public int switchSetting;
    public int craftingRotationCount;
    private static final int[] TICKS_TO_ROTATE = {
        10, 20, 40, 80, 200, 600, 1200, 2400, 6000, 12000, 
        24000
    };

}
