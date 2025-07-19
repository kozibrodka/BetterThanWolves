package net.kozibrodka.wolves.events;

import net.kozibrodka.wolves.items.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.item.tool.TagToolLevel;
import net.modificationstation.stationapi.api.item.tool.ToolLevel;
import net.modificationstation.stationapi.api.item.tool.ToolMaterialFactory;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.template.item.*;
import net.modificationstation.stationapi.api.util.Namespace;

public class ItemListener {
    public static Item bucketCement;
    public static Item wolfRaw;
    public static Item wolfCooked;
    public static Item netherCoal;
    public static Item hempSeeds;
    public static Item hemp;
    public static Item gear;
    public static Item flour;
    public static Item hempFibers;
    public static Item scouredLeather;
    public static Item donut;
    public static Item ropeItem;
    public static Item rollersItem;
    public static Item dung;
    public static Item waterWheelItem;
    public static Item windMillBladeItem;
    public static Item windMillItem;
    public static Item hempCloth;
    public static Item grate;
    public static Item wicker;
    public static Item tannedLeather;
    public static Item strap;
    public static Item belt;
    public static Item foulFood;
    public static Item woodBlade;
    public static Item glue;
    public static Item tallow;
    public static Item haft;
    public static Item steel;
    public static Item refinedPickAxe;
    public static Item refinedShovel;
    public static Item refinedHoe;
    public static Item refinedAxe;
    public static Item refinedSword;
    public static Item groundNetherrack;
    public static Item hellfireDust;
    public static Item concentratedHellfire;
    public static Item armourPlateSteel;
    public static Item helmetSteel;
    public static Item chestPlateSteel;
    public static Item leggingsSteel;
    public static Item bootsSteel;
    public static Item compositeBow;
    public static Item broadHeadArrowhead;
    public static Item broadHeadArrow;
    public static Item coalDust;
    public static Item soulFilter;
    public static Item nothing;
    public static WickerWeavingItem wickerWeaving;
    public static LazyItemTemplate wickerSheet;

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        ToolLevel soulforgedLevel = new TagToolLevel(TagKey.of(BlockRegistry.KEY, NAMESPACE.id("needs_tool_level_soulforged")));
        ToolLevel.GRAPH.putEdge(ToolMaterial.DIAMOND.getToolLevel(), soulforgedLevel);
        ToolMaterial soulforgedSteelMaterial = ToolMaterialFactory.create("soulforged_steel", 3, 2250, 12F, 8).toolLevel(soulforgedLevel);
        // This semicolon exists so my IDE stops falsely greying out the code

