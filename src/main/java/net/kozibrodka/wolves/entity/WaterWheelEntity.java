package net.kozibrodka.wolves.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.block.AxleBlock;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ConfigListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.network.LoadRotationPacket;
import net.minecraft.block.Block;
import net.minecraft.block.LiquidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.TriState;

@HasTrackingParameters(trackingDistance = 240, updatePeriod = Integer.MAX_VALUE, sendVelocity = TriState.FALSE)
public class WaterWheelEntity extends Entity implements EntitySpawnDataProvider
{

    public WaterWheelEntity(World world)
    {
        super(world);
        fRotation = 0.0F;
        bProvidingPower = false;
        iWaterWheelCurrentDamage = 0;
        iWaterWheelTimeSinceHit = 0;
        iWaterWheelRockDirection = 1;
        fWaterWheelCurrentRotationSpeed = 0.0F;
        iFullUpdateTickCount = 0;
        blocksSameBlockSpawning = true;
        setBoundingBoxSpacing(4.8F, 4.8F);
        standingEyeHeight = height / 2.0F;
    }

    public WaterWheelEntity(World world, double x, double y, double z, boolean flag)
    {
        this(world);
        setPosition(x, y, z);
        bWaterWheelIAligned = flag;
        AlignBoundingBoxWithAxis();
        setAligned(bWaterWheelIAligned);
    }

