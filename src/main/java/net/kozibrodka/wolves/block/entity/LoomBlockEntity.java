package net.kozibrodka.wolves.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.InventoryHandler;
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

import java.util.List;

public class LoomBlockEntity extends BlockEntity implements Inventory {

    private final int[][] woolMatrix = {
            {ItemListener.whiteWoolBall.id, ItemListener.orangeWoolBall.id, ItemListener.magentaWoolBall.id, ItemListener.lightBlueWoolBall.id,
            ItemListener.yellowWoolBall.id, ItemListener.limeWoolBall.id, ItemListener.pinkWoolBall.id, ItemListener.darkGreyWoolBall.id,
            ItemListener.lightGreyWoolBall.id, ItemListener.cyanWoolBall.id, ItemListener.purpleWoolBall.id, ItemListener.blueWoolBall.id,
            ItemListener.brownWoolBall.id, ItemListener.greenWoolBall.id, ItemListener.redWoolBall.id, ItemListener.blackWoolBall.id},

            {ItemListener.whiteWoolKnit.id, ItemListener.orangeWoolKnit.id, ItemListener.magentaWoolKnit.id, ItemListener.lightBlueWoolKnit.id,
            ItemListener.yellowWoolKnit.id, ItemListener.limeWoolKnit.id, ItemListener.pinkWoolKnit.id, ItemListener.darkGreyWoolKnit.id,
            ItemListener.lightGreyWoolKnit.id, ItemListener.cyanWoolKnit.id, ItemListener.purpleWoolKnit.id, ItemListener.blueWoolKnit.id,
            ItemListener.brownWoolKnit.id, ItemListener.greenWoolKnit.id, ItemListener.redWoolKnit.id, ItemListener.blackWoolKnit.id}
    };

    public LoomBlockEntity() {
        loomContents = new ItemStack[9];
        loomCounter = 0;
    }

    public void tick() {
        if(world.isRemote) {
            return;
        }
        int ingredientIndex = getIngredientIndex();
        if(ingredientIndex < 0) {
            loomCounter = 0;
            return;
        }
        ItemStack loomShuttle = loomContents[8];
        if (loomShuttle == null) {
            return;
        }
        if (loomShuttle.itemId != ItemListener.wickerLoomShuttle.id && loomShuttle.itemId != ItemListener.woolLoomShuttle.id) {
            loomCounter = 0;
            return;
        }
        if(!BlockListener.loom.isBlockOn(world, x, y, z)) return;
        loomCounter++;
        if (loomCounter < 200) return;
        int outputIndex = getValidOutput(ItemListener.wickerSheet.id);
        if (outputIndex > -1
                && loomShuttle.itemId == ItemListener.wickerLoomShuttle.id
                && loomContents[ingredientIndex] != null
                && loomContents[ingredientIndex].itemId == Item.SUGAR_CANE.id) {
            processItem(ingredientIndex, outputIndex, ItemListener.wickerSheet.id);
        } else {
            for (int i = 0; i < 16; i++) {
                outputIndex = getValidOutput(woolMatrix[1][i]);
                if (outputIndex > -1
                        && loomShuttle.itemId == ItemListener.woolLoomShuttle.id
                        && loomContents[ingredientIndex] != null
                        && loomContents[ingredientIndex].itemId == woolMatrix[0][i]) {
                    processItem(ingredientIndex, outputIndex, woolMatrix[1][i]);
                }
            }
        }
        loomCounter = 0;
    }

    private void processItem(int ingredientIndex, int outputIndex, int itemId) {
        if (loomContents[ingredientIndex].count <= 1) {
            loomContents[ingredientIndex] = null;
        } else {
            loomContents[ingredientIndex].count--;
        }
        if (loomContents[outputIndex] == null) {
            loomContents[outputIndex] = new ItemStack(itemId, 1, 0);
        } else {
            loomContents[outputIndex].count++;
        }
        if (loomContents[8].getDamage2() >= 15) {
            loomContents[8] = new ItemStack(ItemListener.emptyLoomShuttle, 1);
        } else {
            loomContents[8].damage(1, null);
        }
    }

    private int getValidOutput(int outputId) {
        for (int i = 4; i < 8; i++) {
            if (loomContents[i] == null) {
                return i;
            }
            if (loomContents[i].itemId != outputId) {
                continue;
            }
            if (loomContents[i].count < loomContents[i].getMaxCount()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void cancelRemoval() {
        super.cancelRemoval();
    }

    public int size() {
        return 9;
    }

    public ItemStack getStack(int iSlot) {
        return loomContents[iSlot];
    }

    public ItemStack removeStack(int iSlot, int iAmount) {
        return InventoryHandler.decreaseStackSize(this, iSlot, iAmount);
    }

    public void setStack(int slot, ItemStack ItemInstance) {
        loomContents[slot] = ItemInstance;
        if(ItemInstance != null && ItemInstance.count > getMaxCountPerStack()) {
            ItemInstance.count = getMaxCountPerStack();
        }
        markDirty();
    }

    public String getName() {
        return "Loom";
    }

    public void readNbt(NbtCompound nbttagcompound) {
        super.readNbt(nbttagcompound);
        NbtList nbttaglist = nbttagcompound.getList("Items");
        loomContents = new ItemStack[size()];
        for(int i = 0; i < nbttaglist.size(); i++) {
            NbtCompound nbttagcompound1 = (NbtCompound)nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j < loomContents.length) {
                loomContents[j] = new ItemStack(nbttagcompound1);
            }
        }

        if(nbttagcompound.contains("loomCounter")) {
            loomCounter = nbttagcompound.getInt("loomCounter");
        }
    }

    public void writeNbt(NbtCompound nbttagcompound) {
        super.writeNbt(nbttagcompound);
        NbtList nbttaglist = new NbtList();
        for(int i = 0; i < loomContents.length; i++) {
            if(loomContents[i] != null) {
                NbtCompound nbttagcompound1 = new NbtCompound();
                nbttagcompound1.putByte("Slot", (byte)i);
                loomContents[i].writeNbt(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
        nbttagcompound.putInt("loomCounter", loomCounter);
    }

    public int getMaxCountPerStack() {
        return 64;
    }

    public boolean canPlayerUse(PlayerEntity entityplayer) {
        if(world.getBlockEntity(x, y, z) != this) {
            return false;
        } else {
            return entityplayer.getSquaredDistance((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
        }
    }

    public int getIngredientIndex() {
        for(int tempIndex = 0; tempIndex < 4; tempIndex++) {
            if(loomContents[tempIndex] == null) {
                continue;
            }
            Item tempItem = loomContents[tempIndex].getItem();
            if(tempItem != null) {
                return tempIndex;
            }
        }

        return -1;
    }

    public int getGrindProgressScaled(int iScale) {
        return (loomCounter * iScale) / 200;
    }

    public boolean IsGrinding() {
        return loomCounter > 0;
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(World world, String name, int x, int y, int z, float g, float h) {
        List list2 = world.players;
        if(!list2.isEmpty()) {
            for (Object o : list2) {
                ServerPlayerEntity player1 = (ServerPlayerEntity) o;
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g, h));
            }
        }
    }

    private ItemStack[] loomContents;
    public int loomCounter;
}
