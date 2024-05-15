package net.kozibrodka.wolves.events;

import net.kozibrodka.wolves.block.*;
import net.kozibrodka.wolves.materials.CementMaterial;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.class_259;
import net.minecraft.class_389;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.template.block.*;
import net.modificationstation.stationapi.api.util.Null;

public class BlockListener {

    @Entrypoint.Namespace
    public static final Namespace MOD_ID = Null.get();

    @EventListener
    public void registerBlocks(BlockRegistryEvent event) {
        anvil = (TemplateBlock) new AnvilBlock(Identifier.of(MOD_ID, "anvil")).setTranslationKey(MOD_ID, "anvil");
        lightBulbOff = (TemplateBlock) new LightBulbBlock(Identifier.of(MOD_ID, "lightBulbOff")).setTranslationKey(MOD_ID, "light_bulb_off");
        lightBulbOn = (TemplateBlock) new LightBulbBlock(Identifier.of(MOD_ID, "lightBulbOn")).setTranslationKey(MOD_ID, "light_bulb_on").setLuminance(1.0F);
        hibachi = (TemplateBlock) new HibachiBlock(Identifier.of(MOD_ID, "hibachi")).setTranslationKey(MOD_ID, "hibachi");
        hopper = (TemplateBlockWithEntity) new HopperBlock(Identifier.of(MOD_ID, "hopper")).setTranslationKey(MOD_ID, "hopper");
        saw = (TemplateBlock) new SawBlock(Identifier.of(MOD_ID, "saw")).setTranslationKey(MOD_ID, "saw");
        platform = (TemplateBlock) new PlatformBlock(Identifier.of(MOD_ID, "platform")).setTranslationKey(MOD_ID, "platform");
        blockOfWicker = (TemplateBlock) new WickerBlock(Identifier.of(MOD_ID, "blockOfWicker"), Material.SOLID_ORGANIC).setHardness(0.5F).setTranslationKey(MOD_ID, "block_of_wicker").setSoundGroup(Block.DIRT_SOUND_GROUP); //Mango-Pack Addon
        cement = (TemplateBlockWithEntity) new CementBlock(Identifier.of(MOD_ID, "cement")).setTranslationKey(MOD_ID, "cement");
        pulley = (TemplateBlockWithEntity) new PulleyBlock(Identifier.of(MOD_ID, "pulley")).setTranslationKey(MOD_ID, "pulley");
        pressurePlateObsidian = (TemplatePressurePlateBlock) new TemplatePressurePlateBlock(Identifier.of(MOD_ID, "pressurePlateObsidian"), 37, class_389.PLAYERS, Material.STONE).setTranslationKey(MOD_ID, "pressure_plate_obsidian").setHardness(0.5F).setResistance(2000F).setSoundGroup(Block.DEFAULT_SOUND_GROUP);
        moulding = (TemplateBlock) new MouldingBlock(Identifier.of(MOD_ID, "moulding")).setTranslationKey(MOD_ID, "moulding");
        corner = (TemplateBlock) new CornerBlock(Identifier.of(MOD_ID, "corner")).setTranslationKey(MOD_ID, "corner");
        blockDispenser = (TemplateBlockWithEntity) new BlockDispenserBlock(Identifier.of(MOD_ID, "blockDispenser")).setTranslationKey(MOD_ID, "block_dispenser").setHardness(3.5F).setSoundGroup(Block.DEFAULT_SOUND_GROUP);
        cauldron = (TemplateBlockWithEntity) new CauldronBlock(Identifier.of(MOD_ID, "cauldron")).setTranslationKey(MOD_ID, "cauldron").setHardness(3.5F).setResistance(10F).setSoundGroup(Block.METAL_SOUND_GROUP);
        detectorRailWood = (TemplateDetectorRailBlock) new DetectorRailVariants(Identifier.of(MOD_ID, "detectorRailWood"), 22).setTranslationKey(MOD_ID, "detector_rail_wood");
        detectorRailObsidian = (TemplateDetectorRailBlock) new DetectorRailVariants(Identifier.of(MOD_ID, "detectorRailObsidian"), 23).setTranslationKey(MOD_ID, "detector_rail_obsidian");
        companionCube = (TemplateBlock) new CompanionCubeBlock(Identifier.of(MOD_ID, "companionCube")).setTranslationKey(MOD_ID, "companion_cube").setHardness(0.4F).setSoundGroup(Block.WOOL_SOUND_GROUP);
        hempCrop = (TemplatePlantBlock) new HempCropBlock(Identifier.of(MOD_ID, "hempCrop")).setTranslationKey(MOD_ID, "hemp_crop");
        handCrank = (TemplateBlock) new HandCrankBlock(Identifier.of(MOD_ID, "handCrank")).setTranslationKey(MOD_ID, "hand_crank");
        millStone = (TemplateBlockWithEntity) new MillStoneBlock(Identifier.of(MOD_ID, "millStone")).setTranslationKey(MOD_ID, "mill_stone");
        anchor = (TemplateBlock) new AnchorBlock(Identifier.of(MOD_ID, "anchor")).setTranslationKey(MOD_ID, "anchor");
        rope = (TemplateBlock) new RopeBlock(Identifier.of(MOD_ID, "rope")).setTranslationKey(MOD_ID, "rope");
        omniSlab = (TemplateBlock) new OmniSlabBlock(Identifier.of(MOD_ID, "omniSlab")).setTranslationKey(MOD_ID, "omni_slab");
        axleBlock = (TemplateBlock) new AxleBlock(Identifier.of(MOD_ID, "axleBlock")).setTranslationKey(MOD_ID, "axle_block");
        gearBox = (TemplateBlock) new GearboxBlock(Identifier.of(MOD_ID, "gearBox")).setTranslationKey(MOD_ID, "gear_box");
        turntable = (TemplateBlockWithEntity) new TurntableBlock(Identifier.of(MOD_ID, "turntable")).setTranslationKey(MOD_ID, "turntable");
        bellows = (TemplateBlock) new BellowsBlock(Identifier.of(MOD_ID, "bellows")).setTranslationKey(MOD_ID, "bellows");
        stokedFire = (TemplateFireBlock) new StokedFireBlock(Identifier.of(MOD_ID, "stokedFire")).setTranslationKey(MOD_ID, "stoked_fire");
        unfiredPottery = (TemplateBlockWithEntity) new UnfiredPotteryBlock(Identifier.of(MOD_ID, "unfiredPottery")).setTranslationKey(MOD_ID, "unfired_pottery");
        crucible = (TemplateBlockWithEntity) new CrucibleBlock(Identifier.of(MOD_ID, "crucible")).setTranslationKey(MOD_ID, "crucible");
        planter = (TemplateBlock) new PlanterBlock(Identifier.of(MOD_ID, "planter")).setTranslationKey(MOD_ID, "planter");
        vase = (TemplateBlockWithEntity) new VaseBlock(Identifier.of(MOD_ID, "vase")).setTranslationKey(MOD_ID, "vase");
        detectorBlock = (TemplateBlock) new DetectorBlock(Identifier.of(MOD_ID, "detector_block"), Material.field_993).setTranslationKey(MOD_ID, "detector_block");;


        //TODO: Omni slabs are really cursed and kinda bad. Also applies to Cornets/Moudlings - should be option in CFG to remove them

        //TODO: I like the idea of Vanilla providing SLABS (can be placed only horizontally - with fixed placement (basically modern slabs)) & BTW providing PANELS
        if(ConfigListener.wolvesGlass.gameplay_settings.initPanels){
            panelNumber1 = (TemplateBlock) new PanelBlock(Identifier.of(MOD_ID, "fcPanel1"), Block.SAND).setTranslationKey(MOD_ID, "fc_panel_1"); //test panels later hide
            panelNumber2 = (TemplateBlock) new PanelBlock(Identifier.of(MOD_ID, "fcPanel2"), Block.PUMPKIN).setTranslationKey(MOD_ID, "fcPanel_2");
            panelNumber3 = (TemplateBlock) new PanelBlock(Identifier.of(MOD_ID, "fcPanel3"), crucible).setTranslationKey(MOD_ID, "fc_panel3");
            panelNumber4 = (TemplateBlock) new PanelBlock(Identifier.of(MOD_ID, "fcPanel4"), Block.NETHER_PORTAL).setTranslationKey(MOD_ID, "fc_panel_4");
        }

    }
    public static final Material fcCementMaterial = new CementMaterial(class_259.field_2749);
    public static TemplateBlock anvil;
    public static TemplateBlock lightBulbOff;
    public static TemplateBlock lightBulbOn;
    public static TemplateBlock hibachi;
    public static TemplateBlockWithEntity hopper;
    public static TemplateBlock saw;
    public static TemplateBlock platform;
    public static TemplateBlockWithEntity cement;
    public static TemplateBlockWithEntity pulley;
    public static TemplatePressurePlateBlock pressurePlateObsidian;
    public static TemplateBlock moulding;
    public static TemplateBlock corner;
    public static TemplateBlockWithEntity blockDispenser;
    public static TemplateBlockWithEntity cauldron;
    public static TemplateDetectorRailBlock detectorRailWood;
    public static TemplateDetectorRailBlock detectorRailObsidian;
    public static TemplateBlock companionCube;
    public static TemplateBlock blockDetector;
    public static TemplateBlock blockDetectorLogic;
    public static TemplatePlantBlock hempCrop;
    public static TemplateBlock handCrank;
    public static TemplateBlockWithEntity millStone;
    public static TemplateBlock anchor;
    public static TemplateBlock rope;
    public static TemplateBlock omniSlab;
    public static TemplateBlock axleBlock;
    public static TemplateBlock gearBox;
    public static TemplateBlockWithEntity turntable;
    public static TemplateBlock bellows;
    public static TemplateFireBlock stokedFire;
    public static TemplateBlockWithEntity unfiredPottery;
    public static TemplateBlockWithEntity crucible;
    public static TemplateBlock planter;
    public static TemplateBlockWithEntity vase;
    public static TemplateBlock detectorBlock;

    public static TemplateBlock blockOfWicker; //Mango Pack Addon
    public static TemplateBlock panelNumber1;
    public static TemplateBlock panelNumber2;
    public static TemplateBlock panelNumber3;
    public static TemplateBlock panelNumber4;
}
