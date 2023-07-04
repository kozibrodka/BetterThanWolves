package net.kozibrodka.wolves.utils;

import net.kozibrodka.wolves.mixin.BlockRendererAccessor;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;

public class FCUtilsRenderMoving {

//    public static void renderEastFace(BlockBase arg, double d, double e, double f, int i, BlockRenderer blockRenderer) {
//        Tessellator var9 = Tessellator.INSTANCE;
////        if (this.textureOverride >= 0) {
////            i = this.textureOverride;
////        }
//        FCUtilsRender.SetOverrideBlockTexture(blockRenderer,i);
//        Atlas.Sprite testTex =  Atlases.getTerrain().getTexture(i);
//
////        int var10 = (i & 15) << 4;
////        int var11 = i & 240;
////        double var10 = testTex.getStartU();
////        double var11 = testTex.getStartV();
//        double var12 = ((double)testTex.getStartU() + arg.minX * 16.0D) / 512.0D;
//        double var14 = ((double)testTex.getEndU() + arg.maxX * 16.0D - 0.01D) / 512.0D;
//        double var16 = ((double)(testTex.getStartV() + 16) - arg.maxY * 16.0D) / 512.0D;
//        double var18 = ((double)(testTex.getEndV() + 16) - arg.minY * 16.0D - 0.01D) / 512.0D;
//        double var20;
//        if (((BlockRendererAccessor)blockRenderer).getMirrorTexture()) {
//            var20 = var12;
//            var12 = var14;
//            var14 = var20;
//        }
//
//        if (arg.minX < 0.0D || arg.maxX > 1.0D) {
//            var12 = testTex.getStartU();
//            var14 = testTex.getEndU();
//        }
//
//        if (arg.minY < 0.0D || arg.maxY > 1.0D) {
//            var16 = testTex.getStartV();
//            var18 = testTex.getEndV();
//        }
//
//        var20 = var14;
//        double var22 = var12;
//        double var24 = var16;
//        double var26 = var18;
//        if (((BlockRendererAccessor)blockRenderer).getEastFaceRotation() == 2) {
//            var12 = testTex.getStartU();
//            var16 = testTex.getStartV();
//            var14 = testTex.getEndU();
//            var18 = testTex.getEndV();
//            var24 = var16;
//            var26 = var18;
//            var20 = var12;
//            var22 = var14;
//            var16 = var18;
//            var18 = var24;
//        } else if (((BlockRendererAccessor)blockRenderer).getEastFaceRotation() == 1) {
//            var12 = testTex.getStartU();
//            var16 = testTex.getStartV();
//            var14 = testTex.getEndU();
//            var18 = testTex.getEndV();
//            var20 = var14;
//            var22 = var12;
//            var12 = var14;
//            var14 = var22;
//            var24 = var18;
//            var26 = var16;
//        } else if (((BlockRendererAccessor)blockRenderer).getEastFaceRotation() == 3) {
//            var12 = testTex.getStartU();
//            var14 = testTex.getStartV();
//            var16 = testTex.getEndU();
//            var18 = testTex.getEndV();
//            var20 = var14;
//            var22 = var12;
//            var24 = var16;
//            var26 = var18;
//        }
//
//        double var28 = d + arg.minX;
//        double var30 = d + arg.maxX;
//        double var32 = e + arg.minY;
//        double var34 = e + arg.maxY;
//        double var36 = f + arg.minZ;
//        if (((BlockRendererAccessor)blockRenderer).getShadeTopFace()) {
//            var9.colour(((BlockRendererAccessor)blockRenderer).getColourRed00(), ((BlockRendererAccessor)blockRenderer).getColourRed00(), ((BlockRendererAccessor)blockRenderer).getColourRed00());
//            var9.vertex(var28, var34, var36, var20, var24);
//            var9.colour(((BlockRendererAccessor)blockRenderer).getColourRed01(), ((BlockRendererAccessor)blockRenderer).getColourRed01(), ((BlockRendererAccessor)blockRenderer).getColourRed01());
//            var9.vertex(var30, var34, var36, var12, var16);
//            var9.colour(((BlockRendererAccessor)blockRenderer).getColourRed10(), ((BlockRendererAccessor)blockRenderer).getColourRed10(), ((BlockRendererAccessor)blockRenderer).getColourRed10());
//            var9.vertex(var30, var32, var36, var22, var26);
//            var9.colour(((BlockRendererAccessor)blockRenderer).getColurRed11(), ((BlockRendererAccessor)blockRenderer).getColurRed11(), ((BlockRendererAccessor)blockRenderer).getColurRed11());
//            var9.vertex(var28, var32, var36, var14, var18);
//        } else {
//            var9.vertex(var28, var34, var36, var20, var24);
//            var9.vertex(var30, var34, var36, var12, var16);
//            var9.vertex(var30, var32, var36, var22, var26);
//            var9.vertex(var28, var32, var36, var14, var18);
//        }
//
//    }
}
