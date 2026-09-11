package net.kozibrodka.wolves.items;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.template.item.TemplateToolItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class RefinedMattockItem extends TemplateToolItem {
    private final ToolMaterial toolMaterial;

    public RefinedMattockItem(Identifier identifier, ToolMaterial material) {
        super(identifier, 2, material, Block.BLOCKS);
        this.setMaxDamage(material.getDurability());
        toolMaterial = material;
    }

    @Override
    public boolean isSuitableFor(Block block) {
        BlockState blockState = block.getDefaultState();
        TagKey<Block> diamondToolNeeded = TagKey.of(BlockRegistry.INSTANCE.getKey(), Identifier.of("minecraft:needs_diamond_tool"));
        TagKey<Block> ironToolNeeded = TagKey.of(BlockRegistry.INSTANCE.getKey(), Identifier.of("minecraft:needs_iron_tool"));
        TagKey<Block> stoneToolNeeded = TagKey.of(BlockRegistry.INSTANCE.getKey(), Identifier.of("minecraft:needs_stone_tool"));
        switch(toolMaterial) {
            case WOOD:
                if (
                        blockState.isIn(diamondToolNeeded) ||
                                blockState.isIn(ironToolNeeded) ||
                                blockState.isIn(stoneToolNeeded)
                ) return false;
            case STONE:
            case GOLD:
                if (
                        blockState.isIn(diamondToolNeeded) ||
                                blockState.isIn(ironToolNeeded)
                ) return false;
            case IRON:
                if (
                        blockState.isIn(diamondToolNeeded)
                ) return false;
            default:
        }
        return true;
    }


    @Override
    public int getAttackDamage(Entity attackedEntity) {
        return 5;
    }

}
