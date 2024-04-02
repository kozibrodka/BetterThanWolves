package net.kozibrodka.wolves.itemblocks;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class UnfiredPotteryBlockItem extends BlockItem
{

    public UnfiredPotteryBlockItem(int i)
    {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
        setTranslationKey("unfiredPottery");
    }

    public int method_470(int i)
    {
        return i;
    }

    //EXTRA
    public String getTranslationKey(ItemStack itemstack) //getItemNameIS
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

