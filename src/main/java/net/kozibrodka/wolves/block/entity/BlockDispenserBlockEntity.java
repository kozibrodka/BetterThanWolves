// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCTileEntityBlockDispenser.java

package net.kozibrodka.wolves.block.entity;

import net.kozibrodka.wolves.utils.InventoryHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            TileEntity, InventoryBase, ItemInstance, FCUtilsInventory, 
//            NBTTagCompound, ListTag, World, EntityPlayer

public class BlockDispenserBlockEntity extends BlockEntity
        implements Inventory {

    public BlockDispenserBlockEntity() {
        dispenserContents = new ItemStack[9];
        dispenserRandom = new Random();
        iNextSlotIndexToDispense = 0;
    }

    public int size() {
        return 9;
    }

    public ItemStack getStack(int i) {
        return dispenserContents[i];
    }

    public ItemStack removeStack(int iSlot, int iAmount) {
        return InventoryHandler.decreaseStackSize(this, iSlot, iAmount);
    }

    public ItemStack GetNextStackFromInventory() {
        ItemStack nextStack = null;
        int iTempSlot;
        if (iNextSlotIndexToDispense >= dispenserContents.length || dispenserContents[iNextSlotIndexToDispense] == null) {
            iTempSlot = FindNextValidSlotIndex(iNextSlotIndexToDispense);
            if (iTempSlot < 0) {
                return null;
            }
            iNextSlotIndexToDispense = iTempSlot;
        }
        nextStack = removeStack(iNextSlotIndexToDispense, 1);
        iTempSlot = FindNextValidSlotIndex(iNextSlotIndexToDispense);
        if (iTempSlot < 0) {
            iNextSlotIndexToDispense = 0;
        } else {
            iNextSlotIndexToDispense = iTempSlot;
        }
        return nextStack;
    }

    private int FindNextValidSlotIndex(int iCurrentSlot) {
        for (int iTempSlot = iCurrentSlot + 1; iTempSlot < dispenserContents.length; iTempSlot++) {
            if (dispenserContents[iTempSlot] != null) {
                return iTempSlot;
            }
        }

        for (int iTempSlot = 0; iTempSlot < iCurrentSlot; iTempSlot++) {
            if (dispenserContents[iTempSlot] != null) {
                return iTempSlot;
            }
        }

        if (dispenserContents[iCurrentSlot] != null) {
            return iCurrentSlot;
        } else {
            return -1;
        }
    }

    public void setStack(int iSlot, ItemStack ItemInstance) {
        dispenserContents[iSlot] = ItemInstance;
        if (ItemInstance != null && ItemInstance.count > getMaxCountPerStack()) {
            ItemInstance.count = getMaxCountPerStack();
        }
        markDirty();
    }

    public String getName() {
        return "BlockDispenser";
    }

    public void readNbt(NbtCompound nbttagcompound) {
        super.readNbt(nbttagcompound);
        NbtList nbttaglist = nbttagcompound.getList("Items");
        dispenserContents = new ItemStack[size()];
        for (int i = 0; i < nbttaglist.size(); i++) {
            NbtCompound nbttagcompound1 = (NbtCompound) nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if (j >= 0 && j < dispenserContents.length) {
                dispenserContents[j] = new ItemStack(nbttagcompound1);
            }
        }

        if (nbttagcompound.contains("iNextSlotIndexToDispense")) {
            iNextSlotIndexToDispense = nbttagcompound.getInt("iNextSlotIndexToDispense");
        }
    }

    public void writeNbt(NbtCompound nbttagcompound) {
        super.writeNbt(nbttagcompound);
        NbtList nbttaglist = new NbtList();
        for (int i = 0; i < dispenserContents.length; i++) {
            if (dispenserContents[i] != null) {
                NbtCompound nbttagcompound1 = new NbtCompound();
                nbttagcompound1.putByte("Slot", (byte) i);
                dispenserContents[i].writeNbt(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
        nbttagcompound.putInt("iNextSlotIndexToDispense", iNextSlotIndexToDispense);
    }

    public int getMaxCountPerStack() {
        return 64;
    }

    public boolean canPlayerUse(PlayerEntity entityplayer) {
        if (world.getBlockEntity(x, y, z) != this) {
            return false;
        } else {
            return entityplayer.getSquaredDistance((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D) <= 64D;
        }
    }

    private ItemStack[] dispenserContents;
    private final Random dispenserRandom;
    public int iNextSlotIndexToDispense;
}
