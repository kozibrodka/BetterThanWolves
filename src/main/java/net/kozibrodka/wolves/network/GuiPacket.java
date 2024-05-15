package net.kozibrodka.wolves.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.blocks.Hopper;
import net.kozibrodka.wolves.blocks.Pulley;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.PacketListener;
import net.kozibrodka.wolves.mixin.ServerPlayerAccessor;
import net.kozibrodka.wolves.tileentity.BlockDispenserTileEntity;
import net.kozibrodka.wolves.tileentity.CauldronTileEntity;
import net.kozibrodka.wolves.tileentity.CrucibleTileEntity;
import net.kozibrodka.wolves.tileentity.MillStoneTileEntity;
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

public class GuiPacket extends AbstractPacket implements IdentifiablePacket {

    private String tile;
    private int count;
    private int x;
    private int y;
    private int z;

    public GuiPacket(String tileEnt, int count, int posX, int posY, int posZ){
        this.tile = tileEnt;
        this.count = count;
        this.x = posX;
        this.y = posY;
        this.z = posZ;
    }

    public GuiPacket() {
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
    public void apply(PacketHandler arg) {
        switch (FabricLoader.INSTANCE.getEnvironmentType()){
            case CLIENT -> handleClient(arg);
            case SERVER -> handleServer(arg);
        }
    }

    @Environment(EnvType.CLIENT)
    public void handleClient(PacketHandler networkHandler){
            ClientGuiData.count = this.count;
    }

    @Environment(EnvType.SERVER)
    public void handleServer(PacketHandler networkHandler){
        ServerPlayerAccessor accessor = (ServerPlayerAccessor) networkHandler;
        ServerPlayer player = accessor.getServerPlayer();
        if(Objects.equals(tile, "mill")){
            MillStoneTileEntity tile = (MillStoneTileEntity) player.level.getTileEntity(this.x,this.y,this.z);
            if(tile != null) {
                int a = ((MillStoneTileEntity) tile).iMillStoneGrindCounter;
                PacketHelper.sendTo(player, new GuiPacket("mill", a, this.x, this.y, this.z));
            }
        }
        if(Objects.equals(tile, "crucible")){
            CrucibleTileEntity tile = (CrucibleTileEntity) player.level.getTileEntity(this.x,this.y,this.z);
            if(tile != null) {
                int a = ((CrucibleTileEntity) tile).crucibleCookCounter;
                PacketHelper.sendTo(player, new GuiPacket("crucible", a, this.x, this.y, this.z));
            }
        }
        if(Objects.equals(tile, "cauldron")){
            CauldronTileEntity tile = (CauldronTileEntity) player.level.getTileEntity(this.x,this.y,this.z);
            if(tile != null) {
                int a = ((CauldronTileEntity) tile).m_iCauldronCookCounter;
                PacketHelper.sendTo(player, new GuiPacket("cauldron", a, this.x, this.y, this.z));
            }
        }
        if(Objects.equals(tile, "pulley")){
            int a = 0;
            if(((Pulley) BlockListener.pulley).IsBlockOn(player.level, this.x, this.y, this.z)){
                a = 10;
            }
                PacketHelper.sendTo(player, new GuiPacket("pulley", a, this.x, this.y, this.z));
        }
        if(Objects.equals(tile, "hopper")){
            int a = 0;
            if(((Hopper) BlockListener.hopper).IsBlockOn(player.level, this.x, this.y, this.z)){
                a = 10;
            }
            PacketHelper.sendTo(player, new GuiPacket("hopper", a, this.x, this.y, this.z));
        }
        if(Objects.equals(tile, "dispenser")){
            BlockDispenserTileEntity tile = (BlockDispenserTileEntity) player.level.getTileEntity(this.x,this.y,this.z);
            if(tile != null) {
                int a = tile.iNextSlotIndexToDispense;
                PacketHelper.sendTo(player, new GuiPacket("dispenser", a, this.x, this.y, this.z));
            }
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


    private static final Identifier identifier = PacketListener.MOD_ID.id("btw_gui");

    public static void register() {
        IdentifiablePacket.register(identifier, true, true, GuiPacket::new);
    }
}
