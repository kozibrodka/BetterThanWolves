package net.kozibrodka.wolves.config;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

public class SmallTweaksConfig {
    @ConfigEntry(name = "Face Gear Box Away From Player", description = "Face gear boxes away from the player on placement")
    public Boolean faceGearBoxAwayFromPlayer = false;

    @ConfigEntry(name = "Anvil Vanilla Recipes (game restart required!)", description = "Anvil can be used like a crafting table. Only works in top left corner of the grid")
    public Boolean anvilVanillaRecipes = true;

    @ConfigEntry(name = "Deactivate Dung (game restart required!)", description = "Deactivates dung production from wolves and replace it with substitutes in recipes")
    public Boolean deactivateDung = false;

    @ConfigEntry(name = "Add recipe for wicker block (game restart required!)", description = "Only affects recipe, no risk of world corruption")
    public Boolean wickerBlock = true;

    @ConfigEntry(name = "Add recipe for grate block (game restart required!)", description = "Only affects recipe, no risk of world corruption")
    public Boolean grateBlock = true;

    @ConfigEntry(name = "Add recipe for steel block (game restart required!)", description = "Only affects recipe, no risk of world corruption")
    public Boolean steelBlock = true;
}
