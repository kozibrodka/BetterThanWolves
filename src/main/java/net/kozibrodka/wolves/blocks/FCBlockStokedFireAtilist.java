package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.mixin.BlockRendererAccessor;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.util.Random;

public class FCBlockStokedFireAtilist extends TemplateBlockBase implements BlockWithWorldRenderer{
    public FCBlockStokedFireAtilist(Identifier identifier, Material material) {
        super(identifier, material);
        setTranslationKey(identifier.modID, identifier.id);
        setTicksRandomly(true);
        setLightEmittance(1.0F);
    }

    @Override
    public void onScheduledTick(Level arg, int i, int j, int k, Random random) {
        if (arg.getTileMeta(i, j, k) >= 3) arg.setTile(i, j, k, 0);
        else arg.setTileMeta(i, j, k, arg.getTileMeta(i, j, k) + 1);
    }

    @Override
    public void onAdjacentBlockUpdate(Level arg, int i, int j, int k, int l) {
        super.onAdjacentBlockUpdate(arg, i, j, k, l);
        if (arg.getTileId(i, j - 1, k) != BlockBase.FIRE.id)
        {
            if (arg.isAir(i, j - 1, k) && arg.getTileId(i, j - 2, k) == mod_FCBetterThanWolves.fcBBQ.id && (arg.getTileMeta(i, j - 2, k) & 4) > 0) arg.setTile(i, j - 1, k, BlockBase.FIRE.id);
            else arg.setTile(i, j, k, 0);
        }
    }

    @Override
    public int getTextureForSide(int i) {
        return BlockBase.FIRE.texture;
    }

    @Override
    public boolean isFullOpaque() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public Box getCollisionShape(Level arg, int i, int j, int k) {
        return null;
    }

    @Override
    public int getRenderType() {
        // TODO: This is a workaround to make fire render on top of fire, replace with custom fire render in the future
        return 3;
    }

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
