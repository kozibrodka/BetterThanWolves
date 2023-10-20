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
        fcAnvil = (TemplateBlockBase) new Anvil(Identifier.of(MOD_ID, "fcAnvil")).setTranslationKey(MOD_ID, "fcAnvil");
        fcLightBulbOff = (TemplateBlockBase) new LightBulb(Identifier.of(MOD_ID, "fcLightBulbOff")).setTranslationKey(MOD_ID, "fcLightBulbOff");
        fcLightBulbOn = (TemplateBlockBase) new LightBulb(Identifier.of(MOD_ID, "fcLightBulbOn")).setTranslationKey(MOD_ID, "fcLightBulbOn").setLightEmittance(1.0F);
        fcBBQ = (TemplateBlockBase) new Hibachi(Identifier.of(MOD_ID, "fcBBQ")).setTranslationKey(MOD_ID, "fcBBQ");
        fcHopper = (TemplateBlockWithEntity) new Hopper(Identifier.of(MOD_ID, "fcHopper")).setTranslationKey(MOD_ID, "fcHopper");
        fcSaw = (TemplateBlockBase) new Saw(Identifier.of(MOD_ID, "fcSaw")).setTranslationKey(MOD_ID, "fcSaw");
        fcPlatform = (TemplateBlockBase) new Platform(Identifier.of(MOD_ID, "fcPlatform")).setTranslationKey(MOD_ID, "fcPlatform");
        fcBlockOfWicker = (TemplateBlockBase) new Wicker(Identifier.of(MOD_ID, "fcBlockOfWicker"), Material.ORGANIC).setHardness(0.5F).setTranslationKey(MOD_ID, "fcBlockOfWicker").setSounds(BlockBase.GRASS_SOUNDS); //Mango-Pack Addon
        fcCement = (TemplateBlockWithEntity) new net.kozibrodka.wolves.blocks.Cement(Identifier.of(MOD_ID, "fcCement")).setTranslationKey(MOD_ID, "fcCement");
        fcPulley = (TemplateBlockWithEntity) new Pulley(Identifier.of(MOD_ID, "fcPulley")).setTranslationKey(MOD_ID, "fcPulley");
        fcPressurePlateObsidian = (TemplatePressurePlate) new TemplatePressurePlate(Identifier.of(MOD_ID, "fcPressurePlateObsidian"), 37, PressurePlateTrigger.field_1507, Material.STONE).setTranslationKey(MOD_ID, "fcPressurePlateObsidian").setHardness(0.5F).setBlastResistance(2000F).setSounds(BlockBase.STONE_SOUNDS);
        fcMoulding = (TemplateBlockBase) new Moulding(Identifier.of(MOD_ID, "fcMoulding")).setTranslationKey(MOD_ID, "fcMoulding");
        fcCorner = (TemplateBlockBase) new Corner(Identifier.of(MOD_ID, "fcCorner")).setTranslationKey(MOD_ID, "fcCorner");
        fcBlockDispenser = (TemplateBlockWithEntity) new BlockDispenser(Identifier.of(MOD_ID, "fcBlockDispenser")).setTranslationKey(MOD_ID, "fcBlockDispenser").setHardness(3.5F).setSounds(BlockBase.STONE_SOUNDS).setTranslationKey(MOD_ID,"fcBlockDispenser");
        fcCauldron = (TemplateBlockWithEntity) new Cauldron(Identifier.of(MOD_ID, "fcCauldron")).setTranslationKey(MOD_ID, "fcCauldron").setHardness(3.5F).setBlastResistance(10F).setSounds(BlockBase.METAL_SOUNDS).setTranslationKey(MOD_ID,"fcCauldron");
        fcDetectorRailWood = (TemplateDetectorRail) new DetectorRailVariants(Identifier.of(MOD_ID, "fcDetectorRailWood"), 22).setTranslationKey(MOD_ID, "fcDetectorRailWood");
        fcDetectorRailObsidian = (TemplateDetectorRail) new DetectorRailVariants(Identifier.of(MOD_ID, "fcDetectorRailObsidian"), 23).setTranslationKey(MOD_ID, "fcDetectorRailObsidian");
        fcCompanionCube = (TemplateBlockBase) new CompanionCube(Identifier.of(MOD_ID, "fcCompanionCube")).setTranslationKey(MOD_ID, "fcCompanionCube").setHardness(0.4F).setSounds(BlockBase.WOOL_SOUNDS);
        fcBlockDetector = (TemplateBlockBase) new DetectorBlock(Identifier.of(MOD_ID, "fcBlockDetector")).setTranslationKey(MOD_ID, "fcBlockDetector").setHardness(3.5F).setSounds(BlockBase.STONE_SOUNDS).setTranslationKey(MOD_ID,"fcBlockDetector");
        fcBlockDetectorLogic = (TemplateBlockBase) new DetectorLogic(Identifier.of(MOD_ID, "fcBlockDetectorLogic")).setTranslationKey(MOD_ID, "fcBlockDetectorLogic");
        fcHempCrop = (TemplatePlant) new HempCrop(Identifier.of(MOD_ID, "fcHempCrop")).setTranslationKey(MOD_ID, "fcHempCrop");
        fcHandCrank = (TemplateBlockBase) new HandCrank(Identifier.of(MOD_ID, "fcHandCrank")).setTranslationKey(MOD_ID, "fcHandCrank");
        fcMillStone = (TemplateBlockWithEntity) new MillStone(Identifier.of(MOD_ID, "fcMillStone")).setTranslationKey(MOD_ID, "fcMillStone");
        fcAnchor = (TemplateBlockBase) new Anchor(Identifier.of(MOD_ID, "fcAnchor")).setTranslationKey(MOD_ID, "fcAnchor");
        fcRopeBlock = (TemplateBlockBase) new net.kozibrodka.wolves.blocks.Rope(Identifier.of(MOD_ID, "fcRopeBlock")).setTranslationKey(MOD_ID, "fcRopeBlock");
        fcOmniSlab = (TemplateBlockBase) new OmniSlab(Identifier.of(MOD_ID, "fcOmniSlab")).setTranslationKey(MOD_ID, "fcOmniSlab");
        fcAxleBlock = (TemplateBlockBase) new Axle(Identifier.of(MOD_ID, "fcAxleBlock")).setTranslationKey(MOD_ID, "fcAxleBlock");
        fcGearBox = (TemplateBlockBase) new GearBox(Identifier.of(MOD_ID, "fcGearBox")).setTranslationKey(MOD_ID, "fcGearBox");
        fcTurntable = (TemplateBlockWithEntity) new Turntable(Identifier.of(MOD_ID, "fcTurntable")).setTranslationKey(MOD_ID, "fcTurntable");
        fcBellows = (TemplateBlockBase) new Bellows(Identifier.of(MOD_ID, "fcBellows")).setTranslationKey(MOD_ID, "fcBellows");
        fcStokedFire = (TemplateFire) new StokedFire(Identifier.of(MOD_ID, "fcStokedFire")).setTranslationKey(MOD_ID, "fcStokedFire");
        fcUnfiredPottery = (TemplateBlockWithEntity) new UnfiredPottery(Identifier.of(MOD_ID, "fcUnfiredPottery")).setTranslationKey(MOD_ID, "fcUnfiredPottery");
        fcCrucible = (TemplateBlockWithEntity) new Crucible(Identifier.of(MOD_ID, "fcCrucible")).setTranslationKey(MOD_ID, "fcCrucible");
        fcPlanter = (TemplateBlockBase) new Planter(Identifier.of(MOD_ID, "fcPlanter")).setTranslationKey(MOD_ID, "fcPlanter");
        fcVase = (TemplateBlockWithEntity) new Vase(Identifier.of(MOD_ID, "fcVase")).setTranslationKey(MOD_ID, "fcVase");
        superiorDetectorBlock = new DetectorBlockButNotCompletelyAwful(Identifier.of(MOD_ID, "superior_detector_block"), Material.DOODADS);

        //TODO: Extra (test) - later move into expansion probably
        //TODO: My idea: remove crafting of stone omni plates/ corners, moulding and add grinder as a saw for "stone type" blocks
        //TODO: Omni slabs are really cursed and kinda bad, especially with possible mods that upgrade slabs/stairs to modern (i actually have one in development). I suggest panels instead - would be great addon btw brings.
        fcPanelNumber1 = (TemplateBlockBase) new Panel(Identifier.of(MOD_ID, "fcPanel1"), BlockBase.SAND).setTranslationKey(MOD_ID, "fcPanel1"); //test panels later hide
        fcPanelNumber2 = (TemplateBlockBase) new Panel(Identifier.of(MOD_ID, "fcPanel2"), BlockBase.PUMPKIN).setTranslationKey(MOD_ID, "fcPanel2");
        fcPanelNumber3 = (TemplateBlockBase) new Panel(Identifier.of(MOD_ID, "fcPanel3"), fcCrucible).setTranslationKey(MOD_ID, "fcPanel3");
        fcPanelNumber4 = (TemplateBlockBase) new Panel(Identifier.of(MOD_ID, "fcPanel4"), BlockBase.PORTAL).setTranslationKey(MOD_ID, "fcPanel4");
    }

    public static TemplateBlockBase fcAnvil;
    public static TemplateBlockBase fcLightBulbOff;
    public static TemplateBlockBase fcLightBulbOn;
    public static TemplateBlockBase fcBBQ;
    public static TemplateBlockWithEntity fcHopper;
    public static TemplateBlockBase fcSaw;
    public static TemplateBlockBase fcPlatform;
    public static TemplateBlockWithEntity fcCement;
    public static TemplateBlockWithEntity fcPulley;
    public static TemplatePressurePlate fcPressurePlateObsidian;
    public static TemplateBlockBase fcMoulding;
    public static TemplateBlockBase fcCorner;
    public static TemplateBlockWithEntity fcBlockDispenser;
    public static TemplateBlockWithEntity fcCauldron;
    public static TemplateDetectorRail fcDetectorRailWood;
    public static TemplateDetectorRail fcDetectorRailObsidian;
    public static TemplateBlockBase fcCompanionCube;
    public static TemplateBlockBase fcCompanionCube_slab;
    public static TemplateBlockBase fcBlockDetector;
    public static TemplateBlockBase fcBlockDetectorLogic;
    public static TemplateBlockBase fcBlockLens;
    public static TemplatePlant fcHempCrop;
    public static TemplateBlockBase fcHandCrank;
    public static TemplateBlockWithEntity fcMillStone;
    public static TemplateBlockBase fcAnchor;
    public static TemplateBlockBase fcRopeBlock;
    public static TemplateBlockBase fcOmniSlab;
    public static TemplateBlockBase fcAxleBlock;
    public static TemplateBlockBase fcGearBox;
    public static TemplateBlockWithEntity fcTurntable;
    public static TemplateBlockBase fcBellows;
    public static TemplateFire fcStokedFire;
    public static TemplateBlockWithEntity fcUnfiredPottery;
    public static TemplateBlockWithEntity fcCrucible;
    public static TemplateBlockBase fcPlanter;
    public static TemplateBlockWithEntity fcVase;
    public static DetectorBlockButNotCompletelyAwful superiorDetectorBlock;

    public static TemplateBlockBase fcBlockOfWicker; //Mango Pack Addon
    public static TemplateBlockBase fcPanelNumber1;
    public static TemplateBlockBase fcPanelNumber2;
    public static TemplateBlockBase fcPanelNumber3;
    public static TemplateBlockBase fcPanelNumber4;
}
