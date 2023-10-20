// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCUtilsInventory.java

package net.kozibrodka.wolves.utils;

import net.minecraft.entity.Item;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;


public class InventoryHandler
{

    public InventoryHandler()
    {
    }

    public static void ClearInventoryContents(InventoryBase inventory)
    {
        for(int iSlot = 0; iSlot < inventory.getInventorySize(); iSlot++)
        {
            ItemInstance ItemInstance = inventory.getInventoryItem(iSlot);
            if(ItemInstance != null)
            {
                inventory.setInventoryItem(iSlot, null);
            }
        }

    }

    public static void EjectInventoryContents(Level world, int i, int j, int k, InventoryBase inventory)
    {
label0:
        for(int l = 0; l < inventory.getInventorySize(); l++)
        {
            ItemInstance ItemInstance = inventory.getInventoryItem(l);
            if(ItemInstance == null)
            {
                continue;
            }
            float f = world.rand.nextFloat() * 0.7F + 0.15F;
            float f1 = world.rand.nextFloat() * 0.7F + 0.15F;
            float f2 = world.rand.nextFloat() * 0.7F + 0.15F;
            do
            {
                if(ItemInstance.count <= 0)
                {
                    continue label0;
                }
                int i1 = world.rand.nextInt(21) + 10;
                if(i1 > ItemInstance.count)
                {
                    i1 = ItemInstance.count;
                }
                ItemInstance.count -= i1;
                Item entityitem = new Item(world, (float)i + f, (float)j + f1, (float)k + f2, new ItemInstance(ItemInstance.itemId, i1, ItemInstance.getDamage()));
                float f3 = 0.05F;
                entityitem.velocityX = (float)world.rand.nextGaussian() * f3;
                entityitem.velocityY = (float)world.rand.nextGaussian() * f3 + 0.2F;
                entityitem.velocityZ = (float)world.rand.nextGaussian() * f3;
                entityitem.pickupDelay = 10;
                world.spawnEntity(entityitem);
            } while(true);
        }

    }

    public static boolean ConsumeItemsInInventory(InventoryBase inventory, int iShiftedItemIndex, int iItemDamage, int iItemCount)
    {
        for(int iSlot = 0; iSlot < inventory.getInventorySize(); iSlot++)
        {
            ItemInstance tempItemInstance = inventory.getInventoryItem(iSlot);
            if(tempItemInstance == null)
            {
                continue;
            }
            ItemBase tempItem = tempItemInstance.getType();
            if(tempItem.id != iShiftedItemIndex || iItemDamage != -1 && tempItemInstance.getDamage() != iItemDamage)
            {
                continue;
            }
            if(tempItemInstance.count >= iItemCount)
            {
                DecrStackSize(inventory, iSlot, iItemCount);
                return true;
            }
            iItemCount -= tempItemInstance.count;
            inventory.setInventoryItem(iSlot, null);
        }

        return false;
    }

    public static ItemInstance DecrStackSize(InventoryBase inventory, int iSlot, int iAmount)
    {
        if(inventory.getInventoryItem(iSlot) != null)
        {
            if(inventory.getInventoryItem(iSlot).count <= iAmount)
            {
                ItemInstance ItemInstance = inventory.getInventoryItem(iSlot);
                inventory.setInventoryItem(iSlot, null);
                return ItemInstance;
            }
            ItemInstance splitStack = inventory.getInventoryItem(iSlot).split(iAmount);
            if(inventory.getInventoryItem(iSlot).count == 0)
            {
                inventory.setInventoryItem(iSlot, null);
            } else
            {
                inventory.markDirty();
            }
            return splitStack;
        } else
        {
            return null;
        }
    }

    public static int GetFirstOccupiedStack(InventoryBase inventory)
    {
        for(int i = 0; i < inventory.getInventorySize(); i++)
        {
            if(inventory.getInventoryItem(i) != null)
            {
                return i;
            }
        }

        return -1;
    }

