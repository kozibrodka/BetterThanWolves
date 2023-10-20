package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.structure.SpruceTree;
import net.minecraft.level.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(SpruceTree.class)
public class SpruceTreeMixin extends Structure {
    @Override
    public boolean generate(Level arg, Random random, int i, int j, int k) {
        int var6 = random.nextInt(4) + 6;
        int var7 = 1 + random.nextInt(2);
        int var8 = var6 - var7;
        int var9 = 2 + random.nextInt(2);
        boolean var10 = true;
        if (j >= 1 && j + var6 + 1 <= 128) {
            int var11;
            int var13;
            int var15;
            int var21;
            for(var11 = j; var11 <= j + 1 + var6 && var10; ++var11) {
                boolean var12 = true;
                if (var11 - j < var7) {
                    var21 = 0;
                } else {
                    var21 = var9;
                }

                for(var13 = i - var21; var13 <= i + var21 && var10; ++var13) {
                    for(int var14 = k - var21; var14 <= k + var21 && var10; ++var14) {
                        if (var11 >= 0 && var11 < 128) {
                            var15 = arg.getTileId(var13, var11, var14);
                            if (var15 != 0 && var15 != BlockBase.LEAVES.id) {
                                var10 = false;
                            }
                        } else {
                            var10 = false;
                        }
                    }
                }
            }

            if (!var10) {
                return false;
            } else {
                var11 = arg.getTileId(i, j - 1, k);
                if ((var11 == BlockBase.GRASS.id || var11 == BlockBase.DIRT.id || UnsortedUtils.CanPlantGrowOnBlock(arg, i, j - 1, k, BlockBase.SAPLING)) && j < 128 - var6 - 1) {
                    if(var11 != BlockListener.fcPlanter.id)
                    {
                        arg.setTileInChunk(i, j - 1, k, BlockBase.DIRT.id);
                    }
                    var21 = random.nextInt(2);
                    var13 = 1;
                    byte var22 = 0;

                    int var16;
                    int var17;
                    for(var15 = 0; var15 <= var8; ++var15) {
                        var16 = j + var6 - var15;

                        for(var17 = i - var21; var17 <= i + var21; ++var17) {
                            int var18 = var17 - i;

                            for(int var19 = k - var21; var19 <= k + var21; ++var19) {
                                int var20 = var19 - k;
                                if ((Math.abs(var18) != var21 || Math.abs(var20) != var21 || var21 <= 0) && !BlockBase.FULL_OPAQUE[arg.getTileId(var17, var16, var19)]) {
                                    arg.setTileWithMetadata(var17, var16, var19, BlockBase.LEAVES.id, 1);
                                }
                            }
                        }

                        if (var21 >= var13) {
                            var21 = var22;
                            var22 = 1;
                            ++var13;
                            if (var13 > var9) {
                                var13 = var9;
                            }
                        } else {
                            ++var21;
                        }
                    }

                    var15 = random.nextInt(3);

                    for(var16 = 0; var16 < var6 - var15; ++var16) {
                        var17 = arg.getTileId(i, j + var16, k);
                        if (var17 == 0 || var17 == BlockBase.LEAVES.id) {
                            arg.setTileWithMetadata(i, j + var16, k, BlockBase.LOG.id, 1);
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
