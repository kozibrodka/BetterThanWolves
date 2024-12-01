package net.kozibrodka.wolves.compat.ami.cauldron;

import net.glasslauncher.mods.alwaysmoreitems.api.gui.AMIDrawable;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.GuiItemStackGroup;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.RecipeLayout;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeCategory;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.gui.DrawableHelper;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class CauldronRecipeCategory implements RecipeCategory {

    @NotNull
    private final AMIDrawable background = DrawableHelper.createDrawable("/assets/wolves/stationapi/gui/ami_tabs/cauldron.png", 8, 8, 160, 100);

    @NotNull
    @Override
    public String getUid() {
        return "cauldron";
    }

    @NotNull
    @Override
    public String getTitle() {
        return "Cauldron";
    }

    @NotNull
    @Override
    public AMIDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
    }

    @Override
    public void drawAnimations(Minecraft minecraft) {

    }

    @Override
    public void setRecipe(@NotNull RecipeLayout recipeLayout, @NotNull RecipeWrapper recipeWrapper) {
        GuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        int xOffset = 3;
        int yOffset = 8;
        for (int i = 0; i < recipeWrapper.getInputs().size(); i++) {
            guiItemStacks.init(i, true, xOffset + (i % 9) * 18, yOffset + (i / 9) * 18);
        }
        for (int i = 0; i < recipeWrapper.getInputs().size(); i++) {
            guiItemStacks.setFromRecipe(i, recipeWrapper.getInputs().get(i));
        }
        guiItemStacks.init(27, false, 130 + xOffset, yOffset + 36);
        guiItemStacks.setFromRecipe(27, recipeWrapper.getOutputs().get(0));
    }
}
