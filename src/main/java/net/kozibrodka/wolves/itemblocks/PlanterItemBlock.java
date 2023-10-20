package net.kozibrodka.wolves.itemblocks;

import net.minecraft.item.Block;
import net.minecraft.item.ItemInstance;

public class PlanterItemBlock extends Block
{

    public PlanterItemBlock(int i)
    {
        super(i);
        setDurability(0);
        setHasSubItems(true);
        setTranslationKey("planter");
    }

    public int getMetaData(int i)
    {
        return i;
    }

    //EXTRA
    public String getTranslationKey(ItemInstance itemstack) //getItemNameIS
    {
        if(itemstack.getDamage() > 0)
        {
            return (new StringBuilder()).append(super.getTranslationKey()).append(".").append("soil").toString();
        } else
        {
            return (new StringBuilder()).append(super.getTranslationKey()).append(".").append("empty").toString();
        }
    }
}
