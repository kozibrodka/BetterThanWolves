package net.kozibrodka.wolves.entity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import net.modificationstation.stationapi.api.util.TriState;

@HasTrackingParameters(trackingDistance = 240, updatePeriod = Integer.MAX_VALUE, sendVelocity = TriState.FALSE)
public class WindMillEntity extends Entity implements EntitySpawnDataProvider
{

    public WindMillEntity(World world)
    {
        super(world); /// It seems that sometimes, when entity bouding box is no chunk border (propably?), it can be "cut"
        iWindMillRockDirection = 1;
        fWindMillCurrentRotationSpeed = 0.0F;
        iFullUpdateTickCount = 0;
        blocksSameBlockSpawning = true;
        setBoundingBoxSpacing(12.8F, 12.8F);
        standingEyeHeight = height / 2.0F;
    }

    public WindMillEntity(World world, double x, double y, double z,
                          boolean bIAligned)
    {
        this(world);
        setPosition(x, y, z);
        bWindMillIAligned = bIAligned;
        AlignBoundingBoxWithAxis();
        setAligned(bWindMillIAligned);
    }

    public WindMillEntity(World level, double x, double y, double z) {
        this(level);
        setPosition(x, y, z);
        AlignBoundingBoxWithAxis();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void processServerEntityStatus(byte status) {
        if (status >= 6 && status < 14) {
            int dmg = status - 6;
            iWindMillRockDirection = -iWindMillRockDirection;
            iWindMillTimeSinceHit = 10;
            scheduleVelocityUpdate();
            iWindMillCurrentDamage += dmg * 5;
        }else if(status >= 16 && status < 32){
            setBladeColor(0, status - 16);
        }else if(status >= 36 && status < 52){
            setBladeColor(1, status - 36);
        }else if(status >= 56 && status < 72){
            setBladeColor(2, status - 56);
        }else if(status >= 76 && status < 92){
            setBladeColor(3, status - 76);
        }else
        {
            super.processServerEntityStatus(status);
        }
    }

    private void AlignBoundingBoxWithAxis()
    {
        if(bWindMillIAligned)
        {
            boundingBox.set(x - fHalfMillDepth, y - fHalfMillWidth, z - fHalfMillWidth, x + fHalfMillDepth, y + fHalfMillWidth, z + fHalfMillWidth);
        } else
        {
            boundingBox.set(x - fHalfMillWidth, y - fHalfMillWidth, z - fHalfMillDepth, x + fHalfMillWidth, y + fHalfMillWidth, z + fHalfMillDepth);
        }
    }

    @Override
    protected void initDataTracker()
    {
        dataTracker.startTracking(16, (byte) 0); //Facing
        dataTracker.startTracking(17, (int) 0); //Rotation
        dataTracker.startTracking(20, (byte) 0); //BLADE COLOR 0
        dataTracker.startTracking(21, (byte) 0); //BLADE COLOR 1
        dataTracker.startTracking(22, (byte) 0); //BLADE COLOR 2
        dataTracker.startTracking(23, (byte) 0); //BLADE COLOR 3
    }

    @Override
    public void writeNbt(NbtCompound nbttagcompound)
    {
        nbttagcompound.putBoolean("bWindMillIAligned", bWindMillIAligned);
        nbttagcompound.putFloat("fRotation", fRotation);
        nbttagcompound.putBoolean("bProvidingPower", bProvidingPower);
        nbttagcompound.putInt("iOverpowerTimer", iOverpowerTimer);
        nbttagcompound.putInt("iColorIndex", iCurrentBladeColoringIndex);
        nbttagcompound.putInt("iBladeColors0", getBladeColor(0));
        nbttagcompound.putInt("iBladeColors1", getBladeColor(1));
        nbttagcompound.putInt("iBladeColors2", getBladeColor(2));
        nbttagcompound.putInt("iBladeColors3", getBladeColor(3));
    }

    @Override
    public void readNbt(NbtCompound nbttagcompound)
    {
        bWindMillIAligned = nbttagcompound.getBoolean("bWindMillIAligned");
        fRotation = nbttagcompound.getFloat("fRotation");
        bProvidingPower = nbttagcompound.getBoolean("bProvidingPower");
        iOverpowerTimer = nbttagcompound.getInt("iOverpowerTimer");
        iCurrentBladeColoringIndex = nbttagcompound.getInt("iColorIndex");
        setBladeColor(0, nbttagcompound.getInt("iBladeColors0"));
        setBladeColor(1, nbttagcompound.getInt("iBladeColors1"));
        setBladeColor(2, nbttagcompound.getInt("iBladeColors2"));
        setBladeColor(3, nbttagcompound.getInt("iBladeColors3"));
        AlignBoundingBoxWithAxis();
        setAligned(bWindMillIAligned);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void setPositionAndAnglesAvoidEntities(double x, double y, double z, float pitch, float yaw, int interpolationSteps) {
        this.setPosition(x, y, z);
        this.setRotation(pitch, yaw);
    }

    @Override
    protected boolean bypassesSteppingEffects()
    {
        return false;
    }

    @Override
    public Box getCollisionAgainstShape(Entity entity)
    {
        return entity.boundingBox;
    }

    @Override
    public Box getBoundingBox()
    {
        return boundingBox;
    }

    @Override
    public boolean isPushable()
    {
        return false;
    }

    @Override
    public boolean isCollidable()
    {
        return !dead;
    }

    @Override
    public float getEyeHeight() {
        return -1.0F;
    }

    @Override
    public boolean damage(Entity entity, int amount)
    {
        if(world.isRemote || dead)
        {
            return true;
        }
        iWindMillRockDirection = -iWindMillRockDirection;
        iWindMillTimeSinceHit = 10;
        scheduleVelocityUpdate();
        iWindMillCurrentDamage += amount * 5;
        if(amount < 8 && iWindMillCurrentDamage <= 40){
            world.broadcastEntityEvent(this, (byte)(amount+6));
        }
        if(iWindMillCurrentDamage > 40)
        {
            destroyWithDrop();
        }
        return true;
    }

    @Override
    public void animateHurt()
    {
        iWindMillRockDirection = -iWindMillRockDirection;
        iWindMillTimeSinceHit = 10;
        iWindMillCurrentDamage += iWindMillCurrentDamage * 5;
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
            dropItem(ItemListener.windMillItem.id, 1, 0.0F);
            markDead();
        }
    }

    public void clientTick(){
        iFullUpdateTickCount--;
        if(iFullUpdateTickCount <= 0)
        {
            iFullUpdateTickCount = 20;
            int iCenterI = (int)(x - 0.5D);
            int iCenterJ = (int)(y - 0.5D);
            int iCenterK = (int)(z - 0.5D);
            fWindMillCurrentRotationSpeed = ComputeRotation(iCenterI, iCenterJ, iCenterK);
        }
        if(iWindMillTimeSinceHit > 0)
        {
            iWindMillTimeSinceHit--;
        }
        if(iWindMillCurrentDamage > 0)
        {
            iWindMillCurrentDamage--;
        }
        fRotation = (fRotation+fWindMillCurrentRotationSpeed);
        if(fRotation > 360F)
        {
            fRotation = (fRotation-360F);
        } else
        if(fRotation < -360F)
        {
            fRotation = (fRotation+360F);
        }
    }

    @Override
    public void tick()
    {
        if(world.isRemote){
            if(!clientAsked){
                bWindMillIAligned = getAligned();
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
            if(!validateArea(world, iCenterI, iCenterJ, iCenterK, bWindMillIAligned))
            {
                destroyWithDrop();
                return;
            }
            if(!bProvidingPower && ((AxleBlock)BlockListener.axleBlock).getPowerLevel(world, iCenterI, iCenterJ, iCenterK) > 0)
            {
                destroyWithDrop();
                return;
            }
            fWindMillCurrentRotationSpeed = ComputeRotation(iCenterI, iCenterJ, iCenterK);
            if(fWindMillCurrentRotationSpeed > 0.01F || fWindMillCurrentRotationSpeed < -0.01F)
            {
                if(!bProvidingPower)
                {
                    bProvidingPower = true;
                    ((AxleBlock)BlockListener.axleBlock).setPowerLevel(world, iCenterI, iCenterJ, iCenterK, 3);
                }
            } else
            if(bProvidingPower)
            {
                bProvidingPower = false;
                ((AxleBlock)BlockListener.axleBlock).setPowerLevel(world, iCenterI, iCenterJ, iCenterK, 0);
            }
            if(iOverpowerTimer >= 0)
            {
                if(iOverpowerTimer > 0)
                {
                    iOverpowerTimer--;
                }
                if(iOverpowerTimer <= 0)
                {
                    ((AxleBlock)BlockListener.axleBlock).overpower(world, iCenterI, iCenterJ, iCenterK);
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
        fRotation = (fRotation+fWindMillCurrentRotationSpeed);
        if(fRotation > 360F)
        {
            fRotation = (fRotation-360F);
        } else
        if(fRotation < -360F)
        {
            fRotation = (fRotation+360F);
        }
    }

    @Override
    public float getShadowRadius()
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

    @Override
    public boolean interact(PlayerEntity entityplayer)
    {
        ItemStack ItemInstance = entityplayer.inventory.getSelectedItem();
        if (ItemInstance != null && (ItemInstance.itemId == Item.DYE.id || ItemInstance.itemId == ItemListener.dung.id)) {
            int iColor = 0;
            if (ItemInstance.itemId == Item.DYE.id) {
                iColor = WoolBlock.getBlockMeta(ItemInstance.getDamage());
            } else {
                iColor = 12;
            }
            if (world.isRemote) {
                if(entityplayer.canSee(this)){
                    entityplayer.swingHand();
                }
            } else {
                setBladeColor(iCurrentBladeColoringIndex, iColor);
                switch (iCurrentBladeColoringIndex){
                    case 0: world.broadcastEntityEvent(this, (byte) (16 + iColor)); break;
                    case 1: world.broadcastEntityEvent(this, (byte) (36 + iColor)); break;
                    case 2: world.broadcastEntityEvent(this, (byte) (56 + iColor)); break;
                    case 3: world.broadcastEntityEvent(this, (byte) (76 + iColor)); break;
                }
                iCurrentBladeColoringIndex++;
                if(iCurrentBladeColoringIndex >= 4)
                {
                    iCurrentBladeColoringIndex = 0;
                }
                entityplayer.swingHand();
                ItemInstance.count--;
                if (ItemInstance.count == 0) {
                    entityplayer.inventory.setStack(entityplayer.inventory.selectedSlot, null);
                }
            }
        }
        return true;
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
        if(world.dimension.evaporatesWater)
        {
            fRotationAmount = -0.0675F;
            iOverpowerTimer = -1;
        } else
        if(world.hasSkyLight(iCenterI, iCenterJ, iCenterK))
        {
            if(UnsortedUtils.IsBlockBeingPrecipitatedOn(world, iCenterI, 128, iCenterK))
            {
                fRotationAmount = -2F;
                if(iOverpowerTimer < 0)
                {
                    iOverpowerTimer = 30;
                }
            } else
            {
                fRotationAmount = -0.125F;
                iOverpowerTimer = -1;
            }
        }
        return fRotationAmount;
    }

    public static final double fHalfMillDepth = 0.4D;
    public static final double fHalfMillWidth = 6.4D;
    
    public static final float fWindMillHeight = 12.8F;
    public static final float fWindMillWidth = 12.8F;
    public static final float fWindMillDepth = 0.8F;
    public static final int iWindMillMaxDamage = 40;
    public static final float fWindMillRotationPerTick = -0.125F;
    public static final float fWindMillRotationPerTickInStorm = -2F;
    public static final float fWindMillRotationPerTickInHell = -0.0675F;
    public static final int iWindMillTicksPerFullUpdate = 20;
    public static final int iWindMillUpdatesToOverpower = 30;

    public boolean bWindMillIAligned;
    public float fRotation;
    public boolean bProvidingPower;
    public int iOverpowerTimer;
    public int iBladeColors[];
    public int iCurrentBladeColoringIndex;
    public int iWindMillCurrentDamage;
    public int iWindMillTimeSinceHit;
    public int iWindMillRockDirection;

    public float fWindMillCurrentRotationSpeed;
    public int iFullUpdateTickCount;
    public boolean clientAsked;
    public boolean receivedP;

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(EntityListener.NAMESPACE, "WindMill");
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