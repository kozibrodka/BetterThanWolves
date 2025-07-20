package net.kozibrodka.wolves.events;

import net.danygames2014.whatsthis.event.BlockProbeInfoProviderRegistryEvent;
import net.kozibrodka.wolves.compat.whatsthis.providers.block.WolvesProbeInfoProvider;
import net.mine_diver.unsafeevents.listener.EventListener;

public class ProbeInfoProviderListener {
    @EventListener
    public void registerBlockProbeInfoProviders(BlockProbeInfoProviderRegistryEvent event) {
        event.registerProvider(new WolvesProbeInfoProvider());
    }
}
