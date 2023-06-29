package org.spray.heaven.ui.clickui.elements;

import java.awt.Color;

import org.spray.heaven.features.module.Setting;
import org.spray.heaven.features.module.modules.render.ClickUIMod;
import org.spray.heaven.font.IFont;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.ui.clickui.base.AbstractElement;
import org.spray.heaven.util.MathUtil;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.RenderUtil;
import org.spray.heaven.util.render.shader.drawable.RoundedShader;

import net.minecraft.util.math.MathHelper;

public class SliderElement extends AbstractElement {

	private float animation;
	private double stranimation;
	private boolean dragging;

	public SliderElement(Setting setting) {
		super(setting);
	}

	@Override
	public void tick() {
		double currentPos = (setting.getValue() - setting.getMinValue())
				/ (setting.getMaxValue() - setting.getMinValue());
		stranimation = stranimation + (setting.getValue() * 100 / 100 - stranimation) / 2.0D;
		animation = RenderUtil.scrollAnimate(animation, (float) currentPos, .5f);
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		super.render(mouseX, mouseY, delta);
//		Drawable.drawRectWH(x, y, width, height, bgcolor);
		ClickUIMod clickUI = Wrapper.getModule().get("ClickGUI");
		String value = String.valueOf(MathUtil.round(stranimation, 2));
		IFont.WEB_SETTINGS.drawString(setting.getName(), x + 4, y + 1, clickUI.textShadow.isToggle(), -1);
		IFont.WEB_SETTINGS.drawString(value, x + width - 4 - IFont.WEB_SETTINGS.getStringWidth(value), y + 1, clickUI.textShadow.isToggle(), -1);

		Color color = new Color(0xFFE1E1E1);
		RoundedShader.drawRound((float) (x + 4), (float) (y + height - 4), (float) (width - 8), 1, 0.5f,
				new Color(0xff0E0E0E));

		RoundedShader.drawRound((float) (x + 4), (float) (y + height - 4), (float) ((width - 8) * animation), 1, 0.5f,
				color);
		
		RoundedShader.drawRound((float) ((x + 2 + (width - 8) * animation)), (float) (y + height - 5.5f), (float) ((4)), 4, 1.5f,
				color);

		animation = MathUtil.clamp((float) animation, 0, 1);

		if (dragging)
			setValue(mouseX, x + 7, width - 14);
	}

	private void setValue(int mouseX, double x, double width) {
		double diff = setting.getMaxValue() - setting.getMinValue();
		double percentBar = MathHelper.clamp((mouseX - x) / width, 0.0, 1.0);
		double value = setting.getMinValue() + percentBar * diff;

		this.setting.setValue(value);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (button == 0 && hovered) {
			this.dragging = true;
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int button) {
		this.dragging = false;
	}

	@Override
	public void resetAnimation() {
		dragging = false;
		animation = 0f;
		stranimation = 0;
	}

}
