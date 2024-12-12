//package net.kozibrodka.wolves.compat.ami.anvil;
//
//import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeHandler;
//import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
//import net.kozibrodka.wolves.recipe.AnvilShapedRecipe;
//import org.jetbrains.annotations.NotNull;
//
//public class AnvilShapedRecipeHandler implements RecipeHandler<AnvilShapedRecipe> {
//    @NotNull
//    @Override
//    public Class<AnvilShapedRecipe> getRecipeClass() {
//        return AnvilShapedRecipe.class;
//    }
//
//    @NotNull
//    @Override
//    public String getRecipeCategoryUid() {
//        return "anvil_shaped";
//    }
//
//    @NotNull
//    @Override
//    public RecipeWrapper getRecipeWrapper(@NotNull AnvilShapedRecipe recipe) {
//        return new AnvilShapedRecipeWrapper(recipe);
//    }
//
//    @Override
//    public boolean isRecipeValid(@NotNull AnvilShapedRecipe recipe) {
//        return true;
//    }
//}
