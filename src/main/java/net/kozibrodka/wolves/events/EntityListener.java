package net.kozibrodka.wolves.events;

import net.kozibrodka.wolves.entity.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.entity.EntityRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.EntityHandlerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

public class EntityListener {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE = Null.get();;

    @EventListener
    public static void registerEntities(EntityRegisterEvent event) {
        event.register(NAMESPACE.id("WaterWheel"), WaterWheelEntity.class);
        event.register(NAMESPACE.id("WindMill"), WindMillEntity.class);
        event.register(NAMESPACE.id("BroadheadArrow"), BroadheadArrowEntity.class);
        event.register(NAMESPACE.id("BlockLiftedByPlatform"), LiftedBlockEntity.class);
        event.register(NAMESPACE.id("MovingPlatform"), MovingPlatformEntity.class);
        event.register(NAMESPACE.id("MovingAnchor"), MovingAnchorEntity.class);
        event.register(NAMESPACE.id("FallingAnvil"), FallingAnvilEntity.class);
        event.register(NAMESPACE.id("SteelMinecart"), SteelMinecartEntity.class);
        event.register(NAMESPACE.id("StapiTEST"), FCEntityTEST.class);
        event.register(NAMESPACE.id("WaterTEST2"), FCEntityTest2.class);
    }

    @EventListener
    public static void registerEntityHandlers(EntityHandlerRegistryEvent event) {
        event.register(NAMESPACE.id("WaterWheel"), WaterWheelEntity::new);
        event.register(NAMESPACE.id("WindMill"), WindMillEntity::new);
        event.register(NAMESPACE.id("BroadheadArrow"), BroadheadArrowEntity::new);
        event.register(NAMESPACE.id("BlockLiftedByPlatform"), LiftedBlockEntity::new);
        event.register(NAMESPACE.id("MovingPlatform"), MovingPlatformEntity::new);
        event.register(NAMESPACE.id("MovingAnchor"), MovingAnchorEntity::new);
        event.register(NAMESPACE.id("FallingAnvil"), FallingAnvilEntity::new);
        event.register(NAMESPACE.id("SteelMinecart"), SteelMinecartEntity::new);
        event.register(NAMESPACE.id("StapiTEST"), FCEntityTEST::new);
        event.register(NAMESPACE.id("WaterTEST2"), FCEntityTest2::new);
    }
}
