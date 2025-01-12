package net.kozibrodka.wolves.items;

import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class LazyItemTemplate extends TemplateItem {
    public LazyItemTemplate(Identifier identifier) {
        super(identifier);
        this.setTranslationKey(identifier.namespace, identifier.path);
    }
}
