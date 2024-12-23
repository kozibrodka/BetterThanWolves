// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCCraftingManagerCauldronStoked.java

package net.kozibrodka.wolves.recipe;

import net.kozibrodka.wolves.events.ItemListener;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StokedCauldronCraftingManager extends MultiInputCraftingManager
{

    public static final StokedCauldronCraftingManager getInstance()
    {
        return instance;
    }

    /**
     * This abomination adds a "nothing" item to the end of each recipe input list.
     * Important to distinguish stoked from non-stoked recipes.
     * @return Recipe with additional input item for distinction.
     */
    public List getAmiAdjustedRecipes() {
        List adjustedRecipes = new ArrayList();
        for (int i = 0; i < instance.getRecipes().size(); i++) {
            MultiInputRecipe recipe = (MultiInputRecipe) instance.getRecipes().get(i);
            List adaptedInputs = new ArrayList();
            int inputStackCount = recipe.getNumberOfInputStacks();
            for (int j = 0; j < inputStackCount; j++) {
                adaptedInputs.add(recipe.getInputStack(j).copy());
            }
            adaptedInputs.add(new ItemStack(ItemListener.nothing));
            MultiInputRecipe newRecipe = new MultiInputRecipe(recipe.getCopyOfOutputStack(), adaptedInputs);
            adjustedRecipes.add(newRecipe);
        }
        return adjustedRecipes;
    }

    private StokedCauldronCraftingManager() {
    }

    private static final StokedCauldronCraftingManager instance = new StokedCauldronCraftingManager();
}
