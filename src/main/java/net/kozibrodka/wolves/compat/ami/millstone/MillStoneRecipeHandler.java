//package net.kozibrodka.wolves.compat.ami.millstone;
//
//import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeHandler;
//import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
//import net.kozibrodka.wolves.recipe.MillStoneRecipe;
//import org.jetbrains.annotations.NotNull;
//
//public class MillStoneRecipeHandler implements RecipeHandler<MillStoneRecipe> {
//    @NotNull
//    @Override
//    public Class<MillStoneRecipe> getRecipeClass() {
//        return MillStoneRecipe.class;
//    }
//
//    @NotNull
//    @Override
//    public String getRecipeCategoryUid() {
//        return "mill_stone";
//    }
//
//    @NotNull
//    @Override
//    public RecipeWrapper getRecipeWrapper(@NotNull MillStoneRecipe recipe) {
//        return new MillStoneRecipeWrapper(recipe);
//    }
//
//    @Override
//    public boolean isRecipeValid(@NotNull MillStoneRecipe recipe) {
//        return true;
//    }
//}
