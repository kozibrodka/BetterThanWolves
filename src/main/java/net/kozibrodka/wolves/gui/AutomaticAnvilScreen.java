package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.block.entity.AutomaticAnvilBlockEntity;
import net.kozibrodka.wolves.container.AutomaticAnvilScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.opengl.GL11;

public class AutomaticAnvilScreen extends HandledScreen {
    private final AutomaticAnvilBlockEntity automaticAnvilBlockEntity;

    public AutomaticAnvilScreen(PlayerInventory playerInventory, AutomaticAnvilBlockEntity automaticAnvilBlockEntity) {
        super(new AutomaticAnvilScreenHandler(playerInventory, automaticAnvilBlockEntity));
        this.automaticAnvilBlockEntity = automaticAnvilBlockEntity;
        backgroundHeight = 202;
    }

    public void removed() {
        super.removed();
        container.onClosed(minecraft.player);
    }

    protected void drawForeground() {
        textRenderer.draw("Automatic Anvil", 29, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 96) + 2, 0x404040);
    }

    protected void drawBackground(float f) {
        int i = minecraft.textureManager.getTextureId("/assets/wolves/stationapi/textures/gui/auto_anvil.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
        int i1 = automaticAnvilBlockEntity.getScaledCraftingProgress(24);
        this.drawTexture(j + 107, k + 70, 176, 14, i1 + 1, 16);
    }
}
