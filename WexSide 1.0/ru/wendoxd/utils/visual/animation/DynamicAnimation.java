package ru.wendoxd.utils.visual.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import ru.wendoxd.WexSide;

public class DynamicAnimation {
	private final double speed;
	private double startValue;
	private double targetValue;
	private double outValue;
	private double step, prevStep;
	private double delta;

	public DynamicAnimation(double speed) {
		this.speed = WexSide.animationSpeed + speed;
	}

	public DynamicAnimation() {
		this(0);
	}

	public void update() {
		this.prevStep = step;
		this.step = MathHelper.clamp(this.step + speed, 0, 1);
		this.outValue = this.startValue + this.delta * WexSide.createAnimation(this.step);
	}

	public double getValue() {
		return this.startValue + this.delta * WexSide.createAnimation(
				this.prevStep + (this.step - this.prevStep) * Minecraft.getMinecraft().getRenderPartialTicks());
	}

	public void setValue(double value) {
		if (value == targetValue)
			return;
		this.targetValue = value;
		this.startValue = this.outValue;
		this.prevStep = 0;
		this.step = 0;
		this.delta = this.targetValue - this.startValue;
	}

	public void forceSetValue(double value) {
		this.targetValue = value;
		this.startValue = value;
		this.outValue = value;
		this.prevStep = 1;
		this.step = 1;
	}
}
