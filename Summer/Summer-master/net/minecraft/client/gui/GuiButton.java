package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import volcano.summer.client.util.R2DUtils;

public class GuiButton extends Gui
{
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");

    /** Button width in pixels */
    protected int width;

    /** Button height in pixels */
    protected int height;

    /** The x position of this control. */
    public int xPosition;

    /** The y position of this control. */
    public int yPosition;

    /** The string displayed on this control. */
    public String displayString;
    public int id;

    /** True if this control is enabled, false to disable. */
    public boolean enabled;

    /** Hides the button completely if false. */
    public boolean visible;
    protected boolean hovered;
    private static final String __OBFID = "CL_00000668";

    public GuiButton(int buttonId, int x, int y, String buttonText)
    {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
    {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver)
    {
        byte var2 = 1;

        if (!this.enabled)
        {
            var2 = 0;
        }
        else if (mouseOver)
        {
            var2 = 2;
        }

        return var2;
    }

    /**
     * Draws this button to the screen.
     */
    int trans = 4;
    int transide = 0;
    int transtop = 0;
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int var5 = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            
            R2DUtils.drawBorderedRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 1, this.isMouseOver() ? 0x05666666 : 0x15888888, this.isMouseOver() ? 0x50ffffff : 0xccffffff);
            //this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + var5 * 20, this.width / 2, this.height);
            //this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + var5 * 20, this.width / 2, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            
    		Gui.drawRect(xPosition - (hovered ? 2 : 0), yPosition - (hovered ? 2 : 0),
      				xPosition + width + (hovered ? 2 : 0), yPosition + height + (hovered ? 2 : 0), 0x90000000);
    		Gui.drawRect(xPosition + width / 2 - trans - 2, yPosition + height + (hovered ? 2 : 0),
					xPosition + width / 2 + trans + 2, yPosition + height + (hovered ? 1 : -1),
					0xffFFFF00);
    		if (hovered && transide >= height + 1) {
				Gui.drawRect(xPosition - 2, yPosition - 2, xPosition + transtop, yPosition - 1,
						0xffFFFF00);
				Gui.drawRect(xPosition + width + 2, yPosition - 2, xPosition + width - transtop, yPosition - 1,
						0xffFFFF00);

			}
			if (hovered && trans >= width / 2) {
				R2DUtils.drawVLine(xPosition - 2, yPosition + height + 2, yPosition + height - transide - 1,
						0xffFFFF00);

				R2DUtils.drawVLine(xPosition + width + 1, yPosition + height + 2, yPosition + height - transide - 1,
						0xffFFFF00);

			}            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
			if (this.hovered && !(trans >= width / 2)) {
				trans += 10;
			}
			if (this.hovered && trans > width / 2) {
				trans = width / 2;
			}
			if (!this.hovered){
                trans = 4;
                transtop = 0;
                transide = 0;
            }
			if (this.hovered && trans >= width / 2 && !(transide >= height + 1)) {
				transide += 10;
			}
			if (this.hovered && transide >= height + 1) {
				transide = height + 1;
			}
			if (this.hovered && trans >= width / 2 && transide >= height + 1 && !(transtop >= this.width / 2)) {
				transtop += 5;
			}
			mc.fontRendererObj.drawTotalCenteredStringWithShadow(this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 7) / 2, 0xeeffffff);

        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {}

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY) {}

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    public void setWidth(int width1){
        width = width1;
    }

    public void setyPosition(int y){
        yPosition = y;
    }

    public void setxPosition(int x){
        xPosition = x;
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    public boolean isMouseOver()
    {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {}

    public void playPressSound(SoundHandler soundHandlerIn)
    {
        soundHandlerIn.playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0F));
    }
    public void setDisplayString(String display){
        this.displayString = display;
    }

    public int getButtonWidth()
    {
        return this.width;
    }

    public void func_175211_a(int p_175211_1_)
    {
        this.width = p_175211_1_;
    }
}
