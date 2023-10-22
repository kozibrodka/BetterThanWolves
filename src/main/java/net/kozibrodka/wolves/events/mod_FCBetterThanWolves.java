package net.kozibrodka.wolves.events;

import net.glasslauncher.mods.api.gcapi.api.GConfig;
import net.kozibrodka.wolves.blocks.*;
import net.kozibrodka.wolves.entity.*;
import net.kozibrodka.wolves.glasscfg.BetterThanWolvesCFG;
import net.kozibrodka.wolves.items.*;
import net.kozibrodka.wolves.items.Rope;
import net.kozibrodka.wolves.tileentity.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.block.PressurePlateTrigger;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColour;
import net.minecraft.item.tool.ToolMaterial;
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
    private static void registerEntities(EntityRegister event) {
        event.register(WaterWheelEntity.class, String.valueOf(Identifier.of(MOD_ID, "WaterWheel")));
        event.register(WindMillEntity.class, String.valueOf(Identifier.of(MOD_ID, "WindMill")));
        event.register(BroadheadArrowEntity.class, String.valueOf(Identifier.of(MOD_ID, "BroadheadArrow")));
        event.register(LiftedBlockEntity.class, String.valueOf(Identifier.of(MOD_ID, "BlockLiftedByPlatform")));
        event.register(MovingPlatformEntity.class, String.valueOf(Identifier.of(MOD_ID, "MovingPlatform")));
        event.register(MovingAnchorEntity.class, String.valueOf(Identifier.of(MOD_ID, "MovingAnchor")));
        event.register(FCEntityTEST.class, String.valueOf(Identifier.of(MOD_ID, "StapiTEST")));
    }

    @EventListener
    private static void registerMobHandlers(EntityHandlerRegistryEvent event) {
        Registry.register(event.registry, MOD_ID.id("WaterWheel") , WaterWheelEntity::new);
        Registry.register(event.registry, MOD_ID.id("WindMill") , WindMillEntity::new);
        Registry.register(event.registry, MOD_ID.id("BroadheadArrow") , BroadheadArrowEntity::new);
        Registry.register(event.registry, MOD_ID.id("BlockLiftedByPlatform") , LiftedBlockEntity::new);
        Registry.register(event.registry, MOD_ID.id("MovingPlatform") , MovingPlatformEntity::new);
        Registry.register(event.registry, MOD_ID.id("MovingAnchor") , MovingAnchorEntity::new);
        Registry.register(event.registry, MOD_ID.id("StapiTEST") , FCEntityTEST::new);
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


    public static boolean fcDisableAxeChanges = false;
    public static boolean fcFaceGearBoxAwayFromPlayer = false;
    public static boolean fcDisableMinecartChanges = false;
    public static final Material fcCementMaterial = new net.kozibrodka.wolves.materials.Cement(MaterialColour.STONE);;


//    public static TemplateBlockWithEntity fcVase_white;
//    public static TemplateBlockWithEntity fcVase_orange;
//    public static TemplateBlockWithEntity fcVase_magenta;
//    public static TemplateBlockWithEntity fcVase_light_blue;
//    public static TemplateBlockWithEntity fcVase_yellow;
//    public static TemplateBlockWithEntity fcVase_lime;
//    public static TemplateBlockWithEntity fcVase_pink;
//    public static TemplateBlockWithEntity fcVase_gray;
//    public static TemplateBlockWithEntity fcVase_light_gray;
//    public static TemplateBlockWithEntity fcVase_cyan;
//    public static TemplateBlockWithEntity fcVase_purple;
//    public static TemplateBlockWithEntity fcVase_blue;
//    public static TemplateBlockWithEntity fcVase_brown;
//    public static TemplateBlockWithEntity fcVase_green;
//    public static TemplateBlockWithEntity fcVase_red;
//    public static TemplateBlockWithEntity fcVase_black;
    // These are never registered! Why do they exist?
//    public static TemplateBlockWithEntity fcUnfiredPottery_crucible;
//    public static TemplateBlockWithEntity fcUnfiredPottery_planter;
//    public static TemplateBlockWithEntity fcUnfiredPottery_vase;
//    public static TemplateBlockBase fcPanel_wood;
//    public static TemplateBlockBase fcMoulding_wood;
//    public static TemplateBlockBase fcCorner_wood;
}
