// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode
// Source File Name:   FCTileEntityMillStone.java

package net.kozibrodka.wolves.tileentity;

import net.kozibrodka.wolves.blocks.CompanionCube;
import net.kozibrodka.wolves.blocks.MillStone;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.recipe.MillingRecipeRegistry;
import net.kozibrodka.wolves.utils.FCBlockPos;
import net.kozibrodka.wolves.utils.FCUtilsInventory;
import net.kozibrodka.wolves.utils.FCUtilsMisc;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;


public class FCTileEntityMillStone extends TileEntityBase
    implements InventoryBase
{

    public FCTileEntityMillStone()
    {
        millStoneContents = new ItemInstance[3];
        iMillStoneGrindCounter = 0;
    }

    @Override
    public void validate() {
        super.validate();
    }


    public int getInventorySize()
    {
        return 3;
    }

    public ItemInstance getInventoryItem(int iSlot)
    {
        return millStoneContents[iSlot];
    }

    public ItemInstance takeInventoryItem(int iSlot, int iAmount)
    {
        return FCUtilsInventory.DecrStackSize(this, iSlot, iAmount);
    }

    public void setInventoryItem(int iSlot, ItemInstance ItemInstance)
    {
        millStoneContents[iSlot] = ItemInstance;
        if(ItemInstance != null && ItemInstance.count > getMaxItemCount())
        {
            ItemInstance.count = getMaxItemCount();
        }
        markDirty();
    }

    public String getContainerName()
    {
        return "MillStone";
    }

    public void readIdentifyingData(CompoundTag nbttagcompound)
    {
        super.readIdentifyingData(nbttagcompound);
        ListTag nbttaglist = nbttagcompound.getListTag("Items");
        millStoneContents = new ItemInstance[getInventorySize()];
        for(int i = 0; i < nbttaglist.size(); i++)
        {
            CompoundTag nbttagcompound1 = (CompoundTag)nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < millStoneContents.length)
            {
                millStoneContents[j] = new ItemInstance(nbttagcompound1);
            }
        }

        if(nbttagcompound.containsKey("grindCounter"))
        {
            iMillStoneGrindCounter = nbttagcompound.getInt("grindCounter");
        }
    }

    public void writeIdentifyingData(CompoundTag nbttagcompound)
    {
        super.writeIdentifyingData(nbttagcompound);
        ListTag nbttaglist = new ListTag();
        for(int i = 0; i < millStoneContents.length; i++)
        {
            if(millStoneContents[i] != null)
            {
                CompoundTag nbttagcompound1 = new CompoundTag();
                nbttagcompound1.put("Slot", (byte)i);
                millStoneContents[i].toTag(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
        nbttagcompound.put("grindCounter", iMillStoneGrindCounter);
    }

    public int getMaxItemCount()
    {
        return 64;
    }

    public boolean canPlayerUse(PlayerBase entityplayer)
    {
        if(level.getTileEntity(x, y, z) != this)
        {
            return false;
        } else
        {
            return entityplayer.squaredDistanceTo((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
        }
    }

    public int GetUnmilledItemInventoryIndex()
    {
        for(int tempIndex = 0; tempIndex < 3; tempIndex++)
        {
            if(millStoneContents[tempIndex] == null)
            {
                continue;
            }
            ItemBase tempItem = millStoneContents[tempIndex].getType();
            if(tempItem != null)
            {
                return tempIndex;
            }
        }

        return -1;
    }

    public int getGrindProgressScaled(int iScale)
    {
        return (iMillStoneGrindCounter * iScale) / 200;
    }

    public boolean IsGrinding()
    {
        return iMillStoneGrindCounter > 0;
    }

    public void tick() // updateEntity
    {
        int iUnmilledItemIndex = GetUnmilledItemInventoryIndex();

        if(iUnmilledItemIndex < 0)
        {
            iMillStoneGrindCounter = 0;
            return;
        }

        if(!((MillStone)mod_FCBetterThanWolves.fcMillStone).IsBlockOn(level, x, y, z)) return;

        iMillStoneGrindCounter++;

        int iUnmilledItemID = millStoneContents[iUnmilledItemIndex].getType().id;

        if(iUnmilledItemID == mod_FCBetterThanWolves.fcCompanionCube.id) // Companion cube torture during milling
        {
            if(millStoneContents[iUnmilledItemIndex].getDamage() == 0 && level.rand.nextInt(10) == 0)
            {
                level.playSound((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, "mob.wolf.hurt", 2.0F, (level.rand.nextFloat() - level.rand.nextFloat()) * 0.2F + 1.0F);
            }
            if(level.rand.nextInt(20) == 0)
            {
                ItemInstance stringStack = new ItemInstance(ItemBase.string);
                EjectStack(stringStack);
            }
            if(level.rand.nextInt(60) == 0)
            {
                ItemInstance woolStack = new ItemInstance(ItemBase.dyePowder.id, 1, 1);
                EjectStack(woolStack);
            }
        }
        else if(iUnmilledItemID == BlockBase.NETHERRACK.id && level.rand.nextInt(10) == 0) // Random scream when there is netherrack
        {
            level.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "mob.ghast.scream", 0.25F, level.rand.nextFloat() * 0.4F + 0.8F);
        }

        if(iMillStoneGrindCounter < 200) return;

        iMillStoneGrindCounter = 0;

        ItemInstance milledStack = MillingRecipeRegistry.getInstance().getResult(iUnmilledItemID);
        if(milledStack != null)
        {
            milledStack = new ItemInstance(milledStack.itemId, milledStack.count, milledStack.getDamage()); // This looks very redundant but cures the millstone from the curse of losing the item count value from the registry for no apparent reason besides metaphysical powers beyond human comprehension or something, I do not know how to explain this anymore.
            if (milledStack.count == 0) milledStack.count = 1;
            takeInventoryItem(iUnmilledItemIndex, 1);
            EjectStack(milledStack);
        }
        else if(iUnmilledItemID == mod_FCBetterThanWolves.fcCompanionCube.id) // Special recipe (companion cube)
        {
            EjectStack(new ItemInstance(mod_FCBetterThanWolves.fcWolfRaw.id, 1, 0));
            CompanionCube.SpawnHearts(level, x, y, z);
            if(millStoneContents[iUnmilledItemIndex].getDamage() == 0)
            {
                level.playSound((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, "mob.wolf.whine", 0.5F, 2.6F + (level.rand.nextFloat() - level.rand.nextFloat()) * 0.8F);
            }
            takeInventoryItem(iUnmilledItemIndex, 1);
        }

    }

    public void EjectStack(ItemInstance stack)
    {
        FCBlockPos targetPos = new FCBlockPos(x, y, z);
        int iDirection = 2 + level.rand.nextInt(4);
        targetPos.AddFacingAsOffset(iDirection);
        FCUtilsMisc.EjectStackWithRandomOffset(level, targetPos.i, targetPos.j, targetPos.k, stack);
    }

    public boolean AddSingleItemToInventory(int iItemShiftedIndex)
    {
        ItemInstance ItemInstance = new ItemInstance(iItemShiftedIndex, 1, 0);
        return addItemInstanceToInventory(ItemInstance);
    }

    public boolean addItemInstanceToInventory(ItemInstance ItemInstance)
    {
        if(!ItemInstance.isDamaged())
        {
            ItemInstance.count = storePartialItemInstance(ItemInstance);
            if(ItemInstance.count == 0)
            {
                return true;
            }
        }
        int i = getFirstEmptyStack();
        if(i >= 0)
        {
            setInventoryItem(i, ItemInstance);
            return true;
        } else
        {
            return false;
        }
    }

    private int getFirstEmptyStack()
    {
        for(int i = 0; i < getInventorySize(); i++)
        {
            if(getInventoryItem(i) == null)
            {
                return i;
            }
        }

        return -1;
    }

    private int storePartialItemInstance(ItemInstance ItemInstance)
    {
        int i = ItemInstance.itemId;
        int j = ItemInstance.count;
        int k = storeItemInstance(ItemInstance);
        if(k < 0)
        {
            k = getFirstEmptyStack();
        }
        if(k < 0)
        {
            return j;
        }
        if(getInventoryItem(k) == null)
        {
            setInventoryItem(k, new ItemInstance(i, 0, ItemInstance.getDamage()));
        }
        int l = j;
        ItemInstance tempStack = getInventoryItem(k);
        if(l > tempStack.getMaxStackSize() - tempStack.count)
        {
            l = tempStack.getMaxStackSize() - tempStack.count;
        }
        if(l > getMaxItemCount() - tempStack.count)
        {
            l = getMaxItemCount() - tempStack.count;
        }
        if(l == 0)
        {
            return j;
        } else
        {
            j -= l;
            tempStack.count += l;
            setInventoryItem(k, tempStack);
            return j;
        }
    }

    private int storeItemInstance(ItemInstance ItemInstance)
    {
        for(int i = 0; i < getInventorySize(); i++)
        {
            ItemInstance tempStack = getInventoryItem(i);
            if(tempStack != null && tempStack.itemId == ItemInstance.itemId && tempStack.isStackable() && tempStack.count < tempStack.getMaxStackSize() && tempStack.count < getMaxItemCount() && (!tempStack.usesMeta() || tempStack.getDamage() == ItemInstance.getDamage()))
            {
                return i;
            }
        }

        return -1;
    }

    public boolean IsWholeCompanionCubeInInventory()
    {
        for(int tempIndex = 0; tempIndex < 3; tempIndex++)
        {
            if(millStoneContents[tempIndex] == null)
            {
                continue;
            }
            ItemBase tempItem = millStoneContents[tempIndex].getType();
            if(tempItem != null && tempItem.id == mod_FCBetterThanWolves.fcCompanionCube.id && millStoneContents[tempIndex].getDamage() == 0)
            {
                return true;
            }
        }

        return false;
    }

    public boolean IsWholeCompanionCubeNextToBeProcessed()
    {
        int iUnmilledItemIndex = GetUnmilledItemInventoryIndex();
        if(iUnmilledItemIndex >= 0)
        {
            int iUnmilledItemID = millStoneContents[iUnmilledItemIndex].getType().id;
            if(iUnmilledItemID == mod_FCBetterThanWolves.fcCompanionCube.id && millStoneContents[iUnmilledItemIndex].getDamage() == 0)
            {
                return true;
            }
        }
        return false;
    }

    public void markDirty()
    {
        if(IsWholeCompanionCubeInInventory())
        {
            level.playSound((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, "mob.wolf.whine", 0.5F, 2.6F + (level.rand.nextFloat() - level.rand.nextFloat()) * 0.8F);
        }
    }

    private final int iMillStoneInventorySize = 3;
    private final int iMillStoneStackSizeLimit = 64;
    private final double dMillStoneMaxPlayerInteractionDist = 64D;
    private final int iMillStoneTimeToGrind = 200;
    private ItemInstance millStoneContents[];
    public int iMillStoneGrindCounter;
}
