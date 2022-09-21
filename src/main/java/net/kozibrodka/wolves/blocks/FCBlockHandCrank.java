
package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.utils.FCBlockPos;
import net.kozibrodka.wolves.utils.FCMechanicalDevice;
import net.kozibrodka.wolves.utils.FCUtilsMisc;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.util.Random;


public class FCBlockHandCrank extends TemplateBlockBase
    implements FCMechanicalDevice
{

    public FCBlockHandCrank(Identifier iid)
    {
        super(iid, Material.DOODADS);
        setHardness(0.5F);
        setSounds(WOOD_SOUNDS);
        texture = 31;
        setTicksRandomly(true);
    }

    public int getTickrate()
    {
        return iHandCrankTickRate;
    }

    public Box getCollisionShape(Level world, int i, int j, int k)
    {
        return Box.createButWasteMemory((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + fHandCrankBaseHeight, (float)k + 1.0F);
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public void afterPlaced(Level world, int i, int j, int k, Living entityLiving)
    {
        int iFacing = FCUtilsMisc.ConvertPlacingEntityOrientationToFlatBlockFacing(entityLiving);
        SetFacing(world, i, j, k, iFacing);
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
        switch (iFacing)
        {
            case 0: // '\0'
            case 1: // '\001'
                iFacing = 0;
                break;

            case 2: // '\002'
            case 3: // '\003'
                iFacing = 1;
                break;

        }
        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i,j,k, currentState.with(FACING, iFacing));
    }

    /**
     getRenderType  ??
     */
//    public int getRenderType()
//    {
//        return mod_FCBetterThanWolves.iCustomHandCrankRenderID;
//    }

    public void updateBoundingBox(BlockView iBlockAccess, int i, int j, int k)
    {
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public boolean canPlaceAt(Level world, int i, int j, int k)
    {
        return world.isFullOpaque(i, j - 1, k);
    }

    public void activate(Level world, int i, int j, int k, PlayerBase entityplayer)
    {
        canUse(world, i, j, k, entityplayer);
    }

    public boolean canUse(Level world, int i, int j, int k, PlayerBase entityPlayer)
    {
        if(world.isServerSide)
        {
            return true;
        }
//        int iMetaData = world.getTileMeta(i, j, k);
        int iMetaData = world.getBlockState(i,j,k).get(TICKS);
        if(iMetaData == 0)
        {
            if(!CheckForOverpower(world, i, j, k))
            {
                BlockState currentState = world.getBlockState(i, j, k);
                world.setBlockStateWithNotify(i,j,k,currentState.with(TICKS, 1));
//                world.setTileMeta(i, j, k, 1);
                world.method_202(i, j, k, i, j, k);
                world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.click", 1.0F, 2.0F);
                world.updateAdjacentBlocks(i, j, k, id);
                world.method_216(i, j, k, id, getTickrate());
            } else
            {
                BreakCrankWithDrop(world, i, j, k);
            }
        }
        return true;
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
//        int iMetaData = world.getTileMeta(i, j, k);
        int iMetaData = world.getBlockState(i,j,k).get(TICKS);
        if(iMetaData > 0)
        {
            BlockState currentState = world.getBlockState(i, j, k);
            if(iMetaData < 7)
            {
                if(iMetaData <= 6)
                {
                     world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.click", 1.0F, 2.0F);
                }
                if(iMetaData <= 5)
                {
                    world.method_216(i, j, k, id, getTickrate() + iMetaData);
                } else
                {
                    world.method_216(i, j, k, id, iHandCrankDelayBeforeReset);
                }
                world.setBlockStateWithNotify(i,j,k,currentState.with(TICKS, iMetaData + 1));
//                world.setTileMeta(i, j, k, iMetaData + 1);
            } else
            {
                world.setBlockStateWithNotify(i,j,k,currentState.with(TICKS, 0));
//                world.setTileMeta(i, j, k, 0);
                world.method_202(i, j, k, i, j, k);
                 world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.click", 0.3F, 0.7F);
            }
        }
    }

    public void onAdjacentBlockUpdate(Level world, int i, int j, int k, int iid)
    {
        if(!world.isFullOpaque(i, j - 1, k))
        {
            drop(world, i, j, k, world.getTileMeta(i, j, k));
            world.setTile(i, j, k, 0);
        }
    }

    public boolean CanOutputMechanicalPower()
    {
        return true;
    }

    public boolean CanInputMechanicalPower()
    {
        return false;
    }

    public boolean IsInputtingMechanicalPower(Level world, int i, int j, int l)
    {
        return false;
    }

    public boolean IsOutputtingMechanicalPower(Level world, int i, int j, int k)
    {
        return world.getBlockState(i,j,k).get(TICKS) > 0;
//        return world.getTileMeta(i, j, k) > 0;
    }

    public boolean CheckForOverpower(Level world, int i, int j, int k)
    {
        int iNumPotentialDevicesToPower = 0;
        for(int iTempFacing = 2; iTempFacing <= 5; iTempFacing++)
        {
            FCBlockPos tempPos = new FCBlockPos(i, j, k);
            tempPos.AddFacingAsOffset(iTempFacing);
            int iTempid = world.getTileId(tempPos.i, tempPos.j, tempPos.k);
            BlockBase tempBlock = BlockBase.BY_ID[iTempid];
            if(tempBlock == null || !(tempBlock instanceof FCMechanicalDevice))
            {
                continue;
            }
            FCMechanicalDevice tempDevice = (FCMechanicalDevice)tempBlock;
            if(tempDevice.CanInputMechanicalPower())
            {
                iNumPotentialDevicesToPower++;
            }
        }

        return iNumPotentialDevicesToPower > 1;
    }

    public void BreakCrankWithDrop(Level world, int i, int j, int k)
    {
        FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, ItemBase.stick.id, 0);
        FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, BlockBase.COBBLESTONE.id, 0);
        FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, BlockBase.COBBLESTONE.id, 0);
        FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, mod_FCBetterThanWolves.fcGear.id, 0);
        world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.2F, 1.25F);
        world.setTile(i, j, k, 0);
    }

    /**
     * STATES
     */
    public static final IntProperty TICKS = IntProperty.of("ticks", 0, 7);
    public static final IntProperty FACING = IntProperty.of("facing", 0, 1);

    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder){
        builder.add(TICKS);
        builder.add(FACING);
    }

    private static int iHandCrankTickRate = 3;
    private static int iHandCrankDelayBeforeReset = 15;
    public static float fHandCrankBaseHeight = 0.25F;
    private final int iHandCrankShaftTextureIndex = 28;
    private final int iHandCrankBaseTopTextureIndex = 29;
    private final int iHandCrankBaseSideTextureIndex = 30;
    private final int iHandCrankBaseBottomTextureIndex = 31;

}
