package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiLabel extends Gui
{
    protected int width = 200;
    protected int height = 20;
    public int x;
    public int y;
    private final List<String> labels;
    public int id;
    private boolean centered;
    public boolean visible = true;
    private boolean labelBgEnabled;
    private final int textColor;
    private int backColor;
    private int ulColor;
    private int brColor;
    private final Font font;
    private int border;

    public GuiLabel(Font fontObj, int p_i45540_2_, int p_i45540_3_, int p_i45540_4_, int p_i45540_5_, int p_i45540_6_, int p_i45540_7_)
    {
        font = fontObj;
        id = p_i45540_2_;
        x = p_i45540_3_;
        y = p_i45540_4_;
        width = p_i45540_5_;
        height = p_i45540_6_;
        labels = Lists.newArrayList();
        centered = false;
        labelBgEnabled = false;
        textColor = p_i45540_7_;
        backColor = -1;
        ulColor = -1;
        brColor = -1;
        border = 0;
    }

    public void addLine(String p_175202_1_)
    {
        labels.add(I18n.format(p_175202_1_));
    }

    /**
     * Sets the Label to be centered
     */
    public GuiLabel setCentered()
    {
        centered = true;
        return this;
    }

    public void drawLabel(Minecraft mc, int mouseX, int mouseY)
    {
        if (visible)
        {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            drawLabelBackground(mc, mouseX, mouseY);
            int i = y + height / 2 + border / 2;
            int j = i - labels.size() * 10 / 2;

            for (int k = 0; k < labels.size(); ++k)
            {
                if (centered)
                {
                    drawCenteredString(font, labels.get(k), x + width / 2, j + k * 10, textColor);
                }
                else
                {
                    drawString(font, labels.get(k), x, j + k * 10, textColor);
                }
            }
        }
    }

    protected void drawLabelBackground(Minecraft mcIn, int p_146160_2_, int p_146160_3_)
    {
        if (labelBgEnabled)
        {
            int i = width + border * 2;
            int j = height + border * 2;
            int k = x - border;
            int l = y - border;
            Gui.drawRect(k, l, k + i, l + j, backColor);
            drawHorizontalLine(k, k + i, l, ulColor);
            drawHorizontalLine(k, k + i, l + j, brColor);
            drawVerticalLine(k, l, l + j, ulColor);
            drawVerticalLine(k + i, l, l + j, brColor);
        }
    }
}
