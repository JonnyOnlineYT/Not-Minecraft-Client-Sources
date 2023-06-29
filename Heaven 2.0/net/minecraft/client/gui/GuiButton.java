package net.minecraft.client.gui;

import java.awt.Color;

import org.spray.heaven.features.module.modules.render.ClickUIMod;
import org.spray.heaven.font.IFont;
import org.spray.heaven.ui.clickui.Colors;
import org.spray.heaven.util.render.ColorUtil;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.animation.Animation;
import org.spray.heaven.util.render.animation.Direction;
import org.spray.heaven.util.render.animation.impl.DecelerateAnimation;
import org.spray.heaven.util.render.shader.drawable.RoundedShader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class GuiButton extends Gui {
	protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");

	private final ResourceLocation image;

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
	private boolean rect = true;

	private Color rectColor = new Color(34, 34, 34);

	private boolean heavenMode = false;

	private final Animation animation = new DecelerateAnimation(240, 1f);

	private boolean background;

	public GuiButton(int buttonId, int x, int y, String buttonText) {
		this(buttonId, x, y, 200, 20, null, buttonText);
	}

	public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
		this(buttonId, x, y, widthIn, heightIn, null, buttonText);
	}

	public GuiButton(int buttonId, int x, int y, ResourceLocation image) {
		this(buttonId, x, y, 200, 20, image, "");
	}

	public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, ResourceLocation image) {
		this(buttonId, x, y, widthIn, heightIn, image, "");
	}

	public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, ResourceLocation image, String buttonText) {
		this.width = 200;
		this.height = 20;
		this.enabled = true;
		this.visible = true;
		this.id = buttonId;
		this.xPosition = x;
		this.yPosition = y;
		this.width = widthIn;
		this.height = heightIn;
		this.image = image;
		this.displayString = buttonText;
		background = true;
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

	public void func_191745_a(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float p_191745_4_) {
		if (this.visible) {
			this.hovered = p_191745_2_ >= this.xPosition && p_191745_3_ >= this.yPosition
					&& p_191745_2_ < this.xPosition + this.width && p_191745_3_ < this.yPosition + this.height;

			GlStateManager.pushMatrix();

			double centerX = xPosition + width / 2;
			double centerY = yPosition + height / 2;
			int j = 14737632;

			if (!this.enabled) {
				j = 10526880;
			} else if (this.hovered) {
				j = 16777120;
			}

			animation.setDirection(hovered ? Direction.FORWARDS : Direction.BACKWARDS);
			int plusAlpha = (int) (100f * animation.getOutput());
			
			GlStateManager.translate(centerX, centerY, 0);
			GlStateManager.scale(1f + (0.03 * animation.getOutput()), 1f + (0.03 * animation.getOutput()), 1);
			GlStateManager.translate(-centerX, -centerY, 0);

			Color color = new Color(rectColor.getRed(), rectColor.getGreen(), rectColor.getBlue(), 255);

			if (background) {
				if (heavenMode)
					RoundedShader.drawGradientHorizontal(xPosition, yPosition, width, height, 1,
							ColorUtil.applyOpacity(ClickUIMod.getColor(200), 0.9f),
							ColorUtil.applyOpacity(ClickUIMod.getColor(0), 0.9f));
				else
					RoundedShader.drawRound(xPosition, yPosition, width, height, 1, color);
			}

			this.mouseDragged(p_191745_1_, p_191745_2_, p_191745_3_);

			float size = 0.98f;

			if (image != null)
				Drawable.drawTexture(image, xPosition + 6, yPosition + 6, width - 12, height - 12, new Color(j));
			else
				IFont.WEB_SMALL.drawCenteredString(this.displayString, this.xPosition + this.width / 2,
						this.yPosition + this.height / 2 - (IFont.WEB_SMALL.getFontHeight(size) / 2), -1, size);

			GlStateManager.popMatrix();
		}
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

	public void setRectColor(Color color) {
		rectColor = color;
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of
	 * MouseListener.mousePressed(MouseEvent e).
	 */
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition
				&& mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
	}

	public GuiButton setBackground(boolean state) {
		this.background = state;
		return this;
	}

	public GuiButton disableRect() {
		this.rect = false;
		return this;
	}

	public GuiButton enableHeaven() {
		this.heavenMode = true;
		return this;
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

	public void setText(String text) {
		this.displayString = text;
	}
}
