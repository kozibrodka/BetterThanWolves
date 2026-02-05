package net.kozibrodka.wolves.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.EntityListener;
import net.kozibrodka.wolves.utils.ReplaceableBlockChecker;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.RailBlock;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.TriState;

import java.util.List;

@HasTrackingParameters(trackingDistance = 160, updatePeriod = 1, sendVelocity = TriState.TRUE)
public class LiftedBlockEntity extends Entity implements EntitySpawnDataProvider {

    public LiftedBlockEntity(World world) {
        super(world);
        blocksSameBlockSpawning = true;
        setBoundingBoxSpacing(0.98F, 0.98F);
        standingEyeHeight = height / 2.0F;
        velocityX = 0.0D;
        velocityY = 0.0D;
        velocityZ = 0.0D;
//        blockId = 0;
//        blockMetaData = 0;
    }

    public LiftedBlockEntity(World world, int i, int j, int k) {
        this(world);
        setLiftedId(world.getBlockId(i, j, k));
        setLiftedMeta(world.getBlockMeta(i, j, k));
        if (getLiftedId() == Block.POWERED_RAIL.id || getLiftedId() == Block.DETECTOR_RAIL.id || getLiftedId() == BlockListener.detectorRailWood.id || getLiftedId() == BlockListener.detectorRailObsidian.id) {
            setLiftedMeta(getLiftedMeta() & 7);
        } else if (getLiftedId() == Block.REDSTONE_WIRE.id) {
            setLiftedMeta(0);
        }
        setPosition((float) i + 0.5F, (float) j + 0.5F, (float) k + 0.5F);
        lastTickX = prevX = x;
        lastTickY = prevY = y;
        lastTickZ = prevZ = z;
        world.spawnEntity(this);
        world.setBlock(i, j, k, 0);
    }

    public LiftedBlockEntity(World level, Double aDouble, Double aDouble1, Double aDouble2) {
        this(level);
    }


    protected void initDataTracker() {
        dataTracker.startTracking(16, (byte) 0); //blockId
        dataTracker.startTracking(17, (byte) 0); //metaData
    }

    protected void writeNbt(NbtCompound nbttagcompound) {
        nbttagcompound.putInt("m_iid", getLiftedId());
        nbttagcompound.putInt("m_iBlockMetaData", getLiftedMeta());
    }

    protected void readNbt(NbtCompound nbttagcompound) {
        setLiftedId(nbttagcompound.getInt("m_iid"));
        setLiftedMeta(nbttagcompound.getInt("m_iBlockMetaData"));
    }

    protected boolean bypassesSteppingEffects() {
        return false;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void setPositionAndAnglesAvoidEntities(double x, double y, double z, float pitch, float yaw, int interpolationSteps) {
        this.setPosition(x, y, z);
        this.setRotation(pitch, yaw);
    }

    @Override
    public Box getCollisionAgainstShape(Entity other) {
        return null;
    }

    public Box getBoundingBox() {
        return null;
    }

    public boolean isPushable() {
        return false;
    }

    public boolean isCollidable() {
        return false;
    }

    public void onCollision(Entity entity1) {
    }

    public float getShadowRadius() {
        return 0.0F;
    }

    public void tick() {
        if (dead || world.isRemote) {
            return;
        }
        MovingPlatformEntity associatedMovingPlatform = null;
        List collisionList = world.collectEntitiesByClass(MovingPlatformEntity.class, Box.createCached(x - 0.25D, y - 1.25D, z - 0.25D, x + 0.25D, y - 0.75D, z + 0.25D));
        if (collisionList != null && collisionList.size() > 0) {
            associatedMovingPlatform = (MovingPlatformEntity) collisionList.get(0);
            if (!associatedMovingPlatform.dead) {
                double newPosX = associatedMovingPlatform.x;
                double newPosY = associatedMovingPlatform.y + 1.0D;
                double newPosZ = associatedMovingPlatform.z;
                prevX = x;
                prevY = y;
                prevZ = z;
                setPosition(newPosX, newPosY, newPosZ);
            } else {
                associatedMovingPlatform = null;
            }
        }
        if (associatedMovingPlatform == null) {
            int i = MathHelper.floor(x);
            int j = MathHelper.floor(y);
            int k = MathHelper.floor(z);
            ConvertToBlock(i, j, k);
        }
    }

    public void move(double deltaX, double deltaY, double deltaZ) {
        DestroyBlockWithDrop();
    }

    public void DestroyBlockWithDrop() {
        int i = MathHelper.floor(x);
        int j = MathHelper.floor(y);
        int k = MathHelper.floor(z);
        int idDropped = Block.BLOCKS[getLiftedId()].getDroppedItemId(0, world.random);
        if (idDropped > 0) {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, idDropped, 0);
        }
        markDead();
    }

    private void ConvertToBlock(int i, int j, int k) {
        boolean bDestroyBlock = true;
        if (world.getBlockId(i, j - 1, k) == BlockListener.platform.id && ReplaceableBlockChecker.IsReplaceableBlock(world, i, j, k)) {
            world.setBlock(i, j, k, getLiftedId(), getLiftedMeta());
            bDestroyBlock = false;
        }
        if (bDestroyBlock) {
            DestroyBlockWithDrop();
        } else {
            markDead();
        }
    }

    public static boolean CanBlockBeConvertedToEntity(World world, int i, int j, int k) {
        int iTargetid = world.getBlockId(i, j, k);
        Block targetBlock = Block.BLOCKS[iTargetid];
        if (targetBlock != null) {
            if (targetBlock instanceof RailBlock) {
                int iTargetMetaData = world.getBlockMeta(i, j, k);
                return !((iTargetMetaData >= 2) & (iTargetMetaData <= 5));
            }
            return iTargetid == Block.REDSTONE_WIRE.id;
        }
        return false;
    }

//    public int blockId;
//    public int blockMetaData;

    //BlockID
    public int getLiftedId()
    {
        return dataTracker.getByte(16);
    }

    public void setLiftedId(int i)
    {
        dataTracker.set(16, (byte) i);
    }

    //MetaData
    public int getLiftedMeta()
    {
        return dataTracker.getByte(17);
    }

    public void setLiftedMeta(int i)
    {
        dataTracker.set(17, (byte) i);
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(EntityListener.NAMESPACE, "BlockLiftedByPlatform");
    }

    @Override
    public boolean syncTrackerAtSpawn() {
        return true;
    }
}
