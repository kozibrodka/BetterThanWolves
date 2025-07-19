package net.kozibrodka.wolves.items;

import net.kozibrodka.wolves.block.AxleBlock;
import net.kozibrodka.wolves.entity.FCEntityTEST;
import net.kozibrodka.wolves.entity.WaterWheelEntity;
import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class DebugItem2 extends TemplateItem {
    public DebugItem2(Identifier identifier) {
        super(identifier);
        maxCount = 1;
    }

    public boolean useOnBlock(ItemStack itemStack, PlayerEntity playerEntity, World world, int x, int y, int z, int l) {
        int blockId = world.getBlockId(x, y, z);
        if (blockId == BlockListener.axleBlock.id && !world.isRemote) {
            int axisAlignment = ((AxleBlock) BlockListener.axleBlock).getAxisAlignment(world, x, y, z);
            if (axisAlignment != 0) {
                boolean aligned = axisAlignment == 2;
                if (WaterWheelEntity.validateArea(world, x, y, z, aligned)) {
//                    WaterWheelEntity.placeCollisionBlocks(world, x, y, z, aligned);
//                    world.spawnEntity(new WaterWheelEntity(world, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, aligned));
                    world.spawnEntity(new FCEntityTEST(world, (float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, 7));
                    itemStack.count--;
                    return true;
                }
                playerEntity.method_490("Not enough room to place Water Wheel");
            }
        }
        return false;
    }
}
