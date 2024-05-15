// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCTileEntityVase.java

package net.kozibrodka.wolves.tileentity;

import net.kozibrodka.wolves.utils.InventoryHandler;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;

public class VaseTileEntity extends TileEntityBase
    implements InventoryBase
{

    public VaseTileEntity()
    {
        m_VaseContents = new ItemInstance[1];
    }

    public int getInventorySize()
    {
        return 1;
    }

    public int getMaxItemCount()
    {
        return 1;
    }
    public ItemInstance getInventoryItem(int iSlot)
    {
        return m_VaseContents[iSlot];
    }

    public ItemInstance takeInventoryItem(int iSlot, int iAmount)
    {
        return InventoryHandler.decreaseStackSize(this, iSlot, iAmount);
    }

    public void setInventoryItem(int iSlot, ItemInstance ItemInstance)
    {
        m_VaseContents[iSlot] = ItemInstance;
        if(ItemInstance != null && ItemInstance.count > getMaxItemCount())
        {
            ItemInstance.count = getMaxItemCount();
        }
        markDirty();
    }

    public String getContainerName()
    {
        return "Vase";
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

    public void readIdentifyingData(CompoundTag nbttagcompound)
    {
        super.readIdentifyingData(nbttagcompound);
        ListTag nbttaglist = nbttagcompound.getListTag("Items");
        m_VaseContents = new ItemInstance[getInventorySize()];
        for(int i = 0; i < nbttaglist.size(); i++)
        {
            CompoundTag nbttagcompound1 = (CompoundTag)nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < m_VaseContents.length)
            {
                m_VaseContents[j] = new ItemInstance(nbttagcompound1);
            }
        }

    }

    public void writeIdentifyingData(CompoundTag nbttagcompound)
    {
        super.writeIdentifyingData(nbttagcompound);
        ListTag nbttaglist = new ListTag();
        for(int i = 0; i < m_VaseContents.length; i++)
        {
            if(m_VaseContents[i] != null)
            {
                CompoundTag nbttagcompound1 = new CompoundTag();
                nbttagcompound1.put("Slot", (byte)i);
                m_VaseContents[i].toTag(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
    }

    private final int m_iVaseInventorySize = 1;
    private final int m_iVaseStackSizeLimit = 1;
    private final double m_dVaseMaxPlayerInteractionDist = 64D;
    private ItemInstance m_VaseContents[];
}
