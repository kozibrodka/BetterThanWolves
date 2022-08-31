//package net.kozibrodka.wolves.entity;
//import better_than_canis_lupus.mixin.EntityBaseAccessor;
//import net.minecraft.block.BlockBase;
//import net.minecraft.entity.EntityBase;
//import net.minecraft.entity.Item;
//import net.minecraft.entity.Living;
//import net.minecraft.item.ItemInstance;
//import net.minecraft.level.Level;
//import net.minecraft.util.io.CompoundTag;
//import net.minecraft.util.maths.Box;
//import net.minecraft.util.maths.MathHelper;
//
//import java.util.List;
//
//
//public class FCEntityMovingAnchor extends EntityBase
//{
//
//    public FCEntityMovingAnchor(Level world)
//    {
//        super(world);
//        associatedPulleyPos = new FCBlockPos();
//        field_1593 = true;
//        setSize(0.98F, FCBlockAnchor.fAnchorBaseHeight - 0.02F);
//        standingEyeHeight = height / 2.0F;
//        velocityX = 0.0D;
//        velocityY = 0.0D;
//        velocityZ = 0.0D;
//    }
//
//    public FCEntityMovingAnchor(Level world, double x, double y, double z,
//            FCBlockPos pulleyPos, int iMovementDirection)
//    {
//        this(world);
//        associatedPulleyPos.i = pulleyPos.i;
//        associatedPulleyPos.j = pulleyPos.j;
//        associatedPulleyPos.k = pulleyPos.k;
//        if(iMovementDirection > 0)
//        {
//            velocityY = 0.05000000074505806D;
//        } else
//        {
//            velocityY = -0.05000000074505806D;
//        }
//        setPosition(x, y, z);
//        prevRenderX = prevX = x;
//        prevRenderY = prevY = y;
//        prevRenderZ = prevZ = z;
//    }
//
//    protected void initDataTracker()
//    {
//    }
//
//    protected void writeCustomDataToTag(CompoundTag nbttagcompound)
//    {
//        nbttagcompound.put("associatedPulleyPosI", associatedPulleyPos.i);
//        nbttagcompound.put("associatedPulleyPosJ", associatedPulleyPos.j);
//        nbttagcompound.put("associatedPulleyPosK", associatedPulleyPos.k);
//    }
//
//    protected void readCustomDataFromTag(CompoundTag nbttagcompound)
//    {
//        associatedPulleyPos.i = nbttagcompound.getInt("associatedPulleyPosI");
//        associatedPulleyPos.j = nbttagcompound.getInt("associatedPulleyPosJ");
//        associatedPulleyPos.k = nbttagcompound.getInt("associatedPulleyPosK");
//    }
//
//    protected boolean canClimb()
//    {
//        return false;
//    }
//
//    public Box getBoundingBox(EntityBase entity)
//    {
//        return entity.boundingBox;
//    }
//
//    public Box method_1381()
//    {
//        return boundingBox;
//    }
//
//    public boolean method_1380()
//    {
//        return false;
//    }
//
//    public boolean method_1356()
//    {
//        return !removed;
//    }
//
//    public float getEyeHeight()
//    {
//        return 0.0F;
//    }
//
//    public void tick()
//    {
//        if(removed)
//        {
//            return;
//        }
//
//        if(sentVel != velocityY) {
//        	sentVel = velocityY;
//        	Packet230ModLoader packet = new Packet230ModLoader();
//        	packet.packetType = 2;
//        	packet.dataInt = new int[] {entityId};
//        	packet.dataFloat = new float[] {(float)velocityY};
//        	ModLoaderMp.SendPacketToAll(ModLoaderMp.GetModInstance(net.minecraft.src.mod_FCBetterThanWolves.class), packet);
//        }
//
//        FCTileEntityPulley tileEntityPulley = null;
//        int oldJ = MathHelper.floor(y - (double)standingEyeHeight);
//        int i = MathHelper.floor(x);
//        int k = MathHelper.floor(z);
//        int associatedPulleyid = level.getTileId(associatedPulleyPos.i, associatedPulleyPos.j, associatedPulleyPos.k);
//        int iBlockAboveID = level.getTileId(i, oldJ + 1, k);
//        int i2BlockAboveID = level.getTileId(i, oldJ + 2, k);
//        if(associatedPulleyid == mod_FCBetterThanWolves.fcPulley.id && (iBlockAboveID == mod_FCBetterThanWolves.fcPulley.id || iBlockAboveID == mod_FCBetterThanWolves.fcRopeBlock.id || i2BlockAboveID == mod_FCBetterThanWolves.fcPulley.id || i2BlockAboveID == mod_FCBetterThanWolves.fcRopeBlock.id))
//        {
//            tileEntityPulley = (FCTileEntityPulley)level.getTileEntity(associatedPulleyPos.i, associatedPulleyPos.j, associatedPulleyPos.k);
//            if(velocityY > 0.0D)
//            {
//                if(tileEntityPulley.IsLowering())
//                {
//                    velocityY = -velocityY;
//                }
//            } else
//            if(tileEntityPulley.IsRaising())
//            {
//                velocityY = -velocityY;
//            }
//        }
//        MoveEntityInternal(velocityX, velocityY, velocityZ);
//        double newPosY = y;
//        int newJ = MathHelper.floor(newPosY - (double)standingEyeHeight);
//        List list = level.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(0.0D, 0.14999999999999999D, 0.0D));
//        if(list != null && list.size() > 0)
//        {
//            for(int j1 = 0; j1 < list.size(); j1++)
//            {
//                EntityBase entity = (EntityBase)list.get(j1);
//                if(entity.canBePushed() || (entity instanceof Item))
//                {
//                    PushEntity(entity);
//                    continue;
//                }
//                if(entity.removed)
//                {
//                    continue;
//                }
//                if(entity instanceof FCEntityWaterWheel)
//                {
//                    FCEntityWaterWheel entityWaterWheel = (FCEntityWaterWheel)entity;
//                    entityWaterWheel.DestroyWithDrop();
//                    continue;
//                }
//                if(entity instanceof FCEntityWindMill)
//                {
//                    FCEntityWindMill entityWindMill = (FCEntityWindMill)entity;
//                    entityWindMill.DestroyWithDrop();
//                }
//            }
//
//        }
//        if(oldJ != newJ)
//        {
//            if(velocityY > 0.0D)
//            {
//                int iTargetid = level.getTileId(i, newJ + 1, k);
//                if(iTargetid != mod_FCBetterThanWolves.fcRopeBlock.id || tileEntityPulley == null || !tileEntityPulley.IsRaising())
//                {
//                    ConvertToBlock(i, newJ, k);
//                    return;
//                }
//            } else
//            {
//                boolean bEnoughRope = false;
//                if(tileEntityPulley != null)
//                {
//                    int iRopeRequiredToDescend = 2;
//                    if(iBlockAboveID == mod_FCBetterThanWolves.fcPulley.id || iBlockAboveID == mod_FCBetterThanWolves.fcRopeBlock.id)
//                    {
//                        iRopeRequiredToDescend = 1;
//                        int iOldid = level.getTileId(i, oldJ, k);
//                        if(iOldid == mod_FCBetterThanWolves.fcPulley.id || iOldid == mod_FCBetterThanWolves.fcRopeBlock.id)
//                        {
//                            iRopeRequiredToDescend = 0;
//                        }
//                    }
//                    if(tileEntityPulley.GetContainedRopeCount() >= iRopeRequiredToDescend)
//                    {
//                        bEnoughRope = true;
//                    } else
//                    {
//                        bEnoughRope = false;
//                    }
//                }
//                int iTargetid = level.getTileId(i, newJ, k);
//                boolean bStop = false;
//                if(tileEntityPulley == null || !tileEntityPulley.IsLowering() || !bEnoughRope)
//                {
//                    bStop = true;
//                } else
//                if(!FCUtilsWorld.IsReplaceableBlock(level, i, newJ, k))
//                {
//                    if(!BlockBase.BY_ID[iTargetid].material.isSolid())
//                    {
//                        if(iTargetid == mod_FCBetterThanWolves.fcRopeBlock.id)
//                        {
//                            if(!ReturnRopeToPulley())
//                            {
//                                BlockBase.BY_ID[iTargetid].drop(level, i, newJ, k, level.getTileMeta(i, newJ, k));
//                            }
//                        } else
//                        {
//                            BlockBase.BY_ID[iTargetid].drop(level, i, newJ, k, level.getTileMeta(i, newJ, k));
//                        }
//                        level.setTile(i, newJ, k, 0);
//                    } else
//                    {
//                        bStop = true;
//                    }
//                }
//                if(tileEntityPulley != null && level.getTileId(i, oldJ + 1, k) != mod_FCBetterThanWolves.fcRopeBlock.id && level.getTileId(i, oldJ + 1, k) != mod_FCBetterThanWolves.fcPulley.id)
//                {
//                    tileEntityPulley.AttemptToDispenseRope();
//                }
//                if(bStop)
//                {
//                    ConvertToBlock(i, oldJ, k);
//                    return;
//                }
//            }
//        }
//        if(velocityY <= 0.01D && velocityY >= -0.01D)
//        {
//            ConvertToBlock(i, oldJ, k);
//            return;
//        } else
//        {
//            return;
//        }
//    }
//
//    public void move(double deltaX, double deltaY, double deltaZ)
//    {
//        DestroyAnchorWithDrop();
//    }
//
//    public void DestroyAnchorWithDrop()
//    {
//        int i = MathHelper.floor(x);
//        int j = MathHelper.floor(y);
//        int k = MathHelper.floor(z);
//        ItemInstance anchorStack = new ItemInstance(mod_FCBetterThanWolves.fcAnchor);
//        FCUtilsMisc.EjectStackWithRandomOffset(level, i, j, k, anchorStack);
//        remove();
//    }
//
//    private void MoveEntityInternal(double deltaX, double deltaY, double deltaZ)
//    {
//        double newPosX = x + deltaX;
//        double newPosY = y + deltaY;
//        double newPosZ = z + deltaZ;
//        prevX = x;
//        prevY = y;
//        prevZ = z;
//        setPosition(newPosX, newPosY, newPosZ);
//        TestForBlockCollisions();
//    }
//
//    private void TestForBlockCollisions()
//    {
//        int i1 = MathHelper.floor(boundingBox.minX + 0.001D);
//        int k1 = MathHelper.floor(boundingBox.minY + 0.001D);
//        int i2 = MathHelper.floor(boundingBox.minZ + 0.001D);
//        int k3 = MathHelper.floor(boundingBox.maxX - 0.001D);
//        int l3 = MathHelper.floor(boundingBox.maxY - 0.001D);
//        int i4 = MathHelper.floor(boundingBox.maxZ - 0.001D);
//        if(level.checkChunksExist(i1, k1, i2, k3, l3, i4))
//        {
//            for(int j4 = i1; j4 <= k3; j4++)
//            {
//                for(int k4 = k1; k4 <= l3; k4++)
//                {
//                    for(int l4 = i2; l4 <= i4; l4++)
//                    {
//                        int i5 = level.getTileId(j4, k4, l4);
//                        if(i5 > 0)
//                        {
//                            BlockBase.BY_ID[i5].onEntityCollision(level, j4, k4, l4, this);
//                        }
//                    }
//
//                }
//
//            }
//
//        }
//    }
//
//    private void PushEntity(EntityBase entity)
//    {
//        double anchorMaxY = boundingBox.maxY + 0.074999999999999997D;
//        double entityMinY = entity.boundingBox.minY;
//        if(entityMinY < anchorMaxY)
//        {
//            if(entityMinY > anchorMaxY - 0.25D)
//            {
//                double entityYOffset = anchorMaxY - entityMinY;
//                entity.setPosition(entity.x, entity.y + entityYOffset, entity.z);
//                ((EntityBaseAccessor) entity).setFallDistance(0.0F);
//            } else
//            if((entity instanceof Living) && velocityY < 0.0D)
//            {
//                double entityMaxY = entity.boundingBox.maxY;
//                double anchorMinY = boundingBox.minY;
//                if(anchorMinY < entityMaxY - 0.25D)
//                {
//                    entity.damage(null, 1);
//                }
//            }
//        }
//    }
//
//    public void ForceStopByPlatform()
//    {
//        if(removed)
//        {
//            return;
//        }
//        int i;
//        int k;
//        if(velocityY > 0.0D)
//        {
//            i = MathHelper.floor(x);
//            int jAbove = MathHelper.floor(y) + 1;
//            k = MathHelper.floor(z);
//            int iBlockAboveID = level.getTileId(i, jAbove, k);
//            if(iBlockAboveID == mod_FCBetterThanWolves.fcRopeBlock.id)
//            {
//                ((FCBlockRope)mod_FCBetterThanWolves.fcRopeBlock).BreakRope(level, i, jAbove, k);
//            }
//        }
//        i = MathHelper.floor(x);
//        int j = MathHelper.floor(y);
//        k = MathHelper.floor(z);
//        ConvertToBlock(i, j, k);
//    }
//
//    private void ConvertToBlock(int i, int j, int k)
//    {
//        boolean bCanPlace = true;
//        int iTargetid = level.getTileId(i, j, k);
//        if(!FCUtilsWorld.IsReplaceableBlock(level, i, j, k))
//        {
//            if(iTargetid == mod_FCBetterThanWolves.fcRopeBlock.id)
//            {
//                if(!ReturnRopeToPulley())
//                {
//                    FCUtilsMisc.EjectSingleItemWithRandomOffset(level, i, j, k, mod_FCBetterThanWolves.fcRopeItem.id, 0);
//                }
//            } else
//            if(!BlockBase.BY_ID[iTargetid].material.isSolid())
//            {
//                BlockBase.BY_ID[iTargetid].drop(level, i, j, k, level.getTileMeta(i, j, k));
//                level.setTile(i, j, k, mod_FCBetterThanWolves.fcPlatform.id);
//            } else
//            {
//                bCanPlace = false;
//            }
//        }
//        if(bCanPlace)
//        {
//            level.setTile(i, j, k, mod_FCBetterThanWolves.fcAnchor.id);
//            ((FCBlockAnchor)mod_FCBetterThanWolves.fcAnchor).SetAnchorFacing(level, i, j, k, 1);
//        } else
//        {
//            FCUtilsMisc.EjectSingleItemWithRandomOffset(level, i, j, k, mod_FCBetterThanWolves.fcAnchor.id, 0);
//        }
//        remove();
//    }
//
//    public boolean ReturnRopeToPulley()
//    {
//        int associatedPulleyid = level.getTileId(associatedPulleyPos.i, associatedPulleyPos.j, associatedPulleyPos.k);
//        if(associatedPulleyid == mod_FCBetterThanWolves.fcPulley.id)
//        {
//            FCTileEntityPulley tileEntityPulley = (FCTileEntityPulley)level.getTileEntity(associatedPulleyPos.i, associatedPulleyPos.j, associatedPulleyPos.k);
//            if(tileEntityPulley != null)
//            {
//                tileEntityPulley.AddRopeToInventory();
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static final float fMovementSpeed = 0.05F;
//    private FCBlockPos associatedPulleyPos;
//    private double sentVel;
//}
