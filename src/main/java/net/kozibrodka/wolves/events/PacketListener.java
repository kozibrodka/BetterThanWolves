package net.kozibrodka.wolves.events;
import net.kozibrodka.wolves.items.*;
import net.kozibrodka.wolves.network.GuiPacket;
import net.kozibrodka.wolves.network.SoundPacket;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.item.tool.ToolMaterialFactory;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.template.item.*;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

public class PacketListener {

    @Entrypoint.Namespace
    public static final Namespace MOD_ID = Null.get();

    @EventListener
    public void registerPacket(PacketRegisterEvent event) {
        SoundPacket.register();
        GuiPacket.register();
//        IdentifiablePacket.register(MOD_ID.id("btw_sound"), true, true, SoundPacket::new);
    }

}
