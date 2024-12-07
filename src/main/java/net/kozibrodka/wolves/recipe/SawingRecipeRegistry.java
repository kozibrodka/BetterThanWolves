package net.kozibrodka.wolves.recipe;

import java.util.ArrayList;
import net.minecraft.item.ItemStack;

public class SawingRecipeRegistry {
    private static final SawingRecipeRegistry INSTANCE = new SawingRecipeRegistry();
    private ArrayList<ItemStack[]> recipes = new ArrayList<>();

    public static final SawingRecipeRegistry getInstance() {
        return INSTANCE;
    }

    public void addSawingRecipe(int input, ItemStack output) {
        this.recipes.add(new ItemStack[] {new ItemStack(input, 1, 0), output});
    }

    public void addSawingRecipe(ItemStack input, ItemStack output) {
        this.recipes.add(new ItemStack[] {input, output});
    }

    public ItemStack getResult(ItemStack item) {
        for (ItemStack[] items : recipes) {
            if (items[0].isItemEqual(item)) {

                return items[1];
            }
        }
        return null;
    }

    public ArrayList<SawRecipe> getRecipes() {
        ArrayList<SawRecipe> convertedRecipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        for (ItemStack[] recipe : recipes) {
            inputs.add(recipe[0]);
            outputs.add(recipe[1]);
        }
        for (int i = 0; i < inputs.size(); i++) {
            if (i >= outputs.size()) break;
            convertedRecipes.add(new SawRecipe(inputs.get(i), outputs.get(i)));
        }
        return convertedRecipes;
    }
}

