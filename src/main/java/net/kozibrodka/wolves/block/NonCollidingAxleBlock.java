package net.kozibrodka.wolves.block;

import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class NonCollidingAxleBlock extends AxleBlock {
    public NonCollidingAxleBlock(Identifier iid) {
        super(iid);
    }

    @Override
    public Box getCollisionShape(World world, int i, int j, int k) {
        return null;
    }

    @Override
    public int getDroppedItemId(int blockMeta, Random random) {
        return BlockListener.axleBlock.asItem().id;
    }
}
