package net.kozibrodka.wolves.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateAxeItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class RefinedAxeItem extends TemplateAxeItem {
    public RefinedAxeItem(Identifier identifier, ToolMaterial material) {
        super(identifier, material);
    }

    @Override
    public int getAttackDamage(Entity attackedEntity) {
        return 6;
    }
}
