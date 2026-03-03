package net.kozibrodka.wolves.items;

import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class SoulFilterItem extends TemplateItem implements CustomTooltipProvider {
    public SoulFilterItem(Identifier identifier) {
        super(identifier);
        setMaxCount(1);
        setMaxDamage(128);
    }

    @Override
    public @NotNull String[] getTooltip(ItemStack itemStack, String originalTooltip) {
        return new String[] {
                originalTooltip,
                "§7Souls fly away in strong wind"
        };
    }
}
