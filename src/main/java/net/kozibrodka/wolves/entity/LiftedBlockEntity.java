package net.kozibrodka.wolves.entity;

import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.kozibrodka.wolves.utils.ReplaceableBlockChecker;
import net.minecraft.block.BlockBase;
import net.minecraft.block.Rail;
import net.minecraft.entity.EntityBase;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.MathHelper;

import java.util.List;

public class LiftedBlockEntity extends EntityBase
{

    public LiftedBlockEntity(Level world)
    {
        super(world);
        field_1593 = true;
        setSize(0.98F, 0.98F);
        standingEyeHeight = height / 2.0F;
        velocityX = 0.0D;
        velocityY = 0.0D;
        velocityZ = 0.0D;
        m_iid = 0;
        m_iBlockMetaData = 0;
    }

    public LiftedBlockEntity(Level world, int i, int j, int k)
    {
        this(world);
        m_iid = world.getTileId(i, j, k);
        m_iBlockMetaData = world.getTileMeta(i, j, k);
        if(m_iid == BlockBase.GOLDEN_RAIL.id || m_iid == BlockBase.DETECTOR_RAIL.id || m_iid == mod_FCBetterThanWolves.fcDetectorRailWood.id || m_iid == mod_FCBetterThanWolves.fcDetectorRailObsidian.id)
        {
            m_iBlockMetaData &= 7;
        } else
        if(m_iid == BlockBase.REDSTONE_DUST.id)
        {
            m_iBlockMetaData = 0;
        }
        setPosition((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F);
        prevRenderX = prevX = x;
        prevRenderY = prevY = y;
        prevRenderZ = prevZ = z;
        world.spawnEntity(this);
        world.setTile(i, j, k, 0);
    }

    public LiftedBlockEntity(Level level, Double aDouble, Double aDouble1, Double aDouble2) {
        this(level);
    }


    protected void initDataTracker()
    {
    }

    protected void writeCustomDataToTag(CompoundTag nbttagcompound)
    {
        nbttagcompound.put("m_iid", m_iid);
        nbttagcompound.put("m_iBlockMetaData", m_iBlockMetaData);
    }

    protected void readCustomDataFromTag(CompoundTag nbttagcompound)
    {
        m_iid = nbttagcompound.getInt("m_iid");
        m_iBlockMetaData = nbttagcompound.getInt("m_iBlockMetaData");
    }

    protected boolean canClimb()
    {
        return false;
    }

    public Box getBoundingBox(EntityBase entity)
    {
        return null;
    }

    public Box method_1381()
    {
        return null;
    }

    public boolean method_1380()
    {
        return false;
    }

    public boolean method_1356()
    {
        return false;
    }

    public void method_1353(EntityBase entity1)
    {
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
        MovingPlatformEntity associatedMovingPlatform = null;
        List collisionList = level.getEntities(MovingPlatformEntity.class, Box.createButWasteMemory(x - 0.25D, y - 1.25D, z - 0.25D, x + 0.25D, y - 0.75D, z + 0.25D));
        if(collisionList != null && collisionList.size() > 0)
        {
            associatedMovingPlatform = (MovingPlatformEntity)collisionList.get(0);
            if(!associatedMovingPlatform.removed)
            {
                double newPosX = associatedMovingPlatform.x;
                double newPosY = associatedMovingPlatform.y + 1.0D;
                double newPosZ = associatedMovingPlatform.z;
                prevX = x;
                prevY = y;
                prevZ = z;
                setPosition(newPosX, newPosY, newPosZ);
            } else
            {
                associatedMovingPlatform = null;
            }
        }
        if(associatedMovingPlatform == null)
        {
            int i = MathHelper.floor(x);
            int j = MathHelper.floor(y);
            int k = MathHelper.floor(z);
            ConvertToBlock(i, j, k);
        }
    }

    public void move(double deltaX, double deltaY, double deltaZ)
    {
        DestroyBlockWithDrop();
    }

    public void DestroyBlockWithDrop()
    {
        int i = MathHelper.floor(x);
        int j = MathHelper.floor(y);
        int k = MathHelper.floor(z);
        int idDropped = BlockBase.BY_ID[m_iid].getDropId(0, level.rand);
        if(idDropped > 0)
        {
            UnsortedUtils.EjectSingleItemWithRandomOffset(level, i, j, k, idDropped, 0);
        }
        remove();
    }

    private void ConvertToBlock(int i, int j, int k)
    {
        boolean bDestroyBlock = true;
        if(level.getTileId(i, j - 1, k) == mod_FCBetterThanWolves.fcPlatform.id && ReplaceableBlockChecker.IsReplaceableBlock(level, i, j, k))
        {
            level.placeBlockWithMetaData(i, j, k, m_iid, m_iBlockMetaData);
            bDestroyBlock = false;
        }
        if(bDestroyBlock)
        {
            DestroyBlockWithDrop();
        } else
        {
            remove();
        }
    }

    public static boolean CanBlockBeConvertedToEntity(Level world, int i, int j, int k)
    {
        int iTargetid = world.getTileId(i, j, k);
        BlockBase targetBlock = BlockBase.BY_ID[iTargetid];
        if(targetBlock != null)
        {
            if(targetBlock instanceof Rail)
            {
                int iTargetMetaData = world.getTileMeta(i, j, k);
                return !((iTargetMetaData >= 2) & (iTargetMetaData <= 5));
            }
            if(iTargetid == BlockBase.REDSTONE_DUST.id)
            {
                return true;
            }
        }
        return false;
    }

    public int m_iid;
    public int m_iBlockMetaData;
}
