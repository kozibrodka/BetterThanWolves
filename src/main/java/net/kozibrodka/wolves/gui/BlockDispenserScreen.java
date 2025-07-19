// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.block.entity.BlockDispenserBlockEntity;
import net.kozibrodka.wolves.container.BlockDispenserScreenHandler;
import net.kozibrodka.wolves.network.ClientScreenData;
import net.kozibrodka.wolves.network.ScreenPacket;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.lwjgl.opengl.GL11;

public class BlockDispenserScreen extends HandledScreen {

    public BlockDispenserScreen(PlayerInventory inventoryplayer, BlockDispenserBlockEntity fctileentityblockdispenser, int locX, int locY, int locZ) {
        super(new BlockDispenserScreenHandler(inventoryplayer, fctileentityblockdispenser));
        associatedTileEntityBlockDispenser = fctileentityblockdispenser;
        guiX = locX;
        guiY = locY;
        guiZ = locZ;
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

        if (associatedTileEntityBlockDispenser.world == null) {
            PacketHelper.send(new ScreenPacket("dispenser", 0, guiX, guiY, guiZ));
            int l = (ClientScreenData.count % 3) * 18;
            int i1 = (ClientScreenData.count / 3) * 18;
            drawTexture(j + 60 + l, k + 15 + i1, 176, 0, 20, 20);
        } else {
            int l = (associatedTileEntityBlockDispenser.iNextSlotIndexToDispense % 3) * 18;
            int i1 = (associatedTileEntityBlockDispenser.iNextSlotIndexToDispense / 3) * 18;
            drawTexture(j + 60 + l, k + 15 + i1, 176, 0, 20, 20);
        }
    }

    static final int iSelectionIconHeight = 20;
    private final BlockDispenserBlockEntity associatedTileEntityBlockDispenser;
    private final int guiX;
    private final int guiY;
    private final int guiZ;
}
