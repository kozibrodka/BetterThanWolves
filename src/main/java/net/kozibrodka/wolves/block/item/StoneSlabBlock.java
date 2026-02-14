package net.kozibrodka.wolves.block.item;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class StoneSlabBlock extends TemplateBlock {
    public StoneSlabBlock(Identifier identifier, Material material) {
        super(identifier, material);
        setHardness(2.0F);
        setResistance(10.0F);
        setSoundGroup(STONE_SOUND_GROUP);
    }

    public int getTexture(int side, int meta) {
        return side <= 1 ? 6 : 5;
    }

}
