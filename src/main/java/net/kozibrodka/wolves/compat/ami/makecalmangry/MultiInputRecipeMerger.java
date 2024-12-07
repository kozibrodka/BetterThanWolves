package net.kozibrodka.wolves.compat.ami.makecalmangry;

import net.kozibrodka.wolves.recipe.CauldronCraftingManager;
import net.kozibrodka.wolves.recipe.CrucibleCraftingManager;
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
        List amiAdjustedCauldronRecipes = StokedCauldronCraftingManager.getInstance().getAmiAdjustedRecipes();
        for (int i = 0; i < amiAdjustedCauldronRecipes.size(); i++) {
            mergedList.add(amiAdjustedCauldronRecipes.get(i));
        }
        List amiAdjustedCrucibleRecipes = CrucibleCraftingManager.getInstance().getAmiAdjustedRecipes();
        for (int i = 0; i < amiAdjustedCrucibleRecipes.size(); i++) {
            mergedList.add(amiAdjustedCrucibleRecipes.get(i));
        }
        return CauldronCraftingManager.getInstance().getRecipes();
    }
}
