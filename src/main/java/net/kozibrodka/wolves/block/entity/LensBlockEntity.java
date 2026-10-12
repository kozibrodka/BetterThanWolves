package net.kozibrodka.wolves.block.entity;

import net.kozibrodka.wolves.block.LensBlock;
import net.kozibrodka.wolves.events.BlockEntityListener;
import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.block.Block;
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
//        System.out.println("ENTITY");

        if (world.isRemote) {
            return;
        }
        /// interwał sprawdzania. - POGODA / PORA DNIA
        if(tickrate % 100 == 0){
            if(world.getBlockId(x, y, z) == BlockListener.lens.id) {
                ((LensBlock) (Block.BLOCKS[BlockListener.lens.id])).checkIfShouldBeLit(world, x, y, z);
            }
//            System.out.println(this.x + "   " +  this.z);
        }
//        if(tickrate == 0){
//            System.out.println("TILE-ENTI-PLACED");
//        }
        tickrate++;
    }

//    @Override
//    public void markDirty() {
//        super.markDirty();
//        System.out.println("MARKED DIRTY");
//    }

//    @Override
//    public void readNbt(NbtCompound nbttagcompound) {
//        super.readNbt(nbttagcompound);
//        isLensOn = nbttagcompound.getBoolean("shine");
//        System.out.println("CZYTAM NBT");
//        if(isLensOn) { //TODO zamiast schedule update... ale z forceIT w tileEntiyt class.
//            ((LensBlock) (Block.BLOCKS[BlockListener.lens.id])).SetLit(world, x, y, z, true, false);
//        }
//    }
//
//    @Override
//    public void writeNbt(NbtCompound nbttagcompound) {
//        super.writeNbt(nbttagcompound);
//        boolean flag = ((LensBlock)(Block.BLOCKS[BlockListener.lens.id])).IsLit(world, x, y, z);
//        nbttagcompound.putBoolean("shine", flag);
//    }
}
