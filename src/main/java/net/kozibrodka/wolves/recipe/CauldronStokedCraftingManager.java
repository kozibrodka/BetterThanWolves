// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCCraftingManagerCauldronStoked.java

package net.kozibrodka.wolves.recipe;

import net.minecraft.item.ItemInstance;

import java.util.ArrayList;
import java.util.List;

public class CauldronStokedCraftingManager extends MultiInputCraftingManager
{

    public static final CauldronStokedCraftingManager getInstance()
    {
        return instance;
    }

    private CauldronStokedCraftingManager()
    {
    }

    private static final CauldronStokedCraftingManager instance = new CauldronStokedCraftingManager();

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
