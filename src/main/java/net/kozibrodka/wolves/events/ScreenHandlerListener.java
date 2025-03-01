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
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

public class ScreenHandlerListener {

    @Entrypoint.Namespace
    public static Namespace MOD_ID;

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerScreenHandlers(GuiHandlerRegistryEvent event) {
        GuiHandlerRegistry registry = event.registry;
        Registry.register(registry, Identifier.of(MOD_ID, "openAnvil"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openAnvil, () -> null));
        Registry.register(registry, Identifier.of(MOD_ID, "openHopper"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openHopper, HopperBlockEntity::new));
        Registry.register(registry, Identifier.of(MOD_ID, "openCrucible"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openCrucible, CrucibleBlockEntity::new));
        Registry.register(registry, Identifier.of(MOD_ID, "openMillStone"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openMillStone, MillStoneBlockEntity::new));
        Registry.register(registry, Identifier.of(MOD_ID, "openCauldron"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openCauldron, CauldronBlockEntity::new));
        Registry.register(registry, Identifier.of(MOD_ID, "openBlockDispenser"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openBlockDispenser, BlockDispenserBlockEntity::new));
        Registry.register(registry, Identifier.of(MOD_ID, "openPulley"), new GuiHandler((GuiHandler.ScreenFactoryNoMessage) this::openPulley, PulleyBlockEntity::new));
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
}
