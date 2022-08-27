
package net.kozibrodka.wolves.items;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.armour.TemplateArmour;

public class FCItemRefinedArmor extends TemplateArmour
{

    public FCItemRefinedArmor(Identifier iItemID, int iArmorType)
    {
        super(iItemID, 1, 1, iArmorType);
        setDurability(576);
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
