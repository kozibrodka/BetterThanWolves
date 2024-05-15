package net.kozibrodka.wolves.events;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.block.entity.*;
import net.kozibrodka.wolves.gui.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.GuiHandlerRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import uk.co.benjiweber.expressions.tuple.BiTuple;

public class ScreenHandlerListener {

    @Entrypoint.Namespace
    public static final Namespace MOD_ID = Null.get();

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerScreenHandlers(GuiHandlerRegistryEvent event) {
        GuiHandlerRegistry registry = event.registry;
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openAnvil"), BiTuple.of(this::openAnvil, () -> null)); //BAD ONE
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openHopper"), BiTuple.of(this::openHopper, HopperBlockEntity::new));
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openCrucible"), BiTuple.of(this::openCrucible, CrucibleBlockEntity::new));
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openMillStone"), BiTuple.of(this::openMillStone, MillStoneBlockEntity::new));
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openCauldron"), BiTuple.of(this::openCauldron, CauldronBlockEntity::new));
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openBlockDispenser"), BiTuple.of(this::openBlockDispenser, BlockDispenserBlockEntity::new));
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openPulley"), BiTuple.of(this::openPulley, PulleyBlockEntity::new));
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
