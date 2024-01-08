package net.kozibrodka.wolves.entity;

import net.kozibrodka.wolves.blocks.Axle;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.EntityListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.mixin.DataTrackerAccessor;
import net.minecraft.block.BlockBase;
import net.minecraft.block.Fluid;
import net.minecraft.entity.EntityBase;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.Vec3f;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.server.entity.MobSpawnDataProvider;
import net.modificationstation.stationapi.api.util.Identifier;

@HasTrackingParameters(trackingDistance = 160, updatePeriod = 2)
public class WaterWheelEntity extends EntityBase implements EntitySpawnDataProvider
{

    public WaterWheelEntity(Level world)
    {
        super(world);
        setProvidingPower(false);
        setWheelRotation(0.0F);
        setAligned(true);
        iWaterWheelCurrentDamage = 0;
        iWaterWheelTimeSinceHit = 0;
        iWaterWheelRockDirection = 1;
        fWaterWheelCurrentRotationSpeed = 0.0F;
        iFullUpdateTickCount = 0;
        field_1593 = true;
        setSize(4.8F, 4.8F);
        standingEyeHeight = height / 2.0F;
        waterTick = 0;
    }

    public WaterWheelEntity(Level world, double x, double y, double z,
                            boolean bJAligned)
    {
        this(world);
        setPosition(x, y, z);
        setAligned(bJAligned);
        AlignBoundingBoxWithAxis();
    }

    public WaterWheelEntity(Level level, Double aDouble, Double aDouble1, Double aDouble2) {
        this(level);
    }

    private void AlignBoundingBoxWithAxis()
    {
        if(getAligned())
        {
            boundingBox.method_99(x - 0.40000000596046448D, y - 2.4000000953674316D, z - 2.4000000953674316D, x + 0.40000000596046448D, y + 2.4000000953674316D, z + 2.4000000953674316D);
        } else
        {
            boundingBox.method_99(x - 2.4000000953674316D, y - 2.4000000953674316D, z - 0.40000000596046448D, x + 2.4000000953674316D, y + 2.4000000953674316D, z + 0.40000000596046448D);
        }
    }

    protected void initDataTracker()
    {
        dataTracker.startTracking(16, (byte) 0); //ALIGNED
        dataTracker.startTracking(17, (int) 0); //WHEEL ROTATION
        dataTracker.startTracking(18, (byte) 0); //PROVIDING POWER
    }

    protected void writeCustomDataToTag(CompoundTag nbttagcompound)
    {
        nbttagcompound.put("bWaterWheelIAligned", getAligned());
        nbttagcompound.put("fRotation", getWheelRotation());
        nbttagcompound.put("bProvidingPower", getProvidingPower());
    }

    protected void readCustomDataFromTag(CompoundTag nbttagcompound)
    {
        setAligned(nbttagcompound.getBoolean("bWaterWheelIAligned"));
        setWheelRotation(nbttagcompound.getFloat("fRotation"));
        setProvidingPower(nbttagcompound.getBoolean("bProvidingPower"));
        AlignBoundingBoxWithAxis();
    }

    protected boolean canClimb()
    {
        return false;
    }

    public Box getBoundingBox(EntityBase entity)
    {
        return entity.boundingBox;
    }

    public Box method_1381()
    {
        return boundingBox;
    }

    public boolean method_1380()
    {
        return false;
    }

    public boolean method_1356()
    {
        return !removed;
    }

    public boolean damage(EntityBase entity, int i)
    {
        if(level.isServerSide || removed)
        {
            return true;
        }
        iWaterWheelRockDirection = -iWaterWheelRockDirection;
        iWaterWheelTimeSinceHit = 10;
        method_1336();
        iWaterWheelCurrentDamage += i * 5;
        if(iWaterWheelCurrentDamage > 40)
        {
            DestroyWithDrop();
        }
        return true;
    }

    public void method_1312()
    {
        iWaterWheelRockDirection = -iWaterWheelRockDirection;
        iWaterWheelTimeSinceHit = 10;
        iWaterWheelCurrentDamage += iWaterWheelCurrentDamage * 5;
    }

    public void remove()
    {
        if(getProvidingPower())
        {
            int iCenterI = (int)(x - 0.5D);
            int iCenterJ = (int)(y - 0.5D);
            int iCenterK = (int)(z - 0.5D);
            int iCenterid = level.getTileId(iCenterI, iCenterJ, iCenterK);
            if(iCenterid == BlockListener.axleBlock.id)
            {
                ((Axle)BlockListener.axleBlock).SetPowerLevel(level, iCenterI, iCenterJ, iCenterK, 0);
            }
        }
        super.remove();
    }

