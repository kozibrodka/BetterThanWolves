package net.kozibrodka.wolves.glasscfg;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;
import org.checkerframework.framework.qual.CFComment;

public class GameplayCFG {

    @ConfigEntry(name = "Gear Box Placement Face")
    @CFComment("FaceGearBoxAwayFromPlayer")
    public Boolean faceGearBoxAwayFromPlayer = false;

    @ConfigEntry(name = "Anvil Vanilla Recipes")
    @CFComment("template_comment")
    public Boolean anvilVanillaRecipes = true;

    @ConfigEntry(name = "Deactivate Dung", description = "Deactivates dung production from wolves and replace it with substitutes in recipes")
    public Boolean deactivateDung = false;

    @ConfigEntry(name = "Disable Axe Changes")
    public Boolean fcDisableAxeChanges = false;

    @ConfigEntry(name = "Disable Minecart Changes")
    public Boolean fcDisableMinecartChanges = false;
}
