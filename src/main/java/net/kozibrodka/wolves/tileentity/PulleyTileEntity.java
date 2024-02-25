// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCTileEntityPulley.java

package net.kozibrodka.wolves.tileentity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.blocks.Anchor;
import net.kozibrodka.wolves.blocks.Pulley;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.kozibrodka.wolves.utils.ReplaceableBlockChecker;
import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;

import java.util.List;


public class PulleyTileEntity extends TileEntityBase
    implements InventoryBase
{

    public PulleyTileEntity()
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
        return InventoryHandler.decreaseStackSize(this, iSlot, iAmount);
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
        if(level.isServerSide){
            return;
        }
        iUpdateRopeStateCounter--;
        if(iUpdateRopeStateCounter <= 0)
        {
            boolean bIsRedstoneOn = ((Pulley) BlockListener.pulley).IsRedstoneOn(level, x, y, z);
            if(!bIsRedstoneOn)
            {
                boolean bIsOn = ((Pulley)BlockListener.pulley).IsBlockOn(level, x, y, z);
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
        return ((Pulley)BlockListener.pulley).IsBlockOn(level, x, y, z);
    }

    public boolean IsRedstonePowered()
    {
        return ((Pulley)BlockListener.pulley).IsRedstoneOn(level, x, y, z);
    }

    public boolean IsRaising()
    {
        return !IsRedstonePowered() && IsMechanicallyPowered();
    }

    public boolean IsLowering()
    {
        return !IsRedstonePowered() && !IsMechanicallyPowered() && InventoryHandler.getFirstOccupiedStackOfItem(this, ItemListener.ropeItem.id) >= 0;
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
            if(iTempid == BlockListener.anchor.id)
            {
                if(((Anchor)BlockListener.anchor).GetAnchorFacing(level, x, tempj, z) != 1)
                {
                    break;
                }
                ((Anchor)BlockListener.anchor).NotifyAnchorBlockOfAttachedPulleyStateChange(this, level, x, tempj, z);
                continue;
            }
            if(iTempid != BlockListener.rope.id)
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
            if(iTempid == BlockListener.rope.id)
            {
                if(level.getTileId(x, tempj - 1, z) != BlockListener.rope.id)
                {
                    AddRopeToInventory();
                    BlockBase targetBlock = BlockListener.rope;
                    if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER){
                        voicePacket(level, targetBlock.sounds.getWalkSound(), x, tempj, z, targetBlock.sounds.getVolume() / 4F, targetBlock.sounds.getPitch() * 0.8F);
                    }
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
        int iRopeSlot = InventoryHandler.getFirstOccupiedStackOfItem(this, ItemListener.ropeItem.id);
        iUpdateRopeStateCounter = 20;
        if(iRopeSlot >= 0)
        {
            for(int tempj = y - 1; tempj >= 0; tempj--)
            {
                int iTempid = level.getTileId(x, tempj, z);
                if(ReplaceableBlockChecker.IsReplaceableBlock(level, x, tempj, z))
                {
                    if(level.setTile(x, tempj, z, BlockListener.rope.id))
                    {
                        BlockListener.rope.onBlockPlaced(level, x, tempj, z, 0);
                        BlockBase targetBlock = BlockListener.rope;
                        if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER){
                            voicePacket(level, targetBlock.sounds.getWalkSound(), x, tempj, z, targetBlock.sounds.getVolume() / 4F, targetBlock.sounds.getPitch() * 0.8F);
                        }
                        RemoveRopeFromInventory();
                        int iBlockBelowTargetID = level.getTileId(x, tempj - 1, z);
                        if(iBlockBelowTargetID == BlockListener.anchor.id && ((Anchor)BlockListener.anchor).GetAnchorFacing(level, x, tempj - 1, z) == 1)
                        {
                            ((Anchor)BlockListener.anchor).NotifyAnchorBlockOfAttachedPulleyStateChange(this, level, x, tempj - 1, z);
                        }
                        return true;
                    } else
                    {
                        return false;
                    }
                }
                if(iTempid != BlockListener.rope.id)
                {
                    return false;
                }
            }

        }
        return false;
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(Level world, String name, int x, int y, int z, float g, float h){
        List list2 = world.players;
        if(list2.size() != 0) {
            for(int k = 0; k < list2.size(); k++)
            {
                ServerPlayer player1 = (ServerPlayer) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g,h));
            }
        }
    }

    public void AddRopeToInventory()
    {
        ItemInstance ropeStack = new ItemInstance(ItemListener.ropeItem);
        iUpdateRopeStateCounter = 20;
        if(InventoryHandler.addItemInstanceToInventory(this, ropeStack))
        {
            level.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.pop", 0.05F, (level.rand.nextFloat() - level.rand.nextFloat()) * 0.7F + 1.0F);
            if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                voicePacket(level, "random.pop", x, y, z, 0.05F, (level.rand.nextFloat() - level.rand.nextFloat()) * 0.7F + 1.0F);
            }
        } else
        {
            UnsortedUtils.EjectStackWithRandomOffset(level, x, y, z, ropeStack);
        }
    }

    public int GetContainedRopeCount()
    {
        return InventoryHandler.itemCountInInventory(this, ItemListener.ropeItem.id, -1);
    }

    public void RemoveRopeFromInventory()
    {
        int iRopeSlot = InventoryHandler.getFirstOccupiedStackOfItem(this, ItemListener.ropeItem.id);
        if(iRopeSlot >= 0)
        {
            InventoryHandler.decreaseStackSize(this, iRopeSlot, 1);
        }
    }


    private final int iPulleyInventorySize = 2;
    private final int iPulleyStackSizeLimit = 64;
    private final double dPulleyMaxPlayerInteractionDist = 64D;
    private final int iTicksToUpdateRopeState = 20;
    private ItemInstance pulleyContents[];
    private int iUpdateRopeStateCounter;
}
