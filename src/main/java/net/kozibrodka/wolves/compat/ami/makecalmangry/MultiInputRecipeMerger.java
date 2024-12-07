package net.kozibrodka.wolves.compat.ami.makecalmangry;

import net.kozibrodka.wolves.recipe.CauldronCraftingManager;
import net.kozibrodka.wolves.recipe.MultiInputCraftingManager;
import net.kozibrodka.wolves.recipe.StokedCauldronCraftingManager;

import java.util.List;

public class MultiInputRecipeMerger extends MultiInputCraftingManager {

    public static final MultiInputRecipeMerger getInstance()
    {
        return instance;
    }

    private MultiInputRecipeMerger() {
    }

    private static final MultiInputRecipeMerger instance = new MultiInputRecipeMerger();

    @Override
    public List getRecipes() {
        List mergedList = CauldronCraftingManager.getInstance().getRecipes();
        List amiAdjustedRecipes = StokedCauldronCraftingManager.getInstance().getAmiAdjustedRecipes();
        for (int i = 0; i < amiAdjustedRecipes.size(); i++) {
            mergedList.add(amiAdjustedRecipes.get(i));
        }
        return CauldronCraftingManager.getInstance().getRecipes();
    }
}
