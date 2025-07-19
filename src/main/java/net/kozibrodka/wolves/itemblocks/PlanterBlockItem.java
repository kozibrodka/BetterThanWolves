package net.kozibrodka.wolves.itemblocks;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class PlanterBlockItem extends BlockItem {

    public PlanterBlockItem(int i) {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
        setTranslationKey("planter");
    }

    @Override
    public int getPlacementMetadata(int i) {
        return i;
    }

    //EXTRA
    @Override
    public String getTranslationKey(ItemStack itemstack) //getItemNameIS
    {
        if (itemstack.getDamage() > 0) {
            return super.getTranslationKey() + "." + "soil";
        } else {
            return super.getTranslationKey() + "." + "empty";
        }
    }
}
