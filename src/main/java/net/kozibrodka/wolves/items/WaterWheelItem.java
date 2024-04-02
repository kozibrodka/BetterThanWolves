
package net.kozibrodka.wolves.items;

import net.kozibrodka.wolves.block.AxleBlock;
import net.kozibrodka.wolves.entity.WaterWheelEntity;
import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItem;

public class WaterWheelItem extends TemplateItem
{

    public WaterWheelItem(Identifier iItemID)
    {
        super(iItemID);
        maxCount = 1;
    }


    public boolean useOnBlock(ItemStack ItemInstance, PlayerEntity entityplayer, World world, int i, int j, int k, int l)
    {
        int iTargetid = world.getBlockId(i, j, k);
        if(iTargetid == BlockListener.axleBlock.id && !world.isRemote)
        {
            int iAxisAlignment = ((AxleBlock)BlockListener.axleBlock).GetAxisAlignment(world, i, j, k);
            if(iAxisAlignment != 0)
            {
                boolean bIAligned = false;
                if(iAxisAlignment == 2)
                {
                    bIAligned = true;
                }
                if(WaterWheelEntity.WaterWheelValidateAreaAroundBlock(world, i, j, k, bIAligned))
                {
                    world.method_210(new WaterWheelEntity(world, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, bIAligned));
//                    world.spawnEntity(new FCEntityTEST(world, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, rand.nextInt(16)));
                    ItemInstance.count--;
                    return true;
                }
                entityplayer.method_490("Not enough room to place Water Wheel");
            }
        }
        return false;
    }
}
