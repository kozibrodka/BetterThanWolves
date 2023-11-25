// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCBlockCorner.java

package net.kozibrodka.wolves.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

// Referenced classes of package net.minecraft.src:
//            Block, Material, World, Box, 
//            IBlockAccess

public class Corner extends TemplateBlockBase
{

    public Corner(Identifier iid)
    {
        super(iid, Material.WOOD);
        setHardness(0.5F);
        setSounds(WOOD_SOUNDS);
        texture = 4;
    }

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public Box getCollisionShape(Level world, int i, int j, int k)
    {
        float minX = i;
        float maxX = minX + 0.5F;
        float minY = j;
        float maxY = minY + 0.5F;
        float minZ = k;
        float maxZ = minZ + 0.5F;
        if(IsIOffset(world, i, j, k))
        {
            minX += 0.5F;
            maxX += 0.5F;
        }
        if(IsJOffset(world, i, j, k))
        {
            minY += 0.5F;
            maxY += 0.5F;
        }
        if(IsKOffset(world, i, j, k))
        {
            minZ += 0.5F;
            maxZ += 0.5F;
        }
        return Box.createButWasteMemory(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public void updateBoundingBox(BlockView iblockaccess, int i, int j, int k)
    {
        float minX = 0.0F;
        float maxX = minX + 0.5F;
        float minY = 0.0F;
        float maxY = minY + 0.5F;
        float minZ = 0.0F;
        float maxZ = minZ + 0.5F;
        if(IsIOffset(iblockaccess, i, j, k))
        {
            minX += 0.5F;
            maxX += 0.5F;
        }
        if(IsJOffset(iblockaccess, i, j, k))
        {
            minY += 0.5F;
            maxY += 0.5F;
        }
        if(IsKOffset(iblockaccess, i, j, k))
        {
            minZ += 0.5F;
            maxZ += 0.5F;
        }
        setBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public void onBlockPlaced(Level world, int i, int j, int k, int iFacing)
    {
        boolean bIOffset = false;
        boolean bJOffset = false;
        boolean bKOffset = false;
        if(iFacing == 0)
        {
            bJOffset = true;
            bIOffset = GetIOffsetOnPlaceFromNeighbours(world, i, j, k);
            bKOffset = GetKOffsetOnPlaceFromNeighbours(world, i, j, k);
        } else
        if(iFacing == 1)
        {
            bIOffset = GetIOffsetOnPlaceFromNeighbours(world, i, j, k);
            bKOffset = GetKOffsetOnPlaceFromNeighbours(world, i, j, k);
        } else
        if(iFacing == 2)
        {
            bKOffset = true;
            bIOffset = GetIOffsetOnPlaceFromNeighbours(world, i, j, k);
            bJOffset = GetJOffsetOnPlaceFromNeighbours(world, i, j, k);
        } else
        if(iFacing == 3)
        {
            bIOffset = GetIOffsetOnPlaceFromNeighbours(world, i, j, k);
            bJOffset = GetJOffsetOnPlaceFromNeighbours(world, i, j, k);
        } else
        if(iFacing == 4)
        {
            bIOffset = true;
            bJOffset = GetJOffsetOnPlaceFromNeighbours(world, i, j, k);
            bKOffset = GetKOffsetOnPlaceFromNeighbours(world, i, j, k);
        } else
        if(iFacing == 5)
        {
            bJOffset = GetJOffsetOnPlaceFromNeighbours(world, i, j, k);
            bKOffset = GetKOffsetOnPlaceFromNeighbours(world, i, j, k);
        }
        SetCornerAlignment(world, i, j, k, bIOffset, bJOffset, bKOffset);
    }

    public void method_1605()
    {
        setBoundingBox(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int l)
    {
        return 0;
    }

    public void SetFacing(Level world1, int l, int i1, int j1, int k1)
    {
    }

    public boolean CanRotate()
    {
        return false;
    }

    public boolean CanTransmitRotation()
    {
        return false;
    }

    public void Rotate(Level world1, int l, int i1, int j1, boolean flag)
    {
    }

    public int GetCornerAlignment(BlockView iBlockAccess, int i, int j, int k)
    {
        return iBlockAccess.getTileMeta(i, j, k) & 7;
    }

    public boolean IsIOffset(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getTileMeta(i, j, k) & 4) > 0;
    }

    public boolean IsJOffset(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getTileMeta(i, j, k) & 2) > 0;
    }

    public boolean IsKOffset(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getTileMeta(i, j, k) & 1) > 0;
    }

    public void SetCornerAlignment(Level world, int i, int j, int k, int iAlignment)
    {
        int iMetaData = world.getTileMeta(i, j, k) & 8;
        iMetaData |= iAlignment;
        world.setTileMeta(i, j, k, iMetaData);
    }

    public void SetCornerAlignment(Level world, int i, int j, int k, boolean bIAligned, boolean bJAligned, boolean bKAligned)
    {
        int iAlignment = 0;
        if(bIAligned)
        {
            iAlignment |= 4;
        }
        if(bJAligned)
        {
            iAlignment |= 2;
        }
        if(bKAligned)
        {
            iAlignment |= 1;
        }
        SetCornerAlignment(world, i, j, k, iAlignment);
    }

    private boolean GetIOffsetOnPlaceFromNeighbours(Level world, int i, int j, int k)
    {
        if(!world.isAir(i + 1, j, k))
        {
            if(!world.isAir(i - 1, j, k))
            {
                return world.rand.nextInt(2) > 0;
            } else
            {
                return true;
            }
        }
        if(world.isAir(i - 1, j, k))
        {
            return world.rand.nextInt(2) > 0;
        } else
        {
            return false;
        }
    }

    private boolean GetJOffsetOnPlaceFromNeighbours(Level world, int i, int j, int k)
    {
        if(!world.isAir(i, j + 1, k))
        {
            if(!world.isAir(i, j - 1, k))
            {
                return world.rand.nextInt(2) > 0;
            } else
            {
                return true;
            }
        }
        if(world.isAir(i, j - 1, k))
        {
            return world.rand.nextInt(2) > 0;
        } else
        {
            return false;
        }
    }

    private boolean GetKOffsetOnPlaceFromNeighbours(Level world, int i, int j, int k)
    {
        if(!world.isAir(i, j, k + 1))
        {
            if(!world.isAir(i, j, k - 1))
            {
                return world.rand.nextInt(2) > 0;
            } else
            {
                return true;
            }
        }
        if(world.isAir(i, j, k - 1))
        {
            return world.rand.nextInt(2) > 0;
        } else
        {
            return false;
        }
    }

    private static final int iCornerTextureID = 4;
    private static final float fCornerWidth = 0.5F;
    private static final float fCornerWidthOffset = 0.5F;
}
