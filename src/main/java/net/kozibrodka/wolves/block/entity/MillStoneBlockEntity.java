// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode
// Source File Name:   FCTileEntityMillStone.java

package net.kozibrodka.wolves.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.block.CompanionCubeBlock;
import net.kozibrodka.wolves.block.MillStoneBlock;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.recipe.MillingRecipeRegistry;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;


public class MillStoneBlockEntity extends BlockEntity
    implements Inventory
{
    static boolean isHarderThanWolvesPresent = net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("harderthanwolves");

    public MillStoneBlockEntity()
    {
        millStoneContents = new ItemStack[3];
        iMillStoneGrindCounter = 0;
    }

    @Override
    public void cancelRemoval() {
        super.cancelRemoval();
    }


    public int size()
    {
        return 3;
    }

    public ItemStack getStack(int iSlot)
    {
        return millStoneContents[iSlot];
    }

    public ItemStack removeStack(int iSlot, int iAmount)
    {
        return InventoryHandler.decreaseStackSize(this, iSlot, iAmount);
    }

    public void setStack(int iSlot, ItemStack ItemInstance)
    {
        millStoneContents[iSlot] = ItemInstance;
        if(ItemInstance != null && ItemInstance.count > getMaxCountPerStack())
        {
            ItemInstance.count = getMaxCountPerStack();
        }
        markDirty();
    }

    public String getName()
    {
        return "MillStone";
    }

    public void readNbt(NbtCompound nbttagcompound)
    {
        super.readNbt(nbttagcompound);
        NbtList nbttaglist = nbttagcompound.getList("Items");
        millStoneContents = new ItemStack[size()];
        for(int i = 0; i < nbttaglist.size(); i++)
        {
            NbtCompound nbttagcompound1 = (NbtCompound)nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < millStoneContents.length)
            {
                millStoneContents[j] = new ItemStack(nbttagcompound1);
            }
        }

        if(nbttagcompound.contains("grindCounter"))
        {
            iMillStoneGrindCounter = nbttagcompound.getInt("grindCounter");
        }
    }

    public void writeNbt(NbtCompound nbttagcompound)
    {
        super.writeNbt(nbttagcompound);
        NbtList nbttaglist = new NbtList();
        for(int i = 0; i < millStoneContents.length; i++)
        {
            if(millStoneContents[i] != null)
            {
                NbtCompound nbttagcompound1 = new NbtCompound();
                nbttagcompound1.putByte("Slot", (byte)i);
                millStoneContents[i].writeNbt(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
        nbttagcompound.putInt("grindCounter", iMillStoneGrindCounter);
    }

    public int getMaxCountPerStack()
    {
        return 64;
    }

    public boolean canPlayerUse(PlayerEntity entityplayer)
    {
        if(world.getBlockEntity(x, y, z) != this)
        {
            return false;
        } else
        {
            return entityplayer.getSquaredDistance((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
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
            Item tempItem = millStoneContents[tempIndex].getItem();
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
        if(world.isRemote){
            return;
        }
        int iUnmilledItemIndex = GetUnmilledItemInventoryIndex();

        if(iUnmilledItemIndex < 0)
        {
            iMillStoneGrindCounter = 0;
            return;
        }

        if(!((MillStoneBlock) BlockListener.millStone).IsBlockOn(world, x, y, z)) return;

        iMillStoneGrindCounter++;

        Item unmilledItem = millStoneContents[iUnmilledItemIndex].getItem();

        if (unmilledItem == null) {
            return;
        }

        Identifier unmilledItemIdentifier = ItemRegistry.INSTANCE.getId(unmilledItem);

        if(unmilledItemIdentifier == BlockListener.MOD_ID.id("companionCube")) // Companion cube torture during milling
        {
            if(millStoneContents[iUnmilledItemIndex].getDamage() == 0 && world.random.nextInt(10) == 0)
            {
                world.playSound((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, "mob.wolf.hurt", 2.0F, (world.random.nextFloat() - world.random.nextFloat()) * 0.2F + 1.0F);
                if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                    voicePacket(world, "mob.wolf.hurt", x, y, z, 2.0F, (world.random.nextFloat() - world.random.nextFloat()) * 0.2F + 1.0F);
                }
            }
            if(world.random.nextInt(20) == 0)
            {
                ItemStack stringStack = new ItemStack(Item.STRING);
                EjectStack(stringStack);
            }
            if(world.random.nextInt(60) == 0)
            {
                ItemStack woolStack = new ItemStack(Item.DYE, 1, 1);
                EjectStack(woolStack);
            }
        }
        else if(unmilledItemIdentifier == BlockRegistry.INSTANCE.getId(Block.NETHERRACK) && world.random.nextInt(10) == 0 && !isHarderThanWolvesPresent) // Random scream when there is netherrack
        {
            world.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "mob.ghast.scream", 0.25F, world.random.nextFloat() * 0.4F + 0.8F);
            if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                voicePacket(world, "mob.ghast.scream", x, y, z, 0.25F, world.random.nextFloat() * 0.4F + 0.8F);
            }
        }

        if(iMillStoneGrindCounter < 200) return;

        iMillStoneGrindCounter = 0;

        ItemStack milledStack = MillingRecipeRegistry.getInstance().getResult(unmilledItemIdentifier);
        if(milledStack != null)
        {
            milledStack = new ItemStack(milledStack.itemId, milledStack.count, milledStack.getDamage()); // This looks very redundant but cures the millstone from the curse of losing the item count value from the registry for no apparent reason besides metaphysical powers beyond human comprehension or something, I do not know how to explain this anymore.
            if (milledStack.count == 0) milledStack.count = 1;
            removeStack(iUnmilledItemIndex, 1);
            EjectStack(milledStack);
        }
        else if(unmilledItemIdentifier == BlockListener.MOD_ID.id("companionCube")) // Special recipe (companion cube)
        {
            EjectStack(new ItemStack(ItemListener.wolfRaw.id, 1, 0));
            CompanionCubeBlock.SpawnHearts(world, x, y, z);
            if(millStoneContents[iUnmilledItemIndex].getDamage() == 0)
            {
                world.playSound((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, "mob.wolf.whine", 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
                if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                    voicePacket(world, "mob.wolf.whine", x, y, z, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
                }
            }
            removeStack(iUnmilledItemIndex, 1);
        }

    }

    public void EjectStack(ItemStack stack)
    {
        BlockPosition targetPos = new BlockPosition(x, y, z);
        int iDirection = 2 + world.random.nextInt(4);
        targetPos.AddFacingAsOffset(iDirection);
        UnsortedUtils.ejectStackWithRandomOffset(world, targetPos.i, targetPos.j, targetPos.k, stack);
    }

    public boolean AddSingleItemToInventory(int iItemShiftedIndex)
    {
        ItemStack ItemInstance = new ItemStack(iItemShiftedIndex, 1, 0);
        return addItemInstanceToInventory(ItemInstance);
    }

    public boolean addItemInstanceToInventory(ItemStack ItemInstance)
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
            setStack(i, ItemInstance);
            return true;
        } else
        {
            return false;
        }
    }

    private int getFirstEmptyStack()
    {
        for(int i = 0; i < size(); i++)
        {
            if(getStack(i) == null)
            {
                return i;
            }
        }

        return -1;
    }

    private int storePartialItemInstance(ItemStack ItemInstance)
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
        if(getStack(k) == null)
        {
            setStack(k, new ItemStack(i, 0, ItemInstance.getDamage()));
        }
        int l = j;
        ItemStack tempStack = getStack(k);
        if(l > tempStack.getMaxCount() - tempStack.count)
        {
            l = tempStack.getMaxCount() - tempStack.count;
        }
        if(l > getMaxCountPerStack() - tempStack.count)
        {
            l = getMaxCountPerStack() - tempStack.count;
        }
        if(l == 0)
        {
            return j;
        } else
        {
            j -= l;
            tempStack.count += l;
            setStack(k, tempStack);
            return j;
        }
    }

    private int storeItemInstance(ItemStack ItemInstance)
    {
        for(int i = 0; i < size(); i++)
        {
            ItemStack tempStack = getStack(i);
            if(tempStack != null && tempStack.itemId == ItemInstance.itemId && tempStack.isStackable() && tempStack.count < tempStack.getMaxCount() && tempStack.count < getMaxCountPerStack() && (!tempStack.hasSubtypes() || tempStack.getDamage() == ItemInstance.getDamage()))
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
            Item tempItem = millStoneContents[tempIndex].getItem();
            if(tempItem != null && tempItem.id == BlockListener.companionCube.id && millStoneContents[tempIndex].getDamage() == 0)
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
            int iUnmilledItemID = millStoneContents[iUnmilledItemIndex].getItem().id;
            if(iUnmilledItemID == BlockListener.companionCube.id && millStoneContents[iUnmilledItemIndex].getDamage() == 0)
            {
                return true;
            }
        }
        return false;
    }

    public void markDirty()
    {
        if (world == null) {
            return;
        }
        if(IsWholeCompanionCubeInInventory())
        {
            world.playSound((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, "mob.wolf.whine", 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
            if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                voicePacket(world, "mob.wolf.whine", x, y, z, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
            }
        }
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(World world, String name, int x, int y, int z, float g, float h){
        List list2 = world.players;
        if(list2.size() != 0) {
            for(int k = 0; k < list2.size(); k++)
            {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g,h));
            }
        }
    }

    private final int iMillStoneInventorySize = 3;
    private final int iMillStoneStackSizeLimit = 64;
    private final double dMillStoneMaxPlayerInteractionDist = 64D;
    private final int iMillStoneTimeToGrind = 200;
    private ItemStack millStoneContents[];
    public int iMillStoneGrindCounter;
}
