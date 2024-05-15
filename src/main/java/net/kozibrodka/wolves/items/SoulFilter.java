package net.kozibrodka.wolves.items;

import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItem;

public class SoulFilter extends TemplateItem {
    public SoulFilter(Identifier identifier) {
        super(identifier);
        setMaxStackSize(1);
        setDurability(128);
    }
}
