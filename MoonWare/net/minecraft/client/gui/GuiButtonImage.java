package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Namespaced;

public class GuiButtonImage extends ButtonWidget
{
    private final Namespaced field_191750_o;
    private final int field_191747_p;
    private final int field_191748_q;
    private final int field_191749_r;

    public GuiButtonImage(int p_i47392_1_, int p_i47392_2_, int p_i47392_3_, int p_i47392_4_, int p_i47392_5_, int p_i47392_6_, int p_i47392_7_, int p_i47392_8_, Namespaced p_i47392_9_)
    {
        super(p_i47392_1_, p_i47392_2_, p_i47392_3_, p_i47392_4_, p_i47392_5_, "");
        field_191747_p = p_i47392_6_;
        field_191748_q = p_i47392_7_;
        field_191749_r = p_i47392_8_;
        field_191750_o = p_i47392_9_;
    }

    public void func_191746_c(int p_191746_1_, int p_191746_2_)
    {
        x = p_191746_1_;
        y = p_191746_2_;
    }

    public void draw(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float partialTick)
    {
        if (visible)
        {
            hovered = p_191745_2_ >= x && p_191745_3_ >= y && p_191745_2_ < x + width && p_191745_3_ < y + height;
            Minecraft.getTextureManager().bindTexture(field_191750_o);
            GlStateManager.disableDepth();
            int i = field_191747_p;
            int j = field_191748_q;

            if (hovered)
            {
                j += field_191749_r;
            }

            drawTexturedModalRect(x, y, i, j, width, height);
            GlStateManager.enableDepth();
        }
    }
}
