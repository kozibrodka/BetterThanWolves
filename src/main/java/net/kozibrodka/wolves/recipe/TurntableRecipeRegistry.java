package net.kozibrodka.wolves.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.kozibrodka.wolves.events.ItemListener;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class TurntableRecipeRegistry {
    private static final TurntableRecipeRegistry INSTANCE = new TurntableRecipeRegistry();
    private ArrayList<ItemStack[]> recipes = new ArrayList<>();
    private Map<Integer, Integer[][]> rotations = new HashMap<>();

    public static final TurntableRecipeRegistry getInstance() {
        return INSTANCE;
    }

    public void addRotation(int blockId, Integer[][] values) {
        this.rotations.put(blockId, values);
    }

    public Integer[][] getRotationValue(int blockId) {
        return this.rotations.get(blockId);
    }

    public void addTurntableRecipe(Block block, ItemStack output) {
        this.recipes.add(new ItemStack[] {new ItemStack(block, 1, 0), output, null});
    }

    public void addTurntableRecipe(Block block, int meta, ItemStack output) {
        this.recipes.add(new ItemStack[] {new ItemStack(block, 1, meta), output, null});
    }

    public void addTurntableRecipe(Block block, int meta, ItemStack output, ItemStack byproduct) {
        this.recipes.add(new ItemStack[] {new ItemStack(block, 1, meta), output, byproduct});
    }

    public ItemStack[] getResult(ItemStack item) {
        for (ItemStack[] items : recipes) {
            if (items[0].isItemEqual(item)) {
                return new ItemStack[]{items[1], items[2]};
            }
        }
        return null;
    }

    public ArrayList<TurntableRecipe> getRecipes() {
        ArrayList<TurntableRecipe> convertedRecipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        ArrayList<ItemStack> byproducts = new ArrayList<>();
        for (ItemStack[] recipe : recipes) {
            inputs.add(recipe[0]);
            recipe[1].count = 1;
            outputs.add(recipe[1]);
            if (recipe[2] == null) {
                recipe[2] = new ItemStack(ItemListener.nothing, 1);
            }
            byproducts.add(recipe[2]);
        }
        for (int i = 0; i < inputs.size(); i++) {
            if (i >= outputs.size()) break;
            convertedRecipes.add(new TurntableRecipe(inputs.get(i), outputs.get(i), byproducts.get(i)));
        }
        return convertedRecipes;
    }
}
