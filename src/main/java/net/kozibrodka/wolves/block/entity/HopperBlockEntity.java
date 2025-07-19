package net.kozibrodka.wolves.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.block.HopperBlock;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.recipe.HopperPurifyingRecipeRegistry;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.kozibrodka.wolves.utils.ReplaceableBlockChecker;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;


public class HopperBlockEntity extends BlockEntity
    implements Inventory
{

    public HopperBlockEntity()
    {
        hopperContents = new ItemStack[19];
        ejectCounter = 0;
        hopperEjectBlocked = false;
    }

    public int size()
    {
        return 19;
    }

    public ItemStack getStack(int slot)
    {
        return hopperContents[slot];
    }

    public ItemStack removeStack(int slot, int amount)
    {
        return InventoryHandler.decreaseStackSize(this, slot, amount);
    }

    public void setStack(int iSlot, ItemStack ItemInstance)
    {
        hopperContents[iSlot] = ItemInstance;
        if(ItemInstance != null && ItemInstance.count > getMaxCountPerStack())
        {
            ItemInstance.count = getMaxCountPerStack();
        }
        markDirty();
    }


    public String getName()
    {
        return "Hopper";
    }

    public void readNbt(NbtCompound nbttagcompound)
    {
        super.readNbt(nbttagcompound);
        NbtList nbttaglist = nbttagcompound.getList("Items");
        hopperContents = new ItemStack[size()];
        for(int i = 0; i < nbttaglist.size(); i++)
        {
            NbtCompound nbttagcompound1 = (NbtCompound)nbttaglist.get(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < hopperContents.length)
            {
                hopperContents[j] = new ItemStack(nbttagcompound1);
            }
        }

        if(nbttagcompound.contains("grindCounter"))
        {
            ejectCounter = nbttagcompound.getInt("ejectCounter");
        }
    }

    public void writeNbt(NbtCompound nbttagcompound)
    {
        super.writeNbt(nbttagcompound);
        NbtList nbttaglist = new NbtList();
        for(int i = 0; i < hopperContents.length; i++)
        {
            if(hopperContents[i] != null)
            {
                NbtCompound nbttagcompound1 = new NbtCompound();
                nbttagcompound1.putByte("Slot", (byte)i);
                hopperContents[i].writeNbt(nbttagcompound1);
                nbttaglist.add(nbttagcompound1);
            }
        }

        nbttagcompound.put("Items", nbttaglist);
        nbttagcompound.putInt("ejectCounter", ejectCounter);
    }

    public int getMaxCountPerStack()
    {
        return 64;
    }

    public boolean canPlayerUse(PlayerEntity entityplayer) {
        if(world.getBlockEntity(x, y, z) != this) {
            return false;
        } else {
            return entityplayer.getSquaredDistance((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
        }
    }

    public void tick()
    {
        if(world.isRemote){
            return;
        }
        if(!((HopperBlock) BlockListener.hopper).IsBlockOn(world, x, y, z)) return;
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
        hopperContents[18].damage(1, null);
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
        if (getStack(18) == null) return false;
        ItemStack item = null;
        ItemStack recipeResultGetter; // This is a necessary workaround, otherwise the registry gets messed up.
        ItemStack result = null;
        int inputSlot = 0;

        for (; inputSlot < 18; inputSlot++) {
            item = getStack(inputSlot);
            if (item == null) continue;
            Identifier itemIdentifier = ItemRegistry.INSTANCE.getId(item.getItem());
            recipeResultGetter = HopperPurifyingRecipeRegistry.getInstance().getResult(itemIdentifier);
            if (recipeResultGetter != null) result = recipeResultGetter.copy();
            if (result != null) break;
        }

        if (item == null) return false;
        if (result == null) return false;

        for (int outputSlot = 0; outputSlot < 18; outputSlot++) {
            if (getStack(outputSlot) == null)
            {
                convertToHellfireDust(inputSlot, outputSlot, true, result);
                return true;
            }
            else if (result.isItemEqual(getStack(outputSlot)) && getStack(outputSlot).count + result.count <= getStack(outputSlot).getMaxCount())
            {
                Identifier itemIdentifier = ItemRegistry.INSTANCE.getId(item.getItem());
                convertToHellfireDust(inputSlot, outputSlot, false, HopperPurifyingRecipeRegistry.getInstance().getResult(itemIdentifier));
                return true;
            }
        }
        return false;
    }

    public void convertToHellfireDust(int inputSlot, int outputSlot, boolean emptySlot, ItemStack result)
    {
        if (emptySlot)
        {
            setStack(outputSlot, result);
        }
        else
        {
            ItemStack outputItem = getStack(outputSlot);
            outputItem.count += result.count;
            setStack(outputSlot, outputItem);
        }
        ItemStack inputItem = getStack(inputSlot);
        inputItem.count--;
        if (inputItem.count <= 0) setStack(inputSlot, null);
        else setStack(inputSlot, inputItem);
    }

    public void markDirty()
    {
        if (world == null) {
            return;
        }
        world.setBlocksDirty(x, y, z, x, y, z);
        hopperEjectBlocked = false;
        int iOccupiedStacks = InventoryHandler.getOccupiedSlotCountWithinBounds(this, 0, 17);
//        ((Hopper)BlockListener.hopper).SetHopperFull(level, x, y, z, iOccupiedStacks == 18);
        ((HopperBlock)BlockListener.hopper).SetHopperFull(world, x, y, z, iOccupiedStacks);
//        ((Hopper)BlockListener.hopper).SetHasFilter(level, x, y, z, GetFilterType() > 0);
        ((HopperBlock)BlockListener.hopper).SetHasFilter(world, x, y, z, GetFilterType());
    }

    public int GetFilterType()
    {
        ItemStack filterStack = getStack(18);
        if(filterStack != null && filterStack.count > 0)
        {
            if(filterStack.itemId == Block.LADDER.asItem().id)
            {
                return 1;
            }
            if(filterStack.itemId == Block.TRAPDOOR.asItem().id)
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
    public boolean CanCurrentFilterProcessItem(Item item)
    {
        int iFilterType = GetFilterType();
        if(iFilterType > 0)
        {
            if(
                    item.id < 256 &&
                    item.id != Block.SAND.asItem().id &&
                    item.id != Block.GRAVEL.asItem().id &&
                    item.id != Block.TORCH.asItem().id &&
                    item.id != Block.LIT_REDSTONE_TORCH.asItem().id &&
                    item.id != Block.SAPLING.asItem().id &&
                    item.id != Block.DANDELION.asItem().id &&
                    item.id != Block.ROSE.asItem().id &&
                    item.id != Block.BROWN_MUSHROOM.asItem().id &&
                    item.id != Block.RED_MUSHROOM.asItem().id
            )
            {
                return false;
            }
            if(iFilterType == 2)
            {
                if(item.getMaxCount() < 64)
                {
                    return false;
                }
            } else
            if(iFilterType == 3)
            {
                if(
                        item.id != Block.SAND.asItem().id &&
                        item.id != Block.GRAVEL.asItem().id &&
                        item.id != Block.DANDELION.asItem().id &&
                        item.id != Block.ROSE.asItem().id &&
                        item.id != Block.BROWN_MUSHROOM.asItem().id &&
                        item.id != Block.RED_MUSHROOM.asItem().id &&
                        item.id != Item.APPLE.id &&
                        item.id != Item.COAL.id &&
                        item.id != Item.DIAMOND.id &&
                        item.id != Item.STRING.id &&
                        item.id != Item.FEATHER.id &&
                        item.id != Item.SEEDS.id &&
                        item.id != Item.FLINT.id &&
                        item.id != Item.GOLDEN_APPLE.id &&
                        item.id != Item.SNOWBALL.id &&
                        item.id != Item.CLAY.id &&
                        item.id != Item.PAPER.id &&
                        item.id != Item.SLIMEBALL.id &&
                        item.id != Item.EGG.id &&
                        item.id != Item.COOKIE.id &&
                        item.id != Item.GUNPOWDER.id &&
                        item.id != Item.REDSTONE.id &&
                        item.id != Item.GLOWSTONE_DUST.id &&
                        item.id != Item.DYE.id &&
                        item.id != Item.SUGAR.id &&
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
                        item.id != Block.SAND.asItem().id &&
                        item.id != Block.GRAVEL.asItem().id &&
                        item.id != Item.SEEDS.id &&
                        item.id != Item.GUNPOWDER.id &&
                        item.id != Item.REDSTONE.id &&
                        item.id != Item.GLOWSTONE_DUST.id &&
                        item.id != Item.DYE.id &&
                        item.id != Item.SUGAR.id &&
                        item.id != ItemListener.hempSeeds.id &&
                        item.id != ItemListener.flour.id &&
                        item.id != ItemListener.groundNetherrack.id &&
                        item.id != ItemListener.hellfireDust.id &&
                        item.id != ItemListener.coalDust.id
                )
                {
                    if(iFilterType == 5)
                    {
                        if(item.id != Item.STRING.id && item.id != Item.PAPER.id && item.id != ItemListener.hempFibers.id)
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
        return ((HopperBlock)BlockListener.hopper).IsBlockOn(world, x, y, z);
    }

    private void attemptItemEjection()
    {
        int itemIndex = InventoryHandler.getFirstOccupiedStackExcludingItem(this, ItemRegistry.INSTANCE.getId(Item.CLAY));
        if(itemIndex >= 0 && itemIndex < 18)
        {
            ItemStack invStack = getStack(itemIndex);
            int iEjectStackSize;
            if(8 > invStack.count)
            {
                iEjectStackSize = invStack.count;
            } else
            {
                iEjectStackSize = 8;
            }
            ItemStack ejectStack = new ItemStack(invStack.itemId, iEjectStackSize, invStack.getDamage());
            int iTargetI = x;
            int iTargetJ = y - 1;
            int iTargetK = z;
            boolean bEjectIntoWorld = false;
            if(world.isAir(iTargetI, iTargetJ, iTargetK))
            {
                bEjectIntoWorld = true;
            } else
            {
                int blockId = world.getBlockId(iTargetI, iTargetJ, iTargetK);
                if(ReplaceableBlockChecker.IsReplaceableBlock(world, iTargetI, iTargetJ, iTargetK))
                {
                    bEjectIntoWorld = true;
                } else
                {
                    Block targetBlock = Block.BLOCKS[blockId];
                    if(!targetBlock.material.isSolid())
                    {
                        bEjectIntoWorld = true;
                    } else
                    {
                        BlockEntity targetTileEntityBase = world.getBlockEntity(iTargetI, iTargetJ, iTargetK);
                        int iNumItemsStored = 0;
                        if(targetTileEntityBase != null && (targetTileEntityBase instanceof Inventory))
                        {
                            int iMinSlotToAddTo = 0;
                            int iMaxSlotToAddTo = ((Inventory)targetTileEntityBase).size() - 1;
                            boolean canProcessStack = true;
                            if(blockId == Block.FURNACE.id || blockId == Block.LIT_FURNACE.id)
                            {
                                iMaxSlotToAddTo = 0;
                            } else
                            if(blockId == BlockListener.hopper.id)
                            {
                                iMaxSlotToAddTo = 17;
                                canProcessStack = ((HopperBlockEntity)targetTileEntityBase).CanCurrentFilterProcessItem(Item.ITEMS[ejectStack.itemId]);
                            }
                            if(canProcessStack)
                            {
                                if(!InventoryHandler.addItemWithinSlotBounds((Inventory)targetTileEntityBase, ejectStack, iMinSlotToAddTo, iMaxSlotToAddTo))
                                {
                                    if(blockId == Block.CHEST.id)
                                    {
                                        int iTempFacing = 2;
                                        do
                                        {
                                            if(iTempFacing > 5)
                                            {
                                                break;
                                            }
                                            BlockPosition tempOffset = new BlockPosition(iTempFacing);
                                            int tempBlockId = world.getBlockId(iTargetI + tempOffset.i, iTargetJ + tempOffset.j, iTargetK + tempOffset.k);
                                            if(tempBlockId == Block.CHEST.id)
                                            {
                                                targetTileEntityBase = world.getBlockEntity(iTargetI + tempOffset.i, iTargetJ + tempOffset.j, iTargetK + tempOffset.k);
                                                if(targetTileEntityBase != null && (targetTileEntityBase instanceof Inventory))
                                                {
                                                    if(InventoryHandler.addItemInstanceToInventory((Inventory)targetTileEntityBase, ejectStack))
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
                                    removeStack(itemIndex, iNumItemsStored);
                                    world.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.pop", 0.25F, ((world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                                    if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                                        voicePacket(world, "random.pop", x, y, z, 0.25F, ((world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                                    }
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
                List list = world.collectEntitiesByClass(MinecartEntity.class, Box.createCached((float)x + 0.4F, (float)y - 0.5F, (float)z + 0.4F, (float)x + 0.6F, y, (float)z + 0.6F));
                if(list != null && list.size() > 0)
                {
                    int listIndex = 0;
                    do
                    {
                        if(listIndex >= list.size())
                        {
                            break;
                        }
                        MinecartEntity minecartEntity = (MinecartEntity)list.get(listIndex);
                        if(minecartEntity.type == 1 && minecartEntity.boundingBox.intersects(Box.createCached((float)x, (float)y - 0.5F, (float)z, (float)x + 0.25F, y, (float)z + 1.0F)) && minecartEntity.boundingBox.intersects(Box.createCached((float)x + 0.75F, (float)y - 0.5F, (float)z, (float)x + 1.0F, y, (float)z + 1.0F)) && minecartEntity.boundingBox.intersects(Box.createCached((float)x, (float)y - 0.5F, (float)z, (float)x + 1.0F, y, (float)z + 0.25F)) && minecartEntity.boundingBox.intersects(Box.createCached((float)x, (float)y - 0.5F, (float)z + 0.75F, (float)x + 1.0F, y, (float)z + 1.0F)))
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
                                removeStack(itemIndex, iNumItemsStored);
                                world.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.pop", 0.25F, ((world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                                if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                                    voicePacket(world, "random.pop", x, y, z, 0.25F, ((world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                                }
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
                removeStack(itemIndex, iEjectStackSize);
            }
        }
    }

    private void EjectStack(ItemStack stack)
    {
        float xOffset = world.random.nextFloat() * 0.1F + 0.45F;
        float yOffset = -0.35F;
        float zOffset = world.random.nextFloat() * 0.1F + 0.45F;
        ItemEntity entityitem = new ItemEntity(world, (float)x + xOffset, (float)y + yOffset, (float)z + zOffset, stack);
        entityitem.velocityX = 0.0D;
        entityitem.velocityY = -0.0099999997764825821D;
        entityitem.velocityZ = 0.0D;
        entityitem.pickupDelay = 10;
        world.spawnEntity(entityitem);
    }

    private void hopperSoulOverload()
    {
        if(SpawnGhast())
        {
            world.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "mob.ghast.scream", 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
            if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                voicePacket(world, "mob.ghast.scream", x, y, z, 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
            }
        }
        ((HopperBlock)BlockListener.hopper).BreakHopper(world, x, y, z);
    }

    private boolean SpawnGhast()
    {
        LivingEntity entityliving = (LivingEntity) EntityRegistry.create("Ghast", world);
        if(entityliving == null)
        {
            return false;
        }
        for(int i = 0; i < 200; i++)
        {
            double xPos = (double)x + (world.random.nextDouble() - world.random.nextDouble()) * 10D;
            double yPos = (y + world.random.nextInt(21)) - 10;
            double zPos = (double)z + (world.random.nextDouble() - world.random.nextDouble()) * 10D;
            entityliving.setPositionAndAnglesKeepPrevAngles(xPos, yPos, zPos, world.random.nextFloat() * 360F, 0.0F);
            if(entityliving.canSpawn())
            {
                world.spawnEntity(entityliving);
                return true;
            }
        }

        return false;
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(World world, String name, int x, int y, int z, float g, float h){
        List list2 = world.players;
        if(list2.size() != 0) {
            for(int k = 0; k < list2.size(); k++)
            {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g,h));
            }
        }
    }

    private ItemStack[] hopperContents;
    private int ejectCounter;
    public boolean hopperEjectBlocked;
    public int clientOccupiedSlots;
    public int clientFilterType;
}
