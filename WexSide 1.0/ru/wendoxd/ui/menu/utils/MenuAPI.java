package ru.wendoxd.ui.menu.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import ru.wendoxd.WexSide;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.Menu;
import ru.wendoxd.ui.menu.elements.Tab;
import ru.wendoxd.ui.menu.impl.MenuWindow;
import ru.wendoxd.utils.visual.RenderUtils;
import ru.wendoxd.utils.visual.animation.Animation;

import java.util.ArrayList;
import java.util.List;

public class MenuAPI extends MenuWindow {
	public static List<MenuWindow> windows = new ArrayList<>();
	public static Animation transparencies = new Animation();
	public static ArrayList<Tab> tabs = new ArrayList<>();

	public static Tab combat, visuals, movement, player, hud, contextTab;
	public static int mouseX, mouseY, width, height;
	public static boolean initialized;
	public static MenuWindow menuWindow;
	public static Bound2D bound;


	public static void postInit() {
		windows.clear();
		tabs.clear();
		combat = new Tab("E", "Combat");
		visuals = new Tab("F", "Visuals");
		hud = new Tab("G", "HUD");
		movement = new Tab("D", "Movement");
		player = new Tab("B", "Player");
		tabs.add(combat);
		tabs.add(visuals);
		tabs.add(hud);
		tabs.add(movement);
		tabs.add(player);
		transparencies.reset();
		contextTab = combat;
		WexSide.getModules().clear();
		Module.registerModules();
		MenuAPI.bound = new Bound2D(0, 0, 340, 199);
		menuWindow = new MenuAPI();
		windows.add(menuWindow);
		menuWindow.setXY(Menu.width / 2 - (int) ((float) menuWindow.getBound().getMaxX() / 2),
				Menu.height / 2 - (int) ((float) menuWindow.getBound().getMaxY() / 2));
	}

	public static void drawTexturedModalRect(ResourceLocation location, double x, double y, int textureX, int textureY,
			double width, double height) {
		boolean alpha_test = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		Minecraft.getMinecraft().getTextureManager().bindTexture(location);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x, y + height, 0)
				.tex((float) (textureX) * 0.00390625F, (float) (textureY + height) * 0.00390625F).endVertex();
		bufferbuilder.pos(x + width, y + height, 0)
				.tex((float) (textureX + width) * 0.00390625F, (float) (textureY + height) * 0.00390625F).endVertex();
		bufferbuilder.pos(x + width, y, 0)
				.tex((float) (textureX + width) * 0.00390625F, (float) (textureY) * 0.00390625F).endVertex();
		bufferbuilder.pos(x, y, 0).tex((float) (textureX) * 0.00390625F, (float) (textureY) * 0.00390625F).endVertex();
		tessellator.draw();
		if (alpha_test) {
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		} else {
			GL11.glDisable(GL11.GL_ALPHA_TEST);
		}
	}

	public static void drawTexturedRect(ResourceLocation location, double xStart, double yStart, double width,
			double height, double scale) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glScaled(scale, scale, scale);
		drawTexturedModalRect(location, xStart / scale, yStart / scale, 0, 0, width, height);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public static void drawCheckBoxCircleMenu(float x, float y, int color, int color2, float value, MenuWindow mw) {
		GL11.glPushMatrix();
		GL11.glTranslated(-mw.getX(), -mw.getY(), 0);
		x += mw.getX();
		y += mw.getY();
		RenderUtils.drawCircle(x + 10.5f, y + 1.5f, 4.5f, 0.5f, 0.15f, color2);
		RenderUtils.drawCircle(x + 3.5f, y + 1.5f, 4.5f, 0.5f, 0.15f, color2);
		GL11.glPopMatrix();
		x -= mw.getX();
		y -= mw.getY();
		RenderUtils.drawRect(x + 5, y + 2f, x + 12.5f, y + 5.5f, color2);
		GL11.glPushMatrix();
		GL11.glTranslated(-mw.getX(), -mw.getY(), 0);
		x += mw.getX();
		y += mw.getY();
		RenderUtils.drawCircle(x + 2.5f + 7.5f * value * 0.8f, y, 7, 0.5f, 0.15f, color);
		GL11.glPopMatrix();
	}

	public static void drawSlider(float x, float y, int color, int color2, float value) {
		GL11.glPushMatrix();
		GL11.glTranslated(-menuWindow.getX(), -menuWindow.getY(), 0);
		x += menuWindow.getX();
		y += menuWindow.getY();
		RenderUtils.drawCircle(x + 45f, y + 2f, 2f, 0.4f, 0.15f, color2);
		RenderUtils.drawCircle(x + 9f, y + 2f, 2f, 0.4f, 0.15f, color2);
		GL11.glPopMatrix();
		x -= menuWindow.getX();
		y -= menuWindow.getY();
		RenderUtils.drawRect(x + 10, y + 2f, x + 46, y + 4f, color2);
		GL11.glPushMatrix();
		GL11.glTranslated(-menuWindow.getX(), -menuWindow.getY(), 0);
		x += menuWindow.getX();
		y += menuWindow.getY();
		RenderUtils.drawCircle(x + 7 + (36 * value), y - 1f, 8, 0.5f, 0.15f, color);
		GL11.glPopMatrix();
	}

	public static void setupMenuOffset() {
		GL11.glTranslated(menuWindow.getX(), menuWindow.getY(), 0);
	}

	public static void loading() {
		WexSide.initialization();
		postInit();
	}

	public static boolean inBound(int x, int y, int width, int height) {
		if (menuWindow == null)
			return false;
		return Menu.mouseX - menuWindow.getX() > x && Menu.mouseX - menuWindow.getX() < width
				&& Menu.mouseY - menuWindow.getY() > y && Menu.mouseY - menuWindow.getY() < height;
	}

	@Override
	public void draw() {
	}

	@Override
	public boolean click(int x, int y, int mb) {
		return false;
	}

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public Bound2D getBound() {
		return MenuAPI.bound;
	}

	@Override
	public void onAnimation() {
	}

	@Override
	public int width() {
		return 340;
	}

	@Override
	public int height() {
		return 199;
	}

	@Override
	public void update(boolean open) {
	}
}