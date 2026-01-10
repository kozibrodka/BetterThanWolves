package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.api.AffectedByBellows;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.MechanicalDevice;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Random;

public class BellowsBlock extends TemplateBlock
        implements MechanicalDevice, RotatableBlock {

    public BellowsBlock(Identifier iid) {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSoundGroup(WOOD_SOUND_GROUP);
    }

    public int getTextureId(BlockView blockAccess, int i, int j, int k, int iSide) {
        int iFacing = getFacing(blockAccess, i, j, k);
        if (iSide == iFacing) {
            return TextureListener.bellows_front;
        }
        if (iSide == 1) {
            return TextureListener.bellows_top;
        }
        return iSide != 0 ? TextureListener.bellows_side : TextureListener.bellows_top;
    }

    public int getTexture(int iSide) {
        if (iSide == 3) {
            return TextureListener.bellows_front;
        }
        if (iSide == 1) {
            return TextureListener.bellows_top;
        }
        return iSide != 0 ? TextureListener.bellows_side : TextureListener.bellows_top;
    }

    public void setupRenderBoundingBox() {
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.6875F, 1.0F);
    }

    public int getTickRate() {
        return m_iBellowsTickRate;
    }

    public void onPlaced(World world, int i, int j, int k, int iFacing) {
        if (iFacing < 2) {
            iFacing = 2;
        }
        setFacing(world, i, j, k, iFacing);
    }

    public void onPlaced(World world, int i, int j, int k, LivingEntity entityLiving) {
        int iFacing = UnsortedUtils.ConvertPlacingEntityOrientationToFlatBlockFacing(entityLiving);
        setFacing(world, i, j, k, iFacing);
    }

    public void onPlaced(World world, int i, int j, int k) {
        super.onPlaced(world, i, j, k);
        world.scheduleBlockUpdate(i, j, k, BlockListener.bellows.id, getTickRate());
    }

    public boolean isOpaque() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public Box getCollisionShape(World world, int i, int j, int k) {
        if (IsBlockMechanicalOn(world, i, j, k)) {
            return Box.createCached((float) i, (float) j, (float) k, (float) i + 1.0F, (float) j + 0.6875F, (float) k + 1.0F);
        } else {
            return Box.createCached((float) i, (float) j, (float) k, (float) i + 1.0F, (float) j + 1.0F, (float) k + 1.0F);
        }
    }

    public void updateBoundingBox(BlockView iBlockAccess, int i, int j, int k) {
        if (IsBlockMechanicalOn(iBlockAccess, i, j, k)) {
            setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.6875F, 1.0F);
        } else {
            setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
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

    public int getFacing(BlockView iBlockAccess, int i, int j, int k) {
        return (iBlockAccess.getBlockMeta(i, j, k) & 3) + 2;
    }

    public void setFacing(World world, int i, int j, int k, int iFacing) {
        int iMetaData = world.getBlockMeta(i, j, k) & -4;
        if (iFacing >= 2) {
            iFacing -= 2;
        } else {
            iFacing = 0;
        }
        iMetaData |= iFacing;
        world.setBlockMeta(i, j, k, iMetaData);
    }


    public boolean canRotate(BlockView iBlockAccess, int i, int j, int l) {
        return true;
    }

    public boolean canTransmitRotation(BlockView iBlockAccess, int i, int j, int l) {
        return false;
    }

    public void rotate(World world, int i, int j, int k, boolean bReverse) {
        int iFacing = getFacing(world, i, j, k);
        int iNewFacing = UnsortedUtils.RotateFacingAroundJ(iFacing, bReverse);
        if (iNewFacing != iFacing) {
            setFacing(world, i, j, k, iNewFacing);
            world.setBlocksDirty(i, j, k, i, j, k);
            world.scheduleBlockUpdate(i, j, k, BlockListener.bellows.id, getTickRate());
            world.blockUpdate(i, j, k, BlockListener.bellows.id);
        }
        UnsortedUtils.DestroyHorizontallyAttachedAxles(world, i, j, k);
    }

    public boolean canOutputMechanicalPower() {
        return false;
    }

    public boolean canInputMechanicalPower() {
        return true;
    }

    @Override
    public void powerMachine(World world, int x, int y, int z) {
        blow(world, x, y, z);
        world.playSound((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "fire.ignite", 0.5F, world.random.nextFloat() * 0.4F + 2.0F);
        if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
            voicePacket(world, "fire.ignite", x, y, z, 0.5F, world.random.nextFloat() * 0.4F + 2.0F);
        }
        SetBlockMechanicalOn(world, x, y, z, true);
        world.setBlocksDirty(x, y, z, x, y, z);
    }

    @Override
    public void unpowerMachine(World world, int x, int y, int z) {
        LiftCollidingEntities(world, x, y, z);
        world.playSound((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "fire.ignite", 0.5F, world.random.nextFloat() * 0.4F + 2.0F);
        if (FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
            voicePacket(world, "fire.ignite", x, y, z, 0.5F, world.random.nextFloat() * 0.4F + 2.0F);
        }
        SetBlockMechanicalOn(world, x, y, z, false);
        world.setBlocksDirty(x, y, z, x, y, z);
    }

    @Override
    public boolean isMachinePowered(World world, int x, int y, int z) {
        return IsBlockMechanicalOn(world, x, y, z);
    }

    @Override
    public boolean canInputMechanicalPower(World world, int x, int y, int z, int side) {
        return side != 1 && side != getFacing(world, x, y, z);
    }

    public boolean IsBlockMechanicalOn(BlockView iBlockAccess, int i, int j, int k) {
        return (iBlockAccess.getBlockMeta(i, j, k) & 4) > 0;
    }

    public void SetBlockMechanicalOn(World world, int i, int j, int k, boolean bOn) {
        int iMetaData = world.getBlockMeta(i, j, k) & -5;
        if (bOn) {
            iMetaData |= 4;
        }
        world.setBlockMeta(i, j, k, iMetaData);
        world.blockUpdateEvent(i, j, k);
    }

    private void EmitBellowsParticles(World world, int i, int j, int k, Random random) {
        for (int counter = 0; counter < 10; counter++) {
            float smokeX = (float) i + random.nextFloat();
            float smokeY = (float) j + random.nextFloat() * 0.5F;
            float smokeZ = (float) k + random.nextFloat();
            world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
        }

    }

    private void blow(World world, int i, int j, int k) {
        int iFacing = getFacing(world, i, j, k);
        int iFacingSide1 = UnsortedUtils.RotateFacingAroundJ(iFacing, false);
        int iFacingSide2 = UnsortedUtils.RotateFacingAroundJ(iFacing, true);
        BlockPosition particlePos = new BlockPosition(i, j, k);
        particlePos.addFacingAsOffset(iFacing);
        EmitBellowsParticles(world, particlePos.x, particlePos.y, particlePos.z, world.random);
        BlockPosition tempTargetPos = new BlockPosition(i, j, k);
        Block blockWithInterface;
        for (int iTempCount = 0; iTempCount < 3; iTempCount++) {
            tempTargetPos.addFacingAsOffset(iFacing);
            int tempid = world.getBlockId(tempTargetPos.x, tempTargetPos.y, tempTargetPos.z);
            blockWithInterface = Block.BLOCKS[tempid];
            if (blockWithInterface != null) {
                if (blockWithInterface instanceof AffectedByBellows) {
                    ((AffectedByBellows) blockWithInterface).affectBlock(world, tempTargetPos.x, tempTargetPos.y, tempTargetPos.z, tempTargetPos, iFacing);
                    continue;
                }
            }
            if (tempid == Block.FIRE.id || tempid == BlockListener.stokedFire.id) {
                StokeFire(world, tempTargetPos.x, tempTargetPos.y, tempTargetPos.z);
            } else if (!world.isAir(tempTargetPos.x, tempTargetPos.y, tempTargetPos.z)) {
                break;
            }
            BlockPosition tempSidePos1 = new BlockPosition(tempTargetPos.x, tempTargetPos.y, tempTargetPos.z);
            tempSidePos1.addFacingAsOffset(iFacingSide1);
            tempid = world.getBlockId(tempSidePos1.x, tempSidePos1.y, tempSidePos1.z);
            if ((tempid == Block.FIRE.id || tempid == BlockListener.stokedFire.id)) // && world.getTileId(tempSidePos1.x, tempSidePos1.y, tempSidePos1.z) == BlockBase.FIRE.id
            {
                StokeFire(world, tempSidePos1.x, tempSidePos1.y, tempSidePos1.z);
            }
            BlockPosition tempSidePos2 = new BlockPosition(tempTargetPos.x, tempTargetPos.y, tempTargetPos.z);
            tempSidePos2.addFacingAsOffset(iFacingSide2);
            tempid = world.getBlockId(tempSidePos2.x, tempSidePos2.y, tempSidePos2.z);
            if (tempid == Block.FIRE.id || tempid == BlockListener.stokedFire.id) {
                StokeFire(world, tempSidePos2.x, tempSidePos2.y, tempSidePos2.z);
            }
        }

    }

    private void StokeFire(World world, int i, int j, int k) {
        if (world.getBlockId(i, j - 1, k) == BlockListener.hibachi.id) {
            if (world.getBlockId(i, j, k) == BlockListener.stokedFire.id) {
                world.setBlockMeta(i, j, k, 0);
                world.blockUpdateEvent(i, j, k);
            } else {
                world.setBlock(i, j, k, BlockListener.stokedFire.id);
                world.blockUpdateEvent(i, j, k);
            }
            if (world.isAir(i, j + 1, k)) {
                world.setBlock(i, j + 1, k, BlockListener.stokedFire.id);
                world.blockUpdateEvent(i, j, k);
            }
            if (world.getBlockId(i, j + 1, k) == BlockListener.stokedFire.id) {
                world.setBlockMeta(i, j + 1, k, 0);
                world.blockUpdateEvent(i, j, k);
            }
        } else {
            world.setBlock(i, j, k, 0);
            world.blockUpdateEvent(i, j, k);
        }
    }

    private void LiftCollidingEntities(World world, int i, int j, int k) {
        List list = world.getEntities(null, Box.createCached((float) i, (float) j + 0.6875F, (float) k, (float) (i + 1), (float) (j + 1), (float) (k + 1)));
        float extendedMaxY = j + 1;
        if (list != null && list.size() > 0) {
            for (int j1 = 0; j1 < list.size(); j1++) {
                Entity tempEntity = (Entity) list.get(j1);
                if (tempEntity.dead || !tempEntity.isPushable() && !(tempEntity instanceof ItemEntity)) {
                    continue;
                }
                double tempEntityMinY = tempEntity.boundingBox.minY;
                if (tempEntityMinY < (double) extendedMaxY) {
                    double entityYOffset = (double) extendedMaxY - tempEntityMinY;
                    tempEntity.setPos(tempEntity.x, tempEntity.y + entityYOffset, tempEntity.z);
                }
            }

        }
    }

    private static final int m_iBellowsTickRate = 10;
}
