package net.kozibrodka.wolves.blocks;

import net.minecraft.block.DetectorRail;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Minecart;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateDetectorRail;
import net.modificationstation.stationapi.api.template.block.TemplateRail;

import java.util.List;
import java.util.Random;

public class FCBlockDetectorRail extends TemplateRail
        /* implements ITextureProvider */ //MAYBE FOR FAKE LATER
{

    public FCBlockDetectorRail(Identifier iid, int iTextureID)
    {
        super(iid,iTextureID, true);
        this.setTicksRandomly(true);
        setHardness(0.7F);
        setSounds(METAL_SOUNDS);
    }
//
//    public int getTickrate() {
//        return 20;
//    }
//
//    public boolean getEmitsRedstonePower() {
//        return true;
//    }
//
//    public void onEntityCollision(Level arg, int i, int j, int k, EntityBase arg2) {
//        if (!arg.isServerSide) {
//            int var6 = arg.getTileMeta(i, j, k);
//            if ((var6 & 8) == 0) {
//                this.method_1144(arg, i, j, k, var6);
//            }
//        }
//    }
//
//    public void onScheduledTick(Level arg, int i, int j, int k, Random random) {
//        if (!arg.isServerSide) {
//            int var6 = arg.getTileMeta(i, j, k);
//            if ((var6 & 8) != 0) {
//                this.method_1144(arg, i, j, k, var6);
//            }
//        }
//    }
//
//    public boolean isPowered(BlockView arg, int i, int j, int k, int l) {
//        return (arg.getTileMeta(i, j, k) & 8) != 0;
//    }
//
//    public boolean indirectlyPowered(Level arg, int i, int j, int k, int l) {
//        if ((arg.getTileMeta(i, j, k) & 8) == 0) {
//            return false;
//        } else {
//            return l == 1;
//        }
//    }
//
//    private void method_1144(Level arg, int i, int j, int k, int l) {
//        boolean var6 = (l & 8) != 0;
//        boolean var7 = false;
//        float var8 = 0.125F;
//        List var9 = arg.getEntities(Minecart.class, Box.createButWasteMemory((double)((float)i + var8), (double)j, (double)((float)k + var8), (double)((float)(i + 1) - var8), (double)j + 0.25D, (double)((float)(k + 1) - var8)));
//        if (var9.size() > 0) {
//            var7 = true;
//        }
//
//        if (var7 && !var6) {
//            arg.setTileMeta(i, j, k, l | 8);
//            arg.updateAdjacentBlocks(i, j, k, this.id);
//            arg.updateAdjacentBlocks(i, j - 1, k, this.id);
//            arg.method_202(i, j, k, i, j, k);
//        }
//
//        if (!var7 && var6) {
//            arg.setTileMeta(i, j, k, l & 7);
//            arg.updateAdjacentBlocks(i, j, k, this.id);
//            arg.updateAdjacentBlocks(i, j - 1, k, this.id);
//            arg.method_202(i, j, k, i, j, k);
//        }
//
//        if (var7) {
//            arg.method_216(i, j, k, this.id, this.getTickrate());
//        }
//
//    }

}
