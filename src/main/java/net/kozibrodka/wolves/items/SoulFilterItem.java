package net.kozibrodka.wolves.items;

import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItem;

public class SoulFilterItem extends TemplateItem {
    public SoulFilterItem(Identifier identifier) {
        super(identifier);
        setMaxCount(1);
        setMaxDamage(128);
    }
}