    public static int GetFirstOccupiedStackNotOfItem(InventoryBase inventory, int iNotItemID)
    {
        for(int i = 0; i < inventory.getInventorySize(); i++)
        {
            if(inventory.getInventoryItem(i) != null && inventory.getInventoryItem(i).getType().id != iNotItemID)
            {
                return i;
            }
        }

        return -1;
    }

    public static int GetFirstOccupiedStackOfItem(InventoryBase inventory, int iItemID)
    {
        for(int i = 0; i < inventory.getInventorySize(); i++)
        {
            if(inventory.getInventoryItem(i) != null && inventory.getInventoryItem(i).getType().id == iItemID)
            {
                return i;
            }
        }

        return -1;
    }

    public static int CountItemsInInventory(InventoryBase inventory, int iItemID, int iItemDamage)
    {
        int itemCount = 0;
        for(int i = 0; i < inventory.getInventorySize(); i++)
        {
            ItemInstance tempStack = inventory.getInventoryItem(i);
            if(tempStack != null && tempStack.getType().id == iItemID && (iItemDamage == -1 || tempStack.getDamage() == iItemDamage))
            {
                itemCount += inventory.getInventoryItem(i).count;
            }
        }

        return itemCount;
    }

    public static int GetNumOccupiedStacks(InventoryBase inventory)
    {
        int iCount = 0;
        for(int i = 0; i < inventory.getInventorySize(); i++)
        {
            if(inventory.getInventoryItem(i) != null)
            {
                iCount++;
            }
        }

        return iCount;
    }

    public static boolean AddSingleItemToInventory(InventoryBase inventory, int iItemShiftedIndex, int itemDamage)
    {
        ItemInstance ItemInstance = new ItemInstance(iItemShiftedIndex, 1, itemDamage);
        return AddItemInstanceToInventory(inventory, ItemInstance);
    }

    public static boolean AddItemInstanceToInventory(InventoryBase inventory, ItemInstance ItemInstance)
    {
        if(!ItemInstance.isDamaged())
        {
            ItemInstance.count = StorePartialItemInstance(inventory, ItemInstance);
            if(ItemInstance.count == 0)
            {
                return true;
            }
        }
        int i = GetFirstEmptyStack(inventory);
        if(i >= 0)
        {
            inventory.setInventoryItem(i, ItemInstance);
            return true;
        } else
        {
            return false;
        }
    }

    private static int GetFirstEmptyStack(InventoryBase inventory)
    {
        for(int i = 0; i < inventory.getInventorySize(); i++)
        {
            if(inventory.getInventoryItem(i) == null)
            {
                return i;
            }
        }

        return -1;
    }

    private static int StorePartialItemInstance(InventoryBase inventory, ItemInstance ItemInstance)
    {
        int i = ItemInstance.itemId;
        int j = ItemInstance.count;
        int k = FindSlotToStoreItemInstance(inventory, ItemInstance);
        if(k < 0)
        {
            k = GetFirstEmptyStack(inventory);
        }
        if(k < 0)
        {
            return j;
        }
        if(inventory.getInventoryItem(k) == null)
        {
            inventory.setInventoryItem(k, new ItemInstance(i, 0, ItemInstance.getDamage()));
        }
        int l = j;
        ItemInstance tempStack = inventory.getInventoryItem(k);
        if(l > tempStack.getMaxStackSize() - tempStack.count)
        {
            l = tempStack.getMaxStackSize() - tempStack.count;
        }
        if(l > inventory.getMaxItemCount() - tempStack.count)
        {
            l = inventory.getMaxItemCount() - tempStack.count;
        }
        if(l == 0)
        {
            return j;
        } else
        {
            j -= l;
            tempStack.count += l;
            inventory.setInventoryItem(k, tempStack);
            return j;
        }
    }

