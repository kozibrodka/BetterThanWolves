package net.kozibrodka.wolves.itemblocks;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class UnfiredPotteryBlockItem extends BlockItem {

    public UnfiredPotteryBlockItem(int i) {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
        setTranslationKey("unfiredPottery");
    }

    @Override
    public int getPlacementMetadata(int i) {
        return i;
    }

    //EXTRA
    @Override
    public String getTranslationKey(ItemStack itemstack) //getItemNameIS
    {
        if (itemstack.getDamage() == 0) {
            return super.getTranslationKey() + "." + "crucible";
        } else if (itemstack.getDamage() == 1) {
            return super.getTranslationKey() + "." + "planter";
        } else {
            return super.getTranslationKey() + "." + "vase";
        }
    }
}

