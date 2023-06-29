package org.spray.heaven.features.module.modules.display;

import java.awt.Color;
import java.util.Arrays;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.font.FontRenderer;
import org.spray.heaven.font.IFont;
import org.spray.heaven.main.Heaven;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.ui.clickui.Colors;
import org.spray.heaven.util.EntityUtil;
import org.spray.heaven.util.render.ColorUtil;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.shader.drawable.RoundedShader;

@ModuleInfo(name = "Watermark", category = Category.DISPLAY, visible = true, key = Keyboard.KEY_NONE)
public class Watermark extends Module {

	private final Setting mode = register(new Setting("Mode", "Heaven", Arrays.asList("Heaven", "Heaven 2")));

	private static float watermarkHeight = 0;

	@Override
	public void onDisable() {
		watermarkHeight = 0;
	}

	@Override
	public void onRender(int scaleWidth, int scaleHeight, float tickDelta) {

		String username = mc.getSession().getUsername();
		String playerPing = EntityUtil.getPing(mc.player) + " ms";
		String server = mc.getCurrentServerData() == null ? "singleplayer"
				: mc.getCurrentServerData().serverIP.toLowerCase();
		String fps = mc.getDebugFPS() + " fps";

		String[] arrayInfo = new String[] { Heaven.NAME, Heaven.VERSION, username, playerPing, fps, server };

		ArrayListMod list = Wrapper.getModule().get("Arraylist");

		FontRenderer font = IFont.POPPIN_LIST;

		int x = 5;
		int y = 5;

		int height = 12;
		float offsetX = 3;

		float fullWidth = 0;
		for (String info : arrayInfo) {
			fullWidth += font.getStringWidth(info) + 8;
		}

		boolean def = mode.getCurrentMode().equalsIgnoreCase("Heaven");
		Drawable.drawBlurredShadow(x, y - 1, (int) fullWidth - 2, height + 2, 8, new Color(46, 46, 46, 250));
		RoundedShader.drawGradientHorizontal(x - 1, y - 1, fullWidth, height + 2, 4,
				def ? ColorUtil.getListColor(list, 0)
						: ColorUtil.rainbow((int) list.colorSpeed.getValue(), 0, .6f, 1, 1),
				def ? ColorUtil.getListColor(list, 190)
						: ColorUtil.rainbow((int) list.colorSpeed.getValue(), 300, .6f, 1, 1));

		if (def)
			RoundedShader.drawRound(x, y, fullWidth - 2, height, 3, new Color(54, 54, 54, 255));

		for (int i = 0; i < arrayInfo.length; i++) {
			String info = arrayInfo[i];

			font.drawVCenteredStringWithShadow(info, x + offsetX, y, height, -1);

			if (i != arrayInfo.length - 1)
				Drawable.drawRectWH(x + 2 + offsetX + (4 / 2) + font.getStringWidth(info), y + 4, 0.5, height - 8,
						Colors.DIVIDER_COLOR.getRGB());

			offsetX += font.getStringWidth(info) + 8;
		}

		watermarkHeight = y + height + 7;

	}

	public static float getWatermarkHeight() {
		return watermarkHeight;
	}

}
