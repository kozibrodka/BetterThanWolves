package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.container.MillStoneScreenHandler;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ScreenHandlerListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.ScreenPacket;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.block.entity.MillStoneBlockEntity;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.MechanicalDevice;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.World;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;


import java.util.List;
import java.util.Random;

public class MillStoneBlock extends TemplateBlockWithEntity
    implements MechanicalDevice
{

    public MillStoneBlock(Identifier iid)
    {
        super(iid, Material.STONE);
        setHardness(3.5F);
        setSoundGroup(DEFAULT_SOUND_GROUP);
        setTickRandomly(true);
    }

    public int getTickRate()
    {
        return iMillStoneTickRate;
    }

    public int getTexture(int side)
    {
        if(side == 1)
        {
            return TextureListener.millstone_top;
        }
        if(side == 0)
        {
            return TextureListener.millstone_bottom;
        } else
        {
            return TextureListener.millstone_side;
        }
    }

    public void onPlaced(World world, int i, int j, int k)
    {
        super.onPlaced(world, i, j, k);
        world.method_216(i, j, k, BlockListener.millStone.id, getTickRate());
    }

    public void neighborUpdate(World world, int i, int j, int k, int iid)
    {
        boolean bReceivingPower = IsInputtingMechanicalPower(world, i, j, k);
        if(IsBlockOn(world, i, j, k) != bReceivingPower)
        {
            world.method_216(i, j, k, BlockListener.millStone.id, getTickRate());
        }
    }

    public boolean onUse(World world, int i, int j, int k, PlayerEntity entityplayer)
    {
        MillStoneBlockEntity tileEntityMillStone = (MillStoneBlockEntity)world.method_1777(i, j, k);
        ScreenHandlerListener.TempGuiX = i;
        ScreenHandlerListener.TempGuiY = j;
        ScreenHandlerListener.TempGuiZ = k;
        if(world.isRemote){
            PacketHelper.send(new ScreenPacket("mill",0, i, j, k));
        }
        GuiHelper.openGUI(entityplayer, Identifier.of("wolves:openMillStone"), (Inventory) tileEntityMillStone, new MillStoneScreenHandler(entityplayer.inventory, (MillStoneBlockEntity) tileEntityMillStone));
        return true;
    }

    protected BlockEntity createBlockEntity()
    {
        return new MillStoneBlockEntity();
    }

    public void onTick(World world, int i, int j, int k, Random random)
    {
        boolean bReceivingPower = IsInputtingMechanicalPower(world, i, j, k);
        boolean bOn = IsBlockOn(world, i, j, k);
        if(bOn != bReceivingPower)
        {
            if(bOn)
            {
                SetBlockOn(world, i, j, k, false);
            } else
            {
                MillStoneBlockEntity tileEntityMillStone = (MillStoneBlockEntity)world.method_1777(i, j, k);
                if(tileEntityMillStone.IsWholeCompanionCubeNextToBeProcessed())
                {
                    world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "mob.wolf.hurt", 5F, (world.field_214.nextFloat() - world.field_214.nextFloat()) * 0.2F + 1.0F);
                    if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                        voicePacket(world, "mob.wolf.hurt", i, j, k, 5F, (world.field_214.nextFloat() - world.field_214.nextFloat()) * 0.2F + 1.0F);
                    }
                }
                world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.2F, 1.25F);
                if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                    voicePacket(world, "random.explode", i, j, k, 0.2F, 1.25F);
                }
                EmitMillingParticles(world, i, j, k, random);
                SetBlockOn(world, i, j, k, true);
            }
        }
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(World world, String name, int x, int y, int z, float g, float h){
        List list2 = world.field_200;
        if(list2.size() != 0) {
            for(int k = 0; k < list2.size(); k++)
            {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g,h));
            }
        }
    }

    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        if(IsBlockOn(world, i, j, k))
        {
            EmitMillingParticles(world, i, j, k, random);
            if(random.nextInt(5) == 0)
            {
                 world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.1F + random.nextFloat() * 0.1F, 1.25F + random.nextFloat() * 0.1F);
            }
        }
    }

    public void onBreak(World world, int i, int j, int k)
    {
        InventoryHandler.ejectInventoryContents(world, i, j, k, (Inventory)world.method_1777(i, j, k));
        super.onBreak(world, i, j, k);
    }

    public boolean IsBlockOn(World world, int i, int j, int k)
    {
        return world.getBlockMeta(i, j, k) > 0;
    }

    public void SetBlockOn(World world, int i, int j, int k, boolean bOn)
    {
        if(bOn)
        {
            world.method_215(i, j, k, 1);
            world.method_243(i, j, k);
        } else
        {
            world.method_215(i, j, k, 0);
            world.method_243(i, j, k);
        }
    }

    void EmitMillingParticles(World world, int i, int j, int k, Random random)
    {
        for(int counter = 0; counter < 5; counter++)
        {
            float smokeX = (float)i + random.nextFloat();
            float smokeY = (float)j + random.nextFloat() * 0.5F + 1.0F;
            float smokeZ = (float)k + random.nextFloat();
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

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
        for(int iFacing = 0; iFacing <= 1; iFacing++)
        {
            BlockPosition targetPos = new BlockPosition(i, j, k);
            targetPos.AddFacingAsOffset(iFacing);
            int iTargetid = world.getBlockId(targetPos.i, targetPos.j, targetPos.k);
            if(iTargetid != BlockListener.axleBlock.id)
            {
                continue;
            }
            AxleBlock axleBlock = (AxleBlock)BlockListener.axleBlock;
            if(axleBlock.IsAxleOrientedTowardsFacing(world, targetPos.i, targetPos.j, targetPos.k, iFacing) && axleBlock.GetPowerLevel(world, targetPos.i, targetPos.j, targetPos.k) > 0)
            {
                return true;
            }
        }

        for(int iFacing = 2; iFacing <= 5; iFacing++)
        {
            BlockPosition targetPos = new BlockPosition(i, j, k);
            targetPos.AddFacingAsOffset(iFacing);
            int iTargetid = world.getBlockId(targetPos.i, targetPos.j, targetPos.k);
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

    private static int iMillStoneTickRate = 10;
}