    public void DestroyWithDrop()
    {
        if(!removed)
        {
            dropItem(ItemListener.waterWheelItem.id, 1, 0.0F);
            remove();
        }
    }

    public void tick()
    {
        if(removed || level.isServerSide)
        {
            return;
        }
        iFullUpdateTickCount--;
        if(iFullUpdateTickCount <= 0)
        {
            iFullUpdateTickCount = 20;
            int iCenterI = (int)(x - 0.5D);
            int iCenterJ = (int)(y - 0.5D);
            int iCenterK = (int)(z - 0.5D);
            int iCenterid = level.getTileId(iCenterI, iCenterJ, iCenterK);
            if(iCenterid != BlockListener.axleBlock.id)
            {
                DestroyWithDrop();
                return;
            }
            if(!WaterWheelValidateAreaAroundBlock(level, iCenterI, iCenterJ, iCenterK, getAligned()))
            {
                DestroyWithDrop();
                return;
            }
            if(!getProvidingPower() && ((Axle)BlockListener.axleBlock).GetPowerLevel(level, iCenterI, iCenterJ, iCenterK) > 0)
            {
                DestroyWithDrop();
                return;
            }
            fWaterWheelCurrentRotationSpeed = ComputeRotation(iCenterI, iCenterJ, iCenterK);
            /**
             * modloader code
             */
//            if(fWaterWheelCurrentRotationSpeed != sentRotationSpeed) {
//            	mod_FCBetterThanWolves.sendData(this);
//            	sentRotationSpeed = fWaterWheelCurrentRotationSpeed;
//            }
            if(fWaterWheelCurrentRotationSpeed > 0.01F || fWaterWheelCurrentRotationSpeed < -0.01F)
            {
                if(!getProvidingPower())
                {
                    setProvidingPower(true);
                    ((Axle)BlockListener.axleBlock).SetPowerLevel(level, iCenterI, iCenterJ, iCenterK, 3);
                }
            } else
                if (getProvidingPower()) {
                    setProvidingPower(false);
                    ((Axle) BlockListener.axleBlock).SetPowerLevel(level, iCenterI, iCenterJ, iCenterK, 0);
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

        setWheelRotation(getWheelRotation()+fWaterWheelCurrentRotationSpeed);
        if(getWheelRotation() > 360F)
        {
            setWheelRotation(getWheelRotation()-360F);
        } else
        if(getWheelRotation() < -360F)
        {
            setWheelRotation(getWheelRotation()+360F);
        }
    }

    public float getEyeHeight()
    {
        return 0.0F;
    }

    public void move(double deltaX, double deltaY, double deltaZ)
    {
        if(!removed)
        {
            DestroyWithDrop();
        }
    }

    public static boolean WaterWheelValidateAreaAroundBlock(Level world, int i, int j, int k, boolean bIAligned)
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

    public static boolean IsValidBlockForWaterWheelToOccupy(Level world, int i, int j, int k)
    {
        if(!world.isAir(i, j, k))
        {
            int iid = world.getTileId(i, j, k);
            if(iid != BlockBase.FLOWING_WATER.id && iid != BlockBase.STILL_WATER.id)
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
        int iFlowid = level.getTileId(iCenterI, iFlowJ, iCenterK);
        if(iFlowid == BlockBase.FLOWING_WATER.id || iFlowid == BlockBase.STILL_WATER.id)
        {
            Fluid fluidBlock = (Fluid)BlockBase.BY_ID[iFlowid];
            Vec3f flowVector = getFlowVector(fluidBlock, level, iCenterI, iFlowJ, iCenterK);
            if(getAligned())
            {
                if(flowVector.z > 0.33000001311302185D)
                {
                    fRotationAmount = -0.25F;
                } else
                if(flowVector.z < -0.33000001311302185D)
                {
                    fRotationAmount = 0.25F;
                }
            } else
            if(flowVector.x > 0.33000001311302185D)
            {
                fRotationAmount = 0.25F;
            } else
            if(flowVector.x < -0.33000001311302185D)
            {
                fRotationAmount = -0.25F;
            }
        }
        int iOffset;
        int kOffset;
        if(getAligned())
        {
            iOffset = 0;
            kOffset = 2;
        } else
        {
            iOffset = 2;
            kOffset = 0;
        }
        iFlowid = level.getTileId(iCenterI + iOffset, iCenterJ, iCenterK - kOffset);
        if(iFlowid == BlockBase.FLOWING_WATER.id || iFlowid == BlockBase.STILL_WATER.id)
        {
            Fluid fluidBlock = (Fluid)BlockBase.BY_ID[iFlowid];
            fRotationAmount -= 0.25F;
        }
        iFlowid = level.getTileId(iCenterI - iOffset, iCenterJ, iCenterK + kOffset);
        if(iFlowid == BlockBase.FLOWING_WATER.id || iFlowid == BlockBase.STILL_WATER.id)
        {
            Fluid fluidBlock = (Fluid)BlockBase.BY_ID[iFlowid];
            fRotationAmount += 0.25F;
        }
        if(fRotationAmount > 0.25F)
        {
            fRotationAmount = 0.25F;
        } else
        if(fRotationAmount <= -0.25F)
        {
            fRotationAmount = -0.25F;
        }
        return fRotationAmount;
    }

    public Vec3f getFlowVector(Fluid fluidBlock, BlockView iblockaccess, int i, int j, int k)
    {
        Vec3f vec3d = Vec3f.from(0.0D, 0.0D, 0.0D);
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
                    vec3d = vec3d.method_1301((j1 - i) * j2, (k1 - j) * j2, (l1 - k) * j2);
                }
                continue;
            }
            if(i2 >= 0)
            {
                int k2 = i2 - l;
                vec3d = vec3d.method_1301((j1 - i) * k2, (k1 - j) * k2, (l1 - k) * k2);
            }
        }

        if(iblockaccess.getTileMeta(i, j, k) >= 8)
        {
            boolean flag = false;
            if(flag || fluidBlock.isSolid(iblockaccess, i, j, k - 1, 2))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolid(iblockaccess, i, j, k + 1, 3))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolid(iblockaccess, i - 1, j, k, 4))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolid(iblockaccess, i + 1, j, k, 5))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolid(iblockaccess, i, j + 1, k - 1, 2))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolid(iblockaccess, i, j + 1, k + 1, 3))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolid(iblockaccess, i - 1, j + 1, k, 4))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolid(iblockaccess, i + 1, j + 1, k, 5))
            {
                flag = true;
            }
            if(flag)
            {
                vec3d = vec3d.method_1296().method_1301(0.0D, -6D, 0.0D);
            }
        }
        vec3d = vec3d.method_1296();
        return vec3d;
    }

    protected int getEffectiveFlowDecay(Fluid fluidBlock, BlockView iblockaccess, int i, int j, int k)
    {
        if(iblockaccess.getMaterial(i, j, k) != fluidBlock.material)
        {
            return -1;
        }
        int l = iblockaccess.getTileMeta(i, j, k);
        if(l >= 8)
        {
            l = 0;
        }
        return l;
    }

    public static final float fWaterWheelHeight = 4.8F;
    public static final float fWaterWheelWidth = 4.8F;
    public static final float fWaterWheelDepth = 0.8F;
    public static final int iWaterWheelMaxDamage = 40;
    public static final float fWaterWheelRotationPerTick = 0.25F;
    public static final int iWaterWheelTicksPerFullUpdate = 20;
    public int iWaterWheelCurrentDamage;
    public int iWaterWheelTimeSinceHit;
    public int iWaterWheelRockDirection;
    public float fWaterWheelCurrentRotationSpeed;
    public int iFullUpdateTickCount;
    private float sentRotationSpeed;
    public int waterTick;

