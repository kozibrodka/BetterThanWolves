
package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.mixin.LevelAccessor;
import net.kozibrodka.wolves.api.AffectedByBellows;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.MechanicalDevice;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Random;

public class BellowsBlock extends TemplateBlock
    implements MechanicalDevice, RotatableBlock
{

    public BellowsBlock(Identifier iid)
    {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSoundGroup(WOOD_SOUND_GROUP);
    }

    public int getTextureId(BlockView blockAccess, int i, int j, int k, int iSide)
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

    public int getTexture(int iSide)
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

    public void setupRenderBoundingBox()
    {
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.6875F, 1.0F);
    }

    public int getTickRate()
    {
        return m_iBellowsTickRate;
    }

    public void onPlaced(World world, int i, int j, int k, int iFacing)
    {
        if(iFacing < 2)
        {
            iFacing = 2;
        }
        SetFacing(world, i, j, k, iFacing);
    }

    public void onPlaced(World world, int i, int j, int k, LivingEntity entityLiving)
    {
        int iFacing = UnsortedUtils.ConvertPlacingEntityOrientationToFlatBlockFacing(entityLiving);
        SetFacing(world, i, j, k, iFacing);
    }

    public void onPlaced(World world, int i, int j, int k)
    {
        super.onPlaced(world, i, j, k);
        world.scheduleBlockUpdate(i, j, k, BlockListener.bellows.id, getTickRate());
    }

    public boolean isOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public Box getCollisionShape(World world, int i, int j, int k)
    {
        if(IsBlockMechanicalOn(world, i, j, k))
        {
            return Box.createCached((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + 0.6875F, (float)k + 1.0F);
        } else
        {
            return Box.createCached((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);
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

    public void neighborUpdate(World world, int i, int j, int k, int iid)
    {
        world.scheduleBlockUpdate(i, j, k, BlockListener.bellows.id, getTickRate());
    }

    public void onTick(World world, int i, int j, int k, Random random)
    {
        boolean bReceivingMechanicalPower = IsInputtingMechanicalPower(world, i, j, k);
        boolean bMechanicalOn = IsBlockMechanicalOn(world, i, j, k);
        if(bMechanicalOn != bReceivingMechanicalPower)
        {
            if(bReceivingMechanicalPower)
            {
                Blow(world, i, j, k);
                world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "fire.ignite", 0.5F, world.random.nextFloat() * 0.4F + 2.0F);
                if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                    voicePacket(world, "fire.ignite", i, j, k, 0.5F, world.random.nextFloat() * 0.4F + 2.0F);
                }
            } else
            {
                LiftCollidingEntities(world, i, j, k);
                world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "fire.ignite", 0.5F, world.random.nextFloat() * 0.4F + 2.0F);
                if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                    voicePacket(world, "fire.ignite", i, j, k, 0.5F, world.random.nextFloat() * 0.4F + 2.0F);
                }
            }
            SetBlockMechanicalOn(world, i, j, k, bReceivingMechanicalPower);
            world.setBlocksDirty(i, j, k, i, j, k);
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

    public int GetFacing(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getBlockMeta(i, j, k) & 3) + 2;
    }

    public void SetFacing(World world, int i, int j, int k, int iFacing)
    {
        int iMetaData = world.getBlockMeta(i, j, k) & -4;
        if(iFacing >= 2)
        {
            iFacing -= 2;
        } else
        {
            iFacing = 0;
        }
        iMetaData |= iFacing;
        world.setBlockMeta(i, j, k, iMetaData);
    }


    public boolean CanRotate(BlockView iBlockAccess, int i, int j, int l)
    {
        return true;
    }

    public boolean CanTransmitRotation(BlockView iBlockAccess, int i, int j, int l)
    {
        return false;
    }

    public void Rotate(World world, int i, int j, int k, boolean bReverse)
    {
        int iFacing = GetFacing(world, i, j, k);
        int iNewFacing = UnsortedUtils.RotateFacingAroundJ(iFacing, bReverse);
        if(iNewFacing != iFacing)
        {
            SetFacing(world, i, j, k, iNewFacing);
            world.setBlocksDirty(i, j, k, i, j, k);
            world.scheduleBlockUpdate(i, j, k, BlockListener.bellows.id, getTickRate());
            ((LevelAccessor) world).invokeBlockUpdate(i, j, k, BlockListener.bellows.id);
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

    public boolean IsInputtingMechanicalPower(World world, int i, int j, int k)
    {
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

        int iSawFacing = GetFacing(world, i, j, k);
        for(int iFacing = 0; iFacing <= 5; iFacing++)
        {
            if(iFacing == iSawFacing || iFacing == 1)
            {
                continue;
            }
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

        return false;
    }

    public boolean IsOutputtingMechanicalPower(World world, int i, int j, int l)
    {
        return false;
    }

    public boolean IsBlockMechanicalOn(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getBlockMeta(i, j, k) & 4) > 0;
    }

    public void SetBlockMechanicalOn(World world, int i, int j, int k, boolean bOn)
    {
        int iMetaData = world.getBlockMeta(i, j, k) & -5;
        if(bOn)
        {
            iMetaData |= 4;
        }
        world.setBlockMeta(i, j, k, iMetaData);
        world.blockUpdateEvent(i, j, k);
    }

    private void EmitBellowsParticles(World world, int i, int j, int k, Random random)
    {
        for(int counter = 0; counter < 10; counter++)
        {
            float smokeX = (float)i + random.nextFloat();
            float smokeY = (float)j + random.nextFloat() * 0.5F;
            float smokeZ = (float)k + random.nextFloat();
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

    }

    private void Blow(World world, int i, int j, int k)
    {
        int iFacing = GetFacing(world, i, j, k);
        int iFacingSide1 = UnsortedUtils.RotateFacingAroundJ(iFacing, false);
        int iFacingSide2 = UnsortedUtils.RotateFacingAroundJ(iFacing, true);
        BlockPosition particlePos = new BlockPosition(i, j, k);
        particlePos.AddFacingAsOffset(iFacing);
        EmitBellowsParticles(world, particlePos.i, particlePos.j, particlePos.k, world.random);
        BlockPosition tempTargetPos = new BlockPosition(i, j, k);
        Block blockWithInterface;
        for(int iTempCount = 0; iTempCount < 3; iTempCount++)
        {
            tempTargetPos.AddFacingAsOffset(iFacing);
            int tempid = world.getBlockId(tempTargetPos.i, tempTargetPos.j, tempTargetPos.k);
            blockWithInterface = Block.BLOCKS[tempid];
            if (blockWithInterface != null) {
                if (blockWithInterface instanceof AffectedByBellows)
                {
                    ((AffectedByBellows) blockWithInterface).affectBlock(world, tempTargetPos.i, tempTargetPos.j, tempTargetPos.k, tempTargetPos, iFacing);
                    continue;
                }
            }
            if(tempid == Block.FIRE.id || tempid == BlockListener.stokedFire.id)
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
            tempid = world.getBlockId(tempSidePos1.i, tempSidePos1.j, tempSidePos1.k);
            if((tempid == Block.FIRE.id || tempid == BlockListener.stokedFire.id)) // && world.getTileId(tempSidePos1.i, tempSidePos1.j, tempSidePos1.k) == BlockBase.FIRE.id
            {
                StokeFire(world, tempSidePos1.i, tempSidePos1.j, tempSidePos1.k);
            }
            BlockPosition tempSidePos2 = new BlockPosition(tempTargetPos.i, tempTargetPos.j, tempTargetPos.k);
            tempSidePos2.AddFacingAsOffset(iFacingSide2);
            tempid = world.getBlockId(tempSidePos2.i, tempSidePos2.j, tempSidePos2.k);
            if(tempid == Block.FIRE.id || tempid == BlockListener.stokedFire.id)
            {
                StokeFire(world, tempSidePos2.i, tempSidePos2.j, tempSidePos2.k);
            }
        }

    }

    private void StokeFire(World world, int i, int j, int k)
    {
    	if(world.getBlockId(i, j - 1, k) == BlockListener.hibachi.id)
        {
            if(world.getBlockId(i, j, k) == BlockListener.stokedFire.id)
            {
                world.setBlockMeta(i, j, k, 0);
                world.blockUpdateEvent(i, j, k);
            } else
            {
                world.setBlock(i, j, k, BlockListener.stokedFire.id);
                world.blockUpdateEvent(i, j, k);
            }
            if(world.isAir(i, j + 1, k))
            {
                world.setBlock(i, j + 1, k, BlockListener.stokedFire.id);
                world.blockUpdateEvent(i, j, k);
            }
            if(world.getBlockId(i, j + 1, k) == BlockListener.stokedFire.id)
            {
                world.setBlockMeta(i, j + 1, k, 0);
                world.blockUpdateEvent(i, j, k);
            }
        } else
        {
            world.setBlock(i, j, k, 0);
            world.blockUpdateEvent(i, j, k);
        }
    }

    private void LiftCollidingEntities(World world, int i, int j, int k)
    {
        List list = world.getEntities((Entity) null, Box.createCached((float)i, (float)j + 0.6875F, (float)k, (float)(i + 1), (float)(j + 1), (float)(k + 1)));
        float extendedMaxY = j + 1;
        if(list != null && list.size() > 0)
        {
            for(int j1 = 0; j1 < list.size(); j1++)
            {
                Entity tempEntity = (Entity) list.get(j1);
                if(tempEntity.dead || !tempEntity.isPushable() && !(tempEntity instanceof ItemEntity))
                {
                    continue;
                }
                double tempEntityMinY = tempEntity.boundingBox.minY;
                if(tempEntityMinY < (double)extendedMaxY)
                {
                    double entityYOffset = (double)extendedMaxY - tempEntityMinY;
                    tempEntity.setPos(tempEntity.x, tempEntity.y + entityYOffset, tempEntity.z);
                }
            }

        }
    }

    private static int m_iBellowsTickRate = 10;

}
