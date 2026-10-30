package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.block.entity.PulleyBlockEntity;
import net.kozibrodka.wolves.container.PulleyScreenHandler;
import net.kozibrodka.wolves.events.BlockEntityListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.*;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Random;

public class PulleyBlock extends TemplateBlockWithEntity
        implements MechanicalDevice, RotatableBlock, ManualDevice {

    public PulleyBlock(Identifier iid) {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSoundGroup(WOOD_SOUND_GROUP);
    }

    @Override
    public int getTexture(int iSide) {
        if (iSide == 0) {
            return TextureListener.pulley_bottom;
        }
        return iSide != 1 ? TextureListener.pulley_side : 25; //TextureListener.pulley_top
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity playerEntity) {
        PulleyBlockEntity pulleyBlockEntity = (PulleyBlockEntity) world.getBlockEntity(x, y, z);
        GuiHelper.openGUI(playerEntity, Identifier.of(BlockEntityListener.NAMESPACE, "openPulley"), pulleyBlockEntity, new PulleyScreenHandler(playerEntity.inventory, pulleyBlockEntity));
        return true;
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new PulleyBlockEntity();
    }

    @Override
    public void onPlaced(World world, int i, int j, int k) {
        super.onPlaced(world, i, j, k);
        world.scheduleBlockUpdate(i, j, k, id, getTickRate());
    }

    @Override
    public void onBreak(World world, int i, int j, int k) {
        BlockEntity tileEntity = world.getBlockEntity(i, j, k);
        if (tileEntity != null) {
            InventoryHandler.ejectInventoryContents(world, i, j, k, (Inventory) tileEntity);
        }
        super.onBreak(world, i, j, k);
    }

    @Override
    public void neighborUpdate(World world, int i, int j, int k, int iid) {
        world.scheduleBlockUpdate(i, j, k, id, getTickRate());
    }

    @Override
    public int getTickRate() {
        return iPulleyTickRate;
    }

    @Override
    public void onTick(World world, int i, int j, int k, Random random) {
        boolean bStateChanged = false;
        boolean bRedstoneOn = IsRedstoneOn(world, i, j, k);
        boolean bReceivingRedstone = world.isStrongPowered(i, j, k) || world.isStrongPowered(i, j + 1, k);
        if (bRedstoneOn != bReceivingRedstone) {
            world.playNoteBlockActionAt(i,j,k, 0, 0); ///
            EmitPulleyParticles(world, i, j, k, random);
            SetRedstoneOn(world, i, j, k, bReceivingRedstone);
            bStateChanged = true;
        }
        if (bStateChanged) {
            ((PulleyBlockEntity) world.getBlockEntity(i, j, k)).NotifyPulleyEntityOfBlockStateChange();
        }
    }

    @Override
    public void onBlockAction(World world, int x, int y, int z, int code, int pitch) {
        world.playSound((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.explode", 0.2F, 1.25F);
    }

    @Override
    public int getFacing(BlockView iBlockAccess, int i, int j, int l) {
        return 0;
    }

    @Override
    public void setFacing(World world1, int l, int i1, int j1, int k1) {
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
        UnsortedUtils.DestroyHorizontallyAttachedAxles(world, i, j, k);
    }

    @Override
    public boolean canOutputMechanicalPower() {
        return false;
    }

    @Override
    public boolean canInputMechanicalPower() {
        return true;
    }

    @Override
    public void powerMachine(World world, int x, int y, int z, int side) {
        world.playNoteBlockActionAt(x,y,z, 0, 0); ///
        EmitPulleyParticles(world, x, y, z, new Random());
        SetBlockOn(world, x, y, z, true);
        ((PulleyBlockEntity) world.getBlockEntity(x, y, z)).NotifyPulleyEntityOfBlockStateChange();
    }

    @Override
    public void unpowerMachine(World world, int x, int y, int z, int side) {
        world.playNoteBlockActionAt(x,y,z, 0, 0); ///
        EmitPulleyParticles(world, x, y, z, new Random());
        SetBlockOn(world, x, y, z, false);
        ((PulleyBlockEntity) world.getBlockEntity(x, y, z)).NotifyPulleyEntityOfBlockStateChange();
    }

    @Override
    public boolean isMachinePowered(World world, int x, int y, int z) {
        return IsBlockOn(world, x, y, z);
    }

    @Override
    public boolean canInputMechanicalPower(World world, int x, int y, int z, int side) {
        return side != 0;
    }

    public boolean IsBlockOn(BlockView iBlockAccess, int i, int j, int k) {
        if (iBlockAccess == null) {
            return false;
        }
        return (iBlockAccess.getBlockMeta(i, j, k) & 1) > 0;
    }

    public void SetBlockOn(World world, int i, int j, int k, boolean bOn) {
        int iMetaData = world.getBlockMeta(i, j, k);
        if (bOn) {
            iMetaData |= 1;
        } else {
            iMetaData &= -2;
        }
        world.setBlockMeta(i, j, k, iMetaData);
        world.blockUpdateEvent(i, j, k);
    }

    public boolean IsRedstoneOn(BlockView iBlockAccess, int i, int j, int k) {
        return (iBlockAccess.getBlockMeta(i, j, k) & 2) > 0;
    }

    public void SetRedstoneOn(World world, int i, int j, int k, boolean bOn) {
        int iMetaData = world.getBlockMeta(i, j, k) & -3;
        if (bOn) {
            iMetaData |= 2;
        }
        world.setBlockMeta(i, j, k, iMetaData);
        world.blockUpdateEvent(i, j, k);
    }

    void EmitPulleyParticles(World world, int i, int j, int k, Random random) {
        for (int counter = 0; counter < 5; counter++) {
            float smokeX = (float) i + random.nextFloat();
            float smokeY = (float) j + random.nextFloat() * 0.5F + 1.0F;
            float smokeZ = (float) k + random.nextFloat();
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

    }

    @Override
    public void powerManually(World world, int x, int y, int z) {
        if(!IsBlockOn(world, x, y, z)){
            powerMachine(world, x, y, z, 0);
        }
    }

    @Override
    public void stopPoweringManually(World world, int x, int y, int z) {
        if(!isMechanicallyPoweredByAxle(world, x, y, z)){
            unpowerMachine(world, x, y, z, 0);
        }
    }

    private boolean isMechanicallyPoweredByAxle(World world, int i, int j, int k){
        for (int facing = 2; facing < 6; facing++) {
            BlockPosition axlePos2 = new BlockPosition(i, j, k);
            axlePos2.addFacingAsOffset(facing);
            Block block0 = Block.BLOCKS[world.getBlockId(axlePos2.x, axlePos2.y, axlePos2.z)];
            if(block0 instanceof AxleBlock axleBlock){
                boolean power = axleBlock.getPowerLevel(world, axlePos2.x, axlePos2.y, axlePos2.z) > 0;
                boolean aligned = axleBlock.isAxleOrientedTowardsFacing(world, axlePos2.x, axlePos2.y, axlePos2.z, facing);
                if(power && aligned) return true;
            }
        }
        return false;
    }

    private final int iPulleyTopIndex = 62;
    private final int iPulleySideIndex = 63;
    private final int iPulleyBottomIndex = 64;
    private static final int iPulleyTickRate = 10;
}
