package net.kozibrodka.wolves.items;

import net.kozibrodka.wolves.block.AxleBlock;
import net.kozibrodka.wolves.entity.FCEntityTEST;
import net.kozibrodka.wolves.entity.FCEntityTest2;
import net.kozibrodka.wolves.entity.WaterWheelEntity;
import net.kozibrodka.wolves.events.BlockListener;
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

//    public ItemStack use(ItemStack ItemInstance, World world, PlayerEntity entityplayer) {
////        entityplayer.inventory.method_671(new ItemStack(ItemListener.refinedPickAxe, 1));
////        entityplayer.inventory.method_671(new ItemStack(ItemListener.refinedAxe, 1));
////        entityplayer.inventory.method_671(new ItemStack(ItemListener.refinedSword, 1));
//        entityplayer.inventory.addStack(new ItemStack(ItemListener.refinedShovel, 1));
//        entityplayer.inventory.addStack(new ItemStack(ItemListener.waterWheelItem, 1));
////        entityplayer.inventory.method_671(new ItemStack(ItemListener.windMillItem, 1));
////        entityplayer.inventory.method_671(new ItemStack(BED, 1));
////        entityplayer.inventory.method_671(new ItemStack(FLINT_AND_STEEL, 1));
//        entityplayer.inventory.addStack(new ItemStack(GOLDEN_APPLE, 1));
////        entityplayer.inventory.method_671(new ItemStack(Item.DYE, 1, 1));
////        entityplayer.inventory.method_671(new ItemStack(Item.DYE, 1, 2));
////        entityplayer.inventory.method_671(new ItemStack(GOLDEN_APPLE, 1));
////        entityplayer.inventory.method_671(new ItemStack(BlockListener.axleBlock, 64));
//        return ItemInstance;
//    }

    @Override
    public boolean useOnBlock(ItemStack itemStack, PlayerEntity playerEntity, World world, int x, int y, int z, int l) {
        int blockId = world.getBlockId(x, y, z);
        if (blockId == BlockListener.axleBlock.id && !world.isRemote) {
            int axisAlignment = ((AxleBlock) BlockListener.axleBlock).getAxisAlignment(world, x, y, z);
            if (axisAlignment != 0) {
                boolean aligned = axisAlignment == 2;
                if (WaterWheelEntity.validateArea(world, x, y, z, aligned)) {
                    world.spawnEntity(new FCEntityTest2(world, (float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, true));
                    itemStack.count--;
                    return true;
                }
                playerEntity.sendMessage("Not enough room to place Water Wheel");
            }
        }
        return false;
    }
}
