package net.kozibrodka.wolves.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.SeedsItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SeedsItem.class)
public class ItemSeedsMixin extends Item {
    @Shadow
    private int cropBlockId;

    public ItemSeedsMixin(int id) {
        super(id);
    }

    @WrapOperation(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I"))
    public int aVoid(World world, int x, int y, int z, Operation<Integer> original) {
        if (UnsortedUtils.CanPlantGrowOnBlock(world, x, y - 1, z, Block.BLOCKS[this.cropBlockId])) {
            return Block.FARMLAND.id;
        }
        return original.call(world, x, y, z);
    }
}
