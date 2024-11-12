package net.kozibrodka.wolves.compat.ami;

import net.glasslauncher.mods.alwaysmoreitems.api.*;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ConfigListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.recipe.MillingRecipeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.Identifier;

public class AMICompat implements ModPluginProvider {

    @Override
    public String getName() {
        return "Better Than Wolves";
    }

    @Override
    public Identifier getId() {
        return ConfigListener.MOD_ID.id("wolves");
    }

    @Override
    public void onAMIHelpersAvailable(AMIHelpers amiHelpers) {

    }

    @Override
    public void onItemRegistryAvailable(ItemRegistry itemRegistry) {

    }

    @Override
    public void register(ModRegistry registry) {
        registry.addDescription(new ItemStack(ItemListener.hemp), "description.wolves.hemp");
        registry.addDescription(new ItemStack(ItemListener.hempSeeds), "description.wolves.hemp_seeds");
        registry.addDescription(new ItemStack(ItemListener.dung), "description.wolves.dung");
        registry.addDescription(new ItemStack(BlockListener.axleBlock), "description.wolves.axle");
        registry.addDescription(new ItemStack(BlockListener.gearBox), "description.wolves.gear_box");

        registry.addRecipeCategories(new MillStoneRecipeCategory());
        registry.addRecipeHandlers(new MillStoneRecipeHandler());
        registry.addRecipes(MillingRecipeRegistry.getInstance().getRecipes());
    }

    @Override
    public void onRecipeRegistryAvailable(RecipeRegistry recipeRegistry) {

    }

    @Override
    public SyncableRecipe deserializeRecipe(NbtCompound recipe) {
        return null;
    }
}
