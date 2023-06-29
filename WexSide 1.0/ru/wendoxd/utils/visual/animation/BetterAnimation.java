package ru.wendoxd.utils.visual.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import ru.wendoxd.WexSide;

public class BetterAnimation {
	private int prevTick, tick, maxTick;

	public BetterAnimation(int maxTick) {
		this.maxTick = maxTick;
	}

	public BetterAnimation() {
		this(10);
	}

	public void update(boolean update) {
		prevTick = tick;
		tick = MathHelper.clamp(tick + (update ? 1 : -1), 0, maxTick);
	}

	public float getAnimationf() {
		return (float) WexSide.dropAnimation(
				(this.prevTick + (this.tick - this.prevTick) * Minecraft.getMinecraft().getRenderPartialTicks())
						/ maxTick);
	}

	public double getAnimationd() {
		return WexSide.dropAnimation(
				(this.prevTick + (this.tick - this.prevTick) * Minecraft.getMinecraft().getRenderPartialTicks())
						/ maxTick);
	}
}
