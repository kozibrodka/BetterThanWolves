package net.kozibrodka.wolves.events;

import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.block.AnchorBlock;
import net.kozibrodka.wolves.block.AnvilBlock;
import net.kozibrodka.wolves.block.AutomaticAnvilBlock;
import net.kozibrodka.wolves.block.AxleBlock;
import net.kozibrodka.wolves.block.BellowsBlock;
import net.kozibrodka.wolves.block.BlockDispenserBlock;
import net.kozibrodka.wolves.block.CauldronBlock;
import net.kozibrodka.wolves.block.CementBlock;
import net.kozibrodka.wolves.block.CompanionCubeBlock;
import net.kozibrodka.wolves.block.ConverterBlock;
import net.kozibrodka.wolves.block.ConveyorBlock;
import net.kozibrodka.wolves.block.ConveyorExtenderBlock;
import net.kozibrodka.wolves.block.CornerBlock;
import net.kozibrodka.wolves.block.CrucibleBlock;
import net.kozibrodka.wolves.block.DetectorBlock;
import net.kozibrodka.wolves.block.DetectorRailVariants;
import net.kozibrodka.wolves.block.DropperBlock;
import net.kozibrodka.wolves.block.GearboxBlock;
import net.kozibrodka.wolves.block.HandCrankBlock;
import net.kozibrodka.wolves.block.HempCropBlock;
import net.kozibrodka.wolves.block.HibachiBlock;
import net.kozibrodka.wolves.block.HopperBlock;
import net.kozibrodka.wolves.block.LazyBlockTemplate;
import net.kozibrodka.wolves.block.LazyTransparentBlockTemplate;
import net.kozibrodka.wolves.block.LightBulbBlock;
import net.kozibrodka.wolves.block.MachineBusBlock;
import net.kozibrodka.wolves.block.MachinePowerInputBlock;
import net.kozibrodka.wolves.block.MillStoneBlock;
import net.kozibrodka.wolves.block.MouldingBlock;
import net.kozibrodka.wolves.block.NonCollidingAxleBlock;
import net.kozibrodka.wolves.block.OmniSlabBlock;
import net.kozibrodka.wolves.block.PanelBlock;
import net.kozibrodka.wolves.block.PlanterBlock;
import net.kozibrodka.wolves.block.PlatformBlock;
import net.kozibrodka.wolves.block.PulleyBlock;
import net.kozibrodka.wolves.block.RopeBlock;
import net.kozibrodka.wolves.block.SawBlock;
import net.kozibrodka.wolves.block.StokedFireBlock;
import net.kozibrodka.wolves.block.TurntableBlock;
import net.kozibrodka.wolves.block.UnfiredPotteryBlock;
import net.kozibrodka.wolves.block.VaseBlock;
import net.kozibrodka.wolves.block.WickerBlock;
import net.kozibrodka.wolves.block.item.StoneSlabBlock;
import net.kozibrodka.wolves.materials.CementMaterial;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.PressurePlateActivationRule;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.template.block.TemplatePressurePlateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

public class BlockListener {

    static boolean isNewFrontierCraftPresent = FabricLoader.getInstance().isModLoaded("nfc");

    public static final Material CEMENT_MATERIAL = new CementMaterial(MapColor.GRAY);

    // Original BTW blocks
    public static Block anvil;
    public static Block lightBulbOff;
    public static Block lightBulbOn;
    public static Block hibachi;
    public static Block hopper;
    public static Block saw;
    public static Block platform;
    public static Block cement;
    public static Block pulley;
    public static Block pressurePlateObsidian;
    public static Block moulding;
    public static Block corner;
    public static Block blockDispenser;
    public static Block cauldron;
    public static Block detectorRailWood;
    public static Block detectorRailObsidian;
    public static Block companionCube;
    public static Block blockDetector;
    public static Block blockDetectorLogic;
    public static Block hempCrop;
    public static Block handCrank;
    public static Block millStone;
    public static Block anchor;
    public static Block rope;
    public static Block omniSlab;
    public static Block axleBlock;
    public static Block gearBox;
    public static Block turntable;
    public static Block bellows;
    public static Block stokedFire;
    public static Block unfiredPottery;
    public static Block crucible;
    public static Block planter;
    public static Block vase;
    public static Block detectorBlock;
    public static Block nonCollidingAxleBlock;

    // New blocks
    public static TemplateBlock blockOfWicker;
    public static LazyBlockTemplate blockOfGrates;
    public static LazyBlockTemplate blockOfSteel;
    public static Block dropper;
    public static ConveyorBlock conveyor;
    public static ConveyorExtenderBlock conveyorExtender;
    public static AutomaticAnvilBlock automaticAnvil;
    public static LazyBlockTemplate anvilFrame;
    public static MachinePowerInputBlock machinePowerInput;
    public static MachineBusBlock inputBus;
    public static MachineBusBlock outputBus;
    public static TemplateBlock stoneSlabBlock;
    public static TemplateBlock panelStoneS;
    public static TemplateBlock panelSandstone;
    public static TemplateBlock panelWooden;
    public static TemplateBlock panelCobblestone;
    public static TemplateBlock panelBricks;
    public static TemplateBlock panelMossy;
    public static TemplateBlock panelStone;

