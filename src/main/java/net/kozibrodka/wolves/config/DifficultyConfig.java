package net.kozibrodka.wolves.config;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

public class DifficultyConfig {

    @ConfigEntry(name = "Use Bone Meal to grow Hemp", description = "Remove grindyness of Hemp Farming, very easy mode")
    public Boolean boneMealHempFarming = false;

    @ConfigEntry(name = "Wicker Weaving", description = "Changes wicker to require multiple crafting steps, including weaving", requiresRestart = true)
    public Boolean wickerWeaving = false;

    @ConfigEntry(name = "Difficult Soulforged Steel Recipes", description = "Makes soulforged steel recipes use the entire 5x5 anvil crafting grid while also balancing the recycling recipes accordingly", requiresRestart = true)
    public Boolean difficultSoulforgedSteelRecipes = false;

    @ConfigEntry(name = "Complex Soul Filtering", description = "Changes soul filtering to use a soul filter instead of soul sand with extra requirements", requiresRestart = true)
    public Boolean complexSoulFiltering = false;

}
