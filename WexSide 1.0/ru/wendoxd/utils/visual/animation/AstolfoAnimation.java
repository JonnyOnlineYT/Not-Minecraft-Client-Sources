package ru.wendoxd.utils.visual.animation;

import net.minecraft.client.Minecraft;
import ru.wendoxd.utils.visual.pallete.PaletteHelper;

public class AstolfoAnimation {
	private double value, prevValue;

	public void update() {
		this.prevValue = this.value;
		this.value += 0.01;
	}

	public int getColor(double offset) {
		double hue = ((this.prevValue
				+ (this.value - this.prevValue) * Minecraft.getMinecraft().getRenderPartialTicks()) + offset) % 1.;
		if (hue > 0.5F) {
			hue = 0.5F - (hue - 0.5F);
		}
		hue += 0.5F;
		return PaletteHelper.HSBtoRGB((float) hue, 0.5f, 1);
	}
}
