package net.kozibrodka.wolves.itemblocks;

import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.item.BlockItem;

public class OmniSlabBlockItem extends BlockItem
{

    public OmniSlabBlockItem(int i)
    {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
        setTranslationKey("omniSlab");
    }

    public int getTextureId(int i)
    {
        return BlockListener.omniSlab.getTexture(2, i);
    }

    public int method_470(int i)
    {
        return i;
    }
}
