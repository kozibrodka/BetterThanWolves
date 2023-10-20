// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.container.BlockDispenserContainer;
import net.kozibrodka.wolves.tileentity.BlockDispenserTileEntity;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.opengl.GL11;

public class BlockDispenserGUI extends ContainerBase
{

    public BlockDispenserGUI(PlayerInventory inventoryplayer, BlockDispenserTileEntity fctileentityblockdispenser)
    {
        super(new BlockDispenserContainer(inventoryplayer, fctileentityblockdispenser));
        associatedTileEntityBlockDispenser = fctileentityblockdispenser;
    }

    protected void renderForeground()
    {
        textManager.drawText("Block Dispenser", 48, 6, 0x404040);
        textManager.drawText("Inventory", 8, (containerHeight - 96) + 2, 0x404040);
    }

    protected void renderContainerBackground(float f)
    {
        int i = minecraft.textureManager.getTextureId("/assets/wolves/stationapi/textures/gui/fcguiblockdisp.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - containerWidth) / 2;
        int k = (height - containerHeight) / 2;
        blit(j, k, 0, 0, containerWidth, containerHeight);
        int l = (associatedTileEntityBlockDispenser.iNextSlotIndexToDispense % 3) * 18;
        int i1 = (associatedTileEntityBlockDispenser.iNextSlotIndexToDispense / 3) * 18;
        blit(j + 60 + l, k + 15 + i1, 176, 0, 20, 20);
    }

    static final int iSelectionIconHeight = 20;
    private BlockDispenserTileEntity associatedTileEntityBlockDispenser;
}
