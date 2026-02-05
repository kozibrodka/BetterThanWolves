package net.kozibrodka.wolves.block.entity;

import net.kozibrodka.wolves.container.autoanvil.AutoAnvilMatrix;
import net.kozibrodka.wolves.container.autoanvil.InternalAutoAnvilScreenHandler;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.recipe.AnvilCraftingManager;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class AutomaticAnvilBlockEntity extends BlockEntity implements Inventory {
    private static final int INPUT_START = 0;
    private static final int INPUT_END = 25;
    private static final int OUTPUT = 25;
    private static final int INVENTORY_SIZE = 26;
    private static final int CHECK_INTERVAL = 40;
    public static final int RECIPE_TIME = 1000;

    private final ItemStack[] anvilItemStacks = new ItemStack[INVENTORY_SIZE];

    private int craftingProgress;
    private int checkTimer;

    @Override
    public void tick() {
        if (checkTimer < CHECK_INTERVAL) {
            checkTimer++;
        } else {
            checkTimer = 0;
            if (!checkMultiblockStructure()) {
                craftingProgress = 0;
                return;
            }
        }
        if (craftingProgress < RECIPE_TIME) {
            if (canCraft()) {
                craftingProgress++;
            } else {
                craftingProgress = 0;
            }
        } else {
            if (canCraft()) {
                craftItem();
            }
            craftingProgress = 0;
        }
    }

    private boolean checkMultiblockStructure() {
        // Calculate central coordinates
        int meta = world.getBlockMeta(x, y, z) % 6;
        int xCentered = x;
        int zCentered = z;
        switch (meta) {
            case 2:
                zCentered+=2;
                break;
            case 3:
                zCentered-=2;
                break;
            case 4:
                xCentered+=2;
                break;
            case 5:
                xCentered-=2;
                break;
        }
        // Check structural integrity
        int frameCount = 0;
        boolean foundPowerInput = false;
        for (int xOffset = -2; xOffset <= 2; xOffset++) {
            for (int zOffset = -2; zOffset <= 2; zOffset++) {
                int blockId = world.getBlockId(xCentered + xOffset, y, zCentered + zOffset);
                if (blockId == BlockListener.anvilFrame.id) {
                    frameCount++;
                } else if (blockId == BlockListener.machinePowerInput.id) {
                    if (world.getBlockMeta(xCentered + xOffset, y, zCentered + zOffset) >= 6) {
                        foundPowerInput = true;
                    }
                } else if (blockId != BlockListener.outputBus.id && blockId != BlockListener.automaticAnvil.id) {
                    return false;
                }
            }
        }
        if (!foundPowerInput) {
            return false;
        }
        if (frameCount < 22) {
            return false;
        }
        for (int xOffset = -2; xOffset <= 2; xOffset++) {
            for (int zOffset = -2; zOffset <= 2; zOffset++) {
                int blockId = world.getBlockId(xCentered + xOffset, y + 1, zCentered + zOffset);
                if (blockId != BlockListener.inputBus.id) {
                    return false;
                }
            }
        }
        // Meta values to coordinates
        // 2 -> z+
        // 3 -> z-
        // 4 -> x+
        // 5 -> x-
        // Automatic input
        switch (meta) {
            case 2:
                for (int zOffset = 2; zOffset >= -2; zOffset--) {
                    for (int xOffset = 2; xOffset >= -2; xOffset--) {
                        if (world.getBlockEntity(xCentered + xOffset, y + 1, zCentered + zOffset) instanceof MachineBusBlockEntity machineBusBlockEntity) {
                            if (machineBusBlockEntity.getStack(0) == null) {
                                continue;
                            }
                            int ovenIndex = (-xOffset + 2) + (-zOffset + 2) * 5;
                            if (anvilItemStacks[ovenIndex] == null) {
                                anvilItemStacks[ovenIndex] = machineBusBlockEntity.getStack(0);
                                machineBusBlockEntity.setStack(0, null);
                            } else if (anvilItemStacks[ovenIndex].isItemEqual(machineBusBlockEntity.getStack(0))) {
                                int totalCount = anvilItemStacks[ovenIndex].count + machineBusBlockEntity.getStack(0).count;
                                if (totalCount <= machineBusBlockEntity.getStack(0).getMaxCount()) {
                                    anvilItemStacks[ovenIndex].count = totalCount;
                                    machineBusBlockEntity.setStack(0, null);
                                } else {
                                    int leftovers = totalCount - machineBusBlockEntity.getStack(0).getMaxCount();
                                    anvilItemStacks[ovenIndex].count = machineBusBlockEntity.getStack(0).getMaxCount();
                                    ItemStack changedItem = machineBusBlockEntity.getStack(0);
                                    changedItem.count = leftovers;
                                    machineBusBlockEntity.setStack(0, changedItem);
                                }
                            }
                        }
                    }
                }
                break;
            case 3:
                for (int zOffset = -2; zOffset <= 2; zOffset++) {
                    for (int xOffset = -2; xOffset <= 2; xOffset++) {
                        if (world.getBlockEntity(xCentered + xOffset, y + 1, zCentered + zOffset) instanceof MachineBusBlockEntity machineBusBlockEntity) {
                            if (machineBusBlockEntity.getStack(0) == null) {
                                continue;
                            }
                            int ovenIndex = (xOffset + 2) + (zOffset + 2) * 5;
                            if (anvilItemStacks[ovenIndex] == null) {
                                anvilItemStacks[ovenIndex] = machineBusBlockEntity.getStack(0);
                                machineBusBlockEntity.setStack(0, null);
                            } else if (anvilItemStacks[ovenIndex].isItemEqual(machineBusBlockEntity.getStack(0))) {
                                int totalCount = anvilItemStacks[ovenIndex].count + machineBusBlockEntity.getStack(0).count;
                                if (totalCount <= machineBusBlockEntity.getStack(0).getMaxCount()) {
                                    anvilItemStacks[ovenIndex].count = totalCount;
                                    machineBusBlockEntity.setStack(0, null);
                                } else {
                                    int leftovers = totalCount - machineBusBlockEntity.getStack(0).getMaxCount();
                                    anvilItemStacks[ovenIndex].count = machineBusBlockEntity.getStack(0).getMaxCount();
                                    ItemStack changedItem = machineBusBlockEntity.getStack(0);
                                    changedItem.count = leftovers;
                                    machineBusBlockEntity.setStack(0, changedItem);
                                }
                            }
                        }
                    }
                }
                break;
            case 4:
                for (int xOffset = 2; xOffset >= -2; xOffset--) {
                    for (int zOffset = -2; zOffset <= 2; zOffset++) {
                        if (world.getBlockEntity(xCentered + xOffset, y + 1, zCentered + zOffset) instanceof MachineBusBlockEntity machineBusBlockEntity) {
                            if (machineBusBlockEntity.getStack(0) == null) {
                                continue;
                            }
                            int ovenIndex = (zOffset + 2) + (-xOffset + 2) * 5;
                            if (anvilItemStacks[ovenIndex] == null) {
                                anvilItemStacks[ovenIndex] = machineBusBlockEntity.getStack(0);
                                machineBusBlockEntity.setStack(0, null);
                            } else if (anvilItemStacks[ovenIndex].isItemEqual(machineBusBlockEntity.getStack(0))) {
                                int totalCount = anvilItemStacks[ovenIndex].count + machineBusBlockEntity.getStack(0).count;
                                if (totalCount <= machineBusBlockEntity.getStack(0).getMaxCount()) {
                                    anvilItemStacks[ovenIndex].count = totalCount;
                                    machineBusBlockEntity.setStack(0, null);
                                } else {
                                    int leftovers = totalCount - machineBusBlockEntity.getStack(0).getMaxCount();
                                    anvilItemStacks[ovenIndex].count = machineBusBlockEntity.getStack(0).getMaxCount();
                                    ItemStack changedItem = machineBusBlockEntity.getStack(0);
                                    changedItem.count = leftovers;
                                    machineBusBlockEntity.setStack(0, changedItem);
                                }
                            }
                        }
                    }
                }
                break;
            case 5:
                for (int xOffset = -2; xOffset <= 2; xOffset++) {
                    for (int zOffset = 2; zOffset >= -2; zOffset--) {
                        if (world.getBlockEntity(xCentered + xOffset, y + 1, zCentered + zOffset) instanceof MachineBusBlockEntity machineBusBlockEntity) {
                            if (machineBusBlockEntity.getStack(0) == null) {
                                continue;
                            }
                            int ovenIndex = (-zOffset + 2) + (xOffset + 2) * 5;
                            if (anvilItemStacks[ovenIndex] == null) {
                                anvilItemStacks[ovenIndex] = machineBusBlockEntity.getStack(0);
                                machineBusBlockEntity.setStack(0, null);
                            } else if (anvilItemStacks[ovenIndex].isItemEqual(machineBusBlockEntity.getStack(0))) {
                                int totalCount = anvilItemStacks[ovenIndex].count + machineBusBlockEntity.getStack(0).count;
                                if (totalCount <= machineBusBlockEntity.getStack(0).getMaxCount()) {
                                    anvilItemStacks[ovenIndex].count = totalCount;
                                    machineBusBlockEntity.setStack(0, null);
                                } else {
                                    int leftovers = totalCount - machineBusBlockEntity.getStack(0).getMaxCount();
                                    anvilItemStacks[ovenIndex].count = machineBusBlockEntity.getStack(0).getMaxCount();
                                    ItemStack changedItem = machineBusBlockEntity.getStack(0);
                                    changedItem.count = leftovers;
                                    machineBusBlockEntity.setStack(0, changedItem);
                                }
                            }
                        }
                    }
                }
                break;
        }
        // Automatic output
        for (int xOffset = -2; xOffset <= 2; xOffset++) {
            for (int zOffset = -2; zOffset <= 2; zOffset++) {
                // Standard output from output slot
                BlockEntity target = world.getBlockEntity(xCentered + xOffset, y, zCentered + zOffset);
                if (anvilItemStacks[OUTPUT] != null && target instanceof MachineBusBlockEntity machineBusBlockEntity
                        && world.getBlockId(xCentered + xOffset, y, zCentered + zOffset) == BlockListener.outputBus.id) {
                    if (machineBusBlockEntity.getStack(0) == null) {
                        machineBusBlockEntity.setStack(0, anvilItemStacks[OUTPUT]);
                        anvilItemStacks[OUTPUT] = null;
                        return true;
                    } else if (machineBusBlockEntity.getStack(0).isItemEqual(anvilItemStacks[OUTPUT])) {
                        int totalCount = anvilItemStacks[OUTPUT].count + machineBusBlockEntity.getStack(0).count;
                        if (totalCount <= machineBusBlockEntity.getStack(0).getMaxCount()) {
                            ItemStack changedStack = machineBusBlockEntity.getStack(0);
                            changedStack.count = totalCount;
                            machineBusBlockEntity.setStack(0, changedStack);
                            anvilItemStacks[OUTPUT] = null;
                        } else {
                            int leftovers = totalCount - machineBusBlockEntity.getStack(0).getMaxCount();
                            ItemStack changedStack = machineBusBlockEntity.getStack(0);
                            changedStack.count = changedStack.getMaxCount();
                            machineBusBlockEntity.setStack(0, changedStack);
                            anvilItemStacks[OUTPUT].count = leftovers;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean canCraft() {
        AutoAnvilMatrix autoAnvilMatrix = new AutoAnvilMatrix();
        InternalAutoAnvilScreenHandler autoAnvilScreenHandler = new InternalAutoAnvilScreenHandler(autoAnvilMatrix);
        CraftingInventory craftingInventory = new CraftingInventory(autoAnvilScreenHandler, 5, 5);
        for (int slotIndex = INPUT_START; slotIndex < INPUT_END; slotIndex++) {
            if (anvilItemStacks[slotIndex] == null) {
                continue;
            }
            craftingInventory.setStack(slotIndex, anvilItemStacks[slotIndex]);
        }
        ItemStack itemstack = AnvilCraftingManager.getInstance().findMatchingRecipe(craftingInventory);
        if (itemstack == null) {
            return false;
        }
        if (anvilItemStacks[OUTPUT] == null) {
            return true;
        }
        if (!anvilItemStacks[OUTPUT].copy().isItemEqual(itemstack)) {
            return false;
        }
        if (anvilItemStacks[OUTPUT].copy().count < getMaxCountPerStack()
                && anvilItemStacks[OUTPUT].copy().count + itemstack.copy().count < anvilItemStacks[OUTPUT].copy().getMaxCount()) {
            return true;
        }
        return anvilItemStacks[OUTPUT].copy().count + itemstack.copy().count <= itemstack.copy().getMaxCount();
    }

    public void craftItem() {
        if (!canCraft()) {
            return;
        }
        AutoAnvilMatrix autoAnvilMatrix = new AutoAnvilMatrix();
        InternalAutoAnvilScreenHandler autoAnvilScreenHandler = new InternalAutoAnvilScreenHandler(autoAnvilMatrix);
        CraftingInventory craftingInventory = new CraftingInventory(autoAnvilScreenHandler, 5, 5);
        for (int slotIndex = INPUT_START; slotIndex < INPUT_END; slotIndex++) {
            if (anvilItemStacks[slotIndex] == null) {
                continue;
            }
            craftingInventory.setStack(slotIndex, anvilItemStacks[slotIndex]);
        }
        ItemStack itemstack = AnvilCraftingManager.getInstance().findMatchingRecipe(craftingInventory);
        if (anvilItemStacks[OUTPUT] == null) {
            anvilItemStacks[OUTPUT] = itemstack.copy();
        } else if (anvilItemStacks[OUTPUT].itemId == itemstack.copy().itemId) {
            anvilItemStacks[OUTPUT].count += itemstack.copy().count;
        }

        // Clear input slots
        for(int i = INPUT_START; i < INPUT_END; i++) {
            if(anvilItemStacks[i] != null){
                if (anvilItemStacks[i].getItem() instanceof BucketItem
                        && anvilItemStacks[i].getItem().id != Item.BUCKET.id) {
                    anvilItemStacks[i] = new ItemStack(Item.BUCKET, 1);
                } else {
                    anvilItemStacks[i].count--;
                    if (anvilItemStacks[i].copy().count <= 0) {
                        anvilItemStacks[i] = null;
                    }
                }
            }
        }
    }

    @Override
    public int size() {
        return INVENTORY_SIZE;
    }

    @Override
    public ItemStack getStack(int slot) {
        return anvilItemStacks[slot];
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return InventoryHandler.decreaseStackSize(this, slot, amount);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        anvilItemStacks[slot] = stack;
        if (stack != null && stack.count > getMaxCountPerStack()) {
            stack.count = getMaxCountPerStack();
        }
        markDirty();
    }

    @Override
    public String getName() {
        return "AutomaticAnvil";
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (world.getBlockEntity(x, y, z) != this) {
            return false;
        } else {
            return player.getSquaredDistance((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D) <= 64D;
        }
    }

    public void readNbt(NbtCompound nbtCompound) {
        super.readNbt(nbtCompound);
        NbtList nbtList = nbtCompound.getList("Items");
        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound itemCompound = (NbtCompound) nbtList.get(i);
            int j = itemCompound.getByte("Slot") & 0xff;
            if (j < anvilItemStacks.length) {
                anvilItemStacks[j] = new ItemStack(itemCompound);
            }
        }
        craftingProgress = nbtCompound.getInt("CraftingProgress");
    }

    public void writeNbt(NbtCompound nbtCompound) {
        super.writeNbt(nbtCompound);
        NbtList nbtList = new NbtList();
        for (int i = 0; i < anvilItemStacks.length; i++) {
            if (anvilItemStacks[i] != null) {
                NbtCompound itemCompound = new NbtCompound();
                itemCompound.putByte("Slot", (byte) i);
                anvilItemStacks[i].writeNbt(itemCompound);
                nbtList.add(itemCompound);
            }
        }
        nbtCompound.put("Items", nbtList);
        nbtCompound.putInt("CraftingProgress", craftingProgress);
    }

    public int getScaledCraftingProgress(int scale) {
        return (craftingProgress * scale) / RECIPE_TIME;
    }
}
