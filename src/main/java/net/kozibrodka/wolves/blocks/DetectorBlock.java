package net.kozibrodka.wolves.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.ParticleBase;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Random;

public class DetectorBlock extends TemplateBlock {
    public DetectorBlock(Identifier identifier, Material material) {
        super(identifier, material);
        setTicksRandomly(true);
        setHardness(1.5F);
    }

    @Override
    public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int id) {
        if (isObstructed(level, x, y, z)) activateDetector(level, x, y, z);
        else level.method_216(x, y, z, this.id, getTickrate());
    }

    @Override
    public int getTextureForSide(BlockView blockView, int x, int y, int z, int l) {
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
    public int getTextureForSide(int i)
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
    public void onScheduledTick(Level level, int x, int y, int z, Random random) {
        if (isObstructed(level, x, y, z)) return;
        if (getFacing(level, x, y, z) == 1) {
            if (isDetectingRain(level, x, y + 1, z)) {
                activateDetector(level, x, y, z);
                return;
            }
        }
        BlockPosition tempPos = new BlockPosition(x, y, z);
        tempPos.AddFacingAsOffset(getFacing(level, x, y, z));
        List list = level.getEntities(EntityBase.class, Box.createButWasteMemory(tempPos.i, tempPos.j, tempPos.k, 1 + tempPos.i, 1 + tempPos.j, 1 + tempPos.k));
        if (list == null) {
            deactivateDetector(level, x, y, z);
            return;
        }
        if (list.size() == 0) {
            deactivateDetector(level, x, y, z);
            return;
        }
        for(int listIndex = 0; listIndex < list.size(); listIndex++) {
            EntityBase targetEntity = (EntityBase)list.get(listIndex);
            if(targetEntity == null) continue;
            activateDetector(level, x, y, z);
            return;
        }
    }

    private boolean isDetectingRain(Level level, int x, int y, int z) {
        if (!level.isRaining()) return false;
        if (!level.isAboveGroundCached(x, y, z)) return false;
        if (level.method_228(x, z) > y) return false;
        Biome biomeGenBase = level.getBiomeSource().getBiome(x, z);
        if(biomeGenBase.canSnow()) return true;
        return biomeGenBase.canRain();
    }

    private void activateDetector(Level level, int x, int y, int z) {
        if (isBlockOn(level, x, y, z)) {
            level.method_216(x, y, z, BlockListener.detectorBlock.id, getTickrate());
            return;
        }
        level.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.75F, 2.0F);
        if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
            voicePacket(level, "random.click", x, y, z, 0.75F, 2.0F);
        }
        int metadata = level.getTileMeta(x, y, z);
        metadata++;
        level.placeBlockWithMetaData(x, y, z, this.id, metadata);
        level.updateAdjacentBlocks(x, y, z, BlockListener.detectorBlock.id);
        level.method_216(x, y, z, BlockListener.detectorBlock.id, getTickrate());
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

    private void deactivateDetector(Level level, int x, int y, int z) {
        if (!isBlockOn(level, x, y, z)) {
            level.method_216(x, y, z, BlockListener.detectorBlock.id, getTickrate());
            return;
        }
        int metadata = level.getTileMeta(x, y, z);
        metadata--;
        level.placeBlockWithMetaData(x, y, z, BlockListener.detectorBlock.id, metadata);
        level.updateAdjacentBlocks(x, y, z, BlockListener.detectorBlock.id);
        level.method_216(x, y, z, BlockListener.detectorBlock.id, getTickrate());
    }

    private boolean isObstructed(Level level, int x, int y, int z) {
        BlockPosition tempPos = new BlockPosition(x, y, z);
        tempPos.AddFacingAsOffset(getFacing(level, x, y, z));
        return level.getTileId(tempPos.i, tempPos.j, tempPos.k) != 0;
    }

    @Override
    public int getTickrate() {
        return 5;
    }

    @Override
    public void onBlockPlaced(Level level, int x, int y, int z) {
        super.onBlockPlaced(level, x, y, z);
        level.method_216(x, y, z, BlockListener.detectorBlock.id, getTickrate());
    }

    @Override
    public void onBlockPlaced(Level level, int x, int y, int z, int facing) {
        setFacing(level, x, y, z, UnsortedUtils.GetOppositeFacing(facing));
    }

    @Override
    public void afterPlaced(Level level, int x, int y, int z, Living entityLiving) {
        setFacing(level, x, y, z, UnsortedUtils.ConvertPlacingEntityOrientationToBlockFacing(entityLiving));
    }

    @Override
    public boolean isPowered(BlockView blockView, int x, int y, int z, int iHaveNoIdeaWhatThisDoes) {
        return isBlockOn(blockView, x, y, z);
    }

    @Override
    public boolean indirectlyPowered(Level world, int i, int j, int i1, int j1)
    {
        return false;
    }

    @Override
    public boolean getEmitsRedstonePower()
    {
        return true;
    }

    public int getFacing(BlockView blockView, int i, int j, int k) {
        return (blockView.getTileMeta(i, j, k) & -2) >> 1;
    }

    public int getFacing(Level level, int i, int j, int k) {
        return (level.getTileMeta(i, j, k) & -2) >> 1;
    }

    public void setFacing(Level level, int i, int j, int k, int iFacing) {
        int iMetaData = level.getTileMeta(i, j, k);
        iMetaData = iMetaData & 1 | iFacing << 1;
        level.setTileMeta(i, j, k, iMetaData);
    }

    public boolean isBlockOn(BlockView blockView, int i, int j, int k) {
        return (blockView.getTileMeta(i, j, k) & 1) > 0;
    }

    public boolean isBlockOn(Level level, int i, int j, int k) {
        return (level.getTileMeta(i, j, k) & 1) > 0;
    }
}
