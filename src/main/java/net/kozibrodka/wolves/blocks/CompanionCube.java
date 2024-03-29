// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCBlockCompanionCube.java

package net.kozibrodka.wolves.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.itemblocks.CompanionCubeItemBlock;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.block.HasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;

@HasCustomBlockItemFactory(CompanionCubeItemBlock.class)
public class CompanionCube extends TemplateBlock
    implements RotatableBlock, BlockWithInventoryRenderer
{

    public CompanionCube(Identifier iid)
    {
        super(iid, Material.WOOL);
        texture = 20;
    }

    protected int droppedMeta(int iMetaData)
    {
        return (iMetaData & 8) <= 0 ? 0 : 1;
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return true;
    } //ZMIANA KODU

    public Box getCollisionShape(Level world, int i, int j, int k)
    {
        if(GetHalfCubeState(world, i, j, k))
        {
            return Box.createButWasteMemory(i, j, k, (float)(i + 1), (float)j + 0.5F, (float)(k + 1));
        } else
        {
            return Box.createButWasteMemory(i, j, k, (float)(i + 1), (float)(j + 1), (float)(k + 1));
        }
    }

    public Box getOutlineShape(Level world, int i, int j, int k)
    {
        if(GetHalfCubeState(world, i, j, k))
        {
            return Box.createButWasteMemory(i, j, k, (float)(i + 1), (float)j + 0.5F, (float)(k + 1));
        } else
        {
            return Box.createButWasteMemory(i, j, k, (float)(i + 1), (float)(j + 1), (float)(k + 1));
        }
    }

    public void updateBoundingBox(BlockView iblockaccess, int i, int j, int k)
    {
        if(GetHalfCubeState(iblockaccess, i, j, k))
        {
            setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        } else
        {
            setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public int getTextureForSide(BlockView iblockaccess, int i, int j, int k, int iSide)
    {
        int iFacing = GetFacing(iblockaccess, i, j, k);
        if(GetHalfCubeState(iblockaccess, i, j, k))
        {
            if(iSide == 1)
            {
                return TextureListener.companion_meat;
            }
        } else
        if(iSide == iFacing)
        {
            return TextureListener.companion_face;
        }
        return TextureListener.companion_side;
    }

    public int getTextureForSide(int i)
    {
        if(i == 3)
        {
            return TextureListener.companion_face;
        } else
        {
            return TextureListener.companion_side;
        }
    }

    public int getTextureForSide(int iSide, int iMetaData)
    {
        if(iMetaData > 0)
        {
            if(iSide == 1)
            {
                return TextureListener.companion_meat;
            }
        } else
        if(iSide == 3)
        {
            return TextureListener.companion_face;
        }
        return TextureListener.companion_side;
    }

    public void onBlockPlaced(Level world, int i, int j, int k, int iFacing)
    {
        SetFacing(world, i, j, k, UnsortedUtils.GetOppositeFacing(iFacing));
    }

    public void afterPlaced(Level world, int i, int j, int k, Living entityliving)
    {
        if(!GetHalfCubeState(world, i, j, k))
        {
            SpawnHearts(world, i, j, k);
        }
        int iFacing = UnsortedUtils.ConvertPlacingEntityOrientationToBlockFacing(entityliving);
        SetFacing(world, i, j, k, iFacing);
    }

    public void onBlockRemoved(Level world, int i, int j, int k)
    {
        if(!GetHalfCubeState(world, i, j, k))
        {
            world.playSound((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, "mob.wolf.whine", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
        }
    }

    public void onBlockPlaced(Level world, int i, int j, int k)
    {
        int iBlockBelowID = world.getTileId(i, j - 1, k);
        if(iBlockBelowID == id)
        {
            if(GetHalfCubeState(world, i, j - 1, k))
            {
                SetHalfCubeState(world, i, j - 1, k, false);
                world.setTile(i, j, k, 0);
                SpawnHearts(world, i, j - 1, k);
            } else
            {
                super.onBlockPlaced(world, i, j, k);
            }
        } else
        {
            super.onBlockPlaced(world, i, j, k);
        }
    }


    public boolean canSuffocate(Level world, int i, int j, int k)
    {
        return !GetHalfCubeState(world, i, j, k);
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int k)
    {
        return iBlockAccess.getTileMeta(i, j, k) & -9;
    }

    public void SetFacing(Level world, int i, int j, int k, int iFacing)
    {
        int iMetaData = world.getTileMeta(i, j, k) & 8;
        iMetaData |= iFacing;
        world.setTileMeta(i, j, k, iMetaData);
        world.method_243(i, j, k);
    }

    public boolean CanRotate(BlockView iBlockAccess, int i, int j, int l)
    {
        return true;
    }

    public boolean CanTransmitRotation(BlockView iBlockAccess, int i, int j, int l)
    {
        return true;
    }

    public void Rotate(Level world, int i, int j, int k, boolean bReverse)
    {
        int iFacing = GetFacing(world, i, j, k);
        int iNewFacing = UnsortedUtils.RotateFacingAroundJ(iFacing, bReverse);
        if(iNewFacing != iFacing)
        {
            SetFacing(world, i, j, k, iNewFacing);
            world.method_202(i, j, k, i, j, k);
            if(world.rand.nextInt(12) == 0)
            {
                 world.playSound((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, "mob.wolf.whine", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                    voicePacket(world, "mob.wolf.whine", i, j, k, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                }
            }
        }
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

    public boolean GetHalfCubeState(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getTileMeta(i, j, k) & 8) > 0;
    }

    public void SetHalfCubeState(Level world, int i, int j, int k, boolean bState)
    {
        int iMetaData = world.getTileMeta(i, j, k) & -9;
        if(bState)
        {
            iMetaData |= 8;
        }
        world.setTileMeta(i, j, k, iMetaData);
        world.method_243(i, j, k);
    }

    public static void SpawnHearts(Level world, int i, int j, int k)
    {
        String s = "heart";
        for(int tempCount = 0; tempCount < 7; tempCount++)
        {
            double d = world.rand.nextGaussian() * 0.02D;
            double d1 = world.rand.nextGaussian() * 0.02D;
            double d2 = world.rand.nextGaussian() * 0.02D;
            //TODO: particle packets if needed
            world.addParticle(s, (double)i + (double)world.rand.nextFloat(), (double)(j + 1) + (double)world.rand.nextFloat(), (double)k + (double)world.rand.nextFloat(), d, d1, d2);
        }

    }

    public final int m_iCubeTextureIDFront = 21;
    public final int m_iCubeGutsTextureID = 22;

    @Override
    public void renderInventory(BlockRenderer tileRenderer, int meta) {
        byte byte0 = 0;
        if(meta > 0)
        {
            this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
            byte0 = 8;
        } else
        {
            this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, byte0);
    }
}
