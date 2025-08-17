package net.kozibrodka.wolves.recipe;

import net.kozibrodka.wolves.wrappers.HopperPurifyingRecipeWrapperWrapper;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HopperPurifyingRecipeRegistry {
    private static final HopperPurifyingRecipeRegistry INSTANCE = new HopperPurifyingRecipeRegistry();
    private final Map recipes = new HashMap();

    public static final HopperPurifyingRecipeRegistry getInstance() {
        return INSTANCE;
    }

    public void addHopperHauntingRecipe(Identifier i, ItemStack arg) {
        this.recipes.put(i, arg);
    }

    public ItemStack getResult(Identifier i) {
        return (ItemStack) this.recipes.get(i);
    }

    public ArrayList<HopperPurifyingRecipeWrapperWrapper> getRecipes() {
        ArrayList<HopperPurifyingRecipeWrapperWrapper> convertedRecipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        for (Object obj : recipes.keySet()) {
            if (obj instanceof Identifier) {
                inputs.add(new ItemStack(ItemRegistry.INSTANCE.get((Identifier) obj), 1, 0));
                outputs.add(getResult((Identifier) obj));
            }
        }
        for (int i = 0; i < inputs.size(); i++) {
            if (i >= outputs.size()) break;
            convertedRecipes.add(new HopperPurifyingRecipeWrapperWrapper(inputs.get(i), outputs.get(i)));
        }
        return convertedRecipes;
    }
}

