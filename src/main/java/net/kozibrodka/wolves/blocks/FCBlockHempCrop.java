
package net.kozibrodka.wolves.blocks;

import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.utils.FCISoil;
import net.kozibrodka.wolves.utils.FCUtilsMisc;
import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Item;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.template.block.TemplatePlant;
import net.modificationstation.stationapi.api.vanillafix.item.Items;

import java.util.List;
import java.util.Random;

public class FCBlockHempCrop extends TemplatePlant

{

    public FCBlockHempCrop(Identifier i)
    {
        super(i, 240);
        setHardness(0.0F);
        setSounds(GRASS_SOUNDS);
        disableStat();
        float f = 0.2F;
        setBoundingBox(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
        setTicksRandomly(true);
    }

    protected boolean canPlantOnTopOf(int i)
    {
        return i == BlockBase.FARMLAND.id || i == id;
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
        super.onScheduledTick(world, i, j, k, random);
        if(world.placeTile(i, j + 1, k) >= 15 || world.placeTile(i, j + 2, k) >= 15 || world.placeTile(i, j, k) >= 15)
        {
            boolean bOnHydratedSoil = false;
            int iBlockBelowID = world.getTileId(i, j - 1, k);
            if(iBlockBelowID == BlockBase.FARMLAND.id && world.getTileMeta(i, j - 1, k) > 0)
            {
                bOnHydratedSoil = true;
            } else
            if(FCUtilsMisc.CanPlantGrowOnBlock(world, i, j - 1, k, this))
            {
                BlockBase blockBelow = BlockBase.BY_ID[iBlockBelowID];
                if((blockBelow instanceof FCISoil) && ((FCISoil)blockBelow).IsBlockHydrated(world, i, j - 1, k))
                {
                    bOnHydratedSoil = true;
                }
            }
            if(bOnHydratedSoil)
            {
                BlockState currentState = world.getBlockState(i, j, k);
//                int l = world.getTileMeta(i, j, k);
                int l = currentState.get(GROWTH);
                if(l < 7)
                {
                    if(random.nextInt(20) == 0)
                    {
                        l++;
//                        world.setTileMeta(i, j, k, l);
                        world.setBlockStateWithNotify(i,j,k, currentState.with(GROWTH, l));
                    }
                } else
                {
                    int targetj = j + 1;
                    do
                    {
                        if(targetj >= j + 2)
                        {
                            break;
                        }
                        int iTargetid = world.getTileId(i, targetj, k);
                        if(iTargetid != id)
                        {
                            if(world.isAir(i, targetj, k) && random.nextInt(60) == 0)
                            {
//                                world.placeBlockWithMetaData(i, targetj, k, id, 7);
                                world.setTile(i, targetj, k, id);
                                world.setBlockStateWithNotify(i, targetj, k, currentState.with(GROWTH,7));
                            }
                            break;
                        }
                        targetj++;
                    } while(true);
                }
            }
        }
    }

    //TODO w moim modzie z melonami ogarnac co i jak

//    public void beforeDestroyedByExplosion(Level world, int i, int j, int k, int l, float f)
//    {
//        super.beforeDestroyedByExplosion(world, i, j, k, l, f);
//        if(world.isServerSide)
//        {
//            return;
//        }
//        if(world.rand.nextInt(100) < 50)
//        {
//            float f1 = 0.7F;
//            float f2 = world.rand.nextFloat() * f1 + (1.0F - f1) * 0.5F;
//            float f3 = world.rand.nextFloat() * f1 + (1.0F - f1) * 0.5F;
//            float f4 = world.rand.nextFloat() * f1 + (1.0F - f1) * 0.5F;
//            Item entityitem = new Item(world, (float)i + f2, (float)j + f3, (float)k + f4, new ItemInstance(mod_FCBetterThanWolves.fcHempSeeds));
//            entityitem.pickupDelay = 10;
//            world.spawnEntity(entityitem);
//        }
//    }

//    public void dropWithChance(Level level, int i, int j, int k, BlockState state, int l, float f) {
//        if(state.get(GROWTH) == 7){
//            this.drop(level, i, j, k, new ItemInstance(mod_FCBetterThanWolves.fcHemp));
//        }
//    }
//
//        public List<ItemInstance> getDropList(Level level, int x, int y, int z, BlockState blockState, int meta) {
//            if(blockState.get(GROWTH) == 7){
//                return List.of(new ItemInstance(mod_FCBetterThanWolves.fcHemp));
//            }
//            return null;
//        }

//    public int getDropId(int i, Random random)
//    {
//        if(i == 7)
//        {
//            return mod_FCBetterThanWolves.fcHemp.id;
//        } else
//        {
//            return -1;
//        }
//    }

//    public int getDropCount(Random random)
//    {
//        return 1;
//    }

    /**
     * EXTRA
     */
    public void updateBoundingBox(BlockView arg, int i, int j, int k)
    {
        Level level = Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).level;
        float max = 0.1F;
        float f = 0.2F;
        int state;
        if(level.getTileId(i,j,k) == mod_FCBetterThanWolves.fcHempCrop.id) {
            state = 7 - level.getBlockState(i, j, k).get(GROWTH);
            setBoundingBox(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F - (max*state), 0.5F + f);
        }
    }

    //DEBUG
    public boolean canUse(Level world, int i, int j, int k, PlayerBase entityplayer) {
        ItemInstance itemstack = entityplayer.inventory.getHeldItem();
        if (itemstack != null && itemstack.itemId == Items.BONE_MEAL.id) { //Items.BONE_MEAL.id
            if(--itemstack.count == 0) {
                entityplayer.inventory.setInventoryItem(entityplayer.inventory.selectedHotbarSlot, null);
            }
                BlockState currentState = world.getBlockState(i, j, k);
                int chuj = world.getBlockState(i,j,k).get(GROWTH);
                if(chuj < 7){
                    world.setBlockStateWithNotify(i,j,k,currentState.with(GROWTH, chuj + 1));
                    return true;
                    }
        }
        return true;
    }

    /**
     * STATES
     */
    public static final IntProperty GROWTH = IntProperty.of("growth", 0, 7);

    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder){
        builder.add(GROWTH);
    }

}
