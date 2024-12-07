package net.kozibrodka.wolves.compat.ami.multiinput;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.kozibrodka.wolves.recipe.MultiInputRecipeHandler;
import org.jetbrains.annotations.NotNull;

public class CauldronRecipeHandler implements RecipeHandler<MultiInputRecipeHandler> {

    @NotNull
    @Override
    public Class<MultiInputRecipeHandler> getRecipeClass() {
        return MultiInputRecipeHandler.class;
    }

    @NotNull
    @Override
    public String getRecipeCategoryUid() {
        return "cauldron";
    }

    @NotNull
    @Override
    public RecipeWrapper getRecipeWrapper(@NotNull MultiInputRecipeHandler recipe) {
        return new CauldronRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@NotNull MultiInputRecipeHandler recipe) {
        return true;
    }
}