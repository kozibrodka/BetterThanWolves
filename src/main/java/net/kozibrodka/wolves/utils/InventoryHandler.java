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
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;


public class InventoryHandler {

    public InventoryHandler() {
    }

    public static void clearInventoryContents(Inventory inventory) {
        for (int slot = 0; slot < inventory.size(); slot++) {
            ItemStack ItemInstance = inventory.getStack(slot);
            if (ItemInstance != null) {
                inventory.setStack(slot, null);
            }
        }

    }

    public static void ejectInventoryContents(World world, int x, int y, int z, Inventory inventory) {
        slotLoop:
        for (int slot = 0; slot < inventory.size(); slot++) {
            ItemStack itemStack = inventory.getStack(slot);
            if (itemStack == null) {
                continue;
            }
            float xOffset = world.random.nextFloat() * 0.7F + 0.15F;
            float yOffset = world.random.nextFloat() * 0.7F + 0.15F;
            float zOffset = world.random.nextFloat() * 0.7F + 0.15F;
            do {
                if (itemStack.count <= 0) {
                    continue slotLoop;
                }
                int randomStackSize = world.random.nextInt(21) + 10;
                if (randomStackSize > itemStack.count) {
                    randomStackSize = itemStack.count;
                }
                itemStack.count -= randomStackSize;
                ItemEntity itemEntity = new ItemEntity(world, (float) x + xOffset, (float) y + yOffset, (float) z + zOffset, new ItemStack(itemStack.getItem(), randomStackSize, itemStack.getDamage()));
                float randomVelocityFactor = 0.05F;
                itemEntity.velocityX = (float) world.random.nextGaussian() * randomVelocityFactor;
                itemEntity.velocityY = (float) world.random.nextGaussian() * randomVelocityFactor + 0.2F;
                itemEntity.velocityZ = (float) world.random.nextGaussian() * randomVelocityFactor;
                itemEntity.pickupDelay = 10;
                world.spawnEntity(itemEntity);
            } while (true);
        }

    }

    public static void consumeItemsInInventory(Inventory inventory, Identifier itemId, int itemDamage, int itemCount) {
        for (int slot = 0; slot < inventory.size(); slot++) {
            ItemStack tempItemInstance = inventory.getStack(slot);
            if (tempItemInstance == null) {
                continue;
            }
            Item tempItem = tempItemInstance.getItem();
            Identifier tempIdentifier = ItemRegistry.INSTANCE.getId(tempItem);
            if (tempIdentifier != itemId || itemDamage != -1 && tempItemInstance.getDamage() != itemDamage) {
                continue;
            }
            if (tempItemInstance.count >= itemCount) {
                decreaseStackSize(inventory, slot, itemCount);
                return;
            }
            itemCount -= tempItemInstance.count;
            inventory.setStack(slot, null);
        }

    }

    public static ItemStack decreaseStackSize(Inventory inventory, int slot, int amount) {
        if (inventory.getStack(slot) != null) {
            if (inventory.getStack(slot).count <= amount) {
                ItemStack itemInstance = inventory.getStack(slot);
                inventory.setStack(slot, null);
                return itemInstance;
            }
            ItemStack splitStack = inventory.getStack(slot).split(amount);
            if (inventory.getStack(slot).count == 0) {
                inventory.setStack(slot, null);
            } else {
                inventory.markDirty();
            }
            return splitStack;
        } else {
            return null;
        }
    }

    public static int getFirstOccupiedStackExcludingItem(Inventory inventory, Identifier excludedItemId) {
        for (int slot = 0; slot < inventory.size(); slot++) {
            if (inventory.getStack(slot) != null && ItemRegistry.INSTANCE.getId(inventory.getStack(slot).getItem()) != excludedItemId) {
                return slot;
            }
        }
        return -1;
    }

    public static int getFirstOccupiedStackOfItem(Inventory inventory, Identifier itemId) {
        for (int slot = 0; slot < inventory.size(); slot++) {
            if (inventory.getStack(slot) != null && ItemRegistry.INSTANCE.getId(inventory.getStack(slot).getItem()) == itemId) {
                return slot;
            }
        }
        return -1;
    }

    public static int itemCountInInventory(Inventory inventory, Identifier itemId, int itemDamage) {
        int itemCount = 0;
        for (int slot = 0; slot < inventory.size(); slot++) {
            ItemStack tempStack = inventory.getStack(slot);
            if (tempStack != null && ItemRegistry.INSTANCE.getId(tempStack.getItem()) == itemId && (itemDamage == -1 || tempStack.getDamage() == itemDamage)) {
                itemCount += inventory.getStack(slot).count;
            }
        }
        return itemCount;
    }

