
package net.kozibrodka.wolves.blocks;

import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.utils.FCBlockPos;
import net.kozibrodka.wolves.utils.FCIBlock;
import net.kozibrodka.wolves.utils.FCMechanicalDevice;
import net.kozibrodka.wolves.utils.FCUtilsMisc;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Item;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.util.Random;


public class FCBlockGearBox extends TemplateBlockBase
    implements FCMechanicalDevice, FCIBlock
{

    public FCBlockGearBox(Identifier iid)
    {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSounds(WOOD_SOUNDS);
        texture = 38;
        setDefaultState(getDefaultState()
                .with(iFace0, false)
                .with(iFace1, false)
                .with(iFace2, false)
                .with(iFace3, false)
                .with(iFace4, false)
                .with(iFace5, false)
        );
    }

    public int getTickrate()
    {
        return 10;
    }

    public void onBlockPlaced(Level world, int i, int j, int k, int iFacing)
    {
        SetFacing(world, i, j, k, FCUtilsMisc.GetOppositeFacing(iFacing));
    }

    public void afterPlaced(Level world, int i, int j, int k, Living entityLiving) //onBlockPlacedBy
    {
        int iFacing = FCUtilsMisc.ConvertPlacingEntityOrientationToBlockFacing(entityLiving);
        if(mod_FCBetterThanWolves.fcFaceGearBoxAwayFromPlayer)
        {
            iFacing = FCUtilsMisc.GetOppositeFacing(iFacing);
        }
        SetFacing(world, i, j, k, iFacing);
    }

    public void onBlockAdded(Level world, int i, int j, int k)
    {
        super.onBlockPlaced(world, i, j, k);
        world.method_216(i, j, k, id, getTickrate());
    }

    public void onBlockRemoved(Level world, int i, int j, int k)
    {
        if(IsGearBoxOn(world, i, j, k))
        {
            SetGearBoxOnState(world, i, j, k, false);
            ValidateOutputs(world, i, j, k, false);
        }
        super.onBlockRemoved(world, i, j, k);
    }

    public int getBlockTexture(BlockView iblockaccess, int i, int j, int k, int iSide)
    {
        int iFacing = GetFacing(iblockaccess, i, j, k);
        if(iSide == iFacing)
        {
            return iGearBoxFrontTextureIndex;
        }
        FCBlockPos sideBlockPos = new FCBlockPos(i, j, k);
        sideBlockPos.AddFacingAsOffset(iSide);
        if(iblockaccess.getTileId(sideBlockPos.i, sideBlockPos.j, sideBlockPos.k) == mod_FCBetterThanWolves.fcAxleBlock.id && ((FCBlockAxle)mod_FCBetterThanWolves.fcAxleBlock).IsAxleOrientedTowardsFacing(iblockaccess, sideBlockPos.i, sideBlockPos.j, sideBlockPos.k, iSide))
        {
            return iGearBoxOutputTextureIndex;
        } else
        {
            return texture;
        }
    }

    //DEBUG
    public boolean canUse(Level world, int i, int j, int k, PlayerBase entityplayer)
    {
        for(int iSide = 0; iSide < 6; iSide++) {
            int iFacing = GetFacing(world, i, j, k);
            if (iSide == iFacing) {
//                return iGearBoxFrontTextureIndex;
                System.out.println("iSide: " + iSide + " texture: FRONT");
            } else {
                FCBlockPos sideBlockPos = new FCBlockPos(i, j, k);
                sideBlockPos.AddFacingAsOffset(iSide);
                if (world.getTileId(sideBlockPos.i, sideBlockPos.j, sideBlockPos.k) == mod_FCBetterThanWolves.fcAxleBlock.id && ((FCBlockAxle) mod_FCBetterThanWolves.fcAxleBlock).IsAxleOrientedTowardsFacing(world, sideBlockPos.i, sideBlockPos.j, sideBlockPos.k, iSide)) {
                    System.out.println("iSide: " + iSide + " texture: OUTPUT");
                    BlockState currentState = world.getBlockState(i, j, k);
                    switch (iSide) {
                        case 0:
                            world.setBlockStateWithNotify(i, j, k, currentState.with(iFace0, true));
                            break;
                        case 1:
//                            System.out.println("TEST KURWA 1");
                            world.setBlockStateWithNotify(i, j, k, currentState.with(iFace1, true));
                            break;
                        case 2:
                            world.setBlockStateWithNotify(i, j, k, currentState.with(iFace2, true));
                            break;
                        case 3:
                            world.setBlockStateWithNotify(i, j, k, currentState.with(iFace3, true));
                            break;
                        case 4:
                            world.setBlockStateWithNotify(i, j, k, currentState.with(iFace4, true));
                            break;
                        case 5:
//                            System.out.println("TEST KURWA 5");
                            world.setBlockStateWithNotify(i, j, k, currentState.with(iFace5, true));
                            break;
                        default:
                            break;
                    }
//                return iGearBoxOutputTextureIndex;
                } else {
                    BlockState currentState = world.getBlockState(i, j, k);
                    switch (iSide) {
                        case 0:
                            world.setBlockStateWithNotify(i, j, k, currentState.with(iFace0, false));
                            break;
                        case 1:
                            world.setBlockStateWithNotify(i, j, k, currentState.with(iFace1, false));
                            break;
                        case 2:
                            world.setBlockStateWithNotify(i, j, k, currentState.with(iFace2, false));
                            break;
                        case 3:
                            world.setBlockStateWithNotify(i, j, k, currentState.with(iFace3, false));
                            break;
                        case 4:
                            world.setBlockStateWithNotify(i, j, k, currentState.with(iFace4, false));
                            break;
                        case 5:
                            world.setBlockStateWithNotify(i, j, k, currentState.with(iFace5, false));
                            break;
                        default:
                            break;
                    }
                    System.out.println("iSide: " + iSide + " texture: PLANKS");
//                return texture;
                }
            }
        }
        return true; //zeby bylo
    }
    //DEBUG

    public int getTextureForSide(int iSide)
    {
        if(iSide == 3)
        {
            return iGearBoxFrontTextureIndex;
        } else
        {
            return texture;
        }
    }

    public void onAdjacentBlockUpdate(Level world, int i, int j, int k, int iid)
    {
        world.method_216(i, j, k, id, getTickrate());
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
        boolean bReceivingPower = IsInputtingMechanicalPower(world, i, j, k);
        boolean bOn = IsGearBoxOn(world, i, j, k);
        boolean bIsRedstonePowered = world.method_263(i, j, k) || world.method_263(i, j + 1, k);
        if(bIsRedstonePowered)
        {
            bReceivingPower = false;
        }
        if(bOn != bReceivingPower)
        {
            if(bOn)
            {
                SetGearBoxOnState(world, i, j, k, false);
                ValidateOutputs(world, i, j, k, false);
            } else
            {
                world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.05F, 1.0F);
                EmitGearBoxParticles(world, i, j, k, random);
                SetGearBoxOnState(world, i, j, k, true);
                ValidateOutputs(world, i, j, k, true);
            }
        } else
        {
            ValidateOutputs(world, i, j, k, false);
        }
    }

    public void randomDisplayTick(Level world, int i, int j, int k, Random random)
    {
        if(IsGearBoxOn(world, i, j, k))
        {
            EmitGearBoxParticles(world, i, j, k, random);
        }
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int k)
    {
        /**
         * logic change
         */
//        return iBlockAccess.getTileMeta(i, j, k) & 7;
        Level level = Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).level;
        if(level.getTileId(i,j,k) == mod_FCBetterThanWolves.fcGearBox.id) {
            return level.getBlockState(i, j, k).get(FACING);
        }else{
            return 0;
        }
    }

    public void SetFacing(Level world, int i, int j, int k, int iFacing)
    {
        /**
         * logic change
         */
//        int iMetaData = world.getTileMeta(i, j, k) & 8;
//        iMetaData |= iFacing;
//        world.setTileMeta(i, j, k, iMetaData);
        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i,j,k, currentState.with(FACING, iFacing));
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
        System.out.println("METODA ROTATE W GEARBOX");
        int iFacing = GetFacing(world, i, j, k);
        int iNewFacing = FCUtilsMisc.RotateFacingAroundJ(iFacing, bReverse);
        if(iNewFacing != iFacing)
        {
            SetFacing(world, i, j, k, iNewFacing);
            world.method_202(i, j, k, i, j, k);
            world.method_216(i, j, k, id, getTickrate());
//            ((LevelAccessor) world).invokeMethod_235(i, j, k, id);
        }
        FCUtilsMisc.DestroyHorizontallyAttachedAxles(world, i, j, k);
    }

    public boolean IsGearBoxOn(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getTileMeta(i, j, k) & 8) > 0;
    }

    public void SetGearBoxOnState(Level world, int i, int j, int k, boolean bOn)
    {
        int iMetaData = world.getTileMeta(i, j, k) & 7;
        if(bOn)
        {
            iMetaData |= 8;
        }
        world.setTileMeta(i, j, k, iMetaData);
        world.method_243(i, j, k);
    }

    void EmitGearBoxParticles(Level world, int i, int j, int k, Random random)
    {
        for(int counter = 0; counter < 5; counter++)
        {
            float smokeX = (float)i + random.nextFloat();
            float smokeY = (float)j + random.nextFloat() * 0.5F + 1.0F;
            float smokeZ = (float)k + random.nextFloat();
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

    }

    private void ValidateOutputs(Level world, int i, int j, int k, boolean bDestroyIfAlreadyPowered)
    {
        int iBlockFacing = GetFacing(world, i, j, k);
        boolean bIsOn = IsGearBoxOn(world, i, j, k);
        for(int iFacing = 0; iFacing <= 5; iFacing++)
        {
            if(iFacing == iBlockFacing)
            {
                continue;
            }
            FCBlockPos tempPos = new FCBlockPos(i, j, k);
            tempPos.AddFacingAsOffset(iFacing);
            if(world.getTileId(tempPos.i, tempPos.j, tempPos.k) != mod_FCBetterThanWolves.fcAxleBlock.id)
            {
                continue;
            }
            FCBlockAxle axleBlock = (FCBlockAxle)mod_FCBetterThanWolves.fcAxleBlock;
            if(!axleBlock.IsAxleOrientedTowardsFacing(world, tempPos.i, tempPos.j, tempPos.k, iFacing))
            {
                continue;
            }
            int tempPowerLevel = axleBlock.GetPowerLevel(world, tempPos.i, tempPos.j, tempPos.k);
            if(tempPowerLevel > 0 && bDestroyIfAlreadyPowered)
            {
                axleBlock.BreakAxle(world, tempPos.i, tempPos.j, tempPos.k);
                continue;
            }
            if(bIsOn)
            {
                if(tempPowerLevel != 3)
                {
                    axleBlock.SetPowerLevel(world, tempPos.i, tempPos.j, tempPos.k, 3);
                }
                continue;
            }
            if(tempPowerLevel != 0)
            {
                axleBlock.SetPowerLevel(world, tempPos.i, tempPos.j, tempPos.k, 0);
            }
        }

    }

    public void Overpower(Level world, int i, int j, int k)
    {
        boolean bIsRedstonePowered = world.method_263(i, j, k) || world.method_263(i, j + 1, k);
        if(!bIsRedstonePowered)
        {
            BreakGearBox(world, i, j, k);
        }
    }

    public void BreakGearBox(Level world, int i, int j, int k)
    {
        for(int iTemp = 0; iTemp < 4; iTemp++)
        {
            FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, BlockBase.WOOD.id, 0);
        }

        for(int iTemp = 0; iTemp < 3; iTemp++)
        {
            FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, mod_FCBetterThanWolves.fcGear.id, 0);
        }

        FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, ItemBase.redstoneDust.id, 0);
        world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.2F, 1.25F);
        world.setTile(i, j, k, 0);
    }

    public boolean CanOutputMechanicalPower()
    {
        return true;
    }

    public boolean CanInputMechanicalPower()
    {
        return true;
    }

    public boolean IsInputtingMechanicalPower(Level world, int i, int j, int k)
    {
        int iFacing = GetFacing(world, i, j, k);
        FCBlockPos targetBlockPos = new FCBlockPos(i, j, k);
        targetBlockPos.AddFacingAsOffset(iFacing);
        int iTargetid = world.getTileId(targetBlockPos.i, targetBlockPos.j, targetBlockPos.k);
        return iTargetid == mod_FCBetterThanWolves.fcAxleBlock.id && ((FCBlockAxle)mod_FCBetterThanWolves.fcAxleBlock).IsAxleOrientedTowardsFacing(world, targetBlockPos.i, targetBlockPos.j, targetBlockPos.k, iFacing) && ((FCBlockAxle)mod_FCBetterThanWolves.fcAxleBlock).GetPowerLevel(world, targetBlockPos.i, targetBlockPos.j, targetBlockPos.k) > 0;
    }

    public boolean IsOutputtingMechanicalPower(Level world, int i, int j, int k)
    {
        return IsGearBoxOn(world, i, j, k);
    }

    public static final int iGearBoxTickRate = 10;
    private final int iGearBoxFrontTextureIndex = 36;
    private final int iGearBoxOutputTextureIndex = 37;

    /**
     * STATES
     */
    public static final IntProperty FACING = IntProperty.of("facing", 0, 5);
    public static final BooleanProperty iFace0 = BooleanProperty.of("iface0");
    public static final BooleanProperty iFace1 = BooleanProperty.of("iface1");
    public static final BooleanProperty iFace2 = BooleanProperty.of("iface2");
    public static final BooleanProperty iFace3 = BooleanProperty.of("iface3");
    public static final BooleanProperty iFace4 = BooleanProperty.of("iface4");
    public static final BooleanProperty iFace5 = BooleanProperty.of("iface5");

    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder){
        builder.add(FACING);
        builder.add(iFace0);
        builder.add(iFace1);
        builder.add(iFace2);
        builder.add(iFace3);
        builder.add(iFace4);
        builder.add(iFace5);
    }

}
