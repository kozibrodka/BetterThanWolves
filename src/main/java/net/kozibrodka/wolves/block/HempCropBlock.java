package net.kozibrodka.wolves.block;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.utils.SoilTemplate;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.template.block.TemplatePlantBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class HempCropBlock extends TemplatePlantBlock {

    public HempCropBlock(Identifier i) {
        super(i, 240);
        setHardness(0.0F);
        setSoundGroup(DIRT_SOUND_GROUP);
        disableTrackingStatistics();
        float f = 0.2F;
        setBoundingBox(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
        setTickRandomly(true);
    }

    public int getTexture(int iSide, int iMetaData) {
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

    public void onTick(World world, int i, int j, int k, Random random) {
        super.onTick(world, i, j, k, random);
        if (world.getLightLevel(i, j + 1, k) >= 15 || world.getLightLevel(i, j + 2, k) >= 15 || world.getLightLevel(i, j, k) >= 15) {
            boolean onHydratedSoil = false;
            int blockBelowID = world.getBlockId(i, j - 1, k);
            if (blockBelowID == Block.FARMLAND.id && world.getBlockMeta(i, j - 1, k) > 0) {
                onHydratedSoil = true;
            } else if (UnsortedUtils.CanPlantGrowOnBlock(world, i, j - 1, k, this)) {
                Block blockBelow = Block.BLOCKS[blockBelowID];
                if ((blockBelow instanceof SoilTemplate) && ((SoilTemplate) blockBelow).IsBlockHydrated(world, i, j - 1, k)) {
                    onHydratedSoil = true;
                }
            }
            if (onHydratedSoil) {
                int l = world.getBlockMeta(i, j, k);
                if (l < 7) {
                    if (random.nextInt(20) == 0) {
                        l++;
                        world.setBlock(i, j, k, this.id, l);
                    }
                } else {
                    int targetj = j + 1;
                    do {
                        if (targetj >= j + 2) {
                            break;
                        }
                        int targetId = world.getBlockId(i, targetj, k);
                        if (targetId != id) {
                            if (world.isAir(i, targetj, k) && random.nextInt(60) == 0) {
                                world.setBlock(i, targetj, k, id, 7);
                            }
                            break;
                        }
                        targetj++;
                    } while (true);
                }
            }
        }
    }

    public void dropStacks(World world, int i, int j, int k, int l, float f) {
        super.dropStacks(world, i, j, k, l, f);
        if (world.isRemote) {
            return;
        }
        if (world.random.nextInt(100) < 50) {
            float f1 = 0.7F;
            float f2 = world.random.nextFloat() * f1 + (1.0F - f1) * 0.5F;
            float f3 = world.random.nextFloat() * f1 + (1.0F - f1) * 0.5F;
            float f4 = world.random.nextFloat() * f1 + (1.0F - f1) * 0.5F;
            ItemEntity entityitem = new ItemEntity(world, (float) i + f2, (float) j + f3, (float) k + f4, new ItemStack(ItemListener.hempSeeds));
            entityitem.pickupDelay = 10;
            world.spawnEntity(entityitem);
        }
    }

    public int getDroppedItemId(int i, Random random) {
        if (i == 7) {
            return ItemListener.hemp.id;
        } else {
            return -1;
        }
    }

    public int getDroppedItemCount(Random random) {
        return 1;
    }

    @Override
    public boolean onBonemealUse(World world, int x, int y, int z, BlockState state) {
        if (world.getBlockMeta(x, y, z) == 7) {
            if (world.getBlockId(x, y + 1, z) == 0 && world.getBlockId(x, y - 1, z) != BlockListener.hempCrop.id) {
                world.setBlock(x, y + 1, z, BlockListener.hempCrop.id, 7);
                return true;
            }
            return false;
        }
        world.setBlockMeta(x, y, z, 7);
        return true;
    }

    @Override
    protected boolean canPlantOnTop(int id) {
        if (id == BlockListener.hempCrop.id) {
            return true;
        }
        return super.canPlantOnTop(id);
    }
}
