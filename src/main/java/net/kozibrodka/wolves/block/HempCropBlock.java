
package net.kozibrodka.wolves.block;

import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.utils.SoilTemplate;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplatePlantBlock;

import java.util.Random;

public class HempCropBlock extends TemplatePlantBlock

{

    public HempCropBlock(Identifier i)
    {
        super(i, 240);
        setHardness(0.0F);
        setSoundGroup(DIRT_SOUND_GROUP);
        disableTrackingStatistics();
        float f = 0.2F;
        setBoundingBox(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
        setTickRandomly(true);
    }

//    public int getTextureForSide(int i, int j)
//    {
//        if(j < 0)
//        {
//            j = 7;
//        }
//        return texture + j;
//    }

    public int getTexture(int iSide, int iMetaData)
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

    protected boolean method_1683(int i)
    {
        return i == Block.FARMLAND.id || i == id;
    }

    public void onTick(World world, int i, int j, int k, Random random)
    {
        super.onTick(world, i, j, k, random);
        if(world.getLightLevel(i, j + 1, k) >= 15 || world.getLightLevel(i, j + 2, k) >= 15 || world.getLightLevel(i, j, k) >= 15)
        {
            boolean bOnHydratedSoil = false;
            int iBlockBelowID = world.getBlockId(i, j - 1, k);
            if(iBlockBelowID == Block.FARMLAND.id && world.getBlockMeta(i, j - 1, k) > 0)
            {
                bOnHydratedSoil = true;
            } else
            if(UnsortedUtils.CanPlantGrowOnBlock(world, i, j - 1, k, this))
            {
                Block blockBelow = Block.BLOCKS[iBlockBelowID];
                if((blockBelow instanceof SoilTemplate) && ((SoilTemplate)blockBelow).IsBlockHydrated(world, i, j - 1, k))
                {
                    bOnHydratedSoil = true;
                }
            }
            if(bOnHydratedSoil)
            {
                int l = world.getBlockMeta(i, j, k);
                if(l < 7)
                {
                    if(random.nextInt(20) == 0)
                    {
                        l++;
                        world.setBlock(i, j, k, this.id, l);
//                        world.blockUpdateEvent(i, j, k);     ??? Is this needed for serve?
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
                        int iTargetid = world.getBlockId(i, targetj, k);
                        if(iTargetid != id)
                        {
                            if(world.isAir(i, targetj, k) && random.nextInt(60) == 0)
                            {
                                world.setBlock(i, targetj, k, id, 7);
                                //                        world.blockUpdateEvent(i, j, k);     ??? Is this needed for serve?
                            }
                            break;
                        }
                        targetj++;
                    } while(true);
                }
            }
        }
    }

    public void dropStacks(World world, int i, int j, int k, int l, float f)
    {
        super.dropStacks(world, i, j, k, l, f);
        if(world.isRemote)
        {
            return;
        }
        if(world.random.nextInt(100) < 50)
        {
            float f1 = 0.7F;
            float f2 = world.random.nextFloat() * f1 + (1.0F - f1) * 0.5F;
            float f3 = world.random.nextFloat() * f1 + (1.0F - f1) * 0.5F;
            float f4 = world.random.nextFloat() * f1 + (1.0F - f1) * 0.5F;
            ItemEntity entityitem = new ItemEntity(world, (float)i + f2, (float)j + f3, (float)k + f4, new ItemStack(ItemListener.hempSeeds));
            entityitem.pickupDelay = 10;
            world.spawnEntity(entityitem);
        }
    }

    public int getDroppedItemId(int i, Random random)
    {
        if(i == 7)
        {
            return ItemListener.hemp.id;
        } else
        {
            return -1;
        }
    }

    public int getDroppedItemCount(Random random)
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
//        if(level.getTileId(i,j,k) == mod_FCBetterThanWolves.hempCrop.id) {
//            state = 7 - level.getTileMeta(i, j, k);
//            setBoundingBox(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F - (max*state), 0.5F + f);
//        }
//    }

    /**
     * GROWTH DEGUB
     */

}
