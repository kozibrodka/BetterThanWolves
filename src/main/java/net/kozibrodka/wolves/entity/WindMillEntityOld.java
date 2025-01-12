package net.kozibrodka.wolves.entity;


import net.kozibrodka.wolves.block.AxleBlock;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.EntityListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.WoolBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;

@HasTrackingParameters(trackingDistance = 160, updatePeriod = 2)
public class WindMillEntityOld extends Entity implements EntitySpawnDataProvider
{

    public WindMillEntityOld(World world)
    {
        super(world);
//        bWindMillIAligned = true;
//        fRotation = 0.0F;
//        bProvidingPower = false;
//        iOverpowerTimer = -1;
//        iBladeColors = new int[4];
//        for(int iTempIndex = 0; iTempIndex < 4; iTempIndex++)
//        {
//            iBladeColors[iTempIndex] = 0;
//        }

        iWindMillCurrentDamage = 0;
        iWindMillTimeSinceHit = 0;
        iWindMillRockDirection = 1;
        fWindMillCurrentRotationSpeed = 0.0F;
        iCurrentBladeColoringIndex = 0;
        iFullUpdateTickCount = 0;
        blocksSameBlockSpawning = true;
        setBoundingBoxSpacing(12.8F, 12.8F);
        standingEyeHeight = height / 2.0F;
    }

    public WindMillEntityOld(World world, double x, double y, double z,
                          boolean bIAligned)
    {
        this(world);
        setPos(x, y, z);
        setAligned(bIAligned);
        AlignBoundingBoxWithAxis();
    }

    public WindMillEntityOld(World level, Double aDouble, Double aDouble1, Double aDouble2) {
        this(level);
    }

    private void AlignBoundingBoxWithAxis()
    {
        if(getAligned())
        {
            boundingBox.set(x - 0.40000000596046448D, y - 6.4000000953674316D, z - 6.4000000953674316D, x + 0.40000000596046448D, y + 6.4000000953674316D, z + 6.4000000953674316D);
        } else
        {
            boundingBox.set(x - 6.4000000953674316D, y - 6.4000000953674316D, z - 0.40000000596046448D, x + 6.4000000953674316D, y + 6.4000000953674316D, z + 0.40000000596046448D);
        }
    }

    protected void initDataTracker()
    {
        dataTracker.startTracking(16, (byte) 0); //ALIGNED
        dataTracker.startTracking(17, (int) 0); //WHEEL ROTATION
        dataTracker.startTracking(18, (byte) 0); //PROVIDING POWER
        dataTracker.startTracking(19, (byte) 0); //OVERPOWER TIMER
        dataTracker.startTracking(20, (byte) 0); //BLADE COLOR 0
        dataTracker.startTracking(21, (byte) 0); //BLADE COLOR 1
        dataTracker.startTracking(22, (byte) 0); //BLADE COLOR 2
        dataTracker.startTracking(23, (byte) 0); //BLADE COLOR 3
    }

    public void writeNbt(NbtCompound nbttagcompound)
    {
        nbttagcompound.putBoolean("bWindMillIAligned", getAligned());
        nbttagcompound.putFloat("fRotation", getMillRotation());
        nbttagcompound.putBoolean("bProvidingPower", getProvidingPower());
        nbttagcompound.putInt("iOverpowerTimer", getOverpowerTimer());
        nbttagcompound.putInt("iBladeColors0", getBladeColor(0));
        nbttagcompound.putInt("iBladeColors1", getBladeColor(1));
        nbttagcompound.putInt("iBladeColors2", getBladeColor(2));
        nbttagcompound.putInt("iBladeColors3", getBladeColor(3));
    }

    public void readNbt(NbtCompound nbttagcompound)
    {
        setAligned(nbttagcompound.getBoolean("bWindMillIAligned"));
        setMillRotation(nbttagcompound.getFloat("fRotation"));
        setProvidingPower(nbttagcompound.getBoolean("bProvidingPower"));
        setOverpowerTimer(nbttagcompound.getInt("iOverpowerTimer"));
        setBladeColor(0, nbttagcompound.getInt("iBladeColors0"));
        setBladeColor(1, nbttagcompound.getInt("iBladeColors1"));
        setBladeColor(2, nbttagcompound.getInt("iBladeColors2"));
        setBladeColor(3, nbttagcompound.getInt("iBladeColors3"));
        AlignBoundingBoxWithAxis();
    }

    protected boolean bypassesSteppingEffects()
    {
        return false;
    }

