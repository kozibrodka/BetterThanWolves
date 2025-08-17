package net.kozibrodka.wolves.wrappers;

import net.minecraft.item.ItemStack;

public class MillStoneRecipeWrapperWrapper {
    private final ItemStack input;
    private final ItemStack output;

    public MillStoneRecipeWrapperWrapper(ItemStack input, ItemStack output) {
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
