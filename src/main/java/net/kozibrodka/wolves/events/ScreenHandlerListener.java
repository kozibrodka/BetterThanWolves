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
import net.modificationstation.stationapi.api.client.registry.GuiHandlerRegistry;
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Namespace;

public class ScreenHandlerListener {

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    public static int anvilX;
    public static int anvilY;
    public static int anvilZ;

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerScreenHandlers(GuiHandlerRegistryEvent event) {
        GuiHandlerRegistry registry = event.registry;

        Registry.register(registry, NAMESPACE.id("openAnvil"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openAnvil, () -> null));
        Registry.register(registry, NAMESPACE.id("openHopper"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openHopper, HopperBlockEntity::new));
        Registry.register(registry, NAMESPACE.id("openCrucible"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openCrucible, CrucibleBlockEntity::new));
        Registry.register(registry, NAMESPACE.id("openMillStone"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openMillStone, MillStoneBlockEntity::new));
        Registry.register(registry, NAMESPACE.id("openCauldron"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openCauldron, CauldronBlockEntity::new));
        Registry.register(registry, NAMESPACE.id("openBlockDispenser"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openBlockDispenser, BlockDispenserBlockEntity::new));
        Registry.register(registry, NAMESPACE.id("openPulley"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openPulley, PulleyBlockEntity::new));
        Registry.register(registry, NAMESPACE.id("openDropper"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openDropper, DropperBlockEntity::new));
        Registry.register(registry, NAMESPACE.id("openMachineBus"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openMachineBus, MachineBusBlockEntity::new));
        Registry.register(registry, NAMESPACE.id("openAutomaticAnvil"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openAutomaticAnvil, AutomaticAnvilBlockEntity::new));
    }

    @Environment(EnvType.CLIENT)
    public Screen openAnvil(PlayerEntity player, Inventory inventoryBase) {
        return new AnvilScreen(player.inventory, player.world, anvilX, anvilY, anvilZ);
    }

    @Environment(EnvType.CLIENT)
    public Screen openHopper(PlayerEntity player, Inventory inventoryBase) {
        return new HopperScreen(player.inventory, (HopperBlockEntity) inventoryBase);
    }

    @Environment(EnvType.CLIENT)
    public Screen openCrucible(PlayerEntity player, Inventory inventoryBase) {
        return new CrucibleScreen(player.inventory, (CrucibleBlockEntity) inventoryBase);
    }

    @Environment(EnvType.CLIENT)
    public Screen openMillStone(PlayerEntity player, Inventory inventoryBase) {
        return new MillStoneScreen(player.inventory, (MillStoneBlockEntity) inventoryBase);
    }

    @Environment(EnvType.CLIENT)
    public Screen openCauldron(PlayerEntity player, Inventory inventoryBase) {
        return new CauldronScreen(player.inventory, (CauldronBlockEntity) inventoryBase);
    }

    @Environment(EnvType.CLIENT)
    public Screen openBlockDispenser(PlayerEntity player, Inventory inventoryBase) {
        return new BlockDispenserScreen(player.inventory, (BlockDispenserBlockEntity) inventoryBase);
    }

    @Environment(EnvType.CLIENT)
    public Screen openPulley(PlayerEntity player, Inventory inventoryBase) {
        return new PulleyScreen(player.inventory, (PulleyBlockEntity) inventoryBase);
    }

    @Environment(EnvType.CLIENT)
    public Screen openDropper(PlayerEntity player, Inventory inventoryBase) {
        return new DropperScreen(player.inventory, (DropperBlockEntity) inventoryBase);
    }

    @Environment(EnvType.CLIENT)
    public Screen openMachineBus(PlayerEntity player, Inventory inventory) {
        return new MachineBusScreen(player.inventory, (MachineBusBlockEntity) inventory);
    }

    @Environment(EnvType.CLIENT)
    public Screen openAutomaticAnvil(PlayerEntity player, Inventory inventory) {
        return new AutomaticAnvilScreen(player.inventory, (AutomaticAnvilBlockEntity) inventory);
    }
}
