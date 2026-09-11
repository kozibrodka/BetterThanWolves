package net.kozibrodka.wolves.block;

import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.RotatableBlock;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class BuddyBlock extends TemplateBlock implements RotatableBlock {
    private static final int m_iTickRate = 5;
    private static final int m_iTextureIndexOff = 80;
    private static final int m_iTextureIndexOn = 81;
    private static final int m_iTextureIndexOutputOff = 82;
    private static final int m_iTextureIndexOutputOn = 83;

    public BuddyBlock(Identifier identifier) {
        super(identifier, Material.STONE);
        this.setHardness(3.5F);
        this.setSoundGroup(STONE_SOUND_GROUP);
    }

    @Override
    public int getTickRate() {
        return 5;
    }

    @Override
    public int getTextureId(BlockView blockAccess, int i, int j, int k, int iSide) {
        int iFacing = this.getFacing(blockAccess, i, j, k);
        return iFacing == iSide ? (this.IsRedstoneOn(blockAccess, i, j, k) ? TextureListener.buddy_face_on : TextureListener.buddy_face_off) : (this.IsRedstoneOn(blockAccess, i, j, k) ? TextureListener.buddy_on : TextureListener.buddy_off);
    }

    @Override
    public int getTexture(int side) {
        if (side == 3) {
            return TextureListener.buddy_face_off;
        } else {
            return TextureListener.buddy_off;
        }
    }

    @Override
    public void onPlaced(World world, int i, int j, int k) {
        super.onPlaced(world, i, j, k);
        world.scheduleBlockUpdate(i, j, k, this.id, 1);
    }

    @Override
    public void onPlaced(World world, int i, int j, int k, int iFacing) {
        this.setFacing(world, i, j, k, UnsortedUtils.getOppositeFacing(iFacing));
    }

    @Override
    public void onPlaced(World world, int i, int j, int k, LivingEntity entityLiving) {
        int iFacing = UnsortedUtils.ConvertPlacingEntityOrientationToBlockFacing(entityLiving);
        this.setFacing(world, i, j, k, iFacing);
    }

    @Override
    public void neighborUpdate(World world, int i, int j, int k, int iNeighborBlockID) {
        if(!this.IsRedstoneOn(world, i, j, k) && iNeighborBlockID != Block.REDSTONE_WIRE.id && iNeighborBlockID != Block.POWERED_REPEATER.id && iNeighborBlockID != Block.REPEATER.id && iNeighborBlockID != Block.LIT_REDSTONE_TORCH.id && iNeighborBlockID != Block.REDSTONE_TORCH.id && iNeighborBlockID != this.id) {
            world.scheduleBlockUpdate(i, j, k, this.id, 1);
        }
        /// Old Detector Logic Block
        /// currently, I dont know what is this supposed mechanic
//        if(!this.IsRedstoneOn(world, i, j, k) && iNeighborBlockID != mod_FCBetterThanWolves.fcBlockDetectorLogic.id){
//            world.scheduleBlockUpdate(i, j, k, this.id, 1);
//        }
    }

    @Override
    public void onTick(World world, int i, int j, int k, Random random) {
        if(this.IsRedstoneOn(world, i, j, k)) {
            this.SetBlockRedstoneOn(world, i, j, k, false);
            world.blockUpdateEvent(i, j, k);
        } else {
            this.SetBlockRedstoneOn(world, i, j, k, true);
            world.blockUpdateEvent(i, j, k);
            world.scheduleBlockUpdate(i, j, k, this.id, this.getTickRate());
        }

    }

    @Override
    public boolean isPoweringSide(BlockView blockAccess, int i, int j, int k, int iSide) {
        int iFacing = this.getFacing(blockAccess, i, j, k);
        return UnsortedUtils.getOppositeFacing(iSide) == iFacing && this.IsRedstoneOn(blockAccess, i, j, k);
    }

    @Override
    public boolean isStrongPoweringSide(World world, int i, int j, int k, int iSide) {
        int iFacing = this.getFacing(world, i, j, k);
        return UnsortedUtils.getOppositeFacing(iSide) == iFacing && this.IsRedstoneOn(world, i, j, k);
    }

    @Override
    public boolean canEmitRedstonePower() {
        return true;
    }

    @Override
    public int getFacing(BlockView iBlockAccess, int i, int j, int k) {
        return (iBlockAccess.getBlockMeta(i, j, k) & -2) >> 1;
    }

    @Override
    public void setFacing(World world, int i, int j, int k, int iFacing) {
        int iMetaData = world.getBlockMeta(i, j, k);
        iMetaData = iMetaData & 1 | iFacing << 1;
        world.setBlockMeta(i, j, k, iMetaData);
    }

    @Override
    public boolean canRotate(BlockView iblockaccess, int i, int j, int k) {
        return true;
    }

    @Override
    public boolean canTransmitRotation(BlockView iblockaccess, int i, int j, int k) {
        return true;
    }

    @Override
    public void rotate(World world, int i, int j, int k, boolean flag) {
        int iFacing = getFacing(world, i, j, k);
        int iNewFacing = UnsortedUtils.RotateFacingAroundJ(iFacing, flag);
        if (iNewFacing != iFacing) {
            setFacing(world, i, j, k, iNewFacing);
            world.scheduleBlockUpdate(i, j, k, this.id, 1);
            world.setBlocksDirty(i, j, k, i, j, k);
            world.blockUpdateEvent(i, j, k);
        }
    }
    /// methods from oryginal logic, useless.
    public int GetFacingFromMetadata(int iMetadata) {
        return (iMetadata & -2) >> 1;
    }

    public int SetFacingInMetadata(int iMetadata, int iFacing) {
        iMetadata = iMetadata & 1 | iFacing << 1;
        return iMetadata;
    }

    public boolean IsRedstoneOn(BlockView iblockaccess, int i, int j, int k) {
        return (iblockaccess.getBlockMeta(i, j, k) & 1) > 0;
    }

    @Override
    public void onBlockAction(World world, int x, int y, int z, int code, int pitch) {
        world.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.75F, 2.0F);
    }

    public void SetBlockRedstoneOn(World world, int i, int j, int k, boolean bOn) {
        if(bOn != this.IsRedstoneOn(world, i, j, k)) {
            int iMetaData = world.getBlockMeta(i, j, k);
            if(bOn) {
                iMetaData |= 1;
                world.playNoteBlockActionAt(i,j,k, 0, 0); ///
            } else {
                iMetaData &= -2;
            }

            world.setBlockMetaWithoutNotifyingNeighbors(i, j, k, iMetaData);
            int iFacing = this.getFacing(world, i, j, k);
            BlockPosition outputPos = new BlockPosition(i, j, k);
            outputPos.addFacingAsOffset(iFacing);
            Block outputBlock = Block.BLOCKS[world.getBlockId(outputPos.x, outputPos.y, outputPos.z)];
            if(outputBlock != null) {
                outputBlock.neighborUpdate(world, outputPos.x, outputPos.y, outputPos.z, this.id);
            }

            world.notifyNeighbors(outputPos.x, outputPos.y, outputPos.z, this.id);
            world.setBlocksDirty(i, j, k, i, j, k);
        }

    }

}
