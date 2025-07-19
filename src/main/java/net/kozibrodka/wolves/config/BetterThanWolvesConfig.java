package net.kozibrodka.wolves.config;

import net.glasslauncher.mods.gcapi3.api.ConfigCategory;

public class BetterThanWolvesConfig {
    @ConfigCategory(name = "§4Difficulty", description = "Gameplay changes to make some mechanics more difficult")
    public DifficultyConfig difficulty = new DifficultyConfig();

    @ConfigCategory(name = "§aSmall Tweaks", description = "Gameplay changes that do not affect the difficulty")
    public SmallTweaksConfig small_tweaks = new SmallTweaksConfig();
}