//    public boolean bWaterWheelIAligned;
//    public float fRotation;
//    public boolean bProvidingPower;

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(EntityListener.MOD_ID, "WaterWheel");
    }

    //ALIGNED
    public boolean getAligned()
    {
        return (dataTracker.getByte(16) & 1) != 0;
    }

    public void setAligned(boolean flag)
    {
        if(flag)
        {
            dataTracker.setInt(16, (byte) 1);
        } else
        {
            dataTracker.setInt(16, (byte) 0);
        }
    }

    //ROTATION (FLOAT)
    public float getWheelRotation()
    {
//        return ((float) dataTracker.getByte(17)) / 100F;
//        return (float) ((DataTrackerAccessor) dataTracker).getData().get(17).method_963();
        return Float.intBitsToFloat(dataTracker.getInt(17));
    }

    public void setWheelRotation(float age)
    {
//        dataTracker.setInt(17, (byte) ((int) 100F * age));
        dataTracker.setInt(17, Float.floatToRawIntBits(age));
    }

    //POWER
    public boolean getProvidingPower()
    {
        return (dataTracker.getByte(18) & 1) != 0;
    }

    public void setProvidingPower(boolean flag)
    {
        if(flag)
        {
            dataTracker.setInt(18, (byte) 1);
        } else
        {
            dataTracker.setInt(18, (byte) 0);
        }
    }

}
