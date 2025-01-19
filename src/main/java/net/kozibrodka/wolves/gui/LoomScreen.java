package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.block.entity.LoomBlockEntity;
import net.kozibrodka.wolves.container.LoomScreenHandler;
import net.kozibrodka.wolves.network.ClientScreenData;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.opengl.GL11;

public class LoomScreen extends HandledScreen {

    public LoomScreen(PlayerInventory playerInventory, LoomBlockEntity loomBlockEntity, int locX, int locY, int locZ) {
        super(new LoomScreenHandler(playerInventory, loomBlockEntity));
        backgroundHeight = 193;
        associatedLoomBlockEntity = loomBlockEntity;
        //guiX = locX;
        //guiY = locY;
        //guiZ = locZ;
    }

    protected void drawForeground() {
        textRenderer.draw("Loom", 60, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 96) + 2, 0x404040);
    }

    protected void drawBackground(float f) {
        int i = minecraft.textureManager.getTextureId("/assets/wolves/stationapi/textures/gui/fcmillstone.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
        if(associatedLoomBlockEntity.world == null) {
            //PacketHelper.send(new ScreenPacket("mill",0, guiX, guiY, guiZ));
            if(ClientScreenData.isGrinding()) {
                int l = ClientScreenData.getGrindProgressScaled(12);
                drawTexture(j + 80, (k + 18 + 12) - l, 176, 12 - l, 14, l + 2);
            }
        } else {
            if(associatedLoomBlockEntity.IsGrinding()) {
                int l = associatedLoomBlockEntity.getGrindProgressScaled(12);
                drawTexture(j + 80, (k + 18 + 12) - l, 176, 12 - l, 14, l + 2);
            }
        }
    }

    private final LoomBlockEntity associatedLoomBlockEntity;
    //private final int guiX;
    //private final int guiY;
    //private final int guiZ;
}
