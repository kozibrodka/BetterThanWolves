// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCBlockAnvil.java

package net.kozibrodka.wolves.blocks;

import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.gui.FCGuiCraftingAnvil;
import net.kozibrodka.wolves.gui.FCGuiMillStone;
import net.kozibrodka.wolves.mixin.LevelAccessor;
import net.kozibrodka.wolves.utils.FCIBlock;
import net.kozibrodka.wolves.utils.FCUtilsMisc;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

public class FCBlockAnvil extends TemplateBlockBase
    implements FCIBlock
{

    public FCBlockAnvil(Identifier iid)
    {
        super(iid, Material.METAL);
        texture = 79;
        setHardness(3.5F);
        setSounds(METAL_SOUNDS);
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public void onBlockPlaced(Level world, int i, int j, int k, int iFacing)
    {
        if(iFacing < 2)
        {
            iFacing = 2;
        } else
        {
            iFacing = FCUtilsMisc.GetOppositeFacing(iFacing);
        }
        SetFacing(world, i, j, k, iFacing);
    }

    public void afterPlaced(Level world, int i, int j, int k, Living entityLiving)
    {
        int iFacing = FCUtilsMisc.ConvertPlacingEntityOrientationToFlatBlockFacing(entityLiving);
        SetFacing(world, i, j, k, iFacing);
    }

    public boolean canUse(Level world, int i, int j, int k, PlayerBase entityplayer)
    {
        if(world.isServerSide)
        {
            return true;
        } else
        {
            Minecraft minecraft = Minecraft.class.cast(FabricLoader.getInstance().getGameInstance());
            minecraft.openScreen(new FCGuiCraftingAnvil(entityplayer.inventory, world, i, j, k));
            //ModLoader.getMinecraftInstance().displayGuiScreen(new FCGuiCraftingAnvil(entityplayer.inventory, world, i, j, k));
        	return true;
        }
    }

    public Box getCollisionShape(Level world, int i, int j, int k)
    {
        int iFacing = GetFacing(world, i, j, k);
        if(iFacing == 2 || iFacing == 3)
        {
            return Box.createButWasteMemory(((float)i + 0.5F) - 0.25F, (float)j, (float)k, (float)i + 0.5F + 0.25F, (float)j + 1.0F, (float)k + 1.0F);
        } else
        {
            return Box.createButWasteMemory((float)i, (float)j, ((float)k + 0.5F) - 0.25F, (float)i + 1.0F, (float)j + 1.0F, (float)k + 0.5F + 0.25F);
        }
    }

    public void updateBoundingBox(BlockView iblockaccess, int i, int j, int k)
    {
        int iFacing = GetFacing(iblockaccess, i, j, k);
        if(iFacing == 2 || iFacing == 3)
        {
            setBoundingBox(0.25F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
        } else
        {
            setBoundingBox(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 0.75F);
        }
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int k)
    {
        Level level = Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).level;
        if(level.getTileId(i,j,k) == mod_FCBetterThanWolves.fcAnvil.id) {
            return (level.getBlockState(i, j, k).get(FACING) & 3) + 2;
        }else{
            return 0;
        }
//        return iBlockAccess.getTileMeta(i, j, k);
    }

    public void SetFacing(Level world, int i, int j, int k, int iFacing)
    {
        if(iFacing >= 2)
        {
            iFacing -= 2;
        } else
        {
            iFacing = 0;
        }
        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i,j,k, currentState.with(FACING, iFacing));
//        world.setTileMeta(i, j, k, iFacing);
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
        int iNewFacing = FCUtilsMisc.RotateFacingAroundJ(iFacing, bReverse);
        if(iNewFacing != iFacing)
        {
            SetFacing(world, i, j, k, iNewFacing);
            world.method_202(i, j, k, i, j, k);
            world.method_216(i, j, k, id, getTickrate());
            ((LevelAccessor) world).invokeMethod_235(i, j, j, id);
        }
    }

    public static final float m_fAnvilBaseHeight = 0.125F;
    public static final float m_fAnvilBaseWidth = 0.5F;
    public static final float m_fAnvilHalfBaseWidth = 0.25F;
    public static final float m_fAnvilShaftHeight = 0.4375F;
    public static final float m_fAnvilShaftWidth = 0.25F;
    public static final float m_fAnvilHalfShaftWidth = 0.125F;
    public static final float m_fAnvilTopHeight = 0.4375F;
    public static final float m_fAnvilTopWidth = 0.375F;
    public static final float m_fAnvilTopHalfWidth = 0.1875F;

    /**
     * STATES
     */
    public static final IntProperty FACING = IntProperty.of("facing", 0, 3);

    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder){
        builder.add(FACING);
    }
}
