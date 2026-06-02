package net.kozibrodka.wolves.events;

import net.kozibrodka.wolves.entity.BroadheadArrowEntity;
import net.kozibrodka.wolves.entity.FCEntityTEST;
import net.kozibrodka.wolves.entity.FallingAnvilEntity;
import net.kozibrodka.wolves.entity.LiftedBlockEntity;
import net.kozibrodka.wolves.entity.MovingAnchorEntity;
import net.kozibrodka.wolves.entity.MovingPlatformEntity;
import net.kozibrodka.wolves.entity.SteelMinecartEntity;
import net.kozibrodka.wolves.entity.WaterWheelEntity;
import net.kozibrodka.wolves.entity.WindMillEntity;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.entity.EntityRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.EntityHandlerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;

public class EntityListener {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    //TODO: is entity renderdistance correct? might need testing.
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
        event.register(NAMESPACE.id("SteelMinecart"), SteelMinecartEntity::new);
        event.register(NAMESPACE.id("StapiTEST"), FCEntityTEST::new);
    }
}
