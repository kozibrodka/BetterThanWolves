package net.kozibrodka.wolves.events;

import net.glasslauncher.mods.api.gcapi.api.GConfig;
import net.kozibrodka.wolves.entity.*;
import net.kozibrodka.wolves.glasscfg.BetterThanWolvesCFG;
import net.kozibrodka.wolves.items.*;
import net.kozibrodka.wolves.items.Rope;
import net.kozibrodka.wolves.tileentity.*;
import net.kozibrodka.wolves.materials.Cement;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColour;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.event.entity.EntityRegister;
import net.modificationstation.stationapi.api.event.registry.EntityHandlerRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.event.tileentity.TileEntityRegisterEvent;
import net.modificationstation.stationapi.api.item.tool.ToolMaterialFactory;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.registry.Registry;
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

        fcBucketCement = (TemplateBucket) new BucketCement(Identifier.of(MOD_ID, "fcBucketCement"), BlockListener.cement.id).setTranslationKey(MOD_ID, "fcBucketCement");
        fcWolfRaw = (TemplateFoodBase) new TemplateFoodBase(Identifier.of(MOD_ID, "fcWolfRaw"),3,false).setTranslationKey(MOD_ID, "fcWolfRaw");
        fcWolfCooked = (TemplateFoodBase) new TemplateFoodBase(Identifier.of(MOD_ID, "fcWolfCooked"),8,false).setTranslationKey(MOD_ID, "fcWolfCooked");
        fcNethercoal = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcNethercoal")).setTranslationKey(MOD_ID, "fcNethercoal");
        fcHempSeeds = (TemplateSeeds) new TemplateSeeds(Identifier.of(MOD_ID, "fcHempSeeds"), BlockListener.hempCrop.id).setTranslationKey(MOD_ID, "fcHempSeeds");
        fcHemp = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcHemp")).setTranslationKey(MOD_ID, "fcHemp");
        fcGear = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcGear")).setTranslationKey(MOD_ID, "fcGear");
        fcFlour = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcFlour")).setTranslationKey(MOD_ID, "fcFlour");
        fcHempFibers = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcHempFibers")).setTranslationKey(MOD_ID, "fcHempFibers");
        fcScouredLeather = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcScouredLeather")).setTranslationKey(MOD_ID, "fcScouredLeather");
        fcDonut = (TemplateFoodBase) new TemplateFoodBase(Identifier.of(MOD_ID, "fcDonut"),1,false).setTranslationKey(MOD_ID, "fcDonut").setMaxStackSize(12);
        fcRopeItem = (TemplateItemBase) new Rope(Identifier.of(MOD_ID, "fcRopeItem")).setTranslationKey(MOD_ID, "fcRopeItem");
        fcRollersItem = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcRollersItem")).setTranslationKey(MOD_ID, "fcRollersItem");
        fcDung = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcDung")).setTranslationKey(MOD_ID, "fcDung");
        fcWaterWheelItem = (TemplateItemBase) new WaterWheel(Identifier.of(MOD_ID, "fcWaterWheelItem")).setTranslationKey(MOD_ID, "fcWaterWheelItem");
        fcWindMillBladeItem = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcWindMillBladeItem")).setTranslationKey(MOD_ID, "fcWindMillBladeItem").setMaxStackSize(1);
        fcWindMillItem = (TemplateItemBase) new WindMill(Identifier.of(MOD_ID, "fcWindMillItem")).setTranslationKey(MOD_ID, "fcWindMillItem");
        fcHempCloth = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcHempCloth")).setTranslationKey(MOD_ID, "fcHempCloth");
        fcGrate = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcGrate")).setTranslationKey(MOD_ID, "fcGrate");
        fcWicker = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcWicker")).setTranslationKey(MOD_ID, "fcWicker");
        fcTannedLeather = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcTannedLeather")).setTranslationKey(MOD_ID, "fcTannedLeather");
        fcStrap = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcStrap")).setTranslationKey(MOD_ID, "fcStrap");
        fcBelt = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcBelt")).setTranslationKey(MOD_ID, "fcBelt");
        fcFoulFood = (TemplateItemBase) new FoulFood(Identifier.of(MOD_ID, "fcFoulFood")).setTranslationKey(MOD_ID, "fcFoulFood");
        fcWoodBlade = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcWoodBlade")).setTranslationKey(MOD_ID, "fcWoodBlade");
        fcGlue = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcGlue")).setTranslationKey(MOD_ID, "fcGlue");
        fcTallow = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcTallow")).setTranslationKey(MOD_ID, "fcTallow");
        fcHaft = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcHaft")).setTranslationKey(MOD_ID, "fcHaft");
        fcSteel = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcSteel")).setTranslationKey(MOD_ID, "fcSteel");
        fcRefinedPickAxe = (TemplatePickaxe) new TemplatePickaxe(Identifier.of(MOD_ID, "fcRefinedPickAxe"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "fcRefinedPickAxe");
        fcRefinedShovel = (TemplateShovel) new TemplateShovel(Identifier.of(MOD_ID, "fcRefinedShovel"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "fcRefinedShovel");
        fcRefinedHoe = (TemplateHoe) new TemplateHoe(Identifier.of(MOD_ID, "fcRefinedHoe"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "fcRefinedHoe");
        fcRefinedAxe = (TemplateHatchet) new TemplateHatchet(Identifier.of(MOD_ID, "fcRefinedAxe"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "fcRefinedAxe");
        fcRefinedSword = (TemplateSword) new TemplateSword(Identifier.of(MOD_ID, "fcRefinedSword"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "fcRefinedSword");
        fcGroundNetherrack = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcGroundNetherrack")).setTranslationKey(MOD_ID, "fcGroundNetherrack");
        fcHellfireDust = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcHellfireDust")).setTranslationKey(MOD_ID, "fcHellfireDust");
        fcConcentratedHellfire = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcConcentratedHellfire")).setTranslationKey(MOD_ID, "fcConcentratedHellfire");
        fcArmorPlate = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcArmorPlate")).setTranslationKey(MOD_ID, "fcArmorPlate");
        fcPlateHelm = (TemplateArmour) new RefinedArmour(Identifier.of(MOD_ID, "fcPlateHelm"), 0).setTranslationKey(MOD_ID, "fcPlateHelm");
        fcPlateBreastPlate = (TemplateArmour) new RefinedArmour(Identifier.of(MOD_ID, "fcPlateBreastPlate"), 1).setTranslationKey(MOD_ID, "fcPlateBreastPlate");
        fcPlateLeggings = (TemplateArmour) new RefinedArmour(Identifier.of(MOD_ID, "fcPlateLeggings"), 2).setTranslationKey(MOD_ID, "fcPlateLeggings");
        fcPlateBoots = (TemplateArmour) new RefinedArmour(Identifier.of(MOD_ID, "fcPlateBoots"), 3).setTranslationKey(MOD_ID, "fcPlateBoots");
        fcCompositeBow = (TemplateItemBase) new CompositeBow(Identifier.of(MOD_ID, "fcCompositeBow")).setTranslationKey(MOD_ID, "fcCompositeBow");
        fcBroadheadArrowhead = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcBroadheadArrowhead")).setTranslationKey(MOD_ID, "fcBroadheadArrowhead");
        fcBroadheadArrow = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcBroadheadArrow")).setTranslationKey(MOD_ID, "fcBroadheadArrow");
        fcCoalDust = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "fcCoalDust")).setTranslationKey(MOD_ID, "fcCoalDust");
        soulFilter = new SoulFilter(Identifier.of(MOD_ID, "soul_filter"));
    }

    @EventListener
    private static void registerEntities(EntityRegister event) {
        event.register(WaterWheelEntity.class, String.valueOf(Identifier.of(MOD_ID, "WaterWheel")));
        event.register(WindMillEntity.class, String.valueOf(Identifier.of(MOD_ID, "WindMill")));
        event.register(BroadheadArrowEntity.class, String.valueOf(Identifier.of(MOD_ID, "BroadheadArrow")));
        event.register(LiftedBlockEntity.class, String.valueOf(Identifier.of(MOD_ID, "BlockLiftedByPlatform")));
        event.register(MovingPlatformEntity.class, String.valueOf(Identifier.of(MOD_ID, "MovingPlatform")));
        event.register(MovingAnchorEntity.class, String.valueOf(Identifier.of(MOD_ID, "MovingAnchor")));
    }

    @EventListener
    private static void registerMobHandlers(EntityHandlerRegistryEvent event) {
        Registry.register(event.registry, MOD_ID.id("WaterWheel") , WaterWheelEntity::new);
        Registry.register(event.registry, MOD_ID.id("WindMill") , WindMillEntity::new);
        Registry.register(event.registry, MOD_ID.id("BroadheadArrow") , BroadheadArrowEntity::new);
        Registry.register(event.registry, MOD_ID.id("BlockLiftedByPlatform") , LiftedBlockEntity::new);
        Registry.register(event.registry, MOD_ID.id("MovingPlatform") , MovingPlatformEntity::new);
        Registry.register(event.registry, MOD_ID.id("MovingAnchor") , MovingAnchorEntity::new);
    }


    @EventListener
    private static void registerTileEntities(TileEntityRegisterEvent event) {
        event.register(MillStoneTileEntity.class, String.valueOf(Identifier.of(MOD_ID, "TileMillStone")));
        event.register(CrucibleTileEntity.class, String.valueOf(Identifier.of(MOD_ID, "TileCrucible")));
        event.register(CauldronTileEntity.class, String.valueOf(Identifier.of(MOD_ID, "TileCauldron")));
        event.register(PulleyTileEntity.class, String.valueOf(Identifier.of(MOD_ID, "TilePulley")));
        event.register(TurntableTileEntity.class, String.valueOf(Identifier.of(MOD_ID, "TileTurntable")));
        event.register(HopperTileEntity.class, String.valueOf(Identifier.of(MOD_ID, "TileHooper")));
        event.register(BlockDispenserTileEntity.class, String.valueOf(Identifier.of(MOD_ID, "TileDispenser")));
        event.register(UnfiredPotteryTileEntity.class, String.valueOf(Identifier.of(MOD_ID, "TilePottery")));
        event.register(VaseTileEntity.class, String.valueOf(Identifier.of(MOD_ID, "TileVase")));
        event.register(CementTileEntity.class, String.valueOf(Identifier.of(MOD_ID, "TileCement")));
    }

    //TODO EXTRA: Saw animated, Anvil gravity, PANELS/options

    //TODO: NEW
    //TODO: custom recipes xhmi, big tree mixin, some mixin so stoked fire deals dmg


    // TODO: This should be part of the config.
    public static boolean fcFaceGearBoxAwayFromPlayer = false;

    public static final Material fcCementMaterial = new Cement(MaterialColour.STONE);

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
    public static TemplateItemBase soulFilter;
}
