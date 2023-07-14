package net.kozibrodka.wolves.items;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;

public class SoulFilter extends TemplateItemBase {
    public SoulFilter(Identifier identifier) {
        super(identifier);
        setTranslationKey(identifier.modID, identifier.id);
        setMaxStackSize(1);
        setDurability(128);
    }
}
