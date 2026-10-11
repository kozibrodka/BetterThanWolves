package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.EnvironmentInterface;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.Properties;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.world.BlockStateView;

import java.util.Random;

@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithWorldRenderer.class)
public class LensBlock extends TemplateBlock implements BlockWithWorldRenderer {
    public static final int m_iLensMaxRange = 128;
    private static final int m_iLensTextureIDFront = TextureListener.lens_front; // 112
    private static final int m_iLensTextureIDSides = TextureListener.lens_side; // 113
    private static final int m_iLensTextureIDBack = TextureListener.lens_back; // 114
    private static final int iLensTickRate = 1;
    private static final float fMinTriggerLightValue = 12.0F;

    public LensBlock(Identifier identifier) {
        super(identifier, Material.METAL);
        this.setHardness(3.5F);
        this.setSoundGroup(STONE_SOUND_GROUP);
        this.setTickRandomly(true); /// OG=true
        setDefaultState(getDefaultState()
                .with(Properties.FACING, Direction.UP)
                .with(LIT, false)
        );
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder){
        builder.add(Properties.FACING);
        builder.add(LIT);
    }

    private static final BooleanProperty LIT = BooleanProperty.of("lit");

    @Override
    public int getTickRate() {
        return 20; /// oryginalnie 1
    }

    @Override
    public int getTextureId(BlockView iblockaccess, int i, int j, int k, int l) {
        int iFacing = this.GetMetaFacing(iblockaccess, i, j, k);
        return l == iFacing ? TextureListener.lens_front : (l == UnsortedUtils.getOppositeFacing(iFacing) ? TextureListener.lens_back : TextureListener.lens_side);
    }

    @Override
    public int getTexture(int i) {
        return i == 3 ? TextureListener.lens_front : (i == 2 ? TextureListener.lens_back : TextureListener.lens_side);
    }

    @Override
    public void onPlaced(World world, int i, int j, int k, int iDirection) {
//        int iTargetDirection = UnsortedUtils.getOppositeFacing(iDirection);
//        this.SetFacing(world, i, j, k, iTargetDirection);
//        world.scheduleBlockUpdate(i, j, k, this.id, 1);
    }

    @Override
    public void onPlaced(World world, int x, int y, int z) {
        world.scheduleBlockUpdate(x, y, z, this.id, 1);
    }

    @Override
    public void onPlaced(World world, int i, int j, int k, LivingEntity entityLiving) {
        Direction iFacing = UnsortedUtils.convertPlacingOrientationToGeographicalFacing(entityLiving);
        this.SetFacing(world, i, j, k, iFacing);
//        this.SetupBeam(world, i, j, k);
    }

    @Override
    public void onBreak(World world, int i, int j, int k) {
//        BlockState blockState = world.getBlockState(i, j, k);
//        RemoveBeam(world, i, j, k, blockState.get(Properties.FACING));
//        this.RemoveBeam(world, i, j, k);
    }

    @Override
    public void neighborUpdate(World world, int i, int j, int k, int id) {

        world.scheduleBlockUpdate(i, j, k, this.id, 1);
    }

    @Override
    public void onTick(World world, int i, int j, int k, Random random) {
        int iFacing = this.GetMetaFacing(world, i, j, k);
//        boolean bIsLightDetector = this.IsDirectlyFacingBlockDetector(world, i, j, k);
//        if(!bIsLightDetector) {
//            boolean sourcePos = this.CheckForInputLight(world, i, j, k);
//            if(this.IsLit(world, i, j, k) != sourcePos) {
//                this.SetLit(world, i, j, k, sourcePos);
//                if(sourcePos) {
//                    this.TurnBeamOn(world, i, j, k);
//                } else {
//                    this.TurnBeamOff(world, i, j, k);
//                }
//            }
//
//            BlockPosition iSourceLightValue = new BlockPosition(i, j, k);
//            iSourceLightValue.addFacingAsOffset(iFacing);
//            if(world.getBlockId(iSourceLightValue.x, iSourceLightValue.y, iSourceLightValue.z) == 0) {
//                FCBlockDetectorLogic bShouldBeOn = (FCBlockDetectorLogic)((FCBlockDetectorLogic)mod_FCBetterThanWolves.fcBlockDetectorLogic);
//                bShouldBeOn.PropagateBeamsThroughBlock(world, iSourceLightValue.i, iSourceLightValue.j, iSourceLightValue.k);
//            }
//        } else {
//
//        }

        BlockPosition lightSourcePos = new BlockPosition(i, j, k);
        lightSourcePos.addFacingAsOffset(UnsortedUtils.getOppositeFacing(iFacing));
        int iSourceLightValue1 = world.getLightLevel(lightSourcePos.x, lightSourcePos.y, lightSourcePos.z);
        boolean bShouldBeOn1 = iSourceLightValue1 >= 13; /// 8 to mało.   12 w deszczu, 14 przy pochodni. Raczej sensownie od 13 aktywować.
        if(this.IsLit(world, i, j, k) != bShouldBeOn1) {
            this.SetLit(world, i, j, k, bShouldBeOn1);
        }

        world.scheduleBlockUpdate(i, j, k, this.id, this.getTickRate());

    }

