package net.kozibrodka.wolves.container;

import net.kozibrodka.wolves.tileentity.BlockDispenserTileEntity;
import net.minecraft.container.ContainerBase;
import net.minecraft.container.slot.Slot;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;


public class BlockDispenserContainer extends ContainerBase
{

    public BlockDispenserContainer(InventoryBase iinventory, BlockDispenserTileEntity tileEntityBlockDispenser)
    {
        localTileEntityBlockDispenser = tileEntityBlockDispenser;
        for(int i = 0; i < 3; i++)
        {
            for(int l = 0; l < 3; l++)
            {
                addSlot(new Slot(tileEntityBlockDispenser, l + i * 3, 61 + l * 18, 17 + i * 18));
            }

        }

        for(int j = 0; j < 3; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(iinventory, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }

        }

        for(int k = 0; k < 9; k++)
        {
            addSlot(new Slot(iinventory, k, 8 + k * 18, 142));
        }

    }

    public boolean canUse(PlayerBase entityplayer)
    {
        return localTileEntityBlockDispenser.canPlayerUse(entityplayer);
    }

    public ItemInstance transferSlot(int iSlotIndex)
    {
        ItemInstance ItemInstance = null;
        Slot slot = (Slot)slots.get(iSlotIndex);
        if(slot != null && slot.hasItem())
        {
            ItemInstance ItemInstance1 = slot.getItem();
            ItemInstance = ItemInstance1.copy();
            if(iSlotIndex < 9)
            {
                AttemptToPutStackInInventorySlotRange(ItemInstance1, 9, slots.size());
            } else
            {
                AttemptToPutStackInInventorySlotRange(ItemInstance1, 0, 9);
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

    public ItemInstance clickSlot(int i, int j, boolean flag, PlayerBase entityplayer)
    {
        if(i < 9)
        {
            localTileEntityBlockDispenser.iNextSlotIndexToDispense = 0;
        }
        return super.clickSlot(i, j, flag, entityplayer);
    }

    private BlockDispenserTileEntity localTileEntityBlockDispenser;
    private final int iNumBlockDispenserSlots = 9;
}
