
package net.kozibrodka.wolves.blocks;

import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.events.TextureListener;
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

//    public int getTextureForSide(int i, int j)
//    {
//        if(j < 0)
//        {
//            j = 7;
//        }
//        return texture + j;
//    }

    public int getTextureForSide(int iSide, int iMetaData)
    {
        return switch (iMetaData) {
            case 0 -> TextureListener.hemp_0;
            case 1 -> TextureListener.hemp_1;
            case 2 -> TextureListener.hemp_2;
            case 3 -> TextureListener.hemp_3;
            case 4 -> TextureListener.hemp_4;
            case 5 -> TextureListener.hemp_5;
            case 6 -> TextureListener.hemp_6;
            case 7 -> TextureListener.hemp_7;
            default -> 0;
        };
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
                int l = world.getTileMeta(i, j, k);
                if(l < 7)
                {
                    if(random.nextInt(20) == 0)
                    {
                        l++;
                        world.setTileMeta(i, j, k, l);
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
                                world.placeBlockWithMetaData(i, targetj, k, id, 7);
                            }
                            break;
                        }
                        targetj++;
                    } while(true);
                }
            }
        }
    }

    public void beforeDestroyedByExplosion(Level world, int i, int j, int k, int l, float f)
    {
        super.beforeDestroyedByExplosion(world, i, j, k, l, f);
        if(world.isServerSide)
        {
            return;
        }
        if(world.rand.nextInt(100) < 50)
        {
            float f1 = 0.7F;
            float f2 = world.rand.nextFloat() * f1 + (1.0F - f1) * 0.5F;
            float f3 = world.rand.nextFloat() * f1 + (1.0F - f1) * 0.5F;
            float f4 = world.rand.nextFloat() * f1 + (1.0F - f1) * 0.5F;
            Item entityitem = new Item(world, (float)i + f2, (float)j + f3, (float)k + f4, new ItemInstance(mod_FCBetterThanWolves.fcHempSeeds));
            entityitem.pickupDelay = 10;
            world.spawnEntity(entityitem);
        }
    }

    public int getDropId(int i, Random random)
    {
        if(i == 7)
        {
            return mod_FCBetterThanWolves.fcHemp.id;
        } else
        {
            return -1;
        }
    }

    public int getDropCount(Random random)
    {
        return 1;
    }

    /**
     * EXTRA
     */
//    public void updateBoundingBox(BlockView arg, int i, int j, int k)
//    {
//        Level level = Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).level;
//        float max = 0.1F;
//        float f = 0.2F;
//        int state;
//        if(level.getTileId(i,j,k) == mod_FCBetterThanWolves.fcHempCrop.id) {
//            state = 7 - level.getTileMeta(i, j, k);
//            setBoundingBox(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F - (max*state), 0.5F + f);
//        }
//    }

    /**
     * GROWTH DEGUB
     */

}