    private static int FindSlotToStoreItemInstance(InventoryBase inventory, ItemInstance ItemInstance)
    {
        for(int i = 0; i < inventory.getInventorySize(); i++)
        {
            ItemInstance tempStack = inventory.getInventoryItem(i);
            if(tempStack != null && tempStack.itemId == ItemInstance.itemId && tempStack.isStackable() && tempStack.count < tempStack.getMaxStackSize() && tempStack.count < inventory.getMaxItemCount() && (!tempStack.usesMeta() || tempStack.getDamage() == ItemInstance.getDamage()))
            {
                return i;
            }
        }

        return -1;
    }

    public static int GetNumOccupiedStacksInSlotRange(InventoryBase inventory, int iMinSlotIndex, int iMaxSlotIndex)
    {
        int iCount = 0;
        for(int i = iMinSlotIndex; i < inventory.getInventorySize() && i <= iMaxSlotIndex; i++)
        {
            if(inventory.getInventoryItem(i) != null)
            {
                iCount++;
            }
        }

        return iCount;
    }

    public static boolean AddItemInstanceToInventoryInSlotRange(InventoryBase inventory, ItemInstance ItemInstance, int iMinSlotIndex, int iMaxSlotIndex)
    {
        if(!ItemInstance.isDamaged())
        {
            ItemInstance.count = StorePartialItemInstanceInSlotRange(inventory, ItemInstance, iMinSlotIndex, iMaxSlotIndex);
            if(ItemInstance.count == 0)
            {
                return true;
            }
        }
        int i = GetFirstEmptyStackInSlotRange(inventory, iMinSlotIndex, iMaxSlotIndex);
        if(i >= 0)
        {
            inventory.setInventoryItem(i, ItemInstance);
            return true;
        } else
        {
            return false;
        }
    }

    private static int GetFirstEmptyStackInSlotRange(InventoryBase inventory, int iMinSlotIndex, int iMaxSlotIndex)
    {
        for(int i = iMinSlotIndex; i < inventory.getInventorySize() && i <= iMaxSlotIndex; i++)
        {
            if(inventory.getInventoryItem(i) == null)
            {
                return i;
            }
        }

        return -1;
    }

    private static int StorePartialItemInstanceInSlotRange(InventoryBase inventory, ItemInstance ItemInstance, int iMinSlotIndex, int iMaxSlotIndex)
    {
        int i = ItemInstance.itemId;
        int j = ItemInstance.count;
        int k = FindSlotToStoreItemInstanceInSlotRange(inventory, ItemInstance, iMinSlotIndex, iMaxSlotIndex);
        if(k < 0)
        {
            k = GetFirstEmptyStackInSlotRange(inventory, iMinSlotIndex, iMaxSlotIndex);
        }
        if(k < 0)
        {
            return j;
        }
        if(inventory.getInventoryItem(k) == null)
        {
            inventory.setInventoryItem(k, new ItemInstance(i, 0, ItemInstance.getDamage()));
        }
        int l = j;
        ItemInstance tempStack = inventory.getInventoryItem(k);
        if(l > tempStack.getMaxStackSize() - tempStack.count)
        {
            l = tempStack.getMaxStackSize() - tempStack.count;
        }
        if(l > inventory.getMaxItemCount() - tempStack.count)
        {
            l = inventory.getMaxItemCount() - tempStack.count;
        }
        if(l == 0)
        {
            return j;
        } else
        {
            j -= l;
            tempStack.count += l;
            inventory.setInventoryItem(k, tempStack);
            return j;
        }
    }

    private static int FindSlotToStoreItemInstanceInSlotRange(InventoryBase inventory, ItemInstance ItemInstance, int iMinSlotIndex, int iMaxSlotIndex)
    {
        for(int i = iMinSlotIndex; i < inventory.getInventorySize() && i <= iMaxSlotIndex; i++)
        {
            ItemInstance tempStack = inventory.getInventoryItem(i);
            if(tempStack != null && tempStack.itemId == ItemInstance.itemId && tempStack.isStackable() && tempStack.count < tempStack.getMaxStackSize() && tempStack.count < inventory.getMaxItemCount() && (!tempStack.usesMeta() || tempStack.getDamage() == ItemInstance.getDamage()))
            {
                return i;
            }
        }

        return -1;
    }
}
