package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.events.ConfigListener;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.LargeOakTreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LargeOakTreeFeature.class)
public abstract class LargeOakTreeStumpMixin extends Feature {

    @Shadow
    World world;

    @Shadow
    int[] field_648;

    @Inject(at = @At("RETURN"), method = "method_622")
    private void generateStump(CallbackInfo ci) {
        if (!ConfigListener.wolvesGlass.difficulty.treeStumps) {
            return;
        }
        world.setBlock(field_648[0], field_648[1], field_648[2], Block.WOOL.id);
    }
}
