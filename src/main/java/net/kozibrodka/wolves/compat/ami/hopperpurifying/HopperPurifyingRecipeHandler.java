//package net.kozibrodka.wolves.compat.ami.hopperpurifying;
//
//import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeHandler;
//import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
//import net.kozibrodka.wolves.recipe.HopperPurifyingRecipe;
//import org.jetbrains.annotations.NotNull;
//
//public class HopperPurifyingRecipeHandler implements RecipeHandler<HopperPurifyingRecipe> {
//    @NotNull
//    @Override
//    public Class<HopperPurifyingRecipe> getRecipeClass() {
//        return HopperPurifyingRecipe.class;
//    }
//
//    @NotNull
//    @Override
//    public String getRecipeCategoryUid() {
//        return "hopper_purifying";
//    }
//
//    @NotNull
//    @Override
//    public RecipeWrapper getRecipeWrapper(@NotNull HopperPurifyingRecipe recipe) {
//        return new HopperPurifyingRecipeWrapper(recipe);
//    }
//
//    @Override
//    public boolean isRecipeValid(@NotNull HopperPurifyingRecipe recipe) {
//        return true;
//    }
//}
