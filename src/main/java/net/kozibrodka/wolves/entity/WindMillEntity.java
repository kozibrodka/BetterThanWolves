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

@HasTrackingParameters(trackingDistance = 1000, updatePeriod = 20)
public class WindMillEntity extends Entity implements EntitySpawnDataProvider {

    public WindMillEntity(World world) {
        super(world);
        iWindMillCurrentDamage = 0;
        iWindMillTimeSinceHit = 0;
        iWindMillRockDirection = 1;
        fWindMillCurrentRotationSpeed = 0.0F;
        iCurrentBladeColoringIndex = 0;
        iFullUpdateTickCount = 0;
        blocksSameBlockSpawning = true;
        setBoundingBoxSpacing(0.5F, 0.5F);
        standingEyeHeight = height / 2.0F;
    }

    public WindMillEntity(World world, double x, double y, double z, boolean bIAligned) {
        this(world);
        setPos(x, y, z);
        setAligned(bIAligned);
        renderDistanceMultiplier = 64.0F;
    }

    @Override
    public boolean shouldRender(double distance) {
        return distance < 10000;
    }

    public WindMillEntity(World level, Double aDouble, Double aDouble1, Double aDouble2) {
        this(level);
    }

    protected void initDataTracker() {
        dataTracker.startTracking(16, (byte) 0); //ALIGNED
        dataTracker.startTracking(17, 0); //WHEEL ROTATION
        dataTracker.startTracking(18, (byte) 0); //PROVIDING POWER
        dataTracker.startTracking(19, (byte) 0); //OVERPOWER TIMER
        dataTracker.startTracking(20, (byte) 0); //BLADE COLOR 0
        dataTracker.startTracking(21, (byte) 0); //BLADE COLOR 1
        dataTracker.startTracking(22, (byte) 0); //BLADE COLOR 2
        dataTracker.startTracking(23, (byte) 0); //BLADE COLOR 3
    }

    public void writeNbt(NbtCompound nbttagcompound) {
        nbttagcompound.putBoolean("bWindMillIAligned", getAligned());
        nbttagcompound.putFloat("fRotation", getMillRotation());
        nbttagcompound.putBoolean("bProvidingPower", getProvidingPower());
        nbttagcompound.putInt("iOverpowerTimer", getOverpowerTimer());
        nbttagcompound.putInt("iBladeColors0", getBladeColor(0));
        nbttagcompound.putInt("iBladeColors1", getBladeColor(1));
        nbttagcompound.putInt("iBladeColors2", getBladeColor(2));
        nbttagcompound.putInt("iBladeColors3", getBladeColor(3));
    }

    public void readNbt(NbtCompound nbttagcompound) {
        setAligned(nbttagcompound.getBoolean("bWindMillIAligned"));
        setMillRotation(nbttagcompound.getFloat("fRotation"));
        setProvidingPower(nbttagcompound.getBoolean("bProvidingPower"));
        setOverpowerTimer(nbttagcompound.getInt("iOverpowerTimer"));
        setBladeColor(0, nbttagcompound.getInt("iBladeColors0"));
        setBladeColor(1, nbttagcompound.getInt("iBladeColors1"));
        setBladeColor(2, nbttagcompound.getInt("iBladeColors2"));
        setBladeColor(3, nbttagcompound.getInt("iBladeColors3"));
    }

    protected boolean bypassesSteppingEffects() {
        return false;
    }

    public Box method_1379(Entity entity) {
        return entity.boundingBox;
    }

    @Override
    public Box getBoundingBox() {
        return boundingBox;
    }

    public boolean isPushable() {
        return false;
    }

    public boolean isCollidable() {
        return !dead;
    }

    public boolean damage(Entity entity, int i) {
        if (world.isRemote || dead) {
            return true;
        }
        iWindMillRockDirection = -iWindMillRockDirection;
        iWindMillTimeSinceHit = 10;
        scheduleVelocityUpdate();
        iWindMillCurrentDamage += i * 5;
        if (iWindMillCurrentDamage > 40) {
            DestroyWithDrop();
        }
        return true;
    }

    public void animateHurt() {
        iWindMillRockDirection = -iWindMillRockDirection;
        iWindMillTimeSinceHit = 10;
        iWindMillCurrentDamage += iWindMillCurrentDamage * 5;
    }

