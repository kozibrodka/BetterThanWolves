package net.kozibrodka.wolves.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;


public class ArrowOwnerUtil {

    @Environment(EnvType.CLIENT)
    static Minecraft mc = (Minecraft) FabricLoader.getInstance().getGameInstance();

    public static boolean isClientPlayer(int owner){
        return owner == mc.player.id;
    }
}
