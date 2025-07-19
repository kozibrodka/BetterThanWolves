package net.kozibrodka.wolves.entity;

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

import java.util.List;

@HasTrackingParameters(trackingDistance = 160, updatePeriod = 2)
public class LiftedBlockEntity extends Entity implements EntitySpawnDataProvider {

    public LiftedBlockEntity(World world) {
        super(world);
        blocksSameBlockSpawning = true;
        setBoundingBoxSpacing(0.98F, 0.98F);
        standingEyeHeight = height / 2.0F;
        velocityX = 0.0D;
        velocityY = 0.0D;
        velocityZ = 0.0D;
        blockId = 0;
        blockMetaData = 0;
    }

    public LiftedBlockEntity(World world, int i, int j, int k) {
        this(world);
        blockId = world.getBlockId(i, j, k);
        blockMetaData = world.getBlockMeta(i, j, k);
        if (blockId == Block.POWERED_RAIL.id || blockId == Block.DETECTOR_RAIL.id || blockId == BlockListener.detectorRailWood.id || blockId == BlockListener.detectorRailObsidian.id) {
            blockMetaData &= 7;
        } else if (blockId == Block.REDSTONE_WIRE.id) {
            blockMetaData = 0;
        }
        setPos((float) i + 0.5F, (float) j + 0.5F, (float) k + 0.5F);
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
    }

    protected void writeNbt(NbtCompound nbttagcompound) {
        nbttagcompound.putInt("m_iid", blockId);
        nbttagcompound.putInt("m_iBlockMetaData", blockMetaData);
    }

    protected void readNbt(NbtCompound nbttagcompound) {
        blockId = nbttagcompound.getInt("m_iid");
        blockMetaData = nbttagcompound.getInt("m_iBlockMetaData");
    }

    protected boolean bypassesSteppingEffects() {
        return false;
    }

    public Box method_1379(Entity entity) {
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
                setPos(newPosX, newPosY, newPosZ);
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
        int idDropped = Block.BLOCKS[blockId].getDroppedItemId(0, world.random);
        if (idDropped > 0) {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, idDropped, 0);
        }
        markDead();
    }

    private void ConvertToBlock(int i, int j, int k) {
        boolean bDestroyBlock = true;
        if (world.getBlockId(i, j - 1, k) == BlockListener.platform.id && ReplaceableBlockChecker.IsReplaceableBlock(world, i, j, k)) {
            world.setBlock(i, j, k, blockId, blockMetaData);
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

    public int blockId;
    public int blockMetaData;

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(EntityListener.NAMESPACE, "BlockLiftedByPlatform");
    }
}
