package net.kozibrodka.wolves.entity;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.EntityListener;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.kozibrodka.wolves.utils.ReplaceableBlockChecker;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Item;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;

@HasTrackingParameters(trackingDistance = 160, updatePeriod = 2)
public class MovingPlatformEntity extends EntityBase implements EntitySpawnDataProvider
{

    public MovingPlatformEntity(Level world)
    {
        super(world);
        field_1593 = true;
        setSize(0.98F, 0.98F);
        standingEyeHeight = height / 2.0F;
        velocityX = 0.0D;
        velocityY = 0.0D;
        velocityZ = 0.0D;
        m_AssociatedAnchorLastKnownXPos = 0.0D;
        m_AssociatedAnchorLastKnownYPos = 0.0D;
        m_AssociatedAnchorLastKnownZPos = 0.0D;
    }

    public MovingPlatformEntity(Level world, double x, double y, double z,
                                MovingAnchorEntity entityMovingAnchor)
    {
        this(world);
        m_AssociatedAnchorLastKnownXPos = entityMovingAnchor.x;
        m_AssociatedAnchorLastKnownYPos = entityMovingAnchor.y;
        m_AssociatedAnchorLastKnownZPos = entityMovingAnchor.z;
        velocityY = entityMovingAnchor.velocityY;
        setPosition(x, y, z);
        prevRenderX = prevX = x;
        prevRenderY = prevY = y;
        prevRenderZ = prevZ = z;
    }

    public MovingPlatformEntity(Level level, Double aDouble, Double aDouble1, Double aDouble2) {
        this(level);
    }

    protected void initDataTracker()
    {
    }

    protected void writeCustomDataToTag(CompoundTag nbttagcompound)
    {
        nbttagcompound.put("m_AssociatedAnchorLastKnownXPos", m_AssociatedAnchorLastKnownXPos);
        nbttagcompound.put("m_AssociatedAnchorLastKnownYPos", m_AssociatedAnchorLastKnownYPos);
        nbttagcompound.put("m_AssociatedAnchorLastKnownZPos", m_AssociatedAnchorLastKnownZPos);
    }

