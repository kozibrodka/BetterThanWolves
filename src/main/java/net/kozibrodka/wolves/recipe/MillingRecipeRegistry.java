package net.kozibrodka.wolves.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.item.ItemStack;

public class MillingRecipeRegistry {
    private static final MillingRecipeRegistry INSTANCE = new MillingRecipeRegistry();
    private Map recipes = new HashMap();

    public static final MillingRecipeRegistry getInstance() {
        return INSTANCE;
    }

    public void addMillingRecipe(int i, ItemStack arg) {
        this.recipes.put(i, arg);
    }

    public ItemStack getResult(int i) {
        return (ItemStack)this.recipes.get(i);
    }

    // This is not a clean solution, but it should work fine
    /*
    public ArrayList<ItemStack[]> getRecipes() {
        ArrayList<ItemStack[]> itemInstances = new ArrayList<>();
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
            itemInstances.add(new ItemStack[] {inputs.get(i), outputs.get(i)});
        }
        return itemInstances;
    }

     */

    public ArrayList<MillStoneRecipe> getRecipes() {
        ArrayList<MillStoneRecipe> convertedRecipes = new ArrayList<>();
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
            convertedRecipes.add(new MillStoneRecipe(inputs.get(i), outputs.get(i)));
        }
        return convertedRecipes;
    }
}
