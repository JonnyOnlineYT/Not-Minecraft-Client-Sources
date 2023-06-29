package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiSlider extends ButtonWidget
{
    private float sliderPosition = 1.0F;
    public boolean isMouseDown;
    private final String name;
    private final float min;
    private final float max;
    private final GuiPageButtonList.GuiResponder responder;
    private GuiSlider.FormatHelper formatHelper;

    public GuiSlider(GuiPageButtonList.GuiResponder guiResponder, int idIn, int x, int y, String name, float min, float max, float defaultValue, GuiSlider.FormatHelper formatter)
    {
        super(idIn, x, y, 150, 20, "");
        this.name = name;
        this.min = min;
        this.max = max;
        sliderPosition = (defaultValue - min) / (max - min);
        formatHelper = formatter;
        responder = guiResponder;
        text = getDisplayString();
    }

    public float getSliderValue()
    {
        return min + (max - min) * sliderPosition;
    }

    public void setSliderValue(float p_175218_1_, boolean p_175218_2_)
    {
        sliderPosition = (p_175218_1_ - min) / (max - min);
        text = getDisplayString();

        if (p_175218_2_)
        {
            responder.setEntryValue(id, getSliderValue());
        }
    }

    public float getSliderPosition()
    {
        return sliderPosition;
    }

    private String getDisplayString()
    {
        return formatHelper == null ? I18n.format(name) + ": " + getSliderValue() : formatHelper.getText(id, I18n.format(name), getSliderValue());
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver)
    {
        return 0;
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
        if (visible)
        {
            if (isMouseDown)
            {
                sliderPosition = (float)(mouseX - (x + 4)) / (float)(width - 8);

                if (sliderPosition < 0.0F)
                {
                    sliderPosition = 0.0F;
                }

                if (sliderPosition > 1.0F)
                {
                    sliderPosition = 1.0F;
                }

                text = getDisplayString();
                responder.setEntryValue(id, getSliderValue());
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            drawTexturedModalRect(x + (int)(sliderPosition * (float)(width - 8)), y, 0, 66, 4, 20);
            drawTexturedModalRect(x + (int)(sliderPosition * (float)(width - 8)) + 4, y, 196, 66, 4, 20);
        }
    }

    public void setSliderPosition(float p_175219_1_)
    {
        sliderPosition = p_175219_1_;
        text = getDisplayString();
        responder.setEntryValue(id, getSliderValue());
    }

    public FormatHelper getFormatHelper() {
        return formatHelper;
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if (super.mousePressed(mc, mouseX, mouseY))
        {
            sliderPosition = (float)(mouseX - (x + 4)) / (float)(width - 8);

            if (sliderPosition < 0.0F)
            {
                sliderPosition = 0.0F;
            }

            if (sliderPosition > 1.0F)
            {
                sliderPosition = 1.0F;
            }

            text = getDisplayString();
            responder.setEntryValue(id, getSliderValue());
            isMouseDown = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY)
    {
        isMouseDown = false;
    }

    public interface FormatHelper
    {
        String getText(int id, String name, float value);
    }
}
