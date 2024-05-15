package net.kozibrodka.wolves.container;

import net.kozibrodka.wolves.block.entity.BlockDispenserBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;


public class BlockDispenserScreenHandler extends ScreenHandler
{

    public BlockDispenserScreenHandler(Inventory iinventory, BlockDispenserBlockEntity tileEntityBlockDispenser)
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

    public boolean canUse(PlayerEntity entityplayer)
    {
        return localTileEntityBlockDispenser.canPlayerUse(entityplayer);
    }

    public ItemStack getStackInSlot(int iSlotIndex)
    {
        ItemStack ItemInstance = null;
        Slot slot = (Slot)slots.get(iSlotIndex);
        if(slot != null && slot.hasStack())
        {
            ItemStack ItemInstance1 = slot.getStack();
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

    private void AttemptToPutStackInInventorySlotRange(ItemStack iteminstance, int i, int j)
    {
        int k = i;
        if(iteminstance.isStackable())
        {
            for(; iteminstance.count > 0 && k < j; k++)
            {
                Slot slot = (Slot)slots.get(k);
                ItemStack ItemInstance1 = slot.getStack();
                if(ItemInstance1 == null || ItemInstance1.itemId != iteminstance.itemId || iteminstance.hasSubtypes() && iteminstance.getDamage() != ItemInstance1.getDamage())
                {
                    continue;
                }
                int i1 = ItemInstance1.count + iteminstance.count;
                if(i1 <= iteminstance.getMaxCount())
                {
                    iteminstance.count = 0;
                    ItemInstance1.count = i1;
                    slot.markDirty();
                    continue;
                }
                if(ItemInstance1.count < iteminstance.getMaxCount())
                {
                    iteminstance.count -= iteminstance.getMaxCount() - ItemInstance1.count;
                    ItemInstance1.count = iteminstance.getMaxCount();
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
                ItemStack ItemInstance2 = slot1.getStack();
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

    public ItemStack onSlotClick(int i, int j, boolean flag, PlayerEntity entityplayer)
    {
        if(i < 9)
        {
            localTileEntityBlockDispenser.iNextSlotIndexToDispense = 0;
        }
        return super.onSlotClick(i, j, flag, entityplayer);
    }

    private BlockDispenserBlockEntity localTileEntityBlockDispenser;
    private final int iNumBlockDispenserSlots = 9;
}
