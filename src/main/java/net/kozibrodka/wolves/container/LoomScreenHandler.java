package net.kozibrodka.wolves.container;

import net.kozibrodka.wolves.block.entity.LoomBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class LoomScreenHandler extends ScreenHandler {

    public LoomScreenHandler(Inventory playerinventory, LoomBlockEntity loomBlockEntity) {
        localLoomBlockEntity = loomBlockEntity;

        for(int xIndex = 0; xIndex < 2; xIndex++) {
            for(int yIndex = 0; yIndex < 2; yIndex++) {
                addSlot(new Slot(loomBlockEntity, xIndex + yIndex * 2, 26 + xIndex * 18, 61 + yIndex * 18));
            }
        }

        for(int xIndex = 0; xIndex < 2; xIndex++) {
            for(int yIndex = 0; yIndex < 2; yIndex++) {
                addSlot(new Slot(loomBlockEntity, xIndex + yIndex * 2 + 4, 116 + xIndex * 18, 61 + yIndex * 18));
            }
        }

        addSlot(new Slot(loomBlockEntity, 8, 80, 43));

        for(int xIndex = 0; xIndex < 3; xIndex++) {
            for(int yIndex = 0; yIndex < 9; yIndex++) {
                addSlot(new Slot(playerinventory, yIndex + xIndex * 9 + 9, 8 + yIndex * 18, 111 + xIndex * 18));
            }
        }

        for(int iColumn = 0; iColumn < 9; iColumn++) {
            addSlot(new Slot(playerinventory, iColumn, 8 + iColumn * 18, 169));
        }

    }

    public boolean canUse(PlayerEntity playerEntity) {
        return localLoomBlockEntity.canPlayerUse(playerEntity);
    }

    private final LoomBlockEntity localLoomBlockEntity;
}

