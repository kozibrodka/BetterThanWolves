package net.kozibrodka.wolves.tileentity;

import net.kozibrodka.wolves.blocks.FCBlockHopper;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.utils.FCBlockPos;
import net.kozibrodka.wolves.utils.FCUtilsInventory;
import net.kozibrodka.wolves.utils.FCUtilsWorld;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.Item;
import net.minecraft.entity.Living;
import net.minecraft.entity.Minecart;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;
import net.minecraft.util.maths.Box;

import java.util.List;


public class FCTileEntityHopper extends TileEntityBase
    implements InventoryBase
{

    public FCTileEntityHopper()
    {
        hopperContents = new ItemInstance[19];
        iHopperEjectCounter = 0;
        iHoppeContainedSoulCount = 0;
        bHopperEjectBlocked = false;
    }

    public int getInventorySize()
    {
        return 19;
    }

    public ItemInstance getInventoryItem(int iSlot)
    {
        return hopperContents[iSlot];
    }

    public ItemInstance takeInventoryItem(int iSlot, int iAmount)
    {
        return FCUtilsInventory.DecrStackSize(this, iSlot, iAmount);
    }

    public void setInventoryItem(int iSlot, ItemInstance ItemInstance)
    {
        hopperContents[iSlot] = ItemInstance;
        if(ItemInstance != null && ItemInstance.count > getMaxItemCount())
        {
            ItemInstance.count = getMaxItemCount();
        }
        markDirty();
    }

    public String getContainerName()
    {
        return "Hopper";
    }

    public void readIdentifyingData(CompoundTag nbttagcompound)
    {
        super.readIdentifyingData(nbttagcompound);
        ListTag nbttaglist = nbttagcompound.getListTag("Items");
        hopperContents = new ItemInstance[getInventorySize()];
        for(int i = 0; i < nbttaglist.size(); i++)
        {
            CompoundTag nbttagcompound1 = (CompoundTag)nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < hopperContents.length)
            {
                hopperContents[j] = new ItemInstance(nbttagcompound1);
            }
        }

        if(nbttagcompound.containsKey("grindCounter"))
        {
            iHopperEjectCounter = nbttagcompound.getInt("grindCounter");
        }
        if(nbttagcompound.containsKey("iHoppeContainedSoulCount"))
        {
            iHoppeContainedSoulCount = nbttagcompound.getInt("iHoppeContainedSoulCount");
        }
    }

    public void writeIdentifyingData(CompoundTag nbttagcompound)
    {
        super.writeIdentifyingData(nbttagcompound);
        ListTag nbttaglist = new ListTag();
        for(int i = 0; i < hopperContents.length; i++)
        {
            if(hopperContents[i] != null)
            {
                CompoundTag nbttagcompound1 = new CompoundTag();
                nbttagcompound1.put("Slot", (byte)i);
                hopperContents[i].toTag(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
        nbttagcompound.put("grindCounter", iHopperEjectCounter);
        nbttagcompound.put("iHoppeContainedSoulCount", iHoppeContainedSoulCount);
    }

    public int getMaxItemCount()
    {
        return 64;
    }

    public boolean canPlayerUse(PlayerBase entityplayer)
    {
        if(level.getTileEntity(x, y, z) != this)
        {
            return false;
        } else
        {
            return entityplayer.squaredDistanceTo((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
        }
    }

    public void tick()
    {
        if(((FCBlockHopper) mod_FCBetterThanWolves.fcHopper).IsBlockOn(level, x, y, z))
        {
            iHoppeContainedSoulCount = 0;
            if(!bHopperEjectBlocked)
            {
                iHopperEjectCounter++;
                if(iHopperEjectCounter >= 3)
                {
                    AttemptToEjectStackFromInv();
                    iHopperEjectCounter = 0;
                }
            } else
            {
                iHopperEjectCounter = 0;
            }
        } else
        {
            iHopperEjectCounter = 0;
            if(GetFilterType() == 6)
            {
                if(iHoppeContainedSoulCount > 7)
                {
                    HopperSoulOverload();
                }
            } else
            {
                iHoppeContainedSoulCount = 0;
            }
        }
    }

    public void markDirty()
    {
        level.method_202(x, y, z, x, y, z);
        bHopperEjectBlocked = false;
        int iOccupiedStacks = FCUtilsInventory.GetNumOccupiedStacksInSlotRange(this, 0, 17);
        ((FCBlockHopper)mod_FCBetterThanWolves.fcHopper).SetHopperFull(level, x, y, z, iOccupiedStacks == 18);
        //TODO logic change
        ((FCBlockHopper)mod_FCBetterThanWolves.fcHopper).SetHasFilter(level, x, y, z, GetFilterType() > 0);
    }

    public int GetFilterType()
    {
        ItemInstance filterStack = getInventoryItem(18);
        if(filterStack != null && filterStack.count > 0)
        {
            if(filterStack.itemId == BlockBase.LADDER.id)
            {
                return 1;
            }
            if(filterStack.itemId == BlockBase.TRAPDOOR.id)
            {
                return 2;
            }
            if(filterStack.itemId == mod_FCBetterThanWolves.fcGrate.id)
            {
                return 3;
            }
            if(filterStack.itemId == mod_FCBetterThanWolves.fcWicker.id)
            {
                return 4;
            }
            if(filterStack.itemId == mod_FCBetterThanWolves.fcRollersItem.id)
            {
                return 5;
            }
            if(filterStack.itemId == BlockBase.SOUL_SAND.id)
            {
                return 6;
            }
        }
        return 0;
    }

    public boolean CanCurrentFilterProcessItem(ItemBase item)
    {
        int iFilterType = GetFilterType();
        if(iFilterType > 0)
        {
            if(
                    item.id < 256 &&
                    item.id != BlockBase.SAND.id &&
                    item.id != BlockBase.GRAVEL.id &&
                    item.id != BlockBase.TORCH.id &&
                    item.id != BlockBase.REDSTONE_TORCH_LIT.id &&
                    item.id != BlockBase.SAPLING.id &&
                    item.id != BlockBase.DANDELION.id &&
                    item.id != BlockBase.ROSE.id &&
                    item.id != BlockBase.BROWN_MUSHROOM.id &&
                    item.id != BlockBase.RED_MUSHROOM.id
            )
            {
                return false;
            }
            if(iFilterType == 2)
            {
                if(item.getMaxStackSize() < 64)
                {
                    return false;
                }
            } else
            if(iFilterType == 3)
            {
                if(
                        item.id != BlockBase.SAND.id &&
                        item.id != BlockBase.GRAVEL.id &&
                        item.id != BlockBase.DANDELION.id &&
                        item.id != BlockBase.ROSE.id &&
                        item.id != BlockBase.BROWN_MUSHROOM.id &&
                        item.id != BlockBase.RED_MUSHROOM.id &&
                        item.id != ItemBase.apple.id &&
                        item.id != ItemBase.coal.id &&
                        item.id != ItemBase.diamond.id &&
                        item.id != ItemBase.string.id &&
                        item.id != ItemBase.feather.id &&
                        item.id != ItemBase.seeds.id &&
                        item.id != ItemBase.flint.id &&
                        item.id != ItemBase.goldenApple.id &&
                        item.id != ItemBase.snowball.id &&
                        item.id != ItemBase.clay.id &&
                        item.id != ItemBase.paper.id &&
                        item.id != ItemBase.slimeball.id &&
                        item.id != ItemBase.egg.id &&
                        item.id != ItemBase.cookie.id &&
                        item.id != ItemBase.gunpowder.id &&
                        item.id != ItemBase.redstoneDust.id &&
                        item.id != ItemBase.glowstoneDust.id &&
                        item.id != ItemBase.dyePowder.id &&
                        item.id != ItemBase.sugar.id &&
                        item.id != mod_FCBetterThanWolves.fcNethercoal.id &&
                        item.id != mod_FCBetterThanWolves.fcHempSeeds.id &&
                        item.id != mod_FCBetterThanWolves.fcFlour.id &&
                        item.id != mod_FCBetterThanWolves.fcHempFibers.id &&
                        item.id != mod_FCBetterThanWolves.fcDonut.id &&
                        item.id != mod_FCBetterThanWolves.fcDung.id &&
                        item.id != mod_FCBetterThanWolves.fcGroundNetherrack.id &&
                        item.id != mod_FCBetterThanWolves.fcHellfireDust.id &&
                        item.id != mod_FCBetterThanWolves.fcCoalDust.id &&
                        item.id != mod_FCBetterThanWolves.fcBroadheadArrowhead.id
                )
                {
                    return false;
                }
            } else
            if(iFilterType == 4 || iFilterType == 5)
            {
                if(
                        item.id != BlockBase.SAND.id &&
                        item.id != BlockBase.GRAVEL.id &&
                        item.id != ItemBase.seeds.id &&
                        item.id != ItemBase.gunpowder.id &&
                        item.id != ItemBase.redstoneDust.id &&
                        item.id != ItemBase.glowstoneDust.id &&
                        item.id != ItemBase.dyePowder.id &&
                        item.id != ItemBase.sugar.id &&
                        item.id != mod_FCBetterThanWolves.fcHempSeeds.id &&
                        item.id != mod_FCBetterThanWolves.fcFlour.id &&
                        item.id != mod_FCBetterThanWolves.fcGroundNetherrack.id &&
                        item.id != mod_FCBetterThanWolves.fcHellfireDust.id &&
                        item.id != mod_FCBetterThanWolves.fcCoalDust.id
                )
                {
                    if(iFilterType == 5)
                    {
                        if(item.id != ItemBase.string.id && item.id != ItemBase.paper.id && item.id != mod_FCBetterThanWolves.fcHempFibers.id)
                        {
                            return false;
                        }
                    } else
                    {
                        return false;
                    }
                }
            } else
            if(iFilterType == 6 && item.id != mod_FCBetterThanWolves.fcGroundNetherrack.id)
            {
                return false;
            }
        }
        return true;
    }

    public boolean IsEjecting()
    {
        return ((FCBlockHopper)mod_FCBetterThanWolves.fcHopper).IsBlockOn(level, x, y, z);
    }

    private void AttemptToEjectStackFromInv()
    {
        int iStackIndex = FCUtilsInventory.GetFirstOccupiedStackNotOfItem(this, ItemBase.clay.id);
        if(iStackIndex >= 0 && iStackIndex < 18)
        {
            ItemInstance invStack = getInventoryItem(iStackIndex);
            int iEjectStackSize;
            if(8 > invStack.count)
            {
                iEjectStackSize = invStack.count;
            } else
            {
                iEjectStackSize = 8;
            }
            ItemInstance ejectStack = new ItemInstance(invStack.itemId, iEjectStackSize, invStack.getDamage());
            int iTargetI = x;
            int iTargetJ = y - 1;
            int iTargetK = z;
            boolean bEjectIntoWorld = false;
            if(level.isAir(iTargetI, iTargetJ, iTargetK))
            {
                bEjectIntoWorld = true;
            } else
            {
                int iTargetid = level.getTileId(iTargetI, iTargetJ, iTargetK);
                if(FCUtilsWorld.IsReplaceableBlock(level, iTargetI, iTargetJ, iTargetK))
                {
                    bEjectIntoWorld = true;
                } else
                {
                    BlockBase targetBlock = BlockBase.BY_ID[iTargetid];
                    if(!targetBlock.material.isSolid())
                    {
                        bEjectIntoWorld = true;
                    } else
                    {
                        TileEntityBase targetTileEntityBase = level.getTileEntity(iTargetI, iTargetJ, iTargetK);
                        int iNumItemsStored = 0;
                        if(targetTileEntityBase != null && (targetTileEntityBase instanceof InventoryBase))
                        {
                            int iMinSlotToAddTo = 0;
                            int iMaxSlotToAddTo = ((InventoryBase)targetTileEntityBase).getInventorySize() - 1;
                            boolean canProcessStack = true;
                            if(iTargetid == BlockBase.FURNACE.id || iTargetid == BlockBase.FURNACE_LIT.id)
                            {
                                iMaxSlotToAddTo = 0;
                            } else
                            if(iTargetid == mod_FCBetterThanWolves.fcHopper.id)
                            {
                                iMaxSlotToAddTo = 17;
                                canProcessStack = ((FCTileEntityHopper)targetTileEntityBase).CanCurrentFilterProcessItem(ItemBase.byId[ejectStack.itemId]);
                            }
                            if(canProcessStack)
                            {
                                if(!FCUtilsInventory.AddItemInstanceToInventoryInSlotRange((InventoryBase)targetTileEntityBase, ejectStack, iMinSlotToAddTo, iMaxSlotToAddTo))
                                {
                                    if(iTargetid == BlockBase.CHEST.id)
                                    {
                                        int iTempFacing = 2;
                                        do
                                        {
                                            if(iTempFacing > 5)
                                            {
                                                break;
                                            }
                                            FCBlockPos tempOffset = new FCBlockPos(iTempFacing);
                                            int iTempid = level.getTileId(iTargetI + tempOffset.i, iTargetJ + tempOffset.j, iTargetK + tempOffset.k);
                                            if(iTempid == BlockBase.CHEST.id)
                                            {
                                                targetTileEntityBase = level.getTileEntity(iTargetI + tempOffset.i, iTargetJ + tempOffset.j, iTargetK + tempOffset.k);
                                                if(targetTileEntityBase != null && (targetTileEntityBase instanceof InventoryBase))
                                                {
                                                    if(FCUtilsInventory.AddItemInstanceToInventory((InventoryBase)targetTileEntityBase, ejectStack))
                                                    {
                                                        iNumItemsStored = iEjectStackSize;
                                                    } else
                                                    {
                                                        iNumItemsStored = iEjectStackSize - ejectStack.count;
                                                    }
                                                }
                                                break;
                                            }
                                            iTempFacing++;
                                        } while(true);
                                    } else
                                    {
                                        iNumItemsStored = iEjectStackSize - ejectStack.count;
                                    }
                                } else
                                {
                                    iNumItemsStored = iEjectStackSize;
                                }
                                if(iNumItemsStored > 0)
                                {
                                    takeInventoryItem(iStackIndex, iNumItemsStored);
                                    level.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.pop", 0.25F, ((level.rand.nextFloat() - level.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                                }
                            } else
                            {
                                bHopperEjectBlocked = true;
                            }
                        } else
                        {
                            bHopperEjectBlocked = true;
                        }
                    }
                }
            }
            if(bEjectIntoWorld)
            {
                List list = level.getEntities(Minecart.class, Box.createButWasteMemory((float)x + 0.4F, (float)y - 0.5F, (float)z + 0.4F, (float)x + 0.6F, y, (float)z + 0.6F));
                if(list != null && list.size() > 0)
                {
                    int listIndex = 0;
                    do
                    {
                        if(listIndex >= list.size())
                        {
                            break;
                        }
                        Minecart minecartEntity = (Minecart)list.get(listIndex);
                        if(minecartEntity.type == 1 && minecartEntity.boundingBox.boxIntersects(Box.createButWasteMemory((float)x, (float)y - 0.5F, (float)z, (float)x + 0.25F, y, (float)z + 1.0F)) && minecartEntity.boundingBox.boxIntersects(Box.createButWasteMemory((float)x + 0.75F, (float)y - 0.5F, (float)z, (float)x + 1.0F, y, (float)z + 1.0F)) && minecartEntity.boundingBox.boxIntersects(Box.createButWasteMemory((float)x, (float)y - 0.5F, (float)z, (float)x + 1.0F, y, (float)z + 0.25F)) && minecartEntity.boundingBox.boxIntersects(Box.createButWasteMemory((float)x, (float)y - 0.5F, (float)z + 0.75F, (float)x + 1.0F, y, (float)z + 1.0F)))
                        {
                            int iNumItemsStored = 0;
                            if(FCUtilsInventory.AddItemInstanceToInventory(minecartEntity, ejectStack))
                            {
                                iNumItemsStored = iEjectStackSize;
                            } else
                            {
                                iNumItemsStored = iEjectStackSize - ejectStack.count;
                            }
                            if(iNumItemsStored > 0)
                            {
                                takeInventoryItem(iStackIndex, iNumItemsStored);
                                level.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.pop", 0.25F, ((level.rand.nextFloat() - level.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                            }
                            bEjectIntoWorld = false;
                            break;
                        }
                        listIndex++;
                    } while(true);
                }
            }
            if(bEjectIntoWorld)
            {
                EjectStack(ejectStack);
                takeInventoryItem(iStackIndex, iEjectStackSize);
            }
        }
    }

    private void EjectStack(ItemInstance stack)
    {
        float xOffset = level.rand.nextFloat() * 0.1F + 0.45F;
        float yOffset = -0.35F;
        float zOffset = level.rand.nextFloat() * 0.1F + 0.45F;
        Item entityitem = new Item(level, (float)x + xOffset, (float)y + yOffset, (float)z + zOffset, stack);
        entityitem.velocityX = 0.0D;
        entityitem.velocityY = -0.0099999997764825821D;
        entityitem.velocityZ = 0.0D;
        entityitem.pickupDelay = 10;
        level.spawnEntity(entityitem);
    }

    public void ResetContainedSoulCount()
    {
        iHoppeContainedSoulCount = 0;
    }

    public void IncrementContainedSoulCount(int iNumSouls)
    {
        iHoppeContainedSoulCount += iNumSouls;
    }

    private void HopperSoulOverload()
    {
        if(SpawnGhast())
        {
            level.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "mob.ghast.scream", 1.0F, level.rand.nextFloat() * 0.4F + 0.8F);
        }
        ((FCBlockHopper)mod_FCBetterThanWolves.fcHopper).BreakHopper(level, x, y, z);
    }

    private boolean SpawnGhast()
    {
        Living entityliving = (Living) EntityRegistry.create("Ghast", level);
        if(entityliving == null)
        {
            return false;
        }
        for(int i = 0; i < 200; i++)
        {
            double xPos = (double)x + (level.rand.nextDouble() - level.rand.nextDouble()) * 10D;
            double yPos = (y + level.rand.nextInt(21)) - 10;
            double zPos = (double)z + (level.rand.nextDouble() - level.rand.nextDouble()) * 10D;
            entityliving.setPositionAndAngles(xPos, yPos, zPos, level.rand.nextFloat() * 360F, 0.0F);
            if(entityliving.canSpawn())
            {
                level.spawnEntity(entityliving);
                return true;
            }
        }

        return false;
    }

    private final int iHopperInventorySize = 19;
    private final int iHopperStackSizeLimit = 64;
    private final int iHopperStackSizeToDrop = 8;
    private final double dHopperMaxPlayerInteractionDist = 64D;
    private final int iHoppeMaxContainedSoulCount = 7;
    private final int iHopperTimeToEject = 3;
    private ItemInstance hopperContents[];
    private int iHopperEjectCounter;
    private int iHoppeContainedSoulCount;
    public boolean bHopperEjectBlocked;
}
