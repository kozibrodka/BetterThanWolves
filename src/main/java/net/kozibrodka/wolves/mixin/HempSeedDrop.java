package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.minecraft.block.TallGrass;
import net.minecraft.entity.Item;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.awt.event.ItemListener;

@Mixin(TallGrass.class)
@Unique
public class HempSeedDrop {

    public void dropWithChance(Level level, int x, int y, int z, BlockState state, int meta, float chance) {
        float f1 = 0.7F;
        float f2 = level.rand.nextFloat() * f1 + (1.0F - f1) * 0.5F;
        float f3 = level.rand.nextFloat() * f1 + (1.0F - f1) * 0.5F;
        float f4 = level.rand.nextFloat() * f1 + (1.0F - f1) * 0.5F;
        Item entityItem = new Item(level, (float)x + f2, (float)y + f3, (float)z + f4, new ItemInstance(ItemBase.seeds));
        entityItem.pickupDelay = 10;
        if (level.rand.nextInt(8) == 0) level.spawnEntity(entityItem);
        if (level.rand.nextInt(50) != 0)  return;
        f2 = level.rand.nextFloat() * f1 + (1.0F - f1) * 0.5F;
        f3 = level.rand.nextFloat() * f1 + (1.0F - f1) * 0.5F;
        f4 = level.rand.nextFloat() * f1 + (1.0F - f1) * 0.5F;
        entityItem = new Item(level, (float)x + f2, (float)y + f3, (float)z + f4, new ItemInstance(mod_FCBetterThanWolves.fcHempSeeds));
        entityItem.pickupDelay = 10;
        level.spawnEntity(entityItem);
    }
}
