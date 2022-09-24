package net.kozibrodka.wolves.blocks;

import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.tileentity.FCTileEntityTurntable;
import net.kozibrodka.wolves.utils.FCBlockPos;
import net.kozibrodka.wolves.utils.FCIBlock;
import net.kozibrodka.wolves.utils.FCMechanicalDevice;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;

import java.util.Random;

public class FCBlockTurntable extends TemplateBlockWithEntity
    implements FCMechanicalDevice, FCIBlock
{

    public FCBlockTurntable(Identifier iid)
    {
        super(iid, Material.STONE);
        setHardness(2.0F);
        setSounds(STONE_SOUNDS);
        texture = 0;
        setDefaultState(getDefaultState()
                .with(REDSTONE, false)
                .with(POWER, false)
                .with(CLICK, 0)
        );
    }

    public int getTickrate()
    {
        return 10;
    }

    protected TileEntityBase createTileEntity()
    {
        return new FCTileEntityTurntable();
    }

    public void onBlockPlaced(Level world, int i, int j, int k)
    {
        super.onBlockPlaced(world, i, j, k);
        world.method_216(i, j, k, id, getTickrate());
    }

    public void onAdjacentBlockUpdate(Level world, int i, int j, int k, int iid)
    {
        world.method_216(i, j, k, id, getTickrate());
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
        boolean bReceivingMechanicalPower = IsInputtingMechanicalPower(world, i, j, k);
        boolean bMechanicalOn = IsBlockMechanicalOn(world, i, j, k);
        if(bMechanicalOn != bReceivingMechanicalPower)
        {
            EmitTurntableParticles(world, i, j, k, random);
            SetBlockMechanicalOn(world, i, j, k, bReceivingMechanicalPower);
        }
        boolean bReceivingRedstonePower = world.method_263(i, j, k);
        boolean bRedstoneOn = IsBlockRedstoneOn(world, i, j, k);
        if(bRedstoneOn != bReceivingRedstonePower)
        {
            SetBlockRedstoneOn(world, i, j, k, bReceivingRedstonePower);
        }
    }

    public void randomDisplayTick(Level world, int i, int j, int k, Random random)
    {
        if(IsBlockMechanicalOn(world, i, j, k))
        {
            EmitTurntableParticles(world, i, j, k, random);
        }
    }

    public boolean canUse(Level world, int i, int j, int k, PlayerBase entityPlayer)
    {
        if(world.isServerSide)
        {
            return true;
        }
        ItemInstance playerEquippedItem = entityPlayer.getHeldItem();
        if(playerEquippedItem == null)
        {
            FCTileEntityTurntable tileEntityTurntable = (FCTileEntityTurntable)world.getTileEntity(i, j, k);
            int iSwitchSetting = tileEntityTurntable.m_iSwitchSetting;
            if(++iSwitchSetting > 3)
            {
                iSwitchSetting = 0;
            }
            System.out.println(tileEntityTurntable.m_iSwitchSetting + " RAZ");
            tileEntityTurntable.m_iSwitchSetting = iSwitchSetting;
            System.out.println(tileEntityTurntable.m_iSwitchSetting + " DWA");

            clickState(world,i,j,k,iSwitchSetting);

            world.method_202(i, j, k, i, j, k);
            return true;
        } else
        {
            return false;
        }
    }

    public void clickState(Level world,  int i, int j, int k, int click){
        BlockState currentState = world.getBlockState(i, j, k);
        //TODO maybe use only state and remove m_switchsetting
        world.setBlockStateWithNotify(i,j,k,currentState.with(CLICK, click));
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
        return false;
    }

    public boolean CanTransmitRotation(BlockView iBlockAccess, int i, int j, int l)
    {
        return false;
    }

    public void Rotate(Level world1, int l, int i1, int j1, boolean flag)
    {
    }

    public boolean IsBlockMechanicalOn(BlockView iBlockAccess, int i, int j, int k)
    {
        Level level = Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).level;
        if(level.getTileId(i,j,k) == mod_FCBetterThanWolves.fcTurntable.id) {
            return (level.getBlockState(i, j, k).get(POWER));
        }else{
            return false;
        }
//        return (iBlockAccess.getTileMeta(i, j, k) & 1) > 0;
    }

    public void SetBlockMechanicalOn(Level world, int i, int j, int k, boolean bOn)
    {
        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i,j,k,currentState.with(POWER, bOn));
//        int iMetaData = world.getTileMeta(i, j, k) & -2;
//        if(bOn)
//        {
//            iMetaData |= 1;
//        }
//        world.setTileMeta(i, j, k, iMetaData);
//        world.method_243(i, j, k);
    }

    public boolean IsBlockRedstoneOn(BlockView iBlockAccess, int i, int j, int k)
    {
        Level level = Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).level;
        if(level.getTileId(i,j,k) == mod_FCBetterThanWolves.fcTurntable.id) {
            return (level.getBlockState(i, j, k).get(REDSTONE));
        }else{
            return false;
        }
//        return (iBlockAccess.getTileMeta(i, j, k) & 2) > 0;
    }

    public void SetBlockRedstoneOn(Level world, int i, int j, int k, boolean bOn)
    {
        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i,j,k,currentState.with(REDSTONE, bOn));
//        int iMetaData = world.getTileMeta(i, j, k) & -3;
//        if(bOn)
//        {
//            iMetaData |= 2;
//        }
//        world.setTileMeta(i, j, k, iMetaData);
//        world.method_243(i, j, k);
    }

    void EmitTurntableParticles(Level world, int i, int j, int k, Random random)
    {
        for(int counter = 0; counter < 5; counter++)
        {
            float smokeX = (float)i + random.nextFloat();
            float smokeY = (float)j + random.nextFloat() * 0.5F + 1.0F;
            float smokeZ = (float)k + random.nextFloat();
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

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
        FCBlockPos targetPos = new FCBlockPos(i, j, k);
        targetPos.AddFacingAsOffset(0);
        int iTargetid = world.getTileId(targetPos.i, targetPos.j, targetPos.k);
        if(iTargetid == mod_FCBetterThanWolves.fcAxleBlock.id)
        {
            FCBlockAxle axleBlock = (FCBlockAxle)mod_FCBetterThanWolves.fcAxleBlock;
            if(axleBlock.IsAxleOrientedTowardsFacing(world, targetPos.i, targetPos.j, targetPos.k, 0) && axleBlock.GetPowerLevel(world, targetPos.i, targetPos.j, targetPos.k) > 0)
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

    private final int iTurntableTopTextureIndex = 65;
    private final int iTurntableSideTextureIndex = 66;
    private final int iTurntableBottomTextureIndex = 67;
    private final int iTurntableSwitchTextureIndex = 1;
    private static final int iTurntableTickRate = 10;

    /**
     * STATES
     */
    public static final IntProperty CLICK = IntProperty.of("click", 0, 3);
    public static final BooleanProperty POWER = BooleanProperty.of("power");
    public static final BooleanProperty REDSTONE = BooleanProperty.of("redstone");

    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder){
        builder.add(CLICK);
        builder.add(REDSTONE);
        builder.add(POWER);
    }
}
