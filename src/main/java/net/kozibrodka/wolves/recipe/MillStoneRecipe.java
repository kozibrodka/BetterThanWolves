package net.kozibrodka.wolves.recipe;

import net.minecraft.item.ItemStack;

public class MillStoneRecipe {
    private final ItemStack input;
    private final ItemStack output;

    public MillStoneRecipe(ItemStack input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }
}
