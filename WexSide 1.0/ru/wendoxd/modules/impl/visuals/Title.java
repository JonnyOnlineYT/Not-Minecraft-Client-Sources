package ru.wendoxd.modules.impl.visuals;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import ru.wendoxd.WexSide;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.render.EventBlur;
import ru.wendoxd.events.impl.render.EventRender2D;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.MultiSelectBox;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.lib.RectHelper;
import ru.wendoxd.utils.visual.RenderUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class Title extends Module {

	public static CheckBox title = new CheckBox("Title");
	public static MultiSelectBox selected = new MultiSelectBox("Selected",
			new String[] { "Name", "Server", "Time", "FPS", "MS" }, () -> title.isEnabled(true));
	public static CheckBox blur = new CheckBox("Blur", () -> title.isEnabled(true));
	private static final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private static float maxWidth;

	@Override
	protected void initSettings() {
		title.markSetting("Title");
		blur.modifyPath("awoidkawiodw");
		visuals_hud.register(title, selected, blur);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRender2D && title.isEnabled(false)) {
			if (blur.isEnabled(false)) {
				EntityRenderer.blurEnabled = true;
				return;
			}
			drawWaterMark();
		}
		if (event instanceof EventBlur && title.isEnabled(false) && blur.isEnabled(false)) {
			drawWaterMark();
		}
	}

	public static void drawWaterMark() {
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		RectHelper.renderShadow(6, 5, 51 + maxWidth, 16, RenderUtils.rgba(20, 20, 20, 170), 4);
		Fonts.ICONS_25.drawString("a", 10.7f, 11F, RenderUtils.rgba(148, 255, 255, 255));
		Fonts.ICONS_25.drawString("a", 11, 11F, RenderUtils.rgba(255, 255, 255, 255));
		Fonts.MNTSB_14.drawString("wexside.xyz", 25, 12F, RenderUtils.rgba(225, 225, 225, 255));
		List<String> strings = new ArrayList<>();
		if (selected.get(0)) {
			strings.add(WexSide.getProfile().getName());
		}
		if (selected.get(1)) {
			strings.add(
					mc.getCurrentServerData() == null ? "localhost" : mc.getCurrentServerData().serverIP.toLowerCase());
		}
		if (selected.get(2)) {
			strings.add(dateFormat.format(new Date()));
		}
		if (selected.get(3)) {
			strings.add(Minecraft.getDebugFPS() + " fps");
		}
		if (mc.getCurrentServerData() != null && selected.get(4)) {
			strings.add(mc.getCurrentServerData().pingToServer + "ms");
		}
		float width = 16;
		for (String s : strings) {
			RenderUtils.drawRect(60 + width - 3, 10, 60 + width - 2.5F, 17.5F, RenderUtils.rgba(155, 155, 155, 35));
			Fonts.MNTSB_13.drawString(s, 61 + width, 12.5F, RenderUtils.rgba(225, 225, 225, 255));
			width += Fonts.MNTSB_13.getStringWidth(s) + 8;
		}
		maxWidth = width;
	}
}