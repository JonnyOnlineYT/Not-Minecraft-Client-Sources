package org.moonware.client.ui.shader;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.moonware.client.helpers.Utils.ShaderUtil;
import org.moonware.client.helpers.Utils.render.RenderUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class BackgroundShader {
    private final int programId;
    private final int timeUniform;
    private final int mouseUniform;
    private final int resolutionUniform;

    public BackgroundShader(String fragmentShaderLocation) throws IOException {
        int program = GL20.glCreateProgram();
        GL20.glAttachShader(program, createShader(BackgroundShader.class.getResourceAsStream("/assets/passthrough.vsh"), 35633));
        GL20.glAttachShader(program, createShader(BackgroundShader.class.getResourceAsStream(fragmentShaderLocation), 35632));
        GL20.glLinkProgram(program);
        int linked = GL20.glGetProgrami(program, 35714);
        if (linked == 0) {
            System.err.println(GL20.glGetProgramInfoLog(program, GL20.glGetProgrami(program, 35716)));
            throw new IllegalStateException("Shader failed to link");
        }
        programId = program;
        GL20.glUseProgram(program);
        timeUniform = GL20.glGetUniformLocation(program, "time");
        mouseUniform = GL20.glGetUniformLocation(program, "mouse");
        resolutionUniform = GL20.glGetUniformLocation(program, "resolution");
        GL20.glUseProgram(0);
    }

    public void useShader(int width, int height, float mouseX, float mouseY, float time) {
        GL20.glUseProgram(programId);
        RenderUtil.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL20.glUniform2f(resolutionUniform, (float)width, (float)height);
        GL20.glUniform2f(mouseUniform, mouseX / (float)width, 1.0f - mouseY / (float)height);
        GL20.glUniform1f(timeUniform, time);
        ShaderUtil.drawQuads(mouseX, mouseY, width, height);
        GlStateManager.disableBlend();
    }

    public int createShader(InputStream inputStream, int shaderType) throws IOException {
        int shader = GL20.glCreateShader(shaderType);
        GL20.glShaderSource(shader, readStreamToString(inputStream));
        GL20.glCompileShader(shader);
        int compiled = GL20.glGetShaderi(shader, 35713);
        if (compiled == 0) {
            System.err.println(GL20.glGetShaderInfoLog(shader, GL20.glGetShaderi(shader, 35716)));
            throw new IllegalStateException("Failed to compile shader");
        }
        return shader;
    }

    public String readStreamToString(InputStream inputStream) throws IOException {
        int read;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[512];
        while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, read);
        }
        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }
}
