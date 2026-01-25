package net.kozibrodka.wolves.events;

import net.kozibrodka.wolves.block.*;
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

    public static final Material CEMENT_MATERIAL = new CementMaterial(MapColor.GRAY);
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
    public static CollisionBlock collisionBlock;
    public static ObstructionBlock obstructionBlock;

    public static TemplateBlock blockOfWicker; //Mango Pack Addon
    public static LazyBlockTemplate blockOfGrates; //Another Mango Pack Addon
    public static LazyBlockTemplate blockOfSteel;
    public static Block dropper;
    public static ConveyorBlock conveyor;
    public static ConveyorExtenderBlock conveyorExtender;

    public static TemplateBlock panelNumber1;
    public static TemplateBlock panelNumber2;
    public static TemplateBlock panelNumber3;
    public static TemplateBlock panelNumber4;

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
        collisionBlock = new CollisionBlock(NAMESPACE.id("collision_block"), Material.WOOD, 1.5F, Block.WOOD_SOUND_GROUP);
        obstructionBlock = new ObstructionBlock(NAMESPACE.id("obstruction_block"), Material.WOOD, 1.5F, Block.WOOD_SOUND_GROUP);
        blockOfSteel = new LazyBlockTemplate(NAMESPACE.id("block_of_steel"), Material.METAL, 2.5F, Block.METAL_SOUND_GROUP);
        dropper = new DropperBlock(NAMESPACE.id("dropper"), Material.PISTON_BREAKABLE).setHardness(2.5F).setSoundGroup(Block.METAL_SOUND_GROUP);
        conveyor = new ConveyorBlock(Identifier.of(NAMESPACE, "conveyor"), Material.WOOD, 1.5F, Block.WOOD_SOUND_GROUP);
        conveyorExtender= new ConveyorExtenderBlock(Identifier.of(NAMESPACE, "conveyor_extender"), Material.WOOD, 1.5F, Block.WOOD_SOUND_GROUP);

        //TODO: Omni slabs are really cursed and kinda bad. Also applies to Cornets/Moudlings - should be option in CFG to remove them

        //TODO: I like the idea of Vanilla providing SLABS (can be placed only horizontally - with fixed placement (basically modern slabs)) & BTW providing PANELS
        //if(ConfigListener.wolvesGlass.small_tweaks.initPanels){
        //    panelNumber1 = (TemplateBlock) new PanelBlock(MOD_ID.id( "fcPanel1"), Block.SAND).setTranslationKey(MOD_ID, "fc_panel_1"); //test panels later hide
        //    panelNumber2 = (TemplateBlock) new PanelBlock(MOD_ID.id( "fcPanel2"), Block.PUMPKIN).setTranslationKey(MOD_ID, "fcPanel_2");
        //    panelNumber3 = (TemplateBlock) new PanelBlock(MOD_ID.id( "fcPanel3"), crucible).setTranslationKey(MOD_ID, "fc_panel3");
        //    panelNumber4 = (TemplateBlock) new PanelBlock(MOD_ID.id( "fcPanel4"), Block.NETHER_PORTAL).setTranslationKey(MOD_ID, "fc_panel_4");
        //}

    }
}
