package net.kozibrodka.wolves.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.entity.WaterWheelEntity;
import net.kozibrodka.wolves.entity.WindMillEntity;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.ServerWorld;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class DamagePacket extends Packet implements ManagedPacket<DamagePacket> {

    public static final PacketType<DamagePacket> TYPE = PacketType.builder(true, true, DamagePacket::new).build();

    private int damage;
    private int entityId;
    private String entityName;

    public DamagePacket() {
    }

    public DamagePacket(String name, int dmg, int id) {
        this.entityName = name;
        this.damage = dmg;
        this.entityId = id;
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.entityName = stream.readUTF();
            this.damage = stream.readInt();
            this.entityId = stream.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeUTF(this.entityName);
            stream.writeInt(this.damage);
            stream.writeInt(this.entityId);
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

    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler networkHandler) {
        ServerPlayerEntity player = (ServerPlayerEntity) PlayerHelper.getPlayerFromPacketHandler(networkHandler);
        if(player == null){
            return;
        }
        if(Objects.equals(entityName, "water")){
            WaterWheelEntity entity1 = (WaterWheelEntity) ((ServerWorld)player.world).getEntity(this.entityId);
            if(entity1 != null){
                entity1.damage(player, damage);
            }
        }
        if(Objects.equals(entityName, "wind")){
            WindMillEntity entity1 = (WindMillEntity) ((ServerWorld)player.world).getEntity(this.entityId);
            if(entity1 != null){
                entity1.damage(player, damage);
            }
        }
    }

    @Override
    public int size() {
        return 3;
    }

    @Override
    public @NotNull PacketType<DamagePacket> getType() {
        return TYPE;
    }
}
