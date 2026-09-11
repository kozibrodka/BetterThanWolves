//package net.kozibrodka.wolves.block;
//
//import net.kozibrodka.wolves.utils.UnsortedUtils;
//import net.minecraft.block.Block;
//import net.minecraft.block.material.Material;
//import net.minecraft.client.render.block.BlockRenderManager;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.world.BlockView;
//import net.modificationstation.stationapi.api.template.block.TemplateBlock;
//import net.minecraft.world.World;
//import net.modificationstation.stationapi.api.util.Identifier;
//
//import java.util.Random;
//
//
//public class LensBlock extends TemplateBlock implements FCIBlock, ITextureProvider {
//    public static final int m_iLensMaxRange = 128;
//    private static final int m_iLensTextureIDFront = 112;
//    private static final int m_iLensTextureIDSides = 113;
//    private static final int m_iLensTextureIDBack = 114;
//    private static final int iLensTickRate = 1;
//    private static final float fMinTriggerLightValue = 12.0F;
//
//    public LensBlock(Identifier identifier) {
//        super(identifier, Material.METAL);
//        this.setHardness(3.5F);
//        this.setSoundGroup(STONE_SOUND_GROUP);
//        this.setTickRandomly(true);
//    }
//
//    @Override
//    public int getRenderType() {
//        return mod_FCBetterThanWolves.iCustomLensRenderID;
//    }
//
//    @Override
//    public int getTickRate() {
//        return 1;
//    }
//
//    @Override
//    public int getTextureId(BlockView iblockaccess, int i, int j, int k, int l) {
//        int iFacing = this.GetFacing(iblockaccess, i, j, k);
//        return l == iFacing ? 112 : (l == UnsortedUtils.getOppositeFacing(iFacing) ? 114 : 113);
//    }
//
//    @Override
//    public int getTexture(int i) {
//        return i == 3 ? 112 : (i == 2 ? 114 : 113);
//    }
//
//    @Override
//    public void onPlaced(World world, int i, int j, int k, int iDirection) {
//        int iTargetDirection = UnsortedUtils.getOppositeFacing(iDirection);
//        this.SetFacing(world, i, j, k, iTargetDirection);
//        world.scheduleBlockUpdate(i, j, k, this.id, this.getTickRate());
//    }
//
//    @Override
//    public void onPlaced(World world, int i, int j, int k, LivingEntity entityLiving) {
//        int iFacing = UnsortedUtils.ConvertPlacingEntityOrientationToBlockFacing(entityLiving);
//        this.SetFacing(world, i, j, k, iFacing);
//        this.SetupBeam(world, i, j, k);
//    }
//
//    @Override
//    public void onBreak(World world, int i, int j, int k) {
//        this.RemoveBeam(world, i, j, k);
//    }
//
//    @Override
//    public void neighborUpdate(World world, int i, int j, int k, int l) {
//        world.scheduleBlockUpdate(i, j, k, this.id, this.getTickRate());
//    }
//
//    @Override
//    public void onTick(World world, int i, int j, int k, Random random) {
//        int iFacing = this.GetFacing(world, i, j, k);
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
//            FCUtilsBlockPos iSourceLightValue = new FCUtilsBlockPos(i, j, k);
//            iSourceLightValue.AddFacingAsOffset(iFacing);
//            if(world.getBlockId(iSourceLightValue.i, iSourceLightValue.j, iSourceLightValue.k) == 0) {
//                FCBlockDetectorLogic bShouldBeOn = (FCBlockDetectorLogic)((FCBlockDetectorLogic)mod_FCBetterThanWolves.fcBlockDetectorLogic);
//                bShouldBeOn.PropagateBeamsThroughBlock(world, iSourceLightValue.i, iSourceLightValue.j, iSourceLightValue.k);
//            }
//        } else {
//            FCUtilsBlockPos sourcePos1 = new FCUtilsBlockPos(i, j, k);
//            sourcePos1.AddFacingAsOffset(FCUtilsMisc.GetOppositeFacing(iFacing));
//            int iSourceLightValue1 = world.getBlockLightValue(sourcePos1.i, sourcePos1.j, sourcePos1.k);
//            boolean bShouldBeOn1 = iSourceLightValue1 >= 8;
//            if(this.IsLit(world, i, j, k) != bShouldBeOn1) {
//                this.SetLit(world, i, j, k, bShouldBeOn1);
//            }
//
//            world.scheduleBlockUpdate(i, j, k, this.id, this.getTickRate());
//        }
//
//    }
//
//    @Override
//    public int getPistonBehavior() {
//        return 2;
//    }
//
//    public int GetFacing(BlockView iblockaccess, int i, int j, int k) {
//        return (iblockaccess.getBlockMeta(i, j, k) & -2) >> 1;
//    }
//
//    public void SetFacing(World world, int i, int j, int k, int iFacing) {
//        int iMetaData = world.getBlockMeta(i, j, k);
//        iMetaData = iMetaData & 1 | iFacing << 1;
//        world.setBlockMeta(i, j, k, iMetaData);
//    }
//
//    public int GetFacingFromMetadata(int iMetadata) {
//        return (iMetadata & -2) >> 1;
//    }
//
//    public int SetFacingInMetadata(int iMetadata, int iFacing) {
//        iMetadata = iMetadata & 1 | iFacing << 1;
//        return iMetadata;
//    }
//
//    public boolean CanRotateOnTurntable(BlockView iBlockAccess, int i, int j, int k) {
//        return false;
//    }
//
//    public boolean CanTransmitRotationHorizontallyOnTurntable(BlockView blockAccess, int i, int j, int k) {
//        return false;
//    }
//
//    public boolean CanTransmitRotationVerticallyOnTurntable(BlockView blockAccess, int i, int j, int k) {
//        return false;
//    }
//
//    public void RotateAroundJAxis(World world, int i, int j, int k, boolean bReverse) {
//        FCUtilsMisc.StandardRotateAroundJ(this, world, i, j, k, bReverse);
//    }
//
//    public int RotateMetadataAroundJAxis(int iMetadata, boolean bReverse) {
//        return FCUtilsMisc.StandardRotateMetadataAroundJ(this, iMetadata, bReverse);
//    }
//
//    public boolean ToggleFacing(World world, int i, int j, int k, boolean bReverse) {
//        int iFacing = this.GetFacing(world, i, j, k);
//        iFacing = FCUtilsMisc.CycleFacing(iFacing, bReverse);
//        this.SetFacing(world, i, j, k, iFacing);
//        world.markBlocksDirty(i, j, k, i, j, k);
//        return true;
//    }
//
//    public boolean IsLit(BlockView iblockaccess, int i, int j, int k) {
//        return (iblockaccess.getBlockMeta(i, j, k) & 1) > 0;
//    }
//
//    public void SetLit(World world, int i, int j, int k, boolean bOn) {
//        if(bOn != this.IsLit(world, i, j, k)) {
//            int iMetaData = world.getBlockMeta(i, j, k);
//            if(bOn) {
//                iMetaData |= 1;
//            } else {
//                iMetaData &= -2;
//            }
//
//            world.setBlockMetadataWithNotify(i, j, k, iMetaData);
//            world.notifyBlocksOfNeighborChange(i, j, k, this.id);
//            world.markBlocksDirty(i, j, k, i, j, k);
//        }
//
//    }
//
//    private boolean CheckForInputLight(World world, int i, int j, int k) {
//        int iFacing = this.GetFacing(world, i, j, k);
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
//            } else if(iTargetBlockID == this.id && this.IsLit(world, targetPos.i, targetPos.j, targetPos.k) && this.GetFacing(world, targetPos.i, targetPos.j, targetPos.k) == iFacing) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    public void SetupBeam(World world, int i, int j, int k) {
//        FCBlockDetectorLogic logicBlock = (FCBlockDetectorLogic)((FCBlockDetectorLogic)mod_FCBetterThanWolves.fcBlockDetectorLogic);
//        logicBlock.CreateLensBeamFromBlock(world, i, j, k, this.GetFacing(world, i, j, k), 128);
//    }
//
//    private void RemoveBeam(World world, int i, int j, int k) {
//        FCBlockDetectorLogic logicBlock = (FCBlockDetectorLogic)((FCBlockDetectorLogic)mod_FCBetterThanWolves.fcBlockDetectorLogic);
//        logicBlock.RemoveLensBeamFromBlock(world, i, j, k, this.GetFacing(world, i, j, k), 128);
//    }
//
//    private void TurnBeamOn(World world, int i, int j, int k) {
//        FCBlockDetectorLogic logicBlock = (FCBlockDetectorLogic)((FCBlockDetectorLogic)mod_FCBetterThanWolves.fcBlockDetectorLogic);
//        logicBlock.TurnBeamOnFromBlock(world, i, j, k, this.GetFacing(world, i, j, k), 128);
//    }
//
//    private void TurnBeamOff(World world, int i, int j, int k) {
//        FCBlockDetectorLogic logicBlock = (FCBlockDetectorLogic)((FCBlockDetectorLogic)mod_FCBetterThanWolves.fcBlockDetectorLogic);
//        logicBlock.TurnBeamOffFromBlock(world, i, j, k, this.GetFacing(world, i, j, k), 128);
//    }
//
//    private boolean IsDirectlyFacingBlockDetector(World world, int i, int j, int k) {
//        int iFacing = this.GetFacing(world, i, j, k);
//        FCUtilsBlockPos targetPos = new FCUtilsBlockPos(i, j, k);
//        targetPos.AddFacingAsOffset(iFacing);
//        int iTargetBlockID = world.getBlockId(targetPos.i, targetPos.j, targetPos.k);
//        if(iTargetBlockID == mod_FCBetterThanWolves.fcBlockDetector.id) {
//            int iDetectorFacing = ((FCBlockDetectorBlock)((FCBlockDetectorBlock)mod_FCBetterThanWolves.fcBlockDetector)).GetFacing(world, targetPos.i, targetPos.j, targetPos.k);
//            if(iDetectorFacing == FCUtilsMisc.GetOppositeFacing(iFacing)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    public boolean RenderLens(BlockRenderManager renderBlocks, BlockView blockAccess, int i, int j, int k, Block block) {
//        return !this.IsLit(blockAccess, i, j, k) ? renderBlocks.renderStandardBlock(block, i, j, k) : this.RenderLitLens(renderBlocks, blockAccess, i, j, k, block);
//    }
//
//    public boolean RenderLitLens(BlockRenderManager renderBlocks, BlockView blockAccess, int i, int j, int k, Block block) {
//        boolean flag = false;
//        int iFacing = this.GetFacing(blockAccess, i, j, k);
//        int iColorMultiplier = block.colorMultiplier(blockAccess, i, j, k);
//        float f = (float)(iColorMultiplier >> 16 & 255) / 255.0F;
//        float f1 = (float)(iColorMultiplier >> 8 & 255) / 255.0F;
//        float f2 = (float)(iColorMultiplier & 255) / 255.0F;
//        float f3 = 0.5F;
//        float f4 = 1.0F;
//        float f5 = 0.8F;
//        float f6 = 0.6F;
//        float f7 = f4 * f;
//        float f8 = f4 * f1;
//        float f9 = f4 * f2;
//        float f10 = f3 * f;
//        float f11 = f5 * f;
//        float f12 = f6 * f;
//        float f13 = f3 * f1;
//        float f14 = f5 * f1;
//        float f15 = f6 * f1;
//        float f16 = f3 * f2;
//        float f17 = f5 * f2;
//        float f18 = f6 * f2;
//        Tessellator tessellator = Tessellator.instance;
//        if(renderBlocks.renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j - 1, k, 0)) {
//            if(iFacing == 0) {
//                tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
//                tessellator.setBrightness(blockAccess.getLightBrightnessForSkyBlocks(i, j, k, 15));
//            } else {
//                tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j - 1, k));
//                tessellator.setColorOpaque_F(f10, f13, f16);
//            }
//
//            renderBlocks.renderBottomFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(blockAccess, i, j, k, 0));
//            flag = true;
//        }
//
//        if(renderBlocks.renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j + 1, k, 1)) {
//            if(iFacing == 1) {
//                tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
//                tessellator.setBrightness(blockAccess.getLightBrightnessForSkyBlocks(i, j, k, 15));
//            } else {
//                tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j + 1, k));
//                tessellator.setColorOpaque_F(f7, f8, f9);
//            }
//
//            renderBlocks.renderTopFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(blockAccess, i, j, k, 1));
//            flag = true;
//        }
//
//        if(renderBlocks.renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j, k - 1, 2)) {
//            if(iFacing == 2) {
//                tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
//                tessellator.setBrightness(blockAccess.getLightBrightnessForSkyBlocks(i, j, k, 15));
//            } else {
//                tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k - 1));
//                tessellator.setColorOpaque_F(f11, f14, f17);
//            }
//
//            renderBlocks.renderEastFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(blockAccess, i, j, k, 2));
//            flag = true;
//        }
//
//        if(renderBlocks.renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j, k + 1, 3)) {
//            if(iFacing == 3) {
//                tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
//                tessellator.setBrightness(blockAccess.getLightBrightnessForSkyBlocks(i, j, k, 15));
//            } else {
//                tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k + 1));
//                tessellator.setColorOpaque_F(f11, f14, f17);
//            }
//
//            renderBlocks.renderWestFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(blockAccess, i, j, k, 3));
//            flag = true;
//        }
//
//        if(renderBlocks.renderAllFaces || block.shouldSideBeRendered(blockAccess, i - 1, j, k, 4)) {
//            if(iFacing == 4) {
//                tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
//                tessellator.setBrightness(blockAccess.getLightBrightnessForSkyBlocks(i, j, k, 15));
//            } else {
//                tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i - 1, j, k));
//                tessellator.setColorOpaque_F(f12, f15, f18);
//            }
//
//            renderBlocks.renderNorthFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(blockAccess, i, j, k, 4));
//            flag = true;
//        }
//
//        if(renderBlocks.renderAllFaces || block.shouldSideBeRendered(blockAccess, i + 1, j, k, 5)) {
//            if(iFacing == 5) {
//                tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
//                tessellator.setBrightness(blockAccess.getLightBrightnessForSkyBlocks(i, j, k, 15));
//            } else {
//                tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i + 1, j, k));
//                tessellator.setColorOpaque_F(f12, f15, f18);
//            }
//
//            renderBlocks.renderSouthFace(block, (double)i, (double)j, (double)k, block.getBlockTexture(blockAccess, i, j, k, 5));
//            flag = true;
//        }
//
//        return flag;
//    }
//
//    public void RenderLensInvBlock(RenderBlocks renderBlocks, Block block, int iItemDamage, int iRenderType) {
//        this.setBlockBoundsForItemRender();
//        FCUtilsRender.RenderInvBlockWithMetaData(renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage);
//    }
//}
