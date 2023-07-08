package net.kozibrodka.wolves.events;

import net.glasslauncher.hmifabric.event.HMITabRegistryEvent;
import net.kozibrodka.wolves.tabs.MillingRecipeTab;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TabListener {
    @EventListener
    public void registerTabs(HMITabRegistryEvent event) {
        event.registry.register(Identifier.of(mod_FCBetterThanWolves.MOD_ID, "milling"), new MillingRecipeTab(mod_FCBetterThanWolves.MOD_ID), new ItemInstance(mod_FCBetterThanWolves.fcMillStone));
    }
}
