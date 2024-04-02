// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
// Source File Name:   FCBlockAnchor.java

package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.FabricLoader;
import net.kozibrodka.wolves.entity.MovingAnchorEntity;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.ItemListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.block.entity.PulleyBlockEntity;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;


public class AnchorBlock extends TemplateBlock implements BlockWithWorldRenderer, BlockWithInventoryRenderer
{

    public AnchorBlock(Identifier iid)
    {
        super(iid, Material.STONE);
        setHardness(2.0F);
        setSoundGroup(DEFAULT_SOUND_GROUP);
    }

    public int getTexture(int iSide, int iMetaData)
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

    public Box getCollisionShape(World world, int i, int j, int k)
    {
        int iFacing = GetAnchorFacing(world, i, j, k);
        switch(iFacing)
        {
        case 0: // '\0'
            return Box.createCached((float)i, ((float)j + 1.0F) - fAnchorBaseHeight, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);

        case 1: // '\001'
            return Box.createCached((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + fAnchorBaseHeight, (float)k + 1.0F);

        case 2: // '\002'
            return Box.createCached((float)i, (float)j, ((float)k + 1.0F) - fAnchorBaseHeight, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);

        case 3: // '\003'
            return Box.createCached((float)i, (float)j, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + fAnchorBaseHeight);

        case 4: // '\004'
            return Box.createCached(((float)i + 1.0F) - fAnchorBaseHeight, (float)j, (float)k, (float)i + 1.0F, (float)j + 1.0F, (float)k + 1.0F);
        }
        return Box.createCached((float)i, (float)j, (float)k, (float)i + fAnchorBaseHeight, (float)j + 1.0F, (float)k + 1.0F);
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

    public void setupRenderBoundingBox()
    {
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
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
        int iAnchorFacing = iFacing;
        SetAnchorFacing(world, i, j, k, iAnchorFacing);
    }

    public boolean onUse(World world, int i, int j, int k, PlayerEntity entityPlayer)
    {
        if(world.isRemote)
        {
            return true;
        }
        ItemStack playerEquippedItem = entityPlayer.getHand();
        boolean bRopeEquipped = false;
        if(playerEquippedItem != null && playerEquippedItem.itemId == ItemListener.ropeItem.id)
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
        return iBlockAccess.getBlockMeta(i, j, k);
    }

    public void SetAnchorFacing(World world, int i, int j, int k, int iFacing)
    {
        world.method_215(i, j, k, iFacing);
    }

    void RetractRope(World world, int i, int j, int k, PlayerEntity entityPlayer)
    {
        int tempj = j - 1;
        do
        {
            if(tempj < 0)
            {
                break;
            }
            int iTempid = world.getBlockId(i, tempj, k);
            if(iTempid != BlockListener.rope.id)
            {
                break;
            }
            if(world.getBlockId(i, tempj - 1, k) != BlockListener.rope.id)
            {
                AddRopeToPlayerInventory(world, i, j, k, entityPlayer);
                Block targetBlock = BlockListener.rope;
                world.playSound((float) i + 0.5F, (float) j + 0.5F, (float) k + 0.5F, targetBlock.soundGroup.getSound(), (targetBlock.soundGroup.method_1976() + 1.0F) / 5.0F, targetBlock.soundGroup.method_1977() * 0.8F);
                if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                    voicePacket(world, targetBlock.soundGroup.getSound(), i, j, k, (targetBlock.soundGroup.method_1976() + 1.0F) / 5.0F, targetBlock.soundGroup.method_1977() * 0.8F);
                }
                world.setBlock(i, tempj, k, 0);
                break;
            }
            tempj--;
        } while(true);
    }

    @Environment(EnvType.SERVER)
    public void voicePacket(World world, String name, int x, int y, int z, float g, float h){
        List list2 = world.field_200;
        if(list2.size() != 0) {
            for(int k = 0; k < list2.size(); k++)
            {
                ServerPlayerEntity player1 = (ServerPlayerEntity) list2.get(k);
                PacketHelper.sendTo(player1, new SoundPacket(name, x, y, z, g,h));
            }
        }
    }

    private void AddRopeToPlayerInventory(World world, int i, int j, int k, PlayerEntity entityPlayer)
    {
        ItemStack ropeStack = new ItemStack(ItemListener.ropeItem);
        if(entityPlayer.inventory.method_671(ropeStack))
        {
            world.playSound(entityPlayer, "random.pop", 0.2F, ((world.field_214.nextFloat() - world.field_214.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            if(FabricLoader.INSTANCE.getEnvironmentType() == EnvType.SERVER) {
                voicePacket(world, "random.pop", i, j, k, 0.2F, ((world.field_214.nextFloat() - world.field_214.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }
        } else
        {
            UnsortedUtils.ejectStackWithRandomOffset(world, i, j, k, ropeStack);
        }
    }

    public void NotifyAnchorBlockOfAttachedPulleyStateChange(PulleyBlockEntity tileEntityPulley, World world, int i, int j, int k)
    {
        int iMovementDirection = 0;
        if(tileEntityPulley.IsRaising())
        {
            if(world.getBlockId(i, j + 1, k) == BlockListener.rope.id)
            {
                iMovementDirection = 1;
            }
        } else
        if(tileEntityPulley.IsLowering() && (world.method_234(i, j - 1, k) || world.getBlockId(i, j - 1, k) == BlockListener.platform.id))
        {
            iMovementDirection = -1;
        }
        if(iMovementDirection != 0)
        {
            ConvertAnchorToEntity(world, i, j, k, tileEntityPulley, iMovementDirection);
        }
    }

    private void ConvertAnchorToEntity(World world, int i, int j, int k, PulleyBlockEntity attachedTileEntityPulley, int iMovementDirection)
    {
        BlockPosition pulleyPos = new BlockPosition(attachedTileEntityPulley.x, attachedTileEntityPulley.y, attachedTileEntityPulley.z);
        MovingAnchorEntity entityAnchor = new MovingAnchorEntity(world, (float)i + 0.5F, (float)j + fAnchorBaseHeight / 2.0F, (float)k + 0.5F, pulleyPos, iMovementDirection);
        world.method_210(entityAnchor);
        ConvertConnectedPlatformsToEntities(world, i, j, k, entityAnchor);
        world.setBlock(i, j, k, 0);
    }

    private void ConvertConnectedPlatformsToEntities(World world, int i, int j, int k, MovingAnchorEntity associatedAnchorEntity)
    {
        int iTargetJ = j - 1;
        int iTargetid = world.getBlockId(i, iTargetJ, k);
        if(iTargetid == BlockListener.platform.id)
        {
            ((PlatformBlock)BlockListener.platform).CovertToEntitiesFromThisPlatform(world, i, iTargetJ, k, associatedAnchorEntity);
        }
    }

    public static float fAnchorBaseHeight = 0.375F;
    public final int iAnchorLoopTextureIndex = 39;
    private final int iAnchorBaseTopAndBottomTextureIndex = 40;
    private final int iAnchorBaseSideTextureIndex = 41;
    public final int iAnchorRopeTextureIndex = 32;

    @Override
    public boolean renderWorld(BlockRenderManager tileRenderer, BlockView tileView, int x, int y, int z) {
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
        tileRenderer.renderBlock(this, x, y, z);
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
            int i1 = tileView.getBlockId(x, y + 1, z);
            if(i1 == BlockListener.rope.id || i1 == BlockListener.pulley.id)
            {
                this.setBoundingBox(0.5F - f1, f2, 0.5F - f, 0.5F + f1, 1.0F, 0.5F + f);
                flag = true;
            }
        } else
        if(tileView.getBlockId(x, y - 1, z) == BlockListener.rope.id)
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
    public void renderInventory(BlockRenderManager tileRenderer, int meta) {
        this.setupRenderBoundingBox();
        CustomBlockRendering.RenderInvBlockWithMetaData(tileRenderer, this, -0.5F, -0.25F, -0.5F, 1);
        float f = 0.125F;
        float f1 = 0.125F;
        float f2 = 0.25F;
        this.setBoundingBox(0.5F - f1, fAnchorBaseHeight, 0.5F - f, 0.5F + f1, fAnchorBaseHeight + f2, 0.5F + f);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.25F, -0.5F, TextureListener.anchor_button);
    }
}
