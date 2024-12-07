package net.kozibrodka.wolves.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.item.ItemStack;

public class HopperPurifyingRecipeRegistry {
    private static final HopperPurifyingRecipeRegistry INSTANCE = new HopperPurifyingRecipeRegistry();
    private Map recipes = new HashMap();

    public static final HopperPurifyingRecipeRegistry getInstance() {
        return INSTANCE;
    }

    public void addHopperHauntingRecipe(int i, ItemStack arg) {
        this.recipes.put(i, arg);
    }

    public ItemStack getResult(int i) {
        return (ItemStack)this.recipes.get(i);
    }

    public ArrayList<HopperPurifyingRecipe> getRecipes() {
        ArrayList<HopperPurifyingRecipe> convertedRecipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        for (Object obj : recipes.keySet()) {
            if (obj instanceof Integer) {
                inputs.add(new ItemStack((Integer) obj, 1, 0));
                outputs.add(getResult((Integer) obj));
            }
        }
        for (int i = 0; i < inputs.size(); i++) {
            if (i >= outputs.size()) break;
            convertedRecipes.add(new HopperPurifyingRecipe(inputs.get(i), outputs.get(i)));
        }
        return convertedRecipes;
    }
}

