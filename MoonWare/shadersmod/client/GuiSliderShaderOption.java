package shadersmod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

public class GuiSliderShaderOption extends GuiButtonShaderOption
{
    private float sliderValue = 1.0F;
    public boolean dragging;
    private ShaderOption shaderOption;

    public GuiSliderShaderOption(int buttonId, int x, int y, int w, int h, ShaderOption shaderOption, String text)
    {
        super(buttonId, x, y, w, h, shaderOption, text);
        this.shaderOption = shaderOption;
        sliderValue = shaderOption.getIndexNormalized();
        this.text = GuiShaderOptions.getButtonText(shaderOption, width);
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
                shaderOption.setIndexNormalized(sliderValue);
                sliderValue = shaderOption.getIndexNormalized();
                text = GuiShaderOptions.getButtonText(shaderOption, width);
            }

            Minecraft.getTextureManager().bindTexture(ButtonWidget.BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            drawTexturedModalRect(x + (int)(sliderValue * (float)(width - 8)), y, 0, 66, 4, 20);
            drawTexturedModalRect(x + (int)(sliderValue * (float)(width - 8)) + 4, y, 196, 66, 4, 20);
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
            shaderOption.setIndexNormalized(sliderValue);
            text = GuiShaderOptions.getButtonText(shaderOption, width);
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

    public void valueChanged()
    {
        sliderValue = shaderOption.getIndexNormalized();
    }
}
