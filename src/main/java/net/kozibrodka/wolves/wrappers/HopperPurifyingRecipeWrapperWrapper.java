package net.kozibrodka.wolves.wrappers;

import net.minecraft.item.ItemStack;

public class HopperPurifyingRecipeWrapperWrapper {
    private final ItemStack input;
    private final ItemStack output;

    public HopperPurifyingRecipeWrapperWrapper(ItemStack input, ItemStack output) {
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
