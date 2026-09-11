package net.kozibrodka.wolves.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RenderPacket extends Packet implements ManagedPacket<RenderPacket> {

    public static final PacketType<RenderPacket> TYPE = PacketType.builder(true, true, RenderPacket::new).build();

    private int x;
    private int y;
    private int z;
    private int block;
    private int meta;

    public RenderPacket(int posX, int posY, int posZ, int blockId, int metaId) {
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
        switch (FabricLoader.INSTANCE.getEnvironmentType()) {
            case CLIENT -> handleClient(arg);
            case SERVER -> handleServer(arg);
        }
    }

    @Environment(EnvType.CLIENT)
    public void handleClient(NetworkHandler networkHandler) {
        /// Breaking particles + sound


        Minecraft.INSTANCE.particleManager.addBlockBreakParticles(x, y, z, block, meta);
        Block targetBlock = Block.BLOCKS[block];
        if(targetBlock == null){return;}
        Minecraft.INSTANCE.player.world.playSound((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, targetBlock.soundGroup.getSound(), (targetBlock.soundGroup.getVolume() + 1.0F) / 2.0F, targetBlock.soundGroup.getPitch() * 0.8F);

    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler networkHandler) {

    }

    @Override
    public int size() {
        return 8;
    }

    @Override
    public @NotNull PacketType<RenderPacket> getType() {
        return TYPE;
    }
}
