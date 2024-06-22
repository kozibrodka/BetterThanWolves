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
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

public class RecipeListener {

    @Entrypoint.Namespace
    public static final Namespace MOD_ID = Null.get();

    @EventListener
    public void registerRecipes(RecipeRegisterEvent event) {
        addAllModRecipes(event);
    }

    public static void addAllModRecipes(RecipeRegisterEvent event) {
        Identifier type = event.recipeId;
        if (type == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPED.type()) {
            addBlockRecipes();
            addItemRecipes();
            addAlternateVanillaRecipes();
        }
        if (type == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPELESS.type()) {
            addDyeRecipes();
            addConversionRecipes();
        }
        if (type == RecipeRegisterEvent.Vanilla.SMELTING.type()) AddSmeltingRecipes();
        // TODO: Add custom recipe types to the event bus and check for them like vanilla recipes to avoid redundant recipe creation
        addAnvilRecipes();
        addCauldronRecipes();
        addDebugRecipes();
        addMillingRecipes();
        addSawingRecipes();
        addTurntableRecipes();
        addCrucibleRecipes();
        addHopperRecipes();
    }

    private static void addAnvilRecipe(ItemInstance output, Object[] inputs) {
        AnvilCraftingManager.getInstance().addRecipe(output, inputs);
    }

    private static void addShapelessAnvilRecipe(ItemInstance output, Object[] inputs) {
        AnvilCraftingManager.getInstance().addShapelessRecipe(output, inputs);
    }

    public static void addCauldronRecipe(ItemInstance output, ItemInstance[] inputs) {
        CauldronCraftingManager.getInstance().addRecipe(output, inputs);
    }

    public static void addStokedCauldronRecipe(ItemInstance output, ItemInstance[] inputs) {
        CauldronStokedCraftingManager.getInstance().addRecipe(output, inputs);
    }

    public static void addCrucibleRecipe(ItemInstance output, ItemInstance[] inputs) {
        CrucibleCraftingManager.getInstance().addRecipe(output, inputs);
    }

