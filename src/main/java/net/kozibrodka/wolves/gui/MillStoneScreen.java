package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.block.entity.MillStoneBlockEntity;
import net.kozibrodka.wolves.container.MillStoneScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.opengl.GL11;

public class MillStoneScreen extends HandledScreen {
    private final MillStoneBlockEntity millStoneBlockEntity;

    public MillStoneScreen(PlayerInventory playerInventory, MillStoneBlockEntity millStoneBlockEntity) {
        super(new MillStoneScreenHandler(playerInventory, millStoneBlockEntity));
        backgroundHeight = 193;
        this.millStoneBlockEntity = millStoneBlockEntity;
    }

    protected void drawForeground() {
        textRenderer.draw("Mill Stone", 60, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 96) + 2, 0x404040);
    }

    protected void drawBackground(float f) {
        int i = minecraft.textureManager.getTextureId("/assets/wolves/stationapi/textures/gui/fcmillstone.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
        if (millStoneBlockEntity.IsGrinding()) {
            int l = millStoneBlockEntity.getGrindProgressScaled(12);
            drawTexture(j + 80, (k + 18 + 12) - l, 176, 12 - l, 14, l + 2);
        }
    }
}
