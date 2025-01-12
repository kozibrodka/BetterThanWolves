package net.kozibrodka.wolves.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

public class WickerWeavingItem extends LazyItemTemplate {
    public WickerWeavingItem(Identifier identifier) {
        super(identifier);
        this.setMaxDamage(256);
        this.setMaxCount(1);
    }

    @Override
    public ItemStack use(ItemStack stack, World world, PlayerEntity user) {
        if (stack.getDamage2() < 256) {
            stack.damage(1, user);
        }
        return stack;
    }
}
