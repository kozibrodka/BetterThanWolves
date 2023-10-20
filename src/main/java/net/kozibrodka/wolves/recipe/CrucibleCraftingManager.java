package net.kozibrodka.wolves.recipe;

import net.minecraft.item.ItemInstance;

import java.util.ArrayList;
import java.util.List;

public class CrucibleCraftingManager extends MultiInputCraftingManager {

    public static final CrucibleCraftingManager getInstance()
    {
        return instance;
    }

    private CrucibleCraftingManager()
    {
    }

    private static final CrucibleCraftingManager instance = new CrucibleCraftingManager();

    public List getTripleRecipes()
    {
        ArrayList<ItemInstance[]> recipeList = new ArrayList<>();

        for (int i = 0; i < m_recipes.size(); i++) {
            MultiInputRecipeHandler bulkRecipe = (MultiInputRecipeHandler) m_recipes.get(i);
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