    public WaterWheelEntity(World level, double x, double y, double z) {
        this(level);
        setPosition(x, y, z);
        AlignBoundingBoxWithAxis();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void processServerEntityStatus(byte status) {
        if (status >= 6 && status < 14) {
            int dmg = status - 6;
            iWaterWheelRockDirection = -iWaterWheelRockDirection;
            iWaterWheelTimeSinceHit = 10;
            scheduleVelocityUpdate();
            iWaterWheelCurrentDamage += dmg * 5;
        }else{
            super.processServerEntityStatus(status);
        }
    }

    private void AlignBoundingBoxWithAxis()
    {
        if(bWaterWheelIAligned)
        {
            boundingBox.set(x - fHalfWheelDepth, y - fHalfWheelWidth, z - fHalfWheelWidth, x + fHalfWheelDepth, y + fHalfWheelWidth, z + fHalfWheelWidth);
        } else
        {
            boundingBox.set(x - fHalfWheelWidth, y - fHalfWheelWidth, z - fHalfWheelDepth, x + fHalfWheelWidth, y + fHalfWheelWidth, z + fHalfWheelDepth);
        }
    }

    @Override
    public float getEyeHeight() {
        return -1.0F;
    }

    @Override
    protected void initDataTracker() {
        dataTracker.startTracking(16, (byte) 0); //Facing
        dataTracker.startTracking(17, (int) 0); //Rotation
    }

    @Override
    protected void writeNbt(NbtCompound nbttagcompound)
    {
        nbttagcompound.putBoolean("bWaterWheelIAligned", bWaterWheelIAligned);
        nbttagcompound.putFloat("fRotation", fRotation);
        nbttagcompound.putBoolean("bProvidingPower", bProvidingPower);
    }

    @Override
    protected void readNbt(NbtCompound nbttagcompound)
    {
        bWaterWheelIAligned = nbttagcompound.getBoolean("bWaterWheelIAligned");
        fRotation = nbttagcompound.getFloat("fRotation");
        bProvidingPower = nbttagcompound.getBoolean("bProvidingPower");
        AlignBoundingBoxWithAxis();
        setAligned(bWaterWheelIAligned);
    }

    @Override
    protected boolean bypassesSteppingEffects()
    {
        return false;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void setPositionAndAnglesAvoidEntities(double x, double y, double z, float pitch, float yaw, int interpolationSteps) {
        this.setPosition(x, y, z);
        this.setRotation(pitch, yaw);
    }

    @Override
    public Box getBoundingBox() {
        return boundingBox;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isCollidable() {
        return !dead;
    }

    @Override
    public boolean damage(Entity entity, int amount)
    {
        if(world.isRemote || dead){
            return false;
        }
        iWaterWheelRockDirection = -iWaterWheelRockDirection;
        iWaterWheelTimeSinceHit = 10;
        scheduleVelocityUpdate();
        iWaterWheelCurrentDamage += amount * 5;
        if(amount < 8 && iWaterWheelCurrentDamage <= 40){
            world.broadcastEntityEvent(this, (byte)(amount+6));
        }
        if(iWaterWheelCurrentDamage > 40)
        {
            destroyWithDrop();
        }
        return true;
    }

    @Override
    public void animateHurt()
    {
        iWaterWheelRockDirection = -iWaterWheelRockDirection;
        iWaterWheelTimeSinceHit = 10;
        iWaterWheelCurrentDamage += iWaterWheelCurrentDamage * 5;
    }

    @Override
    public void markDead()
    {
        if(bProvidingPower)
        {
            int iCenterI = (int)(x - 0.5D);
            int iCenterJ = (int)(y - 0.5D);
            int iCenterK = (int)(z - 0.5D);
            int iCenterid = world.getBlockId(iCenterI, iCenterJ, iCenterK);
            if(iCenterid == BlockListener.axleBlock.id)
            {
                ((AxleBlock)BlockListener.axleBlock).setPowerLevel(world, iCenterI, iCenterJ, iCenterK, 0);
            }
        }
        super.markDead();
    }

    public void destroyWithDrop()
    {
        if(!dead)
        {
            dropItem(ItemListener.waterWheelItem.id, 1, 0.0F);
            markDead();
        }
    }

    public boolean clientAsked;

    public void clientTick(){
        if(dead) {return;}
        iFullUpdateTickCount--;
        if(iFullUpdateTickCount <= 0)
        {
            iFullUpdateTickCount = 20;
            int iCenterI = (int)(x - 0.5D);
            int iCenterJ = (int)(y - 0.5D);
            int iCenterK = (int)(z - 0.5D);
            fWaterWheelCurrentRotationSpeed = ComputeRotation(iCenterI, iCenterJ, iCenterK);
        }
        if(iWaterWheelTimeSinceHit > 0)
        {
            iWaterWheelTimeSinceHit--;
        }
        if(iWaterWheelCurrentDamage > 0)
        {
            iWaterWheelCurrentDamage--;
        }
        fRotation += fWaterWheelCurrentRotationSpeed;
        if(fRotation > 360F)
        {
            fRotation -= 360F;
        } else
        if(fRotation < -360F)
        {
            fRotation += 360F;
        }
    }

    @Override
    public void tick()
    {
        if(world.isRemote){
            if(!clientAsked){
                bWaterWheelIAligned = getAligned();
                AlignBoundingBoxWithAxis();
//                PacketHelper.send(new LoadRotationPacket(0, this.id)); /// Synchronizacja rotacji, powoduje przeskok przy dołączeniu, jedynie wizualna koretka nie wiem czy jest sens
                clientAsked = true;
            }
            clientTick();
            return;
        }
        if(dead) {return;}
        iFullUpdateTickCount--;
        if(iFullUpdateTickCount <= 0)
        {
            iFullUpdateTickCount = 20;
            int iCenterI = (int)(x - 0.5D);
            int iCenterJ = (int)(y - 0.5D);
            int iCenterK = (int)(z - 0.5D);
            int iCenterid = world.getBlockId(iCenterI, iCenterJ, iCenterK);
            if(iCenterid != BlockListener.axleBlock.id)
            {
                destroyWithDrop();
                return;
            }
            if(!validateArea(world, iCenterI, iCenterJ, iCenterK, bWaterWheelIAligned))
            {
                destroyWithDrop();
                return;
            }
            if(!bProvidingPower && ((AxleBlock)BlockListener.axleBlock).getPowerLevel(world, iCenterI, iCenterJ, iCenterK) > 0)
            {
                destroyWithDrop();
                return;
            }
            fWaterWheelCurrentRotationSpeed = ComputeRotation(iCenterI, iCenterJ, iCenterK);
            if(fWaterWheelCurrentRotationSpeed > 0.01F || fWaterWheelCurrentRotationSpeed < -0.01F)
            {
                if(!bProvidingPower)
                {
                    bProvidingPower = true;
                    ((AxleBlock)BlockListener.axleBlock).setPowerLevel(world, iCenterI, iCenterJ, iCenterK, 3);
                }
            } else
            if (bProvidingPower) {
                bProvidingPower = false;
                ((AxleBlock) BlockListener.axleBlock).setPowerLevel(world, iCenterI, iCenterJ, iCenterK, 0);
            }
        }
        if(iWaterWheelTimeSinceHit > 0)
        {
            iWaterWheelTimeSinceHit--;
        }
        if(iWaterWheelCurrentDamage > 0)
        {
            iWaterWheelCurrentDamage--;
        }
        fRotation += fWaterWheelCurrentRotationSpeed;
        if(fRotation > 360F)
        {
            fRotation -= 360F;
        } else
        if(fRotation < -360F)
        {
            fRotation += 360F;
        }
    }

    @Override
    public float getShadowRadius()
    {
        return 0.0F;
    }

    @Override
    public void move(double deltaX, double deltaY, double deltaZ)
    {
        if(!dead)
        {
            destroyWithDrop();
        }
    }

    public static boolean validateArea(World world, int i, int j, int k, boolean bIAligned)
    {
        if(j + 2 >= 128)
        {
            return false;
        }
        int iOffset;
        int kOffset;
        if(bIAligned)
        {
            iOffset = 0;
            kOffset = 1;
        } else
        {
            iOffset = 1;
            kOffset = 0;
        }
        for(int iHeightOffset = -2; iHeightOffset <= 2; iHeightOffset++)
        {
            for(int iWidthOffset = -2; iWidthOffset <= 2; iWidthOffset++)
            {
                if(iHeightOffset == 0 && iWidthOffset == 0)
                {
                    continue;
                }
                int tempI = i + iOffset * iWidthOffset;
                int tempJ = j + iHeightOffset;
                int tempK = k + kOffset * iWidthOffset;
                if(!IsValidBlockForWaterWheelToOccupy(world, tempI, tempJ, tempK))
                {
                    return false;
                }
            }

        }

        return true;
    }

    public static boolean IsValidBlockForWaterWheelToOccupy(World world, int i, int j, int k)
    {
        if(!world.isAir(i, j, k))
        {
            int iid = world.getBlockId(i, j, k);
            if(iid != Block.FLOWING_WATER.id && iid != Block.WATER.id)
            {
                return false;
            }
        }
        return true;
    }

    public float ComputeRotation(int iCenterI, int iCenterJ, int iCenterK)
    {
        float fRotationAmount = 0.0F;
        int iFlowJ = iCenterJ - 2;
        int iFlowid = world.getBlockId(iCenterI, iFlowJ, iCenterK);
        if(iFlowid == Block.FLOWING_WATER.id || iFlowid == Block.WATER.id)
        {
            LiquidBlock fluidBlock = (LiquidBlock)Block.BLOCKS[iFlowid];
            Vec3d flowVector = getFlowVector(fluidBlock, world, iCenterI, iFlowJ, iCenterK);
            if(bWaterWheelIAligned)
            {
                if(flowVector.z > 0.33D)
                {
                    fRotationAmount = -fWheelRotationPerTick;
                } else
                if(flowVector.z < -0.33D)
                {
                    fRotationAmount = fWheelRotationPerTick;
                }
            } else
            if(flowVector.x > 0.33D)
            {
                fRotationAmount = fWheelRotationPerTick;
            } else
            if(flowVector.x < -0.33D)
            {
                fRotationAmount = -fWheelRotationPerTick;
            }
        }
        int iOffset;
        int kOffset;
        if(bWaterWheelIAligned)
        {
            iOffset = 0;
            kOffset = 2;
        } else
        {
            iOffset = 2;
            kOffset = 0;
        }
        iFlowid = world.getBlockId(iCenterI + iOffset, iCenterJ, iCenterK - kOffset);
        if(iFlowid == Block.FLOWING_WATER.id || iFlowid == Block.WATER.id)
        {
//            LiquidBlock fluidBlock = (LiquidBlock)Block.BLOCKS[iFlowid];
            fRotationAmount -= fWheelRotationPerTick;
        }
        iFlowid = world.getBlockId(iCenterI - iOffset, iCenterJ, iCenterK + kOffset);
        if(iFlowid == Block.FLOWING_WATER.id || iFlowid == Block.WATER.id)
        {
//            LiquidBlock fluidBlock = (LiquidBlock)Block.BLOCKS[iFlowid];
            fRotationAmount += fWheelRotationPerTick;
        }
        if(fRotationAmount > fWheelRotationPerTick)
        {
            fRotationAmount = fWheelRotationPerTick;
        } else
        if(fRotationAmount <= -fWheelRotationPerTick)
        {
            fRotationAmount = -fWheelRotationPerTick;
        }
        return fRotationAmount;
    }

    public Vec3d getFlowVector(LiquidBlock fluidBlock, BlockView iblockaccess, int i, int j, int k)
    {
        Vec3d vec3d = Vec3d.createCached(0.0D, 0.0D, 0.0D);
        int l = getEffectiveFlowDecay(fluidBlock, iblockaccess, i, j, k);
        for(int i1 = 0; i1 < 4; i1++)
        {
            int j1 = i;
            int k1 = j;
            int l1 = k;
            if(i1 == 0)
            {
                j1--;
            }
            if(i1 == 1)
            {
                l1--;
            }
            if(i1 == 2)
            {
                j1++;
            }
            if(i1 == 3)
            {
                l1++;
            }
            int i2 = getEffectiveFlowDecay(fluidBlock, iblockaccess, j1, k1, l1);
            if(i2 < 0)
            {
                if(iblockaccess.getMaterial(j1, k1, l1).blocksMovement())
                {
                    continue;
                }
                i2 = getEffectiveFlowDecay(fluidBlock, iblockaccess, j1, k1 - 1, l1);
                if(i2 >= 0)
                {
                    int j2 = i2 - (l - 8);
                    vec3d = vec3d.add((j1 - i) * j2, (k1 - j) * j2, (l1 - k) * j2);
                }
                continue;
            }
            if(i2 >= 0)
            {
                int k2 = i2 - l;
                vec3d = vec3d.add((j1 - i) * k2, (k1 - j) * k2, (l1 - k) * k2);
            }
        }

        if(iblockaccess.getBlockMeta(i, j, k) >= 8)
        {
            boolean flag = false;
            if(flag || fluidBlock.isSolidFace(iblockaccess, i, j, k - 1, 2))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolidFace(iblockaccess, i, j, k + 1, 3))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolidFace(iblockaccess, i - 1, j, k, 4))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolidFace(iblockaccess, i + 1, j, k, 5))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolidFace(iblockaccess, i, j + 1, k - 1, 2))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolidFace(iblockaccess, i, j + 1, k + 1, 3))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolidFace(iblockaccess, i - 1, j + 1, k, 4))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolidFace(iblockaccess, i + 1, j + 1, k, 5))
            {
                flag = true;
            }
            if(flag)
            {
                vec3d = vec3d.normalize().add(0.0D, -6D, 0.0D);
            }
        }
        vec3d = vec3d.normalize();
        return vec3d;
    }

    protected int getEffectiveFlowDecay(LiquidBlock fluidBlock, BlockView iblockaccess, int i, int j, int k)
    {
        if(iblockaccess.getMaterial(i, j, k) != fluidBlock.material)
        {
            return -1;
        }
        int l = iblockaccess.getBlockMeta(i, j, k);
        if(l >= 8)
        {
            l = 0;
        }
        return l;
    }

    public static final double fHalfWheelWidth = 2.4D;
    public static final double fHalfWheelDepth = 0.4D;
    public static final float fWheelRotationPerTick = 0.25F;

    public static final float fWaterWheelHeight = 4.8F;
    public static final float fWaterWheelWidth = 4.8F;
    public static final float fWaterWheelDepth = 0.8F;
    public static final int iWaterWheelMaxDamage = 40;
    public static final int iWaterWheelTicksPerFullUpdate = 20;

    public int iWaterWheelCurrentDamage;
    public int iWaterWheelTimeSinceHit;
    public int iWaterWheelRockDirection;
    public float fWaterWheelCurrentRotationSpeed;
    public int iFullUpdateTickCount;

    public boolean bWaterWheelIAligned;
    public float fRotation;
    public boolean bProvidingPower;
    public boolean receivedP;

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(ConfigListener.NAMESPACE, "WaterWheel");
    }

    public boolean getAligned() {return (dataTracker.getByte(16) & 1) != 0;}

    public void setAligned(boolean flag)
    {
        if(flag)
        {
            dataTracker.set(16, (byte) 1);
        } else
        {
            dataTracker.set(16, (byte) 0);
        }
    }

    @Override
    public boolean syncTrackerAtSpawn() {
        return true;
    }

}