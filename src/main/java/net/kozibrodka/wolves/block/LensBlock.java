package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.EnvironmentInterface;
import net.kozibrodka.wolves.block.entity.LensBlockEntity;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.state.property.Properties;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

import static net.kozibrodka.wolves.block.BeamOfLightBlock.MAX_DISTANCE;

@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithWorldRenderer.class)
public class LensBlock extends TemplateBlockWithEntity implements BlockWithWorldRenderer {

    public LensBlock(Identifier identifier) {
        super(identifier, Material.METAL);
        this.setHardness(3.5F);
        this.setSoundGroup(STONE_SOUND_GROUP);
        this.setTickRandomly(false); /// OG=true
    }
    //TODO dac rotacje turntable interface.

    @Override
    public int getTickRate() {
        return 20; /// niepotrzebna metoda przy logice z TileEntity
    }

    @Override
    public int getTextureId(BlockView iblockaccess, int i, int j, int k, int l) {
        int iFacing = this.GetMetaFacing(iblockaccess, i, j, k);
        return l == iFacing ? TextureListener.lens_front : (l == UnsortedUtils.getOppositeFacing(iFacing) ? TextureListener.lens_back : 23);
    }

    @Override
    public int getTexture(int i) {
        return i == 3 ? TextureListener.lens_front : (i == 2 ? TextureListener.lens_back : 23); //TextureListener.lens_side
    }


    @Override
    public void onPlaced(World world, int x, int y, int z) {
        super.onPlaced(world, x, y, z);
        world.scheduleBlockUpdate(x, y, z, this.id, 1);
        /// Gdybym zrezignował z tileEntity, tutaj musiałbym Forcować Zapalenie Beama jeżeli blok przedemną to nie jest Beam (to się odpala przy przesunięciu przez pistona)
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new LensBlockEntity();
    }

    @Override
    public void onPlaced(World world, int i, int j, int k, LivingEntity entityLiving) {
//        Direction iFacing = UnsortedUtils.convertPlacingOrientationToGeographicalFacing(entityLiving);
//        this.SetFacingGeo(world, i, j, k, iFacing);
        /// meta
        int iFacing = UnsortedUtils.ConvertPlacingEntityOrientationToBlockFacing(entityLiving);
        this.SetFacing(world, i, j, k, iFacing);
    }


    @Override
    public void neighborUpdate(World world, int i, int j, int k, int id) {
//        System.out.println("updated by:  " + Block.BLOCKS[id].getTranslatedName());
        world.scheduleBlockUpdate(i, j, k, this.id, 10); /// 1 - OG
    }

    @Override
    public void onTick(World world, int i, int j, int k, Random random) {
        checkIfShouldBeLit(world, i, j, k);
//        world.scheduleBlockUpdate(i, j, k, this.id, this.getTickRate()); /// niepotrzebna metoda przy logice z TileEntity, służy do zapętlenia tickowania od pierwszego randomTick lub neighbor. "alternatywa" dla braku TileEnt - wtedy można przesuwać pistonem!
    }

    public void checkIfShouldBeLit(World world, int i, int j, int k){
        int iFacing = this.GetMetaFacing(world, i, j, k);
        BlockPosition lightSourcePos = new BlockPosition(i, j, k);
        BlockPosition dirPos = new BlockPosition(i, j, k);

        lightSourcePos.addFacingAsOffset(UnsortedUtils.getOppositeFacing(iFacing));
        dirPos.addFacingAsOffset(iFacing);

        int iSourceLightValue1 = world.getLightLevel(lightSourcePos.x, lightSourcePos.y, lightSourcePos.z);
        boolean bShouldBeOn1 = iSourceLightValue1 >= 13; /// 8 to mało.   12 w deszczu, 14 przy pochodni. Raczej sensownie od 13 aktywować.
        boolean bCanBeOn2 = world.getBlockId(dirPos.x, dirPos.y, dirPos.z) == 0 || world.getBlockId(dirPos.x, dirPos.y, dirPos.z) == BlockListener.beamLight.id;

//        System.out.println("ŚWIATŁO: " + iSourceLightValue1 + "   " + Block.BLOCKS[world.getBlockId(lightSourcePos.x, lightSourcePos.y, lightSourcePos.z)].getTranslatedName());
        this.SetLit(world, i, j, k, bShouldBeOn1 && bCanBeOn2, false);
    }


    @Override
    public int getPistonBehavior() {
        return 0; /// nie działa przy blockEntity
    }

    public int GetMetaFacing(BlockView iblockaccess, int i, int j, int k) {
        return (iblockaccess.getBlockMeta(i, j, k) & -2) >> 1;
    }

    /// META-DATA FACING
    public void SetFacing(World world, int i, int j, int k, int iFacing) {
        int iMetaData = world.getBlockMeta(i, j, k);
        iMetaData = iMetaData & 1 | iFacing << 1;
        world.setBlockMeta(i, j, k, iMetaData);
    }

