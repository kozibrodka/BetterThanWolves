package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.api.HibachiIgnitionRegistry;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.minecraft.block.Block;
import net.minecraft.block.LiquidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;
import java.util.Random;


public class HibachiBlock extends TemplateBlock {
    public HibachiBlock(Identifier iid) {
        super(iid, Material.STONE);
        setHardness(3.5F);
        setSoundGroup(DEFAULT_SOUND_GROUP);
        setTickRandomly(true);
    }

    @Override
    public int getTexture(int side) {
        if (side == 1) {
            return TextureListener.hibachi_top;
        }
        return side != 0 ? TextureListener.hibachi_side : TextureListener.hibachi_bottom;
    }

    @Override
    public int getTickRate() {
        return 4;
    }

    @Override
    public void onPlaced(World world, int i, int j, int k) {
        world.scheduleBlockUpdate(i, j, k, id, getTickRate());
    }

    @Override
    public void onTick(World world, int i, int j, int k, Random random) {
        boolean bPowered = world.isStrongPowered(i, j, k) || world.isStrongPowered(i, j + 1, k);
        if (bPowered) {
            if (!IsBBQLit(world, i, j, k)) {
                BBQIgnite(world, i, j, k);
            } else {
                int iBlockAboveID = world.getBlockId(i, j + 1, k);
                if (iBlockAboveID != Block.FIRE.id && iBlockAboveID != BlockListener.stokedFire.id && BBQShouldIgniteAbove(world, i, j, k)) {
                    world.playNoteBlockActionAt(i,j,k, 0, 0); /// Fire
                    world.setBlock(i, j + 1, k, FIRE.id);
                }
            }
        } else if (IsBBQLit(world, i, j, k)) {
            BBQExtinguish(world, i, j, k);
        } else {
            int iBlockAboveID = world.getBlockId(i, j + 1, k);
            if (iBlockAboveID == Block.FIRE.id || iBlockAboveID == BlockListener.stokedFire.id) {
                world.setBlock(i, j + 1, k, 0);
            }
        }
    }

    @Override
    public void onBlockAction(World world, int x, int y, int z, int code, int pitch) {
        if(pitch == 0){ /// Fire
            world.playSound((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "fire.ignite", 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
        }else if(pitch == 1){ /// Fizz
            world.playSound((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, "random.fizz", 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
        }
    }

    @Override
    public void neighborUpdate(World world, int i, int j, int k, int blockID) {
        boolean bShouldUpdate = false;
        if (blockID != FIRE.id) {
            boolean bPowered = world.isStrongPowered(i, j, k) || world.isStrongPowered(i, j + 1, k);
            if (IsBBQLit(world, i, j, k) != bPowered) {
                bShouldUpdate = true;
            } else if (IsBBQLit(world, i, j, k)) {
                int iBlockAboveID = world.getBlockId(i, j + 1, k);
                if (iBlockAboveID != Block.FIRE.id && iBlockAboveID != BlockListener.stokedFire.id && BBQShouldIgniteAbove(world, i, j, k)) {
                    bShouldUpdate = true;
                }
            } else if(!IsBBQLit(world, i, j, k) && !bPowered){
                int iBlockAboveID = world.getBlockId(i, j + 1, k);
                if (iBlockAboveID == Block.FIRE.id || iBlockAboveID == BlockListener.stokedFire.id) {
                    bShouldUpdate = true;
                }
            }
        }
        if (bShouldUpdate) {
            world.scheduleBlockUpdate(i, j, k, id, getTickRate());
        }
    }

    public boolean IsBBQLit(World world, int i, int j, int k) {
        int iMetaData = world.getBlockMeta(i, j, k);
        return (iMetaData & 4) > 0;
    }

    private void SetBBQLitFlag(World world, int i, int j, int k) {
        int iMetaData = world.getBlockMeta(i, j, k);
        world.setBlockMeta(i, j, k, iMetaData | 4);
    }

    private void ClearBBQLitFlag(World world, int i, int j, int k) {
        int iMetaData = world.getBlockMeta(i, j, k);
        world.setBlockMeta(i, j, k, iMetaData & -5);
    }

    private boolean BBQShouldIgniteAbove(World world, int i, int j, int k) {
        boolean shouldIgnite = false;
        int iTargetid = world.getBlockId(i, j + 1, k);
        Block targetBlock = Block.BLOCKS[iTargetid];
        if (!world.method_1783(i, j + 1, k)) {
            if (targetBlock != null) {
                if (!(targetBlock instanceof LiquidBlock) && !(targetBlock instanceof CementBlock) && (world.getMaterial(i, j + 1, k) == Material.WOOD || world.getMaterial(i, j + 1, k) == Material.WOOL || world.getMaterial(i, j + 1, k) == Material.SNOW_LAYER || world.getMaterial(i, j + 1, k) == Material.PLANT || world.getMaterial(i, j + 1, k) == Material.PISTON_BREAKABLE || world.getMaterial(i, j + 1, k) == Material.CACTUS || world.getMaterial(i, j + 1, k) == Material.SOLID_ORGANIC || world.getMaterial(i, j + 1, k) == Material.PUMPKIN || world.getMaterial(i, j + 1, k) == Material.AIR)) {
                    shouldIgnite = true;
                }
            } else {
                shouldIgnite = true;
            }
        } else if (FIRE.isFlammable(world, i, j + 1, k)) {
            shouldIgnite = true;
        }
        return shouldIgnite;
    }

    private void BBQIgnite(World world, int i, int j, int k) {
        SetBBQLitFlag(world, i, j, k);
        world.playNoteBlockActionAt(i,j,k, 0, 0); /// Fire
        int ignitedBlockID = HibachiIgnitionRegistry.getInstance().getIgnitedID(world.getBlockId(i, j + 1, k));
        if (ignitedBlockID != 0) {
            world.setBlock(i, j + 1, k, ignitedBlockID);
        } else if (BBQShouldIgniteAbove(world, i, j, k)) {
            world.setBlock(i, j + 1, k, FIRE.id);
        }
    }

    private void BBQExtinguish(World world, int i, int j, int k) {
        ClearBBQLitFlag(world, i, j, k);
        world.playNoteBlockActionAt(i,j,k, 0, 1); /// Fizz
        boolean isFireAbove = world.getBlockId(i, j + 1, k) == FIRE.id || world.getBlockId(i, j + 1, k) == BlockListener.stokedFire.id;
        if (isFireAbove) {
            world.setBlock(i, j + 1, k, 0);
        }
    }

}
