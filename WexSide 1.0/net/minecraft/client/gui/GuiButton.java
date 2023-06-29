package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.lib.RectHelper;
import ru.wendoxd.utils.visual.RenderUtils;

public class GuiButton extends Gui {
	protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");

	/**
	 * Button width in pixels
	 */
	protected int width;

	/**
	 * Button height in pixels
	 */
	protected int height;

	/**
	 * The x position of this control.
	 */
	public int xPosition;

	/**
	 * The y position of this control.
	 */
	public int yPosition;

	/**
	 * The string displayed on this control.
	 */
	public String displayString;
	public int id;

	/**
	 * True if this control is enabled, false to disable.
	 */
	public boolean enabled;

	/**
	 * Hides the button completely if false.
	 */
	public boolean visible;
	protected boolean hovered;

	public GuiButton(int buttonId, int x, int y, String buttonText) {
		this(buttonId, x, y, 200, 20, buttonText);
	}

	public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
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
	 * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this
	 * button and 2 if it IS hovering over this button.
	 */
	protected int getHoverState(boolean mouseOver) {
		int i = 1;

		if (!this.enabled) {
			i = 0;
		} else if (mouseOver) {
			i = 2;
		}

		return i;
	}

	public void func_191745_a(Minecraft mc, int p_191745_2_, int p_191745_3_, float p_191745_4_) {
		mc.entityRenderer.setupOverlayRendering();
		if (this.visible) {
			this.hovered = p_191745_2_ >= this.xPosition && p_191745_3_ >= this.yPosition && p_191745_2_ < this.xPosition + this.width && p_191745_3_ < this.yPosition + this.height;
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,  GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

			RectHelper.renderShadow(this.xPosition, this.yPosition, width - 2, height - (mc.currentScreen instanceof GuiMainMenu ? 0 : 4), RenderUtils.rgba(26, 26, 26, 140), 3);
			this.mouseDragged(mc, p_191745_2_, p_191745_3_);
			int j = RenderUtils.rgba(205, 205, 205, 255);

			if (!this.enabled) {
				j = 10526880;
			} else if (this.hovered) {
				j = RenderUtils.rgba(255, 255, 255, 255);
			}

			if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu) {
				Fonts.MNTSB_14.drawCenteredString(this.displayString, this.xPosition + this.width / 2F,
						this.yPosition + (this.height) / 2F + 0.3f, j);
			} else {
				Fonts.MNTSB_14.drawCenteredString(this.displayString, this.xPosition + this.width / 2F,
						this.yPosition + (this.height - 4) / 2F, j);
			}
			GlStateManager.resetColor();
		}
		mc.entityRenderer.setupOverlayRendering(2);
	}

	/**
	 * Fired when the mouse button is dragged. Equivalent of
	 * MouseListener.mouseDragged(MouseEvent e).
	 */
	protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
	}

	/**
	 * Fired when the mouse button is released. Equivalent of
	 * MouseListener.mouseReleased(MouseEvent e).
	 */
	public void mouseReleased(int mouseX, int mouseY) {
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of
	 * MouseListener.mousePressed(MouseEvent e).
	 */
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition
				&& mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
	}

	/**
	 * Whether the mouse cursor is currently over the button.
	 */
	public boolean isMouseOver() {
		return this.hovered;
	}

	public void drawButtonForegroundLayer(int mouseX, int mouseY) {
	}

	public void playPressSound(SoundHandler soundHandlerIn) {
		soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}

	public int getButtonWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}
