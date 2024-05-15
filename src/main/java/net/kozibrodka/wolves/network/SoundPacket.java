package net.kozibrodka.wolves.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.events.PacketListener;
import net.kozibrodka.wolves.mixin.ClientPlayerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketHandler;
import net.kozibrodka.wolves.events.ItemListener;
import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SoundPacket extends AbstractPacket implements IdentifiablePacket{

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
    public void apply(PacketHandler arg) {
//        System.out.println("Packet Received = Sound : " + this.soundToPlay + "  " + this.x);

        switch (FabricLoader.INSTANCE.getEnvironmentType()){
            case CLIENT -> handleClient(arg);
            case SERVER -> handleServer(arg);
        }
    }

    @Environment(EnvType.CLIENT)
    public void handleClient(PacketHandler networkHandler){
//        System.out.println("Handle Client");
        ClientPlayerAccessor accessor = (ClientPlayerAccessor) networkHandler;
        Minecraft minecraft = accessor.getMinecraft();
//        minecraft.level.playSound((double)minecraft.player.x + 0.5D, (double)minecraft.player.y + 0.5D, (double)minecraft.player.z + 0.5D, this.soundToPlay, 1.0F, 2.0F);
        minecraft.level.playSound((double)this.x + 0.5D, (double)this.y + 0.5D, (double)this.z + 0.5D, this.soundToPlay, this.g, this.h);
    }

    @Environment(EnvType.SERVER)
    public void handleServer(PacketHandler networkHandler){
//        System.out.println("Handle Server HELLO");
//        PacketHelper.send(new SoundPacket("twuj.stary"));
    }

    @Override
    public int length() {
        return 16;
    }

    @Override
    public Identifier getId() {
        return identifier;
    }


    private static final Identifier identifier = PacketListener.MOD_ID.id("btw_sound");

    public static void register() {
        IdentifiablePacket.register(identifier, true, true, SoundPacket::new);
    }

}