    private void SetupBeam(World world, int i, int j, int k, Direction geoDir) {
        BlockPos pos = new BlockPos(i, j, k).add(geoDir.getVector());
        if(world.getBlockId(pos.x, pos.y, pos.z) == 0) {
            world.setBlock(pos.x, pos.y, pos.z, BlockListener.beamLight.id);
            BlockState blockState = world.getBlockState(pos);
            world.setBlockState(pos, blockState.with(Properties.FACING, geoDir));
        }
    }

    private void RemoveBeam(World world, int i, int j, int k, Direction geoDir) {
        BlockPos pos = new BlockPos(i, j, k).add(geoDir.getVector());
        if(world.getBlockId(pos.x, pos.y, pos.z) == BlockListener.beamLight.id){
            world.setBlock(pos.x, pos.y, pos.z, 0);
        }
    }

    @Override
    public int getPistonBehavior() { //todo problem z renderem, trzeba byc zamiast blockState użyć metadata.
        return 0; /// 2
    }

    public int GetMetaFacing(BlockView iblockaccess, int i, int j, int k) {
        if(iblockaccess.getBlockId(i, j ,k) == BlockListener.lens.id) {
            BlockState blockState = ((BlockStateView) iblockaccess).getBlockState(i, j, k);
            return UnsortedUtils.convertDirectionToMeta(blockState.get(Properties.FACING));
        }else
            return 1;
//        return (iblockaccess.getBlockMeta(i, j, k) & -2) >> 1;
    }

    public Direction GetGeoFacing(BlockView iblockaccess, int i, int j, int k) {
        BlockState blockState = ((BlockStateView)iblockaccess).getBlockState(i, j, k);
        return blockState.get(Properties.FACING);
    }

    public void SetFacing(World world, int i, int j, int k, Direction iFacing) {
        BlockState state = world.getBlockState(i, j, k);
        world.setBlockState(i, j, k, state.with(Properties.FACING, iFacing));

//        int iMetaData = world.getBlockMeta(i, j, k);
//        iMetaData = iMetaData & 1 | iFacing << 1;
//        world.setBlockMeta(i, j, k, iMetaData);
    }

    public boolean IsLit(BlockView iblockaccess, int i, int j, int k) {
        BlockState blockState = ((BlockStateView)iblockaccess).getBlockState(i, j, k);
        return blockState.get(LIT);

//        return (iblockaccess.getBlockMeta(i, j, k) & 1) > 0;
    }

    public void SetLit(World world, int i, int j, int k, boolean bOn) {

        BlockState blockState = world.getBlockState(i, j, k);
        if(blockState.get(LIT) != bOn){
            world.setBlockState(i, j, k, blockState.with(LIT, bOn));
            world.notifyNeighbors(i, j, k, this.id);
            world.setBlocksDirty(i, j, k, i, j, k);

            if(bOn == false){ /// ZGASZENIE ŚWIATŁA.
                RemoveBeam(world, i, j, k, blockState.get(Properties.FACING));
            }else{
                SetupBeam(world, i, j, k, blockState.get(Properties.FACING));
            }
        }
//
//        if(bOn != this.IsLit(world, i, j, k)) {
//            int iMetaData = world.getBlockMeta(i, j, k);
//            if(bOn) {
//                iMetaData |= 1;
//            } else {
//                iMetaData &= -2;
//            }
//
//            world.notifyNeighbors(i, j, k, this.id);
//            world.setBlocksDirty(i, j, k, i, j, k);
//        }
    }

//    private boolean CheckForInputLight(World world, int i, int j, int k) {
//        int iFacing = this.GetMetaFacing(world, i, j, k);
//        int iTargetFacing = FCUtilsMisc.GetOppositeFacing(iFacing);
//        FCUtilsBlockPos targetPos = new FCUtilsBlockPos(i, j, k);
//        targetPos.AddFacingAsOffset(iTargetFacing);
//        int iTargetBlockID = world.getBlockId(targetPos.i, targetPos.j, targetPos.k);
//        if(iTargetBlockID > 0) {
//            if((float)lightValue[iTargetBlockID] > 12.0F) {
//                if(iTargetBlockID != mod_FCBetterThanWolves.fcBlockDetectorGlowingLogic.id) {
//                    return true;
//                }
//
//                FCBlockDetectorLogic logicBlock = (FCBlockDetectorLogic)((FCBlockDetectorLogic)mod_FCBetterThanWolves.fcBlockDetectorGlowingLogic);
//                if(logicBlock.ShouldBeProjectingToFacing(world, targetPos.i, targetPos.j, targetPos.k, iFacing)) {
//                    return true;
//                }
//            } else if(iTargetBlockID == this.id && this.IsLit(world, targetPos.i, targetPos.j, targetPos.k) && this.GetMetaFacing(world, targetPos.i, targetPos.j, targetPos.k) == iFacing) {
//                return true;
//            }
//        }
//
//        return false;
//    }



    public boolean IsLitRender(BlockView iblockaccess, int i, int j, int k) {
        if(iblockaccess.getBlockId(i, j ,k) == BlockListener.lens.id) {
            BlockState blockState = ((BlockStateView) iblockaccess).getBlockState(i, j, k);
            return blockState.get(LIT);
        }else{
            return false;
        }

//        return (iblockaccess.getBlockMeta(i, j, k) & 1) > 0;
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

//    public void RenderLensInvBlock(RenderBlocks renderBlocks, Block block, int iItemDamage, int iRenderType) {
//        this.setBlockBoundsForItemRender();
//        FCUtilsRender.RenderInvBlockWithMetaData(renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage);
//    }

}
