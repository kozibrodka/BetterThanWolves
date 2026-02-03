// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.block.entity.CrucibleBlockEntity;
import net.kozibrodka.wolves.container.CrucibleScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.opengl.GL11;

public class CrucibleScreen extends HandledScreen {
    private final CrucibleBlockEntity associatedTileEntityCrucible;

    public CrucibleScreen(PlayerInventory playerInventory, CrucibleBlockEntity crucibleBlockEntity) {
        super(new CrucibleScreenHandler(playerInventory, crucibleBlockEntity));
        backgroundHeight = 193;
        associatedTileEntityCrucible = crucibleBlockEntity;
    }

    protected void drawForeground() {
        textRenderer.draw("Crucible", 66, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 96) + 2, 0x404040);
    }

    protected void drawBackground(float f) {
        int i = minecraft.textureManager.getTextureId("/assets/wolves/stationapi/textures/gui/fccauldron.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
        if (associatedTileEntityCrucible.isCooking()) {
            int l = associatedTileEntityCrucible.getCookProgressScaled(12);
            drawTexture(j + 81, (k + 19 + 12) - l, 176, 12 - l, 14, l + 2);
        }
    }
}
