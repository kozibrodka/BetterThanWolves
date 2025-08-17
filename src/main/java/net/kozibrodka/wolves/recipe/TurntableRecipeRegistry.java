package net.kozibrodka.wolves.recipe;

import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.wrappers.TurntableRecipeWrapperWrapper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TurntableRecipeRegistry {
    private static final TurntableRecipeRegistry INSTANCE = new TurntableRecipeRegistry();
    private final ArrayList<TurntableRecipe> recipes = new ArrayList<>();
    private final Map<Integer, Integer[][]> rotations = new HashMap<>();

    public static TurntableRecipeRegistry getInstance() {
        return INSTANCE;
    }

    public void addRotation(int blockId, Integer[][] values) {
        this.rotations.put(blockId, values);
    }

    public Integer[][] getRotationValue(int blockId) {
        return this.rotations.get(blockId);
    }

    public void addTurntableRecipe(TurntableRecipe turntableRecipe) {
        recipes.add(turntableRecipe);
    }

    public TurntableResult getResult(TurntableInput turntableInput) {
        if (turntableInput == null) {
            return null;
        }
        for (TurntableRecipe turntableRecipe : recipes) {
            if (turntableInput.equals(turntableRecipe.turntableInput())) {
                return turntableRecipe.turntableResult();
            }
        }
        return null;
    }

    public ArrayList<TurntableRecipe> getRecipes() {
        return recipes;
    }
}
