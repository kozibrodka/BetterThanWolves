package net.kozibrodka.wolves.block;

import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.util.Identifier;

public class LazyTransparentBlockTemplate extends LazyBlockTemplate implements BlockWithWorldRenderer, BlockWithInventoryRenderer {


    public LazyTransparentBlockTemplate(Identifier identifier, Material material, float hardness, BlockSoundGroup blockSounds) {
        super(identifier, material, hardness, blockSounds);
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public boolean renderWorld(BlockRenderManager tileRenderer, BlockView tileView, int x, int y, int z) {
        if (tileView.getBlockId(x - 1, y, z) != id) {
            this.setBoundingBox(0.0001F, 0.0625F, 0.0001F, 0.0625F, 0.9375F, 0.9999F);
            tileRenderer.renderBlock(this, x, y, z);
        }
        if (tileView.getBlockId(x, y, z + 1) != id) {
            this.setBoundingBox(0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F, 1.0F);
            tileRenderer.renderBlock(this, x, y, z);
        }
        if (tileView.getBlockId(x + 1, y, z) != id) {
            this.setBoundingBox(0.9375F, 0.0625F, 0.0001F, 0.9999F, 0.9375F, 0.9999F);
            tileRenderer.renderBlock(this, x, y, z);
        }
        if (tileView.getBlockId(x, y, z - 1) != id) {
            this.setBoundingBox(0.0F, 0.0625F, 0.0F, 1.0F, 0.9375F, 0.0625F);
            tileRenderer.renderBlock(this, x, y, z);
        }
        this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        tileRenderer.renderBlock(this, x, y, z);
        this.setBoundingBox(0.0F, 0.9375F, 0.0F, 1.0F, 1.0F, 1.0F);
        tileRenderer.renderBlock(this, x, y, z);
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }

    @Override
    public void renderInventory(BlockRenderManager tileRenderer, int meta) {
        this.setBoundingBox(1E-005F, 1E-005F, 1E-005F, 0.0625F, 0.99999F, 0.99999F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.hopper_grate);
        this.setBoundingBox(0.0F, 0.0F, 0.9375F, 1.0F, 1.0F, 1.0F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.hopper_grate);
        this.setBoundingBox(0.9375F, 1E-005F, 1E-005F, 0.99999F, 0.99999F, 0.99999F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.hopper_grate);
        this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0625F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.hopper_grate);
        this.setBoundingBox(0.0001F, 0.001F, 0.0001F, 0.9999F, 0.0625F, 0.9999F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.hopper_grate);
        this.setBoundingBox(0.0001F, 0.9375F, 0.0001F, 0.9999F, 0.999F, 0.9999F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.hopper_grate);
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

}
