//package net.kozibrodka.wolves.compat.ami.sawmill;
//
//import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeHandler;
//import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
//import net.kozibrodka.wolves.recipe.SawRecipe;
//import org.jetbrains.annotations.NotNull;
//
//public class SawRecipeHandler implements RecipeHandler<SawRecipe> {
//    @NotNull
//    @Override
//    public Class<SawRecipe> getRecipeClass() {
//        return SawRecipe.class;
//    }
//
//    @NotNull
//    @Override
//    public String getRecipeCategoryUid() {
//        return "saw";
//    }
//
//    @NotNull
//    @Override
//    public RecipeWrapper getRecipeWrapper(@NotNull SawRecipe recipe) {
//        return new SawRecipeWrapper(recipe);
//    }
//
//    @Override
//    public boolean isRecipeValid(@NotNull SawRecipe recipe) {
//        return true;
//    }
//}
