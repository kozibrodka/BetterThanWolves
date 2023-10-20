
package net.kozibrodka.wolves.items;

import net.kozibrodka.wolves.blocks.Axle;
import net.kozibrodka.wolves.entity.WindMillEntity;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;

public class WindMill extends TemplateItemBase

{
    public WindMill(Identifier iItemID)
    {
        super(iItemID);
        maxStackSize = 1;
    }
    /**
     Block and Entity Classes not implemented yet
     */

    public boolean useOnTile(ItemInstance iteminstance, PlayerBase entityplayer, Level world, int i, int j, int k, int l)
    {
        int iTargetid = world.getTileId(i, j, k);
        if(iTargetid == mod_FCBetterThanWolves.fcAxleBlock.id && !world.isServerSide)
        {
            int iAxisAlignment = ((Axle)mod_FCBetterThanWolves.fcAxleBlock).GetAxisAlignment(world, i, j, k);
            if(iAxisAlignment != 0)
            {
                boolean bIAligned = false;
                if(iAxisAlignment == 2)
                {
                    bIAligned = true;
                }
                if(WindMillEntity.WindMillValidateAreaAroundBlock(world, i, j, k, bIAligned))
                {
                    world.spawnEntity(new WindMillEntity(world, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, bIAligned));
                    iteminstance.count--;
                    return true;
                }
                entityplayer.sendMessage("Not enough room to place Wind Mill (They are friggin HUGE!)");
            }
        }
        return false;
    }
}
