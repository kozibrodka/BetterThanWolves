package net.kozibrodka.wolves.itemblocks;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class CompanionCubeBlockItem extends BlockItem {

    public CompanionCubeBlockItem(int i) {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
        setTranslationKey("companionCube");
    }

    @Override
    public int getPlacementMetadata(int i) {
        return i <= 0 ? 0 : 8;
    }

    @Override
    public String getTranslationKey(ItemStack itemstack) //getItemNameIS
    {
        if (itemstack.getDamage() > 0) {
            return super.getTranslationKey() + "." + "slab";
        } else {
            return super.getTranslationKey() + "." + "cube";
        }
    }
}
