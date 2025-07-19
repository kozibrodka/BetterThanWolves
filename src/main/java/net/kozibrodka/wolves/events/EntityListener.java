package net.kozibrodka.wolves.events;

import net.kozibrodka.wolves.entity.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.entity.EntityRegister;
import net.modificationstation.stationapi.api.event.registry.EntityHandlerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;

public class EntityListener {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public static void registerEntities(EntityRegister event) {
        event.register(WaterWheelEntity.class, NAMESPACE.id("WaterWheel").toString());
        event.register(WindMillEntity.class, NAMESPACE.id("WindMill").toString());
        event.register(BroadheadArrowEntity.class, NAMESPACE.id("BroadheadArrow").toString());
        event.register(LiftedBlockEntity.class, NAMESPACE.id("BlockLiftedByPlatform").toString());
        event.register(MovingPlatformEntity.class, NAMESPACE.id("MovingPlatform").toString());
        event.register(MovingAnchorEntity.class, NAMESPACE.id("MovingAnchor").toString());
        event.register(FallingAnvilEntity.class, NAMESPACE.id("FallingAnvil").toString());
        event.register(FCEntityTEST.class, NAMESPACE.id("StapiTEST").toString());
    }

    @EventListener
    public static void registerMobHandlers(EntityHandlerRegistryEvent event) {
        event.register(NAMESPACE.id("WaterWheel"), WaterWheelEntity::new);
        event.register(NAMESPACE.id("WindMill"), WindMillEntity::new);
        event.register(NAMESPACE.id("BroadheadArrow"), BroadheadArrowEntity::new);
        event.register(NAMESPACE.id("BlockLiftedByPlatform"), LiftedBlockEntity::new);
        event.register(NAMESPACE.id("MovingPlatform"), MovingPlatformEntity::new);
        event.register(NAMESPACE.id("MovingAnchor"), MovingAnchorEntity::new);
        event.register(NAMESPACE.id("FallingAnvil"), FallingAnvilEntity::new);
        event.register(NAMESPACE.id("StapiTEST"), FCEntityTEST::new);
    }
}
