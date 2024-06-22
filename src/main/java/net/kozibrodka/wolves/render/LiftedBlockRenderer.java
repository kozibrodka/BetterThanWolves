// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.kozibrodka.wolves.render;

import net.kozibrodka.wolves.entity.LiftedBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.RailBlock;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.lwjgl.opengl.GL11;

public class LiftedBlockRenderer extends EntityRenderer
{

    public LiftedBlockRenderer()
    {
        shadowOpacity = 0.0F;
    }

    public void render(Entity entity, double d, double d1, double d2,
                       float f, float f1)
    {
        World world = entity.world;
        LiftedBlockEntity fcentityblockliftedbyplatform = (LiftedBlockEntity)entity;
        int i = fcentityblockliftedbyplatform.m_iid;
        int j = fcentityblockliftedbyplatform.m_iBlockMetaData;
        Block block = Block.BLOCKS[i];
        if(block != null)
        {
            if(block instanceof RailBlock)
            {
                RailBlock blockrail = (RailBlock)block;
                RenderBlockMinecartTrack(world, fcentityblockliftedbyplatform, blockrail, j, d, d1, d2);
            } else
            if(block instanceof RedstoneWireBlock)
            {
                RedstoneWireBlock blockredstonewire = (RedstoneWireBlock)block;
                RenderBlockRedstoneWire(world, fcentityblockliftedbyplatform, blockredstonewire, j, d, d1, d2);
            }
        }
    }

    public boolean RenderBlockRedstoneWire(World world, LiftedBlockEntity fcentityblockliftedbyplatform, RedstoneWireBlock blockredstonewire, int i, double d, double d1, double d2)
    {
        GL11.glPushMatrix();
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        bindTexture("/terrain.png");
        Tessellator tessellator = Tessellator.INSTANCE;
        tessellator.startQuads();
        int j = MathHelper.floor(fcentityblockliftedbyplatform.x);
        int k = MathHelper.floor(fcentityblockliftedbyplatform.y);
        int l = MathHelper.floor(fcentityblockliftedbyplatform.z);
        float f = blockredstonewire.getLuminance(world, j, k, l);
        float f1 = blockredstonewire.getLuminance(world, j, k - 1, l);
        if(f1 > f)
        {
            f = f1;
        }
        tessellator.color(0.4F * f, 0.0F, 0.0F);
        int i1 = blockredstonewire.getTexture(0, i);
        Atlas.Sprite atlasTex =  Atlases.getTerrain().getTexture(i1);
//        int j1 = (i1 & 0xf) << 4;
//        int k1 = i1 & 0xf0;
        double d3 = atlasTex.getStartU();
        double d4 = atlasTex.getEndU();
        double d5 = atlasTex.getStartV();
        double d6 = atlasTex.getEndV();
        float f2 = 0.015625F;
        float f3 = (float)(d + 0.5D);
        float f4 = (float)(d + 0.5D);
        float f5 = (float)(d - 0.5D);
        float f6 = (float)(d - 0.5D);
        float f7 = (float)(d2 - 0.5D);
        float f8 = (float)(d2 + 0.5D);
        float f9 = (float)(d2 + 0.5D);
        float f10 = (float)(d2 - 0.5D);
        float f11 = (float)(d1 - 0.5D) + f2;
        float f12 = (float)(d1 - 0.5D) + f2;
        float f13 = (float)(d1 - 0.5D) + f2;
        float f14 = (float)(d1 - 0.5D) + f2;
        tessellator.vertex(f3, f11, f7, d4, d5);
        tessellator.vertex(f4, f12, f8, d4, d6);
        tessellator.vertex(f5, f13, f9, d3, d6);
        tessellator.vertex(f6, f14, f10, d3, d5);
        tessellator.vertex(f6, f14, f10, d3, d5);
        tessellator.vertex(f5, f13, f9, d3, d6);
        tessellator.vertex(f4, f12, f8, d4, d6);
        tessellator.vertex(f3, f11, f7, d4, d5);
        tessellator.draw();
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glPopMatrix();
        return true;
    }

