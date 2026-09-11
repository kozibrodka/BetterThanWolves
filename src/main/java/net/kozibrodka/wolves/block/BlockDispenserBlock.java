package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.block.entity.BlockDispenserBlockEntity;
import net.kozibrodka.wolves.container.BlockDispenserScreenHandler;
import net.kozibrodka.wolves.entity.BroadheadArrowEntity;
import net.kozibrodka.wolves.entity.SteelMinecartEntity;
import net.kozibrodka.wolves.events.BlockEntityListener;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.ParticlePacket;
import net.kozibrodka.wolves.network.RenderPacket;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.*;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SeedsItem;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Random;


public class BlockDispenserBlock extends TemplateBlockWithEntity implements RotatableBlock {

    public BlockDispenserBlock(Identifier iid) {
        super(iid, Material.STONE);
    }

    @Override
    public int getTextureId(BlockView blockView, int i, int j, int k, int iSide) {
        int iFacing = getFacing(blockView, i, j, k);
        if (iSide == iFacing) {
            return TextureListener.dispenser_face;
        }
        if (iSide == 1) {
            return TextureListener.dispenser_top;
        }
        if (iSide == 0) {
            return TextureListener.dispenser_bottom;
        } else {
            return TextureListener.dispenser_side;
        }
    }

    @Override
    public int getTexture(int iSide) {
        if (iSide == 3) {
            return TextureListener.dispenser_face;
        }
        if (iSide == 1) {
            return TextureListener.dispenser_top;
        }
        if (iSide == 0) {
            return TextureListener.dispenser_bottom;
        } else {
            return TextureListener.dispenser_side;
        }
    }

    @Override
    public int getTickRate() {
        return 4;
    }

    @Override
    public void onPlaced(World world, int i, int j, int k, int iFacing) {
        setFacing(world, i, j, k, UnsortedUtils.getOppositeFacing(iFacing));
        world.scheduleBlockUpdate(i, j, k, BlockListener.blockDispenser.id, getTickRate());
    }

    @Override
    public void onPlaced(World world, int i, int j, int k, LivingEntity entityLiving) {
        int iFacing = UnsortedUtils.ConvertPlacingEntityOrientationToBlockFacing(entityLiving);
        setFacing(world, i, j, k, iFacing);
        world.scheduleBlockUpdate(i, j, k, BlockListener.blockDispenser.id, getTickRate());
    }

    @Override
    public boolean onUse(World world, int i, int j, int k, PlayerEntity playerEntity) {
        if (world == null) {
            return true;
        }
        BlockDispenserBlockEntity blockDispenserBlockEntity = (BlockDispenserBlockEntity) world.getBlockEntity(i, j, k);
        GuiHelper.openGUI(playerEntity, Identifier.of(BlockEntityListener.NAMESPACE, "openBlockDispenser"), blockDispenserBlockEntity, new BlockDispenserScreenHandler(playerEntity.inventory, blockDispenserBlockEntity));
        return true;
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new BlockDispenserBlockEntity();
    }

    @Override
    public void neighborUpdate(World world, int i, int j, int k, int iChangedid) {
        world.scheduleBlockUpdate(i, j, k, BlockListener.blockDispenser.id, getTickRate());
    }

    @Override
    public void onBreak(World world, int i, int j, int k) {
        InventoryHandler.ejectInventoryContents(world, i, j, k, (Inventory) world.getBlockEntity(i, j, k));
        super.onBreak(world, i, j, k);
    }

    @Override
    public void onTick(World world, int i, int j, int k, Random random) {
        ValidateBlockDispenser(world, i, j, k);
        boolean bIsPowered = world.isStrongPowered(i, j, k) || world.isStrongPowered(i, j + 1, k);
        if (bIsPowered) {
            if (!IsOn(world, i, j, k)) {
                TurnOn(world, i, j, k);
            }
        } else if (IsOn(world, i, j, k)) {
            TurnOff(world, i, j, k);
        }
    }

    @Override
    public int getFacing(BlockView iBlockAccess, int i, int j, int k) {
        return iBlockAccess.getBlockMeta(i, j, k) & -9;
    }

    @Override
    public void setFacing(World world, int i, int j, int k, int iFacing) {
        int iMetaData = world.getBlockMeta(i, j, k) & 8;
        iMetaData |= iFacing;
        world.setBlockMeta(i, j, k, iMetaData);
    }

