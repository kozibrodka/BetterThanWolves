package net.kozibrodka.wolves.container;

import net.kozibrodka.wolves.block.entity.CrucibleBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;


public class CrucibleScreenHandler extends ScreenHandler {

    public CrucibleScreenHandler(Inventory playerinventory, CrucibleBlockEntity tileEntityCrucible) {
        localTileEntityCrucible = tileEntityCrucible;
        for (int iRow = 0; iRow < 3; iRow++) {
            for (int iColumn = 0; iColumn < 9; iColumn++) {
                addSlot(new Slot(tileEntityCrucible, iColumn + iRow * 9, 8 + iColumn * 18, 43 + iRow * 18));
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
        return localTileEntityCrucible.canPlayerUse(entityplayer);
    }

    public ItemStack getStackInSlot(int iSlotIndex) {
        ItemStack ItemInstance = null;
        Slot slot = (Slot) slots.get(iSlotIndex);
        if (slot != null && slot.hasStack()) {
            ItemStack ItemInstance1 = slot.getStack();
            ItemInstance = ItemInstance1.copy();
            if (iSlotIndex < 27) {
                AttemptToPutStackInInventorySlotRange(ItemInstance1, 27, slots.size());
            } else {
                AttemptToPutStackInInventorySlotRange(ItemInstance1, 0, 27);
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

    private final int iNumCrucibleSlotRows = 3;
    private final int iNumCrucibleSlotColumns = 9;
    private final int iNumCrucibleSlots = 27;
    private final CrucibleBlockEntity localTileEntityCrucible;
}
