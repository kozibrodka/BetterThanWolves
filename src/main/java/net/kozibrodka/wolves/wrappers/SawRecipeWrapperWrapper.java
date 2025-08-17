package net.kozibrodka.wolves.wrappers;

import net.minecraft.item.ItemStack;

public class SawRecipeWrapperWrapper {
    private final ItemStack input;
    private final ItemStack output;

    public SawRecipeWrapperWrapper(ItemStack input, ItemStack output) {
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
