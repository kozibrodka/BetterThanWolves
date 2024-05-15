package net.kozibrodka.wolves.recipe;

import net.minecraft.item.ItemInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SawingRecipeRegistry {
    private static final SawingRecipeRegistry INSTANCE = new SawingRecipeRegistry();
    private Map recipes = new HashMap();

    public static final SawingRecipeRegistry getInstance() {
        return INSTANCE;
    }

    public void addSawingRecipe(int i, ItemInstance arg) {
        this.recipes.put(i, arg);
    }

    public ItemInstance getResult(int i) {
        return (ItemInstance)this.recipes.get(i);
    }

    // This is not a clean solution, but it should work fine
    public ArrayList<ItemInstance[]> getRecipes() {
        ArrayList<ItemInstance[]> itemInstances = new ArrayList<>();
        ArrayList<ItemInstance> inputs = new ArrayList<>();
        ArrayList<ItemInstance> outputs = new ArrayList<>();
        for (Object obj : recipes.keySet()) {
            if (obj instanceof Integer)
            {
                inputs.add(new ItemInstance((Integer) obj, 1, 0));
                outputs.add(getResult((Integer) obj));
            }
        }
        for (int i = 0; i < inputs.size(); i++) {
            if (i >= outputs.size()) break;
            itemInstances.add(new ItemInstance[] {inputs.get(i), outputs.get(i)});
        }
        return itemInstances;
    }
}

