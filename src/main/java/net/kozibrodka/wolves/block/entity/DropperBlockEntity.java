package net.kozibrodka.wolves.block.entity;

import net.kozibrodka.wolves.utils.InventoryHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class DropperBlockEntity extends BlockEntity implements Inventory {
    private ItemStack[] hopperContents;
    private boolean dropDone;

    public DropperBlockEntity() {
        hopperContents = new ItemStack[2];
        dropDone = false;
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public ItemStack getStack(int slot) {
        return hopperContents[slot];
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return InventoryHandler.decreaseStackSize(this, slot, amount);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        hopperContents[slot] = stack;
        if (stack != null && stack.count > getMaxCountPerStack()) {
            stack.count = getMaxCountPerStack();
        }
        markDirty();
    }

    @Override
    public String getName() {
        return "Dropper";
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (world.getBlockEntity(x, y, z) != this) {
            return false;
        } else {
            return player.getSquaredDistance((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D) <= 64D;
        }
    }

    public void readNbt(NbtCompound nbtCompound) {
        super.readNbt(nbtCompound);
        NbtList nbtList = nbtCompound.getList("Items");
        hopperContents = new ItemStack[size()];
        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound itemCompound = (NbtCompound) nbtList.get(i);
            int j = itemCompound.getByte("Slot") & 0xff;
            if (j < hopperContents.length) {
                hopperContents[j] = new ItemStack(itemCompound);
            }
        }
        dropDone = nbtCompound.getBoolean("DropDone");
    }

    public void writeNbt(NbtCompound nbtCompound) {
        super.writeNbt(nbtCompound);
        NbtList nbtList = new NbtList();
        for (int i = 0; i < hopperContents.length; i++) {
            if (hopperContents[i] != null) {
                NbtCompound itemCompound = new NbtCompound();
                itemCompound.putByte("Slot", (byte) i);
                hopperContents[i].writeNbt(itemCompound);
                nbtList.add(itemCompound);
            }
        }
        nbtCompound.put("Items", nbtList);
        nbtCompound.putBoolean("DropDone", dropDone);
    }
}
