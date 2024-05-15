
package net.kozibrodka.wolves.items;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.utils.ReplaceableBlockChecker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItem;

public class RopeItem extends TemplateItem
{

    public RopeItem(Identifier iItemID)
    {
        super(iItemID);
    }

    public boolean useOnBlock(ItemStack ItemInstance, PlayerEntity entityplayer, World world, int i, int j, int k, int iFacing)
    {
        if(ItemInstance.count == 0)
        {
            return false;
        }
        int iTargetid = world.getBlockId(i, j, k);
        if(iTargetid == BlockListener.anchor.id || iTargetid == BlockListener.rope.id)
        {
            for(int tempj = j - 1; tempj >= 0; tempj--)
            {
                int iTempid = world.getBlockId(i, tempj, k);
                if(ReplaceableBlockChecker.IsReplaceableBlock(world, i, tempj, k))
                {
                    if(world.setBlock(i, tempj, k, BlockListener.rope.id))
                    {
                        BlockListener.rope.onPlaced(world, i, tempj, k, iFacing);
                        BlockListener.rope.onPlaced(world, i, tempj, k, entityplayer);
                        world.playSound((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, BlockListener.rope.soundGroup.getSound(), (BlockListener.rope.soundGroup.method_1976() + 1.0F) / 2.0F, BlockListener.rope.soundGroup.method_1977() * 0.8F);
                        ItemInstance.count--;
                        return true;
                    } else
                    {
                        return false;
                    }
                }
                if(iTempid != BlockListener.rope.id)
                {
                    return false;
                }
            }

        }
        return false;
    }

}
