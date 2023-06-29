package ru.wendoxd.ui.menu.elements.tabelements;

import net.minecraft.util.math.MathHelper;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import ru.wendoxd.ui.menu.utils.Bound2D;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.visual.RenderUtils;
import ru.wendoxd.utils.visual.animation.Animation;
import ru.wendoxd.utils.visual.animation.DynamicAnimation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.function.Supplier;

public class Slider extends PrimaryButton {

	private final DynamicAnimation deltaAnimation;
	private final Animation animation;
	private final Animation animationOutline;
	private final Animation animationPressed;
	private final Animation input;
	private final Bound2D slider;
	private final Bound2D bound;
	private final Bound2D inputBound;
	private int digits, inputDigits;
	private final int width;
	private StringBuilder valueBuilder;
	public double outValue, value, minValue, maxValue;
	private boolean dragging, inputValue;

	public Slider(String name, int digits, double minValue, double maxValue, double defaultValue) {
		this(name, digits, minValue, maxValue, defaultValue, null);
	}

	public Slider(String name, int digits, double minValue, double maxValue, double defaultValue,
			Supplier<Boolean> condition) {
		super(name, condition);
		this.valueBuilder = new StringBuilder();
		this.animation = new Animation();
		this.deltaAnimation = new DynamicAnimation();
		this.animationOutline = new Animation();
		this.animationPressed = new Animation();
		this.input = new Animation();
		this.bound = new Bound2D(0, 0, 0, 0);
		this.slider = new Bound2D(0, 0, 0, 0);
		this.inputBound = new Bound2D(0, 0, 0, 0);
		this.digits = digits;
		this.minValue = minValue;
		this.maxValue = maxValue;
		if (defaultValue != minValue) {
			this.value = defaultValue;
		}
		StringBuilder sb = new StringBuilder();
		if (minValue < 0) {
			sb.append("-");
		}
		sb.append((int) Math.max(minValue, maxValue));
		if (digits > 0) {
			sb.append(".");
		}
		for (int i = 0; i < digits; i++) {
			sb.append(i);
		}
		this.width = Fonts.SEMI_BOLD_12.getStringWidth(sb.toString());
		this.inputDigits = sb.toString().length() - 1 + (digits > 0 ? -1 : 0);
		this.updateValue();
	}

	@Override
	public float draw(float x, float yOffset) {
		RenderUtils.drawRect(x, yOffset, x + 110, (float) (yOffset + 16 * animation.get()),
				RenderUtils.rgba(23, 23, 23, 255));
		this.inputBound.update(x + 102 - width, yOffset + 5, x + 105.5f, (float) (yOffset + 13 * animation.get()));
		RenderUtils.drawRect(x + 102 - width, yOffset + 5, x + 105.5f, (float) (yOffset + 13 * animation.get()),
				RenderUtils.rgba(27 + (this.input.get() * 20), 27 + (this.input.get() * 20),
						27 + (this.input.get() * 20), (255 * animation.get())));
		if (animation.get() > 0) {
			if (dragging) {
				int mx = (int) (MenuAPI.mouseX - MenuAPI.menuWindow.getX() - x - 61 + width);
				if (!Mouse.isButtonDown(0)) {
					this.dragging = false;
				}
				this.value = mx / 36f;
				updateValue();
			}
			if (!inputValue) {
				Fonts.SEMI_BOLD_12.drawCenteredString(
						String.format("%." + digits + "f", this.deltaAnimation.getValue() * this.animation.get()),
						x + 103.5f - width / 2F, yOffset + 8f * (float) animation.get(),
						RenderUtils.rgba(150, 150, 150, (255 * animation.get())));
			} else {
				if (this.valueBuilder.length() == 0) {
					Fonts.SEMI_BOLD_12.drawCenteredString("...", x + 103.5f - width / 2F,
							yOffset + 8f * (float) animation.get(),
							RenderUtils.rgba(150, 150, 150, (255 * animation.get())));
				} else {
					Fonts.SEMI_BOLD_12.drawCenteredString(this.valueBuilder.toString(), x + 103.5f - width / 2F,
							yOffset + 8f * (float) animation.get(),
							RenderUtils.rgba(150, 150, 150, (255 * animation.get())));
				}
			}
			Fonts.MNTSB_13.drawString(name, x + 7, yOffset + 7.5f * (float) animation.get(),
					RenderUtils.rgba(170 + (45 * animationOutline.get()), 170 + (45 * animationOutline.get()),
							170 + (45 * animationOutline.get()), (255 * animation.get())));
			MenuAPI.drawSlider(x + 50.5f - width, yOffset + 6f * (float) animation.get(),
					RenderUtils.rgba(130 + (animationOutline.get() * 30) + (animationPressed.get() * 25),
							130 + (animationOutline.get() * 30) + (animationPressed.get() * 25),
							130 + (animationOutline.get() * 30) + (animationPressed.get() * 25),
							(255 * animation.get())),
					RenderUtils.rgba(90 + (animationOutline.get() * 30), 90 + (animationOutline.get() * 30),
							90 + (animationOutline.get() * 30), (255 * animation.get())),
					(float) (this.value * animation.get()));
			this.slider.update(x + 59 - width, yOffset + 2f * (float) animation.get(), x + 97 - width,
					yOffset + 15f * (float) animation.get());
			this.bound.update(x, yOffset, x + 110, (float) (yOffset + 16 * animation.get()));
		}
		return 16 * (float) this.animation.get();
	}

