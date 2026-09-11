package net.kozibrodka.wolves.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.entity.WaterWheelEntity;
import net.kozibrodka.wolves.entity.WindMillEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.ClientWorld;
import net.minecraft.world.ServerWorld;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LoadRotationPacket extends Packet implements ManagedPacket<LoadRotationPacket> {

    public static final PacketType<LoadRotationPacket> TYPE = PacketType.builder(true, true, LoadRotationPacket::new).build();

    private float rotation;
    private int id;

    public LoadRotationPacket() {
    }

    public LoadRotationPacket(float rot, int code) {
        this.rotation = rot;
        this.id = code;
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.rotation = stream.readFloat();
            this.id = stream.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeFloat(this.rotation);
            stream.writeInt(this.id);
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
        Entity entity = ((ClientWorld)player.world).getEntity(id);
        if(entity instanceof WaterWheelEntity water){
            water.fRotation = this.rotation;
        }
        if(entity instanceof WindMillEntity wind){
            wind.fRotation = this.rotation;
        }
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler networkHandler) {
        ServerPlayerEntity player = (ServerPlayerEntity) PlayerHelper.getPlayerFromPacketHandler(networkHandler);
        if(player == null){
            return;
        }
        Entity entity = ((ServerWorld)player.world).getEntity(id);
        if(entity instanceof WaterWheelEntity water){
            PacketHelper.sendTo(player, new LoadRotationPacket(water.fRotation, water.id));
        }
        if(entity instanceof WindMillEntity wind){
            PacketHelper.sendTo(player, new LoadRotationPacket(wind.fRotation, wind.id));
        }
    }

    @Override
    public int size() {
        return 3;
    }

    @Override
    public @NotNull PacketType<LoadRotationPacket> getType() {
        return TYPE;
    }
}
