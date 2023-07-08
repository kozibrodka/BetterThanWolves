package net.kozibrodka.wolves.mixin;

import net.kozibrodka.wolves.utils.FCUtilsMisc;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.Seeds;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Seeds.class)
public class ItemSeedsMixin extends ItemBase{
    public ItemSeedsMixin(int i) {
        super(i);
    }

    @Shadow
    private int cropTileId;

    //TODO: maybe better mixin design

    @Override
    public boolean useOnTile(ItemInstance arg, PlayerBase arg2, Level arg3, int i, int j, int k, int l) {
        if (l != 1) {
            return false;
        } else {
            int var8 = arg3.getTileId(i, j, k);
            if ((var8 == BlockBase.FARMLAND.id || FCUtilsMisc.CanPlantGrowOnBlock(arg3, i, j, k, null)) && arg3.isAir(i, j + 1, k)) {
                arg3.setTile(i, j + 1, k, this.cropTileId);
                --arg.count;
                return true;
            } else {
                return false;
            }
        }
    }
}
