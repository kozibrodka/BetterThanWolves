package net.kozibrodka.wolves.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.entity.WindMillEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.world.ServerWorld;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class InteractWindPacket extends Packet implements ManagedPacket<InteractWindPacket> {

    public static final PacketType<InteractWindPacket> TYPE = PacketType.builder(true, true, InteractWindPacket::new).build();

    private int bladeNumber;
    private int colorMeta;
    private int windID;

    public InteractWindPacket(int blade, int color, int entityID) {
        this.bladeNumber = blade;
        this.colorMeta = color;
        this.windID = entityID;
    }

    public InteractWindPacket() {
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.bladeNumber = stream.readInt();
            this.colorMeta = stream.readInt();
            this.windID = stream.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(this.bladeNumber);
            stream.writeInt(this.colorMeta);
            stream.writeInt(this.windID);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
//        ServerPlayerEntity player = null;
//        if (networkHandler instanceof ServerPlayNetworkHandler serverPlayNetworkHandler) {
//            player = serverPlayNetworkHandler.player;
//        }

        ServerPlayerEntity player = (ServerPlayerEntity) PlayerHelper.getPlayerFromPacketHandler(networkHandler);
//        WindMillEntity entity1 = (WindMillEntity) ((ServerWorld)Minecraft.INSTANCE.world).getEntity(this.windID);
        if(player == null){
            return;
        }
        WindMillEntity entity1 = (WindMillEntity) ((ServerWorld)player.world).getEntity(this.windID);
        if(entity1 != null){
            ItemStack itemInstance = player.inventory.getSelectedItem();
            if(itemInstance != null){
                entity1.setBladeColor(this.bladeNumber, this.colorMeta);
                itemInstance.count--;
                if (itemInstance.count == 0) {
                    player.inventory.setStack(player.inventory.selectedSlot, null);
                }
            }
        }
    }

    @Override
    public int size() {
        return 3;
    }

    @Override
    public @NotNull PacketType<InteractWindPacket> getType() {
        return TYPE;
    }
}
