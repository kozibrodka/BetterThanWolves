// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCBlockAnchor.java

package net.kozibrodka.wolves.blocks;

import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.entity.FCEntityMovingAnchor;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.tileentity.FCTileEntityPulley;
import net.kozibrodka.wolves.utils.FCBlockPos;
import net.kozibrodka.wolves.utils.FCUtilsMisc;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
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


public class FCBlockAnchor extends TemplateBlockBase
{

    public FCBlockAnchor(Identifier iid)
    {
        super(iid, Material.STONE);
        setHardness(2.0F);
        setSounds(STONE_SOUNDS);
        texture = 41;
    }

    public Box getCollisionShape(Level world, int i, int j, int k)
    {
        int iFacing = GetAnchorFacing(world, i, j, k);
        switch(iFacing)
        {
        case 0: // '\0'
            return Box.createButWasteMemory((float)i, ((float)j + 1.0F) - fAnchorBaseHeight, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);

        case 1: // '\001'
            return Box.createButWasteMemory((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + fAnchorBaseHeight, (float)k + 1.0F);

        case 2: // '\002'
            return Box.createButWasteMemory((float)i, (float)j, ((float)k + 1.0F) - fAnchorBaseHeight, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);

        case 3: // '\003'
            return Box.createButWasteMemory((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + fAnchorBaseHeight);

        case 4: // '\004'
            return Box.createButWasteMemory(((float)i + 1.0F) - fAnchorBaseHeight, (float)j, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);
        }
        return Box.createButWasteMemory((float)i, (float)j, (float)k, (float)i + fAnchorBaseHeight, (float)j + 1.0F, (float)k + 1.0F);
    }

    public void updateBoundingBox(BlockView iblockaccess, int i, int j, int k)
    {
        int iFacing = GetAnchorFacing(iblockaccess, i, j, k);
        switch(iFacing)
        {
        case 0: // '\0'
            setBoundingBox(0.0F, 0.35F, 0.0F, 1.0F, 1.0F, 1.0F);
            break;

        case 1: // '\001'
            setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.65F, 1.0F);
            break;

        case 2: // '\002'
            setBoundingBox(0.0F, 0.0F, 0.35F, 1.0F, 1.0F, 1.0F);
            break;

        case 3: // '\003'
            setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.65F);
            break;

        case 4: // '\004'
            setBoundingBox(0.35F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            break;

        default:
            setBoundingBox(0.0F, 0.0F, 0.0F, 0.65F, 1.0F, 1.0F);
            break;
        }
    }

    public int getTextureForSide(int i, int j)
    {
//        int k = j;
//        return i != k && i != FCUtilsMisc.GetOppositeFacing(k) ? 41 : 40;
        if(i == 0 || i == 1)
        {
            return 1;
        }else{
            return 2;
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

    public void onBlockPlaced(Level world, int i, int j, int k, int iFacing)
    {
        int iAnchorFacing = iFacing;
        SetAnchorFacing(world, i, j, k, iAnchorFacing);
    }

    public boolean canUse(Level world, int i, int j, int k, PlayerBase entityPlayer)
    {
        if(world.isServerSide)
        {
            return true;
        }
        ItemInstance playerEquippedItem = entityPlayer.getHeldItem();
        boolean bRopeEquipped = false;
        if(playerEquippedItem != null && playerEquippedItem.itemId == mod_FCBetterThanWolves.fcRopeItem.id)
        {
            bRopeEquipped = true;
        }
        if(!bRopeEquipped)
        {
            RetractRope(world, i, j, k, entityPlayer);
            return true;
        } else
        {
            return false;
        }
    }

    public int GetAnchorFacing(BlockView iBlockAccess, int i, int j, int k)
    {
//        return iBlockAccess.getTileMeta(i, j, k);
        Level level = Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).level;
        if(level.getTileId(i,j,k) == mod_FCBetterThanWolves.fcAnchor.id) {
            return (level.getBlockState(i, j, k).get(FACING));
        }else{
            return 0;
        }
    }

    public void SetAnchorFacing(Level world, int i, int j, int k, int iFacing)
    {
        BlockState currentState = world.getBlockState(i, j, k);
        world.setBlockStateWithNotify(i,j,k, currentState.with(FACING, iFacing));
//        world.setTileMeta(i, j, k, iFacing);
    }

    void RetractRope(Level world, int i, int j, int k, PlayerBase entityPlayer)
    {
        int tempj = j - 1;
        do
        {
            if(tempj < 0)
            {
                break;
            }
            int iTempid = world.getTileId(i, tempj, k);
            if(iTempid != mod_FCBetterThanWolves.fcRopeBlock.id)
            {
                break;
            }
            if(world.getTileId(i, tempj - 1, k) != mod_FCBetterThanWolves.fcRopeBlock.id)
            {
                AddRopeToPlayerInventory(world, i, j, k, entityPlayer);
                BlockBase targetBlock = mod_FCBetterThanWolves.fcRopeBlock;
                Minecraft.class.cast(FabricLoader.getInstance().getGameInstance()).soundHelper.playSound(targetBlock.sounds.getWalkSound(), (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, (targetBlock.sounds.getVolume() + 1.0F) / 2.0F, targetBlock.sounds.getPitch() * 0.8F);
                world.setTile(i, tempj, k, 0);
                break;
            }
            tempj--;
        } while(true);
    }

    private void AddRopeToPlayerInventory(Level world, int i, int j, int k, PlayerBase entityPlayer)
    {
        ItemInstance ropeStack = new ItemInstance(mod_FCBetterThanWolves.fcRopeItem);
        if(entityPlayer.inventory.addStack(ropeStack))
        {
            world.playSound(entityPlayer, "random.pop", 0.2F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        } else
        {
            FCUtilsMisc.EjectStackWithRandomOffset(world, i, j, k, ropeStack);
        }
    }

    public void NotifyAnchorBlockOfAttachedPulleyStateChange(FCTileEntityPulley tileEntityPulley, Level world, int i, int j, int k)
    {
        int iMovementDirection = 0;
        if(tileEntityPulley.IsRaising())
        {
            if(world.getTileId(i, j + 1, k) == mod_FCBetterThanWolves.fcRopeBlock.id)
            {
                iMovementDirection = 1;
            }
        } else
        if(tileEntityPulley.IsLowering() && (world.isAir(i, j - 1, k) || world.getTileId(i, j - 1, k) == mod_FCBetterThanWolves.fcPlatform.id))
        {
            iMovementDirection = -1;
        }
        if(iMovementDirection != 0)
        {
            ConvertAnchorToEntity(world, i, j, k, tileEntityPulley, iMovementDirection);
        }
    }

    private void ConvertAnchorToEntity(Level world, int i, int j, int k, FCTileEntityPulley attachedTileEntityPulley, int iMovementDirection)
    {
        FCBlockPos pulleyPos = new FCBlockPos(attachedTileEntityPulley.x, attachedTileEntityPulley.y, attachedTileEntityPulley.z);
        FCEntityMovingAnchor entityAnchor = new FCEntityMovingAnchor(world, (float)i + 0.5F, (float)j + fAnchorBaseHeight / 2.0F, (float)k + 0.5F, pulleyPos, iMovementDirection);
        world.spawnEntity(entityAnchor);
        ConvertConnectedPlatformsToEntities(world, i, j, k, entityAnchor);
        world.setTile(i, j, k, 0);
    }

    private void ConvertConnectedPlatformsToEntities(Level world, int i, int j, int k, FCEntityMovingAnchor associatedAnchorEntity)
    {
        int iTargetJ = j - 1;
        int iTargetid = world.getTileId(i, iTargetJ, k);
        if(iTargetid == mod_FCBetterThanWolves.fcPlatform.id)
        {
            ((FCBlockPlatform)mod_FCBetterThanWolves.fcPlatform).CovertToEntitiesFromThisPlatform(world, i, iTargetJ, k, associatedAnchorEntity);
        }
    }

    public static float fAnchorBaseHeight = 0.375F;
    public final int iAnchorLoopTextureIndex = 39;
    private final int iAnchorBaseTopAndBottomTextureIndex = 40;
    private final int iAnchorBaseSideTextureIndex = 41;
    public final int iAnchorRopeTextureIndex = 32;

    /**
     * STATES
     */
    public static final IntProperty FACING = IntProperty.of("facing", 0, 5);

    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder){
        builder.add(FACING);
    }
}
