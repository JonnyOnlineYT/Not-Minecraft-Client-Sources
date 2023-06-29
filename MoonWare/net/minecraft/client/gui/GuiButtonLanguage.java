package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.renderer.GlStateManager;

public class GuiButtonLanguage extends ButtonWidget
{
    public GuiButtonLanguage(int buttonID, int xPos, int yPos)
    {
        super(buttonID, xPos, yPos, 20, 20, "");
    }

    public void draw(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float partialTick)
    {
        if (visible)
        {
            Minecraft.getTextureManager().bindTexture(ButtonWidget.BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = p_191745_2_ >= x && p_191745_3_ >= y && p_191745_2_ < x + width && p_191745_3_ < y + height;
            int i = 106;

            if (flag)
            {
                i += height;
            }

            drawTexturedModalRect(x, y, 0, i, width, height);
        }
    }
}