    public static int getOccupiedStacksCount(Inventory inventory) {
        int count = 0;
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i) != null) {
                count++;
            }
        }
        return count;
    }

    // I could not find a good way to change this one because it relies on block drops which currently can
    // only be accessed through numeric IDs. So I am completely cooked until there is a better solution.
    public static boolean addSingleItemToInventory(Inventory inventory, int itemId, int itemDamage) {
        ItemStack ItemInstance = new ItemStack(itemId, 1, itemDamage);
        return addItemInstanceToInventory(inventory, ItemInstance);
    }

    public static boolean addItemInstanceToInventory(Inventory inventory, ItemStack itemInstance) {
        if (!itemInstance.isDamaged()) {
            itemInstance.count = storePartialItemStack(inventory, itemInstance);
            if (itemInstance.count == 0) {
                return true;
            }
        }
        int slot = getFirstEmptyStack(inventory);
        if (slot >= 0) {
            inventory.setStack(slot, itemInstance);
            return true;
        } else {
            return false;
        }
    }

    private static int getFirstEmptyStack(Inventory inventory) {
        for (int slot = 0; slot < inventory.size(); slot++) {
            if (inventory.getStack(slot) == null) {
                return slot;
            }
        }
        return -1;
    }

    private static int storePartialItemStack(Inventory inventory, ItemStack itemStack) {
        Item item = itemStack.getItem();
        int count = itemStack.count;
        int slot = findValidSlotForItem(inventory, itemStack);
        if (slot < 0) {
            slot = getFirstEmptyStack(inventory);
        }
        if (slot < 0) {
            return count;
        }
        if (inventory.getStack(slot) == null) {
            inventory.setStack(slot, new ItemStack(item, 0, itemStack.getDamage()));
        }
        int insertedItems = count;
        ItemStack tempStack = inventory.getStack(slot);
        if (insertedItems > tempStack.getMaxCount() - tempStack.count) {
            insertedItems = tempStack.getMaxCount() - tempStack.count;
        }
        if (insertedItems > inventory.getMaxCountPerStack() - tempStack.count) {
            insertedItems = inventory.getMaxCountPerStack() - tempStack.count;
        }
        if (insertedItems != 0) {
            count -= insertedItems;
            tempStack.count += insertedItems;
            inventory.setStack(slot, tempStack);
        }
        return count;
    }

    private static int findValidSlotForItem(Inventory inventory, ItemStack itemStack) {
        for (int slot = 0; slot < inventory.size(); slot++) {
            ItemStack tempStack = inventory.getStack(slot);
            if (tempStack != null && tempStack.itemId == itemStack.itemId && tempStack.isStackable() && tempStack.count < tempStack.getMaxCount() && tempStack.count < inventory.getMaxCountPerStack() && (!tempStack.hasSubtypes() || tempStack.getDamage() == itemStack.getDamage())) {
                return slot;
            }
        }
        return -1;
    }

    public static int getOccupiedSlotCountWithinBounds(Inventory inventory, int minimumSlot, int maximumSlot) {
        int slotCount = 0;
        for (int slot = minimumSlot; slot < inventory.size() && slot <= maximumSlot; slot++) {
            if (inventory.getStack(slot) != null) {
                slotCount++;
            }
        }
        return slotCount;
    }

    public static boolean addItemWithinSlotBounds(Inventory inventory, ItemStack itemStack, int minimumSlot, int maximumSlot) {
        if (!itemStack.isDamaged()) {
            itemStack.count = storePartialItemInstanceWithinSlotBounds(inventory, itemStack, minimumSlot, maximumSlot);
            if (itemStack.count == 0) {
                return true;
            }
        }
        int slot = getFirstEmptySlotWithinBounds(inventory, minimumSlot, maximumSlot);
        if (slot >= 0) {
            inventory.setStack(slot, itemStack);
            return true;
        } else {
            return false;
        }
    }

    private static int getFirstEmptySlotWithinBounds(Inventory inventory, int minimumSlot, int maximumSlot) {
        for (int slot = minimumSlot; slot < inventory.size() && slot <= maximumSlot; slot++) {
            if (inventory.getStack(slot) == null) {
                return slot;
            }
        }
        return -1;
    }

    private static int storePartialItemInstanceWithinSlotBounds(Inventory inventory, ItemStack itemStack, int minimumSlot, int maximumSlot) {
        int itemId = itemStack.itemId;
        int count = itemStack.count;
        int slot = findValidSlotWithinBounds(inventory, itemStack, minimumSlot, maximumSlot);
        if (slot < 0) {
            slot = getFirstEmptySlotWithinBounds(inventory, minimumSlot, maximumSlot);
        }
        if (slot < 0) {
            return count;
        }
        if (inventory.getStack(slot) == null) {
            inventory.setStack(slot, new ItemStack(itemId, 0, itemStack.getDamage()));
        }
        int insertedItems = count;
        ItemStack tempStack = inventory.getStack(slot);
        if (insertedItems > tempStack.getMaxCount() - tempStack.count) {
            insertedItems = tempStack.getMaxCount() - tempStack.count;
        }
        if (insertedItems > inventory.getMaxCountPerStack() - tempStack.count) {
            insertedItems = inventory.getMaxCountPerStack() - tempStack.count;
        }
        if (insertedItems != 0) {
            count -= insertedItems;
            tempStack.count += insertedItems;
            inventory.setStack(slot, tempStack);
        }
        return count;
    }

    private static int findValidSlotWithinBounds(Inventory inventory, ItemStack itemInstance, int minimumSlot, int maximumSlot) {
        for (int slot = minimumSlot; slot < inventory.size() && slot <= maximumSlot; slot++) {
            ItemStack tempStack = inventory.getStack(slot);
            if (tempStack != null && tempStack.itemId == itemInstance.itemId && tempStack.isStackable() && tempStack.count < tempStack.getMaxCount() && tempStack.count < inventory.getMaxCountPerStack() && (!tempStack.hasSubtypes() || tempStack.getDamage() == itemInstance.getDamage())) {
                return slot;
            }
        }
        return -1;
    }
}
