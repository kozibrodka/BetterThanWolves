package net.kozibrodka.wolves.block.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.block.PanelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.block.IsBlockReplaceableEvent;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.template.item.TemplateBlockItem;
import net.modificationstation.stationapi.api.util.math.Direction;

public class PanelBlockItem extends TemplateBlockItem {
    public PanelBlockItem(int i) {
        super(i);
        this.setMaxDamage(0);
        this.setHasSubtypes(false);
    }

    @Override
    public boolean useOnBlock(ItemStack stack, PlayerEntity user, World world, int x, int y, int z, int side) {
        if (world.getBlockId(x, y, z) == Block.SNOW.id) {
            side = 0;
        } else {
            if (side == 0) {
                --y;
            }

            if (side == 1) {
                ++y;
            }

            if (side == 2) {
                --z;
            }

            if (side == 3) {
                ++z;
            }

            if (side == 4) {
                --x;
            }

            if (side == 5) {
                ++x;
            }
        }
        boolean isPlacementDouble = checkForDoublePlacement(world, x, y, z, side);
        int squeezedId = world.getBlockId(x,y,z);
        if (stack.count == 0) {
            return false;
        } else if (y == world.getTopY()-1 && Block.BLOCKS[this.getBlock().id].material.isSolid()) {
            return false;
        } else if (stationapi_canReplace(this.getBlock().id, stack, user, world, x, y, z, side) && !isPlacementDouble) {
            Block var8 = Block.BLOCKS[this.getBlock().id];
            if (world.setBlock(x, y, z, this.getBlock().id, this.getPlacementMetadata(stack.getDamage()))) {
                Block.BLOCKS[this.getBlock().id].onPlaced(world, x, y, z, user);
                world.playSound((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), var8.soundGroup.getSound(), (var8.soundGroup.getVolume() + 1.0F) / 2.0F, var8.soundGroup.getPitch() * 0.8F);
                --stack.count;
            }

            return true;
        } else if (isPlacementDouble) {
            {
                Block var8 = Block.BLOCKS[this.getBlock().id];
                Block.BLOCKS[this.getBlock().id].onPlaced(world, x, y, z, side);
                world.playSound((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), var8.soundGroup.getSound(), (var8.soundGroup.getVolume() + 1.0F) / 2.0F, var8.soundGroup.getPitch() * 0.8F);
                --stack.count;
                return true;
            }
        } else if (squeezedId == this.getBlock().id){
            if(checkForSqueeze(world,x,y,z,side)) {
                Block var8 = Block.BLOCKS[this.getBlock().id];
                world.setBlockMeta(x,y,z,4);
                world.blockUpdate(x, y, z, this.id);
                world.playSound((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), var8.soundGroup.getSound(), (var8.soundGroup.getVolume() + 1.0F) / 2.0F, var8.soundGroup.getPitch() * 0.8F);
                --stack.count;
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean checkForSqueeze(World world, int x, int y, int z, int direction) {
        int meta = world.getBlockMeta(x,y,z);
        if (direction == 2  && meta == 3) {
            return true;
        }
        if (direction == 3 && meta == 2) {
            return true;
        }
        if (direction == 4 && meta == 1) {
            return true;
        }
        if (direction == 5 && meta == 0) {
            return true;
        }
        return false;
    }

    public boolean checkForDoublePlacement(World world, int x, int y, int z, int direction) {
        if (direction == 2) {
            int a = world.getBlockId(x,y,z+1);
            int b = world.getBlockMeta(x,y,z+1);
            if(a == this.getBlock().id && b == 2){
                return true;
            }
        }

        if (direction == 3) {
            int a = world.getBlockId(x,y,z-1);
            int b = world.getBlockMeta(x,y,z-1);
            if(a == this.getBlock().id && b == 3){
                return true;
            }
        }

        if (direction == 4) {
            int a = world.getBlockId(x+1,y,z);
            int b = world.getBlockMeta(x+1,y,z);
            if(a == this.getBlock().id && b == 0){
                return true;
            }
        }

        if (direction == 5) {
            int a = world.getBlockId(x-1,y,z);
            int b = world.getBlockMeta(x-1,y,z);
            if(a == this.getBlock().id && b == 1){
                return true;
            }
        }
        return false;
    }

    private boolean stationapi_canReplace(int blockID, ItemStack itemStack, PlayerEntity player, World world, int x, int y, int z, int side) {
        Direction direction = Direction.byId(side);
        Box box = Block.BLOCKS[blockID].getCollisionShape(world, x, y, z);
        return (box == null || world.canSpawnEntity(box)) && ((IsBlockReplaceableEvent) StationAPI.EVENT_BUS.post(IsBlockReplaceableEvent.builder().context(new ItemPlacementContext(player, itemStack, new HitResult(x - direction.getOffsetX(), y - direction.getOffsetY(), z - direction.getOffsetZ(), side, Vec3d.createCached((double)x, (double)y, (double)z)))).build())).context.canPlace() && Block.BLOCKS[blockID].canPlaceAt(world, x, y, z, side);
    }
}
