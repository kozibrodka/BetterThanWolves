// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.container.CauldronContainer;
import net.kozibrodka.wolves.network.ClientGuiData;
import net.kozibrodka.wolves.network.GuiPacket;
import net.kozibrodka.wolves.tileentity.CauldronTileEntity;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.lwjgl.opengl.GL11;


public class CauldronGUI extends ContainerBase
{

    public CauldronGUI(PlayerInventory inventoryplayer, CauldronTileEntity fctileentitycauldron, int locX, int locY, int locZ)
    {
        super(new CauldronContainer(inventoryplayer, fctileentitycauldron));
        containerHeight = 193;
        associatedTileEntityCauldron = fctileentitycauldron;
        guiX = locX;
        guiY = locY;
        guiZ = locZ;
    }

    protected void renderForeground()
    {
        textManager.drawText("Cauldron", 66, 6, 0x404040);
        textManager.drawText("Inventory", 8, (containerHeight - 96) + 2, 0x404040);
    }

    protected void renderContainerBackground(float f)
    {
        int i = minecraft.textureManager.getTextureId("/assets/wolves/stationapi/textures/gui/fccauldron.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - containerWidth) / 2;
        int k = (height - containerHeight) / 2;
        blit(j, k, 0, 0, containerWidth, containerHeight);
        if(associatedTileEntityCauldron.level == null){
            {
                PacketHelper.send(new GuiPacket("cauldron",0, guiX, guiY, guiZ));
                if(ClientGuiData.IsCooking())
                {
                    int l = ClientGuiData.getCookProgressScaled(12);
                    blit(j + 81, (k + 19 + 12) - l, 176, 12 - l, 14, l + 2);
                }
            }
        }else{
            if(associatedTileEntityCauldron.IsCooking())
            {
                int l = associatedTileEntityCauldron.getCookProgressScaled(12);
                blit(j + 81, (k + 19 + 12) - l, 176, 12 - l, 14, l + 2);
            }
        }
    }

    static final int iCauldronGuiHeight = 193;
    static final int iCauldronFireIconHeight = 12;
    private CauldronTileEntity associatedTileEntityCauldron;
    private int guiX;
    private int guiY;
    private int guiZ;
}
