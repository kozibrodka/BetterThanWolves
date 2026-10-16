package net.kozibrodka.wolves.config;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

public class ContentConfig {

    @ConfigEntry(name = "Vertical slabs instead of OmniSlab", description = "Better designed vertical slab replaces cursed omnislabs", requiresRestart = true)
    public Boolean verticalSlabs = true;

    @ConfigEntry(name = "Full Cosmetic Blocks", description = "block of wicker, grates, steel", requiresRestart = true)
    public Boolean cosmetic_blocks = true;

    @ConfigEntry(name = "Extra Machine Blocks", description = "dropper, conveyor, conveyor extender", requiresRestart = true)
    public Boolean machine_blocks = true;

    @ConfigEntry(name = "Steel Minecart", description = "minecart from soulforged steel with faster travel time", requiresRestart = true)
    public Boolean steelminecart = true;

    @ConfigEntry(name = "Automatic Anvil", description = "multiblock construction that allows to auto-craft anvil exclusive items", requiresRestart = true)
    public Boolean autoanvil_blocks = true;


}
