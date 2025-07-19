package net.kozibrodka.wolves.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SoundPacket extends Packet implements ManagedPacket<SoundPacket> {

    public static final PacketType<SoundPacket> TYPE = PacketType.builder(true, true, SoundPacket::new).build();

    private String soundToPlay;
    private int x;
    private int y;
    private int z;
    private float g;
    private float h;

    public SoundPacket() {
    }

    public SoundPacket(String soundName, int posX, int posY, int posZ, float gVal, float hVal) {
        this.soundToPlay = soundName;
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.g = gVal;
        this.h = hVal;
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.soundToPlay = readString(stream, 16);
            this.x = stream.readInt();
            this.y = stream.readInt();
            this.z = stream.readInt();
            this.g = stream.readFloat();
            this.h = stream.readFloat();
//        System.out.println("Packet Read");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void write(DataOutputStream stream) {
        //            stream.writeInt(this.soundToPlay);
        try {
            writeString(this.soundToPlay, stream);
            stream.writeInt(this.x);
            stream.writeInt(this.y);
            stream.writeInt(this.z);
            stream.writeFloat(this.g);
            stream.writeFloat(this.h);

//            System.out.println("Packet Write");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void apply(NetworkHandler arg) {
//        System.out.println("Packet Received = Sound : " + this.soundToPlay + "  " + this.x);

        switch (FabricLoader.INSTANCE.getEnvironmentType()) {
            case CLIENT -> handleClient(arg);
            case SERVER -> handleServer(arg);
        }
    }

    @Environment(EnvType.CLIENT)
    public void handleClient(NetworkHandler networkHandler) {
//        System.out.println("Handle Client");
//        minecraft.level.playSound((double)minecraft.player.x + 0.5D, (double)minecraft.player.y + 0.5D, (double)minecraft.player.z + 0.5D, this.soundToPlay, 1.0F, 2.0F);
        Minecraft.INSTANCE.world.playSound((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D, this.soundToPlay, this.g, this.h);
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler networkHandler) {
//        System.out.println("Handle Server HELLO");
//        PacketHelper.send(new SoundPacket("twuj.stary"));
    }

    @Override
    public int size() {
        return 16;
    }

    @Override
    public @NotNull PacketType<SoundPacket> getType() {
        return TYPE;
    }
}
