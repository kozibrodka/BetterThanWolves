package net.kozibrodka.wolves.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(CakeBlock.class)
public class BlockCakeMixin extends Block {

    /// Forced constructor
    public BlockCakeMixin(int id, Material material) {
        super(id, material);
    }

    @Override
    public void onPlaced(World world, int i, int j, int k) /// NEW Method from Block.class
    {
        super.onPlaced(world, i, j, k);
        boolean flag = world.isStrongPowered(i, j, k);
        if(flag)
        {
            setRedstoneOn(world, i, j, k, true);
            world.playNoteBlockActionAt(i,j,k, 0, 0); /// Ghast Scream
        }
    }

    @Redirect(
            method = "updateBoundingBox",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/BlockView;getBlockMeta(III)I"
            )
    )
    private int redirectGetBlockMeta(BlockView iblockaccess, int i, int j, int k) {
        return this.getEatState(iblockaccess, i, j, k);
    }

    @Redirect(
            method = "getCollisionShape",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getBlockMeta(III)I"
            )
    )
    private int redirectGetBlockMeta2(World world, int i, int j, int k) {
        return this.getEatState(world, i, j, k);
    }

    @Environment(EnvType.CLIENT)
    @Redirect( /// KLIENT ONLY METHOD
            method = "getBoundingBox",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getBlockMeta(III)I"
            )
    )
    private int redirectGetBlockMeta3(World world, int i, int j, int k) {
        return this.getEatState(world, i, j, k);
    }

    @ModifyVariable(method = "getTexture(II)I", at = @At("HEAD"), argsOnly = true, index = 2)
    private int modifyMetadataArg(int meta)
    {
        return meta & 7;
    }

    @Redirect(
            method = "tryEat",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getBlockMeta(III)I"
            )
    )
    private int redirectGetBlockMeta4(World world, int i, int j, int k) {
        return this.getEatState(world, i, j, k);
    }

    @Redirect(
            method = "tryEat",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;setBlockMeta(IIII)V"
            )
    )
    private void redirectSetBlockMeta(World world, int i, int j, int k, int meta) {
        this.setEatState(world, i, j, k, meta);
    }

    @Override /// Method from CakeBlock - full override
    public void neighborUpdate(World world, int i, int j, int k, int l)
    {
        if(!canGrow(world, i, j, k))
        {
            dropStacks(world, i, j, k, world.getBlockMeta(i, j, k));
            world.setBlock(i, j, k, 0);
        } else
        {
            boolean flag = isRedstoneOn(world, i, j, k);
            boolean flag1 = world.isStrongPowered(i, j, k);
            if(flag != flag1)
            {
                setRedstoneOn(world, i, j, k, flag1);
                if(flag1)
                {
                    world.playNoteBlockActionAt(i,j,k, 0, 0); /// Ghast Scream
                }
            }
        }
    }

    @Unique
    public boolean isRedstoneOn(BlockView iblockaccess, int i, int j, int k)
    {
        return (iblockaccess.getBlockMeta(i, j, k) & 8) > 0;
    }

    @Unique
    public void setRedstoneOn(World world, int i, int j, int k, boolean flag)
    {
        int l = world.getBlockMeta(i, j, k) & -9;
        if(flag)
        {
            l |= 8;
        }
        world.setBlockMeta(i, j, k, l);
    }

    @Unique
    public int getEatState(BlockView iblockaccess, int i, int j, int k)
    {
        return iblockaccess.getBlockMeta(i, j, k) & 7;
    }

    @Unique
    public void setEatState(World world, int i, int j, int k, int l)
    {
        int i1 = world.getBlockMeta(i, j, k) & 8;
        i1 |= l;
        world.setBlockMeta(i, j, k, i1);
    }

    @Override
    public void randomDisplayTick(World world, int i, int j, int k, Random random) /// NEW Method from Block.class
    {
        if(isRedstoneOn(world, i, j, k))
        {
            double d = (double)i + 0.5D + ((double)random.nextFloat() - 0.5D) * 0.66600000000000004D;
            double d1 = (double)(float)j + 0.65000000000000002D;
            double d2 = (double)k + 0.5D + ((double)random.nextFloat() - 0.5D) * 0.66600000000000004D;
            float f = 0.06666667F;
            float f1 = f * 0.6F + 0.4F;
            float f2 = f * f * 0.7F - 0.5F;
            float f3 = f * f * 0.6F - 0.7F;
            if(f2 < 0.0F)
            {
                f2 = 0.0F;
            }
            if(f3 < 0.0F)
            {
                f3 = 0.0F;
            }
            world.addParticle("reddust", d, d1, d2, f1, f2, f3);
        }
    }

    @Override
    public void onBlockAction(World world, int x, int y, int z, int code, int pitch) { /// NEW Method from Block.class
        world.playSound((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "mob.ghast.scream", 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
    }
}
