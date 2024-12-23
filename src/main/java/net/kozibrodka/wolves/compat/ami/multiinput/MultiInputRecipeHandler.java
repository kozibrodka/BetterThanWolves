package net.kozibrodka.wolves.compat.ami.multiinput;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.kozibrodka.wolves.recipe.MultiInputRecipe;
import org.jetbrains.annotations.NotNull;

public class MultiInputRecipeHandler implements RecipeHandler<MultiInputRecipe> {

    @NotNull
    @Override
    public Class<MultiInputRecipe> getRecipeClass() {
        return MultiInputRecipe.class;
    }

    @NotNull
    @Override
    public String getRecipeCategoryUid() {
        return "multi_input";
    }

    @NotNull
    @Override
    public RecipeWrapper getRecipeWrapper(@NotNull MultiInputRecipe recipe) {
        return new MultiInputRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@NotNull MultiInputRecipe recipe) {
        return true;
    }
}