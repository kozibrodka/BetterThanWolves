package net.kozibrodka.wolves.itemblocks;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class CompanionCubeBlockItem extends BlockItem
{

    public CompanionCubeBlockItem(int i)
    {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
        setTranslationKey("companionCube");
    }

    public int method_470(int i) //getPlacedBlockMetadata
    {
        return i <= 0 ? 0 : 8;
    }

    public String getTranslationKey(ItemStack itemstack) //getItemNameIS
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
