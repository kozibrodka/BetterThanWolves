package net.kozibrodka.wolves.items;

import net.kozibrodka.wolves.events.ItemListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

public class KnittingItem extends LazyItemTemplate {

    private final int variant;

    public KnittingItem(Identifier identifier, int variant) {
        super(identifier);
        this.variant = variant;
        this.setMaxDamage(256);
        this.setMaxCount(1);
    }

    @Override
    public ItemStack use(ItemStack stack, World world, PlayerEntity user) {
        if (stack.getDamage2() < 255) {
            stack.damage(1, user);
        } else {
            Item outcome = switch (variant) {
                case 0 -> ItemListener.whiteWoolKnit;
                case 1 -> ItemListener.orangeWoolKnit;
                case 2 -> ItemListener.magentaWoolKnit;
                case 3 -> ItemListener.lightBlueWoolKnit;
                case 4 -> ItemListener.yellowWoolKnit;
                case 5 -> ItemListener.limeWoolKnit;
                case 6 -> ItemListener.pinkWoolKnit;
                case 7 -> ItemListener.darkGreyWoolKnit;
                case 8 -> ItemListener.lightGreyWoolKnit;
                case 9 -> ItemListener.cyanWoolKnit;
                case 10 -> ItemListener.purpleWoolKnit;
                case 11 -> ItemListener.blueWoolKnit;
                case 12 -> ItemListener.brownWoolKnit;
                case 13 -> ItemListener.greenWoolKnit;
                case 14 -> ItemListener.redWoolKnit;
                case 15 -> ItemListener.blackWoolKnit;
                default -> Item.STRING;
            };
            user.dropItem(new ItemStack(outcome, 1));
            return new ItemStack(ItemListener.knittingNeedles, 1);
        }
        return stack;
    }
}
