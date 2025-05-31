package net.kozibrodka.wolves.events;

import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.recipe.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.WoolBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;
import net.modificationstation.stationapi.api.recipe.SmeltingRegistry;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

public class RecipeListener {

    static boolean isHarderThanWolvesPresent = FabricLoader.getInstance().isModLoaded("harderthanwolves");

    @Entrypoint.Namespace
    public static Namespace MOD_ID;

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
            addShapedAnvilRecipes();
        }
        if (type == RecipeRegisterEvent.Vanilla.CRAFTING_SHAPELESS.type()) {
            addDyeRecipes();
            addShapelessCraftingRecipes();
            addConversionRecipes();
            addShapelessAnvilRecipes();
            addCauldronRecipes();
            addMillingRecipes();
            addHopperRecipes();
            addSawingRecipes();
            addTurntableRecipes();
        }
        if (type == RecipeRegisterEvent.Vanilla.SMELTING.type()) {
            addCrucibleRecipes();
        }
        addDebugRecipes();
    }

    private static void addAnvilRecipe(ItemStack output, Object[] inputs) {
        AnvilCraftingManager.getInstance().addRecipe(output, inputs);
    }

    private static void addShapelessAnvilRecipe(ItemStack output, Object[] inputs) {
        AnvilCraftingManager.getInstance().addShapelessRecipe(output, inputs);
    }

    public static void addCauldronRecipe(ItemStack output, ItemStack[] inputs) {
        CauldronCraftingManager.getInstance().addRecipe(output, inputs);
    }

    public static void addStokedCauldronRecipe(ItemStack output, ItemStack[] inputs) {
        StokedCauldronCraftingManager.getInstance().addRecipe(output, inputs);
    }

    public static void addCrucibleRecipe(ItemStack output, ItemStack[] inputs) {
        CrucibleCraftingManager.getInstance().addRecipe(output, inputs);
    }

    private static void addBlockRecipes() {
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.anvil, 1), "###", " # ", "###", '#', ItemListener.steel);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.lightBulbOff, 1), " # ", "#X#", " Y ", '#', Block.GLASS, 'X', Item.GLOWSTONE_DUST, 'Y', Item.REDSTONE);
        if (!isHarderThanWolvesPresent) {
            CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.hibachi), "XXX", "#Z#", "#Y#", '#', Block.STONE, 'X', ItemListener.concentratedHellfire, 'Y', Item.REDSTONE, 'Z',
                    Item.GLOWSTONE_DUST);
        }
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.hopper), "# #", "XYX", " Z ", '#', new ItemStack(BlockListener.omniSlab, 1, 1), 'X', ItemListener.gear, 'Y', Block.STONE_PRESSURE_PLATE, 'Z',
                BlockListener.corner);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.saw), "YYY", "XZX", "#X#", '#', Block.PLANKS, 'X', ItemListener.gear, 'Y', Item.IRON_INGOT, 'Z',
                ItemListener.belt);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.platform), "#X#", " # ", "#X#", '#', Block.PLANKS, 'X', ItemListener.wicker);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.platform), "X#X", " X ", "X#X", '#', ItemListener.wicker, 'X', BlockListener.moulding);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.pulley), "#Y#", "XZX", "#Y#", '#', Block.PLANKS, 'X', ItemListener.gear, 'Y', Item.IRON_INGOT, 'Z',
                Item.REDSTONE);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.pressurePlateObsidian, 1), "##", '#', Block.OBSIDIAN);
        if (!isHarderThanWolvesPresent) {
            CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.blockDispenser), "###", "# #", "YXY", '#', Block.MOSSY_COBBLESTONE, 'X', Item.REDSTONE, 'Y', Block.STONE);
        }
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.cauldron), "#Y#", "#X#", "###", '#', Item.IRON_INGOT, 'X', Item.BONE, 'Y', Item.WATER_BUCKET);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.detectorRailWood, 6), "X X", "X#X", "XRX", 'X', Item.IRON_INGOT, 'R', Item.REDSTONE, '#', Block.STONE_PRESSURE_PLATE);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.detectorRailObsidian, 6), "X X", "X#X", "XRX", 'X', Item.IRON_INGOT, 'R', Item.REDSTONE, '#', BlockListener.pressurePlateObsidian);
        if (!isHarderThanWolvesPresent) {
            CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.detectorBlock), "X#X", "###", "ZYZ", '#', Block.COBBLESTONE, 'X', new ItemStack(Item.DYE, 1, 4), 'Y', Item.REDSTONE, 'Z',
                    Block.STONE);
        }
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.handCrank), "  Y", " Y ", "#X#", '#', Block.COBBLESTONE, 'X', ItemListener.gear, 'Y', Item.STICK);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.millStone), "YYY", "YYY", "YXY", 'X', ItemListener.gear, 'Y', Block.STONE);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.axleBlock), "#", "X", "#", '#', Block.PLANKS, 'X', ItemListener.ropeItem);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.axleBlock), "#", "X", "#", '#', BlockListener.moulding, 'X', ItemListener.ropeItem);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.gearBox), "#X#", "XYX", "#X#", '#', Block.PLANKS, 'X', ItemListener.gear, 'Y', Item.REDSTONE);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.gearBox), "#X#", "XYX", "#X#", '#', new ItemStack(BlockListener.omniSlab, 1, 1), 'X', ItemListener.gear, 'Y', Item.REDSTONE);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.turntable), "###", "ZXZ", "ZYZ", '#', new ItemStack(BlockListener.omniSlab, 1, 1), 'X', Item.REDSTONE, 'Y', ItemListener.gear, 'Z',
                Block.STONE);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.bellows), "###", "XXX", "YZY", '#', new ItemStack(BlockListener.omniSlab, 1, 1), 'X', ItemListener.tannedLeather, 'Y', ItemListener.gear, 'Z',
                ItemListener.belt);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.planter, 1, 1), "Y", "X", "#", '#', new ItemStack(BlockListener.planter, 1, 0), 'X', Item.WATER_BUCKET, 'Y', Block.DIRT);
        if (ConfigListener.wolvesGlass.small_tweaks.wickerBlock) {
            CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.blockOfWicker, 1), "XX", "XX", 'X', ItemListener.wicker);
        }
        if (ConfigListener.wolvesGlass.small_tweaks.grateBlock) {
            CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.blockOfGrates, 1), "XX", "XX", 'X', ItemListener.grate);
        }
    }

    private static void addItemRecipes() {
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.bucketCement, 1), "#YX", " Z ", '#', Block.SOUL_SAND, 'X', Block.GRAVEL, 'Y', Item.WATER_BUCKET, 'Z', Item.BUCKET);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.gear, 2), " X ", "X#X", " X ", '#', Block.PLANKS, 'X', Item.STICK);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.ropeItem, 1), "##", "##", "##", '#', ItemListener.hempFibers);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.anchor), " X ", "###", '#', Block.STONE, 'X', Item.IRON_INGOT);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.waterWheelItem), "###", "# #", "###", '#', ItemListener.woodBlade);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.windMillBladeItem), "###", "###", "XXX", '#', ItemListener.hempCloth, 'X', BlockListener.moulding);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.windMillBladeItem), "###", "###", "XXX", '#', ItemListener.hempCloth, 'X', Block.PLANKS);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.windMillItem), " # ", "# #", " # ", '#', ItemListener.windMillBladeItem);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.hempCloth, 1), "###", "###", "###", '#', ItemListener.hempFibers);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.grate, 1), "###", "###", "###", '#', Item.STICK);
        if (!ConfigListener.wolvesGlass.difficulty.wickerWeaving) {
            CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.wicker, 1), "###", "###", "###", '#', Item.SUGAR_CANE);
        }
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.rollersItem, 1), "###", "###", "###", '#', BlockListener.moulding);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.strap, 8), "#", '#', ItemListener.tannedLeather);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.belt, 1), " # ", "# #", " # ", '#', ItemListener.strap);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.woodBlade, 1), "#  ", "#X#", "#  ", '#', new ItemStack(BlockListener.omniSlab, 1, 1), 'X', Item.SLIMEBALL);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.woodBlade, 1), "#  ", "#X#", "#  ", '#', new ItemStack(BlockListener.omniSlab, 1, 1), 'X', ItemListener.glue);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.haft, 1), "Y", "X", "#", '#', BlockListener.moulding, 'X', ItemListener.glue, 'Y', ItemListener.strap);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.haft, 1), "Y", "X", "#", '#', BlockListener.moulding, 'X', Item.SLIMEBALL, 'Y', ItemListener.strap);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.compositeBow, 1), "X#Y", "ZX#", "X#Y", '#', BlockListener.moulding, 'X', Item.BONE, 'Y', Item.SLIMEBALL, 'Z',
                Item.STRING);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.compositeBow, 1), "X#Y", "ZX#", "X#Y", '#', BlockListener.moulding, 'X', Item.BONE, 'Y', ItemListener.glue, 'Z',
                Item.STRING);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.broadHeadArrow, 4), "#", "X", "Y", '#', ItemListener.broadHeadArrowhead, 'X', BlockListener.moulding, 'Y', Item.FEATHER);
        if (ConfigListener.wolvesGlass.difficulty.wickerWeaving) {
            CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.wickerWeaving), "###", "###", "###", '#', Item.SUGAR_CANE);
        }
    }

    private static void addDyeRecipes() {
        for(int i = 0; i < 15; i++) {
            CraftingRegistry.addShapelessRecipe(new ItemStack(BlockListener.vase, 1, WoolBlock.method_1(i)), new ItemStack(Item.DYE, 1, i), new ItemStack(BlockListener.vase, 1, 0));
        }

        if (ConfigListener.wolvesGlass.small_tweaks.deactivateDung) {
            CraftingRegistry.addShapelessRecipe(new ItemStack(Block.WOOL, 1, 12), new ItemStack(Block.LOG), new ItemStack(Block.WOOL, 1, 0));
            CraftingRegistry.addShapelessRecipe(new ItemStack(BlockListener.vase, 1, 12), new ItemStack(Block.LOG), new ItemStack(BlockListener.vase, 1, 0));
        } else {
            CraftingRegistry.addShapelessRecipe(new ItemStack(Block.WOOL, 1, 12), new ItemStack(ItemListener.dung), new ItemStack(Block.WOOL, 1, 0));
            CraftingRegistry.addShapelessRecipe(new ItemStack(BlockListener.vase, 1, 12), new ItemStack(ItemListener.dung), new ItemStack(BlockListener.vase, 1, 0));
        }
    }

    private static void addAlternateVanillaRecipes() {
        CraftingRegistry.addShapedRecipe(new ItemStack(Block.STICKY_PISTON, 1), "#", "X", '#', ItemListener.glue, 'X', Block.PISTON);
        CraftingRegistry.addShapedRecipe(new ItemStack(Block.FENCE, 2), "###", '#', BlockListener.moulding);
        CraftingRegistry.addShapedRecipe(new ItemStack(Block.WOODEN_STAIRS, 1), "# ", "##", '#', BlockListener.moulding);
        CraftingRegistry.addShapedRecipe(new ItemStack(Item.SIGN, 1), "###", "###", " X ", '#', new ItemStack(BlockListener.omniSlab, 1, 1), 'X', Item.STICK);
        CraftingRegistry.addShapedRecipe(new ItemStack(Item.WOODEN_DOOR, 1), "##", "##", "##", '#', new ItemStack(BlockListener.omniSlab, 1, 1));
        CraftingRegistry.addShapedRecipe(new ItemStack(Block.TRAPDOOR, 2), "###", "###", '#', new ItemStack(BlockListener.omniSlab, 1, 1));
        CraftingRegistry.addShapedRecipe(new ItemStack(Item.CAKE, 1), "AAA", "BEB", "CCC", 'A', Item.SUGAR, 'B', Item.MILK_BUCKET, 'C', ItemListener.flour, 'E',
                Item.EGG);
        CraftingRegistry.addShapedRecipe(new ItemStack(Block.TORCH, 4), "#", "X", '#', ItemListener.netherCoal, 'X', Item.STICK);
    }

    private static void addShapelessCraftingRecipes() {
        if (ConfigListener.wolvesGlass.difficulty.wickerWeaving) {
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.wicker), new ItemStack(ItemListener.wickerSheet), new ItemStack(ItemListener.grate));
        }
    }

    private static void addConversionRecipes() {
        CraftingRegistry.addShapelessRecipe(new ItemStack(Block.PLANKS), new ItemStack(BlockListener.omniSlab, 1, 1), new ItemStack(BlockListener.omniSlab, 1, 1));
        CraftingRegistry.addShapelessRecipe(new ItemStack(Block.STONE), new ItemStack(BlockListener.omniSlab, 1, 0), new ItemStack(BlockListener.omniSlab, 1, 0));
        CraftingRegistry.addShapelessRecipe(new ItemStack(BlockListener.omniSlab, 1, 1), new ItemStack(BlockListener.moulding,1,0), new ItemStack(BlockListener.moulding,1,0));
        CraftingRegistry.addShapelessRecipe(new ItemStack(BlockListener.moulding,1,0), new ItemStack(BlockListener.corner,1,0), new ItemStack(BlockListener.corner,1,0));
        CraftingRegistry.addShapelessRecipe(new ItemStack(Item.STRING), new ItemStack(ItemListener.hempFibers), new ItemStack(ItemListener.hempFibers));
        CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.steel, 9), new ItemStack(BlockListener.blockOfSteel));
    }

    private static void addShapedAnvilRecipes() {
        addAnvilRecipe(new ItemStack(ItemListener.refinedPickAxe, 1), new Object[] {
                "###", " X ", " X ", '#', ItemListener.steel, 'X', ItemListener.haft
        });
        addAnvilRecipe(new ItemStack(ItemListener.refinedShovel, 1), new Object[] {
                "#", "X", "X", '#', ItemListener.steel, 'X', ItemListener.haft
        });
        addAnvilRecipe(new ItemStack(ItemListener.refinedHoe, 1), new Object[] {
                "##", " X", " X", '#', ItemListener.steel, 'X', ItemListener.haft
        });
        addAnvilRecipe(new ItemStack(ItemListener.refinedAxe, 1), new Object[] {
                "###", "#X#", " X ", '#', ItemListener.steel, 'X', ItemListener.haft
        });
        addAnvilRecipe(new ItemStack(ItemListener.refinedSword, 1), new Object[] {
                "#", "#", "X", '#', ItemListener.steel, 'X', ItemListener.haft
        });
        addAnvilRecipe(new ItemStack(ItemListener.armourPlateSteel, 1), new Object[] {
                "#X#", '#', ItemListener.strap, 'X', ItemListener.steel
        });
        addAnvilRecipe(new ItemStack(ItemListener.helmetSteel, 1), new Object[] {
                "###", "#X#", '#', ItemListener.steel, 'X', ItemListener.armourPlateSteel
        });
        addAnvilRecipe(new ItemStack(ItemListener.chestPlateSteel, 1), new Object[] {
                "X X", "###", "###", '#', ItemListener.steel, 'X', ItemListener.armourPlateSteel
        });
        addAnvilRecipe(new ItemStack(ItemListener.leggingsSteel, 1), new Object[] {
                "###", "X X", "X X", '#', ItemListener.steel, 'X', ItemListener.armourPlateSteel
        });
        addAnvilRecipe(new ItemStack(ItemListener.bootsSteel, 1), new Object[] {
                "# #", "# #", '#', ItemListener.steel
        });
        if (ConfigListener.wolvesGlass.small_tweaks.steelBlock) {
            addAnvilRecipe(new ItemStack(BlockListener.blockOfSteel, 1), new Object[] {
                    "###", "###", "###", '#', ItemListener.steel
            });
        }
    }

    private static void addShapelessAnvilRecipes() {
        addShapelessAnvilRecipe(new ItemStack(ItemListener.broadHeadArrowhead, 4), new Object[] {
                ItemListener.steel
        });
        addShapelessAnvilRecipe(new ItemStack(BlockListener.omniSlab, 1, 0), new Object[] {
                Block.STONE
        });
    }

    private static void addCauldronRecipes() {
        addCauldronRecipe(new ItemStack(ItemListener.netherCoal, 2), new ItemStack[] {
                new ItemStack(ItemListener.hellfireDust, 4), new ItemStack(ItemListener.coalDust, 1)
        });
        addCauldronRecipe(new ItemStack(ItemListener.concentratedHellfire, 1), new ItemStack[] {
                new ItemStack(ItemListener.hellfireDust, 8)
        });
        if (ConfigListener.wolvesGlass.small_tweaks.deactivateDung) {
            addCauldronRecipe(new ItemStack(ItemListener.tannedLeather, 1), new ItemStack[]{
                    new ItemStack(Block.LOG, 1), new ItemStack(ItemListener.scouredLeather, 1)
            });
            addCauldronRecipe(new ItemStack(Block.WOOL, 8, 12), new ItemStack[] {
                    new ItemStack(Block.LOG, 1), new ItemStack(Block.WOOL, 8, 0)
            });
        } else {
            addCauldronRecipe(new ItemStack(ItemListener.tannedLeather, 1), new ItemStack[]{
                    new ItemStack(ItemListener.dung, 1), new ItemStack(ItemListener.scouredLeather, 1)
            });
            addCauldronRecipe(new ItemStack(Block.WOOL, 8, 12), new ItemStack[] {
                    new ItemStack(ItemListener.dung, 1), new ItemStack(Block.WOOL, 8, 0)
            });
        }
        addCauldronRecipe(new ItemStack(ItemListener.donut, 4), new ItemStack[] {
                new ItemStack(ItemListener.flour, 1)
        });
        for(int i = 0; i < 15; i++) {
            addCauldronRecipe(new ItemStack(Block.WOOL, 8, WoolBlock.method_1(i)), new ItemStack[] {
                    new ItemStack(Item.DYE, 1, i), new ItemStack(Block.WOOL, 8, 0)
            });
        }
        addCauldronRecipe(new ItemStack(Block.TNT), new ItemStack[] {
                new ItemStack(Block.SAND, 4), new ItemStack(Item.GUNPOWDER, 5)
        });
        addCauldronRecipe(new ItemStack(Block.WOOL), new ItemStack[] {
                new ItemStack(Item.STRING, 4)
        });
        addCauldronRecipe(new ItemStack(Item.DYE, 1, 2), new ItemStack[] {
                new ItemStack(Block.CACTUS)
        });
        if (!isHarderThanWolvesPresent) {
            addCauldronRecipe(new ItemStack(ItemListener.soulFilter, 1), new ItemStack[]{
                    new ItemStack(ItemListener.hempCloth, 2), new ItemStack(Block.SOUL_SAND, 1)
            });
        }
        addStokedCauldronRecipe(new ItemStack(ItemListener.glue, 1), new ItemStack[] {
                new ItemStack(Item.LEATHER, 1)
        });
        addStokedCauldronRecipe(new ItemStack(ItemListener.glue, 1), new ItemStack[] {
                new ItemStack(ItemListener.tannedLeather, 1)
        });
        addStokedCauldronRecipe(new ItemStack(ItemListener.glue, 1), new ItemStack[] {
                new ItemStack(ItemListener.scouredLeather, 1)
        });
        addStokedCauldronRecipe(new ItemStack(ItemListener.glue, 1), new ItemStack[] {
                new ItemStack(ItemListener.belt, 2)
        });
        addStokedCauldronRecipe(new ItemStack(ItemListener.glue, 1), new ItemStack[] {
                new ItemStack(Item.BONE, 8)
        });
        addStokedCauldronRecipe(new ItemStack(ItemListener.glue, 1), new ItemStack[] {
                new ItemStack(ItemListener.strap, 8)
        });
        addStokedCauldronRecipe(new ItemStack(ItemListener.glue, 1), new ItemStack[] {
                new ItemStack(Block.WOOL, 16)
        });
        addStokedCauldronRecipe(new ItemStack(ItemListener.glue, 1), new ItemStack[] {
                new ItemStack(Item.FEATHER, 16)
        });
        addStokedCauldronRecipe(new ItemStack(ItemListener.glue, 1), new ItemStack[] {
                new ItemStack(Item.STRING, 64)
        });
        addStokedCauldronRecipe(new ItemStack(ItemListener.glue, 5), new ItemStack[] {
                new ItemStack(Item.LEATHER_HELMET, 1)
        });
        addStokedCauldronRecipe(new ItemStack(ItemListener.glue, 8), new ItemStack[] {
                new ItemStack(Item.LEATHER_CHESTPLATE, 1)
        });
        addStokedCauldronRecipe(new ItemStack(ItemListener.glue, 7), new ItemStack[] {
                new ItemStack(Item.LEATHER_LEGGINGS, 1)
        });
        addStokedCauldronRecipe(new ItemStack(ItemListener.glue, 4), new ItemStack[] {
                new ItemStack(Item.LEATHER_BOOTS, 1)
        });
        addStokedCauldronRecipe(new ItemStack(ItemListener.tallow, 1), new ItemStack[] {
                new ItemStack(Item.COOKED_PORKCHOP, 1)
        });
        addStokedCauldronRecipe(new ItemStack(ItemListener.tallow, 1), new ItemStack[] {
                new ItemStack(Item.RAW_PORKCHOP, 1)
        });
        addStokedCauldronRecipe(new ItemStack(ItemListener.tallow, 1), new ItemStack[] {
                new ItemStack(ItemListener.wolfCooked, 1)
        });
        addStokedCauldronRecipe(new ItemStack(ItemListener.tallow, 1), new ItemStack[] {
                new ItemStack(ItemListener.wolfRaw, 1)
        });
    }

    private static void addMillingRecipes() {
        MillingRecipeRegistry.getInstance().addMillingRecipe(ItemRegistry.INSTANCE.getId(Item.WHEAT), new ItemStack(ItemListener.flour, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(ItemRegistry.INSTANCE.getId(Item.LEATHER), new ItemStack(ItemListener.scouredLeather, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(ItemListener.MOD_ID.id("hemp"), new ItemStack(ItemListener.hempFibers, 4));
        MillingRecipeRegistry.getInstance().addMillingRecipe(ItemRegistry.INSTANCE.getId(Item.SUGAR_CANE), new ItemStack(Item.SUGAR, 1));
        if (!isHarderThanWolvesPresent) {
            MillingRecipeRegistry.getInstance().addMillingRecipe(BlockRegistry.INSTANCE.getId(Block.NETHERRACK), new ItemStack(ItemListener.groundNetherrack, 1));
        }
        MillingRecipeRegistry.getInstance().addMillingRecipe(ItemRegistry.INSTANCE.getId(Item.BONE), new ItemStack(Item.DYE, 3, 15));
        MillingRecipeRegistry.getInstance().addMillingRecipe(ItemRegistry.INSTANCE.getId(Item.COAL), new ItemStack(ItemListener.coalDust, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(BlockRegistry.INSTANCE.getId(Block.ROSE), new ItemStack(Item.DYE, 2, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(BlockRegistry.INSTANCE.getId(Block.DANDELION), new ItemStack(Item.DYE, 2, 11));
    }

    private static void addTurntableRecipes() {
        TurntableRecipeRegistry.getInstance().addTurntableRecipe(Block.CLAY, 0, new ItemStack(BlockListener.unfiredPottery, 16), new ItemStack(Item.CLAY, 1));
        TurntableRecipeRegistry.getInstance().addTurntableRecipe(BlockListener.unfiredPottery, 0, new ItemStack(BlockListener.unfiredPottery, 4, 1), new ItemStack(Item.CLAY, 1));
        TurntableRecipeRegistry.getInstance().addTurntableRecipe(BlockListener.unfiredPottery, 1, new ItemStack(BlockListener.unfiredPottery, 4, 2));
        TurntableRecipeRegistry.getInstance().addTurntableRecipe(BlockListener.unfiredPottery, 2, new ItemStack(ItemListener.nothing, 4, 0), new ItemStack(Item.CLAY, 2));
        TurntableRecipeRegistry.getInstance().addRotation(Block.PUMPKIN.id, new Integer[][] {new Integer[] {0, 1, 2, 3}});
        TurntableRecipeRegistry.getInstance().addRotation(Block.JACK_O_LANTERN.id, new Integer[][] {new Integer[] {0, 1, 2, 3}});
        TurntableRecipeRegistry.getInstance().addRotation(Block.REPEATER.id, new Integer[][] {
                new Integer[] {0, 1, 2, 3},
                new Integer[] {4, 5, 6, 7},
                new Integer[] {8, 9, 10, 11},
                new Integer[] {12, 13, 14, 15}
        });
        TurntableRecipeRegistry.getInstance().addRotation(Block.POWERED_REPEATER.id, new Integer[][] {
                new Integer[] {0, 1, 2, 3},
                new Integer[] {4, 5, 6, 7},
                new Integer[] {8, 9, 10, 11},
                new Integer[] {12, 13, 14, 15}
        });
        TurntableRecipeRegistry.getInstance().addRotation(Block.PISTON.id, new Integer[][] {new Integer[] {4, 2, 5, 3}});
        TurntableRecipeRegistry.getInstance().addRotation(Block.STICKY_PISTON.id, new Integer[][] {new Integer[] {4, 2, 5, 3}});
        TurntableRecipeRegistry.getInstance().addRotation(Block.FURNACE.id, new Integer[][] {new Integer[] {4, 2, 5, 3}});
        TurntableRecipeRegistry.getInstance().addRotation(Block.LIT_FURNACE.id, new Integer[][] {new Integer[] {4, 2, 5, 3}});
        TurntableRecipeRegistry.getInstance().addRotation(Block.DISPENSER.id, new Integer[][] {new Integer[] {4, 2, 5, 3}});
    }

    private static void addCrucibleRecipes() {
        // Material processing
        if (!isHarderThanWolvesPresent) {
            addCrucibleRecipe(new ItemStack(ItemListener.steel, 4), new ItemStack[]{new ItemStack(Item.IRON_INGOT, 3), new ItemStack(ItemListener.concentratedHellfire), new ItemStack(ItemListener.coalDust)});
        }

        // Recycling (iron)
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 9), new ItemStack[] { new ItemStack(Block.IRON_BLOCK) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 8), new ItemStack[] { new ItemStack(Item.IRON_CHESTPLATE, 1, -1) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 7), new ItemStack[] { new ItemStack(Item.IRON_LEGGINGS, 1, -1) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 6), new ItemStack[] { new ItemStack(Item.IRON_DOOR) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 6), new ItemStack[] { new ItemStack(Block.RAIL, 16) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 5), new ItemStack[] { new ItemStack(Item.IRON_HELMET, 1, -1) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 5), new ItemStack[] { new ItemStack(Item.MINECART) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 5), new ItemStack[] { new ItemStack(Item.CHEST_MINECART) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 5), new ItemStack[] { new ItemStack(Item.FURNACE_MINECART) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 4), new ItemStack[] { new ItemStack(Item.IRON_BOOTS, 1, -1) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 4), new ItemStack[] { new ItemStack(Item.COMPASS) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 4), new ItemStack[] { new ItemStack(Item.MAP) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 3), new ItemStack[] { new ItemStack(Item.BUCKET) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 3), new ItemStack[] { new ItemStack(Item.LAVA_BUCKET) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 3), new ItemStack[] { new ItemStack(Item.WATER_BUCKET) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 3), new ItemStack[] { new ItemStack(Item.MILK_BUCKET) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 3), new ItemStack[] { new ItemStack(ItemListener.bucketCement) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 3), new ItemStack[] { new ItemStack(Item.IRON_PICKAXE, 1, -1) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 3), new ItemStack[] { new ItemStack(Item.IRON_AXE, 1, -1) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 2), new ItemStack[] { new ItemStack(Item.IRON_SWORD, 1, -1) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 2), new ItemStack[] { new ItemStack(Item.IRON_HOE, 1, -1) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 2), new ItemStack[] { new ItemStack(Item.SHEARS) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 1), new ItemStack[] { new ItemStack(Item.IRON_SHOVEL, 1, -1) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 1), new ItemStack[] { new ItemStack(Item.FLINT_AND_STEEL, 1, -1) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 1), new ItemStack[] { new ItemStack(Block.DETECTOR_RAIL) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 1), new ItemStack[] { new ItemStack(BlockListener.detectorRailWood) });
        addCrucibleRecipe(new ItemStack(Item.IRON_INGOT, 1), new ItemStack[] { new ItemStack(BlockListener.detectorRailObsidian) });

        // Recycling (gold)
        addCrucibleRecipe(new ItemStack(Item.GOLD_INGOT, 9), new ItemStack[] { new ItemStack(Block.GOLD_BLOCK) });
        addCrucibleRecipe(new ItemStack(Item.GOLD_INGOT, 8), new ItemStack[] { new ItemStack(Item.GOLDEN_CHESTPLATE, 1, -1) });
        addCrucibleRecipe(new ItemStack(Item.GOLD_INGOT, 7), new ItemStack[] { new ItemStack(Item.GOLDEN_LEGGINGS, 1, -1) });
        addCrucibleRecipe(new ItemStack(Item.GOLD_INGOT, 5), new ItemStack[] { new ItemStack(Item.GOLDEN_HELMET, 1, -1) });
        addCrucibleRecipe(new ItemStack(Item.GOLD_INGOT, 4), new ItemStack[] { new ItemStack(Item.GOLDEN_BOOTS, 1, -1) });
        addCrucibleRecipe(new ItemStack(Item.GOLD_INGOT, 4), new ItemStack[] { new ItemStack(Item.CLOCK) });
        addCrucibleRecipe(new ItemStack(Item.GOLD_INGOT, 3), new ItemStack[] { new ItemStack(Item.GOLDEN_PICKAXE, 1, -1) });
        addCrucibleRecipe(new ItemStack(Item.GOLD_INGOT, 3), new ItemStack[] { new ItemStack(Item.GOLDEN_AXE, 1, -1) });
        addCrucibleRecipe(new ItemStack(Item.GOLD_INGOT, 2), new ItemStack[] { new ItemStack(Item.GOLDEN_SWORD, 1, -1) });
        addCrucibleRecipe(new ItemStack(Item.GOLD_INGOT, 2), new ItemStack[] { new ItemStack(Item.GOLDEN_HOE, 1, -1) });
        addCrucibleRecipe(new ItemStack(Item.GOLD_INGOT, 1), new ItemStack[] { new ItemStack(Item.GOLDEN_SHOVEL, 1, -1) });
        addCrucibleRecipe(new ItemStack(Item.GOLD_INGOT, 1), new ItemStack[] { new ItemStack(Block.POWERED_RAIL) });

        // Recycling (steel)
        addCrucibleRecipe(new ItemStack(ItemListener.steel, 8), new ItemStack[] { new ItemStack(ItemListener.chestPlateSteel, 1, -1) });
        addCrucibleRecipe(new ItemStack(ItemListener.steel, 7), new ItemStack[] { new ItemStack(BlockListener.anvil) });
        addCrucibleRecipe(new ItemStack(ItemListener.steel, 7), new ItemStack[] { new ItemStack(ItemListener.leggingsSteel, 1, -1) });
        addCrucibleRecipe(new ItemStack(ItemListener.steel, 5), new ItemStack[] { new ItemStack(ItemListener.helmetSteel, 1, -1) });
        addCrucibleRecipe(new ItemStack(ItemListener.steel, 4), new ItemStack[] { new ItemStack(ItemListener.bootsSteel, 1, -1) });
        addCrucibleRecipe(new ItemStack(ItemListener.steel, 3), new ItemStack[] { new ItemStack(ItemListener.refinedPickAxe, 1, -1) });
        addCrucibleRecipe(new ItemStack(ItemListener.steel, 3), new ItemStack[] { new ItemStack(ItemListener.refinedAxe, 1, -1) });
        addCrucibleRecipe(new ItemStack(ItemListener.steel, 2), new ItemStack[] { new ItemStack(ItemListener.refinedSword, 1, -1) });
        addCrucibleRecipe(new ItemStack(ItemListener.steel, 2), new ItemStack[] { new ItemStack(ItemListener.refinedHoe, 1, -1) });
        addCrucibleRecipe(new ItemStack(ItemListener.steel, 1), new ItemStack[] { new ItemStack(ItemListener.refinedShovel, 1, -1) });
        addCrucibleRecipe(new ItemStack(ItemListener.steel, 1), new ItemStack[] { new ItemStack(ItemListener.armourPlateSteel) });
        addCrucibleRecipe(new ItemStack(ItemListener.steel, 1), new ItemStack[] { new ItemStack(ItemListener.broadHeadArrowhead, 4) });
        addCrucibleRecipe(new ItemStack(ItemListener.steel, 1), new ItemStack[] { new ItemStack(ItemListener.broadHeadArrow, 16) });
    }

    private static void addHopperRecipes() {
        HopperPurifyingRecipeRegistry.getInstance().addHopperHauntingRecipe(ItemListener.MOD_ID.id("groundNetherrack"), new ItemStack(ItemListener.hellfireDust, 1));
    }

    private static void addSawingRecipes() {
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(Block.LOG, 1, 0), new ItemStack(Block.PLANKS, 4));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(Block.LOG, 1, 1), new ItemStack(Block.PLANKS, 4));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(Block.LOG, 1, 2), new ItemStack(Block.PLANKS, 4));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(Block.PLANKS, 1), new ItemStack(BlockListener.omniSlab, 2, 1));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.omniSlab, 1, 1), new ItemStack(BlockListener.moulding, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.omniSlab, 1, 3), new ItemStack(BlockListener.moulding, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.omniSlab, 1, 5), new ItemStack(BlockListener.moulding, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.omniSlab, 1, 7), new ItemStack(BlockListener.moulding, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.omniSlab, 1, 9), new ItemStack(BlockListener.moulding, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.omniSlab, 1, 11), new ItemStack(BlockListener.moulding, 2));

        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.moulding, 1, 0), new ItemStack(BlockListener.corner, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.moulding, 1, 1), new ItemStack(BlockListener.corner, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.moulding, 1, 2), new ItemStack(BlockListener.corner, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.moulding, 1, 3), new ItemStack(BlockListener.corner, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.moulding, 1, 4), new ItemStack(BlockListener.corner, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.moulding, 1, 5), new ItemStack(BlockListener.corner, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.moulding, 1, 6), new ItemStack(BlockListener.corner, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.moulding, 1, 7), new ItemStack(BlockListener.corner, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.moulding, 1, 8), new ItemStack(BlockListener.corner, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.moulding, 1, 9), new ItemStack(BlockListener.corner, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.moulding, 1, 10), new ItemStack(BlockListener.corner, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.moulding, 1, 11), new ItemStack(BlockListener.corner, 2));

        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.corner, 1, 0), new ItemStack(ItemListener.gear, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.corner, 1, 1), new ItemStack(ItemListener.gear, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.corner, 1, 2), new ItemStack(ItemListener.gear, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.corner, 1, 3), new ItemStack(ItemListener.gear, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.corner, 1, 4), new ItemStack(ItemListener.gear, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.corner, 1, 5), new ItemStack(ItemListener.gear, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.corner, 1, 6), new ItemStack(ItemListener.gear, 2));
        SawingRecipeRegistry.getInstance().addSawingRecipe(new ItemStack(BlockListener.corner, 1, 7), new ItemStack(ItemListener.gear, 2));

    }

    private static void addDebugRecipes() {
    }
}
