package org.spray.heaven.ui.menu.views;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.spray.heaven.font.IFont;
import org.spray.heaven.ui.avx.view.View;
import org.spray.heaven.util.render.ColorUtil;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.shader.drawable.RoundedShader;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;

public class MColorPicker extends View {

	private int hue;
	private int saturation = 100;
	private int value = 0;
	private int rgb = 0xff0000;
	private int alpha = 255;

	private final String title;

	private boolean dragging, draggingV;

	int padding = 5;
	float radius = 10;

	public MColorPicker(Color current, String title) {
		int color = current.getRGB();
		rgb = color & 0x00ffffff;
		alpha = (color & 0xff000000) >>> 24;

		int[] hsv = ColorUtil.rgbToHsv(rgb);
		hue = hsv[0] == -1 ? 0 : hsv[0];
		saturation = hsv[1];
		value = hsv[2];
		this.title = title;
	}

	@Override
	protected void renderView(int mouseX, int mouseY, float delta) {
		int shadowMargin = 1;
		radius = height / 2;

		Drawable.drawBlurredShadow(x + shadowMargin, y + shadowMargin, width - shadowMargin, height - shadowMargin, 10,
				ColorUtil.applyOpacity(context.getTheme().getShadowColor(), 0.8f));
		RoundedShader.drawRound(x, y, width, height, 5, context.getTheme().getBackground());

		IFont.WEB_SETTINGS.drawVCenteredString(title, x + 5, y, height / 3, context.getTheme().getTextColor().getRGB());

		Color color = new Color(rgb);
		Drawable.drawBlurredShadow(x + 5, y + height / 1.6f, width / 3f + 1, height / 4f + 1, 5, ColorUtil.applyOpacity(context.getTheme().getShadowColor(), 0.7f));
		RoundedShader.drawRound(x + 5, y + height / 1.6f, width / 3f, height / 4f, 2, color);
		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
		String hex = String.format("#%06x", 0xFFFFFF & Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
		
		IFont.WEB_SETTINGS.drawVCenteredString(hex, x + 5, y + height / 3f - 5, height / 3f, context.getTheme().getTextColor().getRGB());

		renderPicker(x + width - 10 - radius, y + radius, radius - padding);

		RoundedShader.drawGradientVertical(x + width - 10, y + padding, 3, height - padding * 2, 1.5f, Color.BLACK,
				color);

		float cy = y + padding - 1;
		float ch = height - padding - 8;
		float vpos = (float) (cy + (ch - 3 + ((ch - 3) * value)));
		Drawable.drawBlurredShadow(x + width - 10 - 1f, y + 4 - 0.5f + (value * ch / 100), 6, 7, 4,
				ColorUtil.applyOpacity(context.getTheme().getShadowColor(), 0.8f));
		RoundedShader.drawRound(x + width - 10 - 1f, y + 4 + (value * ch / 100), 5, 5, 2f, Color.WHITE);

		float x = this.x + width - (10 + radius);
		float y = this.y + radius;
		float dx = mouseX - x;
		float dy = mouseY - y;
		if (dragging) {
			updateWheel((int) dx, (int) dy);
		}

		x = this.x + width - (10f + 1);
		y = this.y;
		dx = mouseX - x;
		dy = mouseY - y;
		if (draggingV) {
			updateValue((int) dx, (int) dy);
		}
	}

	@Override
	protected boolean viewClicked(int mouseX, int mouseY, int button) {
		float x = this.x + width - (10 + radius);
		float y = this.y + radius;
		float dx = mouseX - x;
		float dy = mouseY - y;
		if (dx * dx + dy * dy <= radius * radius) {
			updateWheel((int) dx, (int) dy);
			dragging = true;
			return true;
		}

		x = this.x + width - (10f + 1);
		y = this.y;
		if (Drawable.isHovered(mouseX, mouseY, x, y, 4, height - padding)) {
			updateValue((int) dx, (int) dy);
			draggingV = true;
			return true;
		}

		return false;
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int button) {
		dragging = false;
		draggingV = false;
	}

	private void renderPicker(float x, float y, float radius) {
		GlStateManager.disableLighting();
		GlStateManager.disableFog();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

		GlStateManager.disableTexture2D();
		GlStateManager.shadeModel(GL11.GL_SMOOTH);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		int col1;
		int col2;
		// Color wheel
		for (float f = 0; f < 360; f += 0.25) {
			bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
			float fRads = (float) Math.toRadians(f);
			col1 = ColorUtil.hsvToRgb((int) f, 100, 100);
			bufferbuilder.pos(x, y, 0).color(1f, 1f, 1f, 1f).endVertex();
			bufferbuilder.pos(x + Math.cos(fRads) * radius, y + Math.sin(fRads) * radius, 0)
					.color((col1 & 0xff0000) >> 16, (col1 & 0x00ff00) >> 8, col1 & 0x0000ff, 255).endVertex();
			tessellator.draw();
		}

		int dist = saturation / 2;
		Drawable.drawBlurredShadow(
				(float) (this.x + width - (16 + radius) + (Math.cos(Math.toRadians(hue)) * dist - 2.5f)),
				this.y + radius + 4.5f + (float) ((Math.sin(Math.toRadians(hue)) * dist) - 2.5f), 6, 6, 6,
				ColorUtil.applyOpacity(context.getTheme().getShadowColor(), 0.8f));
		RoundedShader.drawRound(
				(float) (this.x + width - (16 + radius) + (Math.cos(Math.toRadians(hue)) * dist - 2.5f)),
				this.y + radius + 5 + (float) ((Math.sin(Math.toRadians(hue)) * dist) - 2.5f), 5, 5, 2f, Color.WHITE);
	}

	private void updateWheel(int mx, int my) {
		hue = (int) Math.toDegrees(Math.atan2(mx, my));
		if (hue < 0)
			hue += 360;

		int dist = (int) Math.sqrt(mx * mx + my * my);
		if (dist > radius - 4)
			dist = (int) radius - 4;
		saturation = dist * 2;

		rgb = ColorUtil.hsvToRgb(hue, saturation, 100 - value);
	}

	private void updateValue(int mx, int my) {
		float ch = height;
		value = (int) MathHelper.clamp(my * 100 / ch, 0, 100);

		rgb = ColorUtil.hsvToRgb(hue, saturation, 100 - value);
	}

}
