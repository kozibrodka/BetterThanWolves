package net.kozibrodka.wolves.glasscfg;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

public class DifficultyCFG {

    @ConfigEntry(name = "Wicker Weaving (game restart required!)", description = "Changes wicker to require multiple crafting steps, including weaving")
    public Boolean wickerWeaving = false;

    @ConfigEntry(name = "Wool Knitting (game restart required!)", description = "Changes wool to require multiple crafting steps, including knitting")
    public Boolean woolKnitting = false;

    @ConfigEntry(name = "Tree Stumps (game restart required!)", description = "Newly generated or grown trees now come with stumps which either require TNT or a stump remover to be cleared")
    public Boolean treeStumps = false;

}
