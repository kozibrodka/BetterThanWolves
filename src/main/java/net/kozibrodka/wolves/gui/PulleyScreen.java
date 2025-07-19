// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.block.entity.PulleyBlockEntity;
import net.kozibrodka.wolves.container.PulleyScreenHandler;
import net.kozibrodka.wolves.network.ClientScreenData;
import net.kozibrodka.wolves.network.ScreenPacket;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.lwjgl.opengl.GL11;

public class PulleyScreen extends HandledScreen {

    public PulleyScreen(PlayerInventory inventoryplayer, PulleyBlockEntity fctileentitypulley, int locX, int locY, int locZ) {
        super(new PulleyScreenHandler(inventoryplayer, fctileentitypulley));
        backgroundHeight = 174;
        associatedTileEntityPulley = fctileentitypulley;
        guiX = locX;
        guiY = locY;
        guiZ = locZ;
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

        if (associatedTileEntityPulley.world == null) {
            PacketHelper.send(new ScreenPacket("pulley", 0, guiX, guiY, guiZ));
            if (ClientScreenData.isPowered()) {
                drawTexture(j + 80, k + 18, 176, 0, 14, 14);
            }
        } else {
            if (associatedTileEntityPulley.IsMechanicallyPowered()) {
                drawTexture(j + 80, k + 18, 176, 0, 14, 14);
            }
        }
    }

    static final int iPulleyGuiHeight = 174;
    static final int iPulleyMachineIconWidth = 14;
    static final int iPulleyMachineIconHeight = 14;
    private final PulleyBlockEntity associatedTileEntityPulley;
    private final int guiX;
    private final int guiY;
    private final int guiZ;
}
