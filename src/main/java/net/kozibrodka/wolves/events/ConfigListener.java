package net.kozibrodka.wolves.events;

import net.kozibrodka.wolves.glasscfg.BetterThanWolvesCFG;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Null;


//import net.glasslauncher.mods.api.gcapi.api.GConfig;
import net.kozibrodka.wolves.glasscfg.BetterThanWolvesCFG;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

public class ConfigListener {

    //@GConfig(value = "BetterThanWolvesCFG", visibleName = "Better Than Wolves Config")
    //public static final BetterThanWolvesCFG wolvesGlass = new BetterThanWolvesCFG();

    @Entrypoint.Namespace
    public static final Namespace MOD_ID = Null.get();

    //TODO EXTRA: Saw animated, Anvil gravity, PANELS/options
    //TODO: NEW
    //TODO: custom recipes xhmi, big tree mixin, some mixin so stoked fire deals dmg

}

