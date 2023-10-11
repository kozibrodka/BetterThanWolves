package net.kozibrodka.wolves.gui;

import net.kozibrodka.wolves.container.MillStoneContainer;
import net.kozibrodka.wolves.tileentity.FCTileEntityMillStone;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.opengl.GL11;

public class FCGuiMillStone extends ContainerBase
{

    public FCGuiMillStone(PlayerInventory inventoryplayer, FCTileEntityMillStone fctileentitymillstone)
    {
        super(new MillStoneContainer(inventoryplayer, fctileentitymillstone));
        containerHeight = 193;
        associatedTileEntityMillStone = fctileentitymillstone;
    }

    protected void renderForeground()
    {
        textManager.drawText("Mill Stone", 60, 6, 0x404040);
        textManager.drawText("Inventory", 8, (containerHeight - 96) + 2, 0x404040);
    }

    protected void renderContainerBackground(float f)
    {
        int i = minecraft.textureManager.getTextureId("/assets/wolves/stationapi/textures/gui/fcmillstone.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - containerWidth) / 2;
        int k = (height - containerHeight) / 2;
        blit(j, k, 0, 0, containerWidth, containerHeight);
        if(associatedTileEntityMillStone.IsGrinding())
        {
            int l = associatedTileEntityMillStone.getGrindProgressScaled(12);
            blit(j + 80, (k + 18 + 12) - l, 176, 12 - l, 14, l + 2);
        }
    }

    static final int iMillStoneGuiHeight = 193;
    static final int iMillStoneFireIconHeight = 12;
    private FCTileEntityMillStone associatedTileEntityMillStone;
}
