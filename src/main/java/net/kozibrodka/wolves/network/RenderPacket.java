package net.kozibrodka.wolves.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.blocks.Hopper;
import net.kozibrodka.wolves.blocks.Pulley;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.PacketListener;
import net.kozibrodka.wolves.mixin.ClientPlayerAccessor;
import net.kozibrodka.wolves.mixin.ServerPlayerAccessor;
import net.kozibrodka.wolves.tileentity.*;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.network.PacketHandler;
import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.util.Identifier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class RenderPacket extends AbstractPacket implements IdentifiablePacket {

    private int block;
    private int x;
    private int y;
    private int z;
    private int valueS;
    private int valueF;

    public RenderPacket(int blockId, int posX, int posY, int posZ, int s, int f){
        this.block = blockId;
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.valueS = s;
        this.valueF = f;
    }

    public RenderPacket() {
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.block = stream.readInt();
            this.x = stream.readInt();
            this.y = stream.readInt();
            this.z = stream.readInt();
            this.valueS = stream.readInt();
            this.valueF = stream.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(this.block);
            stream.writeInt(this.x);
            stream.writeInt(this.y);
            stream.writeInt(this.z);
            stream.writeInt(this.valueS);
            stream.writeInt(this.valueF);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(PacketHandler arg) {
        switch (FabricLoader.INSTANCE.getEnvironmentType()){
            case CLIENT -> handleClient(arg);
            case SERVER -> handleServer(arg);
        }
    }

    @Environment(EnvType.CLIENT) //TODO: NOT QUITE WOTKING ;(
    public void handleClient(PacketHandler networkHandler){
        ClientPlayerAccessor accessor = (ClientPlayerAccessor) networkHandler;
        Minecraft minecraft = accessor.getMinecraft();
        if(block == 1){ //TURNTABLE
            TurntableTileEntity tile = (TurntableTileEntity) minecraft.level.getTileEntity(this.x,this.y,this.z);
            if(tile != null) {
                tile.m_iSwitchSetting = this.valueS;
            }
        }
        if(block == 2){ //HOPPER
            HopperTileEntity tile = (HopperTileEntity) minecraft.level.getTileEntity(this.x,this.y,this.z);
            if(tile != null){
                tile.clientFilterType = this.valueS;
                tile.clientOccupiedSlots = this.valueF;
            }
        }
    }

    @Environment(EnvType.SERVER)
    public void handleServer(PacketHandler networkHandler){
        ServerPlayerAccessor accessor = (ServerPlayerAccessor) networkHandler;
        ServerPlayer player = accessor.getServerPlayer();
        if(block == 1){ //TURNTABLE
            TurntableTileEntity tile = (TurntableTileEntity) player.level.getTileEntity(this.x,this.y,this.z);
            int a = ((TurntableTileEntity) tile).m_iSwitchSetting;
            PacketHelper.sendTo(player, new RenderPacket(1, this.x, this.y, this.z, a, 0));
        }
        if(block == 2){ //HOPPER
            HopperTileEntity tile = (HopperTileEntity) player.level.getTileEntity(this.x,this.y,this.z);
            int a = ((HopperTileEntity) tile).GetFilterType();
            int b = InventoryHandler.getOccupiedSlotCountWithinBounds(tile, 0, 17);
            PacketHelper.sendTo(player, new RenderPacket(2, this.x, this.y, this.z, a, b));
        }

    }

    @Override
    public int length() {
        return 16;
    }

    @Override
    public Identifier getId() {
        return identifier;
    }


    private static final Identifier identifier = PacketListener.MOD_ID.id("btw_render");

    public static void register() {
        IdentifiablePacket.register(identifier, true, true, RenderPacket::new);
    }
}
