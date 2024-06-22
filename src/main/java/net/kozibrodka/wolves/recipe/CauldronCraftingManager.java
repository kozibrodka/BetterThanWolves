package net.kozibrodka.wolves.recipe;


import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;

public class CauldronCraftingManager extends MultiInputCraftingManager
{

    public static final CauldronCraftingManager getInstance()
    {
        return instance;
    }

    private CauldronCraftingManager()
    {
    }

    private static final CauldronCraftingManager instance = new CauldronCraftingManager();

    public List getDoubleRecipes()
    {
        ArrayList<ItemStack[]> recipeList = new ArrayList<>();

        for (int i = 0; i < recipes.size(); i++) {
            MultiInputRecipeHandler bulkRecipe = (MultiInputRecipeHandler) recipes.get(i);
            if (bulkRecipe.getNumberOfInputStacks() < 2) continue;
            ItemStack[] recipeArray = new ItemStack[3];
            recipeArray[0] = bulkRecipe.getOutputStack();
            recipeArray[1] = bulkRecipe.getInputStack(0);
            recipeArray[2] = bulkRecipe.getInputStack(1);
            recipeList.add(recipeArray);
        }

        return recipeList;
    }

    public List getSingleRecipes()
    {
        ArrayList<ItemStack[]> recipeList = new ArrayList<>();

        for (int i = 0; i < recipes.size(); i++) {
            MultiInputRecipeHandler bulkRecipe = (MultiInputRecipeHandler) recipes.get(i);
            if (bulkRecipe.getNumberOfInputStacks() != 1) continue;
            ItemStack[] recipeArray = new ItemStack[2];
            recipeArray[0] = bulkRecipe.getOutputStack();
            recipeArray[1] = bulkRecipe.getInputStack(0);
            recipeList.add(recipeArray);
        }

        return recipeList;
    }

}
