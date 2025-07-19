// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCTileEntityVase.java

package net.kozibrodka.wolves.block.entity;

import net.kozibrodka.wolves.utils.InventoryHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class VaseBlockEntity extends BlockEntity
        implements Inventory {

    public VaseBlockEntity() {
        m_VaseContents = new ItemStack[1];
    }

    public int size() {
        return 1;
    }

    public int getMaxCountPerStack() {
        return 1;
    }

    public ItemStack getStack(int iSlot) {
        return m_VaseContents[iSlot];
    }

    public ItemStack removeStack(int iSlot, int iAmount) {
        return InventoryHandler.decreaseStackSize(this, iSlot, iAmount);
    }

    public void setStack(int iSlot, ItemStack ItemInstance) {
        m_VaseContents[iSlot] = ItemInstance;
        if (ItemInstance != null && ItemInstance.count > getMaxCountPerStack()) {
            ItemInstance.count = getMaxCountPerStack();
        }
        markDirty();
    }

    public String getName() {
        return "Vase";
    }

    public boolean canPlayerUse(PlayerEntity entityplayer) {
        if (world.getBlockEntity(x, y, z) != this) {
            return false;
        } else {
            return entityplayer.getSquaredDistance((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D) <= 64D;
        }
    }

    public void readNbt(NbtCompound nbttagcompound) {
        super.readNbt(nbttagcompound);
        NbtList nbttaglist = nbttagcompound.getList("Items");
        m_VaseContents = new ItemStack[size()];
        for (int i = 0; i < nbttaglist.size(); i++) {
            NbtCompound nbttagcompound1 = (NbtCompound) nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if (j >= 0 && j < m_VaseContents.length) {
                m_VaseContents[j] = new ItemStack(nbttagcompound1);
            }
        }

    }

    public void writeNbt(NbtCompound nbttagcompound) {
        super.writeNbt(nbttagcompound);
        NbtList nbttaglist = new NbtList();
        for (int i = 0; i < m_VaseContents.length; i++) {
            if (m_VaseContents[i] != null) {
                NbtCompound nbttagcompound1 = new NbtCompound();
                nbttagcompound1.putByte("Slot", (byte) i);
                m_VaseContents[i].writeNbt(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
    }

    private final int m_iVaseInventorySize = 1;
    private final int m_iVaseStackSizeLimit = 1;
    private final double m_dVaseMaxPlayerInteractionDist = 64D;
    private ItemStack[] m_VaseContents;
}