    @Override
    public boolean canRotate(BlockView iBlockAccess, int i, int j, int l) {
        return true;
    }

    @Override
    public boolean canTransmitRotation(BlockView iBlockAccess, int i, int j, int l) {
        return true;
    }

    @Override
    public void rotate(World world, int i, int j, int k, boolean bReverse) {
        int iFacing = getFacing(world, i, j, k);
        int iNewFacing = UnsortedUtils.RotateFacingAroundJ(iFacing, bReverse);
        if (iNewFacing != iFacing) {
            setFacing(world, i, j, k, iNewFacing);
            world.setBlocksDirty(i, j, k, i, j, k);
            world.blockUpdateEvent(i, j, k);
        }
    }

    public boolean IsOn(World world, int i, int j, int k) {
        int iMetaData = world.getBlockMeta(i, j, k);
        return (iMetaData & 8) > 0;
    }

    private void TurnOn(World world, int i, int j, int k) {
        int iMetaData = world.getBlockMeta(i, j, k);
        iMetaData |= 8;
        world.setBlockMeta(i, j, k, iMetaData);
        world.blockUpdateEvent(i, j, k);
        DispenseBlockOrItem(world, i, j, k, world.random);
    }

    private void TurnOff(World world, int i, int j, int k) {
        int iMetaData = world.getBlockMeta(i, j, k);
        iMetaData &= -9;
        world.setBlockMeta(i, j, k, iMetaData);
        world.blockUpdateEvent(i, j, k);
        ConsumeFacingBlock(world, i, j, k);
    }

    private boolean IsBlockConsumable(Block targetBlock) {
        return targetBlock != Block.BEDROCK &&
                targetBlock != Block.FLOWING_WATER &&
                targetBlock != Block.WATER &&
                targetBlock != Block.FLOWING_LAVA &&
                targetBlock != Block.LAVA &&
                targetBlock != BlockListener.cement &&
                targetBlock != Block.FIRE &&
                targetBlock != Block.SPAWNER &&
                targetBlock != Block.NETHER_PORTAL &&
                targetBlock != Block.PISTON_HEAD &&
                targetBlock != Block.MOVING_PISTON &&
                targetBlock != Block.ICE;
    }

    private boolean AddBlockToInventory(World world, int i, int j, int k, Block targetBlock, int itemMetaData) {
        ValidateBlockDispenser(world, i, j, k);
        BlockDispenserBlockEntity tileEentityDispenser = (BlockDispenserBlockEntity) world.getBlockEntity(i, j, k);
        int itemDamage = targetBlock.getDroppedItemMeta(itemMetaData);
        int droppedItemId;
        if (targetBlock.id == Block.STONE.id) {
            droppedItemId = Block.STONE.asItem().id;
        } else if (targetBlock.id == Block.GRAVEL.id) {
            droppedItemId = Block.GRAVEL.asItem().id;
        } else if (targetBlock.id == Block.CLAY.id) {
            droppedItemId = Block.CLAY.asItem().id;
        } else if (targetBlock.id == Block.GLOWSTONE.id) {
            droppedItemId = Block.GLOWSTONE.asItem().id;
        } else if (targetBlock.id == Block.SNOW_BLOCK.id) {
            droppedItemId = Block.SNOW_BLOCK.asItem().id;
        } else if (targetBlock.id == Block.CAKE.id) {
            if ((itemMetaData & -9) == 0) {
                droppedItemId = Item.CAKE.id;
            } else {
                return false;
            }
        } else if (targetBlock.id == Block.LEAVES.id) {
            droppedItemId = Block.LEAVES.asItem().id;
        } else {
            droppedItemId = targetBlock.getDroppedItemId(itemMetaData, world.random);
        }

        if (droppedItemId > 0) {
            return InventoryHandler.addSingleItemToInventory(tileEentityDispenser, droppedItemId, itemDamage);
        } else {
            return false;
        }
    }

