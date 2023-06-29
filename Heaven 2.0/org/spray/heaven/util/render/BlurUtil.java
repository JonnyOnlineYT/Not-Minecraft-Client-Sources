package org.spray.heaven.util.render;

import com.google.gson.JsonSyntaxException;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

import org.spray.heaven.main.Wrapper;

public class BlurUtil {

	private static ShaderGroup blurShader;
	private static Framebuffer buffer;
	private static int lastScale = 0;
	private static int lastScaleWidth = 0;
	private static int lastScaleHeight = 0;

	static {
		try {
			blurShader = new ShaderGroup(Wrapper.MC.getTextureManager(), Wrapper.MC.getResourceManager(),
					Wrapper.MC.getFramebuffer(), new ResourceLocation("shaders/post/blurarea.json"));
		} catch (JsonSyntaxException | IOException e) {
			System.out.println("EXCEPTIONNNNN: " + e);
			e.printStackTrace();
		}
	}

	private static void reinitShader() {
		blurShader.createBindFramebuffers(Wrapper.MC.displayWidth, Wrapper.MC.displayHeight);
		buffer = new Framebuffer(Wrapper.MC.displayWidth, Wrapper.MC.displayHeight, true);
		buffer.setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);
	}

	public static void draw(int x, int y, int width, int height, int radius) {
		ScaledResolution scale = new ScaledResolution(Wrapper.MC);
		int factor = scale.getScaleFactor();
		int factor2 = scale.getScaledWidth();
		int factor3 = scale.getScaledHeight();
		if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3) {
			reinitShader();
		}
		lastScale = factor;
		lastScaleWidth = factor2;
		lastScaleHeight = factor3;
		blurShader.listShaders.get(0).getShaderManager().getShaderUniform("BlurXY").set(x, factor3 - y - height);
		blurShader.listShaders.get(1).getShaderManager().getShaderUniform("BlurXY").set(x, factor3 - y - height);
		blurShader.listShaders.get(0).getShaderManager().getShaderUniform("BlurCoord").set(width, height);
		blurShader.listShaders.get(1).getShaderManager().getShaderUniform("BlurCoord").set(width, height);
		blurShader.listShaders.get(0).getShaderManager().getShaderUniform("Radius").set(radius);
		blurShader.listShaders.get(1).getShaderManager().getShaderUniform("Radius").set(radius);
		blurShader.loadShaderGroup(Wrapper.MC.getRenderPartialTicks());
		Wrapper.MC.getFramebuffer().bindFramebuffer(true);
	}

}
