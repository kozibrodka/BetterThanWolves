package net.kozibrodka.wolves.events;

import net.glasslauncher.hmifabric.event.HMITabRegistryEvent;
import net.kozibrodka.wolves.tabs.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TabListener {
    @EventListener
    public void registerTabs(HMITabRegistryEvent event) {
        event.registry.register(Identifier.of(mod_FCBetterThanWolves.MOD_ID, "milling"), new MillingRecipeTab(mod_FCBetterThanWolves.MOD_ID), new ItemInstance(mod_FCBetterThanWolves.fcMillStone));
        event.registry.register(Identifier.of(mod_FCBetterThanWolves.MOD_ID, "turntable"), new TurntableRecipeTab(mod_FCBetterThanWolves.MOD_ID), new ItemInstance(mod_FCBetterThanWolves.fcTurntable));
        event.registry.register(Identifier.of(mod_FCBetterThanWolves.MOD_ID, "cauldron_single"), new CauldronSingleRecipeTab(mod_FCBetterThanWolves.MOD_ID), new ItemInstance(mod_FCBetterThanWolves.fcCauldron));
        event.registry.register(Identifier.of(mod_FCBetterThanWolves.MOD_ID, "cauldron_stoked_single"), new CauldronStokedSingleRecipeTab(mod_FCBetterThanWolves.MOD_ID), new ItemInstance(mod_FCBetterThanWolves.fcCauldron));
        event.registry.register(Identifier.of(mod_FCBetterThanWolves.MOD_ID, "cauldron_double"), new CauldronDoubleRecipeTab(mod_FCBetterThanWolves.MOD_ID), new ItemInstance(mod_FCBetterThanWolves.fcCauldron));
        event.registry.register(Identifier.of(mod_FCBetterThanWolves.MOD_ID, "crucible_triple"), new CrucibleTripleRecipeTab(mod_FCBetterThanWolves.MOD_ID), new ItemInstance(mod_FCBetterThanWolves.fcCrucible));
        event.registry.register(Identifier.of(mod_FCBetterThanWolves.MOD_ID, "hopper_haunting"), new HopperHauntingRecipeTab(mod_FCBetterThanWolves.MOD_ID), new ItemInstance(mod_FCBetterThanWolves.fcHopper));
        event.registry.register(Identifier.of(mod_FCBetterThanWolves.MOD_ID, "anvil"), new AnvilRecipeTab(mod_FCBetterThanWolves.MOD_ID), new ItemInstance(mod_FCBetterThanWolves.fcAnvil));
        event.registry.register(Identifier.of(mod_FCBetterThanWolves.MOD_ID, "sawing"), new SawingRecipeTab(mod_FCBetterThanWolves.MOD_ID), new ItemInstance(mod_FCBetterThanWolves.fcSaw));
    }
}