    // Crossover content
    public static LazyBlockTemplate converter;

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public void registerBlocks(BlockRegistryEvent event) {
        anvil = new AnvilBlock(NAMESPACE.id("anvil")).setTranslationKey(NAMESPACE, "anvil");
        lightBulbOff = new LightBulbBlock(NAMESPACE.id("lightBulbOff")).setTranslationKey(NAMESPACE, "light_bulb_off");
        lightBulbOn = new LightBulbBlock(NAMESPACE.id("lightBulbOn")).setTranslationKey(NAMESPACE, "light_bulb_on").setLuminance(1.0F);
        hibachi = new HibachiBlock(NAMESPACE.id("hibachi")).setTranslationKey(NAMESPACE, "hibachi");
        hopper = new HopperBlock(NAMESPACE.id("hopper")).setTranslationKey(NAMESPACE, "hopper");
        saw = new SawBlock(NAMESPACE.id("saw")).setTranslationKey(NAMESPACE, "saw");
        platform = new PlatformBlock(NAMESPACE.id("platform")).setTranslationKey(NAMESPACE, "platform");
        blockOfWicker = (TemplateBlock) new WickerBlock(NAMESPACE.id("blockOfWicker"), Material.SOLID_ORGANIC).setHardness(0.5F).setTranslationKey(NAMESPACE, "block_of_wicker").setSoundGroup(Block.DIRT_SOUND_GROUP); //Mango-Pack Addon
        cement = new CementBlock(NAMESPACE.id("cement")).setTranslationKey(NAMESPACE, "cement");
        pulley = new PulleyBlock(NAMESPACE.id("pulley")).setTranslationKey(NAMESPACE, "pulley");
        pressurePlateObsidian = new TemplatePressurePlateBlock(NAMESPACE.id("pressurePlateObsidian"), 37, PressurePlateActivationRule.PLAYERS, Material.STONE).setTranslationKey(NAMESPACE, "pressure_plate_obsidian").setHardness(0.5F).setResistance(2000F).setSoundGroup(Block.DEFAULT_SOUND_GROUP);
        moulding = new MouldingBlock(NAMESPACE.id("moulding")).setTranslationKey(NAMESPACE, "moulding");
        corner = new CornerBlock(NAMESPACE.id("corner")).setTranslationKey(NAMESPACE, "corner");
        blockDispenser = new BlockDispenserBlock(NAMESPACE.id("blockDispenser")).setTranslationKey(NAMESPACE, "block_dispenser").setHardness(3.5F).setSoundGroup(Block.DEFAULT_SOUND_GROUP);
        cauldron = new CauldronBlock(NAMESPACE.id("cauldron")).setTranslationKey(NAMESPACE, "cauldron").setHardness(3.5F).setResistance(10F).setSoundGroup(Block.METAL_SOUND_GROUP);
        detectorRailWood = new DetectorRailVariants(NAMESPACE.id("detectorRailWood"), 22).setTranslationKey(NAMESPACE, "detector_rail_wood");
        detectorRailObsidian = new DetectorRailVariants(NAMESPACE.id("detectorRailObsidian"), 23).setTranslationKey(NAMESPACE, "detector_rail_obsidian");
        companionCube = new CompanionCubeBlock(NAMESPACE.id("companionCube")).setTranslationKey(NAMESPACE, "companion_cube").setHardness(0.4F).setSoundGroup(Block.WOOL_SOUND_GROUP);
        hempCrop = new HempCropBlock(NAMESPACE.id("hempCrop")).setTranslationKey(NAMESPACE, "hemp_crop");
        handCrank = new HandCrankBlock(NAMESPACE.id("handCrank")).setTranslationKey(NAMESPACE, "hand_crank");
        millStone = new MillStoneBlock(NAMESPACE.id("millStone")).setTranslationKey(NAMESPACE, "mill_stone");
        anchor = new AnchorBlock(NAMESPACE.id("anchor")).setTranslationKey(NAMESPACE, "anchor");
        rope = new RopeBlock(NAMESPACE.id("rope")).setTranslationKey(NAMESPACE, "rope");
        omniSlab = new OmniSlabBlock(NAMESPACE.id("omniSlab")).setTranslationKey(NAMESPACE, "omni_slab");
        stoneSlabBlock = (TemplateBlock) new StoneSlabBlock(Identifier.of(NAMESPACE, "stone_slab_block"), Material.STONE).setTranslationKey(NAMESPACE, "stone_slab_block");
        panelStoneS = (TemplateBlock) new PanelBlock(NAMESPACE.id( "panel_stone_s"), stoneSlabBlock).setTranslationKey(NAMESPACE, "panel_stone_s");
        panelSandstone = (TemplateBlock) new PanelBlock(NAMESPACE.id( "panel_sandstone"), Block.SANDSTONE).setTranslationKey(NAMESPACE, "panel_sandstone");
        panelWooden = (TemplateBlock) new PanelBlock(NAMESPACE.id( "panel_planks"), Block.PLANKS).setTranslationKey(NAMESPACE, "panel_planks");
        panelCobblestone = (TemplateBlock) new PanelBlock(NAMESPACE.id( "panel_cbble"), Block.COBBLESTONE).setTranslationKey(NAMESPACE, "panel_cbble");
        panelBricks = (TemplateBlock) new PanelBlock(NAMESPACE.id( "panel_bricks"), Block.BRICKS).setTranslationKey(NAMESPACE, "panel_bricks");
        panelMossy = (TemplateBlock) new PanelBlock(NAMESPACE.id( "panel_mossy"), Block.MOSSY_COBBLESTONE).setTranslationKey(NAMESPACE, "panel_mossy");
        panelStone = (TemplateBlock) new PanelBlock(NAMESPACE.id( "panel_stone"), Block.STONE).setTranslationKey(NAMESPACE, "panel_stone");
        //
        axleBlock = new AxleBlock(NAMESPACE.id("axleBlock")).setTranslationKey(NAMESPACE, "axle_block");
        gearBox = new GearboxBlock(NAMESPACE.id("gearBox")).setTranslationKey(NAMESPACE, "gear_box");
        turntable = new TurntableBlock(NAMESPACE.id("turntable")).setTranslationKey(NAMESPACE, "turntable");
        bellows = new BellowsBlock(NAMESPACE.id("bellows")).setTranslationKey(NAMESPACE, "bellows");
        stokedFire = new StokedFireBlock(NAMESPACE.id("stokedFire")).setTranslationKey(NAMESPACE, "stoked_fire");
        unfiredPottery = new UnfiredPotteryBlock(NAMESPACE.id("unfiredPottery")).setTranslationKey(NAMESPACE, "unfired_pottery");
        crucible = new CrucibleBlock(NAMESPACE.id("crucible")).setTranslationKey(NAMESPACE, "crucible");
        planter = new PlanterBlock(NAMESPACE.id("planter")).setTranslationKey(NAMESPACE, "planter");
        vase = new VaseBlock(NAMESPACE.id("vase")).setTranslationKey(NAMESPACE, "vase");
        detectorBlock = new DetectorBlock(NAMESPACE.id("detector_block"), Material.PISTON_BREAKABLE).setTranslationKey(NAMESPACE, "detector_block");
        blockOfGrates = new LazyTransparentBlockTemplate(NAMESPACE.id("block_of_grates"), Material.WOOD, 1.5F, Block.WOOD_SOUND_GROUP);
        nonCollidingAxleBlock = new NonCollidingAxleBlock(NAMESPACE.id("non_colliding_axle_block")).setTranslationKey(NAMESPACE, "non_colliding_axle_block");
        blockOfSteel = new LazyBlockTemplate(NAMESPACE.id("block_of_steel"), Material.METAL, 2.5F, Block.METAL_SOUND_GROUP);
        dropper = new DropperBlock(NAMESPACE.id("dropper"), Material.PISTON_BREAKABLE).setHardness(2.5F).setSoundGroup(Block.METAL_SOUND_GROUP);
        conveyor = new ConveyorBlock(Identifier.of(NAMESPACE, "conveyor"), Material.WOOD, 1.5F, Block.WOOD_SOUND_GROUP);
        conveyorExtender= new ConveyorExtenderBlock(Identifier.of(NAMESPACE, "conveyor_extender"), Material.WOOD, 1.5F, Block.WOOD_SOUND_GROUP);
        automaticAnvil = new AutomaticAnvilBlock(Identifier.of(NAMESPACE, "automatic_anvil"), Material.METAL, 2.5F, Block.METAL_SOUND_GROUP);
        anvilFrame = new LazyBlockTemplate(Identifier.of(NAMESPACE, "anvil_frame"), Material.METAL, 2.5F, Block.METAL_SOUND_GROUP);
        machinePowerInput = new MachinePowerInputBlock(Identifier.of(NAMESPACE,  "machine_power_input"), Material.METAL, 2.5F, Block.METAL_SOUND_GROUP);
        inputBus = new MachineBusBlock(Identifier.of(NAMESPACE, "input_bus"), Material.METAL, 2.5F, Block.METAL_SOUND_GROUP);
        outputBus = new MachineBusBlock(Identifier.of(NAMESPACE, "output_bus"), Material.METAL, 2.5F, Block.METAL_SOUND_GROUP);
        if (isNewFrontierCraftPresent) {
            converter = new ConverterBlock(Identifier.of(NAMESPACE, "converter"), Material.METAL, 1.5F, Block.METAL_SOUND_GROUP);
        } else { // Load a dummy version of the block to keep tool tags and other things intact
            converter = new LazyBlockTemplate(Identifier.of(NAMESPACE, "converter"), Material.METAL, 1.5F, Block.METAL_SOUND_GROUP);
        }

        //TODO: Gui's doesnt work on SINGPLAYER correctly - power of machine.
        //TODO: Hopper soul filter - bring back oryginal design.
        //TODO: Sound volume is like oryginal? check why.

    }
}
