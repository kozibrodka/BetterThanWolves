// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCTileEntityPulley.java

package net.kozibrodka.wolves.tileentity;
import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.blocks.Anchor;
import net.kozibrodka.wolves.blocks.Pulley;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.utils.FCUtilsInventory;
import net.kozibrodka.wolves.utils.FCUtilsMisc;
import net.kozibrodka.wolves.utils.FCUtilsWorld;
import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;


public class FCTileEntityPulley extends TileEntityBase
    implements InventoryBase
{

    public FCTileEntityPulley()
    {
        pulleyContents = new ItemInstance[2];
        iUpdateRopeStateCounter = 20;
    }

    public String getContainerName()
    {
        return "Pulley";
    }

    public int getInventorySize()
    {
        return 2;
    }

    public int getMaxItemCount()
    {
        return 64;
    }

    public ItemInstance getInventoryItem(int iSlot)
    {
        return pulleyContents[iSlot];
    }

    public void setInventoryItem(int iSlot, ItemInstance ItemInstance)
    {
        pulleyContents[iSlot] = ItemInstance;
        if(ItemInstance != null && ItemInstance.count > getMaxItemCount())
        {
            ItemInstance.count = getMaxItemCount();
        }
        markDirty();
    }

    public ItemInstance takeInventoryItem(int iSlot, int iAmount)
    {
        return FCUtilsInventory.DecrStackSize(this, iSlot, iAmount);
    }

    public void readIdentifyingData(CompoundTag nbttagcompound)
    {
        super.readIdentifyingData(nbttagcompound);
        ListTag nbttaglist = nbttagcompound.getListTag("Items");
        pulleyContents = new ItemInstance[getInventorySize()];
        for(int i = 0; i < nbttaglist.size(); i++)
        {
            CompoundTag nbttagcompound1 = (CompoundTag)nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < pulleyContents.length)
            {
                pulleyContents[j] = new ItemInstance(nbttagcompound1);
            }
        }

    }

    public void writeIdentifyingData(CompoundTag nbttagcompound)
    {
        super.writeIdentifyingData(nbttagcompound);
        ListTag nbttaglist = new ListTag();
        for(int i = 0; i < pulleyContents.length; i++)
        {
            if(pulleyContents[i] != null)
            {
                CompoundTag nbttagcompound1 = new CompoundTag();
                nbttagcompound1.put("Slot", (byte)i);
                pulleyContents[i].toTag(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
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

    public void tick()
    {
        iUpdateRopeStateCounter--;
        if(iUpdateRopeStateCounter <= 0)
        {
            boolean bIsRedstoneOn = ((Pulley) mod_FCBetterThanWolves.fcPulley).IsRedstoneOn(level, x, y, z);
            if(!bIsRedstoneOn)
            {
                boolean bIsOn = ((Pulley)mod_FCBetterThanWolves.fcPulley).IsBlockOn(level, x, y, z);
                if(bIsOn)
                {
                    AttemptToRetractRope();
                } else
                {
                    AttemptToDispenseRope();
                }
            }
            iUpdateRopeStateCounter = 20;
        }
    }

    public boolean IsMechanicallyPowered()
    {
        return ((Pulley)mod_FCBetterThanWolves.fcPulley).IsBlockOn(level, x, y, z);
    }

    public boolean IsRedstonePowered()
    {
        return ((Pulley)mod_FCBetterThanWolves.fcPulley).IsRedstoneOn(level, x, y, z);
    }

    public boolean IsRaising()
    {
        return !IsRedstonePowered() && IsMechanicallyPowered();
    }

    public boolean IsLowering()
    {
        return !IsRedstonePowered() && !IsMechanicallyPowered() && FCUtilsInventory.GetFirstOccupiedStackOfItem(this, mod_FCBetterThanWolves.fcRopeItem.id) >= 0;
    }

    public void NotifyPulleyEntityOfBlockStateChange()
    {
        iUpdateRopeStateCounter = 20;
        NotifyAttachedAnchorOfEntityStateChange();
    }

    private void NotifyAttachedAnchorOfEntityStateChange()
    {
        for(int tempj = y - 1; tempj >= 0; tempj--)
        {
            int iTempid = level.getTileId(x, tempj, z);
            if(iTempid == mod_FCBetterThanWolves.fcAnchor.id)
            {
                if(((Anchor)mod_FCBetterThanWolves.fcAnchor).GetAnchorFacing(level, x, tempj, z) != 1)
                {
                    break;
                }
                ((Anchor)mod_FCBetterThanWolves.fcAnchor).NotifyAnchorBlockOfAttachedPulleyStateChange(this, level, x, tempj, z);
                continue;
            }
            if(iTempid != mod_FCBetterThanWolves.fcRopeBlock.id)
            {
                break;
            }
        }

    }

    boolean AttemptToRetractRope()
    {
        for(int tempj = y - 1; tempj >= 0; tempj--)
        {
            int iTempid = level.getTileId(x, tempj, z);
            if(iTempid == mod_FCBetterThanWolves.fcRopeBlock.id)
            {
                if(level.getTileId(x, tempj - 1, z) != mod_FCBetterThanWolves.fcRopeBlock.id)
                {
                    AddRopeToInventory();
                    BlockBase targetBlock = mod_FCBetterThanWolves.fcRopeBlock;
                    Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).soundHelper.playSound(targetBlock.sounds.getWalkSound(), (float)x + 0.5F, (float)tempj + 0.5F, (float)z + 0.5F, targetBlock.sounds.getVolume() / 4F, targetBlock.sounds.getPitch() * 0.8F);
                    level.setTile(x, tempj, z, 0);
                    return true;
                }
            } else
            {
                return false;
            }
        }

        return false;
    }

    public boolean AttemptToDispenseRope()
    {
        int iRopeSlot = FCUtilsInventory.GetFirstOccupiedStackOfItem(this, mod_FCBetterThanWolves.fcRopeItem.id);
        iUpdateRopeStateCounter = 20;
        if(iRopeSlot >= 0)
        {
            for(int tempj = y - 1; tempj >= 0; tempj--)
            {
                int iTempid = level.getTileId(x, tempj, z);
                if(FCUtilsWorld.IsReplaceableBlock(level, x, tempj, z))
                {
                    if(level.setTile(x, tempj, z, mod_FCBetterThanWolves.fcRopeBlock.id))
                    {
                        mod_FCBetterThanWolves.fcRopeBlock.onBlockPlaced(level, x, tempj, z, 0);
                        BlockBase targetBlock = mod_FCBetterThanWolves.fcRopeBlock;
                        Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).soundHelper.playSound(targetBlock.sounds.getWalkSound(), (float)x + 0.5F, (float)tempj + 0.5F, (float)z + 0.5F, targetBlock.sounds.getVolume() / 4F, targetBlock.sounds.getPitch() * 0.8F);
                        RemoveRopeFromInventory();
                        int iBlockBelowTargetID = level.getTileId(x, tempj - 1, z);
                        if(iBlockBelowTargetID == mod_FCBetterThanWolves.fcAnchor.id && ((Anchor)mod_FCBetterThanWolves.fcAnchor).GetAnchorFacing(level, x, tempj - 1, z) == 1)
                        {
                            ((Anchor)mod_FCBetterThanWolves.fcAnchor).NotifyAnchorBlockOfAttachedPulleyStateChange(this, level, x, tempj - 1, z);
                        }
                        return true;
                    } else
                    {
                        return false;
                    }
                }
                if(iTempid != mod_FCBetterThanWolves.fcRopeBlock.id)
                {
                    return false;
                }
            }

        }
        return false;
    }

    public void AddRopeToInventory()
    {
        ItemInstance ropeStack = new ItemInstance(mod_FCBetterThanWolves.fcRopeItem);
        iUpdateRopeStateCounter = 20;
        if(FCUtilsInventory.AddItemInstanceToInventory(this, ropeStack))
        {
            level.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.pop", 0.05F, (level.rand.nextFloat() - level.rand.nextFloat()) * 0.7F + 1.0F);
        } else
        {
            FCUtilsMisc.EjectStackWithRandomOffset(level, x, y, z, ropeStack);
        }
    }

    public int GetContainedRopeCount()
    {
        return FCUtilsInventory.CountItemsInInventory(this, mod_FCBetterThanWolves.fcRopeItem.id, -1);
    }

    public void RemoveRopeFromInventory()
    {
        int iRopeSlot = FCUtilsInventory.GetFirstOccupiedStackOfItem(this, mod_FCBetterThanWolves.fcRopeItem.id);
        if(iRopeSlot >= 0)
        {
            FCUtilsInventory.DecrStackSize(this, iRopeSlot, 1);
        }
    }

    private final int iPulleyInventorySize = 2;
    private final int iPulleyStackSizeLimit = 64;
    private final double dPulleyMaxPlayerInteractionDist = 64D;
    private final int iTicksToUpdateRopeState = 20;
    private ItemInstance pulleyContents[];
    private int iUpdateRopeStateCounter;
}
