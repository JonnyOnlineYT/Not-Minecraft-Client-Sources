package volcano.summer.screen.clickgui.original.settings;

import java.awt.Color;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import volcano.summer.base.Summer;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.screen.clickgui.original.R3DUtil;
import volcano.summer.screen.clickgui.original.parts.Component;

public class SliderPart extends Component {

	private ClampedValue value;
	private ModulesPart parent;
	private int offset;
	private int x;
	private int y;

	SliderPart(ClampedValue value, ModulesPart modulesPart, int offset) {
		this.value = value;
		this.parent = modulesPart;
		this.x = modulesPart.parent.getX() + modulesPart.parent.getWidth();
		this.y = modulesPart.parent.getY() + modulesPart.offset;
		this.offset = offset;
	}

	@Override
	public void render() {

		Gui.drawRect(this.parent.parent.getX() + 1, this.parent.parent.getY() + this.offset - 2,
				this.parent.parent.getX() + this.parent.parent.getWidth() - 2,
				this.parent.parent.getY() + this.offset + 10, 0xbb141414);

		double percentBar = (((Double) value.getValue()).floatValue() - ((Double) value.getMin()).floatValue())
				/ (((Double) value.getMax()).floatValue() - ((Double) value.getMin()).floatValue());
		double width = this.parent.parent.getWidth() - 5;
		Gui.drawRect(x + 4, y + 1, (int) (x + width), y + 2, Color.GRAY.getRGB());
		// Gui.drawRect(x + 4, y + 1, x + (percentBar * width), y + 2,
		// ColorUtils.getColor());
		R3DUtil.INSTANCE.drawFilledCircle(x + 2.3 + (percentBar * width), y + 1.5, 1.7, Summer.getColor().getRGB());
		R3DUtil.INSTANCE.drawCircle(x + 2.3 + (percentBar * width), y + 1.5, 1.7, Summer.getColor().getRGB());

		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		GL11.glColor3f(255, 255, 255);
		Summer.mc.fontRendererObj.func_175063_a(
				this.value.getName() + " : " + R3DUtil.INSTANCE.round(((Double) this.value.getValue()).floatValue(), 2),
				(this.parent.parent.getX() * 2 + 10), ((this.parent.parent.getY() + this.offset - 1) * 2 + 12),
				0xFFFFFFFF);
		GL11.glPopMatrix();
	}

	@Override
	public void setOff(int newOff) {
		this.offset = newOff;
	}

	@Override
	public void updateComponent(int mouseX, int mouseY) {
		boolean hovered = (this.isMouseOnButtonD(mouseX, mouseY));
		this.y = this.parent.parent.getY() + this.offset;
		this.x = this.parent.parent.getX();
		if (hovered && this.parent.open && Mouse.isButtonDown(0)) {

			/*
			 * double diff = mouseX - this.parent.parent.getX() - 4; double
			 * value = MathHelper.clamp_float((float) diff, (float)
			 * this.value.getMin(), (float) this.value.getMax());
			 * this.value.setValDouble(value);
			 */

			double diff = Math.min(68, Math.max(0, mouseX - this.x - 4));

			double min = ((Double) value.getMin()).floatValue();
			double max = ((Double) value.getMax()).floatValue();

			if (diff == 0) {
				value.setValue(min);
			} else {
				double newValue = R3DUtil.INSTANCE.round(((diff / 68) * (max - min) + min), 1); // 2
				value.setValue(newValue);
			}

		}
	}

	private boolean isMouseOnButtonD(int x, int y) {
		return x > this.x + 3 && x < this.x + (this.parent.parent.getWidth()) && y > this.y - 3 && y < this.y + 6;
	}
}