    public void markDead() {
        int centerX = (int) (x - 0.5D);
        int centerY = (int) (y - 0.5D);
        int centerZ = (int) (z - 0.5D);
        int centerId = world.getBlockId(centerX, centerY, centerZ);
        if (centerId == BlockListener.nonCollidingAxleBlock.id) {
            world.setBlock(centerX, centerY, centerZ, BlockListener.axleBlock.id, world.getBlockMeta(centerX, centerY, centerZ));
        }
        if (getProvidingPower()) {
            if (centerId == BlockListener.axleBlock.id) {
                ((AxleBlock) BlockListener.axleBlock).SetPowerLevel(world, centerX, centerY, centerZ, 0);
            } else if (centerId == BlockListener.nonCollidingAxleBlock.id) {
                ((AxleBlock) BlockListener.nonCollidingAxleBlock).SetPowerLevel(world, centerX, centerY, centerZ, 0);
            }
        }
        int xOffset;
        int zOffset;
        if (getAligned()) {
            xOffset = 0;
            zOffset = 1;
        } else {
            xOffset = 1;
            zOffset = 0;
        }
        for (int heightOffset = -6; heightOffset <= 6; heightOffset++) {
            for (int widthOffset = -6; widthOffset <= 6; widthOffset++) {
                if (heightOffset == 0 && widthOffset == 0) {
                    continue;
                }
                int tempX = centerX + xOffset * widthOffset;
                int tempY = centerY + heightOffset;
                int tempZ = centerZ + zOffset * widthOffset;
                if (world.getBlockId(tempX, tempY, tempZ) == BlockListener.collisionBlock.id
                        || world.getBlockId(tempX, tempY, tempZ) == BlockListener.obstructionBlock.id) {
                    world.setBlock(tempX, tempY, tempZ, 0);
                }
            }
        }
        super.markDead();
    }

    public void DestroyWithDrop() {
        if (!dead) {
            dropItem(ItemListener.windMillItem.id, 1, 0.0F);
            markDead();
        }
    }

    public void tick() {
        if (dead || world.isRemote) {
            return;
        }
        iFullUpdateTickCount--;
        if (iFullUpdateTickCount <= 0) {
            iFullUpdateTickCount = 20;
            int centerX = (int) (x - 0.5D);
            int centerY = (int) (y - 0.5D);
            int centerZ = (int) (z - 0.5D);
            int centerId = world.getBlockId(centerX, centerY, centerZ);
            if (centerId == BlockListener.axleBlock.id) {
                world.setBlock(centerX, centerY, centerZ, BlockListener.nonCollidingAxleBlock.id, world.getBlockMeta(centerX, centerY, centerZ));
            }
            if (centerId != BlockListener.axleBlock.id && centerId != BlockListener.nonCollidingAxleBlock.id) {
                DestroyWithDrop();
                return;
            }
            if (!validateArea(world, centerX, centerY, centerZ, getAligned())) {
                DestroyWithDrop();
                return;
            }
            if (!getProvidingPower() && ((AxleBlock) BlockListener.axleBlock).GetPowerLevel(world, centerX, centerY, centerZ) > 0) {
                DestroyWithDrop();
                return;
            }
            if (!getProvidingPower() && ((AxleBlock) BlockListener.nonCollidingAxleBlock).GetPowerLevel(world, centerX, centerY, centerZ) > 0) {
                DestroyWithDrop();
                return;
            }
            fWindMillCurrentRotationSpeed = ComputeRotation(centerX, centerY, centerZ);
            if (fWindMillCurrentRotationSpeed > 0.01F || fWindMillCurrentRotationSpeed < -0.01F) {
                if (!getProvidingPower()) {
                    setProvidingPower(true);
                    ((AxleBlock) BlockListener.nonCollidingAxleBlock).SetPowerLevel(world, centerX, centerY, centerZ, 3);
                }
            } else if (getProvidingPower()) {
                setProvidingPower(false);
                ((AxleBlock) BlockListener.nonCollidingAxleBlock).SetPowerLevel(world, centerX, centerY, centerZ, 0);
            }
            if (getOverpowerTimer() >= 0) {
                if (getOverpowerTimer() > 0) {
                    setOverpowerTimer(getOverpowerTimer() - 1);
                }
                if (getOverpowerTimer() <= 0) {
                    ((AxleBlock) BlockListener.nonCollidingAxleBlock).Overpower(world, centerX, centerY, centerZ);
                }
            }
        }
        if (iWindMillTimeSinceHit > 0) {
            iWindMillTimeSinceHit--;
        }
        if (iWindMillCurrentDamage > 0) {
            iWindMillCurrentDamage--;
        }
        setMillRotation(getMillRotation() + fWindMillCurrentRotationSpeed);
        if (getMillRotation() > 360F) {
            setMillRotation(getMillRotation() - 360F);
        } else if (getMillRotation() < -360F) {
            setMillRotation(getMillRotation() + 360F);
        }
    }

    public float getShadowRadius() {
        return 0.0F;
    }

    public boolean canPlayerUse(PlayerEntity entityplayer) {
        if (dead) {
            return false;
        } else {
            return entityplayer.getSquaredDistance(this) <= 256D;
        }
    }

    public boolean interact(PlayerEntity entityplayer) {
        ItemStack ItemInstance = entityplayer.inventory.getSelectedItem();
        if (ItemInstance != null && (ItemInstance.itemId == Item.DYE.id || ItemInstance.itemId == ItemListener.dung.id)) {
            int iColor = 0;
            if (ItemInstance.itemId == Item.DYE.id) {
                iColor = WoolBlock.method_1(ItemInstance.getDamage());
            } else {
                iColor = 12;
            }
            setBladeColor(iCurrentBladeColoringIndex, iColor);
            iCurrentBladeColoringIndex++;
            if (iCurrentBladeColoringIndex >= 4) {
                iCurrentBladeColoringIndex = 0;
            }
            ItemInstance.count--;
            if (ItemInstance.count == 0) {
                entityplayer.inventory.setStack(entityplayer.inventory.selectedSlot, null);
            }
        }
        return true;
    }

