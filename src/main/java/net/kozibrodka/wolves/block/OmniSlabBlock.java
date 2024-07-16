package net.kozibrodka.wolves.block;


import net.kozibrodka.wolves.itemblocks.OmniSlabBlockItem;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.HasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

@HasCustomBlockItemFactory(OmniSlabBlockItem.class)
public class OmniSlabBlock extends TemplateBlock
{

    public OmniSlabBlock(Identifier iid)
    {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSoundGroup(WOOD_SOUND_GROUP);
    }

    protected int getDroppedItemMeta(int iMetaData)
    {
        return iMetaData & 1;
    }

    public int getTexture(int iSide, int iMetaData)
    {
        return (iMetaData & 1) <= 0 ? 1 : 4;
    }

    public int getTexture(int iSide)
    {
        return getTexture(iSide, 0);
    }

    public Box getCollisionShape(World world, int i, int j, int k)
    {
        int iFacing = GetSlabFacing(world, i, j, k);
        switch(iFacing)
        {
        case 0: // '\0'
            return Box.createCached((float)i, ((float)j + 1.0F) - fSlabHeight, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);

        case 1: // '\001'
            return Box.createCached((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + fSlabHeight, (float)k + 1.0F);

        case 2: // '\002'
            return Box.createCached((float)i, (float)j, ((float)k + 1.0F) - fSlabHeight, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);

        case 3: // '\003'
            return Box.createCached((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + fSlabHeight);

        case 4: // '\004'
            return Box.createCached(((float)i + 1.0F) - fSlabHeight, (float)j, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);
        }
        return Box.createCached((float)i, (float)j, (float)k, (float)i + fSlabHeight, (float)j + 1.0F, (float)k + 1.0F);
    }

    public void updateBoundingBox(BlockView iblockaccess, int i, int j, int k)
    {
        int iFacing = GetSlabFacing(iblockaccess, i, j, k);
        switch(iFacing)
        {
        case 0: // '\0'
            setBoundingBox(0.0F, fSlabHeight, 0.0F, 1.0F, 1.0F, 1.0F);
            break;

        case 1: // '\001'
            setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, fSlabHeight, 1.0F);
            break;

        case 2: // '\002'
            setBoundingBox(0.0F, 0.0F, fSlabHeight, 1.0F, 1.0F, 1.0F);
            break;

        case 3: // '\003'
            setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, fSlabHeight);
            break;

        case 4: // '\004'
            setBoundingBox(fSlabHeight, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            break;

        default:
            setBoundingBox(0.0F, 0.0F, 0.0F, fSlabHeight, 1.0F, 1.0F);
            break;
        }
    }

    public void setupRenderBoundingBox()
    {
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, fSlabHeight);
    }

    public boolean isOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }


    public void onPlaced(World world, int i, int j, int k, int iFacing)
    {
        int iSlabFacing = iFacing;
        SetSlabFacing(world, i, j, k, iSlabFacing);
    }

    public int GetSlabFacing(BlockView iBlockAccess, int i, int j, int k)
    {
        return iBlockAccess.getBlockMeta(i, j, k) >> 1;
    }

    public void SetSlabFacing(World world, int i, int j, int k, int iFacing)
    {
        int iMetaData = world.getBlockMeta(i, j, k);
        iMetaData &= 1;
        iMetaData |= iFacing << 1;
        world.setBlockMeta(i, j, k, iMetaData);
    }

    public boolean IsSlabWood(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getBlockMeta(i, j, k) & 1) > 0;
    }

    public static float fSlabHeight = 0.5F;
    private static final int iStoneSlabTextureID = 1;
    private static final int iWoodSlabTextureID = 4;

}
