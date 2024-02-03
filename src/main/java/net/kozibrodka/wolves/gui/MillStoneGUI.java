package net.kozibrodka.wolves.gui;

import net.fabricmc.loader.api.FabricLoader;
import net.kozibrodka.wolves.container.MillStoneContainer;
import net.kozibrodka.wolves.network.ClientGuiData;
import net.kozibrodka.wolves.network.GuiPacket;
import net.kozibrodka.wolves.network.SoundPacket;
import net.kozibrodka.wolves.tileentity.MillStoneTileEntity;
import net.minecraft.block.BlockBase;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.lwjgl.opengl.GL11;

public class MillStoneGUI extends ContainerBase
{

    public MillStoneGUI(PlayerInventory inventoryplayer, MillStoneTileEntity fctileentitymillstone, int locX, int locY, int locZ)
    {
        super(new MillStoneContainer(inventoryplayer, fctileentitymillstone));
        containerHeight = 193;
        associatedTileEntityMillStone = fctileentitymillstone;
        guiX = locX;
        guiY = locY;
        guiZ = locZ;
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
        if(associatedTileEntityMillStone.level == null){
            {
                PacketHelper.send(new GuiPacket("mill",0, guiX, guiY, guiZ));
                if(ClientGuiData.isGrinding())
                {
                    int l = ClientGuiData.getGrindProgressScaled(12);
                    blit(j + 80, (k + 18 + 12) - l, 176, 12 - l, 14, l + 2);
                }
            }
        }else{
            if(associatedTileEntityMillStone.IsGrinding())
            {
                int l = associatedTileEntityMillStone.getGrindProgressScaled(12);
                blit(j + 80, (k + 18 + 12) - l, 176, 12 - l, 14, l + 2);
            }
        }
    }

    static final int iMillStoneGuiHeight = 193;
    static final int iMillStoneFireIconHeight = 12;
    private MillStoneTileEntity associatedTileEntityMillStone;
    private int guiX;
    private int guiY;
    private int guiZ;
}
