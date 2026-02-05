package net.kozibrodka.wolves.container.autoanvil;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class InternalAutoAnvilScreenHandler extends ScreenHandler {

    public InternalAutoAnvilScreenHandler(Inventory inventory) {
        for(int slotIndex = 0; slotIndex < inventory.size(); slotIndex++) {
            int yIndex = slotIndex / 5;
            this.addSlot(new Slot(inventory, slotIndex, slotIndex - yIndex * 5, yIndex));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return false;
    }
}