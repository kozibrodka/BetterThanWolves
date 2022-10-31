package net.kozibrodka.wolves.entity;


import net.kozibrodka.wolves.blocks.FCBlockAxle;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.utils.FCUtilsMisc;
import net.minecraft.block.Wool;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.vanillafix.item.Items;

public class FCEntityWindMill extends EntityBase
{

    public FCEntityWindMill(Level world)
    {
        super(world);
        bWindMillIAligned = true;
        fRotation = 0.0F;
        bProvidingPower = false;
        iOverpowerTimer = -1;
        iBladeColors = new int[4];
        for(int iTempIndex = 0; iTempIndex < 4; iTempIndex++)
        {
            iBladeColors[iTempIndex] = 0;
        }

        iWindMillCurrentDamage = 0;
        iWindMillTimeSinceHit = 0;
        iWindMillRockDirection = 1;
        fWindMillCurrentRotationSpeed = 0.0F;
        iCurrentBladeColoringIndex = 0;
        iFullUpdateTickCount = 0;
        field_1593 = true;
        setSize(12.8F, 12.8F);
        standingEyeHeight = height / 2.0F;
    }

    public FCEntityWindMill(Level world, double x, double y, double z,
            boolean bIAligned)
    {
        this(world);
        setPosition(x, y, z);
        bWindMillIAligned = bIAligned;
        AlignBoundingBoxWithAxis();
    }

    public FCEntityWindMill(Level level, Double aDouble, Double aDouble1, Double aDouble2) {
        this(level);
    }

    private void AlignBoundingBoxWithAxis()
    {
        if(bWindMillIAligned)
        {
            boundingBox.method_99(x - 0.40000000596046448D, y - 6.4000000953674316D, z - 6.4000000953674316D, x + 0.40000000596046448D, y + 6.4000000953674316D, z + 6.4000000953674316D);
        } else
        {
            boundingBox.method_99(x - 6.4000000953674316D, y - 6.4000000953674316D, z - 0.40000000596046448D, x + 6.4000000953674316D, y + 6.4000000953674316D, z + 0.40000000596046448D);
        }
    }

    protected void initDataTracker()
    {
    }

    public void writeCustomDataToTag(CompoundTag nbttagcompound)
    {
        nbttagcompound.put("bWindMillIAligned", bWindMillIAligned);
        nbttagcompound.put("fRotation", fRotation);
        nbttagcompound.put("bProvidingPower", bProvidingPower);
        nbttagcompound.put("iOverpowerTimer", iOverpowerTimer);
        nbttagcompound.put("iBladeColors0", iBladeColors[0]);
        nbttagcompound.put("iBladeColors1", iBladeColors[1]);
        nbttagcompound.put("iBladeColors2", iBladeColors[2]);
        nbttagcompound.put("iBladeColors3", iBladeColors[3]);
    }

