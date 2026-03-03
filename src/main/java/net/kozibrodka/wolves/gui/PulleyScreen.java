// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.block.entity.PulleyBlockEntity;
import net.kozibrodka.wolves.container.PulleyScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.opengl.GL11;

public class PulleyScreen extends HandledScreen {
    private final PulleyBlockEntity pulleyBlockEntity;


    public PulleyScreen(PlayerInventory playerInventory, PulleyBlockEntity pulleyBlockEntity) {
        super(new PulleyScreenHandler(playerInventory, pulleyBlockEntity));
        backgroundHeight = 174;
        this.pulleyBlockEntity = pulleyBlockEntity;
    }

    protected void drawForeground() {
        textRenderer.draw("Pulley", 75, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 96) + 2, 0x404040);
    }

    protected void drawBackground(float f) {
        int i = minecraft.textureManager.getTextureId("/assets/wolves/stationapi/textures/gui/fcguipulley.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
        pulleyBlockEntity.updateMechanicallyPowered();
        if (pulleyBlockEntity.poweredForClient()) {
            drawTexture(j + 80, k + 18, 176, 0, 14, 14);
        }
    }
}
