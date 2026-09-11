package net.kozibrodka.wolves.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.ClientWorld;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SoundPlayerPacket extends Packet implements ManagedPacket<SoundPlayerPacket> {

    public static final PacketType<SoundPlayerPacket> TYPE = PacketType.builder(true, true, SoundPlayerPacket::new).build();

    private String soundToPlay;
    private float volume;
    private float pitch;
    private int livingID;

    public SoundPlayerPacket() {
    }

    public SoundPlayerPacket(String soundName, int entityId, float argVol, float argPit) {
        this.soundToPlay = soundName;
        this.livingID = entityId;
        this.volume = argVol;
        this.pitch = argPit;
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.soundToPlay = stream.readUTF();
            this.volume = stream.readFloat();
            this.pitch = stream.readFloat();
            this.livingID = stream.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeUTF(this.soundToPlay);
            stream.writeFloat(this.volume);
            stream.writeFloat(this.pitch);
            stream.writeInt(this.livingID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apply(NetworkHandler arg) {
        switch (FabricLoader.INSTANCE.getEnvironmentType()) {
            case CLIENT -> handleClient(arg);
            case SERVER -> handleServer(arg);
        }
    }

    @Environment(EnvType.CLIENT)
    public void handleClient(NetworkHandler networkHandler) {
        ClientPlayerEntity player = (ClientPlayerEntity) PlayerHelper.getPlayerFromPacketHandler(networkHandler);
        if(player == null)return;
        Entity entityLiv = ((ClientWorld)player.world).getEntity(this.livingID);
        if(livingID == player.id){
            player.world.playSound(player, this.soundToPlay, this.volume, this.pitch);
            return;
        }
        if(entityLiv == null) return;
        player.world.playSound(entityLiv, this.soundToPlay, this.volume, this.pitch);
        /// ALPHA tworzy dźwięk dla każdego rónież dla Gracza od którego dźwięk pochodzi.
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler networkHandler) {
    }

    @Override
    public int size() {
        return 6;
    }

    @Override
    public @NotNull PacketType<SoundPlayerPacket> getType() {
        return TYPE;
    }
}
