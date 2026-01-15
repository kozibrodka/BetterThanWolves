package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.block.entity.DropperBlockEntity;
import net.kozibrodka.wolves.container.DropperScreenHandler;
import net.kozibrodka.wolves.network.ClientScreenData;
import net.kozibrodka.wolves.network.ScreenPacket;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.lwjgl.opengl.GL11;

public class DropperScreen extends HandledScreen {

    private final DropperBlockEntity associatedBlockEntity;
    private final int guiX;
    private final int guiY;
    private final int guiZ;

    public DropperScreen(PlayerInventory playerInventory, DropperBlockEntity dropperBlockEntity, int locX, int locY, int locZ) {
        super(new DropperScreenHandler(playerInventory, dropperBlockEntity));
        backgroundHeight = 193;
        associatedBlockEntity = dropperBlockEntity;
        guiX = locX;
        guiY = locY;
        guiZ = locZ;
    }

    protected void drawForeground() {
        textRenderer.draw("Dropper", 70, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 96) + 2, 0x404040);
    }

    protected void drawBackground(float f) {
        int i = minecraft.textureManager.getTextureId("/assets/wolves/stationapi/textures/gui/dropper.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);

        if (associatedBlockEntity.world == null) {
            PacketHelper.send(new ScreenPacket("dropper", 0, guiX, guiY, guiZ));
            if (ClientScreenData.isPowered()) {
                drawTexture(j + 80, k + 18, 176, 0, 14, 14);
            }
        }
    }
}
