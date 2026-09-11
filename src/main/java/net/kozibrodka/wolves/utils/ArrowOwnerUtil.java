package net.kozibrodka.wolves.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.world.ClientWorld;
import net.minecraft.world.World;

import java.util.Objects;


public class ArrowOwnerUtil {

    public static boolean isIdPlayer(int code){
        return (WolvesClientUtils.minecraft.player.id == code);
    }

    public static LivingEntity getOwnerEntity(int code, World world){
        if(WolvesClientUtils.minecraft.player.id == code){
            return WolvesClientUtils.minecraft.player;
        }else{
            return (LivingEntity) ((ClientWorld)world).getEntity(code);
        }
    }

}
