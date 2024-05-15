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

    public static void clearInventoryContents(InventoryBase inventory)
    {
        for(int slot = 0; slot < inventory.getInventorySize(); slot++)
        {
            ItemInstance ItemInstance = inventory.getInventoryItem(slot);
            if(ItemInstance != null)
            {
                inventory.setInventoryItem(slot, null);
            }
        }

    }

    public static void ejectInventoryContents(Level world, int x, int y, int z, InventoryBase inventory)
    {
slotLoop:
        for(int slot = 0; slot < inventory.getInventorySize(); slot++)
        {
            ItemInstance itemInstance = inventory.getInventoryItem(slot);
            if(itemInstance == null)
            {
                continue;
            }
            float xOffset = world.rand.nextFloat() * 0.7F + 0.15F;
            float yOffset = world.rand.nextFloat() * 0.7F + 0.15F;
            float zOffset = world.rand.nextFloat() * 0.7F + 0.15F;
            do
            {
                if(itemInstance.count <= 0)
                {
                    continue slotLoop;
                }
                int randomStackSize = world.rand.nextInt(21) + 10;
                if(randomStackSize > itemInstance.count)
                {
                    randomStackSize = itemInstance.count;
                }
                itemInstance.count -= randomStackSize;
                Item itemEntity = new Item(world, (float)x + xOffset, (float)y + yOffset, (float)z + zOffset, new ItemInstance(itemInstance.itemId, randomStackSize, itemInstance.getDamage()));
                float randomVelocityFactor = 0.05F;
                itemEntity.velocityX = (float)world.rand.nextGaussian() * randomVelocityFactor;
                itemEntity.velocityY = (float)world.rand.nextGaussian() * randomVelocityFactor + 0.2F;
                itemEntity.velocityZ = (float)world.rand.nextGaussian() * randomVelocityFactor;
                itemEntity.pickupDelay = 10;
                world.spawnEntity(itemEntity);
            } while(true);
        }

    }

    public static boolean consumeItemsInInventory(InventoryBase inventory, int itemId, int itemDamage, int itemCount)
    {
        for(int slot = 0; slot < inventory.getInventorySize(); slot++)
        {
            ItemInstance tempItemInstance = inventory.getInventoryItem(slot);
            if(tempItemInstance == null)
            {
                continue;
            }
            ItemBase tempItem = tempItemInstance.getType();
            if(tempItem.id != itemId || itemDamage != -1 && tempItemInstance.getDamage() != itemDamage)
            {
                continue;
            }
            if(tempItemInstance.count >= itemCount)
            {
                decreaseStackSize(inventory, slot, itemCount);
                return true;
            }
            itemCount -= tempItemInstance.count;
            inventory.setInventoryItem(slot, null);
        }

        return false;
    }

    public static ItemInstance decreaseStackSize(InventoryBase inventory, int slot, int amount)
    {
        if(inventory.getInventoryItem(slot) != null)
        {
            if(inventory.getInventoryItem(slot).count <= amount)
            {
                ItemInstance itemInstance = inventory.getInventoryItem(slot);
                inventory.setInventoryItem(slot, null);
                return itemInstance;
            }
            ItemInstance splitStack = inventory.getInventoryItem(slot).split(amount);
            if(inventory.getInventoryItem(slot).count == 0)
            {
                inventory.setInventoryItem(slot, null);
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

    public static int getFirstOccupiedStack(InventoryBase inventory)
    {
        for(int slot = 0; slot < inventory.getInventorySize(); slot++)
        {
            if(inventory.getInventoryItem(slot) != null)
            {
                return slot;
            }
        }

        return -1;
    }

    public static int getFirstOccupiedStackExcludingItem(InventoryBase inventory, int excludeditemId)
    {
        for(int slot = 0; slot < inventory.getInventorySize(); slot++)
        {
            if(inventory.getInventoryItem(slot) != null && inventory.getInventoryItem(slot).getType().id != excludeditemId)
            {
                return slot;
            }
        }

        return -1;
    }

    public static int getFirstOccupiedStackOfItem(InventoryBase inventory, int itemId)
    {
        for(int slot = 0; slot < inventory.getInventorySize(); slot++)
        {
            if(inventory.getInventoryItem(slot) != null && inventory.getInventoryItem(slot).getType().id == itemId)
            {
                return slot;
            }
        }

        return -1;
    }

    public static int itemCountInInventory(InventoryBase inventory, int itemId, int itemDamage)
    {
        int itemCount = 0;
        for(int slot = 0; slot < inventory.getInventorySize(); slot++)
        {
            ItemInstance tempStack = inventory.getInventoryItem(slot);
            if(tempStack != null && tempStack.getType().id == itemId && (itemDamage == -1 || tempStack.getDamage() == itemDamage))
            {
                itemCount += inventory.getInventoryItem(slot).count;
            }
        }

        return itemCount;
    }

    public static int getOccupiedStacksCount(InventoryBase inventory)
    {
        int count = 0;
        for(int i = 0; i < inventory.getInventorySize(); i++)
        {
            if(inventory.getInventoryItem(i) != null)
            {
                count++;
            }
        }

        return count;
    }

    public static boolean addSingleItemToInventory(InventoryBase inventory, int itemId, int itemDamage)
    {
        ItemInstance ItemInstance = new ItemInstance(itemId, 1, itemDamage);
        return addItemInstanceToInventory(inventory, ItemInstance);
    }

    public static boolean addItemInstanceToInventory(InventoryBase inventory, ItemInstance itemInstance)
    {
        if(!itemInstance.isDamaged())
        {
            itemInstance.count = storePartialItemInstance(inventory, itemInstance);
            if(itemInstance.count == 0)
            {
                return true;
            }
        }
        int slot = getFirstEmptyStack(inventory);
        if(slot >= 0)
        {
            inventory.setInventoryItem(slot, itemInstance);
            return true;
        } else
        {
            return false;
        }
    }

    private static int getFirstEmptyStack(InventoryBase inventory)
    {
        for(int slot = 0; slot < inventory.getInventorySize(); slot++)
        {
            if(inventory.getInventoryItem(slot) == null)
            {
                return slot;
            }
        }

        return -1;
    }

    private static int storePartialItemInstance(InventoryBase inventory, ItemInstance itemInstance)
    {
        int itemId = itemInstance.itemId;
        int count = itemInstance.count;
        int slot = findValidSlotForItem(inventory, itemInstance);
        if(slot < 0)
        {
            slot = getFirstEmptyStack(inventory);
        }
        if(slot < 0)
        {
            return count;
        }
        if(inventory.getInventoryItem(slot) == null)
        {
            inventory.setInventoryItem(slot, new ItemInstance(itemId, 0, itemInstance.getDamage()));
        }
        int insertedItems = count;
        ItemInstance tempStack = inventory.getInventoryItem(slot);
        if(insertedItems > tempStack.getMaxStackSize() - tempStack.count)
        {
            insertedItems = tempStack.getMaxStackSize() - tempStack.count;
        }
        if(insertedItems > inventory.getMaxItemCount() - tempStack.count)
        {
            insertedItems = inventory.getMaxItemCount() - tempStack.count;
        }
        if(insertedItems == 0)
        {
            return count;
        } else
        {
            count -= insertedItems;
            tempStack.count += insertedItems;
            inventory.setInventoryItem(slot, tempStack);
            return count;
        }
    }

    private static int findValidSlotForItem(InventoryBase inventory, ItemInstance itemInstance)
    {
        for(int slot = 0; slot < inventory.getInventorySize(); slot++)
        {
            ItemInstance tempStack = inventory.getInventoryItem(slot);
            if(tempStack != null && tempStack.itemId == itemInstance.itemId && tempStack.isStackable() && tempStack.count < tempStack.getMaxStackSize() && tempStack.count < inventory.getMaxItemCount() && (!tempStack.usesMeta() || tempStack.getDamage() == itemInstance.getDamage()))
            {
                return slot;
            }
        }

        return -1;
    }

    public static int getOccupiedSlotCountWithinBounds(InventoryBase inventory, int minimumSlot, int maximumSlot)
    {
        int slotCount = 0;
        for(int slot = minimumSlot; slot < inventory.getInventorySize() && slot <= maximumSlot; slot++)
        {
            if(inventory.getInventoryItem(slot) != null)
            {
                slotCount++;
            }
        }

        return slotCount;
    }

    public static boolean addItemWithinSlotBounds(InventoryBase inventory, ItemInstance itemInstance, int minimumSlot, int maximumSlot)
    {
        if(!itemInstance.isDamaged())
        {
            itemInstance.count = storePartialItemInstanceWithinSlotBounds(inventory, itemInstance, minimumSlot, maximumSlot);
            if(itemInstance.count == 0)
            {
                return true;
            }
        }
        int slot = getFirstEmptySlotWithinBounds(inventory, minimumSlot, maximumSlot);
        if(slot >= 0)
        {
            inventory.setInventoryItem(slot, itemInstance);
            return true;
        } else
        {
            return false;
        }
    }

    private static int getFirstEmptySlotWithinBounds(InventoryBase inventory, int minimumSlot, int maximumSlot)
    {
        for(int slot = minimumSlot; slot < inventory.getInventorySize() && slot <= maximumSlot; slot++)
        {
            if(inventory.getInventoryItem(slot) == null)
            {
                return slot;
            }
        }

        return -1;
    }

    private static int storePartialItemInstanceWithinSlotBounds(InventoryBase inventory, ItemInstance itemInstance, int minimumSlot, int maximumSlot)
    {
        int itemId = itemInstance.itemId;
        int count = itemInstance.count;
        int slot = findValidSlotWithinBounds(inventory, itemInstance, minimumSlot, maximumSlot);
        if(slot < 0)
        {
            slot = getFirstEmptySlotWithinBounds(inventory, minimumSlot, maximumSlot);
        }
        if(slot < 0)
        {
            return count;
        }
        if(inventory.getInventoryItem(slot) == null)
        {
            inventory.setInventoryItem(slot, new ItemInstance(itemId, 0, itemInstance.getDamage()));
        }
        int insertedItems = count;
        ItemInstance tempStack = inventory.getInventoryItem(slot);
        if(insertedItems > tempStack.getMaxStackSize() - tempStack.count)
        {
            insertedItems = tempStack.getMaxStackSize() - tempStack.count;
        }
        if(insertedItems > inventory.getMaxItemCount() - tempStack.count)
        {
            insertedItems = inventory.getMaxItemCount() - tempStack.count;
        }
        if(insertedItems == 0)
        {
            return count;
        } else
        {
            count -= insertedItems;
            tempStack.count += insertedItems;
            inventory.setInventoryItem(slot, tempStack);
            return count;
        }
    }

    private static int findValidSlotWithinBounds(InventoryBase inventory, ItemInstance itemInstance, int minimumSlot, int maximumSlot)
    {
        for(int slot = minimumSlot; slot < inventory.getInventorySize() && slot <= maximumSlot; slot++)
        {
            ItemInstance tempStack = inventory.getInventoryItem(slot);
            if(tempStack != null && tempStack.itemId == itemInstance.itemId && tempStack.isStackable() && tempStack.count < tempStack.getMaxStackSize() && tempStack.count < inventory.getMaxItemCount() && (!tempStack.usesMeta() || tempStack.getDamage() == itemInstance.getDamage()))
            {
                return slot;
            }
        }

        return -1;
    }
}
