package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.block.entity.MillStoneBlockEntity;
import net.kozibrodka.wolves.container.MillStoneScreenHandler;
import net.kozibrodka.wolves.network.ClientScreenData;
import net.kozibrodka.wolves.network.ScreenPacket;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.lwjgl.opengl.GL11;

public class MillStoneScreen extends HandledScreen {

    public MillStoneScreen(PlayerInventory inventoryplayer, MillStoneBlockEntity fctileentitymillstone, int locX, int locY, int locZ) {
        super(new MillStoneScreenHandler(inventoryplayer, fctileentitymillstone));
        backgroundHeight = 193;
        associatedTileEntityMillStone = fctileentitymillstone;
        guiX = locX;
        guiY = locY;
        guiZ = locZ;
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
        if (associatedTileEntityMillStone.world == null) {
            {
                PacketHelper.send(new ScreenPacket("mill", 0, guiX, guiY, guiZ));
                if (ClientScreenData.isGrinding()) {
                    int l = ClientScreenData.getGrindProgressScaled(12);
                    drawTexture(j + 80, (k + 18 + 12) - l, 176, 12 - l, 14, l + 2);
                }
            }
        } else {
            if (associatedTileEntityMillStone.IsGrinding()) {
                int l = associatedTileEntityMillStone.getGrindProgressScaled(12);
                drawTexture(j + 80, (k + 18 + 12) - l, 176, 12 - l, 14, l + 2);
            }
        }
    }

    static final int iMillStoneGuiHeight = 193;
    static final int iMillStoneFireIconHeight = 12;
    private final MillStoneBlockEntity associatedTileEntityMillStone;
    private final int guiX;
    private final int guiY;
    private final int guiZ;
}
