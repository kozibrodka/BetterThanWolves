package net.kozibrodka.wolves.events;
import net.kozibrodka.wolves.network.ScreenPacket;
import net.kozibrodka.wolves.network.ParticlePacket;
import net.kozibrodka.wolves.network.RenderPacket;
import net.kozibrodka.wolves.network.SoundPacket;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

public class PacketListener {

    @Entrypoint.Namespace
    public static final Namespace MOD_ID = Null.get();

    @EventListener
    public void registerPacket(PacketRegisterEvent event) {
//        SoundPacket.register();
//        ScreenPacket.register();
//        RenderPacket.register();
//        ParticlePacket.register();
        Registry.register(PacketTypeRegistry.INSTANCE, PacketListener.MOD_ID.id("particle"), ParticlePacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, PacketListener.MOD_ID.id("sound"), SoundPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, PacketListener.MOD_ID.id("gui"), ScreenPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, PacketListener.MOD_ID.id("render"), RenderPacket.TYPE);
//        IdentifiablePacket.register(MOD_ID.id("btw_sound"), true, true, SoundPacket::new);
    }

}
