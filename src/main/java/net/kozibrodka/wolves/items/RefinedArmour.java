
package net.kozibrodka.wolves.items;
import net.kozibrodka.wolves.events.ConfigListener;
import net.minecraft.item.armour.Armour;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.client.item.ArmorTextureProvider;

import net.modificationstation.stationapi.api.template.item.TemplateArmorItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class RefinedArmour extends TemplateArmorItem implements ArmorTextureProvider
{

    public RefinedArmour(Identifier iItemID, int iArmorType)
    {
        super(iItemID, 1, 1, iArmorType);
        setDurability(576);
    }


    @Override
    public Identifier getTexture(Armour armour) {
        return ConfigListener.MOD_ID.id("plate");
    }

    /**
     *  How to set armor texture?
     */

//    public String getArmorTextureFile()
//    {
//        if(tier != 2)
//        {
//            return "/btwmodtex/plate_1.png";
//        } else
//        {
//            return "/btwmodtex/plate_2.png";
//        }
//    }

}
