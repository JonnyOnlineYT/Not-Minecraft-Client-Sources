package ru.wendoxd.modules.impl.visuals;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.menu.EventClickMouse;
import ru.wendoxd.events.impl.player.EventTick;
import ru.wendoxd.events.impl.render.EventBlur;
import ru.wendoxd.events.impl.render.EventRender2D;
import ru.wendoxd.modules.Module;
import ru.wendoxd.modules.impl.movement.LongJump;
import ru.wendoxd.modules.impl.movement.Timer;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.MultiSelectBox;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.lib.RectHelper;
import ru.wendoxd.utils.visual.ColorShell;
import ru.wendoxd.utils.visual.RenderUtils;
import ru.wendoxd.utils.visual.animation.AstolfoAnimation;
import ru.wendoxd.utils.visual.animation.DynamicAnimation;
import ru.wendoxd.utils.visual.hud.DraggableElement;

public class Indicators extends Module {
	private static List<Indicator> indicators = new ArrayList();
	public static DraggableElement panel = new DraggableElement();
	public static Frame frame = new Frame("Indicators");
	public static CheckBox checkBox = new CheckBox("Indicators").markColorPicker();
	public static SelectBox colorType = new SelectBox("Color Type", new String[] { "Static", "State Based", "Astolfo" },
			() -> checkBox.isEnabled(true));
	public static MultiSelectBox visible = new MultiSelectBox("Visible", new String[] { "Timer", "Memory", "DMGFly" },
			() -> checkBox.isEnabled(true));
	public static CheckBox blur = new CheckBox("Blur", () -> checkBox.isEnabled(true));
	public static AstolfoAnimation astolfo = new AstolfoAnimation();

	public Indicators() {
		panel.update(200, 100);
	}

	@Override
	protected void initSettings() {
		panel.setX(6);
		panel.setY(86);
		MenuAPI.hud.register(frame);
		checkBox.getColor().setRGB(148, 147, 255);
		frame.register(checkBox, colorType, visible, blur);
		indicators.add(new Indicator() {

			@Override
			boolean enabled() {
				return Indicators.visible.get(0);
			}

			@Override
			String getName() {
				return "Timer";
			}

			@Override
			double getProgress() {
				return (10 - Timer.value) / (Math.abs(Timer.getMin()) + 10);
			}
		});
		indicators.add(new Indicator() {

			@Override
			boolean enabled() {
				return Indicators.visible.get(1);
			}

			@Override
			String getName() {
				return "Memory";
			}

			@Override
			double getProgress() {
				long total = Runtime.getRuntime().totalMemory();
				long free = Runtime.getRuntime().freeMemory();
				long delta = total - free;
				return (delta / (double) Runtime.getRuntime().maxMemory());
			}
		});
		indicators.add(new Indicator() {

			@Override
			boolean enabled() {
				return Indicators.visible.get(2);
			}

			@Override
			String getName() {
				return "DMG Fly";
			}

			@Override
			double getProgress() {
				return LongJump.getProgress();
			}
		});
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRender2D && checkBox.isEnabled(false)) {
			if (blur.isEnabled(false)) {
				EntityRenderer.blurEnabled = true;
				return;
			}
			draw();
		}
		if (event instanceof EventBlur && checkBox.isEnabled(false) && blur.isEnabled(false)) {
			draw();
		}
		if (event instanceof EventTick) {
			astolfo.update();
			indicators.forEach(indicator -> indicator.update());
		}
		if (event instanceof EventClickMouse) {
			panel.mouseClicked();
		}
	}

	public void draw() {
		GL11.glPushMatrix();
		GL11.glTranslated(panel.getX(), panel.getY(), 0);
		ScaledResolution res = EntityRenderer.res;
		panel.tick(res);
		List<Indicator> enabledIndicators = new ArrayList();
		for (Indicator indicator : indicators) {
			if (indicator.enabled())
				enabledIndicators.add(indicator);
		}
		int enabledCount = enabledIndicators.size();
		if (enabledCount > 0) {
			for (int i = 0; i < enabledCount; i++) {
				GL11.glPushMatrix();
				GL11.glTranslated(46f * i, 0, 0);
				Indicator ind = enabledIndicators.get(i);
				RectHelper.renderShadow(0, 0, 40, 40, RenderUtils.rgba(25, 25, 25, 180), 3);
				GL11.glTranslated(22, 26, 0);
				drawCircle(ind.getName(), ind.progress());
				GL11.glPopMatrix();
			}
		}
		GL11.glPopMatrix();
		panel.update(46 * Math.max(enabledCount, 1) - 2, (int) (enabledCount == 0 ? 0 : 44));
	}

	public void drawCircle(String name, double offset) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		boolean oldState = GL11.glIsEnabled(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glLineWidth(5.5f);
		GL11.glColor4f(0.1f, 0.1f, 0.1f, 0.5f);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		for (int i = 0; i < 360; i++) {
			double x = Math.cos(Math.toRadians(i)) * 11;
			double z = Math.sin(Math.toRadians(i)) * 11;
			GL11.glVertex2d(x, z);
		}
		GL11.glEnd();
		GL11.glBegin(GL11.GL_LINE_STRIP);
		for (int i = -90; i < -90 + (360 * offset); i++) {
			ColorShell colorShell = checkBox.getColor();
			float red = colorShell.getRed();
			float green = colorShell.getGreen();
			float blue = colorShell.getBlue();
			if (colorType.get() == 1) {
				float[] buffer = getRG(offset);
				red = buffer[0];
				green = buffer[1];
				blue = buffer[2];
			} else if (colorType.get() == 2) {
				double stage = (i + 90) / 360.;
				int clr = astolfo.getColor(stage);
				red = ((clr >> 16) & 255);
				green = ((clr >> 8) & 255);
				blue = ((clr & 255));
			}
			GL11.glColor4f(red / 255f, green / 255f, blue / 255f, 1);
			double x = Math.cos(Math.toRadians(i)) * 11;
			double z = Math.sin(Math.toRadians(i)) * 11;
			GL11.glVertex2d(x, z);
		}
		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		if (!oldState)
			GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glColor4f(1, 1, 1, 1);
		Fonts.MNTSB_12.drawCenteredString((int) (offset * 100) + "%", 0.3f, -0.75f,
				RenderUtils.rgba(200, 200, 200, 255));
		Fonts.MNTSB_12.drawCenteredString(name, 0, -19f, RenderUtils.rgba(200, 200, 200, 255));
	}

	public static float[] getRG(double input) {
		return new float[] { 255 - 255 * (float) input, 255 * (float) input, 100 * (float) input };
	}

	public static abstract class Indicator {
		DynamicAnimation animation = new DynamicAnimation();

		void update() {
			this.animation.setValue(Math.max(getProgress(), 0));
			this.animation.update();
		}

		double progress() {
			return this.animation.getValue();
		}

		abstract boolean enabled();

		abstract String getName();

		abstract double getProgress();
	}
}
