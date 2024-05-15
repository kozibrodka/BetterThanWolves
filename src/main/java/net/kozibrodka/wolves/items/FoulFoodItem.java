
package net.kozibrodka.wolves.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateCropBlock;
import net.modificationstation.stationapi.api.template.block.TemplateSaplingBlock;
import net.modificationstation.stationapi.api.template.item.TemplateFoodItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class FoulFoodItem extends TemplateFoodItem

{
    public FoulFoodItem(Identifier identifier, int healAmount, boolean isWolfFood) {
        super(identifier, healAmount, isWolfFood);
        maxCount = 64;
    }

    public boolean useOnBlock(ItemStack ItemInstance, PlayerEntity entityplayer, World world, int i, int j, int k, int l)
    {
        int i1 = world.getBlockId(i, j, k);
        if(i1 == Block.SAPLING.id)
        {
            if(!world.isRemote)
            {
                ((TemplateSaplingBlock)Block.SAPLING).method_533(world, i, j, k, world.field_214);
                ItemInstance.count--;
            }
            return true;
        }
        if(i1 == Block.WHEAT.id)
        {
            if(!world.isRemote)
            {
                ((TemplateCropBlock)Block.WHEAT).method_996(world, i, j, k);
                ItemInstance.count--;
            }
            return true;
        }
        if(i1 == Block.GRASS_BLOCK.id)
        {
            if(!world.isRemote)
            {
                ItemInstance.count--;
label0:
                for(int j1 = 0; j1 < 128; j1++)
                {
                    int k1 = i;
                    int l1 = j + 1;
                    int i2 = k;
                    for(int j2 = 0; j2 < j1 / 16; j2++)
                    {
                        k1 += random.nextInt(3) - 1;
                        l1 += ((random.nextInt(3) - 1) * random.nextInt(3)) / 2;
                        i2 += random.nextInt(3) - 1;
                        if(world.getBlockId(k1, l1 - 1, i2) != Block.GRASS_BLOCK.id || world.method_1780(k1, l1, i2))
                        {
                            continue label0;
                        }
                    }

                    if(world.getBlockId(k1, l1, i2) != 0)
                    {
                        continue;
                    }
                    if(random.nextInt(10) != 0)
                    {
                        world.method_201(k1, l1, i2, Block.GRASS.id, 1);
                        continue;
                    }
                    if(random.nextInt(3) != 0)
                    {
                        world.setBlock(k1, l1, i2, Block.DANDELION.id);
                    } else
                    {
                        world.setBlock(k1, l1, i2, Block.ROSE.id);
                    }
                }

            }
            return true;
        } else
        {
            return false;
        }
    }

}
