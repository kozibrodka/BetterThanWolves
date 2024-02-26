package net.kozibrodka.wolves.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.events.PacketListener;
import net.kozibrodka.wolves.mixin.ClientPlayerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketHandler;
import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParticlePacket extends AbstractPacket implements IdentifiablePacket{

    private String particleToPlay;
    private double x;
    private double y;
    private double z;
    private double a1;
    private double a2;
    private double a3;

    public ParticlePacket() {
    }

    public ParticlePacket(String particleName, double posX, double posY, double posZ, double argX, double argY, double argZ) {
        this.particleToPlay = particleName;
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.a1 = argX;
        this.a2 = argY;
        this.a3 = argZ;
    }

    @Override
    public void read(DataInputStream stream) {
        try {
        this.particleToPlay = readString(stream, 16);
        this.x = stream.readDouble();
        this.y = stream.readDouble();
        this.z = stream.readDouble();
        this.a1 = stream.readDouble();
        this.a2 = stream.readDouble();
        this.a3 = stream.readDouble();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
        writeString(this.particleToPlay, stream);
        stream.writeDouble(this.x);
        stream.writeDouble(this.y);
        stream.writeDouble(this.z);
        stream.writeDouble(this.a1);
        stream.writeDouble(this.a2);
        stream.writeDouble(this.a3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apply(PacketHandler arg) {
        switch (FabricLoader.INSTANCE.getEnvironmentType()){
            case CLIENT -> handleClient(arg);
            case SERVER -> handleServer(arg);
        }
    }

    @Environment(EnvType.CLIENT)
    public void handleClient(PacketHandler networkHandler){
        ClientPlayerAccessor accessor = (ClientPlayerAccessor) networkHandler;
        Minecraft minecraft = accessor.getMinecraft();
        minecraft.level.addParticle(this.particleToPlay, this.x, this.y, this.z, this.a1, this.a2, this.a3);
    }

    @Environment(EnvType.SERVER)
    public void handleServer(PacketHandler networkHandler){
    }

    @Override
    public int length() {
        return 6;
    }

    @Override
    public Identifier getId() {
        return identifier;
    }


    private static final Identifier identifier = PacketListener.MOD_ID.id("btw_particle");

    public static void register() {
        IdentifiablePacket.register(identifier, true, true, ParticlePacket::new);
    }

}
