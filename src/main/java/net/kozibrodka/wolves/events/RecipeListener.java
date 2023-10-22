package net.kozibrodka.wolves.events;

import net.kozibrodka.wolves.recipe.*;
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
        AddCrucibleRecipes();
        AddHopperRecipes();
    }

    private static void AddAnvilRecipe(ItemInstance itemstack, Object aobj[])
    {
        AnvilCraftingManager.getInstance().addRecipe(itemstack, aobj);
    }

    private static void AddShapelessAnvilRecipe(ItemInstance itemstack, Object aobj[])
    {
        AnvilCraftingManager.getInstance().addShapelessRecipe(itemstack, aobj);
    }

    public static void AddCauldronRecipe(ItemInstance itemstack, ItemInstance aitemstack[])
    {
        CauldronCraftingManager.getInstance().AddRecipe(itemstack, aitemstack);
    }

    public static void AddStokedCauldronRecipe(ItemInstance itemstack, ItemInstance aitemstack[])
    {
        CauldronStokedCraftingManager.getInstance().AddRecipe(itemstack, aitemstack);
    }

    public static void AddCrucibleRecipe(ItemInstance itemstack, ItemInstance aitemstack[])
    {
        CrucibleCraftingManager.getInstance().AddRecipe(itemstack, aitemstack);
    }

    private static void AddBlockRecipes()
    {
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.anvil, 1), "###", " # ", "###", '#', ItemListener.steel);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.lightBulbOff, 1), " # ", "#X#", " Y ", '#', BlockBase.GLASS, 'X', ItemBase.glowstoneDust, 'Y', ItemBase.redstoneDust);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.hibachi), "XXX", "#Z#", "#Y#", '#', BlockBase.STONE, 'X', ItemListener.concentratedHellfire, 'Y', ItemBase.redstoneDust, 'Z',
                ItemBase.glowstoneDust);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.hopper), "# #", "XYX", " Z ", '#', new ItemInstance(BlockListener.omniSlab.id, 1, 1), 'X', ItemListener.gear, 'Y', BlockBase.WOODEN_PRESSURE_PLATE, 'Z',
                BlockListener.corner);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.saw), "YYY", "XZX", "#X#", '#', BlockBase.WOOD, 'X', ItemListener.gear, 'Y', ItemBase.ironIngot, 'Z',
                ItemListener.belt);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.platform), "#X#", " # ", "#X#", '#', BlockBase.WOOD, 'X', ItemListener.wicker);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.platform), "X#X", " X ", "X#X", '#', ItemListener.wicker, 'X', BlockListener.moulding);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.pulley), "#Y#", "XZX", "#Y#", '#', BlockBase.WOOD, 'X', ItemListener.gear, 'Y', ItemBase.ironIngot, 'Z',
                ItemBase.redstoneDust);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.pressurePlateObsidian, 1), "##", '#', BlockBase.OBSIDIAN);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.blockDispenser), "###", "# #", "YXY", '#', BlockBase.MOSSY_COBBLESTONE, 'X', ItemBase.redstoneDust, 'Y', BlockBase.STONE);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.cauldron), "#Y#", "#X#", "###", '#', ItemBase.ironIngot, 'X', ItemBase.bone, 'Y', ItemBase.waterBucket);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.detectorRailWood, 6), "X X", "X#X", "XRX", 'X', ItemBase.ironIngot, 'R', ItemBase.redstoneDust, '#', BlockBase.WOODEN_PRESSURE_PLATE);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.detectorRailObsidian, 6), "X X", "X#X", "XRX", 'X', ItemBase.ironIngot, 'R', ItemBase.redstoneDust, '#', BlockListener.pressurePlateObsidian);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.blockDetector), "X#X", "###", "ZYZ", '#', BlockBase.COBBLESTONE, 'X', new ItemInstance(ItemBase.dyePowder, 1, 4), 'Y', ItemBase.redstoneDust, 'Z',
                BlockBase.STONE);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.handCrank), "  Y", " Y ", "#X#", '#', BlockBase.COBBLESTONE, 'X', ItemListener.gear, 'Y', ItemBase.stick);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.millStone), "YYY", "YYY", "YXY", 'X', ItemListener.gear, 'Y', BlockBase.STONE);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.axleBlock), "#", "X", "#", '#', BlockBase.WOOD, 'X', ItemListener.ropeItem);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.axleBlock), "#", "X", "#", '#', BlockListener.moulding, 'X', ItemListener.ropeItem);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.gearBox), "#X#", "XYX", "#X#", '#', BlockBase.WOOD, 'X', ItemListener.gear, 'Y', ItemBase.redstoneDust);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.gearBox), "#X#", "XYX", "#X#", '#', new ItemInstance(BlockListener.omniSlab.id, 1, 1), 'X', ItemListener.gear, 'Y', ItemBase.redstoneDust);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.turntable), "###", "ZXZ", "ZYZ", '#', new ItemInstance(BlockListener.omniSlab.id, 1, 1), 'X', ItemBase.redstoneDust, 'Y', ItemListener.gear, 'Z',
                BlockBase.STONE);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.bellows), "###", "XXX", "YZY", '#', new ItemInstance(BlockListener.omniSlab.id, 1, 1), 'X', ItemListener.tannedLeather, 'Y', ItemListener.gear, 'Z',
                ItemListener.belt);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.planter, 1, 1), "Y", "X", "#", '#', new ItemInstance(BlockListener.planter, 1, 0), 'X', ItemBase.waterBucket, 'Y', BlockBase.DIRT);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.blockOfWicker, 1), "XX", "XX", 'X', ItemListener.wicker);
    }

    private static void AddItemRecipes()
    {
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.bucketCement, 1), "#YX", " Z ", Character.valueOf('#'), BlockBase.SOUL_SAND, Character.valueOf('X'), BlockBase.GRAVEL, Character.valueOf('Y'), ItemBase.waterBucket, Character.valueOf('Z'), ItemBase.bucket);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.gear, 2), " X ", "X#X", " X ", Character.valueOf('#'), BlockBase.WOOD, Character.valueOf('X'), ItemBase.stick);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.ropeItem, 1), "##", "##", "##", Character.valueOf('#'), ItemListener.hempFibers);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.anchor), " X ", "###", Character.valueOf('#'), BlockBase.STONE, Character.valueOf('X'), ItemBase.ironIngot);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.waterWheelItem), "###", "# #", "###", Character.valueOf('#'), ItemListener.woodBlade);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.windMillBladeItem), "###", "###", "XXX", Character.valueOf('#'), ItemListener.hempCloth, Character.valueOf('X'), BlockListener.moulding);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.windMillBladeItem), "###", "###", "XXX", Character.valueOf('#'), ItemListener.hempCloth, Character.valueOf('X'), BlockBase.WOOD);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.windMillItem), " # ", "# #", " # ", Character.valueOf('#'), ItemListener.windMillBladeItem);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.hempCloth, 1), "###", "###", "###", Character.valueOf('#'), ItemListener.hempFibers);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.grate, 1), "###", "###", "###", Character.valueOf('#'), ItemBase.stick);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.wicker, 1), "###", "###", "###", Character.valueOf('#'), ItemBase.sugarCanes);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.rollersItem, 1), "###", "###", "###", Character.valueOf('#'), BlockListener.moulding);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.strap, 8), "#", Character.valueOf('#'), ItemListener.tannedLeather);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.belt, 1), " # ", "# #", " # ", Character.valueOf('#'), ItemListener.strap);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.woodBlade, 1), "#  ", "#X#", "#  ", Character.valueOf('#'), new ItemInstance(BlockListener.omniSlab.id, 1, 1), Character.valueOf('X'), ItemBase.slimeball);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.woodBlade, 1), "#  ", "#X#", "#  ", Character.valueOf('#'), new ItemInstance(BlockListener.omniSlab.id, 1, 1), Character.valueOf('X'), ItemListener.glue);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.haft, 1), "Y", "X", "#", Character.valueOf('#'), BlockListener.moulding, Character.valueOf('X'), ItemListener.glue, Character.valueOf('Y'), ItemListener.strap);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.haft, 1), "Y", "X", "#", Character.valueOf('#'), BlockListener.moulding, Character.valueOf('X'), ItemBase.slimeball, Character.valueOf('Y'), ItemListener.strap);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.compositeBow, 1), "X#Y", "ZX#", "X#Y", Character.valueOf('#'), BlockListener.moulding, Character.valueOf('X'), ItemBase.bone, Character.valueOf('Y'), ItemBase.slimeball, Character.valueOf('Z'),
                ItemBase.string);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.compositeBow, 1), "X#Y", "ZX#", "X#Y", Character.valueOf('#'), BlockListener.moulding, Character.valueOf('X'), ItemBase.bone, Character.valueOf('Y'), ItemListener.glue, Character.valueOf('Z'),
                ItemBase.string);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemListener.broadHeadArrow, 4), "#", "X", "Y", Character.valueOf('#'), ItemListener.broadHeadArrowhead, Character.valueOf('X'), BlockListener.moulding, Character.valueOf('Y'), ItemBase.feather);
    }

    private static void AddDyeRecipes()
    {
        for(int i = 0; i < 15; i++)
        {
            CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockListener.vase, 1, Wool.getColour(i)), new ItemInstance(ItemBase.dyePowder, 1, i), new ItemInstance(BlockListener.vase.id, 1, 0));
        }

        CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.WOOL, 1, 12), new ItemInstance(ItemListener.dung), new ItemInstance(BlockBase.WOOL.id, 1, 0));
        CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockListener.vase, 1, 12), new ItemInstance(ItemListener.dung), new ItemInstance(BlockListener.vase.id, 1, 0));
    }

    private static void AddAlternateVanillaRecipes()
    {
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockBase.STICKY_PISTON, 1), "#", "X", Character.valueOf('#'), ItemListener.glue, Character.valueOf('X'), BlockBase.PISTON);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockBase.FENCE, 2), "###", Character.valueOf('#'), BlockListener.moulding);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockBase.WOOD_STAIRS, 1), "# ", "##", Character.valueOf('#'), BlockListener.moulding);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemBase.sign, 1), "###", "###", " X ", Character.valueOf('#'), new ItemInstance(BlockListener.omniSlab.id, 1, 1), Character.valueOf('X'), ItemBase.stick);
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemBase.woodDoor, 1), "##", "##", "##", Character.valueOf('#'), new ItemInstance(BlockListener.omniSlab.id, 1, 1));
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockBase.TRAPDOOR, 2), "###", "###", Character.valueOf('#'), new ItemInstance(BlockListener.omniSlab.id, 1, 1));
        CraftingRegistry.addShapedRecipe(new ItemInstance(ItemBase.cake, 1), "AAA", "BEB", "CCC", Character.valueOf('A'), ItemBase.sugar, Character.valueOf('B'), ItemBase.milk, Character.valueOf('C'), ItemListener.flour, Character.valueOf('E'),
                ItemBase.egg);
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockBase.TORCH, 4), "#", "X", Character.valueOf('#'), ItemListener.netherCoal, Character.valueOf('X'), ItemBase.stick);
    }

    private static void AddConversionRecipes()
    {
        CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.WOOD), new ItemInstance(BlockListener.omniSlab.id, 1, 1), new ItemInstance(BlockListener.omniSlab.id, 1, 1));
        CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.STONE), new ItemInstance(BlockListener.omniSlab.id, 1, 0), new ItemInstance(BlockListener.omniSlab.id, 1, 0));
        CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockListener.omniSlab.id, 1, 1), new ItemInstance(BlockListener.moulding.id,1,0), new ItemInstance(BlockListener.moulding.id,1,0));
        CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockListener.moulding.id,1,0), new ItemInstance(BlockListener.corner.id,1,0), new ItemInstance(BlockListener.corner.id,1,0));
        CraftingRegistry.addShapelessRecipe(new ItemInstance(ItemBase.string), new ItemInstance(ItemListener.hempFibers), new ItemInstance(ItemListener.hempFibers));
    }

    private static void AddSmeltingRecipes()
    {
        SmeltingRegistry.addSmeltingRecipe(ItemListener.wolfRaw.id, new ItemInstance(ItemListener.wolfCooked));
        SmeltingRegistry.addSmeltingRecipe(ItemListener.flour.id, new ItemInstance(ItemBase.bread));
//        ModLoader.AddSmelting(mod_FCBetterThanWolves.wolfRaw.id, new ItemInstance(mod_FCBetterThanWolves.wolfCooked));
//        ModLoader.AddSmelting(mod_FCBetterThanWolves.flour.id, new ItemInstance(ItemBase.bread));
    }

    private static void AddAnvilRecipes()
    {
        AddAnvilRecipe(new ItemInstance(ItemListener.refinedPickAxe, 1), new Object[] {
                "###", " X ", " X ", '#', ItemListener.steel, 'X', ItemListener.haft
        });
        AddAnvilRecipe(new ItemInstance(ItemListener.refinedShovel, 1), new Object[] {
                "#", "X", "X", '#', ItemListener.steel, 'X', ItemListener.haft
        });
        AddAnvilRecipe(new ItemInstance(ItemListener.refinedHoe, 1), new Object[] {
                "##", " X", " X", '#', ItemListener.steel, 'X', ItemListener.haft
        });
        AddAnvilRecipe(new ItemInstance(ItemListener.refinedAxe, 1), new Object[] {
                "###", "#X#", " X ", '#', ItemListener.steel, 'X', ItemListener.haft
        });
        AddAnvilRecipe(new ItemInstance(ItemListener.refinedSword, 1), new Object[] {
                "#", "#", "X", '#', ItemListener.steel, 'X', ItemListener.haft
        });
        AddAnvilRecipe(new ItemInstance(ItemListener.armourPlateSteel, 1), new Object[] {
                "#X#", '#', ItemListener.strap, 'X', ItemListener.steel
        });
        AddAnvilRecipe(new ItemInstance(ItemListener.helmetSteel, 1), new Object[] {
                "###", "#X#", '#', ItemListener.steel, 'X', ItemListener.armourPlateSteel
        });
        AddAnvilRecipe(new ItemInstance(ItemListener.chestPlateSteel, 1), new Object[] {
                "X X", "###", "###", '#', ItemListener.steel, 'X', ItemListener.armourPlateSteel
        });
        AddAnvilRecipe(new ItemInstance(ItemListener.leggingsSteel, 1), new Object[] {
                "###", "X X", "X X", '#', ItemListener.steel, 'X', ItemListener.armourPlateSteel
        });
        AddAnvilRecipe(new ItemInstance(ItemListener.bootsSteel, 1), new Object[] {
                "# #", "# #", '#', ItemListener.steel, 'X', ItemListener.armourPlateSteel
        });
        AddShapelessAnvilRecipe(new ItemInstance(ItemListener.broadHeadArrowhead, 4), new Object[] {
                ItemListener.steel
        });
        AddShapelessAnvilRecipe(new ItemInstance(BlockListener.omniSlab.id, 1, 0), new Object[] {
                BlockBase.STONE
        });
    }

    private static void AddCauldronRecipes()
    {
        AddCauldronRecipe(new ItemInstance(ItemListener.netherCoal, 2), new ItemInstance[] {
                new ItemInstance(ItemListener.hellfireDust, 4), new ItemInstance(ItemListener.coalDust, 1)
        });
        AddCauldronRecipe(new ItemInstance(ItemListener.concentratedHellfire, 1), new ItemInstance[] {
                new ItemInstance(ItemListener.hellfireDust, 8)
        });
        AddCauldronRecipe(new ItemInstance(ItemListener.tannedLeather, 1), new ItemInstance[] {
                new ItemInstance(ItemListener.dung, 1), new ItemInstance(ItemListener.scouredLeather, 1)
        });
        AddCauldronRecipe(new ItemInstance(ItemListener.donut, 4), new ItemInstance[] {
                new ItemInstance(ItemListener.flour, 1)
        });
        for(int i = 0; i < 15; i++)
        {
            AddCauldronRecipe(new ItemInstance(BlockBase.WOOL, 8, Wool.getColour(i)), new ItemInstance[] {
                    new ItemInstance(ItemBase.dyePowder, 1, i), new ItemInstance(BlockBase.WOOL, 8, 0)
            });
        }
        AddCauldronRecipe(new ItemInstance(BlockBase.WOOL, 8, 12), new ItemInstance[] {
                new ItemInstance(ItemListener.dung, 1), new ItemInstance(BlockBase.WOOL.id, 8, 0)
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
        AddCauldronRecipe(new ItemInstance(ItemListener.soulFilter, 1), new ItemInstance[] {
                new ItemInstance(ItemListener.hempCloth, 2), new ItemInstance(BlockBase.SOUL_SAND, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 1), new ItemInstance[] {
                new ItemInstance(ItemBase.leather, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 1), new ItemInstance[] {
                new ItemInstance(ItemListener.tannedLeather, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 1), new ItemInstance[] {
                new ItemInstance(ItemListener.scouredLeather, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 1), new ItemInstance[] {
                new ItemInstance(ItemListener.belt, 2)
        });
        AddStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 1), new ItemInstance[] {
                new ItemInstance(ItemBase.bone, 8)
        });
        AddStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 1), new ItemInstance[] {
                new ItemInstance(ItemListener.strap, 8)
        });
        AddStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 1), new ItemInstance[] {
                new ItemInstance(BlockBase.WOOL, 16)
        });
        AddStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 1), new ItemInstance[] {
                new ItemInstance(ItemBase.feather, 16)
        });
        AddStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 1), new ItemInstance[] {
                new ItemInstance(ItemBase.string, 64)
        });
        AddStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 5), new ItemInstance[] {
                new ItemInstance(ItemBase.leatherHelmet, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 8), new ItemInstance[] {
                new ItemInstance(ItemBase.leatherChestplate, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 7), new ItemInstance[] {
                new ItemInstance(ItemBase.leatherLeggings, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 4), new ItemInstance[] {
                new ItemInstance(ItemBase.leatherBoots, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(ItemListener.tallow, 1), new ItemInstance[] {
                new ItemInstance(ItemBase.cookedPorkchop, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(ItemListener.tallow, 1), new ItemInstance[] {
                new ItemInstance(ItemBase.rawPorkchop, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(ItemListener.tallow, 1), new ItemInstance[] {
                new ItemInstance(ItemListener.wolfCooked, 1)
        });
        AddStokedCauldronRecipe(new ItemInstance(ItemListener.tallow, 1), new ItemInstance[] {
                new ItemInstance(ItemListener.wolfRaw, 1)
        });
    }

    private static void AddMillingRecipes()
    {
        MillingRecipeRegistry.getInstance().addMillingRecipe(ItemBase.wheat.id, new ItemInstance(ItemListener.flour, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(ItemBase.leather.id, new ItemInstance(ItemListener.scouredLeather, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(ItemListener.hemp.id, new ItemInstance(ItemListener.hempFibers, 4));
        MillingRecipeRegistry.getInstance().addMillingRecipe(ItemBase.sugarCanes.id, new ItemInstance(ItemBase.sugar, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(BlockBase.NETHERRACK.id, new ItemInstance(ItemListener.groundNetherrack, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(ItemBase.bone.id, new ItemInstance(ItemBase.dyePowder, 3, 15));
        MillingRecipeRegistry.getInstance().addMillingRecipe(ItemBase.coal.id, new ItemInstance(ItemListener.coalDust, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(BlockBase.ROSE.id, new ItemInstance(ItemBase.dyePowder, 2, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(BlockBase.DANDELION.id, new ItemInstance(ItemBase.dyePowder, 2, 11));
    }

    private static void AddTurntableRecipes()
    {
        TurntableRecipeRegistry.getInstance().addTurntableRecipe(BlockBase.CLAY.id, new ItemInstance(BlockListener.unfiredPottery));
        TurntableRecipeRegistry.getInstance().addTurntableRecipe(BlockListener.unfiredPottery.id, new ItemInstance(BlockListener.unfiredPottery, 1, 1));
    }

    private static void AddCrucibleRecipes()
    {
        AddCrucibleRecipe(new ItemInstance(ItemListener.steel, 4), new ItemInstance[] { new ItemInstance(ItemBase.ironIngot, 3), new ItemInstance(ItemListener.concentratedHellfire), new ItemInstance(ItemListener.coalDust) });
    }

    private static void AddHopperRecipes()
    {
        HopperHauntingRecipeRegistry.getInstance().addHopperHauntingRecipe(ItemListener.groundNetherrack.id, new ItemInstance(ItemListener.hellfireDust, 1));
    }

    private static void AddSawingRecipes()
    {
        SawingRecipeRegistry.getInstance().addSawingRecipe(BlockBase.LOG.id, new ItemInstance(BlockBase.WOOD, 4));
        SawingRecipeRegistry.getInstance().addSawingRecipe(BlockBase.WOOD.id, new ItemInstance(BlockListener.omniSlab, 2, 1));
        SawingRecipeRegistry.getInstance().addSawingRecipe(BlockListener.omniSlab.id, new ItemInstance(BlockListener.moulding, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(BlockListener.moulding.id, new ItemInstance(BlockListener.corner, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(BlockListener.corner.id, new ItemInstance(ItemListener.gear, 2));
    }

    private static void AddDebugRecipes()
    {
    }
}
