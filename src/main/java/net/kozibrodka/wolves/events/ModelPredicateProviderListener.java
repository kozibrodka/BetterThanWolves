package net.kozibrodka.wolves.events;

import net.kozibrodka.wolves.client.model.block.AxleBlockModelPredicateProvider;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.render.model.BlockModelPredicateProviderRegistryEvent;
import net.modificationstation.stationapi.api.client.registry.BlockModelPredicateProviderRegistry;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

public class ModelPredicateProviderListener {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public void registerBlockModelPredicateProviders(BlockModelPredicateProviderRegistryEvent event){
        event.registry.register(BlockListener.axleBlock, Identifier.of(NAMESPACE, "axle_state"), new AxleBlockModelPredicateProvider());
    }
}
