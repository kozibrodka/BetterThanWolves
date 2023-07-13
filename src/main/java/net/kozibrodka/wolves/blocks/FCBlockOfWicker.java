package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.events.TextureListener;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

public class FCBlockOfWicker extends TemplateBlockBase {
    public FCBlockOfWicker(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    public int getTextureForSide(int side) {
        return TextureListener.hopper_wicker;
    }
}