    private boolean ConsumeEntityAtTargetLoc(World world, int i, int j, int k, int targeti, int targetj, int targetk) {
        ValidateBlockDispenser(world, i, j, k);
        List list = null;
        list = world.collectEntitiesByClass(Entity.class, Box.createCached(targeti, targetj, targetk, targeti + 1, targetj + 1, targetk + 1));
        if (list != null && list.size() > 0) {
            BlockDispenserBlockEntity tileEentityDispenser = (BlockDispenserBlockEntity) world.getBlockEntity(i, j, k);
            for (int listIndex = 0; listIndex < list.size(); listIndex++) {
                Entity targetEntity = (Entity) list.get(listIndex);
                if (targetEntity.dead) {
                    continue;
                }
                if (targetEntity instanceof MinecartEntity targetMinecart) {
                    int minecartType = targetMinecart.type;
                    if (targetMinecart.passenger != null) {
                        targetMinecart.passenger.setVehicle(targetMinecart);
                    }
                    targetMinecart.markDead();
                    switch (minecartType) {
                        case 0: // '\0'
                            if(targetMinecart instanceof SteelMinecartEntity){
                                InventoryHandler.addSingleItemToInventory(tileEentityDispenser, ItemListener.steelMinecart.id, 0);
                            }else{
                                InventoryHandler.addSingleItemToInventory(tileEentityDispenser, Item.MINECART.id, 0);
                            }
                            break;

                        case 1: // '\001'
                            InventoryHandler.addSingleItemToInventory(tileEentityDispenser, Item.CHEST_MINECART.id, 0);
                            break;

                        default:
                            InventoryHandler.addSingleItemToInventory(tileEentityDispenser, Item.FURNACE_MINECART.id, 0);
                            break;
                    }
                    world.playNoteBlockActionAt(i,j,k, 0, 2); /// Click 2
                    return true;
                }
                if (targetEntity instanceof BoatEntity) {
                    targetEntity.markDead();
                    InventoryHandler.addSingleItemToInventory(tileEentityDispenser, Item.BOAT.id, 0);
                    world.playNoteBlockActionAt(i,j,k, 0, 2); /// Click 2
                    return true;
                }
                if (targetEntity instanceof WolfEntity targetWolf) {
                    world.playNoteBlockActionAt(i,j,k, 0, 5); /// Wolf
                    targetEntity.markDead();
                    InventoryHandler.addSingleItemToInventory(tileEentityDispenser, BlockListener.companionCube.asItem().id, 0);
                    for (int tempCount = 0; tempCount < 2; tempCount++) {
                        SpitOutItem(world, i, j, k, new ItemStack(Item.STRING), world.random);
                    }

                    return true;
                }
                if (targetEntity instanceof ChickenEntity targetChicken) {
                    world.playNoteBlockActionAt(i,j,k, 0, 6); /// Chicken
                    targetEntity.markDead();
                    InventoryHandler.addSingleItemToInventory(tileEentityDispenser, Item.EGG.id, 0);
                    SpitOutItem(world, i, j, k, new ItemStack(Item.FEATHER), world.random);
                    return true;
                }
            }

        }
        return false;
    }

    private void ConsumeFacingBlock(World world, int i, int j, int k) {
        int iFacingDirection = getFacing(world, i, j, k);
        BlockPosition targetPos = new BlockPosition(i, j, k);
        targetPos.addFacingAsOffset(iFacingDirection);
        if (!ConsumeEntityAtTargetLoc(world, i, j, k, targetPos.x, targetPos.y, targetPos.z) && !world.isAir(targetPos.x, targetPos.y, targetPos.z)) {
            int blockId = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
            Block targetBlock = Block.BLOCKS[blockId];
            if (targetBlock != null && IsBlockConsumable(targetBlock)) {
                int blockMetaData = world.getBlockMeta(targetPos.x, targetPos.y, targetPos.z);
                if (blockId == id) {
                    BlockDispenserBlockEntity targetTileEentityDispenser = (BlockDispenserBlockEntity) world.getBlockEntity(targetPos.x, targetPos.y, targetPos.z);
                    InventoryHandler.clearInventoryContents(targetTileEentityDispenser);
                }
                if (AddBlockToInventory(world, i, j, k, targetBlock, blockMetaData)) {
                    if(WolvesMpUtils.isEnvServ()){ /// custom - niszczenie bloku
                        WolvesMpUtils.renderPacket(world, targetPos.x, targetPos.y, targetPos.z, blockId, blockMetaData);
                    }else{
                        WolvesClientUtils.minecraft.particleManager.addBlockBreakParticles(targetPos.x, targetPos.y, targetPos.z, blockId, blockMetaData);
                        world.playSound((float) targetPos.x + 0.5F, (float) targetPos.y + 0.5F, (float) targetPos.z + 0.5F, targetBlock.soundGroup.getSound(), (targetBlock.soundGroup.getVolume() + 1.0F) / 2.0F, targetBlock.soundGroup.getPitch() * 0.8F);
                    }
                    world.setBlock(targetPos.x, targetPos.y, targetPos.z, 0);
                }
            }
        }
    }

