package net.kozibrodka.wolves.events;

import net.kozibrodka.wolves.recipe.*;
import net.kozibrodka.wolves.tabs.CrucibleTripleRecipeTab;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.block.Wool;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;
import net.modificationstation.stationapi.api.recipe.SmeltingRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Null;

public class RecipeListener {

    @Entrypoint.ModID
    public static final ModID MOD_ID = Null.get();

    @EventListener
    public void registerRecipes(RecipeRegisterEvent event) {
        AddAllModRecipes(event);
    }

    public static void AddAllModRecipes(RecipeRegisterEvent event)
    {
        Identifier type = event.recipeId;
        if (type == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPED.type()) {
            AddBlockRecipes();
            AddItemRecipes();
            AddAlternateVanillaRecipes();
        }
        if (type == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPELESS.type()) {
            AddDyeRecipes();
            AddConversionRecipes();
        }
        if (type == RecipeRegisterEvent.Vanilla.SMELTING.type()) AddSmeltingRecipes();
        // TODO: Add custom recipe types to the event bus and check for them like vanilla recipes to avoid redundant recipe creation
        AddAnvilRecipes();
        AddCauldronRecipes();
        AddDebugRecipes();
        AddMillingRecipes();
        AddSawingRecipes();
        AddTurntableRecipes(); // Only used for tabs right now. TODO: Integrate this registry into the TileEntity of the turntable
        AddCrucibleRecipes(); // Only used for tabs right now. TODO: Integrate this registry into the TileEntity of the crucible
        AddHopperRecipes();
    }

    private static void AddAnvilRecipe(ItemInstance itemstack, Object aobj[])
    {
        FCCraftingManagerAnvil.getInstance().addRecipe(itemstack, aobj);
    }

    private static void AddShapelessAnvilRecipe(ItemInstance itemstack, Object aobj[])
    {
        FCCraftingManagerAnvil.getInstance().addShapelessRecipe(itemstack, aobj);
    }

    public static void AddCauldronRecipe(ItemInstance itemstack, ItemInstance aitemstack[])
    {
        FCCraftingManagerCauldron.getInstance().AddRecipe(itemstack, aitemstack);
    }

    public static void AddStokedCauldronRecipe(ItemInstance itemstack, ItemInstance aitemstack[])
    {
        FCCraftingManagerCauldronStoked.getInstance().AddRecipe(itemstack, aitemstack);
    }

    public static void AddCrucibleRecipe(ItemInstance itemstack, ItemInstance aitemstack[])
    {
        CraftingManagerCrucible.getInstance().AddRecipe(itemstack, aitemstack);
    }

