package net.kozibrodka.wolves.blocks;


import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.tileentity.FCTileEntityUnfiredPottery;
import net.kozibrodka.wolves.utils.FCIBlock;
import net.kozibrodka.wolves.utils.FCUtilsMisc;
import net.minecraft.block.material.Material;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;

public class FCBlockUnfiredPottery extends TemplateBlockWithEntity
   implements FCIBlock
{

    public FCBlockUnfiredPottery(Identifier iid)
    {
        super(iid, Material.CLAY);
        texture = 75;
        setHardness(0.6F);
        setSounds(GRAVEL_SOUNDS);
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
        int iTileID = world.getTileId(i, j, k);
        if(iTileID == mod_FCBetterThanWolves.fcUnfiredPottery_vase.id)
        {
            return Box.createButWasteMemory((float)i + 0.1875F, (float)j, (float)k + 0.1875F, (float)i + 0.8125F, (float)j + 1.0F, (float)k + 0.8125F);
        }else{
            return Box.createButWasteMemory((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);
        }
//        switch(iTileID)
//        {
//        case 0: // '\0'
//        case 1: // '\001'
//            return Box.createButWasteMemory((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);
//
//        case 2: // '\002'
//            return Box.createButWasteMemory((float)i + 0.1875F, (float)j, (float)k + 0.1875F, (float)i + 0.8125F, (float)j + 1.0F, (float)k + 0.8125F);
//        }
//        return Box.createButWasteMemory((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);
    }

    public void updateBoundingBox(BlockView iBlockAccess, int i, int j, int k)
    {
        int iTileID = iBlockAccess.getTileId(i, j, k);
        if(iTileID == mod_FCBetterThanWolves.fcUnfiredPottery_vase.id)
        {
            setBoundingBox(0.1875F, 0.0F, 0.1875F, 0.8125F, 1.0F, 0.8125F);
        }else{
            setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
//        int iMetaData = iBlockAccess.getTileMeta(i, j, k);
//        switch(iMetaData)
//        {
//        case 0: // '\0'
//        case 1: // '\001'
//            setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
//            break;
//
//        case 2: // '\002'
//            setBoundingBox(0.1875F, 0.0F, 0.1875F, 0.8125F, 1.0F, 0.8125F);
//            break;
//
//        default:
//            setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
//            break;
//        }
    }

    protected TileEntityBase createTileEntity()
    {
        return new FCTileEntityUnfiredPottery();
    }

    public int GetFacing(BlockView iBlockAccess, int i, int j, int l)
    {
        return 0;
    }

    public void SetFacing(Level world1, int l, int i1, int j1, int k1)
    {
    }

    public boolean CanRotate(BlockView iBlockAccess, int i, int j, int l)
    {
        return false;
    }

    public boolean CanTransmitRotation(BlockView iBlockAccess, int i, int j, int l)
    {
        return false;
    }

    public void Rotate(Level world1, int l, int i1, int j1, boolean flag)
    {
    }

    public void Cook_old(Level world, int i, int j, int k)
    {
        int iMetaData = world.getTileMeta(i, j, k);
        int iNewid = 0;
        switch(iMetaData)
        {
        case 0: // '\0'
            iNewid = mod_FCBetterThanWolves.fcCrucible.id;
            break;

        case 1: // '\001'
            iNewid = mod_FCBetterThanWolves.fcPlanter.id;
            break;

        case 2: // '\002'
            iNewid = mod_FCBetterThanWolves.fcVase_white.id;
            break;
        }
        world.setTile(i, j, k, 0);
        if(iNewid > 0)
        {
            FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, iNewid, 0);
        }
    }

    public void Cook(Level world, int i, int j, int k)
    {
        int iTileId = world.getTileId(i, j, k);
        int iNewid = 0;
        if(iTileId == mod_FCBetterThanWolves.fcUnfiredPottery_crucible.id)
        {
            iNewid = mod_FCBetterThanWolves.fcCrucible.id;
        }
        if(iTileId == mod_FCBetterThanWolves.fcUnfiredPottery_planter.id)
        {
            iNewid = mod_FCBetterThanWolves.fcPlanter.id;
        }
        if(iTileId == mod_FCBetterThanWolves.fcUnfiredPottery_vase.id)
        {
            iNewid = mod_FCBetterThanWolves.fcVase_white.id;
        }
        world.setTile(i, j, k, 0);
        if(iNewid > 0)
        {
            FCUtilsMisc.EjectSingleItemWithRandomOffset(world, i, j, k, iNewid, 0);
        }
    }

    public static final float m_fUnfiredPotteryCrucibleHeight = 1F;
    public static final float m_fUnfiredPotteryCrucibleWidth = 0.875F;
    public static final float m_fUnfiredPotteryCrucibleHalfWidth = 0.4375F;
    public static final float m_fUnfiredPotteryCrucibleBandHeight = 0.75F;
    public static final float m_fUnfiredPotteryCrucibleBandHalfHeight = 0.375F;
    public static final float m_fUnfiredPotteryPotWidth = 0.75F;
    public static final float m_fUnfiredPotteryPotHalfWidth = 0.375F;
    public static final float m_fUnfiredPotteryPotBandHeight = 0.3125F;
    public static final float m_fUnfiredPotteryPotBandHalfHeight = 0.15625F;
    public static final float m_fUnfiredPotteryVaseBaseWidth = 0.5F;
    public static final float m_fUnfiredPotteryVaseBaseHalfWidth = 0.25F;
    public static final float m_fUnfiredPotteryVaseBaseHeight = 0.0625F;
    public static final float m_fUnfiredPotteryVaseBodyWidth = 0.625F;
    public static final float m_fUnfiredPotteryVaseBodyHalfWidth = 0.3125F;
    public static final float m_fUnfiredPotteryVaseBodyHeight = 0.375F;
    public static final float m_fUnfiredPotteryVaseNeckBaseWidth = 0.5F;
    public static final float m_fUnfiredPotteryVaseNeckBaseHalfWidth = 0.25F;
    public static final float m_fUnfiredPotteryVaseNeckBaseHeight = 0.0625F;
    public static final float m_fUnfiredPotteryVaseNeckWidth = 0.25F;
    public static final float m_fUnfiredPotteryVaseNeckHalfWidth = 0.125F;
    public static final float m_fUnfiredPotteryVaseNeckHeight = 0.4375F;
    public static final float m_fUnfiredPotteryVaseTopWidth = 0.375F;
    public static final float m_fUnfiredPotteryVaseTopHalfWidth = 0.1875F;
    public static final float m_fUnfiredPotteryVaseTopHeight = 0.0625F;
    private final int iUnfiredPotteryCookingTexture = 76;
}
