package net.kozibrodka.wolves.entity;

import net.kozibrodka.wolves.block.AxleBlock;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.EntityListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.minecraft.block.Block;
import net.minecraft.block.LiquidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;

@HasTrackingParameters(trackingDistance = 160, updatePeriod = 2)
public class WaterWheelEntity extends Entity implements EntitySpawnDataProvider {

    public WaterWheelEntity(World world) {
        super(world);
        setProvidingPower(false);
        setWheelRotation(0.0F);
        iWaterWheelCurrentDamage = 0;
        iWaterWheelTimeSinceHit = 0;
        iWaterWheelRockDirection = 1;
        fWaterWheelCurrentRotationSpeed = 0.0F;
        iFullUpdateTickCount = 0;
        blocksSameBlockSpawning = true;
        setBoundingBoxSpacing(0.5F, 0.5F);
        standingEyeHeight = height / 2.0F;
        waterTick = 0;
    }

    public WaterWheelEntity(World world, double x, double y, double z, boolean bJAligned) {
        this(world);
        setPos(x, y, z);
        setAligned(bJAligned);
    }

    public WaterWheelEntity(World level, Double aDouble, Double aDouble1, Double aDouble2) {
        this(level);
    }

    protected void initDataTracker() {
        dataTracker.startTracking(16, (byte) 0); //ALIGNED
        dataTracker.startTracking(17, (int) 0); //WHEEL ROTATION
        dataTracker.startTracking(18, (byte) 0); //PROVIDING POWER
    }

    protected void writeNbt(NbtCompound nbttagcompound) {
        nbttagcompound.putBoolean("bWaterWheelIAligned", getAligned());
        nbttagcompound.putFloat("fRotation", getWheelRotation());
        nbttagcompound.putBoolean("bProvidingPower", getProvidingPower());
    }

    protected void readNbt(NbtCompound nbttagcompound) {
        setAligned(nbttagcompound.getBoolean("bWaterWheelIAligned"));
        setWheelRotation(nbttagcompound.getFloat("fRotation"));
        setProvidingPower(nbttagcompound.getBoolean("bProvidingPower"));
    }

    protected boolean bypassesSteppingEffects() {
        return false;
    }

