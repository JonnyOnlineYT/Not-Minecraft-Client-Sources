package org.spray.infinity.ui.menu.components.elements.slider;

import org.spray.infinity.features.Setting;
import org.spray.infinity.ui.menu.components.elements.SliderElement;
import org.spray.infinity.utils.MathAssist;
import org.spray.infinity.utils.render.RenderUtil;

import net.minecraft.util.math.MathHelper;

public class DoubleSlider extends SliderElement {

	public DoubleSlider(Setting setting) {
		super(setting);
	}

	@Override
	public void init() {
		valueField.setText(getRenderValue());
	}

	@Override
	public void tick() {
		super.tick();

		double currentPos = (setting.getCurrentValueDouble() - setting.getMinValueDouble())
				/ (setting.getMaxValueDouble() - setting.getMinValueDouble());

		animation = RenderUtil.animate(animation, currentPos);

		if (!valueField.getText().isEmpty()) {
			if (Double.parseDouble(valueField.getText()) == setting.getCurrentValueDouble())
				return;
			try {
				setting.setCurrentValueDouble(Double.parseDouble(valueField.getText()));
			} catch (NumberFormatException e) {
			}
		}
	}

	@Override
	public String getRenderValue() {
		double value = MathAssist.round(setting.getCurrentValueDouble(), 2);
		return String.valueOf(value);
	}

	@Override
	public void setValue(int mouseX, double x, double width) {
		double diff = setting.getMaxValueDouble() - setting.getMinValueDouble();
		double percentBar = MathHelper.clamp((mouseX - x) / width, 0.0, 1.0);
		double val = setting.getMinValueDouble() + percentBar * diff;

		this.setting.setCurrentValueDouble(val);
		valueField.setText(String.valueOf(getRenderValue()));
	}

	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		super.keyPressed(keyCode, scanCode, modifiers);
	}
}
