package net.kozibrodka.wolves.items;

import net.modificationstation.stationapi.api.util.Identifier;

public class KnittingNeedlesItem extends LazyItemTemplate {
    public KnittingNeedlesItem(Identifier identifier) {
        super(identifier);
        this.setMaxCount(1);
    }
}
