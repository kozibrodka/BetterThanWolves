// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCTileEntityBlockDispenser.java

package net.kozibrodka.wolves.tileentity;

import net.kozibrodka.wolves.utils.FCUtilsInventory;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            TileEntity, InventoryBase, ItemInstance, FCUtilsInventory, 
//            NBTTagCompound, ListTag, World, EntityPlayer

public class FCTileEntityBlockDispenser extends TileEntityBase
    implements InventoryBase
{

    public FCTileEntityBlockDispenser()
    {
        dispenserContents = new ItemInstance[9];
        dispenserRandom = new Random();
        iNextSlotIndexToDispense = 0;
    }

    public int getInventorySize()
    {
        return 9;
    }

    public ItemInstance getInventoryItem(int i)
    {
        return dispenserContents[i];
    }

    public ItemInstance takeInventoryItem(int iSlot, int iAmount)
    {
        return FCUtilsInventory.DecrStackSize(this, iSlot, iAmount);
    }

    public ItemInstance GetNextStackFromInventory()
    {
        ItemInstance nextStack = null;
        int iTempSlot;
        if(iNextSlotIndexToDispense >= dispenserContents.length || dispenserContents[iNextSlotIndexToDispense] == null)
        {
            iTempSlot = FindNextValidSlotIndex(iNextSlotIndexToDispense);
            if(iTempSlot < 0)
            {
                return null;
            }
            iNextSlotIndexToDispense = iTempSlot;
        }
        nextStack = takeInventoryItem(iNextSlotIndexToDispense, 1);
        iTempSlot = FindNextValidSlotIndex(iNextSlotIndexToDispense);
        if(iTempSlot < 0)
        {
            iNextSlotIndexToDispense = 0;
        } else
        {
            iNextSlotIndexToDispense = iTempSlot;
        }
        return nextStack;
    }

    private int FindNextValidSlotIndex(int iCurrentSlot)
    {
        for(int iTempSlot = iCurrentSlot + 1; iTempSlot < dispenserContents.length; iTempSlot++)
        {
            if(dispenserContents[iTempSlot] != null)
            {
                return iTempSlot;
            }
        }

        for(int iTempSlot = 0; iTempSlot < iCurrentSlot; iTempSlot++)
        {
            if(dispenserContents[iTempSlot] != null)
            {
                return iTempSlot;
            }
        }

        if(dispenserContents[iCurrentSlot] != null)
        {
            return iCurrentSlot;
        } else
        {
            return -1;
        }
    }

    public void setInventoryItem(int iSlot, ItemInstance ItemInstance)
    {
        dispenserContents[iSlot] = ItemInstance;
        if(ItemInstance != null && ItemInstance.count > getMaxItemCount())
        {
            ItemInstance.count = getMaxItemCount();
        }
        markDirty();
    }

    public String getContainerName()
    {
        return "BlockDispenser";
    }

    public void readIdentifyingData(CompoundTag nbttagcompound)
    {
        super.readIdentifyingData(nbttagcompound);
        ListTag nbttaglist = nbttagcompound.getListTag("Items");
        dispenserContents = new ItemInstance[getInventorySize()];
        for(int i = 0; i < nbttaglist.size(); i++)
        {
            CompoundTag nbttagcompound1 = (CompoundTag)nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < dispenserContents.length)
            {
                dispenserContents[j] = new ItemInstance(nbttagcompound1);
            }
        }

        if(nbttagcompound.containsKey("iNextSlotIndexToDispense"))
        {
            iNextSlotIndexToDispense = nbttagcompound.getInt("iNextSlotIndexToDispense");
        }
    }

    public void writeIdentifyingData(CompoundTag nbttagcompound)
    {
        super.writeIdentifyingData(nbttagcompound);
        ListTag nbttaglist = new ListTag();
        for(int i = 0; i < dispenserContents.length; i++)
        {
            if(dispenserContents[i] != null)
            {
                CompoundTag nbttagcompound1 = new CompoundTag();
                nbttagcompound1.put("Slot", (byte)i);
                dispenserContents[i].toTag(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
        nbttagcompound.put("iNextSlotIndexToDispense", iNextSlotIndexToDispense);
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

    private ItemInstance dispenserContents[];
    private Random dispenserRandom;
    public int iNextSlotIndexToDispense;
}
