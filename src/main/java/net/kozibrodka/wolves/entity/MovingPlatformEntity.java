package net.kozibrodka.wolves.entity;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.EntityListener;
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
import net.kozibrodka.wolves.utils.ReplaceableBlockChecker;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;

@HasTrackingParameters(trackingDistance = 160, updatePeriod = 2)
public class MovingPlatformEntity extends Entity implements EntitySpawnDataProvider
{

    public MovingPlatformEntity(World world)
    {
        super(world);
        blocksSameBlockSpawning = true;
        setBoundingBoxSpacing(0.98F, 0.98F);
        standingEyeHeight = height / 2.0F;
        velocityX = 0.0D;
        velocityY = 0.0D;
        velocityZ = 0.0D;
        m_AssociatedAnchorLastKnownXPos = 0.0D;
        m_AssociatedAnchorLastKnownYPos = 0.0D;
        m_AssociatedAnchorLastKnownZPos = 0.0D;
    }

    public MovingPlatformEntity(World world, double x, double y, double z,
                                MovingAnchorEntity entityMovingAnchor)
    {
        this(world);
        m_AssociatedAnchorLastKnownXPos = entityMovingAnchor.x;
        m_AssociatedAnchorLastKnownYPos = entityMovingAnchor.y;
        m_AssociatedAnchorLastKnownZPos = entityMovingAnchor.z;
        velocityY = entityMovingAnchor.velocityY;
        setPos(x, y, z);
        lastTickX = prevX = x;
        lastTickY = prevY = y;
        lastTickZ = prevZ = z;
    }

    public MovingPlatformEntity(World level, Double aDouble, Double aDouble1, Double aDouble2) {
        this(level);
    }

    protected void initDataTracker()
    {
    }

    protected void writeNbt(NbtCompound nbttagcompound)
    {
        nbttagcompound.putDouble("m_AssociatedAnchorLastKnownXPos", m_AssociatedAnchorLastKnownXPos);
        nbttagcompound.putDouble("m_AssociatedAnchorLastKnownYPos", m_AssociatedAnchorLastKnownYPos);
        nbttagcompound.putDouble("m_AssociatedAnchorLastKnownZPos", m_AssociatedAnchorLastKnownZPos);
    }

    protected void readNbt(NbtCompound nbttagcompound)
    {
        m_AssociatedAnchorLastKnownXPos = nbttagcompound.getDouble("m_AssociatedAnchorLastKnownXPos");
        m_AssociatedAnchorLastKnownYPos = nbttagcompound.getDouble("m_AssociatedAnchorLastKnownYPos");
        m_AssociatedAnchorLastKnownZPos = nbttagcompound.getDouble("m_AssociatedAnchorLastKnownZPos");
    }

    protected boolean bypassesSteppingEffects()
    {
        return false;
    }

    public Box method_1379(Entity entity)
    {
        return entity.boundingBox;
    }

    public Box getBoundingBox()
    {
        return boundingBox;
    }

    public boolean isPushable()
    {
        return false;
    }

    public boolean isCollidable()
    {
        return !dead;
    }

    public float getShadowRadius()
    {
        return 0.0F;
    }

