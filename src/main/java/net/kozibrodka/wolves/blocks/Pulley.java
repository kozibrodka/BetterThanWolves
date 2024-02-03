package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.container.PulleyContainer;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.GUIListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.GuiPacket;
import net.kozibrodka.wolves.tileentity.PulleyTileEntity;
import net.kozibrodka.wolves.utils.*;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;

import java.util.Random;

public class Pulley extends TemplateBlockWithEntity
    implements MechanicalDevice, RotatableBlock
{

    public Pulley(Identifier iid)
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
        PulleyTileEntity tileEntityPulley = (PulleyTileEntity)world.getTileEntity(i, j, k);
        GUIListener.TempGuiX = i;
        GUIListener.TempGuiY = j;
        GUIListener.TempGuiZ = k;
        if(world.isServerSide){
            PacketHelper.send(new GuiPacket("pulley",0, i, j, k));
        }
        GuiHelper.openGUI(entityplayer, Identifier.of("wolves:openPulley"), (InventoryBase) tileEntityPulley, new PulleyContainer(entityplayer.inventory, (PulleyTileEntity) tileEntityPulley));
        return true;
    }

    protected TileEntityBase createTileEntity()
    {
        return new PulleyTileEntity();
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
                InventoryHandler.ejectInventoryContents(world, i, j, k, (InventoryBase) tileEntity);
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
            ((PulleyTileEntity)world.getTileEntity(i, j, k)).NotifyPulleyEntityOfBlockStateChange();
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
            if(iTargetid == BlockListener.axleBlock.id)
            {
                Axle axleBlock = (Axle)BlockListener.axleBlock;
                if(axleBlock.IsAxleOrientedTowardsFacing(world, targetPos.i, targetPos.j, targetPos.k, iFacing) && axleBlock.GetPowerLevel(world, targetPos.i, targetPos.j, targetPos.k) > 0)
                {
                    return true;
                }
                continue;
            }
            if(iTargetid != BlockListener.handCrank.id)
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

    public boolean IsBlockOn(BlockView iBlockAccess, int i, int j, int k)
    {
        if (iBlockAccess == null) {
            return false;
        }
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
        world.method_243(i, j, k);
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
