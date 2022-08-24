package net.kozibrodka.wolves.events;

import net.glasslauncher.mods.api.gcapi.api.GConfig;
import net.kozibrodka.wolves.glasscfg.BetterThanWolvesCFG;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;
import net.modificationstation.stationapi.api.template.block.TemplateMaterialBlock;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;
import net.modificationstation.stationapi.api.util.Null;

public class mod_FCBetterThanWolves {

    @GConfig(value = "BetterThanWolvesCFG", visibleName = "Better Than Wolves Config")
    public static final BetterThanWolvesCFG wolvesGlass = new BetterThanWolvesCFG();

    @Entrypoint.ModID
    public static final ModID MOD_ID = Null.get();


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
    public static TemplateBlockBase fcPulley;
    public static TemplateBlockBase fcPressurePlateObsidian;
    public static TemplateBlockBase fcMoulding;
    public static TemplateBlockBase fcCorner;
    public static TemplateBlockBase fcBlockDispenser;
    public static TemplateBlockBase fcCauldron;
    public static TemplateBlockBase fcDetectorRailWood;
    public static TemplateBlockBase fcDetectorRailObsidian;
    public static TemplateBlockBase fcCompanionCube;
    public static TemplateBlockBase fcBlockDetector;
    public static TemplateBlockBase fcBlockDetectorLogic;
    public static TemplateBlockBase fcBlockLens;
    public static TemplateBlockBase fcHempCrop;
    public static TemplateBlockBase fcHandCrank;
    public static TemplateBlockBase fcMillStone;
    public static TemplateBlockBase fcAnchor;
    public static TemplateBlockBase fcRopeBlock;
    public static TemplateBlockBase fcOmniSlab;
    public static TemplateBlockBase fcAxleBlock;
    public static TemplateBlockBase fcGearBox;
    public static TemplateBlockBase fcTurntable;
    public static TemplateBlockBase fcBellows;
    public static TemplateBlockBase fcStokedFire;
    public static TemplateBlockBase fcUnfiredPottery;
    public static TemplateBlockBase fcCrucible;
    public static TemplateBlockBase fcPlanter;
    public static TemplateBlockBase fcVase;
    public static TemplateBlockBase fcBlockOfWicker; //Mango Pack Addon
    public static TemplateItemBase fcBucketCement;
    public static TemplateItemBase fcWolfRaw;
    public static TemplateItemBase fcWolfCooked;
    public static TemplateItemBase fcNethercoal;
    public static TemplateItemBase fcHempSeeds;
    public static TemplateItemBase fcHemp;
    public static TemplateItemBase fcGear;
    public static TemplateItemBase fcFlour;
    public static TemplateItemBase fcHempFibers;
    public static TemplateItemBase fcScouredLeather;
    public static TemplateItemBase fcDonut;
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
    public static TemplateItemBase fcRefinedPickAxe;
    public static TemplateItemBase fcRefinedShovel;
    public static TemplateItemBase fcRefinedHoe;
    public static TemplateItemBase fcRefinedAxe;
    public static TemplateItemBase fcRefinedSword;
    public static TemplateItemBase fcGroundNetherrack;
    public static TemplateItemBase fcHellfireDust;
    public static TemplateItemBase fcConcentratedHellfire;
    public static TemplateItemBase fcArmorPlate;
    public static TemplateItemBase fcPlateHelm;
    public static TemplateItemBase fcPlateBreastPlate;
    public static TemplateItemBase fcPlateLeggings;
    public static TemplateItemBase fcPlateBoots;
    public static TemplateItemBase fcCompositeBow;
    public static TemplateItemBase fcBroadheadArrowhead;
    public static TemplateItemBase fcBroadheadArrow;
    public static TemplateItemBase fcCoalDust;
}
