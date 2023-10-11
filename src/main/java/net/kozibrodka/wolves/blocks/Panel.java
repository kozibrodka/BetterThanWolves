package net.kozibrodka.wolves.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.mixin.BlockBaseAccessor;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.MathHelper;
import net.minecraft.util.maths.Vec3f;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.util.ArrayList;
import java.util.Random;

public class Panel extends TemplateBlockBase {

    private BlockBase template;

    public Panel(Identifier id, BlockBase blockBase) {
        super(id, blockBase.texture, blockBase.material);
        this.template = blockBase;
        this.setHardness(blockBase.getHardness());
        this.setBlastResistance(((BlockBaseAccessor)blockBase).getResistance() / 3.0F);
        this.setSounds(blockBase.sounds);
        this.setLightOpacity(255);
    }

    public void updateBoundingBox(BlockView arg, int i, int j, int k) {
        int var7 = arg.getTileMeta(i, j, k);
        if (var7 == 0) {
            this.setBoundingBox(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        } else if (var7 == 1) {
            this.setBoundingBox(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
        } else if (var7 == 2) {
            this.setBoundingBox(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
        } else if (var7 == 3) {
            this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
        }
    }

    public Box getCollisionShape(Level arg, int i, int j, int k) {
        return super.getCollisionShape(arg, i, j, k);
    }

    public boolean isFullOpaque() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    @Environment(EnvType.CLIENT)
    public boolean isSideRendered(BlockView arg, int i, int j, int k, int l) {
        return super.isSideRendered(arg, i, j, k, l);
    }

    public void doesBoxCollide(Level arg, int i, int j, int k, Box arg2, ArrayList arrayList) {
        int var7 = arg.getTileMeta(i, j, k);
        if (var7 == 0) {
            this.setBoundingBox(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            super.doesBoxCollide(arg, i, j, k, arg2, arrayList);
        } else if (var7 == 1) {
            this.setBoundingBox(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
            super.doesBoxCollide(arg, i, j, k, arg2, arrayList);
        } else if (var7 == 2) {
            this.setBoundingBox(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
            super.doesBoxCollide(arg, i, j, k, arg2, arrayList);
        } else if (var7 == 3) {
            this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
            super.doesBoxCollide(arg, i, j, k, arg2, arrayList);
        }
    }

    public void method_1605()
    {
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
    }

    @Environment(EnvType.CLIENT)
    public float getBrightness(BlockView arg, int i, int j, int k) {
        return this.template.getBrightness(arg, i, j, k);
    }

    public float getBlastResistance(EntityBase arg) {
        return this.template.getBlastResistance(arg);
    }

    @Environment(EnvType.CLIENT)
    public int getRenderPass() {
        return this.template.getRenderPass();
    }

    protected int droppedMeta(int iMetaData)
    {
        return 0;
    }

    public int getTextureForSide(int i, int j) {
        return this.template.getTextureForSide(i, j);
    }

    public int getTextureForSide(int i) {
        return this.template.getTextureForSide(i);
    }

    @Environment(EnvType.CLIENT)
    public int getTextureForSide(BlockView arg, int i, int j, int k, int l) {
        return this.template.getTextureForSide(arg, i, j, k, l);
    }

    public int getTickrate() {
        return this.template.getTickrate();
    }

    public boolean canPlaceAt(Level arg, int i, int j, int k) {
        return this.template.canPlaceAt(arg, i, j, k);
    }

    public void afterPlaced(Level arg, int i, int j, int k, Living arg2) {
        int var6 = MathHelper.floor((double)(arg2.yaw * 4.0F / 360.0F) + 0.5D) & 3;
        if (var6 == 0) {
            arg.setTileMeta(i, j, k, 2);
        }

        if (var6 == 1) {
            arg.setTileMeta(i, j, k, 1);
        }

        if (var6 == 2) {
            arg.setTileMeta(i, j, k, 3);
        }

        if (var6 == 3) {
            arg.setTileMeta(i, j, k, 0);
        }

    }
}
