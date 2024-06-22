package net.kozibrodka.wolves.recipe;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TurntableRecipeRegistry {
    private static final TurntableRecipeRegistry INSTANCE = new TurntableRecipeRegistry();
    private ArrayList<ItemInstance[]> recipes = new ArrayList<>();
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

    public void addTurntableRecipe(BlockBase block, ItemInstance output) {
        this.recipes.add(new ItemInstance[] {new ItemInstance(block, 1, 0), output, null});
    }

    public void addTurntableRecipe(BlockBase block, int meta, ItemInstance output) {
        this.recipes.add(new ItemInstance[] {new ItemInstance(block, 1, meta), output, null});
    }

    public void addTurntableRecipe(BlockBase block, int meta, ItemInstance output, ItemInstance byproduct) {
        this.recipes.add(new ItemInstance[] {new ItemInstance(block, 1, meta), output, byproduct});
    }

    public ItemInstance[] getResult(ItemInstance item) {
        for (ItemInstance[] items : recipes) {
            if (items[0].isDamageAndIDIdentical(item)) {
                return new ItemInstance[]{items[1], items[2]};
            }
        }
        return null;
    }

    public ArrayList<ItemInstance[]> getRecipes() {
        return recipes;
    }
}
