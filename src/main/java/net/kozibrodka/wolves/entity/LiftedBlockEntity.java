package net.kozibrodka.wolves.entity;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.EntityListener;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.RailBlock;
import net.minecraft.entity.Entity;
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
public class LiftedBlockEntity extends Entity implements EntitySpawnDataProvider
{

    public LiftedBlockEntity(World world)
    {
        super(world);
        field_1593 = true;
        setBoundingBoxSpacing(0.98F, 0.98F);
        eyeHeight = spacingY / 2.0F;
        velocityX = 0.0D;
        velocityY = 0.0D;
        velocityZ = 0.0D;
        m_iid = 0;
        m_iBlockMetaData = 0;
    }

    public LiftedBlockEntity(World world, int i, int j, int k)
    {
        this(world);
        m_iid = world.getBlockId(i, j, k);
        m_iBlockMetaData = world.getBlockMeta(i, j, k);
        if(m_iid == Block.POWERED_RAIL.id || m_iid == Block.DETECTOR_RAIL.id || m_iid == BlockListener.detectorRailWood.id || m_iid == BlockListener.detectorRailObsidian.id)
        {
            m_iBlockMetaData &= 7;
        } else
        if(m_iid == Block.REDSTONE_WIRE.id)
        {
            m_iBlockMetaData = 0;
        }
        method_1340((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F);
        field_1637 = prevX = x;
        field_1638 = prevY = y;
        field_1639 = prevZ = z;
        world.method_210(this);
        world.setBlock(i, j, k, 0);
    }

    public LiftedBlockEntity(World level, Double aDouble, Double aDouble1, Double aDouble2) {
        this(level);
    }


    protected void initDataTracker()
    {
    }

    protected void writeNbt(NbtCompound nbttagcompound)
    {
        nbttagcompound.putInt("m_iid", m_iid);
        nbttagcompound.putInt("m_iBlockMetaData", m_iBlockMetaData);
    }

    protected void readNbt(NbtCompound nbttagcompound)
    {
        m_iid = nbttagcompound.getInt("m_iid");
        m_iBlockMetaData = nbttagcompound.getInt("m_iBlockMetaData");
    }

    protected boolean bypassesSteppingEffects()
    {
        return false;
    }

    public Box method_1379(Entity entity)
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

    public void method_1353(Entity entity1)
    {
    }

    public float method_1366()
    {
        return 0.0F;
    }

    public void tick()
    {
        if(dead || world.isRemote)
        {
            return;
        }
        MovingPlatformEntity associatedMovingPlatform = null;
        List collisionList = world.method_175(MovingPlatformEntity.class, Box.createCached(x - 0.25D, y - 1.25D, z - 0.25D, x + 0.25D, y - 0.75D, z + 0.25D));
        if(collisionList != null && collisionList.size() > 0)
        {
            associatedMovingPlatform = (MovingPlatformEntity)collisionList.get(0);
            if(!associatedMovingPlatform.dead)
            {
                double newPosX = associatedMovingPlatform.x;
                double newPosY = associatedMovingPlatform.y + 1.0D;
                double newPosZ = associatedMovingPlatform.z;
                prevX = x;
                prevY = y;
                prevZ = z;
                method_1340(newPosX, newPosY, newPosZ);
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
        int idDropped = Block.BLOCKS[m_iid].getDroppedItemId(0, world.field_214);
        if(idDropped > 0)
        {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, idDropped, 0);
        }
        markDead();
    }

    private void ConvertToBlock(int i, int j, int k)
    {
        boolean bDestroyBlock = true;
        if(world.getBlockId(i, j - 1, k) == BlockListener.platform.id && ReplaceableBlockChecker.IsReplaceableBlock(world, i, j, k))
        {
            world.method_201(i, j, k, m_iid, m_iBlockMetaData);
            bDestroyBlock = false;
        }
        if(bDestroyBlock)
        {
            DestroyBlockWithDrop();
        } else
        {
            markDead();
        }
    }

    public static boolean CanBlockBeConvertedToEntity(World world, int i, int j, int k)
    {
        int iTargetid = world.getBlockId(i, j, k);
        Block targetBlock = Block.BLOCKS[iTargetid];
        if(targetBlock != null)
        {
            if(targetBlock instanceof RailBlock)
            {
                int iTargetMetaData = world.getBlockMeta(i, j, k);
                return !((iTargetMetaData >= 2) & (iTargetMetaData <= 5));
            }
            if(iTargetid == Block.REDSTONE_WIRE.id)
            {
                return true;
            }
        }
        return false;
    }

    public int m_iid;
    public int m_iBlockMetaData;

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(EntityListener.MOD_ID, "BlockLiftedByPlatform");
    }
}
