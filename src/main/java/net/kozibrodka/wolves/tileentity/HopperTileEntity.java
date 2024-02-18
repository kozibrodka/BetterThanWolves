package net.kozibrodka.wolves.tileentity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.blocks.Hopper;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.network.RenderPacket;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.recipe.HopperHauntingRecipeRegistry;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.kozibrodka.wolves.utils.ReplaceableBlockChecker;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.Item;
import net.minecraft.entity.Living;
import net.minecraft.entity.Minecart;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;

import java.util.List;


public class HopperTileEntity extends TileEntityBase
    implements InventoryBase
{

    public HopperTileEntity()
    {
        hopperContents = new ItemInstance[19];
        ejectCounter = 0;
        hopperEjectBlocked = false;
    }

    public int getInventorySize()
    {
        return 19;
    }

    public ItemInstance getInventoryItem(int slot)
    {
        return hopperContents[slot];
    }

    public ItemInstance takeInventoryItem(int slot, int amount)
    {
        return InventoryHandler.decreaseStackSize(this, slot, amount);
    }

    public void setInventoryItem(int iSlot, ItemInstance ItemInstance)
    {
        hopperContents[iSlot] = ItemInstance;
        if(ItemInstance != null && ItemInstance.count > getMaxItemCount())
        {
            ItemInstance.count = getMaxItemCount();
        }
//        if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER)
//        {
//            renderPacket();
//        }
        markDirty();
    }

    @Environment(EnvType.SERVER)
    public void renderPacket(){
        List list2 = level.players;
        if(list2.size() != 0) {
            for(int k = 0; k < list2.size(); k++)
            {
                ServerPlayer player1 = (ServerPlayer) list2.get(k);
                PacketHelper.sendTo(player1, new RenderPacket(2, x, y, z, GetFilterType() ,InventoryHandler.getOccupiedSlotCountWithinBounds(this, 0, 17)));
                System.out.println("WYSYLAM PAKIET");
            }
        }
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
            ejectCounter = nbttagcompound.getInt("ejectCounter");
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
        nbttagcompound.put("ejectCounter", ejectCounter);
    }

    public int getMaxItemCount()
    {
        return 64;
    }

    public boolean canPlayerUse(PlayerBase entityplayer) {
        if(level.getTileEntity(x, y, z) != this) {
            return false;
        } else {
            return entityplayer.squaredDistanceTo((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
        }
    }

    public void tick()
    {
        if(level.isServerSide){
            return;
        }
        if(!((Hopper) BlockListener.hopper).IsBlockOn(level, x, y, z)) return;
        if(hopperEjectBlocked)
        {
            ejectCounter = 0;
            return;
        }
        ejectCounter++;
        if(ejectCounter < 3) return;
        if (!attemptSoulFiltering())
        {
            attemptItemEjection();
            ejectCounter = 0;
            return;
        }
        hopperContents[18].applyDamage(1, null);
        if (hopperContents[18].getDamage() <= 0)
        {
            hopperContents[18] = null;
            hopperSoulOverload();
        }
        ejectCounter = 0;
    }

    public boolean attemptSoulFiltering()
    {
        if (GetFilterType() != 6) return false;
        if (getInventoryItem(18) == null) return false;
        ItemInstance item = null;
        ItemInstance recipeResultGetter = null; // This is a necessary workaround, otherwise the registry gets messed up.
        ItemInstance result = null;
        int inputSlot = 0;

        for (; inputSlot < 18; inputSlot++) {
            item = getInventoryItem(inputSlot);
            if (item == null) continue;
            recipeResultGetter = HopperHauntingRecipeRegistry.getInstance().getResult(item.itemId);
            if (recipeResultGetter != null) result = recipeResultGetter.copy();
            if (result != null) break;
        }

        if (item == null) return false;
        if (result == null) return false;

        for (int outputSlot = 0; outputSlot < 18; outputSlot++) {
            if (getInventoryItem(outputSlot) == null)
            {
                convertToHellfireDust(inputSlot, outputSlot, true, result);
                return true;
            }
            else if (result.isDamageAndIDIdentical(getInventoryItem(outputSlot)) && getInventoryItem(outputSlot).count + result.count <= getInventoryItem(outputSlot).getMaxStackSize())
            {
                convertToHellfireDust(inputSlot, outputSlot, false, HopperHauntingRecipeRegistry.getInstance().getResult(item.itemId));
                return true;
            }
        }
        return false;
    }

    public void convertToHellfireDust(int inputSlot, int outputSlot, boolean emptySlot, ItemInstance result)
    {
        if (emptySlot)
        {
            setInventoryItem(outputSlot, result);
        }
        else
        {
            ItemInstance outputItem = getInventoryItem(outputSlot);
            outputItem.count += result.count;
            setInventoryItem(outputSlot, outputItem);
        }
        ItemInstance inputItem = getInventoryItem(inputSlot);
        inputItem.count--;
        if (inputItem.count <= 0) setInventoryItem(inputSlot, null);
        else setInventoryItem(inputSlot, inputItem);
    }

    public void markDirty()
    {
//        System.out.println("KURWA 2");
        if (level == null) {
            return;
        }
        level.method_202(x, y, z, x, y, z);
        hopperEjectBlocked = false;
        int iOccupiedStacks = InventoryHandler.getOccupiedSlotCountWithinBounds(this, 0, 17);
//        ((Hopper)BlockListener.hopper).SetHopperFull(level, x, y, z, iOccupiedStacks == 18);
        ((Hopper)BlockListener.hopper).SetHopperFull(level, x, y, z, iOccupiedStacks);
//        ((Hopper)BlockListener.hopper).SetHasFilter(level, x, y, z, GetFilterType() > 0);
        ((Hopper)BlockListener.hopper).SetHasFilter(level, x, y, z, GetFilterType());
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
            if(filterStack.itemId == ItemListener.grate.id)
            {
                return 3;
            }
            if(filterStack.itemId == ItemListener.wicker.id)
            {
                return 4;
            }
            if(filterStack.itemId == ItemListener.rollersItem.id)
            {
                return 5;
            }
            if(filterStack.itemId == ItemListener.soulFilter.id)
            {
                return 6;
            }
        }
        return 0;
    }

    // TODO: Replace this atrocity with an item filter registry
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
                        item.id != ItemListener.netherCoal.id &&
                        item.id != ItemListener.hempSeeds.id &&
                        item.id != ItemListener.flour.id &&
                        item.id != ItemListener.hempFibers.id &&
                        item.id != ItemListener.donut.id &&
                        item.id != ItemListener.dung.id &&
                        item.id != ItemListener.groundNetherrack.id &&
                        item.id != ItemListener.hellfireDust.id &&
                        item.id != ItemListener.coalDust.id &&
                        item.id != ItemListener.broadHeadArrowhead.id
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
                        item.id != ItemListener.hempSeeds.id &&
                        item.id != ItemListener.flour.id &&
                        item.id != ItemListener.groundNetherrack.id &&
                        item.id != ItemListener.hellfireDust.id &&
                        item.id != ItemListener.coalDust.id
                )
                {
                    if(iFilterType == 5)
                    {
                        if(item.id != ItemBase.string.id && item.id != ItemBase.paper.id && item.id != ItemListener.hempFibers.id)
                        {
                            return false;
                        }
                    } else
                    {
                        return false;
                    }
                }
            } else
            if(iFilterType == 6 && item.id != ItemListener.groundNetherrack.id)
            {
                return false;
            }
        }
        return true;
    }

    public boolean IsEjecting()
    {
        return ((Hopper)BlockListener.hopper).IsBlockOn(level, x, y, z);
    }

    private void attemptItemEjection()
    {
        int itemIndex = InventoryHandler.getFirstOccupiedStackExcludingItem(this, ItemBase.clay.id);
        if(itemIndex >= 0 && itemIndex < 18)
        {
            ItemInstance invStack = getInventoryItem(itemIndex);
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
                if(ReplaceableBlockChecker.IsReplaceableBlock(level, iTargetI, iTargetJ, iTargetK))
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
                            if(iTargetid == BlockListener.hopper.id)
                            {
                                iMaxSlotToAddTo = 17;
                                canProcessStack = ((HopperTileEntity)targetTileEntityBase).CanCurrentFilterProcessItem(ItemBase.byId[ejectStack.itemId]);
                            }
                            if(canProcessStack)
                            {
                                if(!InventoryHandler.addItemWithinSlotBounds((InventoryBase)targetTileEntityBase, ejectStack, iMinSlotToAddTo, iMaxSlotToAddTo))
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
                                            BlockPosition tempOffset = new BlockPosition(iTempFacing);
                                            int iTempid = level.getTileId(iTargetI + tempOffset.i, iTargetJ + tempOffset.j, iTargetK + tempOffset.k);
                                            if(iTempid == BlockBase.CHEST.id)
                                            {
                                                targetTileEntityBase = level.getTileEntity(iTargetI + tempOffset.i, iTargetJ + tempOffset.j, iTargetK + tempOffset.k);
                                                if(targetTileEntityBase != null && (targetTileEntityBase instanceof InventoryBase))
                                                {
                                                    if(InventoryHandler.addItemInstanceToInventory((InventoryBase)targetTileEntityBase, ejectStack))
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
                                    takeInventoryItem(itemIndex, iNumItemsStored);
                                    level.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.pop", 0.25F, ((level.rand.nextFloat() - level.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                                }
                            } else
                            {
                                hopperEjectBlocked = true;
                            }
                        } else
                        {
                            hopperEjectBlocked = true;
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
                            if(InventoryHandler.addItemInstanceToInventory(minecartEntity, ejectStack))
                            {
                                iNumItemsStored = iEjectStackSize;
                            } else
                            {
                                iNumItemsStored = iEjectStackSize - ejectStack.count;
                            }
                            if(iNumItemsStored > 0)
                            {
                                takeInventoryItem(itemIndex, iNumItemsStored);
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
                takeInventoryItem(itemIndex, iEjectStackSize);
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

    private void hopperSoulOverload()
    {
        if(SpawnGhast())
        {
            level.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "mob.ghast.scream", 1.0F, level.rand.nextFloat() * 0.4F + 0.8F);
        }
        ((Hopper)BlockListener.hopper).BreakHopper(level, x, y, z);
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

    private ItemInstance[] hopperContents;
    private int ejectCounter;
    public boolean hopperEjectBlocked;
    public int clientOccupiedSlots;
    public int clientFilterType;
}
