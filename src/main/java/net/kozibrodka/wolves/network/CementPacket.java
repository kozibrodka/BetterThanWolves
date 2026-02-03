package net.kozibrodka.wolves.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.block.entity.CementBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CementPacket extends Packet implements ManagedPacket<CementPacket> {

    public static final PacketType<CementPacket> TYPE = PacketType.builder(true, true, CementPacket::new).build();

    private int x;
    private int y;
    private int z;
    private int dryorspread;
    private int argument1;

    public CementPacket() {
    }

    public CementPacket(int posX, int posY, int posZ, int which, int value) {
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.dryorspread = which;
        this.argument1 = value;
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.x = stream.readInt();
            this.y = stream.readInt();
            this.z = stream.readInt();
            this.dryorspread = stream.readInt();
            this.argument1 = stream.readInt();
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
            stream.writeInt(this.dryorspread);
            stream.writeInt(this.argument1);
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
        CementBlockEntity tileEntity = (CementBlockEntity) Minecraft.INSTANCE.world.getBlockEntity(this.x, this.y, this.z);
//        System.out.println("coordscemenet:" + this.x +  this.y + this.z);
        if(tileEntity != null) {
            if (this.dryorspread == 0) {
                tileEntity.dryTime = this.argument1;
            }
            if (this.dryorspread == 1) {
                tileEntity.spreadDist = this.argument1;
            }
        }

//            if (this.dryorspread == 0){
//                try {
//                    tileEntity.dryTime = this.argument1;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (this.dryorspread == 1){
//                try {
//                    tileEntity.spreadDist = this.argument1;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler networkHandler) {
    }

    @Override
    public int size() {
        return 5;
    }

    @Override
    public @NotNull PacketType<CementPacket> getType() {
        return TYPE;
    }
}
