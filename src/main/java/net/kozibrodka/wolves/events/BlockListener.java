package net.kozibrodka.wolves.events;

import net.kozibrodka.wolves.blocks.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.block.PressurePlateTrigger;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.template.block.*;
import net.modificationstation.stationapi.api.util.Null;

public class BlockListener {

    @Entrypoint.ModID
    public static final ModID MOD_ID = Null.get();

    @EventListener
    public void registerBlocks(BlockRegistryEvent event) {
        anvil = (TemplateBlockBase) new Anvil(Identifier.of(MOD_ID, "anvil")).setTranslationKey(MOD_ID, "anvil");
        lightBulbOff = (TemplateBlockBase) new LightBulb(Identifier.of(MOD_ID, "lightBulbOff")).setTranslationKey(MOD_ID, "light_bulb_off");
        lightBulbOn = (TemplateBlockBase) new LightBulb(Identifier.of(MOD_ID, "lightBulbOn")).setTranslationKey(MOD_ID, "light_bulb_on").setLightEmittance(1.0F);
        hibachi = (TemplateBlockBase) new Hibachi(Identifier.of(MOD_ID, "hibachi")).setTranslationKey(MOD_ID, "hibachi");
        hopper = (TemplateBlockWithEntity) new Hopper(Identifier.of(MOD_ID, "hopper")).setTranslationKey(MOD_ID, "hopper");
        saw = (TemplateBlockBase) new Saw(Identifier.of(MOD_ID, "saw")).setTranslationKey(MOD_ID, "saw");
        platform = (TemplateBlockBase) new Platform(Identifier.of(MOD_ID, "platform")).setTranslationKey(MOD_ID, "platform");
        blockOfWicker = (TemplateBlockBase) new Wicker(Identifier.of(MOD_ID, "blockOfWicker"), Material.ORGANIC).setHardness(0.5F).setTranslationKey(MOD_ID, "block_of_wicker").setSounds(BlockBase.GRASS_SOUNDS); //Mango-Pack Addon
        cement = (TemplateBlockWithEntity) new net.kozibrodka.wolves.blocks.Cement(Identifier.of(MOD_ID, "cement")).setTranslationKey(MOD_ID, "cement");
        pulley = (TemplateBlockWithEntity) new Pulley(Identifier.of(MOD_ID, "pulley")).setTranslationKey(MOD_ID, "pulley");
        pressurePlateObsidian = (TemplatePressurePlate) new TemplatePressurePlate(Identifier.of(MOD_ID, "pressurePlateObsidian"), 37, PressurePlateTrigger.field_1507, Material.STONE).setTranslationKey(MOD_ID, "pressure_plate_obsidian").setHardness(0.5F).setBlastResistance(2000F).setSounds(BlockBase.STONE_SOUNDS);
        moulding = (TemplateBlockBase) new Moulding(Identifier.of(MOD_ID, "moulding")).setTranslationKey(MOD_ID, "moulding");
        corner = (TemplateBlockBase) new Corner(Identifier.of(MOD_ID, "corner")).setTranslationKey(MOD_ID, "corner");
        blockDispenser = (TemplateBlockWithEntity) new BlockDispenser(Identifier.of(MOD_ID, "blockDispenser")).setTranslationKey(MOD_ID, "block_dispenser").setHardness(3.5F).setSounds(BlockBase.STONE_SOUNDS);
        cauldron = (TemplateBlockWithEntity) new Cauldron(Identifier.of(MOD_ID, "cauldron")).setTranslationKey(MOD_ID, "cauldron").setHardness(3.5F).setBlastResistance(10F).setSounds(BlockBase.METAL_SOUNDS);
        detectorRailWood = (TemplateDetectorRail) new DetectorRailVariants(Identifier.of(MOD_ID, "detectorRailWood"), 22).setTranslationKey(MOD_ID, "detector_rail_wood");
        detectorRailObsidian = (TemplateDetectorRail) new DetectorRailVariants(Identifier.of(MOD_ID, "detectorRailObsidian"), 23).setTranslationKey(MOD_ID, "detector_rail_obsidian");
        companionCube = (TemplateBlockBase) new CompanionCube(Identifier.of(MOD_ID, "companionCube")).setTranslationKey(MOD_ID, "companion_cube").setHardness(0.4F).setSounds(BlockBase.WOOL_SOUNDS);
        blockDetector = (TemplateBlockBase) new DetectorBlock(Identifier.of(MOD_ID, "blockDetector")).setTranslationKey(MOD_ID, "block_detector").setHardness(3.5F).setSounds(BlockBase.STONE_SOUNDS);
        blockDetectorLogic = (TemplateBlockBase) new DetectorLogic(Identifier.of(MOD_ID, "blockDetectorLogic")).setTranslationKey(MOD_ID, "block_detector_logic");
        hempCrop = (TemplatePlant) new HempCrop(Identifier.of(MOD_ID, "hempCrop")).setTranslationKey(MOD_ID, "hemp_crop");
        handCrank = (TemplateBlockBase) new HandCrank(Identifier.of(MOD_ID, "handCrank")).setTranslationKey(MOD_ID, "hand_crank");
        millStone = (TemplateBlockWithEntity) new MillStone(Identifier.of(MOD_ID, "millStone")).setTranslationKey(MOD_ID, "mill_stone");
        anchor = (TemplateBlockBase) new Anchor(Identifier.of(MOD_ID, "anchor")).setTranslationKey(MOD_ID, "anchor");
        rope = (TemplateBlockBase) new net.kozibrodka.wolves.blocks.Rope(Identifier.of(MOD_ID, "rope")).setTranslationKey(MOD_ID, "rope");
        omniSlab = (TemplateBlockBase) new OmniSlab(Identifier.of(MOD_ID, "omniSlab")).setTranslationKey(MOD_ID, "omni_slab");
        axleBlock = (TemplateBlockBase) new Axle(Identifier.of(MOD_ID, "axleBlock")).setTranslationKey(MOD_ID, "axle_block");
        gearBox = (TemplateBlockBase) new GearBox(Identifier.of(MOD_ID, "gearBox")).setTranslationKey(MOD_ID, "gear_box");
        turntable = (TemplateBlockWithEntity) new Turntable(Identifier.of(MOD_ID, "turntable")).setTranslationKey(MOD_ID, "turntable");
        bellows = (TemplateBlockBase) new Bellows(Identifier.of(MOD_ID, "bellows")).setTranslationKey(MOD_ID, "bellows");
        stokedFire = (TemplateFire) new StokedFire(Identifier.of(MOD_ID, "stokedFire")).setTranslationKey(MOD_ID, "stoked_fire");
        unfiredPottery = (TemplateBlockWithEntity) new UnfiredPottery(Identifier.of(MOD_ID, "unfiredPottery")).setTranslationKey(MOD_ID, "unfired_pottery");
        crucible = (TemplateBlockWithEntity) new Crucible(Identifier.of(MOD_ID, "crucible")).setTranslationKey(MOD_ID, "crucible");
        planter = (TemplateBlockBase) new Planter(Identifier.of(MOD_ID, "planter")).setTranslationKey(MOD_ID, "planter");
        vase = (TemplateBlockWithEntity) new Vase(Identifier.of(MOD_ID, "vase")).setTranslationKey(MOD_ID, "vase");
        superiorDetectorBlock = new DetectorBlockButNotCompletelyAwful(Identifier.of(MOD_ID, "superior_detector_block"), Material.DOODADS);

        //TODO: Extra (test) - later move into expansion probably
        //TODO: My idea: remove crafting of stone omni plates/ corners, moulding and add grinder as a saw for "stone type" blocks
        //TODO: Omni slabs are really cursed and kinda bad, especially with possible mods that upgrade slabs/stairs to modern (i actually have one in development). I suggest panels instead - would be great addon btw brings.
        panelNumber1 = (TemplateBlockBase) new Panel(Identifier.of(MOD_ID, "fcPanel1"), BlockBase.SAND).setTranslationKey(MOD_ID, "fc_panel_1"); //test panels later hide
        panelNumber2 = (TemplateBlockBase) new Panel(Identifier.of(MOD_ID, "fcPanel2"), BlockBase.PUMPKIN).setTranslationKey(MOD_ID, "fcPanel_2");
        panelNumber3 = (TemplateBlockBase) new Panel(Identifier.of(MOD_ID, "fcPanel3"), crucible).setTranslationKey(MOD_ID, "fc_panel3");
        panelNumber4 = (TemplateBlockBase) new Panel(Identifier.of(MOD_ID, "fcPanel4"), BlockBase.PORTAL).setTranslationKey(MOD_ID, "fc_panel_4");
    }

