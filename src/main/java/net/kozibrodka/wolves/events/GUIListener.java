package net.kozibrodka.wolves.events;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.gui.*;
import net.kozibrodka.wolves.tileentity.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.GuiHandlerRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Null;
import uk.co.benjiweber.expressions.tuple.BiTuple;

public class GUIListener {

    @Entrypoint.ModID
    public static final ModID MOD_ID = Null.get();

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerGuiHandlers(GuiHandlerRegistryEvent event) {
        GuiHandlerRegistry registry = event.registry;
//        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openAnvil"), BiTuple.of(this::openAnvil, TileEntityFreezer::new));
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openHooper"), BiTuple.of(this::openHooper, FCTileEntityHopper::new));
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openCrucible"), BiTuple.of(this::openCrucible, FCTileEntityCrucible::new));
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openMillStone"), BiTuple.of(this::openMillStone, FCTileEntityMillStone::new));
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openCauldron"), BiTuple.of(this::openCauldron, FCTileEntityCauldron::new));
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openBlockDispenser"), BiTuple.of(this::openBlockDispenser, FCTileEntityBlockDispenser::new));
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openPulley"), BiTuple.of(this::openPulley, FCTileEntityPulley::new));
    }

//    @Environment(EnvType.CLIENT)
//    public ScreenBase openAnvil(PlayerBase player, InventoryBase inventoryBase) {
//        return new FCGuiCraftingAnvil(player.inventory, (TileEntityFreezer) inventoryBase);
//    }
    @Environment(EnvType.CLIENT)
    public ScreenBase openHooper(PlayerBase player, InventoryBase inventoryBase) {
        return new FCGuiHopper(player.inventory, (FCTileEntityHopper) inventoryBase);
    }
    @Environment(EnvType.CLIENT)
    public ScreenBase openCrucible(PlayerBase player, InventoryBase inventoryBase) {
        return new FCGuiCrucible(player.inventory, (FCTileEntityCrucible) inventoryBase);
    }
    @Environment(EnvType.CLIENT)
    public ScreenBase openMillStone(PlayerBase player, InventoryBase inventoryBase) {
        return new FCGuiMillStone(player.inventory, (FCTileEntityMillStone) inventoryBase);
    }
    @Environment(EnvType.CLIENT)
    public ScreenBase openCauldron(PlayerBase player, InventoryBase inventoryBase) {
        return new FCGuiCauldron(player.inventory, (FCTileEntityCauldron) inventoryBase);
    }
    @Environment(EnvType.CLIENT)
    public ScreenBase openBlockDispenser(PlayerBase player, InventoryBase inventoryBase) {
        return new FCGuiBlockDispenser(player.inventory, (FCTileEntityBlockDispenser) inventoryBase);
    }
    @Environment(EnvType.CLIENT)
    public ScreenBase openPulley(PlayerBase player, InventoryBase inventoryBase) {
        return new FCGuiPulley(player.inventory, (FCTileEntityPulley) inventoryBase);
    }
}