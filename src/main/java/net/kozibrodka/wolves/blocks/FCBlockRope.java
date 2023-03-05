package net.kozibrodka.wolves.blocks;

import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.utils.FCUtilsMisc;
import net.kozibrodka.wolves.utils.FCUtilsRender;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.util.Random;


public class FCBlockRope extends TemplateBlockBase implements BlockWithWorldRenderer
{

    public FCBlockRope(Identifier iid)
    {
        super(iid, Material.DOODADS);
        setHardness(0.5F);
        setSounds(GRASS_SOUNDS);
    }

    public int getTextureForSide(int iSide)
    {
        return TextureListener.rope;
    }

    public int getDropId(int i, Random random)
    {
        return mod_FCBetterThanWolves.fcRopeItem.id;
    }

    public void onAdjacentBlockUpdate(Level world, int i, int j, int k, int iid)
    {
        int iBlockAboveID = world.getTileId(i, j + 1, k);
        if(iBlockAboveID != id && iBlockAboveID != mod_FCBetterThanWolves.fcAnchor.id && iBlockAboveID != mod_FCBetterThanWolves.fcPulley.id)
        {
            drop(world, i, j, k, world.getTileMeta(i, j, k));
            world.setTile(i, j, k, 0);
        }
    }

    public boolean canPlaceAt(Level world, int i, int j, int k)
    {
        int iBlockAboveID = world.getTileId(i, j + 1, k);
        return iBlockAboveID == id || iBlockAboveID == mod_FCBetterThanWolves.fcAnchor.id;
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public Box getCollisionShape(Level world, int i, int j, int k)
    {
        return Box.createButWasteMemory(((float)i + 0.5F) - 0.0625F, (float)j, ((float)k + 0.5F) - 0.0625F, (float)i + 0.5F + 0.0625F, (float)j + 1.0F, (float)k + 0.5F + 0.0625F);
    }

    public void updateBoundingBox(BlockView iBlockAccess, int i, int j, int k)
    {
        setBoundingBox(0.4375F, 0.0F, 0.4375F, 0.5625F, 1.0F, 0.5625F);
    }

    //TODO
    public boolean isLadder()
    {
        return true;
    }

    public void BreakRope(Level world, int i, int j, int k)
    {
        FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, mod_FCBetterThanWolves.fcRopeItem.id, 0);
        Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).soundHelper.playSound(sounds.getWalkSound(), (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, (sounds.getVolume() + 1.0F) / 2.0F, sounds.getPitch() * 0.8F);
        world.setTile(i, j, k, 0);
    }

    public static final float fRopeWidth = 0.125F;

    @Override
    public boolean renderWorld(BlockRenderer tileRenderer, BlockView tileView, int x, int y, int z) {
        boolean flag = FCUtilsRender.GetOverrideBlockTexture(tileRenderer) >= 0;
        int l = this.getTextureForSide(0);
        if(!flag)
        {
            FCUtilsRender.SetOverrideBlockTexture(tileRenderer, l);
        }
        float f = 0.0625F;
        float f1 = 0.0625F;
        float f2 = 1.0F;
        this.setBoundingBox(0.5F - f1, 0.0F, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
        tileRenderer.renderStandardBlock(this, x,y,z);
        if(!flag)
        {
            FCUtilsRender.SetOverrideBlockTexture(tileRenderer, -1);
        }
        return true;
    }
}
