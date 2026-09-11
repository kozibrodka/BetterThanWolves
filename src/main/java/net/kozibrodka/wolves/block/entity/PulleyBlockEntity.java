package net.kozibrodka.wolves.block.entity;

import net.kozibrodka.wolves.block.AnchorBlock;
import net.kozibrodka.wolves.block.PulleyBlock;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.kozibrodka.wolves.utils.ReplaceableBlockChecker;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.kozibrodka.wolves.utils.WolvesMpUtils;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.registry.ItemRegistry;


public class PulleyBlockEntity extends BlockEntity implements Inventory {
    private boolean mechanicallyPowered;
    private boolean updatedByServer;

    public PulleyBlockEntity() {
        pulleyContents = new ItemStack[2];
        iUpdateRopeStateCounter = 20;
    }

    @Override
    public String getName() {
        return "Pulley";
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    @Override
    public ItemStack getStack(int iSlot) {
        return pulleyContents[iSlot];
    }

    @Override
    public void setStack(int iSlot, ItemStack ItemInstance) {
        pulleyContents[iSlot] = ItemInstance;
        if (ItemInstance != null && ItemInstance.count > getMaxCountPerStack()) {
            ItemInstance.count = getMaxCountPerStack();
        }
        markDirty();
    }

    @Override
    public ItemStack removeStack(int iSlot, int iAmount) {
        return InventoryHandler.decreaseStackSize(this, iSlot, iAmount);
    }

    @Override
    public void readNbt(NbtCompound nbttagcompound) {
        super.readNbt(nbttagcompound);
        NbtList nbttaglist = nbttagcompound.getList("Items");
        pulleyContents = new ItemStack[size()];
        for (int i = 0; i < nbttaglist.size(); i++) {
            NbtCompound nbttagcompound1 = (NbtCompound) nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if (j >= 0 && j < pulleyContents.length) {
                pulleyContents[j] = new ItemStack(nbttagcompound1);
            }
        }

    }

    @Override
    public void writeNbt(NbtCompound nbttagcompound) {
        super.writeNbt(nbttagcompound);
        NbtList nbttaglist = new NbtList();
        for (int i = 0; i < pulleyContents.length; i++) {
            if (pulleyContents[i] != null) {
                NbtCompound nbttagcompound1 = new NbtCompound();
                nbttagcompound1.putByte("Slot", (byte) i);
                pulleyContents[i].writeNbt(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity entityplayer) {
        if (world.getBlockEntity(x, y, z) != this) {
            return false;
        } else {
            return entityplayer.getSquaredDistance((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D) <= 64D;
        }
    }

    @Override
    public void tick() {
        if (world.isRemote) {
            return;
        }
        iUpdateRopeStateCounter--;
        if (iUpdateRopeStateCounter <= 0) {
            boolean bIsRedstoneOn = ((PulleyBlock) BlockListener.pulley).IsRedstoneOn(world, x, y, z);
            if (!bIsRedstoneOn) {
                boolean bIsOn = ((PulleyBlock) BlockListener.pulley).IsBlockOn(world, x, y, z);
                if (bIsOn) {
                    AttemptToRetractRope();
                } else {
                    AttemptToDispenseRope();
                }
            }
            iUpdateRopeStateCounter = 20;
        }
    }

    public boolean IsMechanicallyPowered() {
        return ((PulleyBlock) BlockListener.pulley).IsBlockOn(world, x, y, z);
    }

    public void forcefullyUpdateMechanicallyPowered() {
        mechanicallyPowered = IsMechanicallyPowered();
    }

    public void updateMechanicallyPowered() {
        if (updatedByServer) {
            return;
        }
        forcefullyUpdateMechanicallyPowered();
    }

    public boolean poweredForClient() {
        return mechanicallyPowered;
    }

    public void forcefullyChangePoweredStatus(boolean mechanicallyPowered) {
        updatedByServer = true;
        this.mechanicallyPowered = mechanicallyPowered;
    }

    public boolean IsRedstonePowered() {
        return ((PulleyBlock) BlockListener.pulley).IsRedstoneOn(world, x, y, z);
    }

    public boolean IsRaising() {
        return !IsRedstonePowered() && IsMechanicallyPowered();
    }

    public boolean isLowering() {
        return !IsRedstonePowered() && !IsMechanicallyPowered() && InventoryHandler.getFirstOccupiedStackOfItem(this, ItemRegistry.INSTANCE.getId(ItemListener.ropeItem)) >= 0;
    }

    public void NotifyPulleyEntityOfBlockStateChange() {
        iUpdateRopeStateCounter = 20;
        NotifyAttachedAnchorOfEntityStateChange();
    }

    private void NotifyAttachedAnchorOfEntityStateChange() {
        for (int tempj = y - 1; tempj >= 0; tempj--) {
            int tempBlockId = world.getBlockId(x, tempj, z);
            if (tempBlockId == BlockListener.anchor.id) {
                if (((AnchorBlock) BlockListener.anchor).getAnchorFacing(world, x, tempj, z) != 1) {
                    break;
                }
                ((AnchorBlock) BlockListener.anchor).notifyAnchorBlockOfAttachedPulleyStateChange(this, world, x, tempj, z);
                continue;
            }
            if (tempBlockId != BlockListener.rope.id) {
                break;
            }
        }

    }

    boolean AttemptToRetractRope() {
        for (int tempj = y - 1; tempj >= 0; tempj--) {
            int tempBlockId = world.getBlockId(x, tempj, z);
            if (tempBlockId == BlockListener.rope.id) {
                if (world.getBlockId(x, tempj - 1, z) != BlockListener.rope.id) {
                    AddRopeToInventory();
                    Block targetBlock = BlockListener.rope;
                    /// można wyłączyć te dźwięki przy pracy platformy (niszczenia liny)
                    world.playSound(x, tempj, z, targetBlock.soundGroup.getSound(), targetBlock.soundGroup.getVolume() / 4F, targetBlock.soundGroup.getPitch() * 0.8F);
                    if (WolvesMpUtils.isEnvServ()) {
                        WolvesMpUtils.soundPacket(world, x, tempj, z, targetBlock.soundGroup.getSound(), targetBlock.soundGroup.getVolume() / 4F, targetBlock.soundGroup.getPitch() * 0.8F);
                    }
                    world.setBlock(x, tempj, z, 0);
                    return true;
                }
            } else {
                return false;
            }
        }

        return false;
    }

    public boolean AttemptToDispenseRope() {
        int iRopeSlot = InventoryHandler.getFirstOccupiedStackOfItem(this, ItemRegistry.INSTANCE.getId(ItemListener.ropeItem));
        iUpdateRopeStateCounter = 20;
        if (iRopeSlot >= 0) {
            for (int tempj = y - 1; tempj >= 0; tempj--) {
                int tempBlockId = world.getBlockId(x, tempj, z);
                if (ReplaceableBlockChecker.IsReplaceableBlock(world, x, tempj, z)) {
                    if (world.setBlock(x, tempj, z, BlockListener.rope.id)) {
                        BlockListener.rope.onPlaced(world, x, tempj, z, 0);
                        Block targetBlock = BlockListener.rope;
                        /// można wyłączyć te dźwięki przy pracy platformy (niszczenia liny)
                        world.playSound(x, tempj, z, targetBlock.soundGroup.getSound(), targetBlock.soundGroup.getVolume() / 4F, targetBlock.soundGroup.getPitch() * 0.8F);
                        if (WolvesMpUtils.isEnvServ()) {
                            WolvesMpUtils.soundPacket(world, x, tempj, z, targetBlock.soundGroup.getSound(), targetBlock.soundGroup.getVolume() / 4F, targetBlock.soundGroup.getPitch() * 0.8F);
                        }
                        RemoveRopeFromInventory();
                        int iBlockBelowTargetID = world.getBlockId(x, tempj - 1, z);
                        if (iBlockBelowTargetID == BlockListener.anchor.id && ((AnchorBlock) BlockListener.anchor).getAnchorFacing(world, x, tempj - 1, z) == 1) {
                            ((AnchorBlock) BlockListener.anchor).notifyAnchorBlockOfAttachedPulleyStateChange(this, world, x, tempj - 1, z);
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
                if (tempBlockId != BlockListener.rope.id) {
                    return false;
                }
            }

        }
        return false;
    }

    public void AddRopeToInventory() {
        ItemStack ropeStack = new ItemStack(ItemListener.ropeItem);
        iUpdateRopeStateCounter = 20;
        if (InventoryHandler.addItemInstanceToInventory(this, ropeStack)) {
            world.playSound((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.pop", 0.05F, (world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F);
            if (WolvesMpUtils.isEnvServ()) {
                WolvesMpUtils.soundPacket(world, x, y, z, "random.pop", 0.05F, (world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F);
            }
        } else {
            UnsortedUtils.ejectStackWithRandomOffset(world, x, y, z, ropeStack);
        }
    }

    public int GetContainedRopeCount() {
        return InventoryHandler.itemCountInInventory(this, ItemRegistry.INSTANCE.getId(ItemListener.ropeItem), -1);
    }

    public void RemoveRopeFromInventory() {
        int iRopeSlot = InventoryHandler.getFirstOccupiedStackOfItem(this, ItemRegistry.INSTANCE.getId(ItemListener.ropeItem));
        if (iRopeSlot >= 0) {
            InventoryHandler.decreaseStackSize(this, iRopeSlot, 1);
        }
    }


    private final int iPulleyInventorySize = 2;
    private final int iPulleyStackSizeLimit = 64;
    private final double dPulleyMaxPlayerInteractionDist = 64D;
    private final int iTicksToUpdateRopeState = 20;
    private ItemStack[] pulleyContents;
    private int iUpdateRopeStateCounter;
}
