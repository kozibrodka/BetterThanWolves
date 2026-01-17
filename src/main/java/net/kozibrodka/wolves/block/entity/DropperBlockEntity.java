package net.kozibrodka.wolves.block.entity;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class DropperBlockEntity extends BlockEntity implements Inventory {
    private static final int WEIGHT = 1;
    private static final int STORAGE = 0;

    private ItemStack[] dropperContents;
    private boolean dropDone;
    private boolean providePower;

    public DropperBlockEntity() {
        dropperContents = new ItemStack[2];
        dropDone = true;
    }

    @Override
    public void tick() {
        ItemStack weight = dropperContents[WEIGHT];
        if (weight == null) {
            providePower = false;
            megaNotification();
            return;
        }
        if (weight.itemId != ItemListener.weight.id) {
            providePower = false;
            megaNotification();
            return;
        }
        ItemStack storage = dropperContents[STORAGE];
        if (storage == null) {
            providePower = false;
            megaNotification();
            return;
        }
        storage = storage.copy();
        if (storage.count < weight.count) {
            providePower = false;
            megaNotification();
            return;
        }
        int belowId = world.getBlockId(x, y - 1, z);
        if (!(belowId == 0 || Block.BLOCKS[belowId].getCollisionShape(world, x, y - 1, z) == null)) {
            providePower = false;
            megaNotification();
            return;
        }
        providePower = true;
        megaNotification();
        if (dropDone) {
            return;
        }
        ItemStack drop = storage.copy();
        drop.count = weight.count;
        storage.count -= weight.count;
        if (storage.count == 0) {
            dropperContents[STORAGE] = null;
        } else {
            dropperContents[STORAGE] = storage;
        }
        dropDone = true;
        ItemEntity itemEntity = new ItemEntity(world, x + 0.5, y - 0.5, z + 0.5, drop);
        itemEntity.velocityX = 0;
        itemEntity.velocityY = 0;
        itemEntity.velocityZ = 0;
        world.spawnEntity(itemEntity);
    }

    private void megaNotification() {
        if (world.getBlockId(x + 1, y, z) == Block.REDSTONE_WIRE.id) {
            Block.REDSTONE_WIRE.neighborUpdate(world, x + 1, y, z, BlockListener.dropper.id);
        }
        if (world.getBlockId(x - 1, y, z) == Block.REDSTONE_WIRE.id) {
            Block.REDSTONE_WIRE.neighborUpdate(world, x - 1, y, z, BlockListener.dropper.id);
        }
        if (world.getBlockId(x, y, z + 1) == Block.REDSTONE_WIRE.id) {
            Block.REDSTONE_WIRE.neighborUpdate(world, x, y, z + 1, BlockListener.dropper.id);
        }
        if (world.getBlockId(x, y, z - 1) == Block.REDSTONE_WIRE.id) {
            Block.REDSTONE_WIRE.neighborUpdate(world, x, y, z - 1, BlockListener.dropper.id);
        }
    }

    public void requestDrop() {
        dropDone = false;
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public ItemStack getStack(int slot) {
        return dropperContents[slot];
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return InventoryHandler.decreaseStackSize(this, slot, amount);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        dropperContents[slot] = stack;
        if (stack != null && stack.count > getMaxCountPerStack()) {
            stack.count = getMaxCountPerStack();
        }
        markDirty();
    }

    @Override
    public String getName() {
        return "Dropper";
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (world.getBlockEntity(x, y, z) != this) {
            return false;
        } else {
            return player.getSquaredDistance((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D) <= 64D;
        }
    }

    public void readNbt(NbtCompound nbtCompound) {
        super.readNbt(nbtCompound);
        NbtList nbtList = nbtCompound.getList("Items");
        dropperContents = new ItemStack[size()];
        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound itemCompound = (NbtCompound) nbtList.get(i);
            int j = itemCompound.getByte("Slot") & 0xff;
            if (j < dropperContents.length) {
                dropperContents[j] = new ItemStack(itemCompound);
            }
        }
        dropDone = nbtCompound.getBoolean("DropDone");
    }

    public void writeNbt(NbtCompound nbtCompound) {
        super.writeNbt(nbtCompound);
        NbtList nbtList = new NbtList();
        for (int i = 0; i < dropperContents.length; i++) {
            if (dropperContents[i] != null) {
                NbtCompound itemCompound = new NbtCompound();
                itemCompound.putByte("Slot", (byte) i);
                dropperContents[i].writeNbt(itemCompound);
                nbtList.add(itemCompound);
            }
        }
        nbtCompound.put("Items", nbtList);
        nbtCompound.putBoolean("DropDone", dropDone);
    }

    public boolean shouldProvidePower() {
        return providePower;
    }
}
