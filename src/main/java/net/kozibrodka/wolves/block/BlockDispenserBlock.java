
package net.kozibrodka.wolves.block;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.container.BlockDispenserScreenHandler;
import net.kozibrodka.wolves.entity.BroadheadArrowEntity;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ScreenHandlerListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.mixin.BlockBaseAccessor;
import net.kozibrodka.wolves.mixin.ChickenAccessor;
import net.kozibrodka.wolves.mixin.WolfAccessor;
import net.kozibrodka.wolves.network.ScreenPacket;
import net.kozibrodka.wolves.network.ParticlePacket;
import net.kozibrodka.wolves.network.RenderPacket;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.block.entity.BlockDispenserBlockEntity;
import net.kozibrodka.wolves.utils.*;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SeedsItem;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;

import java.util.List;
import java.util.Random;


public class BlockDispenserBlock extends TemplateBlockWithEntity
    implements RotatableBlock
{

    public BlockDispenserBlock(Identifier iid)
    {
        super(iid, Material.STONE);
    }

    public int getTextureId(BlockView iblockaccess, int i, int j, int k, int iSide)
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

    public int getTexture(int iSide)
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

    public int getTickRate()
    {
        return 4;
    }

    public int getDroppedItemId(int i, Random random)
    {
        return BlockListener.blockDispenser.id;
    }

    public void onPlaced(World world, int i, int j, int k, int iFacing)
    {
        SetFacing(world, i, j, k, UnsortedUtils.GetOppositeFacing(iFacing));
        world.method_216(i, j, k, BlockListener.blockDispenser.id, getTickRate());
    }

    public void onPlaced(World world, int i, int j, int k, LivingEntity entityLiving)
    {
        int iFacing = UnsortedUtils.ConvertPlacingEntityOrientationToBlockFacing(entityLiving);
        SetFacing(world, i, j, k, iFacing);
        world.method_216(i, j, k, BlockListener.blockDispenser.id, getTickRate());
    }

    public boolean onUse(World world, int i, int j, int k, PlayerEntity entityplayer) {
        if (world == null) {
            return true;
        }
        BlockDispenserBlockEntity tileEntityBlockDispenser = (BlockDispenserBlockEntity)world.getBlockEntity(i, j, k);
        ScreenHandlerListener.TempGuiX = i;
        ScreenHandlerListener.TempGuiY = j;
        ScreenHandlerListener.TempGuiZ = k;
        if(world.isRemote){
            PacketHelper.send(new ScreenPacket("dispenser",0, i, j, k));
        }
        GuiHelper.openGUI(entityplayer, Identifier.of("wolves:openBlockDispenser"), (Inventory) tileEntityBlockDispenser, new BlockDispenserScreenHandler(entityplayer.inventory, (BlockDispenserBlockEntity) tileEntityBlockDispenser));
        return true;
    }

    protected BlockEntity createBlockEntity()
    {
        return new BlockDispenserBlockEntity();
    }

    public void neighborUpdate(World world, int i, int j, int k, int iChangedid)
    {
        world.method_216(i, j, k, BlockListener.blockDispenser.id, getTickRate());
    }

    public void onBreak(World world, int i, int j, int k)
    {
        InventoryHandler.ejectInventoryContents(world, i, j, k, (Inventory)world.getBlockEntity(i, j, k));
        super.onBreak(world, i, j, k);
    }

    public void onTick(World world, int i, int j, int k, Random random)
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
        return iBlockAccess.getBlockMeta(i, j, k) & -9;
    }

    public void SetFacing(World world, int i, int j, int k, int iFacing)
    {
        int iMetaData = world.getBlockMeta(i, j, k) & 8;
        iMetaData |= iFacing;
        world.method_215(i, j, k, iMetaData);
    }

    public boolean CanRotate(BlockView iBlockAccess, int i, int j, int l)
    {
        return true;
    }

    public boolean CanTransmitRotation(BlockView iBlockAccess, int i, int j, int l)
    {
        return true;
    }

    public void Rotate(World world, int i, int j, int k, boolean bReverse)
    {
        int iFacing = GetFacing(world, i, j, k);
        int iNewFacing = UnsortedUtils.RotateFacingAroundJ(iFacing, bReverse);
        if(iNewFacing != iFacing)
        {
            SetFacing(world, i, j, k, iNewFacing);
            world.method_202(i, j, k, i, j, k);
        }
    }

    public boolean IsOn(World world, int i, int j, int k)
    {
        int iMetaData = world.getBlockMeta(i, j, k);
        return (iMetaData & 8) > 0;
    }

    private void TurnOn(World world, int i, int j, int k)
    {
        int iMetaData = world.getBlockMeta(i, j, k);
        iMetaData |= 8;
        world.method_215(i, j, k, iMetaData);
        world.method_243(i, j, k);
        DispenseBlockOrItem(world, i, j, k, world.field_214);
    }

    private void TurnOff(World world, int i, int j, int k)
    {
        int iMetaData = world.getBlockMeta(i, j, k);
        iMetaData &= -9;
        world.method_215(i, j, k, iMetaData);
        world.method_243(i, j, k);
        ConsumeFacingBlock(world, i, j, k);
    }

    private boolean IsBlockConsumable(Block targetBlock)
    {
        return  targetBlock != Block.BEDROCK &&
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

    private boolean AddBlockToInventory(World world, int i, int j, int k, Block targetBlock, int iTargetMetaData)
    {
        ValidateBlockDispenser(world, i, j, k);
        BlockDispenserBlockEntity tileEentityDispenser = (BlockDispenserBlockEntity)world.getBlockEntity(i, j, k);
        int itemDamage = ((BlockBaseAccessor) targetBlock).invokeDroppedMeta(iTargetMetaData);
        int iTargetidDropped;
        if(targetBlock.id == Block.STONE.id)
        {
            iTargetidDropped = Block.STONE.id;
        } else
        if(targetBlock.id == Block.GRAVEL.id)
        {
            iTargetidDropped = Block.GRAVEL.id;
        } else
        if(targetBlock.id == Block.CLAY.id)
        {
            iTargetidDropped = Block.CLAY.id;
        } else
        if(targetBlock.id == Block.GLOWSTONE.id)
        {
            iTargetidDropped = Block.GLOWSTONE.id;
        } else
        if(targetBlock.id == Block.SNOW_BLOCK.id)
        {
            iTargetidDropped = Block.SNOW_BLOCK.id;
        } else
        if(targetBlock.id == Block.CAKE.id)
        {
            if((iTargetMetaData & -9) == 0)
            {
                iTargetidDropped = Item.CAKE.id;
            } else
            {
                return false;
            }
        } else
        if(targetBlock.id == Block.LEAVES.id)
        {
            iTargetidDropped = Block.LEAVES.id;
        } else
        {
            iTargetidDropped = targetBlock.getDroppedItemId(iTargetMetaData, world.field_214);
        }
        if(iTargetidDropped > 0)
        {
            return InventoryHandler.addSingleItemToInventory(tileEentityDispenser, iTargetidDropped, itemDamage);
        } else
        {
            return false;
        }
    }

    private boolean ConsumeEntityAtTargetLoc(World world, int i, int j, int k, int targeti, int targetj, int targetk)
    {
        ValidateBlockDispenser(world, i, j, k);
        List list = null;
        list = world.method_175(Entity.class, Box.createCached(targeti, targetj, targetk, targeti + 1, targetj + 1, targetk + 1));
        if(list != null && list.size() > 0)
        {
            BlockDispenserBlockEntity tileEentityDispenser = (BlockDispenserBlockEntity)world.getBlockEntity(i, j, k);
            for(int listIndex = 0; listIndex < list.size(); listIndex++)
            {
                Entity targetEntity = (Entity)list.get(listIndex);
                if(targetEntity.dead)
                {
                    continue;
                }
                if(targetEntity instanceof MinecartEntity)
                {
                    MinecartEntity targetMinecart = (MinecartEntity)targetEntity;
                    int minecartType = targetMinecart.field_2275;
                    if(targetMinecart.field_1594 != null)
                    {
                        targetMinecart.field_1594.method_1376(targetMinecart);
                    }
                    targetMinecart.markDead();
                    switch(minecartType)
                    {
                    case 0: // '\0'
                        InventoryHandler.addSingleItemToInventory(tileEentityDispenser, Item.MINECART.id, 0);
                        break;

                    case 1: // '\001'
                        InventoryHandler.addSingleItemToInventory(tileEentityDispenser, Item.CHEST_MINECART.id, 0);
                        break;

                    default:
                        InventoryHandler.addSingleItemToInventory(tileEentityDispenser, Item.FURNACE_MINECART.id, 0);
                        break;
                    }
                     world.playSound(i, j, k, "random.click", 1.0F, 1.2F);
                    if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                        voicePacket(world, "random.click", i, j, k, 1.0F, 1.2F);
                    }
                    return true;
                }
                if(targetEntity instanceof BoatEntity)
                {
                    targetEntity.markDead();
                    InventoryHandler.addSingleItemToInventory(tileEentityDispenser, Item.BOAT.id, 0);
                     world.playSound(i, j, k, "random.click", 1.0F, 1.2F);
                    if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                        voicePacket(world, "random.click", i, j, k, 1.0F, 1.2F);
                    }
                    return true;
                }
                if(targetEntity instanceof WolfEntity)
                {
                    WolfEntity targetWolf = (WolfEntity)targetEntity;
                    world.playSound(targetEntity, ((WolfAccessor) targetWolf).invokeGetHurtSound(), ((WolfAccessor) targetWolf).invokeGetSoundVolume(), (world.field_214.nextFloat() - world.field_214.nextFloat()) * 0.2F + 1.0F);
                    if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                        voicePacket(world, ((WolfAccessor) targetWolf).invokeGetHurtSound(), (int) targetEntity.x, (int) targetEntity.y, (int) targetEntity.z, ((WolfAccessor) targetWolf).invokeGetSoundVolume(), (world.field_214.nextFloat() - world.field_214.nextFloat()) * 0.2F + 1.0F);
                    }
                    targetEntity.markDead();
                    InventoryHandler.addSingleItemToInventory(tileEentityDispenser, BlockListener.companionCube.id, 0);
                    for(int tempCount = 0; tempCount < 2; tempCount++)
                    {
                        SpitOutItem(world, i, j, k, new ItemStack(Item.STRING), world.field_214);
                    }

                    return true;
                }
                if(targetEntity instanceof ChickenEntity)
                {
                    ChickenEntity targetChicken = (ChickenEntity)targetEntity;
                    world.playSound(targetEntity, ((ChickenAccessor) targetChicken).invokeGetHurtSound(), 1.0F, (world.field_214.nextFloat() - world.field_214.nextFloat()) * 0.2F + 1.0F);
                    if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                        voicePacket(world, ((ChickenAccessor) targetChicken).invokeGetHurtSound(), (int) targetEntity.x, (int) targetEntity.y, (int) targetEntity.z, 1.0F, (world.field_214.nextFloat() - world.field_214.nextFloat()) * 0.2F + 1.0F);
                    }
                    targetEntity.markDead();
                    InventoryHandler.addSingleItemToInventory(tileEentityDispenser, Item.EGG.id, 0);
                    SpitOutItem(world, i, j, k, new ItemStack(Item.FEATHER), world.field_214);
                    return true;
                }
            }

        }
        return false;
    }

    private void ConsumeFacingBlock(World world, int i, int j, int k)
    {
        int iFacingDirection = GetFacing(world, i, j, k);
        BlockPosition targetPos = new BlockPosition(i, j, k);
        targetPos.AddFacingAsOffset(iFacingDirection);
        if(!ConsumeEntityAtTargetLoc(world, i, j, k, targetPos.i, targetPos.j, targetPos.k) && !world.method_234(targetPos.i, targetPos.j, targetPos.k))
        {
            int iTargetid = world.getBlockId(targetPos.i, targetPos.j, targetPos.k);
            Block targetBlock = Block.BLOCKS[iTargetid];
            if(targetBlock != null && IsBlockConsumable(targetBlock))
            {
                int iTargetMetaData = world.getBlockMeta(targetPos.i, targetPos.j, targetPos.k);
                if(iTargetid == id)
                {
                    BlockDispenserBlockEntity targetTileEentityDispenser = (BlockDispenserBlockEntity)world.getBlockEntity(targetPos.i, targetPos.j, targetPos.k);
                    InventoryHandler.clearInventoryContents(targetTileEentityDispenser);
                }
                if(AddBlockToInventory(world, i, j, k, targetBlock, iTargetMetaData))
                {
                    if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                        renderPacket(world, targetPos.i, targetPos.j, targetPos.k, iTargetid, iTargetMetaData);
                        voicePacket(world, targetBlock.soundGroup.getSound(), i, j, k, (targetBlock.soundGroup.method_1976() + 1.0F) / 2.0F, targetBlock.soundGroup.method_1977() * 0.8F);
                    }else{
                        Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).field_2808.method_322(targetPos.i, targetPos.j, targetPos.k, iTargetid, iTargetMetaData);
                        world.playSound((float)targetPos.i + 0.5F, (float)targetPos.j + 0.5F, (float)targetPos.k + 0.5F, targetBlock.soundGroup.getSound(), (targetBlock.soundGroup.method_1976() + 1.0F) / 2.0F, targetBlock.soundGroup.method_1977() * 0.8F);
                    }
                	world.setBlock(targetPos.i, targetPos.j, targetPos.k, 0);
                }
            }
        }
    }

    @Environment(EnvType.SERVER)
    public void renderPacket(World world, int x, int y, int z, int blockId, int metaId){
        List list2 = world.field_200;
        if(list2.size() != 0) {
            for(int k = 0; k < list2.size(); k++)
            {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new RenderPacket(x, y, z, blockId, metaId));
            }
        }
    }

    private void SpitOutItem(World world, int i, int j, int k, ItemStack ItemInstance, Random random)
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
        ItemEntity entityitem = new ItemEntity(world, d, d1 - 0.29999999999999999D, d2, ItemInstance);
        double d3 = random.nextDouble() * 0.10000000000000001D + 0.20000000000000001D;
        entityitem.velocityX = (double)f * d3;
        entityitem.velocityY = (double)deltaj * d3 + 0.20000000298023221D;
        entityitem.velocityZ = (double)f1 * d3;
        entityitem.velocityX += random.nextGaussian() * 0.0074999998323619366D * 6D;
        entityitem.velocityY += random.nextGaussian() * 0.0074999998323619366D * 6D;
        entityitem.velocityZ += random.nextGaussian() * 0.0074999998323619366D * 6D;
        world.method_210(entityitem);
    }

    private void DispenseBlockOrItem(World world, int i, int j, int k, Random random)
    {
        ValidateBlockDispenser(world, i, j, k);
        int iFacing = GetFacing(world, i, j, k);
        BlockPosition targetPos = new BlockPosition(i, j, k);
        targetPos.AddFacingAsOffset(iFacing);
        int iTargetid = world.getBlockId(targetPos.i, targetPos.j, targetPos.k);
        Block targetBlock = Block.BLOCKS[iTargetid];
        boolean shouldDispense = false;
        boolean bSuccessfullyDispensed = false;
        if(targetBlock == null)
        {
            shouldDispense = true;
        } else
        if(ReplaceableBlockChecker.IsReplaceableBlock(world, targetPos.i, targetPos.j, targetPos.k) || !targetBlock.material.method_905())
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
            BlockDispenserBlockEntity tileEntityBlockDispenser = (BlockDispenserBlockEntity)world.getBlockEntity(i, j, k);
            ItemStack iteminstance = tileEntityBlockDispenser.GetNextStackFromInventory();
            double d = (double)targetPos.i + (double)f * 0.5D + 0.5D;
            double d1 = (double)targetPos.j + (double)deltaj + 0.5D;
            double d2 = (double)targetPos.k + (double)f1 * 0.5D + 0.5D;
            if(iteminstance != null)
            {
                if(deltaj < 0.1F && deltaj > -0.1F)
                {
                    deltaj = 0.1F;
                }
                if(iteminstance.itemId == Item.ARROW.id)
                {
                    ArrowEntity entityarrow = new ArrowEntity(world, d, d1, d2);
                    entityarrow.method_1291(f, deltaj, f1, 1.1F, 6F);
                    world.method_210(entityarrow);
                    world.playSound(i, j, k, "random.bow", 1.0F, 1.2F);
                    if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                        voicePacket(world, "random.bow", i, j, k, 1.0F, 1.2F);
                    }
                    bSuccessfullyDispensed = true;
                } else
                if(iteminstance.itemId == ItemListener.broadHeadArrow.id)
                {
                    BroadheadArrowEntity entityarrow = new BroadheadArrowEntity(world, d, d1, d2);
                    entityarrow.method_1291(f, deltaj, f1, 1.1F, 6F);
                    entityarrow.velocityX *= 1.5D;
                    entityarrow.velocityY *= 1.5D;
                    entityarrow.velocityZ *= 1.5D;
                    world.method_210(entityarrow);
                    world.playSound(i, j, k, "random.bow", 1.0F, 1.2F);
                    if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                        voicePacket(world, "random.bow", i, j, k, 1.0F, 1.2F);
                    }
                    bSuccessfullyDispensed = true;
                } else
                if(iteminstance.itemId == Item.EGG.id)
                {
                    EggEntity entityegg = new EggEntity(world, d, d1, d2);
                    entityegg.method_1682(f, deltaj, f1, 1.1F, 6F);
                    world.method_210(entityegg);
                    world.playSound(i, j, k, "random.bow", 1.0F, 1.2F);
                    if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                        voicePacket(world, "random.bow", i, j, k, 1.0F, 1.2F);
                    }
                    bSuccessfullyDispensed = true;
                } else
                if(iteminstance.itemId == Item.SNOWBALL.id)
                {
                    SnowballEntity entitysnowball = new SnowballEntity(world, d, d1, d2);
                    entitysnowball.method_1656(f, deltaj, f1, 1.1F, 6F);
                    world.method_210(entitysnowball);
                    world.playSound(i, j, k, "random.bow", 1.0F, 1.2F);
                    if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                        voicePacket(world, "random.bow", i, j, k, 1.0F, 1.2F);
                    }
                    bSuccessfullyDispensed = true;
                } else
                if(iteminstance.itemId == Item.MINECART.id)
                {
                    MinecartEntity entityMinecart = new MinecartEntity(world, d + (double)f * 0.75D, d1 - 0.5D, d2 + (double)f1 * 0.75D, 0);
                    world.method_210(entityMinecart);
                    world.playSound(i, j, k, "random.click", 1.0F, 1.0F);
                    if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                        voicePacket(world, "random.click", i, j, k, 1.0F, 1.0F);
                    }
                    bSuccessfullyDispensed = true;
                } else
                if(iteminstance.itemId == Item.CHEST_MINECART.id)
                {
                    MinecartEntity entityMinecart = new MinecartEntity(world, d + (double)f * 0.75D, d1 - 0.5D, d2 + (double)f1 * 0.75D, 1);
                    world.method_210(entityMinecart);
                    world.playSound(i, j, k, "random.click", 1.0F, 1.0F);
                    if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                        voicePacket(world, "random.click", i, j, k, 1.0F, 1.0F);
                    }
                    bSuccessfullyDispensed = true;
                } else
                if(iteminstance.itemId == Item.FURNACE_MINECART.id)
                {
                    MinecartEntity entityMinecart = new MinecartEntity(world, d + (double)f * 0.75D, d1 - 0.5D, d2 + (double)f1 * 0.75D, 2);
                    world.method_210(entityMinecart);
                    world.playSound(i, j, k, "random.click", 1.0F, 1.0F);
                    if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                        voicePacket(world, "random.click", i, j, k, 1.0F, 1.0F);
                    }
                    bSuccessfullyDispensed = true;
                } else
                if(iteminstance.itemId == Item.BOAT.id)
                {
                    BoatEntity entityBoat = new BoatEntity(world, d + (double)f, d1 - 0.5D, d2 + (double)f1);
                    world.method_210(entityBoat);
                    world.playSound(i, j, k, "random.click", 1.0F, 1.0F);
                    if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                        voicePacket(world, "random.click", i, j, k, 1.0F, 1.0F);
                    }
                    bSuccessfullyDispensed = true;
                } else
                if(iteminstance.getItem() instanceof SeedsItem)
                {
                    iteminstance.count++;
                    if(!iteminstance.getItem().useOnBlock(iteminstance, null, world, targetPos.i, targetPos.j - 1, targetPos.k, 1))
                    {
                        InventoryHandler.addSingleItemToInventory(tileEntityBlockDispenser, iteminstance.itemId, 0);
                    } else
                    {
                        Block newBlock = Block.WHEAT;
                        world.playSound((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, newBlock.soundGroup.getSound(), (newBlock.soundGroup.method_1976() + 1.0F) / 2.0F, newBlock.soundGroup.method_1977() * 0.8F);
                        if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                            voicePacket(world, newBlock.soundGroup.getSound(), i, j, k, (newBlock.soundGroup.method_1976() + 1.0F) / 2.0F, newBlock.soundGroup.method_1977() * 0.8F);
                        }
                        bSuccessfullyDispensed = true;
                    }
                } else
                {
                    Block newBlock = null;
                    if(iteminstance.itemId < 256)
                    {
                        newBlock = Block.BLOCKS[iteminstance.itemId];
                    } else
                    if(iteminstance.itemId == Item.SUGAR_CANE.id)
                    {
                        newBlock = Block.SUGAR_CANE;
                    } else
                    if(iteminstance.itemId == Item.CAKE.id)
                    {
                        newBlock = Block.CAKE;
                    } else
                    if(iteminstance.itemId == Item.REPEATER.id)
                    {
                        newBlock = Block.REPEATER;
                    } else
                    if(iteminstance.itemId == Item.REDSTONE.id)
                    {
                        newBlock = Block.REDSTONE_WIRE;
                    }
                    if(newBlock != null)
                    {
                        int iNewid = newBlock.id;
                        int iTargetDirection = UnsortedUtils.GetOppositeFacing(iFacing);
                        if(world.method_156(iNewid, targetPos.i, targetPos.j, targetPos.k, true, iTargetDirection))
                        {
                            if(iNewid == Block.PISTON.id || iNewid == Block.STICKY_PISTON.id)
                            {
                                world.method_201(targetPos.i, targetPos.j, targetPos.k, iNewid, iFacing);
                            } else
                            {
                                world.method_201(targetPos.i, targetPos.j, targetPos.k, iNewid, iteminstance.getItem().getPlacementMetadata(iteminstance.getDamage()));
                                newBlock.onPlaced(world, targetPos.i, targetPos.j, targetPos.k, iTargetDirection);
                            }
                            world.playSound((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, newBlock.soundGroup.getSound(), (newBlock.soundGroup.method_1976() + 1.0F) / 2.0F, newBlock.soundGroup.method_1977() * 0.8F);
                            if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                                voicePacket(world, newBlock.soundGroup.getSound(), i, j, k, (newBlock.soundGroup.method_1976() + 1.0F) / 2.0F, newBlock.soundGroup.method_1977() * 0.8F);
                            }
                            bSuccessfullyDispensed = true;
                        } else
                        {
                            InventoryHandler.addSingleItemToInventory(tileEntityBlockDispenser, iteminstance.itemId, iteminstance.getDamage());
                        }
                    } else
                    if(world.method_234(targetPos.i, targetPos.j, targetPos.k))
                    {
                        SpitOutItem(world, i, j, k, iteminstance, random);
                        world.playSound(i, j, k, "random.click", 1.0F, 1.0F);
                        if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                            voicePacket(world, "random.click", i, j, k, 1.0F, 1.0F);
                        }
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
                    if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                        particlePacket(world, "reddust", d5, d6, d7, d8, d9, d10);
                    }
                }

            }
        }
        if(!bSuccessfullyDispensed)
        {
            world.playSound(i, j, k, "random.click", 1.0F, 1.2F);
            if(net.fabricmc.loader.FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                voicePacket(world, "random.click", i, j, k, 1.0F, 1.2F);
            }
        }
    }

    private boolean ValidateBlockDispenser(World world, int i, int j, int k)
    {
        BlockEntity tileentity = world.getBlockEntity(i, j, k);
        if(!(tileentity instanceof BlockDispenserBlockEntity))
        {
            BlockDispenserBlockEntity fctileentityblockdispenser = new BlockDispenserBlockEntity();
            if(tileentity instanceof DispenserBlockEntity)
            {
                DispenserBlockEntity tileentitydispenser = (DispenserBlockEntity)tileentity;
                int l = tileentitydispenser.size();
                int i1 = fctileentityblockdispenser.size();
                for(int j1 = 0; j1 < l && j1 < i1; j1++)
                {
                    ItemStack itemstack = tileentitydispenser.getStack(j1);
                    if(itemstack != null)
                    {
                        fctileentityblockdispenser.setStack(j1, itemstack.copy());
                    }
                }

            }
            world.method_157(i, j, k, fctileentityblockdispenser);
            return false;
        } else
        {
            return true;
        }
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(World world, String name, int x, int y, int z, float g, float h){
        List list2 = world.field_200;
        if(list2.size() != 0) {
            for(int k = 0; k < list2.size(); k++)
            {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g,h));
            }
        }
    }

    @Environment(EnvType.SERVER)
    public void particlePacket(World world, String name, double x, double y, double z, double i, double j, double k){
        List list2 = world.field_200;
        if(list2.size() != 0) {
            for(int k1 = 0; k1 < list2.size(); k1++)
            {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k1);
                PacketHelper.sendTo(player1, new ParticlePacket(name, x, y, z,i,j,k));
            }
        }
    }

    public final int blockDispenserTextureIDFront = 7;
    public final int blockDispenserTextureIDSide = 8;
    public final int blockDispenserTextureIDTop = 6;
    public final int blockDispenserTextureIDBottom = 9;
    private final int iBlockDispenserTickRate = 4;

}
