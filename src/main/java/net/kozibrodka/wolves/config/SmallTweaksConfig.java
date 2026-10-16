package net.kozibrodka.wolves.config;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

public class SmallTweaksConfig {
    @ConfigEntry(name = "Face Gear Box Away From Player", description = "Face gear boxes away from the player on placement")
    public Boolean faceGearBoxAwayFromPlayer = false;

    @ConfigEntry(name = "Anvil Vanilla Recipes", description = "Anvil can be used like a crafting table. Only works in top left corner of the grid", requiresRestart = true)
    public Boolean anvilVanillaRecipes = false;

    @ConfigEntry(name = "Deactivate Dung", description = "Deactivates dung production from wolves and replace it with substitutes in recipes", requiresRestart = true)
    public Boolean deactivateDung = false;

    @ConfigEntry(name = "Companion cube converter", description = "Only affects recipe, no risk of world corruption. Makes the converter use a companion cube to craft", requiresRestart = true)
    public Boolean companionCubeConverter = false;

    @ConfigEntry(name = "Beam of Light from Lens full render (re-join world to see)", description = "Alternate design of rendering light beams from lens block")
    public Boolean render_beam = false;
}
