package ru.wendoxd.modules.impl.visuals;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventTick;
import ru.wendoxd.events.impl.render.EventBlur;
import ru.wendoxd.events.impl.render.EventRender2D;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.MultiSelectBox;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.lib.RectHelper;
import ru.wendoxd.utils.visual.RenderUtils;
import ru.wendoxd.utils.visual.animation.Animation;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class Notifications extends Module {
	public static CheckBox notifications = new CheckBox("Notifications").markArrayList("Notifications");
	public static MultiSelectBox selected = new MultiSelectBox("Selected", new String[] { "Modules", "Other" },
			() -> notifications.isEnabled(true));
	public static CheckBox blur = new CheckBox("Blur", () -> notifications.isEnabled(true));
	private static List<Notify> notifies = new ArrayList();

	@Override
	protected void initSettings() {
		selected.modifyPath("NOTIFICATIONSLUHIXF");
		visuals_hud.register(notifications, selected, blur);
	}

	public static void notify(String title, String text, int type) {
		if (!notifications.isEnabled(false)) {
			return;
		}
		if (!selected.get(type)) {
			return;
		}
		notifies.add(new Notify(title, text));
	}

	public static void notify(String title, String text) {
		notifies.add(new Notify(title, text).setMaxTime(200));
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRender2D && notifications.isEnabled(false)) {
			if (blur.isEnabled(false)) {
				EntityRenderer.blurEnabled = true;
				return;
			}
			render(EntityRenderer.res);
		}
		if (event instanceof EventBlur && notifications.isEnabled(false) && blur.isEnabled(false)) {
			render(EntityRenderer.res);
		}
		if (event instanceof EventTick) {
			notifies.forEach(notify -> notify.updateAnimation());
			notifies.removeIf(Notify::updateAnimation);
		}
	}

	public void render(ScaledResolution res) {
		float yOffset = -24;
		if (mc.currentScreen instanceof GuiChat) {
			int i = mc.gameSettings.guiScale;
			if (i == 0) {
				yOffset -= 26;
			}
			if (i == 2) {
				yOffset -= 14;
			}
		}
		for (Notify notify : notifies) {
			GL11.glPushMatrix();
			yOffset -= notify.draw(res, yOffset);
			GL11.glPopMatrix();
		}
	}

	public static class Notify {
		private Animation animation = new Animation();
		private String title, text;
		private float width;
		private int ticks, maxTime = 50;

		public Notify(String title, String text) {
			this.title = title;
			this.text = text;
			this.width = Math.max(Fonts.MNTSB_12.getStringWidth(title), Fonts.MNTSB_12.getStringWidth(text));
		}

		public float draw(ScaledResolution res, float yOffset) {
			int w = res.getScaledWidth(), h = res.getScaledHeight();
			double anim = animation.get();
			GL11.glTranslated(w - (width + 29) * anim, h + yOffset, 0);
			RenderUtils.sizeAnimation(-width * anim, 24 * anim, anim);
			RectHelper.renderShadow(-2, -2f, width + 27, 21f, RenderUtils.rgba(20, 20, 20, 195 * anim), 4);
			Fonts.ICONS_30.drawString("A", 3, 5.5f, RenderUtils.rgba(170, 170, 170, 255 * anim));
			Fonts.MNTSB_12.drawString(this.title, 20, 5f, RenderUtils.rgba(200, 200, 200, 255 * anim));
			Fonts.MNTSB_12.drawString(this.text, 20, 12f, RenderUtils.rgba(170, 170, 170, 255 * anim));
			return 24 * (float) animation.get();
		}

		public boolean updateAnimation() {
			this.animation.update(++this.ticks < maxTime);
			return this.animation.get() == 0;
		}

		public Notify setMaxTime(int maxTime) {
			this.maxTime = maxTime;
			return this;
		}
	}
}