package net.kozibrodka.wolves.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.block.entity.CementBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlatformPacket extends Packet implements ManagedPacket<PlatformPacket> {

    public static final PacketType<PlatformPacket> TYPE = PacketType.builder(true, true, PlatformPacket::new).build();

    private double y;
    private int action;

    public PlatformPacket() {
    }

    public PlatformPacket(double posY, int code) {
        this.y = posY;
        this.action = code;
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.y = stream.readDouble();
            this.action = stream.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeDouble(this.y);
            stream.writeInt(this.action);
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
        if(player == null){
            return;
        }

        if(action == 1) {
            double a = 0.2D; // 0.2D
            double b = 2.8D; // 2.8D
            if((player.y - y) > 2.5D){
//                player.setPosition(player.x, player.y + a, player.z);
                player.setPosition(player.x, this.y + b, player.z); // jak serwer dopiero startuje moze gracz wpaść
            }
        }

        if(action == 2){
            double e = 0.1D;
            player.setPosition(player.x, player.y + e, player.z);
        }
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler networkHandler) {
    }

    @Override
    public int size() {
        return 3;
    }

    @Override
    public @NotNull PacketType<PlatformPacket> getType() {
        return TYPE;
    }
}
