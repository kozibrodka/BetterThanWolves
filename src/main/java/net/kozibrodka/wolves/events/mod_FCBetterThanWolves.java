package net.kozibrodka.wolves.events;

import net.glasslauncher.mods.api.gcapi.api.GConfig;
import net.kozibrodka.wolves.blocks.*;
import net.kozibrodka.wolves.entity.*;
import net.kozibrodka.wolves.glasscfg.BetterThanWolvesCFG;
import net.kozibrodka.wolves.items.*;
import net.kozibrodka.wolves.render.FCRenderBroadheadArrow;
import net.kozibrodka.wolves.render.FCRenderWaterWheel;
import net.kozibrodka.wolves.render.FCRenderWindMill;
import net.kozibrodka.wolves.tileentity.FCTileEntityCauldron;
import net.kozibrodka.wolves.tileentity.FCTileEntityCrucible;
import net.kozibrodka.wolves.tileentity.FCTileEntityMillStone;
import net.kozibrodka.wolves.tileentity.FCTileEntityPulley;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.entity.PigRenderer;
import net.minecraft.client.render.entity.model.Pig;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.client.event.render.entity.EntityRendererRegisterEvent;
import net.modificationstation.stationapi.api.event.entity.EntityRegister;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.EntityHandlerRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.MobHandlerRegistryEvent;
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

//        fcBucketCement = new FCItemBucketCement(Identifier.of(MOD_ID, "fcBucketCement"), fcCement.id).setTranslationKey(MOD_ID, "fcBucketCement");
        fcWolfRaw = new TemplateFoodBase(Identifier.of(MOD_ID, "fcWolfRaw"),3,false).setTranslationKey(MOD_ID, "fcWolfRaw");
        fcWolfCooked = new TemplateFoodBase(Identifier.of(MOD_ID, "fcWolfCooked"),8,false).setTranslationKey(MOD_ID, "fcWolfCooked");
        fcNethercoal = new TemplateItemBase(Identifier.of(MOD_ID, "fcNethercoal")).setTranslationKey(MOD_ID, "fcNethercoal");
        fcHempSeeds = new TemplateSeeds(Identifier.of(MOD_ID, "fcHempSeeds"), fcHempCrop.id).setTranslationKey(MOD_ID, "fcHempSeeds");
        fcHemp = new TemplateItemBase(Identifier.of(MOD_ID, "fcHemp")).setTranslationKey(MOD_ID, "fcHemp");
        fcGear = new TemplateItemBase(Identifier.of(MOD_ID, "fcGear")).setTranslationKey(MOD_ID, "fcGear");
        fcFlour = new TemplateItemBase(Identifier.of(MOD_ID, "fcFlour")).setTranslationKey(MOD_ID, "fcFlour");
        fcHempFibers = new TemplateItemBase(Identifier.of(MOD_ID, "fcHempFibers")).setTranslationKey(MOD_ID, "fcHempFibers");
        fcScouredLeather = new TemplateItemBase(Identifier.of(MOD_ID, "fcScouredLeather")).setTranslationKey(MOD_ID, "fcScouredLeather");
        fcDonut = new TemplateFoodBase(Identifier.of(MOD_ID, "fcDonut"),1,false).setTranslationKey(MOD_ID, "fcDonut").setMaxStackSize(12);
        fcRopeItem = new FCItemRope(Identifier.of(MOD_ID, "fcRopeItem")).setTranslationKey(MOD_ID, "fcRopeItem");
        fcRollersItem = new TemplateItemBase(Identifier.of(MOD_ID, "fcRollersItem")).setTranslationKey(MOD_ID, "fcRollersItem");
        fcDung = new TemplateItemBase(Identifier.of(MOD_ID, "fcDung")).setTranslationKey(MOD_ID, "fcDung");
        fcWaterWheelItem = new FCItemWaterWheel(Identifier.of(MOD_ID, "fcWaterWheelItem")).setTranslationKey(MOD_ID, "fcWaterWheelItem");
        fcWindMillBladeItem = new TemplateItemBase(Identifier.of(MOD_ID, "fcWindMillBladeItem")).setTranslationKey(MOD_ID, "fcWindMillBladeItem").setMaxStackSize(1);
        fcWindMillItem = new FCItemWindMill(Identifier.of(MOD_ID, "fcWindMillItem")).setTranslationKey(MOD_ID, "fcWindMillItem");
        fcHempCloth = new TemplateItemBase(Identifier.of(MOD_ID, "fcHempCloth")).setTranslationKey(MOD_ID, "fcHempCloth");
        fcGrate = new TemplateItemBase(Identifier.of(MOD_ID, "fcGrate")).setTranslationKey(MOD_ID, "fcGrate");
        fcWicker = new TemplateItemBase(Identifier.of(MOD_ID, "fcWicker")).setTranslationKey(MOD_ID, "fcWicker");
        fcTannedLeather = new TemplateItemBase(Identifier.of(MOD_ID, "fcTannedLeather")).setTranslationKey(MOD_ID, "fcTannedLeather");
        fcStrap = new TemplateItemBase(Identifier.of(MOD_ID, "fcStrap")).setTranslationKey(MOD_ID, "fcStrap");
        fcBelt = new TemplateItemBase(Identifier.of(MOD_ID, "fcBelt")).setTranslationKey(MOD_ID, "fcBelt");
        fcFoulFood = new FCItemFoulFood(Identifier.of(MOD_ID, "fcFoulFood")).setTranslationKey(MOD_ID, "fcFoulFood");
        fcWoodBlade = new TemplateItemBase(Identifier.of(MOD_ID, "fcWoodBlade")).setTranslationKey(MOD_ID, "fcWoodBlade");
        fcGlue = new TemplateItemBase(Identifier.of(MOD_ID, "fcGlue")).setTranslationKey(MOD_ID, "fcGlue");
        fcTallow = new TemplateItemBase(Identifier.of(MOD_ID, "fcTallow")).setTranslationKey(MOD_ID, "fcTallow");
        fcHaft = new TemplateItemBase(Identifier.of(MOD_ID, "fcHaft")).setTranslationKey(MOD_ID, "fcHaft");
        fcSteel = new TemplateItemBase(Identifier.of(MOD_ID, "fcSteel")).setTranslationKey(MOD_ID, "fcSteel");
