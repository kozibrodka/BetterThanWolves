
package net.kozibrodka.wolves.items;

import net.kozibrodka.wolves.entity.BroadheadArrowEntity;
import net.kozibrodka.wolves.events.ItemListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItem;

public class CompositeBowItem extends TemplateItem

{
    public CompositeBowItem(Identifier i)
    {
        super(i);
        maxCount = 1;
    }

    public ItemStack use(ItemStack ItemInstance, World world, PlayerEntity entityplayer)
    {
        if(entityplayer.inventory.method_676(ItemListener.broadHeadArrow.id))
        {
            world.playSound(entityplayer, "random.bow", 1.0F, 1.0F / (random.nextFloat() * 0.4F + 0.8F));
            if(!world.isRemote)
            {
                BroadheadArrowEntity arrow = new BroadheadArrowEntity(world, entityplayer);
                arrow.velocityX *= 1.5D;
                arrow.velocityY *= 1.5D;
                arrow.velocityZ *= 1.5D;
                world.spawnEntity(arrow);
            }
        } else
        if(entityplayer.inventory.method_676(Item.ARROW.id))
        {
            world.playSound(entityplayer, "random.bow", 1.0F, 1.0F / (random.nextFloat() * 0.4F + 0.8F));
            if(!world.isRemote)
            {
                ArrowEntity arrow = new ArrowEntity(world, entityplayer);
                arrow.velocityX *= 1.5D;
                arrow.velocityY *= 1.5D;
                arrow.velocityZ *= 1.5D;
                world.spawnEntity(arrow);
            }
        }
        return ItemInstance;
    }

}
