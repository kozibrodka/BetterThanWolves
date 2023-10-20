
package net.kozibrodka.wolves.items;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.utils.ReplaceableBlockChecker;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;

public class Rope extends TemplateItemBase
{

    public Rope(Identifier iItemID)
    {
        super(iItemID);
    }

    public boolean useOnTile(ItemInstance ItemInstance, PlayerBase entityplayer, Level world, int i, int j, int k, int iFacing)
    {
        if(ItemInstance.count == 0)
        {
            return false;
        }
        int iTargetid = world.getTileId(i, j, k);
        if(iTargetid == BlockListener.anchor.id || iTargetid == BlockListener.rope.id)
        {
            for(int tempj = j - 1; tempj >= 0; tempj--)
            {
                int iTempid = world.getTileId(i, tempj, k);
                if(ReplaceableBlockChecker.IsReplaceableBlock(world, i, tempj, k))
                {
                    if(world.setTile(i, tempj, k, BlockListener.rope.id))
                    {
                        BlockListener.rope.onBlockPlaced(world, i, tempj, k, iFacing);
                        BlockListener.rope.afterPlaced(world, i, tempj, k, entityplayer);
                        world.playSound((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, BlockListener.rope.sounds.getWalkSound(), (BlockListener.rope.sounds.getVolume() + 1.0F) / 2.0F, BlockListener.rope.sounds.getPitch() * 0.8F);
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
