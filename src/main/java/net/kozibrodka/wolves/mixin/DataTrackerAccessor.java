package net.kozibrodka.wolves.mixin;

import net.minecraft.class_270;
import net.minecraft.entity.data.DataTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(DataTracker.class)
public interface DataTrackerAccessor {
    @Accessor
    Map<Integer, class_270> getData();
}
