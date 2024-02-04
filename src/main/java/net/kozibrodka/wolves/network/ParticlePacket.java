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
    private float x;
    private float y;
    private float z;

    public ParticlePacket() {
    }

    public ParticlePacket(String particleName, float posX, float posY, float posZ) {
        this.particleToPlay = particleName;
        this.x = posX;
        this.y = posY;
        this.z = posZ;
    }

    @Override
    public void read(DataInputStream stream) {
        try {
        this.particleToPlay = readString(stream, 16);
        this.x = stream.readFloat();
        this.y = stream.readFloat();
        this.z = stream.readFloat();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
        writeString(this.particleToPlay, stream);
        stream.writeFloat(this.x);
        stream.writeFloat(this.y);
        stream.writeFloat(this.z);
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
        minecraft.level.addParticle(this.particleToPlay, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
    }

    @Environment(EnvType.SERVER)
    public void handleServer(PacketHandler networkHandler){
    }

    @Override
    public int length() {
        return 4;
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
