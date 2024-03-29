package net.kozibrodka.wolves.recipe;

import net.minecraft.block.BlockBase;
import net.minecraft.inventory.Crafting;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AnvilCraftingManager
{

    public static final AnvilCraftingManager getInstance()
    {
        return instance;
    }

    private AnvilCraftingManager()
    {
        recipes = new ArrayList();
    }

    public void addRecipe(ItemInstance ItemInstance, Object aobj[]) {
        String s = "";
        int i = 0;
        int width = 0;
        int height = 0;
        if(aobj[i] instanceof String[]) {
            String as[] = (String[])aobj[i++];
            for(int l = 0; l < as.length; l++) {
                String s2 = as[l];
                height++;
                width = s2.length();
                s = (new StringBuilder()).append(s).append(s2).toString();
            }
        } else {
            while(aobj[i] instanceof String) {
                String s1 = (String)aobj[i++];
                height++;
                width = s1.length();
                s = (new StringBuilder()).append(s).append(s1).toString();
            }
        }
        HashMap hashmap = new HashMap();
        for(; i < aobj.length; i += 2) {
            Character character = (Character)aobj[i];
            ItemInstance ItemInstance1 = null;
            if(aobj[i + 1] instanceof ItemBase) {
                ItemInstance1 = new ItemInstance((ItemBase)aobj[i + 1]);
            } else if(aobj[i + 1] instanceof BlockBase) {
                ItemInstance1 = new ItemInstance((BlockBase)aobj[i + 1], 1, -1);
            } else if(aobj[i + 1] instanceof ItemInstance) {
                ItemInstance1 = (ItemInstance)aobj[i + 1];
            }
            hashmap.put(character, ItemInstance1);
        }

        ItemInstance aItemInstance[] = new ItemInstance[width * height];
        for(int i1 = 0; i1 < width * height; i1++) {
            char c = s.charAt(i1);
            if(hashmap.containsKey(Character.valueOf(c))) {
                aItemInstance[i1] = ((ItemInstance)hashmap.get(Character.valueOf(c))).copy();
            } else {
                aItemInstance[i1] = null;
            }
        }

        recipes.add(new AnvilShapedRecipe(width, height, aItemInstance, ItemInstance));
    }

    public void addShapelessRecipe(ItemInstance ItemInstance, Object aobj[]) {
        ArrayList arraylist = new ArrayList();
        Object aobj1[] = aobj;
        int i = aobj1.length;
        for(int j = 0; j < i; j++) {
            Object obj = aobj1[j];
            if(obj instanceof ItemInstance) {
                arraylist.add(((ItemInstance)obj).copy());
                continue;
            }
            if(obj instanceof ItemBase) {
                arraylist.add(new ItemInstance((ItemBase)obj));
                continue;
            }
            if(obj instanceof BlockBase) {
                arraylist.add(new ItemInstance((BlockBase)obj));
            } else {
                throw new RuntimeException("Invalid shapeless recipe!");
            }
        }

        recipes.add(new AnvilShapelessRecipe(ItemInstance, arraylist));
    }

    public ItemInstance findMatchingRecipe(Crafting inventorycrafting) {
        for(int i = 0; i < recipes.size(); i++) {
            AnvilRecipeTemplate irecipe = (AnvilRecipeTemplate)recipes.get(i);
            if(irecipe.canCraft(inventorycrafting)) {
                return irecipe.craft(inventorycrafting);
            }
        }

        return null;
    }

    public List getRecipeList() {
        return recipes;
    }

    private static final AnvilCraftingManager instance = new AnvilCraftingManager();
    private List recipes;

}
