package net.kozibrodka.wolves.itemblocks;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class PlanterBlockItem extends BlockItem
{

    public PlanterBlockItem(int i)
    {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
        setTranslationKey("planter");
    }

    @Override
    public int getPlacementMetadata(int i)
    {
        return i;
    }

    //EXTRA
    @Override
    public String getTranslationKey(ItemStack itemstack) //getItemNameIS
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
