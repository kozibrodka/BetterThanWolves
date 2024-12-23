package net.kozibrodka.wolves.recipe;

import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CrucibleCraftingManager extends MultiInputCraftingManager {

    public static final CrucibleCraftingManager getInstance()
    {
        return instance;
    }

    private CrucibleCraftingManager() {
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
            adaptedInputs.add(new ItemStack(BlockListener.collisionBlock));
            MultiInputRecipe newRecipe = new MultiInputRecipe(recipe.getCopyOfOutputStack(), adaptedInputs);
            adjustedRecipes.add(newRecipe);
        }
        return adjustedRecipes;
    }

    private static final CrucibleCraftingManager instance = new CrucibleCraftingManager();
}
