
package net.kozibrodka.wolves.blocks;

import com.jcraft.jorbis.Block;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.minecraft.block.BlockBase;
import net.minecraft.block.Fluid;
import net.minecraft.block.material.Material;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.util.Random;


public class FCBlockBBQ extends TemplateBlockBase

{
    public FCBlockBBQ(Identifier iid)
    {
        super(iid, Material.STONE);
        setHardness(3.5F);
        setSounds(STONE_SOUNDS);
        setTicksRandomly(true);
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
                if(iBlockAboveID != BlockBase.FIRE.id && iBlockAboveID != mod_FCBetterThanWolves.fcStokedFire.id && BBQShouldIgniteAbove(world, i, j, k))
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
            if(iBlockAboveID == BlockBase.FIRE.id || iBlockAboveID == mod_FCBetterThanWolves.fcStokedFire.id)
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
                if(iBlockAboveID != BlockBase.FIRE.id && iBlockAboveID != mod_FCBetterThanWolves.fcStokedFire.id && BBQShouldIgniteAbove(world, i, j, k))
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
//        int iMetaData = world.getTileMeta(i, j, k);
        int iMetaData = world.getBlockState(i,j,k).get(POWER);
        return (iMetaData & 4) > 0;
    }

    private void SetBBQLitFlag(Level world, int i, int j, int k)
    {
//        int iMetaData = world.getTileMeta(i, j, k);
        BlockState currentState = world.getBlockState(i, j, k);
        int iMetaData = currentState.get(POWER);
        world.setBlockStateWithNotify(i,j,k,currentState.with(POWER,iMetaData | 4));
//        world.setTileMeta(i, j, k, iMetaData | 4);
    }

    private void ClearBBQLitFlag(Level world, int i, int j, int k)
    {
//        int iMetaData = world.getTileMeta(i, j, k);
        BlockState currentState = world.getBlockState(i, j, k);
        int iMetaData = currentState.get(POWER);
        world.setBlockStateWithNotify(i,j,k,currentState.with(POWER,iMetaData & -5));
//        world.setTileMeta(i, j, k, iMetaData & -5);
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
                //TODO cement
                if(!(targetBlock instanceof Fluid)) // && !(targetBlock instanceof FCBlockCement)
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
        if(BBQShouldIgniteAbove(world, i, j, k))
        {
            world.setTile(i, j + 1, k, FIRE.id);
        }
    }

    private void BBQExtinguish(Level world, int i, int j, int k)
    {
        ClearBBQLitFlag(world, i, j, k);
        world.playSound((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
        boolean isFireAbove = world.getTileId(i, j + 1, k) == FIRE.id || world.getTileId(i, j + 1, k) == mod_FCBetterThanWolves.fcStokedFire.id;
        if(isFireAbove)
        {
            world.setTile(i, j + 1, k, 0);
        }
    }

    /**
     * STATES
     */
    public static final IntProperty POWER = IntProperty.of("power", 0, 4);

    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder){
        builder.add(POWER);
    }
}
