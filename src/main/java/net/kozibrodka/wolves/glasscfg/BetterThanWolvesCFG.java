package net.kozibrodka.wolves.glasscfg;

import net.glasslauncher.mods.gcapi3.api.ConfigCategory;

public class BetterThanWolvesCFG {

    @ConfigCategory(name = "§4Difficulty", description = "Gameplay changes to make some mechanics more difficult")
    public DifficultyCFG difficulty = new DifficultyCFG();

    @ConfigCategory(name = "§aSmall Tweaks", description = "Gameplay changes that do not affect the difficulty")
    public SmallTweaksCFG small_tweaks = new SmallTweaksCFG();
}
