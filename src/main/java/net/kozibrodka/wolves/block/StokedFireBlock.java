package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.EnvironmentInterface;
import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.template.block.TemplateFireBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithWorldRenderer.class)
public class StokedFireBlock extends TemplateFireBlock implements BlockWithWorldRenderer {
    public StokedFireBlock(Identifier iid) {
        super(iid, 31);
        setHardness(0.0F);
        setLuminance(1.0F);
        setSoundGroup(WOOD_SOUND_GROUP);
        disableTrackingStatistics();
        ignoreMetaUpdates();
    }

    @Override
    public void onTick(World world, int i, int j, int k, Random random) {
        boolean flag = world.getBlockId(i, j - 1, k) == Block.NETHERRACK.id;
        if (!canPlaceAt(world, i, j, k)) {
            world.setBlock(i, j, k, 0);
//            System.out.println("HELLO 1");
        }

        if (world.getBlockId(i, j - 2, k) == BlockListener.hibachi.id) {
            if (((HibachiBlock) BlockListener.hibachi).IsBBQLit(world, i, j - 2, k)) {
                flag = true;
            } else {
                world.setBlock(i, j, k, 0);
//                System.out.println("HELLO 2");
                return;
            }
        } else if (world.getBlockId(i, j - 1, k) == BlockListener.hibachi.id) {
            flag = true;
        } else {
            world.setBlock(i, j, k, Block.FIRE.id, 15);
//            System.out.println("HELLO 3");
        }

//        if (world.getTileId(i, j - 1, k) == mod_FCBetterThanWolves.hibachi.id) {
//            if (((FCBlockBBQ) mod_FCBetterThanWolves.hibachi).IsBBQLit(world, i, j - 1, k)) {
//                flag = true;
//            }
//        }

        int iMetaData = world.getBlockMeta(i, j, k);
        if (true) //flag
        {

            if (iMetaData < 15) {
                iMetaData++;
                world.setBlockMeta(i, j, k, iMetaData);
            }
            trySpreadingFire(world, i + 1, j, k, 300, random, 15);
            trySpreadingFire(world, i - 1, j, k, 300, random, 15);
            trySpreadingFire(world, i, j - 1, k, 250, random, 15);
            trySpreadingFire(world, i, j + 1, k, 250, random, 15);
            trySpreadingFire(world, i, j, k - 1, 300, random, 15);
            trySpreadingFire(world, i, j, k + 1, 300, random, 15);
            for (int i1 = i - 1; i1 <= i + 1; i1++) {
                for (int j1 = k - 1; j1 <= k + 1; j1++) {
                    for (int k1 = j - 1; k1 <= j + 4; k1++) {
                        if (i1 == i && k1 == j && j1 == k) {
                            continue;
                        }
                        int l1 = 100;
                        if (k1 > j + 1) {
                            l1 += (k1 - (j + 1)) * 100;
                        }
                        int i2 = this.method_1827(world, i1, k1, j1);
                        if (i2 <= 0) {
                            continue;
                        }
                        int j2 = (i2 + 40) / 45;
                        if (
                                j2 <= 0 ||
                                        random.nextInt(l1) > j2 ||
                                        world.isRaining() && world.isRaining(i1, k1, j1) ||
                                        world.isRaining(i1 - 1, k1, k) ||
                                        world.isRaining(i1 + 1, k1, j1) ||
                                        world.isRaining(i1, k1, j1 - 1) ||
                                        world.isRaining(i1, k1, j1 + 1)) {
                            continue;
                        }
                        int k2 = iMetaData + random.nextInt(5) / 4;
                        if (k2 > 15) {
                            k2 = 15;
                        }
//                    world.placeBlockWithMetaData(i1, k1, j1, BlockBase.FIRE.id, k2);
                        world.setBlock(i1, k1, j1, BlockListener.stokedFire.id, k2);
                    }

                }

            }
        }

        if (iMetaData >= 4) //CODE EDIT (oryginal int value - 3) as its little different
        {
            world.setBlock(i, j, k, Block.FIRE.id, 15);
            world.blockUpdateEvent(i, j, k);
//            System.out.println("HELLO 4");
        }
        world.scheduleBlockUpdate(i, j, k, id, getTickRate());
    }


