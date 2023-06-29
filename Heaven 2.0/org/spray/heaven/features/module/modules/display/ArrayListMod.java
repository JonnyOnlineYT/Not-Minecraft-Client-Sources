package org.spray.heaven.features.module.modules.display;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.font.FontRenderer;
import org.spray.heaven.font.IFont;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.ui.clickui.Colors;
import org.spray.heaven.util.render.ColorUtil;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.animation.Direction;

@ModuleInfo(name = "Arraylist", category = Category.DISPLAY, visible = true, key = Keyboard.KEY_NONE)
public class ArrayListMod extends Module {

	private final Setting position = register(
			new Setting("Position", "Top Right", Arrays.asList("Top Right", "Top Left")));

	private final Setting fontMode = register(
			new Setting("Font", "Comfortaa", Arrays.asList("Montserrat", "Comfortaa", "Poppin", "MC")));
	private final Setting fontShadow = register(new Setting("Text Shadow", false));
	private final Setting scale = register(
			new Setting("Font Scale", 1f, 0.8f, 5).setVisible(() -> !fontMode.getCurrentMode().equalsIgnoreCase("MC")));
	public final Setting colorMode = register(new Setting("Color Mode", "Heaven",
			Arrays.asList("Heaven", "Light Rainbow", "Rainbow", "Fade", "Double Color", "Analogous")));
	private final Setting ignoreRenders = register(new Setting("Important", false));
	private final Setting smallLetters = register(new Setting("Small Letters", true));
	private final Setting listOffset = register(new Setting("List Offset", 0, 0, 5));

	public final Setting color = register(new Setting("Color", Colors.CLIENT_COLOR)
			.setVisible(() -> colorMode.getCurrentMode().equalsIgnoreCase("Fade")
					|| colorMode.getCurrentMode().equalsIgnoreCase("Double Color")
					|| colorMode.getCurrentMode().equalsIgnoreCase("Analogous")));
	public final Setting analogColor = register(new Setting("Analogous Color", Color.WHITE)
			.setVisible(() -> colorMode.getCurrentMode().equalsIgnoreCase("Analogous")));

	final Setting colorIndex = register(new Setting("Color Separation", 12, 5, 100));
	public final Setting colorSpeed = register(new Setting("Color Speed", 4.5, 2, 30));

	private final Setting outline = register(new Setting("Outline", false));
	private final Setting rightLine = register(new Setting("Right line", false));
	private final Setting glowing = register(new Setting("Glowing", false));
	private final Setting background = register(new Setting("Background", true));
	private final Setting backgroundAlpha = register(
			new Setting("Background Alpha", 0.82, 0.01, 1).setVisible(background::isToggle));

	private FontRenderer font;

	private List<Module> modules;

	private void getModules() {
		if (modules == null) {
			List<Module> moduleList = Wrapper.getModule().getModules().values().stream().collect(Collectors.toList());
			moduleList.removeIf(module -> !module.isVisible());
			modules = moduleList;
		}
	}

