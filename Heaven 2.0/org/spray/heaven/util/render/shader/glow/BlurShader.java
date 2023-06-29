package org.spray.heaven.util.render.shader.glow;

import org.lwjgl.opengl.GL20;
import org.spray.heaven.main.Wrapper;

public class BlurShader extends FramebufferShader {

	public static final BlurShader BLUR_SHADER = new BlurShader();

	public BlurShader() {
		super("blur.vert", "blur.frag");
	}

	@Override
	public void setupUniforms() {
		setupUniform("u_Size");
		setupUniform("u_Texture");
		// setupUniform("color");
		// setupUniform("divider");
		setupUniform("u_Radius");
		setupUniform("u_Direction");
		setupUniform("u_Direction");
		// setupUniform("maxSample");
	}

	@Override
	public void updateUniforms() {
		GL20.glUniform2f(getUniform("u_Size"), Wrapper.RESOLUTION.getScaledWidth(),
				Wrapper.RESOLUTION.getScaledHeight());
		GL20.glUniform1i(getUniform("u_Texture"), 0);
		// GL20.glUniform3f(getUniform("color"), red, green, blue);
		// GL20.glUniform1f(getUniform("divider"), 140F);
		GL20.glUniform1f(getUniform("u_Radius"), (float) Math.floor(radius));
		GL20.glUniform2f(getUniform("u_Direction"), 1.0f, 0.0f);
		GL20.glUniform2f(getUniform("u_Direction"), 0.0f, 1.0f);
		// GL20.glUniform1f(getUniform("maxSample"), 10F);
	}
}