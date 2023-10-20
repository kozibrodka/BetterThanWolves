package net.kozibrodka.wolves.itemblocks;

import net.minecraft.item.Block;
import net.minecraft.item.ItemInstance;

public class CompanionCubeItemBlock extends Block
{

    public CompanionCubeItemBlock(int i)
    {
        super(i);
        setDurability(0);
        setHasSubItems(true);
        setTranslationKey("fcCompanionCube");
    }

    public int getMetaData(int i) //getPlacedBlockMetadata
    {
        return i <= 0 ? 0 : 8;
    }

    public String getTranslationKey(ItemInstance itemstack) //getItemNameIS
    {
        if(itemstack.getDamage() > 0)
        {
            return (new StringBuilder()).append(super.getTranslationKey()).append(".").append("slab").toString();
        } else
        {
            return (new StringBuilder()).append(super.getTranslationKey()).append(".").append("cube").toString();
        }
    }
}
