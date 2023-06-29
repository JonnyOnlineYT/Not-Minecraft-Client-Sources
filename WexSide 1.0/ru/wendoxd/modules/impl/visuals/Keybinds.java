package ru.wendoxd.modules.impl.visuals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.menu.EventClickMouse;
import ru.wendoxd.events.impl.player.EventTick;
import ru.wendoxd.events.impl.render.EventBlur;
import ru.wendoxd.events.impl.render.EventRender2D;
import ru.wendoxd.modules.BindMode;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.ColorPicker;
import ru.wendoxd.ui.menu.elements.tabelements.MultiSelectBox;
import ru.wendoxd.ui.menu.elements.tabelements.PrimaryButton;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.fonts.FontRenderer;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.lib.RectHelper;
import ru.wendoxd.utils.visual.RenderUtils;
import ru.wendoxd.utils.visual.animation.DynamicAnimation;
import ru.wendoxd.utils.visual.hud.DraggableElement;
import ru.wendoxd.utils.visual.pallete.PaletteHelper;

public class Keybinds extends Module {
	public static Frame frame = new Frame("Keybinds");
	public static CheckBox enabled = new CheckBox("Keybinds");
	public static MultiSelectBox type = new MultiSelectBox("Type", new String[] { "Keybinds", "ArrayList" },
			() -> enabled.isEnabled(false));
	public static SelectBox colorType = new SelectBox("Color", new String[] { "Static", "Fade", "Astolfo" },
			() -> enabled.isEnabled(true) && type.get(1));
	public static ColorPicker colorPicker = new ColorPicker("TextColor",
			() -> (colorType.get() == 0 || colorType.get() == 1) && enabled.isEnabled(true) && type.get(1));
	public static ColorPicker rectPicker = new ColorPicker("RectColor", () -> enabled.isEnabled(true) && type.get(1));
	public static CheckBox shadow = new CheckBox("Shadow", () -> enabled.isEnabled(true) && type.get(1));
	public static CheckBox blur = new CheckBox("Blur", () -> enabled.isEnabled(true));
	public static DraggableElement keybinds = new DraggableElement();
	public static DynamicAnimation widthDynamic = new DynamicAnimation();
	public static DynamicAnimation heightDynamic = new DynamicAnimation();
	public static float prevTick, tick, width, height;

