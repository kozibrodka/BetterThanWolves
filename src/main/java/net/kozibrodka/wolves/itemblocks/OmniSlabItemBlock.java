package net.kozibrodka.wolves.itemblocks;

import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.item.Block;

public class OmniSlabItemBlock extends Block
{

    public OmniSlabItemBlock(int i)
    {
        super(i);
        setDurability(0);
        setHasSubItems(true);
        setTranslationKey("omniSlab");
    }

    public int getTexturePosition(int i)
    {
        return BlockListener.omniSlab.getTextureForSide(2, i);
    }

    public int getMetaData(int i)
    {
        return i;
    }
}
