package net.kozibrodka.wolves.events;

import net.kozibrodka.wolves.entity.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.entity.EntityRegister;
import net.modificationstation.stationapi.api.event.registry.EntityHandlerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Null;

public class EntityListener {

    @Entrypoint.Namespace
    public static Namespace MOD_ID;

    @EventListener
    public static void registerEntities(EntityRegister event) {
        event.register(WaterWheelEntity.class, String.valueOf(Identifier.of(MOD_ID, "WaterWheel")));
        event.register(WindMillEntity.class, String.valueOf(Identifier.of(MOD_ID, "WindMill")));
        event.register(BroadheadArrowEntity.class, String.valueOf(Identifier.of(MOD_ID, "BroadheadArrow")));
        event.register(LiftedBlockEntity.class, String.valueOf(Identifier.of(MOD_ID, "BlockLiftedByPlatform")));
        event.register(MovingPlatformEntity.class, String.valueOf(Identifier.of(MOD_ID, "MovingPlatform")));
        event.register(MovingAnchorEntity.class, String.valueOf(Identifier.of(MOD_ID, "MovingAnchor")));
        event.register(FallingAnvilEntity.class, String.valueOf(Identifier.of(MOD_ID, "FallingAnvil")));
        event.register(FCEntityTEST.class, String.valueOf(Identifier.of(MOD_ID, "StapiTEST")));
    }

    @EventListener
    public static void registerMobHandlers(EntityHandlerRegistryEvent event) {
        Registry.register(event.registry, MOD_ID.id("WaterWheel") , WaterWheelEntity::new);
        Registry.register(event.registry, MOD_ID.id("WindMill") , WindMillEntity::new);
        Registry.register(event.registry, MOD_ID.id("BroadheadArrow") , BroadheadArrowEntity::new);
        Registry.register(event.registry, MOD_ID.id("BlockLiftedByPlatform") , LiftedBlockEntity::new);
        Registry.register(event.registry, MOD_ID.id("MovingPlatform") , MovingPlatformEntity::new);
        Registry.register(event.registry, MOD_ID.id("MovingAnchor") , MovingAnchorEntity::new);
        Registry.register(event.registry, MOD_ID.id("FallingAnvil") , FallingAnvilEntity::new);
        Registry.register(event.registry, MOD_ID.id("StapiTEST") , FCEntityTEST::new);
    }
}
