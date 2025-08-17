package net.kozibrodka.wolves.compat.ami.sawmill;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.kozibrodka.wolves.wrappers.SawRecipeWrapperWrapper;
import org.jetbrains.annotations.NotNull;

public class SawRecipeHandler implements RecipeHandler<SawRecipeWrapperWrapper> {
    @NotNull
    @Override
    public Class<SawRecipeWrapperWrapper> getRecipeClass() {
        return SawRecipeWrapperWrapper.class;
    }

    @NotNull
    @Override
    public String getRecipeCategoryUid() {
        return "saw";
    }

    @NotNull
    @Override
    public RecipeWrapper getRecipeWrapper(@NotNull SawRecipeWrapperWrapper recipe) {
        return new SawRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@NotNull SawRecipeWrapperWrapper recipe) {
        return true;
    }
}
