package net.kozibrodka.wolves.blocks;

import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.util.Random;

public class FCBlockStokedFire extends TemplateBlockBase {
    public FCBlockStokedFire(Identifier identifier, Material material) {
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
        return 6;
    }
}
