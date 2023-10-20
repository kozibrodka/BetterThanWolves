
package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.mixin.LevelAccessor;
import net.kozibrodka.wolves.modsupport.AffectedByBellows;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.MechanicalDevice;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Item;
import net.minecraft.entity.Living;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.util.List;
import java.util.Random;

public class Bellows extends TemplateBlockBase
    implements MechanicalDevice, RotatableBlock
{

    public Bellows(Identifier iid)
    {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSounds(WOOD_SOUNDS);
    }

    public int getTextureForSide(BlockView blockAccess, int i, int j, int k, int iSide)
    {
        int iFacing = GetFacing(blockAccess, i, j, k);
        if(iSide == iFacing)
        {
            return TextureListener.bellows_front;
        }
        if(iSide == 1)
        {
            return TextureListener.bellows_top;
        }
        return iSide != 0 ? TextureListener.bellows_side : TextureListener.bellows_top;
    }

    public int getTextureForSide(int iSide)
    {
        if(iSide == 3)
        {
            return TextureListener.bellows_front;
        }
        if(iSide == 1)
        {
            return TextureListener.bellows_top;
        }
        return iSide != 0 ? TextureListener.bellows_side : TextureListener.bellows_top;
    }

    public void method_1605()
    {
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.6875F, 1.0F);
    }

    public int getTickrate()
    {
        return m_iBellowsTickRate;
    }

    public void onBlockPlaced(Level world, int i, int j, int k, int iFacing)
    {
        if(iFacing < 2)
        {
            iFacing = 2;
        }
        SetFacing(world, i, j, k, iFacing);
    }

    public void afterPlaced(Level world, int i, int j, int k, Living entityLiving)
    {
        int iFacing = UnsortedUtils.ConvertPlacingEntityOrientationToFlatBlockFacing(entityLiving);
        SetFacing(world, i, j, k, iFacing);
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

    public Box getCollisionShape(Level world, int i, int j, int k)
    {
        if(IsBlockMechanicalOn(world, i, j, k))
        {
            return Box.createButWasteMemory((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + 0.6875F, (float)k + 1.0F);
        } else
        {
            return Box.createButWasteMemory((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);
        }
    }

    public void updateBoundingBox(BlockView iBlockAccess, int i, int j, int k)
    {
        if(IsBlockMechanicalOn(iBlockAccess, i, j, k))
        {
            setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.6875F, 1.0F);
        } else
        {
            setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public void onAdjacentBlockUpdate(Level world, int i, int j, int k, int iid)
    {
        world.method_216(i, j, k, id, getTickrate());
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
        boolean bReceivingMechanicalPower = IsInputtingMechanicalPower(world, i, j, k);
        boolean bMechanicalOn = IsBlockMechanicalOn(world, i, j, k);
        if(bMechanicalOn != bReceivingMechanicalPower)
        {
            if(bReceivingMechanicalPower)
            {
                Blow(world, i, j, k);
                world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "fire.ignite", 0.5F, world.rand.nextFloat() * 0.4F + 2.0F);
            } else
            {
                LiftCollidingEntities(world, i, j, k);
                world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "fire.ignite", 0.5F, world.rand.nextFloat() * 0.4F + 2.0F);
            }
            SetBlockMechanicalOn(world, i, j, k, bReceivingMechanicalPower);
            world.method_202(i, j, k, i, j, k);
        }
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getTileMeta(i, j, k) & 3) + 2;
    }

    public void SetFacing(Level world, int i, int j, int k, int iFacing)
    {
        int iMetaData = world.getTileMeta(i, j, k) & -4;
        if(iFacing >= 2)
        {
            iFacing -= 2;
        } else
        {
            iFacing = 0;
        }
        iMetaData |= iFacing;
        world.setTileMeta(i, j, k, iMetaData);
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

        int iSawFacing = GetFacing(world, i, j, k);
        for(int iFacing = 0; iFacing <= 5; iFacing++)
        {
            if(iFacing == iSawFacing || iFacing == 1)
            {
                continue;
            }
            BlockPosition targetPos = new BlockPosition(i, j, k);
            targetPos.AddFacingAsOffset(iFacing);
            int iTargetid = world.getTileId(targetPos.i, targetPos.j, targetPos.k);
            if(iTargetid != BlockListener.fcAxleBlock.id)
            {
                continue;
            }
            Axle axleBlock = (Axle)BlockListener.fcAxleBlock;
            if(axleBlock.IsAxleOrientedTowardsFacing(world, targetPos.i, targetPos.j, targetPos.k, iFacing) && axleBlock.GetPowerLevel(world, targetPos.i, targetPos.j, targetPos.k) > 0)
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

    public boolean IsBlockMechanicalOn(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getTileMeta(i, j, k) & 4) > 0;
    }

    public void SetBlockMechanicalOn(Level world, int i, int j, int k, boolean bOn)
    {
        int iMetaData = world.getTileMeta(i, j, k) & -5;
        if(bOn)
        {
            iMetaData |= 4;
        }
        world.setTileMeta(i, j, k, iMetaData);
        world.method_243(i, j, k);
    }

    private void EmitBellowsParticles(Level world, int i, int j, int k, Random random)
    {
        for(int counter = 0; counter < 10; counter++)
        {
            float smokeX = (float)i + random.nextFloat();
            float smokeY = (float)j + random.nextFloat() * 0.5F;
            float smokeZ = (float)k + random.nextFloat();
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

    }

    private void Blow(Level world, int i, int j, int k)
    {
        int iFacing = GetFacing(world, i, j, k);
        int iFacingSide1 = UnsortedUtils.RotateFacingAroundJ(iFacing, false);
        int iFacingSide2 = UnsortedUtils.RotateFacingAroundJ(iFacing, true);
        BlockPosition particlePos = new BlockPosition(i, j, k);
        particlePos.AddFacingAsOffset(iFacing);
        EmitBellowsParticles(world, particlePos.i, particlePos.j, particlePos.k, world.rand);
        BlockPosition tempTargetPos = new BlockPosition(i, j, k);
        BlockBase blockWithInterface;
        for(int iTempCount = 0; iTempCount < 3; iTempCount++)
        {
            tempTargetPos.AddFacingAsOffset(iFacing);
            int tempid = world.getTileId(tempTargetPos.i, tempTargetPos.j, tempTargetPos.k);
            blockWithInterface = BlockBase.BY_ID[tempid];
            if (blockWithInterface != null) {
                if (blockWithInterface instanceof AffectedByBellows)
                {
                    ((AffectedByBellows) blockWithInterface).affectBlock(world, tempTargetPos.i, tempTargetPos.j, tempTargetPos.k, tempTargetPos, iFacing);
                    continue;
                }
            }
            if(tempid == BlockBase.FIRE.id || tempid == BlockListener.fcStokedFire.id)
            {
                StokeFire(world, tempTargetPos.i, tempTargetPos.j, tempTargetPos.k);
            }
            else
            if(!world.isAir(tempTargetPos.i, tempTargetPos.j, tempTargetPos.k))
            {
                break;
            }
            BlockPosition tempSidePos1 = new BlockPosition(tempTargetPos.i, tempTargetPos.j, tempTargetPos.k);
            tempSidePos1.AddFacingAsOffset(iFacingSide1);
            tempid = world.getTileId(tempSidePos1.i, tempSidePos1.j, tempSidePos1.k);
            if((tempid == BlockBase.FIRE.id || tempid == BlockListener.fcStokedFire.id)) // && world.getTileId(tempSidePos1.i, tempSidePos1.j, tempSidePos1.k) == BlockBase.FIRE.id
            {
                StokeFire(world, tempSidePos1.i, tempSidePos1.j, tempSidePos1.k);
            }
            BlockPosition tempSidePos2 = new BlockPosition(tempTargetPos.i, tempTargetPos.j, tempTargetPos.k);
            tempSidePos2.AddFacingAsOffset(iFacingSide2);
            tempid = world.getTileId(tempSidePos2.i, tempSidePos2.j, tempSidePos2.k);
            if(tempid == BlockBase.FIRE.id || tempid == BlockListener.fcStokedFire.id)
            {
                StokeFire(world, tempSidePos2.i, tempSidePos2.j, tempSidePos2.k);
            }
        }

    }

    private void StokeFire(Level world, int i, int j, int k)
    {
    	if(world.getTileId(i, j - 1, k) == BlockListener.fcBBQ.id)
        {
            if(world.getTileId(i, j, k) == BlockListener.fcStokedFire.id)
            {
                world.setTileMeta(i, j, k, 0);
                world.method_243(i, j, k);
            } else
            {
                world.setTile(i, j, k, BlockListener.fcStokedFire.id);
                world.method_243(i, j, k);
            }
            if(world.isAir(i, j + 1, k))
            {
                world.setTile(i, j + 1, k, BlockListener.fcStokedFire.id);
                world.method_243(i, j, k);
            }
            if(world.getTileId(i, j + 1, k) == BlockListener.fcStokedFire.id)
            {
                world.setTileMeta(i, j + 1, k, 0);
                world.method_243(i, j, k);
            }
        } else
        {
            world.setTile(i, j, k, 0);
            world.method_243(i, j, k);
        }
    }

    private void LiftCollidingEntities(Level world, int i, int j, int k)
    {
        List list = world.getEntities((EntityBase) null, Box.createButWasteMemory((float)i, (float)j + 0.6875F, (float)k, (float)(i + 1), (float)(j + 1), (float)(k + 1)));
        float extendedMaxY = j + 1;
        if(list != null && list.size() > 0)
        {
            for(int j1 = 0; j1 < list.size(); j1++)
            {
                EntityBase tempEntity = (EntityBase) list.get(j1);
                if(tempEntity.removed || !tempEntity.method_1380() && !(tempEntity instanceof Item))
                {
                    continue;
                }
                double tempEntityMinY = tempEntity.boundingBox.minY;
                if(tempEntityMinY < (double)extendedMaxY)
                {
                    double entityYOffset = (double)extendedMaxY - tempEntityMinY;
                    tempEntity.setPosition(tempEntity.x, tempEntity.y + entityYOffset, tempEntity.z);
                }
            }

        }
    }

    private static int m_iBellowsTickRate = 10;

}
