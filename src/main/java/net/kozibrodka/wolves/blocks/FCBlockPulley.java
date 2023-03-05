package net.kozibrodka.wolves.blocks;

import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.container.FCContainerPulley;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.gui.FCGuiMillStone;
import net.kozibrodka.wolves.gui.FCGuiPulley;
import net.kozibrodka.wolves.tileentity.FCTileEntityPulley;
import net.kozibrodka.wolves.utils.*;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;

import java.util.Random;

public class FCBlockPulley extends TemplateBlockWithEntity
    implements FCMechanicalDevice, FCIBlock
{

    public FCBlockPulley(Identifier iid)
    {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSounds(WOOD_SOUNDS);
    }

    public int getTextureForSide(int iSide)
    {
        if(iSide == 0)
        {
            return TextureListener.pulley_bottom;
        }
        return iSide != 1 ? TextureListener.pulley_side : TextureListener.pulley_top;
    }

    public boolean canUse(Level world, int i, int j, int k, PlayerBase entityplayer)
    {
        if(world.isServerSide)
        {
            return true;
        }
        FCTileEntityPulley tileEntityPulley = (FCTileEntityPulley)world.getTileEntity(i, j, k);
        if(tileEntityPulley != null)
        {
            //ModLoader.OpenGUI(entityplayer, new FCGuiPulley(entityplayer.inventory, tileEntityPulley));
            Minecraft minecraft = Minecraft.class.cast(FabricLoader.getInstance().getGameInstance());
            minecraft.openScreen(new FCGuiPulley(entityplayer.inventory, tileEntityPulley));
        }
        return true;
    }

    protected TileEntityBase createTileEntity()
    {
        return new FCTileEntityPulley();
    }

    public void onBlockPlaced(Level world, int i, int j, int k)
    {
        super.onBlockPlaced(world, i, j, k);
        world.method_216(i, j, k, id, getTickrate());
    }

    public void onBlockRemoved(Level world, int i, int j, int k)
    {
            TileEntityBase tileEntity = world.getTileEntity(i, j, k);
            if (tileEntity != null) {
                FCUtilsInventory.EjectInventoryContents(world, i, j, k, (InventoryBase) tileEntity);
            }
            super.onBlockRemoved(world, i, j, k);
    }

    public void onAdjacentBlockUpdate(Level world, int i, int j, int k, int iid)
    {
        world.method_216(i, j, k, id, getTickrate());
    }

    public int getTickrate()
    {
        return iPulleyTickRate;
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
        boolean bReceivingPower = IsInputtingMechanicalPower(world, i, j, k);
        boolean bOn = IsBlockOn(world, i, j, k);
        boolean bStateChanged = false;
        if(bOn != bReceivingPower)
        {
             world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.2F, 1.25F);
            EmitPulleyParticles(world, i, j, k, random);
            SetBlockOn(world, i, j, k, bReceivingPower);
            bStateChanged = true;
        }
        boolean bRedstoneOn = IsRedstoneOn(world, i, j, k);
        boolean bReceivingRedstone = world.method_263(i, j, k) || world.method_263(i, j + 1, k);
        if(bRedstoneOn != bReceivingRedstone)
        {
             world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.2F, 1.25F);
            EmitPulleyParticles(world, i, j, k, random);
            SetRedstoneOn(world, i, j, k, bReceivingRedstone);
            bStateChanged = true;
        }
        if(bStateChanged)
        {
            ((FCTileEntityPulley)world.getTileEntity(i, j, k)).NotifyPulleyEntityOfBlockStateChange();
        }
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
        return true;
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

    public boolean IsRedstoneOn(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getTileMeta(i, j, k) & 2) > 0;
    }

    public void SetRedstoneOn(Level world, int i, int j, int k, boolean bOn)
    {
        int iMetaData = world.getTileMeta(i, j, k) & -3;
        if(bOn)
        {
            iMetaData |= 2;
        }
        world.setTileMeta(i, j, k, iMetaData);
    }

    void EmitPulleyParticles(Level world, int i, int j, int k, Random random)
    {
        for(int counter = 0; counter < 5; counter++)
        {
            float smokeX = (float)i + random.nextFloat();
            float smokeY = (float)j + random.nextFloat() * 0.5F + 1.0F;
            float smokeZ = (float)k + random.nextFloat();
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

    }

    private final int iPulleyTopIndex = 62;
    private final int iPulleySideIndex = 63;
    private final int iPulleyBottomIndex = 64;
    private static int iPulleyTickRate = 10;
}
