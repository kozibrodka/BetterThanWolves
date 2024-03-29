package net.kozibrodka.wolves.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.GuiPacket;
import net.kozibrodka.wolves.network.RenderPacket;
import net.kozibrodka.wolves.tileentity.TurntableTileEntity;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.MechanicalDevice;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;

import java.util.Random;

public class Turntable extends TemplateBlockWithEntity
    implements MechanicalDevice, RotatableBlock
{

    public Turntable(Identifier iid)
    {
        super(iid, Material.STONE);
        setHardness(2.0F);
        setSounds(STONE_SOUNDS);
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

    public int getTickrate()
    {
        return iTurntableTickRate;
    }

    protected TileEntityBase createTileEntity()
    {
        return new TurntableTileEntity();
    }

    public void onBlockPlaced(Level world, int i, int j, int k)
    {
        super.onBlockPlaced(world, i, j, k);
        world.method_216(i, j, k, BlockListener.turntable.id, getTickrate());
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
        if(world == null) {
            return true;
        }
        ItemInstance playerEquippedItem = entityPlayer.getHeldItem();
        if(playerEquippedItem == null)
        {
            BlockState currentState = world.getBlockState(i, j, k);
            int iClick = currentState.get(CLICK);
            if(++iClick > 3)
            {
                iClick = 0;
            }
            world.setBlockStateWithNotify(i,j,k,currentState.with(CLICK, iClick));
            canUseTile(world,i,j,k,iClick);
            return true;
        } else
        {
            return false;
        }
    }

    public boolean canUseTile(Level world, int i, int j, int k, int click)
    {
        TurntableTileEntity tileEntityTurntable = (TurntableTileEntity)world.getTileEntity(i, j, k);
        tileEntityTurntable.m_iSwitchSetting = click;
        world.method_202(i, j, k, i, j, k);
        world.method_243(i, j, k);
        return true;
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

    public boolean IsBlockMechanicalOn(Level world, int i, int j, int k)
    {
        if(world.getTileId(i,j,k) == BlockListener.turntable.id) {
            return (world.getBlockState(i, j, k).get(POWER));
        }else{
            return false;
        }
    }

    public void SetBlockMechanicalOn(Level world, int i, int j, int k, boolean bOn)
    {
        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i,j,k,currentState.with(POWER, bOn));
    }

    public boolean IsBlockRedstoneOn(Level world, int i, int j, int k)
    {
        if(world.getTileId(i,j,k) == BlockListener.turntable.id) {
            return (world.getBlockState(i, j, k).get(REDSTONE));
        }else{
            return false;
        }
    }

    public void SetBlockRedstoneOn(Level world, int i, int j, int k, boolean bOn)
    {
        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i,j,k,currentState.with(REDSTONE, bOn));
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
        BlockPosition targetPos = new BlockPosition(i, j, k);
        targetPos.AddFacingAsOffset(0);
        int iTargetid = world.getTileId(targetPos.i, targetPos.j, targetPos.k);
        if(iTargetid == BlockListener.axleBlock.id)
        {
            Axle axleBlock = (Axle)BlockListener.axleBlock;
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
    private static boolean SETTING_TILE = false;

//    @Override
//    public boolean renderWorld(BlockRenderer tileRenderer, BlockView tileView, int x, int y, int z) {
//        tileRenderer.renderStandardBlock(this, x, y, z);
//        TurntableTileEntity fctileentityturntable = (TurntableTileEntity)tileView.getTileEntity(x, y, z);
//        int l = fctileentityturntable.m_iSwitchSetting;
//        float f = 0.25F + (float)l * 0.125F;
//        this.setBoundingBox(f, 0.3125F, 0.0625F, f + 0.125F, 0.4375F, 1.0625F);
//        CustomBlockRendering.RenderStandardBlockWithTexture(tileRenderer, this, x, y, z, TextureListener.turntable_button);
//        this.setBoundingBox(1.0F - (f + 0.125F), 0.3125F, -0.0625F, 1.0F - f, 0.4375F, 0.9375F);
//        CustomBlockRendering.RenderStandardBlockWithTexture(tileRenderer, this, x, y, z, TextureListener.turntable_button);
//        this.setBoundingBox(0.0625F, 0.3125F, 1.0F - (f + 0.125F), 1.0625F, 0.4375F, 1.0F - f);
//        CustomBlockRendering.RenderStandardBlockWithTexture(tileRenderer, this, x, y, z, TextureListener.turntable_button);
//        this.setBoundingBox(-0.0625F, 0.3125F, f, 0.9375F, 0.4375F, f + 0.125F);
//        CustomBlockRendering.RenderStandardBlockWithTexture(tileRenderer, this, x, y, z, TextureListener.turntable_button);
//        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
//        return true;
//    }

    public static final IntProperty CLICK = IntProperty.of("click", 0, 3);
    public static final BooleanProperty POWER = BooleanProperty.of("power");
    public static final BooleanProperty REDSTONE = BooleanProperty.of("redstone");
//
    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder){
        builder.add(CLICK);
        builder.add(REDSTONE);
        builder.add(POWER);
    }
}
