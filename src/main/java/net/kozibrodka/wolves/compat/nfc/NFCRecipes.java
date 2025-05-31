package net.kozibrodka.wolves.compat.nfc;

import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.events.RecipeListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.newfrontiercraft.nfc.events.init.BlockListener;

public class NFCRecipes {

    public static void addCrucibleRecipes() {
        RecipeListener.addCrucibleRecipe(new ItemStack(ItemListener.steel, 4), new ItemStack[]{new ItemStack(net.newfrontiercraft.nfc.events.init.ItemListener.steelIngot, 2), new ItemStack(ItemListener.concentratedHellfire), new ItemStack(ItemListener.coalDust)});
        RecipeListener.addCrucibleRecipe(new ItemStack(net.newfrontiercraft.nfc.events.init.ItemListener.anthracite, 2), new ItemStack[]{new ItemStack(net.newfrontiercraft.nfc.events.init.ItemListener.anthracite), new ItemStack(ItemListener.netherCoal, 4)});
    }

    public static void addCauldronRecipes() {
        RecipeListener.addCauldronRecipe(new ItemStack(net.newfrontiercraft.nfc.events.init.ItemListener.netherAsh, 2), new ItemStack[] {
                new ItemStack(BlockListener.petrifiedLog, 1), new ItemStack(net.newfrontiercraft.nfc.events.init.ItemListener.netherAsh, 1), new ItemStack(Item.COAL, 2, 1)
        });
        RecipeListener.addStokedCauldronRecipe(new ItemStack(Item.COAL, 2), new ItemStack[] {
                new ItemStack(ItemListener.coalDust, 1), new ItemStack(net.newfrontiercraft.nfc.events.init.ItemListener.netherAsh, 2), new ItemStack(Item.COAL, 1)
        });
    }
}
