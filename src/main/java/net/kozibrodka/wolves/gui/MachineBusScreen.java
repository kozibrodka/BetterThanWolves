package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.block.entity.MachineBusBlockEntity;
import net.kozibrodka.wolves.container.MachineBusScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.opengl.GL11;

public class MachineBusScreen extends HandledScreen {

    public MachineBusScreen(PlayerInventory playerInventory, MachineBusBlockEntity machineBusBlockEntity) {
        super(new MachineBusScreenHandler(playerInventory, machineBusBlockEntity));
        backgroundHeight = 193;
    }

    protected void drawForeground() {
        textRenderer.draw("Machine Bus", 8, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 120), 0x404040);
    }

    protected void drawBackground(float f) {
        int i = minecraft.textureManager.getTextureId("/assets/wolves/stationapi/textures/gui/machine_bus.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
    }
}
