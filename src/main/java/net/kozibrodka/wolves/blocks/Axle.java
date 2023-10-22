package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBase;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.util.Random;


public class Axle extends TemplateBlockBase
{
    public Axle(Identifier iid)
    {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSounds(WOOD_SOUNDS);
    }

    public int getTextureForSide(int iSide)
    {
        if(iSide == 2 || iSide == 3)
        {
            return TextureListener.axle_side;
        }
        if(iSide == 0 || iSide == 1)
        {
            return TextureListener.axle_vertical;
        } else
        {
            return TextureListener.axle_horizontal;
        }
    }

    public int getTextureForSide(BlockView iblockaccess, int i, int j, int k, int iSide) //getBlockTexture
    {
        int iAxis = GetAxisAlignment(iblockaccess, i, j, k);
        if(iAxis == 0)
        {
            if(iSide == 0 || iSide == 1)
            {
                return TextureListener.axle_side;
            } else
            {
                return TextureListener.axle_vertical;
            }
        }
        if(iAxis == 1)
        {
            if(iSide == 2 || iSide == 3)
            {
                return TextureListener.axle_side;
            }
            if(iSide == 0 || iSide == 1)
            {
                return TextureListener.axle_vertical;
            } else
            {
                return TextureListener.axle_horizontal;
            }
        }
        if(iSide == 4 || iSide == 5)
        {
            return TextureListener.axle_side;
        } else
        {
            return TextureListener.axle_horizontal;
        }
    }

    public int getTickrate() //tickRate
    {
        return 1;
    }

