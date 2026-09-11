package net.kozibrodka.wolves.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.network.SoundPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;

import java.util.List;

public class WolvesClientUtils {

    public static Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();

}
