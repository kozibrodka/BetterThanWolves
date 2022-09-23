package net.kozibrodka.wolves.recipe;

import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;

import java.util.ArrayList;
import java.util.List;


public abstract class FCCraftingManagerBulk
{

    protected FCCraftingManagerBulk()
    {
        m_recipes = new ArrayList();
    }

    public void AddRecipe(ItemInstance outputStack, ItemInstance inputStacks[])
    {
        ArrayList arraylist = new ArrayList();
        int iInputStacksArrayLength = inputStacks.length;
        for(int iTempIndex = 0; iTempIndex < iInputStacksArrayLength; iTempIndex++)
        {
            arraylist.add(inputStacks[iTempIndex].copy());
        }

        m_recipes.add(new FCCraftingManagerBulkRecipe(outputStack, arraylist));
    }

    public ItemInstance GetCraftingResult(InventoryBase inventory)
    {
        for(int i = 0; i < m_recipes.size(); i++)
        {
            FCCraftingManagerBulkRecipe tempRecipe = (FCCraftingManagerBulkRecipe)m_recipes.get(i);
            if(tempRecipe.DoesInventoryContainIngredients(inventory))
            {
                return tempRecipe.getCopyOfOutputStack();
            }
        }

        return null;
    }

    public ItemInstance ConsumeIngrediantsAndReturnResult(InventoryBase inventory)
    {
        for(int i = 0; i < m_recipes.size(); i++)
        {
            FCCraftingManagerBulkRecipe tempRecipe = (FCCraftingManagerBulkRecipe)m_recipes.get(i);
            if(tempRecipe.DoesInventoryContainIngredients(inventory))
            {
                tempRecipe.ConsumeInventoryIngrediants(inventory);
                return tempRecipe.getCopyOfOutputStack();
            }
        }

        return null;
    }

    private List m_recipes;
}
