package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kozibrodka.wolves.block.item.PanelBlockItem;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.HasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;
import java.util.Random;

@HasCustomBlockItemFactory(PanelBlockItem.class)
public class PanelBlock extends TemplateBlock {

    private final Block template;

    public PanelBlock(Identifier id, Block blockBase) {
        super(id, blockBase.textureId, blockBase.material);
        this.template = blockBase;
        this.setHardness(blockBase.getHardness());
        this.setResistance(blockBase.resistance); // /3.0F?
        this.setSoundGroup(blockBase.soundGroup);
        this.setOpacity(2);
    }

    public void updateBoundingBox(BlockView arg, int i, int j, int k) {
        int var7 = arg.getBlockMeta(i, j, k);
        if (var7 == 0) {
            this.setBoundingBox(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        } else if (var7 == 1) {
            this.setBoundingBox(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
        } else if (var7 == 2) {
            this.setBoundingBox(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
        } else if (var7 == 3) {
            this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
        } else if (var7 == 4) {
            this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public Box getCollisionShape(World arg, int i, int j, int k) {
        return super.getCollisionShape(arg, i, j, k);
    }

    public boolean isOpaque() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    @Environment(EnvType.CLIENT)
    public boolean isSideVisible(BlockView arg, int i, int j, int k, int l) {
        return super.isSideVisible(arg, i, j, k, l);
    }

    public void addIntersectingBoundingBox(World arg, int i, int j, int k, Box arg2, ArrayList arrayList) {
        int var7 = arg.getBlockMeta(i, j, k);
        if (var7 == 0) {
            this.setBoundingBox(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            super.addIntersectingBoundingBox(arg, i, j, k, arg2, arrayList);
        } else if (var7 == 1) {
            this.setBoundingBox(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
            super.addIntersectingBoundingBox(arg, i, j, k, arg2, arrayList);
        } else if (var7 == 2) {
            this.setBoundingBox(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
            super.addIntersectingBoundingBox(arg, i, j, k, arg2, arrayList);
        } else if (var7 == 3) {
            this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
            super.addIntersectingBoundingBox(arg, i, j, k, arg2, arrayList);
        } else if(var7 == 4)  {
            this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            super.addIntersectingBoundingBox(arg, i, j, k, arg2, arrayList);
        }
    }

    public void setupRenderBoundingBox() {
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
    }

    @Environment(EnvType.CLIENT)
    public float getLuminance(BlockView arg, int i, int j, int k) {
        return this.template.getLuminance(arg, i, j, k);
    }

    public float getBlastResistance(Entity arg) {
        return this.template.getBlastResistance(arg);
    }

    @Environment(EnvType.CLIENT)
    public int getRenderLayer() {
        return this.template.getRenderLayer();
    }

    public int getDroppedItemMeta(int iMetaData) {
        return 0;
    }

    public int getTexture(int i, int j) {
        return this.template.getTexture(i, j);
    }

    public int getTexture(int i) {
        return this.template.getTexture(i);
    }

    public void dropStacks(World world, int x, int y, int z, int meta, float luck) {
        if (!world.isRemote) {
            int var7 = 0;
            if(meta == 4){
                var7 = 2;
            }else {
                var7 = this.getDroppedItemCount(world.random);
            }

            for(int var8 = 0; var8 < var7; ++var8) {
                if (!(world.random.nextFloat() > luck)) {
                    int var9 = this.getDroppedItemId(meta, world.random);
                    if (var9 > 0) {
                        this.dropStack(world, x, y, z, new ItemStack(var9, 1, this.getDroppedItemMeta(meta)));
                    }
                }
            }

        }
    }

    @Environment(EnvType.CLIENT)
    public int getTextureId(BlockView arg, int i, int j, int k, int l) {
        return this.template.getTextureId(arg, i, j, k, l);
    }

    public int getTickRate() {
        return this.template.getTickRate();
    }


    public boolean canPlaceAt(World world, int x, int y, int z, int side) {
        int var5 = world.getBlockId(x, y, z);
        return var5 == 0 || BLOCKS[var5].material.isReplaceable();
    }

    public void onPlaced(World arg, int i, int j, int k, LivingEntity arg2) {
        int var6 = MathHelper.floor((double) (arg2.yaw * 4.0F / 360.0F) + 0.5D) & 3;
        if (var6 == 0) {
            arg.setBlockMeta(i, j, k, 2);
        }

        if (var6 == 1) {
            arg.setBlockMeta(i, j, k, 1);
        }

        if (var6 == 2) {
            arg.setBlockMeta(i, j, k, 3);
        }

        if (var6 == 3) {
            arg.setBlockMeta(i, j, k, 0);
        }

    }

    public void onPlaced(World world, int x, int y, int z, int direction) {
        if (direction == 2) {
            int a = world.getBlockId(x,y,z+1);
            int b = world.getBlockMeta(x,y,z+1);
            if(a == this.id && b == 2){
                world.setBlockMeta(x,y,z+1,4);
                world.blockUpdate(x, y, z+1, this.id);
            }
        }

        if (direction == 3) {
            int a = world.getBlockId(x,y,z-1);
            int b = world.getBlockMeta(x,y,z-1);
            if(a == this.id && b == 3){
                world.setBlockMeta(x,y,z-1,4);
                world.blockUpdate(x, y, z-1, this.id);
            }
        }

        if (direction == 4) {
            int a = world.getBlockId(x+1,y,z);
            int b = world.getBlockMeta(x+1,y,z);
            if(a == this.id && b == 0){
                world.setBlockMeta(x+1,y,z,4);
                world.blockUpdate(x+1, y, z, this.id);
            }
        }

        if (direction == 5) {
            int a = world.getBlockId(x-1,y,z);
            int b = world.getBlockMeta(x-1,y,z);
            if(a == this.id && b == 1){
                world.setBlockMeta(x-1,y,z,4);
                world.blockUpdate(x-1, y, z, this.id);
            }
        }
    }

}
