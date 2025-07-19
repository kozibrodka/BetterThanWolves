// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCBlockCement.java

package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.EnvironmentInterface;
import net.kozibrodka.wolves.block.entity.CementBlockEntity;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithWorldRenderer.class)
public class CementBlock extends TemplateBlockWithEntity
        implements BlockWithWorldRenderer {

    public CementBlock(Identifier iid) {
        super(iid, BlockListener.CEMENT_MATERIAL);
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setHardness(100F);
        setOpacity(255);
        setSoundGroup(Block.SAND_SOUND_GROUP);
        textureId = iCementTexture;
        tempSpreadToSideFlags = new boolean[4];
        tempClosestDownslopeToSideDist = new int[4];
        setTickRandomly(true);
    }

    public int getTextureId(BlockView iblockaccess, int i, int j, int k, int l) {
        if (IsCementPartiallyDry(iblockaccess, i, j, k)) {
            return TextureListener.cement_dry;
        } else {
            return TextureListener.cement;
        }
    }

    protected BlockEntity createBlockEntity() {
        return new CementBlockEntity();
    }

    public boolean isFullCube() {
        return false;
    }

    public boolean isOpaque() {
        return false;
    }

    public boolean hasCollision(int i, boolean flag) {
        return flag && i == 0;
    }

    public boolean isSolidBlock(BlockView iblockaccess, int i, int j, int k, int l) {
        Material new_material = iblockaccess.getMaterial(i, j, k);
        if (new_material == material) {
            return false;
        }
        if (new_material == Material.ICE) {
            return false;
        }
        if (l == 1) {
            return true;
        } else {
            return super.isSolidBlock(iblockaccess, i, j, k, l);
        }
    }

    public Box getCollisionShape(World world, int i, int j, int k) {
        if (world.getBlockId(i, j + 1, k) != id) {
            return Box.createCached(i, j, k, (float) (i + 1), (float) j + 0.5F, (float) (k + 1));
        } else {
            return Box.createCached(i, j, k, (float) (i + 1), (float) (j + 1), (float) (k + 1));
        }
    }

    public int getDroppedItemId(int i, Random random) {
        return 0;
    }

    public int getDroppedItemCount(Random random) {
        return 0;
    }

    public int getTickRate() {
        return 20;
    }

    public float getLuminance(BlockView iBlockAccess, int i, int j, int k) {
        float f = iBlockAccess.method_1782(i, j, k);
        float f1 = iBlockAccess.method_1782(i, j + 1, k);
        return f > f1 ? f : f1;
    }

    public void onPlaced(World world, int i, int j, int k) {
        super.onPlaced(world, i, j, k);
        if (world.getBlockId(i, j, k) == id) {
            world.scheduleBlockUpdate(i, j, k, id, getTickRate());
            if (world.isEmittingRedstonePower(i, j, k)) {
                SetCementPowered(world, i, j, k, true);
            }
        }
    }

    public void randomDisplayTick(World world, int i, int j, int k, Random random) {
        if (!IsCementPartiallyDry(world, i, j, k) && random.nextInt(250) == 0) {
            world.playSound((float) i + 0.5F, (float) j + 0.5F, (float) k + 0.5F, "mob.ghast.moan", 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
        }
    }

    public void onTick(World world, int i, int j, int k, Random random) {
        int cementDist = GetCementSpreadDist(world, i, j, k);
        boolean bOldPowerState = IsCementPowered(world, i, j, k);
        boolean bNewPowerState = world.isEmittingRedstonePower(i, j, k);
        if (bOldPowerState != bNewPowerState) {
            SetCementPowered(world, i, j, k, bNewPowerState);
        }
        if (cementDist > 0) {
            int newCementDist = -100;
            newCementDist = CheckForLesserSpreadDist(world, i - 1, j, k, newCementDist);
            newCementDist = CheckForLesserSpreadDist(world, i + 1, j, k, newCementDist);
            newCementDist = CheckForLesserSpreadDist(world, i, j, k - 1, newCementDist);
            newCementDist = CheckForLesserSpreadDist(world, i, j, k + 1, newCementDist);
            newCementDist = CheckForLesserSpreadDist(world, i, j + 1, k, newCementDist);
            if (newCementDist < 0) {
                newCementDist = -1;
            } else {
                newCementDist++;
            }
            int cementDistUp = GetCementSpreadDist(world, i, j + 1, k);
            if (cementDistUp >= 0 && cementDistUp < newCementDist) {
                newCementDist = cementDistUp + 1;
            }
            if (newCementDist > 0 && newCementDist < cementDist) {
                cementDist = newCementDist;
                SetCementSpreadDist(world, i, j, k, cementDist);
                SetCementDryTime(world, i, j, k, 0);
            }
        } else if (cementDist == 0 && bNewPowerState) {
            SetCementDryTime(world, i, j, k, 0);
        }
        int iDryTime = GetCementDryTime(world, i, j, k);
        iDryTime++;
        int minDryTime = CheckNeighboursCloserToSourceForMinDryTime(world, i, j, k);
        if (minDryTime <= iDryTime) {
            if (minDryTime <= 0) {
                iDryTime = 0;
            } else {
                iDryTime = minDryTime - 1;
            }
        }
        if (iDryTime > 12) {
            world.setBlock(i, j, k, Block.STONE.id);
        } else {
            SetCementDryTime(world, i, j, k, iDryTime);
            world.scheduleBlockUpdate(i, j, k, id, getTickRate());
            if (IsBlockOpenToSpread(world, i, j - 1, k)) {
                int targetCementDist = cementDist + 1;
                if (targetCementDist <= 16) {
                    world.setBlock(i, j - 1, k, id);
                    SetCementSpreadDist(world, i, j - 1, k, targetCementDist);
                }
            } else if (cementDist >= 0 && (cementDist == 0 || blockBlocksFlow(world, i, j - 1, k))) {
                boolean[] spreadToSideFlags = CheckSideBlocksForPotentialSpread(world, i, j, k);
                int spreadDist = cementDist + 1;
                if (spreadDist <= 16) {
                    if (spreadToSideFlags[0]) {
                        AttemptToSpreadToBlock(world, i - 1, j, k, spreadDist);
                    }
                    if (spreadToSideFlags[1]) {
                        AttemptToSpreadToBlock(world, i + 1, j, k, spreadDist);
                    }
                    if (spreadToSideFlags[2]) {
                        AttemptToSpreadToBlock(world, i, j, k - 1, spreadDist);
                    }
                    if (spreadToSideFlags[3]) {
                        AttemptToSpreadToBlock(world, i, j, k + 1, spreadDist);
                    }
                }
            }
        }
    }

    public boolean IsCementPowered(BlockView blockAccess, int i, int j, int k) {
        int iMetaData = blockAccess.getBlockMeta(i, j, k);
        return (iMetaData & 1) > 0;
    }

    private void SetCementPowered(World world, int i, int j, int k, boolean bPowered) {
        boolean bOldState = IsCementPowered(world, i, j, k);
        if (bOldState != bPowered) {
            int iMetaData = world.getBlockMeta(i, j, k);
            if (bPowered) {
                world.playSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "mob.ghast.scream", 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
                iMetaData |= 1;
            } else {
                iMetaData &= -2;
            }
            world.setBlockMeta(i, j, k, iMetaData);
            world.blockUpdateEvent(i, j, k);
        }
    }

    public float GetRenderHeight(BlockView blockAccess, int i, int j, int k) {
        float fRenderHeight = 1.0F;
        if (blockAccess.getMaterial(i, j, k) == material) {
            int dist = GetCementSpreadDist(blockAccess, i, j, k);
            fRenderHeight = (float) (dist + 1) / 18F;
            if (IsCementPartiallyDry(blockAccess, i, j, k)) {
                fRenderHeight *= 0.1F;
            } else {
                fRenderHeight *= 0.5F;
            }
        }
        return fRenderHeight;
    }

    public int GetCementSpreadDist(BlockView blockAccess, int i, int j, int k) {
        if (blockAccess.getMaterial(i, j, k) != material) {
            return -1;
        } else {
            CementBlockEntity tileEntity = (CementBlockEntity) blockAccess.getBlockEntity(i, j, k);
            return tileEntity.spreadDist;
        }
    }

    public void SetCementSpreadDist(World world, int i, int j, int k, int iSpreadDist) {
        CementBlockEntity tileEntity = (CementBlockEntity) world.getBlockEntity(i, j, k);
        tileEntity.spreadDist = iSpreadDist;
        world.notifyNeighbors(i, j, k, id);
        world.setBlocksDirty(i, j, k, i, j, k);
    }

    public boolean IsCementSourceBlock(BlockView blockAccess, int i, int j, int k) {
        return GetCementSpreadDist(blockAccess, i, j, k) == 0;
    }

    public int GetCementDryTime(BlockView blockAccess, int i, int j, int k) {
        if (blockAccess.getMaterial(i, j, k) != material) {
            return 0;
        } else {
            CementBlockEntity tileEntity = (CementBlockEntity) blockAccess.getBlockEntity(i, j, k);
            return tileEntity.dryTime;
        }
    }

    public void SetCementDryTime(World world, int i, int j, int k, int iDryTime) {
        CementBlockEntity tileEntity = (CementBlockEntity) world.getBlockEntity(i, j, k);
        tileEntity.dryTime = iDryTime;
        world.notifyNeighbors(i, j, k, id);
        world.setBlocksDirty(i, j, k, i, j, k);
    }

    public boolean IsCementPartiallyDry(BlockView blockAccess, int i, int j, int k) {
        return GetCementDryTime(blockAccess, i, j, k) >= 8;
    }

    private int CheckNeighboursCloserToSourceForMinDryTime(World world, int i, int j, int k) {
        int minDryTime = 1000;
        int distToSource = GetCementSpreadDist(world, i, j, k);
        minDryTime = GetLesserDryTimeIfCloserToSource(world, i, j + 1, k, distToSource, minDryTime);
        minDryTime = GetLesserDryTimeIfCloserToSource(world, i + 1, j, k, distToSource, minDryTime);
        minDryTime = GetLesserDryTimeIfCloserToSource(world, i - 1, j, k, distToSource, minDryTime);
        minDryTime = GetLesserDryTimeIfCloserToSource(world, i, j, k + 1, distToSource, minDryTime);
        minDryTime = GetLesserDryTimeIfCloserToSource(world, i, j, k - 1, distToSource, minDryTime);
        return minDryTime;
    }

    private int GetLesserDryTimeIfCloserToSource(World world, int i, int j, int k, int distToSource, int dryTime) {
        Material new_material = world.getMaterial(i, j, k);
        if (new_material == material) {
            int targetDistToSource = GetCementSpreadDist(world, i, j, k);
            if (targetDistToSource < distToSource) {
                int targetDryTime = GetCementDryTime(world, i, j, k);
                if (targetDryTime < dryTime) {
                    return targetDryTime;
                }
            }
        }
        return dryTime;
    }

    private void AttemptToSpreadToBlock(World world, int i, int j, int k, int newSpreadDist) {
        if (IsBlockOpenToSpread(world, i, j, k)) {
            int i1 = world.getBlockId(i, j, k);
            if (i1 > 0) {
                Block.BLOCKS[i1].dropStacks(world, i, j, k, world.getBlockMeta(i, j, k));
            }
            world.setBlock(i, j, k, id);
            SetCementSpreadDist(world, i, j, k, newSpreadDist);
        }
    }

    private boolean[] CheckSideBlocksForPotentialSpread(World world, int i, int j, int k) {
        for (int sideNum = 0; sideNum < 4; sideNum++) {
            int iSide = i;
            int jSide = j;
            int kSide = k;
            switch (sideNum) {
                case 0: // '\0'
                    iSide--;
                    break;

                case 1: // '\001'
                    iSide++;
                    break;

                case 2: // '\002'
                    kSide--;
                    break;

                default:
                    kSide++;
                    break;
            }
            tempSpreadToSideFlags[sideNum] = !blockBlocksFlow(world, iSide, jSide, kSide) && (world.getMaterial(iSide, jSide, kSide) != material || !IsCementSourceBlock(world, iSide, jSide, kSide));
        }

        return tempSpreadToSideFlags;
    }

    private boolean blockBlocksFlow(World world, int i, int j, int k) {
        int l = world.getBlockId(i, j, k);
        if (l == Block.DOOR.id || l == Block.IRON_DOOR.id || l == Block.SIGN.id || l == Block.LADDER.id || l == Block.SUGAR_CANE.id) {
            return true;
        }
        if (l == 0) {
            return false;
        }
        Material new_material = Block.BLOCKS[l].material;
        if (new_material == material) {
            return false;
        } else {
            return material.isSolid();
        }
    }

    protected int CheckForLesserSpreadDist(World world, int i, int j, int k, int sourceSpreadDist) {
        int targetSpreadDist = GetCementSpreadDist(world, i, j, k);
        if (targetSpreadDist < 0) {
            return sourceSpreadDist;
        }
        if (sourceSpreadDist < 0 || targetSpreadDist < sourceSpreadDist) {
            return targetSpreadDist;
        } else {
            return sourceSpreadDist;
        }
    }

    private boolean IsBlockOpenToSpread(World world, int i, int j, int k) {
        Material new_material = world.getMaterial(i, j, k);
        if (new_material == material) {
            return false;
        } else {
            return !blockBlocksFlow(world, i, j, k);
        }
    }

    private final int iCementTexture = 15;
    private final int iCementPartiallyDryTexture = 16;
    public static final int iMaxCementSpreadDist = 16;
    public static final int iCementTicksToDry = 12;
    public static final int iCementTicksToPartiallyDry = 8;
    boolean[] tempSpreadToSideFlags;
    int[] tempClosestDownslopeToSideDist;

    @Override
    public boolean renderWorld(BlockRenderManager tileRenderer, BlockView tileView, int x, int y, int z) {
        boolean flag = false;
        Tessellator tessellator = Tessellator.INSTANCE;
        boolean flag1 = this.isSideVisible(tileView, x, y + 1, z, 1);
        boolean flag2 = this.isSideVisible(tileView, x, y - 1, z, 0);
        boolean[] aflag = new boolean[4];
        aflag[0] = this.isSideVisible(tileView, x, y, z - 1, 2);
        aflag[1] = this.isSideVisible(tileView, x, y, z + 1, 3);
        aflag[2] = this.isSideVisible(tileView, x - 1, y, z, 4);
        aflag[3] = this.isSideVisible(tileView, x + 1, y, z, 5);
        if (!flag1 && !flag2 && !aflag[0] && !aflag[1] && !aflag[2] && !aflag[3]) {
            return false;
        }
        boolean flag3 = false;
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        double d = 0.0D;
        double d1 = 1.0D;
        float f4 = RenderCementGetCornerHeightFromNeighbours(tileView, x, y, z);
        float f5 = RenderCementGetCornerHeightFromNeighbours(tileView, x, y, z + 1);
        float f6 = RenderCementGetCornerHeightFromNeighbours(tileView, x + 1, y, z + 1);
        float f7 = RenderCementGetCornerHeightFromNeighbours(tileView, x + 1, y, z);
        if (flag || flag1) {
            flag3 = true;
            int l = this.getTextureId(tileView, x, y, z, 1);
            Atlas.Sprite atlasTX = Atlases.getTerrain().getTexture(l);
            double d2 = atlasTX.getStartU();
            double d3 = atlasTX.getEndU();
            double d4 = atlasTX.getStartV();
            double d5 = atlasTX.getEndV();
            float f13 = this.getLuminance(tileView, x, y, z);
            tessellator.color(f1 * f13, f1 * f13, f1 * f13);
            tessellator.vertex(x, (float) y + f4, z, d2, d4);
            tessellator.vertex(x, (float) y + f5, z + 1, d2, d5);
            tessellator.vertex(x + 1, (float) y + f6, z + 1, d3, d5);
            tessellator.vertex(x + 1, (float) y + f7, z, d3, d4);
        }
        if (flag || flag2) {
            float f8 = this.getLuminance(tileView, x, y - 1, z);
            tessellator.color(f * f8, f * f8, f * f8);
            tileRenderer.renderBottomFace(this, x, y, z, this.getTextureId(tileView, x, y, z, 0));
            flag3 = true;
        }
        for (int i1 = 0; i1 < 4; i1++) {
            int k1 = x;
            int i2 = y;
            int j2 = z;
            if (i1 == 0) {
                j2--;
            } else if (i1 == 1) {
                j2++;
            } else if (i1 == 2) {
                k1--;
            } else if (i1 == 3) {
                k1++;
            }
            int k2 = this.getTextureId(tileView, x, y, z, i1 + 2);
            Atlas.Sprite atlasTX2 = Atlases.getTerrain().getTexture(k2);
            if (!flag && !aflag[i1]) {
                continue;
            }
            float f9;
            float f10;
            float f11;
            float f12;
            float f14;
            float f15;
            if (i1 == 0) {
                f9 = f4;
                f10 = f7;
                f11 = x;
                f14 = x + 1;
                f12 = z;
                f15 = z;
            } else if (i1 == 1) {
                f9 = f6;
                f10 = f5;
                f11 = x + 1;
                f14 = x;
                f12 = z + 1;
                f15 = z + 1;
            } else if (i1 == 2) {
                f9 = f5;
                f10 = f4;
                f11 = x;
                f14 = x;
                f12 = z + 1;
                f15 = z;
            } else {
                f9 = f7;
                f10 = f6;
                f11 = x + 1;
                f14 = x + 1;
                f12 = z;
                f15 = z + 1;
            }
            flag3 = true;
            double d6 = atlasTX2.getStartU();
            double d7 = atlasTX2.getEndU();
            double d8 = atlasTX2.getStartV() + ((1.0F - f9) * 16F) / 512F;
            double d9 = atlasTX2.getStartV() + ((1.0F - f10) * 16F) / 512F;
            double d10 = atlasTX2.getEndV();

            float f16 = this.getLuminance(tileView, k1, i2, j2);
            if (i1 < 2) {
                f16 *= f2;
            } else {
                f16 *= f3;
            }
            tessellator.color(f1 * f16, f1 * f16, f1 * f16);
            tessellator.vertex(f11, (float) y + f9, f12, d6, d8);
            tessellator.vertex(f14, (float) y + f10, f15, d7, d9);
            tessellator.vertex(f14, y, f15, d7, d10);
            tessellator.vertex(f11, y, f12, d6, d10);
        }

        this.minY = d;
        this.maxY = d1;
        return flag3;
    }

    public float RenderCementGetCornerHeightFromNeighbours(BlockView iblockaccess, int i, int j, int k) {
        int l = 0;
        float f = 0.0F;
        for (int i1 = 0; i1 < 4; i1++) {
            int j1 = i - (i1 & 1);
            int k1 = j;
            int l1 = k - (i1 >> 1 & 1);
            if (iblockaccess.getMaterial(j1, k1 + 1, l1) == BlockListener.CEMENT_MATERIAL) {
                return 1.0F;
            }
            Material material = iblockaccess.getMaterial(j1, k1, l1);
            if (material == BlockListener.CEMENT_MATERIAL) {
                if (iblockaccess.method_1783(j1, k1 + 1, l1)) {
                    return 1.0F;
                }
                if (IsCementSourceBlock(iblockaccess, j1, k1, l1)) {
                    f += GetRenderHeight(iblockaccess, j1, k1, l1) * 10F;
                    l += 10;
                }
                f += GetRenderHeight(iblockaccess, j1, k1, l1);
                l++;
                continue;
            }
            if (!material.isSolid()) {
                f += 0.6F;
                l++;
            }
        }

        if (l > 0) {
            return 1.0F - f / (float) l;
        } else {
            return 1.0F;
        }
    }
}
