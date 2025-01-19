package net.kozibrodka.wolves.block;

import net.kozibrodka.wolves.block.entity.LoomBlockEntity;
import net.kozibrodka.wolves.container.LoomScreenHandler;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ScreenHandlerListener;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.InventoryHandler;
import net.kozibrodka.wolves.utils.MechanicalDevice;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class LoomBlock extends LazyBlockWithEntityTemplate implements MechanicalDevice {

    public static final IntProperty TYPES = IntProperty.of("types", 0, 1);

    public LoomBlock(Identifier identifier, Material material, float hardness, BlockSoundGroup blockSounds) {
        super(identifier, material, hardness, blockSounds);
        setTickRandomly(true);
    }

    public int getTickRate() {
        return 10;
    }

    public void onPlaced(World world, int i, int j, int k) {
        super.onPlaced(world, i, j, k);
        world.scheduleBlockUpdate(i, j, k, BlockListener.millStone.id, getTickRate());
    }

    public void neighborUpdate(World world, int i, int j, int k, int iid) {
        boolean bReceivingPower = IsInputtingMechanicalPower(world, i, j, k);
        if(isBlockOn(world, i, j, k) != bReceivingPower) {
            world.scheduleBlockUpdate(i, j, k, BlockListener.millStone.id, getTickRate());
        }
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(TYPES);
    }

    public void onTick(World world, int i, int j, int k, Random random) {
        boolean receivingPower = IsInputtingMechanicalPower(world, i, j, k);
        boolean isOn = isBlockOn(world, i, j, k);
        if(isOn != receivingPower) {
            if(isOn) {
                setBlockOn(world, i, j, k, false);
            } else {
                world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.2F, 1.25F);
                emitLoomParticles(world, i, j, k, random);
                setBlockOn(world, i, j, k, true);
            }
        }
    }

    public boolean onUse(World world, int i, int j, int k, PlayerEntity playerEntity) {
        LoomBlockEntity loomBlockEntity = (LoomBlockEntity)world.getBlockEntity(i, j, k);
        ScreenHandlerListener.TempGuiX = i;
        ScreenHandlerListener.TempGuiY = j;
        ScreenHandlerListener.TempGuiZ = k;
        //if(world.isRemote){
        //    PacketHelper.send(new ScreenPacket("mill",0, i, j, k));
        //}
        GuiHelper.openGUI(playerEntity, Identifier.of("wolves:openLoom"), loomBlockEntity, new LoomScreenHandler(playerEntity.inventory, (LoomBlockEntity) loomBlockEntity));
        return true;
    }

    public void randomDisplayTick(World world, int i, int j, int k, Random random) {
        if(isBlockOn(world, i, j, k)) {
            emitLoomParticles(world, i, j, k, random);
            if(random.nextInt(5) == 0) {
                world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.1F + random.nextFloat() * 0.1F, 1.25F + random.nextFloat() * 0.1F);
            }
        }
    }

    public boolean isBlockOn(World world, int i, int j, int k) {
        return world.getBlockMeta(i, j, k) > 0;
    }

    public void setBlockOn(World world, int i, int j, int k, boolean isOn) {
        if(isOn) {
            world.setBlockMeta(i, j, k, 1);
            world.blockUpdateEvent(i, j, k);
        } else {
            world.setBlockMeta(i, j, k, 0);
            world.blockUpdateEvent(i, j, k);
        }
    }

    public void emitLoomParticles(World world, int i, int j, int k, Random random) {
        for(int counter = 0; counter < 5; counter++) {
            float smokeX = (float)i + random.nextFloat();
            float smokeY = (float)j + random.nextFloat() * 0.5F + 1.0F;
            float smokeZ = (float)k + random.nextFloat();
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }
    }

    public void onBreak(World world, int i, int j, int k) {
        InventoryHandler.ejectInventoryContents(world, i, j, k, (Inventory)world.getBlockEntity(i, j, k));
        super.onBreak(world, i, j, k);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new LoomBlockEntity();
    }

    @Override
    public boolean CanOutputMechanicalPower() {
        return false;
    }

    @Override
    public boolean CanInputMechanicalPower() {
        return true;
    }

    @Override
    public boolean IsInputtingMechanicalPower(World world, int i, int j, int k) {
        BlockPosition targetPos = new BlockPosition(i, j, k);
        targetPos.AddFacingAsOffset(0);
        int targetId = world.getBlockId(targetPos.i, targetPos.j, targetPos.k);
        if(targetId != BlockListener.axleBlock.id) {
            return false;
        }
        AxleBlock axleBlock = (AxleBlock)BlockListener.axleBlock;
        return axleBlock.IsAxleOrientedTowardsFacing(world, targetPos.i, targetPos.j, targetPos.k, 0) && axleBlock.GetPowerLevel(world, targetPos.i, targetPos.j, targetPos.k) > 0;
    }

    @Override
    public boolean IsOutputtingMechanicalPower(World world, int i, int j, int k) {
        return false;
    }
}
