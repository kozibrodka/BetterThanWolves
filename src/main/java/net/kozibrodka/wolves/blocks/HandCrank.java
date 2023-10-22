
package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.MechanicalDevice;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.Vec3f;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.util.Random;


public class HandCrank extends TemplateBlockBase
    implements MechanicalDevice, BlockWithWorldRenderer, BlockWithInventoryRenderer
{

    public HandCrank(Identifier iid)
    {
        super(iid, Material.DOODADS);
        setHardness(0.5F);
        setSounds(WOOD_SOUNDS);
        setTicksRandomly(true);
    }

    public int getTickrate()
    {
        return iHandCrankTickRate;
    }

    public int getTextureForSide(int iSide)
    {
        if(iSide == 1)
        {
            return TextureListener.handcrack_top;
        }
        if(iSide == 0)
        {
            return TextureListener.handcrack_bottom;
        } else
        {
            return TextureListener.handcrack_side;
        }
    }

    public Box getCollisionShape(Level world, int i, int j, int k)
    {
        return Box.createButWasteMemory((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + fHandCrankBaseHeight, (float)k + 1.0F);
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public void updateBoundingBox(BlockView iBlockAccess, int i, int j, int k)
    {
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public boolean canPlaceAt(Level world, int i, int j, int k)
    {
        return world.isFullOpaque(i, j - 1, k);
    }

    public void activate(Level world, int i, int j, int k, PlayerBase entityplayer)
    {
        canUse(world, i, j, k, entityplayer);
    }

    public boolean canUse(Level world, int i, int j, int k, PlayerBase entityPlayer)
    {
        if(world.isServerSide)
        {
            return true;
        }
        int iMetaData = world.getTileMeta(i, j, k);
        if(iMetaData == 0)
        {
            if(!CheckForOverpower(world, i, j, k))
            {
                world.setTileMeta(i, j, k, 1);
                world.method_202(i, j, k, i, j, k);
                world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.click", 1.0F, 2.0F);
                world.updateAdjacentBlocks(i, j, k, id);
                world.method_216(i, j, k, id, getTickrate());
            } else
            {
                BreakCrankWithDrop(world, i, j, k);
            }
        }
        return true;
    }

    public void onScheduledTick(Level world, int i, int j, int k, Random random)
    {
        int iMetaData = world.getTileMeta(i, j, k);
        if(iMetaData > 0)
        {
            if(iMetaData < 7)
            {
                if(iMetaData <= 6)
                {
                    world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.click", 1.0F, 2.0F);
                }
                if(iMetaData <= 5)
                {
                    world.method_216(i, j, k, id, getTickrate() + iMetaData);
                } else
                {
                    world.method_216(i, j, k, id, iHandCrankDelayBeforeReset);
                }
                world.setTileMeta(i, j, k, iMetaData + 1);
            } else
            {
                world.setTileMeta(i, j, k, 0);
                world.method_202(i, j, k, i, j, k);
                world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.click", 0.3F, 0.7F);
            }
        }
    }

    public void onAdjacentBlockUpdate(Level world, int i, int j, int k, int iid)
    {
        if(!world.isFullOpaque(i, j - 1, k))
        {
            drop(world, i, j, k, world.getTileMeta(i, j, k));
            world.setTile(i, j, k, 0);
        }
    }

    public boolean CanOutputMechanicalPower()
    {
        return true;
    }

    public boolean CanInputMechanicalPower()
    {
        return false;
    }

    public boolean IsInputtingMechanicalPower(Level world, int i, int j, int l)
    {
        return false;
    }

    public boolean IsOutputtingMechanicalPower(Level world, int i, int j, int k)
    {
        return world.getTileMeta(i, j, k) > 0;
    }

    public boolean CheckForOverpower(Level world, int i, int j, int k)
    {
        int iNumPotentialDevicesToPower = 0;
        for(int iTempFacing = 2; iTempFacing <= 5; iTempFacing++)
        {
            BlockPosition tempPos = new BlockPosition(i, j, k);
            tempPos.AddFacingAsOffset(iTempFacing);
            int iTempid = world.getTileId(tempPos.i, tempPos.j, tempPos.k);
            BlockBase tempBlock = BlockBase.BY_ID[iTempid];
            if(tempBlock == null || !(tempBlock instanceof MechanicalDevice))
            {
                continue;
            }
            MechanicalDevice tempDevice = (MechanicalDevice)tempBlock;
            if(tempDevice.CanInputMechanicalPower())
            {
                iNumPotentialDevicesToPower++;
            }
        }

        return iNumPotentialDevicesToPower > 1;
    }

    public void BreakCrankWithDrop(Level world, int i, int j, int k)
    {
        UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, ItemBase.stick.id, 0);
        UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, BlockBase.COBBLESTONE.id, 0);
        UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, BlockBase.COBBLESTONE.id, 0);
        UnsortedUtils.EjectSingleItemWithRandomOffset(world, i, j, k, ItemListener.gear.id, 0);
        world.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.explode", 0.2F, 1.25F);
        world.setTile(i, j, k, 0);
    }


    //TODO: render this correctly
    @Override
    public boolean renderWorld(BlockRenderer tileRenderer, BlockView tileView, int x, int y, int z) {
        Tessellator tessellator = Tessellator.INSTANCE;
        float f = 0.5F;
        float f1 = 0.5F;
        float f2 = fHandCrankBaseHeight;
        this.setBoundingBox(0.5F - f1, 0.0F, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
        tileRenderer.renderStandardBlock(this, x, y, z);
        float f3 = this.getBrightness(tileView, x, y, z);
        if(BlockBase.EMITTANCE[this.id] > 0)
        {
            f3 = 1.0F;
        }
        tessellator.colour(f3, f3, f3);
        int l = TextureListener.handcrack_lever;
//        if(FCUtilsRender.GetOverrideBlockTexture(tileRenderer) >= 0)
//        {
//            l = FCUtilsRender.GetOverrideBlockTexture(tileRenderer);
//        }
//        int i1 = (l & 0xf) << 4;
//        int j1 = l & 0xf0;
        Atlas.Sprite testTex =  Atlases.getTerrain().getTexture(l);
//        float f4 = (float)i1 / 256F;
//        float f5 = ((float)i1 + 15.99F) / 256F;
//        float f6 = (float)j1 / 256F;
//        float f7 = ((float)j1 + 15.99F) / 256F;
        float f4 = (float)(testTex.getStartU());
        float f5 = (float)(testTex.getEndU());
        float f6 = (float)(testTex.getStartV());
        float f7 = (float)(testTex.getEndV());

        Vec3f avec3d[] = new Vec3f[8];
        float f8 = 0.0625F;
        float f9 = 0.0625F;
        float f10 = 0.9F;
        avec3d[0] = Vec3f.from(-f8, 0.0D, -f9);
        avec3d[1] = Vec3f.from(f8, 0.0D, -f9);
        avec3d[2] = Vec3f.from(f8, 0.0D, f9);
        avec3d[3] = Vec3f.from(-f8, 0.0D, f9);
        avec3d[4] = Vec3f.from(-f8, f10, -f9);
        avec3d[5] = Vec3f.from(f8, f10, -f9);
        avec3d[6] = Vec3f.from(f8, f10, f9);
        avec3d[7] = Vec3f.from(-f8, f10, f9);
        boolean flag = tileView.getTileMeta(x, y, z) > 0;
        for(int k1 = 0; k1 < 8; k1++)
        {
            if(flag)
            {
                avec3d[k1].z -= 0.0625D;
                avec3d[k1].method_1306(0.35F);
            } else
            {
                avec3d[k1].z += 0.0625D;
                avec3d[k1].method_1306(-0.35F);
            }
            avec3d[k1].method_1308(1.570796F);
            avec3d[k1].x += (double)x + 0.5D;
            avec3d[k1].y += (float)y + 0.125F;
            avec3d[k1].z += (double)z + 0.5D;
        }

        Vec3f vec3d = null;
        Vec3f vec3d1 = null;
        Vec3f vec3d2 = null;
        Vec3f vec3d3 = null;
        for(int l1 = 0; l1 < 6; l1++)
        {
            if(l1 == 0)
            {
//                f4 = (float)(i1 + 7) / 256F;
//                f5 = ((float)(i1 + 9) - 0.01F) / 256F;
//                f6 = (float)(j1 + 0) / 256F;
//                f7 = ((float)(j1 + 2) - 0.01F) / 256F;
                f4 = (float)(testTex.getStartU() + (7/512F));
                f5 = (float)(testTex.getEndU() - (7/512F));
                f6 = (float)(testTex.getStartV());
                f7 = (float)(testTex.getEndV() - (14/512F));

            } else
            if(l1 == 2)
            {
//                f4 = (float)(i1 + 7) / 256F;
//                f5 = ((float)(i1 + 9) - 0.01F) / 256F;
//                f6 = (float)(j1 + 0) / 256F;
//                f7 = ((float)(j1 + 16) - 0.01F) / 256F;
                f4 = (float)(testTex.getStartU() + (7/512F));
                f5 = (float)(testTex.getEndU() - (7/512F));
                f6 = (float)(testTex.getStartV());
                f7 = (float)(testTex.getEndV());
            }
            if(l1 == 0)
            {
                vec3d = avec3d[0];
                vec3d1 = avec3d[1];
                vec3d2 = avec3d[2];
                vec3d3 = avec3d[3];
            } else
            if(l1 == 1)
            {
                vec3d = avec3d[7];
                vec3d1 = avec3d[6];
                vec3d2 = avec3d[5];
                vec3d3 = avec3d[4];
            } else
            if(l1 == 2)
            {
                vec3d = avec3d[1];
                vec3d1 = avec3d[0];
                vec3d2 = avec3d[4];
                vec3d3 = avec3d[5];
            } else
            if(l1 == 3)
            {
                vec3d = avec3d[2];
                vec3d1 = avec3d[1];
                vec3d2 = avec3d[5];
                vec3d3 = avec3d[6];
            } else
            if(l1 == 4)
            {
                vec3d = avec3d[3];
                vec3d1 = avec3d[2];
                vec3d2 = avec3d[6];
                vec3d3 = avec3d[7];
            } else
            if(l1 == 5)
            {
                vec3d = avec3d[0];
                vec3d1 = avec3d[3];
                vec3d2 = avec3d[7];
                vec3d3 = avec3d[4];
            }
            tessellator.vertex(vec3d.x, vec3d.y, vec3d.z, f4, f7);
            tessellator.vertex(vec3d1.x, vec3d1.y, vec3d1.z, f5, f7);
            tessellator.vertex(vec3d2.x, vec3d2.y, vec3d2.z, f5, f6);
            tessellator.vertex(vec3d3.x, vec3d3.y, vec3d3.z, f4, f6);

        }

        return true;
    }

    private static int iHandCrankTickRate = 3;
    private static int iHandCrankDelayBeforeReset = 15;
    public static float fHandCrankBaseHeight = 0.25F;
    private final int iHandCrankShaftTextureIndex = 28;
    private final int iHandCrankBaseTopTextureIndex = 29;
    private final int iHandCrankBaseSideTextureIndex = 30;
    private final int iHandCrankBaseBottomTextureIndex = 31;

    @Override
    public void renderInventory(BlockRenderer tileRenderer, int meta) {
        float f = 0.5F;
        float f1 = 0.5F;
        float f2 = fHandCrankBaseHeight;
        this.setBoundingBox(0.5F - f1, 0.0F, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.5F, -0.5F, 0);
        f = 0.0625F;
        f1 = 0.0625F;
        f2 = 1.0F;
        this.setBoundingBox(0.5F - f1, 0.0F, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.handcrack_lever);
    }
}