    public Box method_1379(Entity entity) {
        return entity.boundingBox;
    }

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
        if(world.isRemote || dead) {
            return true;
        }
        iWaterWheelRockDirection = -iWaterWheelRockDirection;
        iWaterWheelTimeSinceHit = 10;
        scheduleVelocityUpdate();
        iWaterWheelCurrentDamage += i * 5;
        if(iWaterWheelCurrentDamage > 40) {
            destroyWithDrop();
        }
        return true;
    }

    public void animateHurt() {
        iWaterWheelRockDirection = -iWaterWheelRockDirection;
        iWaterWheelTimeSinceHit = 10;
        iWaterWheelCurrentDamage += iWaterWheelCurrentDamage * 5;
    }

    public void markDead() {
        int centerX = (int)(x - 0.5D);
        int centerY = (int)(y - 0.5D);
        int centerZ = (int)(z - 0.5D);
        int centerId = world.getBlockId(centerX, centerY, centerZ);
        if (centerId == BlockListener.nonCollidingAxleBlock.id) {
            world.setBlock(centerX, centerY, centerZ, BlockListener.axleBlock.id, world.getBlockMeta(centerX, centerY, centerZ));
        }
        if(getProvidingPower()) {
            if(centerId == BlockListener.axleBlock.id) {
                ((AxleBlock)BlockListener.axleBlock).SetPowerLevel(world, centerX, centerY, centerZ, 0);
            } else if(centerId == BlockListener.nonCollidingAxleBlock.id) {
                ((AxleBlock)BlockListener.nonCollidingAxleBlock).SetPowerLevel(world, centerX, centerY, centerZ, 0);
            }
        }
        int xOffset;
        int zOffset;
        if(getAligned()) {
            xOffset = 0;
            zOffset = 1;
        } else {
            xOffset = 1;
            zOffset = 0;
        }
        for(int heightOffset = -2; heightOffset <= 2; heightOffset++) {
            for(int widthOffset = -2; widthOffset <= 2; widthOffset++) {
                if(heightOffset == 0 && widthOffset == 0) {
                    continue;
                }
                int tempX = centerX + xOffset * widthOffset;
                int tempY = centerY + heightOffset;
                int tempZ = centerZ + zOffset * widthOffset;
                if(world.getBlockId(tempX, tempY, tempZ) == BlockListener.collisionBlock.id
                        || world.getBlockId(tempX, tempY, tempZ) == BlockListener.obstructionBlock.id) {
                    world.setBlock(tempX, tempY, tempZ, 0);
                }
            }
        }
        super.markDead();
    }

    public void destroyWithDrop() {
        if(!dead) {
            dropItem(ItemListener.waterWheelItem.id, 1, 0.0F);
            markDead();
        }
    }

    public void tick() {
        if(dead || world.isRemote) {
            return;
        }
        iFullUpdateTickCount--;
        if(iFullUpdateTickCount <= 0) {
            iFullUpdateTickCount = 20;
            int centerX = (int)(x - 0.5D);
            int centerY = (int)(y - 0.5D);
            int centerZ = (int)(z - 0.5D);
            int centerId = world.getBlockId(centerX, centerY, centerZ);
            if (centerId == BlockListener.axleBlock.id) {
                world.setBlock(centerX, centerY, centerZ, BlockListener.nonCollidingAxleBlock.id, world.getBlockMeta(centerX, centerY, centerZ));
            }
            if(centerId != BlockListener.axleBlock.id && centerId != BlockListener.nonCollidingAxleBlock.id) {
                destroyWithDrop();
                return;
            }
            if(!validateArea(world, centerX, centerY, centerZ, getAligned())) {
                destroyWithDrop();
                return;
            }
            if(!getProvidingPower() && ((AxleBlock)BlockListener.axleBlock).GetPowerLevel(world, centerX, centerY, centerZ) > 0) {
                destroyWithDrop();
                return;
            }
            if(!getProvidingPower() && ((AxleBlock)BlockListener.nonCollidingAxleBlock).GetPowerLevel(world, centerX, centerY, centerZ) > 0) {
                destroyWithDrop();
                return;
            }
            fWaterWheelCurrentRotationSpeed = ComputeRotation(centerX, centerY, centerZ);
            if(fWaterWheelCurrentRotationSpeed > 0.01F || fWaterWheelCurrentRotationSpeed < -0.01F) {
                if(!getProvidingPower()) {
                    setProvidingPower(true);
                    ((AxleBlock)BlockListener.nonCollidingAxleBlock).SetPowerLevel(world, centerX, centerY, centerZ, 3);
                }
            } else if (getProvidingPower()) {
                setProvidingPower(false);
                ((AxleBlock) BlockListener.nonCollidingAxleBlock).SetPowerLevel(world, centerX, centerY, centerZ, 0);
            }
        }
        if(iWaterWheelTimeSinceHit > 0) {
            iWaterWheelTimeSinceHit--;
        }
        if(iWaterWheelCurrentDamage > 0) {
            iWaterWheelCurrentDamage--;
        }
        setWheelRotation(getWheelRotation()+fWaterWheelCurrentRotationSpeed);
        if(getWheelRotation() > 360F) {
            setWheelRotation(getWheelRotation()-360F);
        } else
        if(getWheelRotation() < -360F) {
            setWheelRotation(getWheelRotation()+360F);
        }
    }

    public float getShadowRadius() {
        return 0.0F;
    }

    public void move(double deltaX, double deltaY, double deltaZ) {
        if(!dead) {
            destroyWithDrop();
        }
    }

    public static boolean validateArea(World world, int x, int y, int z, boolean aligned) {
        if(y + 2 >= 128) {
            return false;
        }
        int xOffset;
        int zOffset;
        if(aligned) {
            xOffset = 0;
            zOffset = 1;
        } else {
            xOffset = 1;
            zOffset = 0;
        }
        for(int heightOffset = -2; heightOffset <= 2; heightOffset++) {
            for(int widthOffset = -2; widthOffset <= 2; widthOffset++) {
                if(heightOffset == 0 && widthOffset == 0) {
                    continue;
                }
                int tempX = x + xOffset * widthOffset;
                int tempY = y + heightOffset;
                int tempZ = z + zOffset * widthOffset;
                if(!isSuitableBlock(world, tempX, tempY, tempZ)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void placeCollisionBlocks(World world, int x, int y, int z, boolean aligned) {
        if(y + 2 >= 128) {
            return;
        }
        int xOffset;
        int zOffset;
        if(aligned) {
            xOffset = 0;
            zOffset = 1;
        } else {
            xOffset = 1;
            zOffset = 0;
        }
        for(int heightOffset = -2; heightOffset <= 2; heightOffset++) {
            for(int widthOffset = -2; widthOffset <= 2; widthOffset++) {
                if(heightOffset == 0 && widthOffset == 0) {
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
        if(!world.isAir(x, y, z)) {
            int id = world.getBlockId(x, y, z);
            return id == Block.FLOWING_WATER.id || id == Block.WATER.id || id == BlockListener.collisionBlock.id;
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
        iFlowid = world.getBlockId(iCenterI + iOffset, iCenterJ, iCenterK - kOffset);
        if(iFlowid == Block.FLOWING_WATER.id || iFlowid == Block.WATER.id)
        {
            LiquidBlock fluidBlock = (LiquidBlock)Block.BLOCKS[iFlowid];
            fRotationAmount -= 0.25F;
        }
        iFlowid = world.getBlockId(iCenterI - iOffset, iCenterJ, iCenterK + kOffset);
        if(iFlowid == Block.FLOWING_WATER.id || iFlowid == Block.WATER.id)
        {
            LiquidBlock fluidBlock = (LiquidBlock)Block.BLOCKS[iFlowid];
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
                if(iblockaccess.getMaterial (j1, k1, l1).blocksMovement())
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
            if(flag || fluidBlock.isSolidBlock(iblockaccess, i, j, k - 1, 2))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolidBlock(iblockaccess, i, j, k + 1, 3))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolidBlock(iblockaccess, i - 1, j, k, 4))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolidBlock(iblockaccess, i + 1, j, k, 5))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolidBlock(iblockaccess, i, j + 1, k - 1, 2))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolidBlock(iblockaccess, i, j + 1, k + 1, 3))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolidBlock(iblockaccess, i - 1, j + 1, k, 4))
            {
                flag = true;
            }
            if(flag || fluidBlock.isSolidBlock(iblockaccess, i + 1, j + 1, k, 5))
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
        if(iblockaccess.getMaterial (i, j, k) != fluidBlock.material)
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

    public boolean interact(PlayerEntity entityplayer)
    {
        if(world.isRemote) {
            System.out.println("KLIENT: " + getAligned());
        } else {
            System.out.println("SERVER: " + getAligned());
        }
        return false;
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
    public boolean typechoosen;

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
            dataTracker.set(16,  (byte)1);
        } else
        {
            dataTracker.set(16,  (byte)0);
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

    @Override
    public boolean syncTrackerAtSpawn() {
        return true;
    }

}
