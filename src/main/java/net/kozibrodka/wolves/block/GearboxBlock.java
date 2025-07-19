package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ConfigListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.MechanicalDevice;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Random;

public class GearboxBlock extends TemplateBlock
        implements MechanicalDevice, RotatableBlock {

    public GearboxBlock(Identifier iid) {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSoundGroup(WOOD_SOUND_GROUP);
        setTickRandomly(true);
    }

    public int getTextureId(BlockView iblockaccess, int i, int j, int k, int iSide) {
        int iFacing = GetFacing(iblockaccess, i, j, k);
        if (iSide == iFacing) {
            return TextureListener.gearbox_front;
        }
        BlockPosition sideBlockPos = new BlockPosition(i, j, k);
        sideBlockPos.AddFacingAsOffset(iSide);
        if (iblockaccess.getBlockId(sideBlockPos.i, sideBlockPos.j, sideBlockPos.k) == BlockListener.axleBlock.id && ((AxleBlock) BlockListener.axleBlock).IsAxleOrientedTowardsFacing(iblockaccess, sideBlockPos.i, sideBlockPos.j, sideBlockPos.k, iSide)
                || iblockaccess.getBlockId(sideBlockPos.i, sideBlockPos.j, sideBlockPos.k) == BlockListener.nonCollidingAxleBlock.id && ((AxleBlock) BlockListener.nonCollidingAxleBlock).IsAxleOrientedTowardsFacing(iblockaccess, sideBlockPos.i, sideBlockPos.j, sideBlockPos.k, iSide)) {
            return TextureListener.gearbox_output;
        } else {
            return TextureListener.gearbox_side;
        }
    }

    public int getTexture(int iSide) {
        if (iSide == 3) {
            return TextureListener.gearbox_front;
        } else {
            return TextureListener.gearbox_side;
        }
    }

    public int getTickRate() {
        return 10;
    }

    public void onPlaced(World world, int i, int j, int k, int iFacing) {
        SetFacing(world, i, j, k, UnsortedUtils.getOppositeFacing(iFacing));
    }

    public void onPlaced(World world, int i, int j, int k, LivingEntity entityLiving) //onBlockPlacedBy
    {
        int iFacing = UnsortedUtils.ConvertPlacingEntityOrientationToBlockFacing(entityLiving);
        if (ConfigListener.wolvesGlass.small_tweaks.faceGearBoxAwayFromPlayer) {
            iFacing = UnsortedUtils.getOppositeFacing(iFacing);
        }
        SetFacing(world, i, j, k, iFacing);
    }

    public void onPlaced(World world, int i, int j, int k) {
        super.onPlaced(world, i, j, k);
        world.scheduleBlockUpdate(i, j, k, BlockListener.gearBox.id, getTickRate());
    }

    public void onBreak(World world, int i, int j, int k) {
        if (IsGearBoxOn(world, i, j, k)) {
            SetGearBoxOnState(world, i, j, k, false);
            ValidateOutputs(world, i, j, k, false);
        }
        super.onBreak(world, i, j, k);
    }

    public void neighborUpdate(World world, int i, int j, int k, int iid) {
        world.scheduleBlockUpdate(i, j, k, BlockListener.gearBox.id, getTickRate());
    }

    public void onTick(World world, int i, int j, int k, Random random) {
        boolean bReceivingPower = IsInputtingMechanicalPower(world, i, j, k);
        boolean bOn = IsGearBoxOn(world, i, j, k);
        boolean bIsRedstonePowered = world.canTransferPower(i, j, k) || world.canTransferPower(i, j + 1, k);
        if (bIsRedstonePowered) {
            bReceivingPower = false;
        }
        if (bOn != bReceivingPower) {
            if (bOn) {
                SetGearBoxOnState(world, i, j, k, false);
                ValidateOutputs(world, i, j, k, false);
            } else {
                world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.explode", 0.05F, 1.0F);
                if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                    voicePacket(world, "random.explode", i, j, k, 0.05F, 1.0F);
                }
                EmitGearBoxParticles(world, i, j, k, random);
                SetGearBoxOnState(world, i, j, k, true);
                ValidateOutputs(world, i, j, k, true);
            }
        } else {
            ValidateOutputs(world, i, j, k, false);
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

    public void randomDisplayTick(World world, int i, int j, int k, Random random) {
        if (IsGearBoxOn(world, i, j, k)) {
            EmitGearBoxParticles(world, i, j, k, random);
        }
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int k) {
        return iBlockAccess.getBlockMeta(i, j, k) & 7;
    }

    public void SetFacing(World world, int i, int j, int k, int iFacing) {
        int iMetaData = world.getBlockMeta(i, j, k) & 8;
        iMetaData |= iFacing;
        world.setBlockMeta(i, j, k, iMetaData);
    }

    public boolean CanRotate(BlockView iBlockAccess, int i, int j, int l) {
        return true;
    }

    public boolean CanTransmitRotation(BlockView iBlockAccess, int i, int j, int l) {
        return true;
    }

    public void Rotate(World world, int i, int j, int k, boolean bReverse) {
        System.out.println("METODA ROTATE W GEARBOX");
        int iFacing = GetFacing(world, i, j, k);
        int iNewFacing = UnsortedUtils.RotateFacingAroundJ(iFacing, bReverse);
        if (iNewFacing != iFacing) {
            SetFacing(world, i, j, k, iNewFacing);
            world.setBlocksDirty(i, j, k, i, j, k);
            world.scheduleBlockUpdate(i, j, k, BlockListener.gearBox.id, getTickRate());
            world.blockUpdate(i, j, k, BlockListener.gearBox.id);
        }
        UnsortedUtils.DestroyHorizontallyAttachedAxles(world, i, j, k);
    }

    public boolean IsGearBoxOn(BlockView iBlockAccess, int i, int j, int k) {
        return (iBlockAccess.getBlockMeta(i, j, k) & 8) > 0;
    }

    public void SetGearBoxOnState(World world, int i, int j, int k, boolean bOn) {
        int iMetaData = world.getBlockMeta(i, j, k) & 7;
        if (bOn) {
            iMetaData |= 8;
        }
        world.setBlockMeta(i, j, k, iMetaData);
        world.blockUpdateEvent(i, j, k);
    }

    void EmitGearBoxParticles(World world, int i, int j, int k, Random random) {
        for (int counter = 0; counter < 5; counter++) {
            float smokeX = (float) i + random.nextFloat();
            float smokeY = (float) j + random.nextFloat() * 0.5F + 1.0F;
            float smokeZ = (float) k + random.nextFloat();
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

    }

    private void ValidateOutputs(World world, int i, int j, int k, boolean bDestroyIfAlreadyPowered) {
        int blockFacing = GetFacing(world, i, j, k);
        boolean isOn = IsGearBoxOn(world, i, j, k);
//        if(world.isServerSide){
//            return;
//        }
        for (int facing = 0; facing <= 5; facing++) {
            if (facing == blockFacing) {
                continue;
            }
            BlockPosition tempPos = new BlockPosition(i, j, k);
            tempPos.AddFacingAsOffset(facing);
            if (world.getBlockId(tempPos.i, tempPos.j, tempPos.k) != BlockListener.axleBlock.id && world.getBlockId(tempPos.i, tempPos.j, tempPos.k) != BlockListener.nonCollidingAxleBlock.id) {
                continue;
            }
            AxleBlock axleBlock = (AxleBlock) BlockListener.axleBlock;
            if (world.getBlockId(tempPos.i, tempPos.j, tempPos.k) == BlockListener.nonCollidingAxleBlock.id) {
                axleBlock = (AxleBlock) BlockListener.nonCollidingAxleBlock;
            }
            if (!axleBlock.IsAxleOrientedTowardsFacing(world, tempPos.i, tempPos.j, tempPos.k, facing)) {
                continue;
            }
            int tempPowerLevel = axleBlock.GetPowerLevel(world, tempPos.i, tempPos.j, tempPos.k);
            if (tempPowerLevel > 0 && bDestroyIfAlreadyPowered) {
                axleBlock.BreakAxle(world, tempPos.i, tempPos.j, tempPos.k);
                continue;
            }
            if (isOn) {
                if (tempPowerLevel != 3) {
                    axleBlock.SetPowerLevel(world, tempPos.i, tempPos.j, tempPos.k, 3);
                }
                continue;
            }
            if (tempPowerLevel != 0) {
                axleBlock.SetPowerLevel(world, tempPos.i, tempPos.j, tempPos.k, 0);
            }
//            world.blockUpdateEvent(tempPos.i, tempPos.j, tempPos.k);
        }

    }

    public void Overpower(World world, int i, int j, int k) {
        boolean bIsRedstonePowered = world.canTransferPower(i, j, k) || world.canTransferPower(i, j + 1, k);
        if (!bIsRedstonePowered) {
            BreakGearBox(world, i, j, k);
        }
    }

    public void BreakGearBox(World world, int i, int j, int k) {
        for (int iTemp = 0; iTemp < 4; iTemp++) {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, Block.PLANKS.asItem().id, 0);
        }

        for (int iTemp = 0; iTemp < 3; iTemp++) {
            UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, ItemListener.gear.id, 0);
        }

        UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, Item.REDSTONE.id, 0);
        world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.explode", 0.2F, 1.25F);
        if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
            voicePacket(world, "random.explode", i, j, k, 0.05F, 1.0F);
        }
        world.setBlock(i, j, k, 0);
    }

    public boolean CanOutputMechanicalPower() {
        return true;
    }

    public boolean CanInputMechanicalPower() {
        return true;
    }

    public boolean IsInputtingMechanicalPower(World world, int i, int j, int k) {
        int iFacing = GetFacing(world, i, j, k);
        BlockPosition targetBlockPos = new BlockPosition(i, j, k);
        targetBlockPos.AddFacingAsOffset(iFacing);
        int iTargetid = world.getBlockId(targetBlockPos.i, targetBlockPos.j, targetBlockPos.k);
        return iTargetid == BlockListener.axleBlock.id && ((AxleBlock) BlockListener.axleBlock).IsAxleOrientedTowardsFacing(world, targetBlockPos.i, targetBlockPos.j, targetBlockPos.k, iFacing) && ((AxleBlock) BlockListener.axleBlock).GetPowerLevel(world, targetBlockPos.i, targetBlockPos.j, targetBlockPos.k) > 0
                || iTargetid == BlockListener.nonCollidingAxleBlock.id && ((AxleBlock) BlockListener.nonCollidingAxleBlock).IsAxleOrientedTowardsFacing(world, targetBlockPos.i, targetBlockPos.j, targetBlockPos.k, iFacing) && ((AxleBlock) BlockListener.nonCollidingAxleBlock).GetPowerLevel(world, targetBlockPos.i, targetBlockPos.j, targetBlockPos.k) > 0;
    }

    public boolean IsOutputtingMechanicalPower(World world, int i, int j, int k) {
        return IsGearBoxOn(world, i, j, k);
    }

}
