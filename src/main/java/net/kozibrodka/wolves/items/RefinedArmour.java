
package net.kozibrodka.wolves.items;
import net.kozibrodka.wolves.events.mod_FCBetterThanWolves;
import net.minecraft.item.armour.Armour;
import net.modificationstation.stationapi.api.client.item.ArmourTextureProvider;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.template.item.armour.TemplateArmour;
import net.modificationstation.stationapi.api.util.Null;

public class RefinedArmour extends TemplateArmour implements ArmourTextureProvider
{

    public RefinedArmour(Identifier iItemID, int iArmorType)
    {
        super(iItemID, 1, 1, iArmorType);
        setDurability(576);
    }


    @Override
    public Identifier getTexture(Armour armour) {
        return mod_FCBetterThanWolves.MOD_ID.id("plate");
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
