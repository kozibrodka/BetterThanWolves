//// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
//// Jad home page: http://www.kpdus.com/jad.html
//// Decompiler options: packimports(3) braces deadcode
//
//package net.kozibrodka.wolves.gui;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.screen.container.ContainerBase;
//import net.minecraft.entity.player.PlayerInventory;
//import org.lwjgl.opengl.GL11;
//
//public class FCGuiHopper extends ContainerBase
//{
//
//    public FCGuiHopper(PlayerInventory inventoryplayer, FCTileEntityHopper fctileentityhopper)
//    {
//        super(new FCContainerHopper(inventoryplayer, fctileentityhopper));
//        containerHeight = 193;
//        associatedTileEntityHopper = fctileentityhopper;
//    }
//
//    protected void renderForeground()
//    {
//        textManager.drawText("Hopper", 70, 6, 0x404040);
//        textManager.drawText("Inventory", 8, (containerHeight - 96) + 2, 0x404040);
//    }
//
//    protected void renderContainerBackground(float f)
//    {
//        int i = minecraft.textureManager.getTextureId("/assets/wolves/stationapi/textures/gui/fcHopper.png");
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        minecraft.textureManager.bindTexture(i);
//        int j = (width - containerWidth) / 2;
//        int k = (height - containerHeight) / 2;
//        blit(j, k, 0, 0, containerWidth, containerHeight);
//        if(associatedTileEntityHopper.IsEjecting())
//        {
//            blit(j + 80, k + 18, 176, 0, 14, 14);
//        }
//    }
//
//    static final int iHopperGuiHeight = 193;
//    static final int iHopperMachineIconHeight = 14;
//    private FCTileEntityHopper associatedTileEntityHopper;
//}
