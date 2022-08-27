
package net.kozibrodka.wolves.items;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.tool.TemplateBucket;


public class FCItemBucketCement extends TemplateBucket
{
    public FCItemBucketCement(Identifier iid, int iFullOfid)
    {
        super(iid, iFullOfid);
        setContainerItem(ItemBase.bucket);
    }

    public ItemInstance use(ItemInstance ItemInstance, Level world, PlayerBase entityplayer)
    {
        world.playSound(entityplayer, "mob.ghast.moan", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
        return super.use(ItemInstance, world, entityplayer);
    }
}
