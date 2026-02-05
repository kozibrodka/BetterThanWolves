// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.block.entity.CauldronBlockEntity;
import net.kozibrodka.wolves.container.CauldronScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.opengl.GL11;


public class CauldronScreen extends HandledScreen {
    private final CauldronBlockEntity cauldronBlockEntity;

    public CauldronScreen(PlayerInventory playerInventory, CauldronBlockEntity cauldronBlockEntity) {
        super(new CauldronScreenHandler(playerInventory, cauldronBlockEntity));
        backgroundHeight = 193;
        this.cauldronBlockEntity = cauldronBlockEntity;
    }

    protected void drawForeground() {
        textRenderer.draw("Cauldron", 66, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 96) + 2, 0x404040);
    }

    protected void drawBackground(float f) {
        int i = minecraft.textureManager.getTextureId("/assets/wolves/stationapi/textures/gui/fccauldron.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
        if (cauldronBlockEntity.IsCooking()) {
            int l = cauldronBlockEntity.getCookProgressScaled(12);
            drawTexture(j + 81, (k + 19 + 12) - l, 176, 12 - l, 14, l + 2);
        }
    }
}
