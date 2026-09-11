package net.kozibrodka.wolves.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.network.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;

import java.util.List;

public class WolvesMpUtils {

    public static boolean isEnvServ(){
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
    }

    public static boolean isEnvClient(){
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Environment(EnvType.SERVER)
    public static void soundPacket(World world, int x, int y, int z, String name, float g, float h) {
        List list2 = world.players;
        if (list2.size() != 0) {
            for (int k = 0; k < list2.size(); k++) {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g, h));
            }
        }
    }

    @Environment(EnvType.SERVER)
    public static void renderPacket(World world, int x, int y, int z, int blockId, int metaId) {
        List list2 = world.players;
        if (list2.size() != 0) {
            for (int k = 0; k < list2.size(); k++) {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new RenderPacket(x, y, z, blockId, metaId));
            }
        }
    }

    @Environment(EnvType.SERVER)
    public static void playerSoundPacket(World world, String name, int id, float g, float h) {
        List list2 = world.players;
        if (list2.size() != 0) {
            for (int k = 0; k < list2.size(); k++) {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPlayerPacket(name, id, g, h));
            }
        }
    }

    @Environment(EnvType.SERVER)
    public static void cementPacket(World world, int x, int y, int z, int b, int arg) {
        List list2 = world.players;
        if (list2.size() != 0) {
            for (int k = 0; k < list2.size(); k++) {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new CementPacket(x, y, z, b, arg));
            }
        }
    }

    @Environment(EnvType.SERVER)
    public static void cementAlphaPacket(World world, int x, int y, int z, int argS, int argD) {
        List list2 = world.players;
        if (list2.size() != 0) {
            for (int k = 0; k < list2.size(); k++) {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new CementAlphaPacket(x, y, z, argS, argD));
            }
        }
    }
}
