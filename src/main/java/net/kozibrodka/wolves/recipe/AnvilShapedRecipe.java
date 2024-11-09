package net.kozibrodka.wolves.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;

public class AnvilShapedRecipe implements AnvilRecipeTemplate {
    private int width;
    private int height;
    private ItemStack[] ingredients;
    private ItemStack output;
    public final int outputId;

    public AnvilShapedRecipe(int i, int j, ItemStack[] args, ItemStack arg) {
        this.outputId = arg.itemId;
        this.width = i;
        this.height = j;
        this.ingredients = args;
        this.output = arg;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public boolean canCraft(CraftingInventory arg) {
        for(int var2 = 0; var2 <= 5 - this.width; ++var2) {
            for(int var3 = 0; var3 <= 5 - this.height; ++var3) {
                if (this.matches(arg, var2, var3, true)) {
                    return true;
                }

                if (this.matches(arg, var2, var3, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean matches(CraftingInventory arg, int i, int j, boolean bl) {
        for(int var5 = 0; var5 < 5; ++var5) {
            for(int var6 = 0; var6 < 5; ++var6) {
                int var7 = var5 - i;
                int var8 = var6 - j;
                ItemStack var9 = null;
                if (var7 >= 0 && var8 >= 0 && var7 < this.width && var8 < this.height) {
                    if (bl) {
                        var9 = this.ingredients[this.width - var7 - 1 + var8 * this.width];
                    } else {
                        var9 = this.ingredients[var7 + var8 * this.width];
                    }
                }

                ItemStack var10 = arg.getStack(var5, var6);
                if (var10 != null || var9 != null) {
                    if (var10 == null && var9 != null || var10 != null && var9 == null) {
                        return false;
                    }

                    if (var9.itemId != var10.itemId) {
                        return false;
                    }

                    if (var9.getDamage() != -1 && var9.getDamage() != var10.getDamage()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public ItemStack craft(CraftingInventory arg) {
        return new ItemStack(this.output.itemId, this.output.count, this.output.getDamage());
    }

    public int getIngredientCount() {
        return this.width * this.height;
    }

    public int getWidth() {
        return width;
    }
}
