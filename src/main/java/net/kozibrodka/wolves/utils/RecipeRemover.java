package net.kozibrodka.wolves.utils;

import net.minecraft.item.ItemBase;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeRegistry;

import java.util.List;

public class RecipeRemover {
    @SuppressWarnings({"unchecked"})
    public static void removeRecipe(ItemBase item, int meta, boolean onlyRemoveFirst) {
        List<Recipe> recipes = RecipeRegistry.getInstance().getRecipes();
        for (int i = 0; i < recipes.size(); i++) {
            Recipe recipe = recipes.get(i);
            if (recipe.getOutput().itemId == item.id) {
                //noinspection SimplifiableConditionalExpression
                if ((meta == -1) ? true : (recipe.getOutput().getDamage() == meta)) {
                    recipes.remove(i);
                    i--;
                    if (onlyRemoveFirst) {
                        return;
                    }
                }
            }
        }
    }

    public static void removeRecipe(ItemBase item, boolean onlyRemoveFirst) {
        removeRecipe(item, -1, onlyRemoveFirst);
    }

    public static void removeRecipe(ItemBase item) {
        removeRecipe(item, -1, false);
    }
}