	@Override
	protected void initSettings() {
		keybinds.setX(5);
		keybinds.setY(61);
		frame.register(enabled, type, colorType, colorPicker, rectPicker, shadow, blur);
		MenuAPI.hud.register(frame);
		colorPicker.getColor().setRGBA(230, 230, 230, 255);
		rectPicker.getColor().setRGBA(20, 20, 20, 80);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventClickMouse) {
			keybinds.mouseClicked();
		}
		if (event instanceof EventTick) {
			prevTick = tick;
			tick = tick + 0.1f;
			for (ru.wendoxd.ui.menu.elements.Tab tab : MenuAPI.tabs) {
				for (Frame frame : tab.getFrames()) {
					for (PrimaryButton button : frame.getButtons()) {
						if (button instanceof CheckBox && ((CheckBox) button).arrayListName() != null) {
							((CheckBox) button).tick();
						}
					}
				}
			}
			widthDynamic.setValue(width);
			heightDynamic.setValue(height);
			widthDynamic.update();
			heightDynamic.update();
			for (ru.wendoxd.ui.menu.elements.Tab tab : MenuAPI.tabs) {
				for (Frame frame : tab.getFrames()) {
					for (PrimaryButton button : frame.getButtons()) {
						if (button instanceof CheckBox && ((CheckBox) button).isVisibleInKeybinds()) {
							((CheckBox) button).updateAnimationEnabled();
						}
					}
				}
			}
		}
		if (event instanceof EventRender2D) {
			if (blur.isEnabled(false)) {
				EntityRenderer.blurEnabled = true;
				return;
			}
			draw();
		}
		if (event instanceof EventBlur && blur.isEnabled(false)) {
			draw();
		}
	}

	public void draw() {
		if (enabled.isEnabled(false)) {
			if (type.get(0)) {
				if (isVisible())
					drawKeybinds(EntityRenderer.res);
			}
			if (type.get(1)) {
				FontRenderer fr = Fonts.MNTSB_13;
				ArrayList<CheckBox> boxes = new ArrayList();
				ScaledResolution res = EntityRenderer.res;
				for (ru.wendoxd.ui.menu.elements.Tab tab : MenuAPI.tabs) {
					for (Frame frame : tab.getFrames()) {
						for (PrimaryButton button : frame.getButtons()) {
							float f;
							if (button instanceof CheckBox && (f = ((CheckBox) button).getAnimation()) > 0) {
								boxes.add((CheckBox) button);
							}
						}
					}
				}
				boxes.sort(Comparator.comparingInt(module -> fr.getStringWidth(module.arrayListName())));
				Collections.reverse(boxes);
				boolean staticColor = colorType.get() == 0;
				boolean fadeColor = colorType.get() == 1;
				boolean astolfoColor = colorType.get() == 2;
				float yOffset = -5;
				int shadowStep = 0;
				float startX = res.getScaledWidth() - 8;
				float startY = 10;
				if (shadow.isEnabled(false)) {
					for (CheckBox box : boxes) {
						GL11.glPushMatrix();
						String name = box.arrayListName();
						float width = Fonts.MNTSB_13.getStringWidth(name);
						GL11.glTranslated((res.getScaledWidth() - startX + width) * (1 - box.getAnimation()), yOffset,
								0);
						int colorText = colorPicker.getColor().build();
						if (astolfoColor) {
							colorText = PaletteHelper.rainbow(((float) shadowStep / (float) boxes.size()) * 0.25f,
									0.5f);
						}
						if (fadeColor) {
							colorText = makeFade(colorText, (float) sin((float) (shadowStep / (float) boxes.size()) * 3
									+ (prevTick + (tick - prevTick) * mc.getRenderPartialTicks())));
						}
						RectHelper.renderShadow(startX - width - 3, startY - 3, width + 13, 15, colorText, 7);
						yOffset += Math.min((8 * box.getAnimation() + 3), 8);
						shadowStep++;
						GL11.glPopMatrix();
					}
				}
				yOffset = -5;
				shadowStep = 0;
				for (CheckBox box : boxes) {
					GL11.glPushMatrix();
					int colorText = colorPicker.getColor().build();
					int colorRect = rectPicker.getColor().build();
					if (astolfoColor) {
						colorText = PaletteHelper.rainbow(((float) shadowStep / (float) boxes.size()) * 0.25f,
								colorPicker.getColor().getAlpha() / 255f);
					}
					if (fadeColor) {
						colorText = makeFade(colorText, (float) sin((float) (shadowStep / (float) boxes.size()) * 3
								+ (prevTick + (tick - prevTick) * mc.getRenderPartialTicks())));
					}
					int rightColor = colorText;
					String name = box.arrayListName();
					float width = Fonts.MNTSB_13.getStringWidth(name);
					GL11.glTranslated((res.getScaledWidth() - startX + width) * (1 - box.getAnimation()), yOffset, 0);
					RenderUtils.drawRect(startX - width - 2, startY - 2, startX + 4, startY + 6, colorRect);
					RenderUtils.drawRect(startX + 4, startY - 2, startX + 6, startY + 6, rightColor);
					Fonts.MNTSB_13.drawString(name, startX - width, startY + 0.5f, colorText);
					yOffset += Math.min((8 * box.getAnimation() + 3), 8);
					GL11.glPopMatrix();
					shadowStep++;
				}
			}
		}
	}

	public static double sin(double input) {
		return Math.abs(Math.sin((input / 2d) * Math.PI));
	}

	public static int makeFade(int color, float progress) {
		int r = color >> 16 & 0xFF;
		int g = (color >> 8 & 0xFF);
		int b = (color & 0xFF);
		int a = (color >> 24 & 0xFF);
		int nr = (int) Math.max(r - 40 * progress, 0);
		int ng = (int) Math.max(g - 40 * progress, 0);
		int nb = (int) Math.max(b - 40 * progress, 0);
		return RenderUtils.rgba(nr, ng, nb, a);
	}

	public static void drawKeybinds(ScaledResolution res) {
		if (!enabled.isEnabled(false))
			return;
		GlStateManager.enableDepth();
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		float width = (float) widthDynamic.getValue();
		float height = (float) heightDynamic.getValue();
		if (mc.currentScreen instanceof GuiChat && mc.player != null) {
			keybinds.tick(res);
		}
		List<CheckBox> boxes = new ArrayList<>();
		for (ru.wendoxd.ui.menu.elements.Tab tab : MenuAPI.tabs) {
			for (Frame frame : tab.getFrames()) {
				for (PrimaryButton button : frame.getButtons()) {
					if (button instanceof CheckBox && ((CheckBox) button).isVisibleInKeybinds()
							&& ((CheckBox) button).getAnimationEnabledKeybind() > 0) {
						boxes.add((CheckBox) button);
					}
				}
			}
		}
		RenderUtils.drawRoundedRect(keybinds.getX(), keybinds.getY(), keybinds.getX() + width, keybinds.getY() + height,
				RenderUtils.rgba(20, 20, 20, 170), 7);
		GL11.glPushMatrix();
		GL11.glTranslated(keybinds.getX(), keybinds.getY(), 1);
		Fonts.ICONS_30.drawString("m", 7, 8.2f, RenderUtils.rgba(220, 220, 220, 255));
		Fonts.MNTSB_13.drawString("Keybinds", 22, 11.2f, RenderUtils.rgba(220, 220, 220, 255));
		float h = 0;
		float resultWidth = 0;
		boxes.sort(Comparator.comparing(CheckBox::getKeyBindName));
		for (CheckBox box : boxes) {
			String bmts = bindModeToString(box.getBindMode());
			float textWidth = Fonts.MNTSB_12.getStringWidth(bmts);
			float totalWidth = textWidth + Fonts.MNTSB_12.getStringWidth(box.getName()) + 28;
			Fonts.MNTSB_12.drawString(box.getName(), 10, 17 + h + box.getAnimationEnabledKeybind() * 8,
					RenderUtils.rgba(220, 220, 220, 220 * box.getAnimationEnabledKeybind()));
			Fonts.MNTSB_12.drawString(bmts, width - textWidth - 10, 17 + h + box.getAnimationEnabledKeybind() * 8,
					RenderUtils.rgba(220, 220, 220, 220 * box.getAnimationEnabledKeybind()));
			h = h + 12 * box.getAnimationEnabledKeybind();
			if (totalWidth > resultWidth)
				resultWidth = totalWidth;
		}
		Keybinds.width = Math.max(62, resultWidth);
		Keybinds.height = h != 0 ? h + 26 : 25;
		keybinds.update((int) widthDynamic.getValue(), (int) heightDynamic.getValue());
		GL11.glPopMatrix();
	}

	public static boolean isVisible() {
		return Minecraft.getMinecraft().currentScreen == null || mc.getMinecraft().currentScreen instanceof GuiChat;
	}

	public static String bindModeToString(BindMode mode) {
		if (mode == mode.ALWAYS) {
			return "[always]";
		}
		if (mode == mode.HOLD) {
			return "[hold]";
		}
		if (mode == mode.TOGGLE) {
			return "[toggle]";
		}
		return "unknown";
	}
}
