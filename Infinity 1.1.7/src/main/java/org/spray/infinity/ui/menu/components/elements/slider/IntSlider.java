package org.spray.infinity.ui.menu.components.elements.slider;

import org.spray.infinity.features.Setting;
import org.spray.infinity.ui.menu.components.elements.SliderElement;
import org.spray.infinity.utils.StringUtil;
import org.spray.infinity.utils.render.RenderUtil;

import net.minecraft.util.math.MathHelper;

public class IntSlider extends SliderElement {

	public IntSlider(Setting setting) {
		super(setting);
	}

	@Override
	public void init() {
		valueField.setText(getRenderValue());
	}

	@Override
	public void tick() {
		super.tick();

		double currentPos = (setting.getCurrentValueInt() - setting.getMinValueInt())
				/ (setting.getMaxValueInt() - setting.getMinValueInt());

		animation = RenderUtil.animate(animation, currentPos);
		stringAnimation = stringAnimation
				+ (Math.round(setting.getCurrentValueInt() * 100) / 100 - stringAnimation) / 2;

		if (!valueField.getText().isEmpty()) {
			if (Integer.parseInt(valueField.getText()) == setting.getCurrentValueInt())
				return;
			try {
				setting.setCurrentValueInt(Integer.parseInt(valueField.getText()));

			} catch (NumberFormatException e) {
			}
		}
	}

	@Override
	public String getRenderValue() {
		int value = (int) (Math.round(stringAnimation * 1000) / 1000);
		return StringUtil.DF(value, 0);
	}

	@Override
	public void setValue(int mouseX, double x, double width) {
		int diff = setting.getMaxValueInt() - setting.getMinValueInt();
		double percentBar = MathHelper.clamp((mouseX - x) / width, 0.0, 1.0);
		int val = (int) (setting.getMinValueInt() + percentBar * diff);

		this.setting.setCurrentValueInt(val);
		valueField.setText(this.getRenderValue());
	}

	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		super.keyPressed(keyCode, scanCode, modifiers);
	}
}
