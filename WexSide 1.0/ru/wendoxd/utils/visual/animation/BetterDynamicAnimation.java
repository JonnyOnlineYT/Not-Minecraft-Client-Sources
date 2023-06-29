package ru.wendoxd.utils.visual.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import ru.wendoxd.WexSide;

public class BetterDynamicAnimation {
	private int maxTicks;
	private double value, dstValue;
	private int prevStep, step;

	public BetterDynamicAnimation(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	public BetterDynamicAnimation() {
		this(5);
	}

	public void update() {
		prevStep = step;
		step = MathHelper.clamp(step + 1, 0, maxTicks);
	}

	public void setValue(double value) {
		if (value != this.dstValue) {
			this.prevStep = 0;
			this.step = 0;
			this.value = dstValue;
			this.dstValue = value;
		}
	}

	public double getAnimationD() {
		float pt = Minecraft.getMinecraft().getRenderPartialTicks();
		double delta = dstValue - value;
		double animation = WexSide.createAnimation((prevStep + (step - prevStep) * pt) / (double) maxTicks);
		return value + delta * animation;
	}
}
