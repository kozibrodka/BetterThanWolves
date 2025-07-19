package net.kozibrodka.wolves.items;

import net.kozibrodka.wolves.events.ItemListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class DebugItem1 extends TemplateItem {

    public DebugItem1(Identifier identifier) {
        super(identifier);
        maxCount = 1;
    }

    public ItemStack use(ItemStack ItemInstance, World world, PlayerEntity entityplayer) {
        //TODO: DELETE
//        entityplayer.inventory.method_671(new ItemStack(ItemListener.refinedPickAxe, 1));
//        entityplayer.inventory.method_671(new ItemStack(ItemListener.refinedAxe, 1));
//        entityplayer.inventory.method_671(new ItemStack(ItemListener.refinedSword, 1));
        entityplayer.inventory.method_671(new ItemStack(ItemListener.refinedShovel, 1));
        entityplayer.inventory.method_671(new ItemStack(ItemListener.waterWheelItem, 1));
//        entityplayer.inventory.method_671(new ItemStack(ItemListener.windMillItem, 1));
//        entityplayer.inventory.method_671(new ItemStack(BED, 1));
//        entityplayer.inventory.method_671(new ItemStack(FLINT_AND_STEEL, 1));
        entityplayer.inventory.method_671(new ItemStack(GOLDEN_APPLE, 1));
//        entityplayer.inventory.method_671(new ItemStack(Item.DYE, 1, 1));
//        entityplayer.inventory.method_671(new ItemStack(Item.DYE, 1, 2));
//        entityplayer.inventory.method_671(new ItemStack(GOLDEN_APPLE, 1));
//        entityplayer.inventory.method_671(new ItemStack(BlockListener.axleBlock, 64));
        //TODO
        return ItemInstance;
    }
}
