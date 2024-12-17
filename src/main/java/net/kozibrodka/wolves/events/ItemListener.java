package net.kozibrodka.wolves.events;

import net.kozibrodka.wolves.items.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.item.tool.ToolMaterialFactory;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.template.item.*;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

public class ItemListener {

    @Entrypoint.Namespace
    public static final Namespace MOD_ID = Null.get();

    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        ToolMaterialFactory.create("STEEL",3,2250,12F,8);

        bucketCement = (TemplateBucketItem) new CementBucketItem(Identifier.of(MOD_ID, "bucketCement"), BlockListener.cement.id).setTranslationKey(MOD_ID, "bucket_cement");
        wolfRaw = (TemplateFoodItem) new TemplateFoodItem(Identifier.of(MOD_ID, "wolfRaw"),3,false).setTranslationKey(MOD_ID, "wolf_raw");
        wolfCooked = (TemplateFoodItem) new TemplateFoodItem(Identifier.of(MOD_ID, "wolfCooked"),8,false).setTranslationKey(MOD_ID, "wolf_cooked");
        netherCoal = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "netherCoal")).setTranslationKey(MOD_ID, "nether_coal");
        hempSeeds = (TemplateSeedsItem) new TemplateSeedsItem(Identifier.of(MOD_ID, "hempSeeds"), BlockListener.hempCrop.id).setTranslationKey(MOD_ID, "hemp_seeds");
        hemp = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "hemp")).setTranslationKey(MOD_ID, "hemp");
        gear = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "gear")).setTranslationKey(MOD_ID, "gear");
        flour = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "flour")).setTranslationKey(MOD_ID, "flour");
        hempFibers = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "hempFibers")).setTranslationKey(MOD_ID, "hemp_fibers");
        scouredLeather = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "scouredLeather")).setTranslationKey(MOD_ID, "scoured_leather");
        donut = (TemplateFoodItem) new TemplateFoodItem(Identifier.of(MOD_ID, "donut"),1,false).setTranslationKey(MOD_ID, "donut").setMaxCount(12);
        ropeItem = (TemplateItem) new RopeItem(Identifier.of(MOD_ID, "ropeItem")).setTranslationKey(MOD_ID, "rope_item");
        rollersItem = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "rollersItem")).setTranslationKey(MOD_ID, "rollers_item");
        dung = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "dung")).setTranslationKey(MOD_ID, "dung");
        waterWheelItem = (TemplateItem) new WaterWheelItem(Identifier.of(MOD_ID, "waterWheelItem")).setTranslationKey(MOD_ID, "water_wheel_item");
        windMillBladeItem = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "windMillBladeItem")).setTranslationKey(MOD_ID, "wind_mill_blade_item").setMaxCount(1);
        windMillItem = (TemplateItem) new WindMillItem(Identifier.of(MOD_ID, "windMillItem")).setTranslationKey(MOD_ID, "wind_mill_item");
        hempCloth = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "hempCloth")).setTranslationKey(MOD_ID, "hemp_cloth");
        grate = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "grate")).setTranslationKey(MOD_ID, "grate");
        wicker = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "wicker")).setTranslationKey(MOD_ID, "wicker");
        tannedLeather = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "tannedLeather")).setTranslationKey(MOD_ID, "tanned_leather");
        strap = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "strap")).setTranslationKey(MOD_ID, "strap");
        belt = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "belt")).setTranslationKey(MOD_ID, "belt");
        foulFood = (TemplateFoodItem) new FoulFoodItem(Identifier.of(MOD_ID, "foulFood"),0,false).setTranslationKey(MOD_ID, "foul_food"); //TODO: how much healt point?
        woodBlade = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "woodBlade")).setTranslationKey(MOD_ID, "wood_blade");
        glue = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "glue")).setTranslationKey(MOD_ID, "glue");
        tallow = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "tallow")).setTranslationKey(MOD_ID, "tallow");
        haft = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "haft")).setTranslationKey(MOD_ID, "haft");
        steel = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "steel")).setTranslationKey(MOD_ID, "steel");
        refinedPickAxe = (TemplatePickaxeItem) new TemplatePickaxeItem(Identifier.of(MOD_ID, "refinedPickAxe"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "refined_pick_axe");
        refinedShovel = (TemplateShovelItem) new TemplateShovelItem(Identifier.of(MOD_ID, "refinedShovel"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "refined_shovel");
        refinedHoe = (TemplateHoeItem) new TemplateHoeItem(Identifier.of(MOD_ID, "refinedHoe"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "refined_hoe");
        refinedAxe = (TemplateAxeItem) new TemplateAxeItem(Identifier.of(MOD_ID, "refinedAxe"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "refined_axe");
        refinedSword = (TemplateSwordItem) new TemplateSwordItem(Identifier.of(MOD_ID, "refinedSword"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "refined_sword");
        groundNetherrack = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "groundNetherrack")).setTranslationKey(MOD_ID, "ground_netherrack");
        hellfireDust = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "hellfireDust")).setTranslationKey(MOD_ID, "hellfire_dust");
        concentratedHellfire = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "concentratedHellfire")).setTranslationKey(MOD_ID, "concentrated_hellfire");
        armourPlateSteel = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "armourPlateSteel")).setTranslationKey(MOD_ID, "armour_plate_steel");
        helmetSteel = (TemplateArmorItem) new RefinedArmorItem(Identifier.of(MOD_ID, "helmetSteel"), 0).setTranslationKey(MOD_ID, "helmet_steel");
        chestPlateSteel = (TemplateArmorItem) new RefinedArmorItem(Identifier.of(MOD_ID, "chestPlateSteel"), 1).setTranslationKey(MOD_ID, "chest_plate_steel");
        leggingsSteel = (TemplateArmorItem) new RefinedArmorItem(Identifier.of(MOD_ID, "leggingsSteel"), 2).setTranslationKey(MOD_ID, "leggings_steel");
        bootsSteel = (TemplateArmorItem) new RefinedArmorItem(Identifier.of(MOD_ID, "bootsSteel"), 3).setTranslationKey(MOD_ID, "boots_steel");
        compositeBow = (TemplateItem) new CompositeBowItem(Identifier.of(MOD_ID, "compositeBow")).setTranslationKey(MOD_ID, "composite_bow");
        broadHeadArrowhead = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "broadHeadArrowhead")).setTranslationKey(MOD_ID, "broad_head_arrowhead");
        broadHeadArrow = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "broadHeadArrow")).setTranslationKey(MOD_ID, "broad_head_arrow");
        coalDust = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "coalDust")).setTranslationKey(MOD_ID, "coal_dust");
        soulFilter = (TemplateItem) new SoulFilterItem(Identifier.of(MOD_ID, "soul_filter")).setTranslationKey(MOD_ID, "soul_filter");
        nothing = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "nothing")).setTranslationKey(MOD_ID, "nothing");
        debug1 = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "debug1")).setTranslationKey(MOD_ID, "debug1");
        debug2 = (TemplateItem) new TemplateItem(Identifier.of(MOD_ID, "debug2")).setTranslationKey(MOD_ID, "debug2");

        //TODO: ADDON IDEA: Refined Steel Minecarts - they would be X times faster than normal ones, great way to give more purpose for rail usage.
    }

    public static TemplateBucketItem bucketCement;
    public static TemplateFoodItem wolfRaw;
    public static TemplateFoodItem wolfCooked;
    public static TemplateItem netherCoal;
    public static TemplateSeedsItem hempSeeds;
    public static TemplateItem hemp;
    public static TemplateItem gear;
    public static TemplateItem flour;
    public static TemplateItem hempFibers;
    public static TemplateItem scouredLeather;
    public static TemplateFoodItem donut;
    public static TemplateItem ropeItem;
    public static TemplateItem rollersItem;
    public static TemplateItem dung;
    public static TemplateItem waterWheelItem;
    public static TemplateItem windMillBladeItem;
    public static TemplateItem windMillItem;
    public static TemplateItem hempCloth;
    public static TemplateItem grate;
    public static TemplateItem wicker;
    public static TemplateItem tannedLeather;
    public static TemplateItem strap;
    public static TemplateItem belt;
    public static TemplateFoodItem foulFood;
    public static TemplateItem woodBlade;
    public static TemplateItem glue;
    public static TemplateItem tallow;
    public static TemplateItem haft;
    public static TemplateItem steel;
    public static TemplatePickaxeItem refinedPickAxe;
    public static TemplateShovelItem refinedShovel;
    public static TemplateHoeItem refinedHoe;
    public static TemplateAxeItem refinedAxe;
    public static TemplateSwordItem refinedSword;
    public static TemplateItem groundNetherrack;
    public static TemplateItem hellfireDust;
    public static TemplateItem concentratedHellfire;
    public static TemplateItem armourPlateSteel;
    public static TemplateArmorItem helmetSteel;
    public static TemplateArmorItem chestPlateSteel;
    public static TemplateArmorItem leggingsSteel;
    public static TemplateArmorItem bootsSteel;
    public static TemplateItem compositeBow;
    public static TemplateItem broadHeadArrowhead;
    public static TemplateItem broadHeadArrow;
    public static TemplateItem coalDust;
    public static TemplateItem soulFilter;
    public static TemplateItem nothing;
    public static TemplateItem debug1;
    public static TemplateItem debug2;
}