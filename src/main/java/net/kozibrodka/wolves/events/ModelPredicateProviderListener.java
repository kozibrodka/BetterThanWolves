package net.kozibrodka.wolves.events;

import net.kozibrodka.wolves.client.model.block.AxleBlockModelPredicateProvider;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.render.model.BlockModelPredicateProviderRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

public class ModelPredicateProviderListener {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public void registerBlockModelPredicateProviders(BlockModelPredicateProviderRegistryEvent event){
        /// Redundant now
        /// Using build-in "meta" predicate, because otherwise Axle being pushed by piston doesnt render correctly.
        /// custom blockModelPredicate is not needed when its dependant on meta like that.
//        event.registry.register(BlockListener.axleBlock, Identifier.of(NAMESPACE, "axle_state"), new AxleBlockModelPredicateProvider());
    }
}
