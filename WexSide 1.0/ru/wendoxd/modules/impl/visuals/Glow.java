package ru.wendoxd.modules.impl.visuals;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;
import ru.wendoxd.WexSide;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.render.EventRender2D;
import ru.wendoxd.events.impl.render.EventRenderPlayer;
import ru.wendoxd.modules.Module;
import ru.wendoxd.utils.visual.ColorShell;
import ru.wendoxd.utils.visual.shaders.ShaderShell;

public class Glow extends Module {
	private static Framebuffer buffer;
	public static boolean ignoreGlint;

	public void onEvent(Event event) {
		if (event instanceof EventRender2D && visuals_entitiestab.get(0).glow.isEnabled()
				&& mc.player.getHealth() > 0) {
			setupFramebuffer();
			ScaledResolution res = ((EventRender2D) event).getScaledResolution();
			ShaderShell.CHAMS_SHADER.attach();
			ColorShell cs = visuals_entitiestab.get(0).glow.getColor();
			setRGB(cs.getRed(), cs.getGreen(), cs.getBlue());
			glDrawFramebuffer(buffer.framebufferTexture, res.getScaledWidth(), res.getScaledHeight());
			ShaderShell.CHAMS_SHADER.detach();
			buffer.framebufferClear();
			mc.getFramebuffer().bindFramebuffer(false);
		}
		if (event instanceof EventRenderPlayer) {
			if (((EventRenderPlayer) event).getPlayer() != mc.player
					&& !WexSide.friendManager.isFriend(((EventRenderPlayer) event).getPlayer().getName())
					&& visuals_entitiestab.get(0).glow.isEnabled() && mc.player.getHealth() > 0) {
				setupFramebuffer();
				buffer.bindFramebuffer(false);
				ignoreGlint = true;
				((EventRenderPlayer) event).draw();
				ignoreGlint = false;
				mc.getFramebuffer().bindFramebuffer(false);
			}
		}
	}

	public static void glDrawFramebuffer(final int framebufferTexture, final int width, final int height) {
		int current = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
		glBindTexture(GL_TEXTURE_2D, framebufferTexture);
		glDisable(GL_ALPHA_TEST);
		boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_BLEND);
		// Make sure blend is enabled
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, 1);
			glVertex2f(0, 0);

			glTexCoord2f(0, 0);
			glVertex2f(0, height);

			glTexCoord2f(1, 0);
			glVertex2f(width, height);

			glTexCoord2f(1, 1);
			glVertex2f(width, 0);
		}
		glEnd();
		if (blend) {
			GL11.glEnable(GL11.GL_BLEND);
		} else {
			GL11.glDisable(GL11.GL_BLEND);
		}
		glEnable(GL_ALPHA_TEST);
		glBindTexture(GL_TEXTURE_2D, current);
	}

	public static void setupFramebuffer() {
		if (buffer == null)
			buffer = new Framebuffer(mc.displayWidth, mc.displayHeight, false);
		if (mc.displayWidth != buffer.framebufferWidth || mc.displayHeight != buffer.framebufferHeight)
			buffer.createBindFramebuffer(mc.displayWidth, mc.displayHeight);
		if (ShaderShell.CHAMS_SHADER == null) {
			ShaderShell.CHAMS_SHADER = new ShaderShell("#version 130\r\n" + "uniform sampler2D t0;\r\n"
					+ "uniform float glow_size = 2;\r\n" + "uniform vec3 glow_colour = vec3(1, 1, 1);\r\n"
					+ "uniform float glow_intensity = 1;\r\n" + "uniform float glow_threshold = 0;\r\n" + "\r\n"
					+ "void main() {\r\n" + "    vec2 tex_coord = gl_TexCoord[0].xy;\r\n"
					+ "    vec4 pixel = texture(t0, tex_coord);\r\n" + "    if (pixel.a <= glow_threshold) {\r\n"
					+ "        ivec2 size = textureSize(t0, 0);\r\n" + "	\r\n"
					+ "        float uv_x = tex_coord.x * size.x;\r\n"
					+ "        float uv_y = tex_coord.y * size.y;\r\n" + "\r\n" + "        float sum = 0.0;\r\n"
					+ "        for (int n = 0; n < 9; ++n) {\r\n"
					+ "            uv_y = (tex_coord.y * size.y) + (glow_size * float(n - 4.5));\r\n"
					+ "            float h_sum = 0.0;\r\n"
					+ "            h_sum += texelFetch(t0, ivec2(uv_x - (4.0 * glow_size), uv_y), 0).a;\r\n"
					+ "            h_sum += texelFetch(t0, ivec2(uv_x - (3.0 * glow_size), uv_y), 0).a;\r\n"
					+ "            h_sum += texelFetch(t0, ivec2(uv_x - (2.0 * glow_size), uv_y), 0).a;\r\n"
					+ "            h_sum += texelFetch(t0, ivec2(uv_x - glow_size, uv_y), 0).a;\r\n"
					+ "            h_sum += texelFetch(t0, ivec2(uv_x, uv_y), 0).a;\r\n"
					+ "            h_sum += texelFetch(t0, ivec2(uv_x + glow_size, uv_y), 0).a;\r\n"
					+ "            h_sum += texelFetch(t0, ivec2(uv_x + (2.0 * glow_size), uv_y), 0).a;\r\n"
					+ "            h_sum += texelFetch(t0, ivec2(uv_x + (3.0 * glow_size), uv_y), 0).a;\r\n"
					+ "            h_sum += texelFetch(t0, ivec2(uv_x + (4.0 * glow_size), uv_y), 0).a;\r\n"
					+ "            sum += h_sum / 9.0;\r\n" + "        }\r\n" + "\r\n"
					+ "        gl_FragColor = vec4(glow_colour, (sum / 9.0) * glow_intensity);\r\n" + "    }\r\n"
					+ "}");
		}
	}

	public static void setRGB(int r, int g, int b) {
		ShaderShell.CHAMS_SHADER.set3F("glow_colour", r / 255f, g / 255f, b / 255f);
	}
}
