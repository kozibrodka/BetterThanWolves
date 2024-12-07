package net.kozibrodka.wolves.compat.ami.multiinput;

import net.glasslauncher.mods.alwaysmoreitems.api.gui.AMIDrawable;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.GuiItemStackGroup;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.RecipeLayout;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeCategory;
import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.gui.DrawableHelper;
import net.kozibrodka.wolves.events.ItemListener;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CauldronRecipeCategory implements RecipeCategory {

    @NotNull
    private final AMIDrawable background = DrawableHelper.createDrawable("/assets/wolves/stationapi/gui/ami_tabs/cauldron.png", 7, 14, 162, 100);
    private boolean stoked = false;

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
        if (stoked) {
            minecraft.textRenderer.draw("Must be", 0, 0, 0x884444);
            minecraft.textRenderer.draw("stoked with", 0, 10, 0x884444);
            minecraft.textRenderer.draw("bellows!", 0, 20, 0x884444);
        }
    }

    @Override
    public void drawAnimations(Minecraft minecraft) {

    }

    @Override
    public void setRecipe(@NotNull RecipeLayout recipeLayout, @NotNull RecipeWrapper recipeWrapper) {
        GuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        int xOffset = 0;
        int yOffset = 0;
        for (int i = 0; i < recipeWrapper.getInputs().size(); i++) {
            guiItemStacks.init(i, true, xOffset + (i % 9) * 18, yOffset + (i / 9) * 18 + 46);
        }
        for (int i = 0; i < recipeWrapper.getInputs().size(); i++) {
            if (((ItemStack)recipeWrapper.getInputs().get(i)).isItemEqual(new ItemStack(ItemListener.nothing))) {
                stoked = true;
                break;
            } else {
                stoked = false;
            }
            guiItemStacks.setFromRecipe(i, recipeWrapper.getInputs().get(i));
        }
        guiItemStacks.init(27, false, xOffset + 72, yOffset);
        guiItemStacks.setFromRecipe(27, recipeWrapper.getOutputs().get(0));
    }
}
