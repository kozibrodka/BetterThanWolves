package net.kozibrodka.wolves.items;

import net.kozibrodka.wolves.entity.SteelMinecartEntity;
import net.minecraft.block.RailBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class SteelMinecartItem extends TemplateItem {

    public SteelMinecartItem(Identifier identifier) {
        super(identifier);
        this.maxCount = 1;
    }

    public boolean useOnBlock(ItemStack stack, PlayerEntity user, World world, int x, int y, int z, int side) {
        int var8 = world.getBlockId(x, y, z);
        if (RailBlock.isRail(var8)) {
            if (!world.isRemote) {
                world.spawnEntity(new SteelMinecartEntity(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), 0));
            }

            --stack.count;
            return true;
        } else {
            return false;
        }
    }
}
