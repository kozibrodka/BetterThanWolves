package net.kozibrodka.wolves.compat.ami.millstone;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.kozibrodka.wolves.wrappers.MillStoneRecipeWrapperWrapper;
import org.jetbrains.annotations.NotNull;

public class MillStoneRecipeHandler implements RecipeHandler<MillStoneRecipeWrapperWrapper> {
    @NotNull
    @Override
    public Class<MillStoneRecipeWrapperWrapper> getRecipeClass() {
        return MillStoneRecipeWrapperWrapper.class;
    }

    @NotNull
    @Override
    public String getRecipeCategoryUid() {
        return "mill_stone";
    }

    @NotNull
    @Override
    public RecipeWrapper getRecipeWrapper(@NotNull MillStoneRecipeWrapperWrapper recipe) {
        return new MillStoneRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@NotNull MillStoneRecipeWrapperWrapper recipe) {
        return true;
    }
}
