package net.kozibrodka.wolves.glasscfg;

import net.glasslauncher.mods.api.gcapi.api.ConfigName;
import net.glasslauncher.mods.api.gcapi.api.MaxLength;
import org.checkerframework.framework.qual.CFComment;

public class GameplayCFG {

    @ConfigName("Template")
    @CFComment("template integer option")
    @MaxLength(
            value = 10
    )
    public Integer btw_diff = 5;

    @ConfigName("Gear Box Placement Face")
    @CFComment("FaceGearBoxAwayFromPlayer")
    public Boolean faceGearBoxAwayFromPlayer = false;

    @ConfigName("Anvil Vanilla Recipes")
    @CFComment("template_comment")
    public Boolean anvilVanillaRecipes = true;

    @ConfigName("Vanilla Style Panels")
    @CFComment("template_comment")
    public Boolean initPanels = false;


//    public static boolean fcDisableAxeChanges = false;
//    public static boolean fcDisableMinecartChanges = false;
}
