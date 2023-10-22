package net.kozibrodka.wolves.events;

import net.kozibrodka.wolves.items.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.item.tool.ToolMaterialFactory;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;
import net.modificationstation.stationapi.api.template.item.TemplateSeeds;
import net.modificationstation.stationapi.api.template.item.armour.TemplateArmour;
import net.modificationstation.stationapi.api.template.item.food.TemplateFoodBase;
import net.modificationstation.stationapi.api.template.item.tool.*;
import net.modificationstation.stationapi.api.util.Null;

public class ItemListener {

    @Entrypoint.ModID
    public static final ModID MOD_ID = Null.get();

    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        ToolMaterialFactory.create("STEEL",3,2250,12F,8);

        bucketCement = (TemplateBucket) new BucketCement(Identifier.of(MOD_ID, "bucketCement"), BlockListener.cement.id).setTranslationKey(MOD_ID, "bucket_cement");
        wolfRaw = (TemplateFoodBase) new TemplateFoodBase(Identifier.of(MOD_ID, "wolfRaw"),3,false).setTranslationKey(MOD_ID, "wolf_raw");
        wolfCooked = (TemplateFoodBase) new TemplateFoodBase(Identifier.of(MOD_ID, "wolfCooked"),8,false).setTranslationKey(MOD_ID, "wolf_cooked");
        netherCoal = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "netherCoal")).setTranslationKey(MOD_ID, "nether_coal");
        hempSeeds = (TemplateSeeds) new TemplateSeeds(Identifier.of(MOD_ID, "hempSeeds"), BlockListener.hempCrop.id).setTranslationKey(MOD_ID, "hemp_seeds");
        hemp = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "hemp")).setTranslationKey(MOD_ID, "hemp");
        gear = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "gear")).setTranslationKey(MOD_ID, "gear");
        flour = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "flour")).setTranslationKey(MOD_ID, "flour");
        hempFibers = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "hempFibers")).setTranslationKey(MOD_ID, "hemp_fibers");
        scouredLeather = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "scouredLeather")).setTranslationKey(MOD_ID, "scoured_leather");
        donut = (TemplateFoodBase) new TemplateFoodBase(Identifier.of(MOD_ID, "donut"),1,false).setTranslationKey(MOD_ID, "donut").setMaxStackSize(12);
        ropeItem = (TemplateItemBase) new Rope(Identifier.of(MOD_ID, "ropeItem")).setTranslationKey(MOD_ID, "rope_item");
        rollersItem = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "rollersItem")).setTranslationKey(MOD_ID, "rollers_item");
        dung = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "dung")).setTranslationKey(MOD_ID, "dung");
        waterWheelItem = (TemplateItemBase) new WaterWheel(Identifier.of(MOD_ID, "waterWheelItem")).setTranslationKey(MOD_ID, "water_wheel_item");
        windMillBladeItem = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "windMillBladeItem")).setTranslationKey(MOD_ID, "wind_mill_blade_item").setMaxStackSize(1);
        windMillItem = (TemplateItemBase) new WindMill(Identifier.of(MOD_ID, "windMillItem")).setTranslationKey(MOD_ID, "wind_mill_item");
        hempCloth = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "hempCloth")).setTranslationKey(MOD_ID, "hemp_cloth");
        grate = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "grate")).setTranslationKey(MOD_ID, "grate");
        wicker = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "wicker")).setTranslationKey(MOD_ID, "wicker");
        tannedLeather = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "tannedLeather")).setTranslationKey(MOD_ID, "tanned_leather");
        strap = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "strap")).setTranslationKey(MOD_ID, "strap");
        belt = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "belt")).setTranslationKey(MOD_ID, "belt");
        foulFood = (TemplateItemBase) new FoulFood(Identifier.of(MOD_ID, "foulFood")).setTranslationKey(MOD_ID, "foul_food");
        woodBlade = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "woodBlade")).setTranslationKey(MOD_ID, "wood_blade");
        glue = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "glue")).setTranslationKey(MOD_ID, "glue");
        tallow = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "tallow")).setTranslationKey(MOD_ID, "tallow");
        haft = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "haft")).setTranslationKey(MOD_ID, "haft");
        steel = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "steel")).setTranslationKey(MOD_ID, "steel");
        refinedPickAxe = (TemplatePickaxe) new TemplatePickaxe(Identifier.of(MOD_ID, "refinedPickAxe"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "refined_pick_axe");
        refinedShovel = (TemplateShovel) new TemplateShovel(Identifier.of(MOD_ID, "refinedShovel"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "refined_shovel");
        refinedHoe = (TemplateHoe) new TemplateHoe(Identifier.of(MOD_ID, "refinedHoe"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "refined_hoe");
        refinedAxe = (TemplateHatchet) new TemplateHatchet(Identifier.of(MOD_ID, "refinedAxe"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "refined_axe");
        refinedSword = (TemplateSword) new TemplateSword(Identifier.of(MOD_ID, "refinedSword"), ToolMaterial.valueOf("STEEL")).setTranslationKey(MOD_ID, "refined_sword");
        groundNetherrack = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "groundNetherrack")).setTranslationKey(MOD_ID, "ground_netherrack");
        hellfireDust = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "hellfireDust")).setTranslationKey(MOD_ID, "hellfireDust");
        concentratedHellfire = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "concentratedHellfire")).setTranslationKey(MOD_ID, "concentrated_hellfire");
        armourPlateSteel = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "armourPlateSteel")).setTranslationKey(MOD_ID, "armour_plate_steel");
        helmetSteel = (TemplateArmour) new RefinedArmour(Identifier.of(MOD_ID, "helmetSteel"), 0).setTranslationKey(MOD_ID, "helmet_steel");
        chestPlateSteel = (TemplateArmour) new RefinedArmour(Identifier.of(MOD_ID, "chestPlateSteel"), 1).setTranslationKey(MOD_ID, "chest_plate_steel");
        leggingsSteel = (TemplateArmour) new RefinedArmour(Identifier.of(MOD_ID, "leggingsSteel"), 2).setTranslationKey(MOD_ID, "leggings_steel");
        bootsSteel = (TemplateArmour) new RefinedArmour(Identifier.of(MOD_ID, "bootsSteel"), 3).setTranslationKey(MOD_ID, "boots_steel");
        compositeBow = (TemplateItemBase) new CompositeBow(Identifier.of(MOD_ID, "compositeBow")).setTranslationKey(MOD_ID, "composite_bow");
        broadHeadArrowhead = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "broadHeadArrowhead")).setTranslationKey(MOD_ID, "broad_head_arrowhead");
        broadHeadArrow = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "broadHeadArrow")).setTranslationKey(MOD_ID, "broad_head_arrow");
        coalDust = (TemplateItemBase) new TemplateItemBase(Identifier.of(MOD_ID, "coalDust")).setTranslationKey(MOD_ID, "coal_dust");
        soulFilter = new SoulFilter(Identifier.of(MOD_ID, "soul_filter"));
    }

    public static TemplateBucket bucketCement;
    public static TemplateFoodBase wolfRaw;
    public static TemplateFoodBase wolfCooked;
    public static TemplateItemBase netherCoal;
    public static TemplateSeeds hempSeeds;
    public static TemplateItemBase hemp;
    public static TemplateItemBase gear;
    public static TemplateItemBase flour;
    public static TemplateItemBase hempFibers;
    public static TemplateItemBase scouredLeather;
    public static TemplateFoodBase donut;
    public static TemplateItemBase ropeItem;
    public static TemplateItemBase rollersItem;
    public static TemplateItemBase dung;
    public static TemplateItemBase waterWheelItem;
    public static TemplateItemBase windMillBladeItem;
    public static TemplateItemBase windMillItem;
    public static TemplateItemBase hempCloth;
    public static TemplateItemBase grate;
    public static TemplateItemBase wicker;
    public static TemplateItemBase tannedLeather;
    public static TemplateItemBase strap;
    public static TemplateItemBase belt;
    public static TemplateItemBase foulFood;
    public static TemplateItemBase woodBlade;
    public static TemplateItemBase glue;
    public static TemplateItemBase tallow;
    public static TemplateItemBase haft;
    public static TemplateItemBase steel;
    public static TemplatePickaxe refinedPickAxe;
    public static TemplateShovel refinedShovel;
    public static TemplateHoe refinedHoe;
    public static TemplateHatchet refinedAxe;
    public static TemplateSword refinedSword;
    public static TemplateItemBase groundNetherrack;
    public static TemplateItemBase hellfireDust;
    public static TemplateItemBase concentratedHellfire;
    public static TemplateItemBase armourPlateSteel;
    public static TemplateArmour helmetSteel;
    public static TemplateArmour chestPlateSteel;
    public static TemplateArmour leggingsSteel;
    public static TemplateArmour bootsSteel;
    public static TemplateItemBase compositeBow;
    public static TemplateItemBase broadHeadArrowhead;
    public static TemplateItemBase broadHeadArrow;
    public static TemplateItemBase coalDust;
    public static TemplateItemBase soulFilter;
}