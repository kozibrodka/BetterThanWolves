package net.kozibrodka.wolves.recipe;

import net.kozibrodka.wolves.utils.FCRecipeAnvil;
import net.minecraft.inventory.Crafting;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.recipe.StationRecipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AnvilShapelessRecipe implements FCRecipeAnvil, StationRecipe {
    private final ItemInstance output;
    private final List input;

    public AnvilShapelessRecipe(ItemInstance arg, List list) {
        this.output = arg;
        this.input = list;
    }

    public ItemInstance getOutput() {
        return this.output;
    }

    public boolean canCraft(Crafting arg) {
        ArrayList var2 = new ArrayList(this.input);

        for(int var3 = 0; var3 < 3; ++var3) {
            for(int var4 = 0; var4 < 3; ++var4) {
                ItemInstance var5 = arg.getInventoryItemXY(var4, var3);
                if (var5 != null) {
                    boolean var6 = false;
                    Iterator var7 = var2.iterator();

                    while(var7.hasNext()) {
                        ItemInstance var8 = (ItemInstance)var7.next();
                        if (var5.itemId == var8.itemId && (var8.getDamage() == -1 || var5.getDamage() == var8.getDamage())) {
                            var6 = true;
                            var2.remove(var8);
                            break;
                        }
                    }

                    if (!var6) {
                        return false;
                    }
                }
            }
        }

        return var2.isEmpty();
    }

    public ItemInstance craft(Crafting arg) {
        return this.output.copy();
    }

    public int getIngredientCount() {
        return this.input.size();
    }

    @Override
    public ItemInstance[] getIngredients() {
        ItemInstance[] ingredients = new ItemInstance[input.size()];
        for (int i = 0; i < input.size(); i++) {
            ingredients[i] = (ItemInstance) input.get(i);
        }
        return ingredients;
    }

    @Override
    public ItemInstance[] getOutputs() {
        return new ItemInstance[] {output};
    }
}