    protected void readCustomDataFromTag(CompoundTag nbttagcompound)
    {
        m_AssociatedAnchorLastKnownXPos = nbttagcompound.getDouble("m_AssociatedAnchorLastKnownXPos");
        m_AssociatedAnchorLastKnownYPos = nbttagcompound.getDouble("m_AssociatedAnchorLastKnownYPos");
        m_AssociatedAnchorLastKnownZPos = nbttagcompound.getDouble("m_AssociatedAnchorLastKnownZPos");
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

    public float getEyeHeight()
    {
        return 0.0F;
    }

    public void tick()
    {
        if(removed)
        {
            return;
        }
        int i = MathHelper.floor(x);
        int oldCentreJ = MathHelper.floor(y);
        int k = MathHelper.floor(z);
        MovingAnchorEntity associatedMovingAnchor = null;
        List list = level.getEntities(MovingAnchorEntity.class, Box.createButWasteMemory(m_AssociatedAnchorLastKnownXPos - 0.25D, m_AssociatedAnchorLastKnownYPos - 0.25D, m_AssociatedAnchorLastKnownZPos - 0.25D, m_AssociatedAnchorLastKnownXPos + 0.25D, m_AssociatedAnchorLastKnownYPos + 0.25D, m_AssociatedAnchorLastKnownZPos + 0.25D));
        if(list != null && list.size() > 0)
        {
            associatedMovingAnchor = (MovingAnchorEntity)list.get(0);
            if(!associatedMovingAnchor.removed)
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
        list = level.getEntities(this, boundingBox.expand(0.0D, 0.14999999999999999D, 0.0D));
        if(list != null && list.size() > 0)
        {
            for(int j1 = 0; j1 < list.size(); j1++)
            {
                EntityBase entity = (EntityBase)list.get(j1);
                if(entity.method_1380() || (entity instanceof Item))
                {
                    PushEntity(entity);
                    continue;
                }
                if(entity.removed)
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
                int iTargetid = level.getTileId(i, newTopJ, k);
                if(!ReplaceableBlockChecker.IsReplaceableBlock(level, i, newTopJ, k))
                {
                    if(!BlockBase.BY_ID[iTargetid].material.isSolid())
                    {
                        if(iTargetid == BlockListener.rope.id)
                        {
                            if(!associatedMovingAnchor.ReturnRopeToPulley())
                            {
                                BlockBase.BY_ID[iTargetid].drop(level, i, newTopJ, k, level.getTileMeta(i, newTopJ, k));
                            }
                        } else
                        {
                            BlockBase.BY_ID[iTargetid].drop(level, i, newTopJ, k, level.getTileMeta(i, newTopJ, k));
                        }
                        level.setTile(i, newTopJ, k, 0);
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
                int iTargetid = level.getTileId(i, newBottomJ, k);
                if(!ReplaceableBlockChecker.IsReplaceableBlock(level, i, newBottomJ, k))
                {
                    if(!BlockBase.BY_ID[iTargetid].material.isSolid())
                    {
                        if(iTargetid == BlockListener.rope.id)
                        {
                            if(!associatedMovingAnchor.ReturnRopeToPulley())
                            {
                                BlockBase.BY_ID[iTargetid].drop(level, i, newBottomJ, k, level.getTileMeta(i, newBottomJ, k));
                            }
                        } else
                        {
                            BlockBase.BY_ID[iTargetid].drop(level, i, newBottomJ, k, level.getTileMeta(i, newBottomJ, k));
                        }
                        level.setTile(i, newBottomJ, k, 0);
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
        ItemInstance platformStack = new ItemInstance(BlockListener.platform);
        UnsortedUtils.EjectStackWithRandomOffset(level, i, j, k, platformStack);
        remove();
    }

    private void MoveEntityInternal(double deltaX, double deltaY, double deltaZ)
    {
        double newPosX = x + deltaX;
        double newPosY = y + deltaY;
        double newPosZ = z + deltaZ;
        prevX = x;
        prevY = y;
        prevZ = z;
        setPosition(newPosX, newPosY, newPosZ);
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
        if(level.method_155(i1, k1, i2, k3, l3, i4))
        {
            for(int j4 = i1; j4 <= k3; j4++)
            {
                for(int k4 = k1; k4 <= l3; k4++)
                {
                    for(int l4 = i2; l4 <= i4; l4++)
                    {
                        int i5 = level.getTileId(j4, k4, l4);
                        if(i5 > 0)
                        {
                            BlockBase.BY_ID[i5].onEntityCollision(level, j4, k4, l4, this);
                        }
                    }
                }
            }
        }
    }

    private void PushEntity(EntityBase entity)
    {
        // if(true)return;
    	double platformMaxY = boundingBox.maxY + 0.074999999999999997D;
        double entityMinY = entity.boundingBox.minY;
        if(entityMinY < platformMaxY)
        {
            if(entityMinY > platformMaxY - 0.25D)
            {
                double entityYOffset = platformMaxY - entityMinY;
                entity.setPosition(entity.x, entity.y + entityYOffset, entity.z);
            } else
            if((entity instanceof Living) && velocityY < 0.0D)
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
        int iTargetid = level.getTileId(i, j, k);
        if(ReplaceableBlockChecker.IsReplaceableBlock(level, i, j, k))
        {
            level.setTile(i, j, k, BlockListener.platform.id);
        } else
        if(!BlockBase.BY_ID[iTargetid].material.isSolid())
        {
            if(iTargetid == BlockListener.rope.id && associatedAnchor != null)
            {
                if(!associatedAnchor.ReturnRopeToPulley())
                {
                    BlockBase.BY_ID[iTargetid].drop(level, i, j, k, level.getTileMeta(i, j, k));
                }
            } else
            {
                BlockBase.BY_ID[iTargetid].drop(level, i, j, k, level.getTileMeta(i, j, k));
            }
            level.setTile(i, j, k, BlockListener.platform.id);
        } else
        {
            UnsortedUtils.EjectSingleItemWithRandomOffset(level, i, j, k, BlockListener.platform.id, 0);
            moveEntities = false;
        }
        UnsortedUtils.PositionAllMoveableEntitiesOutsideOfLocation(level, i, j, k);
        remove();
    }

    private double m_AssociatedAnchorLastKnownXPos;
    private double m_AssociatedAnchorLastKnownYPos;
    private double m_AssociatedAnchorLastKnownZPos;

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(EntityListener.MOD_ID, "MovingPlatform");
    }
}
