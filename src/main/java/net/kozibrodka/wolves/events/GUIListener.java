package net.kozibrodka.wolves.events;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.gui.*;
import net.kozibrodka.wolves.tileentity.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.GuiHandlerRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import uk.co.benjiweber.expressions.tuple.BiTuple;

public class GUIListener {

    @Entrypoint.Namespace
    public static final Namespace MOD_ID = Null.get();

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerGuiHandlers(GuiHandlerRegistryEvent event) {
        GuiHandlerRegistry registry = event.registry;
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openAnvil"), BiTuple.of(this::openAnvil, null)); //BAD ONE
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openHopper"), BiTuple.of(this::openHopper, HopperTileEntity::new));
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openCrucible"), BiTuple.of(this::openCrucible, CrucibleTileEntity::new));
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openMillStone"), BiTuple.of(this::openMillStone, MillStoneTileEntity::new));
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openCauldron"), BiTuple.of(this::openCauldron, CauldronTileEntity::new));
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openBlockDispenser"), BiTuple.of(this::openBlockDispenser, BlockDispenserTileEntity::new));
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openPulley"), BiTuple.of(this::openPulley, PulleyTileEntity::new));
    }

    @Environment(EnvType.CLIENT)
    public ScreenBase openAnvil(PlayerBase player, InventoryBase inventoryBase) {
        return new AnvilGUI(player.inventory, player.level, player.clientX, player.clientY, player.clientZ);
    }
    @Environment(EnvType.CLIENT)
    public ScreenBase openHopper(PlayerBase player, InventoryBase inventoryBase) {
        return new HopperGUI(player.inventory, (HopperTileEntity) inventoryBase);
    }
    @Environment(EnvType.CLIENT)
    public ScreenBase openCrucible(PlayerBase player, InventoryBase inventoryBase) {
        return new CrucibleGUI(player.inventory, (CrucibleTileEntity) inventoryBase);
    }
    @Environment(EnvType.CLIENT)
    public ScreenBase openMillStone(PlayerBase player, InventoryBase inventoryBase) {
        return new MillStoneGUI(player.inventory, (MillStoneTileEntity) inventoryBase);
    }
    @Environment(EnvType.CLIENT)
    public ScreenBase openCauldron(PlayerBase player, InventoryBase inventoryBase) {
        return new CauldronGUI(player.inventory, (CauldronTileEntity) inventoryBase);
    }
    @Environment(EnvType.CLIENT)
    public ScreenBase openBlockDispenser(PlayerBase player, InventoryBase inventoryBase) {
        return new BlockDispenserGUI(player.inventory, (BlockDispenserTileEntity) inventoryBase);
    }
    @Environment(EnvType.CLIENT)
    public ScreenBase openPulley(PlayerBase player, InventoryBase inventoryBase) {
        return new PulleyGUI(player.inventory, (PulleyTileEntity) inventoryBase);
    }
}