    public Box method_1379(Entity entity)
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
        return !dead;
    }

    public boolean damage(Entity entity, int i)
    {
        if(world.isRemote || dead)
        {
            return true;
        }
        iWindMillRockDirection = -iWindMillRockDirection;
        iWindMillTimeSinceHit = 10;
        scheduleVelocityUpdate();
        iWindMillCurrentDamage += i * 5;
        if(iWindMillCurrentDamage > 40)
        {
            DestroyWithDrop();
        }
        return true;
    }

    public void method_1312()
    {
        iWindMillRockDirection = -iWindMillRockDirection;
        iWindMillTimeSinceHit = 10;
        iWindMillCurrentDamage += iWindMillCurrentDamage * 5;
    }

    public void markDead()
    {
        if(getProvidingPower())
        {
            int iCenterI = (int)(x - 0.5D);
            int iCenterJ = (int)(y - 0.5D);
            int iCenterK = (int)(z - 0.5D);
            int iCenterid = world.getBlockId(iCenterI, iCenterJ, iCenterK);
            if(iCenterid == BlockListener.axleBlock.id)
            {
                ((AxleBlock)BlockListener.axleBlock).SetPowerLevel(world, iCenterI, iCenterJ, iCenterK, 0);
            }
        }
        super.markDead();
    }

    public void DestroyWithDrop()
    {
        if(!dead)
        {
            dropItem(ItemListener.windMillItem.id, 1, 0.0F);
            markDead();
        }
    }

    public void tick()
    {
        if(dead || world.isRemote)
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
            int iCenterid = world.getBlockId(iCenterI, iCenterJ, iCenterK);
            if(iCenterid != BlockListener.axleBlock.id)
            {
                DestroyWithDrop();
                return;
            }
            if(!WindMillValidateAreaAroundBlock(world, iCenterI, iCenterJ, iCenterK, getAligned()))
            {
                DestroyWithDrop();
                return;
            }
            if(!getProvidingPower() && ((AxleBlock)BlockListener.axleBlock).GetPowerLevel(world, iCenterI, iCenterJ, iCenterK) > 0)
            {
                DestroyWithDrop();
                return;
            }
            fWindMillCurrentRotationSpeed = ComputeRotation(iCenterI, iCenterJ, iCenterK);
            if(fWindMillCurrentRotationSpeed > 0.01F || fWindMillCurrentRotationSpeed < -0.01F)
            {
                if(!getProvidingPower())
                {
                    setProvidingPower(true);
                    ((AxleBlock)BlockListener.axleBlock).SetPowerLevel(world, iCenterI, iCenterJ, iCenterK, 3);
                }
            } else
            if(getProvidingPower())
            {
                setProvidingPower(false);
                ((AxleBlock)BlockListener.axleBlock).SetPowerLevel(world, iCenterI, iCenterJ, iCenterK, 0);
            }
            if(getOverpowerTimer() >= 0)
            {
                if(getOverpowerTimer() > 0)
                {
                    setOverpowerTimer(getOverpowerTimer()-1);
                }
                if(getOverpowerTimer() <= 0)
                {
                    ((AxleBlock)BlockListener.axleBlock).Overpower(world, iCenterI, iCenterJ, iCenterK);
                }
            }
        }
        if(iWindMillTimeSinceHit > 0)
        {
            iWindMillTimeSinceHit--;
        }
        if(iWindMillCurrentDamage > 0)
        {
            iWindMillCurrentDamage--;
        }
        setMillRotation(getMillRotation()+fWindMillCurrentRotationSpeed);
        if(getMillRotation() > 360F)
        {
            setMillRotation(getMillRotation()-360F);
        } else
        if(getMillRotation() < -360F)
        {
            setMillRotation(getMillRotation()+360F);
        }
    }

    public float method_1366()
    {
        return 0.0F;
    }

    public boolean canPlayerUse(PlayerEntity entityplayer)
    {
        if(dead)
        {
            return false;
        } else
        {
            return entityplayer.getSquaredDistance(this) <= 256D;
        }
    }

    public boolean method_1323(PlayerEntity entityplayer)
    {
        ItemStack ItemInstance = entityplayer.inventory.getSelectedItem();
        if(ItemInstance != null && (ItemInstance.itemId == Item.DYE.id || ItemInstance.itemId == ItemListener.dung.id))
        {
            int iColor = 0;
            if(ItemInstance.itemId == Item.DYE.id)
            {
                iColor = WoolBlock.method_1(ItemInstance.getDamage());
            } else
            {
                iColor = 12;
            }
            setBladeColor(iCurrentBladeColoringIndex, iColor);
            iCurrentBladeColoringIndex++;
            if(iCurrentBladeColoringIndex >= 4)
            {
                iCurrentBladeColoringIndex = 0;
            }
            ItemInstance.count--;
            if(ItemInstance.count == 0)
            {
                entityplayer.inventory.setStack(entityplayer.inventory.selectedSlot, null);
            }
        }
        return true;
    }

    public void move(double deltaX, double deltaY, double deltaZ)
    {
        if(!dead)
        {
            DestroyWithDrop();
        }
    }

    public static boolean WindMillValidateAreaAroundBlock(World world, int i, int j, int k, boolean bIAligned)
    {
        if(j + 6 >= 128)
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
        for(int iHeightOffset = -6; iHeightOffset <= 6; iHeightOffset++)
        {
            for(int iWidthOffset = -6; iWidthOffset <= 6; iWidthOffset++)
            {
                if(iHeightOffset == 0 && iWidthOffset == 0)
                {
                    continue;
                }
                int tempI = i + iOffset * iWidthOffset;
                int tempJ = j + iHeightOffset;
                int tempK = k + kOffset * iWidthOffset;
                if(!IsValidBlockForWindMillToOccupy(world, tempI, tempJ, tempK))
                {
                    return false;
                }
            }

        }
        return true;
    }

    public static boolean IsValidBlockForWindMillToOccupy(World world, int i, int j, int k)
    {
        return world.isAir(i, j, k);
    }

    private float ComputeRotation(int iCenterI, int iCenterJ, int iCenterK)
    {
        float fRotationAmount = 0.0F;
        if(world.dimension.field_2176)
        {
            fRotationAmount = -0.0675F;
            setOverpowerTimer(-1);
        } else
        if(world.hasSkyLight(iCenterI, iCenterJ, iCenterK))
        {
            if(UnsortedUtils.IsBlockBeingPrecipitatedOn(world, iCenterI, 128, iCenterK))
            {
                fRotationAmount = -2F;
                if(getOverpowerTimer() < 0)
                {
                    setOverpowerTimer(30);
                }
            } else
            {
                fRotationAmount = -0.125F;
                setOverpowerTimer(-1);
            }
        }
        return fRotationAmount;
    }

    public static final float fWindMillHeight = 12.8F;
    public static final float fWindMillWidth = 12.8F;
    public static final float fWindMillDepth = 0.8F;
    public static final int iWindMillMaxDamage = 40;
    public static final float fWindMillRotationPerTick = -0.125F;
    public static final float fWindMillRotationPerTickInStorm = -2F;
    public static final float fWindMillRotationPerTickInHell = -0.0675F;
    public static final int iWindMillTicksPerFullUpdate = 20;
    public static final int iWindMillUpdatesToOverpower = 30;
    //    public boolean bWindMillIAligned;