    @Override
    public boolean canPlaceAt(World arg, int i, int j, int k) {
        return arg.shouldSuffocate(i, j - 1, k) || this.areBlocksAroundFlammable(arg, i, j, k) || arg.getBlockId(i, j - 1, k) == BlockListener.stokedFire.id || arg.getBlockId(i, j - 1, k) == Block.FIRE.id;
    }

    @Override
    public void neighborUpdate(World arg, int i, int j, int k, int l) {
        if (!arg.shouldSuffocate(i, j - 1, k) && !this.areBlocksAroundFlammable(arg, i, j, k) && arg.getBlockId(i, j - 1, k) != BlockListener.stokedFire.id && arg.getBlockId(i, j - 1, k) != Block.FIRE.id) {
            arg.setBlock(i, j, k, 0);
        }
    }

    @Override
    public void onPlaced(World arg, int i, int j, int k) {
        if (arg.getBlockId(i, j - 1, k) != Block.OBSIDIAN.id || !Block.NETHER_PORTAL.method_736(arg, i, j, k)) {
            if (!arg.shouldSuffocate(i, j - 1, k) && !this.areBlocksAroundFlammable(arg, i, j, k) && arg.getBlockId(i, j - 1, k) != BlockListener.stokedFire.id && arg.getBlockId(i, j - 1, k) != Block.FIRE.id) {
                arg.setBlock(i, j, k, 0);
            } else {
                arg.scheduleBlockUpdate(i, j, k, this.id, this.getTickRate());
            }
        }
    }

    private boolean areBlocksAroundFlammable(World world, int x, int y, int z) {
        if (this.isBlockFlammable(world, x + 1, y, z)) {
            return true;
        } else if (this.isBlockFlammable(world, x - 1, y, z)) {
            return true;
        } else if (this.isBlockFlammable(world, x, y - 1, z)) {
            return true;
        } else if (this.isBlockFlammable(world, x, y + 1, z)) {
            return true;
        } else if (this.isBlockFlammable(world, x, y, z - 1)) {
            return true;
        } else {
            return this.isBlockFlammable(world, x, y, z + 1);
        }
    }

    @Override
    public void onEntityCollision(World world, int x, int y, int z, Entity entity) {
        super.onEntityCollision(world, x, y, z, entity);
        if (entity == null) {
            return;
        }
        entity.fireTicks = 10;
        entity.damage(null, 2);
    }

