// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCBlockHopper.java

package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.container.HopperContainer;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.modsupport.AffectedByBellows;
import net.kozibrodka.wolves.tileentity.HopperTileEntity;
import net.kozibrodka.wolves.utils.*;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Item;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;

import java.util.List;
import java.util.Random;


public class Hopper extends TemplateBlockWithEntity
    implements MechanicalDevice, RotatableBlock, BlockWithWorldRenderer, BlockWithInventoryRenderer, AffectedByBellows
{
    public Hopper(Identifier iid)
    {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSounds(WOOD_SOUNDS);
        setTicksRandomly(true);
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public int getTickrate()
    {
        return hopperTickRate;
    }

    public Box getCollisionShape(Level world, int i, int j, int k)
    {
        if(!HasFilter(world, i, j, k))
        {
            return Box.create(i, j, k, (float)(i + 1), (float)j + 0.5F, (float)(k + 1));
        } else
        {
            return Box.create(i, j, k, (float)(i + 1), (float)j + 0.99F, (float)(k + 1));
        }
    }

    public Box getOutlineShape(Level world, int i, int j, int k)
    {
        return Box.create(i, j, k, i + 1, (float)j + 1.0F, k + 1);
    }

    public void onBlockPlaced(Level world, int i, int j, int k)
    {
        super.onBlockPlaced(world, i, j, k);
        world.method_216(i, j, k, id, getTickrate());
    }

    public int getTextureForSide(int iSide)
    {
        if(iSide == 0)
        {
            return TextureListener.hopper_bottom;
        }
        return iSide != 1 ? TextureListener.hopper_side : TextureListener.hopper_top;
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public void onAdjacentBlockUpdate(Level world, int i, int j, int k, int iid)
    {
        boolean bReceivingPower = IsInputtingMechanicalPower(world, i, j, k);
        if(IsBlockOn(world, i, j, k) != bReceivingPower)
        {
            world.method_216(i, j, k, id, getTickrate());
        }
        ((HopperTileEntity)world.getTileEntity(i, j, k)).bHopperEjectBlocked = false;
    }

    public boolean canUse(Level world, int i, int j, int k, PlayerBase entityplayer)
    {
//        if(world.isServerSide)
//        {
//            return true;
//        } else
//        {
//            FCTileEntityHopper tileEntityHopper = (FCTileEntityHopper)world.getTileEntity(i, j, k);
//            Minecraft minecraft = Minecraft.class.cast(FabricLoader.getInstance().getGameInstance());
//            minecraft.openScreen(new FCGuiHopper(entityplayer.inventory, tileEntityHopper));
//            //ModLoader.OpenGUI(entityplayer, new FCGuiHopper(entityplayer.inventory, tileEntityHopper));
//            return true;
//        }

        HopperTileEntity tileEntityHopper = (HopperTileEntity)world.getTileEntity(i, j, k);
        GuiHelper.openGUI(entityplayer, Identifier.of("wolves:openHooper"), (InventoryBase) tileEntityHopper, new HopperContainer(entityplayer.inventory, (HopperTileEntity) tileEntityHopper));
        return true;
    }

    protected TileEntityBase createTileEntity()
    {
        return new HopperTileEntity();
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
        boolean bReceivingPower = IsInputtingMechanicalPower(world, i, j, k);
        boolean bOn = IsBlockOn(world, i, j, k);
        boolean bFull = IsHopperFull(world, i, j, k);
        boolean bRedstone = IsRedstoneOutputOn(world, i, j, k);
        if(bOn != bReceivingPower)
        {
             world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.2F, 1.25F);
            EmitHopperParticles(world, i, j, k, random);
            SetBlockOn(world, i, j, k, bReceivingPower);
        }
        if(bFull != bRedstone)
        {
             world.playSound(i, j, k, "random.click", 0.25F, 1.2F);
            SetRedstoneOutputOn(world, i, j, k, bFull);
        }
    }

    public void onBlockRemoved(Level world, int i, int j, int k)
    {
        InventoryHandler.EjectInventoryContents(world, i, j, k, (InventoryBase)world.getTileEntity(i, j, k));
        super.onBlockRemoved(world, i, j, k);
    }

    public void onEntityCollision(Level world, int i, int j, int k, EntityBase entity)
    {
        List collisionList = null;
        boolean bHasFilter = HasFilter(world, i, j, k);
        float fHopperHeight;
        if(!bHasFilter)
        {
            fHopperHeight = 0.5F;
        } else
        {
            fHopperHeight = 0.99F;
        }
        collisionList = world.getEntities(Item.class, Box.createButWasteMemory((float)i, (float)j + fHopperHeight, (float)k, (float)(i + 1), (float)j + fHopperHeight + 0.05F, (float)(k + 1)));
        if(collisionList != null && collisionList.size() > 0)
        {
            HopperTileEntity tileEntityHopper = (HopperTileEntity)world.getTileEntity(i, j, k);
            for(int listIndex = 0; listIndex < collisionList.size(); listIndex++)
            {
                Item targetEntityItem = (Item)collisionList.get(listIndex);
                boolean bSwallowed = false;
                if(!targetEntityItem.removed)
                {
                    ItemBase targetItem = ItemBase.byId[targetEntityItem.item.itemId];
                    if(tileEntityHopper.CanCurrentFilterProcessItem(targetItem))
                    {
                        int iFilterType = tileEntityHopper.GetFilterType();
                        int iTargetItemID = ItemBase.byId[targetEntityItem.item.itemId].id;
                        if(iFilterType == 4 && iTargetItemID == BlockBase.GRAVEL.id)
                        {
                            ItemInstance sandItemInstance = new ItemInstance(BlockBase.SAND.id, targetEntityItem.item.count, 0);
                            int iSandSwallowed = 0;
                            if(InventoryHandler.AddItemInstanceToInventoryInSlotRange(tileEntityHopper, sandItemInstance, 0, 17))
                            {
                                iSandSwallowed = targetEntityItem.item.count;
                                targetEntityItem.remove();
                                bSwallowed = true;
                            } else
                            {
                                iSandSwallowed = targetEntityItem.item.count - sandItemInstance.count;
                                targetEntityItem.item.count -= iSandSwallowed;
                            }
                            if(iSandSwallowed > 0)
                            {
                                world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.pop", 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                                ItemInstance flintItemInstance = new ItemInstance(ItemBase.flint.id, iSandSwallowed, 0);
                                Item flintEntityitem = new Item(world, targetEntityItem.x, targetEntityItem.y, targetEntityItem.z, flintItemInstance);
                                flintEntityitem.pickupDelay = 10;
                                world.spawnEntity(flintEntityitem);
                            }
                        } else
                        if(InventoryHandler.AddItemInstanceToInventoryInSlotRange(tileEntityHopper, targetEntityItem.item, 0, 17))
                        {
                            world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.pop", 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                            targetEntityItem.remove();
                            bSwallowed = true;
                        }
                    }
                }
                if(!bHasFilter || bSwallowed)
                {
                    continue;
                }
                int iBlockAboveID = world.getTileId(i, j + 1, k);
                if(iBlockAboveID != BlockBase.FLOWING_WATER.id && iBlockAboveID != BlockBase.STILL_WATER.id)
                {
                    continue;
                }
                double fHopperFullBoxTop = (double)j + 1.05D;
                if(targetEntityItem.boundingBox.minY < fHopperFullBoxTop)
                {
                    double offset = fHopperFullBoxTop - targetEntityItem.boundingBox.minY;
                    targetEntityItem.setPosition(targetEntityItem.x, targetEntityItem.y + offset, targetEntityItem.z);
                }
            }

        }
    }

    public boolean isPowered(BlockView iBlockAccess, int i, int j, int k, int l)
    {
        return IsRedstoneOutputOn(iBlockAccess, i, j, k);
    }

    public boolean indirectlyPowered(Level world, int i, int j, int i1, int j1)
    {
        return false;
    }

    public boolean getEmitsRedstonePower()
    {
        return true;
    }

    public boolean IsBlockOn(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getTileMeta(i, j, k) & 1) > 0;
    }

    public void SetBlockOn(Level world, int i, int j, int k, boolean bOn)
    {
        int iMetaData = world.getTileMeta(i, j, k);
        if(bOn)
        {
            iMetaData |= 1;
        } else
        {
            iMetaData &= -2;
        }
        world.setTileMeta(i, j, k, iMetaData);
    }

    public boolean IsHopperFull(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getTileMeta(i, j, k) & 2) > 0;
    }

    public void SetHopperFull(Level world, int i, int j, int k, boolean bOn)
    {
        boolean bOldOn = IsHopperFull(world, i, j, k);
        if(bOldOn != bOn)
        {
            int iMetaData = world.getTileMeta(i, j, k);
            if(bOn)
            {
                iMetaData |= 2;
            } else
            {
                iMetaData &= -3;
            }
            world.setTileMeta(i, j, k, iMetaData);
            world.method_216(i, j, k, id, getTickrate());
        }
    }

    public boolean IsRedstoneOutputOn(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getTileMeta(i, j, k) & 4) > 0;
    }

    public void SetRedstoneOutputOn(Level world, int i, int j, int k, boolean bOn)
    {
        int iMetaData = world.getTileMeta(i, j, k);
        if(bOn)
        {
            iMetaData |= 4;
        } else
        {
            iMetaData &= -5;
        }
        world.setTileMeta(i, j, k, iMetaData);
    }

    public boolean HasFilter(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getTileMeta(i, j, k) & 8) > 0;
    }

    public void SetHasFilter(Level world, int i, int j, int k, boolean bOn)
    {
        int iMetaData = world.getTileMeta(i, j, k);
        if(bOn)
        {
            iMetaData |= 8;
        } else
        {
            iMetaData &= -9;
        }
        world.setTileMeta(i, j, k, iMetaData);
    }

    void EmitHopperParticles(Level world, int i, int j, int k, Random random)
    {
        for(int counter = 0; counter < 5; counter++)
        {
            float smokeX = (float)i + random.nextFloat();
            float smokeY = (float)j + random.nextFloat() * 0.5F + 1.0F;
            float smokeZ = (float)k + random.nextFloat();
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

    }

    public void BreakHopper(Level world, int i, int j, int k)
    {
        for(int iTemp = 0; iTemp < 2; iTemp++)
        {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, BlockListener.fcOmniSlab.id, 1);
        }

        for(int iTemp = 0; iTemp < 1; iTemp++)
        {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, mod_FCBetterThanWolves.fcGear.id, 0);
        }

         world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 1.0F, 1.25F);
        world.setTile(i, j, k, 0);
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
        return true;
    }

    public boolean CanTransmitRotation(BlockView iBlockAccess, int i, int j, int l)
    {
        return false;
    }

    public void Rotate(Level world, int i, int j, int k, boolean bReverse)
    {
        UnsortedUtils.DestroyHorizontallyAttachedAxles(world, i, j, k);
    }

    public boolean CanOutputMechanicalPower()
    {
        return false;
    }

    public boolean CanInputMechanicalPower()
    {
        return true;
    }

    public boolean IsInputtingMechanicalPower(Level world, int i, int j, int k)
    {
        for(int iFacing = 2; iFacing <= 5; iFacing++)
        {
            BlockPosition targetPos = new BlockPosition(i, j, k);
            targetPos.AddFacingAsOffset(iFacing);
            int iTargetid = world.getTileId(targetPos.i, targetPos.j, targetPos.k);
            if(iTargetid == BlockListener.fcAxleBlock.id)
            {
                Axle axleBlock = (Axle)BlockListener.fcAxleBlock;
                if(axleBlock.IsAxleOrientedTowardsFacing(world, targetPos.i, targetPos.j, targetPos.k, iFacing) && axleBlock.GetPowerLevel(world, targetPos.i, targetPos.j, targetPos.k) > 0)
                {
                    return true;
                }
                continue;
            }
            if(iTargetid != BlockListener.fcHandCrank.id)
            {
                continue;
            }
            BlockBase targetBlock = BlockBase.BY_ID[iTargetid];
            MechanicalDevice device = (MechanicalDevice)targetBlock;
            if(device.IsOutputtingMechanicalPower(world, targetPos.i, targetPos.j, targetPos.k))
            {
                return true;
            }
        }

        return false;
    }

    public boolean IsOutputtingMechanicalPower(Level world, int i, int j, int l)
    {
        return false;
    }

    private static int hopperTickRate = 10;

    @Override
    public boolean renderWorld(BlockRenderer tileRenderer, BlockView tileView, int x, int y, int z) {
        this.setBoundingBox(0.0F, 0.25F, 0.0F, 0.125F, 1.0F, 0.875F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.0F, 0.25F, 0.875F, 0.875F, 1.0F, 1.0F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.875F, 0.25F, 0.125F, 1.0F, 1.0F, 1.0F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.125F, 0.25F, 0.0F, 1.0F, 1.0F, 0.125F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.125F, 0.25F, 0.125F, 0.875F, 0.375F, 0.875F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.25F, 0.6875F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        HopperTileEntity fctileentityhopper = (HopperTileEntity)tileView.getTileEntity(x, y, z);
        int l = InventoryHandler.GetNumOccupiedStacksInSlotRange(fctileentityhopper, 0, 17);
        if(l > 0)
        {
            float f = (float)l / 18F;
            float f1 = 0.375F;
            float f2 = f1 + 0.0625F + (0.875F - (f1 + 0.0625F)) * f;
            this.setBoundingBox(0.125F, f1, 0.125F, 0.875F, f2, 0.875F);
            CustomBlockRendering.RenderStandardBlockWithTexture(tileRenderer, this, x, y, z, TextureListener.filler);
        }
        int i1 = fctileentityhopper.GetFilterType();
        if(i1 > 0)
        {
//            boolean flag = FCUtilsRender.GetOverrideBlockTexture(tileRenderer) >= 0;
            int byte0 = 0;
            if(i1 == 1)
            {
                byte0 = TextureListener.hopper_ladder; //50
            } else
            if(i1 == 2)
            {
                byte0 = TextureListener.hopper_trapdoor; //51
            } else
            if(i1 == 3)
            {
                byte0 = TextureListener.hopper_grate; //52
            } else
            if(i1 == 4)
            {
                byte0 = TextureListener.hopper_wicker;  //54
            } else
            if(i1 == 5)
            {
                byte0 = TextureListener.hopper_rollers; //53
            } else
            if(i1 == 6)
            {
                byte0 = TextureListener.hopper_soulsand; //55
            }
            this.setBoundingBox(0.125F, 0.875F, 0.125F, 0.875F, 0.9375F, 0.875F);
            CustomBlockRendering.RenderStandardBlockWithTexture(tileRenderer, this, x, y, z, byte0);
        }
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }

    @Override
    public void renderInventory(BlockRenderer tileRenderer, int meta) {
        this.setBoundingBox(0.0F, 0.25F, 0.0F, 0.125F, 1.0F, 0.875F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        this.setBoundingBox(0.0F, 0.25F, 0.875F, 0.875F, 1.0F, 1.0F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        this.setBoundingBox(0.875F, 0.25F, 0.125F, 1.0F, 1.0F, 1.0F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        this.setBoundingBox(0.125F, 0.25F, 0.0F, 1.0F, 1.0F, 0.125F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        this.setBoundingBox(0.125F, 0.25F, 0.125F, 0.875F, 0.375F, 0.875F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        this.setBoundingBox(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.25F, 0.6875F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void affectBlock(Level world, int i, int j, int k, BlockPosition tempTargetPos, int facing) {
        for (int l = 0; l < 2; l++) {
            tempTargetPos.AddFacingAsOffset(facing);
            if (!world.isAir(tempTargetPos.i, tempTargetPos.j, tempTargetPos.k)) return;
        }
        TileEntityBase tileEntityHopper = world.getTileEntity(i, j, k);
        if (tileEntityHopper == null) return;
        if (!(tileEntityHopper instanceof HopperTileEntity)) return;
        if (((HopperTileEntity) tileEntityHopper).GetFilterType() != 6) return;
        ((HopperTileEntity) tileEntityHopper).setInventoryItem(18, new ItemInstance(mod_FCBetterThanWolves.soulFilter, 1));
    }
}
