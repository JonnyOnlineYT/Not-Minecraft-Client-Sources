package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Namespaced;

public class GuiButtonToggle extends ButtonWidget
{
    protected Namespaced field_191760_o;
    protected boolean field_191755_p;
    protected int field_191756_q;
    protected int field_191757_r;
    protected int field_191758_s;
    protected int field_191759_t;

    public GuiButtonToggle(int p_i47389_1_, int p_i47389_2_, int p_i47389_3_, int p_i47389_4_, int p_i47389_5_, boolean p_i47389_6_)
    {
        super(p_i47389_1_, p_i47389_2_, p_i47389_3_, p_i47389_4_, p_i47389_5_, "");
        field_191755_p = p_i47389_6_;
    }

    public void func_191751_a(int p_191751_1_, int p_191751_2_, int p_191751_3_, int p_191751_4_, Namespaced p_191751_5_)
    {
        field_191756_q = p_191751_1_;
        field_191757_r = p_191751_2_;
        field_191758_s = p_191751_3_;
        field_191759_t = p_191751_4_;
        field_191760_o = p_191751_5_;
    }

    public void func_191753_b(boolean p_191753_1_)
    {
        field_191755_p = p_191753_1_;
    }

    public boolean func_191754_c()
    {
        return field_191755_p;
    }

    public void func_191752_c(int p_191752_1_, int p_191752_2_)
    {
        x = p_191752_1_;
        y = p_191752_2_;
    }

    public void draw(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float partialTick)
    {
        if (visible)
        {
            hovered = p_191745_2_ >= x && p_191745_3_ >= y && p_191745_2_ < x + width && p_191745_3_ < y + height;
            Minecraft.getTextureManager().bindTexture(field_191760_o);
            GlStateManager.disableDepth();
            int i = field_191756_q;
            int j = field_191757_r;

            if (field_191755_p)
            {
                i += field_191758_s;
            }

            if (hovered)
            {
                j += field_191759_t;
            }

            drawTexturedModalRect(x, y, i, j, width, height);
            GlStateManager.enableDepth();
        }
    }
}
