package net.kozibrodka.wolves.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public ArrayList<ItemStack[]> getRecipes() {
        return recipes;
    }
}
