package net.kozibrodka.wolves.config;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

public class SmallTweaksConfig {
    @ConfigEntry(name = "Face Gear Box Away From Player", description = "Face gear boxes away from the player on placement")
    public Boolean faceGearBoxAwayFromPlayer = false;

    @ConfigEntry(name = "Anvil Vanilla Recipes (game restart required!)", description = "Anvil can be used like a crafting table. Only works in top left corner of the grid")
    public Boolean anvilVanillaRecipes = false;

    @ConfigEntry(name = "Deactivate Dung (game restart required!)", description = "Deactivates dung production from wolves and replace it with substitutes in recipes")
    public Boolean deactivateDung = false;

    @ConfigEntry(name = "Companion cube converter (game restart required!)", description = "Only affects recipe, no risk of world corruption. Makes the converter use a companion cube to craft")
    public Boolean companionCubeConverter = false;
}