	@Override
	public boolean mouseClicked(int x, int y, int mb) {
		boolean prev = this.inputValue;
		this.inputValue = this.inputBound.inBound();
		if (prev != this.inputValue) {
			this.valueBuilder = new StringBuilder();
		}
		boolean inBound = this.slider.inBound();
		if (inBound && mb == 0) {
			dragging = true;
		}
		return inBound;
	}

	public void updateValue() {
		this.value = MathHelper.clamp(value, 0, 1);
		this.deltaAnimation.setValue(this.minValue + (this.maxValue - this.minValue) * value);
		this.outValue = this.minValue + (this.maxValue - this.minValue) * value;
	}

	public int getIntValue() {
		return isVisible() ? (int) this.outValue : -1;
	}

	public float getFloatValue() {
		return isVisible() ? (float) this.outValue : -1;
	}

	public double getDoubleValue() {
		return isVisible() ? this.outValue : -1;
	}

	@Override
	public void animation() {
		this.input.update(this.inputBound.inBound());
		this.deltaAnimation.update();
		this.animation.update(this.isVisible());
		if (this.isVisible()) {
			if (this.slider.inBound() && !Mouse.isButtonDown(0)) {
				int whell = Mouse.getDWheel();
				if (whell != 0) {
					this.value = value + (whell > 0 ? 0.01 : -0.01);
					this.updateValue();
				}
			}
			this.animationPressed.update(this.dragging);
			this.animationOutline.update(this.bound.inBound() && MenuAPI.contextTab.isAnimationsAllowed());
		}
	}

	@Override
	public void set(float animation) {
		this.animation.set(animation);
	}

	@Override
	public void load(DataInputStream dis) throws Exception {
		this.value = dis.readDouble();
		this.updateValue();
		this.reset();
	}

	@Override
	public void save(DataOutputStream dos) throws Exception {
		dos.writeUTF(this.getPath());
		dos.writeDouble(this.value);
	}

	public void build() {
		try {
			double newValue = MathHelper.clamp(Double.parseDouble(this.valueBuilder.toString()), this.minValue,
					this.maxValue) - minValue;
			double max = this.maxValue - this.minValue;
			this.value = newValue / max;
			this.updateValue();
		} catch (Exception e) {
		}
	}

	@Override
	public void keyTyped(char c, int key) {
		if (inputValue) {
			if (key == Keyboard.KEY_RETURN) {
				this.inputValue = false;
				this.build();
				return;
			}
			boolean withDot = this.valueBuilder.toString().contains(".");
			int length = this.valueBuilder.length() + (withDot ? -1 : 0);
			if ((c >= '0' && c <= '9') && length <= this.inputDigits) {
				this.valueBuilder.append(c);
			}
			if ((c == '.' || c == ',' || c == '/' || c == 'ю') && !withDot) {
				this.valueBuilder.append(".");
			}
			if (this.minValue < 0 && c == '-') {
				this.valueBuilder.append(c);
			}
			if (key == Keyboard.KEY_BACK) {
				if (this.valueBuilder.length() > 0) {
					String newSb = this.valueBuilder.substring(0, Math.max(0, this.valueBuilder.length() - 1));
					this.valueBuilder = new StringBuilder();
					this.valueBuilder.append(newSb);
				}
			}
		}
	}

	@Override
	public void reset() {
		this.animationOutline.reset();
		this.animationPressed.reset();
		this.animation.reset();
	}

	@Override
	public double boundAnimation() {
		return this.animationOutline.get() * animation.get();
	}
}
