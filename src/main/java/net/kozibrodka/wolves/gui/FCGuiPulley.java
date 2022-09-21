// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.container.FCContainerMillStone;
import net.kozibrodka.wolves.container.FCContainerPulley;
import net.kozibrodka.wolves.tileentity.FCTileEntityMillStone;
import net.kozibrodka.wolves.tileentity.FCTileEntityPulley;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.opengl.GL11;

public class FCGuiPulley extends ContainerBase
{

    public FCGuiPulley(PlayerInventory inventoryplayer, FCTileEntityPulley fctileentitypulley)
    {
        super(new FCContainerPulley(inventoryplayer, fctileentitypulley));
        containerHeight = 174;
        associatedTileEntityPulley = fctileentitypulley;
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
        if(associatedTileEntityPulley.IsMechanicallyPowered())
        {
            blit(j + 80, k + 18, 176, 0, 14, 14);
        }
    }

    static final int iPulleyGuiHeight = 174;
    static final int iPulleyMachineIconWidth = 14;
    static final int iPulleyMachineIconHeight = 14;
    private FCTileEntityPulley associatedTileEntityPulley;
}
