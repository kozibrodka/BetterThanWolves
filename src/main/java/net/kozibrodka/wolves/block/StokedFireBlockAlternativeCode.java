package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.EnvironmentInterface;
import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

@EnvironmentInterface(value= EnvType.CLIENT, itf=BlockWithWorldRenderer.class)
public class StokedFireBlockAlternativeCode extends TemplateBlock implements BlockWithWorldRenderer{
    public StokedFireBlockAlternativeCode(Identifier identifier, Material material) {
        super(identifier, material);
//        setTranslationKey(identifier.modID, identifier.id); This goes at register event
        setTickRandomly(true);
        setLuminance(1.0F);
    }

    @Override
    public void onTick(World arg, int i, int j, int k, Random random) {
        if (arg.getBlockMeta(i, j, k) >= 3) arg.setBlock(i, j, k, 0);
        else arg.setBlockMeta(i, j, k, arg.getBlockMeta(i, j, k) + 1);
    }

    @Override
    public void neighborUpdate(World arg, int i, int j, int k, int l) {
        super.neighborUpdate(arg, i, j, k, l);
        if (arg.getBlockId(i, j - 1, k) != Block.FIRE.id)
        {
            if (arg.isAir(i, j - 1, k) && arg.getBlockId(i, j - 2, k) == BlockListener.hibachi.id && (arg.getBlockMeta(i, j - 2, k) & 4) > 0) arg.setBlock(i, j - 1, k, Block.FIRE.id);
            else arg.setBlock(i, j, k, 0);
        }
    }

    @Override
    public int getTexture(int i) {
        return Block.FIRE.textureId;
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean hasCollision() {
        return false;
    }

    @Override
    public Box getCollisionShape(World arg, int i, int j, int k) {
        return null;
    }

    @Override
    public int getRenderType() {
        // TODO: This is a workaround to make fire render on top of fire, replace with custom fire render in the future
        return 3;
    }

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