    public boolean isFullOpaque() //isOpaqueCube
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    } //renderAsNormalBlock

    public void onBlockPlaced(Level world, int i, int j, int k, int iFacing) //onBlockPlaced
    {
        SetAxisAlignmentBasedOnFacing(world, i, j, k, iFacing);
    }

    public void onBlockPlaced(Level world, int i, int j, int k) //onBlockAdded
    {
        super.onBlockPlaced(world, i, j, k);
        SetPowerLevel(world, i, j, k, 0);
        world.method_216(i, j, k, id, getTickrate());
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
        ValidatePowerLevel(world, i, j, k);
    }

    public Box getCollisionShape(Level world, int i, int j, int k) //getCollisionBoundingBoxFromPool
    {
        int iAxis = GetAxisAlignment(world, i, j, k);
        switch(iAxis)
        {
        case 0: // '\0'
            return Box.createButWasteMemory(((float)i + 0.5F) - 0.125F, (float)j, ((float)k + 0.5F) - 0.125F, (float)i + 0.5F + 0.125F, (float)j + 1.0F, (float)k + 0.5F + 0.125F);

        case 1: // '\001'
            return Box.createButWasteMemory(((float)i + 0.5F) - 0.125F, ((float)j + 0.5F) - 0.125F, (float)k, (float)i + 0.5F + 0.125F, (float)j + 0.5F + 0.125F, (float)k + 1.0F);
        }
        return Box.createButWasteMemory((float)i, ((float)j + 0.5F) - 0.125F, ((float)k + 0.5F) - 0.125F, (float)i + 1.0F, (float)j + 0.5F + 0.125F, (float)k + 0.5F + 0.125F);
    }

    public void updateBoundingBox(BlockView iBlockAccess, int i, int j, int k) //setBlockBoundsBasedOnState
    {
        int iAxis = GetAxisAlignment(iBlockAccess, i, j, k);
        switch(iAxis)
        {
        case 0: // '\0'
            setBoundingBox(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
            break;

        case 1: // '\001'
            setBoundingBox(0.375F, 0.375F, 0.0F, 0.625F, 0.625F, 1.0F);
            break;

        default:
            setBoundingBox(0.0F, 0.375F, 0.375F, 1.0F, 0.625F, 0.625F);
            break;
        }
    }

    /**
     * chyba render w inventory
     */
    public void method_1605() //setBlockBoundsForItemRender
    {
        setBoundingBox(0.375F, 0.375F, 0.0F, 0.625F, 0.625F, 1.0F);
    }

    public void onAdjacentBlockUpdate(Level world, int i, int j, int k, int iid) //onNeighborBlockChange
    {
        ValidatePowerLevel(world, i, j, k);
    }

    public void randomDisplayTick(Level world, int i, int j, int k, Random random)
    {
        if(GetPowerLevel(world, i, j, k) > 0)
        {
            EmitAxleParticles(world, i, j, k, random);
        }
    }

    public int GetAxisAlignment(BlockView iBlockAccess, int i, int j, int k)
    {
        return iBlockAccess.getTileMeta(i, j, k) >> 2;
    }

    public void SetAxisAlignmentBasedOnFacing(Level world, int i, int j, int k, int iFacing)
    {
        int iAxis;
        switch(iFacing)
        {
            case 0: // '\0'
            case 1: // '\001'
                iAxis = 0;
                break;

            case 2: // '\002'
            case 3: // '\003'
                iAxis = 1;
                break;

            default:
                iAxis = 2;
                break;
        }
        int iMetaData = world.getTileMeta(i, j, k) & 3;
        iMetaData |= iAxis << 2;
        world.setTileMeta(i, j, k, iMetaData);
    }

    public int GetPowerLevel(BlockView iBlockAccess, int i, int j, int k)
    {
        return iBlockAccess.getTileMeta(i, j, k) & 3;
    }

    public void SetPowerLevel(Level world, int i, int j, int k, int iPowerLevel)
    {
        iPowerLevel &= 3;
        int iMetaData = world.getTileMeta(i, j, k) & 0xc;
        iMetaData |= iPowerLevel;
        world.setTileMeta(i, j, k, iMetaData);
        world.method_243(i, j, k);
    }

    public boolean IsAxleOrientedTowardsFacing(BlockView iBlockAccess, int i, int j, int k, int iFacing)
    {
        int iAxis = GetAxisAlignment(iBlockAccess, i, j, k);
        switch(iAxis)
        {
        case 0: // '\0'
            if(iFacing == 0 || iFacing == 1)
            {
                return true;
            }
            break;

        case 1: // '\001'
            if(iFacing == 2 || iFacing == 3)
            {
                return true;
            }
            break;

        default:
            if(iFacing == 4 || iFacing == 5)
            {
                return true;
            }
            break;
        }
        return false;
    }

    public void PropagatePowerFromSource(Level world, int i, int j, int k, boolean bPowered)
    {
        int iSourceAxis = GetAxisAlignment(world, i, j, k);
        BlockPosition tempPos = new BlockPosition(i, j, k);
        int iTempPowerLevel = 0;
        if(bPowered)
        {
            iTempPowerLevel = 3;
        }
        BlockPosition offsetPos = new BlockPosition();
        switch(iSourceAxis)
        {
        case 0: // '\0'
            offsetPos.AddFacingAsOffset(0);
            break;

        case 1: // '\001'
            offsetPos.AddFacingAsOffset(2);
            break;

        default:
            offsetPos.AddFacingAsOffset(4);
            break;
        }
        for(int tempCount = 0; tempCount < 2; tempCount++)
        {
            do
            {
                int iTempid = world.getTileId(tempPos.i, tempPos.j, tempPos.k);
                if(iTempid != id)
                {
                    break;
                }
                int iTempAxis = GetAxisAlignment(world, tempPos.i, tempPos.j, tempPos.k);
                if(iTempAxis != iSourceAxis)
                {
                    break;
                }
                if(bPowered && iTempPowerLevel <= 0)
                {
                    BreakAxle(world, tempPos.i, tempPos.j, tempPos.k);
                    break;
                }
                SetPowerLevel(world, tempPos.i, tempPos.j, tempPos.k, iTempPowerLevel);
                tempPos.AddPos(offsetPos);
                if(iTempPowerLevel > 0)
                {
                    iTempPowerLevel--;
                }
            } while(true);
            if(tempCount != 0)
            {
                continue;
            }
            offsetPos.Invert();
            tempPos.i = i + offsetPos.i;
            tempPos.j = j + offsetPos.j;
            tempPos.k = k + offsetPos.k;
            iTempPowerLevel = 0;
            if(bPowered)
            {
                iTempPowerLevel = 2;
            }
        }

    }

    public void BreakAxle(Level world, int i, int j, int k)
    {
        if(world.getTileId(i, j, k) == id)
        {
            for(int iTemp = 0; iTemp < 5; iTemp++)
            {
                UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, ItemListener.hempFibers.id, 0);
            }

            for(int iTemp = 0; iTemp < 2; iTemp++)
            {
                UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, ItemBase.stick.id, 0);
            }

            world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.2F, 1.25F);
            world.setTile(i, j, k, 0);
        }
    }

    private void ValidatePowerLevel(Level world, int i, int j, int k)
    {
        int iCurrentPower = GetPowerLevel(world, i, j, k);
        int iAxis = GetAxisAlignment(world, i, j, k);
        if(iCurrentPower != 3)
        {
            BlockPosition potentialSources[] = new BlockPosition[2];
            potentialSources[0] = new BlockPosition(i, j, k);
            potentialSources[1] = new BlockPosition(i, j, k);
            switch(iAxis)
            {
            case 0: // '\0'
                potentialSources[0].AddFacingAsOffset(0);
                potentialSources[1].AddFacingAsOffset(1);
                break;

            case 1: // '\001'
                potentialSources[0].AddFacingAsOffset(2);
                potentialSources[1].AddFacingAsOffset(3);
                break;

            default:
                potentialSources[0].AddFacingAsOffset(4);
                potentialSources[1].AddFacingAsOffset(5);
                break;
            }
            int iMaxNeighborPower = 0;
            int iGreaterPowerNeighbors = 0;
            for(int tempSource = 0; tempSource < 2; tempSource++)
            {
                int iTempid = world.getTileId(potentialSources[tempSource].i, potentialSources[tempSource].j, potentialSources[tempSource].k);
                if(iTempid != id)
                {
                    continue;
                }
                int iTempAxis = GetAxisAlignment(world, potentialSources[tempSource].i, potentialSources[tempSource].j, potentialSources[tempSource].k);
                if(iTempAxis != iAxis)
                {
                    continue;
                }
                int iTempPowerLevel = GetPowerLevel(world, potentialSources[tempSource].i, potentialSources[tempSource].j, potentialSources[tempSource].k);
                if(iTempPowerLevel > iMaxNeighborPower)
                {
                    iMaxNeighborPower = iTempPowerLevel;
                }
                if(iTempPowerLevel > iCurrentPower)
                {
                    iGreaterPowerNeighbors++;
                }
            }

            if(iGreaterPowerNeighbors >= 2)
            {
                BreakAxle(world, i, j, k);
                return;
            }
            int iNewPower = iCurrentPower;
            if(iMaxNeighborPower > iCurrentPower)
            {
                if(iMaxNeighborPower == 1)
                {
                    BreakAxle(world, i, j, k);
                    return;
                }
                iNewPower = iMaxNeighborPower - 1;
            } else
            {
                iNewPower = 0;
            }
            if(iNewPower != iCurrentPower)
            {
//                System.out.println("ZNIAMIA");
                SetPowerLevel(world, i, j, k, iNewPower);
            }
        }
    }

    private void EmitAxleParticles(Level world, int i, int j, int k, Random random)
    {
        for(int counter = 0; counter < 2; counter++)
        {
            float smokeX = (float)i + random.nextFloat();
            float smokeY = (float)j + random.nextFloat() * 0.5F + 0.625F;
            float smokeZ = (float)k + random.nextFloat();
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

    }

    public void Overpower(Level world, int i, int j, int k)
    {
        int iCurrentPower = GetPowerLevel(world, i, j, k);
        int iAxis = GetAxisAlignment(world, i, j, k);
        BlockPosition potentialSources[] = new BlockPosition[2];
        potentialSources[0] = new BlockPosition(i, j, k);
        potentialSources[1] = new BlockPosition(i, j, k);
        switch(iAxis)
        {
        case 0: // '\0'
            potentialSources[0].AddFacingAsOffset(0);
            potentialSources[1].AddFacingAsOffset(1);
            break;

        case 1: // '\001'
            potentialSources[0].AddFacingAsOffset(2);
            potentialSources[1].AddFacingAsOffset(3);
            break;

        default:
            potentialSources[0].AddFacingAsOffset(4);
            potentialSources[1].AddFacingAsOffset(5);
            break;
        }
        for(int tempSource = 0; tempSource < 2; tempSource++)
        {
            int iTempid = world.getTileId(potentialSources[tempSource].i, potentialSources[tempSource].j, potentialSources[tempSource].k);
            if(iTempid == id)
            {
                int iTempAxis = GetAxisAlignment(world, potentialSources[tempSource].i, potentialSources[tempSource].j, potentialSources[tempSource].k);
                if(iTempAxis != iAxis)
                {
                    continue;
                }
                int iTempPowerLevel = GetPowerLevel(world, potentialSources[tempSource].i, potentialSources[tempSource].j, potentialSources[tempSource].k);
                if(iTempPowerLevel < iCurrentPower)
                {
                    Overpower(world, potentialSources[tempSource].i, potentialSources[tempSource].j, potentialSources[tempSource].k);
                }
                continue;
            }
            if(iTempid == BlockListener.gearBox.id)
            {
                ((GearBox)BlockListener.gearBox).Overpower(world, potentialSources[tempSource].i, potentialSources[tempSource].j, potentialSources[tempSource].k);
            }
        }

    }

}
