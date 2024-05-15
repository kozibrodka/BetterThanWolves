
package net.kozibrodka.wolves.items;

import net.kozibrodka.wolves.block.AxleBlock;
import net.kozibrodka.wolves.entity.WindMillEntity;
import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItem;

public class WindMillItem extends TemplateItem

{
    public WindMillItem(Identifier iItemID)
    {
        super(iItemID);
        maxCount = 1;
    }
    /**
     Block and Entity Classes not implemented yet
     */

    public boolean useOnBlock(ItemStack iteminstance, PlayerEntity entityplayer, World world, int i, int j, int k, int l)
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
                if(WindMillEntity.WindMillValidateAreaAroundBlock(world, i, j, k, bIAligned))
                {
                    world.method_210(new WindMillEntity(world, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, bIAligned));
                    iteminstance.count--;
                    return true;
                }
                entityplayer.method_490("Not enough room to place Wind Mill (They are absolutely HUGE!)");
            }
        }
        return false;
    }
}
