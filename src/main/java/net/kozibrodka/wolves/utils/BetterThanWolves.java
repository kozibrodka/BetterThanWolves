package net.kozibrodka.wolves.utils;

import net.fabricmc.loader.api.FabricLoader;

public class BetterThanWolves {
    public static boolean isHarderThanWolvesPresent = FabricLoader.getInstance().isModLoaded("harderthanwolves");
    public static boolean isNewFrontierCraftPresent = FabricLoader.getInstance().isModLoaded("nfc");
    public static boolean isAmiPresent = FabricLoader.getInstance().isModLoaded("alwaysmoreitems");
}
