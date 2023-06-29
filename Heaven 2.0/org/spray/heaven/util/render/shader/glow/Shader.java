package org.spray.heaven.util.render.shader.glow;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Shader {
	public static Shader BOUND;

	private final int id;
	private final Object2IntMap<String> uniformLocations = new Object2IntOpenHashMap<>();

	public Shader(String vertPath, String fragPath) {
		int vert = createShader(read(vertPath), ARBVertexShader.GL_VERTEX_SHADER_ARB);

		int frag = createShader(read(vertPath), ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);

		id = ARBShaderObjects.glCreateProgramObjectARB();

		if (id == 0)
			return;

		ARBShaderObjects.glAttachObjectARB(id, vert);
		ARBShaderObjects.glAttachObjectARB(id, frag);

		ARBShaderObjects.glLinkProgramARB(id);
		ARBShaderObjects.glValidateProgramARB(id);
	}

	private int createShader(String shaderSource, int shaderType) {
		int shader = 0;

		try {
			shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

			if (shader == 0)
				return 0;

			ARBShaderObjects.glShaderSourceARB(shader, shaderSource);
			ARBShaderObjects.glCompileShaderARB(shader);

			if (ARBShaderObjects.glGetObjectParameteriARB(shader,
					ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
				throw new RuntimeException("Error creating shader");

			return shader;
		} catch (final Exception e) {
			ARBShaderObjects.glDeleteObjectARB(shader);
			throw e;

		}
	}

	private String read(String path) {
		try {
			return IOUtils.toString(getClass().getResourceAsStream("/assets/minecraft/infinity/shaders/" + path),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public void bind() {
		GL20.glUseProgram(id);
		BOUND = this;
	}

	private int getLocation(String name) {
		if (uniformLocations.containsKey(name))
			return uniformLocations.getInt(name);

		int location = GL20.glGetUniformLocation(id, name);
		uniformLocations.put(name, location);
		return location;
	}

	public void set(String name, boolean v) {
		GL20.glUniform1i(getLocation(name), v ? 1 : 0);
	}

	public void set(String name, int v) {
		GL20.glUniform1i(getLocation(name), v);
	}

	public void set(String name, double v) {
		GL20.glUniform1f(getLocation(name), (float) v);
	}

	public void set(String name, double v1, double v2) {
		GL20.glUniform2f(getLocation(name), (float) v1, (float) v2);
	}
}