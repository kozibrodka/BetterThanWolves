package net.kozibrodka.wolves.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.events.PacketListener;
import net.kozibrodka.wolves.mixin.ClientPlayerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RenderPacket extends Packet implements ManagedPacket<RenderPacket> {

    public static final PacketType<RenderPacket> TYPE=PacketType.builder(true, true, RenderPacket::new).build();

    private int x;
    private int y;
    private int z;
    private int block;
    private int meta;

    public RenderPacket(int posX, int posY, int posZ, int blockId, int metaId){
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.block = blockId;
        this.meta = metaId;
    }

    public RenderPacket() {
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.x = stream.readInt();
            this.y = stream.readInt();
            this.z = stream.readInt();
            this.block = stream.readInt();
            this.meta = stream.readInt();
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
            stream.writeInt(this.block);
            stream.writeInt(this.meta);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler arg) {
        switch (FabricLoader.INSTANCE.getEnvironmentType()){
            case CLIENT -> handleClient(arg);
            case SERVER -> handleServer(arg);
        }
    }

    @Environment(EnvType.CLIENT) //TODO: NOT QUITE WOTKING ;(
    public void handleClient(NetworkHandler networkHandler){
        ClientPlayerAccessor accessor = (ClientPlayerAccessor) networkHandler;
        Minecraft minecraft = accessor.getMinecraft();
        minecraft.field_2808.method_322(x,y,z, block, meta);
//        Minecraft.class.cast(net.fabricmc.loader.api.FabricLoader.getInstance().getGameInstance()).particleManager.addTileBreakParticles(targetPos.i, targetPos.j, targetPos.k, iTargetid, iTargetMetaData);
//        if(block == 1){ //TURNTABLE
//            TurntableTileEntity tile = (TurntableTileEntity) minecraft.level.getTileEntity(this.x,this.y,this.z);
//            if(tile != null) {
//                tile.switchSetting = this.valueS;
//            }
//        }
//        if(block == 2){ //HOPPER
//            HopperTileEntity tile = (HopperTileEntity) minecraft.level.getTileEntity(this.x,this.y,this.z);
//            if(tile != null){
//                tile.clientFilterType = this.valueS;
//                tile.clientOccupiedSlots = this.valueF;
//            }
//        }
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler networkHandler){
//        ServerPlayerAccessor accessor = (ServerPlayerAccessor) networkHandler;
//        ServerPlayer player = accessor.getServerPlayer();
//        if(block == 1){ //TURNTABLE
//            TurntableTileEntity tile = (TurntableTileEntity) player.level.getTileEntity(this.x,this.y,this.z);
//            int a = ((TurntableTileEntity) tile).switchSetting;
//            PacketHelper.sendTo(player, new RenderPacket(1, this.x, this.y, this.z, a, 0));
//        }
//        if(block == 2){ //HOPPER
//            HopperTileEntity tile = (HopperTileEntity) player.level.getTileEntity(this.x,this.y,this.z);
//            int a = ((HopperTileEntity) tile).GetFilterType();
//            int b = InventoryHandler.getOccupiedSlotCountWithinBounds(tile, 0, 17);
//            PacketHelper.sendTo(player, new RenderPacket(2, this.x, this.y, this.z, a, b));
//        }

    }

    @Override
    public int size() {
        return 8;
    }

    private static final Identifier identifier = PacketListener.MOD_ID.id("btw_render");

    @Override
    public @NotNull PacketType<RenderPacket> getType() {
        return null;
    }
}
