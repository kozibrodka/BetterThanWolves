package net.kozibrodka.wolves.recipe;

import net.kozibrodka.wolves.wrappers.SawRecipeWrapperWrapper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class SawingRecipeRegistry {
    private static final SawingRecipeRegistry INSTANCE = new SawingRecipeRegistry();
    private final ArrayList<ItemStack[]> recipes = new ArrayList<>();

    public static final SawingRecipeRegistry getInstance() {
        return INSTANCE;
    }

    public void addSawingRecipe(ItemStack input, ItemStack output) {
        this.recipes.add(new ItemStack[]{input, output});
    }

    public ItemStack getResult(ItemStack item) {
        for (ItemStack[] items : recipes) {
            if (items[0].isItemEqual(item)) {
                return items[1];
            }
        }
        return null;
    }

    public ArrayList<SawRecipeWrapperWrapper> getRecipes() {
        ArrayList<SawRecipeWrapperWrapper> convertedRecipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        for (ItemStack[] recipe : recipes) {
            inputs.add(recipe[0]);
            outputs.add(recipe[1]);
        }
        for (int i = 0; i < inputs.size(); i++) {
            if (i >= outputs.size()) break;
            convertedRecipes.add(new SawRecipeWrapperWrapper(inputs.get(i), outputs.get(i)));
        }
        return convertedRecipes;
    }
}

