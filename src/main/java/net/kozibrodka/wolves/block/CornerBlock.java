
package net.kozibrodka.wolves.block;

import net.minecraft.block.Material;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class CornerBlock extends TemplateBlock
{

    public CornerBlock(Identifier iid)
    {
        super(iid, Material.WOOD);
        setHardness(0.5F);
        setSoundGroup(WOOD_SOUND_GROUP);
        textureId = 4;
    }

    public boolean isOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public Box getCollisionShape(World world, int i, int j, int k)
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
        return Box.createCached(minX, minY, minZ, maxX, maxY, maxZ);
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

    public void onPlaced(World world, int i, int j, int k, int iFacing)
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

    public void setupRenderBoundingBox()
    {
        setBoundingBox(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int l)
    {
        return 0;
    }

    public void SetFacing(World world1, int l, int i1, int j1, int k1)
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

    public void Rotate(World world1, int l, int i1, int j1, boolean flag)
    {
    }

    public int GetCornerAlignment(BlockView iBlockAccess, int i, int j, int k)
    {
        return iBlockAccess.getBlockMeta(i, j, k) & 7;
    }

    public boolean IsIOffset(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getBlockMeta(i, j, k) & 4) > 0;
    }

    public boolean IsJOffset(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getBlockMeta(i, j, k) & 2) > 0;
    }

    public boolean IsKOffset(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getBlockMeta(i, j, k) & 1) > 0;
    }

    public void SetCornerAlignment(World world, int i, int j, int k, int iAlignment)
    {
        int iMetaData = world.getBlockMeta(i, j, k) & 8;
        iMetaData |= iAlignment;
        world.method_215(i, j, k, iMetaData);
    }

    public void SetCornerAlignment(World world, int i, int j, int k, boolean bIAligned, boolean bJAligned, boolean bKAligned)
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

    private boolean GetIOffsetOnPlaceFromNeighbours(World world, int i, int j, int k)
    {
        if(!world.method_234(i + 1, j, k))
        {
            if(!world.method_234(i - 1, j, k))
            {
                return world.field_214.nextInt(2) > 0;
            } else
            {
                return true;
            }
        }
        if(world.method_234(i - 1, j, k))
        {
            return world.field_214.nextInt(2) > 0;
        } else
        {
            return false;
        }
    }

    private boolean GetJOffsetOnPlaceFromNeighbours(World world, int i, int j, int k)
    {
        if(!world.method_234(i, j + 1, k))
        {
            if(!world.method_234(i, j - 1, k))
            {
                return world.field_214.nextInt(2) > 0;
            } else
            {
                return true;
            }
        }
        if(world.method_234(i, j - 1, k))
        {
            return world.field_214.nextInt(2) > 0;
        } else
        {
            return false;
        }
    }

    private boolean GetKOffsetOnPlaceFromNeighbours(World world, int i, int j, int k)
    {
        if(!world.method_234(i, j, k + 1))
        {
            if(!world.method_234(i, j, k - 1))
            {
                return world.field_214.nextInt(2) > 0;
            } else
            {
                return true;
            }
        }
        if(world.method_234(i, j, k - 1))
        {
            return world.field_214.nextInt(2) > 0;
        } else
        {
            return false;
        }
    }

    private static final int iCornerTextureID = 4;
    private static final float fCornerWidth = 0.5F;
    private static final float fCornerWidthOffset = 0.5F;
}
