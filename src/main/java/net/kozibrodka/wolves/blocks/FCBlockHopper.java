// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCBlockHopper.java

package net.kozibrodka.wolves.blocks;

import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.gui.FCGuiHopper;
import net.kozibrodka.wolves.gui.FCGuiMillStone;
import net.kozibrodka.wolves.tileentity.FCTileEntityHopper;
import net.kozibrodka.wolves.utils.*;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
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
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;

import java.util.List;
import java.util.Random;


public class FCBlockHopper extends TemplateBlockWithEntity
    implements FCMechanicalDevice, FCIBlock
{
    public FCBlockHopper(Identifier iid)
    {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSounds(WOOD_SOUNDS);
        texture = 4;
        setTicksRandomly(true);
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setDefaultState(getDefaultState()
                .with(REDOUTPUT, false)
                .with(POWER, false)
                .with(FULL, 0)
                .with(FILTER, 0)
        );
    }

    public int getTickrate()
    {
        return iHopperTickRate;
    }

    public Box getCollisionShape(Level world, int i, int j, int k)
    {
        if(!HasFilter(world, i, j, k))
        {
            return Box.createButWasteMemory(i, j, k, (float)(i + 1), (float)j + 0.5F, (float)(k + 1));
        } else
        {
            return Box.createButWasteMemory(i, j, k, (float)(i + 1), (float)j + 0.99F, (float)(k + 1));
        }
    }

    public Box getOutlineShape(Level world, int i, int j, int k)
    {
        return Box.createButWasteMemory(i, j, k, i + 1, (float)j + 1.0F, k + 1);
    }

    public void onBlockPlaced(Level world, int i, int j, int k)
    {
        super.onBlockPlaced(world, i, j, k);
        world.method_216(i, j, k, id, getTickrate());
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
        ((FCTileEntityHopper)world.getTileEntity(i, j, k)).bHopperEjectBlocked = false;
    }

    public boolean canUse(Level world, int i, int j, int k, PlayerBase entityplayer)
    {
        if(world.isServerSide)
        {
            return true;
        } else
        {
            FCTileEntityHopper tileEntityHopper = (FCTileEntityHopper)world.getTileEntity(i, j, k);
            Minecraft minecraft = Minecraft.class.cast(FabricLoader.getInstance().getGameInstance());
            minecraft.openScreen(new FCGuiHopper(entityplayer.inventory, tileEntityHopper));
            //ModLoader.OpenGUI(entityplayer, new FCGuiHopper(entityplayer.inventory, tileEntityHopper));
            return true;
        }
    }

    protected TileEntityBase createTileEntity()
    {
        return new FCTileEntityHopper();
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
        FCUtilsInventory.EjectInventoryContents(world, i, j, k, (InventoryBase)world.getTileEntity(i, j, k));
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
            FCTileEntityHopper tileEntityHopper = (FCTileEntityHopper)world.getTileEntity(i, j, k);
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
                            if(FCUtilsInventory.AddItemInstanceToInventoryInSlotRange(tileEntityHopper, sandItemInstance, 0, 17))
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
                        if(iFilterType == 6 && iTargetItemID == mod_FCBetterThanWolves.fcGroundNetherrack.id)
                        {
                            ItemInstance hellfireDustItemInstance = new ItemInstance(mod_FCBetterThanWolves.fcHellfireDust, targetEntityItem.item.count, 0);
                            Item hellfireDustEntityitem = new Item(world, targetEntityItem.x, targetEntityItem.y, targetEntityItem.z, hellfireDustItemInstance);
                            hellfireDustEntityitem.pickupDelay = 10;
                            tileEntityHopper.IncrementContainedSoulCount(hellfireDustItemInstance.count);
                            world.spawnEntity(hellfireDustEntityitem);
                             world.playSound((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, "mob.ghast.moan", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                            targetEntityItem.remove();
                            bSwallowed = true;
                        } else
                        if(FCUtilsInventory.AddItemInstanceToInventoryInSlotRange(tileEntityHopper, targetEntityItem.item, 0, 17))
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

    //TODO logic change
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
            FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, mod_FCBetterThanWolves.fcPanel_wood.id, 1); //TODO meta?
        }

        for(int iTemp = 0; iTemp < 1; iTemp++)
        {
            FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, mod_FCBetterThanWolves.fcGear.id, 0);
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
        FCUtilsMisc.DestroyHorizontallyAttachedAxles(world, i, j, k);
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
            FCBlockPos targetPos = new FCBlockPos(i, j, k);
            targetPos.AddFacingAsOffset(iFacing);
            int iTargetid = world.getTileId(targetPos.i, targetPos.j, targetPos.k);
            if(iTargetid == mod_FCBetterThanWolves.fcAxleBlock.id)
            {
                FCBlockAxle axleBlock = (FCBlockAxle)mod_FCBetterThanWolves.fcAxleBlock;
                if(axleBlock.IsAxleOrientedTowardsFacing(world, targetPos.i, targetPos.j, targetPos.k, iFacing) && axleBlock.GetPowerLevel(world, targetPos.i, targetPos.j, targetPos.k) > 0)
                {
                    return true;
                }
                continue;
            }
            if(iTargetid != mod_FCBetterThanWolves.fcHandCrank.id)
            {
                continue;
            }
            BlockBase targetBlock = BlockBase.BY_ID[iTargetid];
            FCMechanicalDevice device = (FCMechanicalDevice)targetBlock;
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

    public final int iHopperTopTextureIndex = 46;
    public final int iHopperSideTextureIndex = 47;
    public final int iHopperBottomTextureIndex = 48;
    public final int iHopperContentsTextureIndex = 49;
    private final int iHopperLadderFilterTextureID = 50;
    private final int iHopperTrapDoorFilterTextureID = 51;
    private final int iHopperGrateFilterTextureID = 52;
    private final int iHopperRollersFilterTextureID = 53;
    private final int iHopperWhickerFilterTextureID = 54;
    private final int iHopperSoulSandFilterTextureID = 55;
    private static int iHopperTickRate = 10;
    public static final float fHopperCollisionBoxHeight = 0.5F;
    public static final float fHopperCollisionBoxHeightWithFilter = 0.99F;
    public static final float fHopperVisualHeight = 1F;

    public static final BooleanProperty POWER = BooleanProperty.of("power");
    public static final BooleanProperty REDOUTPUT = BooleanProperty.of("redoutput");
    public static final IntProperty FULL = IntProperty.of("full",0,18);
    public static final IntProperty FILTER = IntProperty.of("filter",0,6);

    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder){
        builder.add(REDOUTPUT);
        builder.add(POWER);
        builder.add(FULL);
        builder.add(FILTER);
    }
}
