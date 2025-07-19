package net.kozibrodka.wolves.events;

import net.kozibrodka.wolves.block.entity.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.block.entity.BlockEntityRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;

public class BlockEntityListener {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public static void registerBlockEntities(BlockEntityRegisterEvent event) {
        event.register(MillStoneBlockEntity.class, NAMESPACE.id("TileMillStone").toString());
        event.register(CrucibleBlockEntity.class, NAMESPACE.id("TileCrucible").toString());
        event.register(CauldronBlockEntity.class, NAMESPACE.id("TileCauldron").toString());
        event.register(PulleyBlockEntity.class, NAMESPACE.id("TilePulley").toString());
        event.register(TurntableBlockEntity.class, NAMESPACE.id("TileTurntable").toString());
        event.register(HopperBlockEntity.class, NAMESPACE.id("TileHooper").toString());
        event.register(BlockDispenserBlockEntity.class, NAMESPACE.id("TileDispenser").toString());
        event.register(UnfiredPotteryBlockEntity.class, NAMESPACE.id("TilePottery").toString());
        event.register(VaseBlockEntity.class, NAMESPACE.id("TileVase").toString());
        event.register(CementBlockEntity.class, NAMESPACE.id("TileCement").toString());
    }
}