//        fcRefinedPickAxe = new FCItemRefinedPickAxe(Identifier.of(MOD_ID, "fcRefinedPickAxe")).setTranslationKey(MOD_ID, "fcRefinedPickAxe");
//        fcRefinedShovel = new FCItemRefinedShovel(Identifier.of(MOD_ID, "fcRefinedShovel")).setTranslationKey(MOD_ID, "fcRefinedShovel");
//        fcRefinedHoe = new FCItemRefinedHoe(Identifier.of(MOD_ID, "fcRefinedHoe")).setTranslationKey(MOD_ID, "fcRefinedHoe");
//        fcRefinedAxe = new FCItemRefinedAxe(Identifier.of(MOD_ID, "fcRefinedAxe")).setTranslationKey(MOD_ID, "fcRefinedAxe");
//        fcRefinedSword = new FCItemRefinedSword(Identifier.of(MOD_ID, "fcRefinedSword")).setTranslationKey(MOD_ID, "fcRefinedSword");
        fcRefinedPickAxe = new TemplatePickaxe(Identifier.of(MOD_ID, "fcRefinedPickAxe"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "fcRefinedPickAxe");
        fcRefinedShovel = new TemplateShovel(Identifier.of(MOD_ID, "fcRefinedShovel"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "fcRefinedShovel");
        fcRefinedHoe = new TemplateHoe(Identifier.of(MOD_ID, "fcRefinedHoe"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "fcRefinedHoe");
        fcRefinedAxe = new TemplateHatchet(Identifier.of(MOD_ID, "fcRefinedAxe"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "fcRefinedAxe");
        fcRefinedSword = new TemplateSword(Identifier.of(MOD_ID, "fcRefinedSword"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "fcRefinedSword");
        fcGroundNetherrack = new TemplateItemBase(Identifier.of(MOD_ID, "fcGroundNetherrack")).setTranslationKey(MOD_ID, "fcGroundNetherrack");
        fcHellfireDust = new TemplateItemBase(Identifier.of(MOD_ID, "fcHellfireDust")).setTranslationKey(MOD_ID, "fcHellfireDust");
        fcConcentratedHellfire = new TemplateItemBase(Identifier.of(MOD_ID, "fcConcentratedHellfire")).setTranslationKey(MOD_ID, "fcConcentratedHellfire");
        fcArmorPlate = new TemplateItemBase(Identifier.of(MOD_ID, "fcArmorPlate")).setTranslationKey(MOD_ID, "fcArmorPlate");
        fcPlateHelm = new FCItemRefinedArmor(Identifier.of(MOD_ID, "fcPlateHelm"), 0).setTranslationKey(MOD_ID, "fcPlateHelm");
        fcPlateBreastPlate = new FCItemRefinedArmor(Identifier.of(MOD_ID, "fcPlateBreastPlate"), 1).setTranslationKey(MOD_ID, "fcPlateBreastPlate");
        fcPlateLeggings = new FCItemRefinedArmor(Identifier.of(MOD_ID, "fcPlateLeggings"), 2).setTranslationKey(MOD_ID, "fcPlateLeggings");
        fcPlateBoots = new FCItemRefinedArmor(Identifier.of(MOD_ID, "fcPlateBoots"), 3).setTranslationKey(MOD_ID, "fcPlateBoots");
        fcCompositeBow = new FCItemCompositeBow(Identifier.of(MOD_ID, "fcCompositeBow")).setTranslationKey(MOD_ID, "fcCompositeBow");
        fcBroadheadArrowhead = new TemplateItemBase(Identifier.of(MOD_ID, "fcBroadheadArrowhead")).setTranslationKey(MOD_ID, "fcBroadheadArrowhead");
        fcBroadheadArrow = new TemplateItemBase(Identifier.of(MOD_ID, "fcBroadheadArrow")).setTranslationKey(MOD_ID, "fcBroadheadArrow");
        fcCoalDust = new TemplateItemBase(Identifier.of(MOD_ID, "fcCoalDust")).setTranslationKey(MOD_ID, "fcCoalDust");
    }

    @EventListener
    public void registerBlocks(BlockRegistryEvent event) {
//        fcAnvil = new FCBlockAnvil(Identifier.of(MOD_ID, "fcAnvil")).setTranslationKey(MOD_ID, "fcAnvil");
        fcLightBulbOff = new FCBlockLightBulb(Identifier.of(MOD_ID, "fcLightBulbOff")).setTranslationKey(MOD_ID, "fcLightBulbOff");
        fcLightBulbOn = new FCBlockLightBulb(Identifier.of(MOD_ID, "fcLightBulbOn")).setTranslationKey(MOD_ID, "fcLightBulbOn").setLightEmittance(1.0F);
        fcBBQ = new FCBlockBBQ(Identifier.of(MOD_ID, "fcBBQ")).setTranslationKey(MOD_ID, "fcBBQ");
//        fcHopper = new FCBlockHopper(Identifier.of(MOD_ID, "fcHopper")).setTranslationKey(MOD_ID, "fcHopper");
//        fcSaw = new FCBlockSaw(Identifier.of(MOD_ID, "fcSaw")).setTranslationKey(MOD_ID, "fcSaw");
        fcPlatform = new FCBlockPlatform(Identifier.of(MOD_ID, "fcPlatform")).setTranslationKey(MOD_ID, "fcPlatform");
//        fcBlockOfWicker = new TemplateBlockBase(Identifier.of(MOD_ID, "fcBlockOfWicker"), Material.ORGANIC).setTranslationKey(MOD_ID, "fcBlockOfWicker");
//        fcCement = new FCBlockCement(Identifier.of(MOD_ID, "fcCement")).setTranslationKey(MOD_ID, "fcCement");
        fcPulley = new FCBlockPulley(Identifier.of(MOD_ID, "fcPulley")).setTranslationKey(MOD_ID, "fcPulley");
//        fcPressurePlateObsidian = new TemplatePressurePlate(Identifier.of(MOD_ID, "fcPressurePlateObsidian"), 37, PressurePlateTrigger.field_1507, Material.STONE).setTranslationKey(MOD_ID, "fcPressurePlateObsidian").setHardness(0.5F).setBlastResistance(2000F).setSounds(BlockBase.STONE_SOUNDS);
//        fcMoulding = new FCBlockMoulding(Identifier.of(MOD_ID, "fcMoulding")).setTranslationKey(MOD_ID, "fcMoulding");
//        fcCorner = new FCBlockCorner(Identifier.of(MOD_ID, "fcCorner")).setTranslationKey(MOD_ID, "fcCorner");
//        fcBlockDispenser = new FCBlockBlockDispenser(Identifier.of(MOD_ID, "fcBlockDispenser")).setTranslationKey(MOD_ID, "fcBlockDispenser").setHardness(3.5F).setSounds(BlockBase.STONE_SOUNDS).setBlockName("fcBlockDispenser");
        fcCauldron = new FCBlockCauldron(Identifier.of(MOD_ID, "fcCauldron")).setTranslationKey(MOD_ID, "fcCauldron").setHardness(3.5F).setBlastResistance(10F).setSounds(BlockBase.METAL_SOUNDS).setTranslationKey(MOD_ID,"fcCauldron");
//        fcDetectorRailWood = new FCBlockDetectorRail(Identifier.of(MOD_ID, "fcDetectorRailWood"), 23).setTranslationKey(MOD_ID, "fcDetectorRailWood");
//        fcDetectorRailObsidian = new FCBlockDetectorRail(Identifier.of(MOD_ID, "fcDetectorRailObsidian"), 24).setTranslationKey(MOD_ID, "fcDetectorRailObsidian");
        fcCompanionCube = new FCBlockCompanionCube(Identifier.of(MOD_ID, "fcCompanionCube")).setTranslationKey(MOD_ID, "fcCompanionCube").setHardness(0.4F).setSounds(BlockBase.WOOL_SOUNDS); //wkurwilo mnie na razie
//        fcBlockDetector = new FCBlockDetectorBlock(Identifier.of(MOD_ID, "fcBlockDetector")).setTranslationKey(MOD_ID, "fcBlockDetector").setHardness(3.5F).setSounds(BlockBase.STONE_SOUNDS).setBlockName("fcBlockDetector");
//        fcBlockDetectorLogic = new FCBlockDetectorLogic(Identifier.of(MOD_ID, "fcBlockDetectorLogic")).setTranslationKey(MOD_ID, "fcBlockDetectorLogic");
//        fcBlockLens = new FCBlockLens(Identifier.of(MOD_ID, "fcBlockLens")).setTranslationKey(MOD_ID, "fcBlockLens");
        fcHempCrop = new FCBlockHempCrop(Identifier.of(MOD_ID, "fcHempCrop")).setTranslationKey(MOD_ID, "fcHempCrop");
        fcHandCrank = new FCBlockHandCrank(Identifier.of(MOD_ID, "fcHandCrank")).setTranslationKey(MOD_ID, "fcHandCrank");
        fcMillStone = new FCBlockMillStone(Identifier.of(MOD_ID, "fcMillStone")).setTranslationKey(MOD_ID, "fcMillStone");
        fcAnchor = new FCBlockAnchor(Identifier.of(MOD_ID, "fcAnchor")).setTranslationKey(MOD_ID, "fcAnchor");
        fcRopeBlock = new FCBlockRope(Identifier.of(MOD_ID, "fcRopeBlock")).setTranslationKey(MOD_ID, "fcRopeBlock");
//        fcOmniSlab = new FCBlockOmniSlab(Identifier.of(MOD_ID, "fcOmniSlab")).setTranslationKey(MOD_ID, "fcOmniSlab");
        fcAxleBlock = new FCBlockAxle(Identifier.of(MOD_ID, "fcAxleBlock")).setTranslationKey(MOD_ID, "fcAxleBlock");
        fcGearBox = new FCBlockGearBox(Identifier.of(MOD_ID, "fcGearBox")).setTranslationKey(MOD_ID, "fcGearBox");
        fcTurntable = new FCBlockTurntable(Identifier.of(MOD_ID, "fcTurntable")).setTranslationKey(MOD_ID, "fcTurntable");
        fcBellows = new FCBlockBellows(Identifier.of(MOD_ID, "fcBellows")).setTranslationKey(MOD_ID, "fcBellows");
        fcStokedFire = new FCBlockStokedFire(Identifier.of(MOD_ID, "fcStokedFire")).setTranslationKey(MOD_ID, "fcStokedFire");
//        fcUnfiredPottery = new FCBlockUnfiredPottery(Identifier.of(MOD_ID, "fcUnfiredPottery")).setTranslationKey(MOD_ID, "fcUnfiredPottery");
        fcCrucible = new FCBlockCrucible(Identifier.of(MOD_ID, "fcCrucible")).setTranslationKey(MOD_ID, "fcCrucible");
//        fcPlanter = new FCBlockPlanter(Identifier.of(MOD_ID, "fcPlanter")).setTranslationKey(MOD_ID, "fcPlanter");
//        fcVase = new FCBlockVase(Identifier.of(MOD_ID, "fcVase")).setTranslationKey(MOD_ID, "fcVase");
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
    }

    @EventListener
    private static void registerEntityRenderers(EntityRendererRegisterEvent event) {
        event.renderers.put(FCEntityWaterWheel.class, new FCRenderWaterWheel());
        event.renderers.put(FCEntityWindMill.class, new FCRenderWindMill());
        event.renderers.put(FCEntityBroadheadArrow.class, new FCRenderBroadheadArrow());
    }

    @EventListener
    private static void registerTileEntities(TileEntityRegisterEvent event) {
        event.register(FCTileEntityMillStone.class, String.valueOf(Identifier.of(MOD_ID, "TileMillStone")));
        event.register(FCTileEntityCrucible.class, String.valueOf(Identifier.of(MOD_ID, "TileCrucible")));
        event.register(FCTileEntityCauldron.class, String.valueOf(Identifier.of(MOD_ID, "TileCauldron")));
        event.register(FCTileEntityPulley.class, String.valueOf(Identifier.of(MOD_ID, "TilePulley")));
    }

    //TODO: Crucible MODEL, Companion Cube model, Platform Renderers and Models, Platform render,

    public static boolean fcDisableAxeChanges = false;
    public static boolean fcFaceGearBoxAwayFromPlayer = false;
    public static boolean fcDisableMinecartChanges = false;
//    public static final Material fcCementMaterial;
    public static TemplateBlockBase fcAnvil;
    public static TemplateBlockBase fcLightBulbOff;
    public static TemplateBlockBase fcLightBulbOn;
    public static TemplateBlockBase fcBBQ;
    public static TemplateBlockBase fcHopper;
    public static TemplateBlockBase fcSaw;
    public static TemplateBlockBase fcPlatform;
    public static TemplateBlockBase fcCement;
    public static TemplateBlockWithEntity fcPulley;
    public static TemplatePressurePlate fcPressurePlateObsidian;
    public static TemplateBlockBase fcMoulding;
    public static TemplateBlockBase fcCorner;
    public static TemplateBlockBase fcBlockDispenser;
    public static TemplateBlockWithEntity fcCauldron;
    public static TemplateBlockBase fcDetectorRailWood;
    public static TemplateBlockBase fcDetectorRailObsidian;
    public static TemplateBlockBase fcCompanionCube;
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
    public static TemplateBlockBase fcUnfiredPottery;
    public static TemplateBlockWithEntity fcCrucible;
    public static TemplateBlockBase fcPlanter;
    public static TemplateBlockBase fcVase;
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
