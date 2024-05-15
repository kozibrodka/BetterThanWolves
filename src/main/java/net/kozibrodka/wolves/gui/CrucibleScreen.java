// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.container.CrucibleScreenHandler;
import net.kozibrodka.wolves.network.ClientScreenData;
import net.kozibrodka.wolves.network.ScreenPacket;
import net.kozibrodka.wolves.block.entity.CrucibleBlockEntity;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.lwjgl.opengl.GL11;

public class CrucibleScreen extends HandledScreen
{

    public CrucibleScreen(PlayerInventory inventoryplayer, CrucibleBlockEntity fctileentitycrucible, int locX, int locY, int locZ)
    {
        super(new CrucibleScreenHandler(inventoryplayer, fctileentitycrucible));
        backgroundHeight = 193;
        associatedTileEntityCrucible = fctileentitycrucible;
        guiX = locX;
        guiY = locY;
        guiZ = locZ;
    }

    protected void drawForeground()
    {
        textRenderer.draw("Crucible", 66, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 96) + 2, 0x404040);
    }

    protected void drawBackground(float f)
    {
        int i = minecraft.textureManager.getTextureId("/assets/wolves/stationapi/textures/gui/fccauldron.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);

        if(associatedTileEntityCrucible.world == null){
            {
                PacketHelper.send(new ScreenPacket("crucible",0, guiX, guiY, guiZ));
                if(ClientScreenData.IsCooking())
                {
                    int l = ClientScreenData.getCookProgressScaled(12);
                    drawTexture(j + 81, (k + 19 + 12) - l, 176, 12 - l, 14, l + 2);
                }
            }
        }else{
            if(associatedTileEntityCrucible.isCooking())
            {
                int l = associatedTileEntityCrucible.getCookProgressScaled(12);
                drawTexture(j + 81, (k + 19 + 12) - l, 176, 12 - l, 14, l + 2);
            }
        }
    }

    static final int iCrucibleGuiHeight = 193;
    static final int iCrucibleFireIconHeight = 12;
    private CrucibleBlockEntity associatedTileEntityCrucible;
    private int guiX;
    private int guiY;
    private int guiZ;
}
