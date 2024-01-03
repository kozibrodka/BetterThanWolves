
package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.modsupport.HibachiIgnitionRegistry;
import net.minecraft.block.BlockBase;
import net.minecraft.block.Fluid;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;


public class Hibachi extends TemplateBlock

{
    public Hibachi(Identifier iid)
    {
        super(iid, Material.STONE);
        setHardness(3.5F);
        setSounds(STONE_SOUNDS);
        setTicksRandomly(true);
    }

    public int getTextureForSide(int side)
    {
        if(side == 1)
        {
            return TextureListener.hibachi_top;
        }
        return side != 0 ? TextureListener.hibachi_side : TextureListener.hibachi_bottom;
    }

    public int getTickrate()
    {
        return 4;
    }

    public void onBlockPlaced(Level world, int i, int j, int k)
    {
        world.method_216(i, j, k, id, getTickrate());
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
        boolean bPowered = world.method_263(i, j, k) || world.method_263(i, j + 1, k);
        if(bPowered)
        {
            if(!IsBBQLit(world, i, j, k))
            {
                BBQIgnite(world, i, j, k);
            } else
            {
                int iBlockAboveID = world.getTileId(i, j + 1, k);
                if(iBlockAboveID != BlockBase.FIRE.id && iBlockAboveID != BlockListener.stokedFire.id && BBQShouldIgniteAbove(world, i, j, k))
                {
                    world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "fire.ignite", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
                    world.setTile(i, j + 1, k, FIRE.id);
                }
            }
        } else
        if(IsBBQLit(world, i, j, k))
        {
            BBQExtinguish(world, i, j, k);
        } else
        {
            int iBlockAboveID = world.getTileId(i, j + 1, k);
            if(iBlockAboveID == BlockBase.FIRE.id || iBlockAboveID == BlockListener.stokedFire.id)
            {
                world.setTile(i, j + 1, k, 0);
            }
        }
    }

    public void onAdjacentBlockUpdate(Level world, int i, int j, int k, int l)
    {
        boolean bShouldUpdate = false;
        if(l != FIRE.id)
        {
            boolean bPowered = world.method_263(i, j, k) || world.method_263(i, j + 1, k);
            if(IsBBQLit(world, i, j, k) != bPowered)
            {
                bShouldUpdate = true;
            } else
            if(IsBBQLit(world, i, j, k))
            {
                int iBlockAboveID = world.getTileId(i, j + 1, k);
                if(iBlockAboveID != BlockBase.FIRE.id && iBlockAboveID != BlockListener.stokedFire.id && BBQShouldIgniteAbove(world, i, j, k))
                {
                    bShouldUpdate = true;
                }
            }
        }
        if(bShouldUpdate)
        {
            world.method_216(i, j, k, id, getTickrate());
        }
    }

    public boolean IsBBQLit(Level world, int i, int j, int k)
    {
        int iMetaData = world.getTileMeta(i, j, k);
        return (iMetaData & 4) > 0;
    }

    private void SetBBQLitFlag(Level world, int i, int j, int k)
    {
        int iMetaData = world.getTileMeta(i, j, k);
        world.setTileMeta(i, j, k, iMetaData | 4);
    }

    private void ClearBBQLitFlag(Level world, int i, int j, int k)
    {
        int iMetaData = world.getTileMeta(i, j, k);
        world.setTileMeta(i, j, k, iMetaData & -5);
    }

    private boolean BBQShouldIgniteAbove(Level world, int i, int j, int k)
    {
        boolean shouldIgnite = false;
        int iTargetid = world.getTileId(i, j + 1, k);
        BlockBase targetBlock = BlockBase.BY_ID[iTargetid];
        if(!world.isFullOpaque(i, j + 1, k))
        {
            if(targetBlock != null)
            {
                if(!(targetBlock instanceof Fluid) && !(targetBlock instanceof Cement) && (world.getMaterial(i, j + 1, k) == Material.WOOD || world.getMaterial(i, j + 1, k) == Material.WOOL || world.getMaterial(i, j + 1, k) == Material.SNOW || world.getMaterial(i, j + 1, k) == Material.PLANT || world.getMaterial(i, j + 1, k) == Material.DOODADS || world.getMaterial(i, j + 1, k) == Material.CACTUS || world.getMaterial(i, j + 1, k) == Material.ORGANIC || world.getMaterial(i, j + 1, k) == Material.PUMPKIN || world.getMaterial(i, j + 1, k) == Material.AIR))
                {
                    shouldIgnite = true;
                }
            } else
            {
                shouldIgnite = true;
            }
        } else
        if(FIRE.method_1824(world, i, j + 1, k))
        {
            shouldIgnite = true;
        }
        return shouldIgnite;
    }

    private void BBQIgnite(Level world, int i, int j, int k)
    {
        SetBBQLitFlag(world, i, j, k);
        world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "fire.ignite", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);

        int ignitedBlockID = HibachiIgnitionRegistry.getInstance().getIgnitedID(world.getTileId(i, j + 1, k));
        if (ignitedBlockID != 0)
        {
            world.setTile(i, j + 1, k, ignitedBlockID);
        }
        else if(BBQShouldIgniteAbove(world, i, j, k))
        {
            world.setTile(i, j + 1, k, FIRE.id);
        }
    }

    private void BBQExtinguish(Level world, int i, int j, int k)
    {
        ClearBBQLitFlag(world, i, j, k);
        world.playSound((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
        boolean isFireAbove = world.getTileId(i, j + 1, k) == FIRE.id || world.getTileId(i, j + 1, k) == BlockListener.stokedFire.id;
        if(isFireAbove)
        {
            world.setTile(i, j + 1, k, 0);
        }
    }

}