	@Override
	public void onRender(int scaledWidth, int scaledHeight, float tickDelta) {
		int offsetY = !Wrapper.getModule().get("PotionEffects").isEnabled() && !mc.player.getActivePotionEffects().isEmpty() ? 28 : 1;

		getModules();
		getFont();
		if (position.getCurrentMode().contains("Bottom")) {
			modules.sort((a, b) -> Float.compare(
					getStringWidth(
							smallLetters.isToggle() ? a.getRenderName(false).toLowerCase() : a.getRenderName(false)),
					getStringWidth(
							smallLetters.isToggle() ? b.getRenderName(false).toLowerCase() : b.getRenderName(false))));
		} else {
			modules.sort((a, b) -> Float.compare(
					getStringWidth(
							smallLetters.isToggle() ? b.getRenderName(false).toLowerCase() : b.getRenderName(false)),
					getStringWidth(
							smallLetters.isToggle() ? a.getRenderName(false).toLowerCase() : a.getRenderName(false))));
		}

		List<Module> enabledModules = modules.stream().filter(m -> m.isEnabled() && m.isVisible()).filter(m -> !ignoreRenders.isToggle() || !m.getCategory().equals(Category.RENDER))
				.collect(Collectors.toList());

		float end_margin = rightLine.isToggle() ? 2 : 1;
		float right_width = scaledWidth - end_margin;

		switch (position.getCurrentMode()) {
		case "Top Left":
			right_width = 0;
			offsetY = (int) Watermark.getWatermarkHeight();
			break;
		case "Bottom Left":
			right_width = 2;
			offsetY = scaledHeight;
			break;
		case "Bottom Right":
			offsetY = scaledHeight;
			break;
		}

		GL11.glColor4f(1f, 1f, 1f, 1f);

		int count = 0;
		for (Module m : modules) {
			if (ignoreRenders.isToggle() && m.getCategory().equals(Category.RENDER))
				continue;

			boolean enabled = m.isEnabled();

			m.animation.setDirection(enabled ? Direction.FORWARDS : Direction.BACKWARDS);
			if (!enabled && m.animation.finished(Direction.BACKWARDS))
				continue;

			Color textColor;
			int index = (int) (count * colorIndex.getValue());
			switch (colorMode.getCurrentMode()) {
			case "Heaven":
				textColor = ColorUtil.skyRainbow((int) colorSpeed.getValue(), index);
				break;
			case "Light Rainbow":
				textColor = ColorUtil.rainbow((int) colorSpeed.getValue(), index, .6f, 1, 1);
				break;
			case "Rainbow":
				textColor = ColorUtil.rainbow((int) colorSpeed.getValue(), index, 1f, 1, 1);
				break;
			case "Fade":
				textColor = ColorUtil.fade((int) colorSpeed.getValue(), index, color.getColor(), 1);
				break;
			case "Double Color":
				textColor = ColorUtil.interpolateColorsBackAndForth((int) colorSpeed.getValue(), index,
						color.getColor(), Colors.ALTERNATE_COLOR, true);
				break;
			case "Analogous":
				int val = 1;
				Color analogous = ColorUtil.getAnalogousColor(analogColor.getColor())[val];
				textColor = ColorUtil.interpolateColorsBackAndForth((int) colorSpeed.getValue(), index,
						color.getColor(), analogous, true);
				break;
			default:
				textColor = new Color(-1);
				break;
			}

			String name = smallLetters.isToggle() ? m.getRenderName(false).toLowerCase() : m.getRenderName(false);

			float end = getStringWidth(name) + 2;
			int offset = (int) (getFontHeight());
			float x = right_width - end;

			int i3 = position.getCurrentMode().contains("Left") ? 0 : 1;
			x += Math.abs((m.animation.getOutput() - i3) * (right_width - end));

			float cx = x;
			float cy = offsetY;

			int margin = position.getCurrentMode().contains("Left") ? 4 : 0;

			if (background.isToggle())
				Drawable.drawRectWH(cx - (position.getCurrentMode().contains("Left") ? 1 : 3), cy, end + 7, offset,
						ColorUtil.applyOpacity(ColorUtil.darker(textColor, 0.37f), (float) backgroundAlpha.getValue())
								.getRGB());

			if (glowing.isToggle())
				Drawable.drawBlurredShadow((int) cx - 1, (int) cy, (int) end + 2, offset, 4,
						ColorUtil.applyOpacity(textColor, 0.2f));

			if (outline.isToggle() && m.animation.finished(Direction.FORWARDS)) {
				float rx = (float) (position.getCurrentMode().contains("Left") ? cx + (end + 5.5f) : cx - 3.5f);
				String nextName = smallLetters.isToggle()
						? getNextModule(m, enabledModules).getRenderName(false).toLowerCase()
						: getNextModule(m, enabledModules).getRenderName(false);

				if (position.getCurrentMode().contains("Left")) {
					Drawable.drawRectWH(rx, cy, 0.5, offset, textColor.getRGB());
					float calc = (float) ((end - 0.5) - (getStringWidth(nextName) + 1));
					Drawable.drawRectWH(rx + 0.5 - calc, cy + offset, calc, 0.5, textColor.getRGB());

					if (enabledModules.indexOf(m) == 0)
						Drawable.drawRectWH(cx, (int) cy - 0.5, rx, 0.5, textColor.getRGB());

					if (enabledModules.indexOf(m) == enabledModules.size() - 1)
						Drawable.drawRectWH(cx, (int) cy + offset, rx, 0.5, textColor.getRGB());
				} else {
					Drawable.drawRectWH(rx, cy, 0.5, offset, textColor.getRGB());
					Drawable.drawRectWH(rx, cy + offset, (end - 0.5) - (getStringWidth(nextName) + 1), 0.5,
							textColor.getRGB());

					if (enabledModules.indexOf(m) == 0)
						Drawable.drawRectWH(rx, (int) cy - 0.5, end + 5, 0.5, textColor.getRGB());

					if (enabledModules.indexOf(m) == enabledModules.size() - 1)
						Drawable.drawRectWH(rx, (int) cy + offset, end + 5, 0.5, textColor.getRGB());
				}
			}

			if (rightLine.isToggle()) {
				int padding = outline.isToggle() ? 1 : 0;
				Drawable.drawRectWH(position.getCurrentMode().contains("Left") ? 0 : scaledWidth - 1,
						(int) cy - padding + 0.5, 1, offset + (padding * 2) - 1, textColor.getRGB());
			}

			if (fontMode.getCurrentMode().equalsIgnoreCase("MC")) {
				GL11.glDisable(GL11.GL_CULL_FACE);
				if (fontShadow.isToggle())
					mc.fontRendererObj.drawStringWithShadow(name, cx + margin,
							cy + (offset >> 1) - (getFontHeight() / 2) + 1, textColor.getRGB());
				else
					mc.fontRendererObj.drawString(name, cx + margin, cy + (offset >> 1) - (getFontHeight() / 2) + 1,
							textColor.getRGB(), false);
			} else {
				if (fontShadow.isToggle())
					font.drawVCenteredStringWithShadow(name, cx + margin, cy, offset, textColor.getRGB(),
							(float) scale.getValue());
				else
					font.drawVCenteredString(name, cx + margin, cy, offset, textColor.getRGB(),
							(float) scale.getValue());
			}

			if (position.getCurrentMode().contains("Bottom"))
				offsetY -= (int) ((float) (m.animation.getOutput() * offset));
			else
				offsetY += (int) ((float) (m.animation.getOutput() * offset));
			count++;
		}
	}

	private float getStringWidth(String str) {
		return fontMode.getCurrentMode().equalsIgnoreCase("MC") ? mc.fontRendererObj.getStringWidth(str)
				: font.getStringWidth(str, (float) scale.getValue());
	}

	private float getFontHeight() {
		return (float) (fontMode.getCurrentMode().equalsIgnoreCase("MC")
				? mc.fontRendererObj.FONT_HEIGHT + 1 + listOffset.getValue()
				: font.getFontHeight((float) scale.getValue()) + listOffset.getValue());
	}

	private void getFont() {
		switch (fontMode.getCurrentMode()) {
		case "Montserrat":
			font = IFont.WEB_LIST;
			break;
		case "Comfortaa":
			font = IFont.COMFORTAA_LIST;
			break;
		case "Poppin":
			font = IFont.POPPIN_LIST;
			break;
		}
	}

	@Override
	public void onEnable() {
		super.onEnable();

	}

	private Module getNextModule(Module current, List<Module> moduleList) {
		int index = moduleList.indexOf(current) + 1;
		if (index > moduleList.size() - 1)
			return moduleList.get(moduleList.size() - 1);

		return moduleList.get(index);
	}

}
