package net.kozibrodka.wolves.events;

import net.glasslauncher.mods.gcapi3.api.ConfigRoot;
import net.kozibrodka.wolves.config.BetterThanWolvesConfig;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;

public class ConfigListener {
    @ConfigRoot(value = "BetterThanWolvesCFG", visibleName = "Better Than Wolves Config")
    public static final BetterThanWolvesConfig wolvesGlass = new BetterThanWolvesConfig();

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    //TODO EXTRA: Saw animated

}

