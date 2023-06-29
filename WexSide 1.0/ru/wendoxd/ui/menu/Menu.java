package ru.wendoxd.ui.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.wendoxd.WexSide;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Tab;
import ru.wendoxd.ui.menu.impl.MenuWindow;
import ru.wendoxd.ui.menu.impl.windows.ColorWindow;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.visual.RenderUtils;
import ru.wendoxd.utils.visual.shaders.FramebufferShell;
import ru.wendoxd.utils.visual.shaders.ShaderShell;

public class Menu extends MenuAPI {
	static boolean reset;

	public static void drawScreen() {
		double alpha = transparencies.get();
		if (alpha == 0) {
			return;
		}
		if (Module.visuals_menu_blur.isEnabled(false)) {
			FramebufferShell.blur(0, 0, width, height);
		}
		for (MenuWindow hwnd : windows) {
			if (hwnd instanceof ColorWindow) {
				ColorWindow cwnd = (ColorWindow) hwnd;
				if (cwnd.getAnimation() == 1) {
					RenderUtils.drawRect(cwnd.getX() + 5, cwnd.getY() + 5, cwnd.getX() + cwnd.width() - 5,
							cwnd.getY() + cwnd.height() - 5, RenderUtils.rgba(23, 23, 23, 255));
				}
			}
		}
		windows.forEach(w -> {
			if (w.isActive() && w.isDragging()) {
				if (!Mouse.isButtonDown(0)) {
					w.setDragging(false);
				}
				w.setXY(MathHelper.clamp(mouseX - w.getDragX(), 0, width),
						MathHelper.clamp(mouseY - w.getDragY(), 0, height));
			}
			w.setXY(MathHelper.clamp(w.getX(), 0, width - w.width()),
					MathHelper.clamp(w.getY(), 0, height - w.height()));
		});
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		RenderUtils.drawRect(0, 0, width, height, RenderUtils.rgba(5, 5, 5, alpha * 200));
		if (alpha > 0.5) {
			setupMenuOffset();
			FramebufferShell.blur(0, 0, 340, 199);
		}
		setupMenuOffset();
		FramebufferShell.setupAlphaGuiFramebuffer();
		RenderUtils.drawRect(0, 0, 100, 199, RenderUtils.rgba(10, 10, 10, 240));
		RenderUtils.drawRect(100, 0, 340, 199, RenderUtils.rgba(20, 20, 20, 255));
		RenderUtils.drawRect(5, 32, 95, 32.3f, RenderUtils.rgba(104, 104, 104, 188));
		RenderUtils.drawRect(5, 170, 95, 170.3f, RenderUtils.rgba(104, 104, 104, 188));
		GL11.glPushMatrix();
		GL11.glTranslated(-menuWindow.getX(), -menuWindow.getY(), 0);
		Fonts.MNTSB_14.drawString("UID: " + WexSide.getProfile().getUID(), 0, height - 5.5f,
				RenderUtils.rgba(188, 188, 188, 255));
		GL11.glPopMatrix();
		Fonts.MNTSB_21.drawString("WEXSIDE", 29f, 8.3f, RenderUtils.rgba(148, 255, 255, 255));
		Fonts.MNTSB_21.drawString("WEXSIDE", 29f, 8f, RenderUtils.rgba(255, 255, 255, 255));
		Fonts.MNTSB_14.drawString(WexSide.VERSION_TYPE, 30.5F, 20f, RenderUtils.rgba(198, 198, 198, 255));
		Fonts.ICONS_40.drawString("A", 8.3f, 10, RenderUtils.rgba(148, 148, 148, 255));
		Fonts.ICONS_40.drawString("A", 8, 10.3f, RenderUtils.rgba(148, 255, 255, 255));
		Fonts.ICONS_40.drawString("A", 8, 10, -1);
		int offsetY = 0;
		for (Tab tab : tabs) {
			try {
				tab.draw(offsetY * 25);
				offsetY++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Fonts.MNTSB_16.drawSubstring(WexSide.getProfile().getName(), 31, 178, RenderUtils.rgba(228, 228, 228, 255), 46);
		Fonts.MNTSB_14.drawString("Till:", 31, 188, RenderUtils.rgba(148, 148, 148, 255));
		Fonts.MNTSB_14.drawString(WexSide.getProfile().getExpirationDate(), 45, 188, RenderUtils.rgba(208, 208, 208, 255));
		ShaderShell.CIRCLE_TEXTURE_SHADER.attach();
		ShaderShell.CIRCLE_TEXTURE_SHADER.set1F("radius", 0.6F);
		ShaderShell.CIRCLE_TEXTURE_SHADER.set1F("glow", 0.05F);
		try {
			drawTexturedRect(WexSide.getProfile().getResourceLocation(), 5, 171.5f, 256, 256, 0.1);
		} catch (Exception e) {
			WexSide.getProfile().resourceLocation = new ResourceLocation("wexside/wtf.png");
		}
		ShaderShell.CIRCLE_TEXTURE_SHADER.detach();
		windows.forEach(w -> {
			GL11.glPushMatrix();
			GL11.glTranslated(-menuWindow.getX(), -menuWindow.getY(), 0);
			GL11.glTranslated(w.getX(), w.getY(), 0);
			w.draw();
			GL11.glPopMatrix();
		});
		FramebufferShell.renderAlphaFramebuffer((float) alpha);
	}

	public static void mouseClicked(int x, int y, int mb) {
		int offsetY = 0;
		boolean clicked = false;
		for (Tab tab : tabs) {
			if (tab.mouseClicked(x, y, mb, offsetY * 25)) {
				clicked = true;
			}
			offsetY++;
		}
		if (!clicked) {
			for (MenuWindow window : windows) {
				if (window.click(x, y, mb))
					return;
				if (window.isActive() && window.getBound().inBound()) {
					window.setDragXY(x - window.getX(), y - window.getY());
					window.setDragging(true);
					break;
				}
			}
		}
	}

	public static void animation() {
		int offsetY = 0;
		try {
			double animation = transparencies.get();
			boolean opened = Minecraft.getMinecraft().currentScreen instanceof MenuShell;
			if (reset && animation != 0 && opened) {
				tabs.forEach(Tab::reset);
				reset = false;
			}
			transparencies.update(opened);
			if (animation > 0 && !(opened)) {
				reset = true;
			}
			for (Tab tab : tabs) {
				tab.animation(offsetY * 25);
				offsetY++;
			}
			windows.forEach(MenuWindow::onAnimation);
			try {
			} catch (Exception e) {

			}
		} catch (Exception e) {
		}
	}

	public static void keyTyped(char c, int keyCode) {
		if (MenuAPI.contextTab != null)
			MenuAPI.contextTab.keyTyped(c, keyCode);
	}
}
