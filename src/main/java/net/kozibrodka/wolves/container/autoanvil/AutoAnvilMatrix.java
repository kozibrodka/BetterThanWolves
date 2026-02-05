package net.kozibrodka.wolves.container.autoanvil;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

/**
 * This only exists to be inserted into an automatic anvil inventory.
 */
public class AutoAnvilMatrix implements Inventory {

    private final ItemStack[] items = new ItemStack[25];

    @Override
    public int size() {
        return items.length;
    }

    @Override
    public ItemStack getStack(int slot) {
        return items[slot];
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if (this.items[slot] != null) {
            ItemStack item = this.items[slot];
            this.items[slot] = null;
            return item;
        } else {
            return null;
        }
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        items[slot] = stack;
    }

    @Override
    public String getName() {
        return "Auto-Anvil Matrix";
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }
}

