package net.kozibrodka.wolves.blocks;

import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.gui.FCGuiMillStone;
import net.kozibrodka.wolves.tileentity.FCTileEntityMillStone;
import net.kozibrodka.wolves.utils.FCBlockPos;
import net.kozibrodka.wolves.utils.FCMechanicalDevice;
import net.kozibrodka.wolves.utils.FCUtilsInventory;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;


import java.util.Random;

public class FCBlockMillStone extends TemplateBlockWithEntity
    implements FCMechanicalDevice
{

    public FCBlockMillStone(Identifier iid)
    {
        super(iid, Material.STONE);
        setHardness(3.5F);
        setSounds(STONE_SOUNDS);
        setTicksRandomly(true);
    }

    public int getTickrate()
    {
        return iMillStoneTickRate;
    }

    public int getTextureForSide(int side)
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

    public void onBlockPlaced(Level world, int i, int j, int k)
    {
        super.onBlockPlaced(world, i, j, k);
        world.method_216(i, j, k, id, getTickrate());
    }

    public void onAdjacentBlockUpdate(Level world, int i, int j, int k, int iid)
    {
        boolean bReceivingPower = IsInputtingMechanicalPower(world, i, j, k);
        if(IsBlockOn(world, i, j, k) != bReceivingPower)
        {
            world.method_216(i, j, k, id, getTickrate());
        }
    }

    public boolean canUse(Level world, int i, int j, int k, PlayerBase entityplayer)
    {
        if(world.isServerSide)
        {
            return true;
        } else
        {
            FCTileEntityMillStone tileEntityMillStone = (FCTileEntityMillStone)world.getTileEntity(i, j, k);
            Minecraft minecraft = Minecraft.class.cast(FabricLoader.getInstance().getGameInstance());
            minecraft.openScreen(new FCGuiMillStone(entityplayer.inventory, tileEntityMillStone));
            //ModLoader.OpenGUI(entityplayer, new FCGuiMillStone(entityplayer.inventory, tileEntityMillStone));
            return true;
        }
    }

    protected TileEntityBase createTileEntity()
    {
        return new FCTileEntityMillStone();
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
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
                FCTileEntityMillStone tileEntityMillStone = (FCTileEntityMillStone)world.getTileEntity(i, j, k);
                if(tileEntityMillStone.IsWholeCompanionCubeNextToBeProcessed())
                {
                    world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "mob.wolf.hurt", 5F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
                }
                 world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.2F, 1.25F);
                EmitMillingParticles(world, i, j, k, random);
                SetBlockOn(world, i, j, k, true);
            }
        }
    }

    public void randomDisplayTick(Level world, int i, int j, int k, Random random)
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

    public void onBlockRemoved(Level world, int i, int j, int k)
    {
        FCUtilsInventory.EjectInventoryContents(world, i, j, k, (InventoryBase)world.getTileEntity(i, j, k));
        super.onBlockRemoved(world, i, j, k);
    }

    public boolean IsBlockOn(Level world, int i, int j, int k)
    {
        return world.getTileMeta(i, j, k) > 0;
    }

    public void SetBlockOn(Level world, int i, int j, int k, boolean bOn)
    {
        if(bOn)
        {
            world.setTileMeta(i, j, k, 1);
        } else
        {
            world.setTileMeta(i, j, k, 0);
        }
    }

    void EmitMillingParticles(Level world, int i, int j, int k, Random random)
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

    public boolean IsInputtingMechanicalPower(Level world, int i, int j, int k)
    {
        for(int iFacing = 0; iFacing <= 1; iFacing++)
        {
            FCBlockPos targetPos = new FCBlockPos(i, j, k);
            targetPos.AddFacingAsOffset(iFacing);
            int iTargetid = world.getTileId(targetPos.i, targetPos.j, targetPos.k);
            if(iTargetid != mod_FCBetterThanWolves.fcAxleBlock.id)
            {
                continue;
            }
            FCBlockAxle axleBlock = (FCBlockAxle)mod_FCBetterThanWolves.fcAxleBlock;
            if(axleBlock.IsAxleOrientedTowardsFacing(world, targetPos.i, targetPos.j, targetPos.k, iFacing) && axleBlock.GetPowerLevel(world, targetPos.i, targetPos.j, targetPos.k) > 0)
            {
                return true;
            }
        }

        for(int iFacing = 2; iFacing <= 5; iFacing++)
        {
            FCBlockPos targetPos = new FCBlockPos(i, j, k);
            targetPos.AddFacingAsOffset(iFacing);
            int iTargetid = world.getTileId(targetPos.i, targetPos.j, targetPos.k);
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

    private static int iMillStoneTickRate = 10;
}
