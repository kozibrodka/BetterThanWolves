package net.kozibrodka.wolves.glasscfg;

//import net.glasslauncher.mods.api.gcapi.api.ConfigCategory;

// TODO: Either remove this config or add configurations. We could add an optional more difficult mode.

import net.glasslauncher.mods.gcapi3.api.ConfigCategory;

public class BetterThanWolvesCFG {
    @ConfigCategory(name = "§5Gameplay Settings")
    public GameplayCFG gameplay_settings = new GameplayCFG();
}
