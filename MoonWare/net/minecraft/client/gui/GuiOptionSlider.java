package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.math.MathHelper;

public class GuiOptionSlider extends ButtonWidget
{
    private float sliderValue;
    public boolean dragging;
    private final GameSettings.Options options;
    private final float minValue;
    private final float maxValue;

    public GuiOptionSlider(int buttonId, int x, int y, GameSettings.Options optionIn)
    {
        this(buttonId, x, y, optionIn, 0.0F, 1.0F);
    }

    public GuiOptionSlider(int buttonId, int x, int y, GameSettings.Options optionIn, float minValueIn, float maxValue)
    {
        super(buttonId, x, y, 150, 20, "");
        sliderValue = 1.0F;
        options = optionIn;
        minValue = minValueIn;
        this.maxValue = maxValue;
        Minecraft minecraft = Minecraft.getMinecraft();
        sliderValue = optionIn.normalizeValue(Minecraft.gameSettings.getOptionFloatValue(optionIn));
        text = Minecraft.gameSettings.getKeyBinding(optionIn);
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
            if (dragging)
            {
                sliderValue = (float)(mouseX - (x + 4)) / (float)(width - 8);
                sliderValue = MathHelper.clamp(sliderValue, 0.0F, 1.0F);
                float f = options.denormalizeValue(sliderValue);
                Minecraft.gameSettings.setOptionFloatValue(options, f);
                sliderValue = options.normalizeValue(f);
                text = Minecraft.gameSettings.getKeyBinding(options);
            }

            Minecraft.getTextureManager().bindTexture(ButtonWidget.BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawRect(x + sliderValue * (width - 8F), y, x + sliderValue * (width - 8F) + 4, y + height, -1);
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if (super.mousePressed(mc, mouseX, mouseY))
        {
            sliderValue = (float)(mouseX - (x + 4)) / (float)(width - 8);
            sliderValue = MathHelper.clamp(sliderValue, 0.0F, 1.0F);
            Minecraft.gameSettings.setOptionFloatValue(options, options.denormalizeValue(sliderValue));
            text = Minecraft.gameSettings.getKeyBinding(options);
            dragging = true;
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
        dragging = false;
    }
}
