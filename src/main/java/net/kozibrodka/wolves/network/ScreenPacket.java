package net.kozibrodka.wolves.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.block.HopperBlock;
import net.kozibrodka.wolves.block.PulleyBlock;
import net.kozibrodka.wolves.block.entity.BlockDispenserBlockEntity;
import net.kozibrodka.wolves.block.entity.CauldronBlockEntity;
import net.kozibrodka.wolves.block.entity.CrucibleBlockEntity;
import net.kozibrodka.wolves.block.entity.MillStoneBlockEntity;
import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ScreenPacket extends Packet implements ManagedPacket<ScreenPacket> {

    public static final PacketType<ScreenPacket> TYPE = PacketType.builder(true, true, ScreenPacket::new).build();

    private String tile;
    private int count;
    private int x;
    private int y;
    private int z;

    public ScreenPacket(String tileEnt, int count, int posX, int posY, int posZ) {
        this.tile = tileEnt;
        this.count = count;
        this.x = posX;
        this.y = posY;
        this.z = posZ;
    }

    public ScreenPacket() {
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.tile = readString(stream, 16);
            this.count = stream.readInt();
            this.x = stream.readInt();
            this.y = stream.readInt();
            this.z = stream.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            writeString(this.tile, stream);
            stream.writeInt(this.count);
            stream.writeInt(this.x);
            stream.writeInt(this.y);
            stream.writeInt(this.z);
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
        ClientScreenData.count = this.count;
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler networkHandler) {
        ServerPlayerEntity player = null;
        if (networkHandler instanceof ServerPlayNetworkHandler serverPlayNetworkHandler) {
            player = serverPlayNetworkHandler.player;
        }

        if (player == null) {
            System.err.println("ScreenPacket: Player is null, cannot send packet to client");
            return;
        }

        if (Objects.equals(tile, "mill")) {
            MillStoneBlockEntity tile = (MillStoneBlockEntity) player.world.getBlockEntity(this.x, this.y, this.z);
            if (tile != null) {
                int a = tile.iMillStoneGrindCounter;
                PacketHelper.sendTo(player, new ScreenPacket("mill", a, this.x, this.y, this.z));
            }
        }
        if (Objects.equals(tile, "crucible")) {
            CrucibleBlockEntity tile = (CrucibleBlockEntity) player.world.getBlockEntity(this.x, this.y, this.z);
            if (tile != null) {
                int a = tile.crucibleCookCounter;
                PacketHelper.sendTo(player, new ScreenPacket("crucible", a, this.x, this.y, this.z));
            }
        }
        if (Objects.equals(tile, "cauldron")) {
            CauldronBlockEntity tile = (CauldronBlockEntity) player.world.getBlockEntity(this.x, this.y, this.z);
            if (tile != null) {
                int a = tile.cauldronCookCounter;
                PacketHelper.sendTo(player, new ScreenPacket("cauldron", a, this.x, this.y, this.z));
            }
        }
        if (Objects.equals(tile, "pulley")) {
            int a = 0;
            if (((PulleyBlock) BlockListener.pulley).IsBlockOn(player.world, this.x, this.y, this.z)) {
                a = 10;
            }
            PacketHelper.sendTo(player, new ScreenPacket("pulley", a, this.x, this.y, this.z));
        }
        if (Objects.equals(tile, "hopper")) {
            int a = 0;
            if (((HopperBlock) BlockListener.hopper).IsBlockOn(player.world, this.x, this.y, this.z)) {
                a = 10;
            }
            PacketHelper.sendTo(player, new ScreenPacket("hopper", a, this.x, this.y, this.z));
        }
        if (Objects.equals(tile, "dispenser")) {
            BlockDispenserBlockEntity tile = (BlockDispenserBlockEntity) player.world.getBlockEntity(this.x, this.y, this.z);
            if (tile != null) {
                int a = tile.iNextSlotIndexToDispense;
                PacketHelper.sendTo(player, new ScreenPacket("dispenser", a, this.x, this.y, this.z));
            }
        }


    }

    @Override
    public int size() {
        return 16;
    }

    @Override
    public @NotNull PacketType<ScreenPacket> getType() {
        return TYPE;
    }
}
