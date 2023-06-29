package org.spray.heaven.features.module.modules.misc;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.font.FontRenderer;
import org.spray.heaven.font.IFont;

@ModuleInfo(name = "BPS", category = Category.MISC, visible = true, key = Keyboard.KEY_NONE)
public class BPS extends Module {

	private double calculateBPS() {
		double bps = (Math.hypot(mc.player.posX - mc.player.prevPosX, mc.player.posZ - mc.player.prevPosZ)
				* mc.getTimer().getSpeed()) * 20;
		return Math.round(bps * 100.0) / 100.0;
	}

	@Override
	public void onRender(int scaleWidth, int scaleHeight, float tickDelta) {
		FontRenderer font = IFont.POPPIN_LIST;
		font.drawVCenteredString("BPS: " + calculateBPS(), 860, 455, 15, new Color(227, 15, 236, 255).getRGB(), 1.5F);

	}

}
