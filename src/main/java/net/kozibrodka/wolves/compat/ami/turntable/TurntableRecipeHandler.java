package net.kozibrodka.wolves.compat.ami.turntable;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.kozibrodka.wolves.recipe.TurntableRecipe;
import net.kozibrodka.wolves.wrappers.TurntableRecipeWrapperWrapper;
import org.jetbrains.annotations.NotNull;

public class TurntableRecipeHandler implements RecipeHandler<TurntableRecipe> {
    @NotNull
    @Override
    public Class<TurntableRecipe> getRecipeClass() {
        return TurntableRecipe.class;
    }

    @NotNull
    @Override
    public String getRecipeCategoryUid() {
        return "turntable";
    }

    @NotNull
    @Override
    public RecipeWrapper getRecipeWrapper(@NotNull TurntableRecipe recipe) {
        return new TurntableRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@NotNull TurntableRecipe recipe) {
        return true;
    }
}
