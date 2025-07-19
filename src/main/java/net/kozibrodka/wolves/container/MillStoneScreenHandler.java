package net.kozibrodka.wolves.container;

import net.kozibrodka.wolves.block.entity.MillStoneBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;


public class MillStoneScreenHandler extends ScreenHandler {

    public MillStoneScreenHandler(Inventory playerinventory, MillStoneBlockEntity tileentityMillStone) {
        localTileEntityMillStone = tileentityMillStone;
        for (int iRow = 0; iRow < 3; iRow++) {
            for (int iColumn = 0; iColumn < 1; iColumn++) {
                addSlot(new Slot(tileentityMillStone, iColumn + iRow, 8 + (iColumn + 4) * 18, 43 + iRow * 18));
            }

        }

        for (int iRow = 0; iRow < 3; iRow++) {
            for (int iColumn = 0; iColumn < 9; iColumn++) {
                addSlot(new Slot(playerinventory, iColumn + iRow * 9 + 9, 8 + iColumn * 18, 111 + iRow * 18));
            }

        }

        for (int iColumn = 0; iColumn < 9; iColumn++) {
            addSlot(new Slot(playerinventory, iColumn, 8 + iColumn * 18, 169));
        }

    }

    public boolean canUse(PlayerEntity entityplayer) {
        return localTileEntityMillStone.canPlayerUse(entityplayer);
    }

    public ItemStack getStackInSlot(int iSlotIndex) //func_27086_a
    {
        ItemStack ItemInstance = null;
        Slot slot = (Slot) slots.get(iSlotIndex);
        if (slot != null && slot.hasStack()) {
            ItemStack ItemInstance1 = slot.getStack();
            ItemInstance = ItemInstance1.copy();
            if (iSlotIndex < 3) {
                AttemptToPutStackInInventorySlotRange(ItemInstance1, 3, slots.size());
            } else {
                AttemptToPutStackInInventorySlotRange(ItemInstance1, 0, 3);
            }
            if (ItemInstance1.count == 0) {
                slot.setStack(null);
            } else {
                slot.markDirty();
            }
        }
        return ItemInstance;
    }

    private void AttemptToPutStackInInventorySlotRange(ItemStack ItemInstance, int i, int j) {
        int k = i;
        if (ItemInstance.isStackable()) {
            for (; ItemInstance.count > 0 && k < j; k++) {
                Slot slot = (Slot) slots.get(k);
                ItemStack ItemInstance1 = slot.getStack();
                if (ItemInstance1 == null || ItemInstance1.itemId != ItemInstance.itemId || ItemInstance.hasSubtypes() && ItemInstance.getDamage() != ItemInstance1.getDamage()) {
                    continue;
                }
                int i1 = ItemInstance1.count + ItemInstance.count;
                if (i1 <= ItemInstance.getMaxCount()) {
                    ItemInstance.count = 0;
                    ItemInstance1.count = i1;
                    slot.markDirty();
                    continue;
                }
                if (ItemInstance1.count < ItemInstance.getMaxCount()) {
                    ItemInstance.count -= ItemInstance.getMaxCount() - ItemInstance1.count;
                    ItemInstance1.count = ItemInstance.getMaxCount();
                    slot.markDirty();
                }
            }

        }
        if (ItemInstance.count > 0) {
            int l = i;
            do {
                if (l >= j) {
                    break;
                }
                Slot slot1 = (Slot) slots.get(l);
                ItemStack ItemInstance2 = slot1.getStack();
                if (ItemInstance2 == null) {
                    slot1.setStack(ItemInstance.copy());
                    slot1.markDirty();
                    ItemInstance.count = 0;
                    break;
                }
                l++;
            } while (true);
        }
    }

    private final int iNumMillStoneSlotRows = 3;
    private final int iNumMillStoneSlotColumns = 1;
    private final int iNumMillStoneSlots = 3;
    private final MillStoneBlockEntity localTileEntityMillStone;
}