    private static void AddBlockRecipes()
    {
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcAnvil, 1), "###", " # ", "###", '#', mod_FCBetterThanWolves.fcSteel);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcLightBulbOff, 1), " # ", "#X#", " Y ", '#', BlockBase.GLASS, 'X', ItemBase.glowstoneDust, 'Y', ItemBase.redstoneDust);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcBBQ), "XXX", "#Z#", "#Y#", '#', BlockBase.STONE, 'X', mod_FCBetterThanWolves.fcConcentratedHellfire, 'Y', ItemBase.redstoneDust, 'Z',
                ItemBase.glowstoneDust);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcHopper), "# #", "XYX", " Z ", '#', new ItemInstance(mod_FCBetterThanWolves.fcOmniSlab.id, 1, 1), 'X', mod_FCBetterThanWolves.fcGear, 'Y', BlockBase.WOODEN_PRESSURE_PLATE, 'Z',
                mod_FCBetterThanWolves.fcCorner);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcSaw), "YYY", "XZX", "#X#", '#', BlockBase.WOOD, 'X', mod_FCBetterThanWolves.fcGear, 'Y', ItemBase.ironIngot, 'Z',
                mod_FCBetterThanWolves.fcBelt);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcPlatform), "#X#", " # ", "#X#", '#', BlockBase.WOOD, 'X', mod_FCBetterThanWolves.fcWicker);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcPlatform), "X#X", " X ", "X#X", '#', mod_FCBetterThanWolves.fcWicker, 'X', mod_FCBetterThanWolves.fcMoulding);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcPulley), "#Y#", "XZX", "#Y#", '#', BlockBase.WOOD, 'X', mod_FCBetterThanWolves.fcGear, 'Y', ItemBase.ironIngot, 'Z',
                ItemBase.redstoneDust);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcPressurePlateObsidian, 1), "##", '#', BlockBase.OBSIDIAN);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcBlockDispenser), "###", "# #", "YXY", '#', BlockBase.MOSSY_COBBLESTONE, 'X', ItemBase.redstoneDust, 'Y', BlockBase.STONE);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcCauldron), "#Y#", "#X#", "###", '#', ItemBase.ironIngot, 'X', ItemBase.bone, 'Y', ItemBase.waterBucket);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcDetectorRailWood, 6), "X X", "X#X", "XRX", 'X', ItemBase.ironIngot, 'R', ItemBase.redstoneDust, '#', BlockBase.WOODEN_PRESSURE_PLATE);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcDetectorRailObsidian, 6), "X X", "X#X", "XRX", 'X', ItemBase.ironIngot, 'R', ItemBase.redstoneDust, '#', mod_FCBetterThanWolves.fcPressurePlateObsidian);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcBlockDetector), "X#X", "###", "ZYZ", '#', BlockBase.COBBLESTONE, 'X', new ItemInstance(ItemBase.dyePowder, 1, 4), 'Y', ItemBase.redstoneDust, 'Z',
                BlockBase.STONE);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcHandCrank), "  Y", " Y ", "#X#", '#', BlockBase.COBBLESTONE, 'X', mod_FCBetterThanWolves.fcGear, 'Y', ItemBase.stick);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcMillStone), "YYY", "YYY", "YXY", 'X', mod_FCBetterThanWolves.fcGear, 'Y', BlockBase.STONE);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcAxleBlock), "#", "X", "#", '#', BlockBase.WOOD, 'X', mod_FCBetterThanWolves.fcRopeItem);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcAxleBlock), "#", "X", "#", '#', mod_FCBetterThanWolves.fcMoulding, 'X', mod_FCBetterThanWolves.fcRopeItem);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcGearBox), "#X#", "XYX", "#X#", '#', BlockBase.WOOD, 'X', mod_FCBetterThanWolves.fcGear, 'Y', ItemBase.redstoneDust);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcGearBox), "#X#", "XYX", "#X#", '#', new ItemInstance(mod_FCBetterThanWolves.fcOmniSlab.id, 1, 1), 'X', mod_FCBetterThanWolves.fcGear, 'Y', ItemBase.redstoneDust);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcTurntable), "###", "ZXZ", "ZYZ", '#', new ItemInstance(mod_FCBetterThanWolves.fcOmniSlab.id, 1, 1), 'X', ItemBase.redstoneDust, 'Y', mod_FCBetterThanWolves.fcGear, 'Z',
                BlockBase.STONE);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcBellows), "###", "XXX", "YZY", '#', new ItemInstance(mod_FCBetterThanWolves.fcOmniSlab.id, 1, 1), 'X', mod_FCBetterThanWolves.fcTannedLeather, 'Y', mod_FCBetterThanWolves.fcGear, 'Z',
                mod_FCBetterThanWolves.fcBelt);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcPlanter, 1, 1), "Y", "X", "#", '#', new ItemInstance(mod_FCBetterThanWolves.fcPlanter, 1, 0), 'X', ItemBase.waterBucket, 'Y', BlockBase.DIRT);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcBlockOfWicker, 1), "XX", "XX", 'X', mod_FCBetterThanWolves.fcWicker);
    }

    private static void AddItemRecipes()
    {
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcBucketCement, 1), "#YX", " Z ", Character.valueOf('#'), BlockBase.SOUL_SAND, Character.valueOf('X'), BlockBase.GRAVEL, Character.valueOf('Y'), ItemBase.waterBucket, Character.valueOf('Z'), ItemBase.bucket);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcGear, 2), " X ", "X#X", " X ", Character.valueOf('#'), BlockBase.WOOD, Character.valueOf('X'), ItemBase.stick);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcRopeItem, 1), "##", "##", "##", Character.valueOf('#'), mod_FCBetterThanWolves.fcHempFibers);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcAnchor), " X ", "###", Character.valueOf('#'), BlockBase.STONE, Character.valueOf('X'), ItemBase.ironIngot);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcWaterWheelItem), "###", "# #", "###", Character.valueOf('#'), mod_FCBetterThanWolves.fcWoodBlade);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcWindMillBladeItem), "###", "###", "XXX", Character.valueOf('#'), mod_FCBetterThanWolves.fcHempCloth, Character.valueOf('X'), mod_FCBetterThanWolves.fcMoulding);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcWindMillBladeItem), "###", "###", "XXX", Character.valueOf('#'), mod_FCBetterThanWolves.fcHempCloth, Character.valueOf('X'), BlockBase.WOOD);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcWindMillItem), " # ", "# #", " # ", Character.valueOf('#'), mod_FCBetterThanWolves.fcWindMillBladeItem);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcHempCloth, 1), "###", "###", "###", Character.valueOf('#'), mod_FCBetterThanWolves.fcHempFibers);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcGrate, 1), "###", "###", "###", Character.valueOf('#'), ItemBase.stick);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcWicker, 1), "###", "###", "###", Character.valueOf('#'), ItemBase.sugarCanes);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcRollersItem, 1), "###", "###", "###", Character.valueOf('#'), mod_FCBetterThanWolves.fcMoulding);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcStrap, 8), "#", Character.valueOf('#'), mod_FCBetterThanWolves.fcTannedLeather);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcBelt, 1), " # ", "# #", " # ", Character.valueOf('#'), mod_FCBetterThanWolves.fcStrap);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcWoodBlade, 1), "#  ", "#X#", "#  ", Character.valueOf('#'), new ItemInstance(mod_FCBetterThanWolves.fcOmniSlab.id, 1, 1), Character.valueOf('X'), ItemBase.slimeball);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcWoodBlade, 1), "#  ", "#X#", "#  ", Character.valueOf('#'), new ItemInstance(mod_FCBetterThanWolves.fcOmniSlab.id, 1, 1), Character.valueOf('X'), mod_FCBetterThanWolves.fcGlue);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcHaft, 1), "Y", "X", "#", Character.valueOf('#'), mod_FCBetterThanWolves.fcMoulding, Character.valueOf('X'), mod_FCBetterThanWolves.fcGlue, Character.valueOf('Y'), mod_FCBetterThanWolves.fcStrap);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcHaft, 1), "Y", "X", "#", Character.valueOf('#'), mod_FCBetterThanWolves.fcMoulding, Character.valueOf('X'), ItemBase.slimeball, Character.valueOf('Y'), mod_FCBetterThanWolves.fcStrap);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcCompositeBow, 1), "X#Y", "ZX#", "X#Y", Character.valueOf('#'), mod_FCBetterThanWolves.fcMoulding, Character.valueOf('X'), ItemBase.bone, Character.valueOf('Y'), ItemBase.slimeball, Character.valueOf('Z'),
                ItemBase.string);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcCompositeBow, 1), "X#Y", "ZX#", "X#Y", Character.valueOf('#'), mod_FCBetterThanWolves.fcMoulding, Character.valueOf('X'), ItemBase.bone, Character.valueOf('Y'), mod_FCBetterThanWolves.fcGlue, Character.valueOf('Z'),
                ItemBase.string);
        CraftingRegistry.addShapedRecipe(new ItemInstance(mod_FCBetterThanWolves.fcBroadheadArrow, 4), "#", "X", "Y", Character.valueOf('#'), mod_FCBetterThanWolves.fcBroadheadArrowhead, Character.valueOf('X'), mod_FCBetterThanWolves.fcMoulding, Character.valueOf('Y'), ItemBase.feather);
    }

    private static void AddDyeRecipes()
    {
        for(int i = 0; i < 15; i++)
        {
            CraftingRegistry.addShapelessRecipe(new ItemInstance(mod_FCBetterThanWolves.fcVase, 1, Wool.getColour(i)), new ItemInstance(ItemBase.dyePowder, 1, i), new ItemInstance(mod_FCBetterThanWolves.fcVase.id, 1, 0));
        }

        CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.WOOL, 1, 12), new ItemInstance(mod_FCBetterThanWolves.fcDung), new ItemInstance(BlockBase.WOOL.id, 1, 0));
        CraftingRegistry.addShapelessRecipe(new ItemInstance(mod_FCBetterThanWolves.fcVase, 1, 12), new ItemInstance(mod_FCBetterThanWolves.fcDung), new ItemInstance(mod_FCBetterThanWolves.fcVase.id, 1, 0));
    }

    private static void AddAlternateVanillaRecipes()
    {
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockBase.STICKY_PISTON, 1), "#", "X", Character.valueOf('#'), mod_FCBetterThanWolves.fcGlue, Character.valueOf('X'), BlockBase.PISTON);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockBase.FENCE, 2), "###", Character.valueOf('#'), mod_FCBetterThanWolves.fcMoulding);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockBase.WOOD_STAIRS, 1), "# ", "##", Character.valueOf('#'), mod_FCBetterThanWolves.fcMoulding);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemBase.sign, 1), "###", "###", " X ", Character.valueOf('#'), new ItemInstance(mod_FCBetterThanWolves.fcOmniSlab.id, 1, 1), Character.valueOf('X'), ItemBase.stick);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemBase.woodDoor, 1), "##", "##", "##", Character.valueOf('#'), new ItemInstance(mod_FCBetterThanWolves.fcOmniSlab.id, 1, 1));
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockBase.TRAPDOOR, 2), "###", "###", Character.valueOf('#'), new ItemInstance(mod_FCBetterThanWolves.fcOmniSlab.id, 1, 1));
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemBase.cake, 1), "AAA", "BEB", "CCC", Character.valueOf('A'), ItemBase.sugar, Character.valueOf('B'), ItemBase.milk, Character.valueOf('C'), mod_FCBetterThanWolves.fcFlour, Character.valueOf('E'),
                ItemBase.egg);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockBase.TORCH, 4), "#", "X", Character.valueOf('#'), mod_FCBetterThanWolves.fcNethercoal, Character.valueOf('X'), ItemBase.stick);
    }

    private static void AddConversionRecipes()
    {
        CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.WOOD), new ItemInstance(mod_FCBetterThanWolves.fcOmniSlab.id, 1, 1), new ItemInstance(mod_FCBetterThanWolves.fcOmniSlab.id, 1, 1));
        CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.STONE), new ItemInstance(mod_FCBetterThanWolves.fcOmniSlab.id, 1, 0), new ItemInstance(mod_FCBetterThanWolves.fcOmniSlab.id, 1, 0));
        CraftingRegistry.addShapelessRecipe(new ItemInstance(mod_FCBetterThanWolves.fcOmniSlab.id, 1, 1), new ItemInstance(mod_FCBetterThanWolves.fcMoulding.id,1,0), new ItemInstance(mod_FCBetterThanWolves.fcMoulding.id,1,0));
        CraftingRegistry.addShapelessRecipe(new ItemInstance(mod_FCBetterThanWolves.fcMoulding.id,1,0), new ItemInstance(mod_FCBetterThanWolves.fcCorner.id,1,0), new ItemInstance(mod_FCBetterThanWolves.fcCorner.id,1,0));
        CraftingRegistry.addShapelessRecipe(new ItemInstance(ItemBase.string), new ItemInstance(mod_FCBetterThanWolves.fcHempFibers), new ItemInstance(mod_FCBetterThanWolves.fcHempFibers));
    }

    private static void AddSmeltingRecipes()
    {
        SmeltingRegistry.addSmeltingRecipe(mod_FCBetterThanWolves.fcWolfRaw.id, new ItemInstance(mod_FCBetterThanWolves.fcWolfCooked));
        SmeltingRegistry.addSmeltingRecipe(mod_FCBetterThanWolves.fcFlour.id, new ItemInstance(ItemBase.bread));
//        ModLoader.AddSmelting(mod_FCBetterThanWolves.fcWolfRaw.id, new ItemInstance(mod_FCBetterThanWolves.fcWolfCooked));
//        ModLoader.AddSmelting(mod_FCBetterThanWolves.fcFlour.id, new ItemInstance(ItemBase.bread));
    }

    private static void AddAnvilRecipes()
    {
        AddAnvilRecipe(new ItemInstance(mod_FCBetterThanWolves.fcRefinedPickAxe, 1), new Object[] {
                "###", " X ", " X ", '#', mod_FCBetterThanWolves.fcSteel, 'X', mod_FCBetterThanWolves.fcHaft
        });
        AddAnvilRecipe(new ItemInstance(mod_FCBetterThanWolves.fcRefinedShovel, 1), new Object[] {
                "#", "X", "X", '#', mod_FCBetterThanWolves.fcSteel, 'X', mod_FCBetterThanWolves.fcHaft
        });
        AddAnvilRecipe(new ItemInstance(mod_FCBetterThanWolves.fcRefinedHoe, 1), new Object[] {
                "##", " X", " X", '#', mod_FCBetterThanWolves.fcSteel, 'X', mod_FCBetterThanWolves.fcHaft
        });
        AddAnvilRecipe(new ItemInstance(mod_FCBetterThanWolves.fcRefinedAxe, 1), new Object[] {
                "###", "#X#", " X ", '#', mod_FCBetterThanWolves.fcSteel, 'X', mod_FCBetterThanWolves.fcHaft
        });
        AddAnvilRecipe(new ItemInstance(mod_FCBetterThanWolves.fcRefinedSword, 1), new Object[] {
                "#", "#", "X", '#', mod_FCBetterThanWolves.fcSteel, 'X', mod_FCBetterThanWolves.fcHaft
        });
        AddAnvilRecipe(new ItemInstance(mod_FCBetterThanWolves.fcArmorPlate, 1), new Object[] {
                "#X#", '#', mod_FCBetterThanWolves.fcStrap, 'X', mod_FCBetterThanWolves.fcSteel
        });
        AddAnvilRecipe(new ItemInstance(mod_FCBetterThanWolves.fcPlateHelm, 1), new Object[] {
                "###", "#X#", '#', mod_FCBetterThanWolves.fcSteel, 'X', mod_FCBetterThanWolves.fcArmorPlate
        });
        AddAnvilRecipe(new ItemInstance(mod_FCBetterThanWolves.fcPlateBreastPlate, 1), new Object[] {
                "X X", "###", "###", '#', mod_FCBetterThanWolves.fcSteel, 'X', mod_FCBetterThanWolves.fcArmorPlate
        });
        AddAnvilRecipe(new ItemInstance(mod_FCBetterThanWolves.fcPlateLeggings, 1), new Object[] {
                "###", "X X", "X X", '#', mod_FCBetterThanWolves.fcSteel, 'X', mod_FCBetterThanWolves.fcArmorPlate
        });
        AddAnvilRecipe(new ItemInstance(mod_FCBetterThanWolves.fcPlateBoots, 1), new Object[] {
                "# #", "# #", '#', mod_FCBetterThanWolves.fcSteel, 'X', mod_FCBetterThanWolves.fcArmorPlate
        });
        AddShapelessAnvilRecipe(new ItemInstance(mod_FCBetterThanWolves.fcBroadheadArrowhead, 4), new Object[] {
                mod_FCBetterThanWolves.fcSteel
        });
        AddShapelessAnvilRecipe(new ItemInstance(mod_FCBetterThanWolves.fcOmniSlab.id, 1, 0), new Object[] {
                BlockBase.STONE
        });
    }

    private static void AddCauldronRecipes()
    {
        AddCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcNethercoal, 2), new ItemInstance[] {
                new ItemInstance(mod_FCBetterThanWolves.fcHellfireDust, 4), new ItemInstance(mod_FCBetterThanWolves.fcCoalDust, 1)
        });
        AddCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcConcentratedHellfire, 1), new ItemInstance[] {
                new ItemInstance(mod_FCBetterThanWolves.fcHellfireDust, 8)
        });
        AddCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcTannedLeather, 1), new ItemInstance[] {
                new ItemInstance(mod_FCBetterThanWolves.fcDung, 1), new ItemInstance(mod_FCBetterThanWolves.fcScouredLeather, 1)
        });
        AddCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcDonut, 4), new ItemInstance[] {
                new ItemInstance(mod_FCBetterThanWolves.fcFlour, 1)
        });
        for(int i = 0; i < 15; i++)
        {
            AddCauldronRecipe(new ItemInstance(BlockBase.WOOL, 8, Wool.getColour(i)), new ItemInstance[] {
                    new ItemInstance(ItemBase.dyePowder, 1, i), new ItemInstance(BlockBase.WOOL, 8, 0)
            });
        }
        AddCauldronRecipe(new ItemInstance(BlockBase.WOOL, 8, 12), new ItemInstance[] {
                new ItemInstance(mod_FCBetterThanWolves.fcDung, 1), new ItemInstance(BlockBase.WOOL.id, 8, 0)
        });
        AddCauldronRecipe(new ItemInstance(BlockBase.TNT), new ItemInstance[] {
                new ItemInstance(BlockBase.SAND, 4), new ItemInstance(ItemBase.gunpowder, 5)
        });
        AddCauldronRecipe(new ItemInstance(BlockBase.WOOL), new ItemInstance[] {
                new ItemInstance(ItemBase.string, 4)
        });
        AddCauldronRecipe(new ItemInstance(ItemBase.dyePowder, 1, 2), new ItemInstance[] {
                new ItemInstance(BlockBase.CACTUS)
        });
        AddCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.soulFilter, 1), new ItemInstance[] {
                new ItemInstance(mod_FCBetterThanWolves.fcHempCloth, 2), new ItemInstance(BlockBase.SOUL_SAND, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcGlue, 1), new ItemInstance[] {
                new ItemInstance(ItemBase.leather, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcGlue, 1), new ItemInstance[] {
                new ItemInstance(mod_FCBetterThanWolves.fcTannedLeather, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcGlue, 1), new ItemInstance[] {
                new ItemInstance(mod_FCBetterThanWolves.fcScouredLeather, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcGlue, 1), new ItemInstance[] {
                new ItemInstance(mod_FCBetterThanWolves.fcBelt, 2)
        });
        AddStokedCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcGlue, 1), new ItemInstance[] {
                new ItemInstance(ItemBase.bone, 8)
        });
        AddStokedCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcGlue, 1), new ItemInstance[] {
                new ItemInstance(mod_FCBetterThanWolves.fcStrap, 8)
        });
        AddStokedCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcGlue, 1), new ItemInstance[] {
                new ItemInstance(BlockBase.WOOL, 16)
        });
        AddStokedCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcGlue, 1), new ItemInstance[] {
                new ItemInstance(ItemBase.feather, 16)
        });
        AddStokedCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcGlue, 1), new ItemInstance[] {
                new ItemInstance(ItemBase.string, 64)
        });
        AddStokedCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcGlue, 5), new ItemInstance[] {
                new ItemInstance(ItemBase.leatherHelmet, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcGlue, 8), new ItemInstance[] {
                new ItemInstance(ItemBase.leatherChestplate, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcGlue, 7), new ItemInstance[] {
                new ItemInstance(ItemBase.leatherLeggings, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcGlue, 4), new ItemInstance[] {
                new ItemInstance(ItemBase.leatherBoots, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcTallow, 1), new ItemInstance[] {
                new ItemInstance(ItemBase.cookedPorkchop, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcTallow, 1), new ItemInstance[] {
                new ItemInstance(ItemBase.rawPorkchop, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcTallow, 1), new ItemInstance[] {
                new ItemInstance(mod_FCBetterThanWolves.fcWolfCooked, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(mod_FCBetterThanWolves.fcTallow, 1), new ItemInstance[] {
                new ItemInstance(mod_FCBetterThanWolves.fcWolfRaw, 1)
        });
    }

    private static void AddMillingRecipes()
    {
        MillingRecipeRegistry.getInstance().addMillingRecipe(ItemBase.wheat.id, new ItemInstance(mod_FCBetterThanWolves.fcFlour, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(ItemBase.leather.id, new ItemInstance(mod_FCBetterThanWolves.fcScouredLeather, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(mod_FCBetterThanWolves.fcHemp.id, new ItemInstance(mod_FCBetterThanWolves.fcHempFibers, 4));
        MillingRecipeRegistry.getInstance().addMillingRecipe(ItemBase.sugarCanes.id, new ItemInstance(ItemBase.sugar, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(BlockBase.NETHERRACK.id, new ItemInstance(mod_FCBetterThanWolves.fcGroundNetherrack, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(ItemBase.bone.id, new ItemInstance(ItemBase.dyePowder, 3, 15));
        MillingRecipeRegistry.getInstance().addMillingRecipe(ItemBase.coal.id, new ItemInstance(mod_FCBetterThanWolves.fcCoalDust, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(BlockBase.ROSE.id, new ItemInstance(ItemBase.dyePowder, 2, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(BlockBase.DANDELION.id, new ItemInstance(ItemBase.dyePowder, 2, 11));
    }

    private static void AddTurntableRecipes()
    {
        TurntableRecipeRegistry.getInstance().addTurntableRecipe(BlockBase.CLAY.id, new ItemInstance(mod_FCBetterThanWolves.fcUnfiredPottery));
        TurntableRecipeRegistry.getInstance().addTurntableRecipe(mod_FCBetterThanWolves.fcUnfiredPottery.id, new ItemInstance(mod_FCBetterThanWolves.fcUnfiredPottery, 1, 1));
    }

    private static void AddCrucibleRecipes()
    {
        AddCrucibleRecipe(new ItemInstance(mod_FCBetterThanWolves.fcSteel), new ItemInstance[] { new ItemInstance(ItemBase.ironIngot, 3), new ItemInstance(mod_FCBetterThanWolves.fcConcentratedHellfire), new ItemInstance(mod_FCBetterThanWolves.fcCoalDust) });
    }

    private static void AddHopperRecipes()
    {
        HopperHauntingRecipeRegistry.getInstance().addHopperHauntingRecipe(mod_FCBetterThanWolves.fcGroundNetherrack.id, new ItemInstance(mod_FCBetterThanWolves.fcHellfireDust, 1));
    }

    private static void AddSawingRecipes()
    {
        SawingRecipeRegistry.getInstance().addSawingRecipe(BlockBase.LOG.id, new ItemInstance(BlockBase.WOOD, 4));
        SawingRecipeRegistry.getInstance().addSawingRecipe(BlockBase.WOOD.id, new ItemInstance(mod_FCBetterThanWolves.fcOmniSlab, 2, 1));
        SawingRecipeRegistry.getInstance().addSawingRecipe(mod_FCBetterThanWolves.fcOmniSlab.id, new ItemInstance(mod_FCBetterThanWolves.fcMoulding, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(mod_FCBetterThanWolves.fcMoulding.id, new ItemInstance(mod_FCBetterThanWolves.fcCorner, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(mod_FCBetterThanWolves.fcCorner.id, new ItemInstance(mod_FCBetterThanWolves.fcGear, 2));
    }

    private static void AddDebugRecipes()
    {
    }
}
