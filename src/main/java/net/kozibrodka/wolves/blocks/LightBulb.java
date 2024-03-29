
package net.kozibrodka.wolves.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class LightBulb extends TemplateBlock
    implements RotatableBlock
{

    public LightBulb(Identifier iid)
    {
        super(iid, 0, Material.GLASS);
        setHardness(0.4F);
        setSounds(GLASS_SOUNDS);
    }

    public int getTextureForSide(int i)
    {
        return id != BlockListener.lightBulbOn.id ? TextureListener.bulb_off : TextureListener.bulb_on;
    }

    public int getTickrate()
    {
        return 2;
    }

    public void onBlockPlaced(Level world, int i, int j, int k)
    {
        world.method_216(i, j, k, id, getTickrate());
    }

    public int getDropId(int i, Random random)
    {
        return BlockListener.lightBulbOff.id;
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public float getBrightness(BlockView iBlockAccess, int i, int j, int k)
    {
    	if(id == BlockListener.lightBulbOn.id)
        {
            return 100F;
        } else
        {
            return iBlockAccess.getBrightness(i, j, k);
        }
    }

    public void onAdjacentBlockUpdate(Level world, int i, int j, int k, int l)
    {
        world.method_216(i, j, k, id, getTickrate());
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
        boolean bPowered = world.hasRedstonePower(i, j, k);
        if(bPowered)
        {
            if(!IsLightOn(world, i, j, k))
            {
                LightBulbTurnOn(world, i, j, k);
                return;
            }
        } else
        if(IsLightOn(world, i, j, k))
        {
            LightBulbTurnOff(world, i, j, k);
            return;
        }
    }

    public boolean isPowered(BlockView iBlockAccess, int i, int j, int k, int l) //isPoweringTo
    {
//        return ((Minecraft) net.fabricmc.loader.FabricLoader.INSTANCE.getGameInstance()).level.method_263(i, j, k);

//        return ((MinecraftServer) FabricLoader.getInstance().getGameInstance()).getLevel(0).method_263(i, j, k);
//        FabricLoader.getInstance().getGameInstance();
//        return iBlockAccess.getTileId(i, j, k) == BlockListener.lightBulbOn.id;
        switch (FabricLoader.INSTANCE.getEnvironmentType()){
            case CLIENT -> {
                return powerClient(iBlockAccess, i, j, k, l);
            }
            case SERVER -> {
                return powerServer(iBlockAccess, i, j, k, l);
            }
        }
        return false;
    }

    @Environment(EnvType.CLIENT)
    public boolean powerClient(BlockView iBlockAccess, int i, int j, int k, int l){
        return ((Minecraft) FabricLoader.INSTANCE.getGameInstance()).level.method_263(i, j, k);
    }

    @Environment(EnvType.SERVER)
    public boolean powerServer(BlockView iBlockAccess, int i, int j, int k, int l){
        return ((MinecraftServer) net.fabricmc.loader.api.FabricLoader.getInstance().getGameInstance()).getLevel(0).method_263(i, j, k);
        //TODO: It gets the overworld always.
    }


    public int GetFacing(BlockView iBlockAccess, int i, int j, int l)
    {
        return 0;
    }

    public void SetFacing(Level world1, int l, int i1, int j1, int k1)
    {
    }

    public boolean CanRotate(BlockView iBlockAccess, int i, int j, int l)
    {
        return false;
    }

    public boolean CanTransmitRotation(BlockView iBlockAccess, int i, int j, int l)
    {
        return true;
    }

    public void Rotate(Level world1, int l, int i1, int j1, boolean flag)
    {
    }

    private void LightBulbTurnOn(Level world, int i, int j, int k)
    {
        world.setTile(i, j, k, BlockListener.lightBulbOn.id);
    }

    private void LightBulbTurnOff(Level world, int i, int j, int k)
    {
        world.setTile(i, j, k, BlockListener.lightBulbOff.id);
    }

    public boolean IsLightOn(Level world, int i, int j, int k)
    {
        return world.getTileId(i, j, k) == BlockListener.lightBulbOn.id;
    }

}
