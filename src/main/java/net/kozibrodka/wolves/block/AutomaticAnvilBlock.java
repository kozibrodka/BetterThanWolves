package net.kozibrodka.wolves.block;

import net.kozibrodka.wolves.block.entity.AutomaticAnvilBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

public class AutomaticAnvilBlock extends LazyBlockWithEntityTemplate {
    public AutomaticAnvilBlock(Identifier identifier, Material material, float hardness, BlockSoundGroup blockSounds) {
        super(identifier, material, hardness, blockSounds);
    }

    @Override
    public int getTexture(int side, int meta) {
        if (side == meta) {
            return topTexture;
        }
        return sideTexture;
    }

    @Override
    public void onPlaced(World world, int x, int y, int z, LivingEntity placer) {
        int var6 = MathHelper.floor((double)(placer.yaw * 4.0F / 360.0F) + (double)0.5F) & 3;
        if (var6 == 0) {
            world.setBlockMeta(x, y, z, 2);
        }

        if (var6 == 1) {
            world.setBlockMeta(x, y, z, 5);
        }

        if (var6 == 2) {
            world.setBlockMeta(x, y, z, 3);
        }

        if (var6 == 3) {
            world.setBlockMeta(x, y, z, 4);
        }
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new AutomaticAnvilBlockEntity();
    }
}