        bucketCement = new CementBucketItem(NAMESPACE.id("bucketCement"), BlockListener.cement.id).setTranslationKey(NAMESPACE, "bucket_cement");
        wolfRaw = new TemplateFoodItem(NAMESPACE.id("wolfRaw"), 3, false).setTranslationKey(NAMESPACE, "wolf_raw");
        wolfCooked = new TemplateFoodItem(NAMESPACE.id("wolfCooked"), 8, false).setTranslationKey(NAMESPACE, "wolf_cooked");
        netherCoal = new TemplateItem(NAMESPACE.id("netherCoal")).setTranslationKey(NAMESPACE, "nether_coal");
        hempSeeds = new TemplateSeedsItem(NAMESPACE.id("hempSeeds"), BlockListener.hempCrop.id).setTranslationKey(NAMESPACE, "hemp_seeds");
        hemp = new TemplateItem(NAMESPACE.id("hemp")).setTranslationKey(NAMESPACE, "hemp");
        gear = new TemplateItem(NAMESPACE.id("gear")).setTranslationKey(NAMESPACE, "gear");
        flour = new TemplateItem(NAMESPACE.id("flour")).setTranslationKey(NAMESPACE, "flour");
        hempFibers = new TemplateItem(NAMESPACE.id("hempFibers")).setTranslationKey(NAMESPACE, "hemp_fibers");
        scouredLeather = new TemplateItem(NAMESPACE.id("scouredLeather")).setTranslationKey(NAMESPACE, "scoured_leather");
        donut = new TemplateFoodItem(NAMESPACE.id("donut"), 1, false).setTranslationKey(NAMESPACE, "donut").setMaxCount(12);
        ropeItem = new RopeItem(NAMESPACE.id("ropeItem")).setTranslationKey(NAMESPACE, "rope_item");
        rollersItem = new TemplateItem(NAMESPACE.id("rollersItem")).setTranslationKey(NAMESPACE, "rollers_item");
        dung = new TemplateItem(NAMESPACE.id("dung")).setTranslationKey(NAMESPACE, "dung");
        waterWheelItem = new WaterWheelItem(NAMESPACE.id("waterWheelItem")).setTranslationKey(NAMESPACE, "water_wheel_item");
        windMillBladeItem = new TemplateItem(NAMESPACE.id("windMillBladeItem")).setTranslationKey(NAMESPACE, "wind_mill_blade_item").setMaxCount(1);
        windMillItem = new WindMillItem(NAMESPACE.id("windMillItem")).setTranslationKey(NAMESPACE, "wind_mill_item");
        hempCloth = new TemplateItem(NAMESPACE.id("hempCloth")).setTranslationKey(NAMESPACE, "hemp_cloth");
        grate = new TemplateItem(NAMESPACE.id("grate")).setTranslationKey(NAMESPACE, "grate");
        wicker = new TemplateItem(NAMESPACE.id("wicker")).setTranslationKey(NAMESPACE, "wicker");
        tannedLeather = new TemplateItem(NAMESPACE.id("tannedLeather")).setTranslationKey(NAMESPACE, "tanned_leather");
        strap = new TemplateItem(NAMESPACE.id("strap")).setTranslationKey(NAMESPACE, "strap");
        belt = new TemplateItem(NAMESPACE.id("belt")).setTranslationKey(NAMESPACE, "belt");
        foulFood = new FoulFoodItem(NAMESPACE.id("foulFood")).setTranslationKey(NAMESPACE, "foul_food");
        woodBlade = new TemplateItem(NAMESPACE.id("woodBlade")).setTranslationKey(NAMESPACE, "wood_blade");
        glue = new TemplateItem(NAMESPACE.id("glue")).setTranslationKey(NAMESPACE, "glue");
        tallow = new TemplateItem(NAMESPACE.id("tallow")).setTranslationKey(NAMESPACE, "tallow");
        haft = new TemplateItem(NAMESPACE.id("haft")).setTranslationKey(NAMESPACE, "haft");
        steel = new TemplateItem(NAMESPACE.id("steel")).setTranslationKey(NAMESPACE, "steel");
        refinedPickAxe = new TemplatePickaxeItem(NAMESPACE.id("refinedPickAxe"), soulforgedSteelMaterial).setTranslationKey(NAMESPACE, "refined_pick_axe");
        refinedShovel = new TemplateShovelItem(NAMESPACE.id("refinedShovel"), soulforgedSteelMaterial).setTranslationKey(NAMESPACE, "refined_shovel");
        refinedHoe = new TemplateHoeItem(NAMESPACE.id("refinedHoe"), soulforgedSteelMaterial).setTranslationKey(NAMESPACE, "refined_hoe");
        refinedAxe = new TemplateAxeItem(NAMESPACE.id("refinedAxe"), soulforgedSteelMaterial).setTranslationKey(NAMESPACE, "refined_axe");
        refinedSword = new TemplateSwordItem(NAMESPACE.id("refinedSword"), soulforgedSteelMaterial).setTranslationKey(NAMESPACE, "refined_sword");
        groundNetherrack = new TemplateItem(NAMESPACE.id("groundNetherrack")).setTranslationKey(NAMESPACE, "ground_netherrack");
        hellfireDust = new TemplateItem(NAMESPACE.id("hellfireDust")).setTranslationKey(NAMESPACE, "hellfire_dust");
        concentratedHellfire = new TemplateItem(NAMESPACE.id("concentratedHellfire")).setTranslationKey(NAMESPACE, "concentrated_hellfire");
        armourPlateSteel = new TemplateItem(NAMESPACE.id("armourPlateSteel")).setTranslationKey(NAMESPACE, "armour_plate_steel");
        helmetSteel = new RefinedArmorItem(NAMESPACE.id("helmetSteel"), 0).setTranslationKey(NAMESPACE, "helmet_steel");
        chestPlateSteel = new RefinedArmorItem(NAMESPACE.id("chestPlateSteel"), 1).setTranslationKey(NAMESPACE, "chest_plate_steel");
        leggingsSteel = new RefinedArmorItem(NAMESPACE.id("leggingsSteel"), 2).setTranslationKey(NAMESPACE, "leggings_steel");
        bootsSteel = new RefinedArmorItem(NAMESPACE.id("bootsSteel"), 3).setTranslationKey(NAMESPACE, "boots_steel");
        compositeBow = new CompositeBowItem(NAMESPACE.id("compositeBow")).setTranslationKey(NAMESPACE, "composite_bow");
        broadHeadArrowhead = new TemplateItem(NAMESPACE.id("broadHeadArrowhead")).setTranslationKey(NAMESPACE, "broad_head_arrowhead");
        broadHeadArrow = new TemplateItem(NAMESPACE.id("broadHeadArrow")).setTranslationKey(NAMESPACE, "broad_head_arrow");
        coalDust = new TemplateItem(NAMESPACE.id("coalDust")).setTranslationKey(NAMESPACE, "coal_dust");
        soulFilter = new SoulFilterItem(NAMESPACE.id("soul_filter")).setTranslationKey(NAMESPACE, "soul_filter");
        nothing = new TemplateItem(NAMESPACE.id("nothing")).setTranslationKey(NAMESPACE, "nothing");
        wickerWeaving = new WickerWeavingItem(NAMESPACE.id("wicker_weaving"));
        wickerSheet = new LazyItemTemplate(NAMESPACE.id("wicker_sheet"));

        //TODO: ADDON IDEA: Refined Steel Minecarts - they would be X times faster than normal ones, great way to give more purpose for rail usage.
    }
}