    public void readCustomDataFromTag(CompoundTag nbttagcompound)
    {
        bWindMillIAligned = nbttagcompound.getBoolean("bWindMillIAligned");
        fRotation = nbttagcompound.getFloat("fRotation");
        bProvidingPower = nbttagcompound.getBoolean("bProvidingPower");
        iOverpowerTimer = nbttagcompound.getInt("iOverpowerTimer");
        iBladeColors[0] = nbttagcompound.getInt("iBladeColors0");
        iBladeColors[1] = nbttagcompound.getInt("iBladeColors1");
        iBladeColors[2] = nbttagcompound.getInt("iBladeColors2");
        iBladeColors[3] = nbttagcompound.getInt("iBladeColors3");
        AlignBoundingBoxWithAxis();
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

    public boolean damage(EntityBase entity, int i)
    {
        if(level.isServerSide || removed)
        {
            return true;
        }
        iWindMillRockDirection = -iWindMillRockDirection;
        iWindMillTimeSinceHit = 10;
        method_1336();
        iWindMillCurrentDamage += i * 5;
        if(iWindMillCurrentDamage > 40)
        {
            DestroyWithDrop();
        }
        return true;
    }

    public void method_1312()
    {
        iWindMillRockDirection = -iWindMillRockDirection;
        iWindMillTimeSinceHit = 10;
        iWindMillCurrentDamage += iWindMillCurrentDamage * 5;
    }

    public void remove()
    {
        if(bProvidingPower)
        {
            int iCenterI = (int)(x - 0.5D);
            int iCenterJ = (int)(y - 0.5D);
            int iCenterK = (int)(z - 0.5D);
            int iCenterid = level.getTileId(iCenterI, iCenterJ, iCenterK);
            if(iCenterid == mod_FCBetterThanWolves.fcAxleBlock.id)
            {
                ((FCBlockAxle)mod_FCBetterThanWolves.fcAxleBlock).SetPowerLevel(level, iCenterI, iCenterJ, iCenterK, 0);
            }
        }
        super.remove();
    }

    public void DestroyWithDrop()
    {
        if(!removed)
        {
            dropItem(mod_FCBetterThanWolves.fcWindMillItem.id, 1, 0.0F);
            remove();
        }
    }

    public void tick()
    {
    	if(removed)
        {
            return;
        }
        iFullUpdateTickCount--;
        if(iFullUpdateTickCount <= 0)
        {
            iFullUpdateTickCount = 20;
            int iCenterI = (int)(x - 0.5D);
            int iCenterJ = (int)(y - 0.5D);
            int iCenterK = (int)(z - 0.5D);
            int iCenterid = level.getTileId(iCenterI, iCenterJ, iCenterK);
            if(iCenterid != mod_FCBetterThanWolves.fcAxleBlock.id)
            {
                DestroyWithDrop();
                return;
            }
            if(!WindMillValidateAreaAroundBlock(level, iCenterI, iCenterJ, iCenterK, bWindMillIAligned))
            {
                DestroyWithDrop();
                return;
            }
            if(!bProvidingPower && ((FCBlockAxle)mod_FCBetterThanWolves.fcAxleBlock).GetPowerLevel(level, iCenterI, iCenterJ, iCenterK) > 0)
            {
                DestroyWithDrop();
                return;
            }
            fWindMillCurrentRotationSpeed = ComputeRotation(iCenterI, iCenterJ, iCenterK);
            if(fWindMillCurrentRotationSpeed > 0.01F || fWindMillCurrentRotationSpeed < -0.01F)
            {
                if(!bProvidingPower)
                {
                    bProvidingPower = true;
                    ((FCBlockAxle)mod_FCBetterThanWolves.fcAxleBlock).SetPowerLevel(level, iCenterI, iCenterJ, iCenterK, 3);
                }
            } else
            if(bProvidingPower)
            {
                bProvidingPower = false;
                ((FCBlockAxle)mod_FCBetterThanWolves.fcAxleBlock).SetPowerLevel(level, iCenterI, iCenterJ, iCenterK, 0);
            }
            if(iOverpowerTimer >= 0)
            {
                if(iOverpowerTimer > 0)
                {
                    iOverpowerTimer--;
                }
                if(iOverpowerTimer <= 0)
                {
                    ((FCBlockAxle)mod_FCBetterThanWolves.fcAxleBlock).Overpower(level, iCenterI, iCenterJ, iCenterK);
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
        fRotation += fWindMillCurrentRotationSpeed;
        if(fRotation > 360F)
        {
            fRotation -= 360F;
        } else
        if(fRotation < -360F)
        {
            fRotation += 360F;
        }
    }

    public float getEyeHeight()
    {
        return 0.0F;
    }

    /**
        błędna funckja canUse?
     */
//    public boolean canUse(PlayerBase entityplayer)
//    {
//        if(removed)
//        {
//            return false;
//        } else
//        {
//            return entityplayer.squaredDistanceToToEntity(this) <= 256D;
//        }
//    }

    public boolean interact(PlayerBase entityplayer)
    {
        //TODO ItemBase to Items and possibly there will be now get.damage()
        ItemInstance ItemInstance = entityplayer.inventory.getHeldItem();
        if(ItemInstance != null && ((ItemInstance.itemId > 359 && ItemInstance.itemId < 376) || ItemInstance.itemId == mod_FCBetterThanWolves.fcDung.id))
        {
            int iColor = 0;
            if(ItemInstance.itemId > 359 && ItemInstance.itemId < 376)
            {
                iColor = Wool.getColour((int) MathHelper.abs(360 - ItemInstance.itemId));
            } else
            {
                iColor = 12;
            }
            iBladeColors[iCurrentBladeColoringIndex] = iColor;
            iCurrentBladeColoringIndex++;
            if(iCurrentBladeColoringIndex >= 4)
            {
                iCurrentBladeColoringIndex = 0;
            }
            ItemInstance.count--;
            if(ItemInstance.count == 0)
            {
                entityplayer.inventory.setInventoryItem(entityplayer.inventory.selectedHotbarSlot, null);
            }
        }
        return true;
    }

    public void move(double deltaX, double deltaY, double deltaZ)
    {
        if(!removed)
        {
            DestroyWithDrop();
        }
    }

    public static boolean WindMillValidateAreaAroundBlock(Level world, int i, int j, int k, boolean bIAligned)
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

    public static boolean IsValidBlockForWindMillToOccupy(Level world, int i, int j, int k)
    {
        return world.isAir(i, j, k);
    }

    private float ComputeRotation(int iCenterI, int iCenterJ, int iCenterK)
    {
        float fRotationAmount = 0.0F;
        if(level.dimension.evaporatesWater)
        {
            fRotationAmount = -0.0675F;
            iOverpowerTimer = -1;
        } else
        if(level.isAboveGroundCached(iCenterI, iCenterJ, iCenterK))
        {
            if(FCUtilsMisc.IsBlockBeingPrecipitatedOn(level, iCenterI, 128, iCenterK))
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
    public int iWindMillCurrentDamage;
    public int iWindMillTimeSinceHit;
    public int iWindMillRockDirection;
    public float fWindMillCurrentRotationSpeed;
    public int iCurrentBladeColoringIndex;
    public int iFullUpdateTickCount;
}
