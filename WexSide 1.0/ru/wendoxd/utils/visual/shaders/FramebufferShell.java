package ru.wendoxd.utils.visual.shaders;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.modules.Module;
import ru.wendoxd.utils.visual.RenderUtils;

public class FramebufferShell {
	private static Framebuffer blurFramebuffer, alphaFramebuffer, scrollFramebuffer;
	public static boolean needBlur;

	public static void setupScrollFramebuffer() {
		Minecraft mc = Minecraft.getMinecraft();
		if (scrollFramebuffer == null)
			scrollFramebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, false);
		if (mc.displayWidth != scrollFramebuffer.framebufferWidth
				|| mc.displayHeight != scrollFramebuffer.framebufferHeight)
			scrollFramebuffer.createBindFramebuffer(mc.displayWidth, mc.displayHeight);
		scrollFramebuffer.bindFramebuffer(false);
	}

	public static void renderScrollFramebuffer(int x, int y) {
		Minecraft mc = Minecraft.getMinecraft();
		alphaFramebuffer.bindFramebuffer(false);
		ShaderShell.SCROLL_SHADER.attach();
		GL11.glColor4f(1f, 1f, 1f, 1f);
		ShaderShell.SCROLL_SHADER.set1I("texture", 0);
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, alphaFramebuffer.framebufferTexture);
		ShaderShell.SCROLL_SHADER.set1I("textureBG", 2);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		ShaderShell.SCROLL_SHADER.set1I("maxY", x * 2);
		ShaderShell.SCROLL_SHADER.set1I("minY", y * 2);
		ShaderShell.SCROLL_SHADER.set2F("resolution", mc.displayWidth, mc.displayHeight);
		scrollFramebuffer.framebufferRender(mc.displayWidth, mc.displayHeight);
		ShaderShell.SCROLL_SHADER.detach();
		scrollFramebuffer.framebufferClear();
		alphaFramebuffer.bindFramebuffer(false);
		GL11.glTranslated(MenuAPI.menuWindow.getX() * 2, MenuAPI.menuWindow.getY() * 2, 0);
		GL11.glScaled(2, 2, 0);
	}

	public static void setupAlphaGuiFramebuffer() {
		Minecraft mc = Minecraft.getMinecraft();
		if (alphaFramebuffer == null)
			alphaFramebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, false);
		if (mc.displayWidth != alphaFramebuffer.framebufferWidth
				|| mc.displayHeight != alphaFramebuffer.framebufferHeight)
			alphaFramebuffer.createBindFramebuffer(mc.displayWidth, mc.displayHeight);
		alphaFramebuffer.bindFramebuffer(false);
	}

	public static void renderAlphaFramebuffer(float alpha) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.getFramebuffer().bindFramebuffer(false);
		ShaderShell.MENU_SHADER.attach();
		GL11.glColor4f(1f, 1f, 1f, 1f);
		ShaderShell.MENU_SHADER.set1I("texture", 0);
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.getFramebuffer().framebufferTexture);
		ShaderShell.MENU_SHADER.set1I("textureBG", 2);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		float a = Module.visuals_menu_background.getColor().getAlpha() / 255f;
		ShaderShell.MENU_SHADER.set3F("color", Module.visuals_menu_background.getColor().getRed() / 255.f * a * alpha,
				Module.visuals_menu_background.getColor().getGreen() / 255.f * a * alpha,
				Module.visuals_menu_background.getColor().getBlue() / 255.f * a * alpha);
		ShaderShell.MENU_SHADER.set2F("oneTexel", 1F / mc.displayWidth, 1F / mc.displayHeight);
		ShaderShell.MENU_SHADER.set1F("alpha", alpha);
		alphaFramebuffer.framebufferRender(mc.displayWidth, mc.displayHeight);
		ShaderShell.MENU_SHADER.detach();
		alphaFramebuffer.framebufferClear();
		mc.getFramebuffer().bindFramebuffer(false);
		mc.entityRenderer.setupOverlayRendering();
	}

	public static void blur(int startX, int startY, int endX, int endY) {
		if (ShaderShell.BLUR_SHADER == null)
			return;
		if (!Display.isActive()) {
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
		if (blurFramebuffer == null)
			blurFramebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, false);
		if (mc.displayWidth != blurFramebuffer.framebufferWidth
				|| mc.displayHeight != blurFramebuffer.framebufferHeight)
			blurFramebuffer.createBindFramebuffer(mc.displayWidth, mc.displayHeight);
		blurFramebuffer.bindFramebuffer(false);
		RenderUtils.drawRect(startX, startY, endX, endY, RenderUtils.rgba(0, 0, 0, 255));
		mc.getFramebuffer().bindFramebuffer(false);
		ShaderShell.BLUR_SHADER.attach();
		GL11.glColor4f(1f, 1f, 1f, 1f);
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Minecraft.getMinecraft().getFramebuffer().framebufferTexture);
		ShaderShell.BLUR_SHADER.setupUniforms((float) MenuAPI.transparencies.get() * 5 + 1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		blurFramebuffer.framebufferRenderExt(mc.displayWidth, mc.displayHeight, false);
		ShaderShell.BLUR_SHADER.detach();
		blurFramebuffer.framebufferClear();
		mc.getFramebuffer().bindFramebuffer(false);
		mc.entityRenderer.setupOverlayRendering(2);
	}

	public static void blur(Runnable runnable) {
		if (!Display.isActive()) {
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
		if (blurFramebuffer == null)
			blurFramebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, false);
		if (mc.displayWidth != blurFramebuffer.framebufferWidth
				|| mc.displayHeight != blurFramebuffer.framebufferHeight)
			blurFramebuffer.createBindFramebuffer(mc.displayWidth, mc.displayHeight);
		blurFramebuffer.bindFramebuffer(false);
		runnable.run();
		mc.getFramebuffer().bindFramebuffer(false);
		ShaderShell.BLUR_SHADER.attach();
		GL11.glColor4f(1f, 1f, 1f, 1f);
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Minecraft.getMinecraft().getFramebuffer().framebufferTexture);
		ShaderShell.BLUR_SHADER.setupUniforms(5);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		blurFramebuffer.framebufferRenderExt(mc.displayWidth, mc.displayHeight, false);
		ShaderShell.BLUR_SHADER.detach();
		blurFramebuffer.framebufferClear();
		GL11.glEnable(GL11.GL_BLEND);
		mc.getFramebuffer().bindFramebuffer(false);
		mc.entityRenderer.setupOverlayRendering(2);
		runnable.run();
	}
}
