
package net.kozibrodka.wolves.items;
import net.kozibrodka.wolves.events.ConfigListener;
import net.minecraft.item.armour.Armour;
import net.modificationstation.stationapi.api.client.item.ArmourTextureProvider;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.armour.TemplateArmour;

public class RefinedArmour extends TemplateArmour implements ArmourTextureProvider
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
