package net.kozibrodka.wolves.config;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

public class DifficultyConfig {
    @ConfigEntry(name = "Wicker Weaving (game restart required!)", description = "Changes wicker to require multiple crafting steps, including weaving")
    public Boolean wickerWeaving = false;

    @ConfigEntry(name = "Difficult Soulforged Steel Recipes (game restart required!)", description = "Makes soulforged steel recipes use the entire 5x5 anvil crafting grid while also balancing the recycling recipes accordingly")
    public Boolean difficultSoulforgedSteelRecipes = false;

}