    private void SpitOutItem(World world, int i, int j, int k, ItemStack ItemInstance, Random random) {
        int facing = getFacing(world, i, j, k);
        float deltaj = 0.0F;
        float f = 0.0F;
        float f1 = 0.0F;
        switch (facing) {
            case 0: // '\0'
                deltaj = -1F;
                break;

            case 1: // '\001'
                deltaj = 1.0F;
                break;

            case 2: // '\002'
                f1 = -1F;
                break;

            case 3: // '\003'
                f1 = 1.0F;
                break;

            case 4: // '\004'
                f = -1F;
                break;

            default:
                f = 1.0F;
                break;
        }
        double d = (double) i + (double) f * 0.5D + 0.5D;
        double d1 = (double) j + (double) deltaj + 0.5D;
        double d2 = (double) k + (double) f1 * 0.5D + 0.5D;
        if (deltaj < 0.1F && deltaj > -0.1F) {
            deltaj = 0.1F;
        }
        ItemEntity entityitem = new ItemEntity(world, d, d1 - 0.29999999999999999D, d2, ItemInstance);
        double d3 = random.nextDouble() * 0.10000000000000001D + 0.20000000000000001D;
        entityitem.velocityX = (double) f * d3;
        entityitem.velocityY = (double) deltaj * d3 + 0.20000000298023221D;
        entityitem.velocityZ = (double) f1 * d3;
        entityitem.velocityX += random.nextGaussian() * 0.0074999998323619366D * 6D;
        entityitem.velocityY += random.nextGaussian() * 0.0074999998323619366D * 6D;
        entityitem.velocityZ += random.nextGaussian() * 0.0074999998323619366D * 6D;
        world.spawnEntity(entityitem);
    }

