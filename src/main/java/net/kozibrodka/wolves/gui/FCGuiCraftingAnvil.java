// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.container.AnvilContainer;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.level.Level;
import org.lwjgl.opengl.GL11;

public class FCGuiCraftingAnvil extends ContainerBase
{

    public FCGuiCraftingAnvil(PlayerInventory inventoryplayer, Level world, int i, int j, int k)
    {
        super(new AnvilContainer(inventoryplayer, world, i, j, k));
        containerHeight = 202;
    }

    public void onClose()
    {
        super.onClose();
        container.onClosed(minecraft.player);
    }

    protected void renderForeground()
    {
        textManager.drawText("Anvil", 29, 6, 0x404040);
        textManager.drawText("Inventory", 8, (containerHeight - 96) + 2, 0x404040);
    }

    protected void renderContainerBackground(float f)
    {
        int i = minecraft.textureManager.getTextureId("/assets/wolves/stationapi/gui/inventory/anvil.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - containerWidth) / 2;
        int k = (height - containerHeight) / 2;
        blit(j, k, 0, 0, containerWidth, containerHeight);
    }
}
