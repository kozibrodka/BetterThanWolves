package net.kozibrodka.wolves.compat.ami.hopperpurifying;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeHandler;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.kozibrodka.wolves.wrappers.HopperPurifyingRecipeWrapperWrapper;
import org.jetbrains.annotations.NotNull;

public class HopperPurifyingRecipeHandler implements RecipeHandler<HopperPurifyingRecipeWrapperWrapper> {
    @NotNull
    @Override
    public Class<HopperPurifyingRecipeWrapperWrapper> getRecipeClass() {
        return HopperPurifyingRecipeWrapperWrapper.class;
    }

    @NotNull
    @Override
    public String getRecipeCategoryUid() {
        return "hopper_purifying";
    }

    @NotNull
    @Override
    public RecipeWrapper getRecipeWrapper(@NotNull HopperPurifyingRecipeWrapperWrapper recipe) {
        return new HopperPurifyingRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@NotNull HopperPurifyingRecipeWrapperWrapper recipe) {
        return true;
    }
}