    public void tick()
    {
        if(dead || world.isRemote)
        {
            return;
        }
        int i = MathHelper.floor(x);
        int oldCentreJ = MathHelper.floor(y);
        int k = MathHelper.floor(z);
        MovingAnchorEntity associatedMovingAnchor = null;
        List list = world.collectEntitiesByClass(MovingAnchorEntity.class, Box.createCached(m_AssociatedAnchorLastKnownXPos - 0.25D, m_AssociatedAnchorLastKnownYPos - 0.25D, m_AssociatedAnchorLastKnownZPos - 0.25D, m_AssociatedAnchorLastKnownXPos + 0.25D, m_AssociatedAnchorLastKnownYPos + 0.25D, m_AssociatedAnchorLastKnownZPos + 0.25D));
        if(list != null && list.size() > 0)
        {
            associatedMovingAnchor = (MovingAnchorEntity)list.get(0);
            if(!associatedMovingAnchor.dead)
            {
                velocityY = associatedMovingAnchor.velocityY;
                m_AssociatedAnchorLastKnownXPos = associatedMovingAnchor.x;
                m_AssociatedAnchorLastKnownYPos = associatedMovingAnchor.y;
                m_AssociatedAnchorLastKnownZPos = associatedMovingAnchor.z;
            } else
            {
                associatedMovingAnchor = null;
            }
        }
        double oldPosY = y;
        MoveEntityInternal(velocityX, velocityY, velocityZ);
        double newPosY = y;
        list = world.getEntities(this, boundingBox.expand(0.0D, 0.14999999999999999D, 0.0D));
        if(list != null && list.size() > 0)
        {
            for(int j1 = 0; j1 < list.size(); j1++)
            {
                Entity entity = (Entity)list.get(j1);
                if(entity.isPushable() || (entity instanceof ItemEntity))
                {
                    PushEntity(entity);
                    continue;
                }
                if(entity.dead)
                {
                    continue;
                }
                if(entity instanceof WaterWheelEntity)
                {
                    WaterWheelEntity entityWaterWheel = (WaterWheelEntity)entity;
                    entityWaterWheel.DestroyWithDrop();
                    continue;
                }
                if(entity instanceof WindMillEntity)
                {
                    WindMillEntity entityWindMill = (WindMillEntity)entity;
                    entityWindMill.DestroyWithDrop();
                }
            }

        }
        if(associatedMovingAnchor == null)
        {
            ConvertToBlock(i, oldCentreJ, k, null);
            return;
        }
        if(velocityY > 0.0D)
        {
            int newTopJ = MathHelper.floor(newPosY + 0.49000000953674316D);
            int oldTopJ = MathHelper.floor(oldPosY + 0.49000000953674316D);
            if(newTopJ != oldTopJ)
            {
                int iTargetid = world.getBlockId(i, newTopJ, k);
                if(!ReplaceableBlockChecker.IsReplaceableBlock(world, i, newTopJ, k))
                {
                    if(!Block.BLOCKS[iTargetid].material.isSolid())
                    {
                        if(iTargetid == BlockListener.rope.id)
                        {
                            if(!associatedMovingAnchor.ReturnRopeToPulley())
                            {
                                Block.BLOCKS[iTargetid].dropStacks(world, i, newTopJ, k, world.getBlockMeta(i, newTopJ, k));
                            }
                        } else
                        {
                            Block.BLOCKS[iTargetid].dropStacks(world, i, newTopJ, k, world.getBlockMeta(i, newTopJ, k));
                        }
                        world.setBlock(i, newTopJ, k, 0);
                    } else
                    {
                        ConvertToBlock(i, oldTopJ, k, associatedMovingAnchor);
                        associatedMovingAnchor.ForceStopByPlatform();
                        return;
                    }
                }
            }
        } else
        {
            int newBottomJ = MathHelper.floor(newPosY - 0.49000000953674316D);
            int oldBottomJ = MathHelper.floor(oldPosY - 0.49000000953674316D);
            if(oldBottomJ != newBottomJ)
            {
                int iTargetid = world.getBlockId(i, newBottomJ, k);
                if(!ReplaceableBlockChecker.IsReplaceableBlock(world, i, newBottomJ, k))
                {
                    if(!Block.BLOCKS[iTargetid].material.isSolid())
                    {
                        if(iTargetid == BlockListener.rope.id)
                        {
                            if(!associatedMovingAnchor.ReturnRopeToPulley())
                            {
                                Block.BLOCKS[iTargetid].dropStacks(world, i, newBottomJ, k, world.getBlockMeta(i, newBottomJ, k));
                            }
                        } else
                        {
                            Block.BLOCKS[iTargetid].dropStacks(world, i, newBottomJ, k, world.getBlockMeta(i, newBottomJ, k));
                        }
                        world.setBlock(i, newBottomJ, k, 0);
                    } else
                    {
                        ConvertToBlock(i, oldBottomJ, k, associatedMovingAnchor);
                        associatedMovingAnchor.ForceStopByPlatform();
                        return;
                    }
                }
            }
        }
    }

