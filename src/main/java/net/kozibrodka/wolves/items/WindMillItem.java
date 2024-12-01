
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

    public boolean useOnBlock(ItemStack itemStack, PlayerEntity playerEntity, World world, int x, int y, int z, int l) {
        int targetId = world.getBlockId(x, y, z);
        if(targetId == BlockListener.axleBlock.id && !world.isRemote) {
            int axisAlignment = ((AxleBlock)BlockListener.axleBlock).getAxisAlignment(world, x, y, z);
            if(axisAlignment != 0) {
                boolean aligned = axisAlignment == 2;
                if(WindMillEntity.validateArea(world, x, y, z, aligned)) {
                    WindMillEntity.placeCollisionBlocks(world, x, y, z, aligned);
                    world.spawnEntity(new WindMillEntity(world, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, aligned));
                    itemStack.count--;
                    return true;
                }
                playerEntity.method_490("Not enough room to place Wind Mill (They are absolutely HUGE!)");
            }
        }
        return false;
    }
}
