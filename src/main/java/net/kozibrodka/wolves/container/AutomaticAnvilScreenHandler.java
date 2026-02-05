package net.kozibrodka.wolves.container;

import net.kozibrodka.wolves.block.entity.AutomaticAnvilBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class AutomaticAnvilScreenHandler extends ScreenHandler {
    private final AutomaticAnvilBlockEntity automaticAnvilBlockEntity;

    public AutomaticAnvilScreenHandler(PlayerInventory playerInventory, AutomaticAnvilBlockEntity automaticAnvilBlockEntity) {
        this.automaticAnvilBlockEntity = automaticAnvilBlockEntity;

        for (int l = 0; l < 5; l++) {
            for (int k1 = 0; k1 < 5; k1++) {
                addSlot(new Slot(automaticAnvilBlockEntity, k1 + l * 5, 12 + k1 * 18, 17 + l * 18));
            }
        }
        addSlot(new Slot(automaticAnvilBlockEntity, 25, 142, 71));

        for (int i1 = 0; i1 < 3; i1++) {
            for (int l1 = 0; l1 < 9; l1++) {
                addSlot(new Slot(playerInventory, l1 + i1 * 9 + 9, 8 + l1 * 18, 120 + i1 * 18));
            }
        }
        for (int j1 = 0; j1 < 9; j1++) {
            addSlot(new Slot(playerInventory, j1, 8 + j1 * 18, 178));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return automaticAnvilBlockEntity.canPlayerUse(player);
    }

    public ItemStack quickMove(int i) {
        ItemStack ItemInstance = null;
        Slot slot = (Slot) slots.get(i);
        if (slot != null && slot.hasStack()) {
            ItemStack ItemInstance1 = slot.getStack();
            ItemInstance = ItemInstance1.copy();
            if (i == 0) {
                insertItem(ItemInstance1, 10, 46, true);
            } else if (i >= 10 && i < 37) {
                insertItem(ItemInstance1, 37, 46, false);
            } else if (i >= 37 && i < 46) {
                insertItem(ItemInstance1, 10, 37, false);
            } else {
                insertItem(ItemInstance1, 10, 46, false);
            }
            if (ItemInstance1.count == 0) {
                slot.setStack(null);
            } else {
                slot.markDirty();
            }
            if (ItemInstance1.count != ItemInstance.count) {
                slot.onTakeItem(ItemInstance1);
            } else {
                return null;
            }
        }
        return ItemInstance;
    }
}
