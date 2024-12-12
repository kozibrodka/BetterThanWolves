//package net.kozibrodka.wolves.compat.ami.anvil;
//
//import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
//import net.kozibrodka.wolves.recipe.AnvilShapedRecipe;
//import net.minecraft.client.Minecraft;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AnvilShapedRecipeWrapper implements RecipeWrapper {
//    private final AnvilShapedRecipe recipe;
//
//    public AnvilShapedRecipeWrapper(AnvilShapedRecipe recipe) {
//        this.recipe = recipe;
//    }
//
//    @Override
//    public List<?> getInputs() {
//        return List.of(recipe.getIngredients());
//    }
//
//    @Override
//    public List<?> getOutputs() {
//        return List.of(recipe.getOutput());
//    }
//
//    @Override
//    public void drawInfo(@NotNull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
//
//    }
//
//    @Override
//    public void drawAnimations(@NotNull Minecraft minecraft, int recipeWidth, int recipeHeight) {
//
//    }
//
//    @Nullable
//    @Override
//    public ArrayList<Object> getTooltip(int mouseX, int mouseY) {
//        return null;
//    }
//
//    @Override
//    public boolean handleClick(@NotNull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
//        return false;
//    }
//}
