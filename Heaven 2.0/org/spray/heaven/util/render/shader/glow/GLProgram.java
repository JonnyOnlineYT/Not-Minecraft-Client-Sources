package org.spray.heaven.util.render.shader.glow;

import static org.lwjgl.opengl.GL20.glUseProgram;

public class GLProgram {

	int mProgram;

	public GLProgram() {
	}

	/**
	 * Use this shader program
	 */
	public void use() {
		glUseProgram(mProgram);
	}

	public final int get() {
		return mProgram;
	}
}