//    public float fRotation;
//    public boolean bProvidingPower;
//    public int iOverpowerTimer;
//    public int iBladeColors[];
    public int iWindMillCurrentDamage;
    public int iWindMillTimeSinceHit;
    public int iWindMillRockDirection;
    public float fWindMillCurrentRotationSpeed;
    public int iCurrentBladeColoringIndex;
    public int iFullUpdateTickCount;

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(EntityListener.MOD_ID, "WindMill");
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
            dataTracker.set(16,  (byte)1);
        } else
        {
            dataTracker.set(16,  (byte)0);
        }
    }

    //ROTATION (FLOAT)
    public float getMillRotation()
    {
        return Float.intBitsToFloat(dataTracker.getInt(17));
    }

    public void setMillRotation(float age)
    {
        dataTracker.set(17, Float.floatToRawIntBits(age));
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
            dataTracker.set(18, (byte) 1);
        } else
        {
            dataTracker.set(18, (byte) 0);
        }
    }

    //OVERPOWER
    public int getOverpowerTimer()
    {
        return dataTracker.getByte(19);
    }

    public void setOverpowerTimer(int i)
    {
        dataTracker.set(19, (byte) i);
    }

    //COLORS
    public int getBladeColor(int blade)
    {
        return switch (blade) {
            case 0 -> dataTracker.getByte(20);
            case 1 -> dataTracker.getByte(21);
            case 2 -> dataTracker.getByte(22);
            case 3 -> dataTracker.getByte(23);
            default -> 15;
        };
    }

    public void setBladeColor(int blade, int color)
    {
        switch (blade){
            case 0 -> dataTracker.set(20, (byte) color);
            case 1 -> dataTracker.set(21, (byte) color);
            case 2 -> dataTracker.set(22, (byte) color);
            case 3 -> dataTracker.set(23, (byte) color);
        }
    }
}