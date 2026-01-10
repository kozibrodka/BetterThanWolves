// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCTileEntityTurntable.java

package net.kozibrodka.wolves.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.block.TurntableBlock;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.recipe.*;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.ReplaceableBlockChecker;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.RailBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
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

public class TurntableBlockEntity extends BlockEntity {

    private int rotationCount;
    public int switchSetting;
    public int craftingRotationCount;
    private static final int[] TICKS_TO_ROTATE = {
            10, 20, 40, 80,
            200, 600, 1200, 2400,
            6000, 12000, 24000, 48000
    };

    public TurntableBlockEntity() {
        rotationCount = 0;
        switchSetting = 0;
    }

    public void readNbt(NbtCompound nbttagcompound) {
        super.readNbt(nbttagcompound);
        rotationCount = nbttagcompound.getInt("rotationCount");
        switchSetting = nbttagcompound.getInt("switchSetting");
        if (switchSetting > 3) {
            switchSetting = 3;
        }
        if (nbttagcompound.contains("craftingRotationCount")) {
            craftingRotationCount = nbttagcompound.getInt("craftingRotationCount");
        }
    }

    public void writeNbt(NbtCompound nbttagcompound) {
        super.writeNbt(nbttagcompound);
        nbttagcompound.putInt("rotationCount", rotationCount);
        nbttagcompound.putInt("switchSetting", switchSetting);
        nbttagcompound.putInt("craftingRotationCount", switchSetting);
    }

    public void tick() {
        if (world.isRemote) {
            return;
        }
        if (((TurntableBlock) BlockListener.turntable).IsBlockMechanicalOn(world, x, y, z)) {
            rotationCount++;
            if (rotationCount >= GetTicksToRotate()) {
                rotateTurntable();
                rotationCount = 0;
            }
        } else {
            rotationCount = 0;
        }
    }

    private int GetTicksToRotate() {
        return TICKS_TO_ROTATE[switchSetting];
    }

