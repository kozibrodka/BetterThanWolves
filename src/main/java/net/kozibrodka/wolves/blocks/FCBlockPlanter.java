package net.kozibrodka.wolves.blocks;

import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.utils.FCIBlock;
import net.kozibrodka.wolves.utils.FCISoil;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.util.Random;

public class FCBlockPlanter extends TemplateBlockBase
    implements FCIBlock, FCISoil
{

    public FCBlockPlanter(Identifier iid)
    {
        super(iid, Material.GLASS);
        texture = 77;
        setHardness(0.6F);
        setSounds(GLASS_SOUNDS);
        setTicksRandomly(true);
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
        int iOldGrowthState = GetGrowthState(world, i, j, k);
        int iNewGrowthState = 0;
        if(world.isAir(i, j + 1, k) && world.placeTile(i, j + 1, k) >= 8)
        {
            iNewGrowthState = iOldGrowthState;
            if(random.nextInt(50) == 0 && ++iNewGrowthState > 1)
            {
                iNewGrowthState = 0;
                if(random.nextInt(2) == 0)
                {
                    world.setTile(i, j + 1, k, BlockBase.ROSE.id);
                } else
                {
                    world.setTile(i, j + 1, k, BlockBase.DANDELION.id);
                }
            }
        }
        if(iNewGrowthState != iOldGrowthState)
        {
            SetGrowthState(world, i, j, k, iNewGrowthState);
        }
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

    public boolean CanPlantGrowOnBlock(Level world, int i, int j, int k, BlockBase plantBlock)
    {
        return DoesPlanterContainSoil(world, i, j, k);
    }

    public boolean IsBlockHydrated(Level world, int i, int j, int k)
    {
        return DoesPlanterContainSoil(world, i, j, k);
    }

    public boolean IsBlockConsideredNeighbouringWater(Level world, int i, int j, int k)
    {
        return DoesPlanterContainSoil(world, i, j, k);
    }

    public boolean DoesPlanterContainSoil(BlockView iBlockAccess, int i, int j, int k)
    {
        return true;
    }

    public int GetGrowthState(BlockView iBlockAccess, int i, int j, int k)
    {
        Level level = Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).level;
        if(level.getTileId(i,j,k) == mod_FCBetterThanWolves.fcPlanter.id) {
            return level.getBlockState(i, j, k).get(GROWTH);
        }else{
            return 0;
        }
//        int iMetaData = iBlockAccess.getTileMeta(i, j, k);
//        return (iMetaData & 6) >> 1;
    }

    public void SetGrowthState(Level world, int i, int j, int k, int iGrowthState)
    {
        if(iGrowthState < 3)
        {
            BlockState currentState = world.getBlockState(i, j, k);
            world.setBlockStateWithNotify(i,j,k, currentState.with(GROWTH, iGrowthState));
        }
//        int iMetaData = world.getTileMeta(i, j, k) & -7;
//        iMetaData |= (iGrowthState & 3) << 1;
//        world.setTileMeta(i, j, k, iMetaData);
//        world.method_243(i, j, k);
    }

    public static final float m_fPlanterWidth = 0.75F;
    public static final float m_fPlanterHalfWidth = 0.375F;
    public static final float m_fPlanterBandHeight = 0.3125F;
    public static final float m_fPlanterBandHalfHeight = 0.15625F;
    private final int iPlanterDirtTextureIndex = 78;

    /**
     * STATES
     */
    public static final IntProperty GROWTH = IntProperty.of("growth", 0, 2);

    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder){
        builder.add(GROWTH);
    }
}