    private void DispenseBlockOrItem(World world, int i, int j, int k, Random random) {
        ValidateBlockDispenser(world, i, j, k);
        int iFacing = getFacing(world, i, j, k);
        BlockPosition targetPos = new BlockPosition(i, j, k);
        targetPos.addFacingAsOffset(iFacing);
        int blockId = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
        Block targetBlock = Block.BLOCKS[blockId];
        boolean shouldDispense = false;
        boolean bSuccessfullyDispensed = false;

        if (targetBlock == null) {
            shouldDispense = true;
        } else if (ReplaceableBlockChecker.IsReplaceableBlock(world, targetPos.x, targetPos.y, targetPos.z) || !targetBlock.material.isSolid()) {
            shouldDispense = true;
        }

        if (shouldDispense) {
            float deltaj = 0.0F;
            float f = 0.0F;
            float f1 = 0.0F;
            if (iFacing == 0) {
                deltaj = -1F;
            } else if (iFacing == 1) {
                deltaj = 1.0F;
            } else if (iFacing == 3) {
                f1 = 1.0F;
            } else if (iFacing == 2) {
                f1 = -1F;
            } else if (iFacing == 5) {
                f = 1.0F;
            } else {
                f = -1F;
            }
            BlockDispenserBlockEntity tileEntityBlockDispenser = (BlockDispenserBlockEntity) world.getBlockEntity(i, j, k);
            ItemStack dispensedItemStack = tileEntityBlockDispenser.GetNextStackFromInventory();
            double d = (double) targetPos.x + (double) f * 0.5D + 0.5D;
            double d1 = (double) targetPos.y + (double) deltaj + 0.5D;
            double d2 = (double) targetPos.z + (double) f1 * 0.5D + 0.5D;
            if (dispensedItemStack != null) {
                if (deltaj < 0.1F && deltaj > -0.1F) {
                    deltaj = 0.1F;
                }

                if (dispensedItemStack.itemId == Item.ARROW.id) {
                    ArrowEntity entityarrow = new ArrowEntity(world, d, d1, d2);
                    entityarrow.setVelocity(f, deltaj, f1, 1.1F, 6F);
                    world.spawnEntity(entityarrow);
                    world.playNoteBlockActionAt(i,j,k, 0, 3); /// Bow
                    bSuccessfullyDispensed = true;
                } else if (dispensedItemStack.itemId == ItemListener.broadHeadArrow.id) {
                    BroadheadArrowEntity entityarrow = new BroadheadArrowEntity(world, d, d1, d2);
                    entityarrow.method_1291(f, deltaj, f1, 1.1F, 6F);
                    entityarrow.velocityX *= 1.5D;
                    entityarrow.velocityY *= 1.5D;
                    entityarrow.velocityZ *= 1.5D;
                    world.spawnEntity(entityarrow);
                    world.playNoteBlockActionAt(i,j,k, 0, 3); /// Bow
                    bSuccessfullyDispensed = true;
                } else if (dispensedItemStack.itemId == Item.EGG.id) {
                    EggEntity entityegg = new EggEntity(world, d, d1, d2);
                    entityegg.setVelocity(f, deltaj, f1, 1.1F, 6F);
                    world.spawnEntity(entityegg);
                    world.playNoteBlockActionAt(i,j,k, 0, 3); /// Bow
                    bSuccessfullyDispensed = true;
                } else if (dispensedItemStack.itemId == Item.SNOWBALL.id) {
                    SnowballEntity entitysnowball = new SnowballEntity(world, d, d1, d2);
                    entitysnowball.setVelocity(f, deltaj, f1, 1.1F, 6F);
                    world.spawnEntity(entitysnowball);
                    world.playNoteBlockActionAt(i,j,k, 0, 3); /// Bow
                    bSuccessfullyDispensed = true;
                } else if (dispensedItemStack.itemId == Item.MINECART.id) {
                    MinecartEntity entityMinecart = new MinecartEntity(world, d + (double) f * 0.75D, d1 - 0.5D, d2 + (double) f1 * 0.75D, 0);
                    world.spawnEntity(entityMinecart);
                    world.playNoteBlockActionAt(i,j,k, 0, 1); /// Click 1
                    bSuccessfullyDispensed = true;
                } else if (dispensedItemStack.itemId == Item.CHEST_MINECART.id) {
                    MinecartEntity entityMinecart = new MinecartEntity(world, d + (double) f * 0.75D, d1 - 0.5D, d2 + (double) f1 * 0.75D, 1);
                    world.spawnEntity(entityMinecart);
                    world.playNoteBlockActionAt(i,j,k, 0, 1); /// Click 1
                    bSuccessfullyDispensed = true;
                } else if (dispensedItemStack.itemId == Item.FURNACE_MINECART.id) {
                    MinecartEntity entityMinecart = new MinecartEntity(world, d + (double) f * 0.75D, d1 - 0.5D, d2 + (double) f1 * 0.75D, 2);
                    world.spawnEntity(entityMinecart);
                    world.playNoteBlockActionAt(i,j,k, 0, 1); /// Click 1
                    bSuccessfullyDispensed = true;
                } else if(dispensedItemStack.itemId == ItemListener.steelMinecart.id){
                    SteelMinecartEntity entityMinecart = new SteelMinecartEntity(world, d + (double) f * 0.75D, d1 - 0.5D, d2 + (double) f1 * 0.75D, 0);
                    world.spawnEntity(entityMinecart);
                    world.playNoteBlockActionAt(i,j,k, 0, 1); /// Click 1
                    bSuccessfullyDispensed = true;
                }else if (dispensedItemStack.itemId == Item.BOAT.id) {
                    BoatEntity entityBoat = new BoatEntity(world, d + (double) f, d1 - 0.5D, d2 + (double) f1);
                    world.spawnEntity(entityBoat);
                    world.playNoteBlockActionAt(i,j,k, 0, 1); /// Click 1
                    bSuccessfullyDispensed = true;
                } else if (dispensedItemStack.getItem() instanceof SeedsItem) {
                    dispensedItemStack.count++;
                    if (!dispensedItemStack.getItem().useOnBlock(dispensedItemStack, null, world, targetPos.x, targetPos.y - 1, targetPos.z, 1)) {
                        InventoryHandler.addSingleItemToInventory(tileEntityBlockDispenser, dispensedItemStack.itemId, 0);
                    } else {
                        world.playNoteBlockActionAt(i,j,k, 0, 4); /// Wheat
                        bSuccessfullyDispensed = true;
                    }
                } else {
                    Block newBlock = null;
                    if (dispensedItemStack.getItem() instanceof BlockItem blockItem) {
                        newBlock = blockItem.getBlock();
                    } else if (dispensedItemStack.itemId == Item.SUGAR_CANE.id) {
                        newBlock = Block.SUGAR_CANE;
                    } else if (dispensedItemStack.itemId == Item.CAKE.id) {
                        newBlock = Block.CAKE;
                    } else if (dispensedItemStack.itemId == Item.REPEATER.id) {
                        newBlock = Block.REPEATER;
                    } else if (dispensedItemStack.itemId == Item.REDSTONE.id) {
                        newBlock = Block.REDSTONE_WIRE;
                    }

                    if (newBlock != null) {
                        int newBlockId = newBlock.id;
                        int iTargetDirection = UnsortedUtils.getOppositeFacing(iFacing);
                        if (world.canPlace(newBlockId, targetPos.x, targetPos.y, targetPos.z, true, iTargetDirection)) {
                            if (newBlockId == Block.PISTON.id || newBlockId == Block.STICKY_PISTON.id) {
                                world.setBlock(targetPos.x, targetPos.y, targetPos.z, newBlockId, iFacing);
                            } else {
                                world.setBlock(targetPos.x, targetPos.y, targetPos.z, newBlockId, dispensedItemStack.getItem().getPlacementMetadata(dispensedItemStack.getDamage()));
                                newBlock.onPlaced(world, targetPos.x, targetPos.y, targetPos.z, iTargetDirection);
                            }
                            world.playSound((float) targetPos.x + 0.5F, (float) targetPos.y + 0.5F, (float) targetPos.z + 0.5F, newBlock.soundGroup.getSound(), (newBlock.soundGroup.getVolume() + 1.0F) / 2.0F, newBlock.soundGroup.getPitch() * 0.8F);
                            if(WolvesMpUtils.isEnvServ()){ /// custom - stawianie bloku
                                WolvesMpUtils.soundPacket(world,targetPos.x, targetPos.y, targetPos.z, newBlock.soundGroup.getSound(), (newBlock.soundGroup.getVolume() + 1.0F) / 2.0F, newBlock.soundGroup.getPitch() * 0.8F);
                            }
                            bSuccessfullyDispensed = true;
                        } else {
                            InventoryHandler.addSingleItemToInventory(tileEntityBlockDispenser, dispensedItemStack.itemId, dispensedItemStack.getDamage());
                        }
                    } else if (world.isAir(targetPos.x, targetPos.y, targetPos.z)) {
                        SpitOutItem(world, i, j, k, dispensedItemStack, random);
                        world.playNoteBlockActionAt(i,j,k, 0, 1); /// Click 1
                        bSuccessfullyDispensed = true;
                    } else {
                        InventoryHandler.addSingleItemToInventory(tileEntityBlockDispenser, dispensedItemStack.itemId, dispensedItemStack.getDamage());
                    }
                }
            }

            if (bSuccessfullyDispensed) {
                world.playNoteBlockActionAt(i,j,k, 0, 7); /// Dispensed Particles
            }
        }

        if (!bSuccessfullyDispensed) {
            world.playNoteBlockActionAt(i,j,k, 0, 2); /// Click 2
        }
    }

