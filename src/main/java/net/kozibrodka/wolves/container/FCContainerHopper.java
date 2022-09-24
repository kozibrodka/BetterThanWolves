package net.kozibrodka.wolves.container;

import net.kozibrodka.wolves.tileentity.FCTileEntityHopper;
import net.minecraft.container.ContainerBase;
import net.minecraft.container.slot.Slot;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;


public class FCContainerHopper extends ContainerBase
{

    public FCContainerHopper(InventoryBase playerinventory, FCTileEntityHopper tileentityHopper)
    {
        localTileEntityHopper = tileentityHopper;
        for(int iRow = 0; iRow < 2; iRow++)
        {
            for(int iColumn = 0; iColumn < 9; iColumn++)
            {
                addSlot(new Slot(tileentityHopper, iColumn + iRow * 9, 8 + iColumn * 18, 60 + iRow * 18));
            }

        }

        addSlot(new Slot(tileentityHopper, 18, 80, 37));
        for(int iRow = 0; iRow < 3; iRow++)
        {
            for(int iColumn = 0; iColumn < 9; iColumn++)
            {
                addSlot(new Slot(playerinventory, iColumn + iRow * 9 + 9, 8 + iColumn * 18, 111 + iRow * 18));
            }

        }

        for(int iColumn = 0; iColumn < 9; iColumn++)
        {
            addSlot(new Slot(playerinventory, iColumn, 8 + iColumn * 18, 169));
        }

    }

    public boolean canUse(PlayerBase entityplayer)
    {
        return localTileEntityHopper.canPlayerUse(entityplayer);
    }

    public ItemInstance transferSlot(int iSlotIndex)
    {
        ItemInstance ItemInstance = null;
        Slot slot = (Slot)slots.get(iSlotIndex);
        if(slot != null && slot.hasItem())
        {
            ItemInstance ItemInstance1 = slot.getItem();
            ItemInstance = ItemInstance1.copy();
            if(iSlotIndex < 19)
            {
                AttemptToPutStackInInventorySlotRange(ItemInstance1, 19, slots.size());
            } else
            {
                AttemptToPutStackInInventorySlotRange(ItemInstance1, 0, 18);
            }
            if(ItemInstance1.count == 0)
            {
                slot.setStack(null);
            } else
            {
                slot.markDirty();
            }
        }
        return ItemInstance;
    }

    private void AttemptToPutStackInInventorySlotRange(ItemInstance ItemInstance, int i, int j)
    {
        int k = i;
        if(ItemInstance.isStackable())
        {
            for(; ItemInstance.count > 0 && k < j; k++)
            {
                Slot slot = (Slot)slots.get(k);
                ItemInstance ItemInstance1 = slot.getItem();
                if(ItemInstance1 == null || ItemInstance1.itemId != ItemInstance.itemId || ItemInstance.usesMeta() && ItemInstance.getDamage() != ItemInstance1.getDamage())
                {
                    continue;
                }
                int i1 = ItemInstance1.count + ItemInstance.count;
                if(i1 <= ItemInstance.getMaxStackSize())
                {
                    ItemInstance.count = 0;
                    ItemInstance1.count = i1;
                    slot.markDirty();
                    continue;
                }
                if(ItemInstance1.count < ItemInstance.getMaxStackSize())
                {
                    ItemInstance.count -= ItemInstance.getMaxStackSize() - ItemInstance1.count;
                    ItemInstance1.count = ItemInstance.getMaxStackSize();
                    slot.markDirty();
                }
            }

        }
        if(ItemInstance.count > 0)
        {
            int l = i;
            do
            {
                if(l >= j)
                {
                    break;
                }
                Slot slot1 = (Slot)slots.get(l);
                ItemInstance ItemInstance2 = slot1.getItem();
                if(ItemInstance2 == null)
                {
                    slot1.setStack(ItemInstance.copy());
                    slot1.markDirty();
                    ItemInstance.count = 0;
                    break;
                }
                l++;
            } while(true);
        }
    }

    private final int iNumHopperSlotRows = 2;
    private final int iNumHopperSlotColumns = 9;
    private final int iNumHopperSlots = 18;
    private FCTileEntityHopper localTileEntityHopper;
}