    private void rotateTurntable() {
        world.playSound((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.click", 0.05F, 1.0F);
        if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
            voicePacket(world, "random.click", x, y, z, 0.05F, 1.0F);
        }
        boolean reverseDirection = ((TurntableBlock) BlockListener.turntable).IsBlockRedstoneOn(world, x, y, z);
        int iTempJ = y + 1;
        do {
            if (iTempJ > y + 2) {
                break;
            }
            rotateBlock(x, iTempJ, z, reverseDirection);
            if (CanBlockTransmitRotation(x, iTempJ, z)) {
                RotateBlocksAttachedToBlock(x, iTempJ, z, reverseDirection);
            } else {
                int tempBlockId = world.getBlockId(x, iTempJ, z);
                if (tempBlockId == Block.CLAY.id) {
                    RotateBlocksAttachedToBlock(x, iTempJ, z, reverseDirection);
                }
                break;
            }
            iTempJ++;
        } while (true);
    }

    private void rotateBlock(int i, int j, int k, boolean reverseDirection) {
        int targetId = world.getBlockId(i, j, k);
        int targetMeta = world.getBlockMeta(i, j, k);
        Block targetBlock = Block.BLOCKS[targetId];
        TurntableResult result = TurntableRecipeRegistry.getInstance().getResult(new TurntableInput(targetBlock, targetMeta));
        if (result != null) {
            world.playSound((float) i + 0.5F, (float) j + 0.5F, (float) k + 0.5F, targetBlock.soundGroup.getSound(), (targetBlock.soundGroup.method_1977() + 1.0F) / 5.0F, targetBlock.soundGroup.method_1976() * 0.8F);
            if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                voicePacket(world, targetBlock.soundGroup.getSound(), i, j, k, (targetBlock.soundGroup.method_1977() + 1.0F) / 5.0F, targetBlock.soundGroup.method_1976() * 0.8F);
            }
            rotateRegisteredBlock(i, j, k, result);
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
                world.setBlockMeta(i, j, k, rotationMeta[matrixIndex][currentIndex]);
                world.setBlocksDirty(i, j, k, i, j, k);
                return;
            }
        }
        if (targetBlock instanceof RotatableBlock targetFCBlock) {
            if (targetFCBlock.canRotate(world, i, j, k)) {
                targetFCBlock.rotate(world, i, j, k, reverseDirection);
            }
        } else if (targetBlock instanceof RailBlock targetRail) {
            RotateRail(targetRail, i, j, k, reverseDirection);
        } else if (targetBlock instanceof StairsBlock) {
            RotateStairs(i, j, k, reverseDirection);
        }
    }

    private void rotateRegisteredBlock(int x, int y, int z, TurntableResult outputs) {
        TurntableOutput turntableOutput = outputs.turntableOutput();
        TurntableByproduct turntableByproduct = outputs.turntableByproduct();
        if (craftingRotationCount < turntableOutput.rotations()) {
            craftingRotationCount++;
            return;
        }
        craftingRotationCount = 0;
        if (turntableOutput.block() != BlockListener.obstructionBlock) {
            world.setBlock(x, y, z, turntableOutput.block().id, turntableOutput.blockMeta());
        } else {
            world.setBlock(x, y, z, 0);
        }
        world.setBlocksDirty(x, y, z, x, y, z);
        if (turntableByproduct.item() != ItemListener.nothing) {
            UnsortedUtils.ejectStackWithRandomOffset(world, x, y + 1, z, new ItemStack(turntableByproduct.item(), turntableByproduct.itemCount()));
        }
    }

    private boolean CanBlockTransmitRotation(int i, int j, int k) {
        int iTargetid = world.getBlockId(i, j, k);
        Block targetBlock = Block.BLOCKS[iTargetid];
        if (targetBlock instanceof RotatableBlock targetFCBlock) {
            return targetFCBlock.canTransmitRotation(world, i, j, k);
        }
        if (iTargetid == Block.GLASS.id) {
            return true;
        }
        if (iTargetid == Block.CLAY.id) {
            return false;
        }
        if (targetBlock instanceof PistonBlock blockPiston) {
            int iMetaData = world.getBlockMeta(i, j, k);
            PistonBlock _tmp = blockPiston;
            return !PistonBlock.method_762(iMetaData);
        }
        if (iTargetid == Block.PISTON_HEAD.id || iTargetid == Block.MOVING_PISTON.id) {
            return false;
        } else {
            return world.shouldSuffocate(i, j, k);
        }
    }

    private void RotateBlocksAttachedToBlock(int i, int j, int k, boolean bReverseDirection) {
        int[] newids = new int[4];
        for (int iTempIndex = 0; iTempIndex < 4; iTempIndex++) {
            newids[iTempIndex] = 0;
        }

        for (int iTempFacing = 2; iTempFacing <= 5; iTempFacing++) {
            BlockPosition tempPos = new BlockPosition(i, j, k);
            tempPos.addFacingAsOffset(iTempFacing);
            int tempBlockId = world.getBlockId(tempPos.x, tempPos.y, tempPos.z);
            int iTempMetaData = world.getBlockMeta(tempPos.x, tempPos.y, tempPos.z);
            boolean bAttached = false;
            if (tempBlockId == Block.TORCH.id || tempBlockId == Block.LIT_REDSTONE_TORCH.id || tempBlockId == Block.REDSTONE_TORCH.id) {
                if (iTempMetaData == 1 && iTempFacing == 5 || iTempMetaData == 2 && iTempFacing == 4 || iTempMetaData == 3 && iTempFacing == 3 || iTempMetaData == 4 && iTempFacing == 2) {
                    bAttached = true;
                    if (tempBlockId == Block.REDSTONE_TORCH.id) {
                        tempBlockId = Block.LIT_REDSTONE_TORCH.id;
                    }
                }
            } else if (tempBlockId == Block.LADDER.id) {
                if (iTempMetaData == iTempFacing) {
                    bAttached = true;
                }
            } else if (tempBlockId == Block.WALL_SIGN.id) {
                Block tempBlock = Block.BLOCKS[tempBlockId];
                if (iTempMetaData == iTempFacing) {
                    tempBlock.dropStacks(world, tempPos.x, tempPos.y, tempPos.z, tempBlockId);
                    world.setBlock(tempPos.x, tempPos.y, tempPos.z, 0);
                }
            } else if (tempBlockId == Block.BUTTON.id || tempBlockId == Block.LEVER.id) {
                Block tempBlock = Block.BLOCKS[tempBlockId];
                if (iTempMetaData == 1 && iTempFacing == 5 || iTempMetaData == 2 && iTempFacing == 4 || iTempMetaData == 3 && iTempFacing == 3 || iTempMetaData == 4 && iTempFacing == 2) {
                    tempBlock.dropStacks(world, tempPos.x, tempPos.y, tempPos.z, tempBlock.asItem().id);
                    world.setBlock(tempPos.x, tempPos.y, tempPos.z, 0);
                }
            }
            if (bAttached) {
                int iDestFacing = UnsortedUtils.RotateFacingAroundJ(iTempFacing, bReverseDirection);
                newids[iDestFacing - 2] = tempBlockId;
                world.setBlock(tempPos.x, tempPos.y, tempPos.z, 0);
            }
        }

        for (int iTempIndex = 0; iTempIndex < 4; iTempIndex++) {
            int tempBlockId = newids[iTempIndex];
            if (tempBlockId == 0) {
                continue;
            }
            int iTempFacing = iTempIndex + 2;
            int iTempMetaData = 0;
            BlockPosition tempPos = new BlockPosition(i, j, k);
            tempPos.addFacingAsOffset(iTempFacing);
            if (tempBlockId == Block.TORCH.id || tempBlockId == Block.LIT_REDSTONE_TORCH.id) {
                int iTargetFacing = 0;
                if (iTempFacing == 2) {
                    iTargetFacing = 4;
                } else if (iTempFacing == 3) {
                    iTargetFacing = 3;
                } else if (iTempFacing == 4) {
                    iTargetFacing = 2;
                } else if (iTempFacing == 5) {
                    iTargetFacing = 1;
                }
                iTempMetaData = iTargetFacing;
            } else if (tempBlockId == Block.LADDER.id) {
                iTempMetaData = iTempFacing;
            }
            if (ReplaceableBlockChecker.IsReplaceableBlock(world, tempPos.x, tempPos.y, tempPos.z)) {
                world.setBlock(tempPos.x, tempPos.y, tempPos.z, tempBlockId);
                world.setBlockMeta(tempPos.x, tempPos.y, tempPos.z, iTempMetaData);
                world.setBlocksDirty(tempPos.x, tempPos.y, tempPos.z, tempPos.x, tempPos.y, tempPos.z);
            } else {
                Block tempBlock = Block.BLOCKS[tempBlockId];
                int iOldFacing = UnsortedUtils.RotateFacingAroundJ(iTempFacing, !bReverseDirection);
                BlockPosition oldPos = new BlockPosition(i, j, k);
                oldPos.addFacingAsOffset(iOldFacing);
                tempBlock.dropStacks(world, oldPos.x, oldPos.y, oldPos.z, tempBlock.asItem().id);
            }
        }

    }

    private void RotateRail(RailBlock blockRail, int i, int j, int k, boolean bReverseDirection) {
        int iMetaData = world.getBlockMeta(i, j, k);
        int iDirection = iMetaData;
        if (blockRail.method_1108()) {
            iDirection &= 7;
        }
        if (iDirection == 0) {
            iDirection = 1;
        } else if (iDirection == 1) {
            iDirection = 0;
        } else if (iDirection != 2 && iDirection != 3 && iDirection != 4 && iDirection != 5) {
            if (iDirection == 6) {
                if (bReverseDirection) {
                    iDirection = 7;
                } else {
                    iDirection = 9;
                }
            } else if (iDirection == 7) {
                if (bReverseDirection) {
                    iDirection = 8;
                } else {
                    iDirection = 6;
                }
            } else if (iDirection == 8) {
                if (bReverseDirection) {
                    iDirection = 9;
                } else {
                    iDirection = 7;
                }
            } else if (iDirection == 9) {
                if (bReverseDirection) {
                    iDirection = 6;
                } else {
                    iDirection = 8;
                }
            }
        }
        if (blockRail.method_1108()) {
            iMetaData = iMetaData & 8 | iDirection;
        } else {
            iMetaData = iDirection;
        }
        world.setBlockMeta(i, j, k, iMetaData);
        world.setBlocksDirty(i, j, k, i, j, k);
    }

    private void RotateStairs(int i, int j, int k, boolean bReverseDirection) {
        int iMetaData = world.getBlockMeta(i, j, k);
        int iDirection = iMetaData + 2;
        iDirection = UnsortedUtils.RotateFacingAroundJ(iDirection, !bReverseDirection);
        iMetaData = iDirection - 2;
        world.setBlockMeta(i, j, k, iMetaData);
        world.setBlocksDirty(i, j, k, i, j, k);
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(World world, String name, int x, int y, int z, float g, float h) {
        List list2 = world.players;
        if (list2.size() != 0) {
            for (int k = 0; k < list2.size(); k++) {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g, h));
            }
        }
    }
}
