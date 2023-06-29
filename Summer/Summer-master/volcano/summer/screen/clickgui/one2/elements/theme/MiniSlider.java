package volcano.summer.screen.clickgui.one2.elements.theme;

import org.lwjgl.input.Mouse;

import com.ibm.icu.text.DecimalFormat;

import volcano.summer.client.util.GuiUtils;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.screen.clickgui.one2.elements.Element;

public class MiniSlider extends MiniElement {
	private ClampedValue value;
	private boolean dragging;
	private int lastX;
	private int lastY;

	public MiniSlider(final int posX, final int posY, final ClampedValue value, final Element parent) {
		super(posX, posY, 92, 14, parent);
		this.value = value;
	}

	@Override
	public void draw(final int mouseX, final int mouseY) {
		this.updateSlider(mouseX, mouseY);
		final boolean isHover = mouseX >= this.getPosX() + this.dragX && mouseY >= this.getPosY() + this.dragY
				&& mouseX <= this.getPosX() + this.getWidth() + this.dragX
				&& mouseY <= this.getPosY() + this.getHeight() + this.dragY;
		GuiUtils.drawFineBorderedRect(this.getPosX() + this.dragX, this.getPosY() + this.dragY,
				this.getPosX() + this.getWidth() + this.dragX, this.getPosY() + this.getHeight() + this.dragY,
				isHover ? -1316805 : -12303292, -13882324);
		final int drag = (int) (((Double) this.value.getValue()).floatValue()
				/ ((Double) this.value.getMax()).floatValue() * this.getWidth());
		GuiUtils.drawFineBorderedRect(this.getPosX() + this.dragX, this.getPosY() + this.dragY,
				this.getPosX() + drag + this.dragX, this.getPosY() + this.getHeight() + this.dragY,
				isHover ? -1316805 : -12303292, -1316805);
		GuiUtils.drawCentredStringWithShadow(
				String.valueOf(this.value.getName()) + ": "
						+ ((Double) this.value.getValue() != null ? String.valueOf(this.value.getValue()).substring(0,
								String.valueOf(this.value.getValue()).length() - 2) : this.value.getValue()),
				this.getPosX() + this.getWidth() / 2 + this.dragX, this.getPosY() + 4 + this.dragY, -1);
	}

	private void updateSlider(int mouseX, int mouseY) {
		if (!Mouse.isButtonDown(0)) {
			setDrag(false);
		}
		if (this.dragging) {
			double limit1 = ((Double) this.value.getMin()).floatValue();
			double limit2 = ((Double) this.value.getMax()).floatValue();
			double inc = 0.1D;
			double valAbs = mouseX - (getPosX() + 1.0F);
			double perc = valAbs / (getWidth() - 2.0F);
			perc = Math.min(Math.max(0.0D, perc), 1.0D);
			double valRel = (limit2 - limit1) * perc;
			double val = limit1 + valRel;
			val = Math.round(val * (1.0D / inc)) / (1.0D / inc);
			this.value.setValue(Double.valueOf(val).doubleValue());
		}
	}

	@Override
	public void mouseClicked(final int mouseX, final int mouseY, final int button) {
		final boolean isHover = mouseX >= this.getPosX() + this.dragX && mouseY >= this.getPosY() + this.dragY
				&& mouseX <= this.getPosX() + this.getWidth() + this.dragX
				&& mouseY <= this.getPosY() + this.getHeight() + this.dragY;
		if (isHover && button == 0) {
			this.lastX = mouseX;
			this.lastY = mouseY;
			this.setDrag(true);
		}
	}

	private double round(final double d, final int r) {
		String round = "#";
		for (int i = 0; i < r; ++i) {
			round = String.valueOf(round) + ".#";
		}
		final DecimalFormat twoDForm = new DecimalFormat(round);
		return Double.valueOf(twoDForm.format(d));
	}

	public boolean isDragging() {
		return this.dragging;
	}

	public void setDrag(final boolean dragging) {
		this.dragging = dragging;
	}
}