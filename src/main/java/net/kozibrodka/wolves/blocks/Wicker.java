package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.events.TextureListener;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class Wicker extends TemplateBlock {
    public Wicker(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    public int getTextureForSide(int side) {
        return TextureListener.hopper_wicker;
    }
}