    private static void addBlockRecipes() {
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
        CraftingRegistry.addShapedRecipe(new ItemInstance(BlockListener.detectorBlock), "X#X", "###", "ZYZ", '#', BlockBase.COBBLESTONE, 'X', new ItemInstance(ItemBase.dyePowder, 1, 4), 'Y', ItemBase.redstoneDust, 'Z',
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

    private static void addItemRecipes() {
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

    private static void addDyeRecipes() {
        for(int i = 0; i < 15; i++) {
            CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockListener.vase, 1, Wool.getColour(i)), new ItemInstance(ItemBase.dyePowder, 1, i), new ItemInstance(BlockListener.vase.id, 1, 0));
        }

        CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.WOOL, 1, 12), new ItemInstance(ItemListener.dung), new ItemInstance(BlockBase.WOOL.id, 1, 0));
        CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockListener.vase, 1, 12), new ItemInstance(ItemListener.dung), new ItemInstance(BlockListener.vase.id, 1, 0));
    }

    private static void addAlternateVanillaRecipes() {
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

    private static void addConversionRecipes() {
        CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.WOOD), new ItemInstance(BlockListener.omniSlab.id, 1, 1), new ItemInstance(BlockListener.omniSlab.id, 1, 1));
        CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockBase.STONE), new ItemInstance(BlockListener.omniSlab.id, 1, 0), new ItemInstance(BlockListener.omniSlab.id, 1, 0));
        CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockListener.omniSlab.id, 1, 1), new ItemInstance(BlockListener.moulding.id,1,0), new ItemInstance(BlockListener.moulding.id,1,0));
        CraftingRegistry.addShapelessRecipe(new ItemInstance(BlockListener.moulding.id,1,0), new ItemInstance(BlockListener.corner.id,1,0), new ItemInstance(BlockListener.corner.id,1,0));
        CraftingRegistry.addShapelessRecipe(new ItemInstance(ItemBase.string), new ItemInstance(ItemListener.hempFibers), new ItemInstance(ItemListener.hempFibers));
    }

    private static void AddSmeltingRecipes() {
        SmeltingRegistry.addSmeltingRecipe(ItemListener.wolfRaw.id, new ItemInstance(ItemListener.wolfCooked));
        SmeltingRegistry.addSmeltingRecipe(ItemListener.flour.id, new ItemInstance(ItemBase.bread));
//        ModLoader.AddSmelting(mod_FCBetterThanWolves.wolfRaw.id, new ItemInstance(mod_FCBetterThanWolves.wolfCooked));
//        ModLoader.AddSmelting(mod_FCBetterThanWolves.flour.id, new ItemInstance(ItemBase.bread));
    }

    private static void addAnvilRecipes() {
        addAnvilRecipe(new ItemInstance(ItemListener.refinedPickAxe, 1), new Object[] {
                "###", " X ", " X ", '#', ItemListener.steel, 'X', ItemListener.haft
        });
        addAnvilRecipe(new ItemInstance(ItemListener.refinedShovel, 1), new Object[] {
                "#", "X", "X", '#', ItemListener.steel, 'X', ItemListener.haft
        });
        addAnvilRecipe(new ItemInstance(ItemListener.refinedHoe, 1), new Object[] {
                "##", " X", " X", '#', ItemListener.steel, 'X', ItemListener.haft
        });
        addAnvilRecipe(new ItemInstance(ItemListener.refinedAxe, 1), new Object[] {
                "###", "#X#", " X ", '#', ItemListener.steel, 'X', ItemListener.haft
        });
        addAnvilRecipe(new ItemInstance(ItemListener.refinedSword, 1), new Object[] {
                "#", "#", "X", '#', ItemListener.steel, 'X', ItemListener.haft
        });
        addAnvilRecipe(new ItemInstance(ItemListener.armourPlateSteel, 1), new Object[] {
                "#X#", '#', ItemListener.strap, 'X', ItemListener.steel
        });
        addAnvilRecipe(new ItemInstance(ItemListener.helmetSteel, 1), new Object[] {
                "###", "#X#", '#', ItemListener.steel, 'X', ItemListener.armourPlateSteel
        });
        addAnvilRecipe(new ItemInstance(ItemListener.chestPlateSteel, 1), new Object[] {
                "X X", "###", "###", '#', ItemListener.steel, 'X', ItemListener.armourPlateSteel
        });
        addAnvilRecipe(new ItemInstance(ItemListener.leggingsSteel, 1), new Object[] {
                "###", "X X", "X X", '#', ItemListener.steel, 'X', ItemListener.armourPlateSteel
        });
        addAnvilRecipe(new ItemInstance(ItemListener.bootsSteel, 1), new Object[] {
                "# #", "# #", '#', ItemListener.steel, 'X', ItemListener.armourPlateSteel
        });
        addShapelessAnvilRecipe(new ItemInstance(ItemListener.broadHeadArrowhead, 4), new Object[] {
                ItemListener.steel
        });
        addShapelessAnvilRecipe(new ItemInstance(BlockListener.omniSlab.id, 1, 0), new Object[] {
                BlockBase.STONE
        });
    }

    private static void addCauldronRecipes() {
        addCauldronRecipe(new ItemInstance(ItemListener.netherCoal, 2), new ItemInstance[] {
                new ItemInstance(ItemListener.hellfireDust, 4), new ItemInstance(ItemListener.coalDust, 1)
        });
        addCauldronRecipe(new ItemInstance(ItemListener.concentratedHellfire, 1), new ItemInstance[] {
                new ItemInstance(ItemListener.hellfireDust, 8)
        });
        addCauldronRecipe(new ItemInstance(ItemListener.tannedLeather, 1), new ItemInstance[] {
                new ItemInstance(ItemListener.dung, 1), new ItemInstance(ItemListener.scouredLeather, 1)
        });
        addCauldronRecipe(new ItemInstance(ItemListener.donut, 4), new ItemInstance[] {
                new ItemInstance(ItemListener.flour, 1)
        });
        for(int i = 0; i < 15; i++) {
            addCauldronRecipe(new ItemInstance(BlockBase.WOOL, 8, Wool.getColour(i)), new ItemInstance[] {
                    new ItemInstance(ItemBase.dyePowder, 1, i), new ItemInstance(BlockBase.WOOL, 8, 0)
            });
        }
        addCauldronRecipe(new ItemInstance(BlockBase.WOOL, 8, 12), new ItemInstance[] {
                new ItemInstance(ItemListener.dung, 1), new ItemInstance(BlockBase.WOOL.id, 8, 0)
        });
        addCauldronRecipe(new ItemInstance(BlockBase.TNT), new ItemInstance[] {
                new ItemInstance(BlockBase.SAND, 4), new ItemInstance(ItemBase.gunpowder, 5)
        });
        addCauldronRecipe(new ItemInstance(BlockBase.WOOL), new ItemInstance[] {
                new ItemInstance(ItemBase.string, 4)
        });
        addCauldronRecipe(new ItemInstance(ItemBase.dyePowder, 1, 2), new ItemInstance[] {
                new ItemInstance(BlockBase.CACTUS)
        });
        addCauldronRecipe(new ItemInstance(ItemListener.soulFilter, 1), new ItemInstance[] {
                new ItemInstance(ItemListener.hempCloth, 2), new ItemInstance(BlockBase.SOUL_SAND, 1)
        });
        addStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 1), new ItemInstance[] {
                new ItemInstance(ItemBase.leather, 1)
        });
        addStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 1), new ItemInstance[] {
                new ItemInstance(ItemListener.tannedLeather, 1)
        });
        addStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 1), new ItemInstance[] {
                new ItemInstance(ItemListener.scouredLeather, 1)
        });
        addStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 1), new ItemInstance[] {
                new ItemInstance(ItemListener.belt, 2)
        });
        addStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 1), new ItemInstance[] {
                new ItemInstance(ItemBase.bone, 8)
        });
        addStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 1), new ItemInstance[] {
                new ItemInstance(ItemListener.strap, 8)
        });
        addStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 1), new ItemInstance[] {
                new ItemInstance(BlockBase.WOOL, 16)
        });
        addStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 1), new ItemInstance[] {
                new ItemInstance(ItemBase.feather, 16)
        });
        addStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 1), new ItemInstance[] {
                new ItemInstance(ItemBase.string, 64)
        });
        addStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 5), new ItemInstance[] {
                new ItemInstance(ItemBase.leatherHelmet, 1)
        });
        addStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 8), new ItemInstance[] {
                new ItemInstance(ItemBase.leatherChestplate, 1)
        });
        addStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 7), new ItemInstance[] {
                new ItemInstance(ItemBase.leatherLeggings, 1)
        });
        addStokedCauldronRecipe(new ItemInstance(ItemListener.glue, 4), new ItemInstance[] {
                new ItemInstance(ItemBase.leatherBoots, 1)
        });
        addStokedCauldronRecipe(new ItemInstance(ItemListener.tallow, 1), new ItemInstance[] {
                new ItemInstance(ItemBase.cookedPorkchop, 1)
        });
        addStokedCauldronRecipe(new ItemInstance(ItemListener.tallow, 1), new ItemInstance[] {
                new ItemInstance(ItemBase.rawPorkchop, 1)
        });
        addStokedCauldronRecipe(new ItemInstance(ItemListener.tallow, 1), new ItemInstance[] {
                new ItemInstance(ItemListener.wolfCooked, 1)
        });
        addStokedCauldronRecipe(new ItemInstance(ItemListener.tallow, 1), new ItemInstance[] {
                new ItemInstance(ItemListener.wolfRaw, 1)
        });
    }

    private static void addMillingRecipes() {
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

    private static void addTurntableRecipes() {
        TurntableRecipeRegistry.getInstance().addTurntableRecipe(BlockBase.CLAY, 0, new ItemInstance(BlockBase.CLAY, 4, 1));
        TurntableRecipeRegistry.getInstance().addTurntableRecipe(BlockBase.CLAY, 1, new ItemInstance(BlockBase.CLAY, 4, 2));
        TurntableRecipeRegistry.getInstance().addTurntableRecipe(BlockBase.CLAY, 2, new ItemInstance(BlockBase.CLAY, 4, 3));
        TurntableRecipeRegistry.getInstance().addTurntableRecipe(BlockBase.CLAY, 3, new ItemInstance(BlockListener.unfiredPottery, 4), new ItemInstance(ItemBase.clay, 1));
        TurntableRecipeRegistry.getInstance().addTurntableRecipe(BlockListener.unfiredPottery, 0, new ItemInstance(BlockListener.unfiredPottery, 4, 1), new ItemInstance(ItemBase.clay, 1));
        TurntableRecipeRegistry.getInstance().addTurntableRecipe(BlockListener.unfiredPottery, 1, new ItemInstance(BlockListener.unfiredPottery, 4, 2));
        TurntableRecipeRegistry.getInstance().addTurntableRecipe(BlockListener.unfiredPottery, 2, new ItemInstance(0, 4, 0), new ItemInstance(ItemBase.clay, 2));
        TurntableRecipeRegistry.getInstance().addRotation(BlockBase.PUMPKIN.id, new Integer[][] {new Integer[] {0, 1, 2, 3}});
        TurntableRecipeRegistry.getInstance().addRotation(BlockBase.JACK_O_LANTERN.id, new Integer[][] {new Integer[] {0, 1, 2, 3}});
        TurntableRecipeRegistry.getInstance().addRotation(BlockBase.REDSTONE_REPEATER.id, new Integer[][] {
                new Integer[] {0, 1, 2, 3},
                new Integer[] {4, 5, 6, 7},
                new Integer[] {8, 9, 10, 11},
                new Integer[] {12, 13, 14, 15}
        });
        TurntableRecipeRegistry.getInstance().addRotation(BlockBase.REDSTONE_REPEATER_LIT.id, new Integer[][] {
                new Integer[] {0, 1, 2, 3},
                new Integer[] {4, 5, 6, 7},
                new Integer[] {8, 9, 10, 11},
                new Integer[] {12, 13, 14, 15}
        });
        TurntableRecipeRegistry.getInstance().addRotation(BlockBase.PISTON.id, new Integer[][] {new Integer[] {4, 2, 5, 3}});
        TurntableRecipeRegistry.getInstance().addRotation(BlockBase.STICKY_PISTON.id, new Integer[][] {new Integer[] {4, 2, 5, 3}});
        TurntableRecipeRegistry.getInstance().addRotation(BlockBase.FURNACE.id, new Integer[][] {new Integer[] {4, 2, 5, 3}});
        TurntableRecipeRegistry.getInstance().addRotation(BlockBase.FURNACE_LIT.id, new Integer[][] {new Integer[] {4, 2, 5, 3}});
        TurntableRecipeRegistry.getInstance().addRotation(BlockBase.DISPENSER.id, new Integer[][] {new Integer[] {4, 2, 5, 3}});
    }

    private static void addCrucibleRecipes() {
        // Material processing
        addCrucibleRecipe(new ItemInstance(ItemListener.steel, 4), new ItemInstance[] { new ItemInstance(ItemBase.ironIngot, 3), new ItemInstance(ItemListener.concentratedHellfire), new ItemInstance(ItemListener.coalDust) });

        // Recycling (iron)
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 9), new ItemInstance[] { new ItemInstance(BlockBase.IRON_BLOCK) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 8), new ItemInstance[] { new ItemInstance(ItemBase.ironChestplate) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 7), new ItemInstance[] { new ItemInstance(ItemBase.ironLeggings) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 6), new ItemInstance[] { new ItemInstance(ItemBase.ironDoor) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 6), new ItemInstance[] { new ItemInstance(BlockBase.RAIL, 16) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 5), new ItemInstance[] { new ItemInstance(ItemBase.ironHelmet) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 5), new ItemInstance[] { new ItemInstance(ItemBase.minecart) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 5), new ItemInstance[] { new ItemInstance(ItemBase.minecartChest) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 5), new ItemInstance[] { new ItemInstance(ItemBase.minecartFurnace) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 4), new ItemInstance[] { new ItemInstance(ItemBase.ironBoots) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 4), new ItemInstance[] { new ItemInstance(ItemBase.compass) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 4), new ItemInstance[] { new ItemInstance(ItemBase.map) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 3), new ItemInstance[] { new ItemInstance(ItemBase.bucket) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 3), new ItemInstance[] { new ItemInstance(ItemBase.lavaBucket) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 3), new ItemInstance[] { new ItemInstance(ItemBase.waterBucket) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 3), new ItemInstance[] { new ItemInstance(ItemBase.milk) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 3), new ItemInstance[] { new ItemInstance(ItemListener.bucketCement) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 3), new ItemInstance[] { new ItemInstance(ItemBase.ironPickaxe) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 3), new ItemInstance[] { new ItemInstance(ItemBase.ironAxe) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 2), new ItemInstance[] { new ItemInstance(ItemBase.ironSword) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 2), new ItemInstance[] { new ItemInstance(ItemBase.ironHoe) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 2), new ItemInstance[] { new ItemInstance(ItemBase.shears) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 1), new ItemInstance[] { new ItemInstance(ItemBase.ironShovel) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 1), new ItemInstance[] { new ItemInstance(ItemBase.flintAndSteel) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 1), new ItemInstance[] { new ItemInstance(BlockBase.DETECTOR_RAIL) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 1), new ItemInstance[] { new ItemInstance(BlockListener.detectorRailWood) });
        addCrucibleRecipe(new ItemInstance(ItemBase.ironIngot, 1), new ItemInstance[] { new ItemInstance(BlockListener.detectorRailObsidian) });

        // Recycling (gold)
        addCrucibleRecipe(new ItemInstance(ItemBase.goldIngot, 9), new ItemInstance[] { new ItemInstance(BlockBase.GOLD_BLOCK) });
        addCrucibleRecipe(new ItemInstance(ItemBase.goldIngot, 8), new ItemInstance[] { new ItemInstance(ItemBase.goldChestplate) });
        addCrucibleRecipe(new ItemInstance(ItemBase.goldIngot, 7), new ItemInstance[] { new ItemInstance(ItemBase.goldLeggings) });
        addCrucibleRecipe(new ItemInstance(ItemBase.goldIngot, 5), new ItemInstance[] { new ItemInstance(ItemBase.goldHelmet) });
        addCrucibleRecipe(new ItemInstance(ItemBase.goldIngot, 4), new ItemInstance[] { new ItemInstance(ItemBase.goldBoots) });
        addCrucibleRecipe(new ItemInstance(ItemBase.goldIngot, 4), new ItemInstance[] { new ItemInstance(ItemBase.clock) });
        addCrucibleRecipe(new ItemInstance(ItemBase.goldIngot, 3), new ItemInstance[] { new ItemInstance(ItemBase.goldPickaxe) });
        addCrucibleRecipe(new ItemInstance(ItemBase.goldIngot, 3), new ItemInstance[] { new ItemInstance(ItemBase.goldAxe) });
        addCrucibleRecipe(new ItemInstance(ItemBase.goldIngot, 2), new ItemInstance[] { new ItemInstance(ItemBase.goldSword) });
        addCrucibleRecipe(new ItemInstance(ItemBase.goldIngot, 2), new ItemInstance[] { new ItemInstance(ItemBase.goldHoe) });
        addCrucibleRecipe(new ItemInstance(ItemBase.goldIngot, 1), new ItemInstance[] { new ItemInstance(ItemBase.goldShovel) });
        addCrucibleRecipe(new ItemInstance(ItemBase.goldIngot, 1), new ItemInstance[] { new ItemInstance(BlockBase.GOLDEN_RAIL) });

        // Recycling (steel)
        addCrucibleRecipe(new ItemInstance(ItemListener.steel, 8), new ItemInstance[] { new ItemInstance(ItemListener.chestPlateSteel) });
        addCrucibleRecipe(new ItemInstance(ItemListener.steel, 7), new ItemInstance[] { new ItemInstance(BlockListener.anvil) });
        addCrucibleRecipe(new ItemInstance(ItemListener.steel, 7), new ItemInstance[] { new ItemInstance(ItemListener.leggingsSteel) });
        addCrucibleRecipe(new ItemInstance(ItemListener.steel, 5), new ItemInstance[] { new ItemInstance(ItemListener.helmetSteel) });
        addCrucibleRecipe(new ItemInstance(ItemListener.steel, 4), new ItemInstance[] { new ItemInstance(ItemListener.bootsSteel) });
        addCrucibleRecipe(new ItemInstance(ItemListener.steel, 3), new ItemInstance[] { new ItemInstance(ItemListener.refinedPickAxe) });
        addCrucibleRecipe(new ItemInstance(ItemListener.steel, 3), new ItemInstance[] { new ItemInstance(ItemListener.refinedAxe) });
        addCrucibleRecipe(new ItemInstance(ItemListener.steel, 2), new ItemInstance[] { new ItemInstance(ItemListener.refinedSword) });
        addCrucibleRecipe(new ItemInstance(ItemListener.steel, 2), new ItemInstance[] { new ItemInstance(ItemListener.refinedHoe) });
        addCrucibleRecipe(new ItemInstance(ItemListener.steel, 1), new ItemInstance[] { new ItemInstance(ItemListener.refinedShovel) });
        addCrucibleRecipe(new ItemInstance(ItemListener.steel, 1), new ItemInstance[] { new ItemInstance(ItemListener.armourPlateSteel) });
        addCrucibleRecipe(new ItemInstance(ItemListener.steel, 1), new ItemInstance[] { new ItemInstance(ItemListener.broadHeadArrowhead, 4) });
        addCrucibleRecipe(new ItemInstance(ItemListener.steel, 1), new ItemInstance[] { new ItemInstance(ItemListener.broadHeadArrow, 16) });

    }

    private static void addHopperRecipes() {
        HopperHauntingRecipeRegistry.getInstance().addHopperHauntingRecipe(ItemListener.groundNetherrack.id, new ItemInstance(ItemListener.hellfireDust, 1));
    }

    private static void addSawingRecipes() {
        SawingRecipeRegistry.getInstance().addSawingRecipe(BlockBase.LOG.id, new ItemInstance(BlockBase.WOOD, 4));
        SawingRecipeRegistry.getInstance().addSawingRecipe(BlockBase.WOOD.id, new ItemInstance(BlockListener.omniSlab, 2, 1));
        SawingRecipeRegistry.getInstance().addSawingRecipe(BlockListener.omniSlab.id, new ItemInstance(BlockListener.moulding, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(BlockListener.moulding.id, new ItemInstance(BlockListener.corner, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(BlockListener.corner.id, new ItemInstance(ItemListener.gear, 2));
    }

    private static void addDebugRecipes() {
    }
}
