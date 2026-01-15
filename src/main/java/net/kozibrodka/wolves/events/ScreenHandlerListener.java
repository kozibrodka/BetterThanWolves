package net.kozibrodka.wolves.events;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.block.entity.*;
import net.kozibrodka.wolves.gui.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.modificationstation.stationapi.api.client.gui.screen.GuiHandler;
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;

public class ScreenHandlerListener {

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerScreenHandlers(GuiHandlerRegistryEvent event) {
        event.register(NAMESPACE.id("openAnvil"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openAnvil, () -> null));
        event.register(NAMESPACE.id("openHopper"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openHopper, HopperBlockEntity::new));
        event.register(NAMESPACE.id("openCrucible"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openCrucible, CrucibleBlockEntity::new));
        event.register(NAMESPACE.id("openMillStone"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openMillStone, MillStoneBlockEntity::new));
        event.register(NAMESPACE.id("openCauldron"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openCauldron, CauldronBlockEntity::new));
        event.register(NAMESPACE.id("openBlockDispenser"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openBlockDispenser, BlockDispenserBlockEntity::new));
        event.register(NAMESPACE.id("openPulley"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openPulley, PulleyBlockEntity::new));
        event.register(NAMESPACE.id("openDropper"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openDropper, DropperBlockEntity::new));
    }

    @Environment(EnvType.CLIENT)
    public Screen openAnvil(PlayerEntity player, Inventory inventoryBase) {
        return new AnvilScreen(player.inventory, player.world, TempAnvilX, TempAnvilY, TempAnvilZ);
    }

    public static int TempAnvilX;
    public static int TempAnvilY;
    public static int TempAnvilZ;

    public static int TempGuiX;
    public static int TempGuiY;
    public static int TempGuiZ;

    @Environment(EnvType.CLIENT)
    public Screen openHopper(PlayerEntity player, Inventory inventoryBase) {
        return new HopperScreen(player.inventory, (HopperBlockEntity) inventoryBase, TempGuiX, TempGuiY, TempGuiZ);
    }

    @Environment(EnvType.CLIENT)
    public Screen openCrucible(PlayerEntity player, Inventory inventoryBase) {
        return new CrucibleScreen(player.inventory, (CrucibleBlockEntity) inventoryBase, TempGuiX, TempGuiY, TempGuiZ);
    }

    @Environment(EnvType.CLIENT)
    public Screen openMillStone(PlayerEntity player, Inventory inventoryBase) {
        return new MillStoneScreen(player.inventory, (MillStoneBlockEntity) inventoryBase, TempGuiX, TempGuiY, TempGuiZ);
    }

    @Environment(EnvType.CLIENT)
    public Screen openCauldron(PlayerEntity player, Inventory inventoryBase) {
        return new CauldronScreen(player.inventory, (CauldronBlockEntity) inventoryBase, TempGuiX, TempGuiY, TempGuiZ);
    }

    @Environment(EnvType.CLIENT)
    public Screen openBlockDispenser(PlayerEntity player, Inventory inventoryBase) {
        return new BlockDispenserScreen(player.inventory, (BlockDispenserBlockEntity) inventoryBase, TempGuiX, TempGuiY, TempGuiZ);
    }

    @Environment(EnvType.CLIENT)
    public Screen openPulley(PlayerEntity player, Inventory inventoryBase) {
        return new PulleyScreen(player.inventory, (PulleyBlockEntity) inventoryBase, TempGuiX, TempGuiY, TempGuiZ);
    }

    @Environment(EnvType.CLIENT)
    public Screen openDropper(PlayerEntity player, Inventory inventoryBase) {
        return new DropperScreen(player.inventory, (DropperBlockEntity) inventoryBase, TempGuiX, TempGuiY, TempGuiZ);
    }
}
