package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.utils.ITorchBox;
import net.minecraft.block.Block;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TorchBlock.class)
public class BlockTorchMixin extends Block implements ITorchBox {

    public BlockTorchMixin(int id, Material material) {
        super(id, material);
    }

    @Override
    @Unique
    public void updateBoundingBoxForBeamRender(BlockView blockView, int x, int y, int z) {
        int var7 = blockView.getBlockMeta(x, y, z) & 7;
        float var8 = 0.15F;
        if (var7 == 1) {
            this.setBoundingBox(0.0F, 0.2F, 0.5F - var8, var8 * 2.0F, 0.8F, 0.5F + var8);
        } else if (var7 == 2) {
            this.setBoundingBox(1.0F - var8 * 2.0F, 0.2F, 0.5F - var8, 1.0F, 0.8F, 0.5F + var8);
        } else if (var7 == 3) {
            this.setBoundingBox(0.5F - var8, 0.2F, 0.0F, 0.5F + var8, 0.8F, var8 * 2.0F);
        } else if (var7 == 4) {
            this.setBoundingBox(0.5F - var8, 0.2F, 1.0F - var8 * 2.0F, 0.5F + var8, 0.8F, 1.0F);
        } else {
            var8 = 0.1F;
            this.setBoundingBox(0.5F - var8, 0.0F, 0.5F - var8, 0.5F + var8, 0.6F, 0.5F + var8);
        }

    }
}
