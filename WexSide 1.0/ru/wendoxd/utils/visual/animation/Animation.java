package ru.wendoxd.utils.visual.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import ru.wendoxd.WexSide;

public class Animation {
	private final double animationSpeed;
	private double animationState, prevAnimationState;

	public Animation() {
		this(0);
	}

	public Animation(double animationSpeed) {
		this.animationSpeed = WexSide.animationSpeed + animationSpeed;
	}

	public void update(boolean add) {
		prevAnimationState = animationState;
		animationState = MathHelper.clamp(animationState + (add ? animationSpeed : -animationSpeed), 0, 1);
	}

	public void set(double animation) {
		this.animationState = animation;
		this.prevAnimationState = animation;
	}

	public double get() {
		return WexSide.createAnimation(this.prevAnimationState
				+ (this.animationState - this.prevAnimationState) * Minecraft.getMinecraft().getRenderPartialTicks());
	}

	public double getDrop() {
		return WexSide.dropAnimation(this.prevAnimationState
				+ (this.animationState - this.prevAnimationState) * Minecraft.getMinecraft().getRenderPartialTicks());
	}

	public void reset() {
		this.prevAnimationState = 0;
		this.animationState = 0;
	}
}
