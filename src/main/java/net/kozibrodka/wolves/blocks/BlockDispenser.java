
package net.kozibrodka.wolves.blocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.container.BlockDispenserContainer;
import net.kozibrodka.wolves.entity.BroadheadArrowEntity;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.GUIListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.mixin.BlockBaseAccessor;
import net.kozibrodka.wolves.mixin.ChickenAccessor;
import net.kozibrodka.wolves.mixin.WolfAccessor;
import net.kozibrodka.wolves.network.GuiPacket;
import net.kozibrodka.wolves.tileentity.BlockDispenserTileEntity;
import net.kozibrodka.wolves.utils.*;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.particle.Digging;
import net.minecraft.entity.*;
import net.minecraft.entity.animal.Chicken;
import net.minecraft.entity.animal.Wolf;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.projectile.Arrow;
import net.minecraft.entity.projectile.Egg;
import net.minecraft.entity.projectile.Snowball;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.Seeds;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;

import java.util.List;
import java.util.Random;


public class BlockDispenser extends TemplateBlockWithEntity
    implements RotatableBlock
{

    public BlockDispenser(Identifier iid)
    {
        super(iid, Material.STONE);
    }

    public int getTextureForSide(BlockView iblockaccess, int i, int j, int k, int iSide)
    {
        int iFacing = GetFacing(iblockaccess, i, j, k);
        if(iSide == iFacing)
        {
            return TextureListener.dispenser_face;
        }
        if(iSide == 1)
        {
            return TextureListener.dispenser_top;
        }
        if(iSide == 0)
        {
            return TextureListener.dispenser_bottom;
        } else
        {
            return TextureListener.dispenser_side;
        }
    }

    public int getTextureForSide(int iSide)
    {
        if(iSide == 3)
        {
            return TextureListener.dispenser_face;
        }
        if(iSide == 1)
        {
            return TextureListener.dispenser_top;
        }
        if(iSide == 0)
        {
            return TextureListener.dispenser_bottom;
        } else
        {
            return TextureListener.dispenser_side;
        }
    }

    public int getTickrate()
    {
        return 4;
    }

    public int getDropId(int i, Random random)
    {
        return BlockListener.blockDispenser.id;
    }

    public void onBlockPlaced(Level world, int i, int j, int k, int iFacing)
    {
        SetFacing(world, i, j, k, UnsortedUtils.GetOppositeFacing(iFacing));
        world.method_216(i, j, k, BlockListener.blockDispenser.id, getTickrate());
    }

    public void afterPlaced(Level world, int i, int j, int k, Living entityLiving)
    {
        int iFacing = UnsortedUtils.ConvertPlacingEntityOrientationToBlockFacing(entityLiving);
        SetFacing(world, i, j, k, iFacing);
        world.method_216(i, j, k, BlockListener.blockDispenser.id, getTickrate());
    }

    public boolean canUse(Level world, int i, int j, int k, PlayerBase entityplayer) {
        if (world == null) {
            return true;
        }
        BlockDispenserTileEntity tileEntityBlockDispenser = (BlockDispenserTileEntity)world.getTileEntity(i, j, k);
        GUIListener.TempGuiX = i;
        GUIListener.TempGuiY = j;
        GUIListener.TempGuiZ = k;
        if(world.isServerSide){
            PacketHelper.send(new GuiPacket("dispenser",0, i, j, k));
        }
        GuiHelper.openGUI(entityplayer, Identifier.of("wolves:openBlockDispenser"), (InventoryBase) tileEntityBlockDispenser, new BlockDispenserContainer(entityplayer.inventory, (BlockDispenserTileEntity) tileEntityBlockDispenser));
        return true;
    }

    protected TileEntityBase createTileEntity()
    {
        return new BlockDispenserTileEntity();
    }

    public void onAdjacentBlockUpdate(Level world, int i, int j, int k, int iChangedid)
    {
        world.method_216(i, j, k, BlockListener.blockDispenser.id, getTickrate());
    }

    public void onBlockRemoved(Level world, int i, int j, int k)
    {
        InventoryHandler.ejectInventoryContents(world, i, j, k, (InventoryBase)world.getTileEntity(i, j, k));
        super.onBlockRemoved(world, i, j, k);
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
        ValidateBlockDispenser(world, i, j, k);
        boolean bIsPowered = world.method_263(i, j, k) || world.method_263(i, j + 1, k);
        if(bIsPowered)
        {
            if(!IsOn(world, i, j, k))
            {
                TurnOn(world, i, j, k);
            }
        } else
        if(IsOn(world, i, j, k))
        {
            TurnOff(world, i, j, k);
        }
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int k)
    {
        return iBlockAccess.getTileMeta(i, j, k) & -9;
    }

    public void SetFacing(Level world, int i, int j, int k, int iFacing)
    {
        int iMetaData = world.getTileMeta(i, j, k) & 8;
        iMetaData |= iFacing;
        world.setTileMeta(i, j, k, iMetaData);
    }

    public boolean CanRotate(BlockView iBlockAccess, int i, int j, int l)
    {
        return true;
    }

    public boolean CanTransmitRotation(BlockView iBlockAccess, int i, int j, int l)
    {
        return true;
    }

    public void Rotate(Level world, int i, int j, int k, boolean bReverse)
    {
        int iFacing = GetFacing(world, i, j, k);
        int iNewFacing = UnsortedUtils.RotateFacingAroundJ(iFacing, bReverse);
        if(iNewFacing != iFacing)
        {
            SetFacing(world, i, j, k, iNewFacing);
            world.method_202(i, j, k, i, j, k);
        }
    }

    public boolean IsOn(Level world, int i, int j, int k)
    {
        int iMetaData = world.getTileMeta(i, j, k);
        return (iMetaData & 8) > 0;
    }

    private void TurnOn(Level world, int i, int j, int k)
    {
        int iMetaData = world.getTileMeta(i, j, k);
        iMetaData |= 8;
        world.setTileMeta(i, j, k, iMetaData);
        world.method_243(i, j, k);
        DispenseBlockOrItem(world, i, j, k, world.rand);
    }

    private void TurnOff(Level world, int i, int j, int k)
    {
        int iMetaData = world.getTileMeta(i, j, k);
        iMetaData &= -9;
        world.setTileMeta(i, j, k, iMetaData);
        world.method_243(i, j, k);
        ConsumeFacingBlock(world, i, j, k);
    }

    private boolean IsBlockConsumable(BlockBase targetBlock)
    {
        return  targetBlock != BlockBase.BEDROCK &&
                targetBlock != BlockBase.FLOWING_WATER &&
                targetBlock != BlockBase.STILL_WATER &&
                targetBlock != BlockBase.FLOWING_LAVA &&
                targetBlock != BlockBase.STILL_LAVA &&
                targetBlock != BlockListener.cement &&
                targetBlock != BlockBase.FIRE &&
                targetBlock != BlockBase.MOB_SPAWNER &&
                targetBlock != BlockBase.PORTAL &&
                targetBlock != BlockBase.PISTON_HEAD &&
                targetBlock != BlockBase.MOVING_PISTON &&
                targetBlock != BlockBase.ICE;
    }

    private boolean AddBlockToInventory(Level world, int i, int j, int k, BlockBase targetBlock, int iTargetMetaData)
    {
        ValidateBlockDispenser(world, i, j, k);
        BlockDispenserTileEntity tileEentityDispenser = (BlockDispenserTileEntity)world.getTileEntity(i, j, k);
        int itemDamage = ((BlockBaseAccessor) targetBlock).invokeDroppedMeta(iTargetMetaData);
        int iTargetidDropped;
        if(targetBlock.id == BlockBase.STONE.id)
        {
            iTargetidDropped = BlockBase.STONE.id;
        } else
        if(targetBlock.id == BlockBase.GRAVEL.id)
        {
            iTargetidDropped = BlockBase.GRAVEL.id;
        } else
        if(targetBlock.id == BlockBase.CLAY.id)
        {
            iTargetidDropped = BlockBase.CLAY.id;
        } else
        if(targetBlock.id == BlockBase.GLOWSTONE.id)
        {
            iTargetidDropped = BlockBase.GLOWSTONE.id;
        } else
        if(targetBlock.id == BlockBase.SNOW_BLOCK.id)
        {
            iTargetidDropped = BlockBase.SNOW_BLOCK.id;
        } else
        if(targetBlock.id == BlockBase.CAKE.id)
        {
            if((iTargetMetaData & -9) == 0)
            {
                iTargetidDropped = ItemBase.cake.id;
            } else
            {
                return false;
            }
        } else
        if(targetBlock.id == BlockBase.LEAVES.id)
        {
            iTargetidDropped = BlockBase.LEAVES.id;
        } else
        {
            iTargetidDropped = targetBlock.getDropId(iTargetMetaData, world.rand);
        }
        if(iTargetidDropped > 0)
        {
            return InventoryHandler.addSingleItemToInventory(tileEentityDispenser, iTargetidDropped, itemDamage);
        } else
        {
            return false;
        }
    }

    private boolean ConsumeEntityAtTargetLoc(Level world, int i, int j, int k, int targeti, int targetj, int targetk)
    {
        ValidateBlockDispenser(world, i, j, k);
        List list = null;
        list = world.getEntities(EntityBase.class, Box.createButWasteMemory(targeti, targetj, targetk, targeti + 1, targetj + 1, targetk + 1));
        if(list != null && list.size() > 0)
        {
            BlockDispenserTileEntity tileEentityDispenser = (BlockDispenserTileEntity)world.getTileEntity(i, j, k);
            for(int listIndex = 0; listIndex < list.size(); listIndex++)
            {
                EntityBase targetEntity = (EntityBase)list.get(listIndex);
                if(targetEntity.removed)
                {
                    continue;
                }
                if(targetEntity instanceof Minecart)
                {
                    Minecart targetMinecart = (Minecart)targetEntity;
                    int minecartType = targetMinecart.type;
                    if(targetMinecart.passenger != null)
                    {
                        targetMinecart.passenger.startRiding(targetMinecart);
                    }
                    targetMinecart.remove();
                    switch(minecartType)
                    {
                    case 0: // '\0'
                        InventoryHandler.addSingleItemToInventory(tileEentityDispenser, ItemBase.minecart.id, 0);
                        break;

                    case 1: // '\001'
                        InventoryHandler.addSingleItemToInventory(tileEentityDispenser, ItemBase.minecartChest.id, 0);
                        break;

                    default:
                        InventoryHandler.addSingleItemToInventory(tileEentityDispenser, ItemBase.minecartFurnace.id, 0);
                        break;
                    }
                     world.playSound(i, j, k, "random.click", 1.0F, 1.2F);
                    return true;
                }
                if(targetEntity instanceof Boat)
                {
                    targetEntity.remove();
                    InventoryHandler.addSingleItemToInventory(tileEentityDispenser, ItemBase.boat.id, 0);
                     world.playSound(i, j, k, "random.click", 1.0F, 1.2F);
                    return true;
                }
                if(targetEntity instanceof Wolf)
                {
                    Wolf targetWolf = (Wolf)targetEntity;
                    world.playSound(targetEntity, ((WolfAccessor) targetWolf).invokeGetHurtSound(), ((WolfAccessor) targetWolf).invokeGetSoundVolume(), (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
                    targetEntity.remove();
                    InventoryHandler.addSingleItemToInventory(tileEentityDispenser, BlockListener.companionCube.id, 0);
                    for(int tempCount = 0; tempCount < 2; tempCount++)
                    {
                        SpitOutItem(world, i, j, k, new ItemInstance(ItemBase.string), world.rand);
                    }

                    return true;
                }
                if(targetEntity instanceof Chicken)
                {
                    Chicken targetChicken = (Chicken)targetEntity;
                    world.playSound(targetEntity, ((ChickenAccessor) targetChicken).invokeGetHurtSound(), 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
                    targetEntity.remove();
                    InventoryHandler.addSingleItemToInventory(tileEentityDispenser, ItemBase.egg.id, 0);
                    SpitOutItem(world, i, j, k, new ItemInstance(ItemBase.feather), world.rand);
                    return true;
                }
            }

        }
        return false;
    }

    private void ConsumeFacingBlock(Level world, int i, int j, int k)
    {
        int iFacingDirection = GetFacing(world, i, j, k);
        BlockPosition targetPos = new BlockPosition(i, j, k);
        targetPos.AddFacingAsOffset(iFacingDirection);
        if(!ConsumeEntityAtTargetLoc(world, i, j, k, targetPos.i, targetPos.j, targetPos.k) && !world.isAir(targetPos.i, targetPos.j, targetPos.k))
        {
            int iTargetid = world.getTileId(targetPos.i, targetPos.j, targetPos.k);
            BlockBase targetBlock = BlockBase.BY_ID[iTargetid];
            if(targetBlock != null && IsBlockConsumable(targetBlock))
            {
                int iTargetMetaData = world.getTileMeta(targetPos.i, targetPos.j, targetPos.k);
                if(iTargetid == id)
                {
                    BlockDispenserTileEntity targetTileEentityDispenser = (BlockDispenserTileEntity)world.getTileEntity(targetPos.i, targetPos.j, targetPos.k);
                    InventoryHandler.clearInventoryContents(targetTileEentityDispenser);
                }
                if(AddBlockToInventory(world, i, j, k, targetBlock, iTargetMetaData))
                {
                    if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
//                        voicePacket(world, "random.explode", i, j, k, 0.2F, 1.25F);
                        //packecik
//                        nowy pakiet + dzwieki
                    }else{
                        Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).particleManager.addTileBreakParticles(targetPos.i, targetPos.j, targetPos.k, iTargetid, iTargetMetaData);
                    }

                    world.playSound((float)targetPos.i + 0.5F, (float)targetPos.j + 0.5F, (float)targetPos.k + 0.5F, targetBlock.sounds.getWalkSound(), (targetBlock.sounds.getVolume() + 1.0F) / 2.0F, targetBlock.sounds.getPitch() * 0.8F);
                	world.setTile(targetPos.i, targetPos.j, targetPos.k, 0);
                }
            }
        }
    }

    private void SpitOutItem(Level world, int i, int j, int k, ItemInstance ItemInstance, Random random)
    {
        int facing = GetFacing(world, i, j, k);
        float deltaj = 0.0F;
        float f = 0.0F;
        float f1 = 0.0F;
        switch(facing)
        {
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
        double d = (double)i + (double)f * 0.5D + 0.5D;
        double d1 = (double)j + (double)deltaj + 0.5D;
        double d2 = (double)k + (double)f1 * 0.5D + 0.5D;
        if(deltaj < 0.1F && deltaj > -0.1F)
        {
            deltaj = 0.1F;
        }
        Item entityitem = new Item(world, d, d1 - 0.29999999999999999D, d2, ItemInstance);
        double d3 = random.nextDouble() * 0.10000000000000001D + 0.20000000000000001D;
        entityitem.velocityX = (double)f * d3;
        entityitem.velocityY = (double)deltaj * d3 + 0.20000000298023221D;
        entityitem.velocityZ = (double)f1 * d3;
        entityitem.velocityX += random.nextGaussian() * 0.0074999998323619366D * 6D;
        entityitem.velocityY += random.nextGaussian() * 0.0074999998323619366D * 6D;
        entityitem.velocityZ += random.nextGaussian() * 0.0074999998323619366D * 6D;
        world.spawnEntity(entityitem);
    }

    private void DispenseBlockOrItem(Level world, int i, int j, int k, Random random)
    {
        ValidateBlockDispenser(world, i, j, k);
        int iFacing = GetFacing(world, i, j, k);
        BlockPosition targetPos = new BlockPosition(i, j, k);
        targetPos.AddFacingAsOffset(iFacing);
        int iTargetid = world.getTileId(targetPos.i, targetPos.j, targetPos.k);
        BlockBase targetBlock = BlockBase.BY_ID[iTargetid];
        boolean shouldDispense = false;
        boolean bSuccessfullyDispensed = false;
        if(targetBlock == null)
        {
            shouldDispense = true;
        } else
        if(ReplaceableBlockChecker.IsReplaceableBlock(world, targetPos.i, targetPos.j, targetPos.k) || !targetBlock.material.isSolid())
        {
            shouldDispense = true;
        }
        if(shouldDispense)
        {
            float deltaj = 0.0F;
            float f = 0.0F;
            float f1 = 0.0F;
            if(iFacing == 0)
            {
                deltaj = -1F;
            } else
            if(iFacing == 1)
            {
                deltaj = 1.0F;
            } else
            if(iFacing == 3)
            {
                f1 = 1.0F;
            } else
            if(iFacing == 2)
            {
                f1 = -1F;
            } else
            if(iFacing == 5)
            {
                f = 1.0F;
            } else
            {
                f = -1F;
            }
            BlockDispenserTileEntity tileEntityBlockDispenser = (BlockDispenserTileEntity)world.getTileEntity(i, j, k);
            ItemInstance iteminstance = tileEntityBlockDispenser.GetNextStackFromInventory();
            double d = (double)targetPos.i + (double)f * 0.5D + 0.5D;
            double d1 = (double)targetPos.j + (double)deltaj + 0.5D;
            double d2 = (double)targetPos.k + (double)f1 * 0.5D + 0.5D;
            if(iteminstance != null)
            {
                if(deltaj < 0.1F && deltaj > -0.1F)
                {
                    deltaj = 0.1F;
                }
                if(iteminstance.itemId == ItemBase.arrow.id)
                {
                    Arrow entityarrow = new Arrow(world, d, d1, d2);
                    entityarrow.method_1291(f, deltaj, f1, 1.1F, 6F);
                    world.spawnEntity(entityarrow);
                     world.playSound(i, j, k, "random.bow", 1.0F, 1.2F);
                    bSuccessfullyDispensed = true;
                } else
                if(iteminstance.itemId == ItemListener.broadHeadArrow.id)
                {
                    BroadheadArrowEntity entityarrow = new BroadheadArrowEntity(world, d, d1, d2);
                    entityarrow.method_1291(f, deltaj, f1, 1.1F, 6F);
                    entityarrow.velocityX *= 1.5D;
                    entityarrow.velocityY *= 1.5D;
                    entityarrow.velocityZ *= 1.5D;
                    world.spawnEntity(entityarrow);
                     world.playSound(i, j, k, "random.bow", 1.0F, 1.2F);
                    bSuccessfullyDispensed = true;
                } else
                if(iteminstance.itemId == ItemBase.egg.id)
                {
                    Egg entityegg = new Egg(world, d, d1, d2);
                    entityegg.method_1682(f, deltaj, f1, 1.1F, 6F);
                    world.spawnEntity(entityegg);
                     world.playSound(i, j, k, "random.bow", 1.0F, 1.2F);
                    bSuccessfullyDispensed = true;
                } else
                if(iteminstance.itemId == ItemBase.snowball.id)
                {
                    Snowball entitysnowball = new Snowball(world, d, d1, d2);
                    entitysnowball.method_1656(f, deltaj, f1, 1.1F, 6F);
                    world.spawnEntity(entitysnowball);
                     world.playSound(i, j, k, "random.bow", 1.0F, 1.2F);
                    bSuccessfullyDispensed = true;
                } else
                if(iteminstance.itemId == ItemBase.minecart.id)
                {
                    Minecart entityMinecart = new Minecart(world, d + (double)f * 0.75D, d1 - 0.5D, d2 + (double)f1 * 0.75D, 0);
                    world.spawnEntity(entityMinecart);
                     world.playSound(i, j, k, "random.click", 1.0F, 1.0F);
                    bSuccessfullyDispensed = true;
                } else
                if(iteminstance.itemId == ItemBase.minecartChest.id)
                {
                    Minecart entityMinecart = new Minecart(world, d + (double)f * 0.75D, d1 - 0.5D, d2 + (double)f1 * 0.75D, 1);
                    world.spawnEntity(entityMinecart);
                     world.playSound(i, j, k, "random.click", 1.0F, 1.0F);
                    bSuccessfullyDispensed = true;
                } else
                if(iteminstance.itemId == ItemBase.minecartFurnace.id)
                {
                    Minecart entityMinecart = new Minecart(world, d + (double)f * 0.75D, d1 - 0.5D, d2 + (double)f1 * 0.75D, 2);
                    world.spawnEntity(entityMinecart);
                     world.playSound(i, j, k, "random.click", 1.0F, 1.0F);
                    bSuccessfullyDispensed = true;
                } else
                if(iteminstance.itemId == ItemBase.boat.id)
                {
                    Boat entityBoat = new Boat(world, d + (double)f, d1 - 0.5D, d2 + (double)f1);
                    world.spawnEntity(entityBoat);
                     world.playSound(i, j, k, "random.click", 1.0F, 1.0F);
                    bSuccessfullyDispensed = true;
                } else
                if(iteminstance.getType() instanceof Seeds)
                {
                    iteminstance.count++;
                    if(!iteminstance.getType().useOnTile(iteminstance, null, world, targetPos.i, targetPos.j - 1, targetPos.k, 1))
                    {
                        InventoryHandler.addSingleItemToInventory(tileEntityBlockDispenser, iteminstance.itemId, 0);
                    } else
                    {
                        BlockBase newBlock = BlockBase.CROPS;
                         world.playSound((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, newBlock.sounds.getWalkSound(), (newBlock.sounds.getVolume() + 1.0F) / 2.0F, newBlock.sounds.getPitch() * 0.8F);
                        bSuccessfullyDispensed = true;
                    }
                } else
                {
                    BlockBase newBlock = null;
                    if(iteminstance.itemId < 256)
                    {
                        newBlock = BlockBase.BY_ID[iteminstance.itemId];
                    } else
                    if(iteminstance.itemId == ItemBase.sugarCanes.id)
                    {
                        newBlock = BlockBase.SUGAR_CANES;
                    } else
                    if(iteminstance.itemId == ItemBase.cake.id)
                    {
                        newBlock = BlockBase.CAKE;
                    } else
                    if(iteminstance.itemId == ItemBase.redstoneRepeater.id)
                    {
                        newBlock = BlockBase.REDSTONE_REPEATER;
                    } else
                    if(iteminstance.itemId == ItemBase.redstoneDust.id)
                    {
                        newBlock = BlockBase.REDSTONE_DUST;
                    }
                    if(newBlock != null)
                    {
                        int iNewid = newBlock.id;
                        int iTargetDirection = UnsortedUtils.GetOppositeFacing(iFacing);
                        if(world.canPlaceTile(iNewid, targetPos.i, targetPos.j, targetPos.k, true, iTargetDirection))
                        {
                            if(iNewid == BlockBase.PISTON.id || iNewid == BlockBase.STICKY_PISTON.id)
                            {
                                world.placeBlockWithMetaData(targetPos.i, targetPos.j, targetPos.k, iNewid, iFacing);
                            } else
                            {
                                world.placeBlockWithMetaData(targetPos.i, targetPos.j, targetPos.k, iNewid, iteminstance.getType().getMetaData(iteminstance.getDamage()));
                                newBlock.onBlockPlaced(world, targetPos.i, targetPos.j, targetPos.k, iTargetDirection);
                            }
                             world.playSound((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, newBlock.sounds.getWalkSound(), (newBlock.sounds.getVolume() + 1.0F) / 2.0F, newBlock.sounds.getPitch() * 0.8F);
                            bSuccessfullyDispensed = true;
                        } else
                        {
                            InventoryHandler.addSingleItemToInventory(tileEntityBlockDispenser, iteminstance.itemId, iteminstance.getDamage());
                        }
                    } else
                    if(world.isAir(targetPos.i, targetPos.j, targetPos.k))
                    {
                        SpitOutItem(world, i, j, k, iteminstance, random);
                         world.playSound(i, j, k, "random.click", 1.0F, 1.0F);
                        bSuccessfullyDispensed = true;
                    } else
                    {
                        InventoryHandler.addSingleItemToInventory(tileEntityBlockDispenser, iteminstance.itemId, iteminstance.getDamage());
                    }
                }
            }
            if(bSuccessfullyDispensed)
            {
                for(int i1 = 0; i1 < 10; i1++)
                {
                    double d4 = random.nextDouble() * 0.20000000000000001D + 0.01D;
                    double d5 = d + (double)f * 0.01D + (random.nextDouble() - 0.5D) * (double)f1 * 0.5D;
                    double d6 = d1 + (double)deltaj * 0.01D + (random.nextDouble() - 0.5D) * 0.5D;
                    double d7 = d2 + (double)f1 * 0.01D + (random.nextDouble() - 0.5D) * (double)f * 0.5D;
                    double d8 = (double)f * d4 + random.nextGaussian() * 0.01D;
                    double d9 = (double)deltaj * d4 + -0.029999999999999999D + random.nextGaussian() * 0.01D;
                    double d10 = (double)f1 * d4 + random.nextGaussian() * 0.01D;
                    world.addParticle("smoke", d5, d6, d7, d8, d9, d10);
                }

            }
        }
        if(!bSuccessfullyDispensed)
        {
             world.playSound(i, j, k, "random.click", 1.0F, 1.2F);
        }
    }

    private boolean ValidateBlockDispenser(Level world, int i, int j, int k)
    {
        TileEntityBase tileentity = world.getTileEntity(i, j, k);
        if(!(tileentity instanceof BlockDispenserTileEntity))
        {
            BlockDispenserTileEntity fctileentityblockdispenser = new BlockDispenserTileEntity();
            if(tileentity instanceof TileEntityDispenser)
            {
                TileEntityDispenser tileentitydispenser = (TileEntityDispenser)tileentity;
                int l = tileentitydispenser.getInventorySize();
                int i1 = fctileentityblockdispenser.getInventorySize();
                for(int j1 = 0; j1 < l && j1 < i1; j1++)
                {
                    ItemInstance itemstack = tileentitydispenser.getInventoryItem(j1);
                    if(itemstack != null)
                    {
                        fctileentityblockdispenser.setInventoryItem(j1, itemstack.copy());
                    }
                }

            }
            world.setTileEntity(i, j, k, fctileentityblockdispenser);
            return false;
        } else
        {
            return true;
        }
    }

    public final int blockDispenserTextureIDFront = 7;
    public final int blockDispenserTextureIDSide = 8;
    public final int blockDispenserTextureIDTop = 6;
    public final int blockDispenserTextureIDBottom = 9;
    private final int iBlockDispenserTickRate = 4;

}
