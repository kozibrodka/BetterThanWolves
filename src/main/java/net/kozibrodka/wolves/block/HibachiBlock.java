
package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.api.HibachiIgnitionRegistry;
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


public class HibachiBlock extends TemplateBlock

{
    public HibachiBlock(Identifier iid)
    {
        super(iid, Material.STONE);
        setHardness(3.5F);
        setSoundGroup(DEFAULT_SOUND_GROUP);
        setTickRandomly(true);
    }

    public int getTexture(int side)
    {
        if(side == 1)
        {
            return TextureListener.hibachi_top;
        }
        return side != 0 ? TextureListener.hibachi_side : TextureListener.hibachi_bottom;
    }

    public int getTickRate()
    {
        return 4;
    }

    public void onPlaced(World world, int i, int j, int k)
    {
        world.scheduleBlockUpdate(i, j, k, id, getTickRate());
    }

    public void onTick(World world, int i, int j, int k, Random random)
    {
        boolean bPowered = world.canTransferPower(i, j, k) || world.canTransferPower(i, j + 1, k);
        if(bPowered)
        {
            if(!IsBBQLit(world, i, j, k))
            {
                BBQIgnite(world, i, j, k);
            } else
            {
                int iBlockAboveID = world.getBlockId(i, j + 1, k);
                if(iBlockAboveID != Block.FIRE.id && iBlockAboveID != BlockListener.stokedFire.id && BBQShouldIgniteAbove(world, i, j, k))
                {
                    world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "fire.ignite", 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
                    if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                        voicePacket(world, "fire.ignite", i, j, k, 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
                    }
                    world.setBlock(i, j + 1, k, FIRE.id);
                }
            }
        } else
        if(IsBBQLit(world, i, j, k))
        {
            BBQExtinguish(world, i, j, k);
        } else
        {
            int iBlockAboveID = world.getBlockId(i, j + 1, k);
            if(iBlockAboveID == Block.FIRE.id || iBlockAboveID == BlockListener.stokedFire.id)
            {
                world.setBlock(i, j + 1, k, 0);
            }
        }
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(World world, String name, int x, int y, int z, float g, float h){
        List list2 = world.players;
        if(list2.size() != 0) {
            for(int k = 0; k < list2.size(); k++)
            {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g,h));
            }
        }
    }

    public void neighborUpdate(World world, int i, int j, int k, int l)
    {
        boolean bShouldUpdate = false;
        if(l != FIRE.id)
        {
            boolean bPowered = world.canTransferPower(i, j, k) || world.canTransferPower(i, j + 1, k);
            if(IsBBQLit(world, i, j, k) != bPowered)
            {
                bShouldUpdate = true;
            } else
            if(IsBBQLit(world, i, j, k))
            {
                int iBlockAboveID = world.getBlockId(i, j + 1, k);
                if(iBlockAboveID != Block.FIRE.id && iBlockAboveID != BlockListener.stokedFire.id && BBQShouldIgniteAbove(world, i, j, k))
                {
                    bShouldUpdate = true;
                }
            }
        }
        if(bShouldUpdate)
        {
            world.scheduleBlockUpdate(i, j, k, id, getTickRate());
        }
    }

    public boolean IsBBQLit(World world, int i, int j, int k)
    {
        int iMetaData = world.getBlockMeta(i, j, k);
        return (iMetaData & 4) > 0;
    }

    private void SetBBQLitFlag(World world, int i, int j, int k)
    {
        int iMetaData = world.getBlockMeta(i, j, k);
        world.setBlockMeta(i, j, k, iMetaData | 4);
    }

    private void ClearBBQLitFlag(World world, int i, int j, int k)
    {
        int iMetaData = world.getBlockMeta(i, j, k);
        world.setBlockMeta(i, j, k, iMetaData & -5);
    }

    private boolean BBQShouldIgniteAbove(World world, int i, int j, int k)
    {
        boolean shouldIgnite = false;
        int iTargetid = world.getBlockId(i, j + 1, k);
        Block targetBlock = Block.BLOCKS[iTargetid];
        if(!world.method_1783(i, j + 1, k))
        {
            if(targetBlock != null)
            {
                if(!(targetBlock instanceof LiquidBlock) && !(targetBlock instanceof CementBlock) && (world.getMaterial(i, j + 1, k) == Material.WOOD || world.getMaterial(i, j + 1, k) == Material.WOOL || world.getMaterial(i, j + 1, k) == Material.SNOW_LAYER || world.getMaterial(i, j + 1, k) == Material.PLANT || world.getMaterial(i, j + 1, k) == Material.PISTON_BREAKABLE || world.getMaterial(i, j + 1, k) == Material.CACTUS || world.getMaterial(i, j + 1, k) == Material.SOLID_ORGANIC || world.getMaterial(i, j + 1, k) == Material.PUMPKIN || world.getMaterial(i, j + 1, k) == Material.AIR))
                {
                    shouldIgnite = true;
                }
            } else
            {
                shouldIgnite = true;
            }
        } else
        if(FIRE.isBlockFlammable(world, i, j + 1, k))
        {
            shouldIgnite = true;
        }
        return shouldIgnite;
    }

    private void BBQIgnite(World world, int i, int j, int k)
    {
        SetBBQLitFlag(world, i, j, k);
        world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "fire.ignite", 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
        if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
            voicePacket(world, "fire.ignite", i, j, k, 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
        }
        int ignitedBlockID = HibachiIgnitionRegistry.getInstance().getIgnitedID(world.getBlockId(i, j + 1, k));
        if (ignitedBlockID != 0)
        {
            world.setBlock(i, j + 1, k, ignitedBlockID);
        }
        else if(BBQShouldIgniteAbove(world, i, j, k))
        {
            world.setBlock(i, j + 1, k, FIRE.id);
        }
    }

    private void BBQExtinguish(World world, int i, int j, int k)
    {
        ClearBBQLitFlag(world, i, j, k);
        world.playSound((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, "random.fizz", 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
        if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
            voicePacket(world, "random.fizz", i, j, k, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
        }
        boolean isFireAbove = world.getBlockId(i, j + 1, k) == FIRE.id || world.getBlockId(i, j + 1, k) == BlockListener.stokedFire.id;
        if(isFireAbove)
        {
            world.setBlock(i, j + 1, k, 0);
        }
    }

}