    private boolean ValidateBlockDispenser(World world, int i, int j, int k) {
        BlockEntity tileentity = world.getBlockEntity(i, j, k);
        if (!(tileentity instanceof BlockDispenserBlockEntity)) {
            BlockDispenserBlockEntity fctileentityblockdispenser = new BlockDispenserBlockEntity();
            if (tileentity instanceof DispenserBlockEntity tileentitydispenser) {
                int l = tileentitydispenser.size();
                int i1 = fctileentityblockdispenser.size();
                for (int j1 = 0; j1 < l && j1 < i1; j1++) {
                    ItemStack itemstack = tileentitydispenser.getStack(j1);
                    if (itemstack != null) {
                        fctileentityblockdispenser.setStack(j1, itemstack.copy());
                    }
                }

            }
            world.setBlockEntity(i, j, k, fctileentityblockdispenser);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBlockAction(World world, int x, int y, int z, int code, int meta) {
//        System.out.println("Doszło " + code + "  " + meta);
        if(meta == 1){ /// Click 1
            world.playSound(x, y, z, "random.click", 1.0F, 1.0F);
        }else if(meta == 2){ /// Click 2
            world.playSound(x, y, z, "random.click", 1.0F, 1.2F);
        }else if(meta == 3){ /// Bow
            world.playSound(x, y, z, "random.bow", 1.0F, 1.2F);
        }else if(meta == 4){ /// Wheat
            BlockPosition targetPos = new BlockPosition(x, y, z);
            targetPos.addFacingAsOffset(getFacing(world, x, y, z));
            Block newBlock = Block.WHEAT;
            world.playSound((double) targetPos.x + 0.5D, (double) targetPos.y + 0.5D, (double) targetPos.z + 0.5D, newBlock.soundGroup.getSound(), (newBlock.soundGroup.getVolume() + 1.0F) / 2.0F, newBlock.soundGroup.getPitch() * 0.8F);
        }else if(meta == 5){ /// Wolf
            BlockPosition targetPos = new BlockPosition(x, y, z);
            targetPos.addFacingAsOffset(getFacing(world, x, y, z));
            world.playSound((double) targetPos.x + 0.5D, (double) targetPos.y + 0.5D, (double) targetPos.z + 0.5D, "mob.wolf.hurt", 0.4F, (world.random.nextFloat() - world.random.nextFloat()) * 0.2F + 1.0F);
        }else if(meta == 6){/// Chicken
            BlockPosition targetPos = new BlockPosition(x, y, z);
            targetPos.addFacingAsOffset(getFacing(world, x, y, z));
            world.playSound((double) targetPos.x + 0.5D, (double) targetPos.y + 0.5D, (double) targetPos.z + 0.5D, "mob.chickenhurt", 1.0F, (world.random.nextFloat() - world.random.nextFloat()) * 0.2F + 1.0F);
        }else if(meta == 7) { /// Dispensed Particles
            int iFacing = getFacing(world, x, y, z);
            BlockPosition targetPos = new BlockPosition(x, y, z);
            targetPos.addFacingAsOffset(iFacing);
            float deltaj = 0.0F;
            float f = 0.0F;
            float f1 = 0.0F;
            if (iFacing == 0) {
                deltaj = -1F;
            } else if (iFacing == 1) {
                deltaj = 1.0F;
            } else if (iFacing == 3) {
                f1 = 1.0F;
            } else if (iFacing == 2) {
                f1 = -1F;
            } else if (iFacing == 5) {
                f = 1.0F;
            } else {
                f = -1F;
            }
            double d = (double) targetPos.x + (double) f * 0.5D + 0.5D;
            double d1 = (double) targetPos.y + (double) deltaj + 0.5D;
            double d2 = (double) targetPos.z + (double) f1 * 0.5D + 0.5D;
            if (deltaj < 0.1F && deltaj > -0.1F) {
                deltaj = 0.1F;
            }
            for (int i1 = 0; i1 < 10; i1++) {
                double d4 = world.random.nextDouble() * 0.20000000000000001D + 0.01D;
                double d5 = d + (double) f * 0.01D + (world.random.nextDouble() - 0.5D) * (double) f1 * 0.5D;
                double d6 = d1 + (double) deltaj * 0.01D + (world.random.nextDouble() - 0.5D) * 0.5D;
                double d7 = d2 + (double) f1 * 0.01D + (world.random.nextDouble() - 0.5D) * (double) f * 0.5D;
                double d8 = (double) f * d4 + world.random.nextGaussian() * 0.01D;
                double d9 = (double) deltaj * d4 + -0.029999999999999999D + world.random.nextGaussian() * 0.01D;
                double d10 = (double) f1 * d4 + world.random.nextGaussian() * 0.01D;
                world.addParticle("smoke", d5, d6, d7, d8, d9, d10);
            }
        }
    }

    public final int blockDispenserTextureIDFront = 7;
    public final int blockDispenserTextureIDSide = 8;
    public final int blockDispenserTextureIDTop = 6;
    public final int blockDispenserTextureIDBottom = 9;
    private final int iBlockDispenserTickRate = 4;

}
