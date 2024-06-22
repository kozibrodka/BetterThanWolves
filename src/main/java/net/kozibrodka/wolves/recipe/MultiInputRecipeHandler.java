package net.kozibrodka.wolves.recipe;

import net.kozibrodka.wolves.utils.InventoryHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import java.util.List;


public class MultiInputRecipeHandler
{

    public MultiInputRecipeHandler(ItemStack recipeOutputStack, List recipeInputStacks)
    {
        m_recipeOutputStack = recipeOutputStack;
        this.recipeInputStacks = recipeInputStacks;
    }

    public ItemStack getCopyOfOutputStack()
    {
        if(m_recipeOutputStack == null)
        {
            return null;
        } else
        {
            return m_recipeOutputStack.copy();
        }
    }

    public boolean DoesInventoryContainIngredients(Inventory inventory)
    {
        if(recipeInputStacks != null && !recipeInputStacks.isEmpty())
        {
            for(int listIndex = 0; listIndex < recipeInputStacks.size(); listIndex++)
            {
                ItemStack tempStack = (ItemStack) recipeInputStacks.get(listIndex);
                if(tempStack != null && InventoryHandler.itemCountInInventory(inventory, tempStack.getItem().id, tempStack.getDamage()) < tempStack.count)
                {
                    return false;
                }
            }

            return true;
        } else
        {
            return false;
        }
    }

    public boolean ConsumeInventoryIngredients(Inventory inventory)
    {
        boolean bSuccessful = true;
        if(recipeInputStacks != null && recipeInputStacks.size() > 0)
        {
            for (Object m_recipeInputStack : recipeInputStacks) {
                ItemStack tempStack = (ItemStack) m_recipeInputStack;
                if (tempStack != null && !InventoryHandler.consumeItemsInInventory(inventory, tempStack.getItem().id, tempStack.getDamage(), tempStack.count)) {
                    bSuccessful = false;
                }
            }

        }
        return bSuccessful;
    }

    public ItemStack getInputStack(int index)
    {
        return (ItemStack) recipeInputStacks.get(index);
    }

    public int getNumberOfInputStacks()
    {
        return recipeInputStacks.size();
    }

    public ItemStack getOutputStack()
    {
        return m_recipeOutputStack;
    }

    private final ItemStack m_recipeOutputStack;
    private final List recipeInputStacks;
}
