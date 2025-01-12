package net.kozibrodka.wolves.glasscfg;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

public class SmallTweaksCFG {

    @ConfigEntry(name = "Face Gear Box Away From Player", description = "Face gear boxes away from the player on placement")
    public Boolean faceGearBoxAwayFromPlayer = false;

    @ConfigEntry(name = "Anvil Vanilla Recipes (game restart required!)", description = "Anvil can be used like a crafting table. Only works in top left corner of the grid")
    public Boolean anvilVanillaRecipes = true;

    @ConfigEntry(name = "Deactivate Dung (game restart required!)", description = "Deactivates dung production from wolves and replace it with substitutes in recipes")
    public Boolean deactivateDung = false;


}
