package net.kozibrodka.wolves.mixin;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.DataTrackerEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(DataTracker.class)
public interface DataTrackerAccessor {
    @Accessor("entries")
    Map<Integer, DataTrackerEntry> getData();
}
