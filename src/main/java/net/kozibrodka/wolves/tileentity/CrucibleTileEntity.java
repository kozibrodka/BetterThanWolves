// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCTileEntityCrucible.java

package net.kozibrodka.wolves.tileentity;


import net.kozibrodka.wolves.blocks.Crucible;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.recipe.CrucibleCraftingManager;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;

public class CrucibleTileEntity extends TileEntityBase implements InventoryBase {

    public CrucibleTileEntity() {
        crucibleContents = new ItemInstance[27];
        crucibleCookCounter = 0;
        overStokedFire = false;
    }

    public int getInventorySize() {
        return 27;
    }

    public int getMaxItemCount() {
        return 64;
    }

    public ItemInstance getInventoryItem(int slot) {
        return crucibleContents[slot];
    }

    public ItemInstance takeInventoryItem(int slot, int amount) {
        return InventoryHandler.decreaseStackSize(this, slot, amount);
    }

    public void setInventoryItem(int slot, ItemInstance itemInstance) {
        crucibleContents[slot] = itemInstance;
        if(itemInstance != null && itemInstance.count > getMaxItemCount()) {
            itemInstance.count = getMaxItemCount();
        }
        updateRenderFullness();
    }

    public void updateRenderFullness(){
        if (level == null) {
            return;
        }
        if(level.isServerSide){
            return;
        }
//        level.method_202(x, y, z, x, y, z);
        int iOccupiedStacks = InventoryHandler.getOccupiedSlotCountWithinBounds(this, 0, 26);
        ((Crucible)BlockListener.crucible).SetHasFull(level, x, y, z, iOccupiedStacks);
    }

    public String getContainerName() {
        return "Crucible";
    }

    public boolean canPlayerUse(PlayerBase player) {
        if(level.getTileEntity(x, y, z) != this) {
            return false;
        } else {
            return player.squaredDistanceTo((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
        }
    }

    public void readIdentifyingData(CompoundTag compoundTag) {
        super.readIdentifyingData(compoundTag);
        ListTag nbttaglist = compoundTag.getListTag("Items");
        crucibleContents = new ItemInstance[getInventorySize()];
        for(int i = 0; i < nbttaglist.size(); i++) {
            CompoundTag slotCompoundTag = (CompoundTag)nbttaglist.get(i);
            int j = slotCompoundTag.getByte("Slot") & 0xff;
            if(j < crucibleContents.length) {
                crucibleContents[j] = new ItemInstance(slotCompoundTag);
            }
        }

        crucibleCookCounter = compoundTag.getInt("CrucibleCookCounter");
    }

    public void writeIdentifyingData(CompoundTag nbttagcompound) {
        super.writeIdentifyingData(nbttagcompound);
        ListTag listTag = new ListTag();
        for(int i = 0; i < crucibleContents.length; i++) {
            if(crucibleContents[i] != null) {
                CompoundTag slotCompoundTag = new CompoundTag();
                slotCompoundTag.put("Slot", (byte)i);
                crucibleContents[i].toTag(slotCompoundTag);
                listTag.add(slotCompoundTag);
            }
        }

        nbttagcompound.put("Items", listTag);
        nbttagcompound.put("CrucibleCookCounter", crucibleCookCounter);
    }

    public void tick() {
        if(level.isServerSide) {
            return;
        }
        int stokedFireFactor = getStokedFireFactor();
        if(stokedFireFactor > 0) {
            if(!overStokedFire) {
                overStokedFire = true;
                ((Crucible)BlockListener.crucible).SetHasLava(level, x, y, z, true);
                level.method_202(x, y, z, x, y, z);
            }
            if(areItemsInRegistry()) {
                crucibleCookCounter += stokedFireFactor;
                if(crucibleCookCounter >= 1950) {
                    craftFromRegistry();
                    crucibleCookCounter = 0;
                }
            } else {
                crucibleCookCounter = 0;
            }
        } else {
            if(overStokedFire) {
                overStokedFire = false;
                ((Crucible)BlockListener.crucible).SetHasLava(level, x, y, z, false);
                level.method_202(x, y, z, x, y, z);
            }
            crucibleCookCounter = 0;
        }
    }

    public int getStokedFireFactor() {
        int fireFactor = 0;
        if(level.getTileId(x, y - 1, z) == BlockListener.stokedFire.id && level.getTileId(x, y - 2, z) == BlockListener.stokedFire.id) {
            fireFactor += 5;
            int tempY = y - 1;
            for(int tempX = x - 1; tempX <= x + 1; tempX++) {
                for(int tempZ = z - 1; tempZ <= z + 1; tempZ++) {
                    if((tempX != x || tempZ != z) && level.getTileId(tempX, tempY, tempZ) == BlockListener.stokedFire.id && level.getTileId(tempX, tempY - 1, tempZ) == BlockListener.stokedFire.id) {
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
        ItemInstance outputStack = CrucibleCraftingManager.getInstance().consumeIngredientsAndReturnResult(this);
        if(!InventoryHandler.addItemInstanceToInventory(this, outputStack)) UnsortedUtils.ejectStackWithRandomOffset(level, x, y + 1, z, outputStack);
    }

    private ItemInstance[] crucibleContents;
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
