package net.kozibrodka.wolves.recipe;


import net.minecraft.item.ItemInstance;

import java.util.ArrayList;
import java.util.List;

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
        ArrayList<ItemInstance[]> recipeList = new ArrayList<>();

        for (int i = 0; i < m_recipes.size(); i++) {
            MultiInputRecipeHandler bulkRecipe = (MultiInputRecipeHandler) m_recipes.get(i);
            if (bulkRecipe.getNumberOfInputStacks() < 2) continue;
            ItemInstance[] recipeArray = new ItemInstance[3];
            recipeArray[0] = bulkRecipe.getOutputStack();
            recipeArray[1] = bulkRecipe.getInputStack(0);
            recipeArray[2] = bulkRecipe.getInputStack(1);
            recipeList.add(recipeArray);
        }

        return recipeList;
    }

    public List getSingleRecipes()
    {
        ArrayList<ItemInstance[]> recipeList = new ArrayList<>();

        for (int i = 0; i < m_recipes.size(); i++) {
            MultiInputRecipeHandler bulkRecipe = (MultiInputRecipeHandler) m_recipes.get(i);
            if (bulkRecipe.getNumberOfInputStacks() != 1) continue;
            ItemInstance[] recipeArray = new ItemInstance[2];
            recipeArray[0] = bulkRecipe.getOutputStack();
            recipeArray[1] = bulkRecipe.getInputStack(0);
            recipeList.add(recipeArray);
        }

        return recipeList;
    }

}
