package net.kozibrodka.wolves.itemblocks;

import net.kozibrodka.wolves.events.TextureListener;
import net.minecraft.item.Block;
import net.minecraft.item.ItemInstance;

public class VaseItemBlock extends Block
{

    public VaseItemBlock(int i)
    {
        super(i);
        setDurability(0);
        setHasSubItems(true);
        setTranslationKey("fcVase");
    }

    public int getMetaData(int i)
    {
        return i;
    }

    //EXTRA
    public String getTranslationKey(ItemInstance itemstack) //getItemNameIS
    {
        return switch (itemstack.getDamage()) {
            case 0 -> super.getTranslationKey() + "." + "white";
            case 1 -> super.getTranslationKey() + "." + "orange";
            case 2 -> super.getTranslationKey() + "." + "magenta";
            case 3 -> super.getTranslationKey() + "." + "light_blue";
            case 4 -> super.getTranslationKey() + "." + "yellow";
            case 5 -> super.getTranslationKey() + "." + "lime";
            case 6 -> super.getTranslationKey() + "." + "pink";
            case 7 -> super.getTranslationKey() + "." + "gray";
            case 8 -> super.getTranslationKey() + "." + "light_gray";
            case 9 -> super.getTranslationKey() + "." + "cyan";
            case 10 -> super.getTranslationKey() + "." + "purple";
            case 11 -> super.getTranslationKey() + "." + "blue";
            case 12 -> super.getTranslationKey() + "." + "brown";
            case 13 -> super.getTranslationKey() + "." + "green";
            case 14 -> super.getTranslationKey() + "." + "red";
            case 15 -> super.getTranslationKey() + "." + "black";
            default -> super.getTranslationKey() + "." + "chuj";
        };
    }
}