    public static TemplateBlockBase anvil;
    public static TemplateBlockBase lightBulbOff;
    public static TemplateBlockBase lightBulbOn;
    public static TemplateBlockBase hibachi;
    public static TemplateBlockWithEntity hopper;
    public static TemplateBlockBase saw;
    public static TemplateBlockBase platform;
    public static TemplateBlockWithEntity cement;
    public static TemplateBlockWithEntity pulley;
    public static TemplatePressurePlate pressurePlateObsidian;
    public static TemplateBlockBase moulding;
    public static TemplateBlockBase corner;
    public static TemplateBlockWithEntity blockDispenser;
    public static TemplateBlockWithEntity cauldron;
    public static TemplateDetectorRail detectorRailWood;
    public static TemplateDetectorRail detectorRailObsidian;
    public static TemplateBlockBase companionCube;
    public static TemplateBlockBase blockDetector;
    public static TemplateBlockBase blockDetectorLogic;
    public static TemplatePlant hempCrop;
    public static TemplateBlockBase handCrank;
    public static TemplateBlockWithEntity millStone;
    public static TemplateBlockBase anchor;
    public static TemplateBlockBase rope;
    public static TemplateBlockBase omniSlab;
    public static TemplateBlockBase axleBlock;
    public static TemplateBlockBase gearBox;
    public static TemplateBlockWithEntity turntable;
    public static TemplateBlockBase bellows;
    public static TemplateFire stokedFire;
    public static TemplateBlockWithEntity unfiredPottery;
    public static TemplateBlockWithEntity crucible;
    public static TemplateBlockBase planter;
    public static TemplateBlockWithEntity vase;
    public static DetectorBlockButNotCompletelyAwful superiorDetectorBlock;

    public static TemplateBlockBase blockOfWicker; //Mango Pack Addon
    public static TemplateBlockBase panelNumber1;
    public static TemplateBlockBase panelNumber2;
    public static TemplateBlockBase panelNumber3;
    public static TemplateBlockBase panelNumber4;
}
