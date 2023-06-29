package org.spray.heaven.via.widgets;

import org.spray.heaven.via.ViaMCP;
import org.spray.heaven.via.utils.ProtocolSorter;
import org.spray.heaven.via.utils.ProtocolUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

public class ViaSliderWidget extends GuiButton {

	private float sliderValue;
	public boolean dragging;
	private final float min;
	private final float max;

	private float minValue;
	private float maxValue;
	private float stepValue;

	public ViaSliderWidget(int buttonId, int x, int y) {
		super(buttonId, x, y, 120, 20, "");
		this.sliderValue = 1.0F;
		this.min = 0f;
		this.max = 1f;
		this.minValue = 0;
		this.maxValue = ProtocolSorter.getProtocolVersions().size() - 1;
		stepValue = 1;
		this.sliderValue = (float) ViaMCP.getInstance().stateValue;
		this.displayString = "Version - " + ViaMCP.getInstance().CURRENT_VERSION;
	}

	/**
	 * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this
	 * button and 2 if it IS hovering over this button.
	 */
	protected int getHoverState(boolean mouseOver) {
		return 0;
	}

	/**
	 * Fired when the mouse button is dragged. Equivalent of
	 * MouseListener.mouseDragged(MouseEvent e).
	 */
	protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
		if (this.visible) {
			if (this.dragging) {
				this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
				this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0F, 1.0F);

				float f = denormalizeValue(this.sliderValue);
				ViaMCP.getInstance().CURRENT_VERSION = ProtocolUtils
						.getProtocolName(ProtocolSorter.getProtocolVersions().get((int) f).getVersion());
				ViaMCP.getInstance().setVersion(ProtocolSorter.getProtocolVersions().get((int) f).getVersion());

				this.sliderValue = normalizeValue(f);
				ViaMCP.getInstance().stateValue = this.sliderValue;
				this.displayString = "Version - " + ViaMCP.getInstance().CURRENT_VERSION;
			}

			mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)),
					this.yPosition, 0, 66, 4, 20);
			this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)) + 4,
					this.yPosition, 196, 66, 4, 20);
		}
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of
	 * MouseListener.mousePressed(MouseEvent e).
	 */
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		if (super.mousePressed(mc, mouseX, mouseY)) {
			this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
			this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0F, 1.0F);
			ViaMCP.getInstance().CURRENT_VERSION = ProtocolUtils.getProtocolName(
					ProtocolSorter.getProtocolVersions().get((int) denormalizeValue(sliderValue)).getVersion());
			this.dragging = true;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Fired when the mouse button is released. Equivalent of
	 * MouseListener.mouseReleased(MouseEvent e).
	 */
	public void mouseReleased(int mouseX, int mouseY) {
		this.dragging = false;
	}

	public float normalizeValue(float value) {
		return MathHelper.clamp((this.snapToStepClamp(value) - this.minValue) / (this.maxValue - this.minValue), 0.0F,
				1.0F);
	}

	public float denormalizeValue(float value) {
		return this
				.snapToStepClamp(this.minValue + (this.maxValue - this.minValue) * MathHelper.clamp(value, 0.0F, 1.0F));
	}

	public float snapToStepClamp(float value) {
		value = this.snapToStep(value);
		return MathHelper.clamp(value, this.minValue, this.maxValue);
	}

	private float snapToStep(float value) {
		if (this.stepValue > 0.0F) {
			value = this.stepValue * (float) Math.round(value / this.stepValue);
		}

		return value;
	}
}
