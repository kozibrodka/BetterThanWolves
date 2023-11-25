package net.kozibrodka.wolves.events;

import net.glasslauncher.hmifabric.event.HMITabRegistryEvent;
import net.kozibrodka.wolves.tabs.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TabListener {
    @EventListener
    public void registerTabs(HMITabRegistryEvent event) {
        event.registry.register(Identifier.of(ConfigListener.MOD_ID, "milling"), new MillingRecipeTab(ConfigListener.MOD_ID), new ItemInstance(BlockListener.millStone));
        event.registry.register(Identifier.of(ConfigListener.MOD_ID, "turntable"), new TurntableRecipeTab(ConfigListener.MOD_ID), new ItemInstance(BlockListener.turntable));
        event.registry.register(Identifier.of(ConfigListener.MOD_ID, "cauldron_single"), new CauldronSingleRecipeTab(ConfigListener.MOD_ID), new ItemInstance(BlockListener.cauldron));
        event.registry.register(Identifier.of(ConfigListener.MOD_ID, "cauldron_stoked_single"), new CauldronStokedSingleRecipeTab(ConfigListener.MOD_ID), new ItemInstance(BlockListener.cauldron));
        event.registry.register(Identifier.of(ConfigListener.MOD_ID, "cauldron_double"), new CauldronDoubleRecipeTab(ConfigListener.MOD_ID), new ItemInstance(BlockListener.cauldron));
        event.registry.register(Identifier.of(ConfigListener.MOD_ID, "crucible_triple"), new CrucibleTripleRecipeTab(ConfigListener.MOD_ID), new ItemInstance(BlockListener.crucible));
        event.registry.register(Identifier.of(ConfigListener.MOD_ID, "hopper_haunting"), new HopperHauntingRecipeTab(ConfigListener.MOD_ID), new ItemInstance(BlockListener.hopper));
        event.registry.register(Identifier.of(ConfigListener.MOD_ID, "anvil"), new AnvilRecipeTab(ConfigListener.MOD_ID), new ItemInstance(BlockListener.anvil));
        event.registry.register(Identifier.of(ConfigListener.MOD_ID, "sawing"), new SawingRecipeTab(ConfigListener.MOD_ID), new ItemInstance(BlockListener.saw));
    }
}
