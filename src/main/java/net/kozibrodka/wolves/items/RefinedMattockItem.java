package net.kozibrodka.wolves.items;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.template.item.TemplatePickaxeItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class RefinedMattockItem extends TemplatePickaxeItem {

    private final float miningSpeed;

    public RefinedMattockItem(Identifier identifier, ToolMaterial material) {
        super(identifier, material);
        this.setMaxDamage(material.getDurability());
        this.miningSpeed = material.getMiningSpeedMultiplier();
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, Block block) {
        if (block.getDefaultState().isIn(TagKey.of(BlockRegistry.INSTANCE.getKey(), Identifier.of("mineable/pickaxe")))
                || block.getDefaultState().isIn(TagKey.of(BlockRegistry.INSTANCE.getKey(), Identifier.of("mineable/shovel")))) {
            return this.miningSpeed;
        }
        return 1.0F;
    }

    @Override
    public int getAttackDamage(Entity attackedEntity) {
        return 5;
    }

}
