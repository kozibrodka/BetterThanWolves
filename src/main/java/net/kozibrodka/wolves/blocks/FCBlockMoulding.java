package net.kozibrodka.wolves.blocks;


import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;


public class FCBlockMoulding extends TemplateBlockBase
{

    public FCBlockMoulding(Identifier iid)
    {
        super(iid, Material.WOOD);
        setHardness(0.5F);
        setSounds(WOOD_SOUNDS);
        texture = 4;
    }

    public Box getCollisionShape(Level world, int i, int j, int k)
    {
        int iAlignment = GetMouldingAlignment(world, i, j, k);
        float minX = i;
        float maxX = minX + 1.0F;
        float minY = j;
        float maxY = minY + 1.0F;
        float minZ = k;
        float maxZ = minZ + 1.0F;
        if(iAlignment == 0)
        {
            maxY = minY + 0.5F;
            maxZ = minZ + 0.5F;
        } else
        if(iAlignment == 1)
        {
            minX += 0.5F;
            maxY = minY + 0.5F;
        } else
        if(iAlignment == 2)
        {
            maxY = minY + 0.5F;
            minZ += 0.5F;
        } else
        if(iAlignment == 3)
        {
            maxX = minX + 0.5F;
            maxY = minY + 0.5F;
        } else
        if(iAlignment == 4)
        {
            maxX = minX + 0.5F;
            maxZ = minZ + 0.5F;
        } else
        if(iAlignment == 5)
        {
            minX += 0.5F;
            maxZ = minZ + 0.5F;
        } else
        if(iAlignment == 6)
        {
            minX += 0.5F;
            minZ += 0.5F;
        } else
        if(iAlignment == 7)
        {
            maxX = minX + 0.5F;
            minZ += 0.5F;
        } else
        if(iAlignment == 8)
        {
            minY += 0.5F;
            maxZ = minZ + 0.5F;
        } else
        if(iAlignment == 9)
        {
            minX += 0.5F;
            minY += 0.5F;
        } else
        if(iAlignment == 10)
        {
            minY += 0.5F;
            minZ += 0.5F;
        } else
        {
            maxX = minX + 0.5F;
            minY += 0.5F;
        }
        return Box.createButWasteMemory(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public void updateBoundingBox(BlockView iblockaccess, int i, int j, int k)
    {
        int iAlignment = GetMouldingAlignment(iblockaccess, i, j, k);
        float minX = 0.0F;
        float maxX = minX + 1.0F;
        float minY = 0.0F;
        float maxY = minY + 1.0F;
        float minZ = 0.0F;
        float maxZ = minZ + 1.0F;
        if(iAlignment == 0)
        {
            maxY = minY + 0.5F;
            maxZ = minZ + 0.5F;
        } else
        if(iAlignment == 1)
        {
            minX += 0.5F;
            maxY = minY + 0.5F;
        } else
        if(iAlignment == 2)
        {
            maxY = minY + 0.5F;
            minZ += 0.5F;
        } else
        if(iAlignment == 3)
        {
            maxX = minX + 0.5F;
            maxY = minY + 0.5F;
        } else
        if(iAlignment == 4)
        {
            maxX = minX + 0.5F;
            maxZ = minZ + 0.5F;
        } else
        if(iAlignment == 5)
        {
            minX += 0.5F;
            maxZ = minZ + 0.5F;
        } else
        if(iAlignment == 6)
        {
            minX += 0.5F;
            minZ += 0.5F;
        } else
        if(iAlignment == 7)
        {
            maxX = minX + 0.5F;
            minZ += 0.5F;
        } else
        if(iAlignment == 8)
        {
            minY += 0.5F;
            maxZ = minZ + 0.5F;
        } else
        if(iAlignment == 9)
        {
            minX += 0.5F;
            minY += 0.5F;
        } else
        if(iAlignment == 10)
        {
            minY += 0.5F;
            minZ += 0.5F;
        } else
        {
            maxX = minX + 0.5F;
            minY += 0.5F;
        }
        setBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public void method_1605()
    {
        setBoundingBox(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 1.0F);
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public void onBlockPlaced(Level world, int i, int j, int k, int iFacing)
    {
        int iAlignment = 0;
        int iNumPossibleAlignments = 0;
        boolean bPosIPresent = IsValidNeighbour(world, i + 1, j, k);
        boolean bNegIPresent = IsValidNeighbour(world, i - 1, j, k);
        boolean bPosJPresent = IsValidNeighbour(world, i, j + 1, k);
        boolean bNegJPresent = IsValidNeighbour(world, i, j - 1, k);
        boolean bPosKPresent = IsValidNeighbour(world, i, j, k + 1);
        boolean bNegKPresent = IsValidNeighbour(world, i, j, k - 1);
        boolean bPotentialAlignments[] = new boolean[16];
        for(int iTempIndex = 0; iTempIndex <= 15; iTempIndex++)
        {
            bPotentialAlignments[iTempIndex] = false;
        }

        if(iFacing == 0)
        {
            if(bPosIPresent || bNegIPresent || bPosKPresent || bNegKPresent)
            {
                if(bPosIPresent)
                {
                    bPotentialAlignments[9] = true;
                    iNumPossibleAlignments++;
                }
                if(bNegIPresent)
                {
                    bPotentialAlignments[11] = true;
                    iNumPossibleAlignments++;
                }
                if(bPosKPresent)
                {
                    bPotentialAlignments[10] = true;
                    iNumPossibleAlignments++;
                }
                if(bNegKPresent)
                {
                    bPotentialAlignments[8] = true;
                    iNumPossibleAlignments++;
                }
            } else
            {
                bPotentialAlignments[8] = true;
                bPotentialAlignments[9] = true;
                bPotentialAlignments[10] = true;
                bPotentialAlignments[11] = true;
                iNumPossibleAlignments = 4;
            }
        } else
        if(iFacing == 1)
        {
            if(bPosIPresent || bNegIPresent || bPosKPresent || bNegKPresent)
            {
                if(bPosIPresent)
                {
                    bPotentialAlignments[1] = true;
                    iNumPossibleAlignments++;
                }
                if(bNegIPresent)
                {
                    bPotentialAlignments[3] = true;
                    iNumPossibleAlignments++;
                }
                if(bPosKPresent)
                {
                    bPotentialAlignments[2] = true;
                    iNumPossibleAlignments++;
                }
                if(bNegKPresent)
                {
                    bPotentialAlignments[0] = true;
                    iNumPossibleAlignments++;
                }
            } else
            {
                bPotentialAlignments[0] = true;
                bPotentialAlignments[1] = true;
                bPotentialAlignments[2] = true;
                bPotentialAlignments[3] = true;
                iNumPossibleAlignments = 4;
            }
        } else
        if(iFacing == 2)
        {
            if(bPosIPresent || bNegIPresent)
            {
                if(bPosIPresent)
                {
                    bPotentialAlignments[6] = true;
                    iNumPossibleAlignments++;
                }
                if(bNegIPresent)
                {
                    bPotentialAlignments[7] = true;
                    iNumPossibleAlignments++;
                }
            } else
            {
                bPotentialAlignments[6] = true;
                bPotentialAlignments[7] = true;
                iNumPossibleAlignments = 2;
            }
        } else
        if(iFacing == 3)
        {
            if(bPosIPresent || bNegIPresent)
            {
                if(bPosIPresent)
                {
                    bPotentialAlignments[5] = true;
                    iNumPossibleAlignments++;
                }
                if(bNegIPresent)
                {
                    bPotentialAlignments[4] = true;
                    iNumPossibleAlignments++;
                }
            } else
            {
                bPotentialAlignments[4] = true;
                bPotentialAlignments[5] = true;
                iNumPossibleAlignments = 2;
            }
        } else
        if(iFacing == 4)
        {
            if(bPosKPresent || bNegKPresent)
            {
                if(bPosKPresent)
                {
                    bPotentialAlignments[6] = true;
                    iNumPossibleAlignments++;
                }
                if(bNegKPresent)
                {
                    bPotentialAlignments[5] = true;
                    iNumPossibleAlignments++;
                }
            } else
            {
                bPotentialAlignments[5] = true;
                bPotentialAlignments[6] = true;
                iNumPossibleAlignments = 2;
            }
        } else
        if(bPosKPresent || bNegKPresent)
        {
            if(bPosKPresent)
            {
                bPotentialAlignments[7] = true;
                iNumPossibleAlignments++;
            }
            if(bNegKPresent)
            {
                bPotentialAlignments[4] = true;
                iNumPossibleAlignments++;
            }
        } else
        {
            bPotentialAlignments[4] = true;
            bPotentialAlignments[7] = true;
            iNumPossibleAlignments = 2;
        }
        if(iNumPossibleAlignments > 0)
        {
            int iRandomAlignmentNum = world.rand.nextInt(iNumPossibleAlignments) + 1;
            int iTempIndex = 0;
            do
            {
                if(iTempIndex > 15)
                {
                    break;
                }
                if(bPotentialAlignments[iTempIndex] && --iRandomAlignmentNum <= 0)
                {
                    iAlignment = iTempIndex;
                    break;
                }
                iTempIndex++;
            } while(true);
        }
        SetMouldingAlignment(world, i, j, k, iAlignment);
    }

    public int GetMouldingAlignment(BlockView iBlockAccess, int i, int j, int k)
    {
        return iBlockAccess.getTileMeta(i, j, k);
    }

    public void SetMouldingAlignment(Level world, int i, int j, int k, int iAlignment)
    {
        world.setTileMeta(i, j, k, iAlignment);
    }

    private boolean IsValidNeighbour(Level world, int i, int j, int k)
    {
        int iid = world.getTileId(i, j, k);
        return !world.isAir(i, j, k) && iid != id;
    }

    private static final int iMouldingTextureID = 4;
    private static final float fMouldingWidth = 0.5F;
    private static final float fMouldingLength = 1F;

}
