package org.spray.infinity.ui.menu.components.elements.slider;

import org.spray.infinity.features.Setting;
import org.spray.infinity.ui.menu.components.elements.SliderElement;
import org.spray.infinity.utils.MathAssist;
import org.spray.infinity.utils.StringUtil;
import org.spray.infinity.utils.render.RenderUtil;

import net.minecraft.util.math.MathHelper;

public class FloatSlider extends SliderElement {

	public FloatSlider(Setting setting) {
		super(setting);
	}

	@Override
	public void init() {
		valueField.setText(getRenderValue());
	}

	@Override
	public void tick() {
		super.tick();

		float currentPos = (setting.getCurrentValueFloat() - setting.getMinValueFloat())
				/ (setting.getMaxValueFloat() - setting.getMinValueFloat());

		animation = RenderUtil.animate(animation, currentPos);

		if (!valueField.getText().isEmpty()) {
			if (Float.parseFloat(valueField.getText()) == setting.getCurrentValueFloat())
				return;
			try {
				setting.setCurrentValueFloat(Float.parseFloat(valueField.getText()));

			} catch (NumberFormatException e) {
			}
		}
	}

	@Override
	public String getRenderValue() {
		float value = (float) MathAssist.round(setting.getCurrentValueFloat(), 2);
		return StringUtil.DF(value, 1);
	}

	@Override
	public void setValue(int mouseX, double x, double width) {
		float diff = setting.getMaxValueFloat() - setting.getMinValueFloat();
		float percentBar = (float) MathHelper.clamp((mouseX - x) / width, 0.0, 1.0);
		float val = (float) (setting.getMinValueFloat() + percentBar * diff);

		this.setting.setCurrentValueFloat(val);
		valueField.setText(this.getRenderValue());
	}

	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		super.keyPressed(keyCode, scanCode, modifiers);
	}
}
