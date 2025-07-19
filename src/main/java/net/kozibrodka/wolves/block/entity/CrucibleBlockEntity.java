// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCTileEntityCrucible.java

package net.kozibrodka.wolves.block.entity;


import net.kozibrodka.wolves.block.CrucibleBlock;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.recipe.CrucibleCraftingManager;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class CrucibleBlockEntity extends BlockEntity implements Inventory {

    public CrucibleBlockEntity() {
        crucibleContents = new ItemStack[27];
        crucibleCookCounter = 0;
        overStokedFire = false;
    }

    public int size() {
        return 27;
    }

    public int getMaxCountPerStack() {
        return 64;
    }

    public ItemStack getStack(int slot) {
        return crucibleContents[slot];
    }

    public ItemStack removeStack(int slot, int amount) {
        return InventoryHandler.decreaseStackSize(this, slot, amount);
    }

    public void setStack(int slot, ItemStack itemInstance) {
        crucibleContents[slot] = itemInstance;
        if (itemInstance != null && itemInstance.count > getMaxCountPerStack()) {
            itemInstance.count = getMaxCountPerStack();
        }
        updateRenderFullness();
    }

    public void updateRenderFullness() {
        if (world == null) {
            return;
        }
        if (world.isRemote) {
            return;
        }
//        level.method_202(x, y, z, x, y, z);
        int iOccupiedStacks = InventoryHandler.getOccupiedSlotCountWithinBounds(this, 0, 26);
        ((CrucibleBlock) BlockListener.crucible).SetHasFull(world, x, y, z, iOccupiedStacks);
    }

    public String getName() {
        return "Crucible";
    }

    public boolean canPlayerUse(PlayerEntity player) {
        if (world.getBlockEntity(x, y, z) != this) {
            return false;
        } else {
            return player.getSquaredDistance((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D) <= 64D;
        }
    }

    public void readNbt(NbtCompound compoundTag) {
        super.readNbt(compoundTag);
        NbtList nbttaglist = compoundTag.getList("Items");
        crucibleContents = new ItemStack[size()];
        for (int i = 0; i < nbttaglist.size(); i++) {
            NbtCompound slotCompoundTag = (NbtCompound) nbttaglist.get(i);
            int j = slotCompoundTag.getByte("Slot") & 0xff;
            if (j < crucibleContents.length) {
                crucibleContents[j] = new ItemStack(slotCompoundTag);
            }
        }

        crucibleCookCounter = compoundTag.getInt("CrucibleCookCounter");
    }

    public void writeNbt(NbtCompound nbttagcompound) {
        super.writeNbt(nbttagcompound);
        NbtList listTag = new NbtList();
        for (int i = 0; i < crucibleContents.length; i++) {
            if (crucibleContents[i] != null) {
                NbtCompound slotCompoundTag = new NbtCompound();
                slotCompoundTag.putByte("Slot", (byte) i);
                crucibleContents[i].writeNbt(slotCompoundTag);
                listTag.add(slotCompoundTag);
            }
        }

        nbttagcompound.put("Items", listTag);
        nbttagcompound.putInt("CrucibleCookCounter", crucibleCookCounter);
    }

    public void tick() {
        if (world.isRemote) {
            return;
        }
        int stokedFireFactor = getStokedFireFactor();
        if (stokedFireFactor > 0) {
            if (!overStokedFire) {
                overStokedFire = true;
                ((CrucibleBlock) BlockListener.crucible).SetHasLava(world, x, y, z, true);
                world.setBlocksDirty(x, y, z, x, y, z);
            }
            if (areItemsInRegistry()) {
                crucibleCookCounter += stokedFireFactor;
                if (crucibleCookCounter >= 1950) {
                    craftFromRegistry();
                    crucibleCookCounter = 0;
                }
            } else {
                crucibleCookCounter = 0;
            }
        } else {
            if (overStokedFire) {
                overStokedFire = false;
                ((CrucibleBlock) BlockListener.crucible).SetHasLava(world, x, y, z, false);
                world.setBlocksDirty(x, y, z, x, y, z);
            }
            crucibleCookCounter = 0;
        }
    }

    public int getStokedFireFactor() {
        int fireFactor = 0;
        if (world.getBlockId(x, y - 1, z) == BlockListener.stokedFire.id) {
            fireFactor += 5;
            int tempY = y - 1;
            for (int tempX = x - 1; tempX <= x + 1; tempX++) {
                for (int tempZ = z - 1; tempZ <= z + 1; tempZ++) {
                    if ((tempX != x || tempZ != z) && world.getBlockId(tempX, tempY, tempZ) == BlockListener.stokedFire.id && world.getBlockId(tempX, tempY - 1, tempZ) == BlockListener.stokedFire.id) {
                        fireFactor++;
                    }
                }
            }
        }
        return fireFactor;
    }

    public int getCookProgressScaled(int scale) {
        return (crucibleCookCounter * scale) / 1950;
    }

    public boolean isCooking() {
        return crucibleCookCounter > 0;
    }

    public boolean areItemsInRegistry() {
        return CrucibleCraftingManager.getInstance().getCraftingResult(this) != null;
    }

    public void craftFromRegistry() {
        if (CrucibleCraftingManager.getInstance().getCraftingResult(this) == null) return;
        ItemStack outputStack = CrucibleCraftingManager.getInstance().consumeIngredientsAndReturnResult(this);
        if (!InventoryHandler.addItemInstanceToInventory(this, outputStack))
            UnsortedUtils.ejectStackWithRandomOffset(world, x, y + 1, z, outputStack);
    }

    private ItemStack[] crucibleContents;
    public int crucibleCookCounter;
    public boolean overStokedFire;

    /*
        ---- Crucible Stats ----
        Inventory Size = 27;
        Stack Size Limit = 64;
        Player Interaction Distance = 64D;
        Primary Fire Factor = 5;
        Secondary Fire Factor = 1;
        Cook Time = 1950;
     */
}
