package net.kozibrodka.wolves.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.block.entity.CementBlockEntity;
import net.kozibrodka.wolves.utils.WolvesClientUtils;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CementAlphaPacket extends Packet implements ManagedPacket<CementAlphaPacket> {

    public static final PacketType<CementAlphaPacket> TYPE = PacketType.builder(true, true, CementAlphaPacket::new).build();

    private int x;
    private int y;
    private int z;
    private int spreadDist;
    private int dryTime;

    public CementAlphaPacket() {
    }

    public CementAlphaPacket(int posX, int posY, int posZ, int which, int value) {
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.spreadDist = which;
        this.dryTime = value;
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.x = stream.readInt();
            this.y = stream.readInt();
            this.z = stream.readInt();
            this.spreadDist = stream.readInt();
            this.dryTime = stream.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(this.x);
            stream.writeInt(this.y);
            stream.writeInt(this.z);
            stream.writeInt(this.spreadDist);
            stream.writeInt(this.dryTime);
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
        if (WolvesClientUtils.minecraft.world.isPosLoaded(this.x, this.y, this.z)) {
            BlockEntity var2 = WolvesClientUtils.minecraft.world.getBlockEntity(this.x, this.y, this.z);
            if (var2 instanceof CementBlockEntity tileEntity) {
                tileEntity.dryTime = this.dryTime;
                tileEntity.spreadDist = this.dryTime;
            }
        }
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler networkHandler) {
    }

    @Override
    public int size() {
        return 5;
    }

    @Override
    public @NotNull PacketType<CementAlphaPacket> getType() {
        return TYPE;
    }
}
