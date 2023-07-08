package net.kozibrodka.wolves.events;

import net.glasslauncher.mods.api.gcapi.api.GConfig;
import net.kozibrodka.wolves.blocks.*;
import net.kozibrodka.wolves.entity.*;
import net.kozibrodka.wolves.glasscfg.BetterThanWolvesCFG;
import net.kozibrodka.wolves.items.*;
import net.kozibrodka.wolves.render.*;
import net.kozibrodka.wolves.tileentity.*;
import net.kozibrodka.wolves.utils.FCMaterialCement;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.block.PressurePlateTrigger;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColour;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.client.event.render.entity.EntityRendererRegisterEvent;
import net.modificationstation.stationapi.api.event.entity.EntityRegister;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.EntityHandlerRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.event.tileentity.TileEntityRegisterEvent;
import net.modificationstation.stationapi.api.item.tool.ToolMaterialFactory;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.template.block.*;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;
import net.modificationstation.stationapi.api.template.item.TemplateSeeds;
import net.modificationstation.stationapi.api.template.item.armour.TemplateArmour;
import net.modificationstation.stationapi.api.template.item.food.TemplateFoodBase;
import net.modificationstation.stationapi.api.template.item.tool.*;
import net.modificationstation.stationapi.api.util.Null;

public class mod_FCBetterThanWolves {

    @GConfig(value = "BetterThanWolvesCFG", visibleName = "Better Than Wolves Config")
    public static final BetterThanWolvesCFG wolvesGlass = new BetterThanWolvesCFG();

    @Entrypoint.ModID
    public static final ModID MOD_ID = Null.get();


    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        ToolMaterialFactory.create("STEEL",3,2250,12F,8);

