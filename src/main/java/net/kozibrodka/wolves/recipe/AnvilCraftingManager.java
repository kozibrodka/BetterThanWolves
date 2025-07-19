package net.kozibrodka.wolves.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class AnvilCraftingManager {

    public static final AnvilCraftingManager getInstance() {
        return instance;
    }

    private AnvilCraftingManager() {
        recipes = new ArrayList();
    }

    public void addRecipe(ItemStack output, Object[] inputObjects) {
        String s = "";
        int i = 0;
        int width = 0;
        int height = 0;
        if (inputObjects[i] instanceof String[]) {
            String[] as = (String[]) inputObjects[i++];
            for (int l = 0; l < as.length; l++) {
                String s2 = as[l];
                height++;
                width = s2.length();
                s = s + s2;
            }
        } else {
            while (inputObjects[i] instanceof String) {
                String s1 = (String) inputObjects[i++];
                height++;
                width = s1.length();
                s = s + s1;
            }
        }
        HashMap hashmap = new HashMap();
        for (; i < inputObjects.length; i += 2) {
            Character character = (Character) inputObjects[i];
            ItemStack ItemInstance1 = null;
            if (inputObjects[i + 1] instanceof Item) {
                ItemInstance1 = new ItemStack((Item) inputObjects[i + 1]);
            } else if (inputObjects[i + 1] instanceof Block) {
                ItemInstance1 = new ItemStack((Block) inputObjects[i + 1], 1, -1);
            } else if (inputObjects[i + 1] instanceof ItemStack) {
                ItemInstance1 = (ItemStack) inputObjects[i + 1];
            }
            hashmap.put(character, ItemInstance1);
        }

        ItemStack[] inputs = new ItemStack[width * height];
        for (int i1 = 0; i1 < width * height; i1++) {
            char c = s.charAt(i1);
            if (hashmap.containsKey(c)) {
                inputs[i1] = ((ItemStack) hashmap.get(c)).copy();
            } else {
                inputs[i1] = null;
            }
        }

        recipes.add(new AnvilShapedRecipe(width, height, inputs, output));
    }

    public void addShapelessRecipe(ItemStack ItemInstance, Object[] aobj) {
        ArrayList arraylist = new ArrayList();
        Object[] aobj1 = aobj;
        int i = aobj1.length;
        for (int j = 0; j < i; j++) {
            Object obj = aobj1[j];
            if (obj instanceof ItemStack) {
                arraylist.add(((ItemStack) obj).copy());
                continue;
            }
            if (obj instanceof Item) {
                arraylist.add(new ItemStack((Item) obj));
                continue;
            }
            if (obj instanceof Block) {
                arraylist.add(new ItemStack((Block) obj));
            } else {
                throw new RuntimeException("Invalid shapeless recipe!");
            }
        }

        recipes.add(new AnvilShapelessRecipe(ItemInstance, arraylist));
    }

    public ItemStack findMatchingRecipe(CraftingInventory inventorycrafting) {
        for (int i = 0; i < recipes.size(); i++) {
            AnvilRecipeTemplate irecipe = (AnvilRecipeTemplate) recipes.get(i);
            if (irecipe.canCraft(inventorycrafting)) {
                return irecipe.craft(inventorycrafting);
            }
        }

        return null;
    }

    public List getRecipeList() {
        return recipes;
    }

    public ArrayList getShapedRecipes() {
        ArrayList shapedRecipes = new ArrayList();
        for (Object recipe : recipes) {
            if (recipe instanceof AnvilShapedRecipe) {
                shapedRecipes.add(recipe);
            }
        }
        return shapedRecipes;
    }

    public ArrayList getShapelessRecipes() {
        ArrayList shapelessRecipes = new ArrayList();
        for (Object recipe : recipes) {
            if (recipe instanceof AnvilShapelessRecipe) {
                shapelessRecipes.add(recipe);
            }
        }
        return shapelessRecipes;
    }

    private static final AnvilCraftingManager instance = new AnvilCraftingManager();
    private final List recipes;

}
