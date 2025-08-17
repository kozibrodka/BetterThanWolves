package net.kozibrodka.wolves.block;

import net.kozibrodka.wolves.block.entity.TurntableBlockEntity;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.MechanicalDevice;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class TurntableBlock extends TemplateBlockWithEntity
        implements MechanicalDevice, RotatableBlock {

    public TurntableBlock(Identifier iid) {
        super(iid, Material.STONE);
        setHardness(2.0F);
        setSoundGroup(DEFAULT_SOUND_GROUP);
        setDefaultState(getDefaultState()
                .with(REDSTONE, false)
                .with(POWER, false)
                .with(CLICK, 0)
        );
    }

//    public int getTextureForSide(int iSide)
//    {
//        if(iSide == 0)
//        {
//            return TextureListener.turntable_bottom;
//        }
//        return iSide != 1 ? TextureListener.turntable_side : TextureListener.turntable_top;
//    }

    public int getTickRate() {
        return iTurntableTickRate;
    }

    protected BlockEntity createBlockEntity() {
        return new TurntableBlockEntity();
    }

    public void onPlaced(World world, int i, int j, int k) {
        super.onPlaced(world, i, j, k);
        world.scheduleBlockUpdate(i, j, k, BlockListener.turntable.id, getTickRate());
    }

    public void neighborUpdate(World world, int i, int j, int k, int iid) {
        world.scheduleBlockUpdate(i, j, k, id, getTickRate());
    }

    public void onTick(World world, int i, int j, int k, Random random) {
        boolean bReceivingMechanicalPower = IsInputtingMechanicalPower(world, i, j, k);
        boolean bMechanicalOn = IsBlockMechanicalOn(world, i, j, k);
        if (bMechanicalOn != bReceivingMechanicalPower) {
            EmitTurntableParticles(world, i, j, k, random);
            SetBlockMechanicalOn(world, i, j, k, bReceivingMechanicalPower);
        }
        boolean bReceivingRedstonePower = world.canTransferPower(i, j, k);
        boolean bRedstoneOn = IsBlockRedstoneOn(world, i, j, k);
        if (bRedstoneOn != bReceivingRedstonePower) {
            SetBlockRedstoneOn(world, i, j, k, bReceivingRedstonePower);
        }
    }


    public void randomDisplayTick(World world, int i, int j, int k, Random random) {
        if (IsBlockMechanicalOn(world, i, j, k)) {
            EmitTurntableParticles(world, i, j, k, random);
        }
    }

    public boolean onUse(World world, int i, int j, int k, PlayerEntity entityPlayer) {
        if (world == null) {
            return true;
        }
        ItemStack playerEquippedItem = entityPlayer.getHand();
        if (playerEquippedItem == null) {
            BlockState currentState = world.getBlockState(i, j, k);
            int iClick = currentState.get(CLICK);
            if (++iClick > 3) {
                iClick = 0;
            }
            world.setBlockStateWithNotify(i, j, k, currentState.with(CLICK, iClick));
            canUseTile(world, i, j, k, iClick);
            return true;
        } else {
            return false;
        }
    }

    public void canUseTile(World world, int i, int j, int k, int click) {
        TurntableBlockEntity tileEntityTurntable = (TurntableBlockEntity) world.getBlockEntity(i, j, k);
        tileEntityTurntable.switchSetting = click;
        world.setBlocksDirty(i, j, k, i, j, k);
        world.blockUpdateEvent(i, j, k);
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int l) {
        return 0;
    }

    public void SetFacing(World world1, int l, int i1, int j1, int k1) {
    }

    public boolean CanRotate(BlockView iBlockAccess, int i, int j, int l) {
        return false;
    }

    public boolean CanTransmitRotation(BlockView iBlockAccess, int i, int j, int l) {
        return false;
    }

    public void Rotate(World world1, int l, int i1, int j1, boolean flag) {
    }

    public boolean IsBlockMechanicalOn(World world, int i, int j, int k) {
        if (world.getBlockId(i, j, k) == BlockListener.turntable.id) {
            return (world.getBlockState(i, j, k).get(POWER));
        } else {
            return false;
        }
    }

    public void SetBlockMechanicalOn(World world, int i, int j, int k, boolean bOn) {
        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i, j, k, currentState.with(POWER, bOn));
    }

    public boolean IsBlockRedstoneOn(World world, int i, int j, int k) {
        if (world.getBlockId(i, j, k) == BlockListener.turntable.id) {
            return (world.getBlockState(i, j, k).get(REDSTONE));
        } else {
            return false;
        }
    }

    public void SetBlockRedstoneOn(World world, int i, int j, int k, boolean bOn) {
        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i, j, k, currentState.with(REDSTONE, bOn));
    }

    void EmitTurntableParticles(World world, int i, int j, int k, Random random) {
        for (int counter = 0; counter < 5; counter++) {
            float smokeX = (float) i + random.nextFloat();
            float smokeY = (float) j + random.nextFloat() * 0.5F + 1.0F;
            float smokeZ = (float) k + random.nextFloat();
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

    }

    public boolean CanOutputMechanicalPower() {
        return false;
    }

    public boolean CanInputMechanicalPower() {
        return true;
    }

    public boolean IsInputtingMechanicalPower(World world, int i, int j, int k) {
        BlockPosition targetPos = new BlockPosition(i, j, k);
        targetPos.AddFacingAsOffset(0);
        int blockId = world.getBlockId(targetPos.i, targetPos.j, targetPos.k);
        if (blockId == BlockListener.axleBlock.id) {
            AxleBlock axleBlock = (AxleBlock) BlockListener.axleBlock;
            return axleBlock.IsAxleOrientedTowardsFacing(world, targetPos.i, targetPos.j, targetPos.k, 0) && axleBlock.GetPowerLevel(world, targetPos.i, targetPos.j, targetPos.k) > 0;
        }
        return false;
    }

    public boolean IsOutputtingMechanicalPower(World world, int i, int j, int l) {
        return false;
    }
    
    private static final int iTurntableTickRate = 10;

    public static final IntProperty CLICK = IntProperty.of("click", 0, 3);
    public static final BooleanProperty POWER = BooleanProperty.of("power");
    public static final BooleanProperty REDSTONE = BooleanProperty.of("redstone");

    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CLICK);
        builder.add(REDSTONE);
        builder.add(POWER);
    }
}
