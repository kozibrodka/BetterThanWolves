package net.kozibrodka.wolves.recipe;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;


public abstract class MultiInputCraftingManager
{

    protected MultiInputCraftingManager()
    {
        recipes = new ArrayList();
    }

    public void addRecipe(ItemStack outputStack, ItemStack inputStacks[])
    {
        ArrayList arraylist = new ArrayList();
        int iInputStacksArrayLength = inputStacks.length;
        for(int iTempIndex = 0; iTempIndex < iInputStacksArrayLength; iTempIndex++)
        {
            arraylist.add(inputStacks[iTempIndex].copy());
        }

        recipes.add(new MultiInputRecipeHandler(outputStack, arraylist));
    }

    public ItemStack getCraftingResult(Inventory inventory)
    {
        for(int i = 0; i < recipes.size(); i++)
        {
            MultiInputRecipeHandler tempRecipe = (MultiInputRecipeHandler) recipes.get(i);
            if(tempRecipe.DoesInventoryContainIngredients(inventory))
            {
                return tempRecipe.getCopyOfOutputStack();
            }
        }

        return null;
    }

    public ItemStack consumeIngredientsAndReturnResult(Inventory inventory)
    {
        for(int i = 0; i < recipes.size(); i++)
        {
            MultiInputRecipeHandler tempRecipe = (MultiInputRecipeHandler) recipes.get(i);
            if(tempRecipe.DoesInventoryContainIngredients(inventory))
            {
                tempRecipe.ConsumeInventoryIngredients(inventory);
                return tempRecipe.getCopyOfOutputStack();
            }
        }

        return null;
    }

    protected List recipes;

    public List getRecipes() {
        return recipes;
    }
}
