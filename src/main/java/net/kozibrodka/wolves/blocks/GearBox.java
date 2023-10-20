
package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.mixin.LevelAccessor;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.MechanicalDevice;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemBase;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.util.Random;


public class GearBox extends TemplateBlockBase
    implements MechanicalDevice, RotatableBlock
{

    public GearBox(Identifier iid)
    {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSounds(WOOD_SOUNDS);
    }

    public int getTextureForSide(BlockView iblockaccess, int i, int j, int k, int iSide)
    {
        int iFacing = GetFacing(iblockaccess, i, j, k);
        if(iSide == iFacing)
        {
            return TextureListener.gearbox_front;
        }
        BlockPosition sideBlockPos = new BlockPosition(i, j, k);
        sideBlockPos.AddFacingAsOffset(iSide);
        if(iblockaccess.getTileId(sideBlockPos.i, sideBlockPos.j, sideBlockPos.k) == mod_FCBetterThanWolves.fcAxleBlock.id && ((Axle)mod_FCBetterThanWolves.fcAxleBlock).IsAxleOrientedTowardsFacing(iblockaccess, sideBlockPos.i, sideBlockPos.j, sideBlockPos.k, iSide))
        {
            return TextureListener.gearbox_output;
        } else
        {
            return TextureListener.gearbox_side;
        }
    }

    public int getTextureForSide(int iSide)
    {
        if(iSide == 3)
        {
            return TextureListener.gearbox_front;
        } else
        {
            return TextureListener.gearbox_side;
        }
    }

    public int getTickrate()
    {
        return 10;
    }

    public void onBlockPlaced(Level world, int i, int j, int k, int iFacing)
    {
        SetFacing(world, i, j, k, UnsortedUtils.GetOppositeFacing(iFacing));
    }

    public void afterPlaced(Level world, int i, int j, int k, Living entityLiving) //onBlockPlacedBy
    {
        int iFacing = UnsortedUtils.ConvertPlacingEntityOrientationToBlockFacing(entityLiving);
        if(mod_FCBetterThanWolves.fcFaceGearBoxAwayFromPlayer)
        {
            iFacing = UnsortedUtils.GetOppositeFacing(iFacing);
        }
        SetFacing(world, i, j, k, iFacing);
    }

    public void onBlockPlaced(Level world, int i, int j, int k)  //TODO był błąd LOL czy coś to zmieni?
    {
        super.onBlockPlaced(world, i, j, k);
        world.method_216(i, j, k, id, getTickrate());
    }

    public void onBlockRemoved(Level world, int i, int j, int k)
    {
        if(IsGearBoxOn(world, i, j, k))
        {
            SetGearBoxOnState(world, i, j, k, false);
            ValidateOutputs(world, i, j, k, false);
        }
        super.onBlockRemoved(world, i, j, k);
    }

    public void onAdjacentBlockUpdate(Level world, int i, int j, int k, int iid)
    {
        world.method_216(i, j, k, id, getTickrate());
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
        boolean bReceivingPower = IsInputtingMechanicalPower(world, i, j, k);
        boolean bOn = IsGearBoxOn(world, i, j, k);
        boolean bIsRedstonePowered = world.method_263(i, j, k) || world.method_263(i, j + 1, k);
        if(bIsRedstonePowered)
        {
            bReceivingPower = false;
        }
        if(bOn != bReceivingPower)
        {
            if(bOn)
            {
//                System.out.println("ustawiam false: " + bReceivingPower + bOn);
                SetGearBoxOnState(world, i, j, k, false);
                ValidateOutputs(world, i, j, k, false);
            } else
            {
                world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.05F, 1.0F);
                EmitGearBoxParticles(world, i, j, k, random);
//                System.out.println("ustawiam true: " + bReceivingPower + bOn);
                SetGearBoxOnState(world, i, j, k, true);
                ValidateOutputs(world, i, j, k, true);
            }
        } else
        {
            ValidateOutputs(world, i, j, k, false);
        }
    }

    public void randomDisplayTick(Level world, int i, int j, int k, Random random)
    {
        if(IsGearBoxOn(world, i, j, k))
        {
            EmitGearBoxParticles(world, i, j, k, random);
        }
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int k)
    {
        return iBlockAccess.getTileMeta(i, j, k) & 7;
    }

    public void SetFacing(Level world, int i, int j, int k, int iFacing)
    {
        int iMetaData = world.getTileMeta(i, j, k) & 8;
        iMetaData |= iFacing;
        world.setTileMeta(i, j, k, iMetaData);
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
        System.out.println("METODA ROTATE W GEARBOX");
        int iFacing = GetFacing(world, i, j, k);
        int iNewFacing = UnsortedUtils.RotateFacingAroundJ(iFacing, bReverse);
        if(iNewFacing != iFacing)
        {
            SetFacing(world, i, j, k, iNewFacing);
            world.method_202(i, j, k, i, j, k);
            world.method_216(i, j, k, id, getTickrate());
            ((LevelAccessor) world).invokeMethod_235(i, j, k, id);
        }
        UnsortedUtils.DestroyHorizontallyAttachedAxles(world, i, j, k);
    }

    public boolean IsGearBoxOn(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getTileMeta(i, j, k) & 8) > 0;
    }

    public void SetGearBoxOnState(Level world, int i, int j, int k, boolean bOn)
    {
        int iMetaData = world.getTileMeta(i, j, k) & 7;
        if(bOn)
        {
            iMetaData |= 8;
        }
        world.setTileMeta(i, j, k, iMetaData);
        world.method_243(i, j, k);
    }

    void EmitGearBoxParticles(Level world, int i, int j, int k, Random random)
    {
        for(int counter = 0; counter < 5; counter++)
        {
            float smokeX = (float)i + random.nextFloat();
            float smokeY = (float)j + random.nextFloat() * 0.5F + 1.0F;
            float smokeZ = (float)k + random.nextFloat();
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

    }

    private void ValidateOutputs(Level world, int i, int j, int k, boolean bDestroyIfAlreadyPowered)
    {
        int iBlockFacing = GetFacing(world, i, j, k);
        boolean bIsOn = IsGearBoxOn(world, i, j, k);
        for(int iFacing = 0; iFacing <= 5; iFacing++)
        {
            if(iFacing == iBlockFacing)
            {
                continue;
            }
            BlockPosition tempPos = new BlockPosition(i, j, k);
            tempPos.AddFacingAsOffset(iFacing);
            if(world.getTileId(tempPos.i, tempPos.j, tempPos.k) != mod_FCBetterThanWolves.fcAxleBlock.id)
            {
                continue;
            }
            Axle axleBlock = (Axle)mod_FCBetterThanWolves.fcAxleBlock;
            if(!axleBlock.IsAxleOrientedTowardsFacing(world, tempPos.i, tempPos.j, tempPos.k, iFacing))
            {
                continue;
            }
            int tempPowerLevel = axleBlock.GetPowerLevel(world, tempPos.i, tempPos.j, tempPos.k);
            if(tempPowerLevel > 0 && bDestroyIfAlreadyPowered)
            {
                axleBlock.BreakAxle(world, tempPos.i, tempPos.j, tempPos.k);
                continue;
            }
            if(bIsOn)
            {
                if(tempPowerLevel != 3)
                {
                    axleBlock.SetPowerLevel(world, tempPos.i, tempPos.j, tempPos.k, 3);
                }
                continue;
            }
            if(tempPowerLevel != 0)
            {
                axleBlock.SetPowerLevel(world, tempPos.i, tempPos.j, tempPos.k, 0);
            }
        }

    }

    public void Overpower(Level world, int i, int j, int k)
    {
        boolean bIsRedstonePowered = world.method_263(i, j, k) || world.method_263(i, j + 1, k);
        if(!bIsRedstonePowered)
        {
            BreakGearBox(world, i, j, k);
        }
    }

    public void BreakGearBox(Level world, int i, int j, int k)
    {
        for(int iTemp = 0; iTemp < 4; iTemp++)
        {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, BlockBase.WOOD.id, 0);
        }

        for(int iTemp = 0; iTemp < 3; iTemp++)
        {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, mod_FCBetterThanWolves.fcGear.id, 0);
        }

        UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, ItemBase.redstoneDust.id, 0);
        world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.2F, 1.25F);
        world.setTile(i, j, k, 0);
    }

    public boolean CanOutputMechanicalPower()
    {
        return true;
    }

    public boolean CanInputMechanicalPower()
    {
        return true;
    }

    public boolean IsInputtingMechanicalPower(Level world, int i, int j, int k)
    {
        int iFacing = GetFacing(world, i, j, k);
        BlockPosition targetBlockPos = new BlockPosition(i, j, k);
        targetBlockPos.AddFacingAsOffset(iFacing);
        int iTargetid = world.getTileId(targetBlockPos.i, targetBlockPos.j, targetBlockPos.k);
        return iTargetid == mod_FCBetterThanWolves.fcAxleBlock.id && ((Axle)mod_FCBetterThanWolves.fcAxleBlock).IsAxleOrientedTowardsFacing(world, targetBlockPos.i, targetBlockPos.j, targetBlockPos.k, iFacing) && ((Axle)mod_FCBetterThanWolves.fcAxleBlock).GetPowerLevel(world, targetBlockPos.i, targetBlockPos.j, targetBlockPos.k) > 0;
    }

    public boolean IsOutputtingMechanicalPower(Level world, int i, int j, int k)
    {
        return IsGearBoxOn(world, i, j, k);
    }

}
