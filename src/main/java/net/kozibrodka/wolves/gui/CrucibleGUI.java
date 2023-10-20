// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.container.CrucibleContainer;
import net.kozibrodka.wolves.tileentity.CrucibleTileEntity;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.opengl.GL11;

public class CrucibleGUI extends ContainerBase
{

    public CrucibleGUI(PlayerInventory inventoryplayer, CrucibleTileEntity fctileentitycrucible)
    {
        super(new CrucibleContainer(inventoryplayer, fctileentitycrucible));
        containerHeight = 193;
        associatedTileEntityCrucible = fctileentitycrucible;
    }

    protected void renderForeground()
    {
        textManager.drawText("Crucible", 66, 6, 0x404040);
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
        if(associatedTileEntityCrucible.IsCooking())
        {
            int l = associatedTileEntityCrucible.getCookProgressScaled(12);
            blit(j + 81, (k + 19 + 12) - l, 176, 12 - l, 14, l + 2);
        }
    }

    static final int iCrucibleGuiHeight = 193;
    static final int iCrucibleFireIconHeight = 12;
    private CrucibleTileEntity associatedTileEntityCrucible;
}