        fcBucketCement = (TemplateBucket) new FCItemBucketCement(Identifier.of(MOD_ID, "fcBucketCement"), fcCement.id).setTranslationKey(MOD_ID, "fcBucketCement");
        fcWolfRaw = (TemplateFoodBase) new TemplateFoodBase(Identifier.of(MOD_ID, "fcWolfRaw"),3,false).setTranslationKey(MOD_ID, "fcWolfRaw");
        fcWolfCooked = (TemplateFoodBase) new TemplateFoodBase(Identifier.of(MOD_ID, "fcWolfCooked"),8,false).setTranslationKey(MOD_ID, "fcWolfCooked");
        fcNethercoal = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcNethercoal")).setTranslationKey(MOD_ID, "fcNethercoal");
        fcHempSeeds = (TemplateSeeds) new TemplateSeeds(Identifier.of(MOD_ID, "fcHempSeeds"), fcHempCrop.id).setTranslationKey(MOD_ID, "fcHempSeeds");
        fcHemp = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcHemp")).setTranslationKey(MOD_ID, "fcHemp");
        fcGear = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcGear")).setTranslationKey(MOD_ID, "fcGear");
        fcFlour = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcFlour")).setTranslationKey(MOD_ID, "fcFlour");
        fcHempFibers = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcHempFibers")).setTranslationKey(MOD_ID, "fcHempFibers");
        fcScouredLeather = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcScouredLeather")).setTranslationKey(MOD_ID, "fcScouredLeather");
        fcDonut = (TemplateFoodBase) new TemplateFoodBase(Identifier.of(MOD_ID, "fcDonut"),1,false).setTranslationKey(MOD_ID, "fcDonut").setMaxStackSize(12);
        fcRopeItem = (TemplateItemBase) new FCItemRope(Identifier.of(MOD_ID, "fcRopeItem")).setTranslationKey(MOD_ID, "fcRopeItem");
        fcRollersItem = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcRollersItem")).setTranslationKey(MOD_ID, "fcRollersItem");
        fcDung = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcDung")).setTranslationKey(MOD_ID, "fcDung");
        fcWaterWheelItem = (TemplateItemBase) new FCItemWaterWheel(Identifier.of(MOD_ID, "fcWaterWheelItem")).setTranslationKey(MOD_ID, "fcWaterWheelItem");
        fcWindMillBladeItem = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcWindMillBladeItem")).setTranslationKey(MOD_ID, "fcWindMillBladeItem").setMaxStackSize(1);
        fcWindMillItem = (TemplateItemBase) new FCItemWindMill(Identifier.of(MOD_ID, "fcWindMillItem")).setTranslationKey(MOD_ID, "fcWindMillItem");
        fcHempCloth = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcHempCloth")).setTranslationKey(MOD_ID, "fcHempCloth");
        fcGrate = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcGrate")).setTranslationKey(MOD_ID, "fcGrate");
        fcWicker = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcWicker")).setTranslationKey(MOD_ID, "fcWicker");
        fcTannedLeather = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcTannedLeather")).setTranslationKey(MOD_ID, "fcTannedLeather");
        fcStrap = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcStrap")).setTranslationKey(MOD_ID, "fcStrap");
        fcBelt = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcBelt")).setTranslationKey(MOD_ID, "fcBelt");
        fcFoulFood = (TemplateItemBase) new FCItemFoulFood(Identifier.of(MOD_ID, "fcFoulFood")).setTranslationKey(MOD_ID, "fcFoulFood");
        fcWoodBlade = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcWoodBlade")).setTranslationKey(MOD_ID, "fcWoodBlade");
        fcGlue = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcGlue")).setTranslationKey(MOD_ID, "fcGlue");
        fcTallow = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcTallow")).setTranslationKey(MOD_ID, "fcTallow");
        fcHaft = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcHaft")).setTranslationKey(MOD_ID, "fcHaft");
        fcSteel = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcSteel")).setTranslationKey(MOD_ID, "fcSteel");
//        fcRefinedPickAxe = new FCItemRefinedPickAxe(Identifier.of(MOD_ID, "fcRefinedPickAxe")).setTranslationKey(MOD_ID, "fcRefinedPickAxe");
//        fcRefinedShovel = new FCItemRefinedShovel(Identifier.of(MOD_ID, "fcRefinedShovel")).setTranslationKey(MOD_ID, "fcRefinedShovel");
//        fcRefinedHoe = new FCItemRefinedHoe(Identifier.of(MOD_ID, "fcRefinedHoe")).setTranslationKey(MOD_ID, "fcRefinedHoe");
//        fcRefinedAxe = new FCItemRefinedAxe(Identifier.of(MOD_ID, "fcRefinedAxe")).setTranslationKey(MOD_ID, "fcRefinedAxe");
//        fcRefinedSword = new FCItemRefinedSword(Identifier.of(MOD_ID, "fcRefinedSword")).setTranslationKey(MOD_ID, "fcRefinedSword");
        fcRefinedPickAxe = (TemplatePickaxe) new TemplatePickaxe(Identifier.of(MOD_ID, "fcRefinedPickAxe"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "fcRefinedPickAxe");
        fcRefinedShovel = (TemplateShovel) new TemplateShovel(Identifier.of(MOD_ID, "fcRefinedShovel"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "fcRefinedShovel");
        fcRefinedHoe = (TemplateHoe) new TemplateHoe(Identifier.of(MOD_ID, "fcRefinedHoe"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "fcRefinedHoe");
        fcRefinedAxe = (TemplateHatchet) new TemplateHatchet(Identifier.of(MOD_ID, "fcRefinedAxe"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "fcRefinedAxe");
        fcRefinedSword = (TemplateSword) new TemplateSword(Identifier.of(MOD_ID, "fcRefinedSword"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "fcRefinedSword");
        fcGroundNetherrack = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcGroundNetherrack")).setTranslationKey(MOD_ID, "fcGroundNetherrack");
        fcHellfireDust = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcHellfireDust")).setTranslationKey(MOD_ID, "fcHellfireDust");
        fcConcentratedHellfire = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcConcentratedHellfire")).setTranslationKey(MOD_ID, "fcConcentratedHellfire");
        fcArmorPlate = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcArmorPlate")).setTranslationKey(MOD_ID, "fcArmorPlate");
        fcPlateHelm = (TemplateArmour) new FCItemRefinedArmor(Identifier.of(MOD_ID, "fcPlateHelm"), 0).setTranslationKey(MOD_ID, "fcPlateHelm");
        fcPlateBreastPlate = (TemplateArmour) new FCItemRefinedArmor(Identifier.of(MOD_ID, "fcPlateBreastPlate"), 1).setTranslationKey(MOD_ID, "fcPlateBreastPlate");
        fcPlateLeggings = (TemplateArmour) new FCItemRefinedArmor(Identifier.of(MOD_ID, "fcPlateLeggings"), 2).setTranslationKey(MOD_ID, "fcPlateLeggings");
        fcPlateBoots = (TemplateArmour) new FCItemRefinedArmor(Identifier.of(MOD_ID, "fcPlateBoots"), 3).setTranslationKey(MOD_ID, "fcPlateBoots");
        fcCompositeBow = (TemplateItemBase) new FCItemCompositeBow(Identifier.of(MOD_ID, "fcCompositeBow")).setTranslationKey(MOD_ID, "fcCompositeBow");
        fcBroadheadArrowhead = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcBroadheadArrowhead")).setTranslationKey(MOD_ID, "fcBroadheadArrowhead");
        fcBroadheadArrow = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcBroadheadArrow")).setTranslationKey(MOD_ID, "fcBroadheadArrow");
        fcCoalDust = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcCoalDust")).setTranslationKey(MOD_ID, "fcCoalDust");
    }

    @EventListener
    public void registerBlocks(BlockRegistryEvent event) {
        fcAnvil = (TemplateBlockBase) new FCBlockAnvil(Identifier.of(MOD_ID, "fcAnvil")).setTranslationKey(MOD_ID, "fcAnvil");
        fcLightBulbOff = (TemplateBlockBase) new FCBlockLightBulb(Identifier.of(MOD_ID, "fcLightBulbOff")).setTranslationKey(MOD_ID, "fcLightBulbOff");
        fcLightBulbOn = (TemplateBlockBase) new FCBlockLightBulb(Identifier.of(MOD_ID, "fcLightBulbOn")).setTranslationKey(MOD_ID, "fcLightBulbOn").setLightEmittance(1.0F);
        fcBBQ = (TemplateBlockBase) new FCBlockBBQ(Identifier.of(MOD_ID, "fcBBQ")).setTranslationKey(MOD_ID, "fcBBQ");
        fcHopper = (TemplateBlockWithEntity) new FCBlockHopper(Identifier.of(MOD_ID, "fcHopper")).setTranslationKey(MOD_ID, "fcHopper");
        fcSaw = (TemplateBlockBase) new FCBlockSaw(Identifier.of(MOD_ID, "fcSaw")).setTranslationKey(MOD_ID, "fcSaw");
        fcPlatform = (TemplateBlockBase) new FCBlockPlatform(Identifier.of(MOD_ID, "fcPlatform")).setTranslationKey(MOD_ID, "fcPlatform");
//        fcBlockOfWicker = new TemplateBlockBase(Identifier.of(MOD_ID, "fcBlockOfWicker"), Material.ORGANIC).setHardness(0.5F).setTranslationKey(MOD_ID, "fcBlockOfWicker").setSounds(BlockBase.GRASS_SOUNDS); //Mango-Pack Addon
        fcCement = (TemplateBlockWithEntity) new FCBlockCement(Identifier.of(MOD_ID, "fcCement")).setTranslationKey(MOD_ID, "fcCement");
        fcPulley = (TemplateBlockWithEntity) new FCBlockPulley(Identifier.of(MOD_ID, "fcPulley")).setTranslationKey(MOD_ID, "fcPulley");
        fcPressurePlateObsidian = (TemplatePressurePlate) new TemplatePressurePlate(Identifier.of(MOD_ID, "fcPressurePlateObsidian"), 37, PressurePlateTrigger.field_1507, Material.STONE).setTranslationKey(MOD_ID, "fcPressurePlateObsidian").setHardness(0.5F).setBlastResistance(2000F).setSounds(BlockBase.STONE_SOUNDS);
        fcMoulding = (TemplateBlockBase) new FCBlockMoulding(Identifier.of(MOD_ID, "fcMoulding")).setTranslationKey(MOD_ID, "fcMoulding");
        fcCorner = (TemplateBlockBase) new FCBlockCorner(Identifier.of(MOD_ID, "fcCorner")).setTranslationKey(MOD_ID, "fcCorner");
        fcBlockDispenser = (TemplateBlockWithEntity) new FCBlockBlockDispenser(Identifier.of(MOD_ID, "fcBlockDispenser")).setTranslationKey(MOD_ID, "fcBlockDispenser").setHardness(3.5F).setSounds(BlockBase.STONE_SOUNDS).setTranslationKey(MOD_ID,"fcBlockDispenser");
        fcCauldron = (TemplateBlockWithEntity) new FCBlockCauldron(Identifier.of(MOD_ID, "fcCauldron")).setTranslationKey(MOD_ID, "fcCauldron").setHardness(3.5F).setBlastResistance(10F).setSounds(BlockBase.METAL_SOUNDS).setTranslationKey(MOD_ID,"fcCauldron");
        fcDetectorRailWood = (TemplateDetectorRail) new FCBlockDetectorRail(Identifier.of(MOD_ID, "fcDetectorRailWood"), 22).setTranslationKey(MOD_ID, "fcDetectorRailWood");
        fcDetectorRailObsidian = (TemplateDetectorRail) new FCBlockDetectorRail(Identifier.of(MOD_ID, "fcDetectorRailObsidian"), 23).setTranslationKey(MOD_ID, "fcDetectorRailObsidian");
        fcCompanionCube = (TemplateBlockBase) new FCBlockCompanionCube(Identifier.of(MOD_ID, "fcCompanionCube")).setTranslationKey(MOD_ID, "fcCompanionCube").setHardness(0.4F).setSounds(BlockBase.WOOL_SOUNDS);
        fcBlockDetector = (TemplateBlockBase) new FCBlockDetectorBlock(Identifier.of(MOD_ID, "fcBlockDetector")).setTranslationKey(MOD_ID, "fcBlockDetector").setHardness(3.5F).setSounds(BlockBase.STONE_SOUNDS).setTranslationKey(MOD_ID,"fcBlockDetector");
        fcBlockDetectorLogic = (TemplateBlockBase) new FCBlockDetectorLogic(Identifier.of(MOD_ID, "fcBlockDetectorLogic")).setTranslationKey(MOD_ID, "fcBlockDetectorLogic");
//        fcBlockLens = new FCBlockLens(Identifier.of(MOD_ID, "fcBlockLens")).setTranslationKey(MOD_ID, "fcBlockLens"); //TODO chyba tego nie ma
        // TODO: Translate the TODO above into English
        fcHempCrop = (TemplatePlant) new FCBlockHempCrop(Identifier.of(MOD_ID, "fcHempCrop")).setTranslationKey(MOD_ID, "fcHempCrop");
        fcHandCrank = (TemplateBlockBase) new FCBlockHandCrank(Identifier.of(MOD_ID, "fcHandCrank")).setTranslationKey(MOD_ID, "fcHandCrank");
        fcMillStone = (TemplateBlockWithEntity) new FCBlockMillStone(Identifier.of(MOD_ID, "fcMillStone")).setTranslationKey(MOD_ID, "fcMillStone");
        fcAnchor = (TemplateBlockBase) new FCBlockAnchor(Identifier.of(MOD_ID, "fcAnchor")).setTranslationKey(MOD_ID, "fcAnchor");
        fcRopeBlock = (TemplateBlockBase) new FCBlockRope(Identifier.of(MOD_ID, "fcRopeBlock")).setTranslationKey(MOD_ID, "fcRopeBlock");
        fcOmniSlab = (TemplateBlockBase) new FCBlockOmniSlab(Identifier.of(MOD_ID, "fcOmniSlab")).setTranslationKey(MOD_ID, "fcOmniSlab");
        fcAxleBlock = (TemplateBlockBase) new FCBlockAxle(Identifier.of(MOD_ID, "fcAxleBlock")).setTranslationKey(MOD_ID, "fcAxleBlock");
        fcGearBox = (TemplateBlockBase) new FCBlockGearBox(Identifier.of(MOD_ID, "fcGearBox")).setTranslationKey(MOD_ID, "fcGearBox");
        fcTurntable = (TemplateBlockWithEntity) new FCBlockTurntable(Identifier.of(MOD_ID, "fcTurntable")).setTranslationKey(MOD_ID, "fcTurntable");
        fcBellows = (TemplateBlockBase) new FCBlockBellows(Identifier.of(MOD_ID, "fcBellows")).setTranslationKey(MOD_ID, "fcBellows");
        fcStokedFire = (TemplateBlockBase) new FCBlockStokedFire(Identifier.of(MOD_ID, "fcStokedFire"), Material.FIRE).setTranslationKey(MOD_ID, "fcStokedFire");
        fcUnfiredPottery = (TemplateBlockWithEntity) new FCBlockUnfiredPottery(Identifier.of(MOD_ID, "fcUnfiredPottery")).setTranslationKey(MOD_ID, "fcUnfiredPottery");
        fcCrucible = (TemplateBlockWithEntity) new FCBlockCrucible(Identifier.of(MOD_ID, "fcCrucible")).setTranslationKey(MOD_ID, "fcCrucible");
        fcPlanter = (TemplateBlockBase) new FCBlockPlanter(Identifier.of(MOD_ID, "fcPlanter")).setTranslationKey(MOD_ID, "fcPlanter");
        fcVase = (TemplateBlockWithEntity) new FCBlockVase(Identifier.of(MOD_ID, "fcVase")).setTranslationKey(MOD_ID, "fcVase");
    }

    @EventListener
    private static void registerEntities(EntityRegister event) {
        event.register(FCEntityWaterWheel.class, String.valueOf(Identifier.of(MOD_ID, "WaterWheel")));
        event.register(FCEntityWindMill.class, String.valueOf(Identifier.of(MOD_ID, "WindMill")));
        event.register(FCEntityBroadheadArrow.class, String.valueOf(Identifier.of(MOD_ID, "BroadheadArrow")));
        event.register(FCEntityBlockLiftedByPlatform.class, String.valueOf(Identifier.of(MOD_ID, "BlockLiftedByPlatform")));
        event.register(FCEntityMovingPlatform.class, String.valueOf(Identifier.of(MOD_ID, "MovingPlatform")));
        event.register(FCEntityMovingAnchor.class, String.valueOf(Identifier.of(MOD_ID, "MovingAnchor")));
    }

    @EventListener
    private static void registerMobHandlers(EntityHandlerRegistryEvent event) {
        Registry.register(event.registry, MOD_ID.id("WaterWheel") , FCEntityWaterWheel::new);
        Registry.register(event.registry, MOD_ID.id("WindMill") , FCEntityWindMill::new);
        Registry.register(event.registry, MOD_ID.id("BroadheadArrow") , FCEntityBroadheadArrow::new);
        Registry.register(event.registry, MOD_ID.id("BlockLiftedByPlatform") , FCEntityBlockLiftedByPlatform::new);
        Registry.register(event.registry, MOD_ID.id("MovingPlatform") , FCEntityMovingPlatform::new);
        Registry.register(event.registry, MOD_ID.id("MovingAnchor") , FCEntityMovingAnchor::new);
    }

    @EventListener
    private static void registerEntityRenderers(EntityRendererRegisterEvent event) {
        event.renderers.put(FCEntityWaterWheel.class, new FCRenderWaterWheel());
        event.renderers.put(FCEntityWindMill.class, new FCRenderWindMill());
        event.renderers.put(FCEntityBroadheadArrow.class, new FCRenderBroadheadArrow());
        event.renderers.put(FCEntityBlockLiftedByPlatform.class, new FCRenderBlockLiftedByPlatform());
        event.renderers.put(FCEntityMovingPlatform.class, new FCRenderMovingPlatform());
        event.renderers.put(FCEntityMovingAnchor.class, new FCRenderMovingAnchor());
    }

    @EventListener
    private static void registerTileEntities(TileEntityRegisterEvent event) {
        event.register(FCTileEntityMillStone.class, String.valueOf(Identifier.of(MOD_ID, "TileMillStone")));
        event.register(FCTileEntityCrucible.class, String.valueOf(Identifier.of(MOD_ID, "TileCrucible")));
        event.register(FCTileEntityCauldron.class, String.valueOf(Identifier.of(MOD_ID, "TileCauldron")));
        event.register(FCTileEntityPulley.class, String.valueOf(Identifier.of(MOD_ID, "TilePulley")));
        event.register(FCTileEntityTurntable.class, String.valueOf(Identifier.of(MOD_ID, "TileTurntable")));
        event.register(FCTileEntityHopper.class, String.valueOf(Identifier.of(MOD_ID, "TileHooper")));
        event.register(FCTileEntityBlockDispenser.class, String.valueOf(Identifier.of(MOD_ID, "TileDispenser")));
        event.register(FCTileEntityUnfiredPottery.class, String.valueOf(Identifier.of(MOD_ID, "TilePottery")));
        event.register(FCTileEntityVase.class, String.valueOf(Identifier.of(MOD_ID, "TileVase")));
        event.register(FCTileEntityCement.class, String.valueOf(Identifier.of(MOD_ID, "TileCement")));
    }

    //TODO EXTRA: Saw animated, Anvil gravity, PANELS/options

    //TODO: NEW
    //TODO: render Inventory : rails, cement
    //TODO: Stoked fire dissaperaing?
    //TODO: custom recipes xhmi
    //TODO: planter functionality


    public static boolean fcDisableAxeChanges = false;
    public static boolean fcFaceGearBoxAwayFromPlayer = false;
    public static boolean fcDisableMinecartChanges = false;
    public static final Material fcCementMaterial = new FCMaterialCement(MaterialColour.STONE);;
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
    public static TemplateBlockBase fcStokedFire;
    public static TemplateBlockWithEntity fcUnfiredPottery;
    public static TemplateBlockWithEntity fcCrucible;
    public static TemplateBlockBase fcPlanter;
    public static TemplateBlockWithEntity fcVase;


    public static TemplateBlockWithEntity fcVase_white;
    public static TemplateBlockWithEntity fcVase_orange;
    public static TemplateBlockWithEntity fcVase_magenta;
    public static TemplateBlockWithEntity fcVase_light_blue;
    public static TemplateBlockWithEntity fcVase_yellow;
    public static TemplateBlockWithEntity fcVase_lime;
    public static TemplateBlockWithEntity fcVase_pink;
    public static TemplateBlockWithEntity fcVase_gray;
    public static TemplateBlockWithEntity fcVase_light_gray;
    public static TemplateBlockWithEntity fcVase_cyan;
    public static TemplateBlockWithEntity fcVase_purple;
    public static TemplateBlockWithEntity fcVase_blue;
    public static TemplateBlockWithEntity fcVase_brown;
    public static TemplateBlockWithEntity fcVase_green;
    public static TemplateBlockWithEntity fcVase_red;
    public static TemplateBlockWithEntity fcVase_black;
    // These are never registered! Why do they exist?
    public static TemplateBlockWithEntity fcUnfiredPottery_crucible;
    public static TemplateBlockWithEntity fcUnfiredPottery_planter;
    public static TemplateBlockWithEntity fcUnfiredPottery_vase;
//    public static TemplateBlockBase fcPanel_wood;
//    public static TemplateBlockBase fcMoulding_wood;
//    public static TemplateBlockBase fcCorner_wood;
    public static TemplateBlockBase fcBlockOfWicker; //Mango Pack Addon

    public static TemplateBucket fcBucketCement;
    public static TemplateFoodBase fcWolfRaw;
    public static TemplateFoodBase fcWolfCooked;
    public static TemplateItemBase fcNethercoal;
    public static TemplateSeeds fcHempSeeds;
    public static TemplateItemBase fcHemp;
    public static TemplateItemBase fcGear;
    public static TemplateItemBase fcFlour;
    public static TemplateItemBase fcHempFibers;
    public static TemplateItemBase fcScouredLeather;
    public static TemplateFoodBase fcDonut;
    public static TemplateItemBase fcRopeItem;
    public static TemplateItemBase fcRollersItem;
    public static TemplateItemBase fcDung;
    public static TemplateItemBase fcWaterWheelItem;
    public static TemplateItemBase fcWindMillBladeItem;
    public static TemplateItemBase fcWindMillItem;
    public static TemplateItemBase fcHempCloth;
    public static TemplateItemBase fcGrate;
    public static TemplateItemBase fcWicker;
    public static TemplateItemBase fcTannedLeather;
    public static TemplateItemBase fcStrap;
    public static TemplateItemBase fcBelt;
    public static TemplateItemBase fcFoulFood;
    public static TemplateItemBase fcWoodBlade;
    public static TemplateItemBase fcGlue;
    public static TemplateItemBase fcTallow;
    public static TemplateItemBase fcHaft;
    public static TemplateItemBase fcSteel;
    public static TemplatePickaxe fcRefinedPickAxe;
    public static TemplateShovel fcRefinedShovel;
    public static TemplateHoe fcRefinedHoe;
    public static TemplateHatchet fcRefinedAxe;
    public static TemplateSword fcRefinedSword;
    public static TemplateItemBase fcGroundNetherrack;
    public static TemplateItemBase fcHellfireDust;
    public static TemplateItemBase fcConcentratedHellfire;
    public static TemplateItemBase fcArmorPlate;
    public static TemplateArmour fcPlateHelm;
    public static TemplateArmour fcPlateBreastPlate;
    public static TemplateArmour fcPlateLeggings;
    public static TemplateArmour fcPlateBoots;
    public static TemplateItemBase fcCompositeBow;
    public static TemplateItemBase fcBroadheadArrowhead;
    public static TemplateItemBase fcBroadheadArrow;
    public static TemplateItemBase fcCoalDust;
}
