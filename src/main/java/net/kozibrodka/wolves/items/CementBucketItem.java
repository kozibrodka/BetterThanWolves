
package net.kozibrodka.wolves.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateBucketItem;
import net.modificationstation.stationapi.api.util.Identifier;



public class CementBucketItem extends TemplateBucketItem
{
    public CementBucketItem(Identifier iid, int iFullOfid)
    {
        super(iid, iFullOfid);
        setCraftingReturnItem(Item.BUCKET);
    }

    public ItemStack use(ItemStack ItemInstance, World world, PlayerEntity entityplayer)
    {
        world.playSound(entityplayer, "mob.ghast.moan", 0.5F, 2.6F + (world.field_214.nextFloat() - world.field_214.nextFloat()) * 0.8F);
        return super.use(ItemInstance, world, entityplayer);
    }
}
