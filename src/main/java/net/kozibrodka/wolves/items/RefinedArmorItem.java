
package net.kozibrodka.wolves.items;
import net.kozibrodka.wolves.events.ConfigListener;
import net.minecraft.item.ArmorItem;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.client.item.ArmorTextureProvider;

import net.modificationstation.stationapi.api.template.item.TemplateArmorItem;

public class RefinedArmorItem extends TemplateArmorItem implements ArmorTextureProvider
{

    public RefinedArmorItem(Identifier identifier, int armorType) {
        super(identifier, 1, 1, armorType);
        setMaxDamage(576);
    }


    @Override
    public Identifier getTexture(ArmorItem armour) {
        return ConfigListener.MOD_ID.id("plate");
    }
}
