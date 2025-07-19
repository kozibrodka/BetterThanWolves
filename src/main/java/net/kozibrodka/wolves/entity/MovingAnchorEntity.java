package net.kozibrodka.wolves.entity;

import net.kozibrodka.wolves.block.AnchorBlock;
import net.kozibrodka.wolves.block.RopeBlock;
import net.kozibrodka.wolves.block.entity.PulleyBlockEntity;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.EntityListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.ReplaceableBlockChecker;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;

@HasTrackingParameters(trackingDistance = 160, updatePeriod = 2)
public class MovingAnchorEntity extends Entity implements EntitySpawnDataProvider {

    public MovingAnchorEntity(World world) {
        super(world);
        associatedPulleyPos = new BlockPosition();
        blocksSameBlockSpawning = true;
        setBoundingBoxSpacing(0.98F, AnchorBlock.anchorBaseHeight - 0.02F);
        standingEyeHeight = height / 2.0F;
        velocityX = 0.0D;
        velocityY = 0.0D;
        velocityZ = 0.0D;
    }

    public MovingAnchorEntity(World world, double x, double y, double z,
                              BlockPosition pulleyPos, int iMovementDirection) {
        this(world);
        associatedPulleyPos.i = pulleyPos.i;
        associatedPulleyPos.j = pulleyPos.j;
        associatedPulleyPos.k = pulleyPos.k;
        if (iMovementDirection > 0) {
            velocityY = 0.05000000074505806D;
        } else {
            velocityY = -0.05000000074505806D;
        }
        setPos(x, y, z);
        lastTickX = prevX = x;
        lastTickY = prevY = y;
        lastTickZ = prevZ = z;
    }

    public MovingAnchorEntity(World level, Double aDouble, Double aDouble1, Double aDouble2) {
        this(level);
    }

    protected void initDataTracker() {
    }

    protected void writeNbt(NbtCompound nbttagcompound) {
        nbttagcompound.putInt("associatedPulleyPosI", associatedPulleyPos.i);
        nbttagcompound.putInt("associatedPulleyPosJ", associatedPulleyPos.j);
        nbttagcompound.putInt("associatedPulleyPosK", associatedPulleyPos.k);
    }