    public void move(double deltaX, double deltaY, double deltaZ) {
        if (!dead) {
            DestroyWithDrop();
        }
    }

    public static boolean validateArea(World world, int x, int y, int z, boolean aligned) {
        if (y + 6 >= 128) {
            return false;
        }
        int xOffset;
        int zOffset;
        if (aligned) {
            xOffset = 0;
            zOffset = 1;
        } else {
            xOffset = 1;
            zOffset = 0;
        }
        for (int heightOffset = -6; heightOffset <= 6; heightOffset++) {
            for (int widthOffset = -6; widthOffset <= 6; widthOffset++) {
                if (heightOffset == 0 && widthOffset == 0) {
                    continue;
                }
                int tempX = x + xOffset * widthOffset;
                int tempY = y + heightOffset;
                int tempZ = z + zOffset * widthOffset;
                if (!isSuitableBlock(world, tempX, tempY, tempZ)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void placeCollisionBlocks(World world, int x, int y, int z, boolean aligned) {
        if (y + 6 >= 128) {
            return;
        }
        int xOffset;
        int zOffset;
        if (aligned) {
            xOffset = 0;
            zOffset = 1;
        } else {
            xOffset = 1;
            zOffset = 0;
        }
        for (int heightOffset = -6; heightOffset <= 6; heightOffset++) {
            for (int widthOffset = -6; widthOffset <= 6; widthOffset++) {
                if (heightOffset == 0 && widthOffset == 0) {
                    continue;
                }
                int tempX = x + xOffset * widthOffset;
                int tempY = y + heightOffset;
                int tempZ = z + zOffset * widthOffset;
                if (world.getBlockId(tempX, tempY, tempZ) == 0) {
                    world.setBlock(tempX, tempY, tempZ, BlockListener.collisionBlock.id);
                }
            }
        }
    }

    public static boolean isSuitableBlock(World world, int x, int y, int z) {
        return world.isAir(x, y, z) || world.getBlockId(x, y, z) == BlockListener.collisionBlock.id;
    }

    private float ComputeRotation(int iCenterI, int iCenterJ, int iCenterK) {
        float fRotationAmount = 0.0F;
        if (world.dimension.field_2176) {
            fRotationAmount = -0.0675F;
            setOverpowerTimer(-1);
        } else if (world.hasSkyLight(iCenterI, iCenterJ, iCenterK)) {
            if (UnsortedUtils.IsBlockBeingPrecipitatedOn(world, iCenterI, 128, iCenterK)) {
                fRotationAmount = -2F;
                if (getOverpowerTimer() < 0) {
                    setOverpowerTimer(30);
                }
            } else {
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
        return Identifier.of(EntityListener.NAMESPACE, "WindMill");
    }

    //ALIGNED
    public boolean getAligned() {
        return (dataTracker.getByte(16) & 1) != 0;
    }

    public void setAligned(boolean flag) {
        if (flag) {
            dataTracker.set(16, (byte) 1);
        } else {
            dataTracker.set(16, (byte) 0);
        }
    }

    //ROTATION (FLOAT)
    public float getMillRotation() {
        return Float.intBitsToFloat(dataTracker.getInt(17));
    }

    public void setMillRotation(float age) {
        dataTracker.set(17, Float.floatToRawIntBits(age));
    }

    //POWER
    public boolean getProvidingPower() {
        return (dataTracker.getByte(18) & 1) != 0;
    }

    public void setProvidingPower(boolean flag) {
        if (flag) {
            dataTracker.set(18, (byte) 1);
        } else {
            dataTracker.set(18, (byte) 0);
        }
    }

    //OVERPOWER
    public int getOverpowerTimer() {
        return dataTracker.getByte(19);
    }

    public void setOverpowerTimer(int i) {
        dataTracker.set(19, (byte) i);
    }

    //COLORS
    public int getBladeColor(int blade) {
        return switch (blade) {
            case 0 -> dataTracker.getByte(20);
            case 1 -> dataTracker.getByte(21);
            case 2 -> dataTracker.getByte(22);
            case 3 -> dataTracker.getByte(23);
            default -> 15;
        };
    }

    public void setBladeColor(int blade, int color) {
        switch (blade) {
            case 0 -> dataTracker.set(20, (byte) color);
            case 1 -> dataTracker.set(21, (byte) color);
            case 2 -> dataTracker.set(22, (byte) color);
            case 3 -> dataTracker.set(23, (byte) color);
        }
    }

    @Override
    public boolean syncTrackerAtSpawn() {
        return true;
    }
}
