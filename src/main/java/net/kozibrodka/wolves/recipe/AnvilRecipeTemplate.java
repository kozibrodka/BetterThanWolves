package net.kozibrodka.wolves.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;

public interface AnvilRecipeTemplate {
    boolean canCraft(CraftingInventory arg);

    ItemStack craft(CraftingInventory arg);

    int getIngredientCount();

    ItemStack getOutput();
}
