// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCBlockHopper.java

package net.kozibrodka.wolves.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.container.HopperContainer;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.GUIListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.modsupport.AffectedByBellows;
import net.kozibrodka.wolves.network.GuiPacket;
import net.kozibrodka.wolves.network.RenderPacket;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.tileentity.HopperTileEntity;
import net.kozibrodka.wolves.utils.*;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Item;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;

import java.util.List;
import java.util.Random;


public class Hopper extends TemplateBlockWithEntity
    implements MechanicalDevice, RotatableBlock, BlockWithInventoryRenderer, AffectedByBellows
{
    public Hopper(Identifier iid)
    {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSounds(WOOD_SOUNDS);
        setTicksRandomly(true);
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setDefaultState(getDefaultState()
                .with(REDOUTPUT, false)
                .with(POWER, false)
                .with(FULL, 0)
                .with(FILTER, 0)
        );
    }

    public int getTickrate()
    {
        return hopperTickRate;
    }

    public Box getCollisionShape(Level world, int i, int j, int k)
    {
        if(!HasFilter(world, i, j, k))
        {
            return Box.create(i, j, k, (float)(i + 1), (float)j + 0.5F, (float)(k + 1));
        } else
        {
            return Box.create(i, j, k, (float)(i + 1), (float)j + 0.99F, (float)(k + 1));
        }
    }

    public Box getOutlineShape(Level world, int i, int j, int k)
    {
        return Box.create(i, j, k, i + 1, (float)j + 1.0F, k + 1);
    }

    public void onBlockPlaced(Level world, int i, int j, int k)
    {
        super.onBlockPlaced(world, i, j, k);
        world.method_216(i, j, k, id, getTickrate());
    }

    public int getTextureForSide(int iSide)
    {
        if(iSide == 0)
        {
            return TextureListener.hopper_bottom;
        }
        return iSide != 1 ? TextureListener.hopper_side : TextureListener.hopper_top;
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public void onAdjacentBlockUpdate(Level world, int i, int j, int k, int iid)
    {
        boolean bReceivingPower = IsInputtingMechanicalPower(world, i, j, k);
        if(IsBlockOn(world, i, j, k) != bReceivingPower)
        {
            world.method_216(i, j, k, id, getTickrate());
        }
        ((HopperTileEntity)world.getTileEntity(i, j, k)).hopperEjectBlocked = false;
    }

    public boolean canUse(Level world, int i, int j, int k, PlayerBase entityplayer)
    {
        HopperTileEntity tileEntityHopper = (HopperTileEntity)world.getTileEntity(i, j, k);
        GUIListener.TempGuiX = i;
        GUIListener.TempGuiY = j;
        GUIListener.TempGuiZ = k;
        if(world.isServerSide){
            PacketHelper.send(new GuiPacket("hopper",0, i, j, k));
        }
        GuiHelper.openGUI(entityplayer, Identifier.of("wolves:openHopper"), tileEntityHopper, new HopperContainer(entityplayer.inventory, tileEntityHopper));
        return true;
    }

    protected TileEntityBase createTileEntity()
    {
        return new HopperTileEntity();
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
        boolean bReceivingPower = IsInputtingMechanicalPower(world, i, j, k);
        boolean bOn = IsBlockOn(world, i, j, k);
        boolean bFull = IsHopperFull(world, i, j, k);
        boolean bRedstone = IsRedstoneOutputOn(world, i, j, k);
        if(bOn != bReceivingPower)
        {
             world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.2F, 1.25F);
            if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                voicePacket(world, "random.explode", i, j, k, 0.2F, 1.25F);
            }
            EmitHopperParticles(world, i, j, k, random);
            SetBlockOn(world, i, j, k, bReceivingPower);
        }
        if(bFull != bRedstone)
        {
             world.playSound(i, j, k, "random.click", 0.25F, 1.2F);
            if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                voicePacket(world, "random.click", i, j, k, 0.25F, 1.2F);
            }
            SetRedstoneOutputOn(world, i, j, k, bFull);
        }
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(Level world, String name, int x, int y, int z, float g, float h){
        List list2 = world.players;
        if(list2.size() != 0) {
            for(int k = 0; k < list2.size(); k++)
            {
                ServerPlayer player1 = (ServerPlayer) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g,h));
            }
        }
    }

    public void onBlockRemoved(Level world, int i, int j, int k)
    {
        if (!SETTING_TILE) {
        InventoryHandler.ejectInventoryContents(world, i, j, k, (InventoryBase)world.getTileEntity(i, j, k));
        }
        super.onBlockRemoved(world, i, j, k);
    }

    public void onEntityCollision(Level world, int i, int j, int k, EntityBase entity)
    {
        List collisionList = null;
        if(world.isServerSide){
            return;
        }
        boolean bHasFilter = HasFilter(world, i, j, k);
        float fHopperHeight;
        if(!bHasFilter)
        {
            fHopperHeight = 0.5F;
        } else
        {
            fHopperHeight = 0.99F;
        }
        collisionList = world.getEntities(Item.class, Box.createButWasteMemory((float)i, (float)j + fHopperHeight, (float)k, (float)(i + 1), (float)j + fHopperHeight + 0.05F, (float)(k + 1)));
        if(collisionList != null && collisionList.size() > 0)
        {
            HopperTileEntity tileEntityHopper = (HopperTileEntity)world.getTileEntity(i, j, k);
            for(int listIndex = 0; listIndex < collisionList.size(); listIndex++)
            {
                Item targetEntityItem = (Item)collisionList.get(listIndex);
                boolean bSwallowed = false;
                if(!targetEntityItem.removed)
                {
                    ItemBase targetItem = ItemBase.byId[targetEntityItem.item.itemId];
                    if(tileEntityHopper.CanCurrentFilterProcessItem(targetItem))
                    {
                        int iFilterType = tileEntityHopper.GetFilterType();
                        int iTargetItemID = ItemBase.byId[targetEntityItem.item.itemId].id;
                        if(iFilterType == 4 && iTargetItemID == BlockBase.GRAVEL.id)
                        {
                            ItemInstance sandItemInstance = new ItemInstance(BlockBase.SAND.id, targetEntityItem.item.count, 0);
                            int iSandSwallowed = 0;
                            if(InventoryHandler.addItemWithinSlotBounds(tileEntityHopper, sandItemInstance, 0, 17))
                            {
                                iSandSwallowed = targetEntityItem.item.count;
                                targetEntityItem.remove();
                                bSwallowed = true;
                            } else
                            {
                                iSandSwallowed = targetEntityItem.item.count - sandItemInstance.count;
                                targetEntityItem.item.count -= iSandSwallowed;
                            }
                            if(iSandSwallowed > 0)
                            {
                                world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.pop", 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                                if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                                    voicePacket(world, "random.pop", i, j, k, 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                                }
                                ItemInstance flintItemInstance = new ItemInstance(ItemBase.flint.id, iSandSwallowed, 0);
                                Item flintEntityitem = new Item(world, targetEntityItem.x, targetEntityItem.y, targetEntityItem.z, flintItemInstance);
                                flintEntityitem.pickupDelay = 10;
                                world.spawnEntity(flintEntityitem);
                            }
                        } else
                        if(InventoryHandler.addItemWithinSlotBounds(tileEntityHopper, targetEntityItem.item, 0, 17))
                        {
                            world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.pop", 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                            if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                                voicePacket(world, "random.pop", i, j, k, 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                            }
                            targetEntityItem.remove();
                            bSwallowed = true;
                        }
                    }
                }
                if(!bHasFilter || bSwallowed)
                {
                    continue;
                }
                int iBlockAboveID = world.getTileId(i, j + 1, k);
                if(iBlockAboveID != BlockBase.FLOWING_WATER.id && iBlockAboveID != BlockBase.STILL_WATER.id)
                {
                    continue;
                }
                double fHopperFullBoxTop = (double)j + 1.05D;
                if(targetEntityItem.boundingBox.minY < fHopperFullBoxTop)
                {
                    double offset = fHopperFullBoxTop - targetEntityItem.boundingBox.minY;
                    targetEntityItem.setPosition(targetEntityItem.x, targetEntityItem.y + offset, targetEntityItem.z);
                }
            }
//            if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER){
//                renderPacket(world, tileEntityHopper.x, tileEntityHopper.y, tileEntityHopper.z, tileEntityHopper.GetFilterType(), InventoryHandler.getOccupiedSlotCountWithinBounds(tileEntityHopper, 0, 17));
//            }
//            world.method_243(i, j, k);
        }
        //TODO: Interaction with minecarts?
    }

    public boolean isPowered(BlockView iBlockAccess, int i, int j, int k, int l)
    {
        switch (FabricLoader.INSTANCE.getEnvironmentType()){
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
    public boolean powerClient(BlockView iBlockAccess, int i, int j, int k, int l){
        Level level = Minecraft.class.cast(FabricLoader.INSTANCE.getGameInstance()).level;
        return IsRedstoneOutputOn(level, i, j, k);
    }

    @Environment(EnvType.SERVER)
    public boolean powerServer(BlockView iBlockAccess, int i, int j, int k, int l){
        Level level = ((MinecraftServer) net.fabricmc.loader.api.FabricLoader.getInstance().getGameInstance()).getLevel(0);
        return IsRedstoneOutputOn(level, i, j, k);
        //TODO: dimension?
    }

    public boolean indirectlyPowered(Level world, int i, int j, int i1, int j1)
    {
        return false;
    }

    public boolean getEmitsRedstonePower()
    {
        return true;
    }

    public boolean IsBlockOn(Level world, int i, int j, int k)
    {
        if(world.getTileId(i,j,k) == BlockListener.hopper.id) {
            return (world.getBlockState(i, j, k).get(POWER));
        }else{
            return false;
        }
    }

    public void SetBlockOn(Level world, int i, int j, int k, boolean bOn)
    {
        TileEntityBase tileEntityBase = world.getTileEntity(i, j, k);
        SETTING_TILE = true;

        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i,j,k, currentState.with(POWER, bOn));

        SETTING_TILE = false;
        tileEntityBase.validate();
        world.setTileEntity(i, j, k, tileEntityBase);
    }

    public boolean IsHopperFull(Level world, int i, int j, int k)
    {
        if(world.getTileId(i,j,k) == BlockListener.hopper.id) {
            return (world.getBlockState(i, j, k).get(FULL)) == 18;
        }else{
            return false;
        }
    }

    public void SetHopperFull(Level world, int i, int j, int k, int bOn)
    {
        TileEntityBase tileEntityBase = world.getTileEntity(i, j, k);
        SETTING_TILE = true;

        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i,j,k, currentState.with(FULL, bOn));

        SETTING_TILE = false;
        tileEntityBase.validate();
        world.setTileEntity(i, j, k, tileEntityBase);
    }

    public boolean IsRedstoneOutputOn(Level world, int i, int j, int k)
    {
        if(world.getTileId(i,j,k) == BlockListener.hopper.id) {
            return (world.getBlockState(i, j, k).get(REDOUTPUT));
        }else{
            return false;
        }
    }

    public void SetRedstoneOutputOn(Level world, int i, int j, int k, boolean bOn)
    {
        TileEntityBase tileEntityBase = world.getTileEntity(i, j, k);
        SETTING_TILE = true;

        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i,j,k, currentState.with(REDOUTPUT, bOn));

        SETTING_TILE = false;
        tileEntityBase.validate();
        world.setTileEntity(i, j, k, tileEntityBase);
    }

    public boolean HasFilter(Level world, int i, int j, int k)
    {
        if(world.getTileId(i,j,k) == BlockListener.hopper.id) {
            return (world.getBlockState(i, j, k).get(FILTER)) > 0;
        }else{
            return false;
        }
    }

    public void SetHasFilter(Level world, int i, int j, int k, int bOn)
    {
        TileEntityBase tileEntityBase = world.getTileEntity(i, j, k);
        SETTING_TILE = true;

        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i,j,k, currentState.with(FILTER, bOn));

        SETTING_TILE = false;
        tileEntityBase.validate();
        world.setTileEntity(i, j, k, tileEntityBase);
    }

    void EmitHopperParticles(Level world, int i, int j, int k, Random random)
    {
        for(int counter = 0; counter < 5; counter++)
        {
            float smokeX = (float)i + random.nextFloat();
            float smokeY = (float)j + random.nextFloat() * 0.5F + 1.0F;
            float smokeZ = (float)k + random.nextFloat();
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

    }

    public void BreakHopper(Level world, int i, int j, int k)
    {
        for(int iTemp = 0; iTemp < 2; iTemp++)
        {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, BlockListener.omniSlab.id, 1);
        }

        for(int iTemp = 0; iTemp < 1; iTemp++)
        {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, ItemListener.gear.id, 0);
        }

         world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 1.0F, 1.25F);
        if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
        voicePacket(world, "random.explode", i, j, k, 0.2F, 1.25F);
        }
        world.setTile(i, j, k, 0);
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int l)
    {
        return 0;
    }

    public void SetFacing(Level world1, int l, int i1, int j1, int k1)
    {
    }

    public boolean CanRotate(BlockView iBlockAccess, int i, int j, int l)
    {
        return true;
    }

    public boolean CanTransmitRotation(BlockView iBlockAccess, int i, int j, int l)
    {
        return false;
    }

    public void Rotate(Level world, int i, int j, int k, boolean bReverse)
    {
        UnsortedUtils.DestroyHorizontallyAttachedAxles(world, i, j, k);
    }

    public boolean CanOutputMechanicalPower()
    {
        return false;
    }

    public boolean CanInputMechanicalPower()
    {
        return true;
    }

    public boolean IsInputtingMechanicalPower(Level world, int i, int j, int k)
    {
        for(int iFacing = 2; iFacing <= 5; iFacing++)
        {
            BlockPosition targetPos = new BlockPosition(i, j, k);
            targetPos.AddFacingAsOffset(iFacing);
            int iTargetid = world.getTileId(targetPos.i, targetPos.j, targetPos.k);
            if(iTargetid == BlockListener.axleBlock.id)
            {
                Axle axleBlock = (Axle)BlockListener.axleBlock;
                if(axleBlock.IsAxleOrientedTowardsFacing(world, targetPos.i, targetPos.j, targetPos.k, iFacing) && axleBlock.GetPowerLevel(world, targetPos.i, targetPos.j, targetPos.k) > 0)
                {
                    return true;
                }
                continue;
            }
            if(iTargetid != BlockListener.handCrank.id)
            {
                continue;
            }
            BlockBase targetBlock = BlockBase.BY_ID[iTargetid];
            MechanicalDevice device = (MechanicalDevice)targetBlock;
            if(device.IsOutputtingMechanicalPower(world, targetPos.i, targetPos.j, targetPos.k))
            {
                return true;
            }
        }

        return false;
    }

    public boolean IsOutputtingMechanicalPower(Level world, int i, int j, int l)
    {
        return false;
    }

    private static int hopperTickRate = 10;
    private static boolean SETTING_TILE = false;

    public boolean renderWorld(BlockRenderer tileRenderer, BlockView tileView, int x, int y, int z) {
        this.setBoundingBox(0.0F, 0.25F, 0.0F, 0.125F, 1.0F, 0.875F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.0F, 0.25F, 0.875F, 0.875F, 1.0F, 1.0F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.875F, 0.25F, 0.125F, 1.0F, 1.0F, 1.0F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.125F, 0.25F, 0.0F, 1.0F, 1.0F, 0.125F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.125F, 0.25F, 0.125F, 0.875F, 0.375F, 0.875F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        this.setBoundingBox(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.25F, 0.6875F);
        tileRenderer.renderStandardBlock(this, x, y, z);
        HopperTileEntity fctileentityhopper = (HopperTileEntity)tileView.getTileEntity(x, y, z);
        int l;
        int i1;
        Minecraft mc = (Minecraft) FabricLoader.INSTANCE.getGameInstance();
        if(mc.level.isServerSide){
//            System.out.println("RENDER CLIENT");
//            PacketHelper.send(new RenderPacket(2, x, y, z, 0, 0)); //UPDATES AFTER 1 tick when joining server.
            l = fctileentityhopper.clientOccupiedSlots;
            i1 = fctileentityhopper.clientFilterType;
        }else{
            l = InventoryHandler.getOccupiedSlotCountWithinBounds(fctileentityhopper, 0, 17);
            i1 = fctileentityhopper.GetFilterType();
        }
        if(l > 0)
        {
            float f = (float)l / 18F;
            float f1 = 0.375F;
            float f2 = f1 + 0.0625F + (0.875F - (f1 + 0.0625F)) * f;
            this.setBoundingBox(0.125F, f1, 0.125F, 0.875F, f2, 0.875F);
            CustomBlockRendering.RenderStandardBlockWithTexture(tileRenderer, this, x, y, z, TextureListener.filler);
        }
        if(i1 > 0)
        {
//            boolean flag = FCUtilsRender.GetOverrideBlockTexture(tileRenderer) >= 0;
            int byte0 = 0;
            if(i1 == 1)
            {
                byte0 = TextureListener.hopper_ladder; //50
            } else
            if(i1 == 2)
            {
                byte0 = TextureListener.hopper_trapdoor; //51
            } else
            if(i1 == 3)
            {
                byte0 = TextureListener.hopper_grate; //52
            } else
            if(i1 == 4)
            {
                byte0 = TextureListener.hopper_wicker;  //54
            } else
            if(i1 == 5)
            {
                byte0 = TextureListener.hopper_rollers; //53
            } else
            if(i1 == 6)
            {
                byte0 = TextureListener.hopper_soulsand; //55
            }
            this.setBoundingBox(0.125F, 0.875F, 0.125F, 0.875F, 0.9375F, 0.875F);
            CustomBlockRendering.RenderStandardBlockWithTexture(tileRenderer, this, x, y, z, byte0);
        }
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }

    @Override
    public void renderInventory(BlockRenderer tileRenderer, int meta) {
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
    public void affectBlock(Level world, int i, int j, int k, BlockPosition tempTargetPos, int facing) {
        for (int l = 0; l < 2; l++) {
            tempTargetPos.AddFacingAsOffset(facing);
            if (!world.isAir(tempTargetPos.i, tempTargetPos.j, tempTargetPos.k)) return;
        }
        TileEntityBase tileEntityHopper = world.getTileEntity(i, j, k);
        if (tileEntityHopper == null) return;
        if (!(tileEntityHopper instanceof HopperTileEntity)) return;
        if (((HopperTileEntity) tileEntityHopper).GetFilterType() != 6) return;
        ((HopperTileEntity) tileEntityHopper).setInventoryItem(18, new ItemInstance(ItemListener.soulFilter, 1));
    }

    public static final BooleanProperty POWER = BooleanProperty.of("power");
    public static final BooleanProperty REDOUTPUT = BooleanProperty.of("redoutput");
    public static final IntProperty FULL = IntProperty.of("full",0,18);
    public static final IntProperty FILTER = IntProperty.of("filter",0,6);

    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder) {
        builder.add(REDOUTPUT);
        builder.add(POWER);
        builder.add(FULL);
        builder.add(FILTER);
    }
}
