package net.kozibrodka.wolves.recipe;

import net.minecraft.item.ItemInstance;

import java.util.ArrayList;
import java.util.List;

public class CraftingManagerCrucible extends FCCraftingManagerBulk {

    public static final CraftingManagerCrucible getInstance()
    {
        return instance;
    }

    private CraftingManagerCrucible()
    {
    }

    private static final CraftingManagerCrucible instance = new CraftingManagerCrucible();

    public List getTripleRecipes()
    {
        ArrayList<ItemInstance[]> recipeList = new ArrayList<>();

        for (int i = 0; i < m_recipes.size(); i++) {
            FCCraftingManagerBulkRecipe bulkRecipe = (FCCraftingManagerBulkRecipe) m_recipes.get(i);
            if (bulkRecipe.getNumberOfInputStacks() != 3) continue;
            ItemInstance[] recipeArray = new ItemInstance[4];
            recipeArray[0] = bulkRecipe.getOutputStack();
            recipeArray[1] = bulkRecipe.getInputStack(0);
            recipeArray[2] = bulkRecipe.getInputStack(1);
            recipeArray[3] = bulkRecipe.getInputStack(2);
            recipeList.add(recipeArray);
        }

        return recipeList;
    }
}
