package volcano.summer.screen.clickgui.exeter;

import volcano.summer.client.util.CustomFont;
import volcano.summer.client.util.R2DUtils;
import volcano.summer.client.value.ClampedValue;

public class NumberSlider extends Item {
	private ClampedValue numberProperty;

	public NumberSlider(ClampedValue numberProperty) {
		super((String) numberProperty.getValue());
		this.numberProperty = numberProperty;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		R2DUtils.drawRect(this.x, this.y, this.x + getValueWidth(), this.y + this.height,
				!isHovering(mouseX, mouseY) ? 2002577475 : -1721964477);
		ClickGui.getClickGui().guiFont.drawString(
				String.format("%s§7 %s", new Object[] { getLabel(), this.numberProperty.getValue() }), this.x + 2.3F,
				this.y - 1.0F, CustomFont.FontType.SHADOW_THIN, -1);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if ((isHovering(mouseX, mouseY)) && (mouseButton == 0)) {
		}
	}

	@Override
	public int getHeight() {
		return 14;
	}

	private boolean isHovering(int mouseX, int mouseY) {
		for (Panel panel : ClickGui.getClickGui().getPanels()) {
			if (panel.drag) {
				return false;
			}
		}
		return (mouseX >= getX()) && (mouseX <= getX() + getWidth()) && (mouseY >= getY())
				&& (mouseY <= getY() + this.height);
	}

	private float getValueWidth() {
		return ((Number) this.numberProperty.getMax()).floatValue()
				- ((Number) this.numberProperty.getMin()).floatValue()
				+ ((Number) this.numberProperty.getValue()).floatValue();
	}
}