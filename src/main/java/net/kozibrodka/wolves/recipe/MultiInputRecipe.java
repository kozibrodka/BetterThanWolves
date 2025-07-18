package net.kozibrodka.wolves.recipe;

import net.kozibrodka.wolves.utils.InventoryHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import java.util.List;


public class MultiInputRecipe {

    private final ItemStack recipeOutputStack;
    private final List recipeInputStacks;

    public MultiInputRecipe(ItemStack recipeOutputStack, List recipeInputStacks) {
        this.recipeOutputStack = recipeOutputStack;
        this.recipeInputStacks = recipeInputStacks;
    }

    public ItemStack getCopyOfOutputStack() {
        if(recipeOutputStack == null) {
            return null;
        } else {
            return recipeOutputStack.copy();
        }
    }

    public boolean doesInventoryContainIngredients(Inventory inventory) {
        if(recipeInputStacks != null && !recipeInputStacks.isEmpty()) {
            for (Object recipeInputStack : recipeInputStacks) {
                ItemStack tempStack = (ItemStack) recipeInputStack;
                if (tempStack != null && InventoryHandler.itemCountInInventory(inventory, tempStack.getItem().id, tempStack.getDamage()) < tempStack.count) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void ConsumeInventoryIngredients(Inventory inventory) {
        if(recipeInputStacks != null && !recipeInputStacks.isEmpty()) {
            for (Object recipeOutputStack : recipeInputStacks) {
                ItemStack tempStack = (ItemStack) recipeOutputStack;
                if (tempStack != null) {
                    InventoryHandler.consumeItemsInInventory(inventory, tempStack.getItem().id, tempStack.getDamage(), tempStack.count);
                }
            }

        }
    }

    public ItemStack getInputStack(int index) {
        return (ItemStack) recipeInputStacks.get(index);
    }

    public List getRecipeInputStacks() {
        return recipeInputStacks;
    }

    public int getNumberOfInputStacks() {
        return recipeInputStacks.size();
    }
}
