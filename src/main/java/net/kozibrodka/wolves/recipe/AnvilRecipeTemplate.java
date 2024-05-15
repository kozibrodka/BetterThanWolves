package net.kozibrodka.wolves.recipe;

import net.minecraft.inventory.Crafting;
import net.minecraft.item.ItemInstance;

public interface AnvilRecipeTemplate {
    boolean canCraft(Crafting arg);

    ItemInstance craft(Crafting arg);

    int getIngredientCount();

    ItemInstance getOutput();
}
