package net.kozibrodka.wolves.itemblocks;

import net.minecraft.item.Block;
import net.minecraft.item.ItemInstance;

public class UnfiredPotteryItemBlock extends Block
{

    public UnfiredPotteryItemBlock(int i)
    {
        super(i);
        setDurability(0);
        setHasSubItems(true);
        setTranslationKey("fcUnfiredPottery");
    }

    public int getMetaData(int i)
    {
        return i;
    }

    //EXTRA
    public String getTranslationKey(ItemInstance itemstack) //getItemNameIS
    {
        if(itemstack.getDamage() == 0)
        {
            return (new StringBuilder()).append(super.getTranslationKey()).append(".").append("crucible").toString();
        } else if(itemstack.getDamage() == 1)
        {
            return (new StringBuilder()).append(super.getTranslationKey()).append(".").append("planter").toString();
        }else{
            return (new StringBuilder()).append(super.getTranslationKey()).append(".").append("vase").toString();
        }
    }
}

