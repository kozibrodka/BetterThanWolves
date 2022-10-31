package net.kozibrodka.wolves.blocks;


import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.utils.FCUtilsMisc;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.sql.SQLOutput;

public class FCBlockOmniSlab extends TemplateBlockBase
{

    public FCBlockOmniSlab(Identifier iid)
    {
        super(iid, Material.WOOD);
        setHardness(2.0F);
        setSounds(WOOD_SOUNDS);
        texture = 0;
    }

    public Box getCollisionShape(Level world, int i, int j, int k)
    {
        int iFacing = GetSlabFacing(world, i, j, k);
        switch(iFacing)
        {
        case 0: // '\0'
            return Box.createButWasteMemory((float)i, ((float)j + 1.0F) - fSlabHeight, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);

        case 1: // '\001'
            return Box.createButWasteMemory((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + fSlabHeight, (float)k + 1.0F);

        case 2: // '\002'
            return Box.createButWasteMemory((float)i, (float)j, ((float)k + 1.0F) - fSlabHeight, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);

        case 3: // '\003'
            return Box.createButWasteMemory((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + fSlabHeight);

        case 4: // '\004'
            return Box.createButWasteMemory(((float)i + 1.0F) - fSlabHeight, (float)j, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);
        }
        return Box.createButWasteMemory((float)i, (float)j, (float)k, (float)i + fSlabHeight, (float)j + 1.0F, (float)k + 1.0F);
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

    public boolean isFullOpaque()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

//    public void onBlockPlaced(Level world, int i, int j, int k)
//    {
//        int iBlock_a = world.getTileId(i - 1, j, k);
//        if(iBlockBelowID == id)
//        {
//
//        }
//    }

    public boolean useOnTile(ItemInstance itemstack, PlayerBase playerbase, Level level, int var4, int var5, int var6, int var7) {
        System.out.println("USEONYILE");
        if (itemstack.count == 0) {
            return false;
        }
        return false;
    }

    public void onBlockPlaced(Level world, int i, int j, int k, int iFacing)
    {
        int iSlabFacing = iFacing;
        SetSlabFacing(world, i, j, k, iSlabFacing);
    }

    public void afterPlaced(Level world, int i, int j, int k, Living entityLiving)
    {
        int stejt = world.getBlockState(i,j,k).get(FACING);
        if(stejt < 2){
            SetSlabFacing(world, i, j, k, FCUtilsMisc.ConvertPlacingEntityOrientationToFlatBlockFacing(entityLiving));
        }
    }

    public int GetSlabFacing(BlockView iBlockAccess, int i, int j, int k)
    {
        Level level = Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).level;
        int iTargetid = level.getTileId(i, j, k);
        BlockBase targetBlock = BlockBase.BY_ID[iTargetid];
        if(targetBlock instanceof FCBlockOmniSlab) {
            return level.getBlockState(i, j, k).get(FACING);
        }else{
            return 0;
        }
//        return iBlockAccess.getTileMeta(i, j, k) >> 1;
    }

    public void SetSlabFacing(Level world, int i, int j, int k, int iFacing)
    {
        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i,j,k, currentState.with(FACING, iFacing));
//    	int iMetaData = world.getTileMeta(i, j, k);
//    	iMetaData &= 1;
//        iMetaData |= iFacing << 1;
//        world.setTileMeta(i, j, k, iMetaData);
    }

    //TODO: logic change unnecessary method
    public boolean IsSlabWood(BlockView iBlockAccess, int i, int j, int k)
    {
        return (iBlockAccess.getTileMeta(i, j, k) & 1) > 0;
    }

    public static float fSlabHeight = 0.5F;
    private static final int iStoneSlabTextureID = 1;
    private static final int iWoodSlabTextureID = 4;

    /**
     * STATES
     */
    public static final IntProperty FACING = IntProperty.of("facing", 0, 5);

    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder){
        builder.add(FACING);
    }

}