    // TODO: Replace this messy code. And make the top stoked fire visually connect better to the bottom block. - think its done?
    @Override
    public boolean renderWorld(BlockRenderManager tileRenderer, BlockView blockView, int i, int j, int k) {
        Tessellator var5 = Tessellator.INSTANCE;
        int var6 = this.getTexture(0);
//        if (((BlockRendererAccessor)tileRenderer).getTextureOverride() >= 0) {
//            var6 = ((BlockRendererAccessor)tileRenderer).getTextureOverride();
//        }
        float var7 = this.getLuminance(blockView, i, j, k);
        var5.color(var7, var7, var7);


//        int var8 = (var6 & 15) << 4;
//        int var9 = var6 & 240;
//        double var10 = (double)((float)var8 / 256.0F);
//        double var12 = (double)(((float)var8 + 15.99F) / 256.0F);
//        double var14 = (double)((float)var9 / 256.0F);
//        double var16 = (double)(((float)var9 + 15.99F) / 256.0F);

        Atlas.Sprite atlasTX = Atlases.getTerrain().getTexture(var6);
        double var10 = atlasTX.getStartU();
        double var12 = atlasTX.getEndU();
        double var14 = atlasTX.getStartV();
        double var16 = atlasTX.getEndV();

        float var18 = 1.4F;
        double var21;
        double var23;
        double var25;
        double var27;
        double var29;
        double var31;
        double var33;
        if (true) {
            double var19 = (double) i + 0.5D + 0.2D;
            var21 = (double) i + 0.5D - 0.2D;
            var23 = (double) k + 0.5D + 0.2D;
            var25 = (double) k + 0.5D - 0.2D;
            var27 = (double) i + 0.5D - 0.3D;
            var29 = (double) i + 0.5D + 0.3D;
            var31 = (double) k + 0.5D - 0.3D;
            var33 = (double) k + 0.5D + 0.3D;
            var5.vertex(var27, (float) j + var18, k + 1, var12, var14);
            var5.vertex(var19, j, k + 1, var12, var16);
            var5.vertex(var19, j, k, var10, var16);
            var5.vertex(var27, (float) j + var18, k, var10, var14);
            var5.vertex(var29, (float) j + var18, k, var12, var14);
            var5.vertex(var21, j, k, var12, var16);
            var5.vertex(var21, j, k + 1, var10, var16);
            var5.vertex(var29, (float) j + var18, k + 1, var10, var14);

//            var10 = (double)((float)var8 / 256.0F);
//            var12 = (double)(((float)var8 + 15.99F) / 256.0F);
//            var14 = (double)((float)(var9 + 16) / 256.0F);
//            var16 = (double)(((float)var9 + 15.99F + 16.0F) / 256.0F);

            var10 = atlasTX.getStartU();
            var12 = atlasTX.getEndU();
            var14 = atlasTX.getStartV();
            var16 = atlasTX.getEndV();

            var5.vertex(i + 1, (float) j + var18, var33, var12, var14);
            var5.vertex(i + 1, j, var25, var12, var16);
            var5.vertex(i, j, var25, var10, var16);
            var5.vertex(i, (float) j + var18, var33, var10, var14);
            var5.vertex(i, (float) j + var18, var31, var12, var14);
            var5.vertex(i, j, var23, var12, var16);
            var5.vertex(i + 1, j, var23, var10, var16);
            var5.vertex(i + 1, (float) j + var18, var31, var10, var14);
            var19 = (double) i + 0.5D - 0.5D;
            var21 = (double) i + 0.5D + 0.5D;
            var23 = (double) k + 0.5D - 0.5D;
            var25 = (double) k + 0.5D + 0.5D;
            var27 = (double) i + 0.5D - 0.4D;
            var29 = (double) i + 0.5D + 0.4D;
            var31 = (double) k + 0.5D - 0.4D;
            var33 = (double) k + 0.5D + 0.4D;
            var5.vertex(var27, (float) j + var18, k, var10, var14);
            var5.vertex(var19, j, k, var10, var16);
            var5.vertex(var19, j, k + 1, var12, var16);
            var5.vertex(var27, (float) j + var18, k + 1, var12, var14);
            var5.vertex(var29, (float) j + var18, k + 1, var10, var14);
            var5.vertex(var21, j, k + 1, var10, var16);
            var5.vertex(var21, j, k, var12, var16);
            var5.vertex(var29, (float) j + var18, k, var12, var14);

//            var10 = (double)((float)var8 / 256.0F);
//            var12 = (double)(((float)var8 + 15.99F) / 256.0F);
//            var14 = (double)((float)var9 / 256.0F);
//            var16 = (double)(((float)var9 + 15.99F) / 256.0F);

            var10 = atlasTX.getStartU();
            var12 = atlasTX.getEndU();
            var14 = atlasTX.getStartV();
            var16 = atlasTX.getEndV();

            var5.vertex(i, (float) j + var18, var33, var10, var14);
            var5.vertex(i, j, var25, var10, var16);
            var5.vertex(i + 1, j, var25, var12, var16);
            var5.vertex(i + 1, (float) j + var18, var33, var12, var14);
            var5.vertex(i + 1, (float) j + var18, var31, var10, var14);
            var5.vertex(i + 1, j, var23, var10, var16);
            var5.vertex(i, j, var23, var12, var16);
            var5.vertex(i, (float) j + var18, var31, var12, var14);
        }

        return true;
    }
}
