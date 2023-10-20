// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCBlockAnchor.java

package net.kozibrodka.wolves.blocks;

import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.entity.MovingAnchorEntity;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.kozibrodka.wolves.tileentity.PulleyTileEntity;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;


public class Anchor extends TemplateBlockBase implements BlockWithWorldRenderer, BlockWithInventoryRenderer
{

    public Anchor(Identifier iid)
    {
        super(iid, Material.STONE);
        setHardness(2.0F);
        setSounds(STONE_SOUNDS);
    }

    public int getTextureForSide(int iSide, int iMetaData)
    {
        int iFacing = iMetaData;
        return iSide != iFacing && iSide != UnsortedUtils.GetOppositeFacing(iFacing) ? TextureListener.anchor_side : TextureListener.anchor_top;
    }

//    public int getTextureForSide(int iSide)
//    {
//        if(iSide == 1)
//        {
//            return TextureListener.anchor_top;
//        }
//        return iSide != 0 ? TextureListener.anchor_side : TextureListener.anchor_top;
//    }

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

    public void method_1605()
    {
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
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
        return iBlockAccess.getTileMeta(i, j, k);
    }

    public void SetAnchorFacing(Level world, int i, int j, int k, int iFacing)
    {
        world.setTileMeta(i, j, k, iFacing);
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
            if(iTempid != BlockListener.fcRopeBlock.id)
            {
                break;
            }
            if(world.getTileId(i, tempj - 1, k) != BlockListener.fcRopeBlock.id)
            {
                AddRopeToPlayerInventory(world, i, j, k, entityPlayer);
                BlockBase targetBlock = BlockListener.fcRopeBlock;
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
            UnsortedUtils.EjectStackWithRandomOffset(world, i, j, k, ropeStack);
        }
    }

    public void NotifyAnchorBlockOfAttachedPulleyStateChange(PulleyTileEntity tileEntityPulley, Level world, int i, int j, int k)
    {
        int iMovementDirection = 0;
        if(tileEntityPulley.IsRaising())
        {
            if(world.getTileId(i, j + 1, k) == BlockListener.fcRopeBlock.id)
            {
                iMovementDirection = 1;
            }
        } else
        if(tileEntityPulley.IsLowering() && (world.isAir(i, j - 1, k) || world.getTileId(i, j - 1, k) == BlockListener.fcPlatform.id))
        {
            iMovementDirection = -1;
        }
        if(iMovementDirection != 0)
        {
            ConvertAnchorToEntity(world, i, j, k, tileEntityPulley, iMovementDirection);
        }
    }

    private void ConvertAnchorToEntity(Level world, int i, int j, int k, PulleyTileEntity attachedTileEntityPulley, int iMovementDirection)
    {
        BlockPosition pulleyPos = new BlockPosition(attachedTileEntityPulley.x, attachedTileEntityPulley.y, attachedTileEntityPulley.z);
        MovingAnchorEntity entityAnchor = new MovingAnchorEntity(world, (float)i + 0.5F, (float)j + fAnchorBaseHeight / 2.0F, (float)k + 0.5F, pulleyPos, iMovementDirection);
        world.spawnEntity(entityAnchor);
        ConvertConnectedPlatformsToEntities(world, i, j, k, entityAnchor);
        world.setTile(i, j, k, 0);
    }

    private void ConvertConnectedPlatformsToEntities(Level world, int i, int j, int k, MovingAnchorEntity associatedAnchorEntity)
    {
        int iTargetJ = j - 1;
        int iTargetid = world.getTileId(i, iTargetJ, k);
        if(iTargetid == BlockListener.fcPlatform.id)
        {
            ((Platform)BlockListener.fcPlatform).CovertToEntitiesFromThisPlatform(world, i, iTargetJ, k, associatedAnchorEntity);
        }
    }

    public static float fAnchorBaseHeight = 0.375F;
    public final int iAnchorLoopTextureIndex = 39;
    private final int iAnchorBaseTopAndBottomTextureIndex = 40;
    private final int iAnchorBaseSideTextureIndex = 41;
    public final int iAnchorRopeTextureIndex = 32;

    @Override
    public boolean renderWorld(BlockRenderer tileRenderer, BlockView tileView, int x, int y, int z) {
        float f = 0.5F;
        float f1 = 0.5F;
        float f2 = fAnchorBaseHeight;
        int l = GetAnchorFacing(tileView, x, y, z);
        switch(l)
        {
            case 0: // '\0'
                this.setBoundingBox(0.5F - f1, 1.0F - f2, 0.5F - f, 0.5F + f1, 1.0F, 0.5F + f);
                break;

            case 1: // '\001'
                this.setBoundingBox(0.5F - f1, 0.0F, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
                break;

            case 2: // '\002'
                this.setBoundingBox(0.5F - f1, 0.5F - f, 1.0F - f2, 0.5F + f1, 0.5F + f, 1.0F);
                break;

            case 3: // '\003'
                this.setBoundingBox(0.5F - f1, 0.5F - f, 0.0F, 0.5F + f1, 0.5F + f, f2);
                break;

            case 4: // '\004'
                this.setBoundingBox(1.0F - f2, 0.5F - f1, 0.5F - f, 1.0F, 0.5F + f1, 0.5F + f);
                break;

            default:
                this.setBoundingBox(0.0F, 0.5F - f1, 0.5F - f, f2, 0.5F + f1, 0.5F + f);
                break;
        }
        tileRenderer.renderStandardBlock(this, x, y, z);
        f = 0.125F;
        f1 = 0.125F;
        f2 = 0.25F;
        this.setBoundingBox(0.5F - f1, fAnchorBaseHeight, 0.5F - f, 0.5F + f1, fAnchorBaseHeight + f2, 0.5F + f);
        CustomBlockRendering.RenderStandardBlockWithTexture(tileRenderer, this, x, y, z, TextureListener.anchor_button);
        boolean flag = false;
        f = 0.0625F;
        f1 = 0.0625F;
        f2 = fAnchorBaseHeight;
        if(l == 1)
        {
            int i1 = tileView.getTileId(x, y + 1, z);
            if(i1 == BlockListener.fcRopeBlock.id || i1 == BlockListener.fcPulley.id)
            {
                this.setBoundingBox(0.5F - f1, f2, 0.5F - f, 0.5F + f1, 1.0F, 0.5F + f);
                flag = true;
            }
        } else
        if(tileView.getTileId(x, y - 1, z) == BlockListener.fcRopeBlock.id)
        {
            this.setBoundingBox(0.5F - f1, 0.0F, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
            flag = true;
        }
        if(flag)
        {
            CustomBlockRendering.RenderStandardBlockWithTexture(tileRenderer, this, x, y, z, TextureListener.rope);
        }
        return true;
    }

    @Override
    public void renderInventory(BlockRenderer tileRenderer, int meta) {
        this.method_1605();
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.25F, -0.5F, 1);
        float f = 0.125F;
        float f1 = 0.125F;
        float f2 = 0.25F;
        this.setBoundingBox(0.5F - f1, fAnchorBaseHeight, 0.5F - f, 0.5F + f1, fAnchorBaseHeight + f2, 0.5F + f);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.25F, -0.5F, TextureListener.anchor_button);
    }
}
