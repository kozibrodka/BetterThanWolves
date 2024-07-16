// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCBlockCompanionCube.java

package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.itemblocks.CompanionCubeBlockItem;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.modificationstation.stationapi.api.block.HasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;

@HasCustomBlockItemFactory(CompanionCubeBlockItem.class)
public class CompanionCubeBlock extends TemplateBlock
    implements RotatableBlock, BlockWithInventoryRenderer
{

    public CompanionCubeBlock(Identifier iid)
    {
        super(iid, Material.WOOL);
        textureId = 20;
    }

    protected int getDroppedItemMeta(int iMetaData)
    {
        return (iMetaData & 8) <= 0 ? 0 : 1;
    }

    public boolean isOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return true;
    } //ZMIANA KODU

    public Box getCollisionShape(World world, int i, int j, int k)
    {
        if(GetHalfCubeState(world, i, j, k))
        {
            return Box.createCached(i, j, k, (float)(i + 1), (float)j + 0.5F, (float)(k + 1));
        } else
        {
            return Box.createCached(i, j, k, (float)(i + 1), (float)(j + 1), (float)(k + 1));
        }
    }

    public Box getBoundingBox(World world, int i, int j, int k)
    {
        if(GetHalfCubeState(world, i, j, k))
        {
            return Box.createCached(i, j, k, (float)(i + 1), (float)j + 0.5F, (float)(k + 1));
        } else
        {
            return Box.createCached(i, j, k, (float)(i + 1), (float)(j + 1), (float)(k + 1));
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

    public int getTextureId(BlockView iblockaccess, int i, int j, int k, int iSide)
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

    public int getTexture(int i)
    {
        if(i == 3)
        {
            return TextureListener.companion_face;
        } else
        {
            return TextureListener.companion_side;
        }
    }

    public int getTexture(int iSide, int iMetaData)
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

    public void onPlaced(World world, int i, int j, int k, int iFacing)
    {
        SetFacing(world, i, j, k, UnsortedUtils.getOppositeFacing(iFacing));
    }

    public void onPlaced(World world, int i, int j, int k, LivingEntity entityliving)
    {
        if(!GetHalfCubeState(world, i, j, k))
        {
            SpawnHearts(world, i, j, k);
        }
        int iFacing = UnsortedUtils.ConvertPlacingEntityOrientationToBlockFacing(entityliving);
        SetFacing(world, i, j, k, iFacing);
    }

    public void onBreak(World world, int i, int j, int k)
    {
        if(!GetHalfCubeState(world, i, j, k))
        {
            world.playSound((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, "mob.wolf.whine", 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
        }
    }

    public void onPlaced(World world, int i, int j, int k)
    {
        int iBlockBelowID = world.getBlockId(i, j - 1, k);
        if(iBlockBelowID == id)
        {
            if(GetHalfCubeState(world, i, j - 1, k))
            {
                SetHalfCubeState(world, i, j - 1, k, false);
                world.setBlock(i, j, k, 0);
                SpawnHearts(world, i, j - 1, k);
            } else
            {
                super.onPlaced(world, i, j, k);
            }
        } else
        {
            super.onPlaced(world, i, j, k);
        }
    }


    public boolean canSuffocate(World world, int i, int j, int k)
    {
        return !GetHalfCubeState(world, i, j, k);
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int k)
    {
        return iBlockAccess.getBlockMeta(i, j, k) & -9;
    }

    public void SetFacing(World world, int i, int j, int k, int iFacing)
    {
        int iMetaData = world.getBlockMeta(i, j, k) & 8;
        iMetaData |= iFacing;
        world.setBlockMeta(i, j, k, iMetaData);
        world.blockUpdateEvent(i, j, k);
    }

    public boolean CanRotate(BlockView iBlockAccess, int i, int j, int l)
    {
        return true;
    }

    public boolean CanTransmitRotation(BlockView iBlockAccess, int i, int j, int l)
    {
        return true;
    }

    public void Rotate(World world, int i, int j, int k, boolean bReverse)
    {
        int iFacing = GetFacing(world, i, j, k);
        int iNewFacing = UnsortedUtils.RotateFacingAroundJ(iFacing, bReverse);
        if(iNewFacing != iFacing)
        {
            SetFacing(world, i, j, k, iNewFacing);
            world.setBlocksDirty(i, j, k, i, j, k);
            if(world.random.nextInt(12) == 0)
            {
                 world.playSound((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, "mob.wolf.whine", 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
                if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                    voicePacket(world, "mob.wolf.whine", i, j, k, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
                }
            }
        }
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(World world, String name, int x, int y, int z, float g, float h){
        List list2 = world.players;
        if(list2.size() != 0) {
            for(int k = 0; k < list2.size(); k++)
            {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g,h));
            }
        }
    }

    public boolean GetHalfCubeState(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getBlockMeta(i, j, k) & 8) > 0;
    }

    public void SetHalfCubeState(World world, int i, int j, int k, boolean bState)
    {
        int iMetaData = world.getBlockMeta(i, j, k) & -9;
        if(bState)
        {
            iMetaData |= 8;
        }
        world.setBlockMeta(i, j, k, iMetaData);
        world.blockUpdateEvent(i, j, k);
    }

    public static void SpawnHearts(World world, int i, int j, int k)
    {
        String s = "heart";
        for(int tempCount = 0; tempCount < 7; tempCount++)
        {
            double d = world.random.nextGaussian() * 0.02D;
            double d1 = world.random.nextGaussian() * 0.02D;
            double d2 = world.random.nextGaussian() * 0.02D;
            //TODO: particle packets if needed
            world.addParticle(s, (double)i + (double)world.random.nextFloat(), (double)(j + 1) + (double)world.random.nextFloat(), (double)k + (double)world.random.nextFloat(), d, d1, d2);
        }

    }

    public final int m_iCubeTextureIDFront = 21;
    public final int m_iCubeGutsTextureID = 22;

    @Override
    public void renderInventory(BlockRenderManager tileRenderer, int meta) {
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