    protected void readNbt(NbtCompound nbttagcompound) {
        associatedPulleyPos.i = nbttagcompound.getInt("associatedPulleyPosI");
        associatedPulleyPos.j = nbttagcompound.getInt("associatedPulleyPosJ");
        associatedPulleyPos.k = nbttagcompound.getInt("associatedPulleyPosK");
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

    public float getShadowRadius() {
        return 0.0F;
    }

    public void tick() {
        if (dead || world.isRemote) {
            return;
        }
        PulleyBlockEntity tileEntityPulley = null;
        int oldJ = MathHelper.floor(y - (double) standingEyeHeight);
        int i = MathHelper.floor(x);
        int k = MathHelper.floor(z);
        int associatedPulleyid = world.getBlockId(associatedPulleyPos.i, associatedPulleyPos.j, associatedPulleyPos.k);
        int iBlockAboveID = world.getBlockId(i, oldJ + 1, k);
        int i2BlockAboveID = world.getBlockId(i, oldJ + 2, k);
        if (associatedPulleyid == BlockListener.pulley.id && (iBlockAboveID == BlockListener.pulley.id || iBlockAboveID == BlockListener.rope.id || i2BlockAboveID == BlockListener.pulley.id || i2BlockAboveID == BlockListener.rope.id)) {
            tileEntityPulley = (PulleyBlockEntity) world.getBlockEntity(associatedPulleyPos.i, associatedPulleyPos.j, associatedPulleyPos.k);
            if (velocityY > 0.0D) {
                if (tileEntityPulley.isLowering()) {
                    velocityY = -velocityY;
                }
            } else if (tileEntityPulley.IsRaising()) {
                velocityY = -velocityY;
            }
        }
        MoveEntityInternal(velocityX, velocityY, velocityZ);
        double newPosY = y;
        int newJ = MathHelper.floor(newPosY - (double) standingEyeHeight);
        List list = world.getEntities(this, boundingBox.expand(0.0D, 0.14999999999999999D, 0.0D));
        if (list != null && list.size() > 0) {
            for (int j1 = 0; j1 < list.size(); j1++) {
                Entity entity = (Entity) list.get(j1);
                if (entity.isPushable() || (entity instanceof ItemEntity)) {
                    PushEntity(entity);
                    continue;
                }
                if (entity.dead) {
                    continue;
                }
                if (entity instanceof WaterWheelEntity entityWaterWheel) {
                    entityWaterWheel.destroyWithDrop();
                    continue;
                }
                if (entity instanceof WindMillEntity entityWindMill) {
                    entityWindMill.DestroyWithDrop();
                }
            }

        }
        if (oldJ != newJ) {
            if (velocityY > 0.0D) {
                int iTargetid = world.getBlockId(i, newJ + 1, k);
                if (iTargetid != BlockListener.rope.id || tileEntityPulley == null || !tileEntityPulley.IsRaising()) {
                    ConvertToBlock(i, newJ, k);
                    return;
                }
            } else {
                boolean bEnoughRope = false;
                if (tileEntityPulley != null) {
                    int iRopeRequiredToDescend = 2;
                    if (iBlockAboveID == BlockListener.pulley.id || iBlockAboveID == BlockListener.rope.id) {
                        iRopeRequiredToDescend = 1;
                        int iOldid = world.getBlockId(i, oldJ, k);
                        if (iOldid == BlockListener.pulley.id || iOldid == BlockListener.rope.id) {
                            iRopeRequiredToDescend = 0;
                        }
                    }
                    bEnoughRope = tileEntityPulley.GetContainedRopeCount() >= iRopeRequiredToDescend;
                }
                int iTargetid = world.getBlockId(i, newJ, k);
                boolean bStop = false;
                if (tileEntityPulley == null || !tileEntityPulley.isLowering() || !bEnoughRope) {
                    bStop = true;
                } else if (!ReplaceableBlockChecker.IsReplaceableBlock(world, i, newJ, k)) {
                    if (!Block.BLOCKS[iTargetid].material.isSolid()) {
                        if (iTargetid == BlockListener.rope.id) {
                            if (!ReturnRopeToPulley()) {
                                Block.BLOCKS[iTargetid].dropStacks(world, i, newJ, k, world.getBlockMeta(i, newJ, k));
                            }
                        } else {
                            Block.BLOCKS[iTargetid].dropStacks(world, i, newJ, k, world.getBlockMeta(i, newJ, k));
                        }
                        world.setBlock(i, newJ, k, 0);
                    } else {
                        bStop = true;
                    }
                }
                if (tileEntityPulley != null && world.getBlockId(i, oldJ + 1, k) != BlockListener.rope.id && world.getBlockId(i, oldJ + 1, k) != BlockListener.pulley.id) {
                    tileEntityPulley.AttemptToDispenseRope();
                }
                if (bStop) {
                    ConvertToBlock(i, oldJ, k);
                    return;
                }
            }
        }
        if (velocityY <= 0.01D && velocityY >= -0.01D) {
            ConvertToBlock(i, oldJ, k);
        } else {
        }
    }

    public void move(double deltaX, double deltaY, double deltaZ) {
        DestroyAnchorWithDrop();
    }

    public void DestroyAnchorWithDrop() {
        int i = MathHelper.floor(x);
        int j = MathHelper.floor(y);
        int k = MathHelper.floor(z);
        ItemStack anchorStack = new ItemStack(BlockListener.anchor);
        UnsortedUtils.ejectStackWithRandomOffset(world, i, j, k, anchorStack);
        markDead();
    }

    private void MoveEntityInternal(double deltaX, double deltaY, double deltaZ) {
        double newPosX = x + deltaX;
        double newPosY = y + deltaY;
        double newPosZ = z + deltaZ;
        prevX = x;
        prevY = y;
        prevZ = z;
        setPos(newPosX, newPosY, newPosZ);
        TestForBlockCollisions();
    }

    private void TestForBlockCollisions() {
        int i1 = MathHelper.floor(boundingBox.minX + 0.001D);
        int k1 = MathHelper.floor(boundingBox.minY + 0.001D);
        int i2 = MathHelper.floor(boundingBox.minZ + 0.001D);
        int k3 = MathHelper.floor(boundingBox.maxX - 0.001D);
        int l3 = MathHelper.floor(boundingBox.maxY - 0.001D);
        int i4 = MathHelper.floor(boundingBox.maxZ - 0.001D);
        if (world.isRegionLoaded(i1, k1, i2, k3, l3, i4)) {
            for (int j4 = i1; j4 <= k3; j4++) {
                for (int k4 = k1; k4 <= l3; k4++) {
                    for (int l4 = i2; l4 <= i4; l4++) {
                        int i5 = world.getBlockId(j4, k4, l4);
                        if (i5 > 0) {
                            Block.BLOCKS[i5].onEntityCollision(world, j4, k4, l4, this);
                        }
                    }

                }

            }

        }
    }

    private void PushEntity(Entity entity) {
        double anchorMaxY = boundingBox.maxY + 0.074999999999999997D;
        double entityMinY = entity.boundingBox.minY;
        if (entityMinY < anchorMaxY) {
            if (entityMinY > anchorMaxY - 0.25D) {
                double entityYOffset = anchorMaxY - entityMinY;
                entity.setPos(entity.x, entity.y + entityYOffset, entity.z);
                entity.fallDistance = 0.0F;
            } else if ((entity instanceof LivingEntity) && velocityY < 0.0D) {
                double entityMaxY = entity.boundingBox.maxY;
                double anchorMinY = boundingBox.minY;
                if (anchorMinY < entityMaxY - 0.25D) {
                    entity.damage(null, 1);
                }
            }
        }
    }

    public void ForceStopByPlatform() {
        if (dead) {
            return;
        }
        int i;
        int k;
        if (velocityY > 0.0D) {
            i = MathHelper.floor(x);
            int jAbove = MathHelper.floor(y) + 1;
            k = MathHelper.floor(z);
            int iBlockAboveID = world.getBlockId(i, jAbove, k);
            if (iBlockAboveID == BlockListener.rope.id) {
                ((RopeBlock) BlockListener.rope).BreakRope(world, i, jAbove, k);
            }
        }
        i = MathHelper.floor(x);
        int j = MathHelper.floor(y);
        k = MathHelper.floor(z);
        ConvertToBlock(i, j, k);
    }

    private void ConvertToBlock(int i, int j, int k) {
        boolean bCanPlace = true;
        int iTargetid = world.getBlockId(i, j, k);
        if (!ReplaceableBlockChecker.IsReplaceableBlock(world, i, j, k)) {
            if (iTargetid == BlockListener.rope.id) {
                if (!ReturnRopeToPulley()) {
                    UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, ItemListener.ropeItem.id, 0);
                }
            } else if (!Block.BLOCKS[iTargetid].material.isSolid()) {
                Block.BLOCKS[iTargetid].dropStacks(world, i, j, k, world.getBlockMeta(i, j, k));
                world.setBlock(i, j, k, BlockListener.platform.id);
            } else {
                bCanPlace = false;
            }
        }
        if (bCanPlace) {
            world.setBlock(i, j, k, BlockListener.anchor.id);
            ((AnchorBlock) BlockListener.anchor).setAnchorFacing(world, i, j, k, 1);
        } else {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, BlockListener.anchor.id, 0);
        }
        markDead();
    }

    public boolean ReturnRopeToPulley() {
        int associatedPulleyid = world.getBlockId(associatedPulleyPos.i, associatedPulleyPos.j, associatedPulleyPos.k);
        if (associatedPulleyid == BlockListener.pulley.id) {
            PulleyBlockEntity tileEntityPulley = (PulleyBlockEntity) world.getBlockEntity(associatedPulleyPos.i, associatedPulleyPos.j, associatedPulleyPos.k);
            if (tileEntityPulley != null) {
                tileEntityPulley.AddRopeToInventory();
                return true;
            }
        }
        return false;
    }

    public static final float fMovementSpeed = 0.05F;
    private final BlockPosition associatedPulleyPos;
    private double sentVel;

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(EntityListener.NAMESPACE, "MovingAnchor");
    }
}
