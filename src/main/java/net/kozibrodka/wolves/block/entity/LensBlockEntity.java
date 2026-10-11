package net.kozibrodka.wolves.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class LensBlockEntity extends BlockEntity {

    private boolean isLensOn = false;
    private int tickrate;

    @Override
    public void tick() // updateEntity
    {
        if (world.isRemote) {
            return;
        }
        /// interwał sprawdzania.
        if(tickrate % 20 == 0){

        }
    }

    @Override
    public void readNbt(NbtCompound nbttagcompound) {
        super.readNbt(nbttagcompound);
        isLensOn = nbttagcompound.getBoolean("shine");
    }

    @Override
    public void writeNbt(NbtCompound nbttagcompound) {
        super.writeNbt(nbttagcompound);
        nbttagcompound.putBoolean("shine", isLensOn);
    }
}
