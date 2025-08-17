package net.kozibrodka.wolves.compat.ami.turntable;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.kozibrodka.wolves.wrappers.TurntableRecipeWrapperWrapper;
import org.jetbrains.annotations.NotNull;

public class TurntableRecipeHandler implements RecipeHandler<TurntableRecipeWrapperWrapper> {
    @NotNull
    @Override
    public Class<TurntableRecipeWrapperWrapper> getRecipeClass() {
        return TurntableRecipeWrapperWrapper.class;
    }

    @NotNull
    @Override
    public String getRecipeCategoryUid() {
        return "turntable";
    }

    @NotNull
    @Override
    public RecipeWrapper getRecipeWrapper(@NotNull TurntableRecipeWrapperWrapper recipe) {
        return new TurntableRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@NotNull TurntableRecipeWrapperWrapper recipe) {
        return true;
    }
}
