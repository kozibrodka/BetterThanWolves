package net.kozibrodka.wolves.items;

import net.modificationstation.stationapi.api.util.Identifier;

public class LoadedLoomShuttleItem extends LazyItemTemplate {
    public LoadedLoomShuttleItem(Identifier identifier) {
        super(identifier);
        setMaxCount(1);
        setMaxDamage(15);
    }
}