    public boolean IsLit(BlockView iblockaccess, int i, int j, int k) {
//        BlockState blockState = ((BlockStateView)iblockaccess).getBlockState(i, j, k);
//        return blockState.get(LIT);
        /// meta logic
        return (iblockaccess.getBlockMeta(i, j, k) & 1) > 0;
    }

    public void SetLit(World world, int i, int j, int k, boolean bOn, boolean forceIt) {

//        BlockState blockState = world.getBlockState(i, j, k);
//        if(blockState.get(LIT) != bOn){
//            world.setBlockState(i, j, k, blockState.with(LIT, bOn));
//            world.notifyNeighbors(i, j, k, this.id);
//            world.setBlocksDirty(i, j, k, i, j, k);
//
//            if(bOn == false){ /// ZGASZENIE ŚWIATŁA.
//                RemoveBeam(world, i, j, k, blockState.get(Properties.FACING));
//            }else{
//                SetupBeam(world, i, j, k, blockState.get(Properties.FACING));
//            }
//        }
            /// meta logic
        if(bOn != this.IsLit(world, i, j, k) || forceIt) {
            int iMetaData = world.getBlockMeta(i, j, k);
            if(bOn) {
                iMetaData |= 1;
            } else {
                iMetaData &= -2;
            }
            world.setBlockMetaWithoutNotifyingNeighbors(i, j, k, iMetaData);
            world.notifyNeighbors(i, j, k, this.id);
            world.setBlocksDirty(i, j, k, i, j, k);

            if(bOn == false){ /// ZGASZENIE ŚWIATŁA.
                RemoveBeamMeta(world, i, j, k, GetMetaFacing(world, i, j, k));
            }else{
                SetupBeamMeta(world, i, j, k, GetMetaFacing(world, i, j, k));
            }
        }
    }

    private void SetupBeamMeta(World world, int i, int j, int k, int iFacing) {
        BlockPosition pos = new BlockPosition(i, j, k);
        pos.addFacingAsOffset(iFacing);

        if(world.getBlockId(pos.x, pos.y, pos.z) == 0) {
            world.setBlock(pos.x, pos.y, pos.z, BlockListener.beamLight.id);
            BlockState blockState = world.getBlockState(pos.x, pos.y, pos.z);
            world.setBlockState(pos.x, pos.y, pos.z, blockState.with(Properties.FACING, UnsortedUtils.convertMetaToDirection(iFacing)).with(MAX_DISTANCE, 0));
        }
    }

    private void RemoveBeamMeta(World world, int i, int j, int k, int iFacing) {
        BlockPosition pos = new BlockPosition(i, j, k);
        pos.addFacingAsOffset(iFacing);
        if(world.getBlockId(pos.x, pos.y, pos.z) == BlockListener.beamLight.id){ //todo sprawdź direction dodatkowo?
            world.setBlock(pos.x, pos.y, pos.z, 0);
        }
    }

    public boolean IsLitRender(BlockView iblockaccess, int i, int j, int k) {
        if(iblockaccess.getBlockId(i, j ,k) == BlockListener.lens.id) { /// gdybym chciał przesuwanie przez piston zabezpieczenie.
            return (iblockaccess.getBlockMeta(i, j, k) & 1) > 0;
        }else{
            return false;
        }

    }

    @Override
    public boolean renderWorld(BlockRenderManager blockRenderManager, BlockView blockView, int i, int j, int k) {
        return !this.IsLitRender(blockView, i, j, k) ? this.renderLens(blockRenderManager, blockView, i, j, k) : this.RenderLitLens(blockRenderManager, blockView, i, j, k);
    }

    public boolean renderLens(BlockRenderManager renderBlocks, BlockView blockAccess, int i, int j, int k){
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderBlocks.renderBlock(this, i, j, k);
        return true;
    }

