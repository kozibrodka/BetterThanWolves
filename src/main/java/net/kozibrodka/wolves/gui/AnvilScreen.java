// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.container.AnvilScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class AnvilScreen extends HandledScreen {

    public AnvilScreen(PlayerInventory inventoryplayer, World world, int i, int j, int k) {
        super(new AnvilScreenHandler(inventoryplayer, world, i, j, k));
        backgroundHeight = 202;
    }

    public void removed() {
        super.removed();
        container.onClosed(minecraft.player);
    }

    protected void drawForeground() {
        textRenderer.draw("Anvil", 29, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 96) + 2, 0x404040);
    }

    protected void drawBackground(float f) {
        int i = minecraft.textureManager.getTextureId("/assets/wolves/stationapi/gui/inventory/anvil.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
    }
}
