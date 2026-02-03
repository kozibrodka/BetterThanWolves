// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.block.entity.BlockDispenserBlockEntity;
import net.kozibrodka.wolves.container.BlockDispenserScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.opengl.GL11;

public class BlockDispenserScreen extends HandledScreen {
    private final BlockDispenserBlockEntity blockDispenserBlockEntity;

    public BlockDispenserScreen(PlayerInventory playerInventory, BlockDispenserBlockEntity blockDispenserBlockEntity) {
        super(new BlockDispenserScreenHandler(playerInventory, blockDispenserBlockEntity));
        this.blockDispenserBlockEntity = blockDispenserBlockEntity;
    }

    protected void drawForeground() {
        textRenderer.draw("Block Dispenser", 48, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 96) + 2, 0x404040);
    }

    protected void drawBackground(float f) {
        int i = minecraft.textureManager.getTextureId("/assets/wolves/stationapi/textures/gui/fcguiblockdisp.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
        int l = (blockDispenserBlockEntity.nextDispenserSlot % 3) * 18;
        int i1 = (blockDispenserBlockEntity.nextDispenserSlot / 3) * 18;
        drawTexture(j + 60 + l, k + 15 + i1, 176, 0, 20, 20);
    }
}
