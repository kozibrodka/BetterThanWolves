package net.kozibrodka.wolves.itemblocks;

import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.item.BlockItem;

public class OmniSlabBlockItem extends BlockItem {

    public OmniSlabBlockItem(int i) {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
        setTranslationKey("omniSlab");
    }

    @Override
    public int getTextureId(int i) {
        return BlockListener.omniSlab.getTexture(2, i);
    }

    @Override
    public int getPlacementMetadata(int i) {
        return i;
    }
}
