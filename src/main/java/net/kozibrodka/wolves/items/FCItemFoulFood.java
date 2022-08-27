
package net.kozibrodka.wolves.items;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateCrops;
import net.modificationstation.stationapi.api.template.block.TemplateSapling;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;

public class FCItemFoulFood extends TemplateItemBase

{
    public FCItemFoulFood(Identifier iItemID)
    {
        super(iItemID);
        maxStackSize = 64;
    }

    public boolean useOnTile(ItemInstance ItemInstance, PlayerBase entityplayer, Level world, int i, int j, int k, int l)
    {
        int i1 = world.getTileId(i, j, k);
        if(i1 == BlockBase.SAPLING.id)
        {
            if(!world.isServerSide)
            {
                ((TemplateSapling)BlockBase.SAPLING).growTree(world, i, j, k, world.rand);
                ItemInstance.count--;
            }
            return true;
        }
        if(i1 == BlockBase.CROPS.id)
        {
            if(!world.isServerSide)
            {
                ((TemplateCrops)BlockBase.CROPS).growCropInstantly(world, i, j, k);
                ItemInstance.count--;
            }
            return true;
        }
        if(i1 == BlockBase.GRASS.id)
        {
            if(!world.isServerSide)
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
                        k1 += rand.nextInt(3) - 1;
                        l1 += ((rand.nextInt(3) - 1) * rand.nextInt(3)) / 2;
                        i2 += rand.nextInt(3) - 1;
                        if(world.getTileId(k1, l1 - 1, i2) != BlockBase.GRASS.id || world.canSuffocate(k1, l1, i2))
                        {
                            continue label0;
                        }
                    }

                    if(world.getTileId(k1, l1, i2) != 0)
                    {
                        continue;
                    }
                    if(rand.nextInt(10) != 0)
                    {
                        world.placeBlockWithMetaData(k1, l1, i2, BlockBase.TALLGRASS.id, 1);
                        continue;
                    }
                    if(rand.nextInt(3) != 0)
                    {
                        world.setTile(k1, l1, i2, BlockBase.DANDELION.id);
                    } else
                    {
                        world.setTile(k1, l1, i2, BlockBase.ROSE.id);
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
