package net.kozibrodka.wolves.utils;

import net.minecraft.inventory.Crafting;
import net.minecraft.item.ItemInstance;

public interface FCRecipeAnvil {
    boolean canCraft(Crafting arg);

    ItemInstance craft(Crafting arg);

    int getIngredientCount();

    ItemInstance getOutput();
}
