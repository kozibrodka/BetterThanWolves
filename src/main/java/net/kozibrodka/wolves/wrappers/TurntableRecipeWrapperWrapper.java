package net.kozibrodka.wolves.wrappers;

import net.minecraft.item.ItemStack;

public class TurntableRecipeWrapperWrapper {
    private final ItemStack input;
    private final ItemStack output;
    private final ItemStack byproduct;

    public TurntableRecipeWrapperWrapper(ItemStack input, ItemStack output, ItemStack byproduct) {
        this.input = input;
        this.output = output;
        this.byproduct = byproduct;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack[] getOutputs() {
        return new ItemStack[]{output, byproduct};
    }
}
