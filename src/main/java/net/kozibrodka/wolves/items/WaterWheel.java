
package net.kozibrodka.wolves.items;

import net.kozibrodka.wolves.blocks.Axle;
import net.kozibrodka.wolves.entity.FCEntityTEST;
import net.kozibrodka.wolves.entity.WaterWheelEntity;
import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;

public class WaterWheel extends TemplateItemBase
{

    public WaterWheel(Identifier iItemID)
    {
        super(iItemID);
        maxStackSize = 1;
    }


    public boolean useOnTile(ItemInstance ItemInstance, PlayerBase entityplayer, Level world, int i, int j, int k, int l)
    {
        int iTargetid = world.getTileId(i, j, k);
        if(iTargetid == BlockListener.axleBlock.id && !world.isServerSide)
        {
            int iAxisAlignment = ((Axle)BlockListener.axleBlock).GetAxisAlignment(world, i, j, k);
            if(iAxisAlignment != 0)
            {
                boolean bIAligned = false;
                if(iAxisAlignment == 2)
                {
                    bIAligned = true;
                }
                if(WaterWheelEntity.WaterWheelValidateAreaAroundBlock(world, i, j, k, bIAligned))
                {
//                    world.spawnEntity(new WaterWheelEntity(world, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, bIAligned));
                    world.spawnEntity(new FCEntityTEST(world, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, rand.nextInt(16)));
                    ItemInstance.count--;
                    return true;
                }
                entityplayer.sendMessage("Not enough room to place Water Wheel");
            }
        }
        return false;
    }
}
