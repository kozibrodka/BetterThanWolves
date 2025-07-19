package net.kozibrodka.wolves.config;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

public class DifficultyConfig {
    @ConfigEntry(name = "Wicker Weaving (game restart required!)", description = "Changes wicker to require multiple crafting steps, including weaving")
    public Boolean wickerWeaving = false;

}
