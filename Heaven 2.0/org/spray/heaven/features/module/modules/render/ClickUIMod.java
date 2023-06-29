package org.spray.heaven.features.module.modules.render;

import java.awt.Color;
import java.util.Arrays;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.ui.clickui.Colors;
import org.spray.heaven.util.render.ColorUtil;

@ModuleInfo(name = "ClickGUI", category = Category.RENDER, visible = false, key = Keyboard.KEY_RSHIFT)
public class ClickUIMod extends Module {

	private final Setting colorMode = register(new Setting("Color Mode", "Heaven",
			Arrays.asList("Heaven", "Light Rainbow", "Rainbow", "Fade", "Double Color", "Analogous")));
	private final Setting colorSpeed = register(new Setting("Color Speed", 16, 2, 30));
	public final Setting color = register(new Setting("Color", Colors.CLIENT_COLOR)
			.setVisible(() -> colorMode.getCurrentMode().equalsIgnoreCase("Fade")
					|| colorMode.getCurrentMode().equalsIgnoreCase("Double Color")
					|| colorMode.getCurrentMode().equalsIgnoreCase("Analogous")));
	
	private final Setting analogColor = register(new Setting("Analogous Color", Colors.CLIENT_COLOR)
			.setVisible(() -> colorMode.getCurrentMode().equalsIgnoreCase("Analogous")));

	public final Setting textShadow = register(new Setting("Text Shadow", false));
	public final Setting background = register(new Setting("Background", true));
	public final Setting backgroundAlpha = register(
			new Setting("Background Transparent", 0.85f, 0.01f, 0.99f).setVisible(background::isToggle));
	public final Setting coloredBackground = register(
			new Setting("Colored Background", true).setVisible(background::isToggle));

	@Override
	public void onEnable() {
		mc.displayGuiScreen(Wrapper.MENU);
		disable();
	}

	public static Color getColor(int count) {
		ClickUIMod clickUIMod = Wrapper.getModule().get("ClickGUI");

		int index = (int) (count);
		switch (clickUIMod.colorMode.getCurrentMode()) {
		case "Heaven":
			return ColorUtil.skyRainbow((int) clickUIMod.colorSpeed.getValue(), index);

		case "Light Rainbow":
			return ColorUtil.rainbow((int) clickUIMod.colorSpeed.getValue(), index, .6f, 1, 1);

		case "Rainbow":
			return ColorUtil.rainbow((int) clickUIMod.colorSpeed.getValue(), index, 1f, 1, 1);

		case "Fade":
			return ColorUtil.fade((int) clickUIMod.colorSpeed.getValue(), index, clickUIMod.color.getColor(), 1);

		case "Double Color":
			return ColorUtil.interpolateColorsBackAndForth((int) clickUIMod.colorSpeed.getValue(), index,
					clickUIMod.color.getColor(), Colors.ALTERNATE_COLOR, true);

		case "Analogous":
			int val = 1;
			Color analogous = ColorUtil.getAnalogousColor(clickUIMod.analogColor.getColor())[val];
			return ColorUtil.interpolateColorsBackAndForth((int) clickUIMod.colorSpeed.getValue(), index,
					clickUIMod.color.getColor(), analogous, true);
		default:
			return clickUIMod.color.getColor();
		}
	}

}
