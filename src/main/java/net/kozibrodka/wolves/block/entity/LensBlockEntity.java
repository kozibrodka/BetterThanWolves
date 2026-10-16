package net.kozibrodka.wolves.block.entity;

import net.kozibrodka.wolves.block.LensBlock;
import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;

public class LensBlockEntity extends BlockEntity {

    private int ticks;
    private final int lens_HZ = 100;

    @Override
    public void tick() // updateEntity
    {
        if (world.isRemote) {
            return;
        }
        /// interwał sprawdzania. - POGODA / PORA DNIA
        if(ticks % lens_HZ == 0){
            if(world.getBlockId(x, y, z) == BlockListener.lens.id) {
                ((LensBlock) (Block.BLOCKS[BlockListener.lens.id])).checkIfShouldBeLit(world, x, y, z, false);
            }
        }
        ticks++;
    }

}