    public boolean RenderLitLens(BlockRenderManager renderBlocks, BlockView blockAccess, int i, int j, int k) {
        boolean flag = false;
        int iFacing = this.GetMetaFacing(blockAccess, i, j, k);
        Block block = Block.BLOCKS[blockAccess.getBlockId(i, j, k)];
        int iColorMultiplier = block.getColorMultiplier(blockAccess, i, j, k);
        float f = (float)(iColorMultiplier >> 16 & 255) / 255.0F;
        float f1 = (float)(iColorMultiplier >> 8 & 255) / 255.0F;
        float f2 = (float)(iColorMultiplier & 255) / 255.0F;
        float f3 = 0.5F;
        float f4 = 1.0F;
        float f5 = 0.8F;
        float f6 = 0.6F;
        float f7 = f4 * f;
        float f8 = f4 * f1;
        float f9 = f4 * f2;
        float f10 = f3 * f;
        float f11 = f5 * f;
        float f12 = f6 * f;
        float f13 = f3 * f1;
        float f14 = f5 * f1;
        float f15 = f6 * f1;
        float f16 = f3 * f2;
        float f17 = f5 * f2;
        float f18 = f6 * f2;
        Tessellator tessellator = Tessellator.INSTANCE;
        if(renderBlocks.skipFaceCulling || block.isSideVisible(blockAccess, i, j - 1, k, 0)) {
            if(iFacing == 0) {
                tessellator.color(1.0F, 1.0F, 1.0F);
            } else {
                float blockBrightness = blockAccess.method_1782(i, j - 1, k);
                tessellator.color(f10 * blockBrightness, f13 * blockBrightness, f16 * blockBrightness);
            }
            renderBlocks.renderBottomFace(block, i, j, k, block.getTextureId(blockAccess, i, j, k, 0));
            flag = true;
        }

        if(renderBlocks.skipFaceCulling || block.isSideVisible(blockAccess, i, j + 1, k, 1)) {
            if(iFacing == 1) {
                tessellator.color(1.0F, 1.0F, 1.0F);
            } else {
                float blockBrightness = blockAccess.method_1782(i, j + 1, k);
                tessellator.color(f7 * blockBrightness, f8 * blockBrightness, f9 * blockBrightness);
            }
            renderBlocks.renderTopFace(block, i, j, k, block.getTextureId(blockAccess, i, j, k, 1));
            flag = true;
        }

        if(renderBlocks.skipFaceCulling || block.isSideVisible(blockAccess, i, j, k - 1, 2)) {
            if(iFacing == 2) {
                tessellator.color(1.0F, 1.0F, 1.0F);
            } else {
                float blockBrightness = blockAccess.method_1782(i, j, k - 1);
                tessellator.color(f11 * blockBrightness, f14 * blockBrightness, f17 * blockBrightness);
            }
            renderBlocks.renderEastFace(block, i, j, k, block.getTextureId(blockAccess, i, j, k, 2));
            flag = true;
        }

        if(renderBlocks.skipFaceCulling || block.isSideVisible(blockAccess, i, j, k + 1, 3)) {
            if(iFacing == 3) {
                tessellator.color(1.0F, 1.0F, 1.0F);
            } else {
                float blockBrightness = blockAccess.method_1782(i, j, k + 1);
                tessellator.color(f11 * blockBrightness, f14 * blockBrightness, f17 * blockBrightness);
            }
            renderBlocks.renderWestFace(block, i, j, k, block.getTextureId(blockAccess, i, j, k, 3));
            flag = true;
        }

        if(renderBlocks.skipFaceCulling || block.isSideVisible(blockAccess, i - 1, j, k, 4)) {
            if(iFacing == 4) {
                tessellator.color(1.0F, 1.0F, 1.0F);
            } else {
                float blockBrightness = blockAccess.method_1782(i - 1, j, k);
                tessellator.color(f12 * blockBrightness, f15 * blockBrightness, f18 * blockBrightness);
            }
            renderBlocks.renderNorthFace(block, i, j, k, block.getTextureId(blockAccess, i, j, k, 4));
            flag = true;
        }

        if(renderBlocks.skipFaceCulling || block.isSideVisible(blockAccess, i + 1, j, k, 5)) {
            if(iFacing == 5) {
                tessellator.color(1.0F, 1.0F, 1.0F);
            } else {
                float blockBrightness = blockAccess.method_1782(i + 1, j, k);
                tessellator.color(f12 * blockBrightness, f15 * blockBrightness, f18 * blockBrightness);
            }
            renderBlocks.renderSouthFace(block, i, j, k, block.getTextureId(blockAccess, i, j, k, 5));
            flag = true;
        }

        return flag;
    }


    /// BlockStates logic

//    private void SetupBeam(World world, int i, int j, int k, Direction geoDir) {
//        BlockPos pos = new BlockPos(i, j, k).add(geoDir.getVector());
//        if(world.getBlockId(pos.x, pos.y, pos.z) == 0) {
//            world.setBlock(pos.x, pos.y, pos.z, BlockListener.beamLight.id);
//            BlockState blockState = world.getBlockState(pos);
//            world.setBlockState(pos, blockState.with(Properties.FACING, geoDir));
//        }
//    }
//
//    private void RemoveBeam(World world, int i, int j, int k, Direction geoDir) {
//        BlockPos pos = new BlockPos(i, j, k).add(geoDir.getVector());
//        if(world.getBlockId(pos.x, pos.y, pos.z) == BlockListener.beamLight.id){
//            world.setBlock(pos.x, pos.y, pos.z, 0);
//        }
//    }
//
//    public Direction GetGeoFacing(BlockView iblockaccess, int i, int j, int k) {
//        BlockState blockState = ((BlockStateView)iblockaccess).getBlockState(i, j, k);
//        return blockState.get(Properties.FACING);
//    }
//
//    public void SetFacingGeo(World world, int i, int j, int k, Direction iFacing) {
//        BlockState state = world.getBlockState(i, j, k);
//        world.setBlockState(i, j, k, state.with(Properties.FACING, iFacing));
//    }

}
