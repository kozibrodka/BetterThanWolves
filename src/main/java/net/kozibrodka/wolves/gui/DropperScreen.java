package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.block.entity.DropperBlockEntity;
import net.kozibrodka.wolves.container.DropperScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.opengl.GL11;

public class DropperScreen extends HandledScreen {

    private final DropperBlockEntity dropperBlockEntity;

    public DropperScreen(PlayerInventory playerInventory, DropperBlockEntity dropperBlockEntity) {
        super(new DropperScreenHandler(playerInventory, dropperBlockEntity));
        backgroundHeight = 193;
        this.dropperBlockEntity = dropperBlockEntity;
    }

    protected void drawForeground() {
        textRenderer.draw("Dropper", 70, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 96) + 2, 0x404040);
    }

    protected void drawBackground(float f) {
        int i = minecraft.textureManager.getTextureId("/assets/wolves/stationapi/textures/gui/dropper.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
        if (dropperBlockEntity.isPowered()) {
            drawTexture(j + 80, k + 18, 176, 0, 14, 14);
        }
    }
}
