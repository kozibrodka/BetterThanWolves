// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCBlockAnvil.java

package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.EnvironmentInterface;
import net.kozibrodka.wolves.container.AnvilScreenHandler;
import net.kozibrodka.wolves.entity.FallingAnvilEntity;
import net.kozibrodka.wolves.events.ScreenHandlerListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

@EnvironmentInterface(value= EnvType.CLIENT, itf=BlockWithWorldRenderer.class)
@EnvironmentInterface(value=EnvType.CLIENT, itf=BlockWithInventoryRenderer.class)
public class AnvilBlock extends TemplateBlock
    implements RotatableBlock, BlockWithWorldRenderer, BlockWithInventoryRenderer {

    public AnvilBlock(Identifier iid) {
        super(iid, Material.METAL);
        setHardness(3.5F);
        setSoundGroup(METAL_SOUND_GROUP);
    }

    public int getTexture(int iSide) {
        return TextureListener.anvil;
    }

    public boolean isOpaque() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public void onPlaced(World world, int i, int j, int k, int iFacing) {
        if(iFacing < 2)
        {
            iFacing = 2;
        } else
        {
            iFacing = UnsortedUtils.getOppositeFacing(iFacing);
        }
        SetFacing(world, i, j, k, iFacing);
        world.scheduleBlockUpdate(i, j, k, this.id, this.getTickRate());
    }

    public void onPlaced(World world, int i, int j, int k, LivingEntity entityLiving) {
        world.playSound(i,j,k, "wolves:anvil_place", 1.0F, 1.2F);
        int iFacing = UnsortedUtils.ConvertPlacingEntityOrientationToFlatBlockFacing(entityLiving);
        SetFacing(world, i, j, k, iFacing);
    }

    public boolean onUse(World world, int i, int j, int k, PlayerEntity entityplayer) {
        ScreenHandlerListener.TempAnvilX = i;
        ScreenHandlerListener.TempAnvilY = j;
        ScreenHandlerListener.TempAnvilZ = k;
        GuiHelper.openGUI(entityplayer, Identifier.of("wolves:openAnvil"), entityplayer.inventory, new AnvilScreenHandler(entityplayer.inventory, world, i, j, k));
        return true;
    }

    public Box getCollisionShape(World world, int i, int j, int k) {
        int iFacing = GetFacing(world, i, j, k);
        if(iFacing == 2 || iFacing == 3) {
            return Box.createCached(((float)i + 0.5F) - 0.25F, (float)j, (float)k, (float)i + 0.5F + 0.25F, (float)j + 1.0F, (float)k + 1.0F);
        } else {
            return Box.createCached((float)i, (float)j, ((float)k + 0.5F) - 0.25F, (float)i + 1.0F, (float)j + 1.0F, (float)k + 0.5F + 0.25F);
        }
    }

    public void updateBoundingBox(BlockView iblockaccess, int i, int j, int k) {
        int iFacing = GetFacing(iblockaccess, i, j, k);
        if(iFacing == 2 || iFacing == 3) {
            setBoundingBox(0.25F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
        } else {
            setBoundingBox(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 0.75F);
        }
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int k) {
        return iBlockAccess.getBlockMeta(i, j, k);
    }

    public void SetFacing(World world, int i, int j, int k, int iFacing) {
        world.setBlockMeta(i, j, k, iFacing);
    }

    public boolean CanRotate(BlockView iBlockAccess, int i, int j, int l) {
        return true;
    }

    public boolean CanTransmitRotation(BlockView iBlockAccess, int i, int j, int l) {
        return true;
    }

    public void Rotate(World world, int i, int j, int k, boolean bReverse) {
        int iFacing = GetFacing(world, i, j, k);
        int iNewFacing = UnsortedUtils.RotateFacingAroundJ(iFacing, bReverse);
        if(iNewFacing != iFacing) {
            SetFacing(world, i, j, k, iNewFacing);
            world.setBlocksDirty(i, j, k, i, j, k);
            world.scheduleBlockUpdate(i, j, k, id, getTickRate());
            world.blockUpdate(i, j, j, id);
        }
    }

    public void SetBlockBoundsRotatedAboutJToFacing(float x1, float y1, float z1, float x2, float y2, float z2, int iFacing)
    {
        float rotatedX1;
        float rotatedZ1;
        float rotatedX2;
        float rotatedZ2;
        if(iFacing == 4)
        {
            rotatedX1 = 1.0F - x2;
            rotatedZ1 = 1.0F - z2;
            rotatedX2 = 1.0F - x1;
            rotatedZ2 = 1.0F - z1;
        } else
        if(iFacing == 3)
        {
            rotatedX1 = z1;
            rotatedZ1 = x1;
            rotatedX2 = z2;
            rotatedZ2 = x2;
        } else
        if(iFacing == 2)
        {
            rotatedX1 = 1.0F - z2;
            rotatedZ1 = 1.0F - x2;
            rotatedX2 = 1.0F - z1;
            rotatedZ2 = 1.0F - x1;
        } else
        {
            rotatedX1 = x1;
            rotatedZ1 = z1;
            rotatedX2 = x2;
            rotatedZ2 = z2;
        }
        setBoundingBox(rotatedX1, y1, rotatedZ1, rotatedX2, y2, rotatedZ2);
    }

    public void neighborUpdate(World arg, int i, int j, int k, int l) {
        arg.scheduleBlockUpdate(i, j, k, this.id, this.getTickRate());
    }

    public void onTick(World arg, int i, int j, int k, Random random) {
        this.method_436(arg, i, j, k);
    }

    private void method_436(World arg, int i, int j, int k) {
        if (method_435(arg, i, j - 1, k) && j >= 0) {
            int facing = arg.getBlockMeta(i,j,k);
            byte var8 = 32;
            if (!fallInstantly && arg.isRegionLoaded(i - var8, j - var8, k - var8, i + var8, j + var8, k + var8)) {
                FallingAnvilEntity var9 = new FallingAnvilEntity(arg, (double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), this.id, facing);
                arg.spawnEntity(var9);
            } else {
                arg.setBlock(i, j, k, 0);

                while(method_435(arg, i, j - 1, k) && j > 0) {
                    --j;
                }

                if (j > 0) {
                    arg.setBlock(i, j, k, this.id, facing);
                }
            }
        }

    }

    public static boolean method_435(World arg, int i, int j, int k) {
        int var4 = arg.getBlockId(i, j, k);
        if (var4 == 0) {
            return true;
        } else if (var4 == Block.FIRE.id) {
            return true;
        } else {
            Material var5 = Block.BLOCKS[var4].material;
            if (var5 == Material.WATER) {
                return true;
            } else {
                return var5 == Material.LAVA;
            }
        }
    }
    public int getTickRate() {
        return 3;
    }

    public static boolean fallInstantly = false;

    @Override
    public boolean renderWorld(BlockRenderManager tileRenderer, BlockView tileView, int x, int y, int z) {
        int l = GetFacing(tileView, x, y, z);
        SetBlockBoundsRotatedAboutJToFacing(0.0F, 0.0F, 0.25F, 1.0F, 0.125F, 0.75F, l);
        tileRenderer.renderBlock(this, x, y, z);
        SetBlockBoundsRotatedAboutJToFacing(0.375F, 0.125F, 0.375F, 0.625F, 0.5625F, 0.625F, l);
        tileRenderer.renderBlock(this, x, y, z);
        SetBlockBoundsRotatedAboutJToFacing(0.3125F, 0.5625F, 0.3125F, 0.6875F, 1.0F, 0.6875F, l);
        tileRenderer.renderBlock(this, x, y, z);
        SetBlockBoundsRotatedAboutJToFacing(0.6875F, 0.75F, 0.3125F, 0.75F, 1.0F, 0.6875F, l);
        tileRenderer.renderBlock(this, x, y, z);
        SetBlockBoundsRotatedAboutJToFacing(0.75F, 0.9375F, 0.3125F, 1.0F, 1.0F, 0.6875F, l);
        tileRenderer.renderBlock(this, x, y, z);
        SetBlockBoundsRotatedAboutJToFacing(0.25F, 0.75F, 0.3125F, 0.3125F, 1.0F, 0.6875F, l);
        tileRenderer.renderBlock(this, x, y, z);
        SetBlockBoundsRotatedAboutJToFacing(0.125F, 0.8125F, 0.375F, 0.25F, 1.0F, 0.625F, l);
        tileRenderer.renderBlock(this, x, y, z);
        SetBlockBoundsRotatedAboutJToFacing(0.0F, 0.875F, 0.4375F, 0.125F, 1.0F, 0.5625F, l);
        tileRenderer.renderBlock(this, x, y, z);
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }

    @Override
    public void renderInventory(BlockRenderManager tileRenderer, int meta) {

        setBoundingBox(0.0F, 0.0F, 0.25F, 1.0F, 0.125F, 0.75F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.anvil);
        setBoundingBox(0.375F, 0.125F, 0.375F, 0.625F, 0.5625F, 0.625F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.anvil);
        setBoundingBox(0.3125F, 0.5625F, 0.3125F, 0.6875F, 1.0F, 0.6875F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.anvil);
        setBoundingBox(0.6875F, 0.75F, 0.3125F, 0.75F, 1.0F, 0.6875F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.anvil);
        setBoundingBox(0.75F, 0.9375F, 0.3125F, 1.0F, 1.0F, 0.6875F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.anvil);
        setBoundingBox(0.25F, 0.75F, 0.3125F, 0.3125F, 1.0F, 0.6875F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.anvil);
        setBoundingBox(0.125F, 0.8125F, 0.375F, 0.25F, 1.0F, 0.625F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.anvil);
        setBoundingBox(0.0F, 0.875F, 0.4375F, 0.125F, 1.0F, 0.5625F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.anvil);
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public static final float m_fAnvilBaseHeight = 0.125F;
    public static final float m_fAnvilBaseWidth = 0.5F;
    public static final float m_fAnvilHalfBaseWidth = 0.25F;
    public static final float m_fAnvilShaftHeight = 0.4375F;
    public static final float m_fAnvilShaftWidth = 0.25F;
    public static final float m_fAnvilHalfShaftWidth = 0.125F;
    public static final float m_fAnvilTopHeight = 0.4375F;
    public static final float m_fAnvilTopWidth = 0.375F;
    public static final float m_fAnvilTopHalfWidth = 0.1875F;
}
