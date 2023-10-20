package net.kozibrodka.wolves.container;

import net.kozibrodka.wolves.tileentity.CauldronTileEntity;
import net.minecraft.container.ContainerBase;
import net.minecraft.container.slot.Slot;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;


public class CauldronContainer extends ContainerBase
{

    public CauldronContainer(InventoryBase playerinventory, CauldronTileEntity tileentitycauldron)
    {
        localTileEntityCauldron = tileentitycauldron;
        for(int iRow = 0; iRow < 3; iRow++)
        {
            for(int iColumn = 0; iColumn < 9; iColumn++)
            {
                addSlot(new Slot(tileentitycauldron, iColumn + iRow * 9, 8 + iColumn * 18, 43 + iRow * 18));
            }

        }

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
        return localTileEntityCauldron.canPlayerUse(entityplayer);
    }
    
    public ItemInstance transferSlot(int iSlotIndex)
    {
        ItemInstance ItemInstance = null;
        Slot slot = (Slot)slots.get(iSlotIndex);
        if(slot != null && slot.hasItem())
        {
            ItemInstance ItemInstance1 = slot.getItem();
            ItemInstance = ItemInstance1.copy();
            if(iSlotIndex < 27)
            {
                AttemptToPutStackInInventorySlotRange(ItemInstance1, 27, slots.size());
            } else
            {
                AttemptToPutStackInInventorySlotRange(ItemInstance1, 0, 27);
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

    private void AttemptToPutStackInInventorySlotRange(ItemInstance iteminstance, int i, int j)
    {
        int k = i;
        if(iteminstance.isStackable())
        {
            for(; iteminstance.count > 0 && k < j; k++)
            {
                Slot slot = (Slot)slots.get(k);
                ItemInstance ItemInstance1 = slot.getItem();
                if(ItemInstance1 == null || ItemInstance1.itemId != iteminstance.itemId || iteminstance.usesMeta() && iteminstance.getDamage() != ItemInstance1.getDamage())
                {
                    continue;
                }
                int i1 = ItemInstance1.count + iteminstance.count;
                if(i1 <= iteminstance.getMaxStackSize())
                {
                    iteminstance.count = 0;
                    ItemInstance1.count = i1;
                    slot.markDirty();
                    continue;
                }
                if(ItemInstance1.count < iteminstance.getMaxStackSize())
                {
                    iteminstance.count -= iteminstance.getMaxStackSize() - ItemInstance1.count;
                    ItemInstance1.count = iteminstance.getMaxStackSize();
                    slot.markDirty();
                }
            }

        }
        if(iteminstance.count > 0)
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
                    slot1.setStack(iteminstance.copy());
                    slot1.markDirty();
                    iteminstance.count = 0;
                    break;
                }
                l++;
            } while(true);
        }
    }

    private final int iNumCauldronSlotRows = 3;
    private final int iNumCauldronSlotColumns = 9;
    private final int iNumCauldronSlots = 27;
    private CauldronTileEntity localTileEntityCauldron;
}
