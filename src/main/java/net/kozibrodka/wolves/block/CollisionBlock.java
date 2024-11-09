package net.kozibrodka.wolves.block;

import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

/**
 * Collision block for windmills and waterwheels. Creates an obstruction block when broken.
 */
public class CollisionBlock extends LazyBlockTemplate {
    public CollisionBlock(Identifier identifier, Material material, float hardness, BlockSoundGroup blockSounds) {
        super(identifier, material, hardness, blockSounds);
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public int getDroppedItemCount(Random random) {
        return 0;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public void onDestroyedByExplosion(World world, int x, int y, int z) {
        world.setBlock(x, y, z, BlockListener.obstructionBlock.id);
    }

    @Override
    public void afterBreak(World world, PlayerEntity playerEntity, int x, int y, int z, int meta) {
        world.setBlock(x, y, z, BlockListener.obstructionBlock.id);
    }
}
