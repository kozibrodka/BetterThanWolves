package net.kozibrodka.wolves.events;

import net.kozibrodka.wolves.block.entity.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.block.entity.BlockEntityRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

public class BlockEntityListener {

    @Entrypoint.Namespace
    public static Namespace MOD_ID;

    @EventListener
    public static void registerBlockEntities(BlockEntityRegisterEvent event) {
        event.register(MillStoneBlockEntity.class, String.valueOf(Identifier.of(MOD_ID, "TileMillStone")));
        event.register(CrucibleBlockEntity.class, String.valueOf(Identifier.of(MOD_ID, "TileCrucible")));
        event.register(CauldronBlockEntity.class, String.valueOf(Identifier.of(MOD_ID, "TileCauldron")));
        event.register(PulleyBlockEntity.class, String.valueOf(Identifier.of(MOD_ID, "TilePulley")));
        event.register(TurntableBlockEntity.class, String.valueOf(Identifier.of(MOD_ID, "TileTurntable")));
        event.register(HopperBlockEntity.class, String.valueOf(Identifier.of(MOD_ID, "TileHooper")));
        event.register(BlockDispenserBlockEntity.class, String.valueOf(Identifier.of(MOD_ID, "TileDispenser")));
        event.register(UnfiredPotteryBlockEntity.class, String.valueOf(Identifier.of(MOD_ID, "TilePottery")));
        event.register(VaseBlockEntity.class, String.valueOf(Identifier.of(MOD_ID, "TileVase")));
        event.register(CementBlockEntity.class, String.valueOf(Identifier.of(MOD_ID, "TileCement")));
    }
}
