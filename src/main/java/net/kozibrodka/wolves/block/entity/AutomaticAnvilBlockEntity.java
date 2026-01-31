package net.kozibrodka.wolves.block.entity;

import net.kozibrodka.wolves.utils.InventoryHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class AutomaticAnvilBlockEntity extends BlockEntity implements Inventory {
    private static final int INPUT_START = 0;
    private static final int INPUT_END = 24;
    private static final int OUTPUT = 25;
    private static final int INVENTORY_SIZE = 26;

    private final ItemStack[] anvilItemStacks = new ItemStack[INVENTORY_SIZE];

    private int craftingProgress;

    @Override
    public int size() {
        return INVENTORY_SIZE;
    }

    @Override
    public ItemStack getStack(int slot) {
        return anvilItemStacks[slot];
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return InventoryHandler.decreaseStackSize(this, slot, amount);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        anvilItemStacks[slot] = stack;
        if (stack != null && stack.count > getMaxCountPerStack()) {
            stack.count = getMaxCountPerStack();
        }
        markDirty();
    }

    @Override
    public String getName() {
        return "AutomaticAnvil";
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
        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound itemCompound = (NbtCompound) nbtList.get(i);
            int j = itemCompound.getByte("Slot") & 0xff;
            if (j < anvilItemStacks.length) {
                anvilItemStacks[j] = new ItemStack(itemCompound);
            }
        }
        craftingProgress = nbtCompound.getInt("CraftingProgress");
    }

    public void writeNbt(NbtCompound nbtCompound) {
        super.writeNbt(nbtCompound);
        NbtList nbtList = new NbtList();
        for (int i = 0; i < anvilItemStacks.length; i++) {
            if (anvilItemStacks[i] != null) {
                NbtCompound itemCompound = new NbtCompound();
                itemCompound.putByte("Slot", (byte) i);
                anvilItemStacks[i].writeNbt(itemCompound);
                nbtList.add(itemCompound);
            }
        }
        nbtCompound.put("Items", nbtList);
        nbtCompound.putInt("CraftingProgress", craftingProgress);
    }
}
