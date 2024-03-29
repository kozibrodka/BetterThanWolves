// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.container.PulleyContainer;
import net.kozibrodka.wolves.network.ClientGuiData;
import net.kozibrodka.wolves.network.GuiPacket;
import net.kozibrodka.wolves.tileentity.PulleyTileEntity;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.lwjgl.opengl.GL11;

public class PulleyGUI extends ContainerBase
{

    public PulleyGUI(PlayerInventory inventoryplayer, PulleyTileEntity fctileentitypulley, int locX, int locY, int locZ)
    {
        super(new PulleyContainer(inventoryplayer, fctileentitypulley));
        containerHeight = 174;
        associatedTileEntityPulley = fctileentitypulley;
        guiX = locX;
        guiY = locY;
        guiZ = locZ;
    }

    protected void renderForeground()
    {
        textManager.drawText("Pulley", 75, 6, 0x404040);
        textManager.drawText("Inventory", 8, (containerHeight - 96) + 2, 0x404040);
    }

    protected void renderContainerBackground(float f)
    {
        int i = minecraft.textureManager.getTextureId("/assets/wolves/stationapi/textures/gui/fcguipulley.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - containerWidth) / 2;
        int k = (height - containerHeight) / 2;
        blit(j, k, 0, 0, containerWidth, containerHeight);

        if(associatedTileEntityPulley.level == null){
            PacketHelper.send(new GuiPacket("pulley",0, guiX, guiY, guiZ));
            if(ClientGuiData.isPowered())
            {
                blit(j + 80, k + 18, 176, 0, 14, 14);
            }
        }else{
            if(associatedTileEntityPulley.IsMechanicallyPowered())
            {
                blit(j + 80, k + 18, 176, 0, 14, 14);
            }
        }
    }

    static final int iPulleyGuiHeight = 174;
    static final int iPulleyMachineIconWidth = 14;
    static final int iPulleyMachineIconHeight = 14;
    private PulleyTileEntity associatedTileEntityPulley;
    private int guiX;
    private int guiY;
    private int guiZ;
}
