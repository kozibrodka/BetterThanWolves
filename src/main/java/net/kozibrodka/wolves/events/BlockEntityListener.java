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
        event.register(NAMESPACE.id("TileMillStone").toString(), MillStoneBlockEntity.class);
        event.register(NAMESPACE.id("TileCrucible").toString(), CrucibleBlockEntity.class);
        event.register(NAMESPACE.id("TileCauldron").toString(), CauldronBlockEntity.class);
        event.register(NAMESPACE.id("TilePulley").toString(), PulleyBlockEntity.class);
        event.register(NAMESPACE.id("TileTurntable").toString(), TurntableBlockEntity.class);
        event.register(NAMESPACE.id("TileHooper").toString(), HopperBlockEntity.class);
        event.register(NAMESPACE.id("TileDispenser").toString(), BlockDispenserBlockEntity.class);
        event.register(NAMESPACE.id("TilePottery").toString(), UnfiredPotteryBlockEntity.class);
        event.register(NAMESPACE.id("TileVase").toString(), VaseBlockEntity.class);
        event.register(NAMESPACE.id("TileCement").toString(), CementBlockEntity.class);
        event.register(NAMESPACE.id("Dropper").toString(), DropperBlockEntity.class);
        event.register(NAMESPACE.id("AutomaticAnvil").toString(), AutomaticAnvilBlockEntity.class);
        event.register(NAMESPACE.id("MachineBus").toString(), MachineBusBlockEntity.class);
        event.register(NAMESPACE.id("TileLens").toString(), LensBlockEntity.class);
    }
}
