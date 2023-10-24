package net.kozibrodka.wolves.events;

import net.kozibrodka.wolves.tileentity.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.tileentity.TileEntityRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Null;

public class TileEntityListener {

    @Entrypoint.ModID
    public static final ModID MOD_ID = Null.get();

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
}
