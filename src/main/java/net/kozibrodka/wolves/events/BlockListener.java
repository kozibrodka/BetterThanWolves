package net.kozibrodka.wolves.events;

import net.kozibrodka.wolves.blocks.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.block.PressurePlateTrigger;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.template.block.*;
import net.modificationstation.stationapi.api.util.Null;

public class BlockListener {

    @Entrypoint.Namespace
    public static final Namespace MOD_ID = Null.get();

    @EventListener
    public void registerBlocks(BlockRegistryEvent event) {
        anvil = (TemplateBlock) new Anvil(Identifier.of(MOD_ID, "anvil")).setTranslationKey(MOD_ID, "anvil");
        lightBulbOff = (TemplateBlock) new LightBulb(Identifier.of(MOD_ID, "lightBulbOff")).setTranslationKey(MOD_ID, "light_bulb_off");
        lightBulbOn = (TemplateBlock) new LightBulb(Identifier.of(MOD_ID, "lightBulbOn")).setTranslationKey(MOD_ID, "light_bulb_on").setLightEmittance(1.0F);
        hibachi = (TemplateBlock) new Hibachi(Identifier.of(MOD_ID, "hibachi")).setTranslationKey(MOD_ID, "hibachi");
        hopper = (TemplateBlockWithEntity) new Hopper(Identifier.of(MOD_ID, "hopper")).setTranslationKey(MOD_ID, "hopper");
        saw = (TemplateBlock) new Saw(Identifier.of(MOD_ID, "saw")).setTranslationKey(MOD_ID, "saw");
        platform = (TemplateBlock) new Platform(Identifier.of(MOD_ID, "platform")).setTranslationKey(MOD_ID, "platform");
        blockOfWicker = (TemplateBlock) new Wicker(Identifier.of(MOD_ID, "blockOfWicker"), Material.ORGANIC).setHardness(0.5F).setTranslationKey(MOD_ID, "block_of_wicker").setSounds(BlockBase.GRASS_SOUNDS); //Mango-Pack Addon
        cement = (TemplateBlockWithEntity) new Cement(Identifier.of(MOD_ID, "cement")).setTranslationKey(MOD_ID, "cement");
        pulley = (TemplateBlockWithEntity) new Pulley(Identifier.of(MOD_ID, "pulley")).setTranslationKey(MOD_ID, "pulley");
        pressurePlateObsidian = (TemplatePressurePlateBlock) new TemplatePressurePlateBlock(Identifier.of(MOD_ID, "pressurePlateObsidian"), 37, PressurePlateTrigger.field_1507, Material.STONE).setTranslationKey(MOD_ID, "pressure_plate_obsidian").setHardness(0.5F).setBlastResistance(2000F).setSounds(BlockBase.STONE_SOUNDS);
        moulding = (TemplateBlock) new Moulding(Identifier.of(MOD_ID, "moulding")).setTranslationKey(MOD_ID, "moulding");
        corner = (TemplateBlock) new Corner(Identifier.of(MOD_ID, "corner")).setTranslationKey(MOD_ID, "corner");
        blockDispenser = (TemplateBlockWithEntity) new BlockDispenser(Identifier.of(MOD_ID, "blockDispenser")).setTranslationKey(MOD_ID, "block_dispenser").setHardness(3.5F).setSounds(BlockBase.STONE_SOUNDS);
        cauldron = (TemplateBlockWithEntity) new Cauldron(Identifier.of(MOD_ID, "cauldron")).setTranslationKey(MOD_ID, "cauldron").setHardness(3.5F).setBlastResistance(10F).setSounds(BlockBase.METAL_SOUNDS);
        detectorRailWood = (TemplateDetectorRailBlock) new DetectorRailVariants(Identifier.of(MOD_ID, "detectorRailWood"), 22).setTranslationKey(MOD_ID, "detector_rail_wood");
        detectorRailObsidian = (TemplateDetectorRailBlock) new DetectorRailVariants(Identifier.of(MOD_ID, "detectorRailObsidian"), 23).setTranslationKey(MOD_ID, "detector_rail_obsidian");
        companionCube = (TemplateBlock) new CompanionCube(Identifier.of(MOD_ID, "companionCube")).setTranslationKey(MOD_ID, "companion_cube").setHardness(0.4F).setSounds(BlockBase.WOOL_SOUNDS);
        hempCrop = (TemplatePlantBlock) new HempCrop(Identifier.of(MOD_ID, "hempCrop")).setTranslationKey(MOD_ID, "hemp_crop");
        handCrank = (TemplateBlock) new HandCrank(Identifier.of(MOD_ID, "handCrank")).setTranslationKey(MOD_ID, "hand_crank");
        millStone = (TemplateBlockWithEntity) new MillStone(Identifier.of(MOD_ID, "millStone")).setTranslationKey(MOD_ID, "mill_stone");
        anchor = (TemplateBlock) new Anchor(Identifier.of(MOD_ID, "anchor")).setTranslationKey(MOD_ID, "anchor");
        rope = (TemplateBlock) new Rope(Identifier.of(MOD_ID, "rope")).setTranslationKey(MOD_ID, "rope");
        omniSlab = (TemplateBlock) new OmniSlab(Identifier.of(MOD_ID, "omniSlab")).setTranslationKey(MOD_ID, "omni_slab");
        axleBlock = (TemplateBlock) new Axle(Identifier.of(MOD_ID, "axleBlock")).setTranslationKey(MOD_ID, "axle_block");
        gearBox = (TemplateBlock) new GearBox(Identifier.of(MOD_ID, "gearBox")).setTranslationKey(MOD_ID, "gear_box");
        turntable = (TemplateBlockWithEntity) new Turntable(Identifier.of(MOD_ID, "turntable")).setTranslationKey(MOD_ID, "turntable");
        bellows = (TemplateBlock) new Bellows(Identifier.of(MOD_ID, "bellows")).setTranslationKey(MOD_ID, "bellows");
        stokedFire = (TemplateFireBlock) new StokedFire(Identifier.of(MOD_ID, "stokedFire")).setTranslationKey(MOD_ID, "stoked_fire");
        unfiredPottery = (TemplateBlockWithEntity) new UnfiredPottery(Identifier.of(MOD_ID, "unfiredPottery")).setTranslationKey(MOD_ID, "unfired_pottery");
        crucible = (TemplateBlockWithEntity) new Crucible(Identifier.of(MOD_ID, "crucible")).setTranslationKey(MOD_ID, "crucible");
        planter = (TemplateBlock) new Planter(Identifier.of(MOD_ID, "planter")).setTranslationKey(MOD_ID, "planter");
        vase = (TemplateBlockWithEntity) new Vase(Identifier.of(MOD_ID, "vase")).setTranslationKey(MOD_ID, "vase");
        detectorBlock = (TemplateBlock) new DetectorBlock(Identifier.of(MOD_ID, "detector_block"), Material.DOODADS).setTranslationKey(MOD_ID, "detector_block");;

        //TODO: Extra (test) - later move into expansion probably
        //TODO: My idea: remove crafting of stone omni plates/ corners, moulding and add grinder as a saw for "stone type" blocks
        //TODO: Omni slabs are really cursed and kinda bad, especially with possible mods that upgrade slabs/stairs to modern . I suggest panels instead - would be great addon btw brings.

        //TODO: update, with new vanilla enhacked by pauldevs i dont see a point
//        panelNumber1 = (TemplateBlock) new Panel(Identifier.of(MOD_ID, "fcPanel1"), BlockBase.SAND).setTranslationKey(MOD_ID, "fc_panel_1"); //test panels later hide
//        panelNumber2 = (TemplateBlock) new Panel(Identifier.of(MOD_ID, "fcPanel2"), BlockBase.PUMPKIN).setTranslationKey(MOD_ID, "fcPanel_2");
//        panelNumber3 = (TemplateBlock) new Panel(Identifier.of(MOD_ID, "fcPanel3"), crucible).setTranslationKey(MOD_ID, "fc_panel3");
//        panelNumber4 = (TemplateBlock) new Panel(Identifier.of(MOD_ID, "fcPanel4"), BlockBase.PORTAL).setTranslationKey(MOD_ID, "fc_panel_4");
    }

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
