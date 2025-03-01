package net.kozibrodka.wolves.events;

import net.glasslauncher.mods.gcapi3.api.ConfigRoot;
import net.kozibrodka.wolves.glasscfg.BetterThanWolvesCFG;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Null;

import net.modificationstation.stationapi.api.util.Namespace;

public class ConfigListener {

    @ConfigRoot(value = "BetterThanWolvesCFG", visibleName = "Better Than Wolves Config")
    public static final BetterThanWolvesCFG wolvesGlass = new BetterThanWolvesCFG();

    @Entrypoint.Namespace
    public static Namespace MOD_ID;

    //TODO EXTRA: Saw animated, Anvil gravity, PANELS/options
    //TODO: NEW
    //TODO: custom recipes xhmi, big tree mixin, some mixin so stoked fire deals dmg

}

