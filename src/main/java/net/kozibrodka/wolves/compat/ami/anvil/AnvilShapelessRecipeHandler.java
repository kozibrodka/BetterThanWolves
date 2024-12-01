package net.kozibrodka.wolves.compat.ami.anvil;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.kozibrodka.wolves.recipe.AnvilShapedRecipe;
import net.kozibrodka.wolves.recipe.AnvilShapelessRecipe;
import org.jetbrains.annotations.NotNull;

public class AnvilShapelessRecipeHandler implements RecipeHandler<AnvilShapelessRecipe> {
    @NotNull
    @Override
    public Class<AnvilShapelessRecipe> getRecipeClass() {
        return AnvilShapelessRecipe.class;
    }

    @NotNull
    @Override
    public String getRecipeCategoryUid() {
        return "anvil_shapeless";
    }

    @NotNull
    @Override
    public RecipeWrapper getRecipeWrapper(@NotNull AnvilShapelessRecipe recipe) {
        return new AnvilShapelessRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@NotNull AnvilShapelessRecipe recipe) {
        return true;
    }
}
