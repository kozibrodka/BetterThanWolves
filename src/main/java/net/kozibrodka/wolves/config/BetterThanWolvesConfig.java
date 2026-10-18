package net.kozibrodka.wolves.config;

import net.glasslauncher.mods.gcapi3.api.ConfigCategory;

public class BetterThanWolvesConfig {
    @ConfigCategory(name = "§4Difficulty", description = "Gameplay changes to make some mechanics more difficult", multiplayerSynced = true)
    public DifficultyConfig difficulty = new DifficultyConfig();

    @ConfigCategory(name = "§aSmall Tweaks", description = "Gameplay changes that do not affect the difficulty")
    public SmallTweaksConfig small_tweaks = new SmallTweaksConfig();

    @ConfigCategory(name = "§bContent", description = "Extra content thats not oryginally in BTW for b1.7.3", multiplayerSynced = true)
    public ContentConfig content = new ContentConfig();
}
