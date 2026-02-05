package net.kozibrodka.wolves.compat.ami.turntable;

import net.glasslauncher.mods.alwaysmoreitems.api.recipe.RecipeWrapper;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.recipe.TurntableRecipe;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TurntableRecipeWrapper implements RecipeWrapper {
    private final TurntableRecipe recipe;

    public TurntableRecipeWrapper(TurntableRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public List<?> getInputs() {
        return List.of(new ItemStack(recipe.turntableInput().inputBlock().asItem(), 1, recipe.turntableInput().inputBlockMeta()));
    }

    @Override
    public List<?> getOutputs() {
        Block resultBlock = recipe.turntableResult().turntableOutput().block();
        return List.of(new ItemStack(resultBlock == Block.LOCKED_CHEST ? ItemListener.nothing : resultBlock.asItem(), 1, recipe.turntableResult().turntableOutput().blockMeta()), new ItemStack(recipe.turntableResult().turntableByproduct().item(), recipe.turntableResult().turntableByproduct().itemCount()));
    }

    @Override
    public void drawInfo(@NotNull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

    @Override
    public void drawAnimations(@NotNull Minecraft minecraft, int recipeWidth, int recipeHeight) {

    }

    @Nullable
    @Override
    public ArrayList<Object> getTooltip(int mouseX, int mouseY) {
        return null;
    }

    @Override
    public boolean handleClick(@NotNull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }
}
