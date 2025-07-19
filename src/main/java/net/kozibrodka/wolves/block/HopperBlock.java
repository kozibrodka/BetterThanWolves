// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCBlockHopper.java

package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.api.AffectedByBellows;
import net.kozibrodka.wolves.block.entity.HopperBlockEntity;
import net.kozibrodka.wolves.container.HopperScreenHandler;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.events.ScreenHandlerListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.ScreenPacket;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.*;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Random;

@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithInventoryRenderer.class)
public class HopperBlock extends TemplateBlockWithEntity
        implements MechanicalDevice, RotatableBlock, BlockWithInventoryRenderer, AffectedByBellows {
    public HopperBlock(Identifier iid) {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSoundGroup(WOOD_SOUND_GROUP);
        setTickRandomly(true);
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setDefaultState(getDefaultState()
                .with(REDOUTPUT, false)
                .with(POWER, false)
                .with(FULL, 0)
                .with(FILTER, 0)
        );
    }

    public int getTickRate() {
        return hopperTickRate;
    }

    public Box getCollisionShape(World world, int i, int j, int k) {
        if (!HasFilter(world, i, j, k)) {
            return Box.create(i, j, k, (float) (i + 1), (float) j + 0.5F, (float) (k + 1));
        } else {
            return Box.create(i, j, k, (float) (i + 1), (float) j + 0.99F, (float) (k + 1));
        }
    }

    public Box getBoundingBox(World world, int i, int j, int k) {
        return Box.create(i, j, k, i + 1, (float) j + 1.0F, k + 1);
    }

    public void onPlaced(World world, int i, int j, int k) {
        super.onPlaced(world, i, j, k);
        world.scheduleBlockUpdate(i, j, k, id, getTickRate());
    }

    public int getTexture(int iSide) {
        if (iSide == 0) {
            return TextureListener.hopper_bottom;
        }
        return iSide != 1 ? TextureListener.hopper_side : TextureListener.hopper_top;
    }

    public boolean isOpaque() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public void neighborUpdate(World world, int i, int j, int k, int iid) {
        boolean bReceivingPower = IsInputtingMechanicalPower(world, i, j, k);
        if (IsBlockOn(world, i, j, k) != bReceivingPower) {
            world.scheduleBlockUpdate(i, j, k, id, getTickRate());
        }
        ((HopperBlockEntity) world.getBlockEntity(i, j, k)).hopperEjectBlocked = false;
    }

    public boolean onUse(World world, int i, int j, int k, PlayerEntity entityplayer) {
        HopperBlockEntity tileEntityHopper = (HopperBlockEntity) world.getBlockEntity(i, j, k);
        ScreenHandlerListener.TempGuiX = i;
        ScreenHandlerListener.TempGuiY = j;
        ScreenHandlerListener.TempGuiZ = k;
        if (world.isRemote) {
            PacketHelper.send(new ScreenPacket("hopper", 0, i, j, k));
        }
        GuiHelper.openGUI(entityplayer, Identifier.of("wolves:openHopper"), tileEntityHopper, new HopperScreenHandler(entityplayer.inventory, tileEntityHopper));
        return true;
    }

    protected BlockEntity createBlockEntity() {
        return new HopperBlockEntity();
    }

    public void onTick(World world, int i, int j, int k, Random random) {
        boolean bReceivingPower = IsInputtingMechanicalPower(world, i, j, k);
        boolean bOn = IsBlockOn(world, i, j, k);
        boolean bFull = IsHopperFull(world, i, j, k);
        boolean bRedstone = IsRedstoneOutputOn(world, i, j, k);
        if (bOn != bReceivingPower) {
            world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.explode", 0.2F, 1.25F);
            if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                voicePacket(world, "random.explode", i, j, k, 0.2F, 1.25F);
            }
            EmitHopperParticles(world, i, j, k, random);
            SetBlockOn(world, i, j, k, bReceivingPower);
        }
        if (bFull != bRedstone) {
            world.playSound(i, j, k, "random.click", 0.25F, 1.2F);
            if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                voicePacket(world, "random.click", i, j, k, 0.25F, 1.2F);
            }
            SetRedstoneOutputOn(world, i, j, k, bFull);
        }
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(World world, String name, int x, int y, int z, float g, float h) {
        List list2 = world.players;
        if (list2.size() != 0) {
            for (int k = 0; k < list2.size(); k++) {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g, h));
            }
        }
    }

    public void onBreak(World world, int i, int j, int k) {
        if (!SETTING_TILE) {
            InventoryHandler.ejectInventoryContents(world, i, j, k, (Inventory) world.getBlockEntity(i, j, k));
        }
        super.onBreak(world, i, j, k);
    }

    public void onEntityCollision(World world, int i, int j, int k, Entity entity) {
        List collisionList = null;
        if (world.isRemote) {
            return;
        }
        boolean bHasFilter = HasFilter(world, i, j, k);
        float fHopperHeight;
        if (!bHasFilter) {
            fHopperHeight = 0.5F;
        } else {
            fHopperHeight = 0.99F;
        }
        collisionList = world.collectEntitiesByClass(ItemEntity.class, Box.createCached((float) i, (float) j + fHopperHeight, (float) k, (float) (i + 1), (float) j + fHopperHeight + 0.05F, (float) (k + 1)));
        if (collisionList != null && collisionList.size() > 0) {
            HopperBlockEntity tileEntityHopper = (HopperBlockEntity) world.getBlockEntity(i, j, k);
            for (int listIndex = 0; listIndex < collisionList.size(); listIndex++) {
                ItemEntity targetEntityItem = (ItemEntity) collisionList.get(listIndex);
                boolean bSwallowed = false;
                if (!targetEntityItem.dead) {
                    Item targetItem = Item.ITEMS[targetEntityItem.stack.itemId];
                    if (tileEntityHopper.CanCurrentFilterProcessItem(targetItem)) {
                        int iFilterType = tileEntityHopper.GetFilterType();
                        int iTargetItemID = Item.ITEMS[targetEntityItem.stack.itemId].id;
                        if (iFilterType == 4 && iTargetItemID == Block.GRAVEL.asItem().id) {
                            ItemStack sandItemInstance = new ItemStack(Block.SAND.asItem().id, targetEntityItem.stack.count, 0);
                            int iSandSwallowed = 0;
                            if (InventoryHandler.addItemWithinSlotBounds(tileEntityHopper, sandItemInstance, 0, 17)) {
                                iSandSwallowed = targetEntityItem.stack.count;
                                targetEntityItem.markDead();
                                bSwallowed = true;
                            } else {
                                iSandSwallowed = targetEntityItem.stack.count - sandItemInstance.count;
                                targetEntityItem.stack.count -= iSandSwallowed;
                            }
                            if (iSandSwallowed > 0) {
                                world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.pop", 0.25F, ((world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                                if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                                    voicePacket(world, "random.pop", i, j, k, 0.25F, ((world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                                }
                                ItemStack flintItemInstance = new ItemStack(Item.FLINT.id, iSandSwallowed, 0);
                                ItemEntity flintEntityitem = new ItemEntity(world, targetEntityItem.x, targetEntityItem.y, targetEntityItem.z, flintItemInstance);
                                flintEntityitem.pickupDelay = 10;
                                world.spawnEntity(flintEntityitem);
                            }
                        } else if (InventoryHandler.addItemWithinSlotBounds(tileEntityHopper, targetEntityItem.stack, 0, 17)) {
                            world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.pop", 0.25F, ((world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                            if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                                voicePacket(world, "random.pop", i, j, k, 0.25F, ((world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                            }
                            targetEntityItem.markDead();
                            bSwallowed = true;
                        }
                    }
                }
                if (!bHasFilter || bSwallowed) {
                    continue;
                }
                int iBlockAboveID = world.getBlockId(i, j + 1, k);
                if (iBlockAboveID != Block.FLOWING_WATER.id && iBlockAboveID != Block.WATER.id) {
                    continue;
                }
                double fHopperFullBoxTop = (double) j + 1.05D;
                if (targetEntityItem.boundingBox.minY < fHopperFullBoxTop) {
                    double offset = fHopperFullBoxTop - targetEntityItem.boundingBox.minY;
                    targetEntityItem.setPos(targetEntityItem.x, targetEntityItem.y + offset, targetEntityItem.z);
                }
            }
//            if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER){
//                renderPacket(world, tileEntityHopper.x, tileEntityHopper.y, tileEntityHopper.z, tileEntityHopper.GetFilterType(), InventoryHandler.getOccupiedSlotCountWithinBounds(tileEntityHopper, 0, 17));
//            }
//            world.blockUpdateEvent(i, j, k);
        }
        //TODO: Interaction with minecarts?
    }

    public boolean isEmittingRedstonePower(BlockView iBlockAccess, int i, int j, int k, int l) {
        switch (FabricLoader.INSTANCE.getEnvironmentType()) {
            case CLIENT -> {
                return powerClient(iBlockAccess, i, j, k, l);
            }
            case SERVER -> {
                return powerServer(iBlockAccess, i, j, k, l);
            }
        }
        return false;
    }

    @Environment(EnvType.CLIENT)
    public boolean powerClient(BlockView iBlockAccess, int i, int j, int k, int l) {
        World level = ((Minecraft) FabricLoader.INSTANCE.getGameInstance()).world;
        return IsRedstoneOutputOn(level, i, j, k);
    }

    @Environment(EnvType.SERVER)
    public boolean powerServer(BlockView iBlockAccess, int i, int j, int k, int l) {
        World level = ((MinecraftServer) net.fabricmc.loader.api.FabricLoader.getInstance().getGameInstance()).method_2157(0);
        return IsRedstoneOutputOn(level, i, j, k);
        //TODO: dimension?
    }

    public boolean canTransferPowerInDirection(World world, int i, int j, int i1, int j1) {
        return false;
    }

    public boolean canEmitRedstonePower() {
        return true;
    }

    public boolean IsBlockOn(World world, int i, int j, int k) {
        if (world.getBlockId(i, j, k) == BlockListener.hopper.id) {
            return (world.getBlockState(i, j, k).get(POWER));
        } else {
            return false;
        }
    }

    public void SetBlockOn(World world, int i, int j, int k, boolean bOn) {
        BlockEntity tileEntityBase = world.getBlockEntity(i, j, k);
        SETTING_TILE = true;

        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i, j, k, currentState.with(POWER, bOn));

        SETTING_TILE = false;
        tileEntityBase.cancelRemoval();
        world.setBlockEntity(i, j, k, tileEntityBase);
    }

    public boolean IsHopperFull(World world, int i, int j, int k) {
        if (world.getBlockId(i, j, k) == BlockListener.hopper.id) {
            return (world.getBlockState(i, j, k).get(FULL)) == 18;
        } else {
            return false;
        }
    }

    public void SetHopperFull(World world, int i, int j, int k, int bOn) {
        BlockEntity tileEntityBase = world.getBlockEntity(i, j, k);
        SETTING_TILE = true;

        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i, j, k, currentState.with(FULL, bOn));

        SETTING_TILE = false;
        tileEntityBase.cancelRemoval();
        world.setBlockEntity(i, j, k, tileEntityBase);
    }

    public boolean IsRedstoneOutputOn(World world, int i, int j, int k) {
        if (world.getBlockId(i, j, k) == BlockListener.hopper.id) {
            return (world.getBlockState(i, j, k).get(REDOUTPUT));
        } else {
            return false;
        }
    }

    public void SetRedstoneOutputOn(World world, int i, int j, int k, boolean bOn) {
        BlockEntity tileEntityBase = world.getBlockEntity(i, j, k);
        SETTING_TILE = true;

        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i, j, k, currentState.with(REDOUTPUT, bOn));

        SETTING_TILE = false;
        tileEntityBase.cancelRemoval();
        world.setBlockEntity(i, j, k, tileEntityBase);
    }

    public boolean HasFilter(World world, int i, int j, int k) {
        if (world.getBlockId(i, j, k) == BlockListener.hopper.id) {
            return (world.getBlockState(i, j, k).get(FILTER)) > 0;
        } else {
            return false;
        }
    }

    public void SetHasFilter(World world, int i, int j, int k, int bOn) {
        BlockEntity tileEntityBase = world.getBlockEntity(i, j, k);
        SETTING_TILE = true;

        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i, j, k, currentState.with(FILTER, bOn));

        SETTING_TILE = false;
        tileEntityBase.cancelRemoval();
        world.setBlockEntity(i, j, k, tileEntityBase);
    }

    void EmitHopperParticles(World world, int i, int j, int k, Random random) {
        for (int counter = 0; counter < 5; counter++) {
            float smokeX = (float) i + random.nextFloat();
            float smokeY = (float) j + random.nextFloat() * 0.5F + 1.0F;
            float smokeZ = (float) k + random.nextFloat();
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

    }

    public void BreakHopper(World world, int i, int j, int k) {
        for (int iTemp = 0; iTemp < 2; iTemp++) {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, BlockListener.omniSlab.asItem().id, 1);
        }

        for (int iTemp = 0; iTemp < 1; iTemp++) {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, ItemListener.gear.id, 0);
        }

        world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.explode", 1.0F, 1.25F);
        if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
            voicePacket(world, "random.explode", i, j, k, 0.2F, 1.25F);
        }
        world.setBlock(i, j, k, 0);
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int l) {
        return 0;
    }

    public void SetFacing(World world1, int l, int i1, int j1, int k1) {
    }

    public boolean CanRotate(BlockView iBlockAccess, int i, int j, int l) {
        return true;
    }

    public boolean CanTransmitRotation(BlockView iBlockAccess, int i, int j, int l) {
        return false;
    }

    public void Rotate(World world, int i, int j, int k, boolean bReverse) {
        UnsortedUtils.DestroyHorizontallyAttachedAxles(world, i, j, k);
    }

    public boolean CanOutputMechanicalPower() {
        return false;
    }

    public boolean CanInputMechanicalPower() {
        return true;
    }

    public boolean IsInputtingMechanicalPower(World world, int i, int j, int k) {
        for (int iFacing = 2; iFacing <= 5; iFacing++) {
            BlockPosition targetPos = new BlockPosition(i, j, k);
            targetPos.AddFacingAsOffset(iFacing);
            int blockId = world.getBlockId(targetPos.i, targetPos.j, targetPos.k);
            if (blockId == BlockListener.axleBlock.id) {
                AxleBlock axleBlock = (AxleBlock) BlockListener.axleBlock;
                if (axleBlock.IsAxleOrientedTowardsFacing(world, targetPos.i, targetPos.j, targetPos.k, iFacing) && axleBlock.GetPowerLevel(world, targetPos.i, targetPos.j, targetPos.k) > 0) {
                    return true;
                }
                continue;
            }
            if (blockId != BlockListener.handCrank.id) {
                continue;
            }
            Block targetBlock = Block.BLOCKS[blockId];
            MechanicalDevice device = (MechanicalDevice) targetBlock;
            if (device.IsOutputtingMechanicalPower(world, targetPos.i, targetPos.j, targetPos.k)) {
                return true;
            }
        }

        return false;
    }

    public boolean IsOutputtingMechanicalPower(World world, int i, int j, int l) {
        return false;
    }

    private static final int hopperTickRate = 10;
    private static boolean SETTING_TILE = false;

    public boolean renderWorld(BlockRenderManager tileRenderer, BlockView tileView, int x, int y, int z) {
        this.setBoundingBox(0.0F, 0.25F, 0.0F, 0.125F, 1.0F, 0.875F);
        tileRenderer.renderBlock(this, x, y, z);
        this.setBoundingBox(0.0F, 0.25F, 0.875F, 0.875F, 1.0F, 1.0F);
        tileRenderer.renderBlock(this, x, y, z);
        this.setBoundingBox(0.875F, 0.25F, 0.125F, 1.0F, 1.0F, 1.0F);
        tileRenderer.renderBlock(this, x, y, z);
        this.setBoundingBox(0.125F, 0.25F, 0.0F, 1.0F, 1.0F, 0.125F);
        tileRenderer.renderBlock(this, x, y, z);
        this.setBoundingBox(0.125F, 0.25F, 0.125F, 0.875F, 0.375F, 0.875F);
        tileRenderer.renderBlock(this, x, y, z);
        this.setBoundingBox(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.25F, 0.6875F);
        tileRenderer.renderBlock(this, x, y, z);
        HopperBlockEntity fctileentityhopper = (HopperBlockEntity) tileView.getBlockEntity(x, y, z);
        int l;
        int i1;
        Minecraft mc = (Minecraft) FabricLoader.INSTANCE.getGameInstance();
        if (mc.world.isRemote) {
//            System.out.println("RENDER CLIENT");
//            PacketHelper.send(new RenderPacket(2, x, y, z, 0, 0)); //UPDATES AFTER 1 tick when joining server.
            l = fctileentityhopper.clientOccupiedSlots;
            i1 = fctileentityhopper.clientFilterType;
        } else {
            l = InventoryHandler.getOccupiedSlotCountWithinBounds(fctileentityhopper, 0, 17);
            i1 = fctileentityhopper.GetFilterType();
        }
        if (l > 0) {
            float f = (float) l / 18F;
            float f1 = 0.375F;
            float f2 = f1 + 0.0625F + (0.875F - (f1 + 0.0625F)) * f;
            this.setBoundingBox(0.125F, f1, 0.125F, 0.875F, f2, 0.875F);
            CustomBlockRendering.renderStandardBlockWithTexture(tileRenderer, this, x, y, z, TextureListener.filler);
        }
        if (i1 > 0) {
//            boolean flag = FCUtilsRender.GetOverrideBlockTexture(tileRenderer) >= 0;
            int byte0 = 0;
            if (i1 == 1) {
                byte0 = TextureListener.hopper_ladder; //50
            } else if (i1 == 2) {
                byte0 = TextureListener.hopper_trapdoor; //51
            } else if (i1 == 3) {
                byte0 = TextureListener.hopper_grate; //52
            } else if (i1 == 4) {
                byte0 = TextureListener.hopper_wicker;  //54
            } else if (i1 == 5) {
                byte0 = TextureListener.hopper_rollers; //53
            } else if (i1 == 6) {
                byte0 = TextureListener.hopper_soulsand; //55
            }
            this.setBoundingBox(0.125F, 0.875F, 0.125F, 0.875F, 0.9375F, 0.875F);
            CustomBlockRendering.renderStandardBlockWithTexture(tileRenderer, this, x, y, z, byte0);
        }
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }

    @Override
    public void renderInventory(BlockRenderManager tileRenderer, int meta) {
        this.setBoundingBox(0.0F, 0.25F, 0.0F, 0.125F, 1.0F, 0.875F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        this.setBoundingBox(0.0F, 0.25F, 0.875F, 0.875F, 1.0F, 1.0F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        this.setBoundingBox(0.875F, 0.25F, 0.125F, 1.0F, 1.0F, 1.0F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        this.setBoundingBox(0.125F, 0.25F, 0.0F, 1.0F, 1.0F, 0.125F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        this.setBoundingBox(0.125F, 0.25F, 0.125F, 0.875F, 0.375F, 0.875F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        this.setBoundingBox(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.25F, 0.6875F);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void affectBlock(World world, int i, int j, int k, BlockPosition tempTargetPos, int facing) {
        for (int l = 0; l < 2; l++) {
            tempTargetPos.AddFacingAsOffset(facing);
            if (!world.isAir(tempTargetPos.i, tempTargetPos.j, tempTargetPos.k)) return;
        }
        BlockEntity tileEntityHopper = world.getBlockEntity(i, j, k);
        if (tileEntityHopper == null) return;
        if (!(tileEntityHopper instanceof HopperBlockEntity)) return;
        if (((HopperBlockEntity) tileEntityHopper).GetFilterType() != 6) return;
        ((HopperBlockEntity) tileEntityHopper).setStack(18, new ItemStack(ItemListener.soulFilter, 1));
    }

    public static final BooleanProperty POWER = BooleanProperty.of("power");
    public static final BooleanProperty REDOUTPUT = BooleanProperty.of("redoutput");
    public static final IntProperty FULL = IntProperty.of("full", 0, 18);
    public static final IntProperty FILTER = IntProperty.of("filter", 0, 6);

    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(REDOUTPUT);
        builder.add(POWER);
        builder.add(FULL);
        builder.add(FILTER);
    }
}
