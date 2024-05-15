package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Random;

public class DetectorBlock extends TemplateBlock {
    public DetectorBlock(Identifier identifier, Material material) {
        super(identifier, material);
        setTickRandomly(true);
        setHardness(1.5F);
    }

    @Override
    public void neighborUpdate(World level, int x, int y, int z, int id) {
        if (isObstructed(level, x, y, z)) activateDetector(level, x, y, z);
        else level.method_216(x, y, z, this.id, getTickRate());
    }

    @Override
    public int getTextureId(BlockView blockView, int x, int y, int z, int l) {
        int facing = getFacing(blockView, x, y, z);
        if(l == facing) {
            if(isBlockOn(blockView, x, y, z)) {
                return TextureListener.detector_on;
            } else {
                return TextureListener.detector_off;
            }
        } else if(l == 1) {
            return TextureListener.detector_top;
        } else if(l == 0) {
            return TextureListener.detector_bottom;
        } else {
            return TextureListener.detector_side;
        }
    }

    @Override
    public int getTexture(int i)
    {
        if(i == 3) {
            return TextureListener.detector_off;
        } else if(i == 1) {
            return TextureListener.detector_top;
        } else if(i == 0) {
            return TextureListener.detector_bottom;
        } else {
            return TextureListener.detector_side;
        }
    }

    @Override
    public void onTick(World level, int x, int y, int z, Random random) {
        if (isObstructed(level, x, y, z)) return;
        if (getFacing(level, x, y, z) == 1) {
            if (isDetectingRain(level, x, y + 1, z)) {
                activateDetector(level, x, y, z);
                return;
            }
        }
        BlockPosition tempPos = new BlockPosition(x, y, z);
        tempPos.AddFacingAsOffset(getFacing(level, x, y, z));
        List list = level.method_175(Entity.class, Box.createCached(tempPos.i, tempPos.j, tempPos.k, 1 + tempPos.i, 1 + tempPos.j, 1 + tempPos.k));
        if (list == null) {
            deactivateDetector(level, x, y, z);
            return;
        }
        if (list.size() == 0) {
            deactivateDetector(level, x, y, z);
            return;
        }
        for(int listIndex = 0; listIndex < list.size(); listIndex++) {
            Entity targetEntity = (Entity)list.get(listIndex);
            if(targetEntity == null) continue;
            activateDetector(level, x, y, z);
            return;
        }
    }

    private boolean isDetectingRain(World level, int x, int y, int z) {
        if (!level.method_270()) return false;
        if (!level.method_249(x, y, z)) return false;
        if (level.method_228(x, z) > y) return false;
        Biome biomeGenBase = level.method_1781().method_1787(x, z);
        if(biomeGenBase.canSnow()) return true;
        return biomeGenBase.canRain();
    }

    private void activateDetector(World level, int x, int y, int z) {
        if (isBlockOn(level, x, y, z)) {
            level.method_216(x, y, z, BlockListener.detectorBlock.id, getTickRate());
            return;
        }
        level.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.75F, 2.0F);
        if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
            voicePacket(level, "random.click", x, y, z, 0.75F, 2.0F);
        }
        int metadata = level.getBlockMeta(x, y, z);
        metadata++;
        level.method_201(x, y, z, this.id, metadata);
        level.method_244(x, y, z, BlockListener.detectorBlock.id);
        level.method_216(x, y, z, BlockListener.detectorBlock.id, getTickRate());
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(World world, String name, int x, int y, int z, float g, float h){
        List list2 = world.field_200;
        if(list2.size() != 0) {
            for(int k = 0; k < list2.size(); k++)
            {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g,h));
            }
        }
    }

    private void deactivateDetector(World level, int x, int y, int z) {
        if (!isBlockOn(level, x, y, z)) {
            level.method_216(x, y, z, BlockListener.detectorBlock.id, getTickRate());
            return;
        }
        int metadata = level.getBlockMeta(x, y, z);
        metadata--;
        level.method_201(x, y, z, BlockListener.detectorBlock.id, metadata);
        level.method_244(x, y, z, BlockListener.detectorBlock.id);
        level.method_216(x, y, z, BlockListener.detectorBlock.id, getTickRate());
    }

    private boolean isObstructed(World level, int x, int y, int z) {
        BlockPosition tempPos = new BlockPosition(x, y, z);
        tempPos.AddFacingAsOffset(getFacing(level, x, y, z));
        return level.getBlockId(tempPos.i, tempPos.j, tempPos.k) != 0;
    }

    @Override
    public int getTickRate() {
        return 5;
    }

    @Override
    public void onPlaced(World level, int x, int y, int z) {
        super.onPlaced(level, x, y, z);
        level.method_216(x, y, z, BlockListener.detectorBlock.id, getTickRate());
    }

    @Override
    public void onPlaced(World level, int x, int y, int z, int facing) {
        setFacing(level, x, y, z, UnsortedUtils.GetOppositeFacing(facing));
    }

    @Override
    public void onPlaced(World level, int x, int y, int z, LivingEntity entityLiving) {
        setFacing(level, x, y, z, UnsortedUtils.ConvertPlacingEntityOrientationToBlockFacing(entityLiving));
    }

    @Override
    public boolean isEmittingRedstonePower(BlockView blockView, int x, int y, int z, int iHaveNoIdeaWhatThisDoes) {
        return isBlockOn(blockView, x, y, z);
    }

    @Override
    public boolean canTransferPowerInDirection(World world, int i, int j, int i1, int j1)
    {
        return false;
    }

    @Override
    public boolean canEmitRedstonePower()
    {
        return true;
    }

    public int getFacing(BlockView blockView, int i, int j, int k) {
        return (blockView.getBlockMeta(i, j, k) & -2) >> 1;
    }

    public int getFacing(World level, int i, int j, int k) {
        return (level.getBlockMeta(i, j, k) & -2) >> 1;
    }

    public void setFacing(World level, int i, int j, int k, int iFacing) {
        int iMetaData = level.getBlockMeta(i, j, k);
        iMetaData = iMetaData & 1 | iFacing << 1;
        level.method_215(i, j, k, iMetaData);
    }

    public boolean isBlockOn(BlockView blockView, int i, int j, int k) {
        return (blockView.getBlockMeta(i, j, k) & 1) > 0;
    }

    public boolean isBlockOn(World level, int i, int j, int k) {
        return (level.getBlockMeta(i, j, k) & 1) > 0;
    }
}
