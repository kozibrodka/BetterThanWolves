package net.kozibrodka.wolves.recipe;

import net.minecraft.item.ItemInstance;

import java.util.HashMap;
import java.util.Map;

public class MillingRecipeRegistry {
    private static final MillingRecipeRegistry INSTANCE = new MillingRecipeRegistry();
    private Map recipes = new HashMap();

    public static final MillingRecipeRegistry getInstance() {
        return INSTANCE;
    }

    public void addMillingRecipe(int i, ItemInstance arg) {
        this.recipes.put(i, arg);
    }

    public ItemInstance getResult(int i) {
        return (ItemInstance)this.recipes.get(i);
    }

    public Map getRecipes() {
        return this.recipes;
    }
}
