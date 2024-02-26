
package net.kozibrodka.wolves.items;
import net.kozibrodka.wolves.events.ConfigListener;
import net.minecraft.item.armour.Armour;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.client.item.ArmorTextureProvider;

import net.modificationstation.stationapi.api.template.item.TemplateArmorItem;

public class RefinedArmour extends TemplateArmorItem implements ArmorTextureProvider
{

    public RefinedArmour(Identifier identifier, int armorType) {
        super(identifier, 1, 1, armorType);
        setDurability(576);
    }


    @Override
    public Identifier getTexture(Armour armour) {
        return ConfigListener.MOD_ID.id("plate");
    }
}
