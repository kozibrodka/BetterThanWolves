package net.kozibrodka.wolves.compat.ami;

import net.glasslauncher.mods.alwaysmoreitems.api.*;
import net.kozibrodka.wolves.compat.ami.anvil.AnvilShapedRecipeHandler;
import net.kozibrodka.wolves.compat.ami.anvil.AnvilShapedRecipeCategory;
import net.kozibrodka.wolves.compat.ami.anvil.AnvilShapelessRecipeCategory;
import net.kozibrodka.wolves.compat.ami.anvil.AnvilShapelessRecipeHandler;
import net.kozibrodka.wolves.compat.ami.hopperpurifying.HopperPurifyingRecipeCategory;
import net.kozibrodka.wolves.compat.ami.hopperpurifying.HopperPurifyingRecipeHandler;
import net.kozibrodka.wolves.compat.ami.makecalmangry.MultiInputRecipeMerger;
import net.kozibrodka.wolves.compat.ami.multiinput.MultiInputRecipeCategory;
import net.kozibrodka.wolves.compat.ami.multiinput.MultiInputRecipeHandler;
import net.kozibrodka.wolves.compat.ami.millstone.MillStoneRecipeCategory;
import net.kozibrodka.wolves.compat.ami.millstone.MillStoneRecipeHandler;
import net.kozibrodka.wolves.compat.ami.sawmill.SawRecipeCategory;
import net.kozibrodka.wolves.compat.ami.sawmill.SawRecipeHandler;
import net.kozibrodka.wolves.compat.ami.turntable.TurntableRecipeCategory;
import net.kozibrodka.wolves.compat.ami.turntable.TurntableRecipeHandler;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ConfigListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.recipe.*;
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
        return ConfigListener.NAMESPACE.id("wolves");
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
        registry.addDescription(new ItemStack(ItemListener.wickerSheet), "description.wolves.wicker_sheet");
        registry.addDescription(new ItemStack(ItemListener.wickerWeaving), "description.wolves.wicker_weaving");

        registry.addRecipeCategories(new MillStoneRecipeCategory());
        registry.addRecipeHandlers(new MillStoneRecipeHandler());
        registry.addRecipes(MillingRecipeRegistry.getInstance().getRecipes());

        registry.addRecipeCategories(new AnvilShapedRecipeCategory());
        registry.addRecipeHandlers(new AnvilShapedRecipeHandler());
        registry.addRecipes(AnvilCraftingManager.getInstance().getShapedRecipes());

        registry.addRecipeCategories(new AnvilShapelessRecipeCategory());
        registry.addRecipeHandlers(new AnvilShapelessRecipeHandler());
        registry.addRecipes(AnvilCraftingManager.getInstance().getShapelessRecipes());

        registry.addRecipeCategories(new MultiInputRecipeCategory());
        registry.addRecipeHandlers(new MultiInputRecipeHandler());
        registry.addRecipes(MultiInputRecipeMerger.getInstance().getRecipes());

        registry.addRecipeCategories(new SawRecipeCategory());
        registry.addRecipeHandlers(new SawRecipeHandler());
        registry.addRecipes(SawingRecipeRegistry.getInstance().getRecipes());

        registry.addRecipeCategories(new TurntableRecipeCategory());
        registry.addRecipeHandlers(new TurntableRecipeHandler());
        registry.addRecipes(TurntableRecipeRegistry.getInstance().getRecipes());

        registry.addRecipeCategories(new HopperPurifyingRecipeCategory());
        registry.addRecipeHandlers(new HopperPurifyingRecipeHandler());
        registry.addRecipes(HopperPurifyingRecipeRegistry.getInstance().getRecipes());
    }

    @Override
    public void onRecipeRegistryAvailable(RecipeRegistry recipeRegistry) {

    }

    @Override
    public SyncableRecipe deserializeRecipe(NbtCompound recipe) {
        return null;
    }

    @Override
    public void updateBlacklist(AMIHelpers amiHelpers) {

    }
}