    public void move(double deltaX, double deltaY, double deltaZ)
    {
        DestroyPlatformWithDrop();
    }

    public void DestroyPlatformWithDrop()
    {
        int i = MathHelper.floor(x);
        int j = MathHelper.floor(y);
        int k = MathHelper.floor(z);
        ItemStack platformStack = new ItemStack(BlockListener.platform);
        UnsortedUtils.ejectStackWithRandomOffset(world, i, j, k, platformStack);
        markDead();
    }

    private void MoveEntityInternal(double deltaX, double deltaY, double deltaZ)
    {
        double newPosX = x + deltaX;
        double newPosY = y + deltaY;
        double newPosZ = z + deltaZ;
        prevX = x;
        prevY = y;
        prevZ = z;
        setPos(newPosX, newPosY, newPosZ);
        TestForBlockCollisions();
    }

    private void TestForBlockCollisions()
    {
        int i1 = MathHelper.floor(boundingBox.minX + 0.001D);
        int k1 = MathHelper.floor(boundingBox.minY + 0.001D);
        int i2 = MathHelper.floor(boundingBox.minZ + 0.001D);
        int k3 = MathHelper.floor(boundingBox.maxX - 0.001D);
        int l3 = MathHelper.floor(boundingBox.maxY - 0.001D);
        int i4 = MathHelper.floor(boundingBox.maxZ - 0.001D);
        if(world.isRegionLoaded(i1, k1, i2, k3, l3, i4))
        {
            for(int j4 = i1; j4 <= k3; j4++)
            {
                for(int k4 = k1; k4 <= l3; k4++)
                {
                    for(int l4 = i2; l4 <= i4; l4++)
                    {
                        int i5 = world.getBlockId(j4, k4, l4);
                        if(i5 > 0)
                        {
                            Block.BLOCKS[i5].onEntityCollision(world, j4, k4, l4, this);
                        }
                    }
                }
            }
        }
    }

    private void PushEntity(Entity entity)
    {
        // if(true)return;
    	double platformMaxY = boundingBox.maxY + 0.074999999999999997D;
        double entityMinY = entity.boundingBox.minY;
        if(entityMinY < platformMaxY)
        {
            if(entityMinY > platformMaxY - 0.25D)
            {
                double entityYOffset = platformMaxY - entityMinY;
                entity.setPos(entity.x, entity.y + entityYOffset, entity.z);
            } else
            if((entity instanceof LivingEntity) && velocityY < 0.0D)
            {
                double entityMaxY = entity.boundingBox.maxY;
                double platformMinY = boundingBox.minY;
                if(platformMinY < entityMaxY - 0.25D)
                {
                    entity.damage(null, 1);
                }
            }
        }
    }

    private void ConvertToBlock(int i, int j, int k, MovingAnchorEntity associatedAnchor)
    {
        boolean moveEntities = true;
        int iTargetid = world.getBlockId(i, j, k);
        if(ReplaceableBlockChecker.IsReplaceableBlock(world, i, j, k))
        {
            world.setBlock(i, j, k, BlockListener.platform.id);
        } else
        if(!Block.BLOCKS[iTargetid].material.isSolid())
        {
            if(iTargetid == BlockListener.rope.id && associatedAnchor != null)
            {
                if(!associatedAnchor.ReturnRopeToPulley())
                {
                    Block.BLOCKS[iTargetid].dropStacks(world, i, j, k, world.getBlockMeta(i, j, k));
                }
            } else
            {
                Block.BLOCKS[iTargetid].dropStacks(world, i, j, k, world.getBlockMeta(i, j, k));
            }
            world.setBlock(i, j, k, BlockListener.platform.id);
        } else
        {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, BlockListener.platform.id, 0);
            moveEntities = false;
        }
        UnsortedUtils.PositionAllMoveableEntitiesOutsideOfLocation(world, i, j, k);
        markDead();
    }

    private double m_AssociatedAnchorLastKnownXPos;
    private double m_AssociatedAnchorLastKnownYPos;
    private double m_AssociatedAnchorLastKnownZPos;

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(EntityListener.MOD_ID, "MovingPlatform");
    }
}
