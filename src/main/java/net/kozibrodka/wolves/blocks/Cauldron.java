// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCBlockCauldron.java

package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.container.CauldronContainer;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.tileentity.CauldronTileEntity;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Item;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;

import java.util.List;
import java.util.Random;


public class Cauldron extends TemplateBlockWithEntity
    implements RotatableBlock
{

    public Cauldron(Identifier iid)
    {
        super(iid, Material.METAL);
        setTicksRandomly(true);
    }

    public void onBlockPlaced(Level world, int i, int j, int k)
    {
        super.onBlockPlaced(world, i, j, k);
        world.method_216(i, j, k, id, getTickrate());
    }

    public void onBlockRemoved(Level world, int i, int j, int k)
    {
        InventoryHandler.ejectInventoryContents(world, i, j, k, (InventoryBase)world.getTileEntity(i, j, k));
        super.onBlockRemoved(world, i, j, k);
    }

    public int getTextureForSide(int side)
    {
        if(side == 1)
        {
            return TextureListener.cauldron_top;
        }
        if(side == 0)
        {
            return TextureListener.cauldron_bottom;
        } else
        {
            return TextureListener.cauldron_side;
        }
    }

    public boolean canUse(Level world, int i, int j, int k, PlayerBase entityPlayer)
    {
        CauldronTileEntity tileentitycauldron = (CauldronTileEntity)world.getTileEntity(i, j, k);
        GuiHelper.openGUI(entityPlayer, Identifier.of("wolves:openCauldron"), (InventoryBase) tileentitycauldron, new CauldronContainer(entityPlayer.inventory, (CauldronTileEntity) tileentitycauldron));
        return true;
    }

    protected TileEntityBase createTileEntity()
    {
        return new CauldronTileEntity();
    }

    public Box getCollisionShape(Level world, int i, int j, int k)
    {
        return Box.createButWasteMemory(i, j, k, i + 1, (double)j + 0.99000000953674316D, (double)k + 1.0D);
    }

    public void randomDisplayTick(Level world, int i, int j, int k, Random random)
    {
        if(GetFireUnderState(world, i, j, k) > 0)
        {
            for(int counter = 0; counter < 5; counter++)
            {
                float smokeX = (float)i + random.nextFloat();
                float smokeY = (float)j + random.nextFloat() * 0.5F + 1.0F;
                float smokeZ = (float)k + random.nextFloat();
                world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
            }

        }
    }

    public void onEntityCollision(Level world, int i, int j, int k, EntityBase entity)
    {
        List collisionList = null;
        collisionList = world.getEntities(Item.class, Box.createButWasteMemory((float)i, (double)(float)j + 0.99000000953674316D, (float)k, (float)(i + 1), (double)(float)j + 0.99000000953674316D + 0.05000000074505806D, (float)(k + 1)));
        if(collisionList != null && collisionList.size() > 0)
        {
            CauldronTileEntity tileEntityCauldron = (CauldronTileEntity)world.getTileEntity(i, j, k);
            for(int listIndex = 0; listIndex < collisionList.size(); listIndex++)
            {
                Item targetEntityItem = (Item)collisionList.get(listIndex);
                if(targetEntityItem.removed)
                {
                    continue;
                }
                if(InventoryHandler.addItemInstanceToInventory(tileEntityCauldron, targetEntityItem.item))
                {
                     world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.pop", 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    targetEntityItem.remove();
                    continue;
                }
                int iBlockAboveID = world.getTileId(i, j + 1, k);
                if(iBlockAboveID != BlockBase.FLOWING_WATER.id && iBlockAboveID != BlockBase.STILL_WATER.id)
                {
                    continue;
                }
                double fFullBoxTop = (double)j + 1.05D;
                if(targetEntityItem.boundingBox.minY < fFullBoxTop)
                {
                    double offset = fFullBoxTop - targetEntityItem.boundingBox.minY;
                    targetEntityItem.setPosition(targetEntityItem.x, targetEntityItem.y + offset, targetEntityItem.z);
                }
            }

        }
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
        ValidateFireUnderState(world, i, j, k);
    }

    public void onAdjacentBlockUpdate(Level world, int i, int j, int k, int iid)
    {
        ValidateFireUnderState(world, i, j, k);
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

    public int GetFireUnderState(BlockView iBlockAccess, int i, int j, int k)
    {
        return iBlockAccess.getTileMeta(i, j, k) & 3;
    }

    private void SetFireUnderState(Level world, int i, int j, int k, int iState)
    {
        int iMetaData = world.getTileMeta(i, j, k) & -4;
        iMetaData |= iState & 3;
        world.setTileMeta(i, j, k, iMetaData);
    }

    private void ValidateFireUnderState(Level world, int i, int j, int k)
    {
        int iOldState = GetFireUnderState(world, i, j, k);
        int iNewState = 0;
        if(world.getTileId(i, j - 1, k) == BlockBase.FIRE.id)
        {
            iNewState = 1;
        } else
        if(world.getTileId(i, j - 1, k) == BlockListener.stokedFire.id)
        {
            iNewState = 2;
        }
        if(iNewState != iOldState)
        {
            SetFireUnderState(world, i, j, k, iNewState);
            CauldronTileEntity tileEntityCauldron = (CauldronTileEntity)world.getTileEntity(i, j, k);
            tileEntityCauldron.NotifyOfChangeInFireUnder(iNewState);
        }
    }

    public final int cauldronTopTextureIndex = 17;
    public final int cauldronSideTextureIndex = 18;
    public final int cauldronBottomTextureIndex = 19;
    public static final double dCauldronCollisionBoxHeight = 0.99000000953674316D;
}
