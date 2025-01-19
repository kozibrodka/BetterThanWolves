package net.kozibrodka.wolves.events;

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
            addSmeltingRecipes();
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
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.hibachi), "XXX", "#Z#", "#Y#", '#', Block.STONE, 'X', ItemListener.concentratedHellfire, 'Y', Item.REDSTONE, 'Z',
                Item.GLOWSTONE_DUST);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.hopper), "# #", "XYX", " Z ", '#', new ItemStack(BlockListener.omniSlab.id, 1, 1), 'X', ItemListener.gear, 'Y', Block.STONE_PRESSURE_PLATE, 'Z',
                BlockListener.corner);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.saw), "YYY", "XZX", "#X#", '#', Block.PLANKS, 'X', ItemListener.gear, 'Y', Item.IRON_INGOT, 'Z',
                ItemListener.belt);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.platform), "#X#", " # ", "#X#", '#', Block.PLANKS, 'X', ItemListener.wicker);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.platform), "X#X", " X ", "X#X", '#', ItemListener.wicker, 'X', BlockListener.moulding);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.pulley), "#Y#", "XZX", "#Y#", '#', Block.PLANKS, 'X', ItemListener.gear, 'Y', Item.IRON_INGOT, 'Z',
                Item.REDSTONE);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.pressurePlateObsidian, 1), "##", '#', Block.OBSIDIAN);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.blockDispenser), "###", "# #", "YXY", '#', Block.MOSSY_COBBLESTONE, 'X', Item.REDSTONE, 'Y', Block.STONE);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.cauldron), "#Y#", "#X#", "###", '#', Item.IRON_INGOT, 'X', Item.BONE, 'Y', Item.WATER_BUCKET);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.detectorRailWood, 6), "X X", "X#X", "XRX", 'X', Item.IRON_INGOT, 'R', Item.REDSTONE, '#', Block.STONE_PRESSURE_PLATE);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.detectorRailObsidian, 6), "X X", "X#X", "XRX", 'X', Item.IRON_INGOT, 'R', Item.REDSTONE, '#', BlockListener.pressurePlateObsidian);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.detectorBlock), "X#X", "###", "ZYZ", '#', Block.COBBLESTONE, 'X', new ItemStack(Item.DYE, 1, 4), 'Y', Item.REDSTONE, 'Z',
                Block.STONE);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.handCrank), "  Y", " Y ", "#X#", '#', Block.COBBLESTONE, 'X', ItemListener.gear, 'Y', Item.STICK);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.millStone), "YYY", "YYY", "YXY", 'X', ItemListener.gear, 'Y', Block.STONE);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.axleBlock), "#", "X", "#", '#', Block.PLANKS, 'X', ItemListener.ropeItem);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.axleBlock), "#", "X", "#", '#', BlockListener.moulding, 'X', ItemListener.ropeItem);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.gearBox), "#X#", "XYX", "#X#", '#', Block.PLANKS, 'X', ItemListener.gear, 'Y', Item.REDSTONE);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.gearBox), "#X#", "XYX", "#X#", '#', new ItemStack(BlockListener.omniSlab.id, 1, 1), 'X', ItemListener.gear, 'Y', Item.REDSTONE);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.turntable), "###", "ZXZ", "ZYZ", '#', new ItemStack(BlockListener.omniSlab.id, 1, 1), 'X', Item.REDSTONE, 'Y', ItemListener.gear, 'Z',
                Block.STONE);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.bellows), "###", "XXX", "YZY", '#', new ItemStack(BlockListener.omniSlab.id, 1, 1), 'X', ItemListener.tannedLeather, 'Y', ItemListener.gear, 'Z',
                ItemListener.belt);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.planter, 1, 1), "Y", "X", "#", '#', new ItemStack(BlockListener.planter, 1, 0), 'X', Item.WATER_BUCKET, 'Y', Block.DIRT);
        if (ConfigListener.wolvesGlass.small_tweaks.wickerBlock) {
            CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.blockOfWicker, 1), "XX", "XX", 'X', ItemListener.wicker);
        }
        if (ConfigListener.wolvesGlass.small_tweaks.grateBlock) {
            CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.blockOfGrates, 1), "XX", "XX", 'X', ItemListener.grate);
        }
        if (ConfigListener.wolvesGlass.difficulty.wickerWeaving || ConfigListener.wolvesGlass.difficulty.woolKnitting) {
            CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.bellows), "# #", "X X", "YZY", '#', new ItemStack(BlockListener.moulding), 'X', Item.IRON_INGOT, 'Y', Block.CHEST, 'Z', BlockListener.gearBox);
        }
    }

    private static void addItemRecipes() {
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.bucketCement, 1), "#YX", " Z ", Character.valueOf('#'), Block.SOUL_SAND, Character.valueOf('X'), Block.GRAVEL, Character.valueOf('Y'), Item.WATER_BUCKET, Character.valueOf('Z'), Item.BUCKET);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.gear, 2), " X ", "X#X", " X ", Character.valueOf('#'), Block.PLANKS, Character.valueOf('X'), Item.STICK);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.ropeItem, 1), "##", "##", "##", Character.valueOf('#'), ItemListener.hempFibers);
        CraftingRegistry.addShapedRecipe(new ItemStack(BlockListener.anchor), " X ", "###", Character.valueOf('#'), Block.STONE, Character.valueOf('X'), Item.IRON_INGOT);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.waterWheelItem), "###", "# #", "###", Character.valueOf('#'), ItemListener.woodBlade);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.windMillBladeItem), "###", "###", "XXX", Character.valueOf('#'), ItemListener.hempCloth, Character.valueOf('X'), BlockListener.moulding);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.windMillBladeItem), "###", "###", "XXX", Character.valueOf('#'), ItemListener.hempCloth, Character.valueOf('X'), Block.PLANKS);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.windMillItem), " # ", "# #", " # ", Character.valueOf('#'), ItemListener.windMillBladeItem);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.hempCloth, 1), "###", "###", "###", Character.valueOf('#'), ItemListener.hempFibers);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.grate, 1), "###", "###", "###", Character.valueOf('#'), Item.STICK);
        if (!ConfigListener.wolvesGlass.difficulty.wickerWeaving) {
            CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.wicker, 1), "###", "###", "###", Character.valueOf('#'), Item.SUGAR_CANE);
        }
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.rollersItem, 1), "###", "###", "###", Character.valueOf('#'), BlockListener.moulding);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.strap, 8), "#", Character.valueOf('#'), ItemListener.tannedLeather);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.belt, 1), " # ", "# #", " # ", Character.valueOf('#'), ItemListener.strap);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.woodBlade, 1), "#  ", "#X#", "#  ", Character.valueOf('#'), new ItemStack(BlockListener.omniSlab.id, 1, 1), Character.valueOf('X'), Item.SLIMEBALL);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.woodBlade, 1), "#  ", "#X#", "#  ", Character.valueOf('#'), new ItemStack(BlockListener.omniSlab.id, 1, 1), Character.valueOf('X'), ItemListener.glue);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.haft, 1), "Y", "X", "#", Character.valueOf('#'), BlockListener.moulding, Character.valueOf('X'), ItemListener.glue, Character.valueOf('Y'), ItemListener.strap);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.haft, 1), "Y", "X", "#", Character.valueOf('#'), BlockListener.moulding, Character.valueOf('X'), Item.SLIMEBALL, Character.valueOf('Y'), ItemListener.strap);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.compositeBow, 1), "X#Y", "ZX#", "X#Y", Character.valueOf('#'), BlockListener.moulding, Character.valueOf('X'), Item.BONE, Character.valueOf('Y'), Item.SLIMEBALL, Character.valueOf('Z'),
                Item.STRING);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.compositeBow, 1), "X#Y", "ZX#", "X#Y", Character.valueOf('#'), BlockListener.moulding, Character.valueOf('X'), Item.BONE, Character.valueOf('Y'), ItemListener.glue, Character.valueOf('Z'),
                Item.STRING);
        CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.broadHeadArrow, 4), "#", "X", "Y", Character.valueOf('#'), ItemListener.broadHeadArrowhead, Character.valueOf('X'), BlockListener.moulding, Character.valueOf('Y'), Item.FEATHER);
        if (ConfigListener.wolvesGlass.difficulty.wickerWeaving || ConfigListener.wolvesGlass.difficulty.woolKnitting) {
            CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.emptyLoomShuttle, 1), " X ", "YZY", " X ", 'X', BlockListener.moulding, 'Y', BlockListener.corner, 'Z', Item.STICK);
        }
        if (ConfigListener.wolvesGlass.difficulty.wickerWeaving) {
            CraftingRegistry.addShapedRecipe(new ItemStack(ItemListener.wickerWeaving), "###", "###", "###", '#', Item.SUGAR_CANE);
        }
    }

    private static void addDyeRecipes() {
        for(int i = 0; i < 15; i++) {
            CraftingRegistry.addShapelessRecipe(new ItemStack(BlockListener.vase, 1, WoolBlock.method_1(i)), new ItemStack(Item.DYE, 1, i), new ItemStack(BlockListener.vase.id, 1, 0));
        }

        if (ConfigListener.wolvesGlass.small_tweaks.deactivateDung) {
            CraftingRegistry.addShapelessRecipe(new ItemStack(Block.WOOL, 1, 12), new ItemStack(Block.LOG), new ItemStack(Block.WOOL.id, 1, 0));
            CraftingRegistry.addShapelessRecipe(new ItemStack(BlockListener.vase, 1, 12), new ItemStack(Block.LOG), new ItemStack(BlockListener.vase.id, 1, 0));
        } else {
            CraftingRegistry.addShapelessRecipe(new ItemStack(Block.WOOL, 1, 12), new ItemStack(ItemListener.dung), new ItemStack(Block.WOOL.id, 1, 0));
            CraftingRegistry.addShapelessRecipe(new ItemStack(BlockListener.vase, 1, 12), new ItemStack(ItemListener.dung), new ItemStack(BlockListener.vase.id, 1, 0));
        }
    }

    private static void addAlternateVanillaRecipes() {
        CraftingRegistry.addShapedRecipe(new ItemStack(Block.STICKY_PISTON, 1), "#", "X", Character.valueOf('#'), ItemListener.glue, Character.valueOf('X'), Block.PISTON);
        CraftingRegistry.addShapedRecipe(new ItemStack(Block.FENCE, 2), "###", Character.valueOf('#'), BlockListener.moulding);
        CraftingRegistry.addShapedRecipe(new ItemStack(Block.WOODEN_STAIRS, 1), "# ", "##", Character.valueOf('#'), BlockListener.moulding);
        CraftingRegistry.addShapedRecipe(new ItemStack(Item.SIGN, 1), "###", "###", " X ", Character.valueOf('#'), new ItemStack(BlockListener.omniSlab.id, 1, 1), Character.valueOf('X'), Item.STICK);
        CraftingRegistry.addShapedRecipe(new ItemStack(Item.WOODEN_DOOR, 1), "##", "##", "##", Character.valueOf('#'), new ItemStack(BlockListener.omniSlab.id, 1, 1));
        CraftingRegistry.addShapedRecipe(new ItemStack(Block.TRAPDOOR, 2), "###", "###", Character.valueOf('#'), new ItemStack(BlockListener.omniSlab.id, 1, 1));
        CraftingRegistry.addShapedRecipe(new ItemStack(Item.CAKE, 1), "AAA", "BEB", "CCC", Character.valueOf('A'), Item.SUGAR, Character.valueOf('B'), Item.MILK_BUCKET, Character.valueOf('C'), ItemListener.flour, Character.valueOf('E'),
                Item.EGG);
        CraftingRegistry.addShapedRecipe(new ItemStack(Block.TORCH, 4), "#", "X", Character.valueOf('#'), ItemListener.netherCoal, Character.valueOf('X'), Item.STICK);
    }

    private static void addShapelessCraftingRecipes() {
        if (ConfigListener.wolvesGlass.difficulty.wickerWeaving) {
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.wicker), new ItemStack(ItemListener.wickerSheet), new ItemStack(ItemListener.grate));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.wickerLoomShuttle), new ItemStack(ItemListener.emptyLoomShuttle), new ItemStack(Item.SUGAR_CANE));
        }
        if (ConfigListener.wolvesGlass.difficulty.woolKnitting) {
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.knittingNeedles, 1), new ItemStack(Item.STICK, 1), new ItemStack(Item.STICK, 1));

            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.woolLoomShuttle), new ItemStack(ItemListener.emptyLoomShuttle), new ItemStack(ItemListener.whiteWoolBall));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.woolLoomShuttle), new ItemStack(ItemListener.emptyLoomShuttle), new ItemStack(ItemListener.orangeWoolBall));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.woolLoomShuttle), new ItemStack(ItemListener.emptyLoomShuttle), new ItemStack(ItemListener.magentaWoolBall));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.woolLoomShuttle), new ItemStack(ItemListener.emptyLoomShuttle), new ItemStack(ItemListener.lightBlueWoolBall));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.woolLoomShuttle), new ItemStack(ItemListener.emptyLoomShuttle), new ItemStack(ItemListener.yellowWoolBall));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.woolLoomShuttle), new ItemStack(ItemListener.emptyLoomShuttle), new ItemStack(ItemListener.limeWoolBall));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.woolLoomShuttle), new ItemStack(ItemListener.emptyLoomShuttle), new ItemStack(ItemListener.pinkWoolBall));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.woolLoomShuttle), new ItemStack(ItemListener.emptyLoomShuttle), new ItemStack(ItemListener.darkGreyWoolBall));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.woolLoomShuttle), new ItemStack(ItemListener.emptyLoomShuttle), new ItemStack(ItemListener.lightGreyWoolBall));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.woolLoomShuttle), new ItemStack(ItemListener.emptyLoomShuttle), new ItemStack(ItemListener.cyanWoolBall));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.woolLoomShuttle), new ItemStack(ItemListener.emptyLoomShuttle), new ItemStack(ItemListener.purpleWoolBall));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.woolLoomShuttle), new ItemStack(ItemListener.emptyLoomShuttle), new ItemStack(ItemListener.blueWoolBall));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.woolLoomShuttle), new ItemStack(ItemListener.emptyLoomShuttle), new ItemStack(ItemListener.brownWoolBall));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.woolLoomShuttle), new ItemStack(ItemListener.emptyLoomShuttle), new ItemStack(ItemListener.greenWoolBall));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.woolLoomShuttle), new ItemStack(ItemListener.emptyLoomShuttle), new ItemStack(ItemListener.redWoolBall));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.woolLoomShuttle), new ItemStack(ItemListener.emptyLoomShuttle), new ItemStack(ItemListener.blackWoolBall));

            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.whiteKnitting, 1), new ItemStack(ItemListener.knittingNeedles, 1), new ItemStack(ItemListener.whiteWoolBall, 1), new ItemStack(ItemListener.whiteWoolBall, 1));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.orangeKnitting, 1), new ItemStack(ItemListener.knittingNeedles, 1), new ItemStack(ItemListener.orangeWoolBall, 1), new ItemStack(ItemListener.orangeWoolBall, 1));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.magentaKnitting, 1), new ItemStack(ItemListener.knittingNeedles, 1), new ItemStack(ItemListener.magentaWoolBall, 1), new ItemStack(ItemListener.magentaWoolBall, 1));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.lightBlueKnitting, 1), new ItemStack(ItemListener.knittingNeedles, 1), new ItemStack(ItemListener.lightBlueWoolBall, 1), new ItemStack(ItemListener.lightBlueWoolBall, 1));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.yellowKnitting, 1), new ItemStack(ItemListener.knittingNeedles, 1), new ItemStack(ItemListener.yellowWoolBall, 1), new ItemStack(ItemListener.yellowWoolBall, 1));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.limeKnitting, 1), new ItemStack(ItemListener.knittingNeedles, 1), new ItemStack(ItemListener.limeWoolBall, 1), new ItemStack(ItemListener.limeWoolBall, 1));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.pinkKnitting, 1), new ItemStack(ItemListener.knittingNeedles, 1), new ItemStack(ItemListener.pinkWoolBall, 1), new ItemStack(ItemListener.pinkWoolBall, 1));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.darkGreyKnitting, 1), new ItemStack(ItemListener.knittingNeedles, 1), new ItemStack(ItemListener.darkGreyWoolBall, 1), new ItemStack(ItemListener.darkGreyWoolBall, 1));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.lightGreyKnitting, 1), new ItemStack(ItemListener.knittingNeedles, 1), new ItemStack(ItemListener.lightGreyWoolBall, 1), new ItemStack(ItemListener.lightGreyWoolBall, 1));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.cyanKnitting, 1), new ItemStack(ItemListener.knittingNeedles, 1), new ItemStack(ItemListener.cyanWoolBall, 1), new ItemStack(ItemListener.cyanWoolBall, 1));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.purpleKnitting, 1), new ItemStack(ItemListener.knittingNeedles, 1), new ItemStack(ItemListener.purpleWoolBall, 1), new ItemStack(ItemListener.purpleWoolBall, 1));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.blueKnitting, 1), new ItemStack(ItemListener.knittingNeedles, 1), new ItemStack(ItemListener.blueWoolBall, 1), new ItemStack(ItemListener.blueWoolBall, 1));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.brownKnitting, 1), new ItemStack(ItemListener.knittingNeedles, 1), new ItemStack(ItemListener.brownWoolBall, 1), new ItemStack(ItemListener.brownWoolBall, 1));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.greenKnitting, 1), new ItemStack(ItemListener.knittingNeedles, 1), new ItemStack(ItemListener.greenWoolBall, 1), new ItemStack(ItemListener.greenWoolBall, 1));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.redKnitting, 1), new ItemStack(ItemListener.knittingNeedles, 1), new ItemStack(ItemListener.redWoolBall, 1), new ItemStack(ItemListener.redWoolBall, 1));
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.blackKnitting, 1), new ItemStack(ItemListener.knittingNeedles, 1), new ItemStack(ItemListener.blackWoolBall, 1), new ItemStack(ItemListener.blackWoolBall, 1));

            CraftingRegistry.addShapelessRecipe(new ItemStack(Block.WOOL, 1, 0), new ItemStack(BlockListener.blockOfWicker), new ItemStack(ItemListener.whiteWoolKnit), new ItemStack(ItemListener.whiteWoolKnit));
            CraftingRegistry.addShapelessRecipe(new ItemStack(Block.WOOL, 1, 1), new ItemStack(BlockListener.blockOfWicker), new ItemStack(ItemListener.orangeWoolKnit), new ItemStack(ItemListener.orangeWoolKnit));
            CraftingRegistry.addShapelessRecipe(new ItemStack(Block.WOOL, 1, 2), new ItemStack(BlockListener.blockOfWicker), new ItemStack(ItemListener.magentaWoolKnit), new ItemStack(ItemListener.magentaWoolKnit));
            CraftingRegistry.addShapelessRecipe(new ItemStack(Block.WOOL, 1, 3), new ItemStack(BlockListener.blockOfWicker), new ItemStack(ItemListener.lightBlueWoolKnit), new ItemStack(ItemListener.lightBlueWoolKnit));
            CraftingRegistry.addShapelessRecipe(new ItemStack(Block.WOOL, 1, 4), new ItemStack(BlockListener.blockOfWicker), new ItemStack(ItemListener.yellowWoolKnit), new ItemStack(ItemListener.yellowWoolKnit));
            CraftingRegistry.addShapelessRecipe(new ItemStack(Block.WOOL, 1, 5), new ItemStack(BlockListener.blockOfWicker), new ItemStack(ItemListener.limeWoolKnit), new ItemStack(ItemListener.limeWoolKnit));
            CraftingRegistry.addShapelessRecipe(new ItemStack(Block.WOOL, 1, 6), new ItemStack(BlockListener.blockOfWicker), new ItemStack(ItemListener.pinkWoolKnit), new ItemStack(ItemListener.pinkWoolKnit));
            CraftingRegistry.addShapelessRecipe(new ItemStack(Block.WOOL, 1, 7), new ItemStack(BlockListener.blockOfWicker), new ItemStack(ItemListener.darkGreyWoolKnit), new ItemStack(ItemListener.darkGreyWoolKnit));
            CraftingRegistry.addShapelessRecipe(new ItemStack(Block.WOOL, 1, 8), new ItemStack(BlockListener.blockOfWicker), new ItemStack(ItemListener.lightGreyWoolKnit), new ItemStack(ItemListener.lightGreyWoolKnit));
            CraftingRegistry.addShapelessRecipe(new ItemStack(Block.WOOL, 1, 9), new ItemStack(BlockListener.blockOfWicker), new ItemStack(ItemListener.cyanWoolKnit), new ItemStack(ItemListener.cyanWoolKnit));
            CraftingRegistry.addShapelessRecipe(new ItemStack(Block.WOOL, 1, 10), new ItemStack(BlockListener.blockOfWicker), new ItemStack(ItemListener.purpleWoolKnit), new ItemStack(ItemListener.purpleWoolKnit));
            CraftingRegistry.addShapelessRecipe(new ItemStack(Block.WOOL, 1, 11), new ItemStack(BlockListener.blockOfWicker), new ItemStack(ItemListener.blueWoolKnit), new ItemStack(ItemListener.blueWoolKnit));
            CraftingRegistry.addShapelessRecipe(new ItemStack(Block.WOOL, 1, 12), new ItemStack(BlockListener.blockOfWicker), new ItemStack(ItemListener.brownWoolKnit), new ItemStack(ItemListener.brownWoolKnit));
            CraftingRegistry.addShapelessRecipe(new ItemStack(Block.WOOL, 1, 13), new ItemStack(BlockListener.blockOfWicker), new ItemStack(ItemListener.greenWoolKnit), new ItemStack(ItemListener.greenWoolKnit));
            CraftingRegistry.addShapelessRecipe(new ItemStack(Block.WOOL, 1, 14), new ItemStack(BlockListener.blockOfWicker), new ItemStack(ItemListener.redWoolKnit), new ItemStack(ItemListener.redWoolKnit));
            CraftingRegistry.addShapelessRecipe(new ItemStack(Block.WOOL, 1, 15), new ItemStack(BlockListener.blockOfWicker), new ItemStack(ItemListener.blackWoolKnit), new ItemStack(ItemListener.blackWoolKnit));
        }
        if (ConfigListener.wolvesGlass.difficulty.treeStumps) {
            CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.stumpRemover, 4), new ItemStack(Item.GUNPOWDER), new ItemStack(Block.RED_MUSHROOM));
        }
    }

    private static void addConversionRecipes() {
        CraftingRegistry.addShapelessRecipe(new ItemStack(Block.PLANKS), new ItemStack(BlockListener.omniSlab.id, 1, 1), new ItemStack(BlockListener.omniSlab.id, 1, 1));
        CraftingRegistry.addShapelessRecipe(new ItemStack(Block.STONE), new ItemStack(BlockListener.omniSlab.id, 1, 0), new ItemStack(BlockListener.omniSlab.id, 1, 0));
        CraftingRegistry.addShapelessRecipe(new ItemStack(BlockListener.omniSlab.id, 1, 1), new ItemStack(BlockListener.moulding.id,1,0), new ItemStack(BlockListener.moulding.id,1,0));
        CraftingRegistry.addShapelessRecipe(new ItemStack(BlockListener.moulding.id,1,0), new ItemStack(BlockListener.corner.id,1,0), new ItemStack(BlockListener.corner.id,1,0));
        CraftingRegistry.addShapelessRecipe(new ItemStack(Item.STRING), new ItemStack(ItemListener.hempFibers), new ItemStack(ItemListener.hempFibers));
        CraftingRegistry.addShapelessRecipe(new ItemStack(ItemListener.steel, 9), new ItemStack(BlockListener.blockOfSteel));
    }

    private static void addSmeltingRecipes() {
        SmeltingRegistry.addSmeltingRecipe(ItemListener.wolfRaw.id, new ItemStack(ItemListener.wolfCooked));
        SmeltingRegistry.addSmeltingRecipe(ItemListener.flour.id, new ItemStack(Item.BREAD));
//        ModLoader.AddSmelting(mod_FCBetterThanWolves.wolfRaw.id, new ItemInstance(mod_FCBetterThanWolves.wolfCooked));
//        ModLoader.AddSmelting(mod_FCBetterThanWolves.flour.id, new ItemInstance(ItemBase.bread));
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
        addShapelessAnvilRecipe(new ItemStack(BlockListener.omniSlab.id, 1, 0), new Object[] {
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
                    new ItemStack(Block.LOG, 1), new ItemStack(Block.WOOL.id, 8, 0)
            });
        } else {
            addCauldronRecipe(new ItemStack(ItemListener.tannedLeather, 1), new ItemStack[]{
                    new ItemStack(ItemListener.dung, 1), new ItemStack(ItemListener.scouredLeather, 1)
            });
            addCauldronRecipe(new ItemStack(Block.WOOL, 8, 12), new ItemStack[] {
                    new ItemStack(ItemListener.dung, 1), new ItemStack(Block.WOOL.id, 8, 0)
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
        addCauldronRecipe(new ItemStack(ItemListener.soulFilter, 1), new ItemStack[] {
                new ItemStack(ItemListener.hempCloth, 2), new ItemStack(Block.SOUL_SAND, 1)
        });
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
        MillingRecipeRegistry.getInstance().addMillingRecipe(Item.WHEAT.id, new ItemStack(ItemListener.flour, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(Item.LEATHER.id, new ItemStack(ItemListener.scouredLeather, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(ItemListener.hemp.id, new ItemStack(ItemListener.hempFibers, 4));
        MillingRecipeRegistry.getInstance().addMillingRecipe(Item.SUGAR_CANE.id, new ItemStack(Item.SUGAR, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(Block.NETHERRACK.id, new ItemStack(ItemListener.groundNetherrack, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(Item.BONE.id, new ItemStack(Item.DYE, 3, 15));
        MillingRecipeRegistry.getInstance().addMillingRecipe(Item.COAL.id, new ItemStack(ItemListener.coalDust, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(Block.ROSE.id, new ItemStack(Item.DYE, 2, 1));
        MillingRecipeRegistry.getInstance().addMillingRecipe(Block.DANDELION.id, new ItemStack(Item.DYE, 2, 11));
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
        addCrucibleRecipe(new ItemStack(ItemListener.steel, 4), new ItemStack[] { new ItemStack(Item.IRON_INGOT, 3), new ItemStack(ItemListener.concentratedHellfire), new ItemStack(ItemListener.coalDust) });

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
        HopperPurifyingRecipeRegistry.getInstance().addHopperHauntingRecipe(ItemListener.groundNetherrack.id, new ItemStack(ItemListener.hellfireDust, 1));
    }

    private static void addSawingRecipes() {
        SawingRecipeRegistry.getInstance().addSawingRecipe(Block.LOG.id, new ItemStack(Block.PLANKS, 4));
        SawingRecipeRegistry.getInstance().addSawingRecipe(Block.PLANKS.id, new ItemStack(BlockListener.omniSlab, 2, 1));
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
