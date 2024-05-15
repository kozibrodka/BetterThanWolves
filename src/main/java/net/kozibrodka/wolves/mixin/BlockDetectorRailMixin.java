package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.BlockListener;
import net.minecraft.block.Block;
import net.minecraft.block.DetectorRailBlock;
import net.minecraft.block.RailBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(DetectorRailBlock.class)
public class BlockDetectorRailMixin extends RailBlock {

    protected BlockDetectorRailMixin(int i, int j, boolean bl) {
        super(i, j, bl);
    }

    @Redirect(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/DetectorRailBlock;method_1144(Lnet/minecraft/world/World;IIII)V"))
    private void injected(DetectorRailBlock instance, World i, int j, int k, int l, int var6) {
        railCheck(i,j,k,l,var6);
    }

    @Redirect(method = "onTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/DetectorRailBlock;method_1144(Lnet/minecraft/world/World;IIII)V"))
    private void injected2(DetectorRailBlock instance, World i, int j, int k, int l, int var6) {
        railCheck(i,j,k,l,var6);
    }

    private void railCheck(World arg, int i, int j, int k, int l) {
        boolean var6 = (l & 8) != 0;
        boolean var7 = false;
        float var8 = 0.125F;
        List var9 = arg.method_175(MinecartEntity.class, Box.createCached((double)((float)i + var8), (double)j, (double)((float)k + var8), (double)((float)(i + 1) - var8), (double)j + 0.25D, (double)((float)(k + 1) - var8)));
        if(var9 != null && var9.size() > 0)
        {
            int i1 = 0;
            do
            {
                if(i1 >= var9.size())
                {
                    break;
                }
                MinecartEntity entityminecart = (MinecartEntity)var9.get(i1);
                if(ShouldPlateActivateBasedOnMinecart(arg, i, j, k, entityminecart.field_2275, entityminecart.field_1594))
                {
                    var7 = true;
                    break;
                }
                i1++;
            } while(true);
        }

        if (var7 && !var6) {
            arg.method_215(i, j, k, l | 8);
            arg.method_244(i, j, k, this.id);
            arg.method_244(i, j - 1, k, this.id);
            arg.method_202(i, j, k, i, j, k);
        }

        if (!var7 && var6) {
            arg.method_215(i, j, k, l & 7);
            arg.method_244(i, j, k, this.id);
            arg.method_244(i, j - 1, k, this.id);
            arg.method_202(i, j, k, i, j, k);
        }

        if (var7) {
            arg.method_216(i, j, k, this.id, this.getTickRate());
        }
    }

    public  boolean ShouldPlateActivateBasedOnMinecart(World world, int i, int j, int k, int l, Entity entity)
    {
        int i1 = world.getBlockId(i, j, k);
        if(i1 == BlockListener.detectorRailWood.id)
        {
            return true;
        }
        if(i1 == BlockListener.detectorRailObsidian.id)
        {
            if(entity != null && (entity instanceof PlayerEntity))
            {
                return true;
            }
        } else
        if(i1 == Block.DETECTOR_RAIL.id && (l > 0 || entity != null))
        {
            return true;
        }
        return false;
    }

}
