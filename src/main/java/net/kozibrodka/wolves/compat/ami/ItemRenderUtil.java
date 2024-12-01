package net.kozibrodka.wolves.compat.ami;

import net.minecraft.class_583;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class ItemRenderUtil {

    private static final ItemRenderer ITEM_RENDERER = new ItemRenderer();

    public static void drawScaledItem(Minecraft minecraft, ItemStack itemStack, int x, int y, float scale) {
        // Shading
        GL11.glEnable(32826);
        GL11.glPushMatrix();
        GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
        class_583.method_1930();
        GL11.glPopMatrix();
        // Item rendering with scale
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        ITEM_RENDERER.method_1487(minecraft.textRenderer, minecraft.textureManager, itemStack, (int) (x / scale), (int) (y / scale));
        GL11.glPopMatrix();
    }
}
