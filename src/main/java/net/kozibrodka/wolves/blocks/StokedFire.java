package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.mixin.FireAccessor;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateFire;

import java.util.Random;


public class StokedFire extends TemplateFire implements BlockWithWorldRenderer
{
    public StokedFire(Identifier iid)
    {
        super(iid, 31);
        setHardness(0.0F);
        setLightEmittance(1.0F);
        setSounds(WOOD_SOUNDS);
        disableStat();
        disableNotifyOnMetaDataChange();
    }

    @Override
    public void onScheduledTick(Level world, int i, int j, int k, Random random) {
        boolean flag = world.getTileId(i, j - 1, k) == BlockBase.NETHERRACK.id;
        if (!canPlaceAt(world, i, j, k)) {
            world.setTile(i, j, k, 0);
            System.out.println("HELLO 1");
        }

        if (world.getTileId(i, j - 2, k) == BlockListener.fcBBQ.id) {
            if (((Hibachi) BlockListener.fcBBQ).IsBBQLit(world, i, j - 2, k)) {
                flag = true;
            } else {
                world.setTile(i, j, k, 0);
                System.out.println("HELLO 2");
                return;
            }
        } else if (world.getTileId(i, j - 1, k) == BlockListener.fcBBQ.id){
            flag = true;
        } else{
            world.placeBlockWithMetaData(i, j, k, BlockBase.FIRE.id, 15);
            System.out.println("HELLO 3");
        }

//        if (world.getTileId(i, j - 1, k) == mod_FCBetterThanWolves.fcBBQ.id) {
//            if (((FCBlockBBQ) mod_FCBetterThanWolves.fcBBQ).IsBBQLit(world, i, j - 1, k)) {
//                flag = true;
//            }
//        }

        int iMetaData = world.getTileMeta(i, j, k);
        if (true) //flag
        {

        if (iMetaData < 15) {
            iMetaData++;
            world.setTileMeta(i, j, k, iMetaData);
        }
        ((FireAccessor) this).invokeFireTick(world, i + 1, j, k, 300, random, 15);
        ((FireAccessor) this).invokeFireTick(world, i - 1, j, k, 300, random, 15);
        ((FireAccessor) this).invokeFireTick(world, i, j - 1, k, 250, random, 15);
        ((FireAccessor) this).invokeFireTick(world, i, j + 1, k, 250, random, 15);
        ((FireAccessor) this).invokeFireTick(world, i, j, k - 1, 300, random, 15);
        ((FireAccessor) this).invokeFireTick(world, i, j, k + 1, 300, random, 15);
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
                    int i2 = ((FireAccessor) this).invokeMethod_1827(world, i1, k1, j1);
                    if (i2 <= 0) {
                        continue;
                    }
                    int j2 = (i2 + 40) / 45;
                    if (
                            j2 <= 0 ||
                                    random.nextInt(l1) > j2 ||
                                    world.isRaining() && world.canRainAt(i1, k1, j1) ||
                                    world.canRainAt(i1 - 1, k1, k) ||
                                    world.canRainAt(i1 + 1, k1, j1) ||
                                    world.canRainAt(i1, k1, j1 - 1) ||
                                    world.canRainAt(i1, k1, j1 + 1)) {
                        continue;
                    }
                    int k2 = iMetaData + random.nextInt(5) / 4;
                    if (k2 > 15) {
                        k2 = 15;
                    }
//                    world.placeBlockWithMetaData(i1, k1, j1, BlockBase.FIRE.id, k2);
                    world.placeBlockWithMetaData(i1, k1, j1, BlockListener.fcStokedFire.id, k2);
                }

            }

        }
    }

        if(iMetaData >= 4) //CODE EDIT (3) as its little different
        {
            world.placeBlockWithMetaData(i, j, k, BlockBase.FIRE.id, 15);
            world.method_243(i, j, k);
            System.out.println("HELLO 4");
        }
        world.method_216(i, j, k, id, getTickrate());
    }


    @Override
    public boolean canPlaceAt(Level arg, int i, int j, int k) {
        return arg.canSuffocate(i, j - 1, k) || this.method_1826(arg, i, j, k) || arg.getTileId(i, j - 1, k) == BlockListener.fcStokedFire.id || arg.getTileId(i, j - 1, k) == BlockBase.FIRE.id;
    }

    @Override
    public void onAdjacentBlockUpdate(Level arg, int i, int j, int k, int l) {
        if (!arg.canSuffocate(i, j - 1, k) && !this.method_1826(arg, i, j, k) && arg.getTileId(i, j - 1, k) != BlockListener.fcStokedFire.id && arg.getTileId(i, j - 1, k) != BlockBase.FIRE.id) {
            arg.setTile(i, j, k, 0);
        }
    }

    @Override
    public void onBlockPlaced(Level arg, int i, int j, int k) {
        if (arg.getTileId(i, j - 1, k) != BlockBase.OBSIDIAN.id || !BlockBase.PORTAL.method_736(arg, i, j, k)) {
            if (!arg.canSuffocate(i, j - 1, k) && !this.method_1826(arg, i, j, k ) && arg.getTileId(i, j - 1, k) != BlockListener.fcStokedFire.id && arg.getTileId(i, j - 1, k) != BlockBase.FIRE.id) {
                arg.setTile(i, j, k, 0);
            } else {
                arg.method_216(i, j, k, this.id, this.getTickrate());
            }
        }
    }

    private boolean method_1826(Level arg, int i, int j, int k) {
        if (this.method_1824(arg, i + 1, j, k)) {
            return true;
        } else if (this.method_1824(arg, i - 1, j, k)) {
            return true;
        } else if (this.method_1824(arg, i, j - 1, k)) {
            return true;
        } else if (this.method_1824(arg, i, j + 1, k)) {
            return true;
        } else if (this.method_1824(arg, i, j, k - 1)) {
            return true;
        } else {
            return this.method_1824(arg, i, j, k + 1);
        }
    }


    //TODO: forge methods?
    public boolean isBlockReplaceable(Level world, int i, int j, int l)
    {
        return true;
    }

    public boolean isBlockBurning(Level world, int i, int j, int l)
    {
        return true;
    }

    // TODO: Replace this messy code. And make the top stoked fire visually connect better to the bottom block.
    @Override
    public boolean renderWorld(BlockRenderer tileRenderer, BlockView blockView, int i, int j, int k) {
        Tessellator var5 = Tessellator.INSTANCE;
        int var6 = this.getTextureForSide(0);
//        if (((BlockRendererAccessor)tileRenderer).getTextureOverride() >= 0) {
//            var6 = ((BlockRendererAccessor)tileRenderer).getTextureOverride();
//        }
        float var7 = this.getBrightness(blockView, i, j, k);
        var5.colour(var7, var7, var7);


//        int var8 = (var6 & 15) << 4;
//        int var9 = var6 & 240;
//        double var10 = (double)((float)var8 / 256.0F);
//        double var12 = (double)(((float)var8 + 15.99F) / 256.0F);
//        double var14 = (double)((float)var9 / 256.0F);
//        double var16 = (double)(((float)var9 + 15.99F) / 256.0F);

        Atlas.Sprite atlasTX =  Atlases.getTerrain().getTexture(var6);
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
            double var19 = (double)i + 0.5D + 0.2D;
            var21 = (double)i + 0.5D - 0.2D;
            var23 = (double)k + 0.5D + 0.2D;
            var25 = (double)k + 0.5D - 0.2D;
            var27 = (double)i + 0.5D - 0.3D;
            var29 = (double)i + 0.5D + 0.3D;
            var31 = (double)k + 0.5D - 0.3D;
            var33 = (double)k + 0.5D + 0.3D;
            var5.vertex(var27, (double)((float)j + var18), (double)(k + 1), var12, var14);
            var5.vertex(var19, (double)(j + 0), (double)(k + 1), var12, var16);
            var5.vertex(var19, (double)(j + 0), (double)(k + 0), var10, var16);
            var5.vertex(var27, (double)((float)j + var18), (double)(k + 0), var10, var14);
            var5.vertex(var29, (double)((float)j + var18), (double)(k + 0), var12, var14);
            var5.vertex(var21, (double)(j + 0), (double)(k + 0), var12, var16);
            var5.vertex(var21, (double)(j + 0), (double)(k + 1), var10, var16);
            var5.vertex(var29, (double)((float)j + var18), (double)(k + 1), var10, var14);

//            var10 = (double)((float)var8 / 256.0F);
//            var12 = (double)(((float)var8 + 15.99F) / 256.0F);
//            var14 = (double)((float)(var9 + 16) / 256.0F);
//            var16 = (double)(((float)var9 + 15.99F + 16.0F) / 256.0F);

            var10 = atlasTX.getStartU();
            var12 = atlasTX.getEndU();
            var14 = atlasTX.getStartV();
            var16 = atlasTX.getEndV();

            var5.vertex((double)(i + 1), (double)((float)j + var18), var33, var12, var14);
            var5.vertex((double)(i + 1), (double)(j + 0), var25, var12, var16);
            var5.vertex((double)(i + 0), (double)(j + 0), var25, var10, var16);
            var5.vertex((double)(i + 0), (double)((float)j + var18), var33, var10, var14);
            var5.vertex((double)(i + 0), (double)((float)j + var18), var31, var12, var14);
            var5.vertex((double)(i + 0), (double)(j + 0), var23, var12, var16);
            var5.vertex((double)(i + 1), (double)(j + 0), var23, var10, var16);
            var5.vertex((double)(i + 1), (double)((float)j + var18), var31, var10, var14);
            var19 = (double)i + 0.5D - 0.5D;
            var21 = (double)i + 0.5D + 0.5D;
            var23 = (double)k + 0.5D - 0.5D;
            var25 = (double)k + 0.5D + 0.5D;
            var27 = (double)i + 0.5D - 0.4D;
            var29 = (double)i + 0.5D + 0.4D;
            var31 = (double)k + 0.5D - 0.4D;
            var33 = (double)k + 0.5D + 0.4D;
            var5.vertex(var27, (double)((float)j + var18), (double)(k + 0), var10, var14);
            var5.vertex(var19, (double)(j + 0), (double)(k + 0), var10, var16);
            var5.vertex(var19, (double)(j + 0), (double)(k + 1), var12, var16);
            var5.vertex(var27, (double)((float)j + var18), (double)(k + 1), var12, var14);
            var5.vertex(var29, (double)((float)j + var18), (double)(k + 1), var10, var14);
            var5.vertex(var21, (double)(j + 0), (double)(k + 1), var10, var16);
            var5.vertex(var21, (double)(j + 0), (double)(k + 0), var12, var16);
            var5.vertex(var29, (double)((float)j + var18), (double)(k + 0), var12, var14);

//            var10 = (double)((float)var8 / 256.0F);
//            var12 = (double)(((float)var8 + 15.99F) / 256.0F);
//            var14 = (double)((float)var9 / 256.0F);
//            var16 = (double)(((float)var9 + 15.99F) / 256.0F);

            var10 = atlasTX.getStartU();
            var12 = atlasTX.getEndU();
            var14 = atlasTX.getStartV();
            var16 = atlasTX.getEndV();

            var5.vertex((double)(i + 0), (double)((float)j + var18), var33, var10, var14);
            var5.vertex((double)(i + 0), (double)(j + 0), var25, var10, var16);
            var5.vertex((double)(i + 1), (double)(j + 0), var25, var12, var16);
            var5.vertex((double)(i + 1), (double)((float)j + var18), var33, var12, var14);
            var5.vertex((double)(i + 1), (double)((float)j + var18), var31, var10, var14);
            var5.vertex((double)(i + 1), (double)(j + 0), var23, var10, var16);
            var5.vertex((double)(i + 0), (double)(j + 0), var23, var12, var16);
            var5.vertex((double)(i + 0), (double)((float)j + var18), var31, var12, var14);
        }

        return true;
    }
}
