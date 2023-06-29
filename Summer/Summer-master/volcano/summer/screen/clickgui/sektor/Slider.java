package volcano.summer.screen.clickgui.sektor;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.lwjgl.opengl.GL11;

public class Slider extends Component {
	private String title;
	private double min;
	private double max;
	private double currentValue;
	private boolean isSliding = false;
	private double changeAmount;
	private double sliderValue;

	public Slider(String title, double value, double min, double max, Component parent) {
		this.parent = parent;
		this.title = title;
		this.min = min;
		this.max = max;
		this.currentValue = value;
		this.width = parent.width - 4;
		this.height = 14;
		this.renderWidth = this.width;
		this.renderHeight = this.height;
		this.changeAmount = this.width / (this.max - this.min);
		this.sliderValue = (this.currentValue - this.min) * this.changeAmount;
		this.sliderValue = Math.max(this.sliderValue, 0.0);
		this.onUpdate(this.currentValue);
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks, int parX, int parY) {
		this.onUpdate(this.currentValue);
		GL11.glPushMatrix();
		GL11.glTranslatef(this.parent.x, this.parent.y, 0.0f);
		this.absX = parX + this.x;
		this.absY = parY + this.y;
		int textColor = -657931;
		int outlineColor = -7405568;
		if (this.isSliding) {
			this.sliderValue = mouseX - this.absX + this.x;
			this.sliderValue = Math.min(this.sliderValue, this.width);
			this.sliderValue = Math.max(this.sliderValue, 0.0);
			this.currentValue = Slider.roundToPlace((mouseX - this.absX) / this.changeAmount + this.min, 2);
		} else {
			this.changeAmount = this.width / (this.max - this.min);
			this.sliderValue = (this.currentValue - this.min) * this.changeAmount;
			this.sliderValue = Math.max(this.sliderValue, 0.0);
		}
		this.currentValue = Math.max(this.currentValue, this.min);
		this.currentValue = Math.min(this.currentValue, this.max);
		Render.rectangleGradient(this.x, this.y + this.height - 2, this.width, this.y + this.height,
				new int[] { -14606047, -15066598 });
		Render.rectangleGradient(this.x, this.y + this.height - 2, this.sliderValue, this.y + this.height,
				new int[] { -13958913, -13958913 });
		String displayString = String.valueOf(this.currentValue);
		Slider.mc.fontRendererObj.func_175063_a(this.title, this.x + 3,
				this.y + this.height / 2 - Slider.mc.fontRendererObj.FONT_HEIGHT / 2, textColor);
		Slider.mc.fontRendererObj.func_175063_a(displayString,
				this.x + this.width - 3 - Slider.mc.fontRendererObj.getStringWidth(displayString),
				this.y + this.height / 2 - Slider.mc.fontRendererObj.FONT_HEIGHT / 2, textColor);
		GL11.glPopMatrix();
	}

	public static double roundToPlace(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (!this.isVisible) {
			return;
		}
		if (mouseX >= this.absX && mouseX <= this.absX + this.width && mouseY >= this.absY
				&& mouseY <= this.absY + this.height && mouseButton == 0) {
			this.isSliding = true;
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		if (!this.isVisible) {
			return;
		}
		if (this.isSliding) {
			this.isSliding = false;
		}
	}

	@Override
	public void keyPressed(int key) {
	}

	public void setValue(double val) {
		this.currentValue = val;
	}

	public void onUpdate(double currentValue) {
	}
}