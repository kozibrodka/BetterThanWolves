package net.kozibrodka.wolves.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.mixin.EntityBaseAccessor;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Random;


public class Rope extends TemplateBlock implements BlockWithWorldRenderer
{

    public Rope(Identifier iid)
    {
        super(iid, Material.DOODADS);
        setHardness(0.5F);
        setSounds(GRASS_SOUNDS);
        descensionSpeed = -0.15F;
        ascensionSpeed = 0.2F;
    }

    public int getTextureForSide(int iSide)
    {
        return TextureListener.rope;
    }

    public int getDropId(int i, Random random)
    {
        return ItemListener.ropeItem.id;
    }

    public void onAdjacentBlockUpdate(Level world, int i, int j, int k, int iid)
    {
        int iBlockAboveID = world.getTileId(i, j + 1, k);
        if(iBlockAboveID != id && iBlockAboveID != BlockListener.anchor.id && iBlockAboveID != BlockListener.pulley.id)
        {
            drop(world, i, j, k, world.getTileMeta(i, j, k));
            world.setTile(i, j, k, 0);
        }
    }

    public boolean canPlaceAt(Level world, int i, int j, int k)
    {
        int iBlockAboveID = world.getTileId(i, j + 1, k);
        return iBlockAboveID == id || iBlockAboveID == BlockListener.anchor.id;
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public Box getCollisionShape(Level world, int i, int j, int k)
    {
        return Box.createButWasteMemory(((float)i + 0.5F) - 0.0625F, (float)j, ((float)k + 0.5F) - 0.0625F, (float)i + 0.5F + 0.0625F, (float)j + 1.0F, (float)k + 0.5F + 0.0625F);
    }

    public void updateBoundingBox(BlockView iBlockAccess, int i, int j, int k)
    {
        setBoundingBox(0.4375F, 0.0F, 0.4375F, 0.5625F, 1.0F, 0.5625F);
    }

    public void onEntityCollision(Level world, int i, int j, int k, EntityBase entity)
    {
        if(entity instanceof Living)
        {
            ((EntityBaseAccessor)entity).setFallDistance(0.0F);
            if(entity.velocityY < (double)descensionSpeed)
            {
                entity.velocityY = descensionSpeed;
            }
            if(entity.field_1624)
            {
                entity.velocityY = ascensionSpeed;
            }
        }
    }

    public void BreakRope(Level world, int i, int j, int k)
    {
        UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, ItemListener.ropeItem.id, 0);
        world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, sounds.getWalkSound(), (sounds.getVolume() + 1.0F) / 2.0F, sounds.getPitch() * 0.8F);
        if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
            voicePacket(world, sounds.getWalkSound(), i, j, k, (sounds.getVolume() + 1.0F) / 2.0F, sounds.getPitch() * 0.8F);
        }
        world.setTile(i, j, k, 0);
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(Level world, String name, int x, int y, int z, float g, float h){
        List list2 = world.players;
        if(list2.size() != 0) {
            for(int k = 0; k < list2.size(); k++)
            {
                ServerPlayer player1 = (ServerPlayer) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g,h));
            }
        }
    }

    public static final float fRopeWidth = 0.125F;
    public float descensionSpeed;
    public float ascensionSpeed;

    @Override
    public boolean renderWorld(BlockRenderer tileRenderer, BlockView tileView, int x, int y, int z) {
        boolean flag = CustomBlockRendering.GetOverrideBlockTexture(tileRenderer) >= 0;
        int l = this.getTextureForSide(0);
        if(!flag)
        {
            CustomBlockRendering.SetOverrideBlockTexture(tileRenderer, l);
        }
        float f = 0.0625F;
        float f1 = 0.0625F;
        float f2 = 1.0F;
        this.setBoundingBox(0.5F - f1, 0.0F, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
        tileRenderer.renderStandardBlock(this, x,y,z);
        if(!flag)
        {
            CustomBlockRendering.SetOverrideBlockTexture(tileRenderer, -1);
        }
        return true;
    }
}
