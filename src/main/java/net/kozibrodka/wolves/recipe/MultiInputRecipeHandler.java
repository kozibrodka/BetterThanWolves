package net.kozibrodka.wolves.recipe;

import net.kozibrodka.wolves.utils.InventoryHandler;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;

import java.util.List;


public class MultiInputRecipeHandler
{

    public MultiInputRecipeHandler(ItemInstance recipeOutputStack, List recipeInputStacks)
    {
        m_recipeOutputStack = recipeOutputStack;
        m_recipeInputStacks = recipeInputStacks;
    }

    public ItemInstance getCopyOfOutputStack()
    {
        if(m_recipeOutputStack == null)
        {
            return null;
        } else
        {
            return m_recipeOutputStack.copy();
        }
    }

    public boolean DoesInventoryContainIngredients(InventoryBase inventory)
    {
        if(m_recipeInputStacks != null && m_recipeInputStacks.size() > 0)
        {
            for(int listIndex = 0; listIndex < m_recipeInputStacks.size(); listIndex++)
            {
                ItemInstance tempStack = (ItemInstance)m_recipeInputStacks.get(listIndex);
                if(tempStack != null && InventoryHandler.CountItemsInInventory(inventory, tempStack.getType().id, tempStack.getDamage()) < tempStack.count)
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

    public boolean ConsumeInventoryIngredients(InventoryBase inventory)
    {
        boolean bSuccessful = true;
        if(m_recipeInputStacks != null && m_recipeInputStacks.size() > 0)
        {
            for (Object m_recipeInputStack : m_recipeInputStacks) {
                ItemInstance tempStack = (ItemInstance) m_recipeInputStack;
                if (tempStack != null && !InventoryHandler.ConsumeItemsInInventory(inventory, tempStack.getType().id, tempStack.getDamage(), tempStack.count)) {
                    bSuccessful = false;
                }
            }

        }
        return bSuccessful;
    }

    public ItemInstance getInputStack(int index)
    {
        return (ItemInstance) m_recipeInputStacks.get(index);
    }

    public int getNumberOfInputStacks()
    {
        return m_recipeInputStacks.size();
    }

    public ItemInstance getOutputStack()
    {
        return m_recipeOutputStack;
    }

    private final ItemInstance m_recipeOutputStack;
    private final List m_recipeInputStacks;
}
