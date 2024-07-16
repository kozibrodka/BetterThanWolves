package net.kozibrodka.wolves.block;

import net.kozibrodka.wolves.events.TextureListener;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class WickerBlock extends TemplateBlock {
    public WickerBlock(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    public int getTexture(int side) {
        return TextureListener.hopper_wicker;
    }
}
