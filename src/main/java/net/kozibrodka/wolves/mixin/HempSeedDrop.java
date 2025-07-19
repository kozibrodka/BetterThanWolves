package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.ItemListener;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.StationFlatteningBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TallPlantBlock.class)
public class HempSeedDrop implements StationFlatteningBlock {
    @Override
    public void dropWithChance(World level, int x, int y, int z, BlockState state, int meta, float chance) {
        float f1 = 0.7F;
        float f2 = level.random.nextFloat() * f1 + (1.0F - f1) * 0.5F;
        float f3 = level.random.nextFloat() * f1 + (1.0F - f1) * 0.5F;
        float f4 = level.random.nextFloat() * f1 + (1.0F - f1) * 0.5F;
        ItemEntity entityItem = new ItemEntity(level, (float) x + f2, (float) y + f3, (float) z + f4, new ItemStack(Item.SEEDS));
        entityItem.pickupDelay = 10;
        if (level.random.nextInt(8) == 0) level.spawnEntity(entityItem);
        if (level.random.nextInt(50) != 0) return;
        f2 = level.random.nextFloat() * f1 + (1.0F - f1) * 0.5F;
        f3 = level.random.nextFloat() * f1 + (1.0F - f1) * 0.5F;
        f4 = level.random.nextFloat() * f1 + (1.0F - f1) * 0.5F;
        entityItem = new ItemEntity(level, (float) x + f2, (float) y + f3, (float) z + f4, new ItemStack(ItemListener.hempSeeds));
        entityItem.pickupDelay = 10;
        level.spawnEntity(entityItem);
    }
}
