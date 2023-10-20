package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.structure.BirchTree;
import net.minecraft.level.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(BirchTree.class)
public class BirchTreeMixin extends Structure {
    @Override
    public boolean generate(Level arg, Random random, int i, int j, int k) {
        int var6 = random.nextInt(3) + 5;
        boolean var7 = true;
        if (j >= 1 && j + var6 + 1 <= 128) {
            int var8;
            int var10;
            int var11;
            int var12;
            for(var8 = j; var8 <= j + 1 + var6; ++var8) {
                byte var9 = 1;
                if (var8 == j) {
                    var9 = 0;
                }

                if (var8 >= j + 1 + var6 - 2) {
                    var9 = 2;
                }

                for(var10 = i - var9; var10 <= i + var9 && var7; ++var10) {
                    for(var11 = k - var9; var11 <= k + var9 && var7; ++var11) {
                        if (var8 >= 0 && var8 < 128) {
                            var12 = arg.getTileId(var10, var8, var11);
                            if (var12 != 0 && var12 != BlockBase.LEAVES.id) {
                                var7 = false;
                            }
                        } else {
                            var7 = false;
                        }
                    }
                }
            }

            if (!var7) {
                return false;
            } else {
                var8 = arg.getTileId(i, j - 1, k);
                if ((var8 == BlockBase.GRASS.id || var8 == BlockBase.DIRT.id || UnsortedUtils.CanPlantGrowOnBlock(arg, i, j - 1, k, BlockBase.SAPLING)) && j < 128 - var6 - 1) {
                    if(var8 != BlockListener.planter.id)
                    {
                        arg.setTileInChunk(i, j - 1, k, BlockBase.DIRT.id);
                    }
                    int var16;
                    for(var16 = j - 3 + var6; var16 <= j + var6; ++var16) {
                        var10 = var16 - (j + var6);
                        var11 = 1 - var10 / 2;

                        for(var12 = i - var11; var12 <= i + var11; ++var12) {
                            int var13 = var12 - i;

                            for(int var14 = k - var11; var14 <= k + var11; ++var14) {
                                int var15 = var14 - k;
                                if ((Math.abs(var13) != var11 || Math.abs(var15) != var11 || random.nextInt(2) != 0 && var10 != 0) && !BlockBase.FULL_OPAQUE[arg.getTileId(var12, var16, var14)]) {
                                    arg.setTileWithMetadata(var12, var16, var14, BlockBase.LEAVES.id, 2);
                                }
                            }
                        }
                    }

                    for(var16 = 0; var16 < var6; ++var16) {
                        var10 = arg.getTileId(i, j + var16, k);
                        if (var10 == 0 || var10 == BlockBase.LEAVES.id) {
                            arg.setTileWithMetadata(i, j + var16, k, BlockBase.LOG.id, 2);
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
}
