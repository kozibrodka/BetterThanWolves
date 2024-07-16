// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCUtilsInventory.java

package net.kozibrodka.wolves.utils;

import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public class InventoryHandler
{

    public InventoryHandler()
    {
    }

    public static void clearInventoryContents(Inventory inventory)
    {
        for(int slot = 0; slot < inventory.size(); slot++)
        {
            ItemStack ItemInstance = inventory.getStack(slot);
            if(ItemInstance != null)
            {
                inventory.setStack(slot, null);
            }
        }

    }

    public static void ejectInventoryContents(World world, int x, int y, int z, Inventory inventory)
    {
slotLoop:
        for(int slot = 0; slot < inventory.size(); slot++)
        {
            ItemStack itemInstance = inventory.getStack(slot);
            if(itemInstance == null)
            {
                continue;
            }
            float xOffset = world.random.nextFloat() * 0.7F + 0.15F;
            float yOffset = world.random.nextFloat() * 0.7F + 0.15F;
            float zOffset = world.random.nextFloat() * 0.7F + 0.15F;
            do
            {
                if(itemInstance.count <= 0)
                {
                    continue slotLoop;
                }
                int randomStackSize = world.random.nextInt(21) + 10;
                if(randomStackSize > itemInstance.count)
                {
                    randomStackSize = itemInstance.count;
                }
                itemInstance.count -= randomStackSize;
                ItemEntity itemEntity = new ItemEntity(world, (float)x + xOffset, (float)y + yOffset, (float)z + zOffset, new ItemStack(itemInstance.itemId, randomStackSize, itemInstance.getDamage()));
                float randomVelocityFactor = 0.05F;
                itemEntity.velocityX = (float)world.random.nextGaussian() * randomVelocityFactor;
                itemEntity.velocityY = (float)world.random.nextGaussian() * randomVelocityFactor + 0.2F;
                itemEntity.velocityZ = (float)world.random.nextGaussian() * randomVelocityFactor;
                itemEntity.pickupDelay = 10;
                world.spawnEntity(itemEntity);
            } while(true);
        }

    }

    public static boolean consumeItemsInInventory(Inventory inventory, int itemId, int itemDamage, int itemCount)
    {
        for(int slot = 0; slot < inventory.size(); slot++)
        {
            ItemStack tempItemInstance = inventory.getStack(slot);
            if(tempItemInstance == null)
            {
                continue;
            }
            Item tempItem = tempItemInstance.getItem();
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
            inventory.setStack(slot, null);
        }

        return false;
    }

    public static ItemStack decreaseStackSize(Inventory inventory, int slot, int amount)
    {
        if(inventory.getStack(slot) != null)
        {
            if(inventory.getStack(slot).count <= amount)
            {
                ItemStack itemInstance = inventory.getStack(slot);
                inventory.setStack(slot, null);
                return itemInstance;
            }
            ItemStack splitStack = inventory.getStack(slot).split(amount);
            if(inventory.getStack(slot).count == 0)
            {
                inventory.setStack(slot, null);
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

    public static int getFirstOccupiedStack(Inventory inventory)
    {
        for(int slot = 0; slot < inventory.size(); slot++)
        {
            if(inventory.getStack(slot) != null)
            {
                return slot;
            }
        }

        return -1;
    }

    public static int getFirstOccupiedStackExcludingItem(Inventory inventory, int excludeditemId)
    {
        for(int slot = 0; slot < inventory.size(); slot++)
        {
            if(inventory.getStack(slot) != null && inventory.getStack(slot).getItem().id != excludeditemId)
            {
                return slot;
            }
        }

        return -1;
    }

    public static int getFirstOccupiedStackOfItem(Inventory inventory, int itemId)
    {
        for(int slot = 0; slot < inventory.size(); slot++)
        {
            if(inventory.getStack(slot) != null && inventory.getStack(slot).getItem().id == itemId)
            {
                return slot;
            }
        }

        return -1;
    }

    public static int itemCountInInventory(Inventory inventory, int itemId, int itemDamage)
    {
        int itemCount = 0;
        for(int slot = 0; slot < inventory.size(); slot++)
        {
            ItemStack tempStack = inventory.getStack(slot);
            if(tempStack != null && tempStack.getItem().id == itemId && (itemDamage == -1 || tempStack.getDamage() == itemDamage))
            {
                itemCount += inventory.getStack(slot).count;
            }
        }

        return itemCount;
    }

    public static int getOccupiedStacksCount(Inventory inventory)
    {
        int count = 0;
        for(int i = 0; i < inventory.size(); i++)
        {
            if(inventory.getStack(i) != null)
            {
                count++;
            }
        }

        return count;
    }

    public static boolean addSingleItemToInventory(Inventory inventory, int itemId, int itemDamage)
    {
        ItemStack ItemInstance = new ItemStack(itemId, 1, itemDamage);
        return addItemInstanceToInventory(inventory, ItemInstance);
    }

    public static boolean addItemInstanceToInventory(Inventory inventory, ItemStack itemInstance)
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
            inventory.setStack(slot, itemInstance);
            return true;
        } else
        {
            return false;
        }
    }

    private static int getFirstEmptyStack(Inventory inventory)
    {
        for(int slot = 0; slot < inventory.size(); slot++)
        {
            if(inventory.getStack(slot) == null)
            {
                return slot;
            }
        }

        return -1;
    }

    private static int storePartialItemInstance(Inventory inventory, ItemStack itemInstance)
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
        if(inventory.getStack(slot) == null)
        {
            inventory.setStack(slot, new ItemStack(itemId, 0, itemInstance.getDamage()));
        }
        int insertedItems = count;
        ItemStack tempStack = inventory.getStack(slot);
        if(insertedItems > tempStack.getMaxCount() - tempStack.count)
        {
            insertedItems = tempStack.getMaxCount() - tempStack.count;
        }
        if(insertedItems > inventory.getMaxCountPerStack() - tempStack.count)
        {
            insertedItems = inventory.getMaxCountPerStack() - tempStack.count;
        }
        if(insertedItems == 0)
        {
            return count;
        } else
        {
            count -= insertedItems;
            tempStack.count += insertedItems;
            inventory.setStack(slot, tempStack);
            return count;
        }
    }

    private static int findValidSlotForItem(Inventory inventory, ItemStack itemInstance)
    {
        for(int slot = 0; slot < inventory.size(); slot++)
        {
            ItemStack tempStack = inventory.getStack(slot);
            if(tempStack != null && tempStack.itemId == itemInstance.itemId && tempStack.isStackable() && tempStack.count < tempStack.getMaxCount() && tempStack.count < inventory.getMaxCountPerStack() && (!tempStack.hasSubtypes() || tempStack.getDamage() == itemInstance.getDamage()))
            {
                return slot;
            }
        }

        return -1;
    }

    public static int getOccupiedSlotCountWithinBounds(Inventory inventory, int minimumSlot, int maximumSlot)
    {
        int slotCount = 0;
        for(int slot = minimumSlot; slot < inventory.size() && slot <= maximumSlot; slot++)
        {
            if(inventory.getStack(slot) != null)
            {
                slotCount++;
            }
        }

        return slotCount;
    }

    public static boolean addItemWithinSlotBounds(Inventory inventory, ItemStack itemInstance, int minimumSlot, int maximumSlot)
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
            inventory.setStack(slot, itemInstance);
            return true;
        } else
        {
            return false;
        }
    }

    private static int getFirstEmptySlotWithinBounds(Inventory inventory, int minimumSlot, int maximumSlot)
    {
        for(int slot = minimumSlot; slot < inventory.size() && slot <= maximumSlot; slot++)
        {
            if(inventory.getStack(slot) == null)
            {
                return slot;
            }
        }

        return -1;
    }

    private static int storePartialItemInstanceWithinSlotBounds(Inventory inventory, ItemStack itemInstance, int minimumSlot, int maximumSlot)
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
        if(inventory.getStack(slot) == null)
        {
            inventory.setStack(slot, new ItemStack(itemId, 0, itemInstance.getDamage()));
        }
        int insertedItems = count;
        ItemStack tempStack = inventory.getStack(slot);
        if(insertedItems > tempStack.getMaxCount() - tempStack.count)
        {
            insertedItems = tempStack.getMaxCount() - tempStack.count;
        }
        if(insertedItems > inventory.getMaxCountPerStack() - tempStack.count)
        {
            insertedItems = inventory.getMaxCountPerStack() - tempStack.count;
        }
        if(insertedItems == 0)
        {
            return count;
        } else
        {
            count -= insertedItems;
            tempStack.count += insertedItems;
            inventory.setStack(slot, tempStack);
            return count;
        }
    }

    private static int findValidSlotWithinBounds(Inventory inventory, ItemStack itemInstance, int minimumSlot, int maximumSlot)
    {
        for(int slot = minimumSlot; slot < inventory.size() && slot <= maximumSlot; slot++)
        {
            ItemStack tempStack = inventory.getStack(slot);
            if(tempStack != null && tempStack.itemId == itemInstance.itemId && tempStack.isStackable() && tempStack.count < tempStack.getMaxCount() && tempStack.count < inventory.getMaxCountPerStack() && (!tempStack.hasSubtypes() || tempStack.getDamage() == itemInstance.getDamage()))
            {
                return slot;
            }
        }

        return -1;
    }
}