    public boolean RenderBlockMinecartTrack(World world, LiftedBlockEntity fcentityblockliftedbyplatform, RailBlock blockrail, int i, double d, double d1, double d2)
    {
        GL11.glPushMatrix();
        GL11.glDisable(2896 /*GL_LIGHTING*/);
//        if(blockrail instanceof FCFakeTextureProvider) //UWAGA
//        {
//            bindTexture("/assets/wolves/stationapi/textures/entity/btwterrain01.png");
//        } else
//        {
//            bindTexture("/terrain.png");
//        }
        bindTexture("/terrain.png");
        Tessellator tessellator = Tessellator.INSTANCE;
        tessellator.startQuads();
        int j = MathHelper.floor(fcentityblockliftedbyplatform.x);
        int k = MathHelper.floor(fcentityblockliftedbyplatform.y);
        int l = MathHelper.floor(fcentityblockliftedbyplatform.z);
        float f = blockrail.getLuminance(world, j, k, l);
        float f1 = blockrail.getLuminance(world, j, k - 1, l);
        if(f1 > f)
        {
            f = f1;
        }
        tessellator.color(f, f, f);
        int i1 = blockrail.getTexture(0, i);
        Atlas.Sprite atlasTex =  Atlases.getTerrain().getTexture(i1);
        if(blockrail.method_1108()) //getIsPowered
        {
            i &= 7;
        }
//        int j1 = (i1 & 0xf) << 4;
//        int k1 = i1 & 0xf0;
        double d3 = atlasTex.getStartU();
        double d4 = atlasTex.getEndU();
        double d5 = atlasTex.getStartV();
        double d6 = atlasTex.getEndV();
        float f2 = 0.0625F;
        float f3 = (float)(d + 0.5D);
        float f4 = (float)(d + 0.5D);
        float f5 = (float)(d - 0.5D);
        float f6 = (float)(d - 0.5D);
        float f7 = (float)(d2 - 0.5D);
        float f8 = (float)(d2 + 0.5D);
        float f9 = (float)(d2 + 0.5D);
        float f10 = (float)(d2 - 0.5D);
        float f11 = (float)(d1 - 0.5D) + f2;
        float f12 = (float)(d1 - 0.5D) + f2;
        float f13 = (float)(d1 - 0.5D) + f2;
        float f14 = (float)(d1 - 0.5D) + f2;
        if(i == 1 || i == 2 || i == 3 || i == 7)
        {
            f3 = f6 = (float)(d + 0.5D);
            f4 = f5 = (float)(d - 0.5D);
            f7 = f8 = (float)(d2 + 0.5D);
            f9 = f10 = (float)(d2 - 0.5D);
        } else
        if(i == 8)
        {
            f3 = f4 = (float)(d - 0.5D);
            f5 = f6 = (float)(d + 0.5D);
            f7 = f10 = (float)(d2 + 0.5D);
            f8 = f9 = (float)(d2 - 0.5D);
        } else
        if(i == 9)
        {
            f3 = f6 = (float)(d - 0.5D);
            f4 = f5 = (float)(d + 0.5D);
            f7 = f8 = (float)(d2 - 0.5D);
            f9 = f10 = (float)(d2 + 0.5D);
        }
        if(i == 2 || i == 4)
        {
            f11++;
            f14++;
        } else
        if(i == 3 || i == 5)
        {
            f12++;
            f13++;
        }
        tessellator.vertex(f3, f11, f7, d4, d5);
        tessellator.vertex(f4, f12, f8, d4, d6);
        tessellator.vertex(f5, f13, f9, d3, d6);
        tessellator.vertex(f6, f14, f10, d3, d5);
        tessellator.vertex(f6, f14, f10, d3, d5);
        tessellator.vertex(f5, f13, f9, d3, d6);
        tessellator.vertex(f4, f12, f8, d4, d6);
        tessellator.vertex(f3, f11, f7, d4, d5);
        tessellator.draw();
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glPopMatrix();
        return true;
    }
}
