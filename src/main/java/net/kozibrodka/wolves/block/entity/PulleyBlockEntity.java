// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCTileEntityPulley.java

package net.kozibrodka.wolves.block.entity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.block.AnchorBlock;
import net.kozibrodka.wolves.block.PulleyBlock;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;
import net.kozibrodka.wolves.utils.ReplaceableBlockChecker;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;

import java.util.List;


public class PulleyBlockEntity extends BlockEntity
    implements Inventory
{

    public PulleyBlockEntity()
    {
        pulleyContents = new ItemStack[2];
        iUpdateRopeStateCounter = 20;
    }

    public String getName()
    {
        return "Pulley";
    }

    public int size()
    {
        return 2;
    }

    public int getMaxCountPerStack()
    {
        return 64;
    }

    public ItemStack getStack(int iSlot)
    {
        return pulleyContents[iSlot];
    }

    public void setStack(int iSlot, ItemStack ItemInstance)
    {
        pulleyContents[iSlot] = ItemInstance;
        if(ItemInstance != null && ItemInstance.count > getMaxCountPerStack())
        {
            ItemInstance.count = getMaxCountPerStack();
        }
        markDirty();
    }

    public ItemStack removeStack(int iSlot, int iAmount)
    {
        return InventoryHandler.decreaseStackSize(this, iSlot, iAmount);
    }

    public void readNbt(NbtCompound nbttagcompound)
    {
        super.readNbt(nbttagcompound);
        NbtList nbttaglist = nbttagcompound.getList("Items");
        pulleyContents = new ItemStack[size()];
        for(int i = 0; i < nbttaglist.size(); i++)
        {
            NbtCompound nbttagcompound1 = (NbtCompound)nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < pulleyContents.length)
            {
                pulleyContents[j] = new ItemStack(nbttagcompound1);
            }
        }

    }

    public void writeNbt(NbtCompound nbttagcompound)
    {
        super.writeNbt(nbttagcompound);
        NbtList nbttaglist = new NbtList();
        for(int i = 0; i < pulleyContents.length; i++)
        {
            if(pulleyContents[i] != null)
            {
                NbtCompound nbttagcompound1 = new NbtCompound();
                nbttagcompound1.putByte("Slot", (byte)i);
                pulleyContents[i].writeNbt(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
    }

    public boolean canPlayerUse(PlayerEntity entityplayer)
    {
        if(world.getBlockEntity(x, y, z) != this)
        {
            return false;
        } else
        {
            return entityplayer.method_1347((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
        }
    }

    public void tick()
    {
        if(world.isRemote){
            return;
        }
        iUpdateRopeStateCounter--;
        if(iUpdateRopeStateCounter <= 0)
        {
            boolean bIsRedstoneOn = ((PulleyBlock) BlockListener.pulley).IsRedstoneOn(world, x, y, z);
            if(!bIsRedstoneOn)
            {
                boolean bIsOn = ((PulleyBlock)BlockListener.pulley).IsBlockOn(world, x, y, z);
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
        return ((PulleyBlock)BlockListener.pulley).IsBlockOn(world, x, y, z);
    }

    public boolean IsRedstonePowered()
    {
        return ((PulleyBlock)BlockListener.pulley).IsRedstoneOn(world, x, y, z);
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
            int iTempid = world.getBlockId(x, tempj, z);
            if(iTempid == BlockListener.anchor.id)
            {
                if(((AnchorBlock)BlockListener.anchor).GetAnchorFacing(world, x, tempj, z) != 1)
                {
                    break;
                }
                ((AnchorBlock)BlockListener.anchor).NotifyAnchorBlockOfAttachedPulleyStateChange(this, world, x, tempj, z);
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
            int iTempid = world.getBlockId(x, tempj, z);
            if(iTempid == BlockListener.rope.id)
            {
                if(world.getBlockId(x, tempj - 1, z) != BlockListener.rope.id)
                {
                    AddRopeToInventory();
                    Block targetBlock = BlockListener.rope;
                    if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER){
                        voicePacket(world, targetBlock.soundGroup.getSound(), x, tempj, z, targetBlock.soundGroup.method_1976() / 4F, targetBlock.soundGroup.method_1977() * 0.8F);
                    }
                    world.setBlock(x, tempj, z, 0);
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
                int iTempid = world.getBlockId(x, tempj, z);
                if(ReplaceableBlockChecker.IsReplaceableBlock(world, x, tempj, z))
                {
                    if(world.setBlock(x, tempj, z, BlockListener.rope.id))
                    {
                        BlockListener.rope.onPlaced(world, x, tempj, z, 0);
                        Block targetBlock = BlockListener.rope;
                        if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER){
                            voicePacket(world, targetBlock.soundGroup.getSound(), x, tempj, z, targetBlock.soundGroup.method_1976() / 4F, targetBlock.soundGroup.method_1977() * 0.8F);
                        }
                        RemoveRopeFromInventory();
                        int iBlockBelowTargetID = world.getBlockId(x, tempj - 1, z);
                        if(iBlockBelowTargetID == BlockListener.anchor.id && ((AnchorBlock)BlockListener.anchor).GetAnchorFacing(world, x, tempj - 1, z) == 1)
                        {
                            ((AnchorBlock)BlockListener.anchor).NotifyAnchorBlockOfAttachedPulleyStateChange(this, world, x, tempj - 1, z);
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
    public void voicePacket(World world, String name, int x, int y, int z, float g, float h){
        List list2 = world.field_200;
        if(list2.size() != 0) {
            for(int k = 0; k < list2.size(); k++)
            {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g,h));
            }
        }
    }

    public void AddRopeToInventory()
    {
        ItemStack ropeStack = new ItemStack(ItemListener.ropeItem);
        iUpdateRopeStateCounter = 20;
        if(InventoryHandler.addItemInstanceToInventory(this, ropeStack))
        {
            world.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.pop", 0.05F, (world.field_214.nextFloat() - world.field_214.nextFloat()) * 0.7F + 1.0F);
            if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                voicePacket(world, "random.pop", x, y, z, 0.05F, (world.field_214.nextFloat() - world.field_214.nextFloat()) * 0.7F + 1.0F);
            }
        } else
        {
            UnsortedUtils.ejectStackWithRandomOffset(world, x, y, z, ropeStack);
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
    private ItemStack pulleyContents[];
    private int iUpdateRopeStateCounter;
}
