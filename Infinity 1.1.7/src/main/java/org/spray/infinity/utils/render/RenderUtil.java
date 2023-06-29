package org.spray.infinity.utils.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.UUID;

import org.lwjgl.opengl.GL11;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.utils.Helper;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.GameMode;

public class RenderUtil {

	private static final HashMap<String, Identifier> loadedSkins = new HashMap<>();
	public static TextureUtil TEXTURE = new TextureUtil();

	public void setColor(Color color) {
		GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f,
				color.getAlpha() / 255.0f);
	}

	public void setColor(int rgba) {
		int r = rgba & 0xFF;
		int g = rgba >> 8 & 0xFF;
		int b = rgba >> 16 & 0xFF;
		int a = rgba >> 24 & 0xFF;
		GL11.glColor4b((byte) r, (byte) g, (byte) b, (byte) a);
	}

	public int toRGBA(Color c) {
		return c.getRed() | c.getGreen() << 8 | c.getBlue() << 16 | c.getAlpha() << 24;
	}

	public static void bindSkinTexture(UUID UUID, String name) {
		if (loadedSkins.get(name) == null) {
			UUID uuid = PlayerEntity.getUuidFromProfile(new GameProfile(UUID, name));

			PlayerListEntry entry = new PlayerListEntry(new PlayerListS2CPacket.Entry(new GameProfile(uuid, name), 0,
					GameMode.CREATIVE, new LiteralText(name)));

			loadedSkins.put(name, entry.getSkinTexture());
		}

		RenderSystem.setShaderTexture(0, loadedSkins.get(name));
	}

	public static void bindSkinTexture(String name) {
		bindSkinTexture(null, name);
	}

	public static void drawFace(MatrixStack matrixStack, String name, int x, int y, int w, int h, boolean selected) {
		try {
			bindSkinTexture(name);
			GL11.glEnable(GL11.GL_BLEND);

			if (selected)
				RenderSystem.setShaderColor(1, 1, 1, 1);
			else
				RenderSystem.setShaderColor(0.9F, 0.9F, 0.9F, 1);

			int fw = 192;
			int fh = 192;
			float u = 24;
			float v = 24;
			DrawableHelper.drawTexture(matrixStack, x, y, u, v, w, h, fw, fh);

			GL11.glDisable(GL11.GL_BLEND);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void drawTexture(MatrixStack matrices, Identifier ident, double x, double y, double width,
			double height) {
		RenderSystem.setShaderTexture(0, ident);
		RenderSystem.enableBlend();
		DrawableHelper.drawTexture(matrices, (int) x, (int) y, 0, 0, (int) width, (int) height, (int) width,
				(int) height);
		RenderSystem.disableBlend();
	}

	public static void drawTexture(MatrixStack matrices, Identifier ident, double x, double y, double width,
			double height, int color) {
		RenderSystem.setShaderTexture(0, ident);
		RenderSystem.enableBlend();
		drawTexture(matrices, (int) x, (int) y, 0, 0, (int) width, (int) height, (int) width, (int) height, color);
		RenderSystem.disableBlend();
	}

	public static double animate(double value, double target) {
		return value
				+ ((target - value) * (Helper.MC.getLastFrameDuration() / Infinity.TIMER)) / 2;
	}

	public static void drawImage(MatrixStack matrices, boolean local, double x, double y, double width, double height,
			String identifier) {
		drawImage(matrices, local, x, y, width, height, identifier, -1);
	}

	public static void drawImage(MatrixStack matrices, boolean local, double x, double y, double width, double height,
			String identifier, int color) {
		TEXTURE.bindTexture(identifier, local);

		float f = (float) (color >> 24 & 255) / 255.0F;
		float g = (float) (color >> 16 & 255) / 255.0F;
		float h = (float) (color >> 8 & 255) / 255.0F;
		float k = (float) (color & 255) / 255.0F;

		Matrix4f matrix4f = matrices.peek().getModel();
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR_TEXTURE);
		bufferBuilder.vertex(matrix4f, (float) (x + width), (float) y, 0).color(g, h, k, f).texture(1, 0).next();
		bufferBuilder.vertex(matrix4f, (float) x, (float) y, 0).color(g, h, k, f).texture(0, 0).next();
		bufferBuilder.vertex(matrix4f, (float) x, (float) (y + height), 0).color(g, h, k, f).texture(0, 1).next();
		bufferBuilder.vertex(matrix4f, (float) x, (float) (y + height), 0).color(g, h, k, f).texture(0, 1).next();
		bufferBuilder.vertex(matrix4f, (float) (x + width), (float) (y + height), 0).color(g, h, k, f).texture(1, 1)
				.next();
		bufferBuilder.vertex(matrix4f, (float) (x + width), (float) y, 0).color(g, h, k, f).texture(1, 0).next();
		draw();
	}

	public static void draw() {
		RenderSystem.disableDepthTest();
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		RenderSystem.disableCull();
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
		RenderSystem.enableTexture();

		Tessellator.getInstance().draw();

		RenderSystem.enableDepthTest();
		RenderSystem.enableTexture();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}

	public static void drawTexture(MatrixStack matrices, int x, int y, int width, int height, float u, float v,
			int textureWidth, int textureHeight, int color) {
		drawTexture(matrices, x, x + width, y, y + height, 0, width, height, u, v, textureWidth, textureHeight, color);
	}

	public static void drawTexture(MatrixStack matrices, int x0, int y0, int x1, int y1, int z, int regionWidth,
			int regionHeight, float u, float v, int textureWidth, int textureHeight, int color) {
		drawTexturedQuad(matrices.peek().getModel(), x0, y0, x1, y1, z, (u + 0.0F) / (float) textureWidth,
				(u + (float) regionWidth) / (float) textureWidth, (v + 0.0F) / (float) textureHeight,
				(v + (float) regionHeight) / (float) textureHeight, color);
	}

	private static void drawTexturedQuad(Matrix4f matrices, int x0, int x1, int y0, int y1, int z, float u0, float u1,
			float v0, float v1, int color) {
		float f = (float) (color >> 24 & 255) / 255.0F;
		float g = (float) (color >> 16 & 255) / 255.0F;
		float h = (float) (color >> 8 & 255) / 255.0F;
		float k = (float) (color & 255) / 255.0F;

		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
		bufferBuilder.vertex(matrices, (float) x0, (float) y1, (float) z).color(g, h, k, f).texture(u0, v1).next();
		bufferBuilder.vertex(matrices, (float) x1, (float) y1, (float) z).color(g, h, k, f).texture(u1, v1).next();
		bufferBuilder.vertex(matrices, (float) x1, (float) y0, (float) z).color(g, h, k, f).texture(u1, v0).next();
		bufferBuilder.vertex(matrices, (float) x0, (float) y0, (float) z).color(g, h, k, f).texture(u0, v0).next();
		bufferBuilder.end();
		BufferRenderer.draw(bufferBuilder);
	}

	public static void drawItem(ItemStack itemStack, int x, int y, boolean overlay) {
		Helper.MC.getItemRenderer().renderGuiItemIcon(itemStack, x, y);
		if (overlay)
			Helper.MC.getItemRenderer().renderGuiItemOverlay(Helper.MC.textRenderer, itemStack, x, y, null);
	}
}
