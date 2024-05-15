package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.block.BlockBase;
import net.minecraft.block.DetectorRail;
import net.minecraft.block.Rail;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Minecart;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(DetectorRail.class)
public class BlockDetectorRailMixin extends Rail {

    protected BlockDetectorRailMixin(int i, int j, boolean bl) {
        super(i, j, bl);
    }

    @Redirect(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/DetectorRail;method_1144(Lnet/minecraft/level/Level;IIII)V"))
    private void injected(DetectorRail instance, Level i, int j, int k, int l, int var6) {
        railCheck(i,j,k,l,var6);
    }

    @Redirect(method = "onScheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/DetectorRail;method_1144(Lnet/minecraft/level/Level;IIII)V"))
    private void injected2(DetectorRail instance, Level i, int j, int k, int l, int var6) {
        railCheck(i,j,k,l,var6);
    }

    private void railCheck(Level arg, int i, int j, int k, int l) {
        boolean var6 = (l & 8) != 0;
        boolean var7 = false;
        float var8 = 0.125F;
        List var9 = arg.getEntities(Minecart.class, Box.createButWasteMemory((double)((float)i + var8), (double)j, (double)((float)k + var8), (double)((float)(i + 1) - var8), (double)j + 0.25D, (double)((float)(k + 1) - var8)));
        if(var9 != null && var9.size() > 0)
        {
            int i1 = 0;
            do
            {
                if(i1 >= var9.size())
                {
                    break;
                }
                Minecart entityminecart = (Minecart)var9.get(i1);
                if(ShouldPlateActivateBasedOnMinecart(arg, i, j, k, entityminecart.type, entityminecart.passenger))
                {
                    var7 = true;
                    break;
                }
                i1++;
            } while(true);
        }

        if (var7 && !var6) {
            arg.setTileMeta(i, j, k, l | 8);
            arg.updateAdjacentBlocks(i, j, k, this.id);
            arg.updateAdjacentBlocks(i, j - 1, k, this.id);
            arg.method_202(i, j, k, i, j, k);
        }

        if (!var7 && var6) {
            arg.setTileMeta(i, j, k, l & 7);
            arg.updateAdjacentBlocks(i, j, k, this.id);
            arg.updateAdjacentBlocks(i, j - 1, k, this.id);
            arg.method_202(i, j, k, i, j, k);
        }

        if (var7) {
            arg.method_216(i, j, k, this.id, this.getTickrate());
        }
    }

    public  boolean ShouldPlateActivateBasedOnMinecart(Level world, int i, int j, int k, int l, EntityBase entity)
    {
        int i1 = world.getTileId(i, j, k);
        if(i1 == BlockListener.detectorRailWood.id)
        {
            return true;
        }
        if(i1 == BlockListener.detectorRailObsidian.id)
        {
            if(entity != null && (entity instanceof PlayerBase))
            {
                return true;
            }
        } else
        if(i1 == BlockBase.DETECTOR_RAIL.id && (l > 0 || entity != null))
        {
            return true;
        }
        return false;
    }

}
