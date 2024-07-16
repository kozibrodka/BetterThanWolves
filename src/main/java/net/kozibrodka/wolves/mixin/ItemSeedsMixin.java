package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.utils.UnsortedUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SeedsItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SeedsItem.class)
public class ItemSeedsMixin extends Item{
    public ItemSeedsMixin(int i) {
        super(i);
    }

    @Shadow
    private int cropBlockId;

    //TODO: maybe better mixin design

    @Override
    public boolean useOnBlock(ItemStack arg, PlayerEntity arg2, World arg3, int i, int j, int k, int l) {
        if (l != 1) {
            return false;
        } else {
            int var8 = arg3.getBlockId(i, j, k);
            if ((var8 == Block.FARMLAND.id || UnsortedUtils.CanPlantGrowOnBlock(arg3, i, j, k, null)) && arg3.isAir(i, j + 1, k)) {
                arg3.setBlock(i, j + 1, k, this.cropBlockId);
                --arg.count;
                return true;
            } else {
                return false;
            }
        }
    }
}
