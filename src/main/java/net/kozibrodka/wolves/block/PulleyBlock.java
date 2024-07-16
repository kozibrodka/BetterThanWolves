package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.container.PulleyScreenHandler;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ScreenHandlerListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.ScreenPacket;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.block.entity.PulleyBlockEntity;
import net.kozibrodka.wolves.utils.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;

import java.util.List;
import java.util.Random;

public class PulleyBlock extends TemplateBlockWithEntity
    implements MechanicalDevice, RotatableBlock
{

    public PulleyBlock(Identifier iid)
    {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSoundGroup(WOOD_SOUND_GROUP);
    }

    public int getTexture(int iSide)
    {
        if(iSide == 0)
        {
            return TextureListener.pulley_bottom;
        }
        return iSide != 1 ? TextureListener.pulley_side : TextureListener.pulley_top;
    }

    public boolean onUse(World world, int i, int j, int k, PlayerEntity entityplayer)
    {
        PulleyBlockEntity tileEntityPulley = (PulleyBlockEntity)world.getBlockEntity(i, j, k);
        ScreenHandlerListener.TempGuiX = i;
        ScreenHandlerListener.TempGuiY = j;
        ScreenHandlerListener.TempGuiZ = k;
        if(world.isRemote){
            PacketHelper.send(new ScreenPacket("pulley",0, i, j, k));
        }
        GuiHelper.openGUI(entityplayer, Identifier.of("wolves:openPulley"), (Inventory) tileEntityPulley, new PulleyScreenHandler(entityplayer.inventory, (PulleyBlockEntity) tileEntityPulley));
        return true;
    }

    protected BlockEntity createBlockEntity()
    {
        return new PulleyBlockEntity();
    }

    public void onPlaced(World world, int i, int j, int k)
    {
        super.onPlaced(world, i, j, k);
        world.scheduleBlockUpdate(i, j, k, id, getTickRate());
    }

    public void onBreak(World world, int i, int j, int k)
    {
            BlockEntity tileEntity = world.getBlockEntity(i, j, k);
            if (tileEntity != null) {
                InventoryHandler.ejectInventoryContents(world, i, j, k, (Inventory) tileEntity);
            }
            super.onBreak(world, i, j, k);
    }

    public void neighborUpdate(World world, int i, int j, int k, int iid)
    {
        world.scheduleBlockUpdate(i, j, k, id, getTickRate());
    }

    public int getTickRate()
    {
        return iPulleyTickRate;
    }

    public void onTick(World world, int i, int j, int k, Random random)
    {
        boolean bReceivingPower = IsInputtingMechanicalPower(world, i, j, k);
        boolean bOn = IsBlockOn(world, i, j, k);
        boolean bStateChanged = false;
        if(bOn != bReceivingPower)
        {
             world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.2F, 1.25F);
            if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                voicePacket(world, "random.explode", i, j, k, 0.2F, 1.25F);
            }
            EmitPulleyParticles(world, i, j, k, random);
            SetBlockOn(world, i, j, k, bReceivingPower);
            bStateChanged = true;
        }
        boolean bRedstoneOn = IsRedstoneOn(world, i, j, k);
        boolean bReceivingRedstone = world.canTransferPower(i, j, k) || world.canTransferPower(i, j + 1, k);
        if(bRedstoneOn != bReceivingRedstone)
        {
             world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.2F, 1.25F);
            if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                voicePacket(world, "random.explode", i, j, k, 0.2F, 1.25F);
            }
            EmitPulleyParticles(world, i, j, k, random);
            SetRedstoneOn(world, i, j, k, bReceivingRedstone);
            bStateChanged = true;
        }
        if(bStateChanged)
        {
            ((PulleyBlockEntity)world.getBlockEntity(i, j, k)).NotifyPulleyEntityOfBlockStateChange();
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

    public int GetFacing(BlockView iBlockAccess, int i, int j, int l)
    {
        return 0;
    }

    public void SetFacing(World world1, int l, int i1, int j1, int k1)
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

    public void Rotate(World world, int i, int j, int k, boolean bReverse)
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

    public boolean IsInputtingMechanicalPower(World world, int i, int j, int k)
    {
        for(int iFacing = 2; iFacing <= 5; iFacing++)
        {
            BlockPosition targetPos = new BlockPosition(i, j, k);
            targetPos.AddFacingAsOffset(iFacing);
            int iTargetid = world.getBlockId(targetPos.i, targetPos.j, targetPos.k);
            if(iTargetid == BlockListener.axleBlock.id)
            {
                AxleBlock axleBlock = (AxleBlock)BlockListener.axleBlock;
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
            Block targetBlock = Block.BLOCKS[iTargetid];
            MechanicalDevice device = (MechanicalDevice)targetBlock;
            if(device.IsOutputtingMechanicalPower(world, targetPos.i, targetPos.j, targetPos.k))
            {
                return true;
            }
        }

        return false;
    }

    public boolean IsOutputtingMechanicalPower(World world, int i, int j, int l)
    {
        return false;
    }

    public boolean IsBlockOn(BlockView iBlockAccess, int i, int j, int k)
    {
        if (iBlockAccess == null) {
            return false;
        }
        return (iBlockAccess.getBlockMeta(i, j, k) & 1) > 0;
    }

    public void SetBlockOn(World world, int i, int j, int k, boolean bOn)
    {
        int iMetaData = world.getBlockMeta(i, j, k);
        if(bOn)
        {
            iMetaData |= 1;
        } else
        {
            iMetaData &= -2;
        }
        world.setBlockMeta(i, j, k, iMetaData);
        world.blockUpdateEvent(i, j, k);
    }

    public boolean IsRedstoneOn(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getBlockMeta(i, j, k) & 2) > 0;
    }

    public void SetRedstoneOn(World world, int i, int j, int k, boolean bOn)
    {
        int iMetaData = world.getBlockMeta(i, j, k) & -3;
        if(bOn)
        {
            iMetaData |= 2;
        }
        world.setBlockMeta(i, j, k, iMetaData);
        world.blockUpdateEvent(i, j, k);
    }

    void EmitPulleyParticles(World world, int i, int j, int k, Random random)
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
