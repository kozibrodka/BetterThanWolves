package net.kozibrodka.wolves.utils;

import net.minecraft.item.ItemStack;

public class ItemUtil {
    public static boolean compare(ItemStack filter, ItemStack item) {
        if (item == null) return false;
        boolean sameId = filter.itemId == item.itemId;
        if(!item.getStationNbt().values().isEmpty() && !filter.getStationNbt().values().isEmpty()) {
            boolean sameNBT = item.getStationNbt().getString("material")
                    .equals(filter.getStationNbt().getString("material"));
            return sameId && sameNBT;
        }
        return sameId;
    }
}

