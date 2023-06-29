package org.moonware.client.ui.shader;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.*;
import org.moonware.client.helpers.Helper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class Shader implements Helper {

    private int program;

    private Map<String, Integer> uniformsMap;

    public Shader(String fragmentShader) {
        int vertexShaderID, fragmentShaderID;

        try {
            InputStream vertexStream = getClass().getResourceAsStream("/assets/minecraft/moonware/shaders/vertex.vert");
            vertexShaderID = createShader(IOUtils.toString(vertexStream), ARBVertexShader.GL_VERTEX_SHADER_ARB);
            IOUtils.closeQuietly(vertexStream);

            InputStream fragmentStream = getClass().getResourceAsStream("/assets/minecraft/moonware/shaders/fragment/" + fragmentShader);
            fragmentShaderID = createShader(IOUtils.toString(fragmentStream), ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
            IOUtils.closeQuietly(fragmentStream);
        } catch (Exception e) {
            return;
        }

        if (vertexShaderID == 0 || fragmentShaderID == 0)
            return;

        program = ARBShaderObjects.glCreateProgramObjectARB();

        if (program == 0)
            return;

        ARBShaderObjects.glAttachObjectARB(program, vertexShaderID);
        ARBShaderObjects.glAttachObjectARB(program, fragmentShaderID);

        ARBShaderObjects.glLinkProgramARB(program);
        ARBShaderObjects.glValidateProgramARB(program);
    }

    public void startShader() {
        GL11.glPushMatrix();
        GL20.glUseProgram(program);

        if (uniformsMap == null) {
            uniformsMap = new HashMap<>();
            setupUniforms();
        }

        updateUniforms();
    }

    public void stopShader() {
        GL20.glUseProgram(0);
        GL11.glPopMatrix();
    }

    public abstract void setupUniforms();

    public abstract void updateUniforms();

    private int createShader(String shaderSource, int shaderType) {
        int shader = 0;

        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

            if (shader == 0)
                return 0;

            ARBShaderObjects.glShaderSourceARB(shader, shaderSource);
            ARBShaderObjects.glCompileShaderARB(shader);

            return shader;
        } catch (Exception e) {
            ARBShaderObjects.glDeleteObjectARB(shader);
            throw e;

        }
    }

    public void setUniform(String uniformName, int location) {
        uniformsMap.put(uniformName, location);
    }

    public void setupUniform(String uniformName) {
        setUniform(uniformName, GL20.glGetUniformLocation(program, uniformName));
    }

    public int getUniform(String uniformName) {
        return uniformsMap.get(uniformName);
    }